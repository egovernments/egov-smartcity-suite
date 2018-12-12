/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
var openedWindows = [];
$(document).ready(function () {
    // jQuery plugin to prevent double submission of forms
    $.fn.preventDoubleSubmission = function () {
        $(this).on('submit', function (e) {
            var $form = $(this);

            if ($form.data('submitted') === true) {
                // Previously submitted - don't submit again
                e.preventDefault();
            } else {
                // Mark it so that the next submit can be ignored
                $form.data('submitted', true);
            }
        });
        // Keep chainability
        return this;
    };

    $.fn.serializeObject = function () {
        var obj = {};
        $.each(this.serializeArray(), function (i, o) {
            var n = o.name, v = o.value;
            obj[n] = obj[n] === undefined ? v : $.isArray(obj[n]) ? obj[n].concat(v) : [obj[n], v];
        });
        return obj;
    };

    $('form').preventDoubleSubmission();

    $(".is_valid_number").on("input", function () {
        var regexp = /[^0-9]/g;
        if ($(this).val().match(regexp)) {
            $(this).val($(this).val().replace(regexp, ''));
        }
    });
    $(".is_valid_alphabetWithsplchar").on("input", function () {
        var regexp = /[^A-Z_-]*$/g;
        if ($(this).val().match(regexp)) {
            $(this).val($(this).val().replace(regexp, ''));
        }
    });
    $(".is_valid_alphabet").on("input", function () {
        var regexp = /[^a-zA-Z ]/g;
        if ($(this).val().match(regexp)) {
            $(this).val($(this).val().replace(regexp, ''));
        }
    });

    $(".is_valid_alphaNumWithsplchar").on("input", function () {
        var regexp = /[^a-zA-Z0-9_@./#&+-]*$/;
        if ($(this).val().match(regexp)) {
            $(this).val($(this).val().replace(regexp, ''));
        }
    });

    $(".is_valid_alphanumeric").on("input", function () {
        var regexp = /[^a-zA-Z _0-9]/g;
        if ($(this).val().match(regexp)) {
            $(this).val($(this).val().replace(regexp, ''));
        }
    });
    $(".is_valid_letters_space_hyphen_underscore").on("input", function () {
        var regexp = /[^a-zA-Z _0-9_-]/g;
        if ($(this).val().match(regexp)) {
            $(this).val($(this).val().replace(regexp, ''));
        }
    });
    try {
        $('.twitter-typeahead').css('display', 'block');
    } catch (e) {
    }

    try {
        $(":input").inputmask();
    } catch (e) {
    }

    try {
        $(".datepicker").datepicker({
            format: "dd/mm/yyyy",
            autoclose: true
        });

        var d = new Date();
        var currDate = d.getDate();
        var currMonth = d.getMonth();
        var currYear = d.getFullYear();
        var startDate = new Date(currYear, currMonth, currDate);
        $('.today').datepicker('setDate', startDate);

    } catch (e) {
    }

    try {
        $('[data-toggle="tooltip"]').tooltip({
            'placement': 'bottom'
        });
    } catch (e) {
    }

    try {

        $('.select2').select2({
            placeholder: "Select",
            minimumResultsForSearch: 1,
            width: '100%'
        });

        $('select').on('select2:close', function (evt) {
            $(this).focus();
        });

    } catch (e) {
    }

    $("a.open-popup").click(function (e) {
        window.open(this.href, '' + $(this).attr('data-strwindname') + '', 'width=900, height=700, top=300, left=260,scrollbars=yes');
        return false;
    });

    $(document).on('click', 'a.open-popup', function (e) {
        window.open(this.href, '' + $(this).attr('data-strwindname') + '', 'width=900, height=700, top=300, left=260,scrollbars=yes');
        return false;
    });

    $("form.form-horizontal[data-ajaxsubmit!='true']").submit(function (event) {
        $('.loader-class').modal('show', {backdrop: 'static'});
    });

    //fade out success message
    $(".alert-success").fadeTo(5000, 500).slideUp(500, function () {
        $(".alert-success").alert('close');
    });

    var elements = document.querySelectorAll('input,select,textarea');

    for (var i = 0; i < elements.length; i++) {
        if (elements[i].addEventListener) {
            elements[i].addEventListener('invalid', function () {
                offsettoinvalid();
            });
        } else if (elements[i].attachEvent) {
            elements[i].attachEvent('invalid', function () {
                offsettoinvalid();
            });
        }
    }

    function offsettoinvalid() {
        off = (elements[0].offsetTop + 50);
        $('html, body').animate({scrollTop: off}, 0);
    }

    try {
        jQuery.extend(jQuery.validator.messages, {
            required: "Required"
        });
    } catch (e) {
    }

    $('.signout').click(function () {
        $.each(openedWindows, function (i, val) {
            var window = val;
            window.close();
        });
    });


    //check file name and format is valid
    var fileNamePattern = "^[\\w\\[\\]\\(\\)\\-\\s]{1,245}\\.(0){1,9}";
    $('input:file').change(function () {
        var acceptableExtensions = $(this).data("accepts");
        if (acceptableExtensions) {
            var file = $(this);
            var fileName = file.val().split(/[\\/]/g).pop();
            var acceptableFileNamePattern = new RegExp(fileNamePattern.replace("0", acceptableExtensions), "ig");
            if (!fileName.match(acceptableFileNamePattern)) {
                file.replaceWith(file.val('').clone(true))
                bootbox.alert({
                    title: "Invalid file, ensure the file name and format is valid.",
                    message: "File name must be less than <i style='font-weight: bold'>256</i> character long.<br/>" +
                        "Should not contain any special characters except <i style='font-weight: bold'>- _ ] [ ) ( and space</i>.<br/>" +
                        "Accepts only <i style='font-weight: bold'>" + acceptableExtensions + "</i> formats."
                });
                return false;
            }
        }
    });

    //check file size is valid
    $('input:file').change(function () {
        if ($(this).data("size")) {
            var file = $(this);
            var maxFileSize = parseInt($(this).data("size"));
            if (file.get(0).files.length) {
                var fileSize = (this.files[0].size / 1024 / 1024).toFixed(0);
                if (fileSize > maxFileSize) {
                    bootbox.alert({
                        title: "Invalid file, size exceeded.",
                        message: "File could not be uploaded, it is larger than the maximum allowed file size.<br/>" +
                            "(<i style=\'font-weight: bold\'>Uploaded : " + fileSize + " MB, Allowed : " + maxFileSize + " MB</i>)"
                    });
                    file.replaceWith(file.val('').clone(true));
                    return false;
                }
            }
        }
    });

    //Add tooltip about the file size and the file extension allowed
    $('input:file').hover(function () {
        if ($(this).data("accepts") && $(this).data("size")) {
            $(this).attr('title', 'Accepts only file with extension [' + $(this).data("accepts")
                + '], file size upto [' + $(this).data("size") + 'MB] and file name length upto [255]');
            $(this).tooltip();
        }
    });
});

function showValidationMessage(errorJson) {
    let errorMsg = "";
    $.each(errorJson, function (key, value) {
        let label = $("label[for='" + key + "']").text();
        errorMsg += label + " " + value + ".<br/>";
    });
    bootbox.alert({title: "Validation Error", message: errorMsg});
}

function DateValidation(start, end) {
    if (start != "" && end != "") {
        var stsplit = start.split("/");
        var ensplit = end.split("/");

        start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
        end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];

        return ValidRange(start, end);
    } else {
        return true;
    }
}

