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

    $('#category').change(function () {
        var val = $(this).val();
        if (val !== '') {
            var results = [];
            $.ajax({
                type: "GET",
                url: '/tl/licensesubcategory/by-category',
                dataType: "json",
                data: {categoryId: val},
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

    $('#btnsearch').click(function (e) {
        onSubmitEvent(e);
        return false;
    });
});

var recordTotal = [];
function getSumOfRecords() {

    $.ajax({
        url: "grand-total?" + $("#baseregisterform").serialize(),
        type: 'GET',
        success: function (data) {
            recordTotal = [];
            for (var i = 0; i < data.length; i++) {
                recordTotal.push(data[i]);
            }
        }
    })
}

function openTradeLicense(id) {
    window.open("/tl/viewtradelicense/viewTradeLicense-view.action?id=" + id,
        '', 'scrollbars=yes,width=1000,height=700,status=yes');
}
function onSubmitEvent(event) {
    $('.report-section').removeClass('display-hide');
    event.preventDefault();
    $("#baseregistertbl").dataTable({
        processing: true,
        serverSide: true,
        sort: true,
        filter: true,
        "searching": false,
        responsive: true,
        destroy: true,
        "order": [[1, 'asc']],
        dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
        "autoWidth": false,
        "bDestroy": true,
        buttons: [
            {
                text: 'PDF',
                action: function (e, dt, node, config) {
                    window.open("download?" + $("#baseregisterform").serialize() + "&printFormat=PDF", '_self');
                }
            },
            {
                text: 'XLS',
                action: function (e, dt, node, config) {
                    window.open("download?" + $("#baseregisterform").serialize() + "&printFormat=XLS", '_self');
                }
            }],
        ajax: {
            url: "search",
            type: 'POST',
            data: function (args) {
                return {
                    "args": JSON.stringify(args),
                    "categoryId": $("#category").val(),
                    "subCategoryId": $("#subCategory").val(),
                    "statusId": $("#status").val(),
                    "wardId": $("#ward").val(),
                    "filterName": $("#filter").val()
                };
            }

        },
        columns: [
            {
                "data": function (row, type, set, meta) {
                    return {
                        name: row.tinno,
                        id: row.licenseId
                    };
                },
                "render": function (data, type, row) {
                    return '<a href="javascript:void(0);" onclick="openTradeLicense('
                        + row.licenseId
                        + ');">'
                        + data.name + '</a>';
                },
                "sTitle": "TIN NO.",
                "name": "licensenumber"
            }, {
                "data": "tradetitle",
                "name": "tradetitle",
                "sTitle": "Trade Title"
            }, {
                "data": "category",
                "name": "categoryname",
                "sTitle": "Category"
            }, {
                "data": "subcategory",
                "name": "subcategoryname",
                "sTitle": "Subcategory"
            }, {
                "data": "owner",
                "name": "owner",
                "sTitle": "Trade Owner"
            }, {
                "data": "mobile",
                "name": "mobile",
                "sTitle": "Owner MobileNo"
            }, {
                "data": "assessmentno",
                "name": "assessmentno",
                "sTitle": "Assessment No"
            }, {
                "data": "wardname",
                "name": "wardname",
                "sTitle": "Ward No"
            }, {
                "data": "localityname",
                "name": "localityname",
                "sTitle": "Locality"
            }, {
                "data": "tradeaddress",
                "name": "tradeaddress",
                "sTitle": "Trade Address"
            }, {
                "data": "commencementdate",
                "name": "commencementdate",
                "sTitle": "Commencement Date"
            }, {
                "data": "status",
                "name": "statusname",
                "sTitle": "Status"
            }, {
                "data": "unitofmeasure",
                "name": "uom",
                "sTitle": "UOM"
            }, {
                "data": "tradearea",
                "name": "tradewt",
                "sTitle": "Unit value"
            }, {
                "data": "rate",
                "name": "rateval",
                "sTitle": "Rate"
            }, {
                "data": "arrearlicfee",
                "name": "arrearlicensefee",
                "sTitle": "License Fee(Arrears)"
            }, {
                "data": "arrearpenaltyfee",
                "name": "arrearpenaltyfee",
                "sTitle": "Penalty(Arrears)"
            }, {
                "data": "curlicfee",
                "name": "curlicensefee",
                "sTitle": "License Fee(Current)"
            }, {
                "data": "curpenaltyfee",
                "name": "curpenaltyfee",
                "sTitle": "Penalty(Current)"
            }],
        "aaSorting": [[2, 'asc']],
        "footerCallback": function (row, data, start, end,
                                    display) {
            var api = this.api(), data;
            if (data.length == 0) {
                $('#report-footer').hide();
            } else {
                $('#report-footer').show();
            }
            if (data.length > 0) {
                updateTotalFooter(15, api);
                updateTotalFooter(16, api);
                updateTotalFooter(17, api);
                updateTotalFooter(18, api);
            }
        },
        "aoColumnDefs": [{
            "aTargets": [15, 16, 17, 18],
            "mRender": function (data, type, full) {
                return formatNumberInr(data);
            }
        }]
    });
    $('.loader-class').modal('hide');
    var table = $('#baseregistertbl').DataTable();
    var info = table.page.info();
    if (info.start == 0)
        getSumOfRecords();
}

function updateTotalFooter(colidx, api) {
    // Remove the formatting to get integer data for summation
    var intVal = function (i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
            : typeof i === 'number' ? i : 0;
    };

    // Total over all pages
    total = recordTotal[colidx - 15];

    // Total over this page
    pageTotal = api.column(colidx, {
        page: 'current'
    }).data().reduce(function (a, b) {
        return intVal(a) + intVal(b);
    }, 0);

    // Update footer
    $(api.column(colidx).footer()).html(
        formatNumberInr(pageTotal) + ' (' + formatNumberInr(total) + ')');
}

//inr formatting number
function formatNumberInr(x) {
    if (x) {
        x = x.toString();
        var afterPoint = '';
        if (x.indexOf('.') > 0)
            afterPoint = x.substring(x.indexOf('.'), x.length);
        x = Math.floor(x);
        x = x.toString();
        var lastThree = x.substring(x.length - 3);
        var otherNumbers = x.substring(0, x.length - 3);
        if (otherNumbers != '')
            lastThree = ',' + lastThree;
        var res = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",")
            + lastThree + afterPoint;
        return res;
    }
    return x;
}