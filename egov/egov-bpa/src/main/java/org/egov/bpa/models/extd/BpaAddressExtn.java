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

import org.egov.bpa.models.extd.masters.VillageNameExtn;
import org.egov.infra.admin.master.entity.enums.AddressType;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.egov.infra.workflow.entity.State;

public class BpaAddressExtn extends BaseModel {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	@Required(message = "addressTypeMaster.required")
	private AddressType addressTypeMaster;

	private RegistrationExtn registration;
	private String plotDoorNumber;
	private String plotLandmark;
	private String plotNumber;
	private String plotSurveyNumber;
	private String plotBlockNumber;
	private String streetAddress1;
	private String streetAddress2;
	private String cityTown;
	private VillageNameExtn villageName;
	private String indianState;
	private Integer pincode;
	private String plotSurveyType;


	public AddressType getAddressTypeMaster() {
		return addressTypeMaster;
	}

	public void setAddressTypeMaster(AddressType addressTypeMaster) {
		this.addressTypeMaster = addressTypeMaster;
	}

	public RegistrationExtn getRegistration() {
		return registration;
	}

	public void setRegistration(RegistrationExtn registration) {
		this.registration = registration;
	}

	public String getPlotDoorNumber() {
		return plotDoorNumber;
	}

	public void setPlotDoorNumber(String plotDoorNumber) {
		this.plotDoorNumber = plotDoorNumber;
	}

	public String getPlotLandmark() {
		return plotLandmark;
	}

	public void setPlotLandmark(String plotLandmark) {
		this.plotLandmark = plotLandmark;
	}

	public String getPlotNumber() {
		return plotNumber;
	}

	public void setPlotNumber(String plotNumber) {
		this.plotNumber = plotNumber;
	}

	public String getPlotSurveyNumber() {
		return plotSurveyNumber;
	}

	public void setPlotSurveyNumber(String plotSurveyNumber) {
		this.plotSurveyNumber = plotSurveyNumber;
	}

	public String getPlotBlockNumber() {
		return plotBlockNumber;
	}

	public void setPlotBlockNumber(String plotBlockNumber) {
		this.plotBlockNumber = plotBlockNumber;
	}

	public String getStreetAddress1() {
		return streetAddress1;
	}

	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}

	public String getStreetAddress2() {
		return streetAddress2;
	}

	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}

	public String getCityTown() {
		return cityTown;
	}

	public void setCityTown(String cityTown) {
		this.cityTown = cityTown;
	}

	public VillageNameExtn getVillageName() {
		return villageName;
	}

	public void setVillageName(VillageNameExtn villageName) {
		this.villageName = villageName;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	
	public String getIndianState() {
		return indianState;
	}

	public void setIndianState(String indianState) {
		this.indianState = indianState;
	}

	public String getPlotSurveyType() {
		return plotSurveyType;
	}

	public void setPlotSurveyType(String plotSurveyType) {
		this.plotSurveyType = plotSurveyType;
	}
}
