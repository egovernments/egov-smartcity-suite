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
package org.egov.bpa.models.extd;

import org.egov.bpa.constants.BpaConstants;
import org.egov.infra.admin.master.entity.Boundary;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FeesReportResultExtn {

	private Long count;
	private Boundary zone;
	private Boundary ward;
	private Integer zoneId;
	private Integer statusId;
	private String psnNum;
	private String zoneName;
	private BigDecimal amount = BigDecimal.ZERO;
	private String statusName;
	private String bpafeecode;
	private RegistrationExtn registration;
	private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
	private RegistrationFeeExtn feeObj;
	private RegistrationFeeDetailExtn feeDetailObj;
	private String wardName;

	


	public String getPsnNum() {
		return psnNum;
	}


	public void setPsnNum(String psnNum) {
		this.psnNum = psnNum;
	}


	public RegistrationFeeDetailExtn getFeeDetailObj() {
		return feeDetailObj;
	}

	public void setFeeDetailObj(RegistrationFeeDetailExtn feeDetailObj) {
		this.feeDetailObj = feeDetailObj;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBpafeecode() {
		return bpafeecode;
	}

	public void setBpafeecode(String bpafeecode) {
		this.bpafeecode = bpafeecode;
	}

	
	
	
	public RegistrationExtn getRegistration() {
		return registration;
	}

	public void setRegistration(RegistrationExtn registration) {
		this.registration = registration;
	}

	


	public RegistrationFeeExtn getFeeObj() {
		return feeObj;
	}

	public void setFeeObj(RegistrationFeeExtn feeObj) {
		this.feeObj = feeObj;
	}

	
	public Boundary getZone() {
		return zone;
	}


	public void setZone(Boundary zone) {
		this.zone = zone;
	}


	public Boundary getWard() {
		return ward;
	}


	public void setWard(Boundary ward) {
		this.ward = ward;
	}


	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	
	/*
	 * To get Order issued Date for records in HSUI Reports...
	 */
	public String getIssuedDate() {
		String bpadate = "N/A";
		if (null != this.registration && this.registration.getRegnStatusDetailsSet() != null) {
			for (RegnStatusDetailsExtn regnStatDet : this.registration.getRegnStatusDetailsSet()) {
				if (null != regnStatDet.getStatus() && null != regnStatDet.getStatus().getCode() && regnStatDet.getStatus().getCode().equals(BpaConstants.ORDERISSUEDTOAPPLICANT)) {
					bpadate = sdf1.format(regnStatDet.getStatusdate());
				}
			}
		}

		return bpadate.toString();

	}

	/*
	 * To get Show Whether basement is part of housing unit, if TRUE Show 1 else
	 * 2 in jsp
	 */
	

}
