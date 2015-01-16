jQuery(document).ready(function($)
{
	$('#toggleactions').click(function(){
		if($(this).html()== "More..")
		{
			$(this).html('Less..');
			$('.show-action-more').show();
		}else
		{
			$(this).html('More..');
			$('.show-action-more').hide();
		}
		
	});
	
});