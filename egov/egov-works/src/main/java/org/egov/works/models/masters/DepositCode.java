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
package org.egov.works.models.masters;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.utils.EntityType;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

@Unique(fields = { "code" }, id = "id", tableName = "EGW_DEPOSITCODE", columnName = { "CODE" }, message = "depositCode.isUnique")
public class DepositCode extends BaseModel implements EntityType {

    /**
     *
     */
    private static final long serialVersionUID = -6649203487282172205L;
    private String code;
    @Length(max = 1024, message = "depositCode.description.length")
    private String description;
    private NatureOfWork natureOfWork;

    @Required(message = "depositCode.workName.null")
    @Length(max = 256, message = "depositCode.workName.length")
    private String codeName;
    private Fund fund;
    private Functionary functionary;
    private CFunction function;
    private Scheme scheme;
    private SubScheme subScheme;
    private Department department;
    private Boundary ward;
    private Boundary zone;

    @Required(message = "depositCode.finYear.null")
    private CFinancialYear financialYear;
    private Fundsource fundSource;
    private EgwTypeOfWork typeOfWork;
    private EgwTypeOfWork subTypeOfWork;
    private boolean isActive;

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public NatureOfWork getNatureOfWork() {
        return natureOfWork;
    }

    public void setNatureOfWork(final NatureOfWork natureOfWork) {
        this.natureOfWork = natureOfWork;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(final String codeName) {
        this.codeName = codeName;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public Functionary getFunctionary() {
        return functionary;
    }

    public void setFunctionary(final Functionary functionary) {
        this.functionary = functionary;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(final Scheme scheme) {
        this.scheme = scheme;
    }

    public SubScheme getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final SubScheme subScheme) {
        this.subScheme = subScheme;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Boundary getWard() {
        return ward;
    }

    public void setWard(final Boundary ward) {
        this.ward = ward;
    }

    public Boundary getZone() {
        return zone;
    }

    public void setZone(final Boundary zone) {
        this.zone = zone;
    }

    public CFinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(final CFinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public Fundsource getFundSource() {
        return fundSource;
    }

    public void setFundSource(final Fundsource fundSource) {
        this.fundSource = fundSource;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    public EgwTypeOfWork getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(final EgwTypeOfWork typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public EgwTypeOfWork getSubTypeOfWork() {
        return subTypeOfWork;
    }

    public void setSubTypeOfWork(final EgwTypeOfWork subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
    }

    @Override
    public String getName() {
        return codeName;
    }

    @Override
    public String getEntityDescription() {
        return description;
    }

    @Override
    public Integer getEntityId() {
        return Integer.valueOf(id.intValue());
    }

    @Override
    public String getIfsccode() {

        return null;
    }

    @Override
    public String getModeofpay() {

        return null;
    }

    @Override
    public String getBankaccount() {

        return null;
    }

    @Override
    public String getBankname() {

        return null;
    }

    @Override
    public String getPanno() {

        return null;
    }

    @Override
    public String getTinno() {

        return null;
    }

    @Override
    public EgwStatus getEgwStatus() {

        return null;
    }
}
