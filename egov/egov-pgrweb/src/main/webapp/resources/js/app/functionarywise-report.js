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
var pramdata = "";
var prameter = "";
var recordTotal = [];
jQuery(document).ready(function ($) {

    functionaryreportContainer = $("#functionaryReport-table");
    compreportContainer = $("#compReport-table");

    $('#report-backbutton').hide();

    $('#backButton').click(function (e) {
        $('#report-backbutton').hide();
        $('#status').val("");
        $('#functionaryWithStatus').val("");
        $('#usrid').val("");
        $('#functionaryReport-table_wrapper').show();
        $('#compReport-table_wrapper').hide();
        var table = $('#functionaryReport-table').DataTable();
        var info = table.page.info();
        if (info.start == 0)
            getSumOfRecords()
        callajaxdatatableForCompTypeReport();

    });

    $('#functionaryReportSearch').click(function (e) {
        var fromDate = $("#start_date").val();
        var toDate = $("#end_date").val();
        if ($("#end_date").val() == null)
            toDate = new Date();
        compareDate(fromDate, toDate);

        if (!compareDate(fromDate, toDate)) {
            bootbox.alert("From date  should not be greater than the To Date.");
            $('#end_date').val('');
            return false;
        }
        else {

            var table = $('#functionaryReport-table').DataTable();
            var info = table.page.info();
            if (info.start == 0)
                getSumOfRecords()
            callajaxdatatableForCompTypeReport();
            return true;
        }

    });

    function jsonParameters(obj) {
        var parts = [];
        for (var key in obj) {
            if (obj.hasOwnProperty(key)) {
                parts.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
            }
        }
        return "?" + parts.join('&');
    }


    function getSumOfRecords() {
        $.ajax({

            url: "/pgr/report/functionarywise/grand-total",
            type: 'GET',
            async: false,
            data: {
                "fromDate": $('#start_date').val(),
                "toDate": $('#end_date').val(),
                "status": $('#functionaryWithStatus').val(),
                "complaintDateType": $('#when_date').val()
            },
            success: function (data) {
                recordTotal = [];
                for (var i = 0; i < data.length; i++) {
                    recordTotal.push(data[i]);
                }
            }
        })
    }

    function callajaxdatatableForCompTypeReport() {

        $('#functionaryReport-table_wrapper').show();
        $('#compReport-table_wrapper').hide();
        var startDate = "";
        var endDate = "";
        var usrid = "";
        var when_dateVal = "";

        startDate = $('#start_date').val();
        endDate = $('#end_date').val();

        when_dateVal = $('#when_date').val();
        usrid = $('#usrid').val();
        if ($('#start_date').val() == "")
            startDate = "";

        if ($('#end_date').val() == "")
            endDate = "";

        $('.report-section').removeClass('display-hide');
        $('#report-footer').show();
        $('#report-backbutton').hide();

        functionaryreportContainer.on('preXhr.dt', function (e, settings, data) {
            pramdata = data;
        }).dataTable({
            processing: true,
            serverSide: true,
            sort: true,
            filter: true,
            "searching": false,
            "order": [[0, 'asc']],
            dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
            "autoWidth": false,
            "bDestroy": true,
            buttons: [
                {
                    text: 'PDF',
                    action: function (e, dt, node, config) {
                        window.open("/pgr/report/functionarywise/download" + jsonParameters(pramdata) + "&printFormat=PDF", '', 'scrollbars=yes,width=1300,height=700,status=yes');
                    }
                },
                {
                    text: 'XLS',
                    action: function (e, dt, node, config) {
                        window.open("/pgr/report/functionarywise/download" + jsonParameters(pramdata) + "&printFormat=XLS", '_self');
                    }
                }],
            ajax: {
                url: "/pgr/report/functionarywise",
                type: 'POST',
                data: function (args) {
                    return {
                        "args": JSON.stringify(args),
                        "fromDate": startDate,
                        "toDate": endDate,
                        "usrid": usrid,
                        "complaintDateType": when_dateVal,
                        "status": ""
                    };

                }
            },
            columns: [
                {
                    "data": "name",
                    "sTitle": "Employee Name",
                    "name": "employeeName"
                }, {
                    "mData": getComplaintTypeIdWithDatakey,
                    "render": function (data, type, row) {
                        return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Registered\');" ' +
                            'data-hiddenele="boundary" data-eleval="' + data.registered + '" data-complaintname="' + data.usrid + '">'
                            + data.registered + '</a>';
                    },
                    "sTitle": "Registered",
                    "sClass": "text-right",
                    "name": "registered"
                }, {
                    "mData": getComplaintTypeIdWithDatakey,
                    "render": function (data, type, row) {
                        return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Inprocess\');" ' +
                            'data-hiddenele="boundary" data-eleval="' + data.inprocess + '" data-complaintname="' + data.usrid + '">'
                            + data.inprocess + '</a>';
                    },
                    "sTitle": "Inprocess",
                    "sClass": "text-right",
                    "name": "inprocess"
                }, {
                    "mData": getComplaintTypeIdWithDatakey,
                    "render": function (data, type, row) {
                        return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Completed\');" ' +
                            'data-hiddenele="boundary" data-eleval="' + data.completed + '" data-complaintname="' + data.usrid + '">'
                            + data.completed + '</a>';
                    },
                    "sTitle": "Completed",
                    "sClass": "text-right",
                    "name": "completed"
                }, {
                    "mData": getComplaintTypeIdWithDatakey,
                    "render": function (data, type, row) {
                        return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Rejected\');" ' +
                            'data-hiddenele="boundary" data-eleval="' + data.rejected + '" data-complaintname="' + data.usrid + '">'
                            + data.rejected + '</a>';
                    },
                    "sTitle": "Rejected",
                    "sClass": "text-right",
                    "name": "rejected"
                }, {
                    "mData": getComplaintTypeIdWithDatakey,
                    "render": function (data, type, row) {
                        return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Reopened\');" ' +
                            'data-hiddenele="boundary" data-eleval="' + data.reopened + '" data-complaintname="' + data.usrid + '">'
                            + data.reopened + '</a>';
                    },
                    "sTitle": "Reopened",
                    "sClass": "text-right",
                    "name": "reopened"

                }, {
                    "mData": getComplaintTypeIdWithDatakey,
                    "render": function (data, type, row) {
                        return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Within SLA\');" ' +
                            'data-hiddenele="boundary" data-eleval="' + data.withinsla + '" data-complaintname="' + data.usrid + '">'
                            + data.withinsla + '</a>';
                    },
                    "sTitle": "Within SLA",
                    "sClass": "text-right",
                    "name": "withinSLA"
                }, {
                    "mData": getComplaintTypeIdWithDatakey,
                    "render": function (data, type, row) {
                        return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Beyond SLA\');" ' +
                            'data-hiddenele="boundary" data-eleval="' + data.beyondsla + '" data-complaintname="' + data.usrid + '">'
                            + data.beyondsla + '</a>';
                    },
                    "sTitle": "Beyond SLA",
                    "sClass": "text-right",
                    "name": "beyondSLA"
                }, {
                    "data": "total",
                    "sTitle": "Total",
                    "orderable": false,
                    "sortable": false,
                    "sClass": "text-right"

                }],
            "footerCallback": function (row, data,
                                        start, end, display) {
                var api = this.api(), data;
                if (data.length == 0) {
                    $('#report-footer').hide();
                } else {
                    $('#report-footer').show();
                }
                if (data.length > 0) {
                    updateTotalFooter(1, api, 'registered');
                    updateTotalFooter(2, api, 'inprocess');
                    updateTotalFooter(3, api, 'completed');
                    updateTotalFooter(4, api, 'rejected');
                    updateTotalFooter(5, api, 'reopened');
                    updateTotalFooter(6, api, 'withinsla');
                    updateTotalFooter(7, api, 'beyondsla');
                    updateTotalFooter(8, api, '');

                }
            },
            "aoColumnDefs": [{
                "aTargets": [1, 2, 3, 4, 5, 6],
                "mRender": function (data, type, full) {
                    return formatNumberInr(data);
                }
            }]

        });
    }

    function getComplaintTypeIdWithDatakey(row, type, set, meta) {
        var data;
        if (meta != undefined) {
            if (meta.col === 1)
                data = {'usrid': row.usrid, 'registered': row.registered}
            else if (meta.col === 2)
                data = {'usrid': row.usrid, 'inprocess': row.inprocess}
            else if (meta.col === 3)
                data = {'usrid': row.usrid, 'completed': row.completed}
            else if (meta.col === 4)
                data = {'usrid': row.usrid, 'rejected': row.rejected}
            else if (meta.col === 5)
                data = {'usrid': row.usrid, 'reopened': row.reopened}
            else if (meta.col === 6)
                data = {'usrid': row.usrid, 'withinsla': row.withinsla}
            else if (meta.col === 7)
                data = {'usrid': row.usrid, 'beyondsla': row.beyondsla}

        }
        return data;
    }

    function compareDate(dt1, dt2) {
        var d1, m1, y1, d2, m2, y2, ret;
        dt1 = dt1.split('/');
        dt2 = dt2.split('/');
        ret = (eval(dt2[2]) > eval(dt1[2])) ? 1
            : (eval(dt2[2]) < eval(dt1[2])) ? -1
                : (eval(dt2[1]) > eval(dt1[1])) ? 1
                    : (eval(dt2[1]) < eval(dt1[1])) ? -1	// decimal points
                        : (eval(dt2[0]) > eval(dt1[0])) ? 1
                            : (eval(dt2[0]) < eval(dt1[0])) ? -1
                                : 0;
        if (ret >= 0)
            return true;
        else

            return false;
    }

});


