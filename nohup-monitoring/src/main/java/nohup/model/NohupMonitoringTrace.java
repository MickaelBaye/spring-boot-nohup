package nohup.model;

import org.joda.time.DateTime;

/**
 * TODO documentation
 * Created by mibaye on 31/07/2017.
 */
public class NohupMonitoringTrace {

    private DateTime dateTime;
    private String messageId;
    private String message;
    private String endpoint;
    private NohupResponse.Status status;
    private NohupRequest nohupRequest;

    /**
     * TODO documentation
     * @return
     */
    public DateTime getDateTime() {
        return dateTime;
    }

    /**
     * TODO documentation
     * @param dateTime
     */
    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * TODO documentation
     * @return
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * TODO documentation
     * @param messageId
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    /**
     * TODO documentation
     * @return
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * TODO documentation
     * @param endpoint
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public NohupResponse.Status getStatus() {
        return status;
    }

    public void setStatus(NohupResponse.Status status) {
        this.status = status;
    }

    public NohupRequest getNohupRequest() {
        return nohupRequest;
    }

    /**
     * TODO documentation
     * @param nohupRequest
     */
    public void setNohupRequest(NohupRequest nohupRequest) {
        this.nohupRequest = nohupRequest;
    }

    @Override
    public String toString() {
        return "NohupMonitoringTrace{" +
                "dateTime=" + dateTime +
                ", messageId='" + messageId + '\'' +
                ", message='" + message + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", status=" + status +
                ", nohupRequest=" + nohupRequest +
                '}';
    }
}
