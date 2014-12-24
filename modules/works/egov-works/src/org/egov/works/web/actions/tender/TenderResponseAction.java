package org.egov.works.web.actions.tender;


import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.services.common.DefaultTenderService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.masters.Contractor;
import org.egov.works.services.ContractorService;



@ParentPackage("egov")
@Result(name= TenderResponseAction.WORKORDER, location="workOrder", type="redirect", 
params = {"namespace", "/workorder", "method", TenderResponseAction.NEWFORM})

public class TenderResponseAction extends BaseFormAction {

	private static final Logger   LOGGER = Logger.getLogger(TenderResponseAction.class);
	protected static final String WORKORDER = "workorder" ;
	protected static final String NEWFORM   = "newform" ;
	private ContractorService contractorBidderService;
	private DefaultTenderService defaultTenderService;
	private String noticeNumber;
	private String contractorCode;
	private BigDecimal percentage;
	private Boolean validateFlag;


	public String newform()
	{
		Contractor bidder =  contractorBidderService.getBidderByCode(contractorCode);
		GenericTenderResponse response =  defaultTenderService.saveResponse(noticeNumber, bidder, percentage);
		getSession().put("tenderRespId", response.getId());
		return WORKORDER;
	}


	public String validateReposeForContractor()
	{
		Contractor bidder =  contractorBidderService.getBidderByCode(contractorCode);
		TenderNotice notice = defaultTenderService.getTenderNoticeByNumber(noticeNumber);
		validateFlag = Boolean.FALSE;

		if(null != notice && null != bidder){
			for(TenderUnit unit:notice.getTenderUnits()){
				validateFlag = defaultTenderService.checkUniqueResponseForBidder(bidder.getId(), 
						notice.getTenderFileType().getBidderType(), unit);
			}
		}

		LOGGER.info("::validate response For Contractor flag::"+validateFlag);
		return "contractorFlag";
	}


	public String getNoticeNumber() {
		return noticeNumber;
	}


	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}


	public String getContractorCode() {
		return contractorCode;
	}


	public void setContractorCode(String contractorCode) {
		this.contractorCode = contractorCode;
	}


	public BigDecimal getPercentage() {
		return percentage;
	}


	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}


	@Override
	public GenericTenderResponse getModel() {
		return null;
	}


	public void setContractorBidderService(ContractorService contractorBidderService) {
		this.contractorBidderService = contractorBidderService;
	}


	public Boolean getValidateFlag() {
		return validateFlag;
	}


	public void setDefaultTenderService(DefaultTenderService defaultTenderService) {
		this.defaultTenderService = defaultTenderService;
	}



}
