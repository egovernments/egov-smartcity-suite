/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.mrs.application;

import java.util.ArrayList;
import java.util.List;

public class MarriageConstants {

    public static final String MODULE_NAME = "Marriage Registration";
    public static final String BOUNDARY_TYPE = "Zone";
    public static final String REVENUE_HIERARCHY_TYPE = "REVENUE";

    public static final String APPROVER_ROLE_NAME = "ULB Operator";
    public static final String DATE_FORMAT_DDMMYYYY = "dd-MM-yyyy";

    public static final String REISSUE_FEECRITERIA = "Re-Issue Fee";
    public static final String ADDITIONAL_RULE_REGISTRATION = "MARRIAGE REGISTRATION";
    public static final String IMAGE_CONTEXT_PATH = "/egi";

    // validactions
    public static final String WFLOW_ACTION_STEP_REJECT = "Reject";
    public static final String WFLOW_ACTION_STEP_CANCEL = "Cancel Registration";
    public static final String WFLOW_ACTION_STEP_CANCEL_REISSUE = "Cancel ReIssue";
    public static final String WFLOW_ACTION_STEP_FORWARD = "Forward";
    public static final String WFLOW_ACTION_STEP_APPROVE = "Approve";
    public static final String WFLOW_ACTION_STEP_DIGISIGN = "Sign";
    public static final String WFLOW_ACTION_STEP_PRINTCERTIFICATE = "Print Certificate";

    // Pendingactions
    public static final String WFLOW_PENDINGACTION_PRINTCERTIFICATE = "Certificate Print Pending";
    public static final String WFLOW_PENDINGACTION_DIGISIGNPENDING = "Digital Signature Pending";
    public static final String WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN = "Commisioner Approval Pending_DigiSign";
    public static final String WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT = "Commisioner Approval Pending_PrintCert";

    public static final String APPROVED = "APPROVED";
    public static final String MARRIAGEFEECOLLECTION_FUCNTION_CODE = "MARRIAGE_FUNCTION_CODE";
    public static final String FILESTORE_MODULECODE = "MRS";
    public static final String SENDSMSFROOMMARRIAGEMODULE = "SENDSMSFROOMMARRIAGEMODULE";
    public static final String SENDEMAILFROOMMARRIAGEMODULE = "SENDEMAILFROOMMARRIAGEMODULE";

    public static final String APPL_INDEX_MODULE_NAME = "Marriage Registration";

    public static final String REGISTER_NO_OF_DAYS = "90";
    public static final String MARRIAGEREGISTRATION_DAYS_VALIDATION = "MARRIAGEREGISTRATION_DAYS_VALIDATION";
    public static final String LOCATION_HIERARCHY_TYPE = "LOCATION";
    public static final String BOUNDARYTYPE_LOCALITY = "locality";
    public static final String REISSUE_PRINTREJECTIONCERTIFICATE = "REISSUE_PRINTREJECTIONCERTIFICATE";

    public static final String MOM = "MoM";
    public static final String CF_STAMP = "CF_STAMP";
    public static final String AFFIDAVIT = "AFFIDAVIT";
    public static final String MIC = "MIC";
    public static final String SCHOOL_LEAVING_CERT = "SLC";
    public static final String BIRTH_CERTIFICATE = "BC";
    public static final String DIVORCE_CERTIFICATE = "DCA";
    public static final String DEATH_CERTIFICATE = "DCSWA";
    public static final String NOTARY_AFFIDAVIT = "NotaryAffidavit";
    public static final String RATION_CRAD = "RationCard";
    public static final String ELECTRICITY_BILL = "MSEBBILL";
    public static final String TELEPHONE_BILL = "TelephoneBill";
    public static final String PASSPORT = "Passport";
    public static final String AADHAR = "Aadhar";
    public static final String YEAR = "year";

    public static final String APPCONFKEY_DIGITALSIGNINWORKFLOW = "DIGITALSIGN_IN_WORKFLOW";

    public static final List<String> venuelist = new ArrayList<String>() {
        private static final long serialVersionUID = -6112513531476444226L;
        {
            add("Residence");
            add("Function Hall");
            add("Worship Place");
            add("Others");
        }
    };

    public static final List<String> witnessRelation = new ArrayList<String>() {
        private static final long serialVersionUID = -8054560659655351886L;
        {
            add("S/o");
            add("D/o");
            add("W/o");
        }
    };

    public static final String MARRIAGE_DEPARTMENT_CODE = "MARRIAGE_DEPARTMENT_CODE";
    public static final String MARRIAGE_DEFAULT_FUNCTIONARY_CODE = "MARRIAGE_DEFAULT_FUNCTIONARY_CODE";
    public static final String MARRIAGE_DEFAULT_FUND_SRC_CODE = "MARRIAGE_DEFAULT_FUND_SRC_CODE";
    public static final String MARRIAGE_DEFAULT_FUND_CODE = "MARRIAGE_DEFAULT_FUND_CODE";

    private MarriageConstants() {
        // To hide implicit public
    }

    public static final String APPROVAL_COMMENT = "approvalComment";
    public static final String APPLICATION_NUMBER = "applicationNumber";
    public static final String FILE_STORE_ID_APPLICATION_NUMBER = "fileStoreIdApplicationNumber";
}