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
						value: ct.name
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
		displayKey: 'value',
		source: complaintype.ttAdapter()
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
						value: cl.label
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
		displayKey: 'value',
		source: complaintlocation.ttAdapter()
	});
	
	
	$(":input").inputmask();
	
	/*complaint through*/
	$('input:radio[name="compthr"]').click(function(e) {
		if($('#pform').is(':checked'))
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
		$('#complaintType').typeahead('val',$(this).html());
	});
	
	/**
	 *Based on the selected complaint type make Location is required or not 
	 **/
	$("#complaintType").blur(function(){
		if (this.value === '') {
			 $(".optionalmandate").hide();
			 $("#location").removeAttr('required');
			 $("#landmarkDetails").removeAttr('required');
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
					 $("#landmarkDetails").attr('required','required');
				 } else {
					 $(".optionalmandate").hide();
					 $("#location").removeAttr('required');
					 $("#landmarkDetails").removeAttr('required');
					 $("#location").val("");
					 $("#landmarkDetails").val("");
				 }
			});
		}
	});	
	
	if($("#locationRequired").val() === "false") {
		 $(".optionalmandate").hide();
		 $("#location").removeAttr('required');
		 $("#landmarkDetails").removeAttr('required');
	} else {
		 $(".optionalmandate").show();
		 $("#location").attr('required');
		 $("#landmarkDetails").attr('required');
	}
	
	$("select#rec_centerselect").prop('selectedIndex', 2);
	$('#reg_no').val('CT156YT6C89');
	$('#f-name').val('Manu Srivastava');
	$('#mob-no').val('8076453213');
	$('#email').val('manu@egovernments.org');
	$('#complaintType').typeahead('val','Garbage');
	$('#comptitle').val('Waterlogged in our areas');
	$('#doc').val('Road is waterlogged. kids are floating boats in it. People are washing cars in it. Please fix it soon as the mosquito colonies are going to expload soon!');
	$('#location').typeahead('val','VP Hall Compound Road, Kannappar Thidal, Poongavanapuram, Chennai, Tamil Nadu 600003, India');
	$('#lm').val('Near phoenix mall');
});
