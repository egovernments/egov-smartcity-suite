/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
$deletedAmt = 0;
$(document).ready(function(){
	getLineEstimateDate();
});

$(document).bind("input propertychange", function (e) {
	if($(e.target).attr('name').match(/estimateAmount$/))
	{
		calculateEstimatedAmountTotal();
	}
});

function getSubSchemsBySchemeId(schemeId) {
	$.ajax({
		url: "../lineestimate/getsubschemesbyschemeid/"+schemeId,     
		type: "GET",
		dataType: "json",
		success: function (response) {
			$('#subScheme').empty();
			$('#subScheme').append($("<option value=''>Select from below</option>"));
			var responseObj = JSON.parse(response);
			$.each(responseObj, function(index, value) {
				$('#subScheme').append($('<option>').text(responseObj[index].name).attr('value', responseObj[index].id));
			});
		}, 
		error: function (response) {
			console.log("failed");
		}
	});
}

function addLineEstimate() {
	var rowcount = $("#tblestimate tbody tr").length;
	if (rowcount < 30) {
		if (document.getElementById('estimateRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = $("#tblestimate tbody tr").length;
            var estimateNo = (new Date()).valueOf();
			// validate status variable for exiting function
			var isValid = 1;// for default have success value 0

			// validate existing rows in table
			$("#tblestimate tbody tr").find('input, select').each(
					function() {
						if (($(this).data('optional') === 0)
								&& (!$(this).val())) {
							$(this).focus();
							alert($(this).data('errormsg'));
							isValid = 0;// set validation failure
							return false;
						}
			});

			if (isValid === 0) {
				return false;
			}
			
			// Generate all textboxes Id and name with new index
			$("#estimateRow").clone().find("input, span, errors, textarea").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
						if ($(this).is('span')) {
							if($(this).hasClass('spansno'))
							$(this).html((nextIdx+1));
						}
						else{
							$(this).attr(
									{
										/*'id' : function(_, id) {
											return id.replace('[0]', '['
													+ nextIdx + ']');
										},*/
										'name' : function(_, name) {
											return name.replace('[0]', '['
													+ nextIdx + ']');
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
							
							$(this).attr('readonly', false);
							$(this).removeAttr('disabled');
							$(this).prop('checked', false);
						}

					}).end().appendTo("#tblestimate tbody");
		}
	} else {
		  alert('limit reached!');
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

function deleteLineEstimate(obj) {
    var rIndex = getRow(obj).rowIndex;
    
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
    if(!$("#removedLineEstimateDetailsIds").val()==""){
		$("#removedLineEstimateDetailsIds").val($("#removedLineEstimateDetailsIds").val()+",");
	}
    $("#removedLineEstimateDetailsIds").val($("#removedLineEstimateDetailsIds").val()+$('[name="lineEstimateDetails[' + aIndex + '].id"]').val());

	var tbl=document.getElementById('tblestimate');	
	var rowcount=$("#tblestimate tbody tr").length;

    if(rowcount<=1) {
		alert("This row can not be deleted");
		return false;
	} else {
		//tbl.deleteRow(rIndex);
		//starting index for table fields
		var tempVal = 0;
		$('#tblestimate tbody tr:nth-child('+rIndex+')').hide();
		tempVal = $("#tblestimate tbody tr:nth-child("+rIndex+") input[name$='estimateAmount']").val();
		if($deletedAmt == 0)
			$deletedAmt = parseFloat(tempVal);
		else
			$deletedAmt = parseFloat(tempVal) + parseFloat($deletedAmt);
		var idx=0;
		
		//regenerate index existing inputs in table row
		$("#tblestimate tbody tr").each(function() {
			$(this).find("input, span, errors, textarea").each(function() {
				if ($(this).is('span')) {
					if($(this).hasClass('spansno'))
					$(this).html((idx+1));
				}
				else{
				   $(this).attr({
				      /*'id': function(_, id) {  
				    	  return id.replace(/\[.\]/g, '['+ idx +']'); 
				       },*/
				      'name': function(_, name) {
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
		calculateEstimatedAmountTotal();
		return true;
	}	
}

function calculateEstimatedAmountTotal(){
	validateEstimateAmount();
	var estimateTotal=0;
	$( "input[name$='estimateAmount']" ).each(function(){
		estimateTotal = estimateTotal + parseFloat(($(this).val()?$(this).val():"0"));
	});
	if($deletedAmt != null || $deletedAmt != '') {
		estimateTotal -= parseFloat($deletedAmt);
	}
	$('#estimateTotal').html(estimateTotal);
}

function getLineEstimateDate() {
	var dt = new Date(); 
	var dd = dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate(); 
	var mm = dt.getMonth() + 1;
	var mm = mm < 10 ? "0" + mm : mm;
	var yy = 1900 + dt.getYear();
	$('[name="lineEstimateDate"]').val(dd+"/"+mm+"/"+yy);
}

function validateEstimateAmount() {
	$( "input[name$='estimateAmount']" ).on("keyup", function(){
	    var valid = /^[1-9](\d{0,9})(\.\d{0,2})?$/.test(this.value),
	        val = this.value;
	    
	    if(!valid){
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}

