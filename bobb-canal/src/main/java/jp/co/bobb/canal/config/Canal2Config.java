package jp.co.bobb.canal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/12/14
 */
@Configuration
public class Canal2Config {
    public static String host;
    public static Integer port;
    public static String instance;

    public static Integer batchSize;
    public static Integer sleep;

    @Value("${canal1.server.host}")
    public void setHost(String host) {
        Canal2Config.host = host;
    }

    @Value("${canal1.server.port}")
    public void setPort(String port) {
        Canal2Config.port = Integer.parseInt(port);
    }

    @Value("${canal1.server.instance-vip}")
    public void setInstance(String instance) {
        Canal2Config.instance = instance;
    }

    @Value("${canal1.server.batchSize}")
    public void setBatchSize(String batchSize) {
        Canal2Config.batchSize = Integer.parseInt(batchSize);
    }

    @Value("${canal1.server.sleep}")
    public void setSleep(String sleep) {
        Canal2Config.sleep = Integer.parseInt(sleep);
    }
}
