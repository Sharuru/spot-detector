package self.srr.spot.detector.api.aliyun;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
import lombok.Data;

@Data
public class InstanceModel {

    private String regionId;

    private String zoneId;

    private float pricePerHour;

    private float pricePerCorePerHour;

    private float originPricePerHour;

    private DescribeInstanceTypesResponse.InstanceType instanceType;

    @Override
    public String toString() {
        return "InstanceModel{" +
                "regionId='" + regionId + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", pricePerHour=" + pricePerHour +
                ", pricePerCorePerHour=" + pricePerCorePerHour +
                ", originPricePerHour=" + originPricePerHour +
                ", instanceTypeId=" + instanceType.getInstanceTypeId() +
                ", instanceConfiguration=" + instanceType.getCpuCoreCount() + "C" + instanceType.getMemorySize().intValue() + "G" +
                '}';
    }
}
