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
package org.egov.model.instrument;

import org.egov.infstr.models.BaseModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "EGF_DISHONORCHEQUE_SL_DETAIL")
@SequenceGenerator(name = DishonorChequeSubLedgerDetails.SEQ_EGF_DISHONORCHEQUE_SL_DETAIL, sequenceName = DishonorChequeSubLedgerDetails.SEQ_EGF_DISHONORCHEQUE_SL_DETAIL, allocationSize = 1)
public class DishonorChequeSubLedgerDetails extends BaseModel {

    private static final long serialVersionUID = 7262466422266226010L;

    public static final String SEQ_EGF_DISHONORCHEQUE_SL_DETAIL = "SEQ_EGF_DISHONORCHEQUE_SL_DETAIL";
    @Id
    @GeneratedValue(generator = SEQ_EGF_DISHONORCHEQUE_SL_DETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "detailid")
    private DishonorChequeDetails details;

    private BigDecimal amount;

    private Integer detailTypeId;

    private Integer detailKeyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DishonorChequeDetails getDetails() {
        return details;
    }

    public void setDetails(DishonorChequeDetails details) {
        this.details = details;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getDetailTypeId() {
        return detailTypeId;
    }

    public void setDetailTypeId(Integer detailTypeId) {
        this.detailTypeId = detailTypeId;
    }

    public Integer getDetailKeyId() {
        return detailKeyId;
    }

    public void setDetailKeyId(Integer detailKeyId) {
        this.detailKeyId = detailKeyId;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DishonorChequeSubLedgerDetails other = (DishonorChequeSubLedgerDetails) obj;
        if (detailKeyId == null && detailTypeId == null) {
            if (detailKeyId != null || detailTypeId != null)
                return false;
        } else if (!(detailKeyId == other.detailKeyId && detailTypeId == other.detailTypeId))
            return false;
        return true;
    }
}
