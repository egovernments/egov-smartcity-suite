jQuery('#btnsearch').click(function() {

	callAjaxSearch();
});

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function callAjaxSearch() {
	var drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/eis/employeegrievance/ajaxsearch/" + $('#mode').val(),
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"fnRowCallback" : function(row, data) {
					$(row).on(
							'click',
							function() {
								window.open('/eis/employeegrievance/' + $('#mode').val()
										+ '/' + data.id, '',
										'width=800, height=600');
							});
				},
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ "xls", "pdf", "print" ]
				},
				aaSorting : [],
				columns : [ {
					"data" : "grievanceNumber",
					"sClass" : "text-left"
				}, {
					"data" : "employeeGrievanceType",
					"sClass" : "text-left"
				}, {
					"data" : "employeeCode",
					"sClass" : "text-left"
				}, {
					"data" : "employeeName",
					"sClass" : "text-left"
				}, {
					"data" : "status",
					"sClass" : "text-left"
				}]
			});
}