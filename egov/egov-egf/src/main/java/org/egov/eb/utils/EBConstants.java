/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.eb.utils;

import org.egov.infstr.utils.EGovConfig;
import org.egov.utils.FinancialConstants;

public class EBConstants {
	
	
			
	//TNEB Billing Types
			public static final String Billing_Odd_Month = "ODD";
			public static final String Billing_Even_Month = "EVEN";
			
			public static final String STATUS_TNEB_BILL = "TNEB Bill";
	//TNEB bill statuses 
			public static final String CODE_BILLINFO_RECEIVED = "RECEIVED";
			public static final String CODE_BILLINFO_CREATED = "CREATED";
			public static final String CODE_BILLINFO_CANCELLED = "CANCELLED";
			public static final String CODE_BILLINFO_APPROVED = "APPROVED";
			public static final String CODE_BILLINFO_REJECTED = "REJECTED";
			public static final String CODE_BILLINFO_RECEIVE_FAILED = "RECEIVE_FAILED";
			
	public static final String ERROR_MESSAGE = "An error has occured please contact Administrator";
	
	public static final String STATUS_APPROVED_EXPENSE_BILL = "Approved";

	public static final String TNEB_WEBSERVICE_URL = EGovConfig.getProperty(FinancialConstants.APPLCONFIGNAME, "URL", "", "TNEBWebService");
	public static final String TNEB_WEBSERVICE_USERNAME = EGovConfig.getProperty(FinancialConstants.APPLCONFIGNAME, "username", "", "TNEBWebService");
	public static final String TNEB_WEBSERVICE_PASSWORD = EGovConfig.getProperty(FinancialConstants.APPLCONFIGNAME, "password", "", "TNEBWebService");
	
	public static final String IOB_RECEIPT_URL = EGovConfig.getProperty(FinancialConstants.APPLCONFIGNAME, "URL", "", "IOBService");
		
	public static final String BILL_SUB_TYPE_NAME = "TNEB";
	public static final String BILL_EXPENDITURE_TYPE = "Expense";
	public static final String BILL_DEBIT_GLCODE_ELECTRICITY = "220110108";
	public static final String BILL_CREDIT_GLCODE = "350100301";
	public static final String BILL_ACCOUNTDETAIL_TYPE_NAME = "Electricity";
	
	public static final String DATE_FORMAT_BILL_DUE_DATE = "dd-MMM-yyyy";
	public static final String APPCONFIG_KEY_NMAE_PAYTO = "TNEB Bill Pay to";
	public static final String EBEXPENSEBILL_APPROVED_STATUS = "Approved";
	public static final String DATE_FORMAT_DDMMYYYY = "ddMMyyyy";
	public static final String TNEB_RESPONSE_NORESPONSE = "No Response";
	public static final String TNEB_RESPONSE_RECEIVED = "Response Received";
	
}


