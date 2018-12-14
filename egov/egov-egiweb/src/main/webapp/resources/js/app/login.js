/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

    localStorage.clear();
    $('.recovrbtn').click(function () {
        if ($('#emailOrMobileNum').val() === "") {
            $('#emailOrMobileNoReq').show();
        } else {
            $('#originURL').val(location.origin);
            if ($(this).attr("id") == "recoveryotpbtn")
                $("#byOtp").val(true);
            else
                $("#byOtp").val(false);
            $('#forgotPasswordForm').attr('action', '/egi/login/password/recover').trigger('submit');
        }
        return false;
    });

    $("#otprecoverybtn").click(function () {
            if ($("#token").val() != "") {
                $('#otpForm').attr('action', '/egi/login/password/reset').trigger('submit');
            }
        }
    );

    $('#compsearch').click(function () {
        var compnum = $('#compsearchtxt').val();
        if (compnum !== "") {
            $('.search-error-msg').addClass('display-hide');
            window.open("/pgr/grievance/search?crn=" + compnum, "_blank");
        } else {
            $('.search-error-msg').removeClass('display-hide');
        }
    });

    var checklocation = false;

    $('#username').blur(function () {
        $('#location').find('option:gt(0)').remove();
        if ($.trim($(this).val())) {
            $.ajax({
                url: "preauth-check",
                type: 'POST',
                data: {"username": this.value},
                dataType: "json",
                success: function (preAuthCheckResponse) {
                    preAuthCheck = true;
                    if (preAuthCheckResponse.locationRequired) {
                        $("#location").attr('required', true);
                        $('#counter-section').removeClass('display-hide');
                        $.each(preAuthCheckResponse.locations, function (key, value) {
                            var opt = "<option value=" + value.id + ">" + value.name + "</option>";
                            $('#location').append(opt);
                        });
                        if ($('#location > option').length = 2)
                            $("#location").prop('selectedIndex', 1);
                    } else {
                        $('#location').attr('required', false);
                        $('#counter-section').addClass('display-hide');
                    }

                    if (preAuthCheckResponse.otpAuthRequired) {
                        $('#otp').attr('required', true);
                        $('#otp-section').removeClass('display-hide');
                        $('#otp-msg').show();
                    } else {
                        $('#otp').attr('required', false);
                        $('#otp-section').addClass('display-hide');
                        $('#otp-msg').hide();
                    }
                },
                error: function () {
                    console.log('Error while loading locations');
                }
            });
        } else {
            $('#password').val('');
            $('#location').attr('required', false);
            $('#counter-section').addClass('display-hide');
            $('#otp').attr('required', false);
            $('#otp-section').addClass('display-hide');
        }
    });

    $("#signin-action").click(function (e) {
        if ($('#signform').valid()) {
            if (!preAuthCheck) {
                $('#username').trigger('blur');
                e.preventDefault();
            }
        } else {
            e.preventDefault();
        }
    });

    $("#signform").validate({
        rules: {
            username: "required",
            password: "required"
        },
        messages: {
            username: "Please enter your Username",
            password: "Please enter your Password"
        }
    });

    if (!navigator.cookieEnabled) {
        $('#cookieornoscript').modal('show', {backdrop: 'static'});
    }
});
