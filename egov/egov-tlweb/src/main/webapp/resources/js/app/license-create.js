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

var parentBoundary;
var adminWard;
$(document).ready(function () {

    //focus to error field if server side binding error is their
    $("span.tradelicenceerror").each(function () {
        if ($(this).html()) {
            var validate = $(this).attr('id').split(".")[0];
            $("*[name=" + validate + "]").focus();
        }
        return;
    })

    if ($('#category').val() != '') {
        $('#category').trigger('change');
    }

    $('#subCategory').change(function () {
        $('#uom').val('');
        $.ajax({
            url: "/tl/licensesubcategory/detail-by-feetype",
            type: "GET",
            data: {
                subCategoryId: $('#subCategory').val(),
                feeTypeId: $('#feeTypeId').val()
            },
            cache: false,
            dataType: "json",
            success: function (response) {
                if (response)
                    $('#uom').val(response.uom.name);
                else {
                    $('#uom').val('');
                    bootbox.alert("No UOM mapped for the selected Sub Category");
                }
            }
        });
    });

    if ($('#boundary').val() != '') {
        $('#boundary').trigger('blur');
    }

    if ($("#propertyNo") && $("#propertyNo").val() !== "") {
        getPropertyDetails();
    }

    if ($("#agreementDate").val())
        $("#showAgreementDtl").prop("checked", true);

    showHideAgreement();

    $('#licenseForm :input').not(':hidden').not(':button').each(function (input, item) {
        if ($(item).closest('#workflowDiv').length == 0)
            $(item).attr('disabled', true);
    });

    var fields = $("#enableFields").val();
    fields = fields.split(",");
    if (fields == 'all')
        $('#licenseForm :input').each(function (input, item) {
            $(item).attr('disabled', false);
        });
    else if (fields != 'none')
        $.each(fields, function (key, value) {
            $('#' + (value)).attr('disabled', false);
        });

    var patern = /[^a-zA-Z0-9 _./(),-]/g;
    $('.newpatternvalidation').on("input", function () {
        if ($(this).val().match(patern)) {
            $(this).val($(this).val().replace(patern, ''));
        }
    });

    //taking agreementdate less than commencementdate
    $('#startDate').change(function () {
        var dt = $("#startDate").datepicker('getDate');
        var agrementdate = new Date(dt.getFullYear(), dt.getMonth(), dt.getDate() - 1);
        $("#agreementDate").datepicker('setEndDate', agrementdate);

    })

});

//focus to tab if error occur
$('form').validate({
    ignore: ".ignore",
    invalidHandler: function (e, validator) {
        if (validator.errorList.length)
            $('#settingstab a[href="#' + $(validator.errorList[0].element).closest(".tab-pane").attr('id') + '"]').tab('show');
        validator.errorList[0].element.focus();
    }
});

$('#boundary').blur(function () {
    $('#parentBoundary').find('option:gt(0)').remove();
    $('#adminWard').find('option:gt(0)').remove();
    if (this.value !== '') {
        var parentBoundaryId;
        if ($('#parentBoundary').data('selected-id'))
            parentBoundaryId = $('#parentBoundary').data('selected-id');
        else
            parentBoundaryId = parentBoundary;
        $.ajax({
            type: "GET",
            url: "/egi/boundary/ajaxBoundary-blockByLocality",
            cache: true,
            dataType: "json",
            data: {'locality': this.value},
            success: function (response) {
                if (response.length < 1) {
                    bootbox.alert("Could not find ward for Locality : " +
                        $('#boundary').find(":selected").text());
                    $('#boundary').val('');
                    return;
                }
                $.each(response.results.boundaries, function (key, boundary) {
                    $('#parentBoundary').append('<option '
                        + (boundary.wardId === parentBoundaryId ? 'selected="selected"' : "")
                        + 'value="' + boundary.wardId + '">' + boundary.wardName + '</option>');
                });
                $('#parentBoundary').removeAttr('data-selected-id');
            }
        });

        var adminWardId;
        if ($('#adminWard').data('selected-id'))
            adminWardId = $('#adminWard').data('selected-id');
        else
            adminWardId = adminWard;
        $.ajax({
            type: "GET",
            url: "/egi/boundary/ward-bylocality",
            cache: true,
            dataType: "json",
            data: {'locality': this.value},
            success: function (response) {
                if (response.length < 1) {
                    bootbox.alert("Could not find ward for Locality : " +
                        $('#boundary').find(":selected").text());
                    $('#boundary').val('');
                    return;
                }
                $.each(response, function (key, boundary) {
                    $('#adminWard').append('<option '
                        + (boundary.wardId === adminWardId ? 'selected="selected"' : "")
                        + 'value="' + boundary.wardId + '">' + boundary.wardName + '</option>');
                });
                $('#adminWard').removeAttr('data-selected-id');
            }
        });
    }
});

