package nohup;

import java.util.List;

/**
 * Created by mibaye on 17/08/2016.
 */
public class NohupRequest {

    private String command;
    private List<String> parameters;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "NohupRequest{" +
                "command='" + command + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
