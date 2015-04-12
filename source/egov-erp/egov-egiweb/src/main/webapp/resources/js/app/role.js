jQuery(document).ready(function($)
{
	// Instantiate the Bloodhound suggestion engine
	var rolename = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: 'roleNames?roleName=%QUERY',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data, function (role) {
					return {
						value: role.roleName
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	rolename.initialize();
	
	// Instantiate the Typeahead UI
	$('.typeahead').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 3
		}, {
		displayKey: 'value',
		source: rolename.ttAdapter()
	});
	$('#roleNew').click(function() {
		$('#method').val("New");
		$('#viewRoleForm').trigger('submit');
	})

	$('#roleEdit').click(function() {
		$('#method').val("Edit");
		$('#viewRoleForm').trigger('submit');
	})
	
});