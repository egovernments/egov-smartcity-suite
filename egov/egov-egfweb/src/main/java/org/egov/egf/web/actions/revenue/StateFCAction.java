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

import net.sf.jasperreports.engine.JRException;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.egf.revenue.Grant;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Results(value = {
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=StateFC.pdf" }),
                @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                        "application/xls", "contentDisposition", "no-cache;filename=StateFC.xls" })
})
public class StateFCAction extends BaseRevenueAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;


    /**
     *
     */
    private static final long serialVersionUID = 8558457086956402655L;
    private final String jasperpath = "/reports/templates/RevenueReport.jasper";
    private ReportHelper reportHelper;
    private InputStream inputStream;

    @Override
    public void prepare() {
        super.prepare();
        periodList = new ArrayList<String>();
        periodList.add(Constants.PERIOD_MONTH1);
        periodList.add(Constants.PERIOD_MONTH2);
        periodList.add(Constants.PERIOD_MONTH3);
        periodList.add(Constants.PERIOD_MONTH4);
        periodList.add(Constants.PERIOD_MONTH5);
        periodList.add(Constants.PERIOD_MONTH6);
        periodList.add(Constants.PERIOD_MONTH7);
        periodList.add(Constants.PERIOD_MONTH8);
        periodList.add(Constants.PERIOD_MONTH9);
        periodList.add(Constants.PERIOD_MONTH10);
        periodList.add(Constants.PERIOD_MONTH11);
        periodList.add(Constants.PERIOD_MONTH12);
        setGrantsType(Constants.GRANT_TYPE_SFC);
    }

    public String getUlbName() {
        final Query query = persistenceService.getSession().createSQLQuery(
                "select name from companydetail");
        final List<String> result = query.list();
        if (result != null)
            return result.get(0);
        return "";
    }

    Map<String, Object> getParamMap() {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        final String header = "";
        paramMap.put("ulbName", getUlbName());
        paramMap.put("heading", header);
        return paramMap;
    }

    public String exportPdf() throws JRException, IOException {

        final List<Object> dataSource = new ArrayList<Object>();
        for (final Grant row : grantsList) {
            if (row.getDepartment().getId() != null)
                for (final Department dep : departmentList)
                    if (dep.getId().equals(row.getDepartment().getId()))
                        row.setDepartment(dep);
            for (final CFinancialYear fin : finYearList)
                if (fin.getId().equals(row.getFinancialYear().getId()))
                    row.setFinancialYear(fin);
            dataSource.add(row);
        }
        setInputStream(reportHelper.exportPdf(getInputStream(), jasperpath,
                getParamMap(), dataSource));
        return "PDF";
    }

    public String exportXls() throws JRException, IOException {
        final List<Object> dataSource = new ArrayList<Object>();
        for (final Grant row : grantsList) {
            if (row.getDepartment().getId() != null)
                for (final Department dep : departmentList)
                    if (dep.getId().equals(row.getDepartment().getId()))
                        row.setDepartment(dep);
            for (final CFinancialYear fin : finYearList)
                if (fin.getId().equals(row.getFinancialYear().getId()))
                    row.setFinancialYear(fin);
            dataSource.add(row);
        }
        setInputStream(reportHelper.exportXls(getInputStream(), jasperpath,
                getParamMap(), dataSource));
        return "XLS";
    }

    public ReportHelper getReportHelper() {
        return reportHelper;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

}