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

var reportdatatable;

$(document)
		.ready(
				function() {
					usagetableContainer = $("#tblnatureofusage");
					$('#natureOfUsage-header').hide();
					$('#btnsearch')
							.on('click',
									function(event) {
										var natureOfUsage = "";
										var wardId = "";
										var blockId = "";
										natureOfUsage = $('#natureOfUsage')
												.val();
										wardId = $('#ward').val();
										blockId = $('#block').val();
										event.preventDefault();
										var reportdatatable =  usagetableContainer
												.dataTable({
													type : 'GET',
													responsive : true,
													destroy : true,
													"autoWidth" : false,
													"bDestroy" : true,
													"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
													"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
													"oTableTools" : {
														"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
														"aButtons" : [ 
														               { "sExtends": "pdf","sTitle": jQuery('#pdfTitle').val(),"sPdfMessage": jQuery('#reportTitle').html() },
														               { "sExtends": "xls", "sTitle": jQuery('#pdfTitle').val(),"sPdfMessage": jQuery('#reportTitle').html()  },
														               { "sExtends": "print", "sTitle": jQuery('#pdfTitle').val(),"sPdfMessage": jQuery('#reportTitle').html() }
														             ]
														/*"aButtons" : [ "xls", "pdf", "print" ]*/
													},
													ajax : {
														url : "/ptis/reports/natureOfUsageReportList",
														data : {
															'natureOfUsage' : natureOfUsage,
															'ward' : wardId,
															'block' : blockId
														}
													},
													columns : [
															{
																"sTitle" : "S.no",
															},
															{
																"data" : "assessmentNumber",
																"sTitle" : "Assessment Number"
															},
															{
																"data" : "ownerName",
																"sTitle" : "Owner Name"
															},
															{
																"data" : "mobileNumber",
																"sTitle" : "Mobile Number"
															},
															{
																"data" : "doorNumber",
																"sTitle" : "Door Number"
															},
															{
																"data" : "address",
																"sTitle" : "Address"
															},
															{
																"data" : "halfYearTax",
																"sTitle" : "Half Yearly Tax"
															} ],
															"aoColumnDefs" : [ {
																"aTargets" : [ 2, 3, 4, 5, 6],
																"mRender" : function(data, type, full) {
																	return data;
																}
															} ],
															"fnRowCallback" : function(nRow, aData, iDisplayIndex){
												                $("td:first", nRow).html(iDisplayIndex +1);
												               return nRow;
															}
												});
										jQuery('.loader-class').modal('hide');
										$('#natureOfUsage-header').show();
										reportdatatable.fnSetColumnVis(1, true);
									});
				});

jQuery('#ward').change(
		function() {
			var wardId = jQuery('#ward').val();
			if (wardId == "-1") {
				jQuery('#block').val("");
				jQuery('#block').prop("disabled", true);
			} else {
				jQuery.ajax({
					url : "/egi/boundary/ajaxBoundary-blockByWard.action",
					type : "GET",
					data : {
						wardId : wardId
					},
					cache : false,
					dataType : "json",
					success : function(response) {
						jQuery('#block').prop("disabled", false);
						jQuery('#block').html("");
						jQuery('#block').append(
								"<option value='-1'>All</option>");
						jQuery.each(response, function(j, block) {
							jQuery('#block').append(
									"<option value='" + block.blockId + "'>"
											+ block.blockName + "</option>");
						});
					},
					error : function(response) {
						jQuery('#block').prop("disabled", false);
						jQuery('#block').html("");
						jQuery('#block').append(
								"<option value='-1'>All</option>");
						bootbox.alert("No block details mapped for waard");
					}
				});
			}
		});
