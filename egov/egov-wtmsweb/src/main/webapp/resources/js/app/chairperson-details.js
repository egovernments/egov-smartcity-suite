$(document).ready(function(){
	loadingTable();	
	
	function loadingTable()
	{
		tableContainer = $("#chairperson-table");
		tableContainer.dataTable({
			//processing : true,
			serverSide : true,
			type : 'GET',
			sort : true,
			filter : true,
			responsive : true,
			destroy : true,
			"sPaginationType" : "bootstrap",
			"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
			"autoWidth" : false,
			 ajax : "/wtms/application/ajax-chairpersontable",
			 columns : [ {
					"mData" : "chairPerson",
					"sTitle" : "Chair Person",
				}, 
				 {
					"mData" : "fromDate",
					"sTitle" : "From Date",
				},{
					"mData" : "toDate",
					"sTitle" : "To Date"
				},{
					"mData" : "status",
					"sTitle" : "Status"
				}],
				"fnInitComplete": function(oSettings, json) {
					$('#chairperson-table tbody tr:eq(0) td:last').addClass('error-msg view-content');
				}
		});
	}
	//add chairperson
	    $('button').on('click', function () {
	        var name = $('input').val();
	       if (name != ''){
	        $.ajax({
	            url: '/wtms/application/ajax-chairPersonName',
	            type: "GET",
	            data: {
	                name: name
	            },
	            dataType : 'json'
	        });
	    }
	        alert("Chair person name updated in drop down successfully");
	        loadingTable();
	         });
	   
});