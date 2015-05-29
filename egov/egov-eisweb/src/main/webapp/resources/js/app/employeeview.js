$(document).ready(function(){
	tableContainer1 = $("#employeeassign-table"); 
	
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false
	});
	
	$('#searchemployeeassign').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});

	$('#employeeassign-table').on('click', 'tbody tr', function(){
		$('.assignment-modal').modal('show'); 
	});
});
