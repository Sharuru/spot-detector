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

        if (fetchType.equalsIgnoreCase("fetch")) {
            list.addAll(spotService.getOrderedSpotInstancePlans());
        }

        DataTableResponse resp = new DataTableResponse();

        resp.setData(list);

        return resp;


    }


}
