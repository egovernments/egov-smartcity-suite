$(document).ready(function() {
	 $('#view-boundaries').dataTable( {
	    	processing: true,
	        serverSide: true,
	        sort:false,
	        filter:true,
	        responsive:true,        
	        ajax: "/egi/controller/list-boundaries",
	        "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
	        "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
	        columns : [ {
				"mData" : "name",
				"sTitle" : "Name",
			}, {
				"mData" : "boundaryNameLocal",
				"sTitle" : "Boundary Name Local"
			}, {
				"mData" : "boundaryNum",
				"sTitle" : "Boundary Number"
			}, {
				"mData" : "fromDate",
				"sTitle" : "From Date"
			}, {
				"mData" : "toDae",
				"sTitle" : "To Date"
			}],
	    }); 
	 
	$('#view-boundaries tbody').on('click', 'tr', function () {
		if($(this).hasClass('apply-background'))
		{
			$(this).removeClass('apply-background');
		}else{
			$('#view-boundaries tbody tr').removeClass('apply-background');
			$(this).addClass('apply-background');
		}
		
    } );

});