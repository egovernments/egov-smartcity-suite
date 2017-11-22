/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.lcms.utils.constants;

import java.text.SimpleDateFormat;

public class LcmsConstants {
	public static final String MODULE_TYPE_LEGALCASE = "Legal Case";
	public static final String MODULE_NAME = "LCMS";
	public static final String APPROVED_STATUS = "Approved";
	public static final String CIVIL_COURT_KEY = "Civil Court";
	public static final String EX_PARTE_KEY = "Ex-parte";
	public static final String DATE_FILTER_KEY = "DateFilterKey";
	public static final String JUDGMENTIMPLEMENT_STATUS = "JUDGEMENT_IMPL";
	public static final String JUDGMENTIMPLEMENT_INPROGRESS_STATUS = "Implementation In Progress";
	public static final String START_CASE_YEAR_KEY = "START_CASE_YEAR";
	public static final String LC_NUMBER_TYPE_KEY = "LC_NUMBER_TYPE";
	public static final String LC_NUMBER_OPTIONAL_TYPE = "MANUAL";
	public static final String LC_NUMBER_AUTOMATED_TYPE = "AUTOMATED";
	public static final String APPENDSEPERATE = ",\n";
	public static final String TARGET_VIEW = "view";
	public static final String TARGET_SUCCESS = "success";
	public static final String CONTEMPT_STATUS = "Contempt";
	public static final String APPEAL_STATUS = "Appeal";
	public static final String AGGREGATED_REPORT = "aggregatedReport";
	public static final String FLAT_REPORT = "flatReport";
	public static final String IO_TYPE_SMA = "Stay Made Absolute";
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String DISPOSAL_DATE = "disposalDate";
	public static final String REPORT = "Report";
	public static final String Search = "Search";
	public static final String ADVOCATE_MASTER = "advocateMaster";
	public static final String LCINTERIMORDERINSTANCE_IODATE = "lcinterimorderInstance.ioDate";
	public static final String Department_Name = "department";
	public static final String MONTHLY_REPORT = "monthlyReport";
	public static final String YEARLY_REPORT = "yearlyReport";
	public static final String MONTH = "month(legalcase.casedate)";
	public static final String EGW_STATUS = "egwStatus";
	public static final String LEGAL_CASE = "Legalcase";
	public static final String LEGAL_CASE_DEPARTMENT = "legalcaseDepartment";
	public static final String LEGAL_CASE_DEPARTMENT_AND_DEPARTMENT = "legalcaseDepartment.department";
	public static final String LEGAL_CASE_STATUS = "Legalcase.egwStatus";
	public static final String LEGAL_CASE_STATUS_AND_DESC = "egwStatus.description";
	public static final String DETAILS = "details";
	public static final String Withdrawn = "Withdrawn";
	public static final String WithdrawnDetails = "The case was withdrawn by the party";
	public static final Long IS_ACTIVE = 1L;
	public static final String LEGAL_CASE_ADVOCATE = "advocateMaster";
	public static final String EMPTY_STRING = " ";
	public static final String ZERO_VALUE = "0";
	public static final String SEPARATOR = ",";
	public static final String JudgmentType = "judgmentType";
	public static final String ACCOUNTS_APPROVAL = "ACCOUNTS APPROVAL";
	public static final String COA_DEDITCODE1 = "LEGAL EXPENSES-EXPENSES FOR FILING CASES";
	public static final String YES = "Yes";
	public static final String NO = "No";
	public static final String PURPOSE_ID = "LCMS_PURPOSE_ID";
	public static final String LEGALCASE_LEGALCASEDEPARTMENT = "Legalcase.legalcaseDepartment";
	public static final String LEGALCASE_CASENUMBER = "Legalcase.casenumber";
	public static final String APPROVED = "APPROVED";
	public static final String REJECTED = "REJECTED";
	public static final String END = "END";
	public static final String KEY_COA_DEDITCODE_NOT_FILEDBY_CORP = "LEGALCASE_COA_DEBITCODE_NOT_FILEDBY_CORP";
	public static final String BILL_TYPE = "Final Bill";
	public static final String EXPENDITURE_BILL_TYPE = "Expense";
	public static final String BILL_MODULE = "CBILL";
	public static final String KEY_CFUNCTION_CODE = "LEGALCASE_FUNCTION_CODE";
	public static final String CBILL_CREATED_STATUS = "CREATED";
	public static final String CBILL_APPROVED_STATUS = "APPROVED";
	public static final String CBILL_CANCELLED_STATUS = "Cancelled";
	public static final String MISCDETAILS_STATUS_CONSECUTIONAMENDMENT = "CONSECUTIONAMENDMENT";
	public static final String MISCDETAILS_STATUS_COMPLIANCE = "COMPLIANCE";
	public static final String MISCDETAILS_STATUS_EVIDENCEBEFORECHARGE = "EVIDENCEBEFORECHARGE";
	public static final String MISCDETAILS_STATUS_EVIDENCEAFTERCHARGE = "EVIDENCEAFTERCHARGE";
	public static final String MISCDETAILS_STATUS_NONBAILABLEWARRANT = "NONBAILABLEWARRANT";
	public static final String MISCDETAILS_STATUS_PRONOUNCEMENTOFJUDGEMENT = "PRONOUNCEMENTOFJUDGEMENT";
	public static final String MISCDETAILS_STATUS_CLOSEDFORJUDGEMENT = "CLOSEDFORJUDGEMENT";
	public static final String MISCDETAILS_STATUS_ISSUE = "ISSUE";
	public static final String HEARINGS_STATUS_EVIDENCE = "EVIDENCE";
	public static final String HEARINGS_STATUS_ARGUMENT = "ARGUMENT";
	public static final String LEGALCASE_CRIMINALCASE_CODES = "LEGALCASE_CRIMINALCASE_CODES";
	public static final String LEGALCASE_STATUS_CODE_APPEARANCE = "APPEARANCE";
	public static final String LEGALCASE_STATUS_CODE_REPLY = "REPLY";
	public static final String LEGALCASE_STATUS_CODE_REPLYCUMWRITTENSTATEMENT = "REPLY_CUM_WRITTEN_STATEMENT";
	public static final String LEGALCASE_STATUS_CODE_FILINGOFDOCUMENTS = "FILING_OF_DOCUMENTS";
	public static final String LEGALCASE_STATUS_ORDER = "ORDER";
	public static final String LEGALCASE_STATUS_CREATED = "LCCREATED";
	public static final String LEGALCASE_STATUS_JUDGMENT = "JUDGMENT";
	public static final String LEGALCASE_STATUS_JUDGMENT_IMPLIMENTED = "JUDGEMENT_IMPL";
	public static final String LEGALCASE_STATUS_CLOSED = "CLOSED";
	public static final String LEGALCASE_STATUS_HEARING_REPLYTOTI = "HEARING_REPLY_TO_TI";
	public static final String LEGALCASE_STATUS_ORDER_REPLYTOTI = "ORDER_REPLY_TO_TI";
	public static final String ADVOCATEBILL_REJECTED_STATUS = "PROSECUTOR_REJECTED";
	public static final String ADVOCATEBILL_EDITED_STATUS = "BILL_EDITTED";
	public static final String LEGALCASE_HEARING_STATUS = "HEARING";
	public static final String LEGALCASE_INTERIMSTAY_STATUS = "INTERIM_STAY";
	/**
	 * Time series report action constants
	 */
	public static final String SERIAL_NO = "serialNumber";
	public static final String COUNT = "count";
	public static final String AGG_ITEM = "aggItem";
	public static final String AGG_YEAR = "Year";
	public static final String AGG_MONTH = "Month";
	public static final String QRY_CONSTANTS = "Select distinct(count(legalcase.id))";
	public static final String QRY_SELECTION_CONSTANTS = " from LegalCase legalcase ";
	public static final String QRY_GROUPBY_CONSTANTS = " group by ";
	public static final String QRY_ORDERBY_CONSTANTS = " order by ";
	public static final String FILED_BYCOC_FILING_Expenses = "Filing Expenses";

