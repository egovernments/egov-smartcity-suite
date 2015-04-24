/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.web.actions.citizen;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Registration;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.LetterToPartyExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegnAutoDcrDtlsExtn;
import org.egov.bpa.models.extd.ReportFeesDetailsExtn;
import org.egov.bpa.services.extd.autoDcr.AutoDcrExtnService;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.bpa.utils.ServiceType;
import org.egov.bpa.web.actions.common.BpaRuleBook;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.domain.entity.objection.Inspection;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Query;


@ParentPackage("egov")
@Results({ 
	@Result(name = CitizenSearchAction.FEE_PAYMENT_PDF, type = StreamResult.class, value = "feePaymentReportPDF", params = { "contentType", "application/pdf"})	
})
public class CitizenSearchAction extends BaseFormAction{

	private RegisterBpaService registerBpaService;
	private RegisterBpaExtnService registerBpaExtnService;
	private AutoDcrExtnService autoDcrExtnService;
	private Long registrationId;
	private Registration registrationObj;
	private RegistrationExtn registrationNewObj;
	private Logger LOGGER = Logger.getLogger(getClass());
	private Integer reportId = -1;
	private ReportService reportService;
	private BpaCommonService bpaCommonService;
	private BpaCommonExtnService bpaCommonExtnService;
	private List<LetterToParty> existingLetterToPartyDetails=new ArrayList<LetterToParty>();
	private List<LetterToPartyExtn> existingLetterToPartyNewDetails=new ArrayList<LetterToPartyExtn>();
	private String searchMode;
	private String autoDCRNo;
	private String phoneNo;
	private String planSubmissionNum;
	private String cmdaNum;
	private String emailAddress;
	private List<LpChecklist> lpChkListDet=new ArrayList<LpChecklist>();
	private Long letterToPartyId;
	private Long serviceTypeIdTemp;
	private List<Inspection> postponedInspectionDetails=new ArrayList<Inspection>();	
	private InspectionService inspectionService;
	private Map<String,BillReceiptInfo> billReceiptInfoMap = new HashMap<String,BillReceiptInfo>();
	private List<BillReceiptInfo> billRecptInfoList=new ArrayList<BillReceiptInfo>();
	private InputStream feePaymentReportPDF;
	private List<LpChecklist> lpReplyChkListDet = new ArrayList<LpChecklist>();
	private LetterToPartyService letterToPartyService;
	private List<Registration>oldregList=new ArrayList<Registration>();
	private List<RegistrationExtn>newregList=new ArrayList<RegistrationExtn>();
	private Boolean autoDcrispresent=Boolean.FALSE;
	
	private String securityKey;
	private String planSubmissionNumSearch;

	public LetterToPartyService getLetterToPartyService() {
		return letterToPartyService;
	}
	public void setLetterToPartyService(LetterToPartyService letterToPartyService) {
		this.letterToPartyService = letterToPartyService;
	}
	public AutoDcrService getAutoDcrService() {
		return autoDcrService;
	}
	public void setAutoDcrService(AutoDcrService autoDcrService) {
		this.autoDcrService = autoDcrService;
	}

	private AutoDcrService autoDcrService;

	public List<LpChecklist> getLpReplyChkListDet() {
		return lpReplyChkListDet;
	}
	public void setLpReplyChkListDet(List<LpChecklist> lpReplyChkListDet) {
		this.lpReplyChkListDet = lpReplyChkListDet;
	}

