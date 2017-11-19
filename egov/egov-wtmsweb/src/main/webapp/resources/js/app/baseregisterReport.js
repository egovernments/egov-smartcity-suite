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
		
		oTable= $('#baseRegisterReport-table');
		$('#baseRegister-header').show();
		oTable.dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [ 
				               {
					             "sExtends": "pdf",
                                 "sTitle": "Base Register Report",
                                 "sPdfOrientation": "landscape"
				                },
				                {
						             "sExtends": "xls",
	                                 "sTitle": "Base Register Report"
					             },{
						             "sExtends": "print",
	                                 "sTitle": "Base Register Report"
					               }],
			},
			ajax : {
				url : "/wtms/report/baseRegister/result",
				data : {
					'ward' : ward
				}
			},
			"columns" : [
                          { "data" : "consumerNo", "title": "New Consumer Number", 
                        	  "render": function ( data, type, full, meta ) {
									return '<a href="/wtms/viewDcb/consumerCodeWis/'+data+'" target="_blank">'+data+'</a>';
								    } },
                          { "data" : "oldConsumerNo", "title": "Old Consumer Number"},
						  { "data" : "assessmentNo" , "title": "Assessment Number"},  
						  { "data" : "ownerName", "title": "Owner Name"},
						  { "data" : "doorNo", "title": "Door No"},
						  { "data" : "categoryType", "title": "Category Type"},
						  { "data" : "waterSource", "title": "Water Source"},
						  { "data" : "usageType", "title": "Usage"},
						  { "data" : "pipeSize", "title": "Pipe Size"},
                          {
                                "data" : "monthlyRate",
                                "title" : "Monthly Rate",
                                "sortable" : false,
                                "render" : function(data, type, row) {
                                    return (!data || parseInt(data) == 0 ? "NA"
                                            : data);
                                }
                            },
                          { "data" : "period", "title": "Arrears Period"},
						  { "data" : "arrears", "title": "Arrears Demand", class : 'text-right'},
						  { "data" : "current", "title": "Current Demand", class : 'text-right'},
						  { "data" : "totalDemand", "title": "Total Demand", class : 'text-right'},
						  { "data" : "arrearsCollection", "title": "Arrears Collection", class : 'text-right'},
						  { "data" : "currentCollection", "title": "Current Collection", class : 'text-right'},
						  { "data" : "totalCollection", "title": "Total Collection", class : 'text-right'},
						  ],
						  "aaSorting": [] 
				});
		e.stopPropagation();
	});

});

$('#ward').change(function(){
	console.log("came on change of ward"+$('#ward').val());
	jQuery.ajax({
		url: "/egi/boundary/ajaxBoundary-blockByWard.action",
		type: "GET",
		data: {
			wardId : jQuery('#ward').val()
		},
		cache: false,
		dataType: "json",
		
	});
	
	
});

