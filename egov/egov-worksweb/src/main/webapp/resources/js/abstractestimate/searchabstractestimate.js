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
	fromDate = "";
	toDate = "";
	if($('#fromDate').val() != "") {
		fromDate = $('#fromDate').data('datepicker').date;
	}
	if($('#toDate').val() != "") {
		toDate = $('#toDate').data('datepicker').date;
	}
	var flag = true; 
	if(toDate != '' && fromDate != '') {
		if(fromDate > toDate) {
			flag = false;
			bootbox.alert(' To Date should be greater than From Date');
		}
	}
	
	if (flag && $('form').valid()) {
		callAjaxSearch();
	} else {
		e.preventDefault();
	}
});

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
    	if (indexed_array.hasOwnProperty(n['name']))
    		indexed_array[n['name']] = indexed_array[n['name']] + ',' + n['value'];	
    	else
    		indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");		
	jQuery('.report-section').removeClass('display-hide');
	 var postData = getFormData(jQuery('form'));
		reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/abstractestimate/ajaxsearch",      
					type: "POST",
					"data":  postData
				},
				"bDestroy" : true,
				'bAutoWidth': false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [
					    {
					    	"sExtends": "pdf",
					    	"sTitle": "Search Abstract/Detailed Estimate",
                            "sPdfOrientation": "landscape"
		                },
		                {
				             "sExtends": "xls",
                             "sPdfMessage": "Search Abstract/Detailedt Estimate",
                             "sTitle": "Search Abstract/Detailed Estimate"
			             },
			             {
				             "sExtends": "print",
                             "sPdfMessage": "Search Abstract/Detailed Estimate",
                             "sTitle": "Search Abstract/Detailed Estimate"
			             }],
				},
				"fnRowCallback" : function(row, data, index) {
					if($('#lineEstimateRequired').val() == 'false') {
						$(row).on(
								'click',
								function() {
									window.open('/egworks/abstractestimate/view/'
											+ data.id, '',
											'width=800, height=600');
								});
					} else {
						$('td:eq(1)',row).html('<a href="javascript:void(0);" onclick="openAbstractEstimate(\''+ data.id +'\')">' + data.estimateNumberAndDate + '</a>');
					}
					return row;
				},
				aaSorting: [],				
				columns : [
				   {
						"data" : "lineestimateNumber",
						"render" : function(data, type, row) {
							return '<a href="javascript:void(0);" onclick="openLineEstimate('+row.leId+');" data-hiddenele="lineestimateNumber" data-eleval="'
									+ data + '">' + data + '</a>';
							}
					} ,
					{"data" : "estimateNumberAndDate"} ,{	
					"data" : "nameofwork"} ,
					{"data" : "workIdentificationNumber"} ,{ 
					"data" : "estimateAmount", "sClass" : "text-right"} ,{
					"data" : "departmentName"} ,{
					"data" : "ward","sWidth" : "10"} ,{
					"data" : "status","sWidth" : "10"} ,{
					"data" : "currentowner"} ]				
				});
		
		//show/hide department
		var department = $("#department").val();

		if(department!=""){
			var oTable = $('#resultTable').DataTable();
			oTable.column(4).visible(false);
		}
		
		if($('#lineEstimateRequired').val() == 'false') {
			var oTable = $('#resultTable').DataTable();
			oTable.column(0).visible(false);
		}
		//show/hide department
		var status = $("#status").val();

		if(status!=""){
			var oTable = $('#resultTable').DataTable();
			oTable.column(6).visible(false);
		}
}



$(document).ready(function(){
	$('#toggle-searchae').click(function () {
	    if ($(this).html() == "More..") {
	    	$(this).html('Less..');
	         $('.show-searchae-more').show();
		} else {
	        $(this).html('More..');
	        $('.show-searchae-more').hide();
		}
			
	});
	   
	var abstractEstimateNumber = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/egworks/abstractestimate/getabstractestimatesbynumber?estimateNumber=%QUERY',
            filter: function (data) {
                return $.map(data, function (ct) {
                    return {
                    	estimateNumber: ct
                    };
                });
            }
        }
    });
   
	abstractEstimateNumber.initialize();
	var abstractEstimateNumber_typeahead = $('#abstractEstimateNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'estimateNumber',
		source : abstractEstimateNumber.ttAdapter()
	});
	
	

});

function openLineEstimate(id) {
	window.open("/egworks/lineestimate/view/"+ id , "", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}

function openAbstractEstimate(id) {
	window.open("/egworks/abstractestimate/view/"+ id , "", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}