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
jQuery(document).ready(function($) {
	$('#genericSubReportResult-header').hide();
	$('#reportgeneration-header').hide();
	
	loadsubreportstatus();
	$('#caseStatus').change(function(){
		loadsubreportstatus();
	});

	jQuery('#searchid').click(function(e) {
		var aggregated = $('#aggregatedBy').val();
		if(validategenericsubreport())
			if(aggregated !='') {
				submitForm();
			
		}else
			submitSubReportStatusForm();
			
			
	});
});

var oTable = $('#genericSubReport-table');
var oDataTable;

function validategenericsubreport()
{
	var isFilled=false;
	$('input[type=text], select').each(function(){
	    if($(this).val())
	    {
	    	console.log('value is ->'+$(this).val());
	    	isFilled=true;
	    }
	});
	
	if(!isFilled)
	{
		
		bootbox.alert('Please select Aggregated By or Reports Criteria');
		return false;
	}
	
	return true;

}
function submitForm() {
	if ($('form').valid()) {

		var today = getdate();
		$('#genericSubReport-header').show();
		$('#reportgeneration-header').show();
		oDataTable=oTable.DataTable({
			dom : "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-3 col-xs-6 text-right'B><'col-md-4 col-xs-6 text-right'p>>",
			"autoWidth": false,
			"bDestroy": true,
			"processing": true,
			buttons: [
						{
						    extend: 'excel',
						    filename: 'LegalCase Generic SubReport',
						    exportOptions: {
						        columns: ':visible'
						    }
						},
					  {
					    extend: 'pdf',
					    message: "Report generated on "+today+"",
					    title: 'LegalCase Generic SubReport',
					    filename: 'Generic SubReport',
					    exportOptions: {
					        columns: ':visible'
					    }
					},
					{
					    extend: 'print',
					    title: 'LegalCase Generic SubReport',
					    filename: 'Generic SubReport',
					    exportOptions: {
					        columns: ':visible'
					    }
					}
					],

					ajax : {

						url : "/lcms/reports/genericSubResult?"+$('#genericSubregisterform').serialize(),
					},
					"columns" : [ {
						"title" : "S. No",
						"sClass" : "text-center"
					}, {
						"data" : "aggregatedBy",
						"title" : "Aggregated By",
						"sClass" : "text-center"
					}, {
						"data" : "noOfCase",
						"title" : "Number Of Cases",
						"sClass" : "text-center"
					}
					],
					"fnRowCallback" : function(row, data, index) {

							$('td:eq(2)', row).html(
									'<a href="javascript:void(0);" onclick="setHiddenValueByLink(\''
											+ data.aggregatedBy + '\')">'
											+ data.noOfCase + '</a>');
							return row;
					  
					}
					});
		
		 //s.no auto generation(will work in exported documents too..)
		oDataTable.on( 'order.dt search.dt', function () {
			oDataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
                oDataTable.cell(cell).invalidate('dom'); 
            } );
        } ).draw();
		

	}
}

function getdate() {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth() + 1; // January is 0!

	var yyyy = today.getFullYear();
	if (dd < 10) {
		dd = '0' + dd
	}
	if (mm < 10) {
		mm = '0' + mm
	}
	var today = dd + '/' + mm + '/' + yyyy;
	return today;
}


