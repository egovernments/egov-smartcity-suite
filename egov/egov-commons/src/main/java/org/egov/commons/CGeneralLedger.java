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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "GENERALLEDGER")
@SequenceGenerator(name = CGeneralLedger.SEQ_GENERALLEDGER, sequenceName = CGeneralLedger.SEQ_GENERALLEDGER, allocationSize = 1)
public class CGeneralLedger {
	
	public static final String SEQ_GENERALLEDGER = "SEQ_GENERALLEDGER";
	
	@Id
	@GeneratedValue(generator = SEQ_GENERALLEDGER, strategy = GenerationType.SEQUENCE)
	private Long id = null;
	private Integer voucherlineId;
	private Date effectiveDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="glcodeId")
	private CChartOfAccounts glcodeId;
	private String glcode;
	private Double debitAmount;
	private Double creditAmount;
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="voucherHeaderId")
	private CVoucherHeader voucherHeaderId;
	private Integer functionId;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy="generalLedgerId", targetEntity=CGeneralLedgerDetail.class)
	private Set<CGeneralLedgerDetail> generalLedgerDetails = new HashSet<CGeneralLedgerDetail>();

	/**
	 * @return Returns the glcode.
	 */
	public String getGlcode() {
		return glcode;
	}

	/**
	 * @param glcode The glcode to set.
	 */
	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
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
	 * @return Returns the voucherlineId.
	 */

	public Integer getVoucherlineId() {
		return voucherlineId;
	}

	/**
	 * @param voucherlineId The voucherlineId to set.
	 */
	public void setVoucherlineId(Integer voucherlineId) {
		this.voucherlineId = voucherlineId;
	}

	/**
	 * @return Returns the glcodeId.
	 */

	public CChartOfAccounts getGlcodeId() {
		return glcodeId;
	}

	/**
	 * @param glcodeId The glcodeId to set.
	 */
	public void setGlcodeId(CChartOfAccounts glcodeId) {
		this.glcodeId = glcodeId;
	}

	/**
	 * @return Returns the voucherHeaderId.
	 */

	public CVoucherHeader getVoucherHeaderId() {
		return voucherHeaderId;
	}

	/**
	 * @param voucherHeaderId The voucherHeaderId to set.
	 */
	public void setVoucherHeaderId(CVoucherHeader voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
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
	 * @return Returns the debitAmount.
	 */

	public Double getDebitAmount() {
		return debitAmount;
	}

	/**
	 * @param debitAmount The debitAmount to set.
	 */
	public void setDebitAmount(Double debitAmount) {
		this.debitAmount = debitAmount;
	}

	/**
	 * @return Returns the creditAmount.
	 */

	public Double getCreditAmount() {
		return creditAmount;
	}

	/**
	 * @param creditAmount The creditAmount to set.
	 */
	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Set<CGeneralLedgerDetail> getGeneralLedgerDetails() {
		return generalLedgerDetails;
	}

	public void setGeneralLedgerDetails(Set<CGeneralLedgerDetail> generalLedgerDetails) {
		this.generalLedgerDetails = generalLedgerDetails;
	}

}
