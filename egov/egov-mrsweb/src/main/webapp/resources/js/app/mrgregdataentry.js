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
			$(span).removeClass('error-msg');
			$(span).text(image.name);
		}
		
		$(inputPhoto).trigger('click');
	});
	

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
	    }  
	});

	$("input[id$='religionPractice1']").prop("checked", true);
	
	$('#table_search').keyup(function(){
    	$('#registration_table').fnFilter(this.value);
    });
	
	$('#select-marriagefees').change( function () {
		showFee();
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
	
	$('input[id$="husband.ageInYearsAsOnMarriage"]').blur( function () {
		var age = parseInt( $(this).val() );
		if (age != null && age != undefined && (age < 21)) {
			bootbox.alert("Husband's age should be atlest 21 years");
			$(this).val('');
		}
	})
	
	$('input[id$="wife.ageInYearsAsOnMarriage"]').blur( function () {
		var age = parseInt( $(this).val() );
		if (age != null && age != undefined && (age < 18)) {
			bootbox.alert("wife's age should be atlest 18 years");
			$(this).val('');
		}
	})
	
	$('input[id$="husband.contactInfo.mobileNo"]').blur( function () {
		 var mobileno =  $(this).val();
			if (mobileno.length < 10) {
				bootbox.alert("Please enter 10 digit mobile number");
				$(this).val('');
			}
	})
	
	$('input[id$="wife.contactInfo.mobileNo"]').blur( function () {
		var mobileno =  $(this).val();
		if (mobileno.length < 10) {
			bootbox.alert("Please enter 10 digit mobile number");
			$(this).val('');
		}
	})
	
	$('.witnessage').blur( function () {
		var age = parseInt( $(this).val() );
		if (age != null && age != undefined && (age < 18)) {
			bootbox.alert("witness age should be atleast 18 years");
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
			//$('#see-more-link').hide();
			}else{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
			//$('#see-more-link').show();
		}
	});
	function showFee()
	   {
	    if ($('#select-marriagefees').val() === '') {
	    	$('#txt-feepaid').val('');
				return;
			} else {
			
				$.ajax({
					type: "GET",
					url: "/mrs/registration/calculatemarriagefee",
					cache: true,
					dataType: "json",
					data:{
						'feeId' : $('#select-marriagefees').val()
						}
				}).done(function(value) {
										if (value == 0)
											$('#txt-feepaid').val('');
										else
											$('#txt-feepaid').val(value);
				
				});
			}
		
	   }

// Called from Data Entry Screen - Starts
	
	$('#applicationNum').blur(function(){
		validateApplicationNumber();
	});
	
	$('#registrationNum').blur(function(){
		validateRegistrationNumber();
	});
	
	
	$('#select-registrationunit').change( function () {
		showRegistrationUnit();
	});
	
	
	function validateApplicationNumber(){
		appNo=$('#applicationNum').val();
		if(appNo != '') {
			$.ajax({
				url: "/mrs/registration/checkUniqueAppl-RegNo",      
				type: "GET",
				data: {
					registrationNo : "",  
					applicationNo  : appNo
				},
				dataType: "json",
				success: function (response) { 
					if(response != true) {
							$('#applicationNum').val('');
							bootbox.alert("Entered Application Number already exists. Please Enter Unique Number.");
					}
				}, 
				error: function (response) {
					$('#applicationNum').val('');
					bootbox.alert("connection validation failed");
				}
			});
		}	
	}
	
	function showRegistrationUnit()
	{
	 if ($('#select-registrationunit').val() === '') {
	 	$('#txt-zone').val('');
				return;
			} else {
			
				$.ajax({
					type: "GET",
					url: "/mrs/registration/getmrregistrationunitzone",
					cache: true,
					dataType: "json",
					data:{
						'registrationUnitId' : $('#select-registrationunit').val()
						}
				}).done(function(value) {
					$('#txt-zoneid').val(value.id);
					$('#txt-zone').val(value.name);
				
				});
			}
		
	}
	
	
	function validateRegistrationNumber(){
		regNo=$('#registrationNum').val();
		if(regNo != '') {
			$.ajax({
				url: "/mrs/registration/checkUniqueAppl-RegNo",      
				type: "GET",
				data: {
					registrationNo : regNo,  
					applicationNo  : ""
				},
				dataType: "json",
				success: function (response) { 
					if(response != true) {
							$('#registrationNum').val('');
							bootbox.alert("Entered Registration Number already exists. Please Enter Unique Number.");
					}
				}, 
				error: function (response) {
					$('#registrationNum').val('');
					bootbox.alert("connection validation failed");
				}
			});
		}	
	}
	
	jQuery('form').validate({
	    ignore: ".ignore",
	    invalidHandler: function(e, validator){
	        if(validator.errorList.length)
	        	$('#settingstab a[href="#' + jQuery(validator.errorList[0].element).closest(".tab-pane").attr('id') + '"]').tab('show');
	    }
	});

	$('#dataEntrySubmit').click(function(e){
		if($('form').valid()){
			
		}else{
			e.preventDefault();
		}
	});
	
})

// Called from Data Entry Screen - Ends

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