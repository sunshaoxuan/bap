package jp.co.bobb.upms.listener;

import com.alibaba.fastjson.JSONObject;
import jp.co.bobb.upms.entity.RcPermission;
import jp.co.bobb.upms.entity.RcRole;
import jp.co.bobb.upms.entity.RcUser;
import jp.co.bobb.upms.service.PermissionService;
import jp.co.bobb.upms.service.RoleService;
import jp.co.bobb.upms.service.UserService;
import jp.co.bobb.common.constant.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/12/12
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitQueue.NEW_OWNER_QUEUE)
public class MemberOwnerListener {
    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @RabbitHandler
    public void process(String message) {
        log.info(message);
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);
            if (jsonObject.getIntValue("isOwner") == 0) {
                log.info("owner {} is not owner", jsonObject.getString("mobile"));
                return;
            }
            String mobile = jsonObject.getString("mobile");
            RcRole role = roleService.getRoleByValue("owner");
            RcUser user = userService.findByMobile(mobile);
            List<RcPermission> roles = permissionService.getPermissionsByUserId(user.getId());
            for (RcPermission bean : roles) {
                if (bean.getRoleId().longValue() == role.getId()) {
                    log.info("user {} role {} exist.", user.getPhone(), role.getValue());
                    return;
                }
            }
            permissionService.insertUserOwnerPermission(user.getId(), role.getId());
        } catch (Exception e) {
            log.error("error", e);
        }
    }
}
