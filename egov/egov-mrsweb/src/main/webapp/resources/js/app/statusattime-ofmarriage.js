/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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


$(document).ready(function() {

	$('#btn_registrationmrgstatus_search').click(function() {
		if ($('form').valid()) {
			$('#registrationmrgstatus_table tbody').empty();
			callAjaxSearch();
		}
		return false;
	});

});

function callAjaxSearch() {

	// To get current date
	var currentDate = new Date();
	var day = currentDate.getDate();
	var month = currentDate.getMonth() + 1;
	var year = currentDate.getFullYear();
	var currentDate = day + "-" + month + "-" + year;

	var from = $('#fromDate').val();
	var to = $('#toDate').val();

	$('.report-section').removeClass('display-hide');

	var reportdatatable = $("#marriage_table")
			.dataTable(
					{
						ajax : {
							url : "/mrs/report/status-at-time-marriage",
							type : "POST",
							beforeSend : function() {
								$('.loader-class').modal('show', {
									backdrop : 'static'
								});
							},
							"data" : getFormData($('form')),
							complete : function() {
								$('.loader-class').modal('hide');
							}
						},
						"bDestroy" : true,
						"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
						"aLengthMenu" : [ [ 10, 25, 50, -1 ],
								[ 10, 25, 50, "All" ] ],
						"oTableTools" : {
							"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
							"aButtons" : [ "xls", "pdf", "print" ]
						},
						aaSorting : [],
						columns : [
								{
									"data" : null,
									"title" : 'S.no',
									render : function(data, type, row, meta) {
										return meta.row
												+ meta.settings._iDisplayStart
												+ 1;
									},
									"sClass" : "text-center"
								},
								{
									"data" : "month",
									"title" : 'Month',
									"sClass" : "text-center"
								},
								{
									"data" : "applicantType",
									"title" : 'Applicant Type',
									"sClass" : "text-center"
								},
								{
									"data" : 'married',
									"title" : 'Married',
									render : function(data, type, row, meta) {
										return parseInt(row.married)!==0?'<a onclick="openPopup(\'/mrs/report/status-at-time-marriage/view?'
												+ 'applicantType='+row.applicantType
												+ '&'
												+ 'maritalStatus=Married'
												+ '&'
												+ 'fromDate='+from
												+ '&'
												+ 'toDate='+to
												+ '\')" href="javascript:void(0);">'
												+ row.married + '</a>':row.married;
									},
									"sClass" : "text-center"
								},
								{
									"data" : 'unmarried',
									"title" : 'Unmarried',
									render : function(data, type, row, meta) {
										return parseInt(row.unmarried)!==0?'<a onclick="openPopup(\'/mrs/report/status-at-time-marriage/view?'
												+ 'applicantType='+row.applicantType
												+ '&'
												+ 'maritalStatus=Unmarried'
												+ '&'
												+ 'fromDate='+from
												+ '&'
												+ 'toDate='+to
												+ '\')" href="javascript:void(0);">'
												+ row.unmarried + '</a>':row.unmarried;
									},
									"sClass" : "text-center"
								},
								{
									"data" : 'divorced',
									"title" : 'Divorced',
									render : function(data, type, row, meta) {
										return parseInt(row.divorced)!==0?'<a onclick="openPopup(\'/mrs/report/status-at-time-marriage/view?'
												+ 'applicantType='+row.applicantType
												+ '&'
												+ 'maritalStatus=Divorced'
												+ '&'
												+ 'fromDate='+from
												+ '&'
												+ 'toDate='+to
												+ '\')" href="javascript:void(0);">'
												+ row.divorced + '</a>':row.divorced;
									},
									"sClass" : "text-center"
								},
								{
									"data" : 'widower',
									"title" : 'Widower',
									render : function(data, type, row, meta) {
										return parseInt(row.widower)!==0?'<a onclick="openPopup(\'/mrs/report/status-at-time-marriage/view?'
												+ 'applicantType='+row.applicantType
												+ '&'
												+ 'maritalStatus=Widower'
												+ '&'
												+ 'fromDate='+from
												+ '&'
												+ 'toDate='+to
												+ '\')" href="javascript:void(0);">'
												+ row.widower + '</a>':row.widower;
									},
									"sClass" : "text-center"
								} ]
					});

}


function openPopup(url) {
	window.open(url, 'window',
			'scrollbars=1,resizable=yes,height=600,width=800,status=yes');

}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}
