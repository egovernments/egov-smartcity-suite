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
package org.egov.model.advance;

import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;

import java.util.Date;

public class EgAdvanceRequisitionMis implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 699827158343906305L;
    private Long id;
    private Boundary fieldId;
    private Boundary subFieldId;
    private Department egDepartment;
    private Date lastupdatedtime;
    private Scheme scheme;
    private SubScheme subScheme;
    private CVoucherHeader voucherheader;
    private EgAdvanceRequisition egAdvanceRequisition;
    private Fundsource fundsource;
    private Fund fund;
    private Functionary functionaryId;
    private String payto;
    private Date paybydate;
    private String referencenumber;
    private String sourcePath;
    private String partyBillNumber;
    private Date partyBillDate;
    private CFunction function;

    public EgAdvanceRequisitionMis() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Boundary getFieldId() {
        return fieldId;
    }

    public void setFieldId(final Boundary fieldId) {
        this.fieldId = fieldId;
    }

    public Boundary getSubFieldId() {
        return subFieldId;
    }

    public void setSubFieldId(final Boundary subFieldId) {
        this.subFieldId = subFieldId;
    }

    public Department getEgDepartment() {
        return egDepartment;
    }

    public void setEgDepartment(final Department egDepartment) {
        this.egDepartment = egDepartment;
    }

    public Date getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(final Date lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
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

    public CVoucherHeader getVoucherheader() {
        return voucherheader;
    }

    public void setVoucherheader(final CVoucherHeader voucherheader) {
        this.voucherheader = voucherheader;
    }

    public EgAdvanceRequisition getEgAdvanceRequisition() {
        return egAdvanceRequisition;
    }

    public void setEgAdvanceRequisition(final EgAdvanceRequisition egAdvanceRequisition) {
        this.egAdvanceRequisition = egAdvanceRequisition;
    }

    public Fundsource getFundsource() {
        return fundsource;
    }

    public void setFundsource(final Fundsource fundsource) {
        this.fundsource = fundsource;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(final Fund fund) {
        this.fund = fund;
    }

    public Functionary getFunctionaryId() {
        return functionaryId;
    }

    public void setFunctionaryId(final Functionary functionaryId) {
        this.functionaryId = functionaryId;
    }

    public String getPayto() {
        return payto;
    }

    public void setPayto(final String payto) {
        this.payto = payto;
    }

    public Date getPaybydate() {
        return paybydate;
    }

    public void setPaybydate(final Date paybydate) {
        this.paybydate = paybydate;
    }

    public String getReferencenumber() {
        return referencenumber;
    }

    public void setReferencenumber(final String referencenumber) {
        this.referencenumber = referencenumber;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(final String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getPartyBillNumber() {
        return partyBillNumber;
    }

    public void setPartyBillNumber(final String partyBillNumber) {
        this.partyBillNumber = partyBillNumber;
    }

    public Date getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(final Date partyBillDate) {
        this.partyBillDate = partyBillDate;
    }

    public EgAdvanceRequisitionMis(final Long id, final Boundary fieldId,
            final Boundary subFieldId, final Department egDepartment,
            final Date lastupdatedtime, final Scheme scheme, final SubScheme subScheme,
            final CVoucherHeader voucherheader,
            final EgAdvanceRequisition egAdvanceRequisition, final Fundsource fundsource,
            final Fund fund, final Functionary functionaryId, final String payto, final Date paybydate,
            final String referencenumber, final String sourcePath, final String partyBillNumber,
            final Date partyBillDate) {
        super();
        this.id = id;
        this.fieldId = fieldId;
        this.subFieldId = subFieldId;
        this.egDepartment = egDepartment;
        this.lastupdatedtime = lastupdatedtime;
        this.scheme = scheme;
        this.subScheme = subScheme;
        this.voucherheader = voucherheader;
        this.egAdvanceRequisition = egAdvanceRequisition;
        this.fundsource = fundsource;
        this.fund = fund;
        this.functionaryId = functionaryId;
        this.payto = payto;
        this.paybydate = paybydate;
        this.referencenumber = referencenumber;
        this.sourcePath = sourcePath;
        this.partyBillNumber = partyBillNumber;
        this.partyBillDate = partyBillDate;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

}
