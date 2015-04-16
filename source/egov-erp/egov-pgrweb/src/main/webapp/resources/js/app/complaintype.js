jQuery(document).ready(function($) {
	$('form').validate({
		rules : {
			comp_type_name : {
				required : true
			},
			comp_type_dept : {
				required : true
			},
			comp_type_nod : {
				required : true
			},
			comp_type_loc : {
				required : true
			},
			comp_type_code : {
				required : true
			},
			comp_type_isactive : {
				required : true
			}
		},
		messages : {
			comp_type_name : {
				required : "Complaint type is required",
			},
			comp_type_dept : {
				required : "Department is required",
			},
			comp_type_nod : {
				required : "Number of days to resolve is required"
			},
			comp_type_loc : {
				required : "Location is required"
			},
			comp_type_code : {
				required : "Complaint code is required"
			},
			comp_type_isactive : {
				required : "Is active is required"
			}
		},
		errorPlacement : function(error, element) {
			if (element.attr("type") == "radio") {
				error.insertBefore(element);
			} else {
				error.insertAfter(element);
			}
			$(element).addClass('error');
			$(error).addClass('error-msg');
		},
		success : function(label, element) {
			$(element).removeClass('error');
		},
	});

	$('input[type="checkbox"]').click(function() {
		if ($(this).is(":checked")) {
			$(this).val(true);
		} else if ($(this).is(":not(:checked)")) {
			$(this).val(false);
		}
	});

	$("#comp_type_loc_yes").attr('checked', 'checked');
	$("#buttonCreate").click(function() {
		$('#complaintTypeViewForm').attr('method', 'get');
		$('#complaintTypeViewForm').attr('action', '/pgr/complainttype/create');
	});
	$("#buttonEdit").click(function() {
		var action = '/pgr/complainttype/update/' + $('#compTypeName').val()
		$('#complaintTypeViewForm').attr('method', 'get');
		$('#complaintTypeViewForm').attr('action', action);
	});
});
