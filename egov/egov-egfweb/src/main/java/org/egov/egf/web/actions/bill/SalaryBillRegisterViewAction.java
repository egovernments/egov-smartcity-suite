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
package org.egov.egf.web.actions.bill;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.bills.EgBillregister;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalaryBillRegisterViewAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = 175183173349000135L;
    private Date fromDate;
    private Date toDate;
    private BigDecimal month;
    private Department department;
    private List<EgBillregister> billRegisterList = new ArrayList<EgBillregister>();
    @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    public SalaryBillRegisterViewAction() {
        addRelatedEntity("departmentList", Department.class);
    }

    @Override
    public void prepare() {
        super.prepare();
        addDropdownData("departmentList", masterDataCache.get("egi-department"));
    }

    @Override
    public String execute() throws Exception {
        return "search";
    }

    public String ajaxSearch() {
        if (department.getId() != -1 && !new BigDecimal("-1").equals(month))
            billRegisterList
            .addAll(persistenceService
                    .findAllBy(
                            "from EgBillregister where billdate<=? and billdate>=? and egBillregistermis.egDepartment.id=? and egBillregistermis.month=? order by billdate",
                            toDate, fromDate, department.getId(), month));
        else if (department.getId() == -1 && !new BigDecimal("-1").equals(month))
            billRegisterList.addAll(persistenceService.findAllBy(
                    "from EgBillregister where billdate<=? and billdate>=? and egBillregistermis.month=? order by billdate",
                    toDate, fromDate, month));
        else if (department.getId() != -1 && new BigDecimal("-1").equals(month))
            billRegisterList
            .addAll(persistenceService
                    .findAllBy(
                            "from EgBillregister where billdate<=? and billdate>=? and egBillregistermis.egDepartment.id=? order by billdate",
                            toDate, fromDate, department.getId()));
        else
            billRegisterList.addAll(persistenceService.findAllBy(
                    "from EgBillregister where billdate<=? and billdate>=? order by billdate", toDate, fromDate));
        return "result";
    }

    @Override
    public Object getModel() {
        return null;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setMonth(final BigDecimal month) {
        this.month = month;
    }

    public BigDecimal getMonth() {
        return month;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public void setBillRegisterList(final List<EgBillregister> billRegisterList) {
        this.billRegisterList = billRegisterList;
    }

    public List<EgBillregister> getBillRegisterList() {
        return billRegisterList;
    }

}
