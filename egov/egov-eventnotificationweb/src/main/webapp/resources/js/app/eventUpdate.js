$(document).ready(function(){
		
	$("#eventType").val($("#eventTypeHid").val);
	$("#startHHHid").val($("#startHHH").val);
	$("#startMMHid").val($("#startMMH").val);
	$("#endHHHid").val($("#endHHH").val);
	$("#endMMHid").val($("#endMMH").val);
	
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