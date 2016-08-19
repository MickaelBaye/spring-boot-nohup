package nohup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mibaye on 17/08/2016.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class NohupProcess implements Runnable {

    public enum Status {
        RUNNING ("RUNNING"),
        NOT_RUNNING ("NOT_RUNNING");

        private String status;

        Status(String status) {
            this.status = status;
        }

        String getStatus() {
            return this.status;
        }
    }

    @JsonIgnore
    private static Logger logger = Logger.getLogger(NohupProcess.class.getName());
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

    public NohupProcess(String command, List<String> parameters) {
        this.id = UUID.randomUUID().toString();
        this.command = command;
        this.parameters = parameters;
        this.standardOutput = new ArrayList<>();
        this.errorOutput = new ArrayList<>();
        this.status = Status.NOT_RUNNING;
    }

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
            logger.log(Level.SEVERE, "Failed to run nohup process : " + this.toString(), e);
        }
    }

    public boolean tail() {

        boolean ret = false;
        try {
            standardOutput = readInputStream(process.getInputStream());
            errorOutput = readInputStream(process.getErrorStream());
            ret = true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to tail : " + this.toString());
        }

        return ret;
    }

    private List<String> readInputStream(InputStream inputStream) {

        List<String> ret = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();
            while (line != null) {
                ret.add(line);
                line = br.readLine();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to read inputstream : " + this.toString());
        }
        return ret;
    }

    public boolean interrupt() {
        boolean ret = false;
        try {
            process.destroy();
            ret = true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to interrupt nohup process : " +  this.toString(), e);
        }
        return ret;
    }

    public List<String> getStandardOutput() {
        return standardOutput;
    }

    public void setStandardOutput(List<String> standardOutput) {
        this.standardOutput = standardOutput;
    }

    public List<String> getErrorOutput() {
        return errorOutput;
    }

    public void setErrorOutput(List<String> errorOutput) {
        this.errorOutput = errorOutput;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        if (process == null || !process.isAlive()) {
            status = Status.NOT_RUNNING;
        } else {
            status = Status.RUNNING;
        }
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