	public static final Integer PURPOSE_ID_LEGAL_EXPENSES = 59;

	// Miscellaneous
	public static final String ERROR_KEY = "errors";
	public static final String ADVOCATEMASTER_DETAILTYPE = "lawyer";

	// Script Names
	public static final String SCRIPT_LEGALCASE_SEARCH_WORKFLOW = "legalcase.search.workflow";
	public static final String SCRIPT_LEGALCASE_LCNUMBER = "legalcase.lcnumber.script";
	public static final String SCRIPT_LEGALCASE_CASENUMBER = "legalcase.casenumber.script";

	// Named Queries
	public static final String QUERY_GET_CASEDISPOSAL_BY_LCNUMBER = "getCasedisposalByLcnumber";
	public static final String QUERY_GET_LEGALCASE_BY_LCNUMBER = "getLegalCaseByLcnumber";
	public static final String QUERY_GET_CONTEMPT_BY_ID = "getContemptById";
	public static final String QUERY_GET_JUDGMENTIMPL_BY_LCNUMBER = "getJudgmentImplByLcnumber";
	public static final String QUERY_GET_HEARINGS_BY_LEGALCASE = "getHearingsByLegalcase";
	public static final String QUERY_GET_JUDGMENT_FOR_PARENTID = "getJudgmentForParentId";
	public static final String QUERY_GET_MISCDETAILS_BY_LEGALCASEANDSTATUS = "getMiscDetailsByLegalcaseAndStatus";
	public static final String QUERY_FIND_LEGALCASE_BY_ID = "getLegalCaseById";
	public static final String QUERY_LEGALCASE_FOR_JUNIOR_ADV = "getLegalCaseByJuniorAdvocateId";
	public static final String QUERY_LEGALCASE_FOR_SENIOR_ADV = "getLegalCaseBySeniorAdvocateId";
	public static final String QUERY_FIND_CASESTAGE_BY_ID = "getCaseStageById";
	public static final String QUERY_CASE_STAGE_NOT_FILEDBY_CORP = "getAllCaseStages.By.Excluding.ParamStage";
	public static final String QUERY_CASE_STAGE_FILEDBY_CORP = "getAllCaseStages.By.Excluding.CreateStage";
	public static final String QUERY_FIND_ADVOCATEMASTER_BY_ID = "getAllAdvocateMasterById";
	public static final String QUERY_NOT_PAID_ADV_FOR_MONTH_AND_YEAR = "getNotPaidAdvocateByMonthAndYear";
	public static final String QUERY_ALL_ACTIVE_ADVOCATES = "getAllActiveAdvocateMaster";
	public static final String QUERY_HEARINGBY_CASE_HEARINGDATE_ID = "getHearingsByLegalcaseandHearingDateandId";
	public static final String QUERY_HEARINGBY_CASE_HEARINGDATE = "getHearingsByLegalcaseandHearingDate";
	public static final String QUERY_FUTUREHEARINGBY_CASE_HEARINGDATE = "getFutureHearingsByLegalcaseandHearingDate";
	public static final String QUERY_FUTUREHEARINGBY_CASE_HEARINGDATE_ID = "getFutureHearingsByLegalcaseandHearingDateandId";
	public static final String QUERY_GET_INTERIMORDERBY_LEGALCASE_ID = "getLcinterimorderbyLegalcase";
	public static final String QUERY_HEARINGSBY_HEARINGDATE_FOR_DAILYBOARD = "getHearingsByHearingDate";
	// appconfig keys
	public static final String BILL_WF_MANUAL = "BILL_WORKFLOW_MANUAL";

