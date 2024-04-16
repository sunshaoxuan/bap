package jp.co.bobb.upms.service.impl;

import jp.co.bobb.upms.service.OwnerFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class OwnerFeignServiceImpl implements OwnerFeignService {

    @Override
    public Boolean updateOwnerInfo(String mobile, Integer userId) {
        log.info("调用{}失败", "updateOwnerInfo");
        return Boolean.FALSE;
    }
}
