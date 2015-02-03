$(document).ready(function()
{
	$('#main-menu li').click(function(e){
		$(this).children('ul').slideToggle();
		if($(this).hasClass('has-sub'))
		{
			$(this).removeClass('has-sub').addClass('has-sublevel');
		}else if($(this).hasClass('has-sublevel')){
			$(this).removeClass('has-sublevel').addClass('has-sub');
		}else{
		}
		
	});
	
	$('#main-menu li ul').click(function(e){
		e.stopPropagation();
	});
	
	$('.page-container.horizontal-menu header.navbar .navbar-right > li, .page-container.horizontal-menu header.navbar .navbar-right > li ul li').hover(
	function() {
		$(this).children('ul').show();
	},
	function() {
		$(this).children('ul').hide();
	}
	);
	
	$('.horizontal-mobile-menu').click(function(){
		$('.sidebar-menu').slideToggle();
	});
	
	
	$(window).resize(function(){
		if($(window).width() > 768)
		{
			$('.sidebar-menu').css('display','table-cell');
			}else{
			$('.sidebar-menu').css('display','none');
		}
		
	});
	
	$('.sidebar-collapse-icon').click(function(){
		if($('.sidebar-menu').hasClass('collapsed-menu'))
		{
			//$('.sidebar-menu').removeClass('collapsed-menu');
		}else{
			//$('.sidebar-menu').addClass('collapsed-menu');
			//$('.sidebar-user-info').hide();
		}
		
	});
	
});