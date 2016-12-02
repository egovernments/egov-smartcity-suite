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
$detailsRowCount = $('#detailsSize').val();
$accountCode=0;
$(document).ready(function(){
var accountCode = $('#accountCode').val();
if (accountCode != "") {
	$('#accountCode option').each(function() {
		if ($(this).val() == accountCode)
			$(this).attr('selected', 'selected');
	});
}
});

function addOverhead() {
	var rowcount = $("#tbloverhead tbody tr").length;
	if (rowcount < 30) {
		if (document.getElementById('overheadRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			nextIdx = $("#tbloverhead tbody tr").length;
			
            var estimateNo = (new Date()).valueOf();
			// validate status variable for exiting function
			var isValid = 1;// for default have success value 0

			// validate existing rows in table
			$("#tbloverhead tbody tr").find('input').each(
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
			$("#overheadRow").clone().find("input, errors, textarea").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
							$(this).attr(
									{
										/*'id' : function(_, id) {
											return id.replace('[0]', '['
													+ nextIdx + ']');
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

					}).end().appendTo("#tbloverhead tbody");
			
			generateSno();
			initializeDatePicker();
			
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

$('#save').click(function() {
			if ($('#overheadForm').valid()) {
				var isSuccess=true;
				$('.removeDefaultValues').each(function(i){
					var idx=$(this).data('idx');
					var lumpSumAmt = $('.lumpsumAmount[data-idx="'+ idx +'"]').val();
					var percentage = $('.percentage[data-idx="'+ idx +'"]').val();
					if (percentage == '' && lumpSumAmt == '') {
						isSuccess=false;
						var message = document.getElementById('overheadRateDetails').value;
						bootbox.alert(message);
						return isSuccess;
					}
					if (percentage != '' && lumpSumAmt != '') {
						isSuccess=false;
						var message = document.getElementById('validateLumpsumAndPercentage').value;
						bootbox.alert(message);
						return isSuccess;
					}
					//required if percentage need to restrict less then 100
					if (parseFloat(percentage) > 100) {
						isSuccess=false;
						var textbox=$('.percentage');
						var message = document.getElementById('validateTotalPercentage').value;
						bootbox.alert(message, function(){ 
							setTimeout(function(){ textbox.focus(); }, 400);
						});
						return isSuccess;
					}
				});
				if(isSuccess)
					return validateEndDate();
			}
			return false;
		});

function validateEndDate()
{
	var isSuccess=true;
	$('.EndDate').each(function(i){
		var endDate = '';
		var idx=$(this).data('idx');
		var startDate=$('.StartDate[data-idx="'+ idx +'"]').data('datepicker').date;
		if($(this).val() != "")
			var endDate=$(this).data('datepicker').date;
		if($(this).val() != "" && startDate>endDate)
		{
			isSuccess=false;
			var message = document.getElementById('validateDate').value;
			bootbox.alert(message, function(){ 
				setTimeout(function(){ textbox.focus(); }, 400);
			});
			return false;
		}
	});
	return isSuccess;
}

function deleteOverhead(obj) {
    var rIndex = getRow(obj).rowIndex;
    
    var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;

	var tbl=document.getElementById('tbloverhead');	
	var rowcount=$("#tbloverhead tbody tr").length;

    if(rowcount<=1) {
		bootbox.alert("This row can not be deleted");
		return false;
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx= 0;
		var sno = 1;
		//regenerate index existing inputs in table row
		jQuery("#tbloverhead tbody tr").each(function() {
		
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
		initializeDatePicker();
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

function initializeDatePicker()
{
	$(".datepicker").datepicker({
		format: "dd/mm/yyyy",
		autoclose:true
	});
	try { $(".datepicker").inputmask(); }catch(e){}	
}

function validatePercentage() {
	$( "input[name$='percentage']" ).on("keyup", function(){
	    var valid = /^[0-9](\d{0,9})(\.\d{0,2})?$/.test(this.value),
	        val = this.value;
	    
	    if(!valid){
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}

function validateLumpsumAmount() {
	$( "input[name$='lumpsumAmount']" ).on("keyup", function(){
	    var valid = /[0-9a-zA-Z-& :,/.()@]+?$/.test(this.value),
	        val = this.value;
	    
	    if(!valid){
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}

function createNewOverhead() {
	window.location = "overhead-newform";
}