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

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public NohupResponse nohupNew(@RequestBody NohupRequest nohupRequest) {

        logger.log(Level.INFO, nohupRequest.toString());
        NohupResponse response = new NohupResponse();
        NohupProcess process;

        if (nohupRequest.getCommand() == null || nohupRequest.getCommand().isEmpty()) {
            response.setStatus("KO - Command not accepted : " + nohupRequest.getCommand());
            logger.log(Level.WARNING, "Command not accepted : " + nohupRequest.getCommand());
        } else {
            //TODO: use process instead of request
            try {
                process = new NohupCommand().execute(new NohupProcess(nohupRequest.getCommand(), nohupRequest.getParameters()));
                response.setStatus("OK");
                response.setProcess(process);
                processes.put(response.getProcess().getId(), response.getProcess());
            } catch (Exception e) {
                response.setStatus("KO - Failed to run new process");
                logger.log(Level.SEVERE, "KO - Failed to run new process", e);
            }
        }

        logger.log(Level.INFO, response.toString());

        return response ;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Map<String, NohupProcess> nohupGetAll() {
        logger.log(Level.INFO, processes.toString());
        return processes;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public NohupResponse nohupGetById(@PathVariable String id) {

        logger.log(Level.INFO, "id=" + id);
        NohupResponse response = new NohupResponse();
        NohupProcess process = processes.get(id);

        if (process != null) {
            response.setStatus("OK");
            process.tail();
            response.setProcess(process);
        } else {
            response.setStatus("KO - Nohup process not found : " + id);
            logger.log(Level.WARNING, "Nohup process not found : " + id);
        }

        logger.log(Level.INFO, response.toString());

        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public NohupResponse nohupDeleteById(@PathVariable String id) {

        logger.log(Level.INFO, "id=" + id);
        NohupResponse response = new NohupResponse();
        NohupProcess process = processes.get(id);

        if (process != null) {
            processes.remove(id);
            response.setStatus("OK");
            response.setProcess(process);
        } else {
            response.setStatus("KO - Nohup process not found : " + id);
            logger.log(Level.WARNING, "Nohup process not found : " + id);
        }

        logger.log(Level.INFO, response.toString());

        return response;
    }

    @RequestMapping(value = "/{id}/kill", method = RequestMethod.GET)
    public NohupResponse nohupKillById(@PathVariable String id) {

        logger.log(Level.INFO, "id=" + id);
        NohupResponse response = new NohupResponse();
        NohupProcess process = processes.get(id);

        if (process != null) {
            response.setProcess(process);
            try {
                if (process.kill()) {
                    response.setStatus("OK");
                } else {
                    response.setStatus("KO - Can not kill process : " + process.toString());
                    logger.log(Level.WARNING, "Can not kill process : " + process.toString());
                }
            } catch (Exception e) {
                response.setStatus("KO - Failed to kill process : " + process.toString());
                logger.log(Level.SEVERE, "Failed to kill process : " + process.toString());
            }
        } else {
            response.setStatus("KO - Nohup process not found : " + id);
            logger.log(Level.WARNING, "Nohup process not found : " + id);
        }

        logger.log(Level.INFO, response.toString());

        return response;
    }

    @RequestMapping(value = "/{id}/restart", method = RequestMethod.GET)
    public NohupResponse nohupRestartById(@PathVariable String id) {

        logger.log(Level.INFO, "id=" + id);
        NohupResponse response = new NohupResponse();
        NohupProcess process = processes.get(id);

        if (process != null) {
            response.setProcess(process);
            try {
                //TODO: use process instead of request
                process = new NohupCommand().execute(process);
                response.setStatus("OK");
                response.setProcess(process);
                processes.put(id, process);
            } catch (Exception e) {
                response.setStatus("KO - Failed to restart process : " + process.toString());
                logger.log(Level.SEVERE, "Failed to restart process : " + process.toString());
            }
        } else {
            response.setStatus("KO - Nohup process not found : " + id);
            logger.log(Level.WARNING, "Nohup process not found : " + id);
        }

        logger.log(Level.INFO, response.toString());

        return response;
    }
}