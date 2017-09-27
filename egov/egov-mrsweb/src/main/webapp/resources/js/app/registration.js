/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
	var applicationDate=$('#applicationDate').val();


	// remove mandatory (*) on form load for serial no and page no
	$('.validate-madatory').find("span").removeClass( "mandatory" );
	
	//Added to avoid submitting parent form on Preview button click
	//Used to preview certificate in case of Digital signture
	$("#Preview").toggleClass('btn-primary btn-default');
	$("#Preview").prop('type','button');
	
	$( "#select-venue" ).change(function() {
		var venue = $( "#select-venue option:selected" ).text();
		if(venue == 'Residence'){
			$('#txt-placeofmrg').val('');
			$('.toggle-madatory').find("span").removeClass( "mandatory" );
			$('.addremoverequired').removeAttr( "required" );
			$("#txt-placeofmrg").attr("disabled", "disabled");
		}else{
			$('.toggle-madatory').find("span").addClass( "mandatory" );
			$('.addremoverequired').attr( "required", "true" );
			$("#txt-placeofmrg").removeAttr("disabled", "disabled");
		}
		
	});   
	
	//default currentdate in create screen
	if($('#txt-dateOfMarriage').val()=="")
		$('#txt-dateOfMarriage').datepicker('setDate', new Date());
	//Date of marriage shld not be editable in workflow 
	if($('#registrationStatus').val()=='CREATED' || $('#registrationStatus').val()=='APPROVED'){
		$('#txt-dateOfMarriage').attr('disabled', 'disabled');
	}
	// make form elements disabled at final level of workflow (with and without digital signature)	
	if(($('#registrationStatus').val()=='APPROVED' && $('#pendingActions').val()=='Digital Signature Pending') || $('#registrationStatus').val()=='DIGITALSIGNED'){
		$(':input').attr('readonly','readonly');
		$('#form-updateregistration select').attr('disabled', 'true'); 
		$(':checkbox[readonly="readonly"]').click(function() {
			return false;
			});
		$(".file-ellipsis.upload-file").attr('disabled', 'disabled'); 
		$('#approvalComent').removeAttr('readonly');
	} else if($('#registrationStatus').val()=='APPROVED' && $('#pendingActions').val()=='Certificate Print Pending'){
		$(':input').attr('readonly','readonly');
		$('#form-updateregistration select').attr('disabled', 'true'); 
		$(':checkbox[readonly="readonly"]').click(function() {
			return false;
			});
		$(".file-ellipsis.upload-file").attr('disabled', 'disabled'); 
		$('.exclude_readonly_input').removeAttr('readonly');
		$('#approvalComent').removeAttr('readonly');
	}
	
	
	if($('#registrationStatus').val()=='APPROVED' && $("#feeCollected").val()=='false'){ 
		 $("[id='Print Certificate']").hide();
	 }  
	 
	//Removing file attachment validation from workflow and modify screen 
	if($('#registrationStatus').val()!=''){ 
		$(".file-ellipsis.upload-file").removeAttr('required');
	 }  
	
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
			$(span).removeClass('error-msg');
			$(span).text(image.name);
		}
		
		$(inputPhoto).trigger('click');
	});
	
	if($('#registrationStatus').val() != '' && $('#registrationStatus').val() != 'REJECTED'){  
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
	if($('#registrationStatus').val() != '' && ($('#registrationStatus').val() == 'CREATED' && $('#nextActn').val() != 'Junior/Senior Assistance approval pending') && $('#registrationStatus').val() != 'REJECTED'){ 
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
	    	
	    	var imgAttach = e.target.id;
	    }  
	});

	
	$('#table_search').keyup(function(){
    	$('#registration_table').fnFilter(this.value);
    });
	
	
	$('.slide-history-menu').click(function(){
		$(this).parent().find('.history-slide').slideToggle();
		if($(this).parent().find('#toggle-his-icon').hasClass('fa fa-angle-down'))
		{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
			}else{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
		}
	});
	
	$('.slide-document-menu').click(function(){
		$(this).parent().find('.documentAttach-slide').slideToggle();
		if($(this).parent().find('#toggle-his-icon').hasClass('fa fa-angle-down'))
		{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
			}else{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
		}
	});
	
	jQuery('form').validate({
	    ignore: ".ignore",
	    invalidHandler: function(e, validator){
	        if(validator.errorList.length)
	        	$('#settingstab a[href="#' + jQuery(validator.errorList[0].element).closest(".tab-pane").attr('id') + '"]').tab('show');
	    }
	});
	
	// New Marriage Registration Screen
	$('#Forward').click(function(e){
		validateForm(e);
	});
	
	//To render all marriage registration images from second level
	$('.setimage').each(function() {
		var encodedPhoto = $(this).find('.encodedPhoto').val();
		if(encodedPhoto){
			$(this).find('.marriage-img').attr({
				src : "data:image/jpg;base64," + encodedPhoto
			});
			$(this).find('.marriage-img').attr('data-exist', '');
		}
	});
	
	// To show preview of all uploaded images of marriage registration
	$('.upload-file[data-fileto]').change(function(e){
		if(this.files[0].name){
			readURL(this, $(this).data('fileto'));
		}
	});
	
	$('#Preview').click(function() {
		var url = '/mrs/registration/viewCertificate/'+ $('#marriageRegistration').val();
		window.open(url);
	});


