package nohup.services;

import nohup.model.NohupMonitoringTrace;
import nohup.model.NohupRequest;
import nohup.model.NohupResponse;
import nohup.repositories.NohupMonitoringTraceRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * TODO documentation
 * Created by mibaye on 31/07/2017.
 */
@Service
public class NohupMonitoring {

    private static final Logger LOGGER = LoggerFactory.getLogger(NohupMonitoring.class);

    /*@Autowired
    private ElasticsearchTemplate elasticsearchTemplate;*/

    @Autowired
    private NohupMonitoringTraceRepository repository;

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

        nohupMonitoringTrace.setDateTime(dateTime.toDate());
        nohupMonitoringTrace.setMessageId(messageId);
        nohupMonitoringTrace.setMessage(message);
        nohupMonitoringTrace.setEndpoint(endpoint);
        nohupMonitoringTrace.setStatus(status);
        nohupMonitoringTrace.setNohupRequest(nohupRequest);

        // Logging in nohup-monitoring.log
        if (NohupResponse.Status.OK.equals(status)) {
            LOGGER.info(nohupMonitoringTrace.toString());
        } else {
            LOGGER.error(nohupMonitoringTrace.toString());
        }

        // Save in ElasticSearch
        try {
            /*IndexQuery indexQuery = new IndexQueryBuilder().withId(nohupMonitoringTrace.getId()).withObject(nohupMonitoringTrace).build();
            elasticsearchTemplate.index(indexQuery);*/
            repository.save(nohupMonitoringTrace);
            LOGGER.info("{} saved", nohupMonitoringTrace.toString());
        } catch (Exception e) {
            LOGGER.error("Failed to save trace into ElasticSearch", e);
        }
    }
}
