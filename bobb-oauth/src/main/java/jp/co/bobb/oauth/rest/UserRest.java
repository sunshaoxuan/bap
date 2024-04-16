package jp.co.bobb.oauth.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Parker Sun
 */
@RestController
@Slf4j
public class UserRest {

    @RequestMapping("/user")
    public Principal user(Principal user) {
        log.info(user.toString());
        return user;
    }
}
