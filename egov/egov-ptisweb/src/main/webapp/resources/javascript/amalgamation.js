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

jQuery.noConflict();
jQuery(document).ready(function(){
	try {
		jQuery(".datepicker").datepicker({
			format : "dd/mm/yyyy",
			autoclose:true
		});
	} catch (e) {
		console.warn("No Date Picker " + e);
	}
	var mode=jQuery('#mode').val();
	setupDefaultLogics(mode);
});

function setupDefaultLogics(mode)
{
	if(mode == 'edit'){
		var rowIdx=0;
		jQuery('#floorDetailsTbl tbody tr').each(function(){
			var usage = jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')').find('.floorusage').find('option:selected').text();
			enableDisableFirmname(rowIdx, usage);
			enableDisableFloorArea(rowIdx, jQuery(this).find('.unstructuredland').find('option:selected').val());
			jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')').find('.occupancydate').prop('disabled', true);
			rowIdx++;
		});
		rowIdx = 0;
		jQuery('#amalgamatedPropertiesTbl tbody tr').each(function(){
			jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+')').find('.amlgpropownername,.amlgpropmobileno, .amlgpropaddress').attr('readonly','readonly');
			rowIdx++;
		});
		
		rowIdx = 0;
		jQuery('#ownerInfoTbl tbody tr').each(function(){
			
				jQuery('#ownerInfoTbl tbody tr:eq('+rowIdx+')').find('input').attr('readonly', 'readonly');
				jQuery('#ownerInfoTbl tbody tr:eq('+rowIdx+')').find('select').attr('disabled', 'disabled');
			rowIdx++;
		});
	}
	var userDesign = jQuery('#currentDesignation').val();
	if(userDesign == 'Commissioner') {
		jQuery('#Forward').hide();
	}
}

jQuery(document).on('change', ".unstructuredland", function () {
	var rowIdx=jQuery(this).attr('data-idx');
	var length = jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
		.find('.builtuplength').val();
	var breadth = jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
		.find('.builtupbreadth').val();
	if(length != '' && breadth != ''){
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
			.find('.builtuparea').val(length*breadth);
	}
});

function enableDisableFloorArea(rowIdx, obj){
	if(obj === 'true'){
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
		.find('.builtuplength').val('');
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
			.find('.builtuplength').attr('readonly', 'readonly');
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
			.find('.builtupbreadth').attr('readonly', 'readonly');
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
		.find('.builtupbreadth').val('');
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
			.find('.builtuparea').removeAttr('readonly');
	} else {
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
			.find('.builtuplength').removeAttr('readonly');
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
			.find('.builtupbreadth').removeAttr('readonly');
		var length = jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
					.find('.builtuplength').val();
		var breadth = jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
					.find('.builtupbreadth').val();
		calculatePlinthArea(length, breadth, rowIdx);
	}
}

jQuery(document).on('change', ".unstructuredland", function () {
	var rowIdx=jQuery(this).attr('data-idx');
	var unstrLand = jQuery(this).find('option:selected').val();
	enableDisableFloorArea(rowIdx, unstrLand);
});

jQuery(document).on('change', ".floorusage", function () {
	var rowIdx=jQuery(this).attr('data-idx');
	var floorUsage = jQuery(this).find('option:selected').text();
	enableDisableFirmname(rowIdx, floorUsage);
});

function enableDisableFirmname(rowIdx, usage){
	if(usage == 'Residence'){
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
			.find('.firmname').val('');
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
			.find('.firmname').attr('readonly', 'readonly');
	} else {
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
			.find('.firmname').removeAttr('readonly');
	}
}

jQuery(document).on('blur', ".builtuplength", function(){
	var rowIdx=jQuery(this).data('idx');
	var length = jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
		.find('.builtuplength').val();
	var breadth = jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
		.find('.builtupbreadth').val();
	calculatePlinthArea(length, breadth, rowIdx);
});

