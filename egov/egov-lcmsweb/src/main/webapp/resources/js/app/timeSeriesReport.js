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
	$('#timeSeriesReportResult-header').hide();
	$('#reportgeneration-header').hide();
	
	jQuery('#timeSeriesReportSearch').click(function(e){
				submitForm();

		});
});

function submitForm() {
	if($('form').valid()){
		var aggregatedBy = $("#aggregatedBy").val();
		var today = getdate();
		
		oTable= $('#timeSeriesReportResult-table');
		$('#timeSeriesReportResult-header').show();
		$('#reportgeneration-header').show();
		var isMonthColVisibile = ($("#period").val()==="Month");
		var oDataTable=oTable.dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, 100], [10, 25, 50, 100]],
			"autoWidth": false,
			"bDestroy": true,
			"processing": true,
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [ 
					               {
							             "sExtends": "pdf",
							             "mColumns": [ 1, 2, 3, 4],
		                                 "sPdfMessage": "Report generated on "+today+"",
		                                 "sTitle": "LegalCase Time Series Report",
		                                 "sPdfOrientation": "landscape"
						                },
						                {
								             "sExtends": "xls",
								             "mColumns": [ 1,2,3,4],
			                                 "sPdfMessage": "Time Series Report",
			                                 "sTitle": "LegalCase Time Series Report"
							             },
							             {
								             "sExtends": "print",
								             "mColumns": [ 1,2,3,4,5],
			                                 "sPdfMessage": "Time Series Report",
			                                 "sTitle": "LegalCase Time Series Report"
							             }],
				},
				ajax : {
					
					url : "/lcms/timeseriesreports/timeSeriesReportresults",
					data : {
						'aggregatedBy' :aggregatedBy,
						'period' : $("#period").val(),
						'fromDate' :$("#fromDate").val(),
						'toDate': $("#toDate").val()
					}
				},
				columns :[
				         {"title" : "S.no","sClass" : "text-center"},  
				         { "data" : "aggregatedBy" , "title": "Aggregated By","sClass" : "text-center"}, 
				         { "data" : "year", "title": "Year","sClass" : "text-center"},
				         { "data" : "month", "title": "Month","sClass" : "text-center", "visible":isMonthColVisibile},
				         { "data" : "count", "title": "Number of Cases","sClass" : "text-center"}
				  ],
				           "fnDrawCallback": function ( oSettings ) {
				                if ( oSettings.bSorted || oSettings.bFiltered )
				                {
				                    for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ )
				                    {
				                        $('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html( i+1 );
				                    }
				                }
				            }	
				});
	}
	
	function updateSerialNo()
	{
		$( "#timeSeriesReportsResult-table tbody tr" ).each(function(index) {
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

function getdate()
{
	var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1; //January is 0!

    var yyyy = today.getFullYear();
    if(dd<10){
        dd='0'+dd
    } 
    if(mm<10){
        mm='0'+mm
    } 
    var today = dd+'/'+mm+'/'+yyyy;
    return today;
}


