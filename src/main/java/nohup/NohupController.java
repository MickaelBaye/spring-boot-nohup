package nohup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/nohup")
public class NohupController {

    @Autowired
    private Environment environment;
    private static Logger logger = Logger.getLogger(NohupController.class.getName());
    private static List<String> reservedKeywords ;
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
            try {
                process = new NohupCommand().execute(new NohupProcess(nohupRequest.getCommand(), nohupRequest.getParameters()));
                response.setStatus("OK");
                response.setProcess(process);
                processes.put(response.getProcess().getId(), response.getProcess());
                // Add with alias
                if (nohupRequest.getAlias() != null && !nohupRequest.getAlias().isEmpty()) {
                    if (isReservedKeyword(nohupRequest.getAlias())) {
                        response.setStatus("WARN - Alias not accepted : " + nohupRequest.getAlias());
                        logger.log(Level.WARNING, "Alias not accepted : " + nohupRequest.getAlias());
                    } else if (processes.keySet().contains(nohupRequest.getAlias())) {
                        response.setStatus("WARN - Alias already used : " + nohupRequest.getAlias());
                        logger.log(Level.WARNING, "Alias already used : " + nohupRequest.getAlias());
                    } else {
                        processes.put(nohupRequest.getAlias(), response.getProcess());
                    }
                }
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

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Map<String, NohupProcess> nohupClearAll() {
        nohupKillAll();
        processes.clear();
        logger.log(Level.INFO, processes.toString());
        return processes;
    }

    @RequestMapping(value = "/killAll", method = RequestMethod.GET)
    public Map<String, NohupProcess> nohupKillAll() {

        Collection<NohupProcess> collection = processes.values();
        for (Iterator<NohupProcess> it = collection.iterator(); it.hasNext();) {
            NohupProcess p = it.next();
            if (p.kill()) {
                logger.log(Level.INFO, "Process " + p.toString() + " killed successfully.");
            }
        }
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

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public NohupResponse nohupUpdateById(@PathVariable String id, @RequestBody NohupRequest request) {

        logger.log(Level.INFO, "id=" + id);
        NohupResponse response = new NohupResponse();
        NohupProcess process = processes.get(id);

        if (process != null) {
            if (request.getCommand() == null || request.getCommand().isEmpty()) {
                response.setStatus("KO - Command not accepted : " + request.getCommand());
                logger.log(Level.WARNING, "Command not accepted : " + request.getCommand());
            } else {
                process.setCommand(request.getCommand());
                process.setParameters(request.getParameters());
                process.tail();
                processes.put(id, process);
                response.setProcess(process);
                response.setStatus("OK");
            }
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

    private List<String> initReservedKeyword() {

        String reservedKeywords = environment.getProperty("nohup.reservedKeywords");
        logger.log(Level.INFO, "nohup.reservedKeyword=" + reservedKeywords);

        List<String> ret = new ArrayList<>();
        if (!reservedKeywords.isEmpty()) {
            String[] words = reservedKeywords.split(",");
            for (int i = 0; i < words.length; i++) {
                ret.add(words[i]);
            }
        }

        return ret;
    }

    private boolean isReservedKeyword(String s) {

        logger.log(Level.INFO, "s=" + s);

        boolean ret = false;
        if (reservedKeywords == null) {
            reservedKeywords = initReservedKeyword();
        }
        if (s != null && !s.isEmpty())  {
            ret = reservedKeywords.contains(s);
        }

        logger.log(Level.INFO, "ret=" + ret);

        return ret;
    }
}