	public static final String FEE_PAYMENT_PDF = "feePaymentPdf";
	
	
	public List<BillReceiptInfo> getBillRecptInfoList() {
		return billRecptInfoList;
	}
	public InspectionService getInspectionService() {
		return inspectionService;
	}
	public void setInspectionService(InspectionService inspectionService) {
		this.inspectionService = inspectionService;
	}
	public List<Inspection> getPostponedInspectionDetails() {
		return postponedInspectionDetails;
	}
	public void setPostponedInspectionDetails(
			List<Inspection> postponedInspectionDetails) {
		this.postponedInspectionDetails = postponedInspectionDetails;
	}
	public Long getServiceTypeIdTemp() {
		return serviceTypeIdTemp;
	}
	public void setServiceTypeIdTemp(Long serviceTypeIdTemp) {
		this.serviceTypeIdTemp = serviceTypeIdTemp;
	}
	public Long getLetterToPartyId() {
		return letterToPartyId;
	}
	public void setLetterToPartyId(Long letterToPartyId) {
		this.letterToPartyId = letterToPartyId;
	}
	public List<LpChecklist> getLpChkListDet() {
		return lpChkListDet;
	}
	public void setLpChkListDet(List<LpChecklist> lpChkListDet) {
		this.lpChkListDet = lpChkListDet;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getCmdaNum() {
		return cmdaNum;
	}
	public void setCmdaNum(String cmdaNum) {
		this.cmdaNum = cmdaNum;
	}
	public String getPlanSubmissionNum() {
		return planSubmissionNum;
	}
	public void setPlanSubmissionNum(String planSubmissionNum) {
		this.planSubmissionNum = planSubmissionNum;
	}
	public String getAutoDCRNo() {
		return autoDCRNo;
	}
	public void setAutoDCRNo(String autoDCRNo) {
		this.autoDCRNo = autoDCRNo;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getSearchMode() {
		return searchMode;
	}
	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}
	public BpaCommonService getBpaCommonService() {
		return bpaCommonService;
	}
	public void setBpaCommonService(BpaCommonService bpaCommonService) {
		this.bpaCommonService = bpaCommonService;
	}

	public List<LetterToParty> getExistingLetterToPartyDetails() {
		return existingLetterToPartyDetails;
	}
	public void setExistingLetterToPartyDetails(
			List<LetterToParty> existingLetterToPartyDetails) {
		this.existingLetterToPartyDetails = existingLetterToPartyDetails;
	}
	public ReportService getReportService() {
		return reportService;
	}
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	public Registration getRegistrationObj() {
		return registrationObj;
	}
	public void setRegistrationObj(Registration registrationObj) {
		this.registrationObj = registrationObj;
	}
	public Long getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}
	public RegisterBpaService getRegisterBpaService() {
		return registerBpaService;
	}
	public void setRegisterBpaService(RegisterBpaService registerBpaService) {
		this.registerBpaService = registerBpaService;
	}
	
	public Registration getModel() {
		return null;
	}
	
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	
	public InputStream getFeePaymentReportPDF() {
		return feePaymentReportPDF;
	}
	public void setFeePaymentReportPDF(InputStream feePaymentReportPDF) {
		this.feePaymentReportPDF = feePaymentReportPDF;
	}

	public String newForm(){
		return NEW;
	}


	public String searchResult(){
		//super.search();		
		List<String> roleList =new ArrayList<String>();
		roleList.add("Citizen");
		serchOldRecords();
		//List<Registration> oldSearchResult= searchResult.getList();
		if(!oldregList.isEmpty()){
		prepareActionList(oldregList, roleList);
		}
		else{
			serchNewRecords();
			prepareActionListForNewRecords(newregList, roleList);
		}
		setSearchMode("result");
		return NEW;
	}
	
//dropdown view app
	public String viewApplication()
 {

		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);

		registrationObj = registerBpaService.findById(getRegistrationId(),
				false);

		LOGGER.debug("Entered into print method");

		if (registrationObj != null)
			reportId = registerBpaService.printRegistrationForm(
					registrationObj, getSession());

