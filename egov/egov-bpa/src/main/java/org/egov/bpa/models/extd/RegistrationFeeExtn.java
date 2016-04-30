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

import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RegistrationFeeExtn extends StateAware {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private EgwStatus egwStatus;
	private Boolean isRevised;
	private Date feeDate;
	private RegistrationExtn registration;
	private String challanNumber;
	private String feeRemarks;
	private String legacyFee;
	private Long approverPositionId;
	private String previousObjectState;
	private String previousObjectAction;
	private Integer previousStateOwnerId;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private Set<RegistrationFeeDetailExtn> registrationFeeDetailsSet = new HashSet<RegistrationFeeDetailExtn>(0);

	public String getFormattedFeeDate() {
		return sdf.format(getFeeDate());
	}

	public String getPreviousObjectState() {
		return previousObjectState;
	}

	public void setPreviousObjectState(String previousObjectState) {
		this.previousObjectState = previousObjectState;
	}

	public String getPreviousObjectAction() {
		return previousObjectAction;
	}

	public void setPreviousObjectAction(String previousObjectAction) {
		this.previousObjectAction = previousObjectAction;
	}

	public Integer getPreviousStateOwnerId() {
		return previousStateOwnerId;
	}

	public void setPreviousStateOwnerId(Integer previousStateOwnerId) {
		this.previousStateOwnerId = previousStateOwnerId;
	}

	public String getLegacyFee() {
		return legacyFee;
	}

	public void setLegacyFee(String legacyFee) {
		this.legacyFee = legacyFee;
	}

	public String getFeeRemarks() {
		return feeRemarks;
	}

	public void setFeeRemarks(String feeRemarks) {
		this.feeRemarks = feeRemarks;
	}

	public String getChallanNumber() {
		return challanNumber;
	}

	public void setChallanNumber(String challanNumber) {
		this.challanNumber = challanNumber;
	}

	public Set<RegistrationFeeDetailExtn> getRegistrationFeeDetailsSet() {
		return registrationFeeDetailsSet;
	}

	public void setRegistrationFeeDetailsSet(Set<RegistrationFeeDetailExtn> registrationFeeDetailsSet) {
		this.registrationFeeDetailsSet = registrationFeeDetailsSet;
	}

	public EgwStatus getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}

	public Boolean getIsRevised() {
		return isRevised;
	}

	public void setIsRevised(Boolean isRevised) {
		this.isRevised = isRevised;
	}

	public Date getFeeDate() {
		return feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}

	public RegistrationExtn getRegistration() {
		return registration;
	}

	public void setRegistration(RegistrationExtn registration) {
		this.registration = registration;
	}

	public Long getApproverPositionId() {
		return approverPositionId;
	}

	public void setApproverPositionId(Long approverPositionId) {
		this.approverPositionId = approverPositionId;
	}

	@Override
	public String getStateDetails() {

		return this.registration.getStateDetails();
	}

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
