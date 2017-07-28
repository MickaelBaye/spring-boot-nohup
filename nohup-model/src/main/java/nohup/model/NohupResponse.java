package nohup.model;

/**
 * TODO documentation
 * Created by mibaye on 17/08/2016.
 */
public class NohupResponse {

    /**
     * TODO documentation
     */
    public enum Status {
        OK("OK"), KO("KO");
        String status;
        Status(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    private Status status;
    private String message;
    private NohupProcess process;

    /**
     * Default constructor
     */
    public NohupResponse() {}

    /**
     * TODO documentation
     * @param status
     * @param message
     */
    public NohupResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

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
    public Status getStatus() {
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
    public String getMessage() {
        return message;
    }

    /**
     * TODO documentation
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
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
                "status='" + status.toString() + '\'' +
                ", process='" + processId + '\'' +
                '}';
    }
}
