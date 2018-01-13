package self.srr.spot.detector.common.constants;

public class AliyunConstants {

    // magic number: 0.2 RMB / 4vCPU
    public static final Float maximumPerCorePrice = 0.2F / 4.0F;

    public static final Float maximumPerFourCorePrice = maximumPerCorePrice * 4.0F;

    public static final Integer maximumInternetBandwidthOut = 5;
}