jQuery(document).on('blur', ".builtupbreadth", function(){
	var rowIdx=jQuery(this).data('idx');
	var length = jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
		.find('.builtuplength').val();
	var breadth = jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
		.find('.builtupbreadth').val();
	calculatePlinthArea(length, breadth, rowIdx);
});

function calculatePlinthArea(length, breadth, rowIdx){
	if(length != '' && breadth != ''){
		jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
			.find('.builtuparea').val(length*breadth);
	}
	
	jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')')
	.find('.builtuparea').attr('readonly', 'readonly');
}

jQuery(document).on('blur', ".amlgpropassessmentno", function () {
	var retainerProp=jQuery('#retainerPropertyId').val();
	var rowIdx=jQuery(this).data('idx');
	if(retainerProp == jQuery(this).val()){
		bootbox.alert('Retainer property cannot be used as amalgamated property!');
		jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+') input').val('');
		return;
	}
	var tableObj=document.getElementById('amalgamatedPropertiesTbl');
	var lastRow = tableObj.rows.length;
	var currentAssessment = jQuery(this).val();
    for(var i=0;i<lastRow-2;i++){
    	if(lastRow-1!=1 && currentAssessment == jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+i+')').find('.amlgpropassessmentno').val()){
    		bootbox.alert('This property is already added to Amalgamated Properties list! Please give other property which is not added');
    		jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+') input').val('');
    		return;
    	}
	}
    jQuery.ajax({
        url: "/ptis/common/amalgamation/getamalgamatedpropdetails",
        type: "GET",
        data:{"assessmentNo":jQuery(this).val()},
        beforeSend:function()
        {
        	jQuery('.loader-class').modal('show', {backdrop: 'static'});
        },
        success: function(property){
        	if(property.assessmentNo)
        	{
        		if(property.validationMsg == ''){
        			jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+')')
    				.find('.amlgpropassessmentno').attr("value",property.assessmentNo);
        			jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+')')
    				.find('.amlgpropassessmentno').attr('readonly', true);
        			jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+')')
    				.find('.amlgpropassessmentno').attr('disabled', 'disabled');
        			jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+')')
        				.find('.amlgpropownername').val(property.ownerName);
	        		jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+')')
	    				.find('.amlgpropmobileno').val(property.mobileNo);
	        		jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+')')
	    				.find('.amlgpropaddress').val(property.propertyAddress);
	        		for (i = 0; i < property.owners.length; i++) { 
	        			var tableObj=document.getElementById('ownerInfoTbl');
	        			var tbody=tableObj.tBodies[0];
	        			var lastRow = tableObj.rows.length;
	        			var rowObj = tableObj.rows[1].cloneNode(true);
	        			var nextIdx=(lastRow-1);
	        			jQuery(rowObj).find("input, select").each(function() {
	        				jQuery(this).attr({
	    								'id' : function(_, id) {
	    									return id.replace('[0]', '['
	    											+ nextIdx + ']');
	    								},
	    								'name' : function(_, name) {
	    									return name.replace('[0]', '['
	    											+ nextIdx + ']');
	    									
	    								}
	        				});  
	        				if(jQuery(this).attr('data-idx'))
	        					{
	        						jQuery(this).attr('data-idx', nextIdx);
	        					}
	        						jQuery(this).val('');
	    					
	        				});
	        				tbody.appendChild(rowObj);
	        				jQuery('#ownerInfoTbl tbody tr:last').find('input').val('');
	        				jQuery("input[name='amalgamationOwnersProxy["+ nextIdx +"].upicNo']").val(currentAssessment);
	        				jQuery("input[name='amalgamationOwnersProxy["+ nextIdx +"].owner.aadhaarNumber']").val(property.owners[i].aadhaarNumber);
	        				jQuery("input[name='amalgamationOwnersProxy["+ nextIdx +"].owner.mobileNumber']").val(property.owners[i].mobileNumber);
	        				jQuery("input[name='amalgamationOwnersProxy["+ nextIdx +"].owner.name']").val(property.owners[i].ownerName);
	        				jQuery("select[name='amalgamationOwnersProxy["+ nextIdx +"].owner.gender']").val(property.owners[i].gender);
	        				jQuery("input[name='amalgamationOwnersProxy["+ nextIdx +"].owner.emailId']").val(property.owners[i].emailId);
	        				jQuery("select[name='amalgamationOwnersProxy["+ nextIdx +"].owner.guardianRelation']").val(property.owners[i].guardianRelation);
	        				jQuery("input[name='amalgamationOwnersProxy["+ nextIdx +"].owner.guardian']").val(property.owners[i].guardian);
	        			}
        		} else {
        			bootbox.alert(property.validationMsg);
        			jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+') input')
        				.val('');
        			return;
        		}
        	}
        	else{
        		jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+rowIdx+') input')
        			.val('');
        	}
        	
        },
        complete:function()
        {
        	jQuery('.loader-class').modal('hide');
        },
        error:function()
        {
        }
    });
});

