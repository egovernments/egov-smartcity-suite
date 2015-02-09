jQuery(document).ready(function($)
{
	
	// Instantiate the Bloodhound suggestion engine
	var position = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/pgr/complaint/complaintsTypes?q=%QUERY',
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
	position.initialize();
	
	// Instantiate the Typeahead UI
	$('.typeahead').typeahead({
		hint: true,
		highlight: true,
		minLength: 3
		}, {
		displayKey: 'value',
		source: position.ttAdapter()
	});
	
});