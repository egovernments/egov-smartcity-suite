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

function populateBoundaryTypes(dropdown) {
    populateboundaryType({
        hierarchyTypeId: dropdown.value
    });
}

$("#boundaryType").change(function () {
    $('#boundary').find('option:gt(0)').remove();
    if ($("#boundaryType").val() !== '') {
        $.ajax({
            type: "GET",
            url: "/egi/boundary/search/by-boundarytype",
            data: {'boundaryTypeId': $("#boundaryType").val()},
            dataType: "json",
            success: function (response) {
                $.each(JSON.parse(response), function (key, boundary) {
                    $('#boundary').append('<option value="' + boundary.id + '">' + boundary.name + '</option>');
                });
            }
        });
    }
});

var oTable;

$('#searchBtn').click(function () {
    if ($("#boundaryType").val() === '' || $("#hierarchyType").val() === '') {
        bootbox.alert("Please select a valid Boundary Type");
        return;
    }

    if ($("#boundary").val() !== '') {
        $("#boundaryCreateSearchForm").attr('action', $("#boundary").val());
        $("#boundaryCreateSearchForm").submit();
    } else {
        oTable = $('#view-boundaries').DataTable({
            processing: true,
            serverSide: true,
            type: 'POST',
            sort: true,
            filter: true,
            responsive: true,
            destroy: true,
            "autoWidth": false,
            "order": [[0, 'asc']],
            ajax: {
                url: '/egi/boundary/search',
                type: "POST",
                data: function (args) {
                    return {"args": JSON.stringify(args), "boundaryTypeId": $("#boundaryType").val()};
                }
            },
            "aLengthMenu": [[10, 25, 50, -1],
                [10, 25, 50, "All"]],
            "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
            "columns": [{
                "mData": "name",
                "name": "name",
                "sTitle": "Name",
            }, {
                "mData": "boundaryNameLocal",
                "name": "localName",
                "sTitle": "Local Name"
            }, {
                "mData": "boundaryParentName",
                "name": "parent.name",
                "sTitle": "Parent Boundary Name"
            }, {
                "mData": "boundaryNum",
                "name": "boundaryNum",
                "sTitle": "Boundary Number"
            }, {
                "mData": "active",
                "name": "active",
                "sTitle": "Active"
            }, {
                "mData": "fromDate",
                "name": "fromDate",
                "sTitle": "From Date"
            }, {
                "mData": "toDate",
                "name": "toDate",
                "sTitle": "To Date"
            }, {
                "data": null,
                'sClass': "text-center",
                "bSortable": false,
                "target": -1,
                "defaultContent": '<span class="add-padding"><i class="fa fa-pencil-square-o fa-lg edit"></i></span><span class="add-padding"><i class="fa fa-eye fa-lg view"></i></span>'
            }, {
                "data": "id",
                "visible": false,
                "bSortable": false
            }]
        });


    }
});

$("#view-boundaries").on('click', 'tbody tr td span i.edit', function (event) {
    var id = oTable.row($(this).closest('tr')).data().id;
    var url = '/egi/boundary/update/' + id;
    window.open(url, id, 'width=900, height=700, top=300, left=260,scrollbars=yes');

});

$("#view-boundaries").on('click', 'tbody tr td span i.view', function (event) {
    var id = oTable.row($(this).closest('tr')).data().id;
    var url = '/egi/boundary/view/' + id;
    window.open(url, id, 'width=900, height=700, top=300, left=260,scrollbars=yes');

});

$('#backBtnId').click(function () {
    window.location = '/egi/boundary/update/';
});
