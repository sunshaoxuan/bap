package jp.co.bobb.upms.listener;

import com.alibaba.fastjson.JSONObject;
import jp.co.bobb.upms.entity.RcPermission;
import jp.co.bobb.upms.entity.RcRole;
import jp.co.bobb.upms.entity.RcUser;
import jp.co.bobb.upms.service.PermissionService;
import jp.co.bobb.upms.service.RoleService;
import jp.co.bobb.common.constant.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/12/14
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitQueue.SEND_MEMBER_OWNER)
public class MemberUpdateOwnerNotifyListener {
    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @RabbitHandler
    public void process(String message) {
        log.info(message);
        try {
            RcUser user = JSONObject.parseObject(message, RcUser.class);
            RcRole role = roleService.getRoleByValue("owner");
            log.info(role.toString());
            List<RcPermission> roles = permissionService.getPermissionsByUserId(user.getId());
            if (CollectionUtils.isEmpty(roles)) {
                permissionService.insertUserOwnerPermission(user.getId(), role.getId());
                return;
            }
            log.info(JSONObject.toJSONString(roles));
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
