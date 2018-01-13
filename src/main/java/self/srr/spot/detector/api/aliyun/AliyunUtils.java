package self.srr.spot.detector.api.aliyun;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import self.srr.spot.detector.common.configuration.DetectorConfiguration;

@Slf4j
@Component
public class AliyunUtils {

    private IClientProfile profile;
    private IAcsClient client;

    @Autowired
    public AliyunUtils(DetectorConfiguration detectorConfiguration) {

        profile = DefaultProfile.getProfile(
                detectorConfiguration.getApi().getAliyun().getDefaultRegionId(),
                detectorConfiguration.getApi().getAliyun().getAccessKeyId(),
                detectorConfiguration.getApi().getAliyun().getAccessSecret());

        client = new DefaultAcsClient(profile);
    }

    public <T extends AcsResponse> T callApi(AcsRequest<T> request) {
        try {
            return client.getAcsResponse(request);
        } catch (Throwable e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
