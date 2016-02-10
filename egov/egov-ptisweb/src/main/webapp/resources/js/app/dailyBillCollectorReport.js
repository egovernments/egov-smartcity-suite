var reportdatatable;

$(document)
		.ready(
				function() {
					usagetableContainer = $("#tblbcDailycollection");
					
					var reportdatatable =  usagetableContainer
					.dataTable({
						type : 'GET',
						responsive : true,
						destroy : true,
						"sPaginationType" : "bootstrap",
						"bPaginate": false,
						"bInfo": false,
						"autoWidth" : false,
						"bDestroy" : true,
						"sDom" : "<'row'<'col-xs-19 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-19'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
						"oTableTools" : {
							"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
							"aButtons" : [ "xls", "pdf", "print" ]
						},
						ajax : {
							url : "/ptis/reports/billcollectorDailyCollectionReportList"
						
						},
						columns : [
								{
									"sTitle" : "Sl.no",
								},
								{
									"data" : "district",
									"sTitle" : "District"
								},
								{
									"data" : "ulbName",
									"sTitle" : "Ulb Name"
								},
								{
									"data" : "ulbName",
									"sTitle" : "Ulb Code"
								},
								{
									"data" : "collectorname",
									"sTitle" : "Bill Collector"
								},
								{
									"data" : "mobilenumber",
									"sTitle" : "Mobile Number"
								},
								{
									"data" : "target_arrears_demand",
									"sTitle" : "Arrears"
								},
								{
									"data" : "target_current_demand",
									"sTitle" : "Current"
								},
								{
									"data" : "target_total_demand",
									"sTitle" : "Total"
								},
								{
									"data" : "day_target",
									"sTitle" : "Daily Target"
								},
								{
									"data" : "today_total_collection",
									"sTitle" : "Daily Collection"
								},
								{
									"data" : "cummulative_arrears_collection",
									"sTitle" : "Cummulative Arrears"
								},
								{
									"data" : "cummulative_currentyear_collection",
									"sTitle" : "Cummulative Current"
								},
								{
									"data" : "cummulative_total_Collection",
									"sTitle" : "Cummulative Total"
								},
								{
									"data" : "Percentage_compareWithLastYear",
									"sTitle" : "Percentage"
								},
								{
									"data" : "lastyear_collection",
									"sTitle" : "Last year Today"
								},
								{
									"data" : "lastyear_cummulative_collection",
									"sTitle" : "Last year Cummulative"
								},
								{
									"data" : "Percentage_compareWithLastYear",
									"sTitle" : "Increase in collection"
								},
								{
									"data" : "growth",
									"sTitle" : "% of growth"
								}
								 ],
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
					/*$('#btnsearch')
							.on('click',
									function(event) {
										event.preventDefault();
										
										jQuery('.loader-class').modal('hide');
										
										reportdatatable.fnSetColumnVis(1, true);
									});*/
				});

