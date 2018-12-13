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

$('#approvalDepartment').change(function () {
    $('#approvaldesignation').find('option:gt(0)').remove();
    $('#approverPositionId').find('option:gt(0)').remove();
    if (this.value !== '') {
        $.ajax({
            url: "/eis/ajaxWorkFlow-findDesignationsByObjectTypeAndDesignation",
            type: "GET",
            data: {
                approvalDepartment: $('#approvalDepartment').val(),
                departmentRule: $('#approvalDepartment').find("option:selected").text(),
                type: $('#stateType').val(),
                currentState: $('#currentState').val(),
                amountRule: $('#amountRule').val(),
                additionalRule: $('#additionalRule').val(),
                pendingAction: $('#pendingActions').val(),
                currentDesignation: $('#currentDesignation').val()
            },
            dataType: "json",
            success: function (response) {
                $.each(response, function (index, value) {
                    $('#approvaldesignation').append($('<option>').text(value.name).attr('value', value.id));
                });
            }
        });
    }
});

$('#approvaldesignation').change(function () {
    $('#approverPositionId').find('option:gt(0)').remove();
    $.ajax({
        url: "/eis/ajaxWorkFlow-positionsByDepartmentAndDesignation",
        type: "GET",
        data: {
            approvalDesignation: $('#approvaldesignation').val(),
            approvalDepartment: $('#approvalDepartment').val()
        },
        dataType: "json",
        success: function (response) {
            $.each(response, function (index, value) {
                $('#approverPositionId').append($('<option>').text(value.userName + ' [' + value.positionName +'] ').attr('value', value.positionId));
            });
        }
    });
});

$('#approverPositionId').change(function () {
    $('#approverName').val($('#approverPositionId').find("option:selected").text());
});



    $(".buttonsubmit").click(function () {
        var name = $(this).val();
        if (name == 'Reassign') {
            $(".reassign-screen").show();
            $('#approvalPosition').find('option:gt(0)').remove();
            var result = [];
            $.ajax({
                url: "/tl/license/reassign",
                type: "GET",
                dataType: "json",
                cache: false,
                success: function (data) {
                    $.each(data, function (i) {
                        var obj = {};
                        obj['id'] = i;
                        obj['text'] = data[i];
                        result.push(obj);
                    });
                    $.each(result, function (i) {
                        $('#approvalPosition').append($('<option>').text(result[i].text).attr('value', result[i].id));
                    })
                    $('.reassign-screen').modal('show', {backdrop: 'static'});
                },
            });
        } else {
            $("#workFlowAction").val(name);
            var approverDeptId = $("#approvalDepartment").val();
            var approverDesgId = $("#approvaldesignation").val();
            var approverPosId = $("#approverPositionId").val();
            var approverComments = $("#approvalComment").val();

            if (!approverPosId) {
                var approver = $("#approverPositionId option:selected").text();
                $("approverName").val(approver.split('~')[0]);
            }
            if (nextAction != 'END') {
                if (name == "Forward") {
                    if (!(approverDeptId && approverDesgId && approverPosId)) {
                        bootbox.alert("Please select the mandatory fields");
                        return false;
                    }
                }
            }
            if (name == "Forward" || name == "Approve" || name == "Save") {
                if (approverComments == null || approverComments == "" || approverComments.trim().length == 0) {
                    bootbox.alert("Please enter the Remarks ");
                    return false;
                }
            }
            if ((name == "Reject" || name == "Cancel")) {
                if (approverComments == null || approverComments == "" || approverComments.trim().length == 0) {
                    bootbox.alert("Please enter rejection Remarks ");
                    return false;
                }
            }
            bootbox.confirm({
                message: 'Please confirm, if you wish to ' + name + ' this application.',
                buttons: {
                    'cancel': {
                        label: 'No',
                        className: 'btn-default'
                    },
                    'confirm': {
                        label: 'Yes',
                        className: 'btn-danger'
                    }
                },
                callback: function (result) {
                    if (result) {
                        if ($('#currentState').val()) {
                            var url = '/tl/license/closure/' + name.toLowerCase() + '/' + $('#licenseId').val();
                            $("#licenseClosure").attr('action', url);
                            $("#licenseClosure").attr('method', 'post');
                        }
                        $("#licenseClosure").submit();
                    }
                }

            });
        }
    });
});

window.document.onkeydown = function (event) {
    if ($('#currentState').val()) {
        switch (event.keyCode) {
            case 116 : //F5 button
                window.location.href = '/tl/license/closure/update/' + $('#licenseId').val();
                return true;
            case 82 : //R button
                if (event.ctrlKey) { //Ctrl button
                    window.location.href = '/tl/license/closure/update/' + $('#licenseId').val();
                    return true;
                }
        }
    }
}