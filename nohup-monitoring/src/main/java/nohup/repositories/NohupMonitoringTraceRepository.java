package nohup.repositories;

import nohup.model.NohupMonitoringTrace;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * TODO documentation
 * Created by mibaye on 03/08/2017.
 */
@Repository
public interface NohupMonitoringTraceRepository extends ElasticsearchRepository<NohupMonitoringTrace, Long> {
}
