/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

$(document).ready(function () {

    if ($('#mode').val() == 'duplicateOrError') {
        $('#escalationDiv').removeClass('hidden');
    }
    if ($('#mode').val() == 'dataFound') {
        $('#escalationDiv').removeClass('hidden');
    }
    $("#escalationnewEscalation").click(function () {
        $("#noescalationDataFoundDiv").hide();
        $("#escalationDiv").show();
        $('#escalationDiv').removeClass('hidden');
    });

    $("#addNewRow")
        .click(
            function () {
                var currentIndex = $("#escalationTable tr").length;
                $("#escalationTable tbody")
                    .append(
                        '<tr> <td> '
                        + currentIndex
                        + '</td> <td> <input type="text" id="escalationDescription'
                        + (currentIndex - 1)
                        + '"  class="form-control is_valid_alphanumeric escalationDescription'
                        + (currentIndex - 1)
                        + '"  name="escalationList['
                        + (currentIndex - 1)
                        + '].designation.name" required="required" ></td> <td > <input type=hidden id="escalationFormId'
                        + (currentIndex - 1)
                        + '" name="escalationList['
                        + (currentIndex - 1)
                        + '].designation.id" > <input type=hidden id="escalationId" name="escalationList['
                        + (currentIndex - 1)
                        + '].id" > <input type="text" class="form-control is_valid_number"  maxlength="5" name="escalationList['
                        + (currentIndex - 1)
                        + '].noOfHrs" required="required"></td> <td> <button type="button" onclick="deleteRow(this)" id="Add" class="btn btn-success">Delete </button> </td> </tr>');
                calltypeahead(currentIndex - 1);
                numericonstraint();
            });
    function numericonstraint() {
        $(".is_valid_number").on("input", function () {
            var regexp = /[^0-9]/g;
            if ($(this).val().match(regexp)) {
                $(this).val($(this).val().replace(regexp, ''));
            }
        });
    }

    function calltypeahead(currentIndex) {
        $(".escalationDescription" + currentIndex).typeahead({
            hint: true,
            highlight: true,
            minLength: 3
        }, {
            displayKey: 'name',
            source: designations.ttAdapter()
        }).on(
            'typeahead:selected',
            function (event, data) {
                $("#escalationFormId" + currentIndex).val(
                    data.value);
            })
            .on(
                'change',
                function (event, data) {
                    if ($("#escalationDescription" + currentIndex).val() == '') {
                        $("#escalationFormId" + currentIndex).val('');
                    }
                });
    }


    $("#escalationTimeSearch").click(
        function () {
            $('#searchEscalationTimeForm').attr('method', 'post');
            $('#searchEscalationTimeForm').attr('action', '/pgr/complaint/escalationtime');
        });
    $("#saveEscalationTime")
        .click(
            function (e) {
                if (!checkUniqueDesignationSelected()) {
                    bootbox.alert("Same designation selected in multiple row. Select unique designation.");
                    e.preventDefault();
                } else {
                    $('#saveEscalationTimeForm').attr('method', 'post');
                    $('#saveEscalationTimeForm').attr('action', '/pgr/complaint/escalationtime/update');
                }
            });


    function checkUniqueDesignationSelected() {
        var u = {}, a = [];
        var amountOfRows = $("#escalationTable tr").length;

        if (amountOfRows == 1)
            return false;

        var i;
        for (i = 0; i < (amountOfRows - 1); i++) {

            if (u.hasOwnProperty($("#escalationFormId" + i)
                    .val())) {
                continue;
            }
            a.push($("#escalationFormId" + i).val());
            u[$("#escalationFormId" + i).val()] = 1;
        }

        if (a.length != (amountOfRows - 1)) {
            return false;
        }

        return true;
    }

    var complaintType = new Bloodhound(
        {
            datumTokenizer: function (datum) {
                return Bloodhound.tokenizers
                    .whitespace(datum.value);
            },
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '/pgr/complaint/escalationTime/complaintTypes?complaintTypeName=%QUERY',
                filter: function (data) {
                    return $.map(data, function (ct) {
                        return {
                            name: ct.name,
                            value: ct.id
                        };
                    });
                }
            }
        });


    complaintType.initialize();

    $('#com_type').typeahead({
        hint: false,
        highlight: false,
        minLength: 3
    }, {
        displayKey: 'name',
        source: complaintType.ttAdapter()
    }).on('typeahead:selected', function (event, data) {
        $("#complaintTypeId").val(data.value);
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
            }
            else {
                deleted = val.substring(position[0], position[1]);
            }
        }
        else if (e.which == 46) {
            var val = $(this).val();
            if (position[0] == position[1]) {

                if (position[0] === val.length)
                    deleted = '';
                else
                    deleted = val.substr(position[0], 1);
            }
            else {
                deleted = val.substring(position[0], position[1]);
            }
        }

        if (deleted) {
            $("#complaintTypeId").val('');
            $("#noescalationDataFoundDiv").hide();
            $("#escalationDiv").hide();
        }

    }).on('keypress', this, function (event) {
        //getting charcode by independent browser
        var evt = (evt) ? evt : event;
        var charCode = (evt.which) ? evt.which :
            ((evt.charCode) ? evt.charCode :
                ((evt.keyCode) ? evt.keyCode : 0));
        if ((charCode >= 32 && charCode <= 127)) {
            $("#complaintTypeId").val('');
            $("#noescalationDataFoundDiv").hide();
            $("#escalationDiv").hide();
        }
    }).on('focusout', this, function (event) {
        if (!$("#complaintTypeId").val()) {
            $(this).typeahead('val', '');
            $("#complaintTypeId").val('');
            $("#noescalationDataFoundDiv").hide();
            $("#escalationDiv").hide();
        }
    });


    var designations = new Bloodhound(
        {
            datumTokenizer: function (datum) {
                return Bloodhound.tokenizers
                    .whitespace(datum.value);
            },
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '/pgr/complaint/escalationTime/ajax-approvalDesignations?designationName=%QUERY',
                filter: function (data) {
                    return $.map(data, function (ct) {
                        return {
                            name: ct.name,
                            value: ct.id
                        };
                    });
                }
            }
        });

    designations.initialize();


    $('.escalationDescription').typeahead({
        hint: true,
        highlight: true,
        minLength: 3
    }, {
        displayKey: 'name',
        source: designations.ttAdapter()
    }).on('typeahead:selected', function (event, data) {
        $("#escalationFormId").val(data.value);
    }).on('change', function (event, data) {
        bootbox.alert($('.escalationDescription').val());
        if ($('.escalationDescription').val() == '') {
            $("#escalationFormId").val('');
        }
    });

    var amountOfRows = $("#escalationTable tr").length;

    var i;
    for (i = 0; i < (amountOfRows - 1); i++) {
        calltypeahead(i);
    }
});
