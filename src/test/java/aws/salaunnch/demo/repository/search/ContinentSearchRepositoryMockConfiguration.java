package aws.salaunnch.demo.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ContinentSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ContinentSearchRepositoryMockConfiguration {

    @MockBean
    private ContinentSearchRepository mockContinentSearchRepository;

}
