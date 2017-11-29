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
var tableContainer;
jQuery(document).ready(function() {
	$('#wardWiseReportReport-header').hide();
	$('#report-footer').hide();
	
$('#wardWiseReportSearch').click(function(){
		var fromDate = $("#fromDate").val();
		var toDate = $("#toDate").val();	
		var wards=$("#wards").val();
		var propertyTypes=$("#proprtyTypes").val();
		var serviceTypes=$("#serviceTypes").val();
			var stsplit = fromDate.split("/");
			var ensplit = toDate.split("/");
			fromDate = stsplit[2] + "-" + stsplit[1] + "-"
					+ stsplit[0];
			toDate = ensplit[2] + "-" + ensplit[1] + "-"
					+ ensplit[0];
		$('#wardWiseReportReport-header').show();
		$('#report-footer').show();
		
		$("#wardWise-table").dataTable({
			destroy:true,
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [ 
				               {
					             "sExtends": "pdf",
					             "sTitle": jQuery('#pdfTitle').val(),
                                "sPdfMessage": "Ward Wise Service Type Report, Ward Wise Service Type Report result for dates : "+fromDate+" - "+toDate+"",
                                "sPdfOrientation": "landscape"
				                },
				                {
						             "sExtends": "xls",
						             "sPdfMessage": "Ward Wise Service Type Report, Ward Wise Service Type Report result for dates : "+fromDate+" - "+toDate+"",
						             "sTitle": jQuery('#pdfTitle').val(),
					             },{
						             "sExtends": "print",
						             "sPdfMessage": "Ward Wise Service Type Report, Ward Wise Service Type Report result for dates : "+fromDate+" - "+toDate+"",
						             "sTitle": jQuery('#pdfTitle').val(),
					              }],
				
			},
			ajax : {
				url : "/ptis/reports/wardWiseServiceTypeReport/countApplications" ,
            	"contentType": "application/json",
                "type": "POST",
                "data": function ( d ) {
                  return JSON.stringify({"propertyType":propertyTypes,"revenueWard":wards,"serviceType":serviceTypes,"fromDate": fromDate, "toDate": toDate});
                },
                "dataSrc":"serviceWiseResponse"
			},
			searchable:true,
			columns: [
			{ data: function(row){
				return row.revenueWard}},
		    { data: function(row){
		    	return (row.applicationsApproved+row.applicationsRejected+row.applicationsPending)}},
             { data: function ( row) { 	
                return (row.applicationsApproved)}},
           { data: function (row) { 	
                    return (row.applicationsRejected)}},
            { data: function (row) { 	
                    return (row.applicationsPending)}},
            { data: function (row) { 	
                    return (row.taxBeforeAffctd)}},
            { data: function (row) { 	
                    return (row.taxAfterAffctd)}},
             {data:function(row){
            	 return ((row.taxAfterAffctd)-(row.taxBeforeAffctd))
             }}
			],
			"footerCallback" : function(row, data, start, end, display) {
				var api = this.api(), data;
				if (data.length == 0) {
					jQuery('#report-footer').hide();
				} else {
					jQuery('#report-footer').show(); 
				}
				if (data.length > 0) {
					updateTotalFooter(1, api);
					updateTotalFooter(2, api);
					updateTotalFooter(3, api);
					updateTotalFooter(4, api);
					updateTotalFooter(5, api);
					updateTotalFooter(6, api);
					updateTotalFooter(7,api);
				}
			}
			
		});
});
});
function updateTotalFooter(idx,api){
	// Remove the formatting to get integer data for summation
    var intVal = function ( i ) {
        return typeof i === 'string' ?
            i.replace(/[\$,]/g, '')*1 :
            typeof i === 'number' ?
                i : 0;
    };
    // Total over all pages
    data = api.column( idx ).data();
    total = data.length ?
            data.reduce( function (a,b) {
                    return intVal(a) + intVal(b);
            } ) :
            0;

    // Update footer
    $( api.column( idx ).footer() ).html((total % 1 != 0) ? total.toFixed(2) : total);
}
