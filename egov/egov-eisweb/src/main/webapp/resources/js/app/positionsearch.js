/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

$(document).ready(function(){
	tableContainer1 = $("#position-table"); 
	
	$('#positionSearch').click(function(){
		callajaxdatatable();
		
	});
	
	function callajaxdatatable(){

		tableContainer1.dataTable({
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
