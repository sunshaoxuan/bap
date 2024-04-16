package jp.co.bobb.oauth.endpoint;

import com.alibaba.fastjson.JSONObject;
import jp.co.bobb.oauth.model.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@FrameworkEndpoint
@Slf4j
public class RevokeTokenEndpoint {
    @Resource
    ConsumerTokenServices consumerTokenServices;

    @RequestMapping(value = "/oauth/token", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg revokeToken(@RequestBody JSONObject token) {
        Msg msg = new Msg();
        consumerTokenServices.revokeToken(token.getString("accessToken"));
        msg.setCode(Msg.SUCCESS);
        msg.setMsg("logout success");
        return msg;
    }

}