		LOGGER.debug("Exit from print method");
		return "report";

	}
	public String viewNewApplication()
	{

		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);

		registrationNewObj = registerBpaExtnService.findById(getRegistrationId(),
				false);

		LOGGER.debug("Entered into print method");

		if (registrationNewObj != null)
			reportId = registerBpaExtnService.printRegistrationForm(
					registrationNewObj, getSession());

		LOGGER.debug("Exit from print method");
		return "report";

	}
	//drop down receipt
	public String showCollectedFeeReceipts()
	{
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		registrationObj=registerBpaService.findById(getRegistrationId(),false);
		
		if(registrationObj!=null)
		{
			billReceiptInfoMap=registerBpaService.getCollectedReceiptsByRegistrationId(registrationObj);
			
			for(Map.Entry<String,BillReceiptInfo> map: billReceiptInfoMap.entrySet())
			{
				billRecptInfoList.add(map.getValue());
			}
		}
		return "receipts";
		
	}
	public String showNewCollectedFeeReceipts()
	{
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		registrationNewObj=registerBpaExtnService.findById(getRegistrationId(),false);
		
		if(registrationNewObj!=null)
		{
			billReceiptInfoMap=registerBpaExtnService.getCollectedReceiptsByRegistrationId(registrationNewObj);
			
			for(Map.Entry<String,BillReceiptInfo> map: billReceiptInfoMap.entrySet())
			{
				billRecptInfoList.add(map.getValue());
			}
		}
		return "receipts";
		
	}
	
	//dropdown print challan
	public String showExternalFeeDetails()
	{
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		registrationObj=registerBpaService.findById(getRegistrationId(),false);
		
		if(registrationObj!=null)
		{
			registerBpaService.printExternalFeeDetails(registrationObj, getSession());
		}
		return "externalFeeDetails";
		
	}
//dropdown
	public String showLettertoParty()
	{
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		if(registrationId!=null){
			registrationObj=registerBpaService.findById(getRegistrationId(),false);
			existingLetterToPartyDetails=registerBpaService.getLetterToPartyForRegistrationObject(registrationObj);
		}
		/*for(LetterToParty lp:existingLetterToPartyDetails){
			System.out.println(lp.getLetterToPartyReason().getDescription());
		}*/
		return "lettertoparty";

	}
	public String showNewLettertoParty()
	{
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		if(registrationId!=null){
			registrationNewObj=registerBpaExtnService.findById(getRegistrationId(),false);
			existingLetterToPartyNewDetails=registerBpaExtnService.getLetterToPartyForRegistrationObject(registrationNewObj);
		}
		/*for(LetterToParty lp:existingLetterToPartyDetails){
			System.out.println(lp.getLetterToPartyReason().getDescription());
		}*/
		return "lettertoparty";

	}
	//dopdown
	public String  showLetterToPartyCheckList(){
		ServiceType serviceType=registerBpaService.getServiceTypeById(serviceTypeIdTemp);
			if(serviceType.getCode()!=null){
				lpChkListDet=registerBpaService.getLetterToPartyCheckDtlsForType(letterToPartyId,BpaConstants.LETTERTOPARTYDETAILS);
			}			
		return "lettertopartychecklist";
	}
	

	public String  showLetterToPartyReplyCheckList(){
		ServiceType serviceType=registerBpaService.getServiceTypeById(serviceTypeIdTemp);
		
			if(serviceType.getCode()!=null){
				lpReplyChkListDet = registerBpaService.getLetterToPartyCheckDtlsForType(letterToPartyId,
						BpaConstants.LPCHKLISTTYPE_REPLY);
			}
		
		return "lettertopartyreplychklist";
	}
