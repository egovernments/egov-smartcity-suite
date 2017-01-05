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

$(document).ready(function() {
	
	$('#btnsearch').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
	
});


$(document).ready(function() {
	var contractorSearch = new Bloodhound(
			{
				datumTokenizer : function(datum) {
					return Bloodhound.tokenizers
							.whitespace(datum.value);
				},
				queryTokenizer : Bloodhound.tokenizers.whitespace,
				remote : {
					url : '/egworks/reports/ajax-searchcontractors?code=%QUERY',
					filter : function(data) {
						return $.map(data, function(ct) {
							return {
								name : ct.name,
								code : ct.code
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
				source : contractorSearch.ttAdapter(),
				templates: {
			        suggestion: function (item) {
			        	return item.code+' ~ '+item.name;
			        }
				}
			});
});

$(document).ready(function(){
    var ward = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/egworks/lineestimate/ajax-getward?name=%QUERY',
            filter: function (data) {
                return $.map(data, function (ct) {
                    return {
                    	name: ct.boundaryType.name.toUpperCase() == 'CITY' ? ct.name : ct.boundaryNum + '' ,
                        value: ct.id
                    };
                });
            }
        }
    });
    
    ward.initialize();
	var ward_typeahead = $('#wardInput').typeahead({
		hint : false,
		highlight : false,
		minLength : 1
	}, {
		displayKey : 'name',
		source : ward.ttAdapter(),
	});
	
	typeaheadWithEventsHandling(ward_typeahead,
	'#electionWardId');
});

jQuery('#btnsearch').click(function(e) {
	$('#btndownloadpdf').hide();
	$('#btndownloadexcel').hide();
	var flag = true; 
	if(flag && $('form').valid())
		callAjaxSearch();
	
	var queryParameters = "Contractor Wise Abstract Report For ";
	queryParameters += "Date Range : " + $('#fromDate').val() + " - " + $('#toDate').val() + ",";
	var financialyearid = $('#financialYear').val();
	var natureofwork = $('#natureOfWork').val();
	var workStatus = $('#workStatus').val();
	var contractor = $('#contractorSearch').val();
	var ward = $('#wardInput').val();
	
	if(natureofwork != "")
		queryParameters += "Nature Of Work : "+ $('#natureOfWork').find(":selected").text() + ","
	
    if(workStatus != "")
    	queryParameters += "Work Status : "+workStatus + ","
    
    if(contractor != "")	
    	queryParameters += "Contractor : "+contractor + ","
    
    if(ward != "")
    	queryParameters += "Election Ward : "+ward + ","
    	
	if (queryParameters.endsWith(","))
        queryParameters = queryParameters.substring(0, queryParameters.length - 1);
    
    $('#searchCriteria').html(queryParameters);
    
    $('#resultTable').stickyTableHeaders({
			fixedOffset: $('nav').outerHeight()
		});
    	
});

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
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
				url : "/egworks/reports/ajax-contractorwiseabstractreport",      
				type: "POST",
				"data":  getFormData(jQuery('form'))
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
				$('#btndownloadpdf').show();
				$('#btndownloadexcel').show();
				$('td:eq(0)',row).html(index+1);
				if(index == 0) {
					$createdDate = data.createdDate;
					var dataRunmTime = "The information in this report is not real time, it provides information of the transactions that happened till " + $createdDate;
					$('#dataRun').html(dataRunmTime);
				}
			},
			aaSorting: [],				
			columns : [ { 
				"data" : ""} ,
				{"data" : "ward"}, 
				{"data" : "contractorName"},
				{"data" : "approvedEstimates","sClass" : "text-right"},{"data" : "approvedAmount" ,"sClass" : "text-right"},
				{"data" : "siteNotHandedOverEstimates","sClass" : "text-right"},{"data" : "siteNotHandedOverAmount" , "sClass" : "text-right"},
				{"data" : "notWorkCommencedEstimates" , "sClass" : "text-right"},{"data" : "notWorkCommencedAmount" , "sClass" : "text-right"},
				{"data" : "workCommencedEstimates" , "sClass" : "text-right"},{"data" : "workCommencedAmount" , "sClass" : "text-right"},
				{"data" : "workCompletedEstimates" , "sClass" : "text-right"},{"data" : "workCompletedAmount" , "sClass" : "text-right"},
				{"data" : "balanceWorkEstimates" , "sClass" : "text-right"},{"data" : "balanceWorkAmount" , "sClass" : "text-right"},
				{"data" : "liableAmount" , "sClass" : "text-right"}]				
		});
	}

$('#btndownloadpdf').click(function() {
	var financialyearid = $('#financialYear').val();
	var natureofwork = $('#natureOfWork').val();
	var workStatus = $('#workStatus').val();
	var contractor = $('#contractorSearch').val();
	var ward = $('#electionWardId').val();
	var contentType = 'pdf';
	window.open("/egworks/reports/contractorwiseabstract/pdf?financialYearId="
			+ financialyearid
			+ "&natureOfWork="
			+ natureofwork
			+ "&workStatus="+workStatus + "&ward="+ward+"&contractor="+contractor
			+ "&contentType=pdf", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');

});

$('#btndownloadexcel').click(function() {
	var financialyearid = $('#financialYear').val();
	var natureofwork = $('#natureOfWork').val();
	var workStatus = $('#workStatus').val();
	var contractor = $('#contractorSearch').val();
	var ward = $('#electionWardId').val();
	window.open("/egworks/reports/contractorwiseabstract/pdf?financialYearId="
			+ financialyearid
			+ "&natureOfWork="
			+ natureofwork
			+ "&workStatus="+workStatus + "&ward="+ward+"&contractor="+contractor
			+ "&contentType=excel", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');

});

function getFinancialYearDatesByFYId(fyId) {
	if ($('#financialYear').val() === '') {
		   $('#adminSanctionFromDate').val('');
		   $('#adminSanctionToDate').val('');
			return;
			} else {
				$.ajax({
					url : '/egworks/lineestimate/getfinancilyearbyid',
					type : "get",
					data : {
						fyId : fyId
					},
					success : function(data, textStatus, jqXHR) {
						var currentFYId = $("#currentFinancialYearId").val();
						var selectedFYId = $("#financialYear").val();
						var sdate = new Date(data.startingDate);
						var sd = sdate.getDate();
				        var sm = sdate.getMonth();
				        var sy = sdate.getFullYear();
				        var startingDate = new Date(sy,sm, sd );
				        $("#fromDate").val(startingDate);
				        $("#fromDate").datepicker('setDate', startingDate);
				        if(currentFYId!= null && currentFYId!="" && selectedFYId == currentFYId ){
				        	var edate = new Date();
							var ed = edate.getDate();
					        var em = edate.getMonth();
					        var ey = edate.getFullYear();
					        var endingDate = new Date(ey,em, ed );
					        $("#toDate").val(endingDate);
				        }else{
					        var edate = new Date(data.endingDate);
							var ed = edate.getDate();
					        var em = edate.getMonth();
					        var ey = edate.getFullYear();
					        var endingDate = new Date(ey,em, ed );
					        $("#toDate").val(endingDate);
					        $("#toDate").datepicker('setDate', endingDate);
				        }
					},
					error : function(jqXHR, textStatus, errorThrown) {
						bootbox.alert("Error while get financilyear by id ");
					}
				});
			}
}