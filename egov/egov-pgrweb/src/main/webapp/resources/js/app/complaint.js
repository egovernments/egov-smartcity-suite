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
    });
	
	$(":input").inputmask();
	
	
	$('.freq-ct').click(function(){ 
		$('#complaintTypeName').typeahead('val',$(this).html().trim());
		$("#complaintTypeName").trigger('blur');
	});
	
	/*complaint through*/
	$('input:radio[name="receivingMode"]').click(function(e) {
		$('#receivingCenter').prop('selectedIndex',0);
		disableCRN(); 
		if($('#receivingMode5').is(':checked'))
		{
			enableRC();
		}else
		{
			disableRC();
		}
	});
	
	
	$('input[type=radio][name=receivingMode]').change(function() {
		if ($("input[name=receivingMode]:checked").val() == 'PAPER') {
			enableRC();
		} else {
			disableRC();
		}
	});
	
	$('.tour-section').click(function(){

    	//define text
	    var text = 'Dog';
	    
	    //text is split up to letters
	    $.each(text.split(''), function(i, letter){
	        setTimeout(function(){
	            //we add the letter to the container
	            $('#complaintTypeName').val($('#complaintTypeName').val() + letter);
	            $("#complaintTypeName").trigger("input");
	            $("span.twitter-typeahead .tt-suggestion > p").mouseenter();
	            if(i == 2)
	            { 
	            	$('#complaintTypeName').typeahead('val','Dog menace'); 
	            	$('#complaintTypeName').blur(); 
            	}
	        }, 1000*(i+1));
	    });
	    
		var tour = new Tour({
			  steps: [
			  {
			    element: "#complaintTypeName",
			    title: "Complaint Type",
			    content: "Enter your complaint type and select it from the below suggestion"
			  },
			  {
			    element: "#location-tour",
			    title: "Complaint Location",
			    content: "Enter your complaint location or select it from the map icon"
			  }
			],
			  storage: false,
			  duration: 6000,
			  onStart: function (tour) {
				  
			  },
			  onNext: function (tour) {
				  $('#complaintTypeName').typeahead('val','');
			  },
			  onEnd: function (tour) {
				  $('#complaintTypeName').typeahead('val','');
				  $('#complaintTypeId').val('');
			  },
			  template : "<div class='popover tour'> <div class='arrow'></div> <h3 class='popover-title'></h3> <div class='popover-content'></div> </nav> </div>"
		});
		// Initialize the tour
		tour.init();
		// Start the tour
		tour.start();

	    text = '';
	    $('#complaintTypeName').typeahead('val','');
	    
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