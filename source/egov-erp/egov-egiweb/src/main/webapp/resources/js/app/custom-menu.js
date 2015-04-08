$(document).ready(function () {
	
	var menuheight = ($( window ).height() - 63);
	var ulheight =(menuheight -93);
	
	console.log($(window).height()+"----"+menuheight+"-----"+ulheight);
	
	$('#menu').multilevelpushmenu({
		menuWidth: '250px',
		mode: 'cover',
		onItemClick: function () {
			var e = arguments[0];
			$('#menu_multilevelpushmenu ul li').removeClass('li-active');
			$(e.target).parent().addClass('li-active');
			if ($(e.target).prop('tagName').toLowerCase() == 'input') {
				$(e.target).focus();
				$(e.target).val('');
				$(e.target).unbind('');
				$(e.target).blur(function(){
					$(e.target).val('');
				});
			}
		},
		onGroupItemClick: function () {
			menuheight = ($( window ).height() - 63);
			ulheight =(menuheight -93);
			$('#menu').height(''+menuheight+'px');
			$('#menu_multilevelpushmenu').height(''+menuheight+'px');
			$('#menu, #menu_multilevelpushmenu').css('min-height', ''+menuheight+'px');
			var e = arguments[2];
			if(e.children('div').children('ul').height() > ulheight){
				$('#menu_multilevelpushmenu ul').height(''+ulheight+'px');
				$('#menu_multilevelpushmenu ul').css('overflow-y','scroll');
				}else{
				$('#menu_multilevelpushmenu ul').css('overflow-y','auto');
			}
		},
		onBackItemClick : function () {
			$('#menu_multilevelpushmenu ul').css('overflow-y','auto');
		},
		onCollapseMenuEnd : function () {
			var w = $('#menu.homepage').width()+'px';
			$('.inline-main-content').css('width', 'calc(100% - '+w+')');
		},
		onExpandMenuEnd : function () {
			var w = $('#menu.homepage').width()+'px';
			$('.inline-main-content').css('width', 'calc(100% - '+w+')');
		}
	});
	
	$(window).on('resize', function () {
		setmenuheight();
	}).trigger('resize');
	
	function setmenuheight(){
		menuheight = ($( window ).height() - 63);
		$('#menu').height(''+menuheight+'px');
		$('#menu_multilevelpushmenu').height(''+menuheight+'px');
		$('#menu, #menu_multilevelpushmenu').css('min-height', ''+menuheight+'px');
	}
	
});