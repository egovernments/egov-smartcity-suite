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

					var tbody = $('#floorDetails').children('tbody');
					var table = tbody.length ? tbody : $('#floorDetails');
					var row = '<tr>'
							+ '<td ><select data-first-option="false" name="floorTemp[{{idx}}].classificationId" id="floorTemp[{{idx}}].classificationId" class="form-control" required="required" > <option value="">Select</option><options items="${classificationId}"/></select></td>'
							+ '<td ><select data-first-option="false" name="floorTemp[{{idx}}].usageId" id="floorTemp[{{idx}}].usageId" class="form-control" required="required" > <option value="">Select</option><options items="${usageId}"/></select></td>'
							+ '<td ><select data-first-option="false" name="floorTemp[{{idx}}].floorId" id="floorTemp[{{idx}}].floorId" class="form-control" required="required" > <option value="">Select</option><options items="${floorId}" /></select></td>'
							+ '<td ><select data-first-option="false" name="floorTemp[{{idx}}].occupancyId" id="floorTemp[{{idx}}].occupancyId" class="form-control" required="required" > <option value="">Select</option><options items="${occupancyId}" /></select></td>'
							+ '<td><input name="floorTemp[{{idx}}].constructionDate" id="floorTemp[{{idx}}].constructionDate" type="text" class="form-control datepicker constructionDate" data-date-end-date="0d" required="required" /></td>'
							+ '<td><input name="floorTemp[{{idx}}].constructedPlinthArea" id="floorTemp[{{idx}}].constructedPlinthArea" type="text" required="required" class="form-control" /></td>'
							+ '<td><input name="floorTemp[{{idx}}].plinthAreaInBuildingPlan" id="floorTemp[{{idx}}].plinthAreaInBuildingPlan" type="text" required="required" class="form-control" /></td>'
							+ '<td class="text-center"><a href="javascript:void(0);" class="btn-sm btn-danger" id="deleteFloorRow" ><i class="fa fa-trash"></i></a></td>'
							+ '</tr>';

					$('#addFloorRow')
							.click(
									function() {
										if (validateFloorOnAdd("Please enter or select all values of existing rows before adding. Values cannot empty.")) {
											addRowWithData();
										}
									});

					function addRowFromObject(rowJsonObj) {
						table.append(row.compose(rowJsonObj));
					}
					
					function addRowWithData(){
						var idx = $(tbody).find('tr').length;
						// Add row
						var row = {
							'idx' : idx
						};
						addRowFromObject(row);
						jQuery(".constructionDate")
								.datepicker({
									format : 'dd/mm/yyyy',
									autoclose : true,
								}).data('datepicker');
						loadZonelist("floorTemp[" + idx
								+ "].zoneId");
						loadClassificationlist("floorTemp["
								+ idx + "].classificationId");
						loadUsagelist("floorTemp[" + idx
								+ "].usageId");
						loadFloorlist("floorTemp[" + idx
								+ "].floorId");
						loadOccupancylist("floorTemp["
								+ idx + "].occupancyId");
					}

					String.prototype.compose = (function() {
						var re = /\{{(.+?)\}}/g;
						return function(o) {
							return this.replace(re, function(_, k) {
								return typeof o[k] != 'undefined' ? o[k] : '';
							});
						}
					}());

					function validateFloorOnAdd(message) {
						var isValid = true;
						$('#floorDetails tbody tr')
								.each(
										function(index) {
											var classificationId = $(this)
													.find(
															'*[name$="classificationId"]')
													.val();
											var usageId = $(this).find(
													'*[name$="usageId"]')
													.val();
											var floorId = $(this).find(
													'*[name$="floorId"]')
													.val();
											var occupancyId = $(this).find(
													'*[name$="occupancyId"]')
													.val();
											var constructionDate = $(this)
													.find(
															'*[name$="constructionDate"]')
													.val();
											var constructedPlinthArea = $(this)
													.find(
															'*[name$="constructedPlinthArea"]')
													.val();
											if ( !classificationId
													|| !usageId
													|| !floorId
													|| !constructionDate
													|| !constructedPlinthArea
													|| !occupancyId) {
												bootbox
														.alert(message);
												isValid = false;
												return false;
											}
										});
						return isValid;
					}

					function loadFloorlist(selectBoxName) {
						var floorList = $('#floorId').val();
						var floorListStringArry = floorList.substring(1,
								floorList.length - 1);
						var floorDesc = floorListStringArry.split(',');
						$.each(floorDesc, function(index, floorDesc) {
							$('select[name="' + selectBoxName + '"]').append(
									$('<option>').val(floorDesc.split('=')[0])
											.text(floorDesc.split('=')[1]));
						});
					}

					function loadZonelist(selectBoxName) {
						var zoneList = $('#zoneId').val();
						var zoneListStringArry = zoneList.substring(1,
								zoneList.length - 1);
						var zoneDesc = zoneListStringArry.split(',');
						$.each(zoneDesc, function(index, zone) {
							$('select[name="' + selectBoxName + '"]').append(
									$('<option>').val(zone.split('=')[0]).text(
											zone.split('=')[1]));
						});
					}

					function loadClassificationlist(selectBoxName) {
						var classificationList = $('#classificationId').val();
						var classificationListStringArry = classificationList
								.substring(1, classificationList.length - 1);
						var classificationDesc = classificationListStringArry
								.split(',');
						$.each(classificationDesc, function(index,
								classification) {
							$('select[name="' + selectBoxName + '"]').append(
									$('<option>').val(
											classification.split('=')[0]).text(
											classification.split('=')[1]));
						});
					}

					function loadUsagelist(selectBoxName) {
						var usageList = $('#usageId').val();
						var usageListStringArry = usageList.substring(1,
								usageList.length - 1);
						var usageDesc = usageListStringArry.split(',');
						$.each(usageDesc, function(index, usage) {
							$('select[name="' + selectBoxName + '"]').append(
									$('<option>').val(usage.split('=')[0])
											.text(usage.split('=')[1]));
						});
					}

					function loadOccupancylist(selectBoxName) {
						var occupancyList = $('#occupancyId').val();
						var occupancyListStringArry = occupancyList.substring(
								1, occupancyList.length - 1);
						var occupancyDesc = occupancyListStringArry.split(',');
						$.each(occupancyDesc, function(index, occupancy) {
							$('select[name="' + selectBoxName + '"]').append(
									$('<option>').val(occupancy.split('=')[0])
											.text(occupancy.split('=')[1]));
						});
					}

					function getFormData($form) {
						var unindexed_array = $form.serializeArray();
						var indexed_array = {};

						$.map(unindexed_array, function(n, i) {
							indexed_array[n['name']] = n['value'];
						});

						return indexed_array;
					}

					$('#calculateTax').click(
									function(e) {
										var arv = 0;
										if(!$("#zoneId").val()){
											bootbox.alert("Please select Revenue Zone");
											return false;
										}else if(!validateFloorOnAdd("Please enter or select all values of existing rows before clicking on Calculate Tax button"))
											return false;
										$.ajax({
													url : "/ptis/calculatepropertytax",
													type : "POST",
													data : getFormData($('form')),
													cache : false,
													dataType : "json",
													success : function(response) {
														var exception;
														$('#taxResult tbody').find('tr').remove();
														$('#arv').html('');
														$('#taxResult').removeClass('display-hide');
														$.each(response,function(i,item) {
																			if(i=='exceptionString'){
																				$('#taxResult tbody').find('tr').remove();
																				$('#taxResult').addClass('display-hide');
																				$('#arv').append("<b>"+item+"</b>");
																				exception = i;
																			}
																			else if (i != 'Annual Rental Value') {
																				var row;
																				if (i != 'Total Tax') {
																					row = '<tr><td> '+ i + ' </td> <td class="text-right"> ' + item
																							+ ' </td></tr>'
																				} else {
																					 row = '<tr><td class="view-content"> '
																							+ i
																							+ ' </td> <td class="view-content text-right"> '
																							+ item
																							+ ' </td></tr>'
																				}
																				$("#taxResult tbody").append(row);
																			} else {
																				arv = item;
																			}
																		});
														if(exception!='exceptionString'){
															$('#arv').append("<b>Annual Rental Value is Rs. </b><b>" + arv + "</b>");
														}
														
													},
													error : function(response) {
														$('#taxResult tbody').find('tr').remove();
														$('#arv').html('');
														$('#taxResult').addClass('display-hide');
														$('#arv').append(
																"<b>Calculation Failed!</b>");
													}
												});

									});
					
					
					
					
				    $(document).on('click',"#deleteFloorRow",function (){
				    	 var rowIndex = $(this).closest('td').parent()[0].sectionRowIndex;
				        var rowCount = $('#floorDetails tr').length-1;
				        if(rowCount==1){
				        	bootbox.alert("This row cannot be deleted!");
				        	return false;
				        }
				        if($(this).data('record-id'))
				            deletedId.push($(this).data('record-id'));

				        $(this).closest('tr').remove();

				        $("#floorDetails tbody tr").each(function() {
				            $(this).find("input, select, hidden,textarea").each(function() {
				                var index = $(this).closest('td').parent()[0].sectionRowIndex;
				                if(index>=rowIndex){
				                    var increment = index++;
				                    $(this).attr({
				                        'name': function(_, name){
				                            var idxWithNameStr = name.match(/\d+(\D*)$/g)[0].replace(/\d+\]/g, increment+"]");
				                            return name.replace(/\d+(\D*)$/g, idxWithNameStr);
				                        },
				                        'id': function(_, id){
				                            if(id==undefined){
				                                return "";
				                            }
				                            return id.replace(/\d+(\D*)$/g, +increment);
				                        }
				                    });
				                }

				            });
				        });

				    });


				    $(document).on('click',"#btnReset",function (){
				    	$('#zoneId').val('');
				    	$('#floorDetails tbody').find('tr').remove();
				    	addRowWithData();
				    	$('#taxResult tbody').find('tr').remove();
						$('#arv').html('');
						$('#taxResult').addClass('display-hide');
			    });
				});