function callAjaxByComplaintDetail() {
    console.log('calling inside callAjaxByComplaintDetail');
    var startDate = "";
    var endDate = "";
    var usrid = "";
    var when_dateVal = "";
    startDate = $('#start_date').val();
    endDate = $('#end_date').val();

    when_dateVal = $('#when_date').val();
    usrid = $('#usrid').val();
    if ($('#start_date').val() == "")
        startDate = "";

    if ($('#end_date').val() == "")
        endDate = "";

    $('.report-section').removeClass('display-hide');
    $('#functionaryReport-table_wrapper').hide();
    $('#compReport-table_wrapper').show();
    $('#report-backbutton').show();

    function complaintParameter(obj) {
        var parts = [];
        for (var key in obj) {
            if (obj.hasOwnProperty(key)) {
                parts.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
            }
        }
        return "?" + parts.join('&');
    }

    compreportContainer
        .on('preXhr.dt', function (e, settings, data) {
            prameter = data;
        })
        .dataTable({

            processing: true,
            serverSide: true,
            sort: true,
            filter: true,
            "searching": false,
            "order": [[0, 'asc']],
            dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
            "autoWidth": false,
            "bDestroy": true,
            buttons: [
                {
                    text: 'PDF',
                    action: function (e, dt, node, config) {
                        window.open("/pgr/report/functionarywise/download" + complaintParameter(prameter) + "&printFormat=PDF", '', 'scrollbars=yes,width=1300,height=700,status=yes');
                    }
                },
                {
                    text: 'XLS',
                    action: function (e, dt, node, config) {
                        window.open("/pgr/report/functionarywise/download" + complaintParameter(prameter) + "&printFormat=XLS", '_self');
                    }
                }],
            ajax: {
                url: "/pgr/report/functionarywise",
                type: 'POST',
                data: function (args) {
                    return {
                        "args": JSON.stringify(args),
                        "fromDate": startDate,
                        "toDate": endDate,
                        "usrid": usrid,
                        "complaintDateType": when_dateVal,
                        "status": $('#status').val()
                    };

                }
            },

            columns: [
                {
                    "data": "crn",
                    "sTitle": "Complaint Number",
                    "name": "crn",
                    "render": function (data, type, row) {
                        return '<a href="javascript:void(0);" onclick="window.open(\'/pgr/complaint/view/'
                            + data
                            + '\',\'\', \'width=800, height=600\');" data-hiddenele="selecteduserid" data-eleval="'
                            + data + '">' + data + '</a>';
                    },
                }, {
                    "mData": "createddate",
                    "sTitle": "Grievance Date",
                    "name": "createdDate"

                }, {
                    "data": "complainantname",
                    "sTitle": "Complainant Name",
                    "name": "complainantName"
                }, {
                    "data": "boundaryname",
                    "sTitle": "Grievance Address",
                    "name": "boundaryName"
                }, {
                    "data": "details",
                    "sTitle": "Grievance Details",
                    "name": "complaintDetail"
                }, {
                    "data": "status",
                    "sTitle": "Status",
                    "name": "status"

                }, {
                    "data": "feedback",
                    "sTitle": "Feedback",
                    "name": "feedback"
                }, {
                    "data": "issla",
                    "sTitle": "Within SLA(Yes or No)",
                    "name": "isSLA"
                }]

        });
}

