package org.egov.tender.web.actions.tendernotice;

import java.util.ArrayList;
import java.util.List;

import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.services.tendernotice.TenderNoticeService;
import org.egov.tender.services.tenderresponse.TenderResponseService;
import org.egov.web.actions.BaseFormAction;

public class AjaxTenderNoticeAction extends BaseFormAction{

	
	
	private static final String TENDERNOTICENUMBERUNIQUECHECK="uniqueCheck";
	private String tenderNoticeNumber;
	private TenderNoticeService  tenderNoticeService;
	private Long id;
	private TenderResponseService responseService;
	private Long noticeId;
	List <GenericTenderResponse> approvedBidResponseList = new ArrayList<GenericTenderResponse>();
	public Object getModel()
	{
		return null;
	}
	
	public String tenderNoticeNumberUniqueCheck()
	{
		return TENDERNOTICENUMBERUNIQUECHECK;
	}
	
	public boolean getTenderNoticeNumberUniqueCheckByTenderNumber()
	{
		return tenderNoticeService.getTenderNoticeNumberUniqueCheckByTenderNumber(tenderNoticeNumber, id);

	}
	
	public String getApprovedBidResponse() {		
		approvedBidResponseList=persistenceService.findAllBy("from GenericTenderResponse resp where resp.tenderUnit.id in (select tu.id from TenderUnit tu where tu.tenderNotice.id in(select tn.id from TenderNotice tn where tn.id=?)) and resp.status.code<>'Cancelled'", noticeId);
		return "approvedBidResponses";
	}
	
	public String getTenderNoticeNumber() {
		return tenderNoticeNumber;
	}

	public void setTenderNoticeNumber(String tenderNoticeNumber) {
		this.tenderNoticeNumber = tenderNoticeNumber;
	}

	public TenderNoticeService getTenderNoticeService() {
		return tenderNoticeService;
	}

	public void setTenderNoticeService(TenderNoticeService tenderNoticeService) {
		this.tenderNoticeService = tenderNoticeService;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<GenericTenderResponse> getApprovedBidResponseList() {
		return approvedBidResponseList;
	}

	public void setApprovedBidResponseList(
			List<GenericTenderResponse> approvedBidResponseList) {
		this.approvedBidResponseList = approvedBidResponseList;
	}

	public void setResponseService(TenderResponseService responseService) {
		this.responseService = responseService;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}
	

	
}