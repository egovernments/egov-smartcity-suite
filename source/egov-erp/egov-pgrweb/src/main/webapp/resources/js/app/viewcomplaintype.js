var tableContainer;
$(document).ready(function()
{
	tableContainer = $("#view-complaint-type"); 
	
	tableContainer.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
	});
	
	$('#searchviewcomp').keyup(function(){
		tableContainer.fnFilter(this.value);
	});
	
	$('#view-complaint-type tbody').on('click', 'tr', function () {
		if($(this).hasClass('apply-background'))
		{
			$(this).removeClass('apply-background');
		}else{
			$('#view-complaint-type tbody tr').removeClass('apply-background');
			$(this).addClass('apply-background');
		}
		
    } );
	
});