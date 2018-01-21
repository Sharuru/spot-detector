package self.srr.spot.detector.model;

import lombok.Data;
import self.srr.spot.detector.api.aliyun.InstanceModel;

import java.util.List;

@Data
public class DataTableResponse {

    private List<InstanceModel> data;
}
