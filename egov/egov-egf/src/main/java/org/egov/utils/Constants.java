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
package org.egov.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {
    public static final String SUBSCHEMES = "subSchemes";
    public final static Integer INT_ZERO = 0;
    public final static Long LONG_ZERO = Long.valueOf(0);
    public final static Long LONG_ONE = Long.valueOf(1);
    public static final String SEARCH_CRITERIA_KEY = "searchCriteria";
    public static final String MODE = "mode";
    public static final String LIST = "list";
    public static final String DETAILLIST = "detailList";
    public static final String BUDGETS = "budgets";
    public static final String MODIFYLIST = "modifyList";
    public static final String APPROVE = "approve";
    public static final String MODIFY = "modify";
    public static final String ACTION = "action";
    public static final String ASONDATE = "asondate";
    public static final String FINANCIALYEARID = "financialyearid";
    public static final String SAVE_AND_NEW = "save_and_new";
    public static final String SAVED_DATA = "savedData";
    public static final String INPUT_STREAM = "inputStream";
    public static final String INPUT_NAME = "inputName";
    public static final String EGF = "EGF";
    public static final String CONTENT_TYPE = "contentType";
    public static final String CONTENT_DISPOSITION = "contentDisposition";
    public static final String DEPARTMENT = "department";
    public static final String FUND = "fund";
    public static final String SCHEME = "scheme";
    public static final String SUBSCHEME = "subscheme";
    public static final String FUNCTION = "function";
    public static final String FUNCTIONARY = "functionary";
    public static final String FUNDSOURCE = "fundsource";
    public static final String FIELD = "field";
    public static final String MASTER_DATA_DELETED = "This Entity Missing(Deleted) in Masters *";
    public static final String VOUCHERERRORMESSAGE = "Loaded with Errors refer the * ";
    public static final String GLCODE = "glcode";
    public static final String DEBITAMOUNT = "debitamount";
    public static final String CREDITAMOUNT = "creditamount";
    public static final String DETAILKEY = "detailkey";
    public static final String DETAILCODE = "detailcode";
    public static final String DETAILTYPE_NAME = "detailname";
    public static final Locale LOCALE = new Locale("en", "IN");
    public static final String BOUNDARY = "boundary";
    public static final String SUB_SCHEME = "subScheme";
    public static final String EXECUTING_DEPARTMENT = "executingDepartment";
    public static final String COMMONSMANAGERHOME = "CommonsServiceHome";

    public static final String DEPTID = "deptid";
    public static final String FUNDID = "fundid";
    public static final String SCHEMEID = "schemeid";
    public static final String SUBSCHEMEID = "subschemeid";
    public static final String FUNCTIONID = "functionid";
    public static final String FUNCTIONARYID = "functionaryid";
    public static final String BOUNDARYID = "boundaryid";
    public static final String BUDGET_GROUP = "budgetGroup";
    public static final String BUDGET = "budget";
    public static final String STATE = "state";
    public static final String TOTAL_ASSETS = "Total ASSETS";
    public static final String TOTAL_LIABILITIES = "Total LIABILITIES";
    public static final String TOTAL = "Total";
    public static final String LIABILITIES = "LIABILITIES";
    public static final String ASSETS = "ASSETS";
    public static final String RE = "RE";
    public static final String BE = "BE";
    public static final String ZERO = "0.0";
    public static final String BANK = "bank";
    public static final String CHEQUE = "cheque";
    public static final String RTGS = "rtgs";
    public static final String BANKENTRY = "Bank Entry";
    public static final String DATEFORMAT = "dd-MMM-yyyy";
    public static final String EGFCONFIGXML = "egf_config.xml";
    public static final String VOUCHERNUMBER = "voucherNumber";
    public static final String VOUCHERDATEFROM = "voucherDateFrom";
    public static final String VOUCHERDATETO = "voucherDateTo";
    public static final String VOUCHERHEADER = "voucherHeader";
    public static final String GLDEATILLIST = "glDetailList";
    public static final String VOUCHERDATE = "voucherDate";
    public static final String CREATED = "CREATED";

    public static final String DEPARTMENTID = "departmentId";

    public static final SimpleDateFormat DD_MON_YYYYFORMAT = new SimpleDateFormat("dd-MMM-yyyy", LOCALE);
    public static final SimpleDateFormat DDMMYYYYFORMAT1 = new SimpleDateFormat("dd-MMM-yyyy", LOCALE);
    public static final SimpleDateFormat DDMMYYYYFORMAT2 = new SimpleDateFormat("dd/MM/yyyy", LOCALE);
    public static final String END = "END";

    // These are the standard buttons used in transactions accrross the product
    public static final String SAVECLOSE = "Save & Close";
    public static final String SAVEVIEW = "Save & View";
    public static final String SAVENEW = "Save & New";

    public static final String EDIT = "edit";
    public static final String VIEW = "view";
    public static final String PURCHASE_BILL_PURPOSE_IDS = "purchaseBillPurposeIds";
    public static final String WORKS_BILL_PURPOSE_IDS = "worksBillPurposeIds";
    public static final String CONTINGENCY_BILL_PURPOSE_IDS = "contingencyBillPurposeIds";
    public static final String PENSION_BILL_PURPOSE_IDS = "pensionBillPurposeIds";
    public static final String CHEQUE_NO_GENERATION_APPCONFIG_KEY = "Cheque_no_generation_auto";
    public static final String INCOME = "INCOME";
    public static final String EXPENDITURE = "EXPENDITURE";
    public static final String TOTAL_INCOME = "Total INCOME";
    public static final String TOTAL_EXPENDITURE = "Total EXPENDITURE";
    public static final String ADVANCE_PAYMENT = "Advance Payment";
    public static final String MODULEID = "module id";
    public static final String CONSUMEORRELEASE = "consume or release";
    public static final String REFERENCENUMBER = "reference number";
    public static final String BUDGETHEAD = "budget heed";
    public static final String AMOUNT = "amount";
    public static final String APPROPRIATIONNUMBER = "budget appr number";
    public static final String RECOVERY = "recovery";
    public static final String PERIOD_FIRSTHALF = "I-Half";
    public static final String PERIOD_SECONDHALF = "II-Half";
    public static final String PERIOD_QUARTER1 = "Quarter-I";
    public static final String PERIOD_QUARTER2 = "Quarter-II";
    public static final String PERIOD_QUARTER3 = "Quarter-III";
    public static final String PERIOD_QUARTER4 = "Quarter-IV";
    public static final String PERIOD_MONTH1 = "Jan";
    public static final String PERIOD_MONTH2 = "Feb";
    public static final String PERIOD_MONTH3 = "Mar";
    public static final String PERIOD_MONTH4 = "Apr";
    public static final String PERIOD_MONTH5 = "May";
    public static final String PERIOD_MONTH6 = "Jun";
    public static final String PERIOD_MONTH7 = "Jul";
    public static final String PERIOD_MONTH8 = "Aug";
    public static final String PERIOD_MONTH9 = "Sep";
    public static final String PERIOD_MONTH10 = "Oct";
    public static final String PERIOD_MONTH11 = "Nov";
    public static final String PERIOD_MONTH12 = "Dec";
    public static final String GRANT_TYPE_SFC = "State Finance Commission";
    public static final String GRANT_TYPE_CFC = "Central Finance Commission";
    public static final String GRANT_TYPE_ET = "Entertainment Tax";
    public static final String GRANT_TYPE_SD = "Stamp Duty";
    
    public static final String VIEW_COA = "view coa";
    public static final String VIEW_MODIFY_COA = "view-modify coa";
}
