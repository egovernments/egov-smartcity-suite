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

					var editurl = '/bpa/stakeholder/update/';
					var viewurl = "/bpa/stakeholder/view/";
					
					$('#btnsearchforedit').click(function() {
						callAjaxSearchForUpdate();
					});
					$('#btnsearchforview').click(function() {
						callAjaxSearchForView();
					});

					function callAjaxSearchForUpdate() {
						$('.report-section').removeClass('display-hide');
						$("#editstakeholeder_table")
								.dataTable(
										{
											ajax : {
												url : "/bpa/stakeholder/search/update",
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
											"oTableTools" : {
												"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
												"aButtons" : [ "xls", "pdf",
																"print" ]
											},
											aaSorting : [],
											columns : [
													{
														"data" : "name",
														"sClass" : "text-left"
													},
													{
														"data" : "code",
														"sClass" : "text-left"
													},
													{
														"data" : "businessLicenceNumber",
														"sClass" : "text-left"
													},
													{
														"data" : "coaEnrolmentNumber",
														"sClass" : "text-left"
													},
													{
														"data" : "tinNumber",
														"sClass" : "text-left"
													},
													{
														"data" : null,
														"sClass" : "text-center",
														"render" : function(
																data, type,
																row, meta) {
															return '<button type="button" class="btn btn-xs btn-secondary edit" value='
																	+ editurl
																	+ row.id
																	+ '><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>';
														}
													} ]
										});

					}
					
					function callAjaxSearchForView() {
						$('.report-section').removeClass('display-hide');
						$("#viewstakeholeder_table")
								.dataTable(
										{
											ajax : {
												url : "/bpa/stakeholder/search/view",
												type : "POST",
												traditional: true,
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
											columns : [
													{
														"data" : "name",
														"sClass" : "text-left"
													},
													{
														"data" : "code",
														"sClass" : "text-left"
													},
													{
														"data" : "businessLicenceNumber",
														"sClass" : "text-left"
													},
													{
														"data" : "coaEnrolmentNumber",
														"sClass" : "text-left"
													},
													{
														"data" : "tinNumber",
														"sClass" : "text-left"
													},
													{
														"data" : null,
													    "sClass" : "text-center",
													    "render": function ( data, type, row, meta ) {
														        return '<button type="button" class="btn btn-xs btn-secondary edit" value='+viewurl+row.id +'><span class="glyphicon glyphicon-edit"></span>&nbsp;View</button>';
													    }
													}]

										});

					}
					
					$(document).on('click','.edit',function(){
					    var url = $(this).val();
					    if(url){
					    	openPopup(url);
					    }
					    
					});

					function openPopup(url)
					{
						window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
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
