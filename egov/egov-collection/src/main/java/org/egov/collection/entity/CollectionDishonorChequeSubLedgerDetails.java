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
package org.egov.collection.entity;

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
@Table(name = "EGF_DISHONORCHEQUESUBDETAIL")
@SequenceGenerator(name = CollectionDishonorChequeSubLedgerDetails.SEQ_EGF_DISHONORCHEQUESUBDETAIL, sequenceName = CollectionDishonorChequeSubLedgerDetails.SEQ_EGF_DISHONORCHEQUESUBDETAIL, allocationSize = 1)
public class CollectionDishonorChequeSubLedgerDetails extends BaseModel {

    private static final long serialVersionUID = 7262466422266226010L;

    public static final String SEQ_EGF_DISHONORCHEQUESUBDETAIL = "SEQ_EGF_DISHONORCHEQUESUBDETAIL";
    @Id
    @GeneratedValue(generator = SEQ_EGF_DISHONORCHEQUESUBDETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dishonorchequedetail")
    private CollectionDishonorChequeDetails dishonorchequedetail;

    private BigDecimal amount;

    private Integer detailType;

    private Integer detailKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CollectionDishonorChequeDetails getDishonorchequedetail() {
        return dishonorchequedetail;
    }

    public void setDishonorchequedetail(CollectionDishonorChequeDetails dishonorchequedetail) {
        this.dishonorchequedetail = dishonorchequedetail;
    }

    public Integer getDetailType() {
        return detailType;
    }

    public void setDetailType(Integer detailType) {
        this.detailType = detailType;
    }

    public Integer getDetailKey() {
        return detailKey;
    }

    public void setDetailKey(Integer detailKey) {
        this.detailKey = detailKey;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CollectionDishonorChequeSubLedgerDetails other = (CollectionDishonorChequeSubLedgerDetails) obj;
        if (detailKey == null && detailType == null) {
            if (detailKey != null || detailType != null)
                return false;
        } else if (!(detailKey == other.detailKey && detailType == other.detailType))
            return false;
        return true;
    }
}
