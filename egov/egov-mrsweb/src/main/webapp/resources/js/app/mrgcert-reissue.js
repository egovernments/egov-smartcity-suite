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

$(document).ready( function () {
	var isonline = $('#isonline').val();
	
	//Added to avoid submitting parent form on Preview button click
	//Used to preview certificate in case of Digital signture
	$("#Preview").toggleClass('btn-primary btn-default');
	$("#Preview").prop('type','button');
	
	 if($('#reIssueStatus').val()=='APPROVED' && $("#feeCollected").val()=='false'){
		 $("[id='Print Certificate']").hide();
	 }  
	 
	if($('#reIssueStatus').val()=='CREATED' || $('#reIssueStatus').val()=='APPROVED'){  
		$(".show-row").hide(); 
		$('#approverDetailHeading').hide();
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
	} else {
		$(".show-row").show(); 
		$('#approverDetailHeading').show();
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
	}
	
	if(($('#reIssueStatus').val()=='CREATED' && $('#nextActn').val() != 'Junior/Senior Assistance approval pending') || $('#reIssueStatus').val()=='APPROVED'){ 
        $(".show-row").hide();
        $('#approverDetailHeading').hide();
        $('#approvalDepartment').removeAttr('required');
        $('#approvalDesignation').removeAttr('required');
        $('#approvalPosition').removeAttr('required');
    } else {
        $(".show-row").show();
        $('#approverDetailHeading').show();
        $('#approvalDepartment').attr('required', 'required');
        $('#approvalDesignation').attr('required', 'required');
        $('#approvalPosition').attr('required', 'required');
    }


	$('.slide-history-menu').click(function(){
		$(this).parent().find('.history-slide').slideToggle();
		if($(this).parent().find('#toggle-his-icon').hasClass('fa fa-angle-down'))
		{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
			//$('#see-more-link').hide();
			}else{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
			//$('#see-more-link').show();
		}
	});
	
	$('#select-registrationunit').change( function () { 
		var url;
			url='/mrs/registration/getmrregistrationunitzone'
		showRegistrationUnit(url); 
	})
	
	$('#Preview').click(function() {
		var url = '/mrs/reissue/viewCertificate/'+ $('#reIssue').val();
		window.open(url);
	});
	
	function showRegistrationUnit(url)
	{
	 if ($('#select-registrationunit').val() === '') {
	 	$('#txt-zone').val('');
				return;
			} else {
			
				$.ajax({
					type: "GET",
					url: url,
					cache: true,
					dataType: "json",
					data:{
						'registrationUnitId' : $('#select-registrationunit').val()
						}
				}).done(function(value) {
										/*if (value == 0)
											$('#ttxt-zone').val('');
										else*/
					$('#txt-zoneid').val(value.id);
					$('#txt-zone').val(value.name);
				
				});
			}
		
	}
	
})

$('#txt-phoneNo').blur( function () {
	 var mobileno = jQuery("#txt-phoneNo").val();
		if (mobileno.length < 10) {
			bootbox.alert("Please enter 10 digit mobile number");
			$(this).val('');
		}
	})

jQuery('form').validate({
	    ignore: ".ignore",
	    invalidHandler: function(e, validator){
	        if(validator.errorList.length)
	        	$('#settingstab a[href="#' + jQuery(validator.errorList[0].element).closest(".tab-pane").attr('id') + '"]').tab('show');
	    }
	});
	
	$('#Forward').click(function(e){
		if($('form').valid()){
			
		}else{
			e.preventDefault();
		}
	});

$(".btn-primary").click(function() { 
	var action = $('#workFlowAction').val();
	if(action == 'Reject') { 
		 $('#Reject').attr('formnovalidate','true');
		 var r = confirm("Do You Really Want to Reject The ReIssue!");
		 if (r == true) {
			 var approvalComent=$('#approvalComent').val();
			  if(approvalComent == "") {
				  bootbox.alert("Please enter rejection comments!");
					$('#approvalComent').focus();
					return false; 
			  }
			  else {
				  validateWorkFlowApprover(action);
				  //document.forms[0].submit(); 
			  }
		 } else {
		     return false;
		 }
	}else if(action == 'Cancel ReIssue') { 
		 $('#Cancel Registration').attr('formnovalidate','true');
		 var r = confirm("Do You Really Want to Cancel The ReIssue!");
		 if (r == true) {
			 var approvalComent=$('#approvalComent').val();
			  if(approvalComent == "") {
				  bootbox.alert("Please enter cancellation comments!");
					$('#approvalComent').focus();
					return false; 
			  }
			  else {
				  validateWorkFlowApprover(action);
				  //document.forms[0].submit();
			  }
		 } else {
		     return false;
		 }
	} else{
		validateWorkFlowApprover(action);
	}
	
	  
});