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
    var complaintType = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/pgr/complaint/router/complaintTypes?complaintTypeName=%QUERY',
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

    var position = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/pgr/complaint/router/position?positionName=%QUERY',
            dataType: "json",
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

    var com_pos_typeahead = $('#com_position').typeahead({
        hint: false,
        highlight: true,
        minLength: 3
    }, {
        displayKey: 'name',
        source: position.ttAdapter()
    });

    typeaheadWithEventsHandling(com_pos_typeahead, '#positionId');


    $("#boundary_type_id").change(function () {
        $('#com_boundry').typeahead('val', '');
        $('#com_boundry').typeahead('destroy');
        $("#boundaryId").val('');

        var b_id = $("#boundary_type_id").val();
        var boundaries = new Bloodhound(
            {
                datumTokenizer: function (datum) {
                    return Bloodhound.tokenizers
                        .whitespace(datum.value);
                },
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                remote: {
                    url: '/pgr/complaint/router/boundaries-by-type?boundaryName=%QUERY&boundaryTypeId=' + b_id,
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

        var com_boundry_typeahead = $('#com_boundry').typeahead({
            hint: false,
            highlight: false,
            minLength: 3
        }, {
            displayKey: 'name',
            source: boundaries.ttAdapter()
        });
        typeaheadWithEventsHandling(com_boundry_typeahead, '#boundaryId');
    });

    $("#createRouter").submit(function (e) {
        $('.loader-class').modal('hide');
        $('.all-errors').hide();
        if ($('#positionId').val() == "") {
            $('.positionerror').html('Position is required').show();
            e.preventDefault();
        } else if ($("#boundary_type_id").val() != 0 && $('#boundaryId').val() == "") {
            $('.boundaryerror').html('Boundary is required').show();
            e.preventDefault();
        } else if (($('#com_type').val() != "" && $("#complaintTypeId").val() == "") && $('#com_boundry').val() == "") {
            $('.eithererror').html('Complaint Type or Boundary should be selected').show();
            e.preventDefault();
        } else if (($('#com_type').val() == "" && $("#complaintTypeId").val() == "") && $('#com_boundry').val() == "") {
            $('.eithererror').html('Complaint Type or Boundary should be selected').show();
            e.preventDefault();
        }
        else {
            $('.loader-class').modal('show', {backdrop: 'static'});
        }
    });

});
