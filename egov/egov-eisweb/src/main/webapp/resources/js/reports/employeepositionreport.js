$(document).ready(function(){


	empAssigntableContainer = $("#tblempassign");
	$('#empAssignment-header').hide();
	$('#btnsearch')
	.on('click',
			function(event) {

		var department = "";
		var designation = "";
		var isPrimary = "";
		var position = "";


		department = $('#department').val();
		designation = $('#designation').val();
		position = $('#position').val();
		if(position == "") {
			bootbox.alert("Please Select Position");
			return false;
		}
		isPrimary = $('#isPrimary').val();
		date = $('#date').val();
		event.preventDefault();
		var reportdatatable =  empAssigntableContainer
		.DataTable({
			type : 'GET',
			responsive : true,
			destroy : true,
			"autoWidth" : false,
			"bDestroy" : true,
			 "sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row add-margin'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [  { "sExtends": "pdf","sTitle": jQuery('#pdfTitle').val() },
					               { "sExtends": "xls", "sTitle": jQuery('#pdfTitle').val()  },
					               { "sExtends": "print", "sTitle": jQuery('#pdfTitle').val() } ]
			},
			ajax : {
				url : "/eis/report/empPositionList",
				data : {	
					'department' : department,
					'designation' : designation,
					'position'   : position,
					'isPrimary'  : isPrimary
				}
			},
			columns : [
			           {
			        	   "sTitle" : "S.no",
			           },
			           {
			        
	                            "data" : function(row, type, set, meta){
		                     	return { name:row.employeeCode, id:row.id };
	                         },
	                            "render" : function(data, type, row) {
		                         return '<a href="javascript:void(0);" onclick="goToView(this);" data-hiddenele="employeeCode" data-eleval="'
				                + data.name + '">' + data.name + '</a>';
	                           },
	                   
			        	   "sTitle" : "Employee Code"
			           },
			           {
			        	   "data" : "name",
			        	   "sTitle" : "Employee Name"
			           },
			           {
			        	   "data" : "department",
			        	   "sTitle" : "Department"
			           },
			           {
			        	   "data" : "designation",
			        	   "sTitle" : "Designation"
			           },
			           {
			        	   "data" : "position",
			        	   "sTitle" : "Position"
			           },
			           {
			        	   "data" : "date",
			        	   "sTitle" : "DateRange"
			           },
			           {
			        	   "data" : "isPrimary",
			        	   "sTitle" : "Primary/Temporary"
			           }],
			           "fnRowCallback" : function(nRow, aData, iDisplayIndex){
			        	   $("td:first", nRow).html(iDisplayIndex +1);
			        	   return nRow;
			           }
		});
		jQuery('.loader-class').modal('hide');
		$('#empAssignment-header').show();
		reportdatatable.on( 'order.dt search.dt', function () {
			reportdatatable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
		            cell.innerHTML = i+1;
		            console.log(i+1)
		            reportdatatable.cell(cell).invalidate('dom'); 
		        } );
		    } ).draw();
	});


});

function goToView(obj) {
	jQuery('input[name=' + jQuery(obj).data('hiddenele') + ']')
	.val(jQuery(obj).data('eleval'));   
	window.open("/eis/employee/view/"+jQuery(obj).data('eleval'), '', 'scrollbars=yes,width=1000,height=700,status=yes');
} 

function getPosition() {
	var department = $('#department').val();
	var designation = $('#designation').val();
	
		$.ajax({
			url: "/eis/report/positions?deptId="
				+ department + "&desigId=" + designation,
			type: "GET",
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#position').empty();
				$('#position').append(
						$("<option value=''>Select from below</option>"));
				$.each(response, function(index, value) {
					$('#position').append($('<option>').text(value.name).attr('value', value.id));
				});
			}, 
			error: function (response) {
				console.log("failed");
			}
		});	
}