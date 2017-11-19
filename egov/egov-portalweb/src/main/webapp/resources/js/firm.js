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

$(document).ready(function(){
	
	$('#firmname').keyup(function(){
		var arr = $(this).val();
	    $(this).val(arr.toUpperCase());
	});
	
	$('#pan').blur(function(){
		var regx = /^[A-Za-z]{5}\d{4}[A-za-z]{1}$/;
		  if ($("#pan").val() != "" && !regx.test($("#pan").val())) {
			  bootbox.alert($('#errorPanNumber').val());
			  return false;
		  }
		  return true;
	});
	
	
});

function validateMobileNumber(obj) {
	
	$("input[name$='mobileNumber']")
	.each(
			function() {
				var regx = /\d{10}$/;
				if ($(this).val() != "" && !regx.test($(this).val())) {
					  bootbox.alert($('#errorMobileNumber').val());
				}
				if($(this).val() == $(obj).val() && $(this).attr('name') != $(obj).attr('name')) {
					bootbox.alert($('#errorUniqueMobileNumber').val());
				}
			});
	
}

function validateEmailId(obj) {
	$("input[name$='emailId']")
	.each(
			function() {
				var regx = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
				if ($(this).val() != "" && !regx.test($(this).val())) {
				  bootbox.alert($('#errorEmailId').val());
				}
				if($(this).val() == $(obj).val() && $(this).attr('name') != $(obj).attr('name')) {
					bootbox.alert($('#errorUniqueEmailid').val());
				}
			});
	
}

function addFirmUsers() {
	var rowcount = $("#tblfirm tbody tr").length;
	if (rowcount < 30) {
		if (document.getElementById('firmRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			nextIdx = $("#tblfirm tbody tr").length;
			
			// validate status variable for exiting function
			var isValid = 1;// for default have success value 0

			// validate existing rows in table
			$("#tblfirm tbody tr").find('input').each(
					function() {
						if (($(this).data('optional') === 0)
								&& (!$(this).val())) {
							$(this).focus();
							bootbox.alert($(this).data('errormsg'));
							isValid = 0;// set validation failure
							return false;
						}
			});

			if (isValid === 0) {
				return false;
			}
			
			// Generate all textboxes Id and name with new index
			$("#firmRow").clone().find("input, errors, textarea").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
							$(this).attr(
									{
										/*'id' : function(_, id) {
											return id.replace(/\d+/, nextIdx);
										},*/
										'name' : function(_, name) {
											return name.replace(/\d+/, nextIdx);
										},
										'data-idx' : function(_,dataIdx)
										{
											return nextIdx;
										}
									});

							// if element is static attribute hold values for
							// next row, otherwise it will be reset
							if (!$(this).data('static')) {
								$(this).val('');
								// set default selection for dropdown
								if ($(this).is("select")) {
									$(this).prop('selectedIndex', 0);
								}
							}
							
							$(this).removeAttr('disabled');
							$(this).prop('checked', false);

					}).end().appendTo("#tblfirm tbody");
			
			generateSno();
			
		}
	} else {
		  bootbox.alert('limit reached!');
	}
}

function generateSno()
{
	var idx=1;
	$(".spansno").each(function(){
		$(this).text(idx);
		idx++;
	});
}

function deleteFirmUsers(obj) {
    var rIndex = getRow(obj).rowIndex;
    
    var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;

	var tbl=document.getElementById('tblfirm');	
	var rowcount=$("#tblfirm tbody tr").length;

    if(rowcount<=1) {
		bootbox.alert("This row can not be deleted");
		return false;
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx= 0;
		var sno = 1;
		//regenerate index existing inputs in table row
		jQuery("#tblfirm tbody tr").each(function() {
		
				jQuery(this).find("input, select, textarea, errors, span, input:hidden").each(function() {
					var classval = jQuery(this).attr('class');
					if(classval == 'spansno') {
						jQuery(this).text(sno);
						sno++;
					} else {
					jQuery(this).attr({
					      'name': function(_, name) {
					    	  if(!(jQuery(this).attr('name')===undefined))
					    		  return name.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'id': function(_, id) {
					    	  if(!(jQuery(this).attr('id')===undefined))
					    		  return id.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'class' : function(_, name) {
								if(!(jQuery(this).attr('class')===undefined))
									return name.replace(/\[.\]/g, '['+ idx +']'); 
							},
						  'data-idx' : function(_,dataIdx)
						  {
							  return idx;
						  }
					   });
					}
			    });
				
				idx++;
		});
		
		generateSno();
		return true;
	}	
}

function getRow(obj) {
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode ;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

$('#save').click(function() {
		
		var mobileNumber = $('#tblfirm tbody tr').length - 1;
		var index;
		var regxMobile = /\d{10}$/;
		var regxEmail = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

		for (var i = 0; i <= mobileNumber; i++) {
			index = i;
			var mobileNumber = document.getElementById('tempFirmUsers' + index + '.mobileNumber').value;
			var email = document.getElementById('tempFirmUsers' + index + '.emailId').value;	
			if (mobileNumber != "" && !regxMobile.test(mobileNumber)) {
				  bootbox.alert('Pleas Enter Valid Mobile Number For Row No. ' +index+1);
				  return false;
			}
			
			if (email != "" && !regxEmail.test(email)) {
				  bootbox.alert('Pleas Enter Valid Email Id For Row No. ' +index+1);
				  return false;
			}
			
		}
		
		var regxPan = /^[A-Za-z]{5}\d{4}[A-za-z]{1}$/;
		  if ($("#pan").val() != "" && !regxPan.test($("#pan").val())) {
			  bootbox.alert($('#errorPanNumber').val());
			  return false;
		  }
		return true;
	return false;
});