/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
$(document).ready(function()
{
	
	var typeOfConnection=$('#typeOfConnection').val();
    if (typeOfConnection=="CHANGEOFUSE"){
    	$("#waterSourceDropdown").prop("disabled", true);
    	$("#connectionCategorie").prop("disabled", true);
    }
	var status=$('#statuscode').val();
	var wfstate=$('#wfstate').val();
	var currentUser=$('#currentUser').val();
	var mode =$('#mode').val();
	$('#approvalComent').show();
	var closerConnection=$('#closerConnection').val();
	var approvalPositionExist=$('#approvalPositionExist').val();
	if(approvalPositionExist!=0 && ((status=='CREATED' && wfstate!=null )|| status=='VERIFIED' || status=='ESTIMATIONNOTICEGENERATED' ||status=='ESTIMATIONAMOUNTPAID' || status=='WORKORDERGENERATED' || status=='APPROVED'))
		{
		$(".show-row").hide(); 
		$('#approverDetailHeading').hide();
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
		}
	if(approvalPositionExist!=0 && ((mode=='' || mode=='closereditForAE' || mode=='reconEditForAE')&& closerConnection !=null ))
	{
	$(".show-row").hide(); 
	$('#approverDetailHeading').hide();
	$('#approvalDepartment').removeAttr('required');
	$('#approvalDesignation').removeAttr('required');
	$('#approvalPosition').removeAttr('required');
	}
	if(approvalPositionExist!=0 && (mode=='closeredit' && closerConnection !=null ))
	{
		$(".show-row").show(); 
		$('#approverDetailHeading').show();
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
	}
	
	 if(approvalPositionExist!=0 && status=='CREATED' && (mode =='edit' || mode==''))
		{
			$(".show-row").show(); 
			$('#approverDetailHeading').show();
			$('#approvalDepartment').attr('required', 'required');
			$('#approvalDesignation').attr('required', 'required');
			$('#approvalPosition').attr('required', 'required');
		}
	 
		if(approvalPositionExist=='' || approvalPositionExist==0){
		$(".show-row").show(); 
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
		}
		if(status=='ESTIMATIONNOTICEGENERATED')
		{
			$("#approverDetailHeading").hide(); 
			$(".btn-primary").hide();
			$("#button2").show();
			
		}
		
		
		/*if($('#validationMessage').val()!='')
			bootbox.alert($('#validationMessage').val());*/
		
		function validateDateRange(fromDate, toDate) {
			if (fromDate != "" && toDate != "") {
				var stsplit = fromDate.split("/");
				var ensplit = toDate.split("/");
				
				startDate = Date.parse(stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2]);
				endDate = Date.parse(ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2]);
				
		        // Check the date range, 86400000 is the number of milliseconds in one day
		        var difference = (endDate - startDate) / (86400000 * 7);
		        if (difference < 0) {
					return false;
					} 
		        else {
					return true;
				}	
		    }
		    return true;		
		}
		$("form").submit(function() {
			if($('form').valid())	{
				$('.loader-class').modal('show', {backdrop: 'static'});
			}
			else 
				$('.loader-class').modal('hide');
		});
		
		$(".btn-primary").click(function() { 
			var action = document.getElementById("workFlowAction").value;
			 var status=$('#statuscode').val();
			 if((action == 'Generate Estimation Notice' && status=='VERIFIED') || (action == 'Generate WorkOrder' && status=='APPROVED')) {
				 document.forms[0].submit();	
			 }
			 if((action == 'Forward' && status=='CLOSERINITIATED')) {
				 document.getElementById("mode").value=mode;
				 document.forms[0].submit();	
			 }
			  if(status=='CLOSERINITIATED' && action == 'Reject' ) {
				 
				 $('#Reject').attr('formnovalidate','true');	
				 var approvalComent=$('#approvalComent').val();
				  if(approvalComent == "") {
					  bootbox.alert("Please enter rejection comments!");
						$('#approvalComent').focus();
						return false;
				  }
				  else {
					  validateWorkFlowApprover(action);
					  document.forms[0].submit();
				  }
			  }
			  
			  if(status=='RECONNECTIONINITIATED' && action == 'Reject' ) {
					 
					 $('#Reject').attr('formnovalidate','true');	
					 var approvalComent=$('#approvalComent').val();
					  if(approvalComent == "") {
						  bootbox.alert("Please enter rejection comments!");
							$('#approvalComent').focus();
							return false;
					  }
					  else {
						  validateWorkFlowApprover(action);
						  document.forms[0].submit();
					  }
				  }
			 if(status=='CREATED' && action == 'Submit' && mode == 'fieldInspection') {
				 $('#approvalComent').removeAttr('required');	
	    		 if($('form').valid())	{
 					var estimationCharge = $('#estimationCharges').val();
 			    	if(estimationCharge < 0) {
 			    		bootbox.alert("Please enter the value greater than ZERO for Estimation charges!");
 			    		$('#estimationCharges').focus();
 			    		return false;
 			    	}
 			    	else {
 			    		validateWorkFlowApprover(action);
 			    		document.forms[0].submit(); 
	    			}
	    		 }else{
	    			 setTimeout(function(){ 
	    				 off = ( $(document).scrollTop() - 140);
	    				 $('html, body').animate({scrollTop: off }, 0);
    		    	 }, 100);
	    		 }	    			 
			 }
			 else if(status=='WORKORDERGENERATED' && action=='Execute Tap') {
				 validateTapExecutionDate();
			 }
			 else if(status=='CREATED' && action == 'Reject' && mode == 'fieldInspection') {
				 
				 $('#pipelineDistance').val(0);			
				 if($('#estimationCharges'))
					 $('#estimationCharges').val(0);
				 
				 $('#Reject').attr('formnovalidate','true');	
				 var approvalComent=$('#approvalComent').val();
				  if(approvalComent == "") {
					  bootbox.alert("Please enter rejection comments!");
						$('#approvalComent').focus();
						return false;
				  }
				  else {
					  validateWorkFlowApprover(action);
					  document.forms[0].submit();
				  }
			 }
			 else {
			    document.getElementById("mode").value=mode;
			    var applicationDate = $('#applicationDate').html();
			    var approvalDate = $('#approvalDate').val();
				if(status=='ESTIMATIONAMOUNTPAID' && action=='Approve' ) {
					if(approvalDate =='') {
						$('#approvalDate').attr('required', 'required');	
					}
					if(applicationDate !='' && approvalDate != '') {
						if($('form').valid())	{
							if(!validateDateRange(applicationDate, approvalDate)) {
								bootbox.alert("The Approval Date can not be less than the Date of Application.");
								$('#approvalDate').val('');
								return false;
							}
							else{
						    	validateWorkFlowApprover(action);
							    document.forms[0].submit();	
							}	
						}
					}
			    }
		    	else {
		    		validateWorkFlowApprover(action);
		    		if($('form').valid())
		    			document.forms[0].submit();	
		    	}
			 }
			return;
		});	
		
		changeCategory();	
		function changeCategory() {
			if ($('#connectionCategorie :selected').text().localeCompare("BPL") == 0) {  
				$("#cardHolderDiv").show();
		    	$("#bplCardHolderName").attr('required', 'required');
		    	$("#bplCardHolderName").val();
			}
			else  {
				$("#cardHolderDiv").hide();
		    	$("#bplCardHolderName").removeAttr('required');
		    	$("#bplCardHolderName").val(null);
			}
		}
		$('#connectionCategorie').change(function(){
			changeCategory();
		});
		changeLabel();
		function changeLabel() {
			if ($('#usageType :selected').text().localeCompare("Lodges") == 0) {
				$('#persons').hide();
				$('#rooms').show();
				$('#personsdiv').hide();
				$('#roomsdiv').show();
				$('#numberOfPerson').val(null);
			}
			else {
				$('#persons').show();
				$('#rooms').hide();
				$('#personsdiv').show();
				$('#roomsdiv').hide();
				$('#numberOfRooms').val(null);
				
			}
		}
		
		$('#usageType').change(function () {
			changeLabel();
		});
		
		$('#executionDate').blur(function (){
			validateTapExecutionDate();
		});
		
		function validateTapExecutionDate() {
			var applicationDate = $('#appDate').val();	 
			var executionDate = $('#executionDate').val();
			if(applicationDate !='' && executionDate != '') {
				if(validateDateRange(applicationDate, executionDate)) {
					validateWorkFlowApprover(action);
			    	document.forms[0].submit();	
				}
				else{
					bootbox.alert("The Execution Date can not be less than the Date of Application.");
					$('#executionDate').val('');
					return false;	
				}
			}
		}
		if($('#meterFocus').val()== 'true') {
			$('#meterSerialNumber').focus();
			   }
});