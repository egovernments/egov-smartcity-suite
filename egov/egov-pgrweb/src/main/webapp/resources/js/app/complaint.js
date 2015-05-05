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
		  minLength: 3
		}, {
		displayKey: 'name',
		source: complaintype.ttAdapter()
	}).on('typeahead:selected', function(event, data){
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
		  minLength: 5
		}, {
		displayKey: 'name',
		source: complaintlocation.ttAdapter()
	}).on('typeahead:selected', function(event, data){            
		$("#locationid").val(data.value);    
    });
	
	$(":input").inputmask();
	
	/*complaint through*/
	$('input:radio[name="receivingMode"]').click(function(e) {
		$('#receivingCenter').prop('selectedIndex',0);
		 $("#crn").removeAttr('required');
		 $("#crnReq").hide();
		if($('#receivingMode5').is(':checked'))
		{
			$('#recenter, #regnoblock').show();
		}else
		{
			$('#recenter, #regnoblock').hide();
		}
	});
	
	$('#doc').bind('input propertychange', function() {
		var remchar = parseInt(500 - ($('#doc').val().length));
		$('#rcc').html('Remaining Characters : '+remchar);
		
	});
	
	$('.freq-ct').click(function(){ 
		$('#complaintTypeName').typeahead('val',$(this).html().trim());
		$("#complaintTypeName").trigger('blur');
	});
	
	/**
	 *Based on the selected complaint type make Location is required or not 
	 **/
	$("#complaintTypeName").blur(function(){
		if (this.value === '') {
			 $(".optionalmandate").hide();
			 $("#location").removeAttr('required');
			return;
		} else {
			$.ajax({
				type: "GET",
				url: "isLocationRequired",
				cache: true,
				data:{'complaintTypeName' : this.value}
			}).done(function(value) {
				 if(value === true) {
					 $(".optionalmandate").show();
					 $("#location").attr('required','required');
				 } else {
					 $(".optionalmandate").hide();
					 $("#location").removeAttr('required');
					 $("#location").val("");
					 $("#landmarkDetails").val("");
				 }
			});
		}
	});	
	
	$("#receivingCenter").change(function(){
		if (this.value === '') {
			 $("#crn").removeAttr('required');
			 $("#crnReq").hide();
			return;
		} else {
			$.ajax({
				type: "GET",
				url: "isCrnRequired",
				cache: true,
				data:{'receivingCenterId' : this.value}
			}).done(function(value) {
				 if(value === true) {
					 $("#crn").attr('required','required');
					 $("#crnReq").show();
				 } else {
					 $("#crn").removeAttr('required');
					 $("#crnReq").hide();
				 }
			});
		}
	});	
	
	if($("#locationRequired").val() === "false") {
		 $(".optionalmandate").hide();
		 $("#location").removeAttr('required');
	} else {
		 $(".optionalmandate").show();
		 $("#location").attr('required');
	}
	
	$('input[type=radio][name=receivingMode]').change(function() {
		if ($("input[name=receivingMode]:checked").val() == 'PAPER') {
			$('#recenter, #regnoblock').show();
			$("#receivingCenter, #crn").removeAttr('disabled');
		} else {
			$("#receivingCenter, #crn").attr('disabled', true);
		}
	});

	// MASK SCREEN IMPORTANT
	//$('.loader-class').modal('show', {backdrop: 'static'});
	//$('.loader-class').modal('hide');
	

});

function setComplaintTypeId(obj) {
	$("#complaintTypeId").val(obj)
}
