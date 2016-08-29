/**
 * 
 */

jQuery('#btnsearch').click(function(e) {

	callAjaxSearch();
});

jQuery('#preamblebtnsearch').click(function(e) {

	callAjaxSearchForAgendaPreamble();
});

$('form').keypress(function(e) {
	if (e.which == 13) {
		e.preventDefault();
		callAjaxSearch();
	}
});

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

$('#buttonClose')
		.click(
				function(e) {
					bootbox
							.confirm({
								message : 'Information entered in this screen will be lost if you close this page. please confirm if you want to close',
								buttons : {
									'cancel' : {
										label : 'No',
										className : 'btn-danger pull-right'
									},
									'confirm' : {
										label : 'Yes',
										className : 'btn-danger pull-right'
									}
								},
								callback : function(result) {
									if (result) {
										self.close();
									} else {
										// document.forms["councilPreambleform"].submit();//submit
										// it

										e.preventDefault();

									}
								}
							});
				});

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/council/councilpreamble/ajaxsearch/"
							+ $('#mode').val(),
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"autoWidth" : false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ {
						"sExtends" : "xls"
					}, {
						"sExtends" : "pdf"
					}, {
						"sExtends" : "print"
					} ]
				},
				aaSorting : [],
				columns : [
						{
							"data" : "department",
							"sClass" : "text-left"
						},
						{
							"data" : "preambleNumber",
							"sClass" : "text-left"
						},
						{
							"data" : "gistOfPreamble",
							"sClass" : "text-left"
						},
						{
							"data" : "sanctionAmount",
							"sClass" : "text-left"
						},
						{
							"data" : null,
							"target" : -1,

							sortable : false,
							"render" : function(data, type, full, meta) {
								var mode = $('#mode').val();
								if (mode == 'edit')
									return '<button type="button" class="btn btn-xs btn-secondary edit"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>';
								else
									return '<button type="button" class="btn btn-xs btn-secondary view"><span class="glyphicon glyphicon-tasks"></span>&nbsp;View</button>';
							}
						}, {
							"data" : "id",
							"visible" : false
						} ]
			});
}

function callAjaxSearchForAgendaPreamble() {
	drillDowntableContainer = jQuery("#preambleResultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/council/councilpreamble/ajaxsearch/"
							+ $('#mode').val(),
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"autoWidth" : false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ {
						"sExtends" : "xls"
					}, {
						"sExtends" : "pdf"
					}, {
						"sExtends" : "print"
					} ]
				},
				aaSorting : [],
				columns : [
						{
							"data" : "department",
							"sClass" : "text-left"
						},
						{
							"data" : "preambleNumber",
							"sClass" : "text-left"
						},
						{
							"data" : "gistOfPreamble",
							"sClass" : "text-left"
						},
						{
							"data" : "expectedAmount",
							"sClass" : "text-left"
						},
						{
							"data" : null,
							"target" : -1,

							sortable : false,
							"render" : function(data, type, full, meta) {
								var mode = $('#mode').val();
								if (mode == 'edit')
									return '<button type="button" class="btn btn-xs btn-secondary edit"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>';
								else
									return '<button type="button" class="btn btn-xs btn-secondary view"><span class="glyphicon glyphicon-tasks"></span>&nbsp;View</button>';
							}
						}, {
							"data" : "id",
							"visible" : false
						} ]
			});
}

$("#resultTable").on(
		'click',
		'tbody tr td  .view',
		function(event) {
			var id = reportdatatable.fnGetData($(this).parent().parent(), 5);
			window.open('/council/councilpreamble/' + $('#mode').val() + '/'
					+ id, '', 'width=800, height=600,scrollbars=yes');

		});

$("#resultTable").on(
		'click',
		'tbody tr td  .edit',
		function(event) {
			var id = reportdatatable.fnGetData($(this).parent().parent(), 5);
			window.open('/council/councilpreamble/' + $('#mode').val() + '/'
					+ id, '', 'width=800, height=600,scrollbars=yes');

		});