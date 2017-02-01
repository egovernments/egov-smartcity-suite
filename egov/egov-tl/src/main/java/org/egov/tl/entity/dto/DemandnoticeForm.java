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

package org.egov.tl.entity.dto;

import java.math.BigDecimal;
import java.util.Map;

import org.egov.tl.entity.TradeLicense;

public class DemandnoticeForm {

	private String applicationNumber;
	private Long licenseId;
	private String licenseNumber;
	private String oldLicenseNumber;
	private String categoryName;
	private String subCategoryName;
	private String tradeTitle;
	private String tradeOwnerName;
	private Long wardId;
	private String warName;
	private Long localityId;
	private String localityName;
	private String status;
	private String ownerName;
	private Long categoryId;
	private Long subCategoryId;
	private Long statusId;
	private BigDecimal tlArrearFee;
	private BigDecimal tlArrearPenalty;
	private BigDecimal tlCurrentFee;
	private BigDecimal tlCurentPenalty;

	public DemandnoticeForm() {

	}

	public Long getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getOldLicenseNumber() {
		return oldLicenseNumber;
	}

	public void setOldLicenseNumber(String oldLicenseNumber) {
		this.oldLicenseNumber = oldLicenseNumber;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public String getTradeTitle() {
		return tradeTitle;
	}

	public void setTradeTitle(String tradeTitle) {
		this.tradeTitle = tradeTitle;
	}

	public String getTradeOwnerName() {
		return tradeOwnerName;
	}

	public void setTradeOwnerName(String tradeOwnerName) {
		this.tradeOwnerName = tradeOwnerName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public BigDecimal getTlArrearFee() {
		return tlArrearFee;
	}

	public void setTlArrearFee(BigDecimal tlArrearFee) {
		this.tlArrearFee = tlArrearFee;
	}

	public BigDecimal getTlArrearPenalty() {
		return tlArrearPenalty;
	}

	public void setTlArrearPenalty(BigDecimal tlArrearPenalty) {
		this.tlArrearPenalty = tlArrearPenalty;
	}

	public BigDecimal getTlCurrentFee() {
		return tlCurrentFee;
	}

	public void setTlCurrentFee(BigDecimal tlCurrentFee) {
		this.tlCurrentFee = tlCurrentFee;
	}

	public BigDecimal getTlCurentPenalty() {
		return tlCurentPenalty;
	}

	public void setTlCurentPenalty(BigDecimal tlCurentPenalty) {
		this.tlCurentPenalty = tlCurentPenalty;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public String getWarName() {
		return warName;
	}

	public void setWarName(String warName) {
		this.warName = warName;
	}

	public String getLocalityName() {
		return localityName;
	}

	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}

	public Long getLocalityId() {
		return localityId;
	}

	public void setLocalityId(Long localityId) {
		this.localityId = localityId;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public DemandnoticeForm(final TradeLicense license, Map<String, BigDecimal> licenseFees, String ownerName) {
		BigDecimal balance = BigDecimal.ZERO;
		setLicenseId(license.getId());
		setLicenseNumber(license.getLicenseNumber());
		setOldLicenseNumber(license.getOldLicenseNumber());
		setCategoryName(license.getCategory().getName());
		setSubCategoryName(license.getTradeName().getName());
		setTradeTitle(license.getNameOfEstablishment());
		if (null != license.getLicensee()) {
			setTradeOwnerName(license.getLicensee().getApplicantName());
		}
		setStatus(license.getStatus().getName());
		setOwnerName(ownerName);
		if (null != license.getParentBoundary()) {
			setWarName(license.getParentBoundary().getName());
		}
		setApplicationNumber(license.getApplicationNumber());
		if (licenseFees != null) {
			setTlCurrentFee(licenseFees.get("current") == null ? balance
					: licenseFees.get("current").setScale(0, BigDecimal.ROUND_HALF_UP));
			setTlArrearFee(licenseFees.get("arrear") == null ? balance
					: licenseFees.get("arrear").setScale(0, BigDecimal.ROUND_HALF_UP));
			setTlArrearPenalty(licenseFees.get("penalty") == null ? balance
					: licenseFees.get("penalty").setScale(0, BigDecimal.ROUND_HALF_UP));

		} else {
			setTlCurrentFee(balance);
			setTlArrearFee(balance);
			setTlArrearPenalty(balance);
		}

	}

}
