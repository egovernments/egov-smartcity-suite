$(document).ready(function(){
	loadingTable();	
	
	//add chairperson
	$('#buttonid').click(function() {
		  if ($( "#chairPersonDetailsform" ).valid())
		  {
			var name = $('input').val();
		       if (name != ''){
		        $.ajax({
		            url: '/wtms/application/ajax-activeChairPersonExistsAsOnCurrentDate',
		            type: "GET",
		            data: {
		            	name: name
		            },
		            dataType : 'json',
		            success: function (response) {
		    			console.log("success"+response);
		    			if(response==true){
			    				addChairPerson();
			    				loadingTable();
			    			}
		    			else{
		    				overwritechairperson();
		    				loadingTable();
		    			}
		    		},error: function (response) {
		    			console.log("failed");
		    		}
		        });
		        
		       }
		  }
		});
});

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
				"sTitle" : "S.no",
			},
			{
				"mData" : "chairPerson",
				"sTitle" : "Chair Person Name",
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
			},
			"fnRowCallback" : function(nRow, aData, iDisplayIndex){
                $("td:first", nRow).html(iDisplayIndex +1);
               return nRow;
            }
	});
}

function overwritechairperson()
{
	 if(confirm("On entered date chairperson name is already present, do you want to overwrite it?"))
	 {
		 addChairPerson();
		 
	 }
	 else{
		 console.log("not added");
		 
	 }
}

function addChairPerson()
{
	 $.ajax({
            url: '/wtms/application/ajax-addChairPersonName',
            type: "GET",
            data: {
            	name: $('#name').val()
            },
            dataType : 'json'
        });
	 
	 bootbox.alert("Chair person name updated in drop down successfully");
   
	}