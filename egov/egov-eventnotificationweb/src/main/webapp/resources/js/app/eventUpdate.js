$(document).ready(function(){
			
	if($("#ispaidHid").val() == "true"){
		$('#ispaid')[0].checked = true;
		$("#costLabel").show();
    	$("#costDiv").show();
	}
	
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
		document.getElementById("updateEventform").submit();
		return true;
		//event.preventDefault();
	});
	
});