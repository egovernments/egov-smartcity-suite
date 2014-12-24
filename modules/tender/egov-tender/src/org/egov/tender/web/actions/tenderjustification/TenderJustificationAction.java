package org.egov.tender.web.actions.tenderjustification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bidder;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.DateUtils;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderJustification;
import org.egov.tender.model.TenderResponseLine;
import org.egov.tender.services.tenderjustification.TenderJustificationService;
import org.egov.tender.services.tenderresponse.TenderResponseService;
import org.egov.tender.utils.TenderConstants;
import org.egov.tender.utils.TenderUtils;
import org.egov.tender.web.actions.common.TenderWorkFlowAction;

/**
 * 
 * @author pritiranjan
 *
 */

@SuppressWarnings("serial")
@ParentPackage("egov")
public class TenderJustificationAction extends TenderWorkFlowAction{

	private static final String REPORT="report";
	private TenderJustification tenderJustification = new TenderJustification();
	private TenderResponseService tenderResponseService;
	private TenderJustificationService tenderJustificationService;
	private Long responseId;
	private Map<String,List<String>> tenderBidderType = new HashMap<String,List<String>>();
	private List<TenderResponseLine> responseLineList=new ArrayList<TenderResponseLine>();
	private GenericTenderResponse tenderResponseTemp;
	private String tenderType;
	private ReportService reportService;
	private Integer reportId = -1;
    private String sign;

	public TenderJustificationAction()
	{
		addRelatedEntity("tenderResponse",GenericTenderResponse.class);
	}
	
	@SuppressWarnings("unchecked")
	public void prepare()
	{
		super.prepare();
		if(responseId!=null){
			tenderResponseTemp = tenderResponseService.getLatestNegotiation(responseId);
			tenderJustification.setTenderResponse(tenderResponseTemp);
		}
		else if(idTemp != null)
			tenderJustification = tenderJustificationService.getTenderJustificationById(idTemp);
		addDropdownData("tenderFileTypeList",persistenceService.findAllBy("from TenderFileType where isActive=? order by fileType ",Boolean.TRUE));
		addDropdownData("statusList",persistenceService.findAllByNamedQuery(TenderConstants.GETALLSTATUSINTENDER,
				TenderConstants.TENDERRESPONSEMODULE,(List<String>)scriptService.executeScript(TenderConstants.TENDERRESPONSE_STATUSSCRIPT, ScriptService.createContext("response",tenderJustification.getTenderResponse(),"mode",mode,"responseService",tenderResponseService))));
		tenderBidderType = tenderResponseService.setTenderTypeAndBidderName(tenderJustification.getTenderResponse());
		addDropdownData("bidderList",tenderResponseService.populateBidderList(tenderJustification.getTenderResponse()));
		responseLineList=new ArrayList<TenderResponseLine>(tenderJustification.getTenderResponse().getResponseLines());
	}
	
	private void buildTenderBeforeViewAndModify()
	{
		if(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)!=null && 
				TenderConstants.TENDER_OWNER.equals(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)))
			tenderResponseTemp.setOwner(tenderCommonService.getOwnerById(tenderResponseTemp.getBidderId().intValue()));
		setTenderType(TenderUtils.initCapString(tenderResponseTemp.getBidType().name()));
		
		if(tenderResponseTemp.getPercentage()!=null && BigDecimal.ZERO.compareTo(tenderResponseTemp.getPercentage())>0)
			setSign("-");
		else 
			setSign("+");
	}
	
	public String create()
	{
		tenderJustificationService.save(tenderJustification);
		setMode(VIEW);
		return NEW;
	}
	
	public void validate()
	{
		if(tenderJustification.getRemarks()==null || "".equals(tenderJustification.getRemarks().trim()))
			addFieldError("remarks",getMessage("remarks.required"));
		
		if(tenderJustification.getJustifiedDate()==null)
			addFieldError("justificationDate",getMessage("justificationDate.required"));
		else if(tenderJustification.getJustifiedDate().before(tenderJustification.getTenderResponse().getResponseDate()))
			addActionError(getMessage("response.justification.date.validate"));
	}
	
	private Map<String,Object> justificationNoticeMap = new HashMap<String,Object>();
	
	@SkipValidation
	public String print()
	{
		justificationNoticeMap = new HashMap<String,Object>();
		tenderJustification = tenderJustificationService.getTenderJustificationById(idTemp);
		getMapByPassingModel(); 
		List<TenderJustification> list=new ArrayList<TenderJustification>();
		list.add(tenderJustification);
		ReportRequest reportInput = new ReportRequest(TenderConstants.JUSTIFICATIONREPORT,list,justificationNoticeMap);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		reportId = ReportViewerUtil.addReportToSession(reportOutput,getSession());
	    return REPORT;	
	}
	
	@SkipValidation
	public void getMapByPassingModel()
	{
		justificationNoticeMap = new HashMap<String,Object>();
		justificationNoticeMap.put("DEPARTMENTNAME",tenderResponseTemp.getNotice().getDepartment().getDeptName());
		justificationNoticeMap.put("RESPONSENO",tenderResponseTemp.getNumber());
		justificationNoticeMap.put("DATE",DateUtils.getFormattedDate(new Date(), TenderConstants.DATEPATTERN));
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		justificationNoticeMap.put("NameOfTheCity",session.getAttribute("cityname"));
		justificationNoticeMap.put("logoPath",tenderCommonService.getCityLogoName(request));
		Bidder bidder;
		String address="";
		
		if(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)!=null && 
				TenderConstants.TENDER_OWNER.equals(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0))){
			bidder  = tenderCommonService.getOwnerById(tenderResponseTemp.getBidderId().intValue());
			address =tenderResponseTemp.getBidderAddress().getStreetAddress1();
		}
		else{
			bidder= tenderResponseTemp.getBidder();
			address=bidder.getAddress();
		}
		justificationNoticeMap.put("BIDDER",bidder.getName());
		justificationNoticeMap.put("ADDRESS",address);
		justificationNoticeMap.put("TENDERNOTICENO",tenderResponseTemp.getNotice().getNumber());
		justificationNoticeMap.put("TENDERNO",tenderResponseTemp.getTenderUnit().getTenderUnitNumber());
	}
	
	
	public TenderJustification getModel() {
		return tenderJustification;
	}
	
	@SkipValidation
	public String newform()
	{
		buildTenderBeforeViewAndModify();
		return NEW;
	}
	
	public void setTenderResponseService(TenderResponseService tenderResponseService) {
		this.tenderResponseService = tenderResponseService;
	}
	
	public void setResponseId(Long responseId) {
		this.responseId = responseId;
	}
	
	public Map<String, List<String>> getTenderBidderType() {
		return tenderBidderType;
	}
	
	public List<TenderResponseLine> getResponseLineList() {
		return responseLineList;
	}
	
	public String getTenderType() {
		return tenderType;
	}

	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}

	public void setTenderJustificationService(
			TenderJustificationService tenderJustificationService) {
		this.tenderJustificationService = tenderJustificationService;
	}
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public String getSign() {
		return sign;
	}
	
	public void setSign(String sign) {
		this.sign = sign;
	}

}
