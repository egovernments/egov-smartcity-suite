jQuery(document).ready(function ($) {
    $("#comp_type_nod").on("input", function () {
        var regexp = /[^0-9]/g;
        if ($(this).val().match(regexp)) {
            $(this).val($(this).val().replace(regexp, ''));
        }
    });

    $("#comp_type_name").on("input", function () {
        var regexp = /[^a-zA-Z]/g;
        if ($(this).val().match(regexp)) {
            $(this).val($(this).val().replace(regexp, ''));
        }
    });

    $('form').validate({
        rules: {
            comp_type_name: {
                required: true
            },
            comp_type_dept: {
                required: true
            }
        },
        messages: {
            comp_type_name: {
                required: "Complaint Type is required"
            },
            comp_type_dept: {
                required: "Department is required"
            }
        },
        errorPlacement: function (error, element) {
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
        }
    });

});