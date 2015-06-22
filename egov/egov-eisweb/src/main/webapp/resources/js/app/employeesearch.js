$(document).ready(function(){
	tableContainer1 = $("#employee-table"); 
	
	/*tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false
	});*/
	
	$('#searchemployee').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});

	$('.adv-button').click(function(){
		if($(this).data('advanced') == false){
			$(this).data('advanced', true);	
			$('.advanced-forms').show();			
		}else{
			$(this).data('advanced', false);	
			$('.advanced-forms').hide();	
		}
	});
	
	$('.edit-employee').click(function() {
		var url = '/eis/employee/update/'+ $('#empCode').val();
		$('#searchEmployeeForm').attr('method', 'get');
		$('#searchEmployeeForm').attr('action', url);
	});
	
	$('.view-employee').click(function() {
		var url = '/eis/employee/view/'+ $('#empCode').val();
		$('#searchEmployeeForm').attr('method', 'get');
		$('#searchEmployeeForm').attr('action', url);
	});

});
