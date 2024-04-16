package jp.co.bobb.upms.rest;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jp.co.bobb.common.constant.InquiryMailEnum;
import jp.co.bobb.common.constant.RabbitQueue;
import jp.co.bobb.common.constant.SecurityConstants;
import jp.co.bobb.common.util.Assert;
import jp.co.bobb.common.util.R;
import jp.co.bobb.upms.service.UserService;
import jp.co.bobb.upms.vo.InquiryVO;
import jp.co.bobb.upms.vo.JobHuntingVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;


/**
 * @author Parker Sun
 */
@Controller
@Api(value = "ValidateCodeController", tags = {"验证码接口"})
@Slf4j
public class ValidateCodeController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    AmqpTemplate amqpTemplate;
    @Value("${qq.captcha.url}")
    String captchaUrl;
    @Value("${qq.captcha.appId}")
    String appId;
    @Value("${qq.captcha.appSecretKey}")
    String appSecretKey;
    @Value("${login.aes.key}")
    String aesKey;
    @Value("${login.aes.iv}")
    String ivParameter;
    @Autowired
    private Producer producer;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "validated", notes = "notes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "int", paramType = "query", required = true),
    })
    @GetMapping(value = "/validated")
    @ResponseBody
    public R<Boolean> validated(@RequestParam String mobile) {
        R<Boolean> result = userService.queryMemberByMobile(mobile);
        return result;
    }


    /**
     * 创建验证码
     *
     * @throws Exception
     */
    @GetMapping("/code/{randomStr}")
    public void createCode(@PathVariable String randomStr, HttpServletResponse response)
            throws Exception {
        Assert.isBlank(randomStr, "机器码不能为空");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);

        redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY + randomStr, image,
                SecurityConstants.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "JPEG", out);
        IOUtils.closeQuietly(out);
    }


    /**
     * 发送手机验证码
     * 后期要加接口限制
     *
     * @param mobile 手机号
     * @return R
     */
    @ApiOperation(value = "登录验证码", tags = {"login"}, notes = "登录验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "biz", value = "国际区号", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "ticket", value = "验证ticket", dataType = "string", paramType = "query",
                    required = true),
            @ApiImplicitParam(name = "randstr", value = "随机码", dataType = "string", paramType = "query", required =
                    true),
            @ApiImplicitParam(name = "secret", value = "加密串", dataType = "string", paramType = "query", required = true)
    })
    @ResponseBody
    @GetMapping("/smsCode")
    public R<Boolean> createCode(@RequestParam String biz,
                                 @RequestParam String mobile,
                                 HttpServletRequest request) {
        Assert.isBlank(mobile, "phone number not null");
        Assert.isBlank(biz, "biz number not null");
        String ticket = request.getParameter("ticket");
        String randstr = request.getParameter("randstr");
        String secret = request.getParameter("secret");

        log.info("mobile is {},ticket is {},randstr is {},secret is {}", mobile, ticket, randstr, secret);

        if (!StringUtils.isEmpty(ticket)) {
            String ip = getRequestIp(request);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(captchaUrl)
                    .queryParam("aid", appId)
                    .queryParam("AppSecretKey", appSecretKey)
                    .queryParam("Ticket", ticket)
                    .queryParam("Randstr", randstr)
                    .queryParam("UserIP", ip);
            log.info("builder uri is {}", builder.build().toUriString());
            String captchaResult = restTemplate.getForObject(builder.build().encode().toUri(), String.class);
            log.info("captchaResult is {}", captchaResult);
            JSONObject captchaJSONObject = JSONObject.parseObject(captchaResult);
            if (captchaJSONObject.getIntValue("response") == 1) {
                return userService.sendSmsCode(biz, mobile);
            } else {
                log.info("h5 login failed");
            }
        }

        if (!StringUtils.isEmpty(secret)) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

                byte[] initParam = ivParameter.getBytes();
                IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

                cipher.init(Cipher.ENCRYPT_MODE,
                        new SecretKeySpec(
                                aesKey.getBytes("UTF-8"), "AES"
                        ),
                        ivParameterSpec);

                byte[] bytes = cipher.doFinal(mobile.getBytes("UTF-8"));
                String serviceSecret = new BASE64Encoder().encode(bytes);
                System.out.println(serviceSecret);
                if (serviceSecret.equalsIgnoreCase(secret)) {
                    return userService.sendSmsCode(biz, mobile);
                } else {
                    log.info("wechat mini login failed");
                }
            } catch (Exception e) {
                log.error("Exception", e);
            }
        }
        return new R<>(false, "系统繁忙,请稍后重试.");

    }

    @ResponseBody
    @GetMapping("emailCode")
    public R<Boolean> createEmailCode(@RequestParam("email") String email, HttpServletRequest request) {
        log.info("email:{}", email);
        return userService.sendEmailCode(email, request);
    }

    private String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        ip = ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
        log.info("request ip is {}", ip);
        return ip;
    }

    @ApiOperation(value = "/inquiry/index", notes = "notes")
    @PutMapping("/inquiry/index")
    @ResponseBody
    public R<Boolean> index(@RequestBody InquiryVO inquiryVO) {
        log.info("request {}", inquiryVO);
        InquiryMailEnum inquiryMailEnum = InquiryMailEnum.getMailByDesc(inquiryVO.getMailCode());
        if (null == inquiryMailEnum) {
            return new R(Boolean.TRUE);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("to", inquiryMailEnum.getMail());
        jsonObject.put("subject", inquiryMailEnum.getDesc());
        jsonObject.put("content", inquiryVO.getContent());
        jsonObject.put("name", inquiryVO.getName());
        jsonObject.put("nameJP", inquiryVO.getNameJP());
        jsonObject.put("email", inquiryVO.getEmail());
        jsonObject.put("phone", inquiryVO.getPhone());
        amqpTemplate.convertAndSend(RabbitQueue.MAIL_SEND_QUEUE, jsonObject);

        return new R(Boolean.TRUE);
    }

    @ApiOperation(value = "/inquiry/job", notes = "notes")
    @PostMapping("/inquiry/job")
    @ResponseBody
    public R<Boolean> jobHunting(@RequestParam("file") MultipartFile multipartFile, JobHuntingVO jobHuntingVO) {
        if (multipartFile == null) {
            throw new IllegalArgumentException("参数异常");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("file", multipartFile.getBytes());
            jsonObject.put("to", "hr@51fanxing.co.jp");
            //jsonObject.put("to", "shengni1988@hotmail.com");
            jsonObject.put("subject", jobHuntingVO.getTitle());
            jsonObject.put("fileName", multipartFile.getOriginalFilename());

            jsonObject.put("name", jobHuntingVO.getName());
            jsonObject.put("nameJP", jobHuntingVO.getNameJP());
            jsonObject.put("email", jobHuntingVO.getMail());
            jsonObject.put("phone", jobHuntingVO.getPhone());
            jsonObject.put("sex", jobHuntingVO.getSex());
            jsonObject.put("address", jobHuntingVO.getAddress());
            jsonObject.put("lineTime", jobHuntingVO.getLineTime());

            amqpTemplate.convertAndSend(RabbitQueue.MAIL_JOB_SEND_QUEUE, jsonObject);

        } catch (Exception e) {
            log.error("exception", e);
        }
        return new R(Boolean.TRUE);
    }
}
