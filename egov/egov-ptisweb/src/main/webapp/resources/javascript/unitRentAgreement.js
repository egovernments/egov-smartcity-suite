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
var headerRow;
var dataRow
var rentAgrmntHeader;
var rentAgrmntIconCell;
var agreementPeriod;
var agreementDate;
var incrementInRent;
var selectedRentalUnitIndex = 0;

jQuery(function () {
	var floorTable = document.getElementById('floorDetails');
	var noOfFloors = floorTable.rows.length - 1;

	headerRow = floorTable.rows[0];
	dataRow = floorTable.rows[1];
	
	rentAgrmntHeader = headerRow.cells[headerRow.cells.length - 1];
	rentAgrmntIconCell = dataRow.cells[dataRow.cells.length - 4];
})

function openRentAgreementWindow(obj, mode) {
	selectedRentalUnitIndex = obj.parentNode.parentNode.rowIndex - 1;
	var noOfFloors = document.getElementById('floorDetails').rows.length - 1;
	if (noOfFloors == 1) {
		agreementPeriod = document.forms[0].agreementPeriod.value;
		agreementDate = document.forms[0].agreementDate.value;
		incrementInRent = document.forms[0].incrementInRent.value;
	} else {
		agreementPeriod = document.forms[0].agreementPeriod[selectedRentalUnitIndex].value;
		agreementDate = document.forms[0].agreementDate[selectedRentalUnitIndex].value;
		incrementInRent = document.forms[0].incrementInRent[selectedRentalUnitIndex].value;
	}
	window.open('../commons/unitRentAgreementDetails.jsp?mode=' + mode, 'Agreement Details', 'width=600, height=350');
}

function setUnitRentAgreementDetails(period, date, increment) {
	var noOfFloors = document.getElementById('floorDetails').rows.length - 1;
	
	if (noOfFloors == 1) {
		document.forms[0].agreementPeriod.value = period;
		document.forms[0].agreementDate.value = date;
		document.forms[0].incrementInRent.value = increment;
	} else {
		document.forms[0].agreementPeriod[selectedRentalUnitIndex].value = period;
		document.forms[0].agreementDate[selectedRentalUnitIndex].value = date;
		document.forms[0].incrementInRent[selectedRentalUnitIndex].value = increment;
	}
}

var floorEffectiveDate;

function setFloorEffectiveDate(obj) {
	var occupancyIndex = obj.parentNode.parentNode.rowIndex - 1;
	var noOfFloors = document.getElementById('floorDetails').rows.length - 1;
	
	if (noOfFloors == 1) {
		floorEffectiveDate = document.forms[0].occupancyDate.value;
	} else {
		floorEffectiveDate = document.forms[0].occupancyDate[occupancyIndex].value;
	}
	
	if (floorEffectiveDate == undefined || floorEffectiveDate == null || floorEffectiveDate == "" || floorEffectiveDate == "DD/MM/YYYY") {
		bootbox.alert("Please enter floor occupation date before proceeding...!");
		return false;
	}
}

function resetRentAgrementDetails(obj) {
	rentalUnitIndex = obj.parentNode.parentNode.parentNode.rowIndex - 1;
	var noOfFloors = document.getElementById('floorDetails').rows.length - 1;
	if (noOfFloors == 1) {
		document.forms[0].agreementPeriod.value = "";
		document.forms[0].agreementDate.value = "";
		document.forms[0].incrementInRent.value = "";
	} else {
		document.forms[0].agreementPeriod[rentalUnitIndex].value = "";
		document.forms[0].agreementDate[rentalUnitIndex].value = "";
		document.forms[0].incrementInRent[rentalUnitIndex].value = "";
	}
}
