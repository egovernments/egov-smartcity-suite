$(document).ready(function()
{
	$('#main-menu li').click(function(){
		 $(this).children('ul').slideToggle();
	});
});