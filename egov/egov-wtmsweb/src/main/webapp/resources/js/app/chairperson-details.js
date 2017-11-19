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
	
	$('#resetid').click(function(){
		$('#fromDate').val('');
		$('#toDate').val('');
		$('#chairpersonname').val('');
		$('#isActive').removeAttr('checked');
		return false;
	});

	$('#saveButtonid').click(function(){
		if($("#chairPersonDetailsEditForm").valid()){
			var fromDate = $('#fromDate').val();
			var toDate = $('#toDate').val();
			var isValidDate = dateValidation(fromDate , toDate);
			if(!isValidDate){
				return false;
			}
			return true;
		}
		else{
			return false;
		}
		
	});
	
	
	$('#buttonid').click(function() {
		  if ($( "#chairPersonDetailsform" ).valid())
		  {
			var fromDate = $('#fromDate').val();
			var toDate = $('#toDate').val();
				
			var isValidDate = dateValidation(fromDate , toDate);
			if(!isValidDate){
				return false;
			}
			
			var isPersonExists = isChairPersonExistOnCurrentDate();
			if(!isPersonExists){
				return false;
			}
			
			var isDateValid = validateDates(fromDate, toDate);
			if(!isDateValid){
				return false;
			}
				return true;
			
		  }
		  else
			  {
			  	return false;
			  }
		});
});


function edit(chairPersonId){
	window.open("/wtms/application/chairperson-edit/"+chairPersonId, "_self");
}

$('#addnewid').on("click",function(){
	window.open("/wtms/application/chairperson-create", "_self");
});


function dateValidation(fromDate , toDate){
	var result = compareDate (fromDate , toDate);
	if(result == -1){
		bootbox.alert("From Date should not be greater than To Date");
		return false;
	}
	return true;
}

function validateDates(fromDate, toDate){
	var boolVar = false;
	$.ajax({
		url: '/wtms/application/ajax-activeChairPersonExistsAsOnGivenDate',
		type: "GET",
		data: {
			fromdate: fromDate,
			toDate: toDate
		},
		dataType: 'json',
		async:false,
		success: function(response){
			if(response){
				response = JSON.parse(response);
				if(response.fromDate!=null){
					var fromDateArray = response.fromDate.split("-");
					var fromDateVal = fromDateArray[2] + "/" + fromDateArray[1] + "/" + fromDateArray[0];
					var toDateArray = response.toDate.split("-");
					var toDateVal = toDateArray[2] + "/" + toDateArray[1] + "/" + toDateArray[0];
					bootbox.alert('Chairperson already exists between the range : '+fromDateVal+' and '+toDateVal);
					boolVar =false;
					return false;
				}
				else{
					boolVar = true;
					return true;
				}
			}
		},
		error: function () {
			boolVar =false;
			return false;
		}
		
	});
	return boolVar;
}

function isChairPersonExistOnCurrentDate(){
	var boolVar = false;
	var name = $('input').val();
	var enteredFromDate = $('#fromDate').val();
    if (name != ''){
     $.ajax({
         url: '/wtms/application/ajax-activeChairPersonExistsAsOnCurrentDate',
         type: "GET",
         data: {
         	name: name
         },
         async: false,
         dataType : 'json',
         success: function (response) {
 			if(response!=null && response.toDate !=null){
				var activeToDate = response.toDate;
				var actDate = activeToDate.toString();
				var date=actDate.split("-");
				var toDateVal = date[2]+"/"+date[1]+"/"+date[0];
				
				var activeFromDate = response.fromDate;
				var actFrmDate = activeFromDate.toString();
				var frmdate;
				frmdate=actFrmDate.split("-");
				var fromDateVal = frmdate[2]+"/"+frmdate[1]+"/"+frmdate[0];
				
				var result = compareDate (toDateVal , enteredFromDate);
				if(result == -1){
					bootbox.alert("From Date should be more than "+toDateVal+". Active user exists between "+fromDateVal+" and "+toDateVal);
					boolVar = false;
					return false;
				}
				else{
					boolVar = true;
	 				return true;
				}
	    	}
 			else{
 				boolVar = true;
 				return true;
 			}
 		},error: function () {
 			console.log("failed");
 		}
     });
    }
    return boolVar;
}















