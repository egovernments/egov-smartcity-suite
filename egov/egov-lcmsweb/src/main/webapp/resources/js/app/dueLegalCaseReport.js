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
	
	var redirectUrl="";
	var oTable="";
	$(".btn-primary").click(function(event){
	
	var searchButton=jQuery('.btn-primary').val();
	 if(searchButton !=undefined && searchButton=='pwrDueReportSearch'){
		 redirectUrl="/lcms/reports/pwrDueReportResult?";
		 oTable= $('#pwrDueReport-table');
	 }
	 else if(searchButton !=undefined && searchButton=='counterAffidavitDueReportSearch')
		 {
		 redirectUrl="/lcms/reports/caDueReportResult?";
		 oTable= $('#caDueReport-table');
		 }
	 
	 else if(searchButton !=undefined && searchButton=='emmplyeehearingDueReportSearch')
	 {
		 redirectUrl="/lcms/reports/employeehearingDueReportResult?";
		 oTable= $('#employeehearingDueReport-table');
	 }
	 else if(searchButton !=undefined && searchButton=='judgemntDueReportSearch')
	 {
		 redirectUrl="/lcms/reports/judgementImplDueReportResult?";
		 oTable= $('#judgemntDueReport-table');
	 }
	 submitForm(redirectUrl,oTable);

		});
	
});

var oDataTable;
		
function submitForm(redirectUrl,oTable) {
	if($('form').valid()){
		var today = getdate();
		//$('#dailyBoardReportResult-header').show();
		$('#reportgeneration-header').show();
		oDataTable=oTable.DataTable({
			dom : "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-3 col-xs-6 text-right'B><'col-md-4 col-xs-6 text-right'p>>",
			"autoWidth": false,
			"bDestroy": true,
			"processing": true,
			buttons: [
						{
						    extend: 'excel',
						    title: 'LegalCase Due Report',
						    filename: 'LegalCase Due Report',
						    exportOptions: {
						        columns: ':visible'
						    }
						},
					  {
					    extend: 'pdf',
					    message: "Report generated on "+today+"",
					    title: 'LegalCase Due Report',
					    filename: 'LegalCase Due Report',
					    exportOptions: {
					        columns: ':visible'
					    }
					},
					{
					    extend: 'print',
					    title: 'LegalCase Due Report',
					    filename: 'LegalCase Due Report',
					    exportOptions: {
					        columns: ':visible'
					    }
					}
					],
				ajax : {
					
					url : redirectUrl+$('#dueReportResultForm').serialize(),
				},
				columns :[{"title" : "S.no","sClass" : "text-center"},
				          { "data" : "caseNumber", "title": "Case Number","sClass" : "text-center"},
				          { "data" : "legalcaseno", "title": "LC Number","sClass" : "text-center"},
					      { "data" : "caseTitle" , "title": "Case Title","sClass" : "text-center"},  
						  { "data" : "courtName", "title": "Court Name","sClass" : "text-center"},
						  { "data" : "petitionerName", "title": "Petitioners","sClass" : "text-center"},
						  { "data" : "respondantName", "title": "Respondants","sClass" : "text-center"},
						  { "data" : "standingCouncil", "title": "Standing Council","sClass" : "text-center"},
						  { "data" : "officerIncharge", "title": "In Charge Officer","sClass" : "text-center"},
						  { "data" : "nextDate", "title": "Important Date","sClass" : "text-center"}
						  ],
							"fnRowCallback" : function(row, data, index) {
								$('td:eq(1)',row).html('<a href="javascript:void(0);" onclick="openLegalCase(\''+ data.legalcaseno +'\')">' + data.caseNumber + '</a>');
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
function openLegalCase(lcNumber) {
	window.open("/lcms/application/view/?lcNumber="+ lcNumber , "", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}