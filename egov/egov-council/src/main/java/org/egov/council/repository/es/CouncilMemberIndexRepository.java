package org.egov.council.repository.es;

import org.egov.council.entity.es.CouncilMemberIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouncilMemberIndexRepository extends ElasticsearchRepository<CouncilMemberIndex, String>{

}
