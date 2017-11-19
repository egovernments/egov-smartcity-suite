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
package org.egov.services.voucher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.commons.CVoucherHeader;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.State;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.TaskFailedException;

@Transactional(readOnly = true)
@Service
public class PreApprovedActionHelper {
    @Autowired
    @Qualifier("journalVoucherActionHelper")
    private JournalVoucherActionHelper journalVoucherActionHelper;
    @Autowired
    @Qualifier("voucherService")
    private VoucherService voucherService;
    @Autowired
    @Qualifier("createVoucher")
    private CreateVoucher createVoucher;

    @Autowired
    PositionMasterService positionMasterService;

    @Autowired
    SecurityUtils securityUtils;
    @Transactional
    public CVoucherHeader createVoucherFromBill(CVoucherHeader voucherHeader, WorkflowBean workflowBean, Long billId,
            String voucherNumber, Date voucherDate) throws ApplicationRuntimeException, SQLException, TaskFailedException {
        try {
            Long voucherHeaderId = createVoucher.createVoucherFromBill(billId.intValue(), null,
                    voucherNumber, voucherDate);
            voucherHeader = voucherService.findById(voucherHeaderId, false);
            voucherHeader = sendForApproval(voucherHeader, workflowBean);
        }catch (final ValidationException e) {
            if (e.getErrors().get(0).getMessage() != null && !e.getErrors().get(0).getMessage().equals(StringUtils.EMPTY))
                throw new ValidationException(e.getErrors().get(0).getMessage(), e.getErrors().get(0).getMessage());
            else
                throw new ValidationException("Voucher creation failed", "Voucher creation failed");

        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return voucherHeader;
    }

    @Transactional
    public CVoucherHeader sendForApproval(CVoucherHeader voucherHeader, WorkflowBean workflowBean)
    {
        if (voucherHeader.getState()!=null && !validateOwner(voucherHeader.getState())) {
            throw new ValidationException("exp","Application does not belongs to this inbox");
        }
        try {

            if (FinancialConstants.CREATEANDAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                    && voucherHeader.getState() == null)
            {
                voucherHeader.setStatus(FinancialConstants.CREATEDVOUCHERSTATUS);
            }
            else
            {
                voucherHeader = journalVoucherActionHelper.transitionWorkFlow(voucherHeader, workflowBean);
                voucherService.applyAuditing(voucherHeader.getState());
            }
            voucherService.persist(voucherHeader);

        } catch (final ValidationException e) {

            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return voucherHeader;
    }

    private Boolean validateOwner(State state) {
        List<Position> positionsForUser = positionMasterService.getPositionsForEmployee(securityUtils.getCurrentUser().getId());
        return positionsForUser.contains(state.getOwnerPosition());

    }


}