$('#category').change(function () {
    var val = $(this).val();
    if (val !== '') {
        var results = [];
        $.ajax({
            type: "GET",
            url: '/tl/licensesubcategory/by-category',
            data: {categoryId: val},
            dataType: "json",
            success: function (data) {
                $.each(data, function (i) {
                    var obj = {};
                    obj['id'] = data[i]['id']
                    obj['text'] = data[i]['name'];
                    results.push(obj);
                });
                select2initialize($("#subCategory"), results, false);

                $('[name="tradeName"]').val($('[name="tradeName"]').data('selected-id')).trigger('change');
            },
            error: function () {
                bootbox.alert('something went wrong on server');
            }
        });
    }
});

function getPropertyDetails() {
    var propertyNo = $("#propertyNo").val();
    if (propertyNo != "" && propertyNo != null) {
        $.ajax({
            url: "/ptis/rest/property/" + propertyNo,
            type: "GET",
            contentType: "application/x-www-form-urlencoded",
            success: function (data) {
                if (data.errorDetails && data.errorDetails.errorCode != null && data.errorDetails.errorCode != '') {
                    $('#propertyNo').val('');
                    bootbox.alert(data.errorDetails.errorMessage);
                } else {
                    if (data.boundaryDetails != null) {
                        $("#boundary").val(data.boundaryDetails.localityId);
                        parentBoundary = data.boundaryDetails.wardId;
                        adminWard = data.boundaryDetails.adminWardId;
                        $("#boundary").trigger('blur');
                        $("#address").val(data.propertyAddress);
                        $("#boundary").attr('disabled', true);
                        $("#parentBoundary").attr('disabled', true);
                        $("#adminWard").attr('disabled', true);
                        $("#address").attr("readonly", true);

                    }
                }
            },
            error: function (e) {
                $('#propertyNo').val('');
                bootbox.alert("Error getting property details");
            }
        });
    }
    $("#boundary").removeAttr("disabled");
    $("#parentBoundary").removeAttr("disabled");
    $("#adminWard").removeAttr("disabled");
    $("#address").removeAttr("readonly");
}

function showHideAgreement() {
    if (document.getElementById("showAgreementDtl").checked) {
        document.getElementById("agreementSec").style.display = "";
        $("#agreementDate").attr("required", true);
        $("#agreementDocNo").attr("required", true);
    } else {
        document.getElementById("agreementSec").style.display = "none";
        $("#agreementDate").val('');
        $("#agreementDocNo").val('');
        $("#agreementDate").attr("required", false);
        $("#agreementDocNo").attr("required", false);
    }
}

$(".savebutton").click(function () {
    if ($('#licenseForm').valid()) {
        if ($("#approvalComment").val() == "" || $("#approvalComment").val() == null) {
            bootbox.alert("Please enter approver remarks");
            return false;
        }
        else {
            $("#licenseForm").submit();
            return true;
        }
    }
    else return false;
})
