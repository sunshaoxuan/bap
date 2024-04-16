package jp.co.bobb.canal.task;

import jp.co.bobb.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/07/31
 */
@Slf4j
@Component
public class KouZaRepaymentTask {

    @Resource
    AmqpTemplate amqpTemplate;

    String KOU_ZA_REPAYMENT_QUEUE = "KOU.ZA.REPAYMENT.QUEUE";

    String KOU_ZA_REPAYMENT_INSURE_BACK = "KOU.ZA.REPAYMENT.INSURE.BACK";

    /**
     * 口座还款 每月1号1日还款
     */
    @Scheduled(cron = "0 0 10 1 * ?")
    public void healthCheck() {
        amqpTemplate.convertAndSend(KOU_ZA_REPAYMENT_QUEUE, DateUtil.getNowLocalDate());
    }


    /**
     * 口座还款 每天4点还款
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void repaymentInsureBack() {
        amqpTemplate.convertAndSend(KOU_ZA_REPAYMENT_INSURE_BACK, DateUtil.getNowLocalDate());
    }
}
