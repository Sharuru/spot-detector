package self.srr.spot.detector.api.aliyun;

import com.aliyuncs.ecs.model.v20140526.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import self.srr.spot.detector.common.configuration.DetectorConfiguration;
import self.srr.spot.detector.common.constants.AliyunConstants;

@Component
@Slf4j
public class InstanceApi {

    @Autowired
    AliyunUtils aliyunUtils;

    @Autowired
    DetectorConfiguration detectorConfiguration;

    public CreateInstanceResponse createSpotInstance(String regionId, String zoneId, String imageId, String instanceType, String hostname, Integer cpuCoreCount) {

        CreateInstanceRequest request = new CreateInstanceRequest();

        request.setRegionId(regionId);
        request.setZoneId(zoneId);
        request.setImageId(imageId);
        request.setInstanceType(instanceType);

        request.setHostName(hostname);
        request.setPassword(detectorConfiguration.getApi().getAliyun().getDefaultLoginPassword());

        request.setInternetChargeType("PayByTraffic");
        request.setInstanceChargeType("PostPaid");
        request.setSpotStrategy("SpotWithPriceLimit");

        request.setSpotPriceLimit(AliyunConstants.maximumPerFourCorePrice * (cpuCoreCount / 4.0F));

        return aliyunUtils.callApi(request);

    }

    public ModifyInstanceNetworkSpecResponse modifyInstanceNetworkSpec(String instanceId) {

        ModifyInstanceNetworkSpecRequest request = new ModifyInstanceNetworkSpecRequest();

        request.setInstanceId(instanceId);
        request.setInternetMaxBandwidthOut(AliyunConstants.maximumInternetBandwidthOut);

        return aliyunUtils.callApi(request);

    }


}
