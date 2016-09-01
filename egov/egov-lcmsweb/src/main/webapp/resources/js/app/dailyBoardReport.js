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
var tableContainer;
var reportdatatable;
jQuery(document).ready(
		function($) {

			tableContainer = $('#dailyBoardReportsResults');
			document.onkeydown = function(evt) {
				var keyCode = evt ? (evt.which ? evt.which : evt.keyCode)
						: event.keyCode;
				if (keyCode == 13) {
					submitForm();
				}
			}
			$('#dailyBoardReportSearch').click(function() {
				submitForm();
			});

		});


		
function submitForm() {
	if($('form').valid()){
	
	$('.report-section').removeClass('display-hide');
	$('#report-footer').show();
	reportdatatable = tableContainer
			.dataTable({
				ajax : {
					
					url : "/lcms/reports/dailyBoardReportresults",
					data : {
						'caseType' :$("#caseCatogory").val(),
						'fromDate' :$("#fromDate").val(),
						'toDate': $("#toDate").val(),
						'officerIncharge' : $("#officerIncharge").val(),
						'standingCouncil': $("#standingCouncil").val()
					}
				},
				"sPaginationType" : "bootstrap",
				"autoWidth" : false,
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ "xls", "pdf", "print" ]
				},
				"fnRowCallBack" : function(row,data,index) {
					$('td:eq(0)',row).html(index+1);
				},
				columns :[{"title" : "S.no"},
				             
					       { "data" : "caseTitle" , "title": "Case Title"},  
						  { "data" : "courtName", "title": "Court Name"},
						  { "data" : "caseNumber", "title": "Case Number"},
						  { "data" : "petitioners", "title": "Petitioners"},
						  { "data" : "respondants", "title": "Respondants"},
						  { "data" : "petitionType", "title": "Petition Type"},
						  { "data" : "standingCouncil", "title": "Standing Council"},
						  { "data" : "officerIncharge", "title": "In Charge Officer"},
						  { "data" : "caseStatus", "title": "Status"},
						  { "data" : "nextDate", "title": "Next Imp Date"}
						  ],
						  
			  
						  "footerCallback" : function(row, data, start, end, display) {
								var api = this.api(), data;
								if (data.length == 0) {
									jQuery('#report-footer').hide();
								} else {
									jQuery('#report-footer').show(); 
								}
							},
				            "fnDrawCallback": function ( oSettings ) {
				                if ( oSettings.bSorted || oSettings.bFiltered )
				                {
				                    for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ )
				                    {
				                        $('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html( i+1 );
				                    }
				                }
				            },
				            
							"aoColumnDefs" : []		
				});
		
	}
	
	function updateSerialNo()
	{
		$( "#dailyBoardReportsResults-table tbody tr" ).each(function(index) {
			if($(this).find('td').length>1)
			{
				oDataTable.fnUpdate(''+(index+1), index, 0);
			}
		});
		
	}
	

}

function onchnageofDate() {
	var date;
	var d = new Date();
	var curr_month = d.getMonth();
	var curr_date = d.getDate();
	if (curr_date <= 9) {
		curr_date = ("0" + curr_date);
	}
	curr_month++;
	if (!(curr_month > 9)) {
		curr_month = ("0" + curr_month);
	}
	var curr_year = d.getFullYear();
	date = curr_date + "/" + (curr_month) + "/" + curr_year;
	$("#toDate").val(date);

}


