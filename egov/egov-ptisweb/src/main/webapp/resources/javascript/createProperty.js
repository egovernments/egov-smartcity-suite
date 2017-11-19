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
/**
 * Includes all the validations for create property
 */
jQuery.noConflict();
function makePropertyTypeMandatory() {
    var propertyType = document.forms[0].propTypeId.options[document.forms[0].propTypeId.selectedIndex].text;
    if (propertyType == "select") {
            bootbox.alert("Please select category of ownership");
            jQuery('#propTypeId').focus();
            return false;
    }
}

function makePlingthAreaReadonly() {
	var isAppurtenantCalRequired = true;
	var totalPlingthArea = 0;
	if (jQuery('#floorDetailsEntered').is(":checked")) {
		if (jQuery('#areaOfPlot').val() == null || jQuery('#areaOfPlot').val() == "") {
			bootbox.alert('Extent of Site is mandatory');
			jQuery('#floorDetailsEntered').prop('checked', false);
			isAppurtenantCalRequired = false;
			return false;
		}
		jQuery('#areaOfPlot').attr('readonly', true);
		jQuery("#floorDetails tr").find('input, select').each(function() {
			if (jQuery(this).attr('id') == 'builtUpArealength' || jQuery(this).attr('id') == 'builtUpAreabreadth') {
				jQuery(this).attr('readonly', true);
			}
			if (jQuery(this).attr('id') == 'unstructuredLand') {
				jQuery(this).attr('disabled', true);
			}
		});
		jQuery("#floorDetails tr").find('input').each(function() {
			if(jQuery(this).attr('id') == 'builtUpArea') {
				if (jQuery(this).val() == null || jQuery(this).val() == "") {
					bootbox.alert("Plinth area is mandatory");
					jQuery('#floorDetailsEntered').prop('checked', false);
					enableBuiltUpAreaDetails();
					isAppurtenantCalRequired = false;
					return false;
				}
				totalPlingthArea = parseFloat(totalPlingthArea) + parseFloat(jQuery(this).val());
				jQuery(this).attr('readonly', true);
			}
		});
		if (isAppurtenantCalRequired) {
			isAppurtenantLand(jQuery('#areaOfPlot').val(), totalPlingthArea);
		}
	} else {
		jQuery('#areaOfPlot').removeAttr('readonly');
		enableBuiltUpAreaDetails();
		jQuery("#floorDetails tr").find('input').each(function() {
			if(jQuery(this).attr('id') == 'builtUpArea') {
				totalPlingthArea = parseFloat(totalPlingthArea) + parseFloat(jQuery(this).val());
			}
		});
		isAppurtenantLand(jQuery('#areaOfPlot').val(), totalPlingthArea);
	}
}

function isAppurtenantLand(extentOfSite, totalPlingthArea) {
	jQuery.ajax({
		url: "/ptis/common/ajaxCommon-isAppurTenant.action",
		type : "GET",
		data : {
			"extentOfSite" : extentOfSite,
			"plingthArea" : totalPlingthArea
		},
		dataType : "json",
	}).done(function(response) {
		if (response.isAppurTenantLand) {
			jQuery('tr.vacantlanddetaills').show();
			jQuery('#vacantLandArea').val(response.vacantLandArea);
			jQuery('#vacantLandArea').attr('readOnly', true);
			jQuery('#extentAppartenauntLand').val(response.extentAppartenauntLand);
		} else {
			jQuery('tr.vacantlanddetaills').hide();
			jQuery('#extentAppartenauntLand').val("");
		}
		jQuery('#appurtenantLandChecked').val(response.isAppurTenantLand);
	});
}

function enableBuiltUpAreaDetails() {
	var unstructured;
	jQuery("#floorDetails tr").find('input, select').each(function() {
		if (jQuery(this).attr('id') == 'unstructuredLand') {
			jQuery(this).removeAttr('disabled');
			unstructured = jQuery(this).val();
		}
		if (jQuery(this).attr('id') == 'builtUpArealength' || jQuery(this).attr('id') == 'builtUpAreabreadth' 
			|| jQuery(this).attr('id') == 'builtUpArea') {
			if (unstructured == "true") {
				if (jQuery(this).attr('id') == 'builtUpArea') {
					jQuery(this).removeAttr('readonly');
				}
				if (jQuery(this).attr('id') == 'builtUpArealength' || jQuery(this).attr('id') == 'builtUpAreabreadth') {
					jQuery(this).attr('readonly', true);
				}
			} else {
				if (jQuery(this).attr('id') == 'builtUpArealength' || jQuery(this).attr('id') == 'builtUpAreabreadth' ) {
					jQuery(this).removeAttr('readonly');
				}
				if (jQuery(this).attr('id') == 'builtUpArea') {
					jQuery(this).attr('readonly', true);
				}
			}
		}
	});
}

