package jp.co.bobb.canal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/29
 */
@Configuration
public class CanalConfig {

    public static String host;
    public static Integer port;
    public static String instance;

    public static Integer batchSize;
    public static Integer sleep;

    @Value("${canal.server.host}")
    public void setHost(String host) {
        CanalConfig.host = host;
    }

    @Value("${canal.server.port}")
    public void setPort(String port) {
        CanalConfig.port = Integer.parseInt(port);
    }

    @Value("${canal.server.instance-vip}")
    public void setInstance(String instance) {
        CanalConfig.instance = instance;
    }

    @Value("${canal.server.batchSize}")
    public void setBatchSize(String batchSize) {
        CanalConfig.batchSize = Integer.parseInt(batchSize);
    }

    @Value("${canal.server.sleep}")
    public void setSleep(String sleep) {
        CanalConfig.sleep = Integer.parseInt(sleep);
    }
}
