jQuery.noConflict();
jQuery(document).ready(function(){
    
	jQuery('.add-attachment').click(function(){
       console.log('came');
       jQuery(this).parent().before('<div class="col-sm-3 add-margin"> <input type="file" class="form-control" required> </div>');
    });
    
	jQuery('.motorcheck').click(function(){
    	jQuery('.motorpart').toggle();
    });
    
});