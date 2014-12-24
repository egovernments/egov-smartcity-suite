package org.egov.works.services;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.rateContract.Indent;
import org.egov.works.models.tender.TenderFile;

public interface TenderFileService extends BaseService<TenderFile,Long>, org.egov.tender.services.common.TenderFileService, org.egov.tender.services.common.TenderService{
	public void setTenderFileNumber(TenderFile entity,CFinancialYear finYear);	
	public List<AbstractEstimate> getAbstractEstimateListByTenderFile(TenderFile entity);
	public List<Indent> getIndentListByTenderFile(TenderFile entity);
}
