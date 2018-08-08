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

var reportdatatable;
var param;
var recordTotal = [];
$(document).ready(function (e) {
    drillDowntableContainer = $("#tbldcbdrilldown");
    $('#btnsearch').click(function (e) {

        var table = $('#tbldcbdrilldown').DataTable();
        if ($('#ward').val() != null) {
            var wardid = "";
            $("#ward option:selected").each(function () {
                var $this = $(this);
                wardid = wardid + $this.val() + ",";
            });
            wardid = wardid.substring(0, wardid.length - 1);
        }
        else if (!$('#ward').val())
            var wardid = "";

        if ($('#adminward').val() != null) {
            var adminwardid = "";
            $("#adminward option:selected").each(function () {
                var $this = $(this);
                adminwardid = adminwardid + $this.val() + ",";
            });
            adminwardid = adminwardid.substring(0, adminwardid.length - 1);
        }
        else if (!$('#adminward').val())
            var adminwardid = "";

        var info = table.page.info();
        if (info.start == 0)
            getSumOfRecords(wardid,adminwardid);
        searchDCBReport(wardid,adminwardid);
    });
});

function getSumOfRecords(wardid,adminwardid) {

    $.ajax({
        url: "grand-total",
        data: {
            'licenseNumber': $('#licensenumber').val(),
            'activeLicense': $('#activeLicense').val(),
            'wardId': wardid,
            'adminWardId':adminwardid
        },
        type: 'GET',
        async: true,
        success: function (data) {
            recordTotal = [];
            for (var i = 0; i < data.length; i++) {
                recordTotal.push(data[i]);
            }
        }
    })
}

function openTradeLicense(obj) {
    window.open("/tl/viewtradelicense/viewTradeLicense-view.action?id="
        + $(obj).data('eleval'), '',
        'scrollbars=yes,width=1000,height=700,status=yes');
}

function jasonParam(obj) {
    var parts = [];
    for (var key in obj) {
        if (obj.hasOwnProperty(key)) {
            parts.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
        }
    }
    return "?" + parts.join('&');
}

$.fn.dataTable.ext.errMode = function () {
    $('.loader-class').modal('hide');
};

function searchDCBReport(wardid,adminwardid) {
    try {
        $('.loader-class').modal('show', {backdrop: 'static'});
        $('.report-section').removeClass('display-hide');
        $('#report-footer').show();
        $("#tbldcbdrilldown").dataTable().fnDestroy();
        reportdatatable = drillDowntableContainer
            .on('preXhr.dt', function (e, settings, data) {
                param = data;
            }).dataTable({
            processing: true,
            serverSide: true,
            sort: true,
            filter: true,
            "searching": false,
            dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
            "autoWidth": true,
            "bDestroy": true,
            scrollX: true,
            buttons: [
                {
                    text: 'PDF',
                    action: function (e, dt, node, config) {
                        var url = "download" + jasonParam(param) + "&printFormat=PDF";
                        window.open(url, '_self');
                    }
                },
                {
                    text: 'XLS',
                    action: function (e, dt, node, config) {
                        var url = "download" + jasonParam(param) + "&printFormat=XLS";
                        window.open(url, '_self');
                    }
                }],
            responsive: true,
            destroy: true,
            "order": [[0, 'asc']],
            ajax: {
                url: "search",
                type: 'POST',
                data: function (args) {
                    return {
                        "args": JSON.stringify(args),
                        'licenseNumber': $('#licensenumber').val(),
                        'activeLicense': $('#activeLicense').val(),
                        'wardId': wardid,
                        'adminWardId':adminwardid
                    }
                }
            },

            columns: [
                {
                    "data": function (row, type, set, meta) {
                        return {
                            name: row.licenseNumber,
                            id: row.licenseId
                        };
                    },
                    "render": function (data, type, row) {
                        return '<a href="javascript:void(0);" onclick="openTradeLicense(this);" data-hiddenele="id" data-eleval="'
                            + data.id + '">' + data.name + '</a>';
                    },
                    "sTitle": "License No.",
                    "name": "licenseNumber",
                    "width": 10
                }, {
                    "data": "oldLicenseNo",
                    "name": "oldLicenseNumber",
                    "sTitle": "Old License No.",
                    "width": 10
                }, {
                    "data": "ward",
                    "name": "wardName",
                    "sTitle": "Revenue Ward",
                    "width": 10
                }, {
                    "data": "adminWard",
                    "name": "adminWard",
                    "sTitle": "Election Ward",
                    "width": 10
                }, {
                    "data": "active",
                    "name": "active",
                    "sTitle": "Active",
                    "width": 10
                }, {
                    "data": "arrearDemand",
                    "name": "arrearDemand",
                    "sTitle": "Arrears"
                }, {
                    "data": "currentDemand",
                    "name": "currentDemand",
                    "sTitle": "Current"
                }, {
                    "data": "totalDemand",
                    "orderable": false,
                    "sortable": false,
                    "sTitle": "Total"
                }, {
                    "data": "arrearCollection",
                    "name": "arrearCollection",
                    "sTitle": "Arrears"
                }, {
                    "data": "currentCollection",
                    "name": "currentCollection",
                    "sTitle": "Current"
                }, {
                    "data": "totalCollection",
                    "orderable": false,
                    "sortable": false,
                    "sTitle": "Total"
                }, {
                    "data": "arrearBalance",
                    "name": "arrearBalance",
                    "sTitle": "Arrears"
                }, {
                    "data": "currentBalance",
                    "name": "currentBalance",
                    "sTitle": "Current"
                }, {
                    "data": "totalBalance",
                    "orderable": false,
                    "sortable": false,
                    "sTitle": "Total"
                }],
            "footerCallback": function (row, data, start, end, display) {
                var api = this.api(), data;
                if (data.length == 0) {
                    $('#report-footer').hide();
                } else {
                    $('#report-footer').show();
                }
                if (data.length > 0) {
                    updateTotalFooter(5, api, true);
                    updateTotalFooter(6, api, true);
                    updateTotalFooter(7, api, true);
                    updateTotalFooter(8, api, true);
                    updateTotalFooter(9, api, true);
                    updateTotalFooter(10, api, true);
                    updateTotalFooter(11, api, true);
                    updateTotalFooter(12, api, true);
                    updateTotalFooter(13, api, true);
                }
            },
            "initComplete": function (settings, json) {
                $('.loader-class').modal('hide');
            },
            "aoColumnDefs": [{
                "aTargets": [5, 6, 7, 8, 9, 10, 11, 12, 13],
                "mRender": function (data, type, full) {
                    return formatNumberInr(data);
                },
                className: "dt-right"
            }]
        });


        if ($('#mode').val() == 'property') {
            reportdatatable.fnSetColumnVis(1, false);
        } else {
            reportdatatable.fnSetColumnVis(1, true);
        }
    } catch (e) {
        $('.loader-class').modal('hide');
        console.log(e);
    }
}

function updateTotalFooter(colidx, api, isServerSide) {
    // Remove the formatting to get integer data for summation
    var intVal = function (i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
            : typeof i === 'number' ? i : 0;
    };

    // Total over all pages
    if (isServerSide && recordTotal != null)
        total = recordTotal[colidx - 5];
    else
        total = api.column(colidx).data().reduce(function (a, b) {
            return intVal(a) + intVal(b);
        });

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

// inr formatting number
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