var fileformatsinclude = ['jpeg','jpg','png']; 
	
	$('.validate-file').change( function(e) {		
		/*validation for file upload*/
		myfile= $( this ).val();
		var ext = myfile.split('.').pop();
		if($.inArray(ext.toLowerCase(), fileformatsinclude) > -1){
			//do something    
		}
		else{
			bootbox.alert("Please upload  .jpeg, .jpg and .png format documents only");
			$( this ).val('');
			return false;
		}	
		
		var image = $( this ).prop('files')[0];
		var fileReader = new FileReader();
		var inputUpload = $(this);

		fileReader.onload = function(e) {   
           $( $(inputUpload).siblings('img') ).prop('src', e.target.result);
		}
       
		fileReader.readAsDataURL(image);
		
		var fileInput = $(this);
		var maxSize = 2097152; // file size in
								// bytes(2MB)
		var inMB = maxSize / 1024 / 1024;
		if (fileInput.get(0).files.length) {
			var fileSize = this.files[0].size; // in
												// bytes
			var charlen = (this.value
					.split('/').pop().split(
							'\\').pop()).length;
			if (charlen > 50) {
				bootbox
						.alert('File length should not exceed 50 characters!');
				fileInput.replaceWith(fileInput
						.val('').clone(true));
				return false;
			} else if (fileSize > maxSize) {
				bootbox
						.alert('File size should not exceed '
								+ inMB + ' MB!');
				fileInput.replaceWith(fileInput
						.val('').clone(true));
				return false;
			}
		}
	});
	
	if($('#message').val()){
		bootbox.alert($('#message').val());
		return false; 
	}
});
	
	
function readURL(input, imgId) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function (e) {
			$('img[id="'+imgId+'"]').attr('src', e.target.result);
		}

		reader.readAsDataURL(input.files[0]);
	}
}

