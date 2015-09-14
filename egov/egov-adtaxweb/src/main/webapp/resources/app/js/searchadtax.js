$(document).ready(function(){
    
    
    $('input[type=radio][name=optradio]').change(function() {
        if (this.value == 'agency') {
            $('.hoarding_section').hide();
            $('.agency_section').show();
        }
        else if (this.value == 'hoarding') {
            $('.agency_section').hide();
            $('.hoarding_section').show();
        }
    });
    
    
    tableContainer1 = $("#search_adtax_agency"); 
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bDestroy": true,
		"autoWidth": false
	});
    
    $('#adtaxsearch_agency').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
    
    tableContainer2 = $("#search_adtax_hoarding"); 
	tableContainer2.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bDestroy": true,
		"autoWidth": false
	});
    
    $('#adtaxsearch_hoarding').keyup(function(){
		tableContainer2.fnFilter(this.value);
	});
    
});