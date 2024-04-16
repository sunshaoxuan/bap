package jp.co.bobb.message.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import jp.co.bobb.common.constant.RabbitQueue;
import jp.co.bobb.common.constant.SecurityConstants;
import jp.co.bobb.common.enums.SMSTemplateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * 消息通知
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitQueue.NOTICE_QUEUE)
public class NoticeListener {

    @RabbitHandler
    public void process(JSONObject message) {
        log.info("message is {}", message);
        try {
            // 阿里云短信集成固定写法
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            final String product = "Dysmsapi";
            final String domain = "dysmsapi.aliyuncs.com";
            final String accessKeyId = "Ypf3OzaRNKEv3EQ7";
            final String accessKeySecret = "u0G2BvLTJJILmnjQ6f1S8K6AxZeeZg";
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                    accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            // 国内的手机不需要加前缀，国际的需要
            if (SecurityConstants.DEFAULT_BIZ.equals(message.getString("bizId"))) {
                request.setPhoneNumbers(message.getString("mobile"));
                request.setSignName("BoBB科技");
            } else {
                request.setPhoneNumbers(message.getString("bizId") + message.getString("mobile"));
                request.setSignName("TMK");
            }
            request.setTemplateCode(message.getString("templateCode"));
            // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            if (SMSTemplateEnum.INJAPAN_REMIND_OWNER.getKey().equals(message.getString("key"))) {
                request.setTemplateParam("{\"name\":\"" + message.getString("name") + "\",\"days\":\"" + message.getString("days") + "\"}");
            } else if (SMSTemplateEnum.CHANGEPHONE_SENGCODE.getKey().equals(message.getString("key"))) {
                request.setTemplateParam("{\"code\":\"" + message.getString("code") + "\"}");
            } else if (SMSTemplateEnum.CHANGEPHONE_REMIND_MANAGE.getKey().equals(message.getString("key"))) {
                request.setTemplateParam("{\"name\":\"" + message.getString("name") + "\"}");
            }
            log.info(JSON.toJSONString(request));
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("acs result {}", JSONObject.toJSONString(sendSmsResponse));
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                log.info("message send success");
            } else {
                log.error("message send fail");
            }
        } catch (Exception e) {
            log.error("send message exception", e);
        }
    }

}
