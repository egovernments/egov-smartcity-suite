package org.egov.wtms.autonumber.impl;

import java.io.Serializable;

import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.wtms.autonumber.BillReferenceNumberGenerator;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillReferenceNumberGeneratorImpl implements BillReferenceNumberGenerator {

    @Autowired
    private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Override
    public String generateBillNumber(final String installmentYear) {
        final String sequenceName = WaterTaxConstants.WATER_CONN_BILLNO_SEQ + installmentYear;
        final Serializable nextSequence = applicationSequenceNumberGenerator.getNextSequence(sequenceName);
        return String.format("%s%06d", waterTaxUtils.getCityCode(), nextSequence);
    }

}
