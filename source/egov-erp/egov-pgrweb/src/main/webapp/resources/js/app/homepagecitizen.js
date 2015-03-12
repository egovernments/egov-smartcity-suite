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
		$('.tabs-style-topline nav li').removeClass('tab-current');
		$('.content-wrap section').removeClass('content-current');
		$(this).addClass('tab-current');
		$($(this).attr('data-newreq-section')).addClass('content-current');
	});
	
});