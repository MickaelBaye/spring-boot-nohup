package nohup;

/**
 * Created by mibaye on 17/08/2016.
 */
public class NohupResponse {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NohupResponse{" +
                "status='" + status + '\'' +
                '}';
    }
}
