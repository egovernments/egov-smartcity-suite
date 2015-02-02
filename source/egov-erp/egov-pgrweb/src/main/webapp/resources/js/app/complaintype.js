jQuery(document).ready(function($)
{	
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
			$(element).addClass('error');
			$(error).addClass('error-msg');
		},
		success: function (label, element) {
			$(element).removeClass('error');
		},
	});
});

