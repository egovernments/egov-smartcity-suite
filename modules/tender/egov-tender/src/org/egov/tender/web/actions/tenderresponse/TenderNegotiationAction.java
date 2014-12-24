package org.egov.tender.web.actions.tenderresponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bidder;
import org.egov.commons.EgwStatus;
import org.egov.infstr.models.State;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderResponseLine;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.utils.TenderConstants;
import org.egov.tender.utils.TenderUtils;

/**
 * 
 * @author pritiranjan
 *
 */

public class TenderNegotiationAction extends ResponseAction{

	private static final long serialVersionUID = 1L;
	private Long responseId;
	private EgwStatus tempStatus;
	private Map<String,Object> negotiationNoticeMap=new HashMap<String,Object>();
	private ReportService reportService;
	private Integer reportId = -1;
	public TenderNegotiationAction()
	{
		addRelatedEntity("status", EgwStatus.class);
		addRelatedEntity("createdBy", UserImpl.class);
		addRelatedEntity("modifiedBy", UserImpl.class);
		addRelatedEntity("state", State.class);
		addRelatedEntity("tenderUnit",TenderUnit.class);
		addRelatedEntity("parent",GenericTenderResponse.class);
	}
	
	public void prepare()
	{
		super.prepare();
		if(responseId!=null){
			tenderResponse=tenderResponseService.getTenderResponseById(responseId);
		}
		addDropdownData("tenderFileTypeList",persistenceService.findAllBy("from TenderFileType where isActive=? order by fileType ",Boolean.TRUE));
		addDropdownData("statusList",persistenceService.findAllBy("select distinct status from EgwStatus status where status.moduletype=? and (status.code = ? or status.code =? or status.code=?)"
				,TenderConstants.TENDERRESPONSEMODULE,TenderConstants.TENDERRESPONSE_NEGOTIATED ,
				TenderConstants.TENDERRESPONSE_RENEGOTIATED,TenderConstants.TENDERRESPONSE_ACCEPTED));
		tenderBidderType = tenderResponseService.setTenderTypeAndBidderName(tenderResponse);
		addDropdownData("bidderList",tenderResponseService.populateBidderList(tenderResponse));
	}
	
	public String load()
	{
		if(responseId!=null){
			tenderResponse=tenderResponseService.getLatestNegotiation(responseId);
		}
		tempStatus=tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERRESPONSEMODULE, TenderConstants.TENDERRESPONSE_NEGOTIATED);
		buildTenderBeforeViewAndModify();
		setMode(NEW);
		return NEW;
	}
	
	public void buildTenderBeforeViewAndModify()
	{
		responseLineList=new ArrayList<TenderResponseLine>(tenderResponse.getResponseLines());
		if(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)!=null && 
				TenderConstants.TENDER_OWNER.equals(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)))
			tenderResponse.setOwner(tenderCommonService.getOwnerById(tenderResponse.getBidderId().intValue()));
		setTenderType(TenderUtils.initCapString(tenderResponse.getBidType().name()));
		
		if(tenderResponse.getPercentage()!=null && BigDecimal.ZERO.compareTo(tenderResponse.getPercentage())>0)
			setSign("-");
		else 
			setSign("+");
	}
	
	public String view()
	{
		buildTenderBeforeViewAndModify();
		tempStatus=tenderResponse.getStatus();
		setMode(TenderConstants.TENDERRESPONSE_RENEGOTIATED.equals(tenderResponse.getStatus().getCode()) ? VIEW:MODIFY );
		return NEW;
	}
	
	public String create()
	{
		buildTenderResponse();
		tenderResponseService.saveNegotiation(tenderResponse);
		tempStatus=tenderResponse.getStatus();
		setMode(VIEW);
		return NEW;
	}

	@SkipValidation
	public String print()
	{
		negotiationNoticeMap = new HashMap<String,Object>();
		tenderResponse = tenderResponseService.getTenderResponseById(responseId);
		getMapByPassingModel(); 
		List<GenericTenderResponse> list=new ArrayList<GenericTenderResponse>();
		list.add(tenderResponse);
		ReportRequest reportInput = new ReportRequest(TenderConstants.NEGOTIATIONREPORT,list,negotiationNoticeMap);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		reportId = ReportViewerUtil.addReportToSession(reportOutput,getSession());
		return "report";
	}
	
	/**
	 * This method will generate the map for jasper
	 * 
	 */
	
	@SkipValidation
	public void getMapByPassingModel()
	{
		negotiationNoticeMap = new HashMap<String,Object>();
		negotiationNoticeMap.put("DEPARTMENTNAME",tenderResponse.getNotice().getDepartment().getDeptName());
		negotiationNoticeMap.put("RESPONSENO",tenderResponse.getNumber());
		negotiationNoticeMap.put("DATE",DateUtils.getFormattedDate(new Date(), TenderConstants.DATEPATTERN));
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		negotiationNoticeMap.put("NameOfTheCity",session.getAttribute("cityname"));
		negotiationNoticeMap.put("logoPath",tenderCommonService.getCityLogoName(request));
		Bidder bidder;
		String address="";
		
		if(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)!=null && 
				TenderConstants.TENDER_OWNER.equals(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0))){
			bidder  = tenderCommonService.getOwnerById(tenderResponse.getBidderId().intValue());
			address = tenderResponse.getBidderAddress().getStreetAddress1();
		}
		else{
			bidder= tenderResponse.getBidder();
			address=bidder.getAddress();
		}
		
		negotiationNoticeMap.put("BIDDER",bidder.getName());
		negotiationNoticeMap.put("ADDRESS",address);
		negotiationNoticeMap.put("TENDERNOTICENO",tenderResponse.getNotice().getNumber());
		negotiationNoticeMap.put("TENDERNO",tenderResponse.getTenderUnit().getTenderUnitNumber());
	}
	
	public GenericTenderResponse getModel() {
		return tenderResponse;
	}

	public Long getResponseId() {
		return responseId;
	}
	
	public void setResponseId(Long responseId) {
		this.responseId = responseId;
	}

	public EgwStatus getTempStatus() {
		return tempStatus;
	}
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}
}
