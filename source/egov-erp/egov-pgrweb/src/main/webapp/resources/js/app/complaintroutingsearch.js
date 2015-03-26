jQuery(document).ready(function($)
{
	
	// Instantiate the Bloodhound suggestion engine
	var complaintype = new Bloodhound({
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
	
	
	tableContainer = $("#com_routing_search").dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-6 col-md-3 col-left'i><'col-xs-6 col-md-3 text-right col-left'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-xs-12 col-md-3 add-margin col-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"oTableTools": {
			"sSwfPath": "../../../../../../egov-egiweb/src/main/webapp/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons": [ "copy", "csv", "xls", "pdf", "print" ]
		}
	});
	
	tableContainer.columnFilter({
		"sPlaceHolder" : "head:after"
	});
	
	$('#com_routing_search tbody').on('click', 'tr', function () {
		if($(this).hasClass('apply-background'))
		{
			$(this).removeClass('apply-background');
		}else{
			$('#com_routing_search tbody tr').removeClass('apply-background');
			$(this).addClass('apply-background');
		}
		
    } );
	
});