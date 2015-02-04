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
			$('#main-menu').removeClass('sidebar-collapsed');
			$('#main-menu li a span, #main-menu li ul li').css({'visibility':'visible'});
			$('.sidebar-menu, .sidebar-menu-inner').removeClass('collapsed-menu');
			$('.logo-env').removeClass('collapsed-logo');
			$('#change-menu-logo').removeClass('entypo-right-dir').addClass('entypo-left-dir');
			//$('#main-menu > li').addClass('has-sub');
			$('#main-menu > li > a > span').removeClass('collapsed-span');
			$('#main-menu > li > ul').removeClass('collapsed-ul');
			$('#main-menu > li a span.badge').removeClass('collapsed-badge');
			$('.sidebar-user-info').show();
			}else{
			/*collapsed*/
			$('#main-menu').addClass('sidebar-collapsed');
			$('#main-menu li a span, #main-menu ul').css({'visibility':'hidden'});
			$('#main-menu ul').css('display','none');
			$('.sidebar-menu, .sidebar-menu-inner').addClass('collapsed-menu');
			$('.logo-env').addClass('collapsed-logo');
			$('#change-menu-logo').removeClass('entypo-left-dir').addClass('entypo-right-dir');
			$('#main-menu > li').removeClass('has-sub has-sublevel');
			$('#main-menu > li > a > span').addClass('collapsed-span');
			$('#main-menu > li > ul').addClass('collapsed-ul');
			$('#main-menu > li a span.badge').addClass('collapsed-badge');
			$('.sidebar-user-info').hide();
		}
		
	});
	
	$('.page-container .sidebar-menu #main-menu li').hover(function(e){
		if($('#main-menu').hasClass('sidebar-collapsed'))
		{
			$(this).children('a').children('span').css('visibility','visible');
			$(this).children('ul').css({'visibility':'visible', 'display':'block'});
			$(this).children('ul').children('li').children('a').children('span').css({'visibility':'visible'});
			}else{
				
		}
	},
	function() {
		if($('#main-menu').hasClass('sidebar-collapsed'))
		{
			$(this).children('a').children('span').css('visibility','hidden');
			$(this).children('ul').css({'visibility':'hidden', 'display':'none'});
			}else{
		}
	});
	
	
	$(".page-container .sidebar-menu #main-menu li ul li").mouseleave(function () {
		if($('#main-menu').hasClass('sidebar-collapsed'))
		{
			$(this).children('a').children('span').css({'visibility':'visible', 'display':'block'});
		}else{
		}
		
	});
	
	
	});	