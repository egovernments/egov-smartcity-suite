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
package org.egov.services.receipt;

import org.egov.egf.commons.EgovCommon;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.receipt.ReceiptVoucher;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EmployeeServiceOld;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiptService extends PersistenceService<ReceiptVoucher, Long> {
    @Autowired
    protected EisCommonService eisCommonService;
    private @Autowired AppConfigValueService appConfigValuesService;
    private EmployeeServiceOld employeeServiceOld;
    private PersistenceService persistenceService;
    private @Autowired EgovCommon egovCommon;
    public ReceiptService() {
        super(ReceiptVoucher.class);
    }

    public ReceiptService(Class<ReceiptVoucher> type) {
        super(type);
    }

    public Position getPositionForEmployee(final Employee emp) throws ApplicationRuntimeException
    {
        return eisCommonService.getPrimaryAssignmentPositionForEmp(emp.getId());
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void createVoucherfromPreApprovedVoucher(final ReceiptVoucher rv) {
        final List<AppConfigValues> appList = appConfigValuesService
                .getConfigValuesByModuleAndKey("EGF", "APPROVEDVOUCHERSTATUS");
        final String approvedVoucherStatus = appList.get(0).getValue();
        rv.getVoucherHeader().setStatus(Integer.valueOf(approvedVoucherStatus));
    }

    public void cancelVoucher(final ReceiptVoucher rv) {
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF", "cancelledstatus");
        final String approvedVoucherStatus = appList.get(0).getValue();
        rv.getVoucherHeader().setStatus(Integer.valueOf(approvedVoucherStatus));
    }

    public String getDesginationName()
    {
        // TODO: Now employee is extending user so passing userid to get assingment -- changes done by Vaibhav
        final Assignment assignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(ApplicationThreadLocals.getUserId(),
                new Date());
        return assignment.getDesignation().getName();
    }

    public Department getDepartmentForWfItem(final ReceiptVoucher rv)
    {
        // TODO: Now employee is extending user so passing userid to get assingment -- changes done by Vaibhav
        final Assignment assignment = eisCommonService.getLatestAssignmentForEmployeeByToDate(rv.getCreatedBy().getId(),
                new Date());
        return assignment.getDepartment();
    }

    public Position getPositionForWfItem(final ReceiptVoucher rv)
    {
        return eisCommonService.getPositionByUserId(rv.getCreatedBy().getId());
    }

    public Boundary getBoundaryForUser(final ReceiptVoucher rv)
    {
        return egovCommon.getBoundaryForUser(rv.getCreatedBy());
    }

    public Department getDepartmentForUser(final User user)
    {
        return egovCommon.getDepartmentForUser(user, eisCommonService, employeeServiceOld, persistenceService);
    }

    public void setEmployeeServiceOld(final EmployeeServiceOld employeeServiceOld) {
        this.employeeServiceOld = employeeServiceOld;
    }

    // TODO : Need to move to collection
    public String getReceiptHeaderforDishonor(final String mode, final Long bankAccId, final Long bankId,
            final String chequeDDNo, final String chqueDDDate) {
        final StringBuilder sb = new StringBuilder(300);
        new ArrayList<Object>();
        sb.append("FROM egcl_collectionheader rpt,egcl_collectioninstrument ci,egf_instrumentheader ih,egf_instrumenttype it,egw_status status,bank b,"
                + "bankbranch bb,bankaccount ba WHERE rpt.id = ci.collectionheader AND ci.instrumentheader = ih.id AND status.id = ih.id_status "
                + "AND b.id = bb.bankid AND bb.id = ba.branchid AND ba.id = ih.bankaccountid AND ih.instrumenttype = it.id and it.type  = '"
                + mode
                + "' AND ((ih.ispaycheque ='0' AND status.moduletype ='Instrument' "
                + "AND status.description = 'Deposited') OR (ih.ispaycheque = '1' AND status.moduletype = 'Instrument' AND status.description = 'New'))");
        /*
         * sb.append("from org.egov.collection.entity.ReceiptHeader rpt join " +
         * "rpt.receiptInstrument ih where ih.instrumentType.type=? " +
         * "and ((ih.isPayCheque=0 and ih.statusId.moduletype='Instrument' and ih.statusId.description='Deposited') or " +
         * "(ih.isPayCheque=1 and ih.statusId.moduletype='Instrument' and ih.statusId.description='New'))");
         */

        if (bankAccId != null && bankAccId != 0 && bankAccId != -1)
            sb.append(" AND ih.bankaccountid=" + bankAccId + "");
        if ((bankAccId == null || bankAccId == 0) && bankId != null
                && bankId != 0)
            sb.append(" AND ih.bankid=" + bankAccId + "");
        if (!"".equals(chequeDDNo) && chequeDDNo != null)
            sb.append(" AND ih.instrumentnumber=trim('" + chequeDDNo + "') ");
        if (!"".equals(chqueDDDate) && chqueDDDate != null)
            sb.append(" AND ih.instrumentdate >= '" + chqueDDDate + "' ");

        return sb.toString();
    }
}