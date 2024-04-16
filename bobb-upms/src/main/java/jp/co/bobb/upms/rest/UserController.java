package jp.co.bobb.upms.rest;

import jp.co.bobb.common.constant.MemberTitle;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.common.vo.UserVO;
import jp.co.bobb.upms.dto.PageDTO;
import jp.co.bobb.upms.entity.MemberInfo;
import jp.co.bobb.upms.entity.RcUser;
import jp.co.bobb.upms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Parker Sun
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("register/{mobile}/{code}")
    public Result register(@PathVariable("mobile") String mobile, @PathVariable("code") String code) {
        log.info("mobile is {},code is {}", mobile, code);
        RcUser user = userService.register(mobile, Short.valueOf(code));
        if (user == null) {
            return Result.failure(100, "自动注册失败");
        }
        return Result.ok().setData(user.getId());
    }

    @GetMapping("register/{mobile}")
    public Result register(@PathVariable("mobile") String mobile) {
        log.info("mobile is {}", mobile);
        RcUser user = userService.register(mobile, null);
        if (user == null) {
            return Result.failure(100, "自动注册失败");
        }
        return Result.ok().setData(user.getId());
    }

    @GetMapping("findByUsername/{username}")
    public Result findByUsername(@PathVariable("username") String username) {
        RcUser user = userService.findByUsername(username);
        if (user == null) {
            return Result.failure(100, "用户不存在");
        }
        log.info("username:{}", username);
        return Result.ok().setData(user);
    }

    @GetMapping("findByMobile/{mobile}")
    public Result findByMobile(@PathVariable("mobile") String mobile) {
        log.info("mobile is {}", mobile);
        RcUser user = userService.findByMobile(mobile);
        if (user == null) {
            log.info("手机号不存在");
            return Result.failure(100, "手机号不存在");
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        MemberInfo memberInfo = userService.selectMemberMemberInfoByUserId(user.getId());
        if (null != memberInfo) {
            vo.setLevel(memberInfo.getLevel().intValue());
        } else {
            vo.setLevel(MemberTitle.GENERAL_MEMBER.getLevel());
        }

        return Result.ok().setData(vo);
    }

    @GetMapping("findByEmail/{email:.+}")
    public Result findByEmail(@PathVariable("email") String email) {
        log.info("email is {}", email);
        RcUser user = userService.findByEmail(email);
        if (user == null) {
            log.info("邮箱号不存在");
            return Result.failure(100, "邮箱号不存在");
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        MemberInfo memberInfo = userService.selectMemberMemberInfoByUserId(user.getId());
        if (null != memberInfo) {
            vo.setLevel(memberInfo.getLevel().intValue());
        } else {
            vo.setLevel(MemberTitle.GENERAL_MEMBER.getLevel());
        }

        return Result.ok().setData(vo);
    }

    @GetMapping("updateUser/{mobile}/{code}")
    public Result updateUser(@PathVariable("mobile") String mobile, @PathVariable("code") String code) {
        log.info("mobile is {},code is {}", mobile, code);
        Boolean result = userService.updateUser(mobile, Short.valueOf(code));
        return result ? Result.ok() : Result.failure(100, "更新失败");
    }

    @GetMapping("updateWxInfo/{userId}/{openId}/{unionId}")
    public Result updateWxInfo(@PathVariable("userId") Integer userId,
                               @PathVariable("openId") String openId,
                               @PathVariable("unionId") String unionId) {
        log.info("userId:{},openId:{},unionId:{}", userId, openId, unionId);
        userService.updateMemberWxInfo(userId, openId, unionId);
        return Result.ok().setData(Boolean.TRUE);
    }

    /**
     * 查询内部用户接口
     *
     * @param pageNum  页码（默认为1）
     * @param pageSize 每页用户数（默认为10）
     * @return
     */
    @GetMapping("findInsideUser")
    public Result findInsideUser(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageDTO result = userService.findInsideUser(pageNum, pageSize);
        if (result.getTotal() == 0) {
            return Result.failure(100, "不存在内部用户");
        }
        return Result.ok().setData(result);
    }

    /**
     * 查询外部用户接口
     *
     * @param pageNum  页码（默认为1）
     * @param pageSize 每页用户数（默认为10）
     * @return
     */
    @GetMapping("findOutsideUser")
    public Result findOutsideUser(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageDTO result = userService.findOutsideUser(pageNum, pageSize);
        if (result.getTotal() == 0) {
            return Result.failure(100, "不存在外部用户");
        }
        return Result.ok().setData(result);
    }
}
