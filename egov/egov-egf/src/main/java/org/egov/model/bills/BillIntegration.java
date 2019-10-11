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

package org.egov.model.bills;

import static org.egov.model.bills.BillIntegration.SEQ_BILLINTEGRATION;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "egf_bill_integration")
@SequenceGenerator(name = SEQ_BILLINTEGRATION, sequenceName = SEQ_BILLINTEGRATION, allocationSize = 1)
public class BillIntegration extends AbstractAuditable {

    public static final String SEQ_BILLINTEGRATION = "SEQ_EGF_BILLINTEGRATION";
    private static final long serialVersionUID = 7630238192598939863L;
    @Expose
    @Id
    @GeneratedValue(generator = SEQ_BILLINTEGRATION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "tp_bill_no")
    @Length(min = 1, max = 128)
    @SafeHtml
    private String tpbillno;

    @NotBlank
    @Length(min = 1, max = 128)
    @SafeHtml
    private String source;

    @ManyToOne
    @JoinColumn(name = "bill_register_id")
    private EgBillregister egBill;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getTpbillno() {
        return tpbillno;
    }

    public void setTpbillno(String tpbillno) {
        this.tpbillno = tpbillno;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public EgBillregister getEgBill() {
        return egBill;
    }

    public void setEgBill(EgBillregister egBill) {
        this.egBill = egBill;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof BillIntegration))
            return false;
        BillIntegration that = (BillIntegration) other;
        return Objects.equals((getSource() + getTpbillno() + egBill.getId()),
                (that.getSource() + that.getTpbillno() + that.egBill.getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getTpbillno(), egBill.getId());
    }
}
