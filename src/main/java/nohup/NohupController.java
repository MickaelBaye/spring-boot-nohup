package nohup;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class NohupController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Nohup!";
    }

}