function enableFieldsForPropType() {
	var propType = document.forms[0].propTypeId.options[document.forms[0].propTypeId.selectedIndex].text;
	if (propType != "select") {
		if (propType == "Vacant Land") {
			jQuery("#floorDetails tr").find('input, select').each(function() {
				if (jQuery(this).is("select")) {
					jQuery(this).prop('selectedIndex', 0);
				}
				else {
				    jQuery(this).val("");
				}
			});
			jQuery("#floorDetails tr").each(function(){
				rIndex = getRow(this).rowIndex;
				var tbl= document.getElementById('floorDetails');
				if(rIndex > 1) {
					tbl.deleteRow(rIndex);
				}
			});
			enableBuiltUpAreaDetails();
			jQuery("#amenitiesTable tr").find('input:checkbox').each(function() {
				jQuery(this).prop('checked', false);
			});
			jQuery("tr.construction").find('select').each(function() {
				jQuery(this).prop('selectedIndex', 0);
			});
			jQuery('tr.floordetails').hide();
			jQuery('tr.vacantlanddetaills').show();
			jQuery('tr.construction').hide();
			jQuery('tr.amenities').hide();
			jQuery("#appurtenantLandChecked").val("");
			jQuery("#extentAppartenauntLand").val("");
			jQuery('#vacantLandArea').removeAttr('readOnly');
			jQuery('#floorDetailsEntered').prop('checked', false);
			jQuery('tr.floordetails').hide();
			jQuery('#areaOfPlot').val("");
			jQuery('tr.extentSite').hide();
			jQuery('tr.occupancydetails').hide();
			jQuery('tr.bpddetailsheader').hide();
			jQuery('tr.bpddetails').hide();
			jQuery("#apartment").prop('selectedIndex', 0);
			jQuery('div.apartmentRow').hide();
			jQuery('#houseNoSpan').hide();
		} else {
			jQuery('tr.floordetails').show();
			var appurtenantLandChecked = jQuery("#appurtenantLandChecked").val();
			if (appurtenantLandChecked == 'true') {
				jQuery('tr.vacantlanddetaills').show();
			} else {
				jQuery("#vacantLandTable tr, #boundaryData tr").find('input').each(function() {
					jQuery(this).val("");
				});
				jQuery('tr.vacantlanddetaills').hide();
			}
			jQuery('tr.extentSite').show();
			jQuery('tr.occupancydetails').show();
			jQuery('tr.construction').show();
			jQuery('tr.amenities').show();
			jQuery('#areaOfPlot').removeAttr('readOnly');
			jQuery('tr.floordetails').show();
			jQuery('#floorDetailsEntered').prop('checked', false);
			jQuery('tr.bpddetailsheader').show();
			jQuery('div.apartmentRow').show();
			jQuery('#houseNoSpan').show();
		}
	}
}

var lasthd;
var lasttd;
function hideAddRmvBtnForResidFlats() {
	var catType = document.getElementById("propTypeCategoryId").options[document
			.getElementById("propTypeCategoryId").selectedIndex].text;
	var subtractAmountHd = 0;
	var subtractAmountTd = 0;
	if (catType != null && catType == "Residential Flats") {

		document.getElementById("plotArea").style.display = "none";
		document.getElementById("undivArea").style.display = "";

		var tab = document.getElementById("floorDetails");

		if (tab.rows.length > 2) {
			resetFloorsDetails();
		}

		row1 = tab.rows[0];
		row2 = tab.rows[1];

		// if there is add/delete icon is present
		if (row1.cells.length == 20) {
			// remove add/delete for residential flats
			subtractAmountHd = 1;
			subtractAmountTd = 4;
		} else if (row1.cells.length == 21) { // if there is add/delete
												// and rent agreement icon
												// is present
			subtractAmountHd = 2;
			subtractAmountTd = 5;
		}

		lasthd = row1.cells[row1.cells.length - subtractAmountHd];
		lasttd = row2.cells[row2.cells.length - subtractAmountTd];

		jQuery(lasthd).hide();
		jQuery(lasttd).hide();

	} else {
		document.getElementById("plotArea").style.display = "";
		document.getElementById("undivArea").style.display = "none";
		appendAddRemoveBtn();
	}
}

function resetFloorsDetails() {
	var tbl = document.getElementById('floorDetails');
	if (tbl != null) {
		var rowo = tbl.rows;
		resetCreateFloorDetails(rowo);
	}
}

function loadUsages(req, res)
{
	jQuery('select[id="floorUsage"]').find('option:not(:first)').remove();
	jQuery.each(res.results, function( index, option ) {
        jQuery('select[id="floorUsage"]').append(jQuery('<option>').text(option.Text).attr('value', option.Value));
    });
}

function copyDropdown() {
	var dropdwn = document.getElementById('floorUsage').cloneNode(true);
	var row = document.getElementById('floorDetails');
	var count = row.rows.length;
	if (count > 2) {
		for (i = 0; i < count - 2; i++) {
			usgid = 'floorUsage' + i;
			newid = document.getElementById(usgid);
			if (newid != null) {
				newid.options.length = 0;
				for (j = 0; j < dropdwn.length; j++) {
					newid.options[j] = new Option(dropdwn.options[j].text,
							dropdwn.options[j].value);
				}
				newid.selectedIndex = dropdwn.selectedIndex;
			} else {
				continue;
			}
		}
	}
}

function appendAddRemoveBtn() {

	var tab = document.getElementById("floorDetails");
	var hdrCells = tab.rows[0].cells.length;
	hdrRow = tab.rows[0];
	dataRow = tab.rows[1];

	if (lasthd != undefined || lasthd != null || lasthd != "") {
		jQuery(lasthd).show();
	}

	if (lasttd != undefined || lasttd != null || lasttd != "") {
		jQuery(lasttd).show();
	}
}

