package jp.co.bobb.upms.listener;

import com.alibaba.fastjson.JSONObject;
import jp.co.bobb.upms.entity.RcUser;
import jp.co.bobb.upms.service.PermissionService;
import jp.co.bobb.upms.service.UserService;
import jp.co.bobb.common.constant.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/12/10
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitQueue.VIP_MEMBER_QUEUE_UPDATE)
public class VipMemberUpdateListener {

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    AmqpTemplate amqpTemplate;

    @RabbitHandler
    public void process(String message) {
        log.info("accept's message is {}", message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        Integer memberId = jsonObject.getInteger("memberId");
        String avatar = jsonObject.getString("avatar");
        String username = jsonObject.getString("memberName");
        String name = jsonObject.getString("nickname");
        Date birthday = jsonObject.getDate("birthday");
        int sex = jsonObject.getIntValue("gender");
        String email = jsonObject.getString("email");
        String phone = jsonObject.getString("memberPhone");
        int status = jsonObject.getIntValue("status");

        RcUser user = new RcUser();
        user.setAvatar(avatar);
        user.setMemberId(memberId);
        user.setUsername(username);
        user.setName(name);
        user.setBirthday(birthday);
        user.setSex(sex);
        user.setEmail(email);
        user.setPhone(phone);
        user.setStatus(status);
        RcUser rcUser = userService.selectUserByMemberId(memberId);
        if (null != rcUser) {
            //userService.updateUserInfo(user);
            log.info("user info update");
        } else {
            try {
                userService.insertUser(user);
                amqpTemplate.convertAndSend(RabbitQueue.QUERY_MEMBER_OWNER, JSONObject.toJSONString(user));
                log.info("user info insert");
            } catch (Exception e) {
                log.error("insert user exception", e);
            }
        }
        log.info(user.toString());
    }
}
