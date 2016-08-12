package org.egov.stms.transactions.service;

import java.util.List;

import org.egov.stms.masters.entity.FeesDetailMaster;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.transactions.repository.SewerageConnectionFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageConnectionFeeService {
    
    protected SewerageConnectionFeeRepository sewerageConnectionFeeRepository;
    
    @Autowired
    public SewerageConnectionFeeService(
            final SewerageConnectionFeeRepository sewerageConnectionFeeRepository) {
        this.sewerageConnectionFeeRepository = sewerageConnectionFeeRepository;
    }
        
    public List<SewerageConnectionFee> findAllByApplicationDetailsAndFeesDetail(SewerageApplicationDetails sewerageApplicationDetails,
            FeesDetailMaster feesDetailMaster){
        return sewerageConnectionFeeRepository.findAllByApplicationDetailsAndFeesDetail(sewerageApplicationDetails, feesDetailMaster);
    }
    

}
