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
package com.exilant.eGov.src.reports;

import org.apache.log4j.Logger;
import org.egov.infra.exception.ApplicationRuntimeException;

import java.util.List;

public class ReportEngineBean {
    private static final Logger LOGGER = Logger.getLogger(ReportEngineBean.class);
    private String fundId;
    private String fundsourceId;
    private String departmentId;
    private String functionaryId;
    private String fromVoucherNumber;
    private String toVoucherNumber;
    private String finacialYearId;
    private String fiscalPeriodId;
    private String fromDate;
    private String toDate;
    private String divisionId;
    private String schemeId;
    private String subSchemeId;
    private String functionId;
    private List<String> excludeStatuses;
    private List<String> includeStatuses;
    private int filtersCount = 0;

    public String getFundId() {
        return fundId;

    }

    public void setFundId(final String fundId) {
        this.fundId = fundId;
        filtersCount += 1;
    }

    public String getFundsourceId() {
        return fundsourceId;

    }

    public void setFundsourceId(final String fundsourceId) {
        this.fundsourceId = fundsourceId;
        filtersCount += 1;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final String departmentId) {
        this.departmentId = departmentId;
        filtersCount += 1;
    }

    public String getFunctionaryId() {
        return functionaryId;
    }

    public void setFunctionaryId(final String functionaryId) {
        this.functionaryId = functionaryId;
        filtersCount += 1;
    }

    public String getFromVoucherNumber() {
        return fromVoucherNumber;
    }

    public void setFromVoucherNumber(final String fromVoucherNumber) {
        this.fromVoucherNumber = fromVoucherNumber;
        filtersCount += 1;
    }

    public String getToVoucherNumber() {
        return toVoucherNumber;

    }

    public void setToVoucherNumber(final String toVoucherNumber) {
        this.toVoucherNumber = toVoucherNumber;
        filtersCount += 1;
    }

    public String getFinacialYearId() {
        return finacialYearId;
    }

    public void setFinacialYearId(final String finacialYearId) {
        this.finacialYearId = finacialYearId;
        filtersCount += 1;
    }

    public String getFiscalPeriodId() {
        return fiscalPeriodId;
    }

    public void setFiscalPeriodId(final String fiscalPeriodId) {
        this.fiscalPeriodId = fiscalPeriodId;
        filtersCount += 1;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(final String toDate) {
        this.toDate = toDate;
        filtersCount += 1;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(final String divisionId) {
        this.divisionId = divisionId;
        filtersCount += 1;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(final String schemeId) {
        this.schemeId = schemeId;
        filtersCount += 1;
    }

    public String getSubSchemeId() {
        return subSchemeId;
    }

    public void setSubSchemeId(final String subSchemeId) {
        this.subSchemeId = subSchemeId;
        filtersCount += 1;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final String functionId) {
        this.functionId = functionId;
        filtersCount += 1;
    }

    public List<String> getExcludeStatuses() {
        return excludeStatuses;
    }

    public void setExcludeStatuses(final List<String> excludeStatuses) {
        this.excludeStatuses = excludeStatuses;

    }

    public List<String> getIncludeStatuses() {
        return includeStatuses;
    }

    public void setIncludeStatuses(final List<String> includeStatuses) {
        this.includeStatuses = includeStatuses;
    }

    public int getFiltersCount() {
        return filtersCount;
    }

    public String getCommaSeperatedValues(final List<String> list) throws ApplicationRuntimeException
    {
        final StringBuffer commaSeperatedValues = new StringBuffer("");
        if (!list.isEmpty())
        {
            if (list.size() == 1)
                commaSeperatedValues.append(list.get(0).toString());
            else
            {

                String comma = "";
                for (int i = 0; i < list.size(); i++)
                {
                    commaSeperatedValues.append(comma + list.get(i).toString());
                    comma = ",";
                }
            }
        } else
            throw new ApplicationRuntimeException("List contains 0 items cannot create comma seperate values");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("*************Comma seprated values");
        if (LOGGER.isInfoEnabled())
            LOGGER.info(commaSeperatedValues);
        return commaSeperatedValues.toString();
    }

}
