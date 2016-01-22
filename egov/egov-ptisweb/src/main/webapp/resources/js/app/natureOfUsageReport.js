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
													"sPaginationType" : "bootstrap",
													"autoWidth" : false,
													"bDestroy" : true,
													"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
													"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
													"oTableTools" : {
														"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
														"aButtons" : [ "xls", "pdf", "print" ]
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
						console.log("failed");
						jQuery('#block').prop("disabled", false);
						jQuery('#block').html("");
						jQuery('#block').append(
								"<option value='-1'>All</option>");
						bootbox.alert("No block details mapped for waard");
					}
				});
			}
		});
