
jQuery.noConflict();

function doLoadingMask() {
    jQuery("#loadingMask").dialog({ 
        modal: true,
        width: 250,  
        height: 100,
        position: [(window.width / 2),100],
        closeOnEscape: false,
        resizable: false,
        open: function(event, ui) {
            jQuery(".ui-dialog-titlebar-close").hide();
            jQuery(".ui-dialog-titlebar").hide();   
        }
    });
}

function undoLoadingMask() {
    jQuery("#loadingMask").dialog("close");
}


jQuery(document).ready(function() {
	jQuery( "form" ).submit(function( event ) {
		doLoadingMask();
	});
});


