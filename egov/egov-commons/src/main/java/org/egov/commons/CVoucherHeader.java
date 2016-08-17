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
package org.egov.commons;

import org.egov.infra.workflow.entity.StateAware;
import org.hibernate.search.annotations.DocumentId;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "VOUCHERHEADER")
@SequenceGenerator(name = CVoucherHeader.SEQ_VOUCHERHEADER, sequenceName = CVoucherHeader.SEQ_VOUCHERHEADER, allocationSize = 1)
public class CVoucherHeader extends StateAware {

    private static final long serialVersionUID = -1950866465902911747L;
    public static final String SEQ_VOUCHERHEADER = "SEQ_VOUCHERHEADER";

    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_VOUCHERHEADER, strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String type;
    private String description;
    private Date effectiveDate;
    private String voucherNumber;
    private Date voucherDate;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "fundId")
    private Fund fundId;
    private Integer fiscalPeriodId;
    private Integer status;
    private Long originalvcId;
    private Integer isConfirmed;
    private Long refvhId;
    private String cgvn;
    private Integer moduleId;
    @Transient
    private String voucherSubType;
    @Transient
    private Boolean isRestrictedtoOneFunctionCenter;
    @Transient
    private String voucherNumberPrefix;
    //field is the confirmation after the budget check fails and user says to continue
    @Transient
    private Boolean allowNegetive;
    public Boolean getAllowNegetive() {
		return allowNegetive;
	}

	public void setAllowNegetive(Boolean allowNegetive) {
		this.allowNegetive = allowNegetive;
	}

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL , fetch=FetchType.LAZY,mappedBy="voucherHeaderId",targetEntity=CGeneralLedger.class )
    private Set<CGeneralLedger> generalLedger;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "voucherheaderid" , targetEntity=Vouchermis.class)
    private Vouchermis vouchermis;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }


    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the Type.
     */

    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return Returns the Description.
     */

    public String getDescription() {
        return description;
    }

    /**
     * @param Description The Description to set.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return Returns the effectiveDate.
     */

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * @param effectiveDate The effectiveDate to set.
     */
    public void setEffectiveDate(final Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * @return Returns the voucherDate.
     */
    public Date getVoucherDate() {
        return voucherDate;
    }

    /**
     * @param voucherDate The voucherDate to set.
     */
    public void setVoucherDate(final Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    /**
     * @return Returns the voucherNumber.
     */
    public String getVoucherNumber() {
        return voucherNumber;
    }

    /**
     * @param voucherNumber The voucherNumber to set.
     */
    public void setVoucherNumber(final String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    /**
     * @return Returns the fundId.
     */

    public Fund getFundId() {
        return fundId;
    }

    /**
     * @param fundId The fundId to set.
     */
    public void setFundId(final Fund fundId) {
        this.fundId = fundId;
    }

    /**
     * @return Returns the fiscalPeriodId.
     */

    public Integer getFiscalPeriodId() {
        return fiscalPeriodId;
    }

    /**
     * @param fiscalPeriodId The fiscalPeriodId to set.
     */
    public void setFiscalPeriodId(final Integer fiscalPeriodId) {
        this.fiscalPeriodId = fiscalPeriodId;
    }

    /**
     * @return Returns the status.
     */

    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(final Integer status) {
        this.status = status;
    }

    /**
     * @return Returns the originalvcId.
     */
    public Long getOriginalvcId() {
        return originalvcId;
    }

    /**
     * @param originalvcId The originalvcId to set.
     */
    public void setOriginalvcId(final Long originalvcId) {
        this.originalvcId = originalvcId;
    }

    /**
     * @return Returns the isConfirmed.
     */

    public Integer getIsConfirmed() {
        return isConfirmed;
    }

    /**
     * @param isConfirmed The isConfirmed to set.
     */
    public void setIsConfirmed(final Integer isConfirmed) {
        this.isConfirmed = isConfirmed;
    }


    public Long getRefvhId() {
        return refvhId;
    }

    public void setRefvhId(Long refvhId) {
        this.refvhId = refvhId;
    }

    /**
     * @return Returns the cgvn.
     */

    public String getCgvn() {
        return cgvn;
    }

    /**
     * @param cgvn The cgvn to set.
     */
    public void setCgvn(final String cgvn) {
        this.cgvn = cgvn;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(final Integer moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String getStateDetails() {
        return voucherNumber;
    }

    public Set<CGeneralLedger> getGeneralledger() {
        return generalLedger;
    }

    public Vouchermis getVouchermis() {
        return vouchermis;
    }

    public void setVouchermis(final Vouchermis vouchermis) {
        this.vouchermis = vouchermis;
    }

    public void reset() {

        name = null;
        type = null;
        description = null;
        effectiveDate = null;
        voucherNumber = null;
        voucherDate = null;
        fundId = null;
        fiscalPeriodId = null;
        status = null;
        originalvcId = null;
        isConfirmed = null;
        refvhId = null;
        cgvn = null;
        moduleId = null;
        vouchermis = null;

    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (final CGeneralLedger detail : generalLedger)
            amount = amount.add(new BigDecimal(detail.getDebitAmount()));
        return amount;
    }

    public Boolean getIsRestrictedtoOneFunctionCenter() {
        return isRestrictedtoOneFunctionCenter;
    }

    public void setIsRestrictedtoOneFunctionCenter(final Boolean isRestrictedtoOneFunctionCenter) {
        this.isRestrictedtoOneFunctionCenter = isRestrictedtoOneFunctionCenter;
    }

    public String getVoucherSubType() {
        return voucherSubType;
    }

    public void setVoucherSubType(final String voucherSubType) {
        this.voucherSubType = voucherSubType;
    }

    public Set<CGeneralLedger> getGeneralLedger() {
        return generalLedger;
    }

    public void setGeneralLedger(Set<CGeneralLedger> generalLedger) {
        this.generalLedger = generalLedger;
    }

	public String getVoucherNumberPrefix() {
		return voucherNumberPrefix;
	}

	public void setVoucherNumberPrefix(String voucherNumberPrefix) {
		this.voucherNumberPrefix = voucherNumberPrefix;
	}

	

}