function resetNonResidentialDetails(propType, floorRow) {
	if (propType == "Non-Residential") {
		for (var j = (floorRow.length - 1); j >= 1; j--) {
			var indexval = j - 1;
			if (indexval == 0) {
				document.getElementById("floorNo").disabled = false;
				document.getElementById("floorType").disabled = false;
				document.getElementById("width").disabled = false;
				document.getElementById("length").disabled = false;
				document.getElementById("interWallArea").disabled = false;
				document.getElementById("width").className = "";
				document.getElementById("length").className = "";
				document.getElementById("interWallArea").className = "";
				document.getElementById("width").readOnly = false;
				document.getElementById("length").readOnly = false;
				document.getElementById("interWallArea").readOnly = false;
				document.getElementById("floorConstType").disabled = false;
				document.getElementById("constrYear").disabled = false;
			} else {
				document.forms[0].width[indexval].setAttribute('name',
						'propertyDetail.floorDetails[' + indexval
								+ '].extraField4');
				document.forms[0].length[indexval].setAttribute('name',
						'propertyDetail.floorDetails[' + indexval
								+ '].extraField5');
				document.forms[0].interWallArea[indexval].setAttribute('name',
						'propertyDetail.floorDetails[' + indexval
								+ '].extraField6');
				document.forms[0].width[indexval].disabled = false;
				document.forms[0].length[indexval].disabled = false;
				document.forms[0].interWallArea[indexval].disabled = false;
				document.forms[0].width[indexval].className = "";
				document.forms[0].length[indexval].className = "";
				document.forms[0].interWallArea[indexval].className = "";
				document.forms[0].width[indexval].readOnly = false;
				document.forms[0].length[indexval].readOnly = false;
				document.forms[0].interWallArea[indexval].readOnly = false;
				eval('document.getElementById("floorConstType' + (indexval - 1)
						+ '").disabled=false');
				document.forms[0].constrYear[indexval].disabled = false;
			}
		}
	} else {
		for (var j = (floorRow.length - 1); j >= 1; j--) {
			var indexval = j - 1;
			if (indexval == 0) {
				document.getElementById("floorNo").disabled = false;
				document.getElementById("floorType").disabled = false;
				document.getElementById("width").value = "";
				document.getElementById("length").value = "";
				document.getElementById("interWallArea").value = "";
				document.getElementById("width").className = "hiddentext";
				document.getElementById("length").className = "hiddentext";
				document.getElementById("interWallArea").className = "hiddentext";
				document.getElementById("width").readOnly = true;
				document.getElementById("length").readOnly = true;
				document.getElementById("interWallArea").readOnly = true;
				document.getElementById("floorConstType").disabled = false;
				document.getElementById("constrYear").disabled = false;
			} else {
				document.forms[0].width[indexval].setAttribute('name',
						'propertyDetail.floorDetails[' + indexval
								+ '].extraField4');
				document.forms[0].length[indexval].setAttribute('name',
						'propertyDetail.floorDetails[' + indexval
								+ '].extraField5');
				document.forms[0].interWallArea[indexval].setAttribute('name',
						'propertyDetail.floorDetails[' + indexval
								+ '].extraField6');
				document.forms[0].floorNo[indexval].disabled = false;
				document.forms[0].floorType[indexval].disabled = false;
				document.forms[0].width[indexval].value = "";
				document.forms[0].length[indexval].value = "";
				document.forms[0].interWallArea[indexval].value = "";
				document.forms[0].width[indexval].className = "hiddentext";
				document.forms[0].length[indexval].className = "hiddentext";
				document.forms[0].interWallArea[indexval].className = "hiddentext";
				document.forms[0].width[indexval].readOnly = true;
				document.forms[0].length[indexval].readOnly = true;
				document.forms[0].interWallArea[indexval].readOnly = true;
				eval('document.getElementById("floorConstType' + (indexval - 1)
						+ '").disabled=false');
				document.forms[0].constrYear[indexval].disabled = false;
			}
		}
	}
}

function enableCorresAddr() {
	if (document.forms[0].corrAddressDiff.checked == true) {
		document.getElementById("corrAddress1").readOnly = false;
		document.getElementById("corrAddress2").readOnly = false;
		document.getElementById("corrPinCode").readOnly = false;
		document.getElementById("corrAddress1").className = "";
		document.getElementById("corrAddress2").className = "";
		document.getElementById("corrPinCode").className = "";
		document.getElementById("add1Row").style.display = "";
		document.getElementById("add2Row").style.display = "";
		document.getElementById("corrAddrHdr").style.display = "";
	} else {
		document.getElementById("add1Row").style.display = "none";
		document.getElementById("add2Row").style.display = "none";
		document.getElementById("corrAddrHdr").style.display = "none";
	}
}

function enableTaxExemptReason() {
	if (document.forms[0].chkIsTaxExempted.checked == true) {
		document.getElementById("taxExemptReason").disabled = false;
	} else {
		document.getElementById("taxExemptReason").disabled = true;
	}
}

function resetDetailsForTenant(obj) {
	var rIndex = getRow(obj).rowIndex;
	var tbl = document.getElementById('floorDetails');
	if (tbl != null) {
		var rowo = tbl.rows;
		for (var j = (rowo.length - 1); j >= 1; j--) {
			var indexval = rIndex - 1;
			var selIndex_Occ = obj.selectedIndex;
			var selText_Occ = obj.options[selIndex_Occ].text;
			if (selText_Occ == 'Tenanted') {
				if (indexval == 0) {
					document.getElementById("rent").className = "";
					document.getElementById("rent").readOnly = false;
				} else {
					document.forms[0].rent[indexval].setAttribute('name',
							'propertyDetail.floorDetails[' + indexval
									+ '].rentPerMonth');
					document.forms[0].rent[indexval].className = "";
					document.forms[0].rent[indexval].readOnly = false;
				}
			} else if (selText_Occ != '--select--') {
				if (indexval == 0) {
					document.getElementById("rent").value = "";
					document.getElementById("rent").className = "hiddentext";
					document.getElementById("rent").readOnly = true;
				} else {
					document.forms[0].rent[indexval].setAttribute('name',
							'propertyDetail.floorDetails[' + indexval
									+ '].rentPerMonth');
					document.forms[0].rent[indexval].value = "";
					document.forms[0].rent[indexval].className = "hiddentext";
					document.forms[0].rent[indexval].readOnly = true;
				}
			}
		}
	}
}

