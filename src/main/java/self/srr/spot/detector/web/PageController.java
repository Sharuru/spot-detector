package self.srr.spot.detector.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import self.srr.spot.detector.api.aliyun.InstanceModel;
import self.srr.spot.detector.model.DataTableResponse;
import self.srr.spot.detector.service.SpotService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class PageController {

    @Autowired
    SpotService spotService;

    @RequestMapping("/")
    public String page() {
        return "index";
    }

    @RequestMapping("/data/plans")
    @ResponseBody
    public DataTableResponse getSpotPlans(@RequestParam(name = "type", required = false, defaultValue = "empty") String fetchType) {

        List<InstanceModel> list = new ArrayList<>();

        InstanceModel dummy = new InstanceModel();
        dummy.setRegionId("cn-dummy");
        dummy.setZoneId("cn-dummy-a");
        dummy.setPricePerHour(0.2F);
        dummy.setPricePerCorePerHour(0.1F);
        dummy.setOriginPricePerHour(2.0F);
        dummy.setTypeId("ecs.dummy.xlarge");
        dummy.setConfigName("2C4G");
        InstanceModel dummy1 = new InstanceModel();
        dummy1.setRegionId("cn-dummy");
        dummy1.setZoneId("cn-dummy-b");
        dummy1.setPricePerHour(0.4F);
        dummy1.setPricePerCorePerHour(0.2F);
        dummy1.setOriginPricePerHour(4.0F);
        dummy1.setTypeId("ecs.dummy.xxlarge");
        dummy1.setConfigName("2C8G");
        InstanceModel dummy3 = new InstanceModel();
        dummy3.setRegionId("cn-dummy");
        dummy3.setZoneId("cn-dummy-a");
        dummy3.setPricePerHour(0.21F);
        dummy3.setPricePerCorePerHour(0.12F);
        dummy3.setOriginPricePerHour(2.01F);
        dummy3.setTypeId("ecs.dummy.xlarge");
        dummy3.setConfigName("2C4G");

        if (fetchType.equalsIgnoreCase("fetch")) {
            //list.addAll(spotService.getSpotInstancePlans());
            list.add(dummy);
            list.add(dummy1);
            list.add(dummy3);
        }

        DataTableResponse resp = new DataTableResponse();

        resp.setData(list);

        return resp;


    }


}
