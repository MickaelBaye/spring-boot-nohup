package nohup.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nohup.exceptions.FailedKillAllException;
import nohup.exceptions.FailedKillException;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO documentation
 * Created by mibaye on 17/08/2016.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class NohupProcess implements Runnable {

    /**
     * TODO documentation
     */
    public enum Status {
        RUNNING("RUNNING"),
        NOT_RUNNING("NOT_RUNNING");

        private String status;

        Status(String status) {
            this.status = status;
        }

        String getStatus() {
            return this.status;
        }
    }

    @JsonIgnore
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NohupProcess.class);
    @JsonProperty
    private String id;
    @JsonProperty
    private String command;
    @JsonProperty
    private List<String> parameters;
    @JsonIgnore
    private Process process;
    @JsonIgnore
    private Thread thread;
    @JsonProperty
    private List<String> standardOutput;
    @JsonProperty
    private List<String> errorOutput;
    @JsonProperty
    private Status status;
    @JsonProperty
    private List<String> aliases;

    /**
     * TODO documentation
     * @param command
     * @param parameters
     */
    public NohupProcess(String command, List<String> parameters, String alias) {
        this.id = UUID.randomUUID().toString();
        this.command = command;
        this.parameters = parameters;
        this.standardOutput = new ArrayList<>();
        this.errorOutput = new ArrayList<>();
        this.status = Status.NOT_RUNNING;
        this.aliases = new ArrayList<>();
        this.aliases.add(alias);
    }

    /**
     * TODO documentation
     */
    @Override
    public void run() {

        List<String> fullCommand = new ArrayList<>();
        fullCommand.add(command);
        fullCommand.addAll(parameters);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(fullCommand);
            process = processBuilder.start();
            status = Status.RUNNING;
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to run nohup process : {}", this.toString()), e);
        }
    }

    /**
     * TODO documentation
     * @return
     */
    public boolean tail() {

        boolean ret = false;
        try {
            standardOutput.addAll(readInputStream(process.getInputStream()));
            errorOutput.addAll(readInputStream(process.getErrorStream()));
            ret = true;
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to tail : {}", this.toString()), e);
        }

        return ret;
    }

    /**
     * TODO documentation
     * @param inputStream
     * @return
     */
    private List<String> readInputStream(InputStream inputStream) {

        List<String> ret = new ArrayList<>();
        try {
            byte[] bytes = new byte[256];
            String line;
            StringTokenizer st;
            while (inputStream.available() > 0) {
                inputStream.read(bytes);
                line = new String(bytes);
                st = new StringTokenizer(line, "\r\n");
                while (st.hasMoreTokens()) {
                    ret.add(st.nextToken());
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to read inputstream : {}", this.toString()), e);
        }
        return ret;
    }

    /**
     * TODO documentation
     * @return
     */
    public void kill() throws FailedKillException {
        try {
            process.destroy();
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to kill process : {}", this.toString()), e);
            throw new FailedKillException(String.format("Failed to kill process : {}", this.toString()), e);
        }
    }

    /**
     * TODO documentation
     * @return
     */
    public List<String> getStandardOutput() {
        return standardOutput;
    }

    /**
     * TODO documentation
     * @param standardOutput
     */
    public void setStandardOutput(List<String> standardOutput) {
        this.standardOutput = standardOutput;
    }

    /**
     * TODO documentation
     * @return
     */
    public List<String> getErrorOutput() {
        return errorOutput;
    }

    /**
     * TODO documentation
     * @param errorOutput
     */
    public void setErrorOutput(List<String> errorOutput) {
        this.errorOutput = errorOutput;
    }

    /**
     * TODO documentation
     * @return
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * TODO documentation
     * @param thread
     */
    public void setThread(Thread thread) {
        this.thread = thread;
    }

    /**
     * TODO documentation
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * TODO documentation
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * TODO documentation
     * @return
     */
    public Status getStatus() {
        if (process == null || !process.isAlive()) {
            status = Status.NOT_RUNNING;
        } else {
            status = Status.RUNNING;
        }
        return status;
    }

    /**
     * TODO documentation
     * @param status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * TODO documentation
     * @return
     */
    public String getCommand() {
        return command;
    }

    /**
     * TODO documentation
     * @param command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * TODO documentation
     * @return
     */
    public List<String> getParameters() {
        return parameters;
    }

    /**
     * TODO documentation
     * @param parameters
     */
    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    /**
     * TODO Documentation
     * @return
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * TODO documentation
     * @param aliases
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    @Override
    public String toString() {
        return "NohupProcess{" +
                "id='" + id + '\'' +
                ",command='" + command + '\'' +
                ",parameters='" + parameters.toString() + '\'' +
                '}';
    }
}
