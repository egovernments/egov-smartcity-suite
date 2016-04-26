$(document).ready(function(){
	var contractorSearch = new Bloodhound(
			{
				datumTokenizer : function(datum) {
					return Bloodhound.tokenizers
							.whitespace(datum.value);
				},
				queryTokenizer : Bloodhound.tokenizers.whitespace,
				remote : {
					url : '/egworks/letterofacceptance/ajaxsearchcontractors-loa?contractorname=%QUERY',
					filter : function(data) {
						return $.map(data, function(ct) {
							return {
								name : ct,
							};
						});
					}
				}
			});

	contractorSearch.initialize();
	var contractorSearch_typeahead = $('#contractorCode')
			.typeahead({
				hint : true,
				highlight : true,
				minLength : 3
			}, {
				displayKey : 'name',
				source : contractorSearch.ttAdapter()
			});
});

jQuery('#btnsearch').click(function(e) {

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
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/letterofacceptance/ajax-contractorsforloa",
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"fnRowCallback" : function(row, data, index) {
					$('td:eq(0)',row).html(index+1);
					$(row).on(
							'click',
							function() {
								if (window.opener != null && !window.opener.closed) {
									window.opener.document.getElementById('contractorSearch').value = data.name;
									window.opener.document.getElementById('contractor').value = data.id;
									window.opener.document.getElementById('contractorCode').value = data.code;
								}
								window.close();
							});
				},
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ ]
				},
				aaSorting : [],
				columns : [ {
					"data" : "",
					"sClass" : "text-center"
				}, {
					"data" : "name",
					"sClass" : "text-center"
				}, {
					"data" : "code",
					"sClass" : "text-center"
				} ]
			});
}