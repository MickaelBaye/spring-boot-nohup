package nohup;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/nohup")
public class NohupController {

    private Map<String, Runnable> processes = new HashMap<>();

    @RequestMapping(method = RequestMethod.POST)
    public NohupResponse nohup(@RequestBody NohupRequest nohupRequest) {

        NohupResponse response = new NohupCommand().execute(nohupRequest);

        return response ;
    }
}