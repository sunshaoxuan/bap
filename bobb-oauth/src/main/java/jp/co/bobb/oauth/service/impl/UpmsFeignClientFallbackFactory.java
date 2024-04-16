package jp.co.bobb.oauth.service.impl;

import feign.hystrix.FallbackFactory;
import jp.co.bobb.oauth.service.UserService;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpmsFeignClientFallbackFactory implements FallbackFactory<UserService> {

    @Override
    public UserService create(Throwable throwable) {
        return new UserService() {

            @Override
            public Result<UserVO> findByUsername(String username) {
                log.error("e", throwable);
                return Result.failure(100, "调用findByUsername接口失败");
            }

            @Override
            public Result<UserVO> findUserByMobile(String mobile) {
                log.error("e", throwable);
                return Result.failure(100, "调用findUserByMobile接口失败");
            }

            @Override
            public Result<Long> autoRegister(String mobile, String code) {
                log.error("e", throwable);
                return Result.failure(100, "调用autoRegister接口失败");
            }

            @Override
            public Result updateUser(String mobile, String code) {
                log.error("e", throwable);
                return Result.failure(100, "调用updateUser接口失败");
            }

            @Override
            public Result<UserVO> findUserByEmail(String email) {
                log.error("e", throwable);
                return Result.failure(100, "调用findUserByEmail接口失败");
            }
        };
    }
}
