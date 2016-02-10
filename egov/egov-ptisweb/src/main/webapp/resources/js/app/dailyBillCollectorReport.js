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
							"aButtons" : [ "print" ]
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
									"sTitle" : "Name"
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
									"sTitle" : "Day Target"
								},
								{
									"data" : "today_total_collection",
									"sTitle" : "Day Collection"
								},
								{
									"data" : "cummulative_arrears_collection",
									"sTitle" : "Arrears"
								},
								{
									"data" : "cummulative_currentyear_collection",
									"sTitle" : "Current"
								},
								{
									"data" : "cummulative_total_Collection",
									"sTitle" : " Total"
								},
								{
									"data" : "cummulative_currentYear_Percentage",
									"sTitle" : "%"
								},
								{
									"data" : "lastyear_collection",
									"sTitle" : "Today"
								},
								{
									"data" : "lastyear_cummulative_collection",
									"sTitle" : "Cummulative"
								},
								{
									"data" : "Percentage_compareWithLastYear",
									"sTitle" : "Increase of collection"
								},
								{
									"data" : "growth",
									"sTitle" : "% of growth"
								}
								 ],"footerCallback" : function(row, data, start, end, display) {
										var api = this.api(), data;
										if (data.length == 0) {
											$('#report-footer').hide();
										} else {
											$('#report-footer').show();
										}
										if (data.length > 0) {
											updateTotalFooter(6, api);
											updateTotalFooter(7, api);
											updateTotalFooter(8, api);
											updateTotalFooter(9, api);
											updateTotalFooter(10, api);
											updateTotalFooter(11, api);
											updateTotalFooter(12, api);
											updateTotalFooter(13, api);
											averageTotalFooter(14, api,data.length); 
											updateTotalFooter(15, api);
											updateTotalFooter(16, api);
											updateTotalFooter(17, api);
											averageTotalFooter(18, api,data.length);

										}
									},
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

function averageTotalFooter(colidx, api,totalsize) { 
	// Remove the formatting to get integer data for summation
	var intVal = function(i) {
		return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
				: typeof i === 'number' ? i : 0;
	};

	// Total over all pages
	total = api.column(colidx).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	});

	// Total over this page
	pageTotal = api.column(colidx, {
		page : 'current'
	}).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	}, 0);
	// Update footer
	$(api.column(colidx).footer()).html(
			formatNumberInr(pageTotal/totalsize) + ' (' + formatNumberInr(total/totalsize) + ')');
}

function updateTotalFooter(colidx, api) {
	// Remove the formatting to get integer data for summation
	var intVal = function(i) {
		return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
				: typeof i === 'number' ? i : 0;
	};

	// Total over all pages
	total = api.column(colidx).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	});

	// Total over this page
	pageTotal = api.column(colidx, {
		page : 'current'
	}).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	}, 0);

	// Update footer
	$(api.column(colidx).footer()).html(
			formatNumberInr(pageTotal) + ' (' + formatNumberInr(total) + ')');
}
function formatNumberInr(x) {
	if (x) {
		x = x.toString();
		var afterPoint = '';
		if (x.indexOf('.') > 0)
			afterPoint = x.substring(x.indexOf('.'), x.length);
		x = Math.floor(x);
		x = x.toString();
		var lastThree = x.substring(x.length - 3);
		var otherNumbers = x.substring(0, x.length - 3);
		if (otherNumbers != '')
			lastThree = ',' + lastThree;
		var res = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",")
				+ lastThree + afterPoint;
		return res;
	}
	return x;
}