function callAjaxBydrillDownReport(aggregatedByValues) {
	
	var caseNumber = $("#caseNumber").val();
	var lcNumber = $("#lcNumber").val();
	var aggregatedBy = $('#aggregatedBy').val();
	var aggregatedByValue = aggregatedByValues;
	var fromDate =$("#fromDate").val();
	var toDate = $("#toDate").val();
	var today = getdate();
	
	oDataTable.clear().draw();
	oDataTable.destroy();
	oTable.remove();
	$('#tabledata').append('<table class="table table-bordered table-hover multiheadertbl" id="genericSubReport-table"> </table>')
	oTable = $('#genericSubReport-table');
	
	$('#genericSubReport-header').show();
	$('#reportgeneration-header').show();
	oDataTable=oTable.DataTable({
		dom : "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-3 col-xs-6 text-right'B><'col-md-4 col-xs-6 text-right'p>>",
		"autoWidth": false,
		"bDestroy": true,
		"processing": true,
		buttons: [
					{
					    extend: 'excel',
					    filename: 'LegalCase Drill Down Report',
					    exportOptions: {
					        columns: ':visible'
					    }
					},
				  {
				    extend: 'pdf',
				    message: "Report generated on "+today+"",
				    title: 'LegalCase Drill Down Report',
				    filename: 'Drill Down Report',
				    exportOptions: {
				        columns: ':visible'
				    }
				},
				{
				    extend: 'print',
				    title: 'LegalCase Drill Down Report',
				    filename: 'Generic SubReport',
				    exportOptions: {
				    	columns: ':visible'
				    }
				}],

				ajax : {
					url : "/lcms/reports/genericdrilldownreportresults",
					data : {
						'aggregatedBy' :aggregatedBy,
						'aggregatedByValue': aggregatedByValues
						
					}
				
				},
				columns : [
				           {"title" : "S.no","sClass" : "text-left"}, 
						{
							"data" : "legalcaseno",
							"sTitle" : "Legal Case Number",
							"className" : "text-left",
							"render" : function(data, type, full, meta) {
								return '<a href="/lcms/application/view/?lcNumber='
										+ data + '">' + data + '</div>';
							}
						},
						{
							"data" : "casenumber",
							"sTitle" : "Case Number",
							"className" : "text-left"
						},

						{
							"data" : "casetitle",
							"sTitle" : "Case Title",
							"className" : "text-left"
						},
						{
							"data" : "courtname",
							"sTitle" : "Court Name",
							"className" : "text-left"
						},
						{
							"data" : "standingcouncil",
							"sTitle" : "Standing Council",
							"className" : "text-left"
						},
						{
							"data" : "statusDesc",
							"sTitle" : "Case Status",
							"className" : "text-left"
						},
						{
							"data" : "petitioners",
							"sTitle" : "Petitioners",
							"className" : "text-left"
						},
						{
							"data" : "respondants",
							"sTitle" : "Respondents",
							"className" : "text-left"
						}
						]
	});
		
		 //s.no auto generation(will work in exported documents too..)
		oDataTable.on( 'order.dt search.dt', function () {
			oDataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
                oDataTable.cell(cell).invalidate('dom'); 
            } );
        } ).draw();
		

	}


	

function openLegalCase(lcNumber) {
	window.open("/lcms/application/view/?lcNumber="+ lcNumber , "", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}


function setHiddenValueByLink(aggregatedByValue) {
	callAjaxBydrillDownReport(aggregatedByValue);
	
}

function loadsubreportstatus(){
	if ($('#caseStatus :selected').text().localeCompare("Created") == 0 ) { 
		$("#reportstatus").show();
		}
	else
		$("#reportstatus").hide();
}


function submitSubReportStatusForm() {
	if ($('form').valid()) {

		var today = getdate();
		var caseNumber = $("#caseNumber").val();
		var lcNumber = $("#lcNumber").val();
		var isMonthColVisibile = false;
		$('#genericSubReport-header').show();
		$('#reportgeneration-header').show();
		oDataTable=oTable.DataTable({
			dom : "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-3 col-xs-6 text-right'B><'col-md-4 col-xs-6 text-right'p>>",
			"autoWidth": false,
			"bDestroy": true,
			"processing": true,
			buttons: [
						{
						    extend: 'excel',
						    filename: 'LegalCase Generic SubReport Status',
						    exportOptions: {
						        columns: ':visible'
						    }
						},
					  {
					    extend: 'pdf',
					    message: "Report generated on "+today+"",
					    title: 'LegalCase Generic SubReport Status',
					    filename: 'Generic SubReport Status',
					    exportOptions: {
					        columns: ':visible'
					    }
					},
					{
					    extend: 'print',
					    title: 'LegalCase Generic SubReport Status',
					    filename: 'Generic SubReport Status',
					    exportOptions: {
					        columns: ':visible'
					    }
					}
					],
					ajax : {

						url : "/lcms/reports/genericSubResult?"+$('#genericSubregisterform').serialize(),
					},
					columns : [
					           {"title" : "S.no","sClass" : "text-left"}, 
							{
								"data" : "legalcaseno",
								"sTitle" : "Legal Case Number",
								"className" : "text-left",
								"render" : function(data, type, full, meta) {
									return '<a href="/lcms/application/view/?lcNumber='
											+ data + '">' + data + '</div>';
								}
							},
							{
								"data" : "casenumber",
								"sTitle" : "Case Number",
								"className" : "text-left"
							},

							{
								"data" : "casetitle",
								"sTitle" : "Case Title",
								"className" : "text-left"
							},
							{
								"data" : "courtname",
								"sTitle" : "Court Name",
								"className" : "text-left"
							},
							{
								"data" : "standingcouncil",
								"sTitle" : "Standing Council",
								"className" : "text-left"
							},
							{
								"data" : "statusDesc",
								"sTitle" : "Case Status",
								"className" : "text-left",
								
							},
							{
								"data" : "petitioners",
								"sTitle" : "Petitioners",
								"className" : "text-left"
							},
							{
								"data" : "respondants",
								"sTitle" : "Respondents",
								"className" : "text-left"
							}
		],
		});	
					
		 //s.no auto generation(will work in exported documents too..)
		oDataTable.on( 'order.dt search.dt', function () {
			oDataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
                cell.innerHTML = i+1;
                oDataTable.cell(cell).invalidate('dom'); 
            } );
        } ).draw();
		

	}
		

	}