package jp.co.bobb.upms.service;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;

@FeignClient(name = "bobb-oa")
public interface OaFeignService {

    /**
     * 对于已经存在的代理人，如果对应的手机号跟新登录的手机号一致的情况下，就把该会员id更新到对应的代理人信息上
     *
     * @return
     */
    @DeleteMapping("feign/delete/tree/cache")
    Boolean deleteRoleTreeCache();

}
