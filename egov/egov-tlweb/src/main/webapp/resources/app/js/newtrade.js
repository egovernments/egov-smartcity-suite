$(document).ready(function(){
    
    $('.add-attachment').click(function(){
       console.log('came');
       $(this).parent().before('<div class="col-sm-3 add-margin"> <input type="file" class="form-control" required> </div>');
    });
    
    $('.motorcheck').click(function(){
        $('.motorpart').toggle();
    });
    
});