package org.egov.works.services.qualityControl;

import org.egov.works.models.qualityControl.SampleLetterHeader;
import org.egov.works.services.BaseService;

public interface SampleLetterService  extends BaseService<SampleLetterHeader,Long> {
	public String generateSampleLetterNumber(SampleLetterHeader sampleLetterHeader);
	
	public String generateCoveringLetterNumber(SampleLetterHeader sampleLetterHeader);
}
