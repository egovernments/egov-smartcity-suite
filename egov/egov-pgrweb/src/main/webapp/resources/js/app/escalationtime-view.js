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

    tableContainer1 = $("#escalationTime-table");

    $('#escalationTimeSearch').click(function () {
        callajaxdatatable();
    });

    function callajaxdatatable() {
        $('.report-section').removeClass('display-hide');
        tableContainer1
            .dataTable({
                processing: true,
                serverSide: true,
                sort: true,
                filter: true,
                "searching": false,
                responsive: true,
                destroy: true,
                "order": [[0, 'asc']],
                ajax: {
                    type: "GET",
                    url: "/pgr/complaint/escalationtime/view/",
                    data: function (args) {
                        return {
                            "args": JSON.stringify(args),
                            'complaintTypeId': $('#complaintTypeId').val(),
                            'designationId': $('#designationId').val()
                        }
                    }
                },
                "aLengthMenu": [[10, 25, 50, -1],
                    [10, 25, 50, "All"]],
                "autoWidth": false,
                "bDestroy": true,
                dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
                columns: [{
                    "data": "complaintType",
                    "name": "complaintType.name"
                }, {
                    "data": "designation",
                    "name": "designation.name"
                }, {
                    "data": "noOfHours",
                    "name": "noOfHrs"
                }]
            });
    }

    $('#searchEscalationByCompTypeDesig').keyup(function () {
        tableContainer1.fnFilter(this.value);
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

    var designation_type_typeahead = $('#designation_type').typeahead({
        hint: false,
        highlight: false,
        minLength: 3
    }, {
        displayKey: 'name',
        source: designations.ttAdapter()
    });
    typeaheadWithEventsHandling(designation_type_typeahead, '#designationId');

});
