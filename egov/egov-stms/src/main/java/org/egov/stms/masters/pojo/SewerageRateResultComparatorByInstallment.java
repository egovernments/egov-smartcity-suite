package org.egov.stms.masters.pojo;

import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class SewerageRateResultComparatorByInstallment implements Comparator<SewerageRateDCBResult>{

    @Override
    public int compare(final SewerageRateDCBResult s1, final SewerageRateDCBResult s2){
        return s1.getInstallmentYearId().compareTo(s2.getInstallmentYearId());
    }
}
