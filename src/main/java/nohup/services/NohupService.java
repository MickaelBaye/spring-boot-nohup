package nohup.services;

import nohup.controllers.NohupController;
import nohup.exceptions.*;
import nohup.model.NohupCommand;
import nohup.model.NohupProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * TODO documentation
 * Created by mibaye on 24/07/2017.
 */
@Service
public class NohupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NohupService.class);

    @Autowired
    private Environment environment;
    private static List<String> reservedKeywords ;
    private Map<String, NohupProcess> processes = new HashMap<>();

    /**
     * TODO Documentation
     * @param command
     * @param parameters
     * @param alias
     * @return
     */
    public NohupProcess create(String command, List<String> parameters, String alias) throws CommandNotAcceptedException, FailedRunProcessException, AliasNotAcceptedException, AliasAlreadyUsedException {

        LOGGER.info("command = {}, parameters = {}, alias = {}", command, parameters, alias);
        NohupProcess nohupProcess = null;

        if (command == null || command.isEmpty()) {
            LOGGER.warn("Command not accepted : {}", command);
            throw new CommandNotAcceptedException(String.format("Command not accepted : %s", command));
        } else try {
            nohupProcess = new NohupCommand().execute(new NohupProcess(command, parameters, alias));
        } catch (Exception e) {
            LOGGER.error("Failed to run new process", e);
            throw new FailedRunProcessException("Failed to run new process", e);
        }

        processes.put(nohupProcess.getId(), nohupProcess);
        addAlias(alias, nohupProcess);

        return nohupProcess;
    }

    /**
     * TODO documentation
     * @param id
     * @return
     * @throws ProcessNotFoundException
     */
    public NohupProcess getById(String id) throws ProcessNotFoundException {

        NohupProcess process = processes.get(id);
        if (process == null) {
            LOGGER.error("Process not found, id = {}", id);
            throw new ProcessNotFoundException(String.format("Process not found, id = %s", id));
        }
        process.tail();
        return process;
    }

    /**
     *
     * @param id
     * @param command
     * @param parameters
     * @param alias
     * @return
     * @throws CommandNotAcceptedException
     * @throws FailedRunProcessException
     * @throws ProcessNotFoundException
     */
    public NohupProcess updateById(String id, String command, List<String> parameters, String alias) throws CommandNotAcceptedException, ProcessNotFoundException, AliasNotAcceptedException, AliasAlreadyUsedException {

        LOGGER.info("id = {}, command = {}, parameters = {}, alias = {}", id, command, parameters, alias);
        NohupProcess nohupProcess = getById(id);

        if (command == null || command.isEmpty()) {
            LOGGER.warn("Command not accepted : {}", command);
            throw new CommandNotAcceptedException(String.format("Command not accepted : %s", command));
        } else {
            nohupProcess.setCommand(command);
            nohupProcess.setParameters(parameters);
            nohupProcess.tail();
        }

        processes.put(nohupProcess.getId(), nohupProcess);
        addAlias(alias, nohupProcess);

        return nohupProcess;
    }

    /**
     * TODO documentation
     * @param id
     * @return
     * @throws ProcessNotFoundException
     */
    public NohupProcess deleteById(String id) throws ProcessNotFoundException {

        NohupProcess process = processes.remove(id);
        if (process == null) {
            LOGGER.error("Process not found, id = {}", id);
            throw new ProcessNotFoundException(String.format("Process not found, id = %s", id));
        }
        return process;
    }

    /**
     * TODO documentation
     * @param id
     * @return
     * @throws ProcessNotFoundException
     * @throws FailedKillException
     */
    public NohupProcess killById(String id) throws ProcessNotFoundException, FailedKillException {

        NohupProcess process = getById(id);
        process.kill();
        return process;
    }

    /**
     * TODO documentation
     * @param id
     * @return
     * @throws ProcessNotFoundException
     * @throws FailedRunProcessException
     */
    public NohupProcess restartById(String id) throws ProcessNotFoundException, FailedRunProcessException {

        NohupProcess process = getById(id);
        try {
            process = new NohupCommand().execute(process);
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to restart process : %s", process.toString()), e);
            throw new FailedRunProcessException(String.format("Failed to restart process : %s", process.toString()), e);
        }

        return process;
    }

    /**
     * TODO Documentation
     * @return
     */
    public Map<String, NohupProcess> getProcesses () {
        return processes;
    }

    /**
     * TODO documentation
     * @throws FailedClearAllException
     */
    public void clearAll() throws FailedClearAllException {
        try {
            killAll();
            processes.clear();
        } catch (Exception e) {
            LOGGER.error("Failed to clear all processes", e);
            throw new FailedClearAllException("Failed to clear all processes", e);
        }
    }

    /**
     * TODO documentation
     * @throws FailedKillAllException
     */
    public void killAll() throws FailedKillAllException {
        try {
            Collection<NohupProcess> collection = processes.values();
            for (Iterator<NohupProcess> it = collection.iterator(); it.hasNext();) {
                NohupProcess p = it.next();
                p.kill();
                LOGGER.info("Process {} killed successfully.", p.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to kill all processes", e);
            throw new FailedKillAllException("Failed to kill all processes", e);
        }
    }

    /**
     * TODO documentation
     * @param alias
     * @param nohupProcess
     * @throws AliasNotAcceptedException
     * @throws AliasAlreadyUsedException
     */
    private void addAlias(String alias, NohupProcess nohupProcess) throws AliasNotAcceptedException, AliasAlreadyUsedException {
        if (alias != null && !alias.isEmpty()) {
            if (isReservedKeyword(alias)) {
                LOGGER.warn("Alias not accepted : {}", alias);
                throw new AliasNotAcceptedException(String.format("Alias not accepted : %s", alias));
            } else if (processes.keySet().contains(alias)) {
                LOGGER.warn("Alias already used : {}", alias);
                throw new AliasAlreadyUsedException(String.format("Alias already used : %s", alias));
            } else {
                nohupProcess.getAliases().add(alias);
                processes.put(alias, nohupProcess);
            }
        }
    }

    /**
     * TODO documentation
     * @param s
     * @return
     */
    private boolean isReservedKeyword(String s) {

        LOGGER.info("s = {}", s);

        boolean ret = false;
        if (reservedKeywords == null) {
            reservedKeywords = initReservedKeyword();
        }
        if (s != null && !s.isEmpty())  {
            ret = reservedKeywords.contains(s);
        }

        LOGGER.info("ret = {}", ret);

        return ret;
    }

    /**
     * TODO documentation
     * @return
     */
    private List<String> initReservedKeyword() {

        String reservedKeywords = environment.getProperty("nohup.reservedKeywords");
        LOGGER.info("nohup.reservedKeyword = {}", reservedKeywords);

        List<String> ret = new ArrayList<>();
        if (!reservedKeywords.isEmpty()) {
            String[] words = reservedKeywords.split(",");
            for (int i = 0; i < words.length; i++) {
                ret.add(words[i]);
            }
        }

        return ret;
    }
}
