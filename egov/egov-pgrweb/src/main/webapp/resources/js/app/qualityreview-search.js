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


$(document).ready(function (e) {

    $('#location').change(function () {
        $.ajax({
            url: "/pgr/ajax-getChildLocation",
            type: "GET",
            data: {
                id: $('#location').val()
            },
            dataType: "json",
            success: function (response) {
                $('#childLocation').empty();
                $('#childLocation').append($("<option value=''>Select</option>"));
                $.each(response, function (index, value) {
                    $('#childLocation').append($('<option>').text(value.name).attr('value', value.id));
                });
            },
        });
    });

    $('#btnsearch').click(function (e) {
        if ($('#searchform').valid()) {
            onSubmitEvent(e);
        }
        else
            return false;
    });
});

function onSubmitEvent(event) {
    $('.report-section').removeClass('display-hide');
    event.preventDefault();
    var table = $("#searchGrievance").DataTable({
        processing: true,
        serverSide: true,
        sort: false,
        filter: true,
        "searching": false,
        responsive: true,
        destroy: true,
        "order": [[1, 'asc']],
        dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
        "autoWidth": false,
        "bDestroy": true,
        ajax: {
            url: "/pgr/qualityreview/search/",
            type: 'GET',
            data: function (args) {
                return {
                    "args": JSON.stringify(args),
                    "crn": $("#complaintNumber").val(),
                    "complaintId": $("#complaintId").val(),
                    "fromDate": $("#fromDate").val(),
                    "toDate": $("#toDate").val(),
                    "rating": $("#rating").val(),
                    "locationId":$("#location").val(),
                    "childLocationId":$("#childLocation").val()
                };
            }
        },
        columns: [
            {
                "data": "crn",
                "sTitle": "CRN"
            }, {
                "data": "reviewed",
                "sTitle": "Reviewed"
            }, {
                "data": "grievanceType",
                "sTitle": "Grievance Type"
            }, {
                "data": "owner",
                "sTitle": "Complainant Name"
            }, {
                "data": "location",
                "sTitle": "Location"
            }, {
                "data": "status",
                "sTitle": "Status"
            }, {
                "data": "department",
                "sTitle": "Department"
            }, {
                "data": "date",
                "sTitle": "Registration Date"
            }]
    });

    $('#searchGrievance tbody').on('click', 'tr', function () {
        var data = table.row(this).data();
        window.open("/pgr/complaint/qualityreview/" + data.crn);
    });
}
