package nohup.services;

import nohup.model.NohupMonitoringTrace;
import nohup.model.NohupRequest;
import nohup.model.NohupResponse;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * TODO documentation
 * Created by mibaye on 31/07/2017.
 */
@Service
public class NohupMonitoring {

    private static final Logger LOGGER = LoggerFactory.getLogger(NohupMonitoring.class);

    /**
     * TODO documentation
     * @param dateTime
     * @param messageId
     * @param message
     * @param endpoint
     * @param status
     * @param nohupRequest
     */
    public void monitor(DateTime dateTime, String messageId, String message, String endpoint, NohupResponse.Status status, NohupRequest nohupRequest) {

        NohupMonitoringTrace nohupMonitoringTrace = new NohupMonitoringTrace();

        nohupMonitoringTrace.setDateTime(dateTime);
        nohupMonitoringTrace.setMessageId(messageId);
        nohupMonitoringTrace.setMessage(message);
        nohupMonitoringTrace.setEndpoint(endpoint);
        nohupMonitoringTrace.setStatus(status);
        nohupMonitoringTrace.setNohupRequest(nohupRequest);

        if (NohupResponse.Status.OK.equals(status)) {
            LOGGER.info(nohupMonitoringTrace.toString());
        } else {
            LOGGER.error(nohupMonitoringTrace.toString());
        }
    }
}
