package nohup.controllers;

import nohup.exceptions.*;
import nohup.model.NohupProcess;
import nohup.model.NohupRequest;
import nohup.model.NohupResponse;
import nohup.services.NohupMonitoring;
import nohup.services.NohupService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * TODO documentation
 */
@RestController
@RequestMapping("/nohup")
public class NohupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NohupController.class);

    @Resource
    private NohupService nohupService;

    @Resource
    private NohupMonitoring nohupMonitoring;

    /**
     * TODO Documentation
     * @param nohupRequest
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public NohupResponse nohupNew(@RequestBody NohupRequest nohupRequest) {

        LOGGER.info(nohupRequest.toString());

        NohupResponse nohupResponse = new NohupResponse();
        NohupProcess nohupProcess = null;

        try {
            nohupProcess = nohupService.create(nohupRequest.getCommand(), nohupRequest.getParameters(), nohupRequest.getAlias());
            nohupResponse.setStatus(NohupResponse.Status.OK);
            nohupResponse.setProcess(nohupProcess);
            LOGGER.info("OK - create new process = {}", nohupResponse.toString());
        } catch (FailedRunProcessException | CommandNotAcceptedException | AliasNotAcceptedException | AliasAlreadyUsedException e) {
            LOGGER.error("KO", e);
            nohupResponse.setStatus(NohupResponse.Status.KO);
            nohupResponse.setMessage(e.getMessage());
            nohupMonitoring.monitor(new DateTime(), "", e.getMessage(), "nohupNew", NohupResponse.Status.KO, null);
            return nohupResponse;
        }

        nohupMonitoring.monitor(new DateTime(), "", nohupResponse.getMessage(), "nohupNew", nohupResponse.getStatus(), nohupRequest);
        LOGGER.info("OK - create process = {}", nohupResponse.toString());

        return nohupResponse ;
    }

    /**
     * TODO documentation
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Map<String, NohupProcess> nohupGetAll() {
        Map<String, NohupProcess> processes = nohupService.getProcesses();
        nohupMonitoring.monitor(new DateTime(), "", "", "nohupGetAll", NohupResponse.Status.OK, null);
        LOGGER.info("OK - get all processes = {}", processes.toString());
        return processes;
    }

    /**
     * TODO documentation
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Map<String, NohupProcess> nohupClearAll() {
        Map<String, NohupProcess> processes;
        try {
            nohupService.clearAll();
        } catch (FailedClearAllException e) {
            LOGGER.error("KO - clear all processes", e);
            processes = nohupService.getProcesses();
            nohupMonitoring.monitor(new DateTime(), "", e.getMessage(), "nohupClearAll", NohupResponse.Status.KO, null);
            return processes;
        }
        processes = nohupService.getProcesses();
        nohupMonitoring.monitor(new DateTime(), "", "", "nohupClearAll", NohupResponse.Status.OK, null);
        LOGGER.info("OK - clear all processes = {}", processes.toString());
        return processes;
    }

    /**
     * TODO Documentation
     * @return
     */
    @RequestMapping(value = "/killAll", method = RequestMethod.GET)
    public Map<String, NohupProcess> nohupKillAll() {
        Map<String, NohupProcess> processes = null;
        try {
            nohupService.killAll();
        } catch (FailedKillAllException e) {
            LOGGER.error("KO - kill all processes", e);
            processes = nohupService.getProcesses();
            nohupMonitoring.monitor(new DateTime(), "", e.getMessage(), "nohupKillAll", NohupResponse.Status.KO, null);
            return processes;
        }
        processes = nohupService.getProcesses();
        nohupMonitoring.monitor(new DateTime(), "", "", "nohupKillAll", NohupResponse.Status.OK, null);
        LOGGER.info("OK - kill all processes = {}", processes.toString());
        return processes;
    }

    /**
     * TODO documentation
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public NohupResponse nohupGetById(@PathVariable String id) {

        LOGGER.info("id = {}", id);

        NohupResponse response = new NohupResponse();
        NohupProcess process;

        try {
            process = nohupService.getById(id);
            response.setStatus(NohupResponse.Status.OK);
            response.setProcess(process);
        } catch (ProcessNotFoundException e){
            LOGGER.error(String.format("Process not found : %s", id), e);
            response.setStatus(NohupResponse.Status.KO);
            // response.setMessage(String.format("Process not found : %s", id));
            response.setMessage(e.getMessage());
            nohupMonitoring.monitor(new DateTime(), "", e.getMessage(), "nohupGetById", NohupResponse.Status.KO, null);
            return response;
        }

        nohupMonitoring.monitor(new DateTime(), "", "", "nohupGetById", NohupResponse.Status.OK, null);
        LOGGER.info("OK - get process = {}", response.toString());

        return response;
    }

    /**
     * TODO documentation
     * @param id
     * @param nohupRequest
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public NohupResponse nohupUpdateById(@PathVariable String id, @RequestBody NohupRequest nohupRequest) {

        LOGGER.info("id = {}, nohupRequest = {}", id, nohupRequest.toString());
        NohupResponse nohupResponse = new NohupResponse();
        NohupProcess nohupProcess;

        try {
            nohupProcess = nohupService.updateById(id, nohupRequest.getCommand(), nohupRequest.getParameters(), nohupRequest.getAlias());
            nohupResponse.setProcess(nohupProcess);
            nohupResponse.setStatus(NohupResponse.Status.OK);
        } catch (CommandNotAcceptedException | AliasNotAcceptedException | AliasAlreadyUsedException | ProcessNotFoundException e) {
            LOGGER.error("KO", e);
            nohupResponse.setStatus(NohupResponse.Status.KO);
            // nohupResponse.setMessage(String.format("Command not accepted : {}", nohupRequest.getCommand()));
            nohupResponse.setMessage(e.getMessage());
            nohupMonitoring.monitor(new DateTime(), "", e.getMessage(), "nohupUpdateById", NohupResponse.Status.KO, nohupRequest);
            return nohupResponse;
        }

        nohupMonitoring.monitor(new DateTime(), "", "", "nohupUpdateById", NohupResponse.Status.OK, nohupRequest);
        LOGGER.info("OK - update process = {}", nohupResponse.toString());

        return nohupResponse;
    }

    /**
     * TODO documentation
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public NohupResponse nohupDeleteById(@PathVariable String id) {

        LOGGER.info("id = {}", id);
        NohupResponse nohupResponse = new NohupResponse();
        NohupProcess process ;

        try {
            process = nohupService.deleteById(id);
            nohupResponse.setStatus(NohupResponse.Status.OK);
            nohupResponse.setProcess(process);
        } catch (ProcessNotFoundException e) {
            LOGGER.error(String.format("KO - Process not found : {}", id), e);
            nohupResponse.setStatus(NohupResponse.Status.KO);
            nohupResponse.setMessage(String.format("Process not found : {}", id));
            nohupMonitoring.monitor(new DateTime(), "", e.getMessage(), "nohupDeleteById", NohupResponse.Status.KO, null);
            return nohupResponse;
        }

        nohupMonitoring.monitor(new DateTime(), "", "", "nohupDeleteById", NohupResponse.Status.OK, null);
        LOGGER.info("OK - delete process = {}", nohupResponse.toString());

        return nohupResponse;
    }

    /**
     * TODO documentation
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/kill", method = RequestMethod.GET)
    public NohupResponse nohupKillById(@PathVariable String id) {

        LOGGER.info("id = {}", id);
        NohupResponse nohupResponse = new NohupResponse();
        NohupProcess process = null;

        try {
            process = nohupService.killById(id);
            nohupResponse.setStatus(NohupResponse.Status.OK);
            nohupResponse.setProcess(process);
        } catch (ProcessNotFoundException | FailedKillException e) {
            LOGGER.error(String.format("KO - Process not found : %s", id), e);
            nohupResponse.setStatus(NohupResponse.Status.KO);
            // nohupResponse.setMessage(String.format("Process not found : %s", id));
            nohupResponse.setMessage(e.getMessage());
            nohupMonitoring.monitor(new DateTime(), "", e.getMessage(), "nohupKillById", NohupResponse.Status.KO, null);
            return nohupResponse;
        }

        nohupMonitoring.monitor(new DateTime(), "", "", "nohupDeleteById", NohupResponse.Status.OK, null);
        LOGGER.info("OK - kill process = {}", nohupResponse.toString());

        return nohupResponse;
    }

    /**
     * TODO documentation
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/restart", method = RequestMethod.GET)
    public NohupResponse nohupRestartById(@PathVariable String id) {

        LOGGER.info("id = {}", id);
        NohupResponse nohupResponse = new NohupResponse();
        NohupProcess process = null;

        try {
            process = nohupService.restartById(id);
            nohupResponse.setStatus(NohupResponse.Status.OK);
            nohupResponse.setProcess(process);
        } catch (ProcessNotFoundException | FailedRunProcessException e) {
            LOGGER.error(String.format("KO - Process not found : %s", id), e);
            nohupResponse.setStatus(NohupResponse.Status.KO);
            nohupResponse.setMessage(String.format("Process not found : %s", id));
            nohupMonitoring.monitor(new DateTime(), "", e.getMessage(), "nohupRestartById", NohupResponse.Status.KO, null);
            return nohupResponse;
        }

        nohupMonitoring.monitor(new DateTime(), "", "", "nohupDeleteById", NohupResponse.Status.OK, null);
        LOGGER.info("OK - restart process = {}", nohupResponse.toString());

        return nohupResponse;
    }

}