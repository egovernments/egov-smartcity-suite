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
$(document).ready(function() {
	
$("#search")
			.click(
					function(event) {
						$('#resultdiv').empty();
						if ($('#penaltysearchform').valid()) {
							var param = "licenseAppType=";
							param = param
									+ $('#licenseAppType').val();
							$.ajax({
									url : "/tl/penaltyRates/search?"
											+ param,
									type : "GET",
									// dataType: "json",
									success : function(response) {
										$('#resultdiv').html(
												response);
										$("#penalty").hide();
										if (jQuery('#result tbody tr').length == 1) {
											jQuery(
													'input[name="penaltyRatesList[0].fromRange"]')
													.attr(
															"readonly",
															false);
										}
									},
									error : function(response) {
										bootbox.alert("failed");
									}
								});
						}else{
							event.preventDefault();
						}
					});
	
	$("#add-row").click(
			function(event) {
				var rowCount = $('#result tbody tr').length;
				var valid = true;
				//validate all rows before adding new row
				$('#result tbody tr').each(function(index,value){
					$('#result tbody tr:eq('+index+') td input[type="text"]').each(function(i,v){
						if(!$.trim($(v).val())){
							valid = false;
							bootbox.alert("Enter all values for existing rows!",function(){
								$(v).focus();
							});
							return false;
						}
					});
				});
				if(valid){
					//Create new row
					var newRow = $('#result tbody tr:first').clone();
					newRow.find("input").each(function(){
		    	        $(this).attr({
		    	        	'name': function(_, name) { return name.replace(/\[.\]/g, '['+ rowCount +']'); }
		    	        });
		    	    });
					$('#result tbody').append(newRow);
					var prev_tovalue = $('#result tbody tr:eq('+(rowCount-1)+')').find('input.tovalue').val();
					$('#result tbody tr:last').find('input').val('');
					$('#result tbody tr:last').find('input.fromvalue').val(prev_tovalue);
					patternvalidation();
				}
			});

	
	$('#result tbody').on('click','tr td .delete-row',function(e){
		var id = $(this).closest('tr').find('td:eq(0) .penaltyId').val();
		//console.log(id)
		var idx = $(this).closest('tr').index();
		if(idx == 0){
			bootbox.alert('Cannot delete first row!');
		}else if((idx < ($('#result tbody tr').length - 1))){
			bootbox.alert('Try to delete from last row!');
		}else{
			bootbox.confirm("This will delete the row permanently. Press OK to Continue. ",function(result) {
				if(result){
					if(!id){
						$('#result tbody tr:last').remove();
					}else{
						$.ajax({
							url : '../penaltyRates/deleterow?penaltyRateId='+id,
							type : "GET",
							success : function(response) {
								$('#result tbody tr:last').remove();
							},
							error : function(response) {
								bootbox.alert("Unable to delete this row.");
						   }
						});
					}
				}
			});
		}
	});
});
