package org.egov.demand.repository.demanddetailvariation;

import org.egov.demand.model.EgDemandDetailVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandDetailVariationRepository extends JpaRepository<EgDemandDetailVariation,Long> {

}
