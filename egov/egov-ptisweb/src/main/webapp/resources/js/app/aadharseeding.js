/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

jQuery(document)
		.ready(
				function() {
					$(".check_box").prop("checked", false)
					$('#searchAssessmentResult-header').hide();
					$('#searchAssessment')
							.click(
									function(e) {
										var ward = $("#wardId").val();
										var electionWard = $("#electionWardId")
												.val();
										var assessmentNo = $("#assessmentNo")
												.val();
										var doorNo = $("#doorNo").val();
										oTable = $('#searchAssessmentResult-table');
										$('#searchAssessmentResult-header')
												.show();
										oTable
												.dataTable({
													"aLengthMenu" : [
															[ 20, 50, 100 ],
															[ 20, 50, 100 ] ],
													"autoWidth" : false,
													"bDestroy" : true,
													ajax : {
														url : "/ptis/aadharseeding/result",
														type : "GET",
														data : {
															wardId : ward,
															electionWardId : electionWard,
															assessmentNo : assessmentNo,
															doorNo : doorNo
														}
													},
													"columns" : [

															{
																"data" : function(
																		row,
																		type,
																		set,
																		meta) {
																	return {
																		name : row.assessmentNo,
																		id : row.assessmentNo
																	};
																},
																"render" : function(
																		data,
																		type,
																		row) {
																	return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this);" data-hiddenele="assessmentNo" data-eleval="'
																			+ row.assessmentNo
																			+ '">'
																			+ row.assessmentNo
																			+ '</a>';
																},
																"title" : "Name",
																"name" : "propertyId"
															},
															{
																"data" : "doorNo",
																"title" : "Door No"
															},
															{
																"data" : "ownerName",
																"title" : "Owner Name"
															},
															{
																"data" : "address",
																"title" : "Property Address"
															} ],
													"aaSorting" : []
												});
									});
				});
$(document).ready(function(){
    if(localStorage.getItem("status") == "OK")
    {
    	bootbox.alert("Aadhar Seeding Approved/Rejected for the selected Assessments successfully!");
        localStorage.clear();
    }
});
function setHiddenValueByLink(obj) {
	window.open("../aadharseeding/aadhardataupdateform/" + obj.innerHTML
			+ '/CREATED', '', "_self");
}

$('.allCheckBoxClass').on(
		'click',
		function() {
			$('#approvalTable tbody input[type="checkbox"]').prop('checked',
					this.checked);
		});

$(".viewButton").click(
		function() {
			var assessmentNo = $(this).parent().parent().find('td').eq(1)
					.text().trim();
			window.open("../aadharseeding/aadhardataupdateform/" + assessmentNo
					+ '/UPDATED',
					'scrollbars=yes,width=1000,height=700,status=yes');
		});

$('#updateBtn').on(
		'click',
		function() {
			var jsonObj = [];
			var myObj = {};
			jQuery('.check_box:checked').each(
					function() {
						myObj = {
							"propertyId" : ""
									+ $(this).parent().parent().find('td')
											.eq(1).text().trim(),
							"mode" : "approve"				
						}
						jsonObj.push(myObj);
					});
			if(jQuery.isEmptyObject(jsonObj)){
				bootbox
				.alert("Please select the records to Update!");
			}
			else{
				var obj = {
						"info" : jsonObj
					}
				var o = JSON.stringify(obj);
				jQuery.ajax({
					url : "/ptis/aadharseeding/aadhardataapprovalform",
					type : "POST",
					data : JSON.stringify(o),
					cache : false,
					contentType : "application/json; charset=utf-8",
					success : function() {
						localStorage.setItem("status","OK")
						window.location.reload()
					}
				});
			}
		});

$('#rejectBtn').on(
		'click',
		function() {
			var jsonObj = [];
			var myObj = {};
			jQuery('.check_box:checked').each(
					function() {
						myObj = {
							"propertyId" : ""
									+ $(this).parent().parent().find('td')
											.eq(1).text().trim(),
							"mode" : "reject"					
						}
						jsonObj.push(myObj);
					});
			if(jQuery.isEmptyObject(jsonObj)){
				bootbox
				.alert("Please select the records to Update!");
			}
			else{
				var obj = {
						"info" : jsonObj
					}
				var o = JSON.stringify(obj);
				jQuery.ajax({
					url : "/ptis/aadharseeding/aadhardataapprovalform",
					type : "POST",
					data : JSON.stringify(o),
					cache : false,
					contentType : "application/json; charset=utf-8",
					success : function() {
						localStorage.setItem("status","OK")
						window.location.reload()
					}
				});
			}
		});
