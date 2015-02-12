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
			url: 'http://google.com',
			filter: function (complaintlocation) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(complaintlocation.results, function (cl) {
					return {
						value: cl.original_title
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	complaintlocation.initialize();
	
	// Instantiate the Typeahead UI
	$('#location').typeahead(null, {
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
	
});