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
function showCourtVerdictHeaderTab() {
	document.getElementById('courtverdict_header').style.display = '';
	setCSSClasses('courtVerdictHeaderTab', 'First Active');
	setCSSClasses('demandDetailTab', '');
	hideDemandHeaderTab();

}
function showDemandHeaderTab() {
	document.getElementById('demand_header').style.display = '';
	setCSSClasses('courtVerdictHeaderTab', 'First BeforeActive');
	setCSSClasses('demandDetailTab', 'Last Active ActiveLast');
	hidepropertyHeaderTab();

}
function setCSSClasses(id, classes) {
	document.getElementById(id).setAttribute('class', classes);
	document.getElementById(id).setAttribute('className', classes);
}
function hidepropertyHeaderTab() {
	document.getElementById('courtverdict_header').style.display = 'none';
}
function hideDemandHeaderTab() {
	document.getElementById('demand_header').style.display = 'none';
}

function populateBoundaries() {
	jQuery.ajax({
		url : "/egi/boundary/ajaxboundary-activeblockbylocality",
		type : "GET",
		data : {
			locality : jQuery('#locality').val()
		},
		cache : false,
		dataType : "json",
		success : function(response) {
			jQuery('#wardId').html("");
			jQuery('#blockId').html("");

			jQuery.each(response.results.boundaries, function(j, boundary) {
				if (boundary.wardId) {
					jQuery('#wardId').append(
							"<option value='" + boundary.wardId + "'>"
									+ boundary.wardName + "</option>");
				}
				jQuery('#blockId').append(
						"<option value='" + boundary.blockId + "'>"
								+ boundary.blockName + "</option>");
			});

		},
		error : function(response) {
			jQuery('#wardId').html("");
			jQuery('#blockId').html("");

			bootbox.alert("No boundary details mapped for locality")
		}
	});

}

function populateBlock() {
	jQuery.ajax({
		url : "/egi/boundary/ajaxBoundary-blockByWard.action",
		type : "GET",
		data : {
			wardId : jQuery('#wardId').val()
		},
		cache : false,
		dataType : "json",
		success : function(response) {
			jQuery('#blockId').html("");
			jQuery.each(response, function(j, block) {
				jQuery('#blockId').append(
						"<option value='" + block.blockId + "'>"
								+ block.blockName + "</option>");
			});

		},
		error : function(response) {
			jQuery('#blockId').html("");
			bootbox.alert("No block details mapped for ward")
		}
	});
}

function populateUsage() {
	jQuery.ajax({
		url : "/ptis/common/getusagebypropertytype",
		type : "GET",
		data : {
			propTypeCategory : jQuery('#propTypeCategoryId').val()
		},
		cache : false,
		dataType : "json",
		success : function(response) {

			jQuery('#floorDetailsTable tbody tr').each(
					function() {
						var select = jQuery(this).find("#usage");

						select.empty();
						select.append(jQuery(jQuery('<option>').text(
								'--Select--').attr('value', "-1")));
						jQuery.each(response, function(index, value) {

							select.append(jQuery('<option>').text(
									value.usageName).attr('value', value.id));
						});

					});
		},
		error : function() {

		}
	});
}
function populatePropTypeCategory() {
	var propType = jQuery('#propType').val();
	jQuery.ajax({
		url : "/ptis/common/getcategorybypropertytype",
		type : "GET",
		data : {
			"propType" : propType
		},
		cache : false,
		dataType : "json",
		success : function(response) {
			jQuery('#propTypeCategoryId').html("");
			jQuery('#propTypeCategoryId').append(
					jQuery('<option>').text('--select--').attr('value', ""));

			jQuery.each(response, function(index, propCat) {

				jQuery('#propTypeCategoryId').append(
						"<option value='" + index + "'>" + propCat
								+ "</option>");
			});
			var category = jQuery('#propTypeCategory').val();
			jQuery("#propTypeCategoryId").val(category);
		},

		error : function() {
		}
	});

}
jQuery(window).load(function() {

	var action = jQuery('#action :selected').text();
	viewOrEditToggle(action);

	var propCat = jQuery('#propTypeCategoryId :selected').text();
	if (propCat != null) {
		jQuery('#propTypeCategoryId').val('<c:out value="${propCat}"></c:out>')
	}
	populatePropTypeCategory();
	populateBoundaries();
});

