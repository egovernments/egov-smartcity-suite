/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
	
jQuery(document).ready(function($)
{
	// Instantiate the Bloodhound suggestion engine
	var complaintype = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: 'complaintTypes?complaintTypeName=%QUERY',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data, function (ct) {
					return {
						name: ct.name,
						value: ct.id
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	complaintype.initialize();
	// Instantiate the Typeahead UI
	$('.typeahead').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: complaintype.ttAdapter()
	}).on('typeahead:selected typeahead:autocompleted typeahead:matched', function(event, data){
		$("#complaintTypeId").val(data.value);    
    });
	
	// Instantiate the Bloodhound suggestion engine
	var complaintlocation = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: 'locations?locationName=%QUERY',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data, function (cl) {
					return {
						name: cl.name,
						value: cl.id
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	complaintlocation.initialize();
	
	// Instantiate the Typeahead UI
	$('#location').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 3
		}, {
		displayKey: 'name',
		source: complaintlocation.ttAdapter()
	}).on('typeahead:selected', function(event, data){            
		$("#locationid").val(data.value);    
		$('#lat, #lng').val(0.0);
    });
	
	$(":input").inputmask();
	
	
	$('.freq-ct').click(function(){ 
		$('#complaintTypeName').typeahead('val',$(this).html().trim());
		$("#complaintTypeName").trigger('blur');
	});
	
	$('input[type=radio][name=receivingMode]').change(function() {
		$('#receivingCenter').prop('selectedIndex',0);
		disableCRN(); 
		if ($("input[name=receivingMode]:checked").val() == 'MANUAL') {
			enableRC();
		} else {
			disableRC();
		}
	});
	
	$('.tour-section').click(function(){
		$('.demo-class').modal('show', {backdrop: 'static'});
		var tour = new Tour({
			  steps: [
	          {
			    element: "#f-name",
			    title: "Name",
			    content: "Enter your full name!"
			  },
			  {
			    element: "#mob-no",
			    title: "Mobile Number",
			    content: "Enter your valid 10 digit mobile number!"
			  },
			  {
			    element: "#email",
			    title: "Email ID",
			    content: "Enter your valid email id!"
			  },
			  {
			    element: "#address",
			    title: "Address",
			    content: "Enter your present residential address!"
			  },
			  {
			    element: "#complaintTypeName",
			    title: "Grievance Type",
			    content: "Enter your grievance type and select it from the suggestion or choose it from below frequently filed grievance type!"
			  },
			  {
			    element: "#doc",
			    title: "Grievance Details",
			    content: "Describe your grievance details briefly!"
			  },
			  {
			    element: "#upload-section",
			    title: "Upload Photograph / Video",
			    content: "Upload grievance relevated photo / video (Max : 3 files)!"
			  },
			  {
			    element: "#location-tour",
			    title: "Grievance Location",
			    content: "Enter your grievance location or select it from the map icon!"
			  },
			  {
			    element: "#landmarkDetails",
			    title: "Landmark",
			    content: "Enter your landmark (if any)!"
			  },
			  {
			    element: "#captcha-section iframe",
			    title: "Captcha",
			    content: "Click on the checkbox to validate captcha!"
			  },
			  {
			    element: "#create-griev",
			    title: "Create Grievance",
			    content: "Finally, Click here to submit your grievance!"
			  }],
			  storage: false,
			  duration: 5000,
			  onShown: function (tour) {
				  console.log(tour.getCurrentStep());
				  var step = tour.getCurrentStep();
				  if(step == 0){
					  typingfeel('James Jackson', '#f-name');
				  }else if(step == 1){
					  typingfeel('9988776655', '#mob-no');
				  }else if(step == 2){
					  typingfeel('james.jackson@gmail.com', '#email');
				  }else if(step == 3){
					  typingfeel('Colorado U.S', '#address');
				  }else if(step == 4){ 
				    typingfeelintypeahead('Dog','#complaintTypeName','Dog menace');
				  }else if(step == 5){
					  typingfeel('Dog menace in madiwala', '#doc');
				  }else if(step == 7){ 
				    typingfeelintypeahead('Rev','#location','Revenue, Zone-4, Srikakulam  Municipality');
				  }else if(step == 8){
					  typingfeel('Spencer Plaza', '#landmarkDetails');
				  }
			  },
			  onEnd: function (tour) {
				  location.reload();
			  },
			  template : "<div class='popover tour'> <div class='arrow'></div> <h3 class='popover-title'></h3> <div class='popover-content'></div> </nav> </div>"
		});
		// Initialize the tour
		tour.init();
		// Start the tour
		tour.start();
	    
	});

});

$("#receivingCenter").change(function(){
	if (this.value === '') {
		disableCRN();
		return;
	} else {
		$.ajax({
			type: "GET",
			url: "isCrnRequired",
			cache: true,
			data:{'receivingCenterId' : this.value}
		}).done(function(value) {
			 if(value === true) {
				 enabledCRN();
			 } else {
				 disableCRN();
			 }
		});
	}
});	

function typingfeel(text, input){
	$.each(text.split(''), function(i, letter){
        setTimeout(function(){
            //we add the letter to the container
            $(input).val($(input).val() + letter);
        }, 200*(i+1));
    });
}

function typingfeelintypeahead(text, input, typeaheadtext){
	//text is split up to letters
    $.each(text.split(''), function(i, letter){
        setTimeout(function(){
            //we add the letter to the container
            $(input).val($(input).val() + letter);
            $(input).trigger("input");
            $("span.twitter-typeahead .tt-suggestion > p").mouseenter();
            if(i == 2)
            { 
            	$(input).typeahead('val',typeaheadtext); 
            	$(input).blur(); 
        	}
        }, 1000*(i+1));
    });
}

function setComplaintTypeId(obj) {
	$("#complaintTypeId").val(obj)
}

function enableRC() {
	$('#recenter').show();
	$("#receivingCenter").removeAttr('disabled');
}

function disableRC(){
	$('#recenter').hide();
	$("#receivingCenter").attr('disabled', true)
}

function enabledCRN() {
	$('#regnoblock').show();
	$("#crnReq").show();
//	$("#crn").attr('required','required');
	$("#crn").removeAttr('disabled');
}

function disableCRN() {
	$('#regnoblock').hide();
	$("#crnReq").hide();
	$("#crn").val("");
	$("#crn").removeAttr('required');
	$("#crn").attr('disabled',true);
}

/*demo code*/
function showChangeDropdown(dropdown)
{
	$('.drophide').hide();
	var showele = $(dropdown).find("option:selected").data('show');
	if(showele)
	{
	  $(showele).show();	
	}
}