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
	
	$("#updateBtn").hide();
	$("#resulttable").hide();
					jQuery('#search').click(function(e) {
						var assessmentNo=$("#assessmentNo").val();
						var applicationNo=$("#applicationNo").val();
						var applicationType=$("#applicationType").val();
						var electionWard=$("#electionWard").val();
						if(electionWard == '') {
							bootbox.alert("Please select election ward");
							return false;  
						}
						$("#updateBtn").show();
						$("#resulttable").show();   
						var locality=$("#locality").val();
										var oTable = jQuery('#resulttable');
										oTable
												.dataTable({
													destroy:true,
													ajax : {
														url : "/ptis/survey/properties/search",
														type : "POST",
														data : function(args) {
															return {
																"args" : JSON.stringify(args),
																"electionWard" : electionWard,
																"locality" : locality,
																"applicationNo" : applicationNo,
																"assessmentNo":assessmentNo,
																"applicationType" : applicationType
															};
														},
													},
													"aoColumnDefs" : [ {
														bSortable : false,
														aTargets : [ 0 ],
														'checkboxes' : {
															'selectRow' : true
														}
													} ],
													'select' : {
														'style' : 'multi'
													},
													"columns" : [
													   {
														   "data" : "id",
														   "name" : "applicationNumber",
														   'render' : function(data,type,full,meta) {
																return '<input type="checkbox" class="check_box" name="applicationNumber" value="'+ $('<div/>').text(data).html()
																		+ '">';
															}
													   },
													   {
															"data" : "applicationNumber",
															"name" : "applicationNumber",
															"render":function(data,type,full,meta){
																return '<label>'+full.applicationNumber+'</label> <input type="hidden" class="applicationNumber_class" name="applicationNumber" value="'+full.applicationNumber+'"></input>';
															}
														},
														{
															"data" : "assessmentNo",
														},
														{
															"data" : "electionWard",
														},
														{
															"data" : "status",
														},
														{
															"data" : "natureofwork",
														},
														{
															"data" : "sender",
														}
															
													],
												});
									});

				
					$('#resulttable').on('click', 'input[type="checkbox"]', function() {
						  var group = "input:checkbox[name='"+$(this).attr("name")+"']";
						  $(group).prop("checked",false);
						  $(this).prop("checked",true);
					});   

						jQuery('#updateBtn').click(function() { 
							var idx = 0;
							var jsonObj= [];
							var myObj= {};
							var status=false;
							var applicationNo;
							jQuery('.check_box:checked').each(function() {
								var $tr = jQuery(this).closest('tr');
								status=true;
								applicationNo = $tr.find('.applicationNumber_class').val();
								jsonObj.push(applicationNo);
								idx=idx+1;
							});
							if(status) { 
								/*var obj={
										"info":jsonObj
								}
								var o=JSON.stringify(obj);*/
								//$('#applicationNumbersArray').val(jsonObj);
								//alert($('#applicationNumbersArray').val());
							/*	jQuery
								.ajax({
									url : "/ptis/survey/properties/updateworkflow?applicationNo="+applicationNo,
									type : "POST", 
									contentType: "application/json",
									cache : false,
									dataType : "json",
									success : function(response) {
										if(response=="Success"){ 
											 setTimeout(function(){// wait for 5 secs(2)
										           location.reload(); // then reload the page.(3)
										      }, 5000);
											// bootbox.alert("Property workflow Updated Successfully....");
											
										}
									},
									error : function(response) {
										bootbox.alert("Property workflow Updated UnSuccessfully....");
									}
								});*/
								 setTimeout(function(){// wait for 5 secs(2)
							           location.reload(); // then reload the page.(3)
							      }, 5000);
						jQuery('#surveyApplication-form')
								.attr('method', 'post'); 
						jQuery('#surveyApplication-form')
								.attr('action','/ptis/survey/properties/updateworkflow?applicationNumbersArray='+jsonObj);
						jQuery('#surveyApplication-form').submit();
							} 
							else{
								bootbox.alert("Please select property to update....");
							}
						});
			
					

				});


