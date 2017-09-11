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
package org.egov.restapi.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AmenitiesDetails implements Serializable {

	private Boolean hasLift;
	private Boolean hasToilet;
	private Boolean hasWaterTap;
	private Boolean hasElectricity;
	private Boolean hasAttachedBathroom;
	private Boolean hasWaterHarvesting;
	private Boolean hasCableConnection;

	public Boolean hasLift() {
		return hasLift;
	}

	public void setLift(Boolean hasLift) {
		this.hasLift = hasLift;
	}

	public Boolean hasToilet() {
		return hasToilet;
	}

	public void setToilet(Boolean hasToilet) {
		this.hasToilet = hasToilet;
	}

	public Boolean hasWaterTap() {
		return hasWaterTap;
	}

	public void setWaterTap(Boolean hasWaterTap) {
		this.hasWaterTap = hasWaterTap;
	}

	public Boolean hasElectricity() {
		return hasElectricity;
	}

	public void setElectricity(Boolean hasElectricity) {
		this.hasElectricity = hasElectricity;
	}

	public Boolean hasAttachedBathroom() {
		return hasAttachedBathroom;
	}

	public void setAttachedBathroom(Boolean hasAttachedBathroom) {
		this.hasAttachedBathroom = hasAttachedBathroom;
	}

	public Boolean hasWaterHarvesting() {
		return hasWaterHarvesting;
	}

	public void setWaterHarvesting(Boolean hasWaterHarvesting) {
		this.hasWaterHarvesting = hasWaterHarvesting;
	}

	public Boolean hasCableConnection() {
		return hasCableConnection;
	}

	public void setCableConnection(Boolean hasCableConnection) {
		this.hasCableConnection = hasCableConnection;
	}

	@Override
	public String toString() {
		return "AmenitiesRequest [hasLift=" + hasLift + ", hasToilet=" + hasToilet + ", hasWaterTap=" + hasWaterTap
				+ ", hasElectricity=" + hasElectricity + ", hasAttachedBathroom=" + hasAttachedBathroom
				+ ", hasWaterHarvesting=" + hasWaterHarvesting + ", hasCableConnection=" + hasCableConnection + "]";
	}

}
