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

    $('#create-btn,#save-btn').click(function () {
        if ($('#licenseAppType').val() === '') {
            bootbox.alert("Please select an Application Type");
            return false;
        }
        $('.licapptype').prop("value", $('#licenseAppType').val());
        return true;
    });

    $('#result tbody').on('click', 'tr td .delete-row', function () {
        var id = $(this).closest('tr').find('td:eq(0) .detailId').val();
        var idx = $(this).closest('tr').index();
        var obj = $(this);
        if (idx == 0) {
            bootbox.alert('Cannot delete first row!');
        } else if ((idx < ($('#result tbody tr:visible').length - 1))) {
            bootbox.alert('Try to delete from last row!');
        } else {
            bootbox.confirm("Do you want to delete this penalty rate ? ", function (result) {
                if (result) {

                    if (obj.data('func')) {
                        obj.closest('tr').remove();
                    } else {
                        if (obj.closest('tr').hasClass('dynamicInput'))
                            obj.closest('tr').remove();
                        else
                            obj.closest('tr').hide().find('input.markedForRemoval').val('true');

                    }
                }
            });
        }
    });

    $("#addrow").click(function (event) {
        var rowCount = $('#result tbody tr').length;
        var valid = true;
        //validate all rows before adding new row
        $('#result tbody tr').each(function (index, value) {
            $('#result tbody tr:eq(' + index + ') td input[type="text"]').each(function (i, v) {
                if (!$.trim($(v).val())) {
                    valid = false;
                    bootbox.alert("Enter all values for existing rows!", function () {
                        $(v).focus();
                    });
                    return false;
                }
            });
        });
        if (valid) {
            //Create new row
            var newRow = $('#result tbody tr:first').clone();
            newRow.find("input").each(function () {
                $(this).attr({
                    'name': function (_, name) {
                        return name.replace(/\[.\]/g, '[' + rowCount + ']');
                    }
                });
            });
            $('#result tbody').append(newRow);
            $('#result tbody tr:last').addClass('dynamicInput');
            var prev_tovalue = $('#result tbody tr:eq(' + (rowCount - 1) + ')').find('input.tovalue').val();
            var currentRowObj = $('#result tbody tr:last');
            currentRowObj.find('input').val('');
            currentRowObj.find('input.fromvalue').val(prev_tovalue);
            currentRowObj.find('input.markedForRemoval').val('false');
            patternvalidation();
        }
    });

    $("#search-create-btn").click(function () {
        let appType = $('#licenseAppType').val();
        if (appType === '') {
            bootbox.alert("Select a License Application Type");
            $('#rates').hide();
        } else {
            $.ajax({
                url: "create/" + appType,
                type: "GET",
                success: function (data) {
                    if (data == true) {
                        bootbox.alert("Penalty Rates already defined for " + $('#licenseAppType option:selected').text());
                        $('#rates').hide();
                    } else {
                        $('#rates').show();
                    }
                },
                error: function () {
                    $('.rates').hide();
                }
            });
        }
    });

    $("#search-btn").click(function () {
        let appType = $('#licenseAppType').val();
        if (appType === '') {
            bootbox.alert("Select a License Application Type");
            $('#rates').hide();
        } else {
            $('#rates').show();
            oTable = $('#resultTable').DataTable({
                processing: true,
                serverSide: false,
                "bPaginate": false,
                sort: false,
                filter: false,
                ajax: {
                    type: "POST",
                    data: {
                        applicationTypeId: appType
                    }
                },
                "autoWidth": false,
                "bDestroy": true,
                columns: [
                    {
                        "data": "fromRange",
                        "sClass": "text-right"
                    }, {
                        "data": "toRange",
                        "sClass": "text-right"
                    }, {
                        "data": "rate",
                        "sClass": "text-right"
                    }
                ]
            });
        }
    });

    $("#edit-btn").click(function () {
        let appType = $('#licenseAppType').val();
        if (appType === '') {
            bootbox.alert("Select a License Application Type");
        } else {
            window.open("/tl/penaltyrates/update/" + appType, appType, 'width=900, height=700, top=300, left=260,scrollbars=yes');
        }
    });
});