	public static final String mixedChar = "^[a-z|A-Z|]+[a-z|A-Z|&/ :,-.]*";
	public static final String alphaNumeric = "[0-9a-zA-Z]+";
	public static final String mixedCharType1 = "^[a-z|A-Z|]+[a-z|A-Z|0-9|&/() .]*";
	public static final String caseNumberRegx = "[0-9a-zA-Z-&/() .]+";
	public static final String numeric = "[0-9]+";
	public static final String alphabets = "[A-Za-z]+";
	public static final String alphaNumericwithSpace = "[0-9a-zA-Z ]+";
	public static final String alphaNumericwithSlashes = "[0-9a-zA-Z/]+";
	public static final String email = "^[\\w\\.-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$";
	public static final String numericiwithMixedChar = "[0-9-,]+";
	public static final String lengthCheckForMobileNo = "^((\\+)?(\\d{2}[-]))?(\\d{10}){1}?$";
	public static final String numericiValForPhoneNo = "[0-9-,()OR]+";
	public static final String orderNumberFormat = "[0-9a-zA-Z-&/(){}\\[\\]]+";
	// public static final String dateFormat = "(0[1-9]|[12][0-9]|3[01])[-
	// /.](0[1-9]|1[012])[- /.](19|20)[0-9]{2}";
	public static final String searchMixedCharType1 = "[0-9a-zA-Z-&/*]+";
	public static final String mixedCharType1withComma = "^[a-z|A-Z|]+[a-z|A-Z|0-9|&/() .,]*";
	public static final String referenceNumberTIRegx = "[0-9a-zA-Z-&/() .]+";

