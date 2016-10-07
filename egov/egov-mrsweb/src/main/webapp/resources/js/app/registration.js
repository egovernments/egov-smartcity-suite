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
	$('body').on('click', 'img.attach-photo', function () {
	    
		var img = $(this);
		
	    var inputPhoto = document.createElement('INPUT');
		inputPhoto.setAttribute('type', 'file');
	
		inputPhoto.onchange = function() {
	
			var image = $(inputPhoto).prop('files')[0];
			var fileReader = new FileReader();
	
			fileReader.onload = function(e) {
	           var imgData = e.target.result; 
	           $(img).prop('src', imgData);
	           $($(img).siblings('input')).val(imgData);
			}
	       
			fileReader.readAsDataURL(image);
			var span = $(img).siblings('span');
			//$(span).css({'color' : ''});
			$(span).removeClass('error-msg');
			$(span).text(image.name);
		}
		
		$(inputPhoto).trigger('click');
	});
	
	if($('#registrationStatus').val()=='Created' || $('#registrationStatus').val()=='Approved'){  
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
	


	// Showing the respective tab when mandatory data is not filled in
	$('div.tab-content input').bind('invalid', function(e) {
	    if (!e.target.validity.valid) {
	    	var elem = $(e.target).parents( "div[id$='-info']" )[0];
	    	
	    	$('.nav-tabs-top li.active').removeClass('active')
	    	$('.nav-tabs-bottom li.active').removeClass('active')    	
	    	
	    	if (elem != undefined || elem != null) {
	    		$('.nav-tabs-top a[href="#'+elem.id+'"]').parent().addClass('active');
	        	$('.nav-tabs-bottom a[href="#'+elem.id+'"]').parent().addClass('active');
	        	$('div[id$="-info"].active').removeClass('in active');
	        	$('div#' + elem.id).addClass('in active');	
	    	}
	    	
	    	console.log('target' + e.target.id);
	    	var imgAttach = e.target.id;
	    	
	    	/*if (imgAttach.search('.photo') > 0) {
	    		var span = $(e.target).siblings('span'); 
		    	//$(span).css({'color' : 'red'});
		    	$(span).addClass('error-msg');
		    	$(span).text("Photo is required");
	    	}	 */   		    	
	    }  
	});

	$("input[id$='religionPractice1']").prop("checked", true);
	
	$('#table_search').keyup(function(){
    	$('#registration_table').fnFilter(this.value);
    });
	
	
	/*$('#btn_viewdetails').click( function () {
		console.log('registrationId = ' + registrationId);
		window.open('/mrs/registration/' + registrationId + '?mode=view');
	})
	
	$('#btn_collectfee').click( function () {
		window.open('/mrs/collection/bill/' + registrationId);
	})*/
	
	$('#select-marriagefees').change( function () {
		$('#txt-feepaid').val($(this).val());
		$('#txt_feecriteria').val($('#select-marriagefees option:selected').text());
	})
	
	$('input[id$="email"]').blur(function() {
		var pattern = new RegExp("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
		var email = $(this).val();
		if (!pattern.test(email) && $(this).val().length > 0) {
			var span = $(this).siblings('span'); 
	    	$(span).addClass('error-msg');
	    	$(span).text('Please enter valid email..!');
			$(this).show();
			$(this).val("");
		} else {
			var span = $(this).siblings('span'); 
			$(span).removeClass('error-msg');
	    	$(span).text('');
		}
	});
	
	$('.month-field').blur( function () {
		var month = parseInt( $(this).val() );
		if (month != null && month != undefined && (month < 0 || month > 12)) {
			bootbox.alert("Invalid month(s)..!!");
			$(this).val('');
		}
	})
	
	$('a[id^="signature"]').click( function () {
		var content = $( $(this).siblings('input[type="hidden"]') ).val();
		var value = "data:image/jpeg;base64," + content ;
		var link = document.createElement('a');
		link.href = toBinaryString(value);
		link.download = 'signature.jpg';
		link.click();		
	})
	
})

function validateChecklists() {
	var noOfCheckboxes1 = $('input[id^="ageProofH"]:checked').length;
	var noOfCheckboxes2 = $('input[id^="ageProofW"]:checked').length;

	if (noOfCheckboxes1 == undefined || noOfCheckboxes1 == null || noOfCheckboxes1 == 0) {
		bootbox.alert("Any one Age Proof for Husband is mandatory");
		$('#ageProofHLC').focus();
		return false;
	}

	if (noOfCheckboxes2 == undefined || noOfCheckboxes2 == null || noOfCheckboxes2 == 0) {
		bootbox.alert("Any one Age Proof for Wife is mandatory");
		$('#ageProofWLC').focus();
		return false;
	}

	var noOfResProofCheckboxes1 = $('input[id^="resProofH"]:checked').length;
	var noOfResProofCheckboxes2 = $('input[id^="resProofW"]:checked').length;

	if (noOfResProofCheckboxes1 == undefined || noOfResProofCheckboxes1 == null || noOfResProofCheckboxes1 == 0) {
		bootbox.alert("Any one Residence Proof for Husband is mandatory");
		return false;
	}

	if (noOfResProofCheckboxes2 == undefined || noOfResProofCheckboxes2 == null || noOfResProofCheckboxes2 == 0) {
		bootbox.alert("Any one Residence Proof for Wife is mandatory");
		return false;
	}
	
	return true;
}

function removeMandatory() {
	if($('#registrationStatus').val()=='Created'){  
		$('#husband-photo').removeAttr('required');
		$('#wife-photo').removeAttr('required');
	}
}


$(".btn-primary").click(function() { 
	removeMandatory();
	var action = $('#workFlowAction').val();
	if(action == 'Reject') { 
		 $('#Reject').attr('formnovalidate','true');
		 var r = confirm("Do You Really Want to Reject The Registration!");
		 if (r == true) {
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
		 } else {
		     return false;
		 }
	}
	
	 if(action == 'Cancel Registration') { 
		 $('#Cancel Registration').attr('formnovalidate','true');
		 var r = confirm("Do You Really Want to Cancel The Registration!");
		 if (r == true) {
			 var approvalComent=$('#approvalComent').val();
			  if(approvalComent == "") {
				  bootbox.alert("Please enter cancellation comments!");
					$('#approvalComent').focus();
					return false; 
			  }
			  else {
				  validateWorkFlowApprover(action);
				  document.forms[0].submit();
			  }
		 } else {
		     return false;
		 }
	} 
	
	validateWorkFlowApprover(action);  
	
	/*if ($('form').valid()) {
		document.forms[0].submit();	
	} else {
		e.stopPropagation();
		e.preventDefault();
	}*/
	
});