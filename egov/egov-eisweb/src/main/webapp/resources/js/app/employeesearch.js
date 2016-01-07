$(document).ready(function(){
	tableContainer1 = $("#employee-table"); 
	$("#searchbtn").click(function (){
		tableContainer1.dataTable({
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[5,10, 25, 50, -1], [5,10, 25, 50, "All"]],
			"bDestroy": true,
			"autoWidth": false,
			"ajax": "ajax/employees?"+$("#searchEmployeeForm").serialize(),
			"columns": [
			            { "data": "slno","width": "5%" },
						{ "data": "name","width": "10%" },
						{ "data": "code","width": "10%" },
						{ "data": "department","width": "15%" },
						{ "data": "designation","width": "15%" },
						{ "data": "position","width": "15%" },
						{ "data": "daterange","width": "20%" },
						{ "data" : null, "target":-1,"defaultContent": '<button type="button" class="btn btn-xs btn-secondary edit-employee add-margin"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>&nbsp;<button type="button" class="btn btn-xs btn-secondary view-employee"><span class="glyphicon glyphicon-tasks"></span>&nbsp;View</button>'}
			]
		});
		

	});
	
	
	$('#searchemployee').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});

	$("#employee-table").on('click','tbody tr td .edit-employee',function(event) {
		var code = tableContainer1.fnGetData($(this).parent().parent(),2);
		var url = '/eis/employee/update/'+code ;
		$('#searchEmployeeForm').attr('method', 'get');
		$('#searchEmployeeForm').attr('action', url);
		window.location=url;
	});
	
	$("#employee-table").on('click','tbody tr td .view-employee',function(event) {
		var code = tableContainer1.fnGetData($(this).parent().parent(),2);
		var url = '/eis/employee/view/'+code ;
		$('#searchEmployeeForm').attr('method', 'get');
		$('#searchEmployeeForm').attr('action', url);
		window.location=url;
	});

});
