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

import org.egov.infstr.models.BaseModel;

import java.util.HashSet;
import java.util.Set;

public class AutoDcrExtn extends BaseModel {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private String autoDcrNum;
	private String applicant_name;
	private String email;
	private Long mobileno;
	private String zone;
	private String ward;
	private String doorno;
	private String plotno;
	private String surveyno;
	private String village;
	private String blockno;
	private Long plotarea;
	private String address;
	private Integer floorCount;
	private Set<AutoDcrFloorDtlsExtn> autoDcrFlrDtlsSet = new HashSet<AutoDcrFloorDtlsExtn>();

	public Integer getFloorCount() {
		return floorCount;
	}

	public void setFloorCount(Integer floorCount) {
		this.floorCount = floorCount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getMobileno() {
		return mobileno;
	}

	public void setMobileno(Long mobileno) {
		this.mobileno = mobileno;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getDoorno() {
		return doorno;
	}

	public void setDoorno(String doorno) {
		this.doorno = doorno;
	}

	public String getPlotno() {
		return plotno;
	}

	public void setPlotno(String plotno) {
		this.plotno = plotno;
	}

	public String getSurveyno() {
		return surveyno;
	}

	public void setSurveyno(String surveyno) {
		this.surveyno = surveyno;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getBlockno() {
		return blockno;
	}

	public void setBlockno(String blockno) {
		this.blockno = blockno;
	}

	public Long getPlotarea() {
		return plotarea;
	}

	public void setPlotarea(Long plotarea) {
		this.plotarea = plotarea;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getApplicant_name() {
		return applicant_name;
	}

	public void setApplicant_name(String applicant_name) {
		this.applicant_name = applicant_name;
	}

	public String getAutoDcrNum() {
		return autoDcrNum;
	}

	public void setAutoDcrNum(String autoDcrNum) {
		this.autoDcrNum = autoDcrNum;
	}

	public Set<AutoDcrFloorDtlsExtn> getAutoDcrFlrDtlsSet() {
		return autoDcrFlrDtlsSet;
	}

	public void setAutoDcrFlrDtlsSet(Set<AutoDcrFloorDtlsExtn> autoDcrFlrDtlsSet) {
		this.autoDcrFlrDtlsSet = autoDcrFlrDtlsSet;
	}

}
