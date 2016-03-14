function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/EGF/budgetuploadreport/ajaxsearch",
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ "xls", "pdf", "print" ]
				},
				aaSorting : [],
				columns : [ {
					"data" : "fundCode",
					"sClass" : "text-left"
				}, {
					"data" : "deptCode",
					"sClass" : "text-left"
				}, {
					"data" : "functionCode",
					"sClass" : "text-left"
				}, {
					"data" : "glCode",
					"sClass" : "text-left"
				}, {
					"data" : "approvedReAmount",
					"sClass" : "text-right"
				}, {
					"data" : "planningReAmount",
					"sClass" : "text-right"
				} , {
					"data" : "approvedBeAmount",
					"sClass" : "text-right"
				}, {
					"data" : "planningBeAmount",
					"sClass" : "text-right"
				} ]
			});
}

$('#reBudget').change(function() {
	$('#referenceBudget').html('');
	$.ajax({
		url : '/EGF/budgetuploadreport/ajax/getReferenceBudget',
		type : "get",
		data : {
			budgetId :$('#reBudget').val()
		},
		success : function(data, textStatus, jqXHR) {
			$('#referenceBudget').html(data.name)
		},
		error : function(jqXHR, textStatus, errorThrown) {
			bootbox.alert("Error while getting reference budget");
		}
	});
});