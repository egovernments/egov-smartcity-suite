package org.egov.egf.autonumber;

import org.egov.commons.CVoucherHeader;
import org.springframework.stereotype.Service;
@Service
public interface VouchernumberGenerator {
	public String getNextNumber(CVoucherHeader vh);

}
