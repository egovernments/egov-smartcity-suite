package org.egov.tender.web.actions.tenderresponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwStatus;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.State;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.tender.interfaces.Tenderable;
import org.egov.tender.interfaces.TenderableGroup;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderJustification;
import org.egov.tender.model.TenderResponseLine;
import org.egov.tender.model.TenderResponseLineComparator;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.utils.TenderConstants;

/**
 * 
 * 
 * 
 * 
 * @author pritiranjan
 *
 */


@ParentPackage("egov")
@SuppressWarnings("serial")
public class TenderResponseAction extends ResponseAction{

	private static final Logger LOGGER   = Logger.getLogger(TenderResponseAction.class);
	private static final String ERRORMSG = "Insufficient Information in TenderNotice";
	private String justifiedRemarks;
	private Date justifiedDate;
	private Long negotiationId;
	private String negotiationStatus;
	private EmployeeService employeeService;
	private String cancelRemarks;
	private String bidResponseNumber;
	private String messageKey;
	private Long responseId;
	private String sourcepage="";
	
	public TenderResponseAction()
	{
		addRelatedEntity("status", EgwStatus.class);
		addRelatedEntity("createdBy", UserImpl.class);
		addRelatedEntity("modifiedBy", UserImpl.class);
		addRelatedEntity("state", State.class);
		addRelatedEntity("tenderUnit",TenderUnit.class);
	}
	
	@SuppressWarnings("unchecked")
	public void prepare()
	{
		LOGGER.info("start prepare method");
		if(responseId==null) {
			if(idTemp!=null && mode != null){
				tenderResponse=tenderResponseService.getTenderResponseById(idTemp);
				tenderResponse=tenderResponseService.merge(tenderResponse);
			}
			else if (idTemp!=null){
				tenderResponse=tenderResponseService.getTenderResponseById(idTemp);
			}
			super.prepare();
			addDropdownData("tenderFileTypeList",persistenceService.findAllBy("from TenderFileType where isActive=? order by fileType ",Boolean.TRUE));
			addDropdownData("statusList",persistenceService.findAllByNamedQuery(TenderConstants.GETALLSTATUSINTENDER,
					TenderConstants.TENDERRESPONSEMODULE,(List<String>)scriptService.executeScript(TenderConstants.TENDERRESPONSE_STATUSSCRIPT, 
							ScriptService.createContext("response",tenderResponse,"mode",mode,"responseService",tenderResponseService))));
			autoGenResponse=tenderCommonService.getAppconfigValue(TenderConstants.TENDER,TenderConstants.FLAG_AUTO_TENDERRESPONSENUMBER, "0");
			if(tenderUnitId!=null){
				tenderResponse.setTenderUnit((TenderUnit)persistenceService.find("from TenderUnit where id=?",tenderUnitId));
			}
			tenderBidderType=tenderResponseService.setTenderTypeAndBidderName(tenderResponse);
			addDropdownData("bidderList",tenderResponseService.populateBidderList(tenderResponse));
		}

		LOGGER.info("end prepare method");
	}
	
	/**
	 * This method is to load TenderNoticeUnit to Response
	 */
	
	@SkipValidation
	public String loadTenderNoticeToCreateResponse()
	{
		LOGGER.info("start loadTenderNoticeToCreateResponse method");
		//TenderResponseLine responseLine=null;
		tenderResponse.setStatus(tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERRESPONSEMODULE, TenderConstants.TENDERRESPONSE_CREATED));

		if(tenderResponse.getTenderUnit().getTenderableGroups().isEmpty()){
			
			if(tenderResponse.getTenderUnit().getTenderEntities().isEmpty())
				throw new EGOVRuntimeException(ERRORMSG);
			
			for(Tenderable tendarable : tenderResponse.getTenderUnit().getTenderEntities())
				responseLineList.add(createResponseLineFromTenderable(tendarable));
		}
		
		else{
			
			for(TenderableGroup tenderGroup : tenderResponse.getTenderUnit().getTenderableGroups()){
				if(tenderGroup.getEntities().isEmpty())
				{
					if(tenderResponse.getTenderUnit().getTenderEntities().isEmpty())
						throw new EGOVRuntimeException(ERRORMSG);
					
					for(Tenderable tendarable : tenderResponse.getTenderUnit().getTenderEntities())
						responseLineList.add(createResponseLineFromTenderable(tendarable));
					break;
				}
				else{
					for(Tenderable tendarable:tenderGroup.getEntities())
						responseLineList.add(createResponseLineFromTenderable(tendarable));
				}
			}
		}
		
