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
var recordTotal = [];
jQuery(document).ready(function ($) {

    drillDowntableContainer = $("#drilldownReport-table");
    compwisedrilldowntbl = $("#drilldownReport-compwise");
    $('#report-backbutton').hide();

    $('#backButton').click(function (e) {

        if ($('#selecteduserid').val()) {
            $('#selecteduserid').val("");
            callAjaxByUserNameType();
        } else if ($('#complainttypeid').val()) {
            $('#complainttypeid').val("");
            callAjaxByComplaintType();
        } else if ($('#deptid').val()) {
            $('#deptid').val("");
            callAjaxByDepartment();
            if ($('#mode').val() != 'ByBoundary') {
                $('#report-backbutton').hide();
            }
        } else if ($('#locality').val()) {
            $('#locality').val("");
            callAjaxByLocality();
        }
        else if ($('#boundary').val()) {
            $('#boundary').val("");
            callajaxdatatableForDrilDownReport();
        }

    });

    $('#drilldownReportSearch').click(function (e) {

        $('#locality').val("");
        $('#deptid').val("");
        $('#complainttypeid').val("");
        $('#selecteduserid').val("");
        $('#boundary').val("");
        $('#type').val("");

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
            if ($('#mode').val() == 'ByBoundary') {
                callajaxdatatableForDrilDownReport();
            } else {
                callAjaxByDepartment();
                $('#report-backbutton').hide();
            }
            return true;
        }
    });
});

function callajaxdatatableForDrilDownReport() {

    $('.report-section').removeClass('display-hide');
    $("#drilldownReport-table_wrapper").show();
    $("#drilldownReport-compwise_wrapper").hide();
    $('#report-footer').show();
    $('#report-backbutton').hide();

    loadNextLevelReports();
}

