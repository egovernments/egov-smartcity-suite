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
	
	// Instantiate the Bloodhound suggestion engine
	var designation = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/eis/employee/ajax/designations?designationName=%QUERY',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data, function (designation) {
					return {
						name: designation.name,
						value: designation.id
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	designation.initialize();
	// Instantiate the Typeahead UI
	var typeaheadobj =$('#designationName').typeahead({
		  hint: false,
		  highlight: false,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: designation.ttAdapter()
	});
	typeaheadWithEventsHandling(typeaheadobj, '#designationId'); 
	
	
	function validateAssignment() {
		var deptId = $("#deptId").val();
		var desigId =$("#designationId").val();
		var fromDate = $("#fromDate").val();
		var toDate = $("#toDate").val();
		var posId = $("#positionId").val();
		var hoddept = (null!=$("#hodDeptId").val() || 'undefined'!=$("#hodDeptId").val())?$("#hodDeptId").val():null;
		var validate = true;
		if($("#isHodYes").prop("checked") && hoddept==null){
			$('.hoderror').html('HOD Department is required').show();
			validate = false;	
		}
		else{
			$('.hoderror').hide();
		}
		
		if(null==deptId || ''==deptId){
			$('.departmenterror').html('Department is required').show();
			validate = false;
		}
		if(null==desigId || ''==desigId) {
			$('.designationerror').html('Designation is required').show();
			validate = false;
		}
		if(null==fromDate || ''==fromDate) {
			$('.fromdateerror').html('From Date is required').show();
			validate = false;
		}
		if(null==toDate || ''==toDate) {
			$('.todateerror').html('To Date is required').show();
			validate = false;
		}
		if(null==posId || ''==posId){
			$('.positionerror').html('Position is required').show();
			validate = false;
		}
		return validate;
	}
	
	$("#deptId").blur(function (){
		var deptId = $("#deptId").val();
		$("#positionId").val("");
		$("#positionName").val("");
		if(null!=deptId || ''!=deptId){
			$('.departmenterror').hide();
		}
	});
	
	$("#designationName").blur(function (){
		var desigId = $("#designationName").val();
		$("#positionId").val("");
		$("#positionName").val("");
		if(null!=desigId || ''!=desigId){
			$('.designationerror').hide();
		}
		else
			$("#designationId").val("");
	});
	
	$("#positionName").blur(function (){
		var posId = $("#positionName").val();
		if(null!=posId || ''!=posId){
			$('.positionerror').hide();
		}
		else
			$("#positionId").val("");
	});
	
	$("#fromDate").blur(function (){
		var fromDate = $("#fromDate").val();
		if(null!=fromDate || ''!=fromDate){
			$('.fromdateerror').hide();
		}
	});
	
	$("#toDate").blur(function (){
		var toDate = $("#toDate").val();
		if(null!=toDate || ''!=toDate){
			$('.todateerror').html('To Date is required').hide();
		}
	});
	
	$("#isHodNo").blur(function (){
		$("#hoderror").hide();
	});
	
	function validateDateRange() {

		if($("#fromDate").val() != '' && $("#toDate").val() != ''){
			var start = $("#fromDate").val();
			var end = $("#toDate").val();
			var stsplit = start.split("/");
			var ensplit = end.split("/");

			start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
			end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];


			var startDate = Date.parse(start);
			var endDate = Date.parse(end);

			// Check the date range, 86400000 is the number of milliseconds in one day
			var difference = (endDate - startDate) / (86400000 * 7);
			if (difference < 0) {
				bootbox.alert("From date  should not be greater than the To Date.");
				$('#toDate').val('');
				return false;
			} else {
				return true;
			}
			return true;
		}
	}
	
	
	
	//Position auto-complete
	
	var positions = new Bloodhound(
		{
			datumTokenizer : function(datum) {
				return Bloodhound.tokenizers
						.whitespace(datum.value);
			},
			queryTokenizer : Bloodhound.tokenizers.whitespace,
			remote : {
				url : '/eis/employee/ajax/positions',
				replace: function(uri, uriEncodedQuery) {
					return uri + '?positionName='+uriEncodedQuery+'&deptId='+ $("#deptId").val()+'&desigId='+$("#designationId").val()+'&fromDate='+$("#fromDate").val()+'&toDate='+$("#toDate").val()+'&primary='+$("#primary_yes").prop("checked");
					
				},
				filter : function(data) {
					// Map the remote source JSON array to a
					// JavaScript object array
					return $.map(data, function(position) {
						return {
							name: position.name,
							value: position.id
						};
					});
				}
			}
		});
		positions.initialize();
		
	
	var typeaheadobj = $('#positionName').typeahead({
		hint: false,
		highlight: false,
		minLength: 1
		}, {
		displayKey: 'name',
		source: positions.ttAdapter()
		});
	
	typeaheadWithEventsHandling(typeaheadobj, '#positionId'); 
	
	$("#positionName").focus(function() {
		validateAssignment();
		$('.positionerror').hide();
		positions.initialize();
	});
	
	var rowCount=0;
	var edit=false;
	var deleteRow="";
	var editedRowIndex="";
	
	$("#btn-add").click(function() {
		var primary = $("#primary_yes").prop("checked");

		if($("#mode").val()=="update") {
			$("#fromDate").prop('disabled', false);
			$('#fromDate').addClass( "form-control datepicker" );
		}

		if(validateAssignment() && validateDateRange()) {
			if(!edit){
				if(primary==true){
					validatePrimaryPosition(edit);
				}
				else{
				rowCount = $("#assignmentTable tr").length;
				addRow(rowCount);
				rowCount++;
				resetAssignmentValues();
				}
			}
			else{		
				if(primary==true ){	
					 validatePrimaryPosition(edit);
					 edit=false;
				}
				else{
					deleteRow.remove();
					addRow(editedRowIndex);		
					edit=false;
					resetAssignmentValues();
				}
			}
			
		}	
	});
	
	function validatePrimaryPosition(edit)
	{		
		$.ajax({
			url: '/eis/employee/ajax/primaryPosition',
			type: "GET",
			data: {
				positionId : $("#positionId").val(),
				assignmentId : $("#editassignIds").val(), 
				code : $("#code").val(),
				fromDate : $("#fromDate").val(),
				toDate : $("#toDate").val()
			},
			dataType : 'json',
			success: function (response) {
				if(response != ""){
					response = response.substring(0,response.length-1);
					bootbox.alert("Assignment overlaps with existing primary assignment of employee "+response);
					edit=false;
				}
				else if(edit){	
					deleteRow.remove();
					addRow(editedRowIndex);		
					edit=false;
				}
				else{
					rowCount = $("#assignmentTable tr").length;
					addRow(rowCount);
					rowCount++;
				}
				resetAssignmentValues();

			},error: function (response) {
				console.log("failed");
			}
		});
	}
	
	function resetAssignmentValues() {
		if(!edit) {
			$("#primary_yes").prop("checked",true);
			$("#primary_no").prop("checked",false);
			$("#fromDate").val("");
			$("#toDate").val("");
			$("#deptId").val("");
			$("#designationName").val("");
			$("#designationId").val("");
			$("#positionId").val("");
			$("#positionName").val("");
			$("#fundId").val("");
			$("#functionId").val("");
			$("#functionaryId").val("");
			$("#gradeId").val("");
			$("#isHodYes").prop("checked",false);
			$("#isHodNo").prop("checked",true);
			$("#hodDeptId").find('option').attr('selected', false);
			$('#hodDeptDiv').hide();
		}	
	}
	
	
	function addRow(index) {
		var fund = (null!=$("#fundId").val() || 'undefined'!=$("#fundId").val())?$("#fundId").val():null;
		var ftn = (null!=$("#functionId").val() || 'undefined'!=$("#functionId").val())?$("#functionId").val():null;
		var functionary = (null!=$("#functionaryId").val() || 'undefined'!=$("#functionaryId").val())?$("#functionaryId").val():null;
		var grade = (null!=$("#gradeId").val() || 'undefined'!=$("#gradeId").val())?$("#gradeId").val():null;
		$('.hoderror').hide;
		var hoddept = (null!=$("#hodDeptId").val() || 'undefined'!=$("#hodDeptId").val())?$("#hodDeptId").val():null;
		var result = $('#hodDeptId :selected').map(function(i, opt) {
			  return $(opt).text();
			}).toArray().join(', ');
	   var hoddeptname = result.split(",");
		
		var hodInput="";
		var hodname = "";
		var hodDepartment = "";
		if(null!=hoddeptname){
			for(var i=0;i<hoddeptname.length;i++) {
				
				hodname = hodname+'<input type="text" id="assignments['+index+'].hodDept['+i+'].hod" name="assignments['+index+'].hodDept['+i+'].hod" value="'+hoddeptname[i]+'"/>';
			}
			hodname = hodname+'<input type="hidden" id="hodNames'+index+'" value="'+hoddeptname+'"/>';
		}
		if(null!=hoddept){
			for(var i=0;i<hoddept.length;i++) {
				hodInput = hodInput+'<input type="hidden" id="assignments['+index+'].deptSet['+i+'].hod" name="assignments['+index+'].deptSet['+i+'].hod" value="'+hoddept[i]+'"/>';
				hodDepartment = hodDepartment+'<input type="hidden" id="assignments['+index+'].hodList['+i+'].hod" name="assignments['+index+'].hodList['+i+'].hod" value="'+hoddept[i]+'"/>';

			}
			hodInput = hodInput+'<input type="hidden" id="hodIds'+index+'" value="'+hoddept+'"/>';
		}
		
		var del="";
		  del='<span class="add-padding"><i id="delete_row" class="fa fa-remove"  value="'+index+'"></i></span>';
		var text = 
					'<tr>'+
						'<td>'+
							'<input type="hidden" id="assignments['+index+'].fromDate" name="assignments['+index+'].fromDate" '+
							'value="'+$("#fromDate").val()+'"/>'+
							'<input type="hidden" id="assignments['+index+'].toDate" name="assignments['+index+'].toDate" '+
							'value="'+$("#toDate").val()+'"/>'+
							'<input type="text" id="table_date_range'+index+'" class="form-control" readonly="readonly" style="text-align:center"/>'+
						'</td>'+	
						'<td>'+
							'<input type="hidden" class="isPrimary" id="assignments['+index+'].primary" name="assignments['+index+'].primary" '+ 
							'value="'+$("#primary_yes").prop("checked")+'"/>'+
							'<input type="text" id="table_primary'+index+'" class="form-control checkPrimary" readonly="readonly" style="text-align:center"/>'+ 
						'</td>'+	
						'<td>'+
							'<input type="hidden" id="assignments['+index+'].department" name="assignments['+index+'].department" '+
							'value="'+$("#deptId").val()+'"/>'+
							'<input type="text" id="table_department'+index+'" class="form-control" readonly="readonly" style="text-align:center"/>'+
						'</td>'+	
						'<td>'+
							'<input type="hidden" id="assignments['+index+'].designation" name="assignments['+index+'].designation" '+
							'value="'+$("#designationId").val()+'"/>'+
							'<input type="text" id="table_designation'+index+'" class="form-control" readonly="readonly" style="text-align:center"/>'+
						'</td>'+	
						'<td>'+	
							'<input type="hidden" id="assignments['+index+'].position" name="assignments['+index+'].position" '+
							'value="'+$("#positionId").val()+'"/>'+
							'<input type="text" id="table_position'+index+'" class="form-control" readonly="readonly" style="text-align:center"/>'+
							'<input type="hidden" id="assignments['+index+'].fund" name="assignments['+index+'].fund" '+
							'value="'+fund+'"/>'+
							'<input type="hidden" id="assignments['+index+'].function" name="assignments['+index+'].function" '+
							'value="'+ftn+'"/>'+
							'<input type="hidden" id="assignments['+index+'].functionary" name="assignments['+index+'].functionary" '+
							'value="'+functionary+'"/>'+
							'<input type="hidden" id="assignments['+index+'].grade" name="assignments['+index+'].grade" '+
							'value="'+grade+'"/>' +
						'</td>'+
						'<td>'+	
						hodInput +
						hodname +
						hodDepartment +
					'</td>'+
						'<td>'+	
							'<span class="add-padding"><i id="edit_row" class="fa fa-edit" value="'+index+'"></i></span>'+del+
						'</td>'+	
					'</tr>';	
		
		$("#assignmentTable").append(text);
		$("#table_date_range"+index+"").val($("#fromDate").val() + " - "+$("#toDate").val());
		$("#table_primary"+index+"").val($("#primary_yes").prop("checked")?"Yes":"No");
		$("#table_department"+index+"").val($("#deptId").find('option:selected').text());
		$("#table_designation"+index+"").val($("#designationName").val());
		$("#table_position"+index+"").val($("#positionName").val());
		
	}
	
	$(document).on('click',"#delete_row",function (){
		if(!$("#removedassignIds").val()==""){
			$("#removedassignIds").val($("#removedassignIds").val()+",");
		}
		if($("#table_assignid"+$(this).attr("value")+"").val()!=undefined){
		 $("#removedassignIds").val($("#removedassignIds").val()+$("#table_assignid"+$(this).attr("value")+"").val());
		}
		$(this).closest('tr').remove();
	});
	
	
	$(document).on('click',"#edit_row",function (){

		if($("#mode").val()=="update") {
			$("#fromDate").prop('disabled', true);
			$('#fromDate').removeClass( "datepicker" );
		}
		
		
		if($("#table_assignid"+$(this).attr("value")+"").val()!=undefined){
		 $("#editassignIds").val($("#table_assignid"+$(this).attr("value")+"").val());
		}
		
		edit = true;
		deleteRow = $(this).closest('tr');
		editedRowIndex =$(this).attr("value");
		var hodInput="";
		var hoddept = $("#hodDeptIds"+editedRowIndex).val() ;
		var primary = document.getElementById("assignments["+editedRowIndex+"].primary").value;
		var fromDate = document.getElementById("assignments["+editedRowIndex+"].fromDate").value;
		var toDate = document.getElementById("assignments["+editedRowIndex+"].toDate").value;
		var dept = document.getElementById("assignments["+editedRowIndex+"].department").value;
		var desig = document.getElementById("assignments["+editedRowIndex+"].designation").value;
		var desigName = document.getElementById("table_designation"+editedRowIndex).value;
		var pos = document.getElementById("assignments["+editedRowIndex+"].position").value;
		var posName = document.getElementById("table_position"+editedRowIndex).value;
		var fund = document.getElementById("assignments["+editedRowIndex+"].fund").value;
		var ftn = document.getElementById("assignments["+editedRowIndex+"].function").value;
		var functionary = document.getElementById("assignments["+editedRowIndex+"].functionary").value;
		var grade = document.getElementById("assignments["+editedRowIndex+"].grade").value;
		
	
		if(null!=hoddept && hoddept != "0"){
			for(var i=0;i<hoddept;i++) {
              	hodInput = document.getElementById("assignments["+editedRowIndex+"].deptSet["+i+"].hod").value + "," + hodInput ;		
			}
			
		}else{
			hodInput=(null!=$("#hodIds"+editedRowIndex).val() || 'undefined'!=$("#hodIds"+editedRowIndex).val())?$("#hodIds"+editedRowIndex).val():null;
		}
		if(primary=="true"){
			$("#primary_yes").prop("checked",true);
			$("#primary_no").prop("checked",false);
		}
		if(primary=="false"){
			$("#primary_yes").prop("checked",false);
			$("#primary_no").prop("checked",true);
		}		
		$("#fromDate").val(fromDate);
		$("#toDate").val(toDate);
		$("#deptId").val(dept);
		$("#designationId").val(desig);
		$("#designationName").val(desigName);
		$("#positionId").val(pos);
		$("#positionName").val(posName);
		$("#fundId").val(fund);
		$("#functionId").val(ftn);
		$("#functionaryId").val(functionary);
		$("#grade").val(grade);
		if(hodInput!="" && hodInput!=null) {
			
				var dataArray = hodInput.split(","); 
				$("#hodDeptId").val(dataArray);
				$("#isHodYes").prop("checked",true);
				$("#isHodNo").prop("checked",false);
				$('#hodDeptDiv').show();
				
		}	
		else{
			$("#isHodYes").prop("checked",false);
			$("#isHodNo").prop("checked",true);
			$('#hodDeptDiv').hide();
		}
	});
	
	
	$("#isHodYes").click(function () {
		$('#hodDeptDiv').show();
	});
	
	$("#isHodNo").click(function () {
		$('#hodDeptDiv').hide();
		$("#hodDeptId").find('option').attr('selected', false);
	});
	
	$("#primary_yes").click(function () {
		resetAssignmentValues();
	});
	
	$("#primary_no").click(function () {
		resetAssignmentValues();
		$("#primary_no").prop("checked",true);
		$("#primary_yes").prop("checked",false);
	});
	
	
	$("#isactive_no").click(function () {
		$("#isactive_no").prop("checked",true);
		$("#isactive_yes").prop("checked",false);
	});
	
	function getdate() {
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth() + 1; // January is 0!

		var yyyy = today.getFullYear();
		if (dd < 10) {
			dd = '0' + dd
		}
		if (mm < 10) {
			mm = '0' + mm
		}
		var today = dd + '/' + mm + '/' + yyyy;
		return today;
	}
	
   $('#btnsubmit').click(function(e){
	   
	   if($('form').valid()){
		 
		var count = 0;
		var isactive_no = $("#isactive_no").prop("checked");
		var i=1;
		var length = $("#assignmentTable tr").length;
		if($("#mode").val()=="update") //create
		{
		i=0;
		length = length-1;
		}

			//assignments['+index+'].toDate
        if(($("#assignmentTable tr").length-1)>=1 && isactive_no && ($("#mode").val()=="update")){
        	for(i;i<length;i++) {
        		var date = $("#table_date_range"+i).val();
    			var stsplit = date.split("-");
    			var toDate = stsplit[1];
        		
        		var currDate = getdate();
        		if (dateDifference(currDate, toDate) <= 0) {
        			count++;
        			break;        			
        		} 				
			}			
		}
        if((length == 0) && isactive_no){
			bootbox.alert("Employee should have assignment");
			return false;
        }
        
        var submit = true;

		if(count>0){
			bootbox.confirm("There are assignment/s still open for this user, it may affect the workflow items if any associated with this user.  Do you still want to deactivate the user ?", function (result) {
    			if(result){
    				$("form").submit();
				}
    			else{
    			}
    				
    		});
		}
		else{
		$("form").submit();
		}

	   }
		
   })

   function validateDateRange() {

		if($("#fromDate").val() != '' && $("#toDate").val() != ''){
			var start = $("#fromDate").val();
			var end = $("#toDate").val();
			
			if (dateDifference(start,end) < 0) {
				bootbox.alert("From date  should not be greater than the To Date.");
				$('#toDate').val('');
				return false;
			} else {
				return true;
			}
			return true;
		}
	}
	
   function dateDifference(start,end){
		var stsplit = start.split("/");
		var ensplit = end.split("/");

		start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
		end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];


		var startDate = Date.parse(start);
		var endDate = Date.parse(end);

		// Check the date range, 86400000 is the number of milliseconds in one day
		var difference = (endDate - startDate) / (86400000 * 7);
		return difference;
	
	}
	
	function validateJurisdiction() {
		$('.boundaryTypeerror').hide();
		$('.boundaryerror').hide();
		$('.duplicatejurisdictionerror').hide();
		
		var i=1;
		var length = $("#jurisdictionTable tr").length;
		if($("#mode").val()=="update")
		{
		 i=0;
		 length = length - 1 ;
		}

			if(($("#jurisdictionTable tr").length-1)>=1){		
				for(i;i<length;i++) {
					if(($('#table_boundaryType'+i).val()).localeCompare($("#boundaryTypeId").find('option:selected').text())==0){
						if(($('#table_boundary'+i).val()).localeCompare($("#boundarySelect").find('option:selected').text())==0){
							$('.duplicatejurisdictionerror').html('Already this jurisdiction combination exist').show();
							return false;
						}
					}
				}
			}
		
		$('.boundaryTypeerror').hide();
		$('.boundaryerror').hide();
		var boundaryTypeId = $("#boundaryTypeId").val();
		var boundaryId =$("#boundarySelect").val();
		var validate = true;
		if(null==boundaryTypeId || ''==boundaryTypeId){
			$('.boundaryTypeerror').html('Boundary Type is required').show();
			validate = false;
		}
		if(null==boundaryId || ''==boundaryId) {
			$('.boundaryerror').html('Boundary is required').show();
			validate = false;
		}
		return validate;
	}
	var jurdctnrowCount=0;
	var jurdctnedit=false;
	var jurdctndeleteRow="";
	var jurdctneditedRowIndex="";
	$("#btn-addJurdctn").click(function() {
		if(validateJurisdiction()) {
			if(!jurdctnedit){
				jurdctnrowCount = $("#jurisdictionTable tr").length;
				if($("#mode").val()=="update") 
					jurdctnrowCount = jurdctnrowCount-1;
				jurdctnaddRow(jurdctnrowCount);
				jurdctnrowCount++;
			}
			else{
				jurdctndeleteRow.remove();
				jurdctnaddRow(jurdctneditedRowIndex);		
				jurdctnedit=false;
			}
			resetJurisdictionValues();
		}
	});
	function resetJurisdictionValues() {
		if(!jurdctnedit) {
			$("#boundaryTypeId").val("");
			$("#boundarySelect").val("");
		}	
	}
	function jurdctnaddRow(index) {
		var del="";
			del='<span class="add-padding"><i id="jurdctndelete_row" class="fa fa-remove"></i></span>';
		var text = 
					'<tr>'+
						'<td>'+
							'<input type="hidden" id="jurisdictions['+index+'].boundaryType" name="jurisdictions['+index+'].boundaryType" '+
							'value="'+$("#boundaryTypeId").val()+'"/>'+
							'<input type="text" id="table_boundaryType'+index+'" class="form-control" readonly="readonly" style="text-align:center"/>'+
						'</td>'+	
						'<td>'+
							'<input type="hidden" id="jurisdictions['+index+'].boundary" name="jurisdictions['+index+'].boundary" '+
							'value="'+$("#boundarySelect").val()+'"/>'+
							'<input type="text" id="table_boundary'+index+'" class="form-control" readonly="readonly" style="text-align:center"/>'+
						'</td>'+	
						'<td>'+	
							'<span class="add-padding"><i id="jurdctnedit_row" class="fa fa-edit" value="'+index+'"></i></span>'+del+
						'</td>'+	
					'</tr>';	
		$("#jurisdictionTable").append(text);
		$("#table_boundaryType"+index+"").val($("#boundaryTypeId").find('option:selected').text());
		$("#table_boundary"+index+"").val($("#boundarySelect").find('option:selected').text());
	}

	$(document).on('click',"#jurdctndelete_row",function (){
		if(!$("#removedJurisdictionIds").val()==""){
			$("#removedJurisdictionIds").val($("#removedJurisdictionIds").val()+",");
		}
		if($("#table_jurisdictionid"+$(this).attr("value")+"").val()!=undefined){
		 $("#removedJurisdictionIds").val($("#removedJurisdictionIds").val()+$("#table_jurisdictionid"+$(this).attr("value")+"").val());
		}
		$(this).closest('tr').remove();
	});

	$(document).on('click',"#jurdctnedit_row",function (){
		jurdctnedit = true;
		jurdctndeleteRow = $(this).closest('tr');
		jurdctneditedRowIndex =$(this).attr("value");
		var boundaryType = document.getElementById("jurisdictions["+jurdctneditedRowIndex+"].boundaryType").value;
		var boundary = document.getElementById("jurisdictions["+jurdctneditedRowIndex+"].boundary").value;
		var boundaryTypeName = document.getElementById("table_boundaryType"+jurdctneditedRowIndex).value;
		var boundaryName = document.getElementById("table_boundary"+jurdctneditedRowIndex).value;
		populateboundarySelect({
			boundaryTypeId : boundaryType
		});
		$("#boundaryTypeId").val(boundaryType);
		$("#boundarySelect").val(boundary);
	});

});

function populateBoundary(dropdown) {
	populateboundarySelect({
		boundaryTypeId : dropdown.value
	});
}