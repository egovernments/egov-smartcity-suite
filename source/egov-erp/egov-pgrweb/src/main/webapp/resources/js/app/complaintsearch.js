
var tableContainer;

jQuery(document).ready(function($)
{
	$(":input").inputmask();
	
	$(".datepicker").datepicker({
		 format: "dd/mm/yyyy"
	});
	
	$('#toggle-searchcomp').click(function(){
		if($(this).html()== "More..")
		{
			$(this).html('Less..');
			$('.show-searchcomp-more').show();
		}else
		{
			$(this).html('More..');
			$('.show-searchcomp-more').hide();
		}
		
	});
	
	
	tableContainer = $("#csearch").dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-6 col-md-3 col-left'i><'col-xs-6 col-md-3 text-right col-left'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-xs-12 col-md-3 add-margin col-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"oTableTools": {
			"sSwfPath": "../swf/copy_csv_xls_pdf.swf",
			"aButtons": [ "copy", "csv", "xls", "pdf", "print" ]
		}
	});
	
	tableContainer.columnFilter({
		"sPlaceHolder" : "head:after"
	});
	
	$("#when_date").change(function () {
		populatedate($('#when_date').val());
	});
	
	
	
	function populatedate(id) {	
		var d = new Date();
		var quarter = getquarter(d);
		var start,end;
		switch (id) {
			case "lastyear":       
			var start = ((quarter ==4) ? (d.getFullYear()-2) : (d.getFullYear()-1)); 
			var end = ((quarter == 4) ? (d.getFullYear()-1) : (d.getFullYear()));
			var pyearstart = new Date(start,3,1);
			var pyearend =  new Date(end,2,31);
			$("#start_date").datepicker("setDate", pyearstart);
			$("#end_date").datepicker("setDate", pyearend);
			break;
			
			case "thisyear": 
			var start = ((quarter == 4) ? (d.getFullYear()-1) : (d.getFullYear())); 
			var end = ((quarter == 4) ? (d.getFullYear()) : (d.getFullYear()+1));
			var cyearstart = new Date(start,3,1);
			var cyearend = new Date(end,2,31);
			$("#start_date").datepicker("setDate", cyearstart);
			$("#end_date").datepicker("setDate", cyearend);
			break;
			
			case "lastquarter":
		var lqy = ((quarter == 4) ? d.getFullYear()-1 : d.getFullYear());
		var firstDate = new Date(lqy, quarter * 3 - 3, 1);
		$("#start_date").datepicker("setDate", firstDate);
		$("#end_date").datepicker("setDate", new Date(firstDate.getFullYear(), firstDate.getMonth() + 3, 0));
		break;
		
		case "thisquarter":
		var month;
		if(quarter == 4){
		month=0; 
		}else if(quarter == 1){ 
		month = 3; 
		}else if(quarter == 2){ 
		month = 6; 
		}else if(quarter == 3){ 
		month = 9; 
		}
		
		var firstDate = new Date(d.getFullYear(), month, 1);
		$("#start_date").datepicker("setDate", firstDate);
		$("#end_date").datepicker("setDate", new Date(firstDate.getFullYear(), firstDate.getMonth() + 3, 0));
		break;
		
		case "lastmonth":
		var firstDay = new Date(d.getFullYear(), d.getMonth()-1, 1);
		var lastDay = new Date(d.getFullYear(), d.getMonth(), 0);
		$("#start_date").datepicker("setDate", firstDay);
		$("#end_date").datepicker("setDate", lastDay);
		break;
		
		case "thismonth":
		var firstDay = new Date(d.getFullYear(), d.getMonth(), 1);
		var lastDay = new Date(d.getFullYear(), d.getMonth() + 1, 0);
		$("#start_date").datepicker("setDate", firstDay);
		$("#end_date").datepicker("setDate", lastDay);
		break;
		
		case "lastweek":
		var firstday = new Date(d.setDate((d.getDate() - d.getDay()-7)));
		var lastday = new Date(d.setDate(d.getDate() - d.getDay()+6));
		var firstDay = new Date(firstday.getFullYear(), firstday.getMonth(), firstday.getDate());
		var lastDay = new Date(lastday.getFullYear(), lastday.getMonth(), lastday.getDate());
		$("#start_date").datepicker("setDate", firstDay);
		$("#end_date").datepicker("setDate", lastDay);
		break;
		
		case "thisweek":
		var firstday = new Date(d.setDate(d.getDate() - d.getDay()));
		var lastday = new Date(d.setDate(d.getDate() - d.getDay()+6));
		var firstDay = new Date(firstday.getFullYear(), firstday.getMonth(), firstday.getDate());
		var lastDay = new Date(lastday.getFullYear(), lastday.getMonth(), lastday.getDate());
		$("#start_date").datepicker("setDate", firstDay);
		$("#end_date").datepicker("setDate", lastDay);
		break;
		
		case "today":
		$("#start_date").datepicker("setDate", d);
		$("#end_date").val("");
		break;
		}
		}
		
		function getquarter(d)
		{
		if(d.getMonth() >=0 && d.getMonth() <=2)
		{
		quarter = 4;
		}else if(d.getMonth() >=3 && d.getMonth() <=5)
		{
		quarter = 1;
		}else if(d.getMonth() >=6 && d.getMonth() <=8)
		{
		quarter = 2;
		}else if(d.getMonth() >=9 && d.getMonth() <=11)
		{
		quarter = 3;
		}
		
		return quarter;
		}
		
		$(".checkdate").focus(function () {
		
		$(".checkdate").change(function() { 
		console.log("custom dates");
		$("select#when_date").prop('selectedIndex', 0);  
		});
		
		});
		
		$(".checkdate").focusout(function () {
		
		var start = $('#start_date').val();
		var end = $('#end_date').val();
		
		if(start != "" && end != ""){
		var stsplit = start.split("/");
		var ensplit = end.split("/");
		
		start = stsplit[1]+"/"+stsplit[0]+"/"+stsplit[2];
		end = ensplit[1]+"/"+ensplit[0]+"/"+ensplit[2];
		
		ValidRange(start,end);
		}
		
		});
		
		function ValidRange(start,end)
		{
		var startDate = Date.parse(start);
		var endDate = Date.parse(end);
		
		// Check the date range, 86400000 is the number of milliseconds in one day
		var difference = (endDate - startDate) / (86400000 * 7);
		if (difference < 0) {
		alert("The start date must come before the end date.");
		return false;
		}else{
		return true;
		}
		
		return true;
		
		
		}
		
		
		});					