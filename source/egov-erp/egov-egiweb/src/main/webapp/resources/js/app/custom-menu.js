$(document).ready(function () {
	
	var openedWindows = [];
	
	var menuheight = ($( window ).height() - 63);
	var ulheight =(menuheight -93);
	
	$('#menu').multilevelpushmenu({
		menuWidth: '250px',
		mode: 'cover',
		menu:menuItems,
		onItemClick: function (event) {
			var e = arguments[0],$item = arguments[2];
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
			
			if($(e.target).hasClass('remove-feedback'))
			{
				$.ajax({
					type: "GET",
					url: "home/remove-favourite",
					cache: false,
					data:{'actionId' : $(e.target).parent().parent().attr('id')}
				}).done(function(value) {
					 if(value === true) {
						 $(e.target).parent().parent().remove();
					 } else {
						
					 }
				});
			}
			else{
				var itemHref = $item.find( 'a:first' ).attr( 'href' );
				var windowObjectReference = window.open(itemHref, ''+$item.attr('id')+'', 'width=900, height=700, top=300, left=150'); 
				openedWindows.push(windowObjectReference);
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
	
	$("a.open-popup").click(function(e) {
		// to open in good size for user
		var width = window.innerWidth /0.66 ; 
		// define the height in 
		var height = width * window.innerWidth / window.innerHeight; 
		// Ratio the hight to the width as the user screen ratio
		var windowObjectReference = window.open(this.href, ''+$(this).attr('data-strwindname')+'', 'width=900, height=700, top=300, left=150'); 
		openedWindows.push(windowObjectReference);
		return false;
	});
	
	$(document).on('click', 'a.open-popup', function() {
		// to open in good size for user
		var width = window.innerWidth /0.66 ; 
		// define the height in 
		var height = width * window.innerWidth / window.innerHeight; 
		// Ratio the hight to the width as the user screen ratio
		var windowObjectReference = window.open(this.href, ''+$(this).attr('data-strwindname')+'', 'width=900, height=700, top=300, left=150'); 
		openedWindows.push(windowObjectReference);
		return false;
	});
	
	$('.signout').click(function(){
		$.each( openedWindows, function( i, val ) {
			var window = val;
			window.close();
		});
	});
	
});