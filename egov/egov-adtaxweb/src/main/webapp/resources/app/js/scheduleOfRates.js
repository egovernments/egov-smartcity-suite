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
var tableContainer;
jQuery(document).ready(function ($) {
	if ($('#mode').val() == 'dataFound') {
		jQuery('#schedleOfrateDiv').removeClass('hidden');
	}
	$("#addnewscheduleofrate").click(function() {
		$("#noscheduleofrateDataFoundDiv").hide();
		$("#schedleOfrateDiv").show();
		jQuery('#schedleOfrateDiv').removeClass('hidden');
	});
	
	$('#category').change(function(){
		$.ajax({
			url: "/adtax/ajax-subCategories",    
			type: "GET",
			data: {
				category : $('#category').val()   
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#subCategory').empty();
				$('#subCategory').append($("<option value=''>Select from below</option>"));
				$.each(response, function(index, value) {
					$('#subCategory').append($('<option>').text(value.description).attr('value', value.id));
				});
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
	$(".btn-add")
	.click(
			function() {
				var currentIndex = $("#schedleOfrateTable tr").length;
				    	addNewRowToTable(currentIndex);
			});

	
	$('#schedleOfrateBtn').click(function() {
			$('#scheduleOfRateformResult').attr('method', 'post');
	 	$('#scheduleOfRateformResult').attr('action', '/adtax/rates/create');
	 //	$('#viewEscalation').submit(); 
	});
	$('#scheduleOfRateSearch').click(function() {
		$('#scheduleOfRateform').attr('method', 'post');
 	$('#scheduleOfRateform').attr('action', '/adtax/rates/search');

	});
	
	$('#scheduleOfRateSearchAgain').click(function() {
		$('#scheduleOfRateform').attr('method', 'GET');
 	$('#scheduleOfRateform').attr('action', '/adtax/rates/search');

	});

});

function addNewRowToTable(currentIndex)
{
	$("#schedleOfrateTable tbody")
	.append(
			'<tr> <td> <input id="positionHierarchyFromPositionId'+(currentIndex - 1)+'" name="advertisementRatesDetails['+(currentIndex - 1)+'].id"  type="hidden"> <input class="form-control is_valid_number" id="advertisementRatesDetailsUnitFrom'+(currentIndex - 1)+'"  autocomplete="off"  name="advertisementRatesDetails['+(currentIndex - 1)+'].unitFrom"  required="required" type="text"></td><td><input class="form-control is_valid_number" id="advertisementRatesDetailsUnitTo'+(currentIndex - 1)+'"  autocomplete="off"  name="advertisementRatesDetails['+(currentIndex - 1)+'].unitTo"  required="required" type="text"></td> <td> <input class="form-control is_valid_number" id="advertisementRatesDetailsAmount'+(currentIndex - 1)+'"  autocomplete="off"  name="advertisementRatesDetails['+(currentIndex - 1)+'].amount"  required="required" type="text"></td>  <td> <button type="button" onclick="deleteRow(this)" id="Add" class="btn btn-success">Delete </button> </td></tr>');
					
		
}