function resetDetailsForTenantOnload() {
	var tbl = document.getElementById('floorDetails');
	if (tbl != null) {
		var rowo = tbl.rows;
		for (var j = 0; j < rowo.length - 1; j++) {
			if (rowo.length == 2) {
				var selText_Occ = document.forms[0].floorOccupation.options[document.forms[0].floorOccupation.selectedIndex].text;
			} else {
				var selText_Occ = document.forms[0].floorOccupation[j].options[document.forms[0].floorOccupation[j].selectedIndex].text;
			}
			if (selText_Occ == 'Tenanted') {
				if (rowo.length == 2) {
					document.getElementById("rent").className = "";
					document.getElementById("rent").readOnly = false;
				} else {
					document.forms[0].rent[j].setAttribute('name',
							'propertyDetail.floorDetails[' + j
									+ '].rentPerMonth');
					document.forms[0].rent[j].className = "";
					document.forms[0].rent[j].readOnly = false;
				}
			} else if (selText_Occ != '--select--') {
				if (rowo.length == 2) {
					document.getElementById("rent").value = "";
					document.getElementById("rent").className = "hiddentext";
					document.getElementById("rent").readOnly = true;
				} else {
					document.forms[0].rent[j].setAttribute('name',
							'propertyDetail.floorDetails[' + j
									+ '].rentPerMonth');
					document.forms[0].rent[j].value = "";
					document.forms[0].rent[j].className = "hiddentext";
					document.forms[0].rent[j].readOnly = true;
				}
			}
		}
	}
}

function enableRentBox() {
	var propType = document.forms[0].propTypeMaster.options[document.forms[0].propTypeMaster.selectedIndex].text;
	var opancy = document.forms[0].occupation.options[document.forms[0].occupation.selectedIndex].text;
	if (propType == "Open Plot" && opancy == "Tenanted") {
		document.getElementById("rentBox").readOnly = false;
		document.getElementById("rentBox").className = "";
		document.getElementById("rentRow").style.display = "";
	} else {
		document.getElementById("rentRow").style.display = "none";
	}
}

function openWindow(fileName) {
	var filePath = "../help/property/" + fileName;
	window.open(filePath, '_blank',
			'location=no,resizable=no,scrollbars=yes,left=600px,top=0px');
}
function makeMandatory() {
	var reason = document.forms[0].mutationId.options[document.forms[0].mutationId.selectedIndex].text;
	if (reason == "BIFURCATION") {
		jQuery('div.parentIndexText').show();
	} else {
		jQuery('#parentIndex').val("");
		jQuery('div.parentIndexText').hide();
	}
}

function showMutationFields(mutationReason) {
	if (mutationReason == "BIFURCATION") {
		jQuery('div.parentIndexText').show();
	} else {
		jQuery('div.parentIndexText').hide();
	}
}

function populateUsg() {
	var propType = document.forms[0].propTypeMaster.options[document.forms[0].propTypeMaster.selectedIndex].text;

	if (propType == "select") {
		populateUsages();
		populateFloorUsages();
	} else {
		if (propType == "Open Plot") {
			populateUsages();
		} else {
			if (propType != "Mixed"
					&& !document.forms[0].isfloorDetailsRequired.checked == true) {
				populateFloorUsages();
			}
		}
	}
}
function populateUsages() {
	jQuery("#propertyCategory").val(document.getElementById("propTypeCategoryId").value);
	populatefloorUsage({
		propTypeCategory : document.getElementById("propTypeCategoryId").value
	});
}


function toggleForResNonRes() {

	var propType = document.forms[0].propTypeMaster.options[document.forms[0].propTypeMaster.selectedIndex].text;
	var propCategory = document.getElementById("propTypeCategoryId");
	var nonResPlotAreaRow = document.getElementById("nonResPlotAreaRow");

	if (propType == "Open Plot"
			&& propCategory.options[propCategory.selectedIndex].text == 'Residential + Non-Residential') {
		nonResPlotAreaRow.style.display = "table-row";
	} else {
		document.getElementById('nonResPlotArea').value = "";
		nonResPlotAreaRow.style.display = "none";
	}

}

function hideAlvForGovtPropTypes() {
	var tbl = document.getElementById("floorDetails");
	var floorRow = tbl.rows;
	for (var i = (floorRow.length - 1); i >= 1; i--) {
		var indexval = i - 1;
		if (indexval == 0) {
			document.getElementById("manualAlv").value = "";
			document.getElementById("manualAlv").className = "hiddentext";
			document.getElementById("manualAlv").readOnly = true;
		} else {
			document.forms[0].manualAlv[indexval].value = "";
			document.forms[0].manualAlv[indexval].className = "hiddentext";
			document.forms[0].manualAlv[indexval].readOnly = true;
		}
	}
}

function enableAlvForNonGovtPropTypes() {
	var tbl = document.getElementById("floorDetails");
	var floorRow = tbl.rows;
	for (var i = (floorRow.length - 1); i >= 1; i--) {
		var indexval = i - 1;
		if (indexval == 0) {
			document.getElementById("manualAlv").className = "";
			document.getElementById("manualAlv").readOnly = false;
		} else {
			document.forms[0].manualAlv[indexval].className = "";
			document.forms[0].manualAlv[indexval].readOnly = false;
		}
	}
}

function toggleFloorDetails() {
	var propType = document.forms[0].propTypeId.options[document.forms[0].propTypeId.selectedIndex].text;
	if (propType == "Vacant Land") {
		jQuery('tr.floordetails').hide();
	} else {
		jQuery('tr.floordetails').show();
	}
	if (propType == "Apartments") {
		bootbox.alert("Please select Apartment/Complex Name");
	}
}

