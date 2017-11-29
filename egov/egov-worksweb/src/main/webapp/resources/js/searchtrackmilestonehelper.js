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
	$('#typeofwork').blur(function(){
		 if ($('#typeofwork').val() === '') {
			   $('#subTypeOfWork').empty();
			   $('#subTypeOfWork').append($('<option>').text('Select from below').attr('value', ''));
				return;
				} else {
				$.ajax({
					type: "GET",
					url: "/egworks/lineestimate/getsubtypeofwork",
					cache: true,
					dataType: "json",
					data:{'id' : $('#typeofwork').val()}
				}).done(function(value) {
					console.log(value);
					$('#subTypeOfWork').empty();
					$('#subTypeOfWork').append($("<option value=''>Select from below</option>"));
					$.each(value, function(index, val) {
					     $('#subTypeOfWork').append($('<option>').text(val.description).attr('value', val.id));
					});
				});
			}
		});
	
	var workIdNumber = new Bloodhound({
	    datumTokenizer: function (datum) {
	        return Bloodhound.tokenizers.whitespace(datum.value);
	    },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    remote: {
	        url: '/egworks/milestone/ajaxworkidentificationnumbers-trackmilestone?code=%QUERY',
	        filter: function (data) {
	            return $.map(data, function (ct) {
	                return {
	                    name: ct
	                };
	            });
	        }
	    }
	});

	workIdNumber.initialize();
	var workIdNumber_typeahead = $('#workIdentificationNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : workIdNumber.ttAdapter()
	});
});

jQuery('#btnsearch').click(function(e) {
	var trackMilestoneFromDate = '';
	var trackMilestoneToDate = '';
	if ($('#trackMilestoneFromDate').val() != "") {
		trackMilestoneFromDate = $('#trackMilestoneFromDate').data('datepicker').date;
	}
	if ($('#trackMilestoneToDate').val() != "") {
		trackMilestoneToDate = $('#trackMilestoneToDate').data('datepicker').date;
	}
	var flag = true;
	if (trackMilestoneToDate != '' && trackMilestoneFromDate != '') {
		if (trackMilestoneFromDate > trackMilestoneToDate) {
			flag = false;
			var message = document.getElementById('validateDate').value;
			bootbox.alert(message);
		}
	}
	if(flag)
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
					url : "/egworks/milestone/ajaxtrackmilestone-search",
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"fnRowCallback" : function(row, data, index) {
					$('td:eq(0)',row).html(index+1);
					$('td:eq(1)', row).html(
							'<a href="javascript:void(0);" onclick="openLineEstimate(\''
									+ data.lineEstimateId + '\')">'
									+ data.workIdentificationNumber + '</a>');
					$('td:eq(4)', row).html(
							'<a href="javascript:void(0);" onclick="openLOA(\''
									+ data.workOrderId + '\')">'
									+ data.workOrderNumber + '</a>');
					$('td:eq(5)',row).html(parseFloat(Math.round(data.agreementAmount * 100) / 100).toFixed(2));
					$('td:eq(9)',row).html(parseFloat(Math.round(data.total * 100) / 100).toFixed(2));
					$('td:eq(10)', row).html(
							'<a href="javascript:void(0);" onclick="openTrackMilestone(\''
									+ data.id + '\')">View Tracked Milestone</a>');
				},
				"bPaginate": false,
				"bDestroy" : true,
				'bAutoWidth': false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ ]
				},
				aaSorting : [],
				columns : [ {
					"data" : "",
					"sClass" : "text-center",
					"sWidth" : "10%"
				}, {
					"data" : "",
					"sClass" : "text-center",
					"sWidth" : "10%"
				}, {
					"data" : "nameOfWork",
					"sClass" : "text-center",
					"sWidth" : "10%"
				}, {
					"data" : "department",
					"sClass" : "text-center",
					"sWidth" : "10%"
				}, {
					"data" : "",
					"sClass" : "text-center",
					"sWidth" : "15%"
				}, {
					"data" : "",
					"sClass" : "text-right",
					"sWidth" : "10%"
				}, {
					"data" : "typeOfWork",
					"sClass" : "text-right",
					"sWidth" : "10%"
				}, {
					"data" : "subTypeOfWork",
					"sClass" : "text-center",
					"sWidth" : "10%"
				}, {
					"data" : "status",
					"sClass" : "text-center",
					"sWidth" : "10%"
				}, {
					"data" : "total",
					"sClass" : "text-right",
					"sWidth" : "10%"
				},{
					"data" : "",
					"sClass" : "text-center",
					"sWidth" : "10%"
				} ]
			});
}

function openLineEstimate(lineEstimateId) {
	window.open("/egworks/lineestimate/view/" + lineEstimateId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function openLOA(workOrderId) {
	window.open("/egworks/letterofacceptance/view/" + workOrderId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function openTrackMilestone(trackMilestoneId) {
	window.open("/egworks/milestone/viewtrackmilestone/" + trackMilestoneId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
