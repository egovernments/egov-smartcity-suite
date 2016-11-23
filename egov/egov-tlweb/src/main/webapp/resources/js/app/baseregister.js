/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

$(document).ready(function () {

    $('#category').change(function () {
        $.ajax({
            url: "/tl/licensesubcategory/subcategories-by-category",
            type: "GET",
            data: {
                categoryId: $('#category').val()
            },
            dataType: "json",
            success: function (response) {
                var subCategory = $('#subCategory')
                subCategory.find("option:gt(0)").remove();
                $.each(response, function (index, value) {
                    subCategory.append($('<option>').text(value.name).attr('value', value.id));
                });
            }
        })
    });

    $('#btnsearch').click(function (e) {
        onSubmitEvent(e);
        return false;
    });
});

function openTradeLicense(id) {
    window.open("../viewtradelicense/viewTradeLicense-view.action?id=" + id, '', 'scrollbars=yes,width=1000,height=700,status=yes');
}
function onSubmitEvent(event) {
    $('.report-section').removeClass('display-hide');
    event.preventDefault();
    var reportdatatable = $("#baseregistertbl")
        .dataTable({
            type: 'GET',
            responsive: true,
            destroy: true,
            ajax: {
                url: "/tl/baseregister/search-resultList?" + $("#baseregisterform").serialize()
            },
            "autoWidth": false,
            "bDestroy": true,
            "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
            "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "oTableTools": {
                "sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
                "aButtons": ["xls", "pdf", "print"]
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
                        return '<a href="javascript:void(0);" onclick="openTradeLicense(' + row.licenseId + ');">' + data.name + '</a>';
                    },
                    "sTitle": "TIN NO."
                }, {
                    "data": "tradetitle",
                    "sTitle": "Trade Title"
                }, {
                    "data": "category",
                    "sTitle": "Category"
                }, {
                    "data": "subcategory",
                    "sTitle": "Subcategory"
                }, {
                    "data": "owner",
                    "sTitle": "Trade Owner"
                }, {
                    "data": "mobile",
                    "sTitle": "Owner MobileNo"
                }, {
                    "data": "assessmentno",
                    "sTitle": "Assessment No"
                }, {
                    "data": "wardname",
                    "sTitle": "Ward No"
                }, {
                    "data": "localityname",
                    "sTitle": "Locality"
                }, {
                    "data": "tradeaddress",
                    "sTitle": "Trade Address"
                }, {
                    "data": "commencementdate",
                    "sTitle": "Commencement Date"
                }, {
                    "data": "status",
                    "sTitle": "Status"
                }, {
                    "data": "arrearlicfee",
                    "sTitle": "License Fee(Arrears)"
                }, {
                    "data": "arrearpenaltyfee",
                    "sTitle": "Penalty(Arrears)"
                }, {
                    "data": "curlicfee",
                    "sTitle": "License Fee(Current)"
                }, {
                    "data": "curpenaltyfee",
                    "sTitle": "Penalty(Current)"
                }],
            "aaSorting": [[2, 'asc']],
            "footerCallback": function (row, data, start, end, display) {
                var api = this.api(), data;
                if (data.length == 0) {
                    jQuery('#report-footer').hide();
                } else {
                    jQuery('#report-footer').show();
                }
                if (data.length > 0) {
                    updateTotalFooter(12, api);
                    updateTotalFooter(13, api);
                    updateTotalFooter(14, api);
                    updateTotalFooter(15, api);
                }
            },
            "aoColumnDefs": [{
                "aTargets": [12, 13, 14, 15],
                "mRender": function (data, type, full) {
                    return formatNumberInr(data);
                }
            }]
        });
    jQuery('.loader-class').modal('hide');
}

function updateTotalFooter(colidx, api) {
    // Remove the formatting to get integer data for summation
    var intVal = function (i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
            : typeof i === 'number' ? i : 0;
    };

    // Total over all pages
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
    jQuery(api.column(colidx).footer()).html(
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