function resetGovtFloorDtls() {
	var tbl = document.getElementById('floorDetails');
	if (tbl != null) {
		resetFloor();
	}
	document.getElementById("dateOfCompletion").value = "";
}

var j;
var dropdown;
var baseRateLocationFactors;
var rentChartLocationFactors;

var constTypeValues = new Array();
var isRentChartExecuted = false;
var isSelected = true;
var dateConstant = new Date(2020, 03, 01); // 01/04/2020 given in the format
											// yyyy/MM-1/dd [(MM-1) as month
											// starts from 0]
var occDate = document.createElement("input");

function populateFloorConstTypeDropDowns() {

	baseRateLocationFactors = document.createElement("select");
	rentChartLocationFactors = document.createElement("select");

	baseRateLocationFactors.id = "baseRateLF";
	rentChartLocationFactors.id = "rentChartLF";

	occDate.value = "02/04/2008";
	oDate = occDate;
	dropdown = baseRateLocationFactors;
	// populateStructuralClassifications();

}

function populateStructuralClassifications() {
	var xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest
			.open(
					"GET",
					"/ptis/common/ajaxCommon-populateStructuralClassifications.action?completionOccupationDate="
							+ oDate.value, true);
	xmlHttpRequest.send();
	xmlHttpRequest.onreadystatechange = function() {
		if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
			successHandler(null, JSON.parse(xmlHttpRequest.responseText));
		}
	};
}

successHandler = function(req, res) {
	var resLength = res.ResultSet.Result.length + 1;
	var dropDownLength = dropdown.length;

	dropdown.options[0] = new Option("select", "-1");

	for (var i = 0; i < res.ResultSet.Result.length; i++) {
		dropdown.options[i + 1] = new Option(res.ResultSet.Result[i].Text,
				res.ResultSet.Result[i].Value);
	}

	while (dropDownLength > resLength) {
		dropdown.options[res.ResultSet.Result.length + 1] = null;
		dropDownLength = dropDownLength - 1;
	}

	var v = oDate.value;
	if (new Date(v.substr(6, 4), (parseInt(v.substr(3, 2)) - 1), v.substr(0, 2)) >= dateConstant) {
		baseRateLocationFactors.options = dropdown.options;
	} else {
		for (var i = 0; i < dropdown.options.length; i++) {
			rentChartLocationFactors.options[i] = new Option(
					dropdown.options[i].text, dropdown.options[i].value);
		}
	}

	if (rentChartLocationFactors.options.length > 1) {
		isRentChartExecuted = true;
	}

	if (!isRentChartExecuted) {
		rentChart();
	}

	if (isRentChartExecuted) {
		populateFloorConstTypesOnValidationErrors();
	}
}

function rentChart() {
	occDate.value = "31/03/2008";
	oDate = occDate;
	dropdown = rentChartLocationFactors;
	populateStructuralClassifications();
}

function populateDropDown(value) {
	var v = oDate.value;
	dropdown.options.length = 0;
	if (v == "" || v == "DD/MM/YYYY") {
		dropdown.options[0] = new Option("select", "-1");
		dropdown.options.selectedIndex = 0;
		return;
	}
	if (new Date(v.substr(6, 4), (parseInt(v.substr(3, 2)) - 1), v.substr(0, 2)) >= dateConstant) {
		copyDropDown(baseRateLocationFactors, dropdown, value);
	} else {
		copyDropDown(rentChartLocationFactors, dropdown, value);
	}
}

function setDropDwn(currentObject) {
	var parentRow = currentObject.parentElement.parentElement.parentElement;

	if (navigator.appName == 'Microsoft Internet Explorer') {
		dropdown = parentRow.cells[12].childNodes[0].childNodes[0];
	} else {
		dropdown = parentRow.cells[12].childNodes[1].childNodes[1];
	}
}

var unitTypeCatDropDown;
var usageDropDown;

function setUnitTypeCatAndUsageDropDwn(currentObject, onError) {

	if (onError) {
		parentRow = currentObject;
	} else {
		parentRow = currentObject.parentElement.parentElement.parentElement;
	}

	if (navigator.appName == 'Microsoft Internet Explorer') {
		unitTypeCatDropDown = parentRow.cells[2].childNodes[0].childNodes[0];
		usageDropDown = parentRow.cells[8].childNodes[0].childNodes[1];
	} else {
		unitTypeCatDropDown = parentRow.cells[2].childNodes[1].childNodes[1];
		var child = parentRow.cells[8].childNodes[1];
		usageDropDown = child.childNodes[3] == undefined ? child.childNodes[1]
				: child.childNodes[3];
	}
}

var openPlotCategory;
var residentialCategory;
var nonResdCategory;

function prepareUnitTypeCategories() {
	openPlotCategory = document.createElement("select");
	residentialCategory = document.createElement("select");
	nonResdCategory = document.createElement("select");

	openPlotCategory.id = 'openPlotCategory';
	residentialCategory.id = 'residentialCategory';
	nonResdCategory.id = 'nonResdCategory';

	unitTypeCatDropDown = openPlotCategory;
	// var propTypeId = document.getElementById("unitType").options[1].value;
	// var propTypeId = 0;
	// populateUnitTypeCategory(propTypeId);
}

function populateUnitTypeCategory(propTypeId) {
	var xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.open("GET",
			"/ptis/common/ajaxCommon!propTypeCategoryByPropType.action?propTypeId="
					+ propTypeId, true);
	xmlHttpRequest.send();
	xmlHttpRequest.onreadystatechange = function() {
		if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
			unitTypeCatSuccessHandler(JSON.parse(xmlHttpRequest.responseText));
		}
	};
}

