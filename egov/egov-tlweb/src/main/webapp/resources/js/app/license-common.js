/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

function refreshInbox() {
    if (opener && opener.top.document.getElementById('inboxframe')) {
        opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
    } else if (opener && opener.opener && opener.opener.top.document.getElementById('inboxframe')) {
        opener.opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
    }
}

$(document).ready(function () {

    $('form').validate({
        ignore: [],
        invalidHandler: function (e, validator) {
            if (validator.errorList.length)
                $('#settingstab a[href="#' +
                    $(validator.errorList[0].element).closest(".tab-pane").attr('id') + '"]').tab('show');
            validator.errorList[0].element.focus();
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
                },
                error: function () {
                    bootbox.alert('something went wrong on server');
                }
            });
        }
    });
});

function downloadDigisignedLicenseCertificate(signedFileStoreId) {
    var params = [
        'height=' + screen.height,
        'width=' + screen.width,
        'fullscreen=yes'
    ].join(',');
    window.open('/tl/digitalSignature/tradeLicense/downloadSignedLicenseCertificate?signedFileStoreId=' + signedFileStoreId, "NoticeWindow", params);
}

function showMessage(id, msg) {
    $('#' + id).show();
    $('#' + id).text(msg);
}
function clearMessage(id) {
    $('#' + id).hide();
    $('#' + id).text('');
}

function isValidEmailAddress(emailAddress) {
    var pattern = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
    return pattern.test(emailAddress);
};

function toggleFields(flag, excludedFields) {
    for (var i = 0; i < document.forms[0].length; i++) {
        document.forms[0].elements[i].disabled = flag;
    }
    for (var j = 0; j < excludedFields.length; j++) {
        if ($('#' + excludedFields[j])) {
            //Since id can contains space and special character jquery can find by id
            var element = document.getElementById(excludedFields[j]);
            $(element).removeAttr("disabled");
        }
    }
}

