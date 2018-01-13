package self.srr.spot.detector.api.aliyun;

import lombok.Data;

@Data
public class InstanceModel {

    private String regionId;

    private String zoneId;

    private String instanceType;

    private float pricePerHour;

    private float pricePerCorePerHour;

    private float originPricePerHour;
}
