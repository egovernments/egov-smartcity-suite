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
	
	if ($("input[name=receivingMode]:checked").val() === 'PAPER') {
		$('#recenter, #regnoblock').show();
	}
});