function validateChecklists() {
	//Passport is assumed to be common proof for both age and residence
	//If passport is not attached then validate for other age and residence proof documents
	if($('#registrationStatus').val()==""){
		var ageAddrProofAttached = false;
		if($('input[id^="indvcommonhusbandPassport"]').val()==""){
			 $('input[type="file"][id^="ageproofhusband"]').toArray().map(function(item) {
				 if(item.value!="")
					 ageAddrProofAttached = true;
			 });
			 if (!ageAddrProofAttached) {
				bootbox.alert("Any one Age Proof for Husband is mandatory");
				return false;
			}
			 
			 ageAddrProofAttached = false;
			 $('input[type="file"][id^="addressproofhusband"]').toArray().map(function(item) {
				 if(item.value!="")
					 ageAddrProofAttached = true;
			 });
			 if (!ageAddrProofAttached) {
				 bootbox.alert("Any one Residence Proof for Husband is mandatory");
					return false;
			 }
		}
		if($('input[id^="indvcommonwifePassport"]').val()==""){
			 ageAddrProofAttached = false;
			 $('input[type="file"][id^="ageproofwife"]').toArray().map(function(item) {
				 if(item.value!="")
					 ageAddrProofAttached = true;
			 });
			 if (!ageAddrProofAttached) {
				bootbox.alert("Any one Age Proof for Wife is mandatory");
				return false;
			}
			 
			 ageAddrProofAttached = false;
			 $('input[type="file"][id^="addressproofwife"]').toArray().map(function(item) {
				 if(item.value!="")
					 ageAddrProofAttached = true;
			 });
			 if (!ageAddrProofAttached) {
				 bootbox.alert("Any one Residence Proof for Wife is mandatory");
					return false;
			 }
		}
		return true;
	}else{
		return true;
	}  
	
}

function removeMandatory() {
	if($('#registrationStatus').val()=='Created'){  
		$('#husband-photo').removeAttr('required');
		$('#wife-photo').removeAttr('required');
	}
}

function validateApplicationDate(){
	var one_day=1000*60*60*24
	var start;
	var end;
	var days;
	if(applicationDate){
		start=$('#txt-dateOfMarriage').datepicker('getDate');
		end = $('#applicationDate').datepicker('getDate'); // current date
		days = (end.getTime() - start.getTime())/one_day;
	} else {
		 start = $('#txt-dateOfMarriage').datepicker('getDate'); // date of marriage
		 end = new Date();
		 days   = Math.round((end.getTime() - start.getTime())/one_day);
	}// current date
	return days;
}


$(".btn-primary").click(function(e) { 
		if($('#allowDaysValidation').val()=="YES"){
			var noOfDays = validateApplicationDate();
			if(noOfDays>90){
				bootbox.alert("Application will not be accepted beyond 90 days from the date of marriage "+$('#txt-dateOfMarriage').val());
				return false; 
			}
		}
		
	var action = $('#workFlowAction').val();
	if(action == 'Reject') { 
		 $('#Reject').attr('formnovalidate','true');
		 var r = confirm("Do You Really Want to Reject The Registration!");
		 if (r) {
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
		 var res = confirm("Do You Really Want to Cancel The Registration!");
		 if (res) {
			 var approvalComment=$('#approvalComent').val();
			  if(approvalComment == "") {
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
	
		 if(action == 'Print Certificate') {
			 validateSerialNumber(e);
		 }
		 
		 if(action == 'Approve') {
			 validateMandatoryOnApprove(action);
			 validateForm(e);
		 }
});

function validateMandatoryOnApprove(action) {
	if (action == 'Approve') {
		$('.validate-madatory').find("span").addClass("mandatory");
		$('.addremovemandatory').attr("required", "true");
	} else {
		$('.validate-madatory').find("span").removeClass("mandatory");
		$('.addremovemandatory').removeattr("required", "true");
	}
}

function validateForm(e) {
	if ($('form').valid()) {
		// if valid submit the form
	} else {
		e.preventDefault();
	}
}
$('#txt-serialNo').change(function(e){
	validateSerialNumber(e);
});

function validateSerialNumber(e){
	var serialNo=$('#txt-serialNo').val();
	if(serialNo != '') {
		$.ajax({
			url: "/mrs/registration/checkunique-serialno",      
			type: "GET",
			data: {
				"serialNo"  : serialNo
			},
			dataType: "json",
			success: function (response) { 
				if(response) {
					    $('#txt-serialNo').val('');
						bootbox.alert("Entered Serial Number already exists. Please Enter Unique Number.");
						validateForm(e);
				}
				else
					validateForm(e);
			}, 
			error: function (response) {
				$('#txt-serialNo').val('');
				e.preventDefault();

			}
		});
	}	
}
