/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
var tableContainer;
jQuery(document).ready(function($) {
		$(":input").inputmask();
		tableContainer = $("#aplicationSearchResults");
		var userrole = $('#userRole').val();
		$('#searchapprvedapplication').click(function() {
			$.post("/wtms/search/waterSearch/",$('#waterSearchRequestForm').serialize())
			.done(function(searchResult) {
			console.log(JSON.stringify(searchResult));
			tableContainer.dataTable({
			destroy : true,
			"sPaginationType" : "bootstrap",
			"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
			"autoWidth" : false,
			searchable : true,
			data : searchResult,
			columns : [{title : 'Applicant Name',data : 'resource.searchable.consumername'},
			           {title : 'Consumer No.',class : 'row-detail',data : 'resource.clauses.consumercode'},
			           {title : 'Address',data : 'resource.searchable.locality'},
			           {title : 'apptype',data : 'resource.clauses.applicationcode',"bVisible" : false},
			           {title : 'Usage Type',data : 'resource.clauses.usage'},
			           {title : 'Total Due',data : 'resource.clauses.totaldue'},
			           {title : 'Status',data : 'resource.clauses.status'},
			           {title : 'conntype',data : 'resource.clauses.connectiontype',"bVisible" : false},
			           {title : 'WaterTax Due',data : 'resource.clauses.waterTaxDue',"bVisible" : false},
				       {title : 'Actions',
			        	   render : function(data,type,full) {
									if (full != null&& full.resource != undefined && full.resource.clauses.applicationcode != undefined &&
											(full.resource.clauses.applicationcode == 'ADDNLCONNECTION' )) {
										if (full.resource.clauses.status == 'ACTIVE' ) {
										if (userrole == "CSC Operator" && full.resource.clauses.waterTaxDue > 0) {
										return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="2">Change of use</option><option value="6">Collect Fees</option></select>');
										}
										else if (userrole == "CSC Operator" ) {
											return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="2">Change of use</option></select>');
											}
										else if (userrole == "ULB Operator" &&  full.resource.clauses.connectiontype =='METERED') {
											return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="8">Enter Meter Reading</option></select>');
										}
										else if (userrole == "ULB Operator" &&  full.resource.clauses.connectiontype !='METERED') {
											return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option></select>');
										}
										else if(userrole=='CSC Operator'){
											return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="6">Collect Tax</option></select>');
											
										}
									} else if ((userrole == 'CSC Operator' || userrole == 'ULB Operator' )
											&& full.resource.clauses.status == 'DISCONNECTED') {
										return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="4">Reconnection</option></select>');
									}
									else if(userrole=='Super User' & full.resource.clauses.status == 'DISCONNECTED') {
											return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
									}
									}
								if (full != null&& full.resource != undefined&& full.resource.clauses.applicationcode != undefined
										&& full.resource.clauses.applicationcode == 'NEWCONNECTION') {
									if (full.resource.clauses.status == 'ACTIVE') {
										if (userrole == "CSC Operator" && full.resource.clauses.waterTaxDue > 0) {
											return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="6">Collect Tax</option></select>');
										}
										else if (userrole == "CSC Operator" ) {
											return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="1">Additional connection</option><option value="2">Change of use</option></select>');
										}
										else if (userrole == "ULB Operator" && full.resource.clauses.connectiontype =='METERED' ) {
											return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="8">Enter Meter Reading</option></select>');
										}
										else if (userrole == "ULB Operator" && full.resource.clauses.connectiontype !='METERED') {
											return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="1">Additional connection</option><option value="2">Change of use</option></select>');
										}
										else if(userrole=='Super User'){
											return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="6">Collect Fees</option></select>');
										}
										
									} else if ((userrole == 'CSC Operator' )&& full.resource.clauses.status == 'DISCONNECTED') {
										return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="4">Reconnection</option></select>');
									}
									else if (((userrole == 'ULB Operator') || (userrole=='Super User')) && full.resource.clauses.status == 'DISCONNECTED') {
										return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
									}
								}	
								if (full != null&& full.resource != undefined && full.resource.clauses.applicationcode != undefined &&
										 full.resource.clauses.applicationcode == 'CHANGEOFUSE') {
																				if (userrole == "CSC Operator" && full.resource.clauses.status == 'ACTIVE' && full.resource.clauses.waterTaxDue > 0) {
																				return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="6">Collect Tax</option></select>');
																				}
																				else if (userrole == "ULB Operator" && full.resource.clauses.status == 'ACTIVE' && full.resource.clauses.connectiontype =='METERED') {
																					return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="8">Enter Meter Reading</option></select>');
																				}
																				else if (userrole == "ULB Operator" &&  full.resource.clauses.status == 'ACTIVE' && full.resource.clauses.connectiontype !='METERED') {
																					return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
																				}
																				else if(userrole=='Super User'){
																					return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
																				}
																			} 
							if (full!=null && full.resource !=undefined ){
								if((full.resource.clauses.status == 'CLOSED'|| full.resource.clauses.status == 'HOLDING'|| userrole == "Water Tax Approver")) { // Assistant
																				// Engineer,Commitioner
									return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
								} else if (userrole == "Operator") { // Collection
																		// Operator
									return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="6">Collect Tax</option></select>');
								} 
								else
									{
									return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option></select>');
									
									}
							}
								

							}

						} ]
			});
			})
		});
														
					$("#aplicationSearchResults").on('change','tbody tr td .dropchange',
							function() {
							var consumerNumber = tableContainer.fnGetData($(this).parent().parent(), 1);
										if (this.value == 0) {
											var url = '/wtms/application/view/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
										} else if (this.value == 1) {
											var url = '/wtms/application/addconnection/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
											// window.location.href="applyforadditionalconnection.html"
										} else if (this.value == 2) {
											if (consumerNumber != '') {
												var url = '/wtms/application/changeOfUse/'+ consumerNumber;
												$('#waterSearchRequestForm').attr('method', 'get');
												$('#waterSearchRequestForm').attr('action', url);
												window.location = url;
											}
											// window.location.href="changeofuse.html"
										} else if (this.value == 3) {

											// window.location.href="reconnection.html"
										} else if (this.value == 4) {
											// window.location.href="disconnectionotice.html"
										} else if (this.value == 5) {
											// window.location.href="disconnectionotice.html"
										} else if (this.value == 6) {
											var url = '/wtms/application/generatebill/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
										} else if (this.value == 7) {
											// window.location.href="excecutiondate.html"
										}
										else if (this.value == 8) {
											var url = '/wtms/application/meterentry/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
										} 

									});

						$('#aplicationSearchResults').on('click','tbody tr td.row-detail',
									function() {
										var consumerNumber = tableContainer.fnGetData(this);
										var url = '/wtms/application/view/'+ consumerNumber;
										$('#waterSearchRequestForm').attr('method', 'get');
										$('#waterSearchRequestForm').attr('action', url);
										window.open(url, 'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
									});

					$('#aplicationSearchResults').on('click','tbody tr td.row-detail',
									function() {
										var consumerNumber = tableContainer.fnGetData(this);
										var url = '/wtms/application/view/'+ consumerNumber;
										$('#waterSearchRequestForm').attr('method', 'get');
										$('#waterSearchRequestForm').attr('action', url);
										window.open(url, 'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');

									});

					$('#searchwatertax').keyup(function() {
						tableContainer.fnFilter(this.value);
					});

				});