jQuery(document).on('blur', ".txtaadhar", function () {

	var rowidx=jQuery(this).data('idx');
	var aadharNo = jQuery(this).val();
    jQuery.ajax({
    	url: "/egi/aadhaar/"+aadharNo,
        type: "GET",
        beforeSend:function()
        {
        	jQuery('.loader-class').modal('show', {backdrop: 'static'});
        },
        success: function(){
        	var userInfoObj = jQuery.parseJSON(value);
			if(userInfoObj.uid == aadharNo) {
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.name']").val(userInfoObj.name);
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.name']").attr('readonly', true);
				if(userInfoObj.gender == 'M' || userInfoObj.gender == 'Male') {
					jQuery("select[name='amalgamationOwnersProxy["+ rowidx +"].owner.gender']").val("MALE");
				} else if (userInfoObj.gender == 'F' || userInfoObj.gender == 'Female') {
					jQuery("select[name='amalgamationOwnersProxy["+ rowidx +"].owner.gender']").val("FEMALE");
				} else {
					jQuery("select[name='amalgamationOwnersProxy["+ rowidx +"].owner.gender']").val("OTHERS");
				} 
				jQuery("select[name='amalgamationOwnersProxy["+ rowidx +"].owner.gender']").attr('disabled','disabled');
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.mobileNumber']").val(userInfoObj.phone);
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.mobileNumber']").attr('readonly', true);
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.emailId']").attr('readonly', true);
				jQuery("select[name='amalgamationOwnersProxy["+ rowidx +"].owner.guardianRelation']").removeAttr('disabled');
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.guardian']").val(userInfoObj.careof);
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.guardian']").attr('readonly', true);
			} else {
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.aadhaarNumber']").val("");
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.name']").val("");
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.name']").attr('readonly', false);
				jQuery("select[name='amalgamationOwnersProxy["+ rowidx +"].owner.gender']").removeAttr('disabled');
				jQuery("select[name='amalgamationOwnersProxy["+ rowidx +"].owner.gender']").val("");
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.mobileNumber']").val("").attr('readonly', false);
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.emailId']").attr('readonly', false);
				jQuery("select[name='amalgamationOwnersProxy["+ rowidx +"].owner.guardianRelation']").removeAttr('disabled');
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.guardian']").val("");
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.guardian']").attr('readonly', false);
				if(aadharNo != "NaN") {
					bootbox.alert("Aadhar number is not valid");
				}
		   }
        	
        },
        complete:function()
        {
        	jQuery('.loader-class').modal('hide');
        },
        error:function()
        {
        }
    });
});

