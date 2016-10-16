package org.egov.pgr.repository.elasticsearch;

import org.egov.pgr.entity.elasticsearch.ComplaintIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintIndexRepository extends ElasticsearchRepository<ComplaintIndex, String>,ComplaintIndexCustomRepository {

}