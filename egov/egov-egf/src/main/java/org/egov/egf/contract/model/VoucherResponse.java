/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.egf.contract.model;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.Fund;

public class VoucherResponse {

    private Long id;
    private String name;
    private String type;
    private String voucherNumber;
    private String description;
    private String voucherDate;
    private Fund fund;
    private FiscalPeriodContract fiscalPeriod;
    private String status;
    private Long originalVhId;
    private Long refVhId;
    private String cgvn;
    private Long moduleId;
    private String source;
    private SchemeContract scheme;
    private SchemeContract subScheme;
    private FunctionaryContract functionary;
    private FundsourceContract fundsource;
    private List<AccountDetailContract> ledgers = new ArrayList<>(0);

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(final String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public FiscalPeriodContract getFiscalPeriod() {
        return fiscalPeriod;
    }

    public void setFiscalPeriod(final FiscalPeriodContract fiscalPeriod) {
        this.fiscalPeriod = fiscalPeriod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Long getOriginalVhId() {
        return originalVhId;
    }

    public void setOriginalVhId(final Long originalVhId) {
        this.originalVhId = originalVhId;
    }

    public Long getRefVhId() {
        return refVhId;
    }

    public void setRefVhId(final Long refVhId) {
        this.refVhId = refVhId;
    }

    public String getCgvn() {
        return cgvn;
    }

    public void setCgvn(final String cgvn) {
        this.cgvn = cgvn;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(final Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<AccountDetailContract> getLedgers() {
        return ledgers;
    }

    public void setLedgers(final List<AccountDetailContract> ledgers) {
        this.ledgers = ledgers;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public SchemeContract getScheme() {
        return scheme;
    }

    public void setScheme(final SchemeContract scheme) {
        this.scheme = scheme;
    }

    public SchemeContract getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(final SchemeContract subScheme) {
        this.subScheme = subScheme;
    }

    public FunctionaryContract getFunctionary() {
        return functionary;
    }

    public void setFunctionary(final FunctionaryContract functionary) {
        this.functionary = functionary;
    }

    public FundsourceContract getFundsource() {
        return fundsource;
    }

    public void setFundsource(final FundsourceContract fundsource) {
        this.fundsource = fundsource;
    }

}
