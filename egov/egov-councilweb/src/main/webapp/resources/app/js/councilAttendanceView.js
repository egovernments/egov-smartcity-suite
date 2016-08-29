$(document).ready(function() {

	drillDowntableContainer = jQuery("#attendanceResultTable");	
	
		reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/council/councilmeeting/attendance/ajaxsearch/"+$('#councilId').val(),      
					type: "GET"
				},
				"sPaginationType" : "bootstrap",
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
				"autoWidth" : false,
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ 
					               {
						             "sExtends": "pdf",
						             "sPdfMessage": "Report generated on "+$('#currDate').val()+"",
				                     "sTitle": "Council Meeting Attendance Report",
				                     "sPdfOrientation": "landscape"
					                },
					                {
							             "sExtends": "xls",
				                         "sPdfMessage": "Council Meeting Attendance Report",
				                         "sTitle": "Council Meeting Attendance Report"
						             },
						             {
							             "sExtends": "print",
				                         "sTitle": "Council Meeting Attendance Report"
						             }],
			},
				aaSorting: [],				

				columns : [ {
									"data" : "name",
									"sClass" : "text-left"
								}, {
									"data" : "electionWard",
									"sClass" : "text-center"
								}, {
									"data" : "designation",
									"sClass" : "text-center"
								}, {
									"data" : "qualification",
									"sClass" : "text-left"
								}, {
									"data" : "partyAffiliation",
									"sClass" : "text-center"
								}, {
									"data" : "mobileNumber",
									"sClass" : "text-left"
								}, {
									"data" : "address",
									"sClass" : "text-left"
								}, {
									"data" : "meetingDate",
									"sClass" : "text-left"
								}, {
									"data" : "committeeType",
									"sClass" : "text-left"
								}, {
									"data" : "attendance",
									"sClass" : "text-left"
								} ]				
			});
			
});