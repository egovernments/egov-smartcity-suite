
jQuery.noConflict();

function doLoadingMask() {
    jQuery("#loadingMask").dialog({ 
        modal: true,
        width: 250,  
        height: 90,
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