function callAjaxByDepartment() {

    $('.report-section').removeClass('display-hide');
    $("#drilldownReport-table_wrapper").show();
    $("#drilldownReport-compwise_wrapper").hide();
    $('#report-footer').show();
    $('#report-backbutton').show();

    loadNextLevelReports();

}
function callAjaxByLocality() {

    $('.report-section').removeClass('display-hide');
    $("#drilldownReport-table_wrapper").show();
    $("#drilldownReport-compwise_wrapper").hide();
    $('#report-footer').show();
    $('#report-backbutton').show();

    loadNextLevelReports();

}
function callAjaxByComplaintType() {

    $('.report-section').removeClass('display-hide');
    $("#drilldownReport-table_wrapper").show();
    $("#drilldownReport-compwise_wrapper").hide();
    $('#report-footer').show();
    $('#report-backbutton').show();

    loadNextLevelReports();

}
function callAjaxByUserNameType() {

    $('.report-section').removeClass('display-hide');
    $("#drilldownReport-table_wrapper").show();
    $("#drilldownReport-compwise_wrapper").hide();
    $('#report-footer').show();

    loadNextLevelReports();

}
function callAjaxByComplaintDetail() {

    var startDate = "";
    var endDate = "";
    var modeVal = "";
    var when_dateVal = "";
    startDate = $('#start_date').val();
    endDate = $('#end_date').val();
    modeVal = $('#mode').val();
    when_dateVal = $('#when_date').val();

    if ($('#start_date').val() == "")
        startDate = "";
    if ($('#end_date').val() == "")
        endDate = "";

    $('.report-section').removeClass('display-hide');
    $("#drilldownReport-table_wrapper").hide();
    $("#drilldownReport-compwise_wrapper").show();
    $('#report-footer').hide();

    var params = {
        "fromDate": startDate,
        "toDate": endDate,
        "groupBy": modeVal,
        "deptid": $('#deptid').val(),
        "complainttypeid": $('#complainttypeid').val(),
        "selecteduserid": $('#selecteduserid').val(),
        "type": $('#type').val(),
        "complaintDateType": when_dateVal
    };

    if ($("#boundary").length > 0) {
        params["boundary"] = $('#boundary').val();
    }

    if ($("#locality").length > 0) {
        params["locality"] = $('#locality').val();
    }

    compwisedrilldowntbl.dataTable({
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
                    window.open("/pgr/report/drilldown/download" + jsonParameters(params) + "&printFormat=PDF", '', 'scrollbars=yes,width=1300,height=700,status=yes');
                }
            },
            {
                text: 'XLS',
                action: function (e, dt, node, config) {
                    window.open("/pgr/report/drilldown/download" + jsonParameters(params) + "&printFormat=XLS", '_self');
                }
            }],
        ajax: {
            url: "/pgr/report/drilldown",
            data: function (args) {
                params["args"] = JSON.stringify(args);
                return params;
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


function loadNextLevelReports() {

    var startDate = "";
    var endDate = "";
    var modeVal = "";
    var when_dateVal = "";
    var startDate = $('#start_date').val();
    var endDate = $('#end_date').val();
    var modeVal = $('#mode').val();
    var when_dateVal = $('#when_date').val();

    if ($('#start_date').val() == "")
        startDate = "";
    if ($('#end_date').val() == "")
        endDate = "";

    var params = {
        "fromDate": startDate,
        "toDate": endDate,
        "groupBy": modeVal,
        "deptid": $('#deptid').val(),
        "complainttypeid": $('#complainttypeid').val(),
        "selecteduserid": $('#selecteduserid').val(),
        "type": $('#type').val(),
        "complaintDateType": when_dateVal
    };

    if ($("#boundary").length > 0) {
        params["boundary"] = $('#boundary').val();
    }
    if ($("#locality").length > 0) {
        params["locality"] = $('#locality').val();
    }

    getSumOfRecords(params);

    drillDowntableContainer.dataTable({
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
                    window.open("/pgr/report/drilldown/download" + jsonParameters(params) + "&reportTitle=" + reportTitle + "&printFormat=PDF", '', 'scrollbars=yes,width=1300,height=700,status=yes');
                }
            },
            {
                text: 'XLS',
                action: function (e, dt, node, config) {
                    window.open("/pgr/report/drilldown/download" + jsonParameters(params) + "&reportTitle=" + reportTitle + "&printFormat=XLS", '_self');
                }
            }],
        ajax: {
            url: "/pgr/report/drilldown",
            data: function (args) {
                params["args"] = JSON.stringify(args);
                return params;
            }
        },
        columns: [
            {
                "data": "name",
                "render": function (data, type, row) {
                    return renderAnchorLink(data);
                },
                "sTitle": getColumnTitle(),
                "name": getColumnName()
            }, {
                "mData": "registered",
                "sTitle": "Registered",
                "sClass": "text-right",
                "name": "registered"

            }, {
                "data": "inprocess",
                "sTitle": "Inprocess",
                "name": "inprocess",
                "sClass": "text-right"
            }, {
                "data": "completed",
                "sTitle": "Completed",
                "name": "completed",
                "sClass": "text-right"
            }, {
                "data": "rejected",
                "sTitle": "Rejected",
                "name": "rejected",
                "sClass": "text-right"

            }, {
                "data": "reopened",
                "sTitle": "Reopened",
                "name": "reopened",
                "sClass": "text-right"

            }, {
                "data": "withinsla",
                "sTitle": "Within SLA",
                "name": "withinSLA",
                "sClass": "text-right"
            }, {
                "data": "beyondsla",
                "sTitle": "Beyond SLA",
                "name": "beyondSLA",
                "sClass": "text-right"
            }, {
                "data": "total",
                "sTitle": "Total",
                "orderable": false,
                "sortable": false,
                "sClass": "text-right"

            }],
        "footerCallback": function (row, data, start, end, display) {
            var api = this.api(), data;
            if (data.length == 0) {
                $('#report-footer').hide();
            } else {
                $('#report-footer').show();
            }
            if (data.length > 0) {
                updateTotalFooter(1, api);
                updateTotalFooter(2, api);
                updateTotalFooter(3, api);
                updateTotalFooter(4, api);
                updateTotalFooter(5, api);
                updateTotalFooter(6, api);
                updateTotalFooter(7, api);
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

function renderAnchorLink(data) {

    var reportWise = "";
    var reportType = "";

    if ($('#complainttypeid').val()) {
        reportWise = "UserNameWise";
        reportType = "selecteduserid";
    }
    else if ($('#deptid').val()) {
        reportWise = "ComplaintTypeWise";
        reportType = "complainttypeid";
    }
    else if ($('#locality').length > 0 && $('#locality').val()) {
        reportWise = "Departmentwise";
        reportType = "deptid";
    }
    else if ($('#boundary').length > 0 && $('#boundary').val()) {
        reportWise = "LocalityWise";
        reportType = "locality";
    }
    else if ($('#boundary').length > 0) {
        reportWise = "Boundarywise";
        reportType = "boundary";
    }
    else if ($('#deptid').length > 0) {
        reportWise = "Departmentwise";
        reportType = "deptid";
    }
    return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'' + reportWise + '\');" data-hiddenele="' + reportType + '" data-eleval="'
        + data + '">' + data + '</a>';
}

var reportTitle;
function getColumnTitle() {
    if ($('#complainttypeid').val()) {
        return reportTitle = "User name-Position";
    }
    else if ($('#deptid').val()) {
        return reportTitle = "Grievance Type Name";
    }
    else if ($('#locality').length > 0 && $('#locality').val()) {
        return reportTitle = "Department Name";
    }
    else if ($('#boundary').length > 0 && $('#boundary').val()) {
        return reportTitle = "Locality Name";
    }
    else if ($('#boundary').length > 0) {
        return reportTitle = "Boundary Name";
    }
    else if ($('#deptid').length > 0) {
        return reportTitle = "Department Name";
    }
}

function getColumnName() {
    if ($('#complainttypeid').val()) {
        return "employeePosition";
    }
    else if ($('#deptid').val()) {
        return "complaintTypeName";
    }
    else if ($('#locality').length > 0 && $('#locality').val()) {
        return "department";
    }
    else if ($('#boundary').length > 0 && $('#boundary').val()) {
        return "locality";
    }
    else if ($('#boundary').length > 0) {
        return "parentBoundary";
    }
    else if ($('#deptid').length > 0) {
        return "department";
    }
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

    $('input[name=type]').val(paaram);
    $('input[name=' + $(obj).data('hiddenele') + ']')
        .val($(obj).data('eleval'));
    if (paaram == 'Departmentwise') {
        // bootbox.alert('call complaint type');
        callAjaxByComplaintType();
    } else if (paaram == 'Boundarywise') {
        // bootbox.alert('call locality type');
        callAjaxByLocality();
    } else if (paaram == 'ComplaintTypeWise') {
        callAjaxByUserNameType();
    } else if (paaram == 'UserNameWise') {
        callAjaxByComplaintDetail();
    } else if (paaram == 'LocalityWise') {
        callAjaxByDepartment();
    }
}

function getSumOfRecords(pramdata) {
    $.ajax({

        url: "/pgr/report/drilldown/grand-total" + jsonParameters(pramdata),
        type: 'GET',
        async: false,

        success: function (data) {
            recordTotal = [];
            for (var i = 0; i < data.length; i++) {
                recordTotal.push(data[i]);
            }
        }
    })
}

function jsonParameters(obj) {
    var parts = [];
    for (var key in obj) {
        if (obj.hasOwnProperty(key)) {
            parts.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
        }
    }
    return "?" + parts.join('&');
}

function add(a, b) {
    return a + b;
}

function updateTotalFooter(colidx, api) {
    // Remove the formatting to get integer data for summation
    var intVal = function (i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
            : typeof i === 'number' ? i : 0;
    };

    // Total over all pages
    var total;

    if (colidx > recordTotal.length) {
        total = recordTotal.concat().splice(5, 7).reduce(add, 0);
    }
    else {
        // Total over all pages
        total = recordTotal[colidx - 1];
    }

    // Total over this page
    pageTotal = api.column(colidx, {
        page: 'current'
    }).data().reduce(function (a, b) {
        return intVal(a) + intVal(b);
    }, 0);

    // Update footer
    $(api.column(colidx).footer()).html(
        '<b>' + formatNumberInr(pageTotal) + ' (' + formatNumberInr(total)
        + ')</b>');
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