function ValidRange(start, end) {
    var retvalue = false;
    var startDate = Date.parse(start);
    var endDate = Date.parse(end);

    // Check the date range, 86400000 is the number of milliseconds in one day
    var difference = (endDate - startDate) / (86400000 * 7);
    if (difference < 0) {
        bootbox.alert("Start date must come before the end date.");
    } else {
        retvalue = true;
    }
    return retvalue;
}

//Typeahead event handling
$.fn.getCursorPosition = function () {
    var el = $(this).get(0);
    var pos = 0;
    var posEnd = 0;
    if ('selectionStart' in el) {
        pos = el.selectionStart;
        posEnd = el.selectionEnd;
    } else if ('selection' in document) {
        el.focus();
        var Sel = document.selection.createRange();
        var SelLength = document.selection.createRange().text.length;
        Sel.moveStart('character', -el.value.length);
        pos = Sel.text.length - SelLength;
        posEnd = Sel.text.length;
    }
    return [pos, posEnd];
};

function typeaheadWithEventsHandling(typeaheadobj, hiddeneleid, dependentfield) {
    typeaheadobj.on('typeahead:selected', function (event, data) {
        //setting hidden value
        $(hiddeneleid).val(data.value);
    }).on('keydown', this, function (event) {
        var e = event;

        var position = $(this).getCursorPosition();
        var deleted = '';
        var val = $(this).val();
        if (e.which == 8) {
            if (position[0] == position[1]) {
                if (position[0] == 0)
                    deleted = '';
                else
                    deleted = val.substr(position[0] - 1, 1);
            } else {
                deleted = val.substring(position[0], position[1]);
            }
        } else if (e.which == 46) {
            var val = $(this).val();
            if (position[0] == position[1]) {

                if (position[0] === val.length)
                    deleted = '';
                else
                    deleted = val.substr(position[0], 1);
            } else {
                deleted = val.substring(position[0], position[1]);
            }
        }

        if (deleted) {
            $(hiddeneleid).val('');
            cleardependentfield(dependentfield);
        }

    }).on('keypress', this, function (event) {
        //getting charcode by independent browser
        var evt = (evt) ? evt : event;
        var charCode = (evt.which) ? evt.which :
            ((evt.charCode) ? evt.charCode :
                ((evt.keyCode) ? evt.keyCode : 0));
        //only characters keys condition
        if ((charCode >= 32 && charCode <= 127)) {
            //clearing input hidden value on keyup
            $(hiddeneleid).val('');
            cleardependentfield(dependentfield);
        }
    }).on('focusout', this, function (event) {
        //focus out clear textbox, when no values selected from suggestion list
        if (!$(hiddeneleid).val()) {
            $(this).typeahead('val', '');
            cleardependentfield(dependentfield);
        }
    });
}

function cleardependentfield(dependentfield) {
    if (!dependentfield) {
        return;
    }
    if ($(dependentfield).prop("type") == 'select-one' || $(dependentfield).prop("type") == 'select-multiple') {
        $(dependentfield).empty();
    } else if ($(dependentfield).prop("type") == 'text' || $(dependentfield).prop("type") == 'textarea') {
        $(dependentfield).val('');
    }
}

function disableRefresh(e) {
    var key = (e.which || e.keyCode);
    if (e.ctrlKey) {
        if (key == 82 || key == 116)
            e.preventDefault();
    } else if (key == 116) {
        e.preventDefault();
    }

}

function preventBack() {
    history.pushState(null, null, document.URL);
    window.addEventListener('popstate', function () {
        history.pushState(null, null, document.URL);
    });
}

function select2initialize(obj, data, multiple) {

    obj.empty();

    if (!multiple)
        obj.append("<option value=''>---- Select ----</option>");

    $('.select2').select2({
        allowClear: true,
        placeholder: "---- Select ----",
        minimumResultsForSearch: 1,
        data: data,
        multiple: multiple,
        width: '100%'
    });
}