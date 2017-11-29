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

jQuery('#searchApproval').click(function(e) {
	var financialYearId = $('#financialYear').val();
	if (financialYearId) {
		search();
	}
});

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

jQuery('#approve,#reject').click(
		function(e) {
			var chkArray = [];
			var button = $(this).attr("id");
			$("#checkBoxList:checked").each(function() {
				chkArray.push($(this).val());
			});
			var checked = isChecked(chkArray);
			if (checked != false) {
				var selected;
				selected = chkArray.join(',');
				var comments = $('#comments').val();
				$.ajax({
					type : "POST",
					url : "/EGF/budgetapproval/"+button+ "?checkedArray="
							+ selected.toString() + "&comments=" + comments,
					success : function(response) {
						console.log("success" + response);
						window.location.href = "/EGF/budgetapproval/success"
								+ "?message=" + response
					},
					error : function(response) {
						console.log("failed");
					}
				});
			}
		});

function search() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/EGF/budgetapproval/search",
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"bDestroy" : true,
				"autoWidth" : false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"fnRowCallback" : function(row, data, index) {
					$('td:eq(0)', row).html(
							'<input type="checkbox" name="selectCheckbox" id="checkBoxList"value="'
									+ data.id + '"/>');
					if(index == 0) {
						var approvedBudgetCount = "Number of budgets already approved - " + data.approvedBudget;
						var verifiedBudgetCount = "Number of budgets in progress/verified - " + data.verifiedBudget;
				        var notInitiatedCount = "Number of budgets not yet initiated - " + data.notInitiated;
						$('#approvedBudget').html(approvedBudgetCount);
						$('#verifiedBudget').html(verifiedBudgetCount);
						$('#notInitiated').html(notInitiatedCount);
					}
					return row;
				},
				aaSorting : [],
				columns : [ {
					"data" : ""
				}, {
					"data" : "department",
					"sClass" : "text-left"
				}, {
					"data" : "referenceBudget",
					"sClass" : "text-left"
				}, {
					"data" : "parent",
					"sClass" : "text-left"
				}, {
					"data" : "reAmount",
					"sClass" : "text-left"
				}, {
					"data" : "beAmount",
					"sClass" : "text-left"
				},
				{
					"data" : "count",
					"sClass" : "text-left"
				}]
			});
}

function isChecked(chkArray) {
	var checked = $(chkArray).length;
	if (!checked) {
		alert("Please check at least one checkbox");
		return false;
	}
}