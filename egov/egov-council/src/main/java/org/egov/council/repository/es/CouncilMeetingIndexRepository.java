package org.egov.council.repository.es;

import org.egov.council.entity.es.CouncilMeetingIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouncilMeetingIndexRepository extends ElasticsearchRepository<CouncilMeetingIndex, String>{

}
