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


// Called from Data Entry Screen - Starts
	
	$('#applicationNum').blur(function(){
		validateApplicationNumber();
	});
	
	$('#registrationNum').blur(function(){
		validateRegistrationNumber();
	});
	
	$('#txt-serialNo').blur(function(){
		validateSerialNumber();
	});

	
	function validateApplicationNumber(){
		var appNo=$('#applicationNum').val();
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
					if(!response) {
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
	
	function validateSerialNumber(){
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
							bootbox.alert("Entered Serial Number already exists. Please Enter Unique Number.");
							$('#txt-serialNo').val('');
					}
				}, 
				error: function (response) {
					$('#txt-serialNo').val('');
					bootbox.alert("connection validation failed");
				}
			});
		}	
	}
	
	function validateRegistrationNumber(){
		var regNo=$('#registrationNum').val();
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
					if(!response) {
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
			if (!validateChecklists()) {
				return false;
			}
		}else{
			e.preventDefault();
		}
	});
	
})

// Called from Data Entry Screen - Ends

function validateChecklists() {
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
}