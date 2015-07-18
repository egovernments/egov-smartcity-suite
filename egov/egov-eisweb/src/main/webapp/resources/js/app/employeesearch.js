$(document).ready(function(){
	tableContainer1 = $("#employee-table"); 
	$("#searchbtn").click(function (){
		var search = new Array();
		search.push($("#searchText").val());
		if(null== search || ''==search){
			$("#freeText").val(false);
			if(null!=$("#name").val() && ''!=$("#name").val())
				search.push($("#name").val());
			if(null!=$("#code").val() && ''!=$("#code").val())
				search.push($("#code").val());
			if(null!=$("#aadhaar").val() && ''!=$("#aadhaar").val())
				search.push($("#aadhaar").val());
			if(null!=$("#pan").val() && ''!=$("#pan").val())
				search.push($("#pan").val());
			if(null!=$("#mobileNumber").val() && ''!=$("#mobileNumber").val())
				search.push($("#mobileNumber").val());
			if(null!=$("#email").val() && ''!=$("#email").val())
				search.push($("#email").val());
			if(null!=$("#department").val() && ''!=$("#department").val())
				search.push($("#department").val());
			if(null!=$("#designation").val() && ''!=$("#designation").val())
				search.push($("#designation").val());
			if(null!=$("#position").val() && ''!=$("#position").val())
				search.push($("#position").val());
			if(null!=$("#function").val() && ''!=$("#function").val())
				search.push($("#function").val());
			if(null!=$("#functionary").val() && ''!=$("#functionary").val())
				search.push($("#functionary").val());
			if(null!=$("#status").val() && ''!=$("#status").val())
				search.push($("#status").val());
			if(null!=$("#employeeType").val() && ''!=$("#employeeType").val())
				search.push($("#employeeType").val());
		}
		else
			$("#freeText").val(true);
		tableContainer1.dataTable({
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[5,10, 25, 50, -1], [5,10, 25, 50, "All"]],
			"bDestroy": true,
			"autoWidth": false,
			"ajax": "ajax/employees?searchText="+search+"&freeText="+$("#freeText").val()+"&"+$("#searchEmployeeForm").serialize(),
			"columns": [
			            { "data": "slno","width": "5%" },
						{ "data": "name","width": "10%" },
						{ "data": "code","width": "10%" },
						{ "data": "department","width": "15%" },
						{ "data": "designation","width": "15%" },
						{ "data": "position","width": "15%" },
						{ "data": "daterange","width": "20%" },
						{ "data" : null, "target":-1,"defaultContent": '<button type="button" class="btn btn-xs btn-secondary edit-employee"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>&nbsp;<button type="button" class="btn btn-xs btn-secondary view-employee"><span class="glyphicon glyphicon-view"></span>&nbsp;View</button>'}
			]
		});
		

	});
	
	
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
		$("#searchText").val("");
	});
	
	$("#searchText").focus(function (){
		$(".btn-danger").trigger("click");
		$(".adv-button").data('advanced', false);
		$('.advanced-forms').hide();
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
