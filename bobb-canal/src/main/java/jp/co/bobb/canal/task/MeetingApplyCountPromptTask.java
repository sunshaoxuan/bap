package jp.co.bobb.canal.task;

import jp.co.bobb.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/01/19
 */
@Component
@Slf4j
public class MeetingApplyCountPromptTask {
    @Autowired
    AmqpTemplate amqpTemplate;

    String TAX_CAL_QUEUE = "MEETING.APPLY.COUNT.QUEUE";

    String MEETING_AUTO_SET_OFFLINE = "MEETING_AUTO_SET_OFFLINE.QUEUE";


    /**
     * 说明会报名钉钉机器人提示
     */
    //@Scheduled(cron = "0 0 15 * * ?")
    public void healthCheck() {
        amqpTemplate.convertAndSend(TAX_CAL_QUEUE, "1");
    }

    /**
     * 说明会閉める
     */
    @Scheduled(cron = "0 30 * * * ?")
    public void meetingAutoClose() {
        amqpTemplate.convertAndSend(MEETING_AUTO_SET_OFFLINE, DateUtil.getNowLocalDateTime());
    }
}
