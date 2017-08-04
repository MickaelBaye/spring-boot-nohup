package nohup.model;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.UUID;

/**
 * TODO documentation
 * Created by mibaye on 31/07/2017.
 */
@Document(indexName = "nohup", type = "trace")
public class NohupMonitoringTrace {

    @Id
    private Long id;
    @Field(type = FieldType.Date)
    private Date dateTime;
    @Field(type = FieldType.keyword)
    private String messageId;
    @Field(type = FieldType.text)
    private String message;
    @Field(type = FieldType.keyword)
    private String endpoint;
    @Field(type = FieldType.keyword)
    private NohupResponse.Status status;
    @Field(type = FieldType.Nested)
    private NohupRequest nohupRequest;

    /**
     * Default constructor
     */
    public NohupMonitoringTrace() {
        this.id = System.currentTimeMillis();
    }

    /**
     * TODO Documentation
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * TODO documentation
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * TODO documentation
     * @return
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * TODO documentation
     * @param dateTime
     */
    public void setDateTime(Date dateTime) {
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
