package nohup.model;

import org.joda.time.DateTime;

import java.util.List;

/**
 * TODO documentation
 * Created by mibaye on 17/08/2016.
 */
public class NohupRequest {

    private String id;
    private String command;
    private List<String> parameters;
    private String alias;

    /**
     * TODO documentation
     * @return
     */
    public String getAlias() {
        return alias;
    }

    /**
     * TODO documentation
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
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

    @Override
    public String toString() {
        return "NohupRequest{" +
                "id='" + id + '\'' +
                ", command='" + command + '\'' +
                ", parameters=" + parameters +
                ", alias='" + alias + '\'' +
                '}';
    }

}
