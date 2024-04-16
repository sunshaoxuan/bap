package jp.co.bobb.oauth.service;

import jp.co.bobb.oauth.service.impl.UpmsFeignClientFallbackFactory;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.common.vo.UserVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "bobb-upms", fallbackFactory = UpmsFeignClientFallbackFactory.class)
public interface UserService {
    @GetMapping("user/findByUsername/{username}")
    Result<UserVO> findByUsername(@PathVariable("username") String username);

    @GetMapping("user/findByMobile/{mobile}")
    Result<UserVO> findUserByMobile(@PathVariable("mobile") String mobile);

    @GetMapping("user/register/{mobile}/{code}")
    Result<Long> autoRegister(@PathVariable("mobile") String mobile, @PathVariable("code") String code);

    @GetMapping("user/updateUser/{mobile}/{code}")
    Result updateUser(@PathVariable("mobile") String mobile, @PathVariable("code") String code);

    @GetMapping("user/findByEmail/{email}")
    Result<UserVO> findUserByEmail(@PathVariable("email") String email);
}
