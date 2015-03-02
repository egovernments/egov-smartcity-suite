jQuery(document).ready(function($)
{
	$(".is_valid_number").on("input", function(){
        var regexp = /[^0-9]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$(".is_valid_alphabet").on("input", function(){
		var regexp = /[^a-zA-Z]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$('.twitter-typeahead').css('display','block');
	
	$(":input").inputmask();
	
    $(".datepicker").datepicker({
        format: "dd/mm/yyyy"
	});
	
	
	$(".form-horizontal").submit(function( event ) {
		$('.loader-class').modal('show', {backdrop: 'static'});
	});
	
});

