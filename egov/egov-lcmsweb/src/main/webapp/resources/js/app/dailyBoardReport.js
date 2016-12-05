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
	$('#dailyBoardReportResult-header').hide();
	$('#reportgeneration-header').hide();
	
	jQuery('#dailyBoardReportSearch').click(function(e){
				submitForm();

		});
});


		
function submitForm() {
	if($('form').valid()){
		var caseType = $("#caseCategory").val();
		var today = getdate();
		
		oTable= $('#dailyBoardReportResult-table');
		$('#dailyBoardReportResult-header').show();
		$('#reportgeneration-header').show();
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
							             "mColumns": [ 1, 2, 3, 4,5,6,7,8,9,10],
		                                 "sPdfMessage": "Report generated on "+today+"",
		                                 "sTitle": "LegalCase Daily Board Report",
		                                 "sPdfOrientation": "landscape"
						                },
						                {
								             "sExtends": "xls",
								             "mColumns": [ 1,2,3,4,5,6,7,8,9,10],
			                                 "sPdfMessage": "Daily Board Report",
			                                 "sTitle": "LegalCase Daily Board Report"
							             },
							             {
								             "sExtends": "print",
								             "mColumns": [ 1,2,3,4,5,6,7,8,9,10],
			                                 "sPdfMessage": "Daily Board Report",
			                                 "sTitle": "LegalCase Daily Board Report"
							             }],
				},
				ajax : {
					
					url : "/lcms/reports/dailyBoardReportresults",
					data : {
						'caseType' :caseType,
						'fromDate' :$("#fromDate").val(),
						'toDate': $("#toDate").val(),
						'officerIncharge' : $("#officerIncharge").val()
					}
				},
				columns :[{"title" : "S.no","sClass" : "text-center"},
				             
					       { "data" : "caseTitle" , "title": "Case Title" ,"sClass" : "text-center"},  
						  { "data" : "courtName", "title": "Court Name","sClass" : "text-center"},
						  { "data" : "caseNumber", "title": "Case Number","sClass" : "text-center"},
						  { "data" : "petitionerName", "title": "Petitioners","sClass" : "text-center"},
						  { "data" : "respondantName", "title": "Respondants","sClass" : "text-center"},
						  { "data" : "petitionType", "title": "Petition Type","sClass" : "text-center"},
						  { "data" : "standingCouncil", "title": "Standing Council","sClass" : "text-center"},
						  { "data" : "officerIncharge", "title": "In Charge Officer","sClass" : "text-center"},
						  { "data" : "caseStatus", "title": "Status","sClass" : "text-center"},
						  { "data" : "nextDate", "title": "Next Imp Date","sClass" : "text-center"}
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