//dropdown
	public String showInspectionSchedule()
	{
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		if(registrationId!=null){
			registrationObj=registerBpaService.findById(getRegistrationId(),false);
		
			postponedInspectionDetails=inspectionService.getInspectionListforRegistrationObject(registrationObj);
		}
			for(Inspection inspectiondetails:postponedInspectionDetails){
				if(inspectiondetails.getParent()!=null)
					inspectiondetails.getParent().setPostponedDate(inspectiondetails.getInspectionDate());
			}
		
       return "inspectionschedule";
	}
	
	public  String getUsertName(Integer id)
    {
		String owner=bpaCommonService.getUsertName(id);
		return owner;
    }
	//print LP ack
	public String ackPrint() {
		try{
			HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
			ReportRequest reportRequest = null;
			Map<String, Object> reportParams = new HashMap<String, Object>();
		Registration	registration=letterToPartyService.getRegistrationObjectbyId(registrationId);
			Map<String,Object> reportData = constructLpReplyReportData(registration);
			
			reportRequest = new ReportRequest(BpaConstants.LPREPLYACKREPORT, reportData, reportParams);
			reportRequest.setPrintDialogOnOpenReport(true);
			reportId = ReportViewerUtil.addReportToSession(reportService.createReport(reportRequest), getSession());

			return "ackReport";
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception : " + e);
		}
		
	}
	
	private Map<String,Object> constructLpReplyReportData(Registration registration) {
		Map<String,Object> reportData = new HashMap<String,Object>();
		LetterToParty lpReply = letterToPartyService.getLatestLetterToPartyForRegObj(registration);
		RegnAutoDcr autoDcr = autoDcrService.getAutoDcrByLpIdAndRegId(registration, lpReply);
		String address = registration.getBpaOwnerAddress();
		
		reportData.put("zone", registration.getAdminboundaryid().getParent().getId());
		reportData.put("ward", registration.getAdminboundaryid().getName());
		reportData.put("planSubmissionNum", registration.getPlanSubmissionNum());
		reportData.put("applicantName", registration.getOwner().getFirstName());
		reportData.put("address", address);
		if(autoDcr != null) {
			if(autoDcr.getAutoDcrNum() != null && !autoDcr.getAutoDcrNum().equals(""))
				reportData.put("autoDCRNo", autoDcr.getAutoDcrNum());
		}
		
		reportData.put("ackNo", lpReply.getAcknowledgementNumber());
		reportData.put("replyDate", lpReply.getReplyDate());
		reportData.put("lpDate", lpReply.getLetterDate());
		reportData.put("lpNumber", lpReply.getLetterToPartyNumber());
		
		reportData.put("lpReason", lpReply.getLetterToPartyReason().getDescription());
		if(lpReply.getLetterToPartyReplyRemarks()!=null) {
			reportData.put("remarks", lpReply.getLetterToPartyReplyRemarks());
		}
		return reportData;
	}