var isOpenPlotCatPrepared = false;
var isResdCatPrepared = false;
var isNonResdCatPrepared = false;

var isCategoriesPrepared = false;

function unitTypeCatSuccessHandler(res) {
	var resLength = res.ResultSet.Result.length + 1;
	var dropDownLength = unitTypeCatDropDown.length;

	unitTypeCatDropDown.options[0] = new Option("select", "-1");

	for (var i = 0; i < res.ResultSet.Result.length; i++) {
		unitTypeCatDropDown.options[i + 1] = new Option(
				res.ResultSet.Result[i].Text, res.ResultSet.Result[i].Value);
	}

	while (dropDownLength > resLength) {
		unitTypeCatDropDown.options[res.ResultSet.Result.length + 1] = null;
		dropDownLength = dropDownLength - 1;
	}

	isOpenPlotCatPrepared = (openPlotCategory.options.length > 1) ? true
			: false;
	isResdCatPrepared = (residentialCategory.options.length > 1) ? true : false;
	isNonResdCatPrepared = (nonResdCategory.options.length > 1) ? true : false;

	if (isOpenPlotCatPrepared) {
		openPlotCategory.remove(3);
	}

	if (!isResdCatPrepared) {
		// Prepare Residential Categories
		unitTypeCatDropDown = residentialCategory;
		propTypeId = document.getElementById("unitType").options[2].value;
		populateUnitTypeCategory(propTypeId);
	} else if (!isNonResdCatPrepared) {
		// Prepare Non-Residential Categories
		unitTypeCatDropDown = nonResdCategory;
		propTypeId = document.getElementById("unitType").options[3].value;
		populateUnitTypeCategory(propTypeId);
	}

	if (isOpenPlotCatPrepared && isResdCatPrepared && isNonResdCatPrepared) {
		isCategoriesPrepared = true;
	}
}

/**
 * Populates the unit type category dropdown
 * 
 * @param currentObject
 *            The Unit Type object
 */
function populateUnitTypeCatDropDown(currentObject, catValue) {
	var unitType = currentObject.options[currentObject.selectedIndex].text;

	if (unitType != 'select') {
		unitTypeCatDropDown.options.length = 0;
	} else {
		unitTypeCatDropDown.options.length = 1;
	}

	if (unitType == 'Open Plot') {
		copyDropDown(openPlotCategory, unitTypeCatDropDown, catValue);
	} else if (unitType == 'Residential') {
		copyDropDown(residentialCategory, unitTypeCatDropDown, catValue);
	} else if (unitType == 'Non-Residential') {
		copyDropDown(nonResdCategory, unitTypeCatDropDown, catValue);
	}
}

/**
 * Copies the options and returns the index if value is matched with option
 * 
 * @param srcDropDown
 * @param destDropDown
 * @param values
 * @returns
 */
function copyDropDown(from, to, value) {
	var index = 0;
	for (var i = 0; i < from.options.length; i++) {
		to.options[i] = new Option(from.options[i].text, from.options[i].value);
		if (value != null && value == to.options[i].value) {
			index = i;
		}
	}
	to.selectedIndex = index;
}

var openPlotUsages;
var residentialUsages;
var nonResdUsages;

function prepareUsagesForUnitTypes() {
	openPlotUsages = document.createElement("select");
	residentialUsages = document.createElement("select");
	nonResdUsages = document.createElement("select");

	openPlotUsages.id = 'openPlotUsages';
	residentialUsages.id = 'residentialUsages';
	nonResdUsages.id = 'nonResdUsages';

	usageDropDown = openPlotUsages;
	/*
	 * unitTypeId = document.getElementById("unitType").options[1].value;
	 * populateUnitTypeUsages(unitTypeId);
	 */
}

function populateUnitTypeUsages(unitTypeId) {
	var xmlHttpRequest = new XMLHttpRequest();
	xmlHttpRequest.open("GET",
			"/ptis/common/ajaxCommon!usageByPropType.action?propTypeId="
					+ unitTypeId, true);
	xmlHttpRequest.send();
	xmlHttpRequest.onreadystatechange = function() {
		if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
			unitTypeUsagesSuccessHandler(JSON
					.parse(xmlHttpRequest.responseText));
		}
	};
}

var isOpenPlotUsagesPrepared = false;
var isResdUsagesPrepared = false;
var isNonResdUsagesPrepared = false;
var isUsagesPrepared = false;
function unitTypeUsagesSuccessHandler(res) {
	var resLength = res.ResultSet.Result.length + 1;
	var dropDownLength = usageDropDown.length;

	usageDropDown.options[0] = new Option("select", "-1");

	for (var i = 0; i < res.ResultSet.Result.length; i++) {
		usageDropDown.options[i + 1] = new Option(res.ResultSet.Result[i].Text,
				res.ResultSet.Result[i].Value);
	}

	while (dropDownLength > resLength) {
		usageDropDown.options[res.ResultSet.Result.length + 1] = null;
		dropDownLength = dropDownLength - 1;
	}

	isOpenPlotUsagesPrepared = (openPlotUsages.options.length > 1) ? true
			: false;
	isResdUsagesPrepared = (residentialUsages.options.length > 1) ? true
			: false;
	isNonResdUsagesPrepared = (nonResdUsages.options.length > 1) ? true : false;

	if (!isResdUsagesPrepared) {
		// Prepare Residential Usages
		usageDropDown = residentialUsages;
		unitTypeId = document.getElementById("unitType").options[2].value;
		populateUnitTypeUsages(unitTypeId);
	} else if (!isNonResdUsagesPrepared) {
		// Prepare Non-Residential Usages
		usageDropDown = nonResdUsages;
		unitTypeId = document.getElementById("unitType").options[3].value;
		populateUnitTypeUsages(unitTypeId);
	}

	if (isOpenPlotUsagesPrepared && isResdUsagesPrepared
			&& isNonResdUsagesPrepared) {
		isUsagesPrepared = true;
	}
}

