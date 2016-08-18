package nohup;

/**
 * Created by mibaye on 17/08/2016.
 */
public class NohupResponse {

    private String status;
    private NohupProcess process;

    public NohupProcess getProcess() {
        return process;
    }

    public void setProcess(NohupProcess process) {
        this.process = process;
    }

    public String getStatus() {
        return status;
    }

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
