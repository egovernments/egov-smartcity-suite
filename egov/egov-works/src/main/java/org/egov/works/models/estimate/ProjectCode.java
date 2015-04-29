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
package org.egov.works.models.estimate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

public class ProjectCode extends BaseModel implements EntityType  {

	private String code;
	private Set<AbstractEstimate> estimates = new HashSet<AbstractEstimate>();
	private Boolean isActive;
	@Length(max=1024,message="projectCode.description.length")
	private String description;
	//@Required(message="projectCode.name.null")
	@Length(max=1024,message="projectCode.name.length")
	private String codeName;
	private EgwStatus egwStatus;
	private Double projectValue;
	private Date completionDate;
	private boolean isFinalBill;
	
	public ProjectCode(){
           }
	public ProjectCode(AbstractEstimate abstractEstimate, String code){
		this.estimates.add(abstractEstimate);
		this.code=code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public void addEstimate(AbstractEstimate estimate){
		this.estimates.add(estimate);
	}
	
	public Set<AbstractEstimate> getEstimates() {
		return estimates;
	}
	public void setEstimates(Set<AbstractEstimate> estimates) {
		this.estimates = estimates;
	}

	public String getBankaccount() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBankname() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getIfsccode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getModeofpay() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return codeName;
	}

	public String getPanno() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTinno() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getEntityId() {
		return Integer.valueOf(id.intValue());
	}
	
	@Override
	public String getEntityDescription() {
		return description;
	}
	/*public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}

	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}*/
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public EgwStatus getEgwStatus() {
		return egwStatus;
	}
	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}
	public Double getProjectValue() {
		return projectValue;
	}
	public void setProjectValue(Double projectValue) {
		this.projectValue = projectValue;
	}
	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	
	public boolean getIsFinalBill() {
		return isFinalBill;
	}
	public void setIsFinalBill(boolean isFinalBill) {
		this.isFinalBill = isFinalBill;
	}
	
}
