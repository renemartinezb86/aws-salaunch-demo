package aws.salaunnch.demo.repository.search;

import aws.salaunnch.demo.domain.Marine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Marine} entity.
 */
public interface MarineSearchRepository extends ElasticsearchRepository<Marine, Long> {
}
