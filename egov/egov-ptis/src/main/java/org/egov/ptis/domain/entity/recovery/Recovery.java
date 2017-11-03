/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.ptis.domain.entity.recovery;

import org.egov.commons.EgwStatus;
import org.egov.demand.model.EgBill;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.entity.property.BasicProperty;

import javax.validation.Valid;

public class Recovery extends StateAware<Position> {
    private static final long serialVersionUID = 1L;
    private Long id;

    private BasicProperty basicProperty;
    private EgwStatus status;
    private EgBill bill;
    @Valid
    private Warrant warrant;
    @Valid
    private IntimationNotice intimationNotice;
    @Valid
    private WarrantNotice warrantNotice;
    @Valid
    private CeaseNotice ceaseNotice;

    @Override
    public String getStateDetails() {

        return getBasicProperty().getUpicNo();
    }

    @Required(message = "recovery.basicProperty.null")
    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    public Warrant getWarrant() {
        return warrant;
    }

    public void setWarrant(Warrant warrant) {
        this.warrant = warrant;
    }

    public IntimationNotice getIntimationNotice() {
        return intimationNotice;
    }

    public void setIntimationNotice(IntimationNotice intimationNotice) {
        this.intimationNotice = intimationNotice;
    }

    public WarrantNotice getWarrantNotice() {
        return warrantNotice;
    }

    public void setWarrantNotice(WarrantNotice warrantNotice) {
        this.warrantNotice = warrantNotice;
    }

    public CeaseNotice getCeaseNotice() {
        return ceaseNotice;
    }

    public void setCeaseNotice(CeaseNotice ceaseNotice) {
        this.ceaseNotice = ceaseNotice;
    }

    @Required(message = "recovery.status.null")
    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    @Required(message = "recovery.bill.null")
    public EgBill getBill() {
        return bill;
    }

    public void setBill(EgBill bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("UcipNo :").append(
                null != basicProperty ? basicProperty.getUpicNo() : " ");
        sb.append("BillNo :").append(null != bill ? bill.getBillNo() : " ");
        sb.append("Status :").append(
                null != status ? status.getDescription() : " ");
        return sb.toString();
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
