jQuery(document).ready(function($)
{
	// Instantiate the Bloodhound suggestion engine
	var complaintype = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: 'http://google.com',
			filter: function (complaintype) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(complaintype.results, function (ct) {
					return {
						value: ct.original_title
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	complaintype.initialize();
	
	// Instantiate the Typeahead UI
	$('#com_type').typeahead(null, {
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
	$('#clocation').typeahead(null, {
		displayKey: 'value',
		source: complaintlocation.ttAdapter()
	});
	
	
	$('.twitter-typeahead').css('display','block');
	
	
	$(":input").inputmask();
	
	$("#f-name").on("input", function(){
		var regexp = /[^a-zA-Z]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
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
		var remchar = parseInt(400 - ($('#doc').val().length));
		$('#rcc').html('Remaining Characters : '+remchar);
		
	});
	
	
});