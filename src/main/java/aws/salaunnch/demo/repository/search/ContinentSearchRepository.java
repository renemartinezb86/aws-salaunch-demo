package aws.salaunnch.demo.repository.search;

import aws.salaunnch.demo.domain.Continent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Continent} entity.
 */
public interface ContinentSearchRepository extends ElasticsearchRepository<Continent, Long> {
}
