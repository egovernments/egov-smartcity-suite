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

$(document)
    .ready(
        function () {
            if ($('#mode').val() == 'duplicateOrError') {
                jQuery('#escalationDiv').removeClass('hidden');
            }
            if ($('#mode').val() == 'dataFound') {
                jQuery('#escalationDiv').removeClass('hidden');
            }
            $("#escalationnewEscalation").click(function () {
                $("#noescalationDataFoundDiv").hide();
                $("#escalationDiv").show();
                jQuery('#escalationDiv').removeClass('hidden');
            });


            tableContainer1 = $("#escalation-table");

            $('#escalationSearch').click(function (e) {
                callajaxdatatable(e);
            });

            function callajaxdatatable(e) {
                var cmTypeId = 0;
                var posId = 0;
                if ($('#grievanceType').val() == "")
                    cmTypeId = 0;
                else
                    cmTypeId = $('#grievanceType').val();

                if ($('#positionId').val() == "")
                    posId = 0;
                else
                    posId = $('#positionId').val();
                $('.report-section').removeClass('display-hide');
                tableContainer1
                    .dataTable({
                        ajax: {
                            type: "GET",
                            async: false,
                            url: "/pgr/complaint/escalation/view/",
                            data: {
                                positionId: posId,
                                complaintId: cmTypeId
                            }
                        },
                        "aLengthMenu": [[10, 25, 50, -1],
                            [10, 25, 50, "All"]],
                        dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
                        "autoWidth": false,
                        "bDestroy": true,
                        buttons: [{
                            extend: 'pdf',
                            title: 'Escalation View',
                            filename: 'Escalation View',
                            orientation: 'landscape',
                            footer: true,
                            pageSize: 'A4'
                        }, {
                            extend: 'excel',
                            filename: 'Escalation View',
                            footer: true
                        }],
                        columns: [{
                            "data": "grievanceType"
                        }, {
                            "data": "positionFrom"
                        }, {
                            "data": "positionTo"
                        }, {
                            "data": "positionFromId",
                            "visible": false
                        }, {
                            "data": "id",
                            "visible": false
                        }
                        ]
                    });
                e.stopPropagation();
            }

            $('#escalation-table').on('click', 'tbody tr', function (event) {
                if ($(event.target).attr('class') == 'btn btn-xs btn-secondary edit-escalation') {
                    var fid = tableContainer1.fnGetData(this, 5);
                    window.open('/pgr/escalation/view/' + fid, '', 'height=800,width=800,scrollbars=yes,resizable=yes');
                }
            });

            var complaintType = new Bloodhound(
                {
                    datumTokenizer: function (datum) {
                        return Bloodhound.tokenizers
                            .whitespace(datum.value);
                    },
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    remote: {
                        url: '/pgr/complaint/complaintTypes?complaintTypeName=%QUERY',
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

            var com_type_typeahead = $('#com_type').typeahead({
                hint: false,
                highlight: false,
                minLength: 3
            }, {
                displayKey: 'name',
                source: complaintType.ttAdapter()
            });
            typeaheadWithEventsHandling(com_type_typeahead, '#complaintTypeId');

            var com_subtype_typeahead = $('#com_subtype').typeahead({
                hint: true,
                highlight: true,
                minLength: 3
            }, {
                displayKey: 'name',
                source: complaintType.ttAdapter()
            });
            typeaheadWithEventsHandling(com_subtype_typeahead, '#grievanceType');

            var position = new Bloodhound({
                datumTokenizer: function (datum) {
                    return Bloodhound.tokenizers.whitespace(datum.value);
                },
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                remote: {
                    url: '/pgr/complaint/escalation/position?positionName=%QUERY',
                    filter: function (data) {
                        return $.map(data, function (pos) {
                            return {
                                name: pos.name,
                                value: pos.id
                            };
                        });
                    }
                }
            });

            position.initialize();
            var com_position_typeahead = $('#com_position').typeahead({
                hint: false,
                highlight: false,
                minLength: 3
            }, {
                displayKey: 'name',
                source: position.ttAdapter()
            });
            typeaheadWithEventsHandling(com_position_typeahead, '#positionId');

            $("#boundary_type_id").change(function () {
                $('#com_boundry').typeahead('destroy');
                var b_id = $("#boundary_type_id").val();
                var boundaries = new Bloodhound(
                    {
                        datumTokenizer: function (datum) {
                            return Bloodhound.tokenizers
                                .whitespace(datum.value);
                        },
                        queryTokenizer: Bloodhound.tokenizers.whitespace,
                        remote: {
                            url: '/pgr/complaint/escalation/boundaries-by-type?boundaryName=%QUERY&boundaryTypeId=' + b_id,
                            filter: function (data) {
                                return $.map(data, function (boundList) {
                                    return {
                                        name: boundList.name,
                                        value: boundList.id
                                    };
                                });
                            }
                        }
                    });
                boundaries.initialize();
                $('#com_boundry').typeahead({
                    hint: true,
                    highlight: true,
                    minLength: 3
                }, {
                    displayKey: 'name',
                    source: boundaries.ttAdapter()
                }).on('typeahead:selected', function (event, data) {
                    $("#boundaryId").val(data.value);
                }).on('change', function (event, data) {
                    if ($('#com_boundry').val() == '') {
                        $("#boundaryId").val('');
                    }
                });
            });


            $(".btn-add")
                .click(
                    function () {
                        var currentIndex = $("#escalationTable tr").length;

                        var fromName = $('#escalationHierarchyfromPositionName' + (currentIndex - 2)).val();
                        var toName = $('#escalationHierarchyToPositionid' + (currentIndex - 2) + ' option:selected').text();

                        if (fromName == "" || fromName == null) {
                            bootbox.alert('Heirarchy From position is mandatory in the last row');
                        } else if (toName == "" || toName == "Select" || toName == null) {
                            bootbox.alert('Heirarchy to position is mandatory in the last row');
                        }
                        else if (fromName == toName) {
                            bootbox.alert('Heirarchy from position and To position are same in single line.');
                            $('#escalationHierarchyToPositionid' + (currentIndex - 2)).val('');
                        }
                        else {
                            addNewRowToTable(currentIndex);
                            calltypeahead(currentIndex - 1);
                        }
                    });

            $('#escalationCreateSearch').click(function () {
                $('#viewEscalation').attr('method', 'post');
                $('#viewEscalation').attr('action', '/pgr/complaint/escalation/search');
            });

        });

function calltypeahead(currentIndex) {
    var desgn;
    $(".approvalDepartment" + currentIndex).change(function () {
        var departmentElmId = $(this).attr('id');
        var departmentId = $("#" + departmentElmId + " option:selected").val();
        currentIndex = departmentElmId.replace('approvalDepartment', '');
        $.ajax({
            url: "/pgr/ajax-designationsByDepartment",
            type: "GET",
            dataType: "json",
            data: {
                approvalDepartment: departmentId
            },
            async: "false",
            success: function (response) {
                var approverDesignation = $('#approvalDesignation' + currentIndex);
                approverDesignation.find('option:gt(0)').remove();
                $('#escalationHierarchyToPositionid' + currentIndex).find('option:gt(0)').remove();
                $.each(response, function (index, value) {
                    approverDesignation.append($('<option>').text(value.name).attr('value', value.id));
                });
                desgn = approverDesignation.val(approverDesignation.attr("data-optvalue"));
                approverDesignation.trigger('change');
            },
            error: function (response) {
            }
        });
    });

    $(".approvalDesignation" + currentIndex).change(function () {
        if (desgn.val() != null) {
            $.ajax({
                url: "/pgr/ajax-positionsByDepartmentAndDesignation",
                type: "GET",
                dataType: "json",
                data: {
                    approvalDepartment: $(".approvalDepartment" + currentIndex + " option:selected").val(),
                    approvalDesignation: $(".approvalDesignation" + currentIndex + " option:selected").val()
                },
                async: false,
                success: function (response) {
                    var position = $('#escalationHierarchyToPositionid' + currentIndex);
                    position.find('option:gt(0)').remove();
                    $.each(response, function (index, item) {
                        position.append($('<option>', {
                            value: item.id,
                            text: item.name
                        }));
                    });
                    position.val(position.attr("data-optvalue"));
                },
                error: function (response) {
                }
            });
        }
    });

    $(".escalationHierarchyToPositionid" + currentIndex).change(function () {
        var totalTableRows = $("#escalationTable tr").length;
        if (currentIndex < (totalTableRows - 2)) {
            $('#escalationHierarchyFromPositionId' + (currentIndex + 1)).val($('#escalationHierarchyFromPositionId' + (currentIndex)).val());
            $('#escalationHierarchyfromPositionName' + (currentIndex + 1)).val($('#escalationHierarchyfromPositionName' + (currentIndex)).val());
        }

    });
}

function addNewRowToTable(currentIndex) {
    $("#escalationTable tbody")
        .append(
            '<tr> <td> <input id="escalationHierarchyFromPositionId' + (currentIndex - 1) + '" name="escalationHierarchyList[' + (currentIndex - 1) + '].fromPosition.id"  type="hidden"> <input class="form-control is_valid_alphabet" id="escalationHierarchyfromPositionName' + (currentIndex - 1) + '"  autocomplete="off"  name="escalationHierarchyList[' + (currentIndex - 1) + '].fromPosition.name"  readonly="readonly" required="required" type="text"> <input id="escalationHierarchyId' + (currentIndex - 1) + '" name="escalationHierarchyList[' + (currentIndex - 1) + '].id"  type="hidden"></td> <td><select  data-first-option="false" id="escalationHierarchySubType' + (currentIndex - 1) + '" class="form-control escalationHierarchySubType' + (currentIndex - 1) + '" name="escalationHierarchyList[' + (currentIndex - 1) + '].grievanceType"  > <option value="">Select </option>  </select>	</td> ' + '<td>' + '<select path="" data-first-option="false" id="approvalDepartment' + (currentIndex - 1) + '" class="form-control approvalDepartment' + (currentIndex - 1) + '"  > <option value="">Select </option> </select>	</td>' + ' <td><select path="" data-first-option="false" id="approvalDesignation' + (currentIndex - 1) + '" class="form-control approvalDesignation' + (currentIndex - 1) + '"  > <option value="">Select </option> </select></td>	<td> <select  path="" data-first-option="false"  name="escalationHierarchyList[' + (currentIndex - 1) + '].toPosition.id" id="escalationHierarchyToPositionid' + (currentIndex - 1) + '" class="form-control escalationHierarchyToPositionid' + (currentIndex - 1) + '"  required="required"> <option value="">Select</option> </select></td> <td> <button type="button" onclick="deleteRow(this)" id="Add" class="btn btn-success">Delete </button> </td></tr>');

    var currentRowFromPositionname = $('#escalationHierarchyfromPositionName' + (currentIndex - 1));
    var currentRowFromPositionId = $('#escalationHierarchyFromPositionId' + (currentIndex - 1));
    currentRowFromPositionId.val($('#escalationHierarchyFromPositionId' + (currentIndex - 2)).val());
    currentRowFromPositionname.val($('#escalationHierarchyfromPositionName' + (currentIndex - 2)).val());

    $('#escalationHierarchySubType' + (currentIndex - 1)).html($('#escalationHierarchySubType' + (currentIndex - 2)).html());
    $('#approvalDepartment' + (currentIndex - 1)).html($('#approvalDepartment' + (currentIndex - 2)).html());
    $('#approvalDepartment' + (currentIndex - 1)).val("");
    $('#escalationHierarchySubType' + (currentIndex - 1)).val("");

}


function checkUniqueDesignationSelected() {
    var u = {}, a = [];
    var totalTableRows = $("#escalationTable tr").length;
    if (totalTableRows == 1)
        return false;

    var i;
    var subtypeval;
    for (i = 0; i < (totalTableRows - 1); i++) {
        if ($("#escalationHierarchySubType" + i).val() == '')
            subtypeval = 'NA';
        else
            subtypeval = $("#escalationHierarchySubType" + i).val();
        if (u.hasOwnProperty($("#escalationHierarchyFromPositionId" + i)
                .val() + subtypeval)) {
            continue;
        }
        a.push($("#escalationHierarchyFromPositionId" + i).val() + subtypeval);
        u[$("#escalationHierarchyFromPositionId" + i).val() + subtypeval] = 1;
    }


    if (a.length != (totalTableRows - 1)) {
        bootbox.alert("From Position and complaint type should be unique.\nPosition and complaint type is same in multiple rows. Please redefine.");
        return false;
    }

    var j;
    for (j = 0; j < (totalTableRows - 1); j++) {
        if ($("#escalationHierarchyFromPositionId" + j).val() == $("#escalationHierarchyToPositionid" + j).val()) {
            bootbox.alert('Heirarchy from position and To position are same in single line.');
            return false;
        }
        if ($("#escalationHierarchySubType" + j).val() == '') {
            bootbox.alert('Please select the Grievance type');
            return false;
        }
    }

    var url = '/pgr/complaint/escalation/search/update/' + $('#formpositionId').val();
    $('#saveEscalationForm').attr('method', 'post');
    $('#saveEscalationForm').attr('action', url);
    return true;
}


function deleteRow(obj) {
    var tb1 = document.getElementById("escalationTable");
    var lastRow = (tb1.rows.length) - 1;
    var j;
    var curRow = getRow(obj).rowIndex;
    if (lastRow == 1) {
        bootbox.alert('You cannot delete this row.');
        return false;
    } else {

        for (j = curRow; j < lastRow; j++) {
            $("#escalationHierarchyFromPositionId" + j).attr('name', "escalationHierarchyList[" + (j - 1) + "].fromPosition.id");
            $("#escalationHierarchyFromPositionId" + j).attr('id', "escalationHierarchyFromPositionId" + (j - 1));

            $("#escalationHierarchyfromPositionName" + j).attr('name', "escalationHierarchyList[" + (j - 1) + "].fromPosition.name");
            $("#escalationHierarchyfromPositionName" + j).attr('id', "escalationHierarchyfromPositionName" + (j - 1));

            $("#escalationHierarchySubType" + j).attr('name', "escalationHierarchyList[" + (j - 1) + "].grievanceType");
            $("#escalationHierarchySubType" + j).attr('class', "form-control escalationHierarchySubType" + (j - 1));
            $("#escalationHierarchySubType" + j).attr('id', "escalationHierarchySubType" + (j - 1));

            $("#approvalDepartment" + j).attr('class', "form-control approvalDepartment" + (j - 1));
            $("#approvalDepartment" + j).attr('id', "approvalDepartment" + (j - 1));

            $("#approvalDesignation" + j).attr('class', "form-control approvalDesignation" + (j - 1));
            $("#approvalDesignation" + j).attr('id', "approvalDesignation" + (j - 1));

            $("#escalationHierarchyToPositionid" + j).attr('class', "form-control escalationHierarchyToPositionid" + (j - 1));
            $("#escalationHierarchyToPositionid" + j).attr('name', "escalationHierarchyList[" + (j - 1) + "].toPosition.id");
            $("#escalationHierarchyToPositionid" + j).attr('id', "escalationHierarchyToPositionid" + (j - 1));
        }
        tb1.deleteRow(curRow);
        return true;
    }
}

function getRow(obj) {
    if (!obj) return null;
    tag = obj.nodeName.toUpperCase();
    while (tag != 'BODY') {
        if (tag == 'TR') return obj;
        obj = obj.parentNode;
        tag = obj.nodeName.toUpperCase();
    }
    return null;
}