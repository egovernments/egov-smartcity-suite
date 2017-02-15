/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */

package org.egov.adtax.utils.constants;

public class AdvertisementTaxConstants {
    public static final String MODULE_NAME = "Advertisement Tax";
    public static final String MODULETYPE = "adtax";
    public static final String YEARLY = "Yearly";
    public static final String MONTHLY = "Monthly";
    public static final String DEMANDREASON_ADVERTISEMENTTAX = "Advertisemnt_Tax";
    public static final String DEMANDREASON_ENCROCHMENTFEE = "Enchroachmnt_Fee";
    public static final String DEMANDREASON_ARREAR_ADVERTISEMENTTAX = "Arrear_Adv_Tax";
    public static final String SERVICE_CODE = "ADTAX";
    public static final String COLL_RECEIPTDETAIL_DESC_PREFIX = "Collection";
    public static final String PENALTYCALCULATIONREQUIRED = "Penalty Calculation required";
    public static final String SERVICETAXANDCESSCOLLECTIONREQUIRED =  "Service Tax And Cess Collection required";
    public static final String TOTALRESULTTOBEFETCH = "Number of Records used in next year demand generation";
    public static final String CALCULATESORBYUNIT = "Calculate SOR By Unit";
    public static final String DEMANDREASON_PENALTY = "Penalty";
    public static final String PENALTYAMOUNT = "PENALTYAMOUNT";
    public static final String PENDINGDEMANDAMOUNT = "PENDINGDEMANDAMOUNT";
    public static final String AGENCY_PREFIX_CONSUMERCODE = "AGENCY-";

    public static final String BOUNDARYTYPE_LOCALITY = "locality";
    public static final String BOUNDARYTYPE_ELECTIONWARD = "Ward";
    public static final String BOUNDARYTYPE_ZONE = "Zone";
    public static final String LOCATION_HIERARCHY_TYPE = "LOCATION";
    public static final String ADMINISTRATION_HIERARCHY_TYPE = "REVENUE";
    public static final String ELECTION_HIERARCHY_TYPE = "ADMINISTRATION";

    public static final String APPLICATION_MODULE_TYPE = "ADVERTISEMENT";
    public static final String APPLICATION_STATUS_CREATED = "CREATED";
    public static final String APPLICATION_STATUS_APPROVED = "APPROVED";
    public static final String APPLICATION_STATUS_ADTAXAMOUNTPAID = "ADTAXAMTPAYMENTPAID";
    public static final String APPLICATION_STATUS_ADTAXPERMITGENERATED = "ADTAXPERMITGENERATED";

    public static final String WF_APPROVE_BUTTON = "Approve";
    public static final String WF_REJECT_BUTTON = "REJECT";
    public static final String WF_CANCELAPPLICATION_BUTTON = "CANCEL APPLICATION";
    public static final String WF_CANCELRENEWAL_BUTTON = "CANCEL RENEWAL";
    
    public static final String WF_NEW_STATE = "NEW";
    public static final String WF_REJECT_STATE = "Rejected";
    public static final String WF_END_STATE = "END";
    public static final String NATURE_OF_WORK = "Advertisement";
    public static final String CREATE_ADDITIONAL_RULE = "CREATEADVERTISEMENT";
    public static final String COLON_CONCATE = "::";
    public static final String ADVERTISEMENT_COLLECTION_TYPE = "Advertisement";
    public static final String FEECOLLECTIONMESSAGE = "Fee Collection : Advertisement Number -";

    public static final String STRING_DEPARTMENT_CODE = "REV";
    public static final String DEFAULT_FUNCTIONARY_CODE = "1";
    public static final String DEFAULT_FUND_SRC_CODE = "01";
    public static final String DEFAULT_FUND_CODE = "01";
    public static final String FEECOLLECTION = "Fee Collection";
    public static final String ADVERTISEMENT_FUCNTION_CODE = "9300";
    public static final String APPCONFIG_FUCNTION_CODE="Function Code";
    public static final String BILL_TYPE_AUTO = "AUTO";
    public static final String WARD = "Ward";
    public static final String ADDRESSTYPEASOWNER = "OWNER";
    public static final String APPLICATION_STATUS_CANCELLED = "CANCELLED";
    public static final String WF_PERMITORDER_BUTTON = "GENERATE PERMIT ORDER";
    public static final String WORKFLOW_ACTION = "workFlowAction";
    public static final String PERMITORDER = "permitOrder";
    public static final String WF_DEMANDNOTICE_BUTTON = "GENERATE DEMAND NOTICE";
    public static final String COLLECTION_REMARKS = "Collection done";
    public static final String DEMANDNOTICE = "demandNotice";

    public static final String IMAGES_BASE_PATH = "/egi/resources/global/images/";
    public static final String IMAGE_CONTEXT_PATH = "/egi";
    public static final String ADVERTISEMENTPERMITODERTITLE = "Advertisement Permit Order";
    public static final String ADVERTISEMENTDEMANDNOTICETITLE = "Advertisement Demand Notice";
    public static final String RENEWAL_ADDITIONAL_RULE = "RENEWADVERTISEMENT";
    public static final String TOTAL_DEMAND = "Demand";
    public static final String PENDINGAMOUNT = "Pending Amount";
    public static final String TOTALCOLLECTION = " Total Collection";
    public static final String PERMIT_STATUS_PAYMENTPENDING="ADTAXAMTPAYMENTPENDING";
    
    //CSC operator related constants
    public static final String ADTAX_ROLEFORNONEMPLOYEE = "ADTAXROLEFORNONEMPLOYEE";
    public static final String CSC_OPERATOR_ROLE = "CSC Operator";
    public static final String ADTAX_WORKFLOWDESIGNATION_FOR_CSCOPERATOR = "ADTAXDESIGNATIONFORCSCOPERATORWORKFLOW";
    public static final String ADTAX_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR = "ADTAXDEPARTMENTFORCSCOPERATORWORKFLOW";
    public static final String ADTAX_WORKFLOWDESIGNATION = "ADTAXDESIGNATIONFORWORKFLOW";
    public static final String ADTAX_WORKFLOWDEPARTEMENT = "ADTAXDEPARTMENTFORWORKFLOW";

}
