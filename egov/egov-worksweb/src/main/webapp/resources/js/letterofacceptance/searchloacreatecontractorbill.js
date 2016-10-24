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

$('#btncreateloa').hide();

jQuery('#btnsearch').click(function(e) {
	
	var fromDate = '';
	var toDate = '';
	if($('#fromDate').val() != "") {
		fromDate = $('#fromDate').data('datepicker').date;
	}
	if($('#toDate').val() != "") {
		toDate = $('#toDate').data('datepicker').date;
	}
	var flag = true; 
	if(toDate != '' && fromDate != "") {
		if(fromDate > toDate) {
			flag = false;
			bootbox.alert('To Date should be greater than From Date');
		}
	}
	if(flag)
	callAjaxSearch();
});

jQuery('#btncreateloa').click(function(e) {
	var workOrderId = $('input[name=selectCheckbox]:checked').val();
	if(workOrderId == null) {
		var message = $('#errorMessage').html();
		bootbox.alert(message);
	}
	else {
	var workOrderNumber = $('input[name=selectCheckbox]:checked').attr('data').split(':')[0];
	var woeId = $('input[name=selectCheckbox]:checked').attr('data').split(':')[1];
		$.ajax({
			type : "GET",
			url : '/egworks/letterofacceptance/ajaxvalidate-createcontractorbill?workOrderId='+ workOrderId,
			cache : true,
			dataType : "json",
			success : function(message) {
				if (message != "") {
					bootbox.alert(message);
				} else {
					window.location = "/egworks/contractorbill/newform?woeId=" + woeId;
				}
			},
			error : function(response) {
			}
		});
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

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/letterofacceptance/ajaxsearch-loaforcontractorbill",
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
					if (data.estimateNumber != null)
						$('td:eq(0)',row).html('<input type="radio" data='+ data.workOrderNumber + ':' + data.woeId  +' name="selectCheckbox" value="'+ data.id +'"/>');
						$('td:eq(1)', row).html(index + 1);
						$('td:eq(2)', row).html(
								'<a href="javascript:void(0);" onclick="openAbstractEstimate(\''
										+ data.aeId + '\')">'
										+ data.estimateNumber + '</a>');
						$('td:eq(4)', row).html(
								'<a href="javascript:void(0);" onclick="openLetterOfAcceptance(\''
										+ data.id + '\')">'
										+ data.workOrderNumber + ' -- ' + data.workOrderDate + '</a>');
						$('td:eq(6)',row).html(parseFloat(Math.round(data.workOrderAmount * 100) / 100).toFixed(2));
						if(!isNaN(data.mbAmount))
                            $('td:eq(8)',row).html(parseFloat(Math.round(data.mbAmount * 100) / 100).toFixed(2));
						$('#btncreateloa').show();
					return row;
				},
				aaSorting : [],
				columns : [ {
					"data" : "","sWidth": "1%"} ,{ 
					"data" : "","autoWidth": "false","sWidth": "2%"
				},{
					"data" : "estimateNumber","width": "13.5%"
				}, {
					"data" : "nameOfWork","sWidth": "15%"
					
				},{
					"data" : "workOrderNumber","width": "13.5%"
				}, {
					"data" : "contractor","sWidth": "15%",
						 "render":function(data, type, full, meta){
						       return  full.contractor + "/" + full.contractorcode;
						    } 
				}, {
					"data" : "workOrderAmount","width": "6%",
					"sClass" : "text-right"
				} ,{
					"data" : "mbRefNumbers","width": "12%"
				} ,{
					"data" : "mbAmount","width": "12%",
						"sClass" : "text-right"
				}]
			});
}

function openLetterOfAcceptance(id) {
	window.open("/egworks/letterofacceptance/view/" + id, '','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

$(document).ready(function() {
	var estimateNumber = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/egworks/letterofacceptance/ajaxestimatenumbers-contractorbill?estimateNumber=%QUERY',
			filter : function(data) {
				return $.map(data, function(ct) {
					return {
						name : ct
					};
				});
			}
		}
	});

	estimateNumber.initialize();
	var estimateNumber_typeahead = $('#estimateNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : estimateNumber.ttAdapter()
	});
});

$(document).ready(function() {
	
	$('#btnsearch').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
	
	var contractorSearch = new Bloodhound(
			{
				datumTokenizer : function(datum) {
					return Bloodhound.tokenizers
							.whitespace(datum.value);
				},
				queryTokenizer : Bloodhound.tokenizers.whitespace,
				remote : {
					url : '/egworks/letterofacceptance/ajaxsearchcontractors-loa?contractorname=%QUERY',
					filter : function(data) {
						return $.map(data, function(ct) {
							return {
								name : ct,
							};
						});
					}
				}
			});

	contractorSearch.initialize();
	var contractorSearch_typeahead = $('#contractorSearch')
			.typeahead({
				hint : true,
				highlight : true,
				minLength : 3
			}, {
				displayKey : 'name',
				source : contractorSearch.ttAdapter()
			}).on('typeahead:selected', function(event, data) {
				$("#contractorCode").val(data.code);
				$("#contractor").val(data.value);
			});
});

$(document).ready(function() {
	var workOrderNumber = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/egworks/letterofacceptance/ajaxloanumber-contractorbill?workOrderNumber=%QUERY',
			filter : function(data) {
				return $.map(data, function(ct) {
					return {
						name : ct
					};
				});
			}
		}
	});

	workOrderNumber.initialize();
	var workOrderNumber_typeahead = $('#workOrderNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : workOrderNumber.ttAdapter()
	});
});


function openAbstractEstimate(id) {
	window.open("/egworks/abstractestimate/view/"+ id , "", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}