$(document).ready(function () {
	
	var openedWindows = [];
	var addfav_li;
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
				if (confirm("Do you wish to remove this from Favourite ?")) {
					$.ajax({
						type: "GET",
						url: "home/remove-favourite",
						data:{'actionId' : $(e.target).parent().parent().attr('id')}
						}).done(function(value) {
						if(value === true) {
							$(e.target).parent().parent().remove();
							} else {
							alert("Could not delete it from Favourite");
						}
					});
				}
				}else if($(e.target).hasClass('added-as-fav')){
				if (confirm("Do you wish to remove this from Favourite ?")) {
					$(e.target).removeClass('added-as-fav');
				}
				}else if($(e.target).hasClass('add-to-favourites')){
				$('.favourites').modal('show', {backdrop: 'static'});
				console.log($item.attr('id')+'------'+$item.find( 'a:first' ).attr( 'href' )+'----'+$(e.target).parent().text());
				addfav_li = $item.attr('id');
				$('#fav-name').val($(e.target).parent().text());
				//$(e.target).addClass('added-as-fav');
				}else{
				var itemHref = $item.find( 'a:first' ).attr( 'href' );
				var windowObjectReference = window.open(itemHref, ''+$item.attr('id')+'', 'width=900, height=700, top=300, left=150,scrollbars=yes'); 
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
	
	var actionId='';
	
	$(window).on('resize', function () {
		setmenuheight();
	}).trigger('resize');
	
	function setmenuheight(){
		menuheight = ($( window ).height() - 63);
		$('#menu').height(''+menuheight+'px');
		$('#menu_multilevelpushmenu').height(''+menuheight+'px');
		$('#menu, #menu_multilevelpushmenu').css('min-height', ''+menuheight+'px');
	}
	
	$('.add-fav').click(function(){
		$('.favourites').modal('hide');
		$.ajax({
			type: "GET",
			url: "home/add-favourite",
			data:{'actionId' : actionId,'name':'Test','contextRoot':'egi'}
		}).done(function(value) {
			//TODO 
		});
		$('#'+addfav_li+' a i').addClass('added-as-fav');
	});
	
	$("a.open-popup").click(function(e) {
		// to open in good size for user
		var width = window.innerWidth /0.66 ; 
		// define the height in 
		var height = width * window.innerWidth / window.innerHeight; 
		// Ratio the hight to the width as the user screen ratio
		var windowObjectReference = window.open(this.href, ''+$(this).attr('data-strwindname')+'', 'width=900, height=700, top=300, left=150,scrollbars=yes'); 
		openedWindows.push(windowObjectReference);
		return false;
	});
	
	$(document).on('click', 'a.open-popup', function() {
		// to open in good size for user
		var width = window.innerWidth /0.66 ; 
		// define the height in 
		var height = width * window.innerWidth / window.innerHeight; 
		// Ratio the hight to the width as the user screen ratio
		var windowObjectReference = window.open(this.href, ''+$(this).attr('data-strwindname')+'', 'width=900, height=700, top=300, left=150,scrollbars=yes'); 
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
