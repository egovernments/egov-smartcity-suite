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
			
			loadsubreportstatus();
			$('#statusId').change(function(){
				loadsubreportstatus();
			});
			
			tableContainer = $('#legalCaseResults');
			document.onkeydown = function(evt) {
				var keyCode = evt ? (evt.which ? evt.which : evt.keyCode)
						: event.keyCode;
				if (keyCode == 13) {
					submitForm();
				}
			}
			$('#legalcaseReportSearch').click(function() {
				submitForm();
			});
			
		});
$('#searchapp').keyup(function(){
	tableContainer.fnFilter(this.value);
	});

function submitForm() {

	var caseNumber = $("#caseNumber").val();
	var lcNumber = $("#lcNumber").val();

	$('.report-section').removeClass('display-hide');
	$('#report-footer').show();
	var isCancelled = jQuery('#isStatusExcluded').is(":checked");
	$.post("/lcms/elasticsearch/legalcasesearch/",$('#searchlegalcaseForm').serialize())
	.done(function(searchResult) {
	console.log(JSON.stringify(searchResult));
	tableContainer.dataTable({
	destroy : true,
	"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
	"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
	"autoWidth" : false,
	"oTableTools" : {
		"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
		"aButtons" : [ 
		               {
			             "sExtends": "pdf",
			             "mColumns": [0,1,2,4,5,6,9],
                         "sPdfMessage": "",
                         "sTitle": "Water Connection Report",
                         "sPdfOrientation": "landscape"
		                },
		                {
				             "sExtends": "xls",
				             "mColumns": [0,1,2,4,5,6,9],
                             "sPdfMessage": "Water Connection Report",
                             "sTitle": "Water Connection Report"
			             },
			             {
				             "sExtends": "print",
				             "mColumns": [0,1,2,4,5,6,9],
                             "sPdfMessage": "",
                             "sTitle": "Water Connection Report"
			             }],
		
	},
	searchable : true,
	data : searchResult,
				columns : [

						{
							"data": "lcNumber",
							"sTitle" : "Legal Case Number",
							"className" : "text-right",
							"render" : function(data, type, full, meta) {
								return '<a href="/lcms/application/view/?lcNumber='
										+ data + '">' + data + '</div>';
							}
						},
						{
							"data" : "caseNumber",
							"sTitle" : "Case Number",
							"className" : "text-right"
						},

						{
							"data" : "caseTitle",
							"sTitle" : "Case Title",
							"className" : "text-right"
						},
						{
							"data" : "courtName",
							"sTitle" : "Court",
							"className" : "text-right"
						},
						{
							"data" : "standingCouncil",
							"sTitle" : "Standing Council",
							"className" : "text-right"
						},
						{
							"data" : "caseStatus",
							"sTitle" : "Case Status",
							"className" : "text-right"
						},
						{
							"data" : "petName",
							"sTitle" : "Petitioners",
							"className" : "text-left"
						},
						{
							"data" : "resName",
							"sTitle" : "Respondents",
							"className" : "text-left"
						},
						{
							title : 'Actions',
							"className" : "text-right",
							render : function(data, type, full) {

								if (full.caseStatus == 'Created'
										|| full.caseStatus == 'Hearing In Progress' || full.caseStatus == 'Interim Stay' ) {
									return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="1">Judgment</option><option value="2">Add/Edit Standing Counsel</option><option value="10">Add/Edit Counter Affidavit Details</option><option value="3">Edit legalCase</option><option value="4">View legalCase</option><option value="6">Hearings</option><option value="7">Interim Order</option><option value="8">Close Case</option></select>');
								} else if (full.caseStatus == 'Judgment') {
									return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="4">View legalCase</option><option value="5">Edit Judgment</option><option value="8">Close Case</option><option value="11">Judgment Implementation</option></select>');
								} else if (full.caseStatus == 'Closed') {
									return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="9">Edit Close Case</option></select>');
								} else if (full.caseStatus == 'Judgment Implemented') {
									return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="12">Edit Judgment Implementation</option><option value="8">Close Case</option></select>');
								}
							}
						} ],
						
							
							});
							
	if(tableContainer.fnGetData().length > 1000){
		$('#searchResultDiv').hide();
		$('#search-exceed-msg').show();
	/*	$('#search-exceed-count').html(tableContainer.fnGetData().length);*/
	}else{
		$('#search-exceed-msg').hide();
		$('#searchResultDiv').show();
	}
	
	
	})
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
	$("#caseToDate").val(date);

}
$("#legalCaseResults").on('change', 'tbody tr td .dropchange', function() {
	var lcNumber = tableContainer.fnGetData($(this).parent().parent(), 0);
	if (this.value == 1) {
		var url = '/lcms/judgment/new/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}

	if (this.value == 2) {
		var url = '/lcms/standingCouncil/create/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}
	if (this.value == 10) {
		var url = '/lcms/counterAffidavit/create/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}

	if (this.value == 5) {
		var url = '/lcms/judgment/edit/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}
	if (this.value == 4) {
		var url = '/lcms/application/view/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}
	if (this.value == 3) {
		var url = '/lcms/application/edit/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}
	if (this.value == 6) {
		var url = '/lcms/hearing/list/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}
	if (this.value == 7) {
		var url = '/lcms/lcinterimorder/list/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}
	if (this.value == 8) {
		var url = '/lcms/legalcasedisposal/new/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}
	if (this.value == 9) {
		var url = '/lcms/legalcasedisposal/edit/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}
	if (this.value == 11) {
		var url = '/lcms/judgmentimpl/new/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}
	if (this.value == 12) {
		var url = '/lcms/judgmentimpl/new/?lcNumber=' + lcNumber;
		$('#searchlegalcaseForm1').attr('method', 'get');
		$('#searchlegalcaseForm1').attr('action', url);
		window.location = url;

	}
});

function loadsubreportstatus(){
	if ($('#statusId :selected').text().localeCompare("Created") == 0 ) { 
		$("#reportstatus").show();
		}
	else
		$("#reportstatus").hide();
}