function add(a, b) {
    return a + b;
}

function updateTotalFooter(colidx, api, key) {
    // Remove the formatting to get integer data for summation
    var intVal = function (i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
            : typeof i === 'number' ? i : 0;
    };

    var total;

    if (colidx > recordTotal.length) {
        total = recordTotal.concat().splice(5, 7).reduce(add, 0);
    }
    else {
        // Total over all pages
        total = recordTotal[colidx - 1];
    }

    if (api.column(colidx).data().length === 1) {
        if (typeof api.column(colidx).data()[0] === 'object')
            total = api.column(colidx).data()[0][key];
    }

    // Total over this page
    pageTotal = api.column(colidx, {
        page: 'current'
    }).data().reduce(function (a, b) {
        var a1 = (a !== null && typeof a === 'object' ? a[key] : a);
        var b1 = (b !== null && typeof b === 'object' ? b[key] : b);
        return intVal(a1) + intVal(b1);
    }, 0);


    // Update footer
    $(api.column(colidx).footer()).html(
        '<b>' + formatNumberInr(pageTotal) + ' (' + formatNumberInr(total) + ')</b>');
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

function showChangeDropdown(dropdown) {
    $('.drophide').hide();
    var showele = $(dropdown).find("option:selected").data('show');
    if (showele) {
        $(showele).show();
    } else {
        $('#start_date').val("");
        $('#end_date').val("");

    }
}
function setHiddenValueByLink(obj, paaram) {
    if ($(obj).data('eleval') > 0) {
        $('input[name=status]').val(paaram);
        $('input[name=usrid]').val($(obj).data('complaintname'));
        callAjaxByComplaintDetail();
    }


}
