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
		alert("Please enter floor occupation date before proceeding...!");
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
