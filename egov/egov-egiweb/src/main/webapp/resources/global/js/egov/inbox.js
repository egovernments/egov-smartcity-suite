jQuery(document).ready(function($){
	
	$(window).unload(function(){
		parent.window.opener.inboxloadmethod();
	});
	
});