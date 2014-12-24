package org.egov.works.services.qualityControl.impl;

import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.qualityControl.CoveringLetterNumberGenerator;
import org.egov.works.models.qualityControl.SampleLetterHeader;
import org.egov.works.models.qualityControl.SampleLetterNumberGenerator;
import org.egov.works.services.impl.BaseServiceImpl;
import org.egov.works.services.qualityControl.SampleLetterService;

public class SampleLetterServiceImpl extends BaseServiceImpl<SampleLetterHeader, Long> implements SampleLetterService{
	
	private CommonsService commonsService;
	private SampleLetterNumberGenerator sampleLetterNumberGenerator;
	private CoveringLetterNumberGenerator coveringLetterNumberGenerator; 

	public SampleLetterServiceImpl(
			PersistenceService<SampleLetterHeader, Long> persistenceService) {
		super(persistenceService);
		// TODO Auto-generated constructor stub
	}
	
	public String generateSampleLetterNumber(SampleLetterHeader sampleLetterHeader){
		CFinancialYear financialYear=commonsService.getFinancialYearByDate(sampleLetterHeader.getSampleLetterDate());
		return sampleLetterNumberGenerator.getSampleLetterNumber(sampleLetterHeader, financialYear);
	}
	
	public String generateCoveringLetterNumber(SampleLetterHeader sampleLetterHeader){
		CFinancialYear financialYear=commonsService.getFinancialYearByDate(sampleLetterHeader.getSampleLetterDate());
		return coveringLetterNumberGenerator.getCoveringLetterNumber(sampleLetterHeader, financialYear);
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setSampleLetterNumberGenerator(
			SampleLetterNumberGenerator sampleLetterNumberGenerator) {
		this.sampleLetterNumberGenerator = sampleLetterNumberGenerator;
	}

	public void setCoveringLetterNumberGenerator(
			CoveringLetterNumberGenerator coveringLetterNumberGenerator) {
		this.coveringLetterNumberGenerator = coveringLetterNumberGenerator;
	}

}
