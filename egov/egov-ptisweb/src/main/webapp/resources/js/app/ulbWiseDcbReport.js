var reportdatatable;

$(document)
		.ready(
				function() {
					$('#type').val($('#typeDefaultVal').val());
					usagetableContainer = $("#tblulbDCBcollection"); 
					$('#tblulbDCBcollection').hide();
					$('#tblulbDCBcollectionheader').hide();
					$('#uldDCBCollectionReportSearch')
							.on('click',
									function(event) {
								event.preventDefault();
							
					 usagetableContainer.dataTable({
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
							url : "/ptis/reports/ulbWiseDCBList?"+$("#bcDailyCollectionReportForm").serialize()
						
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
									"data" : "ulbCode",
									"sTitle" : "Ulb Code"
								},
								{
									"data" : "collectorname",
									"sTitle" : "Name" ,"sWidth": "180px"
								},
								{
									"data" : "mobilenumber",
									"sTitle" : "Mobile Number"
								},
								{
									"data" : "totalaccessments",
									"sTitle" : " No. of assessments", "sClass": "text-right"
								},
								{
									"data" : "arrears_demand",
									"sTitle" : "Arrear tax", "sClass": "text-right"
								},
								{
									"data" : "arrears_penalty",
									"sTitle" : "Arrear interest", "sClass": "text-right"
								},
								{
									"data" : "current_demand",
									"sTitle" : "Current tax", "sClass": "text-right"
								},
								{
									"data" : "current_penalty",
									"sTitle" : "Current interest", "sClass": "text-right"
								},
								{
									"data" : "target_total_demand",
									"sTitle" : "Total", "sClass": "text-right"
								},
								{
									"data" : "target_total_demandInterest",
									"sTitle" : "Total Interest", "sClass": "text-right"
								},
								
								{
									"data" : "arrears_demand_collection",
									"sTitle" : "Arrear tax", "sClass": "text-right"
								},
								{
									"data" : "arrears_penalty_collection",
									"sTitle" : "Arrear interest", "sClass": "text-right"
								},
								{
									"data" : "current_demand_collection",
									"sTitle" : "Current tax", "sClass": "text-right"
								},
								{
									"data" : "current_penalty_collection",
									"sTitle" : "Current interest", "sClass": "text-right"
								},
								{
									"data" : "cummulative_total_Collection",
									"sTitle" : "Total", "sClass": "text-right"
								},
								{
									"data" : "cummulative_total_CollectionPercentage",
									"sTitle" : "Total Percentage", "sClass": "text-right"
								},
								{
								"data" : "cummulative_total_CollectionInterest",
								"sTitle" : "Total Interest", "sClass": "text-right"
								},
								{
									"data" : "cummulative_total_CollectionInterestPercentage",
									"sTitle" : "Total Interest Percentage", "sClass": "text-right"
								},
								{
									"data" : "balance_arrearTax",
									"sTitle" : "Arrear tax", "sClass": "text-right"
								},
								{
									"data" : "balance_arrearInterest",
									"sTitle" : "Arrear interest", "sClass": "text-right"
								},
								{
									"data" : "balance_currentTax",
									"sTitle" : "Current tax", "sClass": "text-right"
								},
								{
									"data" : "balance_currentInterest",
									"sTitle" : "Current interest", "sClass": "text-right"
								},
								{
									"data" : "balance_total",
									"sTitle" : "Total", "sClass": "text-right"
								},{
								"data" : "balance_totalInterest",
								"sTitle" : "Total Interest", "sClass": "text-right"
								}
							
								 ],"footerCallback" : function(row, data, start, end, display) {
										var api = this.api(), data;
										if (data.length == 0) {
											$('#report-footer').hide();
										} else {
											$('#report-footer').show();
										}
										if (data.length > 0) {
											updateTotalFooter(7, api);
										    updateTotalFooter(8, api);
											updateTotalFooter(9, api);
											updateTotalFooter(10, api);
											updateTotalFooter(11, api);
											updateTotalFooter(12, api);
											updateTotalFooter(13, api);
											updateTotalFooter(14, api); 
											updateTotalFooter(15, api);
											updateTotalFooter(16, api);
											updateTotalFooter(17, api);
											updateTotalFooter(18, api);
											updateTotalFooter(19, api);
											updateTotalFooter(20, api);
											updateTotalFooter(21, api);
											updateTotalFooter(22, api);
											updateTotalFooter(23, api);
											updateTotalFooter(24, api);
											updateTotalFooter(25, api);
											updateTotalFooter(26, api);

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
					 
					 $('#tblulbDCBcollection').show();
					 $('#tblulbDCBcollectionheader').show();
					 
							});
				
					$('#region').change(function(){
						$.ajax({
							url: "/ptis/reports/getRegionHeirarchyByType",    
							type: "GET",
							data: {
								regionName : $('#region').val(),   
								type : 'DISTRICT'
							},
							dataType: "json",
							success: function (response) {
								console.log("success"+response);
								$('#districtId').empty();
								$('#districtId').append($("<option value=''>All</option>"));
								$('#cityId').empty();
								$('#cityId').append($("<option value=''>All</option>"));
								
								$.each(response, function(index, value) {
									$('#districtId').append($('<option>').text(value.name).attr('value', value.name));
								});
								
							}, 
							error: function (response) {
								
								console.log("failed");
							}
						});
					});
					
					$('#districtId').change(function(){
						$.ajax({
							url: "/ptis/reports/getRegionHeirarchyByType",    
							type: "GET",
							data: {
								regionName : $('#districtId').val(),   
								type : 'CITY'
							},
							dataType: "json",
							success: function (response) {
								console.log("success"+response);
								$('#cityId').empty();
								$('#cityId').append($("<option value=''>All</option>"));
								$.each(response, function(index, value) {
									$('#cityId').append($('<option>').text(value.name).attr('value', value.name));
								});
								
							}, 
							error: function (response) {
								console.log("failed");
							}
						});
					});
					
				
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
			formatNumberInr((pageTotal/totalsize).toFixed(2)) + ' (' + formatNumberInr((total/totalsize).toFixed(2)) + ')');
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
			formatNumberInr(pageTotal.toFixed(2)) + ' (' + formatNumberInr(total.toFixed(2)) + ')');
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