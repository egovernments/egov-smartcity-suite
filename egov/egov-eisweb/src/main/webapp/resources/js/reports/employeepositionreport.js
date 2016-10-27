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
		.dataTable({
			type : 'GET',
			responsive : true,
			destroy : true,
			"autoWidth" : false,
			"bDestroy" : true,
			"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [ "xls", "pdf", "print" ]
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
			        	   "data" : "code",
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
			        	   "data" : "fromDate",
			        	   "sTitle" : "FromDate"
			           },
			           {
			        	   "data" : "toDate",
			        	   "sTitle" : "ToDate"
			           },
			           {
			        	   "data" : "isPrimary",
			        	   "sTitle" : "Primary/Temp"
			           }],
			           "aoColumnDefs" : [ {
			        	   "aTargets" : [ 2, 3, 4, 5, 6],
			        	   "mRender" : function(data, type, full) {
			        		   return data;
			        	   }
			           } ],
			           "fnRowCallback" : function(nRow, aData, iDisplayIndex){
			        	   $("td:first", nRow).html(iDisplayIndex +1);
			        	   return nRow;
			           }
		});
		jQuery('.loader-class').modal('hide');
		$('#empAssignment-header').show();
		reportdatatable.fnSetColumnVis(1, true);
	});


});

