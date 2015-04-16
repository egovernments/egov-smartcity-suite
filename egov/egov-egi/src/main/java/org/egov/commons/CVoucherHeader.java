/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.annotation.Search;
@Entity
@Table(name="VOUCHERHEADER")
public class CVoucherHeader extends StateAware {
	private static final long serialVersionUID = 1L;

	private String cgn;
	private Date cgDate;
	private String name;
	private String type;
	private String description;
	@NotNull
	private Date effectiveDate;
	private String voucherNumber;
	private Date voucherDate;
	private Integer departmentId;
	@ManyToOne
	private Fund fundId;
	private Integer fiscalPeriodId;
	private Integer status;
	private Long originalvcId;
	@ManyToOne
	private Fundsource fundsourceId;
	private Integer isConfirmed;
	private Integer functionId;
	private String refcgNo;
	private String cgvn;
	private Integer moduleId;
	@OneToMany(cascade=CascadeType.ALL,orphanRemoval = true)
	private Set<VoucherDetail> voucherDetail = new HashSet<VoucherDetail>(0);
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(referencedColumnName="voucherheaderid")
	private Vouchermis vouchermis;

	/**
	 * @return Returns the cgn.
	 */
	public String getCgn() {
		return cgn;
	}

	/**
	 * @param cgn The cgn to set.
	 */
	public void setCgn(String cgn) {
		this.cgn = cgn;
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
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the cgDate.
	 */
	public Date getCgDate() {
		return cgDate;
	}

	/**
	 * @param cgDate The cgDate to set.
	 */
	public void setCgDate(Date cgDate) {
		this.cgDate = cgDate;
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
	public void setType(String type) {
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
	public void setDescription(String description) {
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
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return Returns the voucherDate.
	 */
	@Search(searchOp = Search.Operator.between)
	public Date getVoucherDate() {
		return voucherDate;
	}

	/**
	 * @param voucherDate The voucherDate to set.
	 */
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}

	/**
	 * @return Returns the voucherNumber.
	 */
	@Search(searchOp = Search.Operator.contains)
	public String getVoucherNumber() {
		return voucherNumber;
	}

	/**
	 * @param voucherNumber The voucherNumber to set.
	 */
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	/**
	 * @return Returns the departmentId.
	 */

	public Integer getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId The departmentId to set.
	 */
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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
	public void setFundId(Fund fundId) {
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
	public void setFiscalPeriodId(Integer fiscalPeriodId) {
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
	public void setStatus(Integer status) {
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
	public void setOriginalvcId(Long originalvcId) {
		this.originalvcId = originalvcId;
	}

	/**
	 * @return Returns the fundsourceId.
	 */

	public Fundsource getFundsourceId() {
		return fundsourceId;
	}

	/**
	 * @param fundsourceId The fundsourceId to set.
	 */
	public void setFundsourceId(Fundsource fundsourceId) {
		this.fundsourceId = fundsourceId;
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
	public void setIsConfirmed(Integer isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	/**
	 * @return Returns the functionId.
	 */

	public Integer getFunctionId() {
		return functionId;
	}

	/**
	 * @param functionId The functionId to set.
	 */
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}

	/**
	 * @return Returns the refcgNo.
	 */

	public String getRefcgNo() {
		return refcgNo;
	}

	/**
	 * @param refcgNo The refcgNo to set.
	 */
	public void setRefcgNo(String refcgNo) {
		this.refcgNo = refcgNo;
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
	public void setCgvn(String cgvn) {
		this.cgvn = cgvn;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	@Override
	public String getStateDetails() {
		return voucherNumber;
	}

	public Set<VoucherDetail> getVoucherDetail() {
		return voucherDetail;
	}

	public void setVoucherDetail(Set<VoucherDetail> voucherDetail) {
		this.voucherDetail = voucherDetail;
	}

	public void addVoucherDetail(VoucherDetail voucherdetail) {
		getVoucherDetail().add(voucherdetail);
	}

	public Vouchermis getVouchermis() {
		return vouchermis;
	}

	public void setVouchermis(Vouchermis vouchermis) {
		this.vouchermis = vouchermis;
	}

	public void reset() {

		this.cgn = null;
		this.cgDate = null;
		this.name = null;
		this.type = null;
		this.description = null;
		this.effectiveDate = null;
		this.voucherNumber = null;
		this.voucherDate = null;
		this.departmentId = null;
		this.fundId = null;
		this.fiscalPeriodId = null;
		this.status = null;
		this.originalvcId = null;
		this.fundsourceId = null;
		this.isConfirmed = null;
		this.functionId = null;
		this.refcgNo = null;
		this.cgvn = null;
		this.moduleId = null;
		this.vouchermis = null;

	}

	public BigDecimal getTotalAmount() {
		BigDecimal amount = BigDecimal.ZERO;
		for (VoucherDetail detail : voucherDetail) {
			amount = amount.add(detail.getDebitAmount());
		}
		return amount;
	}

}
