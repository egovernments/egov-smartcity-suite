$(document).ready(function()
{	
	$(".msg a").click(function(e) {
		//do something
		e.stopPropagation();
		var senderElement = e.target;
		//check if sender is the DIV element
		window.location = url;
	});
	
	$('.menu-item').click(function(e)
	{
		$('.citizen-screens').hide();
		$('.hr-menu li').removeClass('active');
		$(this).parent().addClass('active');
		$($(this).data('show-screen')).show();
	});
	
	$("#sortby_drop li a").click(function(){
		$("#sortby_drop > .btn > span > b").html($(this).html());
	});
	
	
	$('.tabs-style-topline nav li').click(function(){
		if($(this).attr('data-section') == "newrequest")
		{
			$('.tabs-style-topline nav li').removeClass('tab-current-newreq');
			$('.content-wrap section').removeClass('content-current-newreq');
			$(this).addClass('tab-current-newreq');
			$($(this).attr('data-newreq-section')).addClass('content-current-newreq');
		}else if($(this).attr('data-section') == "myaccount")
		{
			$('.tabs-style-topline nav li').removeClass('tab-current-myacc');
			$('.content-wrap section').removeClass('content-current-myacc');
			$(this).addClass('tab-current-myacc');
			$($(this).attr('data-myaccount-section')).addClass('content-current-myacc');
		}
	});
	
});