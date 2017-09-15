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

jQuery(document).ready(function() {
					
					jQuery('#updateBtn').prop("disabled",true); 
					// jQuery('#baseRegister-header').hide();
					jQuery('#bulkBoundarySearchBtn').click(function(e) {
										var propertyid = jQuery('#propertyId').val();
										var houseNo = jQuery('#houseNo').val();
										var zone = jQuery('#revenueZoneId').val();
										var locality = jQuery('#localityId').val();
										var block = jQuery('#blockId').val();
										var ward = jQuery('#wardId').val();
										var electionWard = jQuery('#electionWardId').val();
										var res=checking();
										if(res==true){
											jQuery('#updateBtn').prop("disabled",false);
										}
										else{
											var status=false;
											if(zone != "" || locality != "" || electionWard != "" || block!="" || ward!=""){
												status=true;
											}
											
											if(status==false){
												bootbox.alert("Please Select Atleast One Option...");
												return;
											}
											else{
												jQuery('#updateBtn').prop("disabled",false);
											}
										}
										var oTable = jQuery('#bulkBoundarytable');
										// $('#baseRegister-header').show();
										oTable
												.dataTable({
													processing : true,
													serverSide : true,
													sort : true,
													filter : true,
													"searching" : false,
													responsive : true,
													destroy : true,
													"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
													"aLengthMenu": [[50, 100, 1000], [50, 100, 1000]],
													"autoWidth" : false,
													pagingType: 'full_numbers',
													"bDestroy" : true,
													"order" : [ [ 1, 'asc' ] ],
													ajax : {
														url : "/ptis/bulkboundaryupdation/search",
														type : "POST",
														data : function(args) {
															return {
																"args" : JSON.stringify(args),
																"assessmentNumber" : propertyid,
																"doorNumber" : houseNo,
																"zone" : zone,
																"block":block,
																"ward":ward,
																"locality" : locality,
																"electionWard" : electionWard
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
																"name" : "propertyId",
																'render' : function(data,type,full,meta) {
																	return '<input type="checkbox" class="check_box" name="propertyId" value="'+ $('<div/>').text(data).html()
																			+ '">';
																}
															},
															{
																"data" : "assessmentNumber",
																"name" : "propertyId",
																"render":function(data,type,full,meta){
																	return '<label>'+full.assessmentNumber+'</label> <input type="hidden" class="assessmentNumber_class" name="assessmentNumber" value="'+full.assessmentNumber+'"></input>';
															  }
															 
															},
															{
																"data" : "doorNo",
																"name" : "houseNo"
															},
															{
																"data" : "ownerName"
															},
															{
																"data" : "zone"
															},
															{
																"data" : "localities",
																"render" : function(data,type,full,meta) {
																	var $select = $("<select ></select>",
																			{
																				"class" : "localityClass_tbl",
																				"name" : "locality"
																			});
																	$.each(data,function(k,v) {
																						var $option = $("<option></option>",
																								{
																									"text" : v.bndryName,
																									"value" : v.bndryId
																								});
																						if (full.selectedlocality === v.bndryId) {
																							$option.attr("selected","selected")
																						}
																						$select.append($option);
																					});
																	return $select.prop("outerHTML");

																}
															},
															{
																"data" : "blocks",
																"render" : function(data,type,full,meta) {
																	var $select = $("<select></select>",
																			{
																				"class" : "blockClass_tbl",
																				"name" : "block"
																			});
																	$.each(data,function(k,v) {
																						var $option = $("<option></option>",
																								{
																									"text" : v.blockName,
																									"value" : v.blockId
																								});
																						if (full.selectedblock === v.blockId) {
																							$option.attr("selected","selected")
																						}
																						$select.append($option);
																					});
																	return $select.prop("outerHTML");
																}
															},
															{
																"data" : "revenueWards",
																"render" : function(data,type,full,meta) {
																	var $select = $("<select></select>",
																			{
																				"class" : "wardClass_tbl",
																				"name" : "revWard"
																			});
																	$.each(data,function(k,v) {
																						var $option = $("<option></option>",
																								{
																									"text" : v.wardName,
																									"value" : v.wardId
																								});
																						if (full.selectedrevward === v.wardId) {
																							$option.attr("selected","selected")
																						}
																						$select.append($option);
																					});
																	return $select.prop("outerHTML");
																}
															},
															{
																"data" : "electionWards",
																"render" : function(data,type,full,meta) {
																	var $select = $("<select></select>",
																			{
																				"class" : "electionWard",
																				"name" : "elecwards"
																			});
																	$.each(data,function(k,v) {
																						var $option = $("<option></option>",
																								{
																									"text" : v.bndryName,
																									"value" : v.bndryId
																								});
																						if (full.selectedWard === v.bndryId) {
																							$option.attr("selected","selected")
																						}
																						$select.append($option);
																					});
																	return $select.prop("outerHTML");
																}
															},
													],
												});
										e.stopPropagation();
									});

					/*
					 * jQuery(document) .ready( function() {
					 * jQuery("bulkBoundarytable tr
					 * .localityClass_tbl").each(function (i,el) {
					 * 
					 * loadrevenue($(this)); console.log('value',
					 * $(this).val()); }); });
					 */
					
					function loadrevenue(localityDropdown) {
						var locality = localityDropdown.val();
						jQuery
								.ajax({
									url : "/egi/public/boundary/ajaxBoundary-blockByLocality",
									type : "GET",
									data : {
										"locality" : locality
									},
									cache : false,
									dataType : "json",
									success : function(response) {
										var $tr = localityDropdown.closest('tr');
										$tr.find('.blockClass_tbl').html("");
										$tr.find('.wardClass_tbl').html("");
										jQuery.each(response.results.boundaries,function(j, boundary) {
												if (boundary.wardId) {
													$tr.find('.wardClass_tbl').append("<option value='"+ boundary.wardId+ "'>"+ boundary.wardName+ "</option>");
												}
												$tr.find('.blockClass_tbl').append("<option value='"+ boundary.blockId+ "'>"+ boundary.blockName+ "</option>");
										});
									},
									error : function(response) {
										$tr.find('.wardClass_tbl').html("");
										$tr.find('.blockClass_tbl').html("");
										bootbox.alert("No boundary details mapped for locality")
									}
								});

					}

					jQuery(document).on('change', ".localityClass_tbl",function() {
						loadrevenue(jQuery(this));
					});

					jQuery(document).on('change', ".blockClass_tbl", function() {
						loadward(jQuery(this));
					});

					function loadward(blockDropdown) {
						var block = blockDropdown.val();
						jQuery
								.ajax({
									url : "/ptis/bulkboundaryupdation/ajaxBoundary-wardByBlock",
									type : "GET",
									data : {
										blockId : block
									},
									cache : false,
									dataType : "json",
									success : function(response) {
										var $tr = blockDropdown.closest('tr');
										$tr.find('.wardClass_tbl').html("");
										jQuery.each(response,function(j, ward) {
											$tr.find('.wardClass_tbl').append("<option value='"+ ward.wardId + "'>"+ ward.wardName+ "</option>");
										});
									},
									error : function(response) {
										jQuery('#revenueBlockId').html("");
										bootbox.alert("No ward details mapped for Block")
									}
								});
					}

					// Handle click on "Select all" control
					$('.allCheckBoxClass').on('click',function() {
						// Check/uncheck all checkboxes in the table
						$('#bulkBoundarytable tbody input[type="checkbox"]').prop('checked', this.checked);
					});

					jQuery(document).ready(function() {

						jQuery('#updateBtn').click(function() {
							var idx = 0;
							var jsonObj= [];
							var myObj= {};
							var status=false;
							jQuery('.check_box:checked').each(function() {
								var $tr = jQuery(this).closest('tr');
								if($tr.find('.wardClass_tbl').val()==null || $tr.find('.blockClass_tbl').val()==null ){
									bootbox.alert("No boundary details mapped for selected locality......");
									return;
								}else{
									status=true;
									myObj={
											"propertyId":""+$tr.find('.assessmentNumber_class').val(),
											"locality":""+$tr.find('.localityClass_tbl').val(),
											"block":""+$tr.find('.blockClass_tbl').val(),
											"ward":""+$tr.find('.wardClass_tbl').val(),
											"electionWard":""+$tr.find('.electionWard').val()
									}
								jsonObj.push(myObj);
								idx=idx+1;
								}
								
							});
							if(status){
								var obj={
										"info":jsonObj
								}
								var o=JSON.stringify(obj);
								jQuery
								.ajax({
									url : "/ptis/bulkboundaryupdate/update",
									type : "POST",
									data : JSON.stringify(o),
									cache : false,
									contentType: "application/json; charset=utf-8",
									success : function(response) {
										if(response=="Success"){
											 setTimeout(function(){// wait for 5 secs(2)
										           location.reload(); // then reload the page.(3)
										      }, 5000);
											 bootbox.alert("Boundary Updated Successfully....");
											
										}
									},
									error : function(response) {
										bootbox.alert("Boundary Updated UnSuccessfully....");
									}
								});
							}
							else{
								bootbox.alert("Please select boundary to update....");
							}
						});
					});

				});

function checking(){
	var status=false;
	var empty=0;
	$('input[type=text]').each(function(){
		if(this.value==""){
			empty++;
		}
	});
	if(empty==2){
		status=false;
	}
	else{
		status=true;
	}
	return status;
}
