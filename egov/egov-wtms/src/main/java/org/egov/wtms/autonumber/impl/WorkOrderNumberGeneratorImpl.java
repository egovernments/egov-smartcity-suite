package org.egov.wtms.autonumber.impl;

import java.io.Serializable;

import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.wtms.autonumber.WorkOrderNumberGenerator;
import org.egov.wtms.utils.WaterTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderNumberGeneratorImpl implements WorkOrderNumberGenerator {
    private static final String WORKORDER_NUMBER_SEQ_PREFIX = "SEQ_WORKORDER_NUMBER";
    @Autowired
    private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Override
    public String generateWorkOrderNumber() {
        final String sequenceName = WORKORDER_NUMBER_SEQ_PREFIX;
        final Serializable nextSequence = applicationSequenceNumberGenerator.getNextSequence(sequenceName);
        return String.format("%s%06d", waterTaxUtils.getCityCode(), nextSequence);
    }

}
