package jp.co.bobb.canal.task;

import jp.co.bobb.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/11/14
 */
@Component
@Slf4j
public class TrustTask {
    /**
     * 房租租金预收
     */
    String TRUST_RENT_COST_QUEUE = "TRUST_RENT_COST.QUEUE";

    /**
     * 业者托管管理费
     */
    String TRUST_MANAGER_COST_QUEUE = "TRUST_MANAGER_COST.QUEUE";

    /**
     * 承诺托管收益
     */
    String TRUST_PROMISE_COST_QUEUE = "TRUST_PROMISE_COST.QUEUE";
    /**
     * 长租订单提前终止日期到期后 自动更新长租订单状态到已完成
     */
    String LOGN_RENT_ORDER_FINISH_UPDATE = "LONG_RENT_ORDER_FINISH_UPDATE.QUEUE";

    /**
     * 平台托管管理费
     */
    String PLATFORM_MANAGER_COST_QUEUE = "PLATFORM_MANAGER_COST.QUEUE";

    /**
     * 托管订单状态变更
     */
    String TRUST_ORDER_STATUS_UPDATE = "TRUST_ORDER_STATUS_UPDATE.QUEUE";


    /**
     * 长租合约更新提醒
     */
    String LONG_RENT_ORDER_RENEW_NOTIFY_QUEUE = "LONG_RENT_ORDER_RENEW_NOTIFY.QUEUE";

    /**
     * 委託契約の決算を通知する
     */
    String TRUST_ORDER_SETTLE_NOTIFY_QUEUE = "TRUST_ORDER_SETTLE_NOTIFY.QUEUE";


    /**
     * 月次売上収支報告を通知する
     */
    String TRUST_MONTH_FINANCIAL_NOTIFY = "TRUST_MONTH_FINANCIAL_NOTIFY.QUEUE";

    /**
     * 管理部银行匹配数据報告を通知する
     */
    String TRUST_BANK_TRADE_MATCH_DAY_FINANCIAL_NOTIFY = "TRUST_BANK_TRADE_MATCH_DAY_FINANCIAL_NOTIFY.QUEUE";

    String TRUST_HOUSE_RENT_DIRECT_QUEUE = "TRUST_HOUSE_RENT_DIRECT.QUEUE";

    String PROMISE_TRUST_HOUSE_YEAR_COUNT = "PROMISE_TRUST_HOUSE_YEAR_COUNT.QUEUE";


    @Autowired
    AmqpTemplate amqpTemplate;
    String TRUST_ORDER_OVERDUE_NOTIFY_QUEUE = "TRUST.ORDER.OVERDUE.NOTIFY.QUEUE";

    /**
     * 托管订单状态变更 每天0点1分执行
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void trustOrderStatusUpdate() {
        amqpTemplate.convertAndSend(TRUST_ORDER_STATUS_UPDATE, DateUtil.getNowLocalDate());
    }

    /**
     * 托管订单状态变更 每天0点1分执行
     */
    @Scheduled(cron = "0 0 9 ? * MON")
    public void trust_house_rent_direct() {
        amqpTemplate.convertAndSend(TRUST_HOUSE_RENT_DIRECT_QUEUE, DateUtil.getNowLocalDate());
    }

    /**
     * 长租订单提前终止日期到期后 自动更新长租订单状态到已完成 每天凌晨0点30分
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void longRentOrderFinishUpdate() {
        log.info("task sign send");
        amqpTemplate.convertAndSend(LOGN_RENT_ORDER_FINISH_UPDATE, DateUtil.getNowLocalDate());
    }

    /**
     * 业者托管管理费 每月1号2点
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void managerCost() {
        amqpTemplate.convertAndSend(TRUST_MANAGER_COST_QUEUE, DateUtil.getNowLocalDate());
    }

    /**
     * 房租租金预收 每月1号3点执行
     */
    @Scheduled(cron = "0 0 3 1 * ?")
    public void trustRentCost() {
        amqpTemplate.convertAndSend(TRUST_RENT_COST_QUEUE, DateUtil.getNowLocalDate());
    }

    /**
     * 月次売上収支報告 每月1号4点执行
     */
    @Scheduled(cron = "0 0 4 1 * ?")
    public void uRiAgeShuUShiHouKoKu() {
        amqpTemplate.convertAndSend(TRUST_MONTH_FINANCIAL_NOTIFY, LocalDate.now());
    }

    /**
     * 平台托管管理费 每月1号5点
     */
    @Scheduled(cron = "0 0 5 1 * ?")
    public void platformManagerCost() {
        amqpTemplate.convertAndSend(PLATFORM_MANAGER_COST_QUEUE, DateUtil.getNowLocalDate());
    }

    /**
     * 承诺托管收益（由于部分托管收益的开始时间不是月度第一天、因此job是定义成每天执行） 每天23点
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void promiseCost() {
        amqpTemplate.convertAndSend(TRUST_PROMISE_COST_QUEUE, DateUtil.getNowLocalDate());
    }

    /**
     * 长租订单续签提醒（每天早上5点执行）
     */
    @Scheduled(cron = "0 0 5 * * ?")
    public void longRentOrderRenewNotify() {
        amqpTemplate.convertAndSend(LONG_RENT_ORDER_RENEW_NOTIFY_QUEUE, DateUtil.getNowLocalDate());
    }

    /**
     * 委託契約の決算を通知する（每天早上6点执行）
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void trustOrderSettleNotify() {
        amqpTemplate.convertAndSend(TRUST_ORDER_SETTLE_NOTIFY_QUEUE, DateUtil.getNowLocalDate());
    }

    /**
     * 銀行明細をマッチンぐすることは財務部に通知する（每天早上７点执行）
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void trustBankTradeMatchDayFinancialNotify() {
        amqpTemplate.convertAndSend(TRUST_BANK_TRADE_MATCH_DAY_FINANCIAL_NOTIFY, LocalDate.now());
    }

    /**
     * 支払調書一览 每年1月31号执行
     */
    @Scheduled(cron = "0 0 0 31 1 ?")
    public void promiseTrustHouseYearCount() {
        amqpTemplate.convertAndSend(PROMISE_TRUST_HOUSE_YEAR_COUNT, LocalDate.now());
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void healthCheck() {
        amqpTemplate.convertAndSend(TRUST_ORDER_OVERDUE_NOTIFY_QUEUE, "1");
    }
}
