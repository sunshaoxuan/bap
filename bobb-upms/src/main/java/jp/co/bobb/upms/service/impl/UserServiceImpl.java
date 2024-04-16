package jp.co.bobb.upms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.RandomUtil;
import jp.co.bobb.upms.dto.PageDTO;
import jp.co.bobb.upms.dto.UserInfoDTO;
import jp.co.bobb.upms.entity.MemberInfo;
import jp.co.bobb.upms.entity.RcRole;
import jp.co.bobb.upms.entity.RcUser;
import jp.co.bobb.upms.mapper.MemberInfoMapper;
import jp.co.bobb.upms.mapper.RcRoleMapper;
import jp.co.bobb.upms.mapper.RcUserMapper;
import jp.co.bobb.upms.mapper.RcUserRoleMapper;
import jp.co.bobb.upms.service.OwnerFeignService;
import jp.co.bobb.upms.service.UserService;
import jp.co.bobb.common.constant.*;
import jp.co.bobb.common.util.DateUtil;
import jp.co.bobb.common.util.R;
import jp.co.bobb.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Parker Sun
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    RcUserMapper userMapper;

    @Resource
    RcUserRoleMapper userRoleMapper;

    @Resource
    RcRoleMapper roleMapper;


    @Resource
    MemberInfoMapper memberInfoMapper;

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    OwnerFeignService ownerFeignService;

    @Override
    public RcUser findByUsername(String username) {

        Example example = Example.builder(RcUser.class)
                .where(Sqls.custom().andEqualTo("username", username))
                .build();
        RcUser rcUser = userMapper.selectOneByExample(example);


        return rcUser;
    }

    /**
     * 发送验证码
     * <p>
     * 1. 先去redis 查询是否 60S内已经发送
     * 2. 未发送： 判断手机号是否存 ? false :产生4位数字  手机号-验证码
     * 3. 发往消息中心-》发送信息
     * 4. 保存redis
     *
     * @param mobile 手机号
     * @return true、false
     */
    @Override
    public R<Boolean> sendSmsCode(String biz, String mobile) {
        Object tempCode = redisTemplate.opsForValue().get(SecurityConstants.DEFAULT_CODE_KEY + mobile);
        if (tempCode != null) {
            log.info(MessageFormat.format("用户:{0}验证码未失效{1}", mobile, tempCode));
            return new R<>(false, "验证码未失效，请失效后再次申请");
        }

        String code = RandomUtil.randomNumbers(4);
        log.info(MessageFormat.format("短信发送请求消息中心 -> 手机号:{0} -> 验证码：{1}", mobile, code));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("biz", biz);
        jsonObject.put("mobile", mobile);
        jsonObject.put("code", code);
        if (biz.equals(SecurityConstants.DEFAULT_BIZ)) {
            jsonObject.put("templateCode", "SMS_187225676");
        } else {
            jsonObject.put("templateCode", "SMS_199773964");
        }
        jsonObject.put("action", "login");
        amqpTemplate.convertAndSend(RabbitQueue.LOGIN_CODE_QUEUE, jsonObject);
        return new R<>(true);
    }

    @Override
    public RcUser findByMobile(String mobile) {
        Object mapValue = redisTemplate.opsForHash().get(RedisConstants.USER_CACHE_HASH_MAP, mobile);
        if (null != mapValue) {
            UserInfoDTO user = (UserInfoDTO) mapValue;
            if (System.currentTimeMillis() - user.getCacheTime() < TimeUnit.MINUTES.toMillis(30)) {
                RcUser result = new RcUser();
                BeanUtils.copyProperties(user, result);
                return result;
            }
        }
        Example example = Example.builder(RcUser.class)
                .where(Sqls.custom().andEqualTo("phone", mobile))
                .build();
        RcUser user = userMapper.selectOneByExample(example);
        if (null == user) {
            return null;
        }
        UserInfoDTO cacheUser = new UserInfoDTO();
        BeanUtils.copyProperties(user, cacheUser);
        cacheUser.setCacheTime(System.currentTimeMillis());
        redisTemplate.opsForHash().put(RedisConstants.USER_CACHE_HASH_MAP, mobile, cacheUser);
        return user;
    }

    @Override
    public R<Integer> updateUserInfo(RcUser user) {
        int row = userMapper.updateUserInfo(user);
        return new R<>(row);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUser(RcUser user) {
        userMapper.insert(user);
        // 赋权（普通会员）
        RcRole rcRole = roleMapper.getRoleByValue(RoleEnum.GENERAL_MEMEBR_ROLE.getValue());
        userRoleMapper.insertPermission(user.getId(), rcRole.getId());
    }

    @Override
    public RcUser selectUserByMemberId(Integer memberId) {
        return userMapper.selectUserByMemberId(memberId);
    }

    @Override
    public PageDTO findInsideUser(int pageNum, int pageSize) {
        PageDTO page = new PageDTO();
        Integer count = userMapper.countInsideUser();
        page.setTotal(count);
        if (count != 0) {
            List<RcUser> userList = userMapper.findInsideUser(pageSize, (pageNum - 1) * pageSize);
            dealUser(page, userList);
            page.setPageNum(pageNum);
            page.setPageSize(pageSize);
            page.setPageCount((count / pageSize) + 1);
        }
        return page;
    }

    @Override
    public PageDTO findOutsideUser(int pageNum, int pageSize) {
        PageDTO page = new PageDTO();
        int count = userMapper.countOutsideUser();
        page.setTotal(count);
        if (count != 0) {
            List<RcUser> userList = userMapper.findOutsideUser(pageSize, (pageNum - 1) * pageSize);
            dealUser(page, userList);
            page.setPageNum(pageNum);
            page.setPageSize(pageSize);
            page.setPageCount((count / pageSize) + 1);
        }
        return page;
    }

    private void dealUser(PageDTO page, List<RcUser> userList) {
        if (null != userList && userList.size() != 0) {
            List<UserVO> userVOList = Lists.newArrayList();
            for (RcUser user : userList) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                userVOList.add(userVO);
            }
            page.setInfo(userVOList);
        }
    }

    @Override
    public R<Boolean> queryMemberByMobile(String mobile) {
        RcUser rcUser = userMapper.queryMemberByMobile(mobile);
        if (null != rcUser) {
            return new R<>(Boolean.TRUE);
        }
        return new R<>(Boolean.FALSE);
    }

    @Override
    @Transactional
    public RcUser register(String mobile, Short code) {
        RcUser user = new RcUser();
        user.setUsername(mobile);
        user.setName(mobile);
        user.setPhone(mobile);
        user.setStatus(1);
        user.setRegisterTime(Instant.now().getEpochSecond());
        user.setCreateTime(DateUtil.getNowDate());
        user.setUpdateTime(DateUtil.getNowDate());
        if (null != code) {
            user.setIntlTelCode(code);
        }
        userMapper.insert(user);
        //user = userMapper.queryMemberByMobile(mobile);

        userMapper.insertUserInfo(user.getId(), MemberTitle.GENERAL_MEMBER.getLevel(), DateUtil.getNowDate());

        // 赋权（普通会员）
        RcRole rcRole = roleMapper.getRoleByValue(RoleEnum.GENERAL_MEMEBR_ROLE.getValue());
        userRoleMapper.insertPermission(user.getId(), rcRole.getId());
        // 如果已经存在代理人，则需要将会员id更新到代理人表 bobb_owner
        Boolean feignResult = ownerFeignService.updateOwnerInfo(mobile, user.getId());
        if (Objects.equals(feignResult, Boolean.TRUE)) {
            log.info("代理人表更新成功，mobile:{},userId:{}", mobile, user.getId());
        } else {
            log.error("代理人表更新失败，mobile:{},userId:{}", mobile, user.getId());
        }
        return user;
    }

    @Override
    public MemberInfo selectMemberMemberInfoByUserId(Integer userId) {
        return memberInfoMapper.selectByMemberId(userId);
    }

    @Override
    public void updateMemberWxInfo(Integer userId, String openId, String unionId) {
        memberInfoMapper.updateWxInfo(userId, openId, unionId);
    }

    @Override
    public Boolean updateUser(String mobile, Short code) {
        RcUser user = userMapper.selectByMobile(mobile);
        if (null != user) {
            user.setIntlTelCode(code);
        }
        int result = userMapper.updateUserByPrimaryKey(user);
        return result == 1 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public RcUser findByEmail(String email) {
        Object mapValue = redisTemplate.opsForHash().get(RedisConstants.USER_CACHE_HASH_MAP, email);
        if (null != mapValue) {
            UserInfoDTO user = (UserInfoDTO) mapValue;
            if (System.currentTimeMillis() - user.getCacheTime() < TimeUnit.MINUTES.toMillis(30)) {
                RcUser result = new RcUser();
                BeanUtils.copyProperties(user, result);
                return result;
            }
        }
        Example example = Example.builder(RcUser.class)
                .where(Sqls.custom().andEqualTo("email", email))
                .build();
        RcUser user = userMapper.selectOneByExample(example);
        if (null == user) {
            return null;
        }
        UserInfoDTO cacheUser = new UserInfoDTO();
        BeanUtils.copyProperties(user, cacheUser);
        cacheUser.setCacheTime(System.currentTimeMillis());
        redisTemplate.opsForHash().put(RedisConstants.USER_CACHE_HASH_MAP, email, cacheUser);
        return user;
    }

    @Override
    public R<Boolean> sendEmailCode(String email, HttpServletRequest request) {
        String domain = request.getHeader(SecurityConstants.HTTP_SERVER_DOMAIN_NAME);
        log.info("domain:{}", domain);
        Assert.notNull(domain, "domain is not null");
        switch (domain) {
            case "admin.bobb.com":
            case "oa.bobb.com":
            case "admin-waiter.smartwe.co.jp": {
                return Optional.ofNullable(userMapper.selectByEmail(email))
                        .map(rcUser -> {
                            String code = RandomUtil.randomNumbers(4);
                            //log.info("发送邮件 -> 邮件号码:{} -> 验证码：{}", email, code);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("email", email);
                            jsonObject.put("code", code);
                            jsonObject.put("action", "email_login");
                            // 发送验证码到邮箱
                            if (StringUtils.isBlank(rcUser.getSpaceName()) || "smartwe.liu@51fanxing.co.jp".equals(email)) {
                                amqpTemplate.convertAndSend(RabbitQueue.EMAIL_LOGIN_CODE_QUEUE, jsonObject);
                            } else {
                                // 发送验证码到google chat
                                amqpTemplate.convertAndSend(RabbitQueue.GOOGLE_CHAT_LOGIN_CODE_QUEUE, jsonObject);
                            }
                            // 把验证码写到redis中
                            jsonObject.put("username", rcUser.getUsername());
                            amqpTemplate.convertAndSend(RabbitQueue.LOGIN_CODE_CONFIRM_QUEUE, jsonObject);
                            return new R<>(true);
                        }).orElse(new R<>(false, "もう一度メールアドレスを確認してください"));
            }
            default: {
                return new R<>(false, "暂时只支持BoBB后台系统使用邮件验证码登录");
            }
        }
    }
}
