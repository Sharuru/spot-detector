package self.srr.spot.detector.service;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import self.srr.spot.detector.DetectorApplication;
import self.srr.spot.detector.api.aliyun.AreaApi;
import self.srr.spot.detector.api.aliyun.InstanceApi;
import self.srr.spot.detector.api.aliyun.InstanceModel;
import self.srr.spot.detector.api.aliyun.PublicIpApi;
import self.srr.spot.detector.common.configuration.DetectorConfiguration;
import self.srr.spot.detector.common.constants.AliyunConstants;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.System.exit;

@Slf4j
@Service
public class SpotService {

    @Autowired
    DetectorConfiguration detectorConfiguration;

    @Autowired
    AreaApi areaApi;

    @Autowired
    InstanceApi instanceApi;

    @Autowired
    PublicIpApi publicIpApi;

    public List<InstanceModel> getOrderedSpotInstancePlans() {

        Map<String, DescribeInstanceTypesResponse.InstanceType> instancesConfigurationMaps = new HashMap<>();

        Map<String, InstanceModel> targetInstantMaps = new HashMap<>();

        // initialize instance type configuration map
        areaApi.describeInstanceTypes().getInstanceTypes().forEach(type -> {
            // only 4vCPU or 8vCPU would be selected
            if (type.getCpuCoreCount() == 4 || type.getCpuCoreCount() == 2) {
                instancesConfigurationMaps.putIfAbsent(type.getInstanceTypeId(), type);
            }
        });


        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<Runnable>> futures = new ArrayList<>();

        // visit all regions and zones
        areaApi.describeRegions().getRegions().forEach(aRegion -> {
            // choose cn regions and zones only
            if (aRegion.getRegionId().startsWith(detectorConfiguration.getApi().getAliyun().getDefaultRegionPrefix())) {

                Runnable visitRegionTask = () -> {

                    log.info("visiting region: " + aRegion.getRegionId() + " on new thread");

                    areaApi.describeZones(aRegion.getRegionId()).getZones().forEach(aZone -> {

                        log.info("now visiting region zone: " + aRegion.getRegionId() + " -> " + aZone.getZoneId());
                        aZone.getAvailableInstanceTypes().forEach(aInstanceType -> {
                            // target instance type

                            // FIXME filter t5 type
                            String[] insTypeArr = aInstanceType.split("\\.");
                            if (insTypeArr.length >= 3 && !insTypeArr[1].startsWith("t")) {

                                if (instancesConfigurationMaps.containsKey(aInstanceType)) {
                                    areaApi.describeSpotPriceHistories(aRegion.getRegionId(), aZone.getZoneId(), aInstanceType, "vpc").getSpotPrices().forEach(aPrice -> {
                                        // target instances
                                        if (aPrice.getSpotPrice() / instancesConfigurationMaps.get(aInstanceType).getCpuCoreCount() <= AliyunConstants.maximumPerCorePrice) {

                                            InstanceModel model = new InstanceModel();

                                            model.setRegionId(aRegion.getRegionId());
                                            model.setZoneId(aZone.getZoneId());
                                            model.setInstanceType(instancesConfigurationMaps.get(aInstanceType));
                                            model.setOriginPricePerHour(aPrice.getOriginPrice());
                                            model.setPricePerHour(aPrice.getSpotPrice());
                                            model.setPricePerCorePerHour(aPrice.getSpotPrice() / instancesConfigurationMaps.get(aInstanceType).getCpuCoreCount());

                                            targetInstantMaps.putIfAbsent(model.toString(), model);
                                        }
                                    });
                                }
                            }
                        });

                    });
                };

                Future visitResult = executorService.submit(visitRegionTask);
                futures.add(visitResult);

            }
        });

        // wait for all tasks to complete before continuing
        for (Future<Runnable> aFuture : futures) {
            try {
                aFuture.get();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        //shut down the executor executorService so that this thread can exit
        executorService.shutdownNow();

        log.info("visit complete.");

        List<InstanceModel> targetInstants = new ArrayList<>(targetInstantMaps.values());

        targetInstants.sort(Comparator.comparing(InstanceModel::getPricePerCorePerHour));

        log.info("Got: " + targetInstants.size() + " instance configurations matched requirement.");

        return targetInstants;
    }


    public void dummyArea() {

//
//        //TODO
        //InstanceModel m = targetInstants.get(0);
//
//        for (int i = 0; i < 10; i++) {
//            String name = "test-group-k-";
//            name = name + (i + 1);
//        CreateInstanceResponse r1 = instanceApi.createSpotInstance(
//                "cn-qingdao",
//                "cn-qingdao-b",
//                "ubuntu_16_0402_64_20G_alibase_20171227.vhd",
//                "ecs.s2.small",
//                name,
//                0);
//                //instancesConfigurationMaps.get("ecs.s3.medium").getCpuCoreCount());
//
//            publicIpApi.allocatePublicIpAddress(r1.getInstanceId());
//            instanceApi.modifyInstanceNetworkSpec(r1.getInstanceId());
//        }

        exit(0);
    }
}
