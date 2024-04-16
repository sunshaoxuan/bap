package jp.co.bobb.canal.task;

import jp.co.bobb.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GlBookingTask {
    public static final String GL_BOOKING_LIMIT_SCAN = "GL.LIMIT.TIME.SCAN.QUEUE";

    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * GL限量售卖扫描器 每周一6点执行
     */
    @Scheduled(cron = "0 0 6 ? * MON")
    public void trustOrderStatusUpdate() {
        amqpTemplate.convertAndSend(GL_BOOKING_LIMIT_SCAN, DateUtil.getNowLocalDate());
    }
}
