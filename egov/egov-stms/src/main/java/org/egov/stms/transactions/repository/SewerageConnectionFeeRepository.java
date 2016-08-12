package org.egov.stms.transactions.repository;

import java.util.List;

import org.egov.stms.masters.entity.FeesDetailMaster;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SewerageConnectionFeeRepository extends JpaRepository<SewerageConnectionFee, Long>{
    
    List<SewerageConnectionFee> findAllByApplicationDetailsAndFeesDetail(SewerageApplicationDetails sewerageApplicationDetails,
            FeesDetailMaster feesDetailMaster);

}
