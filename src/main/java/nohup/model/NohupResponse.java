package nohup.model;

/**
 * TODO documentation
 * Created by mibaye on 17/08/2016.
 */
public class NohupResponse {

    private String status;
    private NohupProcess process;

    /**
     * TODO documentation
     * @return
     */
    public NohupProcess getProcess() {
        return process;
    }

    /**
     * TODO documentation
     * @param process
     */
    public void setProcess(NohupProcess process) {
        this.process = process;
    }

    /**
     * TODO documentation
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * TODO documentation
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {

        String processId;
        if (process == null) {
            processId = null;
        } else {
            processId = process.getId();
        }

        return "NohupResponse{" +
                "status='" + status + '\'' +
                ", process='" + processId + '\'' +
                '}';
    }
}