jQuery(document).ready(function() {

	jQuery('#assmntDetails-edit').hide()
	jQuery('#assmntDetails').show()
	jQuery('#demand').hide()

	jQuery("#action").change(function() {
		var action = jQuery('#action :selected').text();
		viewOrEditToggle(action);

	});
});

function viewOrEditToggle(action) {

	if (action != "Re-Assessment") {
		jQuery('#assmntDetails-edit').hide()
		jQuery('#assmntDetails').show()
	} else {
		jQuery('#assmntDetails').hide()
		jQuery('#assmntDetails-edit').show()

		hideOrShowFloorDetails();

		jQuery('#locality').change(function() {
			populateBoundaries();
		});
		jQuery('#propTypeCategoryId').change(function() {
			populateUsage();

		});
		jQuery('#propType').change(function() {

			populatePropTypeCategory();
			hideOrShowFloorDetails();

		});
		/*
		 * jQuery('#propTypeCategoryId').val(jQuery('#propTypeCategoryId
		 * option:selected').val());
		 */
		jQuery("#propTypeCategoryId").val(
				document.getElementById("propTypeCategory").value);
		jQuery("#wardId").val(document.getElementById("ward").value);
		jQuery("#blockId").val(document.getElementById("block").value);
	}
	if (action != "Update Demand Directly") {
		jQuery('#demand').hide()
	} else {
		jQuery('#demand').show()
	}
}

function hideOrShowFloorDetails() {
	var propType = jQuery('#propType').val();
	if (propType == "VAC_LAND") {
		jQuery('#vacLandDetailsdiv').show()
		jQuery('#floorDetailsdiv').hide()
	} else {
		jQuery('#vacLandDetailsdiv').hide()
		jQuery('#floorDetailsdiv').show()
	}
}
function enableDisableFloorArea(obj){
	var rowIdx=jQuery(obj).attr('data-idx');
	rowIdx = Number(rowIdx) + 1;
	var unstrLand = jQuery(obj).find('option:selected').val();
	if(unstrLand === 'true'){
		jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
		.find('.builtuplength').val('');
		jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
			.find('.builtuplength').attr('disabled', true);
		jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
			.find('.builtupbreadth').attr('disabled', true);
		jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
		.find('.builtupbreadth').val('');
		jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
			.find('.builtuparea').removeAttr('disabled');
	} else {
		jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
			.find('.builtuplength').removeAttr('disabled');
		jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
			.find('.builtupbreadth').removeAttr('disabled');
		var length = jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
					.find('.builtuplength').val();
		var breadth = jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
					.find('.builtupbreadth').val();
		calculatePlinthArea(length, breadth, rowIdx);
	}
}

function calculateAreaLength(obj){
	var rowIdx=jQuery(obj).data('idx');
	rowIdx = Number(rowIdx) + 1;
	var length = jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
		.find('.builtuplength').val();
	console.log(jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
			.find('.builtuplength'));
	var breadth = jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
		.find('.builtupbreadth').val();
	calculatePlinthArea(length, breadth, rowIdx);
}

function calculateAreaBreadth(obj){
	var rowIdx=jQuery(obj).data('idx');
	rowIdx = Number(rowIdx) + 1;
	var length = jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
		.find('.builtuplength').val();
	var breadth = jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
		.find('.builtupbreadth').val();
	calculatePlinthArea(length, breadth, rowIdx);
}

function showHideLengthBreadth(){
	var tbl=document.getElementById("floorDetailsTable");
    var tabLength = (tbl.rows.length)-1;
    for(var i=1;i<=tabLength;i++){
         enableDisableLengthBreadth(getControlInBranch(tbl.rows[i],'unstructuredLand'));
    }
}
function calculatePlinthArea(length, breadth, rowIdx){
	if(length != '' && breadth != ''){
		jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
			.find('.builtuparea').val(length*breadth);
	}
	
	jQuery('#floorDetailsTable tbody tr:eq('+rowIdx+')')
	.find('.builtuparea').attr('readonly', 'readonly');
}

