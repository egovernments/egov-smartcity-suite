var tableContainer1;
var tableContainer2;
var tableContainer3;
$(document).ready(function()
{
	tableContainer1 = $("#citizen_mycomplaints"); 
	
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row add-border'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
	});
	
	$('#mycomplaintsearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
	
	tableContainer2 = $("#citizen_taxes"); 
	
	tableContainer2.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row add-border'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
	});
	
	$('#taxesearch').keyup(function(){
		tableContainer2.fnFilter(this.value);
	});
	
	tableContainer3 = $("#citizen_properties"); 
	
	tableContainer3.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
	});
	
	$('#propertysearch').keyup(function(){
		tableContainer3.fnFilter(this.value);
	});
	
});