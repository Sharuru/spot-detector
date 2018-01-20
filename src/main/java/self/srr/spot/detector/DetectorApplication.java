package self.srr.spot.detector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import self.srr.spot.detector.common.configuration.DetectorConfiguration;

@SpringBootApplication
@EnableConfigurationProperties({DetectorConfiguration.class})
@Slf4j
public class DetectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DetectorApplication.class, args);
    }
}


