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

jQuery(document).ready(function () {
    $('#dailyCollectionReport-header').hide();
    $('#report-footer').hide();

    function validDateRange(start, end) {

        if (start.getTime() > end.getTime()) {
            bootbox.alert("From date  should not be greater than the To Date.");
            $('#toDate').val('');
            return false;
        } else
            return true;

    }

    $.fn.dataTable.ext.errMode = function () {
        $('.loader-class').modal('hide');
    };

    $('#dailyCollectionReportSearch').click(function (e) {
        try {
            $('#dailyCollectionReport-header').hide();
            if ($('#dailyCollectionform').valid()) {
                $('.loader-class').modal('show', {backdrop: 'static'});
                validDateRange($("#fromDate").datepicker('getDate'), $("#toDate").datepicker('getDate'));
                var frmDt = $("#fromDate").val();
                var toDt = $("#toDate").val();
                $('#frmDt').text(frmDt);
                $('#toDt').text(toDt);
                var oTable = $('#dailyCollReport-table');
                $.post("/tl/reports/dailycollectionreport", $('#dailyCollectionform').serialize())
                    .done(function (searchResult) {
                        $('#dailyCollectionReport-header').show();
                        oTable.DataTable({
                            "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                            dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l>" +
                            "<'col-md-2 col-xs-6 text-left'B><'col-md-5 col-xs-6 text-right'p>>",
                            "autoWidth": true,
                            "bDestroy": true,
                            responsive: true,
                            destroy: true,
                            scrollX: true,
                            buttons: [{
                                extend: 'pdf',
                                title: 'Trade License Daily Collection Report [' + frmDt + ' - ' + toDt + ']',
                                filename: 'TL_DCR_' + frmDt + ' - ' + toDt,
                                orientation: 'landscape',
                                pageSize: 'A4',
                                footer: true,
                                exportOptions: {
                                    columns: ':visible'
                                }
                            }, {
                                extend: 'excel',
                                title: 'Trade License Daily Collection Report [' + frmDt + ' - ' + toDt + ']',
                                filename: 'TL_DCR_' + frmDt + ' - ' + toDt,
                                footer: true,
                                exportOptions: {
                                    columns: ':visible'
                                }
                            }, {
                                extend: 'print',
                                title: 'Trade License Daily Collection Report [' + frmDt + ' - ' + toDt + ']',
                                filename: 'TL_DCR_' + frmDt + ' - ' + toDt,
                                orientation: 'landscape',
                                pageSize: 'A4',
                                footer: true,
                                exportOptions: {
                                    columns: ':visible'
                                }
                            }],
                            searchable: true,
                            data: searchResult,
                            columns: [
                                {title: 'Receipt No.', data: 'receiptNumber'},
                                {
                                    title: 'Receipt Date', data: 'receiptDate',
                                    render: function (data, type, full) {
                                        if (data != null && data != undefined && data != undefined) {
                                            var regDateSplit = data.split("T")[0].split("-");
                                            return regDateSplit[2] + "/" + regDateSplit[1] + "/" + regDateSplit[0];
                                        }
                                        else return "";
                                    }
                                },
                                {title: 'License / Application No.', data: 'consumerCode'},
                                {title: 'Owner Name', data: 'consumerName'},
                                {title: 'Ward', data: 'revenueWard'},
                                {title: 'Paid At', data: 'channel'},
                                {title: 'Payment mode', data: 'paymentMode'},
                                {title: 'Status', data: 'status'},
                                {title: 'Paid From', data: 'installmentFrom'},
                                {title: 'Paid To', data: 'installmentTo'},
                                {title: 'Arrears', data: 'arrearAmount', "className": "text-right"},
                                {title: 'Current', data: 'currentAmount', "className": "text-right"},
                                {title: 'Penalty', data: 'latePaymentCharges', "className": "text-right"},
                                {title: 'Collection', data: 'totalAmount', "className": "text-right"}
                            ],
                            "aaSorting": [[4, 'desc']],
                            "footerCallback": function (row, data, start, end, display) {
                                var api = this.api(), data;
                                if (data.length == 0) {
                                    jQuery('#report-footer').hide();
                                } else {
                                    jQuery('#report-footer').show();
                                }
                                if (data.length > 0) {
                                    updateTotalFooter(10, api);
                                    updateTotalFooter(11, api);
                                    updateTotalFooter(12, api);
                                    updateTotalFooter(13, api);
                                }
                            },
                            "initComplete": function (settings, json) {
                                $('.loader-class').modal('hide');
                            },
                            "aoColumnDefs": [{
                                "aTargets": [10, 11, 12, 13],
                                "mRender": function (data, type, full) {
                                    return formatNumberInr(data);
                                }
                            }]
                        });
                        e.stopPropagation();
                        $('.loader-class').modal('hide');
                    });
                return true;
            }
            else
                return false;
        } catch (e) {
            $('.loader-class').modal('hide');
            console.log(e);
        }
    });
});


function updateTotalFooter(colidx, api) {
    // Remove the formatting to get integer data for summation
    var intVal = function (i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
            : typeof i === 'number' ? i : 0;
    };

    // Total over all pages
    var total = api.column(colidx).data().reduce(function (a, b) {
        return intVal(a) + intVal(b);
    });

    // Total over this page
    var pageTotal = api.column(colidx, {
        page: 'current'
    }).data().reduce(function (a, b) {
        return intVal(a) + intVal(b);
    }, 0);

    // Update footer
    $(api.column(colidx).footer()).html(
        formatNumberInr(pageTotal) + ' (' + formatNumberInr(total)
        + ')');
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

