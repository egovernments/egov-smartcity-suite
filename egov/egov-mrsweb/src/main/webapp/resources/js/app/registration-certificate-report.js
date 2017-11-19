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

$(document)
		.ready(
				function() {

					$('#btnregcertificatesearch').click(function() {
						if ($('form').valid()) {
							callAjaxSearch();
						} else {
							e.preventDefault();
						}
					});

					function callAjaxSearch() {
						$('.report-section').removeClass('display-hide');
						var reportdatatable = $("#marriagecertificate_table")
								.dataTable(
										{
											"initComplete": function(settings, json){ 
									            var  info = this.api().page.info();
									            $("div.displayCount").html('Total No.of Records Available In Entered Search Criteria are : '+ info.recordsTotal);
									        },
											ajax : {
												url : "/mrs/report/certificatedetails",
												type : "POST",
												beforeSend : function() {
													$('.loader-class')
															.modal(
																	'show',
																	{
																		backdrop : 'static'
																	});
												},
												"data" : getFormData($('form')),
												complete : function() {
													$('.loader-class').modal(
															'hide');
												}
											},
											"bDestroy" : true,
											"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
											"aLengthMenu" : [
													[ 10, 25, 50, -1 ],
													[ 10, 25, 50, "All" ] ],
											"oTableTools" : {
												"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
												"aButtons" : [ "xls", "pdf",
														"print" ]
											},
											aaSorting : [],
											columns : [{
												"data" : "registrationNo",
												render: function(data, type, row, meta){
													 var url;
													 if(!row.registrationNo)
													 {
														 return "N/A";
													 } else if(row.certificateType == 'REGISTRATION'){
														 url="/mrs/registration/view/"+row.id;
													 }else{
														 url="/mrs/reissue/view/"+row.id;
													 }
													 
													 return '<a onclick="openPopup(\''+ url + '\')" href="javascript:void(0);">'
																+ row.registrationNo + '</a>';
											},
												"sClass" : "text-left"
											}, {
												"data" : "",
												render : function(data,type, row, meta) {
													if(row.certificateNo == '' || row.certificateNo == 'undefined'){
														return 'N/A';
													}
													else{
														return row.certificateNo; 
													}
												},
												"sClass" : "text-left"
											},
											{
												"data" : "zone",
												"sClass" : "text-left"
											},
											{
												"data" : "certificateDate",
												"sClass" : "text-left"
											},
											{
												"data" : "certificateType",
												"sClass" : "text-left"
											},{
												"data" : "registrationDate",
												"sClass" : "text-left"
											}, {
												"data" : "dateOfMarriage",
												"sClass" : "text-left"
											}, {
												"data" : "husbandName",
												"sClass" : "text-left"
											}, {
												"data" : "wifeName",
												"sClass" : "text-left"
											},{
												"data" : "remarks",
												"sClass" : "text-left"
											}]

										});
					}

				});

function openPopup(url)
{
	window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}
