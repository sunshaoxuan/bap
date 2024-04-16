package jp.co.bobb.canal.task;

import jp.co.bobb.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 考勤定时任务:
 *
 * @author wen
 */
@Slf4j
@Component
public class AttendanceTask {

    @Resource
    AmqpTemplate amqpTemplate;

    /**
     * 考勤数据初始化
     * <p>
     */
    String ATTENDANCE_FORMAT_INITIALIZE = "ATTENDANCE.FORMAT.INITIALIZE";

    /**
     * 处理昨天的考勤数据
     * <p>
     */
    String ATTENDANCE_YESTERDAY_JSON = "ATTENDANCE.YESTERDAY.JSON";

    String ATTENDANCE_HURI_SUBTRACT_QUEUE = "ATTENDANCE.HURI.SUBTRACT.QUEUE";


    /**
     * 每个月15号1点33分，初始化下个月的考勤数据
     */
    @Scheduled(cron = "1 33 1 15 * ?")
    public void attendanceJSONInit() {
        log.info("Attendance attendanceJSONInit");
        amqpTemplate.convertAndSend(ATTENDANCE_FORMAT_INITIALIZE, DateUtil.getNowLocalDate());
    }

    /**
     * 每天2点43分30秒触发一次，处理昨天的考勤数据
     */
    @Scheduled(cron = "30 43 2 ? * *")
    public void attendanceYesterdayJSONUpdate() {
        log.info("Attendance attendanceYesterdayJSONUpdate");
        amqpTemplate.convertAndSend(ATTENDANCE_YESTERDAY_JSON, DateUtil.getNowLocalDate());
    }

    /**
     * 每个月1号2点17分30s，对上个月的振休数据进行自动减少
     */
    @Scheduled(cron = "30 17 2 1 * ?")
    public void attendanceHuriAutoReduce() {
        log.info("Attendance attendanceHuriAutoReduce");
        amqpTemplate.convertAndSend(ATTENDANCE_HURI_SUBTRACT_QUEUE, DateUtil.getNowLocalDate());
    }
}
