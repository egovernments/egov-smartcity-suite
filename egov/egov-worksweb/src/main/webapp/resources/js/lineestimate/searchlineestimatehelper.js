jQuery('#btnsearch').click(function(e) {
		
		callAjaxSearch();
	});
	
	function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
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
					url : "/egworks/lineestimate/ajaxsearch",      
					type: "POST",
					"data":  getFormData(jQuery('form'))
				},
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : []
				},
				aaSorting: [],				
				columns : [ { 
					"data" : "lineEstimateNumber", "sClass" : "text-left"} ,{ 
					"data" : "executingDepartment", "sClass" : "text-left"} ,{ 
					"data" : "fund", "sClass" : "text-left"} ,{ 
					"data" : "function", "sClass" : "text-left"} ,{ 
					"data" : "budgetHead", "sClass" : "text-left"} ,{
					"data" : "createdBy", "sClass" : "text-left"} ,{
					"data" : "totalAmount", "sClass" : "text-right"}, {
					"data" : "", "target":-1, "sClass" : "text-left","defaultContent": '<select id="actionDropdown" class="form-control"><option value="">Select from below</option><option value="1">View Lineestimate</option><option value="2">View PDF</option><option value="3">View Workflow history</option><option value="4">View Documents</option></select>'
					}]				
				});
			}

$(document).ready(function(){
    var estimateNumber = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/egworks/lineestimate/lineEstimateNumbers?name=%QUERY',
            filter: function (data) {
                return $.map(data, function (ct) {
                    return {
                        name: ct
                    };
                });
            }
        }
    });
   
    estimateNumber.initialize();
		var estimateNumber_typeahead = $('#estimateNumber').typeahead({
			hint : true,
			highlight : true,
			minLength : 3
		}, {
			displayKey : 'name',
			source : estimateNumber.ttAdapter()
		});
	});