jQuery(document).on('blur', ".mobileno", function () {

	var rowidx=jQuery(this).data('idx');
    jQuery.ajax({
        url: "/ptis/common/ajaxCommon-getUserByMobileNo.action",
        type: "GET",
        data:{"mobileNumber":jQuery(this).val()},
        dataType: "json",
        beforeSend:function()
        {
        	jQuery('.loader-class').modal('show', {backdrop: 'static'});
        },
        success: function(response){
        	if(response.exists) {
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.name']").val(response.name);
				jQuery("select[name='amalgamationOwnersProxy["+ rowidx +"].owner.gender']").val(response.gender);
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.mobileNumber']").val(response.mobileNumber);
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.emailId']").val(response.email);
				jQuery("select[name='amalgamationOwnersProxy["+ rowidx +"].owner.guardianRelation']").val(response.guardianRelarion);
				jQuery("input[name='amalgamationOwnersProxy["+ rowidx +"].owner.guardian']").val(response.guardian);
		    }
        },
        complete:function()
        {
        	jQuery('.loader-class').modal('hide');
        },
        error:function()
        {
        }
    });
});

jQuery(document).on('change', ".proptypecategory", function () {
	var propCat = jQuery('#propertyCategory').val();
	jQuery.ajax({
		url: "/ptis/common/getusagebypropertytype",
        type: "GET",
        data:{"propTypeCategory":propCat},
		success: function (response) {
			var rowIdx=0;
			jQuery('#floorDetailsTbl tbody tr').each(function(){
				jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')').find('#floorUsage').empty();
				jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')').find('#floorUsage').append(jQuery('<option>').text('--Select--').attr('value', "-1"));
				jQuery.each(response, function(index, value) {
					jQuery('#floorDetailsTbl tbody tr:eq('+rowIdx+')').find('#floorUsage').append(jQuery('<option>').text(value.usageName).attr('value', value.id));
				});
				rowIdx++;
			});
		}, 
		error: function () {
		}
	});
});

function addAmalgamatedProperties()
{     
			var tableObj=document.getElementById('amalgamatedPropertiesTbl');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			
			var nextIdx=(lastRow-1);
			jQuery(rowObj).find("input, select").each(
					function() {
					
					jQuery(this).attr({
								'id' : function(_, id) {
									return id.replace('[0]', '['
											+ nextIdx + ']');
								},
								'name' : function(_, name) {
									return name.replace('[0]', '['
											+ nextIdx + ']');
									
								}
					}).attr("value",'');  
					
					if(jQuery(this).attr('data-idx'))
					{
						jQuery(this).attr('data-idx', nextIdx);
					}
					
					
		   });
			tbody.appendChild(rowObj);
			jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+nextIdx+')')
			.find('.amlgpropassessmentno').attr('readonly', false);
			jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+nextIdx+')')
			.find('.amlgpropassessmentno').removeAttr('disabled');
			 jQuery('#amalgamatedPropertiesTbl tbody tr:last').find('input').val('');
		   
}

jQuery(document).on('click',"#delete_row",function (){
	var table = document.getElementById('amalgamatedPropertiesTbl');
    var rowCount = table.rows.length;
    var currentAssessment = jQuery('#amalgamatedPropertiesTbl tbody tr:eq('+jQuery(this).closest('tr').index()+')')
	.find('.amlgpropassessmentno').val();
    var counts = rowCount - 1;
    if(counts==1)
	{
		bootbox.alert("This Row cannot be deleted");
		return false;
	}else{	
		removeOwners(currentAssessment);
		jQuery(this).closest('tr').remove();		
		jQuery("#amalgamatedPropertiesTbl tr:eq(1) td span[alt='AddF']").show();
		//regenerate index existing inputs in table row
		regenerateIndex('amalgamatedPropertiesTbl');
		return true;
	}
});