//prepare action for old Bpa records
	public void prepareActionList(List<Registration> registrationSearchList,
			List<String> roleList) {
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		List<String> actionsList=BpaRuleBook.getInstance().getSearchActionsByRoles(roleList);

		if(!actionsList.isEmpty()){
			HashMap<Integer,String> statusMap=bpaCommonService.getStatusIdMap();
			for(Registration registrationObj:registrationSearchList){
				if(actionsList.contains(BpaConstants.VIEWAPPLICATION))
					registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWAPPLICATION);
				RegnAutoDcr autoDcrObj = autoDcrService.getAutoDcrByRegId(registrationObj.getId());
				if(autoDcrObj!=null){
					 autoDcrispresent=Boolean.TRUE;
				}
				if(actionsList.contains(BpaConstants.VIEWADMISSIONFEERECEIPT))
					registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWADMISSIONFEERECEIPT);
				Criteria statusCrit=bpaCommonService.createStatusChangeCriteria(registrationObj);
				List<Integer> statusIdList=statusCrit.list();
				for(Integer statusId:statusIdList){
					if(statusMap.get(statusId).equals(BpaConstants.LETTERTOPARTYSENT)){
						if(!registrationObj.getRegisterBpaSearchActions().contains(BpaConstants.VIEWLETTERTOPARTY)){						
							if(actionsList.contains(BpaConstants.VIEWLETTERTOPARTY))
								registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWLETTERTOPARTY);
						}
					}else if(statusMap.get(statusId).equals(BpaConstants.INSPECTIONSCHEDULED)){
						if(!registrationObj.getRegisterBpaSearchActions().contains(BpaConstants.VIEWINSPECTIONSCHEDULE)){
							if(actionsList.contains(BpaConstants.VIEWINSPECTIONSCHEDULE))
								registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWINSPECTIONSCHEDULE);
						}
					}else if(statusMap.get(statusId).equals(BpaConstants.CHALLANNOTICESENT)){
						if(!registrationObj.getRegisterBpaSearchActions().contains(BpaConstants.VIEWFEETOBEPAID)){
							if(actionsList.contains(BpaConstants.VIEWFEETOBEPAID))
								registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWFEETOBEPAID);
						}
					}
				}
			}
		}
	}
	public void prepareActionListForNewRecords(List<RegistrationExtn> registrationSearchList,
			List<String> roleList) {
		HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		List<String> actionsList=BpaRuleBook.getInstance().getSearchActionsByRoles(roleList);

		if(!actionsList.isEmpty()){
			HashMap<Integer,String> statusMap=bpaCommonExtnService.getStatusIdMap();
			for(RegistrationExtn registrationObj:registrationSearchList){
				if(actionsList.contains(BpaConstants.VIEWAPPLICATION))
					registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWAPPLICATION);
				
				
				if(actionsList.contains(BpaConstants.VIEWADMISSIONFEERECEIPT))
					registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWADMISSIONFEERECEIPT);
				Criteria statusCrit=bpaCommonExtnService.createStatusChangeCriteria(registrationObj);
				List<Integer> statusIdList=statusCrit.list();
				RegnAutoDcrDtlsExtn autoDcrObj = autoDcrExtnService.getAutoDcrByRegId(registrationObj.getId());
				if(autoDcrObj!=null){
					 autoDcrispresent=Boolean.TRUE;
				}
				for(Integer statusId:statusIdList){
					/*if(statusMap.get(statusId).equals(BpaConstants.LETTERTOPARTYSENT)){
						if(!registrationObj.getRegisterBpaSearchActions().contains(BpaConstants.VIEWLETTERTOPARTY)){						
							if(actionsList.contains(BpaConstants.VIEWLETTERTOPARTY))
								registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWLETTERTOPARTY);
						}
					}else if(statusMap.get(statusId).equals(BpaConstants.INSPECTIONSCHEDULED)){
						if(!registrationObj.getRegisterBpaSearchActions().contains(BpaConstants.VIEWINSPECTIONSCHEDULE)){
							if(actionsList.contains(BpaConstants.VIEWINSPECTIONSCHEDULE))
								registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWINSPECTIONSCHEDULE);
						}
					}else*/ if(statusMap.get(statusId).equals(BpaConstants.CHALLANNOTICESENT)){
						if(!registrationObj.getRegisterBpaSearchActions().contains(BpaConstants.VIEWFEETOBEPAID)){
							if(actionsList.contains(BpaConstants.VIEWFEETOBEPAID))
								registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWFEETOBEPAID);
						}
					}
				}
			}
		}
	}
	
	public String getAutoDcrPath(Long Id)
	{
		String path="";
		RegnAutoDcrDtlsExtn autoDcrObj = autoDcrExtnService
	
			.getAutoDcrByRegId(Id);
	if (autoDcrObj != null) {
		path=autoDcrObj.getAutoDcrDtlsExtn().getLogicalPath();
	}
	return path;
	}
	public String getOldAutoDcrPath(Long Id)
	{
		String path="";
		RegnAutoDcr autoDcrObj = autoDcrService.getAutoDcrByRegId(Id);
		if (autoDcrObj != null) {
		AutoDcr autoNum=autoDcrService.getAutoDcrByAutoDcrNum(autoDcrObj.getAutoDcrNum());
		if(autoNum!=null){
		path=autoNum.getLogicalPath();
		}
	}
	return path;
	}
	//result of old bpa	
	/*@Override
			public SearchQuery prepareQuery(String sortField, String sortOrder) {

				List<Object> paramList = new ArrayList<Object>();
				StringBuilder dynQuery = new StringBuilder(800);
				dynQuery.append( " from Registration reg where id is not null ") ;

				if(StringUtils.isNotBlank(getSecurityKey()))
				{
					dynQuery.append(" and (reg.securityKey) like ?");
					paramList.add(getSecurityKey());
				}

				if(StringUtils.isNotBlank(getPlanSubmissionNum()))
				{
					dynQuery.append(" and lower(reg.planSubmissionNum) like ?");
					paramList.add(getPlanSubmissionNum().toLowerCase());
				}

				if(StringUtils.isNotBlank(getAutoDCRNo()))
				{
					dynQuery.append(" and reg.id in (select registration.id from AutoDcr where autoDcrNum=? ) ");
					paramList.add(getAutoDCRNo().toString());
				}


				if(StringUtils.isNotBlank(getCmdaNum()))
				{
					dynQuery.append(" and lower(reg.cmdaNum) like ?");
					paramList.add("%"+getCmdaNum().toLowerCase()+"%");
				}


				if(getEmailAddress()!=null&&StringUtils.isNotBlank(getEmailAddress()))
				{
					dynQuery.append(" and lower(reg.owner.emailAddress) like ? "); 
					paramList.add("%"+getEmailAddress().toLowerCase()+"%");
				}

				if(StringUtils.isNotBlank(getPhoneNo())){
					dynQuery.append(" and (reg.owner.homePhone || reg.owner.officePhone || reg.owner.mobilePhone) like ? ");
					paramList.add(getPhoneNo());
				}

				//setPageSize(10);
				String regSearchQuery=" select distinct reg  "+	dynQuery;
				String countQuery = " select count(distinct reg)  " + dynQuery;
				return new SearchQueryHQL(regSearchQuery, countQuery, paramList);

			}*/
	public List<Registration> serchOldRecords()
	{
		StringBuilder dynQuery = new StringBuilder(800);
		dynQuery.append( " from Registration reg where id is not null  ") ;
		if(getSecurityKey()!=null && !"".equals(getSecurityKey()))
		{
			dynQuery.append(" and (reg.securityKey) =:securityKeycode");
		}
		if(StringUtils.isNotBlank(getPlanSubmissionNum()))
		{
			dynQuery.append(" and upper(reg.planSubmissionNum) =:plansubNumber");
		}

		if(StringUtils.isNotBlank(getAutoDCRNo()))
		{
			dynQuery.append(" and reg.id in (select registration.id from RegnAutoDcr where autoDcrNum =:autodcrNum) ");
		}


		if(StringUtils.isNotBlank(getCmdaNum()))
		{
			dynQuery.append(" and lower(reg.cmdaNum) =:cmdanuber");
		}


		if(getEmailAddress()!=null&&StringUtils.isNotBlank(getEmailAddress()))
		{
			dynQuery.append(" and lower(reg.owner.emailAddress) =:emailaddresss "); 
		}

		if(StringUtils.isNotBlank(getPhoneNo())){
			dynQuery.append(" and (reg.owner.homePhone || reg.owner.officePhone || reg.owner.mobilePhone) =:phonenumber ");
		}
		dynQuery.append(" order by id");
	Query query = HibernateUtil.getCurrentSession().createQuery(dynQuery.toString());
	if(getSecurityKey()!=null && !"".equals(getSecurityKey())){
	query.setString("securityKeycode", getSecurityKey());
	}
	if(StringUtils.isNotBlank(getPlanSubmissionNum()))
	{
				 query.setString("plansubNumber", getPlanSubmissionNum().toUpperCase()); 
			 }
	if(StringUtils.isNotBlank(getAutoDCRNo()))
	{
		 query.setString("autodcrNum", getAutoDCRNo().toUpperCase()); 
	}


	if(StringUtils.isNotBlank(getCmdaNum()))
	{
		 query.setString("cmdanuber", getCmdaNum().toLowerCase()); 
	}


	if(getEmailAddress()!=null&&StringUtils.isNotBlank(getEmailAddress()))
	{
		 query.setString("emailaddresss", getEmailAddress().toLowerCase()); 
	}

	if(StringUtils.isNotBlank(getPhoneNo())){
		 query.setString("phonenumber", getPhoneNo()); 
	}
	oldregList=query.list();
	return oldregList;
	}
	
	
	public List<RegistrationExtn> serchNewRecords()
	{
		StringBuilder dynQuery2 = new StringBuilder(800);
		dynQuery2.append( " from RegistrationExtn reg where id is not null  ") ;
		if(getSecurityKey()!=null && !"".equals(getSecurityKey()))
		{
			dynQuery2.append(" and (reg.securityKey) = :securityKeycode");
		}
		if(StringUtils.isNotBlank(getPlanSubmissionNum()))
		{
			dynQuery2.append(" and upper(reg.planSubmissionNum)=:plansubNumber");
		}

		if(StringUtils.isNotBlank(getAutoDCRNo()))
		{
			dynQuery2.append(" and reg.id in (select registration.id from RegnAutoDcrDtlsExtn where autoDcrNum =:autodcrNum) ");
		}


		if(StringUtils.isNotBlank(getCmdaNum()))
		{
			dynQuery2.append(" and lower(reg.cmdaNum) =:cmdanuber");
		}


		if(getEmailAddress()!=null&&StringUtils.isNotBlank(getEmailAddress()))
		{
			dynQuery2.append(" and lower(reg.owner.emailAddress) =:emailaddresss "); 
		}

		if(StringUtils.isNotBlank(getPhoneNo())){
			dynQuery2.append(" and (reg.owner.homePhone || reg.owner.officePhone || reg.owner.mobilePhone) =:phonenumber ");
		}
		dynQuery2.append(" order by id");
		
	Query query2 = HibernateUtil.getCurrentSession().createQuery(dynQuery2.toString());
	if(getSecurityKey()!=null && !"".equals(getSecurityKey())){
	query2.setString("securityKeycode", getSecurityKey());
	}
	if(StringUtils.isNotBlank(getPlanSubmissionNum()))
	{
				 query2.setString("plansubNumber", getPlanSubmissionNum().toUpperCase()); 
			 }
	if(StringUtils.isNotBlank(getAutoDCRNo()))
	{
		 query2.setString("autodcrNum", getAutoDCRNo().toUpperCase()); 
	}


	if(StringUtils.isNotBlank(getCmdaNum()))
	{
		 query2.setString("cmdanuber", getCmdaNum().toLowerCase()); 
	}


	if(getEmailAddress()!=null&&StringUtils.isNotBlank(getEmailAddress()))
	{
		 query2.setString("emailaddresss", getEmailAddress().toLowerCase()); 
	}

	if(StringUtils.isNotBlank(getPhoneNo())){
		 query2.setString("phonenumber", getPhoneNo()); 
	}
	newregList=query2.list();
	return newregList;
	}
