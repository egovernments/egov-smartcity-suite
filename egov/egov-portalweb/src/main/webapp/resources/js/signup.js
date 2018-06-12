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

$(document).ready(function () {

    $('#otp-section,#signup-section').hide();

    $('#mobileNumber').blur(function () {
        if ($('#mobileNumber').val().length > 0 && $('#mobileNumber').val().length < 10) {
            $('#mobnumberValid').show();
            $('#mobileNumber').val("");
        } else {
            $('#mobnumberValid').hide();
        }
    });

    $('#emailId').blur(function () {
        var pattern = new RegExp("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        var email = $('#emailId').val();
        if (!pattern.test(email) && $('#emailId').val().length > 0) {
            $('#emailValid').show();
            $('#emailId').val("");
        } else {
            $('#emailValid').hide();
        }
    });

    $('.check-password').blur(function () {
        if (($('#password').val() != "") && ($('#con-password').val() != "")) {
            if ($('#password').val() === $('#con-password').val()) {
                $('.password-error').hide();
                $('.check-password').removeClass('error');
            } else {
                $('.password-error').show();
                $('.check-password').addClass('error');
                if ($('.error-check').is(':visible')) {
                    $('.error-check').hide();
                }
            }
        }
    });

    $('#password').blur(
        function () {
            $('.password-invalid').hide();
            if ($(this).val()) {
                $.ajax({
                    url: "signup/validate-pwd",
                    dataType: "json",
                    data: {"pswd": $(this).val()},
                    success: function (data) {
                        if (data) {
                            $('.password-invalid').hide();
                        } else {
                            $('.password-invalid').show();
                        }
                    },
                    error: function () {
                        console.error('Error while validating password');
                    }
                });
            }
        }
    );

    $('#name').keyup(function () {
        var arr = $(this).val().split(' ');
        var result = "";
        for (var x = 0; x < arr.length; x++)
            result += arr[x].substring(0, 1).toUpperCase() + arr[x].substring(1) + ' ';
        $(this).val(result.substring(0, result.length - 1));
    });

    $('#signupbtn').click(function (e) {
        if ($('form').valid()) {
            $('#username').val($('#mobileNumber').val());
            $('#signupform').trigger('submit');
        } else {
            e.preventDefault();
        }
    });

    $('#password, #username').popover({trigger: "focus", placement: "bottom"})

    $('#otpbtn').click(function () {
        if (!$('#mobileNumber').val()) {
            $(".mobile-error").show();
            return false;
        } else {
            $(".mobile-error").hide();
            $(this).hide();
        }
        $.ajax({
            url: "signup/otp/" + $('#mobileNumber').val(),
            dataType: "json",
            success: function (data) {
                if (data) {
                    console.log('OTP sent');
                    $('#activationcode').val('');
                    $('#signup-section,#otp-section').show();
                    $('#otpbtn-section').hide();
                }
            },
            error: function () {
				console.error('Error while sending otp');
                $(this).show();
            }
        });
    });

    $(document).on('click', '.password-view,.otp-view', function () {
        if ($(this).hasClass('password-view')) {
            if ($(this).data('view') == 'show') {
                $('.check-password').attr({type: 'text', autocomplete: 'off'});
                $(this).parent().empty().html('<i class="fa fa-eye-slash password-view" data-view="hide" aria-hidden="true"></i>');
            } else {
                $('.check-password').attr({type: 'password', autocomplete: 'new-password'});
                $(this).parent().empty().html('<i class="fa fa-eye password-view" data-view="show" aria-hidden="true"></i>');
            }
        } else if ($(this).hasClass('otp-view')) {
            if ($(this).data('view') == 'show') {
                $(this).closest('.form-group').find('input').attr({type: 'text', autocomplete: 'off'});
                $(this).parent().empty().html('<i class="fa fa-eye-slash otp-view" data-view="hide" aria-hidden="true"></i>');
            } else {
                $(this).closest('.form-group').find('input').attr({type: 'password', autocomplete: 'new-password'});
                $(this).parent().empty().html('<i class="fa fa-eye otp-view" data-view="show" aria-hidden="true"></i>');
            }
        }
    });

});