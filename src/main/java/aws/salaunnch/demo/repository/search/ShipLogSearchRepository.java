package aws.salaunnch.demo.repository.search;

import aws.salaunnch.demo.domain.ShipLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ShipLog} entity.
 */
public interface ShipLogSearchRepository extends ElasticsearchRepository<ShipLog, Long> {
}
