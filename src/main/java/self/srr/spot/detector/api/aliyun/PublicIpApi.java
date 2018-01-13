package self.srr.spot.detector.api.aliyun;

import com.aliyuncs.ecs.model.v20140526.AllocatePublicIpAddressRequest;
import com.aliyuncs.ecs.model.v20140526.AllocatePublicIpAddressResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PublicIpApi {


    @Autowired
    AliyunUtils aliyunUtils;

    public AllocatePublicIpAddressResponse allocatePublicIpAddress(String instanceId) {

        AllocatePublicIpAddressRequest request = new AllocatePublicIpAddressRequest();

        request.setInstanceId(instanceId);

        return aliyunUtils.callApi(request);
    }


}