	public static final String COURTNAME = "Court Name";
	public static final String COURTTYPE = "Court Type";
	public static final String CASECATEGORY = "Case Category";
	public static final String PETITIONTYPE = "Petition Type";
	public static final String OFFICERINCHRGE = "In charge officer";
	public static final String CASESTATUS = "Case Status";
	public static final String STANDINGCOUNSEL = "Standing Counsel";
	public static final String JUDGEMENTOUTCOME = "Judgment outcome";
	public static final String DUEPWRREPORT = "PWR due date";
	public static final String DUECAREPORT = "CA Due date";
	public static final String DUEJUDGEMENTIMPLPREPORT = "Judgement Implementation";
	public static final String DUEEMPLOYEEHEARINGREPORT = "Employee Hearings";
	public static final String LEGALCASE_DOCUMENTNAME = "LegalCase";
	public static final String JUDGMENT_DOCUMENTNAME = "Judgment";
	public static final String LCINTERIOMORDER_DOCUMENTNAME ="LcInterimOrder";
	public static final String PWR_DOCUMENTNAME ="Pwr";
	public static final String APPEAL_DOCUMENTNAME ="Appeal";
	
	public static final String CODE_REPORTSTATUS_COUNTERFILED = "COUNTER_FILED";
        public static final String CODE_REPORTSTATUS_PWRPENDING = "PWR_PENDING";
        public static final String CODE_REPORTSTATUS_DCAPENDING = "DCA_PENDING";
        
        public static final String DATE_FORMAT_DDMMYYYY = "dd-MM-yyyy";
        public static final SimpleDateFormat DATEFORMATTER_DD_MM_YYYY = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY );
        
        public static final String LEGALCASE_INDEX_NAME = "legalcasedocument";
        public static final String HEARINGS_INDEX_NAME = "hearingsdocument";
        
        public static final String LEGALCASE_STATUS_JUDGMENT_IMPLIMENTED_DESC = "Judgment Implemented";
        public static final String LEGALCASE_STATUS_CLOSED_DESC = "Closed";
        public static final String LEGALCASE_STATUS_HEARING_DESC = "Hearing In Progress";
        public static final String LEGALCASE_INTERIMSTAY_STATUS_DESC = "Interim Stay";
        public static final String LEGALCASE_STATUS_CREATED_DESC = "Created";
        
        public static final String STANDINGCOUNSEL_ROLES="STANDINGCOUNSEL_ROLES";
      
}
