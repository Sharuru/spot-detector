package self.srr.spot.detector;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import self.srr.spot.detector.api.aliyun.DescribeApi;
import self.srr.spot.detector.api.aliyun.InstanceModel;
import self.srr.spot.detector.common.configuration.DetectorConfiguration;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.exit;

@SpringBootApplication
@EnableConfigurationProperties({DetectorConfiguration.class})
@Slf4j
public class DetectorApplication implements CommandLineRunner {

    @Autowired
    DetectorConfiguration detectorConfiguration;

    @Autowired
    DescribeApi describeApi;

    // magic number
    private Float maximumPerCorePrice = 0.2f / 4.0f;


    public static void main(String[] args) {
        SpringApplication.run(DetectorApplication.class, args);
    }

    @Override
    public void run(String... args) {

        Map<String, DescribeInstanceTypesResponse.InstanceType> instancesConfigurationMaps = new HashMap<>();

        Map<String, InstanceModel> targetInstantMaps = new HashMap<>();

        // initialize instance type configuration map
        describeApi.describeInstanceTypes().getInstanceTypes().forEach(type -> {
            // only 4vCPU or 8vCPU would be selected
            if (type.getCpuCoreCount() == 4 || type.getCpuCoreCount() == 8) {
                instancesConfigurationMaps.putIfAbsent(type.getInstanceTypeId(), type);
            }
        });

        // visit all regions and zones
        describeApi.describeRegions().getRegions().forEach(aRegion -> {
            // choose cn regions and zones only
            if (aRegion.getRegionId().startsWith(detectorConfiguration.getApi().getAliyun().getDefaultRegionPrefix())) {
                describeApi.describeZones(aRegion.getRegionId()).getZones().forEach(aZone -> {
                    log.info("now visiting region zone: " + aRegion.getRegionId() + " - " + aZone.getZoneId());
                    aZone.getAvailableInstanceTypes().forEach(aInstanceType -> {
                        // target instance type
                        if (instancesConfigurationMaps.containsKey(aInstanceType)) {
                            describeApi.describeSpotPriceHistories(aRegion.getRegionId(), aZone
                                    .getZoneId(), aInstanceType, "vpc").getSpotPrices().forEach(aPrice -> {
                                // target instancess
                                if (aPrice.getSpotPrice() / instancesConfigurationMaps.get(aInstanceType).getCpuCoreCount() <= maximumPerCorePrice) {

                                    InstanceModel model = new InstanceModel();

                                    model.setRegionId(aRegion.getRegionId());
                                    model.setZoneId(aZone.getZoneId());
                                    model.setInstanceType(aInstanceType);
                                    model.setPricePerHour(aPrice.getSpotPrice());
                                    model.setPricePerCorePerHour(aPrice.getSpotPrice() / instancesConfigurationMaps.get(aInstanceType).getCpuCoreCount());

                                    targetInstantMaps.putIfAbsent(model.toString(), model);
                                }
                            });
                        }
                    });
                });
            }
        });

        log.info("visit complete.");

        List<InstanceModel> targetInstants = new ArrayList<>(targetInstantMaps.values());

        targetInstants.sort(Comparator.comparing(InstanceModel::getPricePerCorePerHour));

        targetInstants.forEach(o -> System.out.println(o.toString()));

        exit(0);

    }


}
