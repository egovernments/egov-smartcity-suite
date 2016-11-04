jQuery('#btnsearch').click(function(e) {
		
		callAjaxSearch();
	});

$('input').keypress(function (e) {
    if (e.which == 13) {
    	e.preventDefault();
    	callAjaxSearch();
    }
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
					url : "/council/councilparty/ajaxsearch/"+$('#mode').val(),      
					type: "POST",
					"data":  getFormData(jQuery('form'))
				},
				/*"fnRowCallback": function (row, data, index) {
						$(row).on('click', function() {
				console.log(data.id);
				window.open('/council/councilparty/'+ $('#mode').val() +'/'+data.id,'','width=800, height=600');
			});
				 },*/
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ "xls", "pdf", "print" ]
				},
				aaSorting: [],				
				columns : [ { 
"data" : "name", "sClass" : "text-left"} ,{ 
"data" : "isActive", "sClass" : "text-left"},{"data" : null, "target":-1,
	
    sortable: false,
    "render": function ( data, type, full, meta ) {
        var mode = $('#mode').val();
   	 if(mode == 'edit')
       	 return '<button type="button" class="btn btn-xs btn-secondary edit"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>';
        else
       	return '<button type="button" class="btn btn-xs btn-secondary view"><span class="glyphicon glyphicon-tasks"></span>&nbsp;View</button>';
    }
}
,{ "data": "id", "visible":false }]				
			});
			}

$("#resultTable").on('click','tbody tr td  .view',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),3);
	window.open('/council/councilparty/'+ $('#mode').val() +'/'+ id,'','width=800, height=600,scrollbars=yes');
	
});

$("#resultTable").on('click','tbody tr td  .edit',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),3);
	window.open('/council/councilparty/'+ $('#mode').val() +'/'+ id,'','width=800, height=600,scrollbars=yes');
	
});