package nohup;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/nohup")
public class NohupController {

    private static Logger logger = Logger.getLogger(NohupController.class.getName());

    private Map<String, NohupProcess> processes = new HashMap<>();

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public NohupResponse nohupNew(@RequestBody NohupRequest nohupRequest) {

        logger.log(Level.INFO, nohupRequest.toString());
        NohupResponse response = new NohupResponse();
        if (nohupRequest.getCommand() == null || nohupRequest.getCommand().isEmpty()) {
            response.setStatus("KO - Command not accepted : " + nohupRequest.getCommand());
            logger.log(Level.WARNING, "Command not accepted : " + nohupRequest.getCommand());
        } else {
            response = new NohupCommand().execute(nohupRequest);
            processes.put(response.getProcess().getId(), response.getProcess());
        }

        logger.log(Level.INFO, response.toString());

        return response ;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public NohupResponse nohupGetById(@PathVariable String id) {

        logger.log(Level.INFO, "id=" + id);
        NohupResponse response = new NohupResponse();
        NohupProcess process = processes.get(id);

        if (process != null) {
            response.setStatus("OK");
            response.setProcess(process);
        } else {
            response.setStatus("KO - Nohup process not found : " + id);
            logger.log(Level.WARNING, "Nohup process not found : " + id);
        }

        logger.log(Level.INFO, response.toString());

        return response;
    }

    @RequestMapping(value = "/interrupt/{id}", method = RequestMethod.GET)
    public NohupResponse nohupInterruptById(@PathVariable String id) {

        logger.log(Level.INFO, "id=" + id);
        NohupResponse response = new NohupResponse();
        NohupProcess process = processes.get(id);

        if (process != null) {
            response.setProcess(process);
            try {
                if (process.interrupt()) {
                    response.setStatus("OK");
                    processes.remove(id);
                } else {
                    response.setStatus("KO - Can not interrupt process : " + process.toString());
                    logger.log(Level.WARNING, "Can not interrupt process : " + process.toString());
                }
            } catch (Exception e) {
                response.setStatus("KO - Failed to interrupt process : " + process.toString());
                logger.log(Level.SEVERE, "Failed to interrupt process : " + process.toString());
            }
        } else {
            response.setStatus("KO - Nohup process not found : " + id);
            logger.log(Level.WARNING, "Nohup process not found : " + id);
        }

        logger.log(Level.INFO, response.toString());

        return response;
    }
}