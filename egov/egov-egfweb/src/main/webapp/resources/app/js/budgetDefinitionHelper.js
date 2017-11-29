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

jQuery('#btnsearch').click(function(e) {

	callAjaxSearch();
});

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/EGF/budgetdefinition/ajaxsearch/"
							+ $('#mode').val(),
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"fnRowCallback" : function(row, data, index) {
					$(row)
							.on(
									'click',
									function() {
										console.log(data.id);
										window
												.open('/EGF/budgetdefinition/'
														+ $('#mode').val()
														+ '/' + data.id, '',
														'width=800, height=600,scrollbars=yes');
									});
				},
				"bDestroy" : true,
				"autoWidth" : false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ "xls", "pdf", "print" ]
				},
				aaSorting : [],
				columns : [ {
					"data" : "name",
					"sClass" : "text-left"
				}, {
					"data" : "isbere",
					"sClass" : "text-left"
				}, {
					"data" : "financialYear",
					"sClass" : "text-left"
				}, {
					"data" : "parent",
					"sClass" : "text-left"
				}, {
					"data" : "reference",
					"sClass" : "text-left"
				}, {
					"data" : "isActiveBudget",
					"sClass" : "text-left"
				}, {
					"data" : "isPrimaryBudget",
					"sClass" : "text-left"
				} ]
			});
}

var financialYearId = $('#financialYearId').val();

function getParentByFinancialYear(financialYearId) {
	var isBereChecked = $("input[name='isbere']:checked").val();
	if ($('#financialYearId').val() === '' && financialYearId === '') {
		$('#financialYearId').empty();
		$('#financialYearId').append(
				$('<option>').text('Select from below').attr('value', ''));
		return;
	} else {
		$.ajax(
				{
					url : "/EGF/budgetdefinition/parents" + "?financialYearId="
							+ financialYearId + "&isBeRe=" + isBereChecked,
					type : "GET",
					dataType : "json"
				}).done(
				function(value) {
					console.log(value);
					$('#parent').empty();
					$('#parent').append(
							$("<option value=''>Select from below</option>"));
					value = $.parseJSON(value);
					$.each(value, function(index, val) {
						var selected = "";
						$('#parent').append(
								$('<option ' + selected + '>').text(val.name)
										.attr('value', val.id));
					});

				});
	}
}

function getReferenceBudgets(financialYearId) {
	if ($('#financialYearId').val() === '' && financialYearId === '') {
		$('#financialYearId').empty();
		$('#financialYearId').append(
				$('<option>').text('Select from below').attr('value', ''));
		return;
	} else {
		$
				.ajax(
						{
							url : "/EGF/budgetdefinition/referencebudget?financialYearId="
									+ financialYearId,
							type : "GET",
							dataType : "json"
						})
				.done(
						function(value) {
							console.log(value);
							$('#referenceBudget').empty();
							$('#referenceBudget')
									.append(
											$("<option value=''>Select from below</option>"));
							value = $.parseJSON(value);
							$
									.each(
											value,
											function(index, val) {
												var selected = "";
												$('#referenceBudget')
														.append(
																$(
																		'<option '
																				+ selected
																				+ '>')
																		.text(
																				val.name)
																		.attr(
																				'value',
																				val.id));
											});
						});
	}
}

function getDropDownsForModify(budgetId) {
	if ($('#budgetId').val() === '' && budgetId === '') {
		$('#budgetId').empty();
		$('#budgetId').append(
				$('<option>').text('Select from below').attr('value', ''));
		return;
	} else {
		$
				.ajax(
						{
							url : "/EGF/budgetdefinition/ajaxgetdropdownsformodify?budgetId="
									+ budgetId,
							type : "GET",
							dataType : "json"
						}).done(
						function(value) {
							console.log(value);
							value = $.parseJSON(value);
							$.each(value, function(index, val) {
								var selected = "";
								if (val.reference != '') {
									$('#referenceBudget').append(
											$('<option ' + selected + '>')
													.text(val.reference).attr(
															'value',
															val.referenceId));
								}

								if (val.parent != '') {
									$('#parent').append(
											$('<option ' + selected + '>')
													.text(val.parent).attr(
															'value',
															val.parentId));
								}
							});
						});
	}
}

var budgetId = $('#budgetId').val();
$(document).ready(function() {

	if (financialYearId != "") {
		$('#financialYear option').each(function() {
			if ($(this).val() == financialYearId)
				$(this).attr('selected', 'selected');
		});
	}
	if (budgetId != "" && budgetId != undefined) {
		getDropDownsForModify(budgetId);
	}

	if ($('#mode').val() == "edit") {
		$('.disablefield').attr('disabled', 'disabled');
		$("input[type=radio]").attr('disabled', true);
		$("#isPrimaryBudget").attr('disabled', true);
	}

});

function resetFunction() {
	$('#financialYear').val('');
	$('#referenceBudget').val('');
	$('#parent').val('');
}
