$(document).ready(function(){
		
	$('#ispaid').on('change', function(event) { 
		//alert(($(this).is(':checked')));
	    if ($(this).is(':checked')) {
	        //$(this).trigger("change");
	        $("#costLabel").show();
	    	$("#costDiv").show();
	    }else{
	    	$("#costLabel").hide();
	    	$("#costDiv").hide();
	    }
	    event.preventDefault();
	});
	
	$(".btn-primary").click(function(event){
		document.getElementById("createEventform").submit(); 
		//document.forms[0].submit;
		return true;
		//event.preventDefault();
	});
	
});