function populateUnitTypeUsageDropDown(currentObject, usageValue) {
	var unitType = currentObject.options[currentObject.selectedIndex].text;

	if (unitType != 'select') {
		usageDropDown.options.length = 0;
	} else {
		usageDropDown.options.length = 1;
	}

	if (unitType == 'Open Plot') {
		copyDropDown(openPlotUsages, usageDropDown, usageValue);
	} else if (unitType == 'Residential') {
		copyDropDown(residentialUsages, usageDropDown, usageValue);
	} else if (unitType == 'Non-Residential') {
		copyDropDown(nonResdUsages, usageDropDown, usageValue);
	}
}

var floors;
var propTypeDropDown;
var propType;
var unitTypeDropDown;
var unitType;
var width = null;
var length = null;
var wall = null;

function init() {
	floors = document.getElementById("floorDetails").rows;
	propTypeDropDown = document.getElementById("propTypeMaster");
	propType = propTypeDropDown.options[propTypeDropDown.selectedIndex].text;
}

function toggleUnitTypeAndCategory() {
	init();
	var disable = (propType == 'Mixed') ? false : true;

	for (var j = 1; j < floors.length; j++) {
		var cell1 = floors[j].cells[1];
		var cell2 = floors[j].cells[2];

		if (navigator.appName == 'Microsoft Internet Explorer') {
			// In IE each nested HTML element is a child of outer HTML element
			uType = cell1.childNodes[0].childNodes[0]; // UnitType dropdown
			uTypeCategory = cell2.childNodes[0].childNodes[0]; // UnitTypeCategory
																// dropdown
		} else {
			uType = cell1.childNodes[1].childNodes[1]; // UnitType dropdown
			uTypeCategory = cell2.childNodes[1].childNodes[1]; // UnitTypeCategory
																// dropdown
		}

		if (disable) {
			uType.selectedIndex = 0;
			uTypeCategory.selectedIndex = 0;
		}

		uType.disabled = disable;
		uTypeCategory.disabled = disable;
	}
}

function toggleForMixedPropertyOnUnitType() {
	init();
	if (propType == 'Mixed') {
		for (var j = 1; j < floors.length; j++) {
			toggleFields(floors[j], null);
		}
	}
}

function toggleFields(floor, disable) {
	var unType = null;
	var selectedUnitType = null;
	var cell3 = floor.cells[3];
	var cell5 = floor.cells[5];
	var cell12 = floor.cells[12];
	var cell13 = floor.cells[13];
	var cell15 = floor.cells[15];
	var cell16 = floor.cells[16];
	var cell17 = floor.cells[17];

	if (navigator.appName == 'Microsoft Internet Explorer') {
		flrNo = cell3.childNodes[0].childNodes[0];
		flrType = cell5.childNodes[0].childNodes[0];
		sf = cell12.childNodes[0].childNodes[0];
		af = cell13.childNodes[0].childNodes[0];
		width = cell15.childNodes[0].childNodes[0];
		length = cell16.childNodes[0].childNodes[0];
		wall = cell17.childNodes[0].childNodes[0];
	} else {
		flrNo = cell3.childNodes[1].childNodes[1];
		flrType = cell5.childNodes[1].childNodes[1];
		sf = cell12.childNodes[1].childNodes[1];
		af = cell13.childNodes[1].childNodes[1];
		width = cell15.childNodes[1].childNodes[1];
		length = cell16.childNodes[1].childNodes[1];
		wall = cell17.childNodes[1].childNodes[1];
	}

	if (disable == null) {
		if (navigator.appName == 'Microsoft Internet Explorer') {
			unType = floor.cells[1].childNodes[0].childNodes[0];
		} else {
			unType = floor.cells[1].childNodes[1].childNodes[1];
		}
		selectedUnitType = unType.options[unType.selectedIndex].text;
		disable = (selectedUnitType == 'Non-Residential') ? false : true;
	}

	var className = (disable) ? "hiddentext" : "";

	if (disable) {
		width.value = "";
		length.value = "";
		wall.value = "";
	}

	width.className = length.className = wall.className = className;
	width.disabled = length.disabled = wall.disabled = disable;
	width.readOnly = length.readOnly = wall.readOnly = disable;

	disable = (selectedUnitType != null && (selectedUnitType == 'Residential' || selectedUnitType == 'Non-Residential')) ? false
			: true;

	if (disable) {
		flrNo.selectedIndex = 0;
		flrType.selectedIndex = 0;
		sf.selectedIndex = 0;
		af.selectedIndex = 0;
	}

	flrNo.disabled = flrType.disabled = sf.disabled = af.disabled = disable;

}

function resetFloor() {
	document.forms[0].extraField1.value = "";
	document.forms[0].unitType.value = "-1";
	document.forms[0].unitTypeCategory.value = "-1";
	document.forms[0].floorNo.value = "-10";
	document.forms[0].floorTaxExemptReason.value = "-1";
	document.forms[0].floorType.value = "-1";
	document.forms[0].extraField2.value = "";
	document.forms[0].assessableArea.value = "";
	document.forms[0].floorUsage.value = "-1";
	document.forms[0].floorOccupation.value = "-1";
	document.forms[0].floorWaterRate.value = "-1";
	document.forms[0].floorConstType.value = "-1";
	document.forms[0].constrYear.value = "-1";
	document.forms[0].occupancyDate.value = "";
	document.forms[0].rent.value = "";
	document.forms[0].width.value = "";
	document.forms[0].length.value = "";
	document.forms[0].interWallArea.value = "";
	document.forms[0].manualAlv.value = "";
}

