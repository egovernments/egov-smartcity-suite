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
package org.egov.works.services.contractoradvance;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgwStatus;
import org.egov.eis.entity.DrawingOfficer;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ContractorAdvanceService {

    /**
     * @param contractorAdvanceRequisition , actionName, advanceAccountCode @ save Contractor Advance Object and return
     * ContractorAdvanceRequisition
     * @throws ValidationException
     */
    public ContractorAdvanceRequisition save(ContractorAdvanceRequisition contractorAdvanceRequisition,
            String actionName, Long advanceAccountCode) throws ValidationException;

    /**
     * @param Id
     * @return ContractorAdvanceRequisition Object by passing Id
     * @throws ValidationException
     */
    public ContractorAdvanceRequisition getContractorAdvanceRequisitionById(Long Id) throws ValidationException;

    /**
     * @param workOrderEstimateId , contractorAdvanceRequisitionId
     * @return Advance already paid by passing workOrderEstimateId
     * @throws ValidationException
     */
    public BigDecimal getAdvancePaidByWOEstimateId(Long workOrderEstimateId) throws ValidationException;

    /**
     * @param workOrderEstimateId , contractorAdvanceRequisitionId
     * @return Advance already paid by passing workOrderEstimateId and contractorAdvanceRequisitionId. It gets all the advance
     * made till this ARF. It is used in modify(workflow) and view.
     * @throws ValidationException
     */
    public BigDecimal getAdvancePaidByWOEstIdForView(Long workOrderEstimateId, Long contractorAdvanceRequisitionId)
            throws ValidationException;

    /**
     * Get List of Contractor Advance payable account codes based on Account code Purpose 'CONTRACTOR_ADVANCE_ACCOUNTCODE'
     */
    public List<CChartOfAccounts> getContractorAdvanceAccountcodes() throws ValidationException;

    /**
     * @param abstractEstimate
     * @return Estimate value including RE's if any
     * @throws ValidationException
     */
    public BigDecimal getTotalEstimateValueIncludingRE(AbstractEstimate abstractEstimate) throws ValidationException;

    /**
     * @param contractorAdvanceRequisition
     * @return the functionary as UAC for the workflow
     * @throws ValidationException
     */
    public Integer getFunctionaryForWorkflow(ContractorAdvanceRequisition contractorAdvanceRequisition);

    /**
     * @param contractorAdvanceRequisition , actionName Cancel Contractor Advance Object
     * @throws ValidationException
     */
    public void cancelContractorAdvanceRequisition(ContractorAdvanceRequisition contractorAdvanceRequisition,
            String actionName) throws ValidationException;

    /**
     * @return get all Status for ContractorAdvanceRequisition object excluding 'NEW' status
     */
    public List<EgwStatus> getAllContractorAdvanceRequisitionStatus();

    /**
     * @return get all distinct DrawingOfficers from ContractorAdvanceRequisition object which are already created
     */
    public List<DrawingOfficer> getAllDrawingOfficerFromARF();

    /**
     * Get List of Drawing officer from given list of designations which are read from app config values based on the date passed
     * and for given search criteria from auto complete as name or code(query string)
     */
    public List<HashMap> getDrawingOfficerListForARF(String query, Date advanceRequisitionDate);

    /**
     * @param workOrderEstimateId
     * @return Advance COA object used in Advance Requisition of Work Order estimate
     */
    public CChartOfAccounts getContractorAdvanceAccountcodeForWOE(Long workOrderEstimateId);

    /**
     * @param workOrderEstimateId , asOnDate
     * @return Advance already paid by passing workOrderEstimateId and asOnDate where Payment Voucher Status=0(Approved)
     */
    public BigDecimal getTotalAdvancePaymentMadeByWOEstimateId(Long workOrderEstimateId, Date asOnDate);

    /**
     * @param workOrderEstimateId
     * @return ContractorAdvanceRequisition object which is in work flow
     */
    public ContractorAdvanceRequisition getContractorARFInWorkflowByWOEId(Long workOrderEstimateId);
}