///dropdown 
			public String feePaymentPdf()  throws JRException, Exception {
				HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
			//	feePaymentReportPDF = generateFeePaymentReportPDF();
				Registration registration=null;
			
				if(registrationId!=null){
					registration=registerBpaService.findById(registrationId,false);
				if(registration!=null) {	
					List<ReportFeesDetails> reportFeeDetailsList=registerBpaService.getSanctionedReportFeeDtls(registration);
					feePaymentReportPDF = bpaCommonService.generateFeePaymentReportPDF(registration,reportFeeDetailsList);
				}
				
				}
				return FEE_PAYMENT_PDF;
			}
			
			public String feeNewPaymentPdf()  throws JRException, Exception {
				HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
			//	feePaymentReportPDF = generateFeePaymentReportPDF();
				RegistrationExtn registration=null;
			
				if(registrationId!=null){
					registration=registerBpaExtnService.findById(registrationId,false);
				if(registration!=null) {	
					List<ReportFeesDetailsExtn> reportFeeDetailsList=registerBpaExtnService.getSanctionedReportFeeDtls(registration);
					feePaymentReportPDF = bpaCommonExtnService.generateFeePaymentReportPDF(registration,reportFeeDetailsList);
				}
				
				}
				return FEE_PAYMENT_PDF;
			}

			private InputStream generateFeePaymentReportPDF()
			{
				Registration registration=null;
				InputStream inputStream = null;
				ReportRequest reportRequest = null;
				if(registrationId!=null)
					registration=registerBpaService.findById(registrationId,false);
				
				
				BigDecimal finalFeeTobePaid= bpaCommonService.isFeeCollectionPending(registration);
								
				reportRequest = new ReportRequest("ChallanReport",registration, createHeaderParams(registration,FEE_PAYMENT_PDF,finalFeeTobePaid));
				ReportOutput reportOutput = reportService.createReport(reportRequest);				
				if (reportOutput != null && reportOutput.getReportOutputData() != null)
					inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
					
				return inputStream;
			}

			private Map<String,Object> createHeaderParams(Registration registration, String type,BigDecimal finalAmount){
				Map<String,Object> reportParams = new HashMap<String,Object>();
				List<ReportFeesDetails> reportFeeDetailsList=registerBpaService.getSanctionedReportFeeDtls(registration);
				if(type.equalsIgnoreCase(FEE_PAYMENT_PDF)){
					reportParams.put("planSubmissionNumber", registration.getPlanSubmissionNum());
					reportParams.put("dateofPlanSubmission", registration.getPlanSubmissionDate());
					reportParams.put("applicantName", registration.getOwner().getFirstName());
					reportParams.put("applicantAddress", registration.getBpaOwnerAddress());
					reportParams.put("reportFeeList", reportFeeDetailsList);
				}
				return reportParams; 
			}
			
			@SkipValidation
			public String sendSecurityKey(){
				
				return "securityKey";
			}
			//send securty key to mobile
			@SkipValidation
			public String sendSmsOrMail(){
				HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);

				Registration registration = (Registration) persistenceService.find
						("from Registration reg where lower(reg.planSubmissionNum) like ? ", 
								getPlanSubmissionNumSearch().toLowerCase());
				
				if(registration!=null){
					bpaCommonService.buildEmail(registration, BpaConstants.SMSEMAILSECURITYKEY, null);
					bpaCommonService.buildSMS(registration, BpaConstants.SMSEMAILSECURITYKEY);
					addActionMessage("Security Key Will be sent through SMS (and Mail if provided) Within 15 minutes. You can search your record using Security Key");
				}else{
					RegistrationExtn registrationNewObj = (RegistrationExtn) persistenceService.find
							("from RegistrationExtn reg where lower(reg.planSubmissionNum) like ? ", getPlanSubmissionNumSearch().toLowerCase());
					
					if(registrationNewObj!=null)
					{
						bpaCommonExtnService.buildEmail(registrationNewObj, BpaConstants.SMSEMAILSECURITYKEY, null);
						bpaCommonExtnService.buildSMS(registrationNewObj, BpaConstants.SMSEMAILSECURITYKEY);
						addActionMessage("Security Key Will be sent through SMS (and Mail if provided) Within 15 minutes. You can search your record using Security Key");
				
					}
					else{
					addActionMessage("Please Enter Correct and Complete Plan Submission Number to get Security Key.");
					}
					
				}
				return NEW;
			}
			
			public String getSecurityKey() {
				return securityKey;
			}
			public void setSecurityKey(String securityKey) {
				this.securityKey = securityKey;
			}
			public String getPlanSubmissionNumSearch() {
				return planSubmissionNumSearch;
			}
			public void setPlanSubmissionNumSearch(String planSubmissionNumSearch) {
				this.planSubmissionNumSearch = planSubmissionNumSearch;
			}
			public List<Registration> getOldregList() {
				return oldregList;
			}
			public void setOldregList(List<Registration> oldregList) {
				this.oldregList = oldregList;
			}
			public List<RegistrationExtn> getNewregList() {
				return newregList;
			}
			public void setNewregList(List<RegistrationExtn> newregList) {
				this.newregList = newregList;
			}
			public RegisterBpaExtnService getRegisterBpaExtnService() {
				return registerBpaExtnService;
			}
			public void setRegisterBpaExtnService(
					RegisterBpaExtnService registerBpaExtnService) {
				this.registerBpaExtnService = registerBpaExtnService;
			}
			public RegistrationExtn getRegistrationNewObj() {
				return registrationNewObj;
			}
			public void setRegistrationNewObj(RegistrationExtn registrationNewObj) {
				this.registrationNewObj = registrationNewObj;
			}
			public BpaCommonExtnService getBpaCommonExtnService() {
				return bpaCommonExtnService;
			}
			public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonExtnService) {
				this.bpaCommonExtnService = bpaCommonExtnService;
			}
			public List<LetterToPartyExtn> getExistingLetterToPartyNewDetails() {
				return existingLetterToPartyNewDetails;
			}
			public void setExistingLetterToPartyNewDetails(
					List<LetterToPartyExtn> existingLetterToPartyNewDetails) {
				this.existingLetterToPartyNewDetails = existingLetterToPartyNewDetails;
			}
			public AutoDcrExtnService getAutoDcrExtnService() {
				return autoDcrExtnService;
			}
			public void setAutoDcrExtnService(AutoDcrExtnService autoDcrExtnService) {
				this.autoDcrExtnService = autoDcrExtnService;
			}
			public Boolean getAutoDcrispresent() {
				return autoDcrispresent;
			}
			public void setAutoDcrispresent(Boolean autoDcrispresent) {
				this.autoDcrispresent = autoDcrispresent;
			}
			

}
