package self.srr.spot.detector.api.aliyun;

import com.aliyuncs.ecs.model.v20140526.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AreaApi {

    @Autowired
    AliyunUtils aliyunUtils;

    public DescribeRegionsResponse describeRegions() {

        DescribeRegionsRequest request = new DescribeRegionsRequest();

        return aliyunUtils.callApi(request);
    }

    public DescribeZonesResponse describeZones(String regionId) {

        DescribeZonesRequest request = new DescribeZonesRequest();

        request.setRegionId(regionId);
        request.setSpotStrategy("SpotWithPriceLimit");
        request.setInstanceChargeType("PostPaid");

        return aliyunUtils.callApi(request);
    }

    public DescribeInstanceTypesResponse describeInstanceTypes() {

        DescribeInstanceTypesRequest request = new DescribeInstanceTypesRequest();

        return aliyunUtils.callApi(request);
    }

    public DescribeSpotPriceHistoryResponse describeSpotPriceHistories(String regionId, String zoneId, String instanceType, String networkType) {

        DescribeSpotPriceHistoryRequest request = new DescribeSpotPriceHistoryRequest();

        request.setRegionId(regionId);
        request.setZoneId(zoneId);
        request.setInstanceType(instanceType);
        request.setNetworkType(networkType);

        return aliyunUtils.callApi(request);
    }
}