function toggleFloorWaterRate() {
	var rows = document.getElementById('floorDetails').rows.length - 1;
	var propType = document.forms[0].propTypeMaster.options[document.forms[0].propTypeMaster.selectedIndex].text;
	var isDisabled = (propType == "State Government" || propType == "Central Government") ? true
			: false;
	if (rows == 1) {
		if (isDisabled) {
			document.forms[0].floorWaterRate.value = "-1";
		}
		document.forms[0].floorWaterRate.disabled = isDisabled;
	} else {
		for (var i = 0; i < rows; i++) {
			if (isDisabled) {
				document.forms[0].floorWaterRate[i].value = "-1";
			}
			document.forms[0].floorWaterRate[i].disabled = isDisabled;
		}
	}
}

function enableFloorFieldsForGovtProperty() {
	var floorRows = document.getElementById("floorDetails").rows;

	if (navigator.appName == 'Microsoft Internet Explorer') {
		for (var i = 1; i <= (floorRows.length - 1); i++) {
			flrNo = floorRows[i].cells[3].childNodes[0].childNodes[0];
			flrType = floorRows[i].cells[4].childNodes[0].childNodes[0];
			sf = floorRows[i].cells[11].childNodes[0].childNodes[0];
			af = floorRows[i].cells[12].childNodes[0].childNodes[0];
			width = floorRows[i].cells[14].childNodes[0].childNodes[0];
			length = floorRows[i].cells[15].childNodes[0].childNodes[0];
			wall = floorRows[i].cells[16].childNodes[0].childNodes[0];

			width.className = length.className = wall.className = "";

			width.disabled = length.disabled = wall.disabled = false;
			width.readOnly = length.readOnly = wall.readOnly = false;
			flrNo.disabled = flrType.disabled = sf.disabled = af.disabled = false;
		}
	} else {
		for (var i = 1; i <= (floorRows.length - 1); i++) {
			flrNo = floorRows[i].cells[3].childNodes[1].childNodes[1];
			flrType = floorRows[i].cells[4].childNodes[1].childNodes[1];
			sf = floorRows[i].cells[11].childNodes[1].childNodes[1];
			af = floorRows[i].cells[12].childNodes[1].childNodes[1];
			width = floorRows[i].cells[14].childNodes[1].childNodes[1];
			length = floorRows[i].cells[15].childNodes[1].childNodes[1];
			wall = floorRows[i].cells[16].childNodes[1].childNodes[1];

			width.className = length.className = wall.className = "";

			width.disabled = length.disabled = wall.disabled = false;
			width.readOnly = length.readOnly = wall.readOnly = false;
			flrNo.disabled = flrType.disabled = sf.disabled = af.disabled = false;
		}
	}

}

var oldPropertyType;
function onChangeOfPropertyTypeFromMixedToOthers(selectedPropertyType) {
	if (oldPropertyType == "Mixed") {
		populateFloorConstTypesOnValidationErrors();
	}

	oldPropertyType = selectedPropertyType;
}

function hideFloorTaxExemption() {
	jQuery("[id^='floorTaxExemptReason']").attr("disabled", "disabled");
}

function showFloorTaxExemption() {
	jQuery("[id^='floorTaxExemptReason']").removeAttr("disabled");
}

function enableOrDisableSiteOwnerDetails(obj) {
	
	if (jQuery(obj).is(":checked")) {
		jQuery('td.siteowner').show();
	} else {
		jQuery('#siteOwner').val("");
		jQuery('td.siteowner').hide(); 
	}
}

function enableOrDisableBPADetails(obj) {
	if (jQuery(obj).is(":checked")) {
		jQuery('tr.bpddetails').show();
	} else {
		jQuery('#buildingPermissionNo').val("");
		jQuery('#buildingPermissionDate').val("");
		jQuery("#deviationPercentage").prop('selectedIndex', 0);
		jQuery('tr.bpddetails').hide();
	}
}

function enableFieldsForPropTypeView(propType,appurtenantLandChecked) {
	if (propType != "select") {
		if (propType == "Vacant Land") {
			jQuery('tr.floordetails').hide();
			jQuery('tr.vacantlanddetaills').show();
			jQuery('tr.construction, .construction').hide();
			jQuery('tr.amenities').hide();
			jQuery('tr.extentSite').hide();
			jQuery('tr.occupancydetails').hide();
			jQuery('#floorDetailsEntered').prop('checked', false);
			jQuery('tr.floordetails').hide();
			jQuery("#apartment").prop('selectedIndex', 0);
			jQuery('div.apartmentRow').hide();
		} else {
			jQuery('tr.floordetails').show();
			if (appurtenantLandChecked == 'true') {
				jQuery('tr.vacantlanddetaills').show();
			} else {
				jQuery('tr.vacantlanddetaills').hide();
			}
			jQuery('tr.extentSite').show();
			jQuery('tr.occupancydetails').show();
			jQuery('tr.occupancydetails').show();
			jQuery('tr.construction, .construction').show();
			jQuery('tr.amenities').show();
			jQuery('tr.floordetails').show();
			jQuery('#floorDetailsEntered').prop('checked', false);
			jQuery('div.apartmentRow').show();
		}
	}
}

function confirmSubmit(msg) {  
	var ans=confirm(msg + " ?");	
	if(ans) {
		return true;
	}
	else {
		return false;		
	}
}
