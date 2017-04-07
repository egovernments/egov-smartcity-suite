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
var reportdatatable;
jQuery(document).ready(function($) {
	if($('#noJAORSAMessage') && $('#noJAORSAMessage').val())
		bootbox.alert($('#noJAORSAMessage').val());

});
function validateMobileNumber(obj)
{

	var text = obj.value;
	if(text!=''){
		
		if(text.length!=10)
		{		
			obj.value="";
			
			bootbox.alert("Invalid Mobile length");
			return false;
	
		}
	validatePhoneNumber(obj,'mobile');
	}
	return true;
}

function validatePhoneNumber(obj,mode){
	var text = obj.value;
	if(text!=""){
		
	var msg;
	if(mode=='mobile')
		msg='<s:text name="invalid.mobileno" />';
	else
		msg='<s:text name="invalid.teleno" />';
	if(isNaN(text))
	{
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = msg;
		obj.value="";
		return false;
	}
	if(text<=0)
	{
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = msg;
		obj.value="";
		return false;
	}
	if(text.replace(".","~").search("~")!=-1)
	{
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="period.notallowed" />';
		obj.value='';
		return false;
	}
	if(text.replace("+","~").search("~")!=-1)
	{
		dom.get("bpa_error_area").style.display = '';
		document.getElementById("bpa_error_area").innerHTML = '<s:text name="plus.notallowed" />';
		obj.value='';
		return false;
	}
	}
	return true;
}
$('#ward').change(function(){
	jQuery.ajax({
		url: "/egi/public/boundary/ajaxBoundary-blockByWard.action",
		type: "GET",
		data: {
			wardId : jQuery('#ward').val()
		},
		cache: false,
		dataType: "json",
		success: function (response) {
			console.log("success"+response);
			jQuery('#block').html("");
			jQuery('#block').append("<option value=''>Select</option>");
			jQuery.each(response, function(index, value) {
				jQuery('#block').append($('<option>').text(value.blockName).attr('value', value.blockId));
			});
		}, 
		error: function (response) {
			jQuery('#block').html("");
			jQuery('#block').append("<option value=''>Select</option>");
			console.log("failed");
		}
	});
});

$('#serviceType').change(function(){
	jQuery.ajax({
		url: "/bpa/ajax/getAdmissionFees",
		type: "GET",
		data: {
			serviceType : jQuery('#serviceType').val()
		},
		cache: false,
		dataType: "json",
		success: function (response) {
			
				$("#admissionfeeAmount").prop("disabled", true);
				jQuery('#admissionfeeAmount').val(response);

		}, 
		error: function (response) {
			
		}
	});
});
/*$('#serviceType').change(function(){
	jQuery.ajax({
		url: "/bpa/ajax/getDocumentListByServiceType",
		type: "GET",
		data: {
			serviceType : jQuery('#serviceType').val()
		},
		cache: false,
		dataType: "json",
		success: function (response) {
			

		}, 
		error: function (response) {
			
		}
	});
});*/


//toggle between multiple tab
jQuery('form').validate({
	ignore: ".ignore",
	invalidHandler: function(e, validator){
	if(validator.errorList.length)
	$('#settingstab a[href="#' + jQuery(validator.errorList[0].element).closest(".tab-pane").attr('id') + '"]').tab('show');
	}
	});

$('#buttonSubmit').click(function(e) {
	if ($('form').valid()) {
		console.log('submitted')
	} else {
		e.preventDefault();
	}
});