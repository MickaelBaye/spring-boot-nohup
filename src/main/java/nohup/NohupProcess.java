package nohup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public NohupProcess(String command, List<String> parameters) {
        this.id = UUID.randomUUID().toString();
        this.command = command;
        this.parameters = parameters;
    }

    @Override
    public void run() {

        List<String> fullCommand = new ArrayList<>();
        fullCommand.add(command);
        fullCommand.addAll(parameters);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(fullCommand);
            process = processBuilder.start();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to run nohup process : " + this.toString(), e);
        }
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
