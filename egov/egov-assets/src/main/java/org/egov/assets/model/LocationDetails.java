/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.assets.model;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "EGASSET_LOCATIONDETAILS")
@SequenceGenerator(name = LocationDetails.SEQ, sequenceName = LocationDetails.SEQ, allocationSize = 1)
public class LocationDetails extends AbstractAuditable {

	private static final long serialVersionUID = 730236511745178022L;
	
	public static final String SEQ = "seq_egasset_locationdetails";
	
	//Default constructor
	public LocationDetails() {

	}
	
	@Id
	@GeneratedValue(generator = LocationDetails.SEQ, strategy = GenerationType.SEQUENCE)
	private Long id;
	
 
	@ManyToOne
	@JoinColumn(name = "location_id", nullable = false)
    private Boundary location;

	@ManyToOne
    @JoinColumn(name = "zone_id")
    private Boundary zone;
	
	@ManyToOne
    @JoinColumn(name = "revenue_ward_id")
    private Boundary revenueWard;
	
	@ManyToOne
    @JoinColumn(name = "block_id")
    private Boundary block;
	
	@ManyToOne
	@JoinColumn(name = "street_id")
	private Boundary street;
	
	@ManyToOne
	@JoinColumn(name = "election_ward_id")
	private Boundary electionWard;
	
	@Length(max = 150)
	@Column(name = "doornumber")
	private String doorNumber;
	
	@Length(max = 10)
	@Column(name = "pincode")
	private BigDecimal pinCode;
	
	
	public Boundary getLocation() {
		return location;
	}

	public void setLocation(Boundary location) {
		this.location = location;
	}

	public Boundary getZone() {
		return zone;
	}

	public void setZone(Boundary zone) {
		this.zone = zone;
	}

	public Boundary getRevenueWard() {
		return revenueWard;
	}

	public void setRevenueWard(Boundary revenueWard) {
		this.revenueWard = revenueWard;
	}

	public Boundary getBlock() {
		return block;
	}

	public void setBlock(Boundary block) {
		this.block = block;
	}

	public Boundary getStreet() {
		return street;
	}

	public void setStreet(Boundary street) {
		this.street = street;
	}

	public Boundary getElectionWard() {
		return electionWard;
	}

	public void setElectionWard(Boundary electionWard) {
		this.electionWard = electionWard;
	}

	public String getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(String doorNumber) {
		this.doorNumber = doorNumber;
	}

	public BigDecimal getPinCode() {
		return pinCode;
	}

	public void setPinCode(BigDecimal pinCode) {
		this.pinCode = pinCode;
	}

	@Override
	protected void setId(Long id) {

		
	}

	@Override
	public Long getId() {

		return null;
	}
}