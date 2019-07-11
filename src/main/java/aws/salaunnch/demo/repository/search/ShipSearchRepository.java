package aws.salaunnch.demo.repository.search;

import aws.salaunnch.demo.domain.Ship;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Ship} entity.
 */
public interface ShipSearchRepository extends ElasticsearchRepository<Ship, Long> {
}
