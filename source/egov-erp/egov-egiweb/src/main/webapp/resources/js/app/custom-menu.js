$(document).ready(function () {
	$('#menu').multilevelpushmenu({
		menuWidth: '250px',
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
		onCollapseMenuEnd : function () {
			var w = $('#menu.homepage').width()+'px';
			$('.inline-main-content').css('width', 'calc(100% - '+w+')');
		},
		onExpandMenuEnd : function () {
			var w = $('#menu.homepage').width()+'px';
			$('.inline-main-content').css('width', 'calc(100% - '+w+')');
		}
	});
	
});