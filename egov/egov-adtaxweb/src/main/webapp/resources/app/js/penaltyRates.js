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

function validateValue(){

	var isSuccessValidation=true;
	
	$('#penaltyRatesTable input[type="text"]:not([readonly])').each(function(){

		if(isNaN($(this).val()))
		{
		  isSuccessValidation=false;
		  $(this).focus();
		  return false;
		}
	});
	
	if(!isSuccessValidation)
	{
		bootbox.alert("The value entered is invalid. Enter a valid number ");
	}
	return isSuccessValidation;
}

function validateRangeToValue(obj)
{
	var rangeTo=parseFloat(obj.value);
	var rangeFrom=parseFloat($(obj).closest('td').prev('td').find('input.range-from').val());
	
	if(rangeTo && rangeFrom >= rangeTo)
	{
		bootbox.alert('Range To value should be greater than range from value ->'+ rangeFrom );
		$(obj).val("");
		return false;
	}
	else{
		$(obj).closest('tr').next().find('input.range-from').val($(obj).val());
		 if (parseFloat($(obj).val())>=parseFloat($(obj).closest('tr').next().find('input.range-to').val()))
		    {
		    	$(obj).closest('tr').next().find('input.range-to').val("").focus();
		    }
		var nextindex = $(obj).closest('tr').index() + 1;
		$('#penaltyRatesTable tbody').find('tr:gt('+nextindex+')').remove();
		$("#penaltyRatesTable tr:last .delete-button").show();
		return true;
	}
	return true;
}

$('.penaltyRatesSaveButton').click(function(){
	$('#penaltyRatesChangeForm').attr('method','post');
	$('#penaltyRatesChangeForm').attr('action','/adtax/penalty/create');
});


$(".btn-addRow").click(function(){
	
	var currentIndex = $("#penaltyRatesTable tr").length;
	addNewRowToTable(currentIndex);
});

function getRow(obj) {
	if (!obj) {
		return null;
	}
	tag = obj.nodeName.toUpperCase();
	while (tag != "BODY") {
		if (tag == "TR") {
			return obj;
		}
		obj = obj.parentNode;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}
function deleteRow(obj){

	var tbl=document.getElementById("penaltyRatesTable");
	var lastRow=(tbl.rows.length)-1;
	var j;
	var curRow=getRow(obj).rowIndex;
		if(curRow==1||lastRow==1){
			bootbox.alert("You can not delete this row");
			return false;
			}
		else{
			for(j=curRow; j<lastRow; j++){

				$("#advertisementPenaltyRatesId"+j).attr('name',"advertisementPenaltyRates["+(j-1)+"].id");
				$("#advertisementPenaltyRatesId"+j).attr('id',"advertisementPenaltyRatesId"+(j-1));

				$("#advertisementPenaltyRatesRangeFrom"+j).attr('name',"advertisementPenaltyRates["+(j-1)+"].rangeFrom");
				$("#advertisementPenaltyRatesRangeFrom"+j).attr('id', "advertisementPenaltyRatesRangeFrom"+(j-1));

				$("#advertisementPenaltyRatesRangeTo"+j).attr('name',"advertisementPenaltyRates["+(j-1)+"].rangeTo");
				$("#advertisementPenaltyRatesRangeTo"+j).attr('id',"advertisementPenaltyRatesRangeTo"+(j-1));

				$("advertisementPenaltyRatesPercentage"+j).attr('name',"advertisementPenaltyRates["+(j-1)+"].percentage");
				$("advertisementPenaltyRatesPercentage"+j).attr('id',"advertisementPenaltyRatesPercentage"+(j-1));

				}
			tbl.deleteRow(curRow);
			$("#penaltyRatesTable tr:last .delete-button").show();
			$("#penaltyRatesTable tr:last .range-to").prop("readOnly", false);
			return true;
			}
	}


function addNewRowToTable(currentIndex)
{
	var tr = $('#penaltyRatesTable tr:last'); 
		  var firstcolumnval =    $(tr).find("td").find('input.range-from').val();
		   var secondcolumnval = $(tr).find("td").find('input.range-to').val();
		  var Thirdcolumnval = $(tr).find("td").find('input.percentage').val();
	 
	  if (firstcolumnval=="" || firstcolumnval==null || firstcolumnval=="undefined")
	    {
		  	bootbox.alert('Range From is mandatory in the last row');
	    }else  if (secondcolumnval=="" || secondcolumnval==null || secondcolumnval=="undefined")
	    {
	    	bootbox.alert('Range To is mandatory in the last row');
	    }else  if (Thirdcolumnval=="" || Thirdcolumnval==null || Thirdcolumnval=="undefined")
	    {
	    	bootbox.alert('Percentage is mandatory in the last row');
	    }else  if (parseFloat(firstcolumnval)>=parseFloat(secondcolumnval))
	    {
	    	bootbox.alert('Range To value should be greater than Range from value in the last row');
	    	$(tr).find("td").find('input.range-to').val("");
	    }else
	    	{
				$( "#penaltyRatesTable tr:last .delete-button").hide();
				$("#penaltyRatesTable tbody")
				.append(
						'<tr> <td> <input id="advertisementPenaltyRatesId'+(currentIndex - 1)+'" name="advtPenaltyRatesList['+(currentIndex - 1)+'].id"  type="hidden"> <input class="form-control form-control patternvalidation range-from" style="text-align: center; font-size: 12px;" id="advertisementPenaltyRatesRangeFrom'+(currentIndex - 1)+'" data-pattern="numerichyphen" autocomplete="off"  name="advtPenaltyRatesList['+(currentIndex - 1)+'].rangeFrom"  readonly="readonly" required="required" type="text"></td><td><input class="form-control form-control patternvalidation range-to" data-pattern="numerichyphen" maxlength="8" style="text-align: center; font-size: 12px;" id="advertisementPenaltyRatesRangeTo'+(currentIndex - 1)+'"  autocomplete="off"  name="advtPenaltyRatesList['+(currentIndex - 1)+'].rangeTo" onblur="return validateRangeToValue(this);"  required="required" type="text"></td> <td> <input class="form-control form-control patternvalidation percentage" data-pattern="decimalvalue" maxlength="7" style="text-align: center; font-size: 12px;" id="advertisementPenaltyRatesPercentage'+(currentIndex - 1)+'"  autocomplete="off"  name="advtPenaltyRatesList['+(currentIndex - 1)+'].percentage" type="text"  required="required" ></td>  <td> <button type="button" onclick="deleteRow(this)" id="Add" class="btn btn-primary display-hide delete-button">Delete Row </button> </td></tr>');
								
				$("#penaltyRatesTable tr:last .delete-button").show();
				patternvalidation();
				
				$('#advertisementPenaltyRatesRangeFrom'+(currentIndex - 1)).val($('#advertisementPenaltyRatesRangeTo'+(currentIndex - 2)).val());
	    	}
}
		