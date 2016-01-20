$(document).ready(function(){
	tableContainer1 = $("#position-table"); 
	
	$('#positionSearch').click(function(){
		callajaxdatatable();
		
	});
	
	function callajaxdatatable(){

		tableContainer1.dataTable({
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"ajax": {
	        	url : "/eis/position/resultList-update",
	        	data : {
	        		departmentId : $('#position_dept').val(),
	        		designationId : $('#position_desig').val()
	        	}
	        },
	       	"columns" : [{
				"mData" : "name",
				"sTitle" : "Position"
			},{
				"mData" : "outSourcedPost",
				"sTitle" : "Outsourced Posts"
			},{
				"targets": -1,
				"mData" : null,
				"defaultContent": '<button type="button" class="btn btn-xs btn-secondary edit-position"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>'
				
			},{
				"mData" : "positionId",
				"visible": false
			},{
				"mData" : "outSourcedPostCount",
				"visible": false
			},{
				"mData" : "sanctionedPostCount",
				"visible": false
			},{
				"mData" : "departmentName",
				"visible": false
			},{
				"mData" : "designationName",
				"visible": false
			},{
				"mData" : "isOutSourced",
				"visible": false

			}]
		});
		
	}
	
	
	$('#position-table').on( 'draw.dt', function () {
	/*	var outsourced=0, sanctioned=0;
		var oTable = $('#position-table').dataTable();
		$.each( oTable.fnGetData(), function(i, row){
				if(oTable.fnGetData(i,1) == true)
					outsourced=outsourced+1;
					sanctioned=sanctioned+1;
		})
		
		$("#outSourcedPost").val(outsourced); 
		$("#sanctionedPost").val(sanctioned); */
		 getSanctionAndOutsourcePositions();
	  } );
	
	/*$('#searchposition').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});*/

	function getSanctionAndOutsourcePositions(){
	 $.ajax({
         url: '/eis/position/position-getTotalPositionCount',
         type: 'GET',
         data: {departmentId : $('#position_dept').val(),designationId : $('#position_desig').val()
        	 },
         success: function(data) { 
         	var msg = data.split('/');
         	$("#outSourcedPost").val(msg[0]); 
    		$("#sanctionedPost").val(msg[1]); 	
         //	bootbox.alert(msg(0)+msg(1));
         },
         error: function() {
        	 bootbox.alert("Internal server error occurred, please try after sometime.");
         }
 }); 
	}
	 
	
	
	$('#position-table').on('click', 'tbody tr', function(event){
		if($(event.target).attr('class') ==  'btn btn-xs btn-secondary edit-position')
	    {
			$("#positionname-edit").val(tableContainer1.fnGetData(this,0));
			$("#designationname-edit").val(tableContainer1.fnGetData(this,7));
			$("#departmentname-edit").val(tableContainer1.fnGetData(this,6));
			$("#position-id").val(tableContainer1.fnGetData(this,3));
			if(tableContainer1.fnGetData(this,1) == true){
				$('#outSourced-Yes').prop('checked', true);
			}else{
				$('#outSourced-No').prop('checked', true);
			}
			/*$("#outSourcedPost").val(tableContainer1.fnGetData(this,4)); 
			$("#sanctionedPost").val(tableContainer1.fnGetData(this,5)); */
			$('.position-modal').modal('show');
		}
			
	});
	
	$(".is_valid_letters_space_hyphen_underscore").on("input", function(){
        var regexp = /[^a-zA-Z _0-9_-]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$('#position-form').on('submit', function(e){
	       e.preventDefault();
	       $.ajax({
            url: '/eis/position/position-update',
            type: 'GET',
            data: {'desigName': $("#designationname-edit").val(), 'positionName':$("#positionname-edit").val(),'deptName':$("#departmentname-edit").val(),'isoutsourced':$('#outSourced-Yes').prop('checked'),'positionId':$("#position-id").val()},
            success: function(data) {
            	var msg = "";
            	if (data == "SUCCESS") {
            		msg = "Position has been updated."
            	} else if (data == "POSITIONNAMEALREADYEXIST") {
            		msg = "Position name is already present."
            	} else if (data == "NOCHANGESINEXISTINGNAME") {
            		msg = "No changes with current position name."
            	}else if (data == "POSITIONNAMEISNULL") {
            		msg = "Position name is mandatory"
            	}  
            	
            	if (data == "SUCCESS") {
             		callajaxdatatable();
            	}
            	bootbox.alert(msg);
            },
            error: function() {
            	bootbox.alert("Internal server error occurred, please try after sometime.");
            }, complete : function() {
            	$('.position-modal, .loader-class').modal('hide');
            }
    }); 
	});
	
	
	
});
