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
    $('#btnsearch').attr("disabled",true);
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

$.fn.dataTable.ext.errMode = function () {
    $('.loader-class').modal('hide');
};

function onSubmitEvent(event) {
    try {
        $('.loader-class').modal('show', {backdrop: 'static'});
        $('.report-section').removeClass('display-hide');
        event.preventDefault();
        var table = $("#baseregistertbl");
        if ( $.fn.dataTable.isDataTable(table) )
            table.DataTable().destroy();
        table = table.DataTable({
            processing: true,
            serverSide: true,
            sort: true,
            filter: true,
            "searching": false,
            responsive: true,
            destroy: true,
            scrollX: true,
            "order": [[0, 'asc']],
            dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
            "autoWidth": true,
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
                url: "search?" + $("#baseregisterform").serialize(),
                type: 'POST',
                data: function (args) {
                    return {
                        "args": JSON.stringify(args)
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
                    "sTitle": "License No.",
                    "name": "licenseNumber"
                }, {
                    "data": "oldLicenseNo",
                    "name": "oldLicenseNumber",
                    "sTitle": "Old License No."
                }, {
                    "data": "tradetitle",
                    "name": "tradeTitle",
                    "sTitle": "Trade Title"
                }, {
                    "data": "category",
                    "name": "categoryName",
                    "sTitle": "Category"
                }, {
                    "data": "subcategory",
                    "name": "subCategoryName",
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
                    "name": "assessmentNo",
                    "sTitle": "Assessment No"
                }, {
                    "data": "revenueWard",
                    "name": "wardName",
                    "sTitle": "Revenue Ward"
                }, {
                    "data": "electionWard",
                    "name": "adminWardName",
                    "sTitle": "Election Ward"
                }, {
                    "data": "localityname",
                    "name": "localityName",
                    "sTitle": "Locality"
                }, {
                    "data": "tradeaddress",
                    "name": "tradeAddress",
                    "sTitle": "Trade Address"
                }, {
                    "data": "commencementdate",
                    "name": "commencementDate",
                    "sTitle": "Commencement Date"
                }, {
                    "data": "status",
                    "name": "statusName",
                    "sTitle": "Status"
                }, {
                    "data": "unitofmeasure",
                    "name": "unitOfMeasure",
                    "sTitle": "UOM"
                }, {
                    "data": "tradearea",
                    "name": "tradeWt",
                    "sTitle": "Unit value"
                }, {
                    "data": "rate",
                    "name": "rateVal",
                    "sTitle": "Rate"
                }, {
                    "data": "arrearlicfee",
                    "name": "arrearLicenseFee",
                    "sTitle": "License Fee(Arrears)"
                }, {
                    "data": "arrearpenaltyfee",
                    "name": "arrearPenaltyFee",
                    "sTitle": "Penalty(Arrears)"
                }, {
                    "data": "curlicfee",
                    "name": "curLicenseFee",
                    "sTitle": "License Fee(Current)"
                }, {
                    "data": "curpenaltyfee",
                    "name": "curPenaltyFee",
                    "sTitle": "Penalty(Current)"
                }],
            "aaSorting": [[0, 'asc']],
            "footerCallback": function (row, data, start, end,
                                        display) {
                var api = this.api(), data;
                if (data.length == 0) {
                    $('#report-footer').hide();
                } else {
                    $('#report-footer').show();
                }
                if (data.length > 0) {
                    updateTotalFooter(17, api);
                    updateTotalFooter(18, api);
                    updateTotalFooter(19, api);
                    updateTotalFooter(20, api);
                }
            },
            "initComplete": function (settings, json) {
                $('.loader-class').modal('hide');
                $('#btnsearch').attr("disabled",false);
            },
            "aoColumnDefs": [{
                "aTargets": [17, 18, 19, 20],
                "mRender": function (data, type, full) {
                    return formatNumberInr(data);
                },
                className: "dt-right"
            }]
        });
        $('.loader-class').modal('hide');
        var info = table.page.info();
        if (info.start == 0)
            getSumOfRecords();
    } catch (e) {
        $('.loader-class').modal('hide');
        console.log(e);
    }
}

function updateTotalFooter(colidx, api) {
    // Remove the formatting to get integer data for summation
    var intVal = function (i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
            : typeof i === 'number' ? i : 0;
    };

    // Total over all pages
    total = recordTotal[colidx - 17];

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