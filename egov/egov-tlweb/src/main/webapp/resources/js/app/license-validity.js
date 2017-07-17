/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *  
 *  Copyright (C) 2017  eGovernments Foundation
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

$('#basedOnFinancialYear').change(function () {
    if ($(this).is(':checked')) {
        $('.nonfindata').hide();
        $('#day').val('0');
        $('#month').val('0');
        $('#week').val('0');
        $('#year').val('0');
    } else
        $('.nonfindata').show();
});

$('#basedOnFinancialYear').trigger('change');

$('#btnsearch').click(function (e) {
    drillDowntableContainer = $("#resultTable");
    $('.report-section').removeClass('display-hide');
    reportdatatable = drillDowntableContainer
        .dataTable({
            ajax: {
                url: "/tl/validity/search" + "?licenseCategory=" + $('#licenseCategory').val() + "&natureOfBusiness=" + $("#natureOfBusiness").val(),
                type: "POST"
            },
            "fnRowCallback": function (row, data, index) {

            },
            "bDestroy": true,
            "autoWidth": false,
            "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
            "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "oTableTools": {
                "sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
                "aButtons": ["xls", "pdf", "print"]
            },
            aaSorting: [],
            columns: [
                {
                    "data": "natureOfBusiness",
                    "sClass": "text-left"
                }, {
                    "data": "licenseCategory",
                    "sClass": "text-left"
                }, {
                    "data": "basedOnFinancialYear",
                    "sClass": "text-left"
                },
                {
                    "data": "day",
                    "sClass": "text-right"
                }, {
                    "data": "week",
                    "sClass": "text-right"
                }, {
                    "data": "month",
                    "sClass": "text-right"
                }, {
                    "data": "year",
                    "sClass": "text-right"
                },
                {
                    "data": null,
                    'sClass': "text-center",
                    "bSortable": false,
                    "target": -1,
                    "defaultContent": '<span class="add-padding"><i class="fa fa-pencil-square-o fa-lg edit"></i></span><span class="add-padding"><i class="fa fa-eye fa-lg view"></i></span>'
                }
                ,
                {"data": "id", "visible": false}
            ]
        });
});

$("#resultTable").on('click', 'tbody tr td .edit', function (event) {
    var id = reportdatatable.fnGetData($(this).parent().parent(), 8);
    var url = '/tl/validity/update/' + id;
    window.open(url, id, 'width=900, height=700, top=300, left=260,scrollbars=yes');
});

$("#resultTable").on('click', 'tbody tr td .view', function (event) {
    var id = reportdatatable.fnGetData($(this).parent().parent(), 8);
    var url = '/tl/validity/view/' + id;
    window.open(url, id, 'width=900, height=700, top=300, left=260,scrollbars=yes');
});

