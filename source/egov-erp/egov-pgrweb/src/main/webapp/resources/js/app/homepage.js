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
		$(this).children('ul').removeAttr('style');
		$(this).children('ul').hide();
	}
    );
	
	/* ADDING SUB MENU INDICATOR FOR PARENT MENUS */
	
	$('.horizontal-menu .nav ul li:has(ul)').each(function(){
		
		$(this).children('a:first').append('<i class="entypo-left-open-mini left-float"></i>');
		
	});
	
	
	/* MENU OVERFLOW HANDLE FUNCTIONALITY START */
	
	/* OVERFLOW MENU ADDING INDICATOR */
	$('.horizontal-menu .navbar.navbar-fixed-top .menu-responsive').append('<span class="scrollup"> <i class="entypo-up-open-big"></i> </span> <span class="scrolldown"> <i class="entypo-down-open-big"></i> </span>');
	
	$('.horizontal-menu .navbar.navbar-fixed-top .menu-responsive, .horizontal-menu .navbar.navbar-fixed-top .menu-responsive ul').hover(function() {
		
		if($(this).children('ul').length)
		{
			
			$(this).children('.scrollup').css({
	'width':$(this).children('ul').width(),
	});
	
	//calculate top for bottom overflow menu
	var top=$(window).height() - ($('.scrolldown').height()+$('.scrolldown').offset().top);
	
	$(this).children('.scrolldown').css({
	'top':top,
	'width':$(this).children('ul').width(),
	});
	
	$(this).children('ul').attr('data-real-height', $(this).children('ul').height());
	
	//initialize menu show with some default configs
	if(($(this).children('ul').height()+$(this).children('ul').offset().top) > $(window).height()){
	var getHeight=$(window).height()-$(this).children('ul').offset().top;
	$(this).children('ul').css({'display':'block', 'height':getHeight});
	$(this).children('.scrolldown').show();
	}
	else{
	$(this).children('.scrolldown').hide();
	}
	
	$(this).children('ul').scrollTop(0);
	
	}
	
	},function() {
	
	
	$(this).children('.scrollup, .scrolldown').css({			
	'display':'none'
	});
	
	});
	
	function chkShowOrHideWhichIndicatorOverflowMenu(elem)
	{
	//show overflow indicator condition for top
	if($(elem).children('ul').scrollTop() > 1){
	$(elem).children('.scrollup').show();
	}
	else{
	$(elem).children('.scrollup').hide();
	}
	
	//show overflow indicator condition for bottom
	if(($(elem).children('ul').scrollTop() + ($(window).height() - $(elem).children('ul').offset().top)) < $(elem).children('ul').data('real-height')){
	$(elem).children('.scrolldown').show();
	}
	else{
	$(elem).children('.scrolldown').hide();
	}
	}
	
	var scrollelem;
	
	function scrollToTop(){
	chkShowOrHideWhichIndicatorOverflowMenu($(scrollelem).parent());
	$(scrollelem).stop().animate({
	scrollTop: '-=40'
	}, 90,scrollToTop);
	}
	
	function scrollToBottom(){
	chkShowOrHideWhichIndicatorOverflowMenu($(scrollelem).parent());
	$(scrollelem).stop().animate({
	scrollTop: '+=40'
	}, 90,scrollToBottom);
	}        
	
	function stop(){
	$(scrollelem).stop();
	}
	
	
	$(document).on('mouseover','.scrolldown',function(){
	$(this).parent().children('ul').addClass('enable-scroll');
	scrollelem=$(this).parent().children('ul');
	scrollToBottom();
	}).on("mouseleave", ".scrolldown", function(event){
	stop();
	});	
	
	
	$(document).on('mouseover','.scrollup',function(){
	scrollelem=$(this).parent().children('ul');
	scrollToTop();
	}).on("mouseleave", ".scrollup", function(event){
	stop();
	});
	
	/* MENU OVERFLOW HANDLE FUNCTIONALITY END */
	
	
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
	
	$('.sidebar-collapse-icon, .sidebar-collapse-to-normal').click(function(){
	if($('.sidebar-menu').hasClass('collapsed-menu'))
	{
	//not collapsed
	sidebar_collapsed_menu();
	}else{
	/*collapsed*/
	no_sidebar_collapsed_menu();
	}
	
	});
	
	
	$('.page-container .sidebar-menu #main-menu li').hover(function(e){
	if($('#main-menu').hasClass('sidebar-collapsed'))
	{
	$(this).children('a').children('span').css('visibility','visible');
	$(this).children('ul').css({'visibility':'visible', 'display':'block'});
	$(this).children('ul').children('li').children('a').children('span').css({'visibility':'visible'});
	$(this).children('a').children('span').removeClass('change-li-ul-li');
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
	$(this).parent().parent().children('a').children('span').removeClass('change-li-ul-li');
	}else{
	}
	
	});
	
	$(".page-container .sidebar-menu #main-menu li ul li").hover(function () {
	if($('#main-menu').hasClass('sidebar-collapsed'))
	{
	$(this).parent().parent().children('a').children('span').addClass('change-li-ul-li');
	}else{
	}
	
	});
	
	$("li#search form button").click(function(e){
	e.preventDefault();//this code prevents default functionality of submit button i.e. to submit form
	//do something
	if($('#main-menu').hasClass('sidebar-collapsed'))
	{
	$(this).parent().children('.search-input').addClass('focussed');
	}
	});
	
	$("li#search form button").focus(function() {
	if($('#main-menu').hasClass('sidebar-collapsed'))
	{
	$(this).parent().children('.search-input').addClass('focussed');
	}
	});
	
	$("li#search form .search-input").blur(function() {
	if($('#main-menu').hasClass('sidebar-collapsed'))
	{
	$(this).parent().children('.search-input').removeClass('focussed');
	}
	});
	
	function sidebar_collapsed_menu()
	{
	$('#main-menu').removeClass('sidebar-collapsed');
	$('#main-menu li a span, #main-menu li ul li').css({'visibility':'visible'});
	$('.sidebar-menu, .sidebar-menu-inner').removeClass('collapsed-menu');
	//$('.logo-env').removeClass('collapsed-logo');
	$('.logo-env').show();
	$('.change-menu-normal').removeClass('entypo-right-dir').addClass('entypo-left-dir');
	$('#main-menu > li:has(ul)').addClass('has-sub');
	$('#main-menu > li > a > span').removeClass('collapsed-span');
	$('#main-menu > li > ul').removeClass('collapsed-ul');
	$('#main-menu > li a span.badge').removeClass('collapsed-badge');
	$('.sidebar-user-info').removeClass('collapsed-ui');
	$('.user-link img').removeClass('collapsed-img');
	$('.sidebar-user-info .sui-normal a span, .sidebar-user-info .sui-normal a strong').css('display','block');
	}
	
	function no_sidebar_collapsed_menu()
	{
	$('.sidebar-menu').show();
	$('#main-menu').addClass('sidebar-collapsed');
	$('#main-menu li a span, #main-menu ul').css({'visibility':'hidden'});
	$('#main-menu ul').css('display','none');
	$('.sidebar-menu, .sidebar-menu-inner').addClass('collapsed-menu');
	//$('.logo-env').addClass('collapsed-logo');
	$('.logo-env').hide();
	$('.change-menu-normal').removeClass('entypo-left-dir').addClass('entypo-right-dir');
	$('#main-menu > li').removeClass('has-sub has-sublevel');
	$('#main-menu > li > a > span').addClass('collapsed-span');
	$('#main-menu > li > ul').addClass('collapsed-ul');
	$('#main-menu > li a span.badge').addClass('collapsed-badge');
	$('.sidebar-user-info').addClass('collapsed-ui');
	$('.user-link img').addClass('collapsed-img');
	$('.sidebar-user-info .sui-normal a span, .sidebar-user-info .sui-normal a strong').css('display','none');
	}
	
	$(window).resize(function(){
	var wwt = $( window ).width();
	if(wwt < 768)
	{
	sidebar_collapsed_menu();
	}else if((wwt > 767) && (wwt < 992))
	{
	no_sidebar_collapsed_menu();
	}else if(wwt > 991){
	sidebar_collapsed_menu();
	}
	});
	
	/******Dinesh******/
	
	
	});		