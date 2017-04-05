/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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

$(document).ready(function() {
	jQuery('#btnsearch').click(function(e) {

		callAjaxSearch();
	});
	
	function callAjaxSearch() {
		jQuery('.report-section').removeClass('display-hide');
		jQuery("#meetingDetailsTable")
				.dataTable(
						{
							ajax : {
								url : "/council/councilreports/meetingdetails/search",
								type : "POST",
								beforeSend : function() {
									$('.loader-class').modal('show', {
										backdrop : 'static'
									});
								},
								"data" : getFormData(jQuery('form')),
								complete : function() {
									$('.loader-class').modal('hide');
								}
							},
							"bDestroy" : true,
							"autoWidth" : false,
							"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
							"aLengthMenu" : [ [ 10, 25, 50, -1 ],
									[ 10, 25, 50, "All" ] ],
							"oTableTools" : {
								"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
								"aButtons" : [ {
									"sExtends" : "xls"
								}, {
									"sExtends" : "pdf"
								}, {
									"sExtends" : "print"
								} ]
							},
							aaSorting : [],
							columns : [
									{
										"data" : "",
										render : function(data, type, row, meta) {
											return meta.row
													+ meta.settings._iDisplayStart
													+ 1;
										},
										"sClass" : "text-left"
									}, {
										"data" : "committeeType",
										"sClass" : "text-left"
									}, {
										"data" : "meetingNumber",
										"sClass" : "text-left",
										"render": function ( data, type, row, meta ) {
											return '<a target="_new" onclick="openPopup(\'/council/councilmom/view/'+ row.id +'\')">'+data+'</a>' 
										  }
									
									}, {
										"data" : "meetingDate",
										"sClass" : "text-left"
									}, {
										"data" : "meetingLocation",
										"sClass" : "text-left"
									}, {
										"data" : "meetingTime",
										"sClass" : "text-left"
									}, {
										"data" : "totalPreamblesUsed",
										"sClass" : "text-center"
									}, {
										"data" : "approvedPreambles",
										"sClass" : "text-center"
									}, {
										"data" : "adjournedPreambles",
										"sClass" : "text-center"
									}, {
										"data" : "rejectedPreambles",
										"sClass" : "text-center"
									}, {
										"data" : "totalMembers",
										"sClass" : "text-center"
									}, {
										"data" : "noOfPresent",
										"sClass" : "text-center"
									}, {
										"data" : "noOfAbsent",
										"sClass" : "text-center"
									}

							]
						});
	}
});


function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

$(document).on('change','.dropchange',function(){
    var url = $(this).val();
    if(url){
    	openPopup(url);
    }
    
});

function openPopup(url)
{
	window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
}
