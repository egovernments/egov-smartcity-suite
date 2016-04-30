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
package org.egov.bpa.web.actions.extd.common;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.commons.EgwStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BpaExtnRuleBook {


	private Logger LOGGER = Logger.getLogger(getClass());
	private static final BpaExtnRuleBook bpaRuleBook = new BpaExtnRuleBook();
	public static final Map<String,List<String>> BPAROLEACTIONMAP = new HashMap<String,List<String>>();
	public static final Map<String,List<String>> STATUSACTIONMAP = new HashMap<String,List<String>>();
	public static final Map<String,List<String>> CITIZENSTATUSACTIONMAP = new HashMap<String,List<String>>();
	public static final Map<String,List<String>> BPAROLEVIEWMAP = new HashMap<String,List<String>>();
	public static final Map<String,List<String>> BPAROLEACTIONORDERMAP = new HashMap<String,List<String>>();
	public static final Map<String,List<String>> SEARCHBPAROLEACTIONMAP = new HashMap<String,List<String>>();
	public static final Map<String,String> ACTIONMETHODMAP = new HashMap<String,String>(); 
	private static final String ADDSITEINSPECTION = "Add SiteInspection";
	
	private static final String ADDINSPECTIONDATE = "Add InspectionDate"; 
	public static final String SUPERUSER    = "SUPERUSER";
	public static final String DEFAULT      = "DEFAULT";
	private static final String MODIFYINSPECTIONDATE = "Modify InspectionDate";
	//private static final String COLLECTFEEDETAILS = "Add FeeDetails";
	private static final String MODIFYFEEDETAILS = "Modify FeeDetails";
	private static final String CREATELETTERTOPARTY = "Create Letter To Party";
	private static final String REJECTBPA = "Reject Unconsidered";
	public static final String BPAEXECUTIVEENGINEERROLE    = "BPA EE"; 
	public static final String BPAASSISTANTENGINEERROLE    = "BPA AEE";
	public static final String BPAASSISTANTROLE    = "BPA Assistant";
	public static final String BPAOPERATORROLE    = "Operator";
	private static final String ADDNEWSITEINSPECTION = "Add New SiteInspection";
	private static final String PRINTREJECTIONNOTICE = "Print Unconsidered Notice";
	private static final String CITIZEN="PORTALUSERCITIZEN";
	public static final String CITIZENROLE    = "PORTALUSERCITIZEN";
	private static final String ADDREVISEDFEE = "Add Revised Fee";
	
	public static final String BPA_ASSISTANTENGINEER_ROLE = "BPA AE";
	public static final String BPA_SE_ROLE = "BPA SE";
	public static final String BPA_RDC_ROLE = "BPA RDC";
	
	private BpaExtnRuleBook()
	{
		super();
	}
	public static BpaExtnRuleBook getInstance()
	{
		return bpaRuleBook;
	}

	static
	{
		List<String> DefaultActions = Arrays.asList(ADDINSPECTIONDATE,ADDSITEINSPECTION,CREATELETTERTOPARTY);
		List<String> OperatorActions = Arrays.asList();
		List<String> AssistantActions = Arrays.asList(BpaConstants.ADDCHALLANSENTDATE,BpaConstants.BUILDINGMEASUREMENT,BpaConstants.UPDATESIGNATURE,
				BpaConstants.ORDERPREPARED,BpaConstants.REJECTORDPREP,BpaConstants.REJECTORDISSUED,
				BpaConstants.PRINTBUILDINGPERMIT,BpaConstants.PRINTPLANPERMIT,PRINTREJECTIONNOTICE); //,BpaConstants.CAPTURE_DD_DETAILS removed as dd details not required.
		List<String> AsstEngineerActions = Arrays.asList(ADDINSPECTIONDATE,MODIFYINSPECTIONDATE,ADDSITEINSPECTION,BpaConstants.ADDAPPROVALINFORMATION,ADDNEWSITEINSPECTION,
				BpaConstants.COLLECTFEEDETAILS,MODIFYFEEDETAILS,CREATELETTERTOPARTY,BpaConstants.CREATELETTERTOCMDA,REJECTBPA); //ADDREVISEDFEE
		List<String> AsstExeEngineerActions = Arrays.asList(ADDINSPECTIONDATE,MODIFYINSPECTIONDATE,ADDSITEINSPECTION,BpaConstants.ADDAPPROVALINFORMATION,ADDNEWSITEINSPECTION,
				BpaConstants.COLLECTFEEDETAILS,MODIFYFEEDETAILS,CREATELETTERTOPARTY,REJECTBPA);//ADDREVISEDFEE  
		List<String> ExecutiveEngineerActions = Arrays.asList();
		List<String> SuperintendingEngineerActions = Arrays.asList();
		List<String> RDCActions = Arrays.asList();
		
		List<String> DefaultViews = Arrays.asList(BpaConstants.REGISTRATIONDETAILS,BpaConstants.INSPECTIONDETAILS,
				BpaConstants.LETTERTOPARTYDETAILS,BpaConstants.FEEDETAILS);
		List<String> OperatorViews = Arrays.asList(BpaConstants.REGISTRATIONDETAILS,BpaConstants.INSPECTIONDETAILS,
				BpaConstants.LETTERTOPARTYDETAILS,BpaConstants.FEEDETAILS);
		List<String> AssistantViews = Arrays.asList(BpaConstants.REGISTRATIONDETAILS,BpaConstants.INSPECTIONDETAILS,
				BpaConstants.LETTERTOPARTYDETAILS,BpaConstants.FEEDETAILS);
		List<String> AsstEngineerViews = Arrays.asList(BpaConstants.INSPECTIONDETAILS,BpaConstants.LETTERTOPARTYDETAILS,
				BpaConstants.FEEDETAILS);
		List<String> ExecutiveEngineerViews = Arrays.asList(BpaConstants.INSPECTIONDETAILS,BpaConstants.LETTERTOPARTYDETAILS,
				BpaConstants.FEEDETAILS);
		List<String> superindentEngineerViews = Arrays.asList(BpaConstants.INSPECTIONDETAILS,BpaConstants.LETTERTOPARTYDETAILS,
				BpaConstants.FEEDETAILS);
		List<String> RDCEngineerViews = Arrays.asList(BpaConstants.INSPECTIONDETAILS,BpaConstants.LETTERTOPARTYDETAILS,
				BpaConstants.FEEDETAILS);
		List<String> AsstExecEngineerViews = Arrays.asList(BpaConstants.INSPECTIONDETAILS,BpaConstants.LETTERTOPARTYDETAILS,
				BpaConstants.FEEDETAILS);
		
		//List<String> RegistrationCreatedActions = Arrays.asList(ADDINSPECTIONDATE,CREATELETTERTOPARTY,REJECTBPA);
		List<String> RegistrationCreatedActions = Arrays.asList(ADDINSPECTIONDATE,REJECTBPA);
		//List<String> InspectionFixedActions = Arrays.asList(MODIFYINSPECTIONDATE,ADDSITEINSPECTION,BpaConstants.ADDAPPROVALINFORMATION,CREATELETTERTOPARTY,REJECTBPA);
		List<String> InspectionFixedActions = Arrays.asList(MODIFYINSPECTIONDATE,ADDSITEINSPECTION,BpaConstants.ADDAPPROVALINFORMATION,REJECTBPA);
		List<String> InspectedActions = Arrays.asList(ADDNEWSITEINSPECTION,BpaConstants.ADDAPPROVALINFORMATION,BpaConstants.COLLECTFEEDETAILS,CREATELETTERTOPARTY,REJECTBPA);
		List<String> FeesCollectedActions = Arrays.asList(MODIFYFEEDETAILS,BpaConstants.ADDAPPROVALINFORMATION,CREATELETTERTOPARTY,REJECTBPA);
		List<String> LetterToPartyCreatedActions = Arrays.asList(ADDINSPECTIONDATE);
		//List<String> ApplicantSignatureActions = Arrays.asList(BpaConstants.UPDATESIGNATURE);
		List<String> OrderPreparationActions = Arrays.asList(BpaConstants.BUILDINGMEASUREMENT,BpaConstants.ORDERPREPARED,BpaConstants.CAPTURE_DD_DETAILS);
		List<String> OrderIssuedActions = Arrays.asList(BpaConstants.ORDERISSUED);
		List<String> RejectOrdPrepActions = Arrays.asList(PRINTREJECTIONNOTICE,BpaConstants.REJECTORDPREP);
		List<String> RejectOrdIssActions = Arrays.asList(BpaConstants.REJECTORDISSUED);
		List<String> ApprovedActions = Arrays.asList(BpaConstants.ADDCHALLANSENTDATE,BpaConstants.PRINTBUILDINGPERMIT,BpaConstants.PRINTPLANPERMIT,
				BpaConstants.BUILDINGMEASUREMENT,BpaConstants.UPDATESIGNATURE,BpaConstants.ORDERPREPARED,BpaConstants.CAPTURE_DD_DETAILS);
		
		List<String> ChallanSentDateActions = Arrays.asList();
		List<String> ChallanAmountCollectedActions = Arrays.asList(BpaConstants.PRINTBUILDINGPERMIT,BpaConstants.PRINTPLANPERMIT,
				BpaConstants.UPDATESIGNATURE,BpaConstants.CAPTURE_DD_DETAILS);
	
		
		
		 
		
		
		
		List<String> CitizenRegistrationCreatedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenInspectionFixedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenInspectedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenFeesCreatedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenLPCreatedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);	
		List<String> CitizenLetterToPartySentActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenApplicationSignUpdatedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenOrderPreparedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenOrderIssuedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenRejectOrdPrepActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenRejectOrderPreparedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenApprovedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenChallanSentDateActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY,BpaConstants.VIEWFEETOBEPAID);
		List<String> CitizenChallanAmountCollectedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenUnderRejectionActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		List<String> CitizenRejectOrderIssuedActions = Arrays.asList(BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWINSPECTIONSCHEDULE,BpaConstants.VIEWLETTERTOPARTY);
		
		
		ACTIONMETHODMAP.put(BpaConstants.VIEWINSPECTIONSCHEDULE, "showInspectionSchedule");
		ACTIONMETHODMAP.put(BpaConstants.VIEWADMISSIONFEERECEIPT, "showCollectedFeeReceipts");
		ACTIONMETHODMAP.put(BpaConstants.VIEWFEETOBEPAID, "feePaymentPdf");
		ACTIONMETHODMAP.put(BpaConstants.VIEWLETTERTOPARTY, "showLettertoParty");
		ACTIONMETHODMAP.put(BpaConstants.DISCARDREGISTRATION, "discard");
		ACTIONMETHODMAP.put(BpaConstants.MODIFYREGISTRATION, "modifyForm");
		ACTIONMETHODMAP.put(BpaConstants.PAYFEEONLINE, "onlineFeePayment");
				
		 
		
		List<String> CitizenActions = Arrays.asList(
				BpaConstants.VIEWAPPLICATION,BpaConstants.VIEWLETTERTOPARTY,BpaConstants.VIEWFEETOBEPAID,
				BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWLETTERTOPARTY,BpaConstants.VIEWCHALLANNOTICE,
				BpaConstants.VIEWINSPECTIONSCHEDULE);
		
		
		BPAROLEACTIONMAP.put(BPAOPERATORROLE, OperatorActions);
		BPAROLEACTIONMAP.put(BPAASSISTANTROLE, AssistantActions);
		BPAROLEACTIONMAP.put(BPA_ASSISTANTENGINEER_ROLE, AsstEngineerActions);
		BPAROLEACTIONMAP.put(BPAASSISTANTENGINEERROLE, AsstExeEngineerActions);
		BPAROLEACTIONMAP.put(BPAEXECUTIVEENGINEERROLE, ExecutiveEngineerActions);
		BPAROLEACTIONMAP.put(CITIZENROLE, CitizenActions);
		BPAROLEACTIONMAP.put(DEFAULT, DefaultActions);
		BPAROLEACTIONMAP.put(BPA_SE_ROLE, SuperintendingEngineerActions);
		BPAROLEACTIONMAP.put(BPA_RDC_ROLE, RDCActions);
		

		BPAROLEVIEWMAP.put(BPAOPERATORROLE, OperatorViews);
		BPAROLEVIEWMAP.put(BPAASSISTANTROLE, AssistantViews);
		BPAROLEVIEWMAP.put(BPA_ASSISTANTENGINEER_ROLE, AsstEngineerViews);
		BPAROLEVIEWMAP.put(BPAASSISTANTENGINEERROLE, AsstExecEngineerViews);
		BPAROLEVIEWMAP.put(BPAEXECUTIVEENGINEERROLE, ExecutiveEngineerViews);
		BPAROLEVIEWMAP.put(BPA_RDC_ROLE, RDCEngineerViews);
		BPAROLEVIEWMAP.put(BPA_SE_ROLE, superindentEngineerViews);
		BPAROLEVIEWMAP.put(DEFAULT, DefaultViews);

		STATUSACTIONMAP.put(BpaConstants.APPLICATIONREGISTERED, RegistrationCreatedActions);
		STATUSACTIONMAP.put(BpaConstants.INSPECTIONSCHEDULED, InspectionFixedActions);
		STATUSACTIONMAP.put(BpaConstants.INSPECTED, InspectedActions);
		STATUSACTIONMAP.put(BpaConstants.FEESCREATED, FeesCollectedActions);
		STATUSACTIONMAP.put(BpaConstants.LETTERTOPARTYSENT, LetterToPartyCreatedActions);
		STATUSACTIONMAP.put(BpaConstants.STATUSAPPROVED, ApprovedActions);
		STATUSACTIONMAP.put(BpaConstants.CHALLANNOTICESENT, ChallanSentDateActions);
		STATUSACTIONMAP.put(BpaConstants.CHALLANAMOUNTCOLLECTED, ChallanAmountCollectedActions);
		STATUSACTIONMAP.put(BpaConstants.APPLICANTSIGNUPDATED, OrderPreparationActions);
		STATUSACTIONMAP.put(BpaConstants.ORDERPREPARED, OrderIssuedActions);
		STATUSACTIONMAP.put(BpaConstants.REJECTIONAPPROVED,RejectOrdPrepActions);
		STATUSACTIONMAP.put(BpaConstants.REJECTORDERPREPARED, RejectOrdIssActions);
		
		
		CITIZENSTATUSACTIONMAP.put(BpaConstants.APPLICATIONREGISTERED, CitizenRegistrationCreatedActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.INSPECTIONSCHEDULED, CitizenInspectionFixedActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.INSPECTED, CitizenInspectedActions); 
		CITIZENSTATUSACTIONMAP.put(BpaConstants.FEESCREATED, CitizenFeesCreatedActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.LETTERTOPARTYCREATED, CitizenLPCreatedActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.LETTERTOPARTYSENT, CitizenLetterToPartySentActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.LPREPLYRECEIVED, CitizenLetterToPartySentActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.STATUSAPPROVED, CitizenApprovedActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.CHALLANNOTICESENT, CitizenChallanSentDateActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.CHALLANAMOUNTCOLLECTED, CitizenChallanAmountCollectedActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.APPLICANTSIGNUPDATED, CitizenApplicationSignUpdatedActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.ORDERPREPARED, CitizenOrderPreparedActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.ORDERISSUEDTOAPPLICANT, CitizenOrderIssuedActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.UNDERREJECTION, CitizenUnderRejectionActions);
		
		CITIZENSTATUSACTIONMAP.put(BpaConstants.REJECTIONAPPROVED,CitizenRejectOrdPrepActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.REJECTORDERPREPARED, CitizenRejectOrderPreparedActions);
		CITIZENSTATUSACTIONMAP.put(BpaConstants.REJECTORDERISSUED, CitizenRejectOrderIssuedActions);
		 
		
		
		List<String> SearchDefaultActions = Arrays.asList(
				BpaConstants.VIEWAPPLICATION,BpaConstants.COLLECTFEE,BpaConstants.VIEWFEETOBEPAID,BpaConstants.VIEWREJECTIONORDER,
				BpaConstants.LETTERTOPARTYREPLY,BpaConstants.VIEWBUILDINGPERMITORDER,BpaConstants.VIEWPLANINGPERMITORDER,BpaConstants.ORDERISSUED,BpaConstants.CMDALETTERTOPARTYREPLY);
		List<String> SearchOperatorActions = Arrays.asList(
				BpaConstants.VIEWAPPLICATION,BpaConstants.COLLECTFEE,BpaConstants.VIEWFEETOBEPAID,
				BpaConstants.LETTERTOPARTYREPLY,BpaConstants.ORDERISSUED,BpaConstants.CMDALETTERTOPARTYREPLY);
		List<String> SearchAssistantActions =Arrays.asList(
				BpaConstants.VIEWAPPLICATION,BpaConstants.VIEWFEETOBEPAID,BpaConstants.VIEWREJECTIONORDER,
				BpaConstants.VIEWBUILDINGPERMITORDER,BpaConstants.VIEWPLANINGPERMITORDER,BpaConstants.PRINTEXTERNALFEEDETAILS);
		List<String> SearchAEEActions =Arrays.asList(
				BpaConstants.VIEWAPPLICATION,BpaConstants.VIEWFEETOBEPAID);
		List<String> SearchAEActions =Arrays.asList(
				BpaConstants.VIEWAPPLICATION,BpaConstants.VIEWFEETOBEPAID); //BpaConstants.ADDREVISEDFEE,
		List<String> SearchEEActions =Arrays.asList(
				BpaConstants.VIEWAPPLICATION,BpaConstants.VIEWFEETOBEPAID);
		List<String> SearchSEActions =Arrays.asList(
				BpaConstants.VIEWAPPLICATION,BpaConstants.VIEWFEETOBEPAID);
		List<String> SearchRDCActions =Arrays.asList(
				BpaConstants.VIEWAPPLICATION,BpaConstants.VIEWFEETOBEPAID);

		List<String> CitizenSearchActions = Arrays.asList(
				BpaConstants.VIEWAPPLICATION,BpaConstants.VIEWLETTERTOPARTY,BpaConstants.VIEWFEETOBEPAID,
				BpaConstants.VIEWADMISSIONFEERECEIPT,BpaConstants.VIEWLETTERTOPARTY,BpaConstants.VIEWCHALLANNOTICE,
				BpaConstants.VIEWINSPECTIONSCHEDULE);
		

		SEARCHBPAROLEACTIONMAP.put(BPAOPERATORROLE, SearchOperatorActions);
		SEARCHBPAROLEACTIONMAP.put(BPAASSISTANTROLE, SearchAssistantActions);
		SEARCHBPAROLEACTIONMAP.put(BPAASSISTANTENGINEERROLE, SearchAEEActions);
		SEARCHBPAROLEACTIONMAP.put(BPAEXECUTIVEENGINEERROLE, SearchEEActions);
		SEARCHBPAROLEACTIONMAP.put(BPA_ASSISTANTENGINEER_ROLE, SearchAEActions);
		
		SEARCHBPAROLEACTIONMAP.put(BPA_SE_ROLE, SearchSEActions);
		SEARCHBPAROLEACTIONMAP.put(BPA_RDC_ROLE, SearchRDCActions);
				
		SEARCHBPAROLEACTIONMAP.put(CITIZEN, CitizenSearchActions);
		SEARCHBPAROLEACTIONMAP.put(DEFAULT, SearchDefaultActions);

		
	}
	
	public List<String> getSearchActionsByRoles(List<String> roleName)
	{
		List<String> actionList = Collections.EMPTY_LIST;
		if(roleName!=null && !roleName.isEmpty()){

			if(roleName.contains(BPAOPERATORROLE))
				actionList = SEARCHBPAROLEACTIONMAP.get(BPAOPERATORROLE);
			else if(roleName.contains(BPAASSISTANTROLE))
				actionList = SEARCHBPAROLEACTIONMAP.get(BPAASSISTANTROLE);
			else if(roleName.contains(BPAASSISTANTENGINEERROLE))
				actionList = SEARCHBPAROLEACTIONMAP.get(BPAASSISTANTENGINEERROLE);
			else if(roleName.contains(BPAEXECUTIVEENGINEERROLE))
				actionList = SEARCHBPAROLEACTIONMAP.get(BPAEXECUTIVEENGINEERROLE);
			else if(roleName.contains(CITIZEN))
				actionList = SEARCHBPAROLEACTIONMAP.get(CITIZEN);
			 else if (roleName.contains(BPA_ASSISTANTENGINEER_ROLE)) {
				actionList = SEARCHBPAROLEACTIONMAP.get(BPA_ASSISTANTENGINEER_ROLE);
			} else if (roleName.contains(BPA_SE_ROLE)) {
				actionList = SEARCHBPAROLEACTIONMAP.get(BPA_SE_ROLE);
			} else if (roleName.contains(BPA_RDC_ROLE)) {
				actionList = SEARCHBPAROLEACTIONMAP.get(BPA_RDC_ROLE);
			} 
			else
				actionList = SEARCHBPAROLEACTIONMAP.get(DEFAULT);
		}
		return actionList;
	}

	public List<String> getActionsByRoles(List<String> roleName,EgwStatus registrationStatus)
	{
		List<String> actionList = Collections.EMPTY_LIST;
		LOGGER.debug(" ************ Role Name " + roleName);
		LOGGER.debug(" ************ registrationStatus  " + registrationStatus);
		if (roleName != null && !roleName.isEmpty()) {
			if (roleName.contains(CITIZEN.toUpperCase())) {
				actionList = BPAROLEACTIONMAP.get(CITIZENROLE);
				return filterCitizenActionsByStatus(actionList,registrationStatus);
			} else if (roleName.contains(BPAOPERATORROLE)) {
				 actionList = BPAROLEACTIONMAP.get(BPAOPERATORROLE);
			} else if (roleName.contains(BPAASSISTANTROLE)) {
				actionList = BPAROLEACTIONMAP.get(BPAASSISTANTROLE);  
			} else if (roleName.contains(BPA_ASSISTANTENGINEER_ROLE)) {
				actionList = BPAROLEACTIONMAP.get(BPA_ASSISTANTENGINEER_ROLE);
			} else if (roleName.contains(BPAASSISTANTENGINEERROLE)) {
				actionList = BPAROLEACTIONMAP.get(BPAASSISTANTENGINEERROLE);
			} else if (roleName.contains(BPAEXECUTIVEENGINEERROLE)) {
				actionList = BPAROLEACTIONMAP.get(BPAEXECUTIVEENGINEERROLE); 
			} else if (roleName.contains(BPA_SE_ROLE)) {
				actionList = BPAROLEACTIONMAP.get(BPA_SE_ROLE);
			} else if (roleName.contains(BPA_RDC_ROLE)) {
				actionList = BPAROLEACTIONMAP.get(BPA_RDC_ROLE);
			} else {
				actionList = BPAROLEACTIONMAP.get(DEFAULT);
			}
		}
		return filterActionsByStatus(actionList,registrationStatus);
	}

	
	public List<String> getViewsByRoles(List<String> roleName)
	{
		List<String> viewList = Collections.EMPTY_LIST;

		if(roleName!=null && !roleName.isEmpty()){

			if(roleName.contains(BPAOPERATORROLE))
				viewList = BPAROLEVIEWMAP.get(BPAOPERATORROLE);
			else if(roleName.contains(BPAASSISTANTROLE))
				viewList = BPAROLEVIEWMAP.get(BPAASSISTANTROLE);
			else if(roleName.contains(BPAASSISTANTENGINEERROLE))
				viewList = BPAROLEVIEWMAP.get(BPAASSISTANTENGINEERROLE);
			else if(roleName.contains(BPAEXECUTIVEENGINEERROLE))
				viewList = BPAROLEVIEWMAP.get(BPAEXECUTIVEENGINEERROLE);
			else if(roleName.contains(BPA_ASSISTANTENGINEER_ROLE))
				viewList = BPAROLEVIEWMAP.get(BPA_ASSISTANTENGINEER_ROLE);
			else if(roleName.contains(BPA_SE_ROLE))
				viewList = BPAROLEVIEWMAP.get(BPA_SE_ROLE);
			else if(roleName.contains(BPA_RDC_ROLE))
				viewList = BPAROLEVIEWMAP.get(BPA_RDC_ROLE);
			else
				viewList = BPAROLEVIEWMAP.get(DEFAULT);
		}
		return (viewList);
	}


	public List<String> filterCitizenActionsByStatus(List<String> actions,EgwStatus registrationStatus)
	{

		LOGGER.info(" ************ actions  " + actions);
		HashMap<String,String> citizenActionsMethodMap = new HashMap<String,String>();
		List<String> list = new ArrayList<String>();
		if(actions!=null && !actions.isEmpty()){
			List<String> statusActionList = Collections.EMPTY_LIST;
			if(registrationStatus!=null&&registrationStatus.getCode()!=null&&!registrationStatus.getCode().equals("")){
				
				LOGGER.info(" ************ registrationStatus  " + registrationStatus.getCode());
				statusActionList=	CITIZENSTATUSACTIONMAP.get(registrationStatus.getCode());
				LOGGER.info(" ....... statusActionList  " + statusActionList);
				for (String  act : actions) {
					if(statusActionList!=null&&statusActionList.contains(act)) {
						list.add(act);
					}
				}

				LOGGER.info("<<<<<<<<<<<<<< List  " + list);
				return list;		
				
			}
			else return  Collections.EMPTY_LIST;
		}else return  Collections.EMPTY_LIST;
	}

	
	public List<String> filterActionsByStatus(List<String> actions,EgwStatus registrationStatus)
	{

		LOGGER.info(" ************ actions  " + actions);
		
		List<String> list = new ArrayList<String>();
		if(actions!=null && !actions.isEmpty()){
			List<String> statusActionList = Collections.EMPTY_LIST;
			if(registrationStatus!=null&&registrationStatus.getCode()!=null&&!registrationStatus.getCode().equals("")){
				
				LOGGER.info(" ************ registrationStatus  " + registrationStatus.getCode());
				statusActionList=	STATUSACTIONMAP.get(registrationStatus.getCode());
				LOGGER.info(" ....... statusActionList  " + statusActionList);
				for (String  act : actions) {
					if(statusActionList!=null&&statusActionList.contains(act)) {
						list.add(act);
					}
				}

				LOGGER.info("<<<<<<<<<<<<<< List  " + list);
				return list;				
			}
			else return  Collections.EMPTY_LIST;
		}else return  Collections.EMPTY_LIST;
	}


	public Boolean checkViewsforRoles(List<String> roleList,String views) {
		List<String> viewList=	getViewsByRoles(roleList);
		if(viewList!=null && viewList.contains(views)){
			return Boolean.TRUE;
		}
		else return Boolean.FALSE;
	}
}
