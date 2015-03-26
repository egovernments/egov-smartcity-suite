$(document).ready(function()
{
	tableContainer1 = $("#official_inbox"); 
	
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row add-border'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"columns": [
		{ "width": "15%" },
		{ "width": "20%" },
		{ "width": "20%" },
		{ "width": "20%" },
		{ "width": "25%" }
		],
	});
	
	$('#inboxsearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
	
});