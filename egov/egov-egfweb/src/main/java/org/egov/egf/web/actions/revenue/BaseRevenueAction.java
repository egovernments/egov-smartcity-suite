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
package org.egov.egf.web.actions.revenue;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.egf.revenue.Grant;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.model.instrument.InstrumentHeader;

import java.util.ArrayList;
import java.util.List;



public class BaseRevenueAction extends BaseFormAction {
    private static final long serialVersionUID = 1594209619636642478L;
    protected List<Grant> grantsList;
    protected List<Department> departmentList;
    protected List<CFinancialYear> finYearList;
    protected List<String> periodList;
    protected String grantsType;
    protected List<Grant> grantSearchList;
    protected String mode;
    protected Grant grant = new Grant();

    public BaseRevenueAction() {

    }

    @Override
    public Grant getModel() {

        return grant;
    }

    @Override
    public void prepare()
    {
        mode = "view";
        finYearList = persistenceService.findAllBy("from CFinancialYear  where isActiveForPosting=true order by finYearRange DESC");
        departmentList = persistenceService.findAllBy("from Department order by deptName");
        // addDropdownData("finanYearList",
        // persistenceService.findAllBy("from CFinancialYear  where isActiveForPosting=true order by finYearRange "));

    }

    public String newForm() {
        mode = "create";
        grantsList = new ArrayList<Grant>();
        grantsList.add(new Grant());
        return "new";
    }

    public String beforeModify() {
        final StringBuffer query = new StringBuffer();
        query.append("From Grant gr where gr.financialYear.id=? and gr.grantType=? and gr.department.id=?");
        grantsList = persistenceService.findAllBy(query.toString(), grant.getFinancialYear().getId(), grant.getGrantType(),
                grant.getDepartment().getId());
        if (mode.equals("edit"))
            return "edit";
        else
            return "view";
    }

    public String saveOrupdate() {
        // Grant gtr = grantsList.get(0);
        // //persistenceService.setType(Grant.class);
        for (final Grant gtr : grantsList) {
            gtr.setDepartment((Department) persistenceService.find("from Department where id=?", gtr.getDepartment().getId()));
            gtr.setFinancialYear((CFinancialYear) persistenceService.find("from CFinancialYear where id=?", gtr
                    .getFinancialYear().getId()));
            gtr.setAccrualVoucher((CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", gtr
                    .getAccrualVoucher().getId()));
            if (gtr.getIhID().getId() != null)
                gtr.setIhID((InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?", gtr.getIhID().getId()));
            else
                gtr.setIhID(null);
            if (gtr.getGeneralVoucher().getId() != null)
                gtr.setGeneralVoucher((CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", gtr
                        .getGeneralVoucher().getId()));
            else
                gtr.setGeneralVoucher(null);
            if (gtr.getReceiptVoucher().getId() != null)
                gtr.setReceiptVoucher((CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", gtr
                        .getReceiptVoucher().getId()));
            else
                gtr.setReceiptVoucher(null);
            gtr.setGrantType(getGrantsType());
        }
        //persistenceService.setType(Grant.class);
        for (final Grant gtr : grantsList)
            persistenceService.persist(gtr);
        return "result";
    }

    public List<Grant> getGrantsList() {
        return grantsList;
    }

    public void setGrantsList(final List<Grant> grantsList) {
        this.grantsList = grantsList;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(final List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public List<CFinancialYear> getFinYearList() {
        return finYearList;
    }

    public void setFinYearList(final List<CFinancialYear> finYearList) {
        this.finYearList = finYearList;
    }

    public List<String> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(final List<String> periodList) {
        this.periodList = periodList;
    }

    public String getGrantsType() {
        return grantsType;
    }

    public void setGrantsType(final String grantsType) {
        this.grantsType = grantsType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

}
