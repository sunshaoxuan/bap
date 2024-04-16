package jp.co.bobb.canal.task;

import jp.co.bobb.common.constant.RabbitQueue;
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
 * @date 2021/03/02
 */
@Slf4j
@Component
public class FxyxTask {

    @Resource
    AmqpTemplate amqpTemplate;


    /**
     * solr 每30分钟执行一次 搜索相关
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void solr() {
        log.info("solr");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "101");
    }

    /**
     * 102   每天上午10点执行一次    每天上午十点发到钉钉群的订单统计
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void order_count() {
        log.info("order_count");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "102");
    }

    /**
     * 103   每天凌晨1点执行一次    微博订单请求拉取
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void wei_bo_order_get() {
        log.info("wei_bo_order_get");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "103");
    }

    /**
     * 104   每小时执行一次       订单自动完成
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void order_auto_complete() {
        log.info("order_auto_complete");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "104");
    }

    /**
     * 105   每小时执行一次          超过24小时订单取消
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void order_auto_cancel() {
        log.info("order_auto_cancel");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "105");
    }

    /**
     * 106   每天夜里抽空执行一次     自动完成订单的评论
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void order_auto_complete_comment() {
        log.info("order_auto_complete_comment");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "106");
    }

    /**
     * 111   每天凌晨三点执行一次     对账
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void account_check() {
        log.info("account_check");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "111");
    }

    /**
     * 110   每小时执行一次          评论相关
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void order_comment() {
        log.info("order_comment");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "110");
    }


    /**
     * 107   每两小时执行一次        快递相关
     */
    @Scheduled(cron = "0 10 0/2 * * ?")
    public void delivery_107() {
        log.info("delivery_107");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "107");
    }

    /**
     * 107   每两小时执行一次        快递相关
     */
    @Scheduled(cron = "0 20 0/2 * * ?")
    public void delivery_108() {
        log.info("delivery_108");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "108");
    }

    /**
     * 107   每两小时执行一次        快递相关
     */
    @Scheduled(cron = "0 30 0/2 * * ?")
    public void delivery_109() {
        log.info("delivery_109");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "109");
    }

    /**
     * 112  直播报名更新状态  每小时0分或1分执行一次
     */
    @Scheduled(cron = "0 1 0/1 * * ?")
    public void delivery_112() {
        log.info("delivery_112");
        amqpTemplate.convertAndSend(RabbitQueue.FX_YX_DING_DING_ROBOT, "112");
    }
}
