package self.srr.spot.detector.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class PageController {

    @RequestMapping("/")
    public String page() {
        return "index";
    }


}
