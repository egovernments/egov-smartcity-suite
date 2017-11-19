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

$subTypeOfWorkId = 0;
$(document).ready(function(){
	$('#typeofwork').trigger('blur');
	$('#subTypeOfWork').trigger('blur');
});

jQuery('#btnsearch').click(function(e) {
	if($('#searchRequestLetterOfAcceptance').valid())
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

//LOA created estimate numbers
$(document).ready(function() {
	var estimateNumber = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/egworks/letterofacceptance/ajaxestimatenumbers?name=%QUERY',
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

//Showing WorkOrderNumber Where LOA is Approved 
$(document).ready(function() {
	var workOrderNumber = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/egworks/letterofacceptance/ajaxloanumber-milestone?workOrderNumber=%QUERY',
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
					var selected="";
					if($subTypeOfWorkId)
					{
						if($subTypeOfWorkId==val.id)
						{
							selected="selected";
						}
					}
				     $('#subTypeOfWork').append($('<option '+ selected +'>').text(val.description).attr('value', val.id));
				});
			});
		}
	});


var workIdentificationNumber = new Bloodhound({
    datumTokenizer: function (datum) {
        return Bloodhound.tokenizers.whitespace(datum.value);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/egworks/letterofacceptance/ajaxworkidentificationnumber-milestone?code=%QUERY',
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

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/letterofacceptance/ajaxsearch-loaformilestone",
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
					if(data.workOrderAmount != "")
						$('td:eq(10)',row).html(parseFloat(Math.round(data.workOrderAmount * 100) / 100).toFixed(2));
					if (data.estimateNumber != null)
						$('td:eq(0)',row).html('<input type="radio" data='+ data.estimateNumber +' name="selectCheckbox" value="'+ data.id +'"/>');
					$('td:eq(1)', row).html(index + 1);
					$('td:eq(8)', row).html(
							'<a href="javascript:void(0);" onclick="openLetterOfAcceptance(\''
									+ data.id + '\')">'
									+ data.workOrderNumber + '</a>');
					return row;
				},
				aaSorting : [],
				columns : [ {
						"data" : "", "sClass" : "text-center","sWidth": "1%"} ,{ 
						"data" : "","autoWidth": "false",},{
						"data" : "estimateNumber","sClass" : "text-left"}, {
						"data" : "typeOfWork","sClass" : "text-left"}, {
						"data" : "subTypeOfWork","sClass" : "text-left"}, {
						"data" : "estimateDate","sClass" : "text-left",
						render: function (data, type, full) {
							if(full!=null &&  full.estimateDate != undefined) {
								var regDateSplit = full.estimateDate.split(" ")[0].split("-");		
								return regDateSplit[2] + "/" + regDateSplit[1] + "/" + regDateSplit[0];
							}
							else return "";
				    	}}, {
						"data" : "nameOfTheWork","sClass" : "text-left"}, {
						"data" : "workIdentificationNumber","sClass" : "text-left"	}, {
						"data" : "workOrderNumber","sClass" : "text-left"}, {
						"data" : "workOrderDate","sClass" : "text-center",
						render: function (data, type, full) {
							if(full!=null &&  full.workOrderDate != undefined) {
								var regDateSplit = full.workOrderDate.split(" ")[0].split("-");		
								return regDateSplit[2] + "/" + regDateSplit[1] + "/" + regDateSplit[0];
							}
							else return "";
				    	}}, {
						"data" : "workOrderAmount","sClass" : "text-right"
				} ]
			});
}

function openLetterOfAcceptance(id) {
	window.open("/egworks/letterofacceptance/view/" + id, '','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

jQuery('#createMilestone').click(function(e) {
	var estimateNumber = $('input[name=selectCheckbox]:checked').attr('data');
	if(estimateNumber == null) {
		var message = document.getElementById('selectLOA').value;
		bootbox.alert(message);
	}else {
			window.location = "/egworks/milestone/newform?estimateNumber="+estimateNumber;
		}
});