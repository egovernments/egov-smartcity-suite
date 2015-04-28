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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.constants;

public interface PropertyTaxConstants {
	// General constants used across Clients
	public static final String PTMODULENAME = "Property Tax";
	public static final String PTISCONFIGFILE = "ptis_egov_config.xml";
	public static final String DEACTIVATION = "DEACTIVATION";
	public static final String DATE_FORMAT_DDMMYYY = "dd/MM/yyyy";
	public static final String SESSIONLOGINID = "com.egov.user.LoginUserId";
	public static final String SESSION_VAR_LOGIN_USER_NAME = "com.egov.user.LoginUserName";
	public static final String CITIZENUSER = "citizenUser";
	public static final String PROP_ADDR_TYPE = "PROPERTY";
	public static final String OWNER_ADDR_TYPE = "OWNER";
	public static final String TRANSFER = "TRANSFER";

	// Demand Category Constants used across Clients
	public static final String REASON_CATEGORY_CODE_TAX = "TAX";
	public static final String REASON_CATEGORY_CODE_PENALTY = "PENALTY";
	public static final String REASON_CATEGORY_CODE_ADVANCE = "ADVANCE";
	public static final String REASON_CATEGORY_CODE_FINES = "FINES";

	// Demand Reason Constants used across Clients
	public static final String ADVANCE_DMD_RSN_CODE = "ADVANCE";
	public static final String PENALTY_DMD_RSN_CODE = "PENALTY";
	public static final String LPPAY_PENALTY_DMDRSNCODE = "PENALTY_FINES";

	// Others
	public static final String CURR_DMD_STR = "CURR_DMD";
	public static final String ARR_DMD_STR = "ARR_DMD";
	public static final String CURR_COLL_STR = "CURR_COLL";
	public static final String ARR_COLL_STR = "ARR_COLL";
	public static final String CURRENT_REBATE_STR = "CURRENT_REBATE";
	public static final String ARREAR_REBATE_STR = "ARREAR_REBATE";

	public static final String BUILT_UP_PROPERTY = "BuiltUpProperty";
	public static final String VACANT_PROPERTY = "VacantProperty";

	// objection status CODE values
	public static final String OBJECTION_MODULE = "PTObejction";
	public static final String OBJECTION_CREATED = "CREATED";
	public static final String OBJECTION_HEARING_FIXED = "HEARING DATE FIXED";
	public static final String OBJECTION_HEARING_COMPLETED = "HEARING COMPLETED";
	public static final String OBJECTION_INSPECTION_COMPLETED = "INSPECTION COMPLETED";
	public static final String OBJECTION_ACCEPTED = "OBJECTION ACCEPTED";
	public static final String OBJECTION_REJECTED = "OBJECTION REJECTED";

	public static final String OBJECTION_ADDHEARING_DATE = "ADD HEARING DATE";
	public static final String OBJECTION_RECORD_HEARINGDETAILS = "RECORD HEARING DETAILS";
	public static final String OBJECTION_RECORD_INSPECTIONDETAILS = "RECORD INSPECTION DETAILS";
	public static final String OBJECTION_RECORD_OBJECTIONOUTCOME = "RECORD OBJECTION OUTCOME";
	public static final String OBJECTION_RECORD_SAVED = "OBJECTION RECORD";
	public static final String OBJECTION_HEARINGDATE_SAVED = "HEARING DATE";

	public static final String RECOVERY_MODULE = "PTRecovery";
	public static final String RECOVERY_NOTICE155CREATED = "NOTICE155CREATED";
	public static final String RECOVERY_NOTICE155GENERATIONPENDING = "NOTICE155GENERATIONPENDING";
	public static final String RECOVERY_NOTICE155GENERATED = "NOTICE155GENERATED";
	public static final String RECOVERY_WARRANTPREPARED = "WARRANT APPLICATION PREPARED";
	public static final String RECOVERY_WARRANTAPPROVED = "WARRANT APPLICATION APPROVED";
	public static final String RECOVERY_WARRANTNOTICECREATED = "WARRANT NOTICE CREATED";
	public static final String RECOVERY_WARRANTNOTICEISSUED = "WARRANT NOTICE ISSUED";
	public static final String RECOVERY_CEASENOTICECREATED = "CEASE NOTICE CREATED";
	public static final String RECOVERY_CEASENOTICEISSUED = "CEASE NOTICE ISSUED";

	// GIS Search Property
	public static final String SRCH_BOUNDARY_ID = "BOUNDARYID";
	public static final String SRCH_OWNER_NAME = "OWNERNAME";
	public static final String SRCH_NEW_HOUSE_NO = "NEWHOUSENO";
	public static final String SRCH_OLD_HOUSE_NO = "OLDHOUSENO";
	public static final String SRCH_PROPERTY_TYPE = "PROPERTYTYPE";
	public static final String SRCH_DEMAND_FROM_AMOUNT = "DMD_FROM_AMOUNT";
	public static final String SRCH_DEMAND_TO_AMOUNT = "DMD_TO_AMOUNT";
	public static final String SRCH_DEFAULTER_FROM_AMOUNT = "DEFAULTER_FROM_AMOUNT";
	public static final String SRCH_DEFAULTER_TO_AMOUNT = "DEFAULTER_TO_AMOUNT";

	// Property status values
	public static final Character STATUS_ISHISTORY = 'H';
	public static final Character STATUS_ISACTIVE = 'A';
	public static final Character STATUS_WORKFLOW = 'W';
	public static final Character STATUS_OBJECTED = 'O';
	public static final Character STATUS_DEMAND_INACTIVE = 'I';
	public static final Character STATUS_CANCELLED = 'C';

	// Bill types
	public static final String BILLTYPE_MANUAL = "MANUAL";
	public static final String BILLTYPE_AUTO = "AUTO";

	public static final Character CANCELLED_RECEIPT_STATUS = 'C';

	// PTIS MODULEID IN EG_MODULES
	public static final String PTIS_EG_MODULES_ID = "2";

	// Demand Status when Cheque bounced/cheque amount paid
	public static final Character DMD_STATUS_CHEQUE_BOUNCED = 'B';
	public static final Character DMD_STATUS_CHQ_BOUNCE_AMOUNT_PAID = 'P';
	public static final Character DMD_STATUS_NO_CHQ_BOUNCED = 'N';

	// Named Queries
	public static final String QUERY_PROPERTYIMPL_BYID = "PROPERTYIMPL_BYID";
	public static final String GET_PROPERTY_TYPES = "getPropertyTypes";
	public static final String QUERY_STATUS_BY_MODULE_AND_CODE = "getStatusByModuleAndCode";
	public static final String QUERY_PROP_STATUS_BY_STATUSCODE = "getPropStatusByStatusCode";

	public static final String STRING_NOT_AVAILABLE = "N/A";

	public static final String PTIS_COLLECTION_SERVICE_CODE = "PT";

	public static final String STATUS_OBJECTED_STR = "OBJECTED";

	public static final String STR_YES = "Yes";
	
	public static final String MUTATION_FEE_STR = "MUTATION FEE";

}
