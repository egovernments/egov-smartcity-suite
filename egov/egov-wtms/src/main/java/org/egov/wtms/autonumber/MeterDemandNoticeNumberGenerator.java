package org.egov.wtms.autonumber;

import org.springframework.stereotype.Service;

@Service
public interface MeterDemandNoticeNumberGenerator {

    public String generateMeterDemandNoticeNumber();
}
