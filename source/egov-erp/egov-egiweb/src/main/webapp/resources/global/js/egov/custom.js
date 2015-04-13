$(document).ready(function()
{
	$("a.open-popup").click(function(e) {
		// to open in good size for user
		var width = window.innerWidth /0.66 ; 
		// define the height in 
		var height = width * window.innerWidth / window.innerHeight; 
		// Ratio the hight to the width as the user screen ratio
		window.open(this.href, 'newwindow', 'width=900, height=700, top=300, left=150');
		return false;
		
	});
	
	$(document).on('click', 'a.open-popup', function() {
		// to open in good size for user
		var width = window.innerWidth /0.66 ; 
		// define the height in 
		var height = width * window.innerWidth / window.innerHeight; 
		// Ratio the hight to the width as the user screen ratio
		window.open(this.href, 'newwindow', 'width=900, height=700, top=300, left=150');
		return false;
	});
	
	$(".is_valid_number").on("input", function(){
        var regexp = /[^0-9]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$(".is_valid_alphabet").on("input", function(){
		var regexp = /[^a-zA-Z ]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$(".is_valid_alphaNumeric").on("input", function(){
		var regexp = /[^a-zA-Z0-9-:/]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$(".is_valid_alphanumeric").on("input", function(){
		var regexp = /[^a-zA-Z _0-9]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$('.twitter-typeahead').css('display','block');
	
	try { $(":input").inputmask(); }catch(e){}
	
	try { 
		$(".datepicker").datepicker({
			format: "dd/mm/yyyy"
		}); 
		}catch(e){
		console.warn("No Date Picker");
	}
	
	try { 
		$('[data-toggle="tooltip"]').tooltip({
			'placement': 'bottom'
		});
		}catch(e){
		console.warn("No tooltip");
	}
	
	
	$(".form-horizontal").submit(function( event ) {
		$('.loader-class').modal('show', {backdrop: 'static'});
	});
	
	
	
});