		Collections.sort(responseLineList,new TenderResponseLineComparator());
		if(tenderResponse.getResponseDate()==null)
			tenderResponse.setResponseDate(new Date());
		setMode(NEW);
		LOGGER.info("end loadTenderNoticeToCreateResponse method");
		return NEW;
	}
	
	/**
	 *   To create TenderResponse
	 */
	
	public String create()
	{
		LOGGER.info("start create method");
		buildTenderResponse();
		tenderResponse.setPosition(getPosition());
		if(tenderResponse.getCurrentState()!=null)
			tenderResponse.getCurrentState().setText2(comments);
		tenderResponseService.save(tenderResponse, workFlowType);
		updateNegotiation();
		setMode(VIEW);
		if(tenderResponse.getNotice()!=null&&tenderResponse.getNotice().getTenderFileType()!=null&&tenderResponse.getNotice().getTenderFileType().getFileType().equals(TenderConstants.WORKSINDENT)&&
				tenderResponse.getStatus()!=null&&tenderResponse.getStatus().getCode().equals(TenderConstants.TENDERRESPONSE_ACCEPTED)){
		Set<TenderResponseLine> responseLines=tenderResponse.getResponseLines();
		Set<TenderResponseLine> responseLineswithzero=new HashSet(0);
		for(TenderResponseLine lineObj:responseLines){
			if(Math.abs(lineObj.getBidRate().intValue())==0)
				responseLineswithzero.add(lineObj);
		}
		responseLines.removeAll(responseLineswithzero);
		}
		responseLineList=new ArrayList<TenderResponseLine>(tenderResponse.getResponseLines());
		Collections.sort(responseLineList,new TenderResponseLineComparator());
		LOGGER.info("end create method");
		return NEW;
	}
	
	private void updateNegotiation()
	{
		if(negotiationId != null && negotiationStatus !=null && 
				!TenderConstants.TENDERRESPONSE_NEGOTIATED.equals(negotiationStatus.trim()))
		{
			GenericTenderResponse tenderNegotiation = tenderResponseService.getTenderResponseById(negotiationId);
			tenderNegotiation.setStatus(tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERRESPONSEMODULE,negotiationStatus));
			tenderResponseService.updateNegotiation(tenderNegotiation);
		}
	}
	
	@SkipValidation
	public String view()
	{
		buildTenderBeforeViewAndModify();
		negotiationDetails = tenderResponseService.getNegotiationDetail(tenderResponse.getId());
		setMode(VIEW);
		return NEW;
	}
	
	
	@SkipValidation
	public String modify()
	{
		LOGGER.info("start modify method");
		buildTenderBeforeViewAndModify();
		if(tenderResponse.getCurrentState()!=null)
		{
			List<String> wfNextValue=tenderCommonService.getFurtherWorkflowState(tenderResponse.getCurrentState().getValue(),TenderConstants.WFSTATUS_NEXT,tenderResponse);
			setWfStatus(wfNextValue.isEmpty()? null:wfNextValue.get(1));
		}
		setMode(tenderCommonService.isModifyableByLoginUser(tenderResponse) && TenderConstants.TENDERRESPONSE_CREATED.equals(tenderResponse.getStatus().getCode())? MODIFY:NOTMODIFY);
		
		if(TenderConstants.WF_APPROVE_STATE.equals(wfStatus)){
			negotiationDetails = tenderResponseService.getNegotiationDetail(tenderResponse.getId());
			TenderJustification justification=tenderResponseService.getJustificationByResponse(tenderResponse);
			if(justification!=null){
				setJustifiedDate(justification.getJustifiedDate());
				setJustifiedRemarks(justification.getRemarks());
			}
		}
		LOGGER.info("end modify method");
		return NEW;
	}
	
	
	
		
	/**
	 *   This method is to validate model
	 */
	
	
	public void validate()
	{
		LOGGER.info("start validate method");
		if (tenderResponse.getResponseDate() == null)
			addFieldError("responseDate", getMessage("responseDate.required"));
		if(!isAutoGenResponse() && (tenderResponse.getNumber()== null || "".equals(tenderResponse.getNumber().trim())))
			addFieldError("responseNumber", getMessage("responseNumber.required"));
		if(tenderBidderType!=null){
			//Bidder validation for works and stores 
			if((tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)!=null 
					&& !TenderConstants.TENDER_OWNER.equals(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)))
					&& (tenderResponse.getBidderId() == null || tenderResponse.getBidderId() == -1)){
				addFieldError("bidderName",tenderResponse.getNotice().getTenderFileType().getBidderType()+" is Required");
			}
			//Bidder validation for LnE
			else if(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)!=null &&
					TenderConstants.TENDER_OWNER.equals(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)))
			{
				if(tenderResponse.getOwner()!=null && 
						(tenderResponse.getOwner().getFirstName() == null || "".equals(tenderResponse.getOwner().getFirstName().trim())))
					addFieldError("bidderName",getMessage("bidderName.required"));

				if(tenderResponse.getBidderAddress()!=null && 
						(tenderResponse.getBidderAddress().getStreetAddress1() == null || "".equals(tenderResponse.getBidderAddress().getStreetAddress1().trim())))
					addFieldError("bidderAddress",getMessage("bidderAddress.required"));

				if(tenderResponse.getOwner()!=null && 
						(tenderResponse.getOwner().getOfficePhone() == null || "".equals(tenderResponse.getOwner().getOfficePhone().trim())))
					addFieldError("officePhone",getMessage("officePhone.required"));

				if(tenderResponse.getRatePerUnit()==null)
					addFieldError("ratePerUnit",getMessage("ratePerUnit.required"));
			}
			else if(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)!=null 
					&& !TenderConstants.TENDER_OWNER.equals(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0))
					&& (tenderResponse.getBidderId() != null || tenderResponse.getBidderId() != -1) 
					&& tenderResponseService.checkUniqueResponseForBidder(tenderResponse.getId(), tenderResponse.getBidderId(),
							tenderResponse.getNotice().getTenderFileType().getBidderType(), tenderResponse.getTenderUnit()))
			{
				addActionError(getMessageWithParam("response.exist.bidder",new String[]{tenderResponse.getNotice().getTenderFileType().getBidderType()}));
			}
		}
		if(!isAutoGenResponse() && (tenderResponse.getNumber()!= null && !"".equals(tenderResponse.getNumber().trim()) 
				&& tenderResponseService.checkUniqueResponseNumber(tenderResponse.getNumber(), tenderResponse.getId())))
			addActionError(getMessage("responseNumber.alreadyExist"));
		LOGGER.info("end validate method");
	}
	
	@SkipValidation
	public String cancelApprovedBidresponse() {  
		tenderResponse= tenderResponseService.findById(responseId, false);
		//List<GenericTenderResponse> List= tenderResponseService.getNegotiationDetail(responseId);
		for(GenericTenderResponse res:tenderResponseService.getNegotiationDetail(responseId)) {
			res.setStatus(tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERRESPONSEMODULE,TenderConstants.TENDERRESPONSE_CANCELLED));
			tenderResponseService.persist(res);
		}
		tenderResponse.getCurrentState().getPrevious().setValue("CANCELLED");
		PersonalInformation personinfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));
		String empName="";
		if(personinfo.getEmployeeFirstName()!=null)
			empName=personinfo.getEmployeeFirstName();
		if(personinfo.getEmployeeLastName()!=null)
			empName=empName.concat(" ").concat(personinfo.getEmployeeLastName());
		tenderResponse.getCurrentState().getPrevious().setText1(cancelRemarks+". Tender Bid Response Cancelled by: "+empName);
		tenderResponse.setStatus(tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERRESPONSEMODULE,TenderConstants.TENDERRESPONSE_CANCELLED));
		bidResponseNumber=tenderResponse.getNumber();
		tenderResponseService.persist(tenderResponse);
		messageKey=bidResponseNumber+": Tender Bid Response Cancelled successfully"; 
		return SUCCESS;
	}
	
	public int getLoggedInUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
	
	private List<EgwStatus> negotiationStatusList= new ArrayList<EgwStatus>();
	
	
	@SuppressWarnings("unchecked")
	public List<EgwStatus> getNegotiationStatusList()
	{
		
		if(negotiationStatusList.isEmpty())
			negotiationStatusList = (List<EgwStatus>)persistenceService.findAllByNamedQuery(TenderConstants.GETALLSTATUSINTENDER,
					TenderConstants.TENDERRESPONSEMODULE,Arrays.asList(TenderConstants.TENDERRESPONSE_ACCEPTED,TenderConstants.TENDERRESPONSE_NEGOTIATED,
					TenderConstants.TENDERRESPONSE_RENEGOTIATED));
		return negotiationStatusList;
	}
	
	public GenericTenderResponse getModel() {
		return tenderResponse;
	}
	
	public String getJustifiedRemarks() {
		return justifiedRemarks;
	}

	public void setJustifiedRemarks(String justifiedRemarks) {
		this.justifiedRemarks = justifiedRemarks;
	}

	public Date getJustifiedDate() {
		return justifiedDate;
	}

	public void setJustifiedDate(Date justifiedDate) {
		this.justifiedDate = justifiedDate;
	}
	
	public void setNegotiationId(Long negotiationId) {
		this.negotiationId = negotiationId;
	}

	public void setNegotiationStatus(String negotiationStatus) {
		this.negotiationStatus = negotiationStatus;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public String getBidResponseNumber() {
		return bidResponseNumber;
	}

	public void setBidResponseNumber(String bidResponseNumber) {
		this.bidResponseNumber = bidResponseNumber;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public Long getResponseId() {
		return responseId;
	}

	public void setResponseId(Long responseId) {
		this.responseId = responseId;
	}

	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}
	
}
