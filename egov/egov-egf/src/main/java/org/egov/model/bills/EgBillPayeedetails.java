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

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.model.recoveries.Recovery;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "EG_BILLPAYEEDETAILS")
@SequenceGenerator(name = EgBillPayeedetails.SEQ_EG_BILLPAYEEDETAILS, sequenceName = EgBillPayeedetails.SEQ_EG_BILLPAYEEDETAILS, allocationSize = 1)
public class EgBillPayeedetails extends AbstractPersistable<Integer> implements java.io.Serializable {

    private static final long serialVersionUID = -6620941691239597456L;

    public static final String SEQ_EG_BILLPAYEEDETAILS = "SEQ_EG_BILLPAYEEDETAILS";

    @Id
    @GeneratedValue(generator = SEQ_EG_BILLPAYEEDETAILS, strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "billdetailid")
    private EgBilldetails egBilldetailsId;

    private Integer accountDetailTypeId;

    private Integer accountDetailKeyId;

    private BigDecimal debitAmount;

    private BigDecimal creditAmount;

    private Date lastUpdatedTime;

    @Transient
    private String detailTypeName;

    @Transient
    private String detailKeyName;

    @Transient
    private Boolean isDebit;

    @ManyToOne
    @JoinColumn(name = "tdsid")
    private Recovery recovery;

    @Length(max = 250)
    private String narration;

    public Integer getAccountDetailKeyId() {
        return accountDetailKeyId;
    }

    public void setAccountDetailKeyId(final Integer accountDetailKeyId) {
        this.accountDetailKeyId = accountDetailKeyId;
    }

    public EgBilldetails getEgBilldetailsId() {
        return egBilldetailsId;
    }

    public void setEgBilldetailsId(final EgBilldetails egBilldetailsId) {
        this.egBilldetailsId = egBilldetailsId;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(final BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(final BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(final Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Integer getAccountDetailTypeId() {
        return accountDetailTypeId;
    }

    public void setAccountDetailTypeId(final Integer accountDetailTypeId) {
        this.accountDetailTypeId = accountDetailTypeId;
    }

    public Recovery getRecovery() {
        return recovery;
    }

    public void setRecovery(final Recovery recovery) {
        this.recovery = recovery;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public String getDetailTypeName() {
        return detailTypeName;
    }

    public void setDetailTypeName(final String detailTypeName) {
        this.detailTypeName = detailTypeName;
    }

    public String getDetailKeyName() {
        return detailKeyName;
    }

    public void setDetailKeyName(final String detailKeyName) {
        this.detailKeyName = detailKeyName;
    }

    public Boolean getIsDebit() {
        return isDebit;
    }

    public void setIsDebit(final Boolean isDebit) {
        this.isDebit = isDebit;
    }

}