function changeHiddenPlotArea(obj){
	
	
	var plotArea = obj.value;
	jQuery('#courtVerdictViewForm').append(jQuery('<input/>', {
        id: 'plotAreaId',
        type: 'hidden',
        name: 'plotId',
        value: plotArea
        }));
}
function changeHiddenLayoutAuthority(obj){
	
	
	var layoutAuthority = obj.value;
	jQuery('#courtVerdictViewForm').append(jQuery('<input/>', {
        id: 'layoutAuthorityId',
        type: 'hidden',
        name: 'layoutId',
        value: layoutAuthority
        }));
}
function addFloors()
{
	var tbl = document.getElementById('floorDetailsTable');
	var rowO = tbl.rows.length;
	
	if (rowO <= 50 && document.getElementById('floorDetailsRow') != null) {
		// get Next Row Index to Generate
		var nextIdx = tbl.rows.length - 1;

		// validate status variable for exiting function
		var isValid = 1;// for default have success value 0

		// validate existing rows in table
		jQuery("#floorDetailsTable tr:not(:first)").find('input, select').each(
				function() {
					if ((jQuery(this).data('optional') === 0)
							&& (!jQuery(this).val())) {
						jQuery(this).focus();
						bootbox.alert(jQuery(this).data('errormsg'));
						isValid = 0;// set validation failure
						return false;
					}
				});

		if (isValid === 0) {
			return false;
		}

		// Generate all textboxes Id and name with new index
		jQuery("#floorDetailsRow").clone().find("input, select").each(
				function() {

					jQuery(this).attr({
						'id' : function(_, id) {
							return id.replace('[0]', '[' + nextIdx + ']');
						},
						'name' : function(_, name) {
							return name.replace('[0]', '[' + nextIdx + ']');
						}
					}).val('');
					if (jQuery(this).attr('data-idx')) {
						jQuery(this).attr('data-idx', nextIdx);
					}

					if (jQuery(this).data('calculate')) {
						jQuery(this).attr(
								'data-calculate',
								jQuery(this).data('calculate').replace('[0]',
										'[' + nextIdx + ']'));
					}

					if (jQuery(this).data('result')) {
						jQuery(this).attr(
								'data-result',
								jQuery(this).data('result').replace('[0]',
										'[' + nextIdx + ']'));
					}
					jQuery(this).attr('readOnly', false);
					// set default selection for dropdown
					if (jQuery(this).is("select")) {
						jQuery(this).prop('selectedIndex', 0);
					}
					if(jQuery(this).hasClass('occupancydate'))
					{
						jQuery(this).val(today);
					}
					
				}).end().appendTo("#floorDetailsTable");

		jQuery("#floorDetailsTable tr:last td span[alt='AddF']").hide();
		reinitializeDatepicker();
		
	}
}

jQuery(document).on('click',"#deleteFloor",function (){
	var table = document.getElementById('floorDetailsTable');
    var rowCount = table.rows.length;
    var counts = rowCount - 1;
    if(counts==1)
	{
		bootbox.alert("This Row cannot be deleted");
		return false;
	}else{	

		jQuery(this).closest('tr').remove();		
		
		jQuery("#floorDetailsTable tr:eq(1) td span[alt='AddF']").show();
		//regenerate index existing inputs in table row
		regenerateIndex('floorDetailsTable');
		return true;
	}
});

function reinitializeDatepicker(){
	jQuery(".datepicker").datepicker({
		format: 'dd/mm/yyyy',
		autoclose:true
	});
}
function regenerateIndex(tableId){
	var idx=0;
	jQuery("#"+tableId+" tr:not(:first)").each(function() {
	jQuery(this).find("input, select").each(function() {
	   jQuery(this).attr({
	      'id': function(_, id) {  
	    	  return id.replace(/\[.\]/g, '['+ idx +']'); 
	       },
	      'name': function(_, name) {
	    	  return name.replace(/\[.\]/g, '['+ idx +']'); 
	      },
	   });
	   
	   if(jQuery(this).attr('data-idx'))
		{
			jQuery(this).attr('data-idx', idx);
		}
	  });
	
	idx++;
});
}