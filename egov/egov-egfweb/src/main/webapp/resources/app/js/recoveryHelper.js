$('#btnsearch').click(function(e) {

	callAjaxSearch();
});

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function callAjaxSearch() {
	drillDowntableContainer = $("#resultTable");
	$('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/EGF/recovery/ajaxsearch/" + $('#mode').val(),
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"fnRowCallback" : function(row, data, index) {
					$(row).on(
							'click',
							function() {
								console.log(data.id);
								window.open('/EGF/recovery/' + $('#mode').val()
										+ '/' + data.id, '',
										'width=800, height=600');
							});
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
					"data" : "recoverycode",
					"sClass" : "text-left"
				}, {
					"data" : "recoveryname",
					"sClass" : "text-left"
				}, {
					"data" : "subledgertype",
					"sClass" : "text-left"
				}, {
					"data" : "chartofaccounts",
					"sClass" : "text-left"
				}, {
					"data" : "remittedto",
					"sClass" : "text-left"
				}, {
					"data" : "ifscCode",
					"sClass" : "text-left"
				}, {
					"data" : "accountNumber",
					"sClass" : "text-left"
				}, {
					"data" : "isactive",
					"sClass" : "text-center"
				} ]
			});
}

$('#bankLoan').change(function() {
	console.log("with in function");
	if ($("#bankDiv").hasClass("display-hide"))
		$('#bankDiv').removeClass('display-hide');
	else
		$('#bankDiv').addClass('display-hide');

});

$('#egPartytype').change(

		function() {
			console.log("inside function");
			$.ajax({
				method : "GET",
				url : "ajax/getAccountCodes",
				data : {
					subLedgerCode : $('#egPartytype').find('option:selected').text()
				},
				async : true
			}).done(
					function(msg) {
						$('#chartofaccounts').empty();
						var output = '<option value="">Select</option>';
						$.each(msg, function(index, value) {
							output += '<option value=' + value.id + '>'
									+ value.glcode + ' - ' + value.name
									+ '</option>';
						});
						$('#chartofaccounts').append(output);
					});

		});
