$(document).ready(function(){

    tableContainer1 = $("#pendingtable"); 
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bDestroy": true,
		"autoWidth": false
	});
    
    $('#inboxsearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
    
});