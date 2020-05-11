/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
	$('#budgetDetails').hide();
	$('#hideTable').attr('disabled', true);
	$('#accountHead').val("");

});

$('#searchBudget')
		.on(
				'click',
				function() {

					var finYear = $('#financialYear').val();
					var deptId = $('#department').val();
					var fundId = $('#fund').val();
					var functionId = $('#function').val();
					var glCode = $('#accountHead').val();
					var glCodeId = $('#accountHeadId').val();

					if (finYear == null || finYear === "") {
						bootbox.alert("Please select financial year.");
						return false;
					}
					if (deptId == null || deptId === "") {
						bootbox.alert("Please select department.");
						return false;
					}
					if (fundId == null || fundId === "") {
						bootbox.alert("Please select fund.");
						return false;
					}
					if (functionId == null || functionId === "") {
						bootbox.alert("Please select function.");
						return false;
					}
					if (glCode == null || glCode === "") {
						bootbox.alert("Please enter account head.");
						return false;
					} 	
						 	$
							.ajax({
								url : "/council/budget/getbudgetdetails?finYearId="
										+ finYear
										+ "&departmentId="
										+ deptId
										+ "&fundId="
										+ fundId
										+ "&functionId="
										+ functionId + "&glCodeId=" + glCodeId,
								type : "POST",
								success : function(response) {
									$('#budgetDetailsTable tbody').empty();
									$('#hideTable').attr('disabled', false);
									//console.log(response[0]);
									var budgetDetails = response;
									if (budgetDetails.length == 0) {
										$('#budgetDetails').hide();
										$('#hideTable').attr('disabled', true);
										bootbox.alert("No budget available.");
										return false;
									}
									$('#budgetDetails').show();

									$.each(budgetDetails,
													function(index, budget) {
														$('#budgetBalance')
																.val(budget.budgetBalance);
														$('#budgetDetailsTable tbody')
																.append(
																		$('<tr>')
																				.append(
																						'<td>'
																								+ (index + 1)
																								+ '</td>')
																				.append(
																						'<td align="right">'
																								+ budget.approvedAmount
																								+ '</td>')
																				.append(
																						'<td align="right">'
																								+ budget.billsCreatedAmount
																								+ '</td>')
																				.append(
																						'<td align="right">'
																								+ budget.budgetBalance
																								+ '</td>'));

													});
								},
								error : function(response) {
									
									if(response.status===400){
										bootbox
										.alert(response.responseJSON);
									}else{
									bootbox
											.alert("An error occured while fetching the budget details.");
									}
									//console.log(response);

								}
							});

				}); 
	 


$(function() {
	$("#accountHead").autocomplete({
		minLength : 3,
		source : function(request, response) {
			$.ajax({
				url : "/council/coa/getallaccountcodes?glCode=" + request.term,
				method : "get",
				contentType : "application/json",
				dataType : 'json',
				success : function(data) {
					response(data);
				},
				error : function(err) {
					bootbox.alert("Error while fetching the coa details");
				}
			});
		},
		select : function(event, ui) {
			$("#accountHead").val(ui.item.glcode + "-" + ui.item.name);
			$("#accountHeadId").val(ui.item.id);
			return false;
		}
	}).data("ui-autocomplete")._renderItem = function(options, item) {
		return $("<option>").append(
				$('<option>').text(item.glcode + "-" + item.name).attr('value',
						item.id)).appendTo(options);

	};
});

	$('#hideTable').on('click', function(e) {
	if (e.target.value === 'Hide') {
		$('#budgetDetails').hide();
		$('#hideTable').val('Show');
	} else {
		$('#budgetDetails').show();
		$('#hideTable').val('Hide');
	}

});
