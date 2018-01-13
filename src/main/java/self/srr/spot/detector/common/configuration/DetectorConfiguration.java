package self.srr.spot.detector.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "detector")
@Data
public class DetectorConfiguration {

    private Api api;

    @Data
    public static class Api {
        private Aliyun aliyun;
    }

    @Data
    public static class Aliyun {
        private String defaultRegionId;
        private String defaultRegionPrefix;
        private String accessKeyId;
        private String accessSecret;
    }
}
