package jp.co.bobb.canal.task;

import jp.co.bobb.canal.runner.Canal2Runner;
import jp.co.bobb.canal.runner.CanalRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/29
 */
//@Component
@Slf4j
public class CanalTask {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Value("${canal.server.database}")
    String bobbDatabase;

    @Value("${canal1.server.database}")
    String vipDatabase;

    /**
     * 健康检查  canal重启 当机 异常重启时 canal消费者定时任务的方式保证消费继续进行
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void healthCheck() {
        int runners = CanalRunner.getRunners();
        if (runners < 1) {
            log.info("canal start");
            log.info("CanalRunner instance:{}", runners);
            log.info("CanalRunner execute restart");
            CanalRunner.restart(amqpTemplate, bobbDatabase);
            log.info("canal end");
        }

    }

    /**
     * 健康检查  canal重启 当机 异常重启时 canal消费者定时任务的方式保证消费继续进行
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void healthCheck2() {
        int runners = Canal2Runner.getRunners();
        if (runners < 1) {
            log.info("canal start");
            log.info("Canal2Runner instance:{}", runners);
            log.info("Canal2Runner execute restart");
            Canal2Runner.restart(amqpTemplate, vipDatabase);
            log.info("canal end");
        }
    }


}
