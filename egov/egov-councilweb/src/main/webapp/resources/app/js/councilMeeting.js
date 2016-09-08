jQuery('#btnsearch').click(function(e) {
		
		callAjaxSearch();
	});

$('form').keypress(function (e) {
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
					url : "/council/councilmeeting/ajaxsearch/"+$('#mode').val(),      
					type: "POST",
					"data":  getFormData(jQuery('form'))
				},
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"autoWidth": false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [{"sExtends" : "xls"},
						           {"sExtends" :"pdf"},
						           {"sExtends" : "print"}]
				},
				aaSorting: [],				
				columns : [ { 
"data" : "committeeType", "sClass" : "text-left"} ,{ 
"data" : "meetingNumber", "sClass" : "text-left"} ,{ 
"data" : "meetingDate", "sClass" : "text-left"},{
"data" : "meetingLocation", "sClass" : "text-left"},{
"data" : "meetingTime", "sClass" : "text-left"},{
"data" : "meetingStatus", "sClass" : "text-left", "title": "Meeting Status"}
,{ "data" : null, "target":-1,
	
    sortable: false,
    "render": function ( data, type, row, meta ) {
        var mode = $('#mode').val();
        if(mode == 'edit'){
          	 return '<button type="button" class="btn btn-xs btn-secondary edit"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>';
        }else{
        	 if(row.meetingStatus=="MOM FINALISED"){
        		 return '<button type="button" class="btn btn-xs btn-secondary generateMom"><span class="glyphicon glyphicon-tasks"></span>&nbsp;Print Resolution</button> &nbsp;<button type="button" class="btn btn-xs btn-secondary view"><span class="glyphicon glyphicon-tasks"></span>&nbsp;View</button>';
        	 }else        	
          	     return '<button type="button" class="btn btn-xs btn-secondary view"><span class="glyphicon glyphicon-tasks"></span>&nbsp;View</button>';
         }
    }
}
,{ "data": "id", "visible":false }
]				
			});
			}



$("#resultTable").on('click','tbody tr td  .view',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),7);
	//window.open('/council/agenda/'+ $('#mode').val() +'/'+id,'','width=800, height=600,scrollbars=yes');
	window.open('/council/councilmom/'+ $('#mode').val() +'/'+id,'','width=800, height=600,scrollbars=yes');
	
});

$("#resultTable").on('click','tbody tr td  .edit',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),7);
	//window.open('/council/agenda/'+ $('#mode').val() +'/'+id,'','width=800, height=600,scrollbars=yes');
	window.open('/council/councilmeeting/'+ $('#mode').val() +'/'+id,'','width=800, height=600,scrollbars=yes');
	
});

$("#resultTable").on('click','tbody tr td  .generateMom',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),7);
	window.open('/council/councilmeeting/generateresolution'+'/'+id,'','width=800, height=600,scrollbars=yes');
	
});