function removeOwners(currentAssessment){
	var table = document.getElementById('ownerInfoTbl');
    for(var i=(table.rows.length)-1;i>0;i--){
    	if(jQuery("input[name='amalgamationOwnersProxy["+ i +"].upicNo']").val() == currentAssessment){
    		jQuery("#ownerInfoTbl tbody tr:eq("+i+")").remove();
			jQuery("#ownerInfoTbl tr:eq(1) td span[alt='AddF']").show();
   	 	}
    }
    regenerateIndex('ownerInfoTbl');
    return true;
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

function addOwners()
{     
	
			var tableObj=document.getElementById('ownerInfoTbl');
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			
			var nextIdx=(lastRow-1);
			jQuery(rowObj).find("input, select").each(
					function() {
					
					jQuery(this).attr({
								'id' : function(_, id) {
									return id.replace('[0]', '['
											+ nextIdx + ']');
								},
								'name' : function(_, name) {
									return name.replace('[0]', '['
											+ nextIdx + ']');
									
								}
					});  
					
					if(jQuery(this).attr('data-idx'))
					{
						jQuery(this).attr('data-idx', nextIdx);
					}
					
					if(jQuery(this).attr('readonly'))
					{
						jQuery(this).removeAttr('readonly')
					}
					
					if(jQuery(this).attr('disabled'))
					{
						jQuery(this).removeAttr('disabled')
					}
					
					jQuery(this).val('');
					
		   });
			tbody.appendChild(rowObj);
			jQuery('#ownerInfoTbl tbody tr:last').find('input').val('');
}

jQuery(document).on('click', ".deleteowner", function () {

	var isOwnerOfParent = jQuery(this).closest('tr').find('.ownerofparent').val();
	if(isOwnerOfParent === "true"){
		bootbox.alert("Owners from the retainer property cannot be deleted");
		return false;
	}
	var table = document.getElementById('ownerInfoTbl');
    var rowCount = table.rows.length;
    var counts = rowCount - 1;
    if(counts==1)
	{
		bootbox.alert("This Row cannot be deleted");
		return false;
	}else{	
			jQuery(this).closest('tr').remove();		
			jQuery("#ownerInfoTbl tr:eq(1) td span[alt='AddF']").show();
			regenerateIndex('ownerInfoTbl');
			return true;
	}
});

function addFloors()
{
	var tbl = document.getElementById('floorDetailsTbl');
	var rowO = tbl.rows.length;
	var today = document.getElementById('instStartDtId').value;
	/*var dd = today.getDate();
	var mm = today.getMonth()+1; 
	var yyyy = today.getFullYear();
	today = dd+'/'+mm+'/'+yyyy;*/
	
	if (rowO <= 50 && document.getElementById('floorDetailsRow') != null) {
		// get Next Row Index to Generate
		var nextIdx = tbl.rows.length - 1;

		// validate status variable for exiting function
		var isValid = 1;// for default have success value 0

		// validate existing rows in table
		jQuery("#floorDetailsTbl tr:not(:first)").find('input, select').each(
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
					
				}).end().appendTo("#floorDetailsTbl");

		jQuery("#floorDetailsTbl tr:last td span[alt='AddF']").hide();
		reinitializeDatepicker();
		patternvalidation();
	}
}

jQuery(document).on('click',"#deleteFloor",function (){
	var table = document.getElementById('floorDetailsTbl');
    var rowCount = table.rows.length;
    var counts = rowCount - 1;
    if(counts==1)
	{
		bootbox.alert("This Row cannot be deleted");
		return false;
	}else{	

		jQuery(this).closest('tr').remove();		
		
		jQuery("#floorDetailsTbl tr:eq(1) td span[alt='AddF']").show();
		//regenerate index existing inputs in table row
		regenerateIndex('floorDetailsTbl');
		return true;
	}
});

function reinitializeDatepicker(){
	jQuery(".datepicker").datepicker({
		format: 'dd/mm/yyyy',
		autoclose:true
	});
}

function enableOccupancyDate(){
	var rowIdx = 0;
	jQuery('#floorDetailsTbl tbody tr').each(
			function() {
				jQuery('#floorDetailsTbl tbody tr:eq(' + rowIdx + ')').find(
						'.occupancydate').prop('disabled', false);
				rowIdx++;
			});
}