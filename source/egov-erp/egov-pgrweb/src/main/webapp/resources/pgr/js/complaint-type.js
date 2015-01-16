jQuery(document).ready(function($)
{
	$("#comp_type_nod").on("input", function(){
        var regexp = /[^0-9]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$("#comp_type_name").on("input", function(){
		var regexp = /[^a-zA-Z]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	
	$('form').validate({
		rules: {
            comp_type_name: {
                required: true
			},
            comp_type_dept: {
                required: true
			},
			comp_type_nod: {
                required: true
			},
            comp_type_loc: {
                required: true
			}
		},
		messages: {
			comp_type_name: {
				required: "Complaint type is required",
			},  
			comp_type_dept: {
				required: "Department is required",
			},
			comp_type_nod: {
				required: "Number of days to resolve is required"
			},
			comp_type_loc: {
				required: "Location is required"
			}
		},
		errorPlacement: function(error, element) {
			if (element.attr("type") == "radio") {
				error.insertBefore(element);
				} else {
				error.insertAfter(element);
			}
			$(element).css('border','1px solid red');
			$(error).css('color','red');
		},
		success: function (label, element) {
            $(element).css('border','1px solid #ebebeb');
		},
		//just to block submit for demo
		submitHandler: function(form) {
			alert("valid form");
			return false;
		}
	});
	
});