jQuery(document).ready(function($)
{
	$('#when_date').on('change', function() {
		if( this.value == 5) // or $(this).val()
		{
			$('.date_enable').removeAttr('disabled');
		}else
		{
			$(".date_enable").attr('disabled','disabled');
		}
	});
	
	$('#toggle-searchcomp').click(function(){
		if($(this).html()== "More..")
		{
			$(this).html('Less..');
			$('.show-searchcomp-more').show();
		}else
		{
			$(this).html('More..');
			$('.show-searchcomp-more').hide();
		}
		
	});
	
	var table = $("#table-3").dataTable({
		"sPaginationType": "bootstrap",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bStateSave": true
	});
	
	table.columnFilter({
		"sPlaceHolder" : "head:after"
	});
	
	$(".dataTables_wrapper select").select2({
		minimumResultsForSearch: -1
	});
	
	
});