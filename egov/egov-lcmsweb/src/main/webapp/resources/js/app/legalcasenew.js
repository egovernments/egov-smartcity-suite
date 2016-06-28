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
$(document).ready(function(){
	
	$(".show-ManualLcNumber").hide(); 
	$("#bipartisanDetails[0].governmentDepartment").prop("disabled", true);
	var department = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/lcms/legalcase/ajax/departments?departmentName=%QUERY',
			filter: function (data) {
				return $.map(data, function (department) {
					return {
						name: department.name,
						value: department.id
						
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	department.initialize();
	// Instantiate the Typeahead UI
	
	var typeaheadobj =$('#departmentName').typeahead({
		  hint: false,
		  highlight: false,
		  minLength: 1
		}, {
			displayKey: 'name',
			source: department.ttAdapter()
		});
	
		//typeaheadWithEventsHandling(typeaheadobj, '#departmentId'); 
		
		
		$("#departmentName").blur(function (){
			var desigId = $("#departmentName").val();
			if(null!=desigId || ''!=desigId){
				$('.designationerror').hide();
			}
			else
				$("#departmentId").val("");
		});
		var assignPosition = new Bloodhound({
			datumTokenizer: function (datum) {
				return Bloodhound.tokenizers.whitespace(datum.value);
			},
			queryTokenizer: Bloodhound.tokenizers.whitespace,
			remote: {
				url: '/lcms/legalcase/ajax/positions',
				replace: function(url, uriEncodedQuery) {
					return url + '?positionName='+uriEncodedQuery+'&departmentName='+ $("#departmentName").val();
					
				},
				filter: function (data) {
					return $.map(data, function (position) {
						return {
							name: position.name,
							value: position.id
							
						};
					});
				}
			}
		});
		
		// Initialize the Bloodhound suggestion engine
		assignPosition.initialize();
		// Instantiate the Typeahead UI
		
		var typeaheadobj =$('#positionName').typeahead({
			  hint: false,
			  highlight: false,
			  minLength: 1
			}, {
				displayKey: 'name',
				source: assignPosition.ttAdapter()
			});
		
	
});
function enableGovtDept()
{
	var govtcheck=$('#activeid').val() ;
	if(govtcheck==true)
		{
		$("#bipartisanDetails[0].governmentDepartment").prop("disabled", false);

		}
	
}

function checkLCType()
{
	 if($('#lcNumberType').val() == "MANUAL")
{
		 $(".show-ManualLcNumber").show(); 

}
else
{
	$(".show-ManualLcNumber").hide(); 
}
document.getElementById("lcNumber").value="";	
document.getElementById("wpYear").value="--select---";
 
}


