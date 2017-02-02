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
jQuery('#btnsearch').click(function(e) {
	callAjaxSearch();
});

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/measurementbook/cancel/ajax-search",
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"bDestroy" : true,
				'bAutoWidth': false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : []
				},
				"fnRowCallback" : function(row, data, index) {
					$('td:eq(0)',row).html('<input type="radio" data="' + data.billNumber + '" name="selectCheckbox" value="'+ data.id +'"/>');
					if (data.workOrderNumber != null)
						$('td:eq(1)', row).html(
								'<a href="javascript:void(0);" onclick="viewMB(\''
										+ data.id + '\')">'
										+ data.mbRefNo + '</a> / ' + data.mbDate);
					$('td:eq(2)',row).html(parseFloat(Math.round(data.mbAmount * 100) / 100).toFixed(2));
					if (data.estimateNumber != null)
						$('td:eq(3)', row).html(
								'<a href="javascript:void(0);" onclick="viewEstimate(\''
										+ data.estimateId + '\')">'
										+ data.estimateNumber + '</a>');
					if (data.workOrderNumber != null)
						$('td:eq(4)', row).html(
								'<a href="javascript:void(0);" onclick="viewLetterOfAcceptance(\''
										+ data.workOrderId + '\')">'
										+ data.workOrderNumber + '</a>');
					return row;
				},
				aaSorting : [],
				columns : [ {
					"data" : "","width": "2%"
				}, {
					"data" : "","autoWidth": "false"
				}, {
					"data" : "",
					"sClass" : "text-right" ,"width": "6%"
				}, {
					"data" : "","autoWidth": "false"
				}, {
					"data" : "","autoWidth": "false"
				}, {
					"data" : "contractor","autoWidth": "false"
				}, {
					"data" : "workIdNumber","autoWidth": "false"
				} ]
			});
}

function viewLetterOfAcceptance(id) {
	window.open("/egworks/letterofacceptance/view/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function viewMB(id) {
	window.open("/egworks/mb/view/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function viewEstimate(id) {
	window.open("/egworks/abstractestimate/view/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

$(document).ready(function() {
	var loaNumber = new Bloodhound({
	    datumTokenizer: function (datum) {
	        return Bloodhound.tokenizers.whitespace(datum.value);
	    },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    remote: {
	        url: '/egworks/measurementbook/ajaxloanumbers-mbtocancel?code=%QUERY',
	        filter: function (data) {
	            return $.map(data, function (ct) {
	                return {
	                    name: ct
	                };
	            });
	        }
	    }
	});

	loaNumber.initialize();
	var loaNumber_typeahead = $('#loaNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : loaNumber.ttAdapter()
	});
	
	var contractor = new Bloodhound({
	    datumTokenizer: function (datum) {
	        return Bloodhound.tokenizers.whitespace(datum.value);
	    },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    remote: {
	        url: '/egworks/measurementbook/ajaxcontractors-mbtocancel?code=%QUERY',
	        filter: function (data) {
	            return $.map(data, function (ct) {
	                return {
	                    name: ct
	                };
	            });
	        }
	    }
	});

	contractor.initialize();
	var contractor_typeahead = $('#contractorName').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : contractor.ttAdapter()
	});
	
	var workIdentificationNumber = new Bloodhound({
	    datumTokenizer: function (datum) {
	        return Bloodhound.tokenizers.whitespace(datum.value);
	    },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    remote: {
	        url: '/egworks/measurementbook/ajaxworkidentificationnumbers-mbtocancel?code=%QUERY',
	        filter: function (data) {
	            return $.map(data, function (ct) {
	                return {
	                    name: ct
	                };
	            });
	        }
	    }
	});

	workIdentificationNumber.initialize();
	var workIdentificationNumber_typeahead = $('#workIdentificationNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : workIdentificationNumber.ttAdapter()
	});
	
	$('#btncancel').click(function() {
		var mbId = $('input[name=selectCheckbox]:checked').val();
		var message = "";
		if(mbId == null) {
			bootbox.alert("Please select a MB to Cancel");
		} else {
			if($('#cancellationReason').val() == 'Others')
				$('#cancellationRemarks').attr('required', 'required');
			else
				$('#cancellationRemarks').removeAttr('required');
			
			if($("#cancelForm").valid()) {
				var billNumber = $('input[name=selectCheckbox]:checked').attr('data');
				$('#cancelForm #id').val(mbId);
				if(billNumber != '') {
					message = $('#billCreatedMessage').val();
					message = message.replace(/\{0\}/g, billNumber);
					bootbox.alert(message);
				} else {
					bootbox.confirm($('#confirm').val(), function(result) {
						if(!result) {
							bootbox.hideAll();
							return false;
						} else {
							$.ajax({
								type: "GET",
								url: "/egworks/measurementbook/ajaxvalidatelatestmb-mbtocancel/"+mbId,
								cache: true,
							}).done(function(value) {
								if(value == '') {
									$("#cancelForm").attr('action','/egworks/measurementbook/cancel');
									$("#cancelForm").submit();
								} else {
									message = $('#latestMBError').val();
									message = message.replace(/\{0\}/g, value);
									message = message.replace(/\{1\}/g, value);
									bootbox.alert(message);
								}
							});
						}
					});
				}
			}
		}
		return false;
	});
	$("#departments").val($("#defaultDepartmentId").val());
});