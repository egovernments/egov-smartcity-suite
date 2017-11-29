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
package org.egov.infstr.models;

import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.entity.BankAccountServiceMap;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Unique(fields = { "code","name" }, id = "id", tableName = "EGCL_SERVICEDETAILS", columnName = { "CODE","NAME" }, message = "masters.serviceDetailsCodeOrName.isunique")
public class ServiceDetails extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String serviceUrl;
    private Boolean isEnabled;
    private String callBackurl;
    private String serviceType;
    private String code;
    private Boolean voucherCreation;
    private Boolean isVoucherApproved;
    private Date voucherCutOffDate;
    private Integer orderNumber;

    private Set<BankAccountServiceMap> bankAccountServiceMap = new HashSet<BankAccountServiceMap>(0);
    private Fund fund;
    private Fundsource fundSource;

    private Functionary functionary;

    private Scheme scheme;
    private SubScheme subscheme;
    private ServiceCategory serviceCategory;

    private Set<Department> serviceDept = new LinkedHashSet<Department>(0);

    private Set<ServiceAccountDetails> serviceAccountDtls = new LinkedHashSet<ServiceAccountDetails>(0);

    private CFunction function;

    public void addServiceAccountDtls(final ServiceAccountDetails account) {
        getServiceAccountDtls().add(account);
    }

    public void addServiceDept(final Department dept) {
        getServiceDept().add(dept);
    }

    /**
     * @return the serviceName
     */
    @Required(message = "service.name.null")
    public String getName() {
        return name;
    }

    /**
     * @param serviceName
     *            the serviceName to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the serviceUrl
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * @param serviceUrl
     *            the serviceUrl to set
     */
    public void setServiceUrl(final String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /**
     * @return the isEnabled
     */
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    /**
     * @param isEnabled
     *            the isEnabled to set
     */
    public void setIsEnabled(final Boolean isEnabled) {
        if (null == isEnabled)
            this.isEnabled = Boolean.FALSE;
        else
            this.isEnabled = isEnabled;
    }

    /**
     * @return the callBackurl
     */
    public String getCallBackurl() {
        return callBackurl;
    }

    /**
     * @param callBackurl
     *            the callBackurl to set
     */
    public void setCallBackurl(final String callBackurl) {
        this.callBackurl = callBackurl;
    }

    /**
     * @return the serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType
     *            the serviceType to set
     */
    public void setServiceType(final String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return the code
     */
    @Required(message = "service.code.null")
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * @return the fund
     */
    public Fund getFund() {
        return fund;
    }

    /**
     * @param fund
     *            the fund to set
     */
    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    /**
     * @return the fundSource
     */
    public Fundsource getFundSource() {
        return fundSource;
    }

    /**
     * @param fundSource
     *            the fundSource to set
     */
    public void setFundSource(final Fundsource fundSource) {
        this.fundSource = fundSource;
    }

    /**
     * /**
     * 
     * @return the functionary
     */
    public Functionary getFunctionary() {
        return functionary;
    }

    /**
     * @param functionary
     *            the functionary to set
     */
    public void setFunctionary(final Functionary functionary) {
        this.functionary = functionary;
    }

    public Set<BankAccountServiceMap> getBankAccountServiceMap() {
        return bankAccountServiceMap;
    }

    public void setBankAccountServiceMap(final Set<BankAccountServiceMap> bankAccountServiceMap) {
        this.bankAccountServiceMap = bankAccountServiceMap;
    }

    public void addBankAccountServiceMap(final BankAccountServiceMap bankAccountServiceMap) {
        getBankAccountServiceMap().add(bankAccountServiceMap);
    }

    public Boolean getVoucherCreation() {
        return voucherCreation;
    }

    public void setVoucherCreation(final Boolean voucherCreation) {
        if (null == voucherCreation)
            this.voucherCreation = Boolean.FALSE;
        else
            this.voucherCreation = voucherCreation;

    }

    /**
     * @return the scheme
     */
    public Scheme getScheme() {
        return scheme;
    }

    /**
     * @param scheme
     *            the scheme to set
     */
    public void setScheme(final Scheme scheme) {
        this.scheme = scheme;
    }

    /**
     * @return the subscheme
     */
    public SubScheme getSubscheme() {
        return subscheme;
    }

    /**
     * @param subscheme
     *            the subscheme to set
     */
    public void setSubscheme(final SubScheme subscheme) {
        this.subscheme = subscheme;
    }

    /**
     * @return the serviceCategory
     */
    public ServiceCategory getServiceCategory() {
        return serviceCategory;
    }

    /**
     * @param serviceCategory
     *            the serviceCategory to set
     */
    public void setServiceCategory(final ServiceCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    /**
     * @return the serviceDept
     */
    public Set<Department> getServiceDept() {
        return serviceDept;
    }

    /**
     * @param serviceDept
     *            the serviceDept to set
     */
    public void setServiceDept(final Set<Department> serviceDept) {
        this.serviceDept = serviceDept;
    }

    public Set<ServiceAccountDetails> getServiceAccountDtls() {
        return serviceAccountDtls;
    }

    public void setServiceAccountDtls(final Set<ServiceAccountDetails> serviceAccountDtls) {
        this.serviceAccountDtls = serviceAccountDtls;
    }

    /**
     * @return the isVoucherApproved
     */
    public Boolean getIsVoucherApproved() {
        return isVoucherApproved;
    }

    /**
     * @param isVoucherApproved
     *            the isVoucherApproved to set
     */
    public void setIsVoucherApproved(final Boolean isVoucherApproved) {
        this.isVoucherApproved = isVoucherApproved;
    }

    /**
     * @return the voucherCutOffDate
     */
    public Date getVoucherCutOffDate() {
        return voucherCutOffDate;
    }

    /**
     * @param voucherCutOffDate
     *            the voucherCutOffDate to set
     */
    public void setVoucherCutOffDate(final Date voucherCutOffDate) {
        this.voucherCutOffDate = voucherCutOffDate;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

}
