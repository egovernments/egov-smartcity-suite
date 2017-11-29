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


jQuery(document).ready(function() {
	$('#baseRegister-header').hide();
$('#baseRegisterReportSearch').click(function(e){
		var ward = $("#ward").val();
		var block = $("#block").val();
		var exemptedCase = $("#exemptedCase").is(':checked');
		oTable= $('#baseRegisterReport-table');
		$('#baseRegister-header').show();
		oTable.dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[20, 50, 100], [20, 50, 100]],
			"autoWidth": false,
			"bDestroy": true,
			processing: true,
	        serverSide: true,
	        sort: true,
	        filter: true,
	        "searching": false,
	        "order": [[0, 'asc']],
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [ 
				               {
					             "sExtends": "pdf",
                                 "sTitle": jQuery('#pdfTitle').val(),
                                 "sPdfMessage": "Base Register(VLT) Report",
                                 "sPdfOrientation": "landscape"
				                },
				                {
						             "sExtends": "xls",
						             "sTitle": jQuery('#pdfTitle').val(),
	                                 "sPdfMessage": "Base Register(VLT) Report"
					             },{
						             "sExtends": "print",
						             "sTitle": jQuery('#pdfTitle').val(),
	                                 "sPdfMessage": "Base Register(VLT) Report"
					               }],
				
			},
			ajax : {
				url : "/ptis/report/baseRegisterVlt/result",
				type :"GET",
			    data: function (args) {
			    	return{
			    	"args": JSON.stringify(args),
					'ward' : ward,
					'block' : block,
					'exemptedCase' : exemptedCase,
					"mode":'VLT'
				};
			    },
			},
			"columns" : [
						  { "data" : "assessmentNo" , "title": "Assessment Number","name":"propertyId"},
						  { "data" : "oldAssessmentNo" , "title": "Old Assessment Number"},
						  { "data" : "sitalArea" , "title": "Plot Area"},
						  { "data" : "ward" , "title": "Ward"}, 
						  { "data" : "ownerName", "title": "Owner Name"},
						  { "data" : "surveyNo", "title": "surveyNo"},
						  { "data" : "taxationRate", "title": "Taxation Rate"},
						  { "data" : "marketValue", "title": "Market Value"},
						  { "data" : "documentValue", "title": "Document Value"},
						  { "data" : "higherValueForImposedtax", "title": "Higher Value For Imposed Tax "},
						  { "data" : "isExempted", "title": "Exempted"},
						  { "data" : "propertyTaxFirstHlf", "title": "PropertyTax FirstHlf"},
						  { "data" : "libraryCessTaxFirstHlf", "title": "LibraryCessTax FirstHlf"},
						  { "data" : "propertyTaxSecondHlf", "title": "PropertyTax SecondHlf"},
						  { "data" : "libraryCessTaxSecondHlf", "title": "LibraryCessTax SecondHlf"},
						  { "data" : "currTotal", "title": "CurrTotal"},
						  { "data" : "penaltyFines", "title": "Penalty Fines"},
						  { "data" : "arrearPeriod", "title": "Arrear Period"},
						  { "data" : "arrearPropertyTax", "title": "ArrearPropertyTax"},
						  { "data" : "arrearLibraryTax", "title": "ArrearLibraryTax"},
						  { "data" : "arrearPenaltyFines", "title": "ArrearPenaltyFines"},
						  { "data" : "arrearTotal", "title": "ArrearTotal"},
						  { "data" : "arrearColl", "title": "Arrear Collection"},
						  { "data" : "currentColl", "title": "Current Collection"},
						  { "data" : "totalColl", "title": "Total Collection"},
						  ],
						  "aaSorting": [] 
				});
		e.stopPropagation();
	});


$('#ward').change(function(){
	jQuery.ajax({
		url: "/egi/boundary/ajaxBoundary-blockByWard.action",
		type: "GET",
		data: {
			wardId : jQuery('#ward').val()
		},
		cache: false,
		dataType: "json",
		success: function (response) {
			jQuery('#block').html("");
			jQuery('#block').append("<option value=''>Select</option>");
			jQuery.each(response, function(index, value) {
				jQuery('#block').append($('<option>').text(value.blockName).attr('value', value.blockId));
			});
		}, 
		error: function (response) {
			jQuery('#block').html("");
			jQuery('#block').append("<option value=''>Select</option>");
		}
	});
});
	
});

