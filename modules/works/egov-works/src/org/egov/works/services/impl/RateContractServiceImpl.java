package org.egov.works.services.impl;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.services.PersistenceService;
import org.egov.tender.TenderableType;
import org.egov.tender.model.TenderResponseLine;
import org.egov.works.models.rateContract.Indent;
import org.egov.works.models.rateContract.IndentDetail;
import org.egov.works.models.rateContract.RateContract;
import org.egov.works.models.rateContract.RateContractDetail;
import org.egov.works.models.rateContract.RateContractNumberGenerator;
import org.egov.works.services.RateContractService;

public class RateContractServiceImpl  extends BaseServiceImpl<RateContract,Long> implements RateContractService{
	
	private RateContractNumberGenerator rateContractNumberGenerator;
	public RateContractServiceImpl(PersistenceService<RateContract, Long> persistenceService) {
		super(persistenceService);
	}
	
	public void setRateContractNumber(RateContract entity, CFinancialYear finYear) {
		if(entity.getRcNumber() == null) {
			entity.setRcNumber(rateContractNumberGenerator.getRateContractNumber(entity, finYear));
		}
	}

	public void setRateContractNumberGenerator(
			RateContractNumberGenerator rateContractNumberGenerator) {
			this.rateContractNumberGenerator = rateContractNumberGenerator;
	}

	public IndentDetail getActivityFromRateContractDetailAndIndent(RateContractDetail rcDetail,Indent indent){
		IndentDetail indentDetail=null;
		if(rcDetail.getSorNumber().contains("^"))
		{
			String[] SOR_number=rcDetail.getSorNumber().split("\\^");
			indentDetail=(IndentDetail) genericService.find("from IndentDetail detail where detail.indent.id=? and detail.scheduleOfRate.category.code=? and detail.scheduleOfRate.code=?", indent.getId(),SOR_number[0],SOR_number[1]);
		}
		else 
		{
			indentDetail=(IndentDetail) genericService.find("from IndentDetail detail where detail.indent.id=? and detail.nonSor.id=?",indent.getId()
					,new Long(rcDetail.getSorNumber()));
		}
		return indentDetail;
	}
}

