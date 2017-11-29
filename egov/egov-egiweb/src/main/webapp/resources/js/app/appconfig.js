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

    $('#appConfigModuleName').change(function () {
        $.ajax({
            url: "/egi/app/config/formodule/" + $('#appConfigModuleName').val(),
            type: "GET",
            dataType: "json",
            success: function (response) {
                var appConfigDrpDown = $('#appConfigKeyName');
                appConfigDrpDown.find("option:gt(0)").remove();
                $.each(response, function (index, value) {
                    appConfigDrpDown.append($('<option>').text(value.keyName).attr('value', value.keyName));
                });

            },
            error: function (response) {
                console.log("failed");
            }
        });
    });

    $('#search-view-btn').on('click', function () {
        oTable = $('#view-appConfig-tbl').DataTable({
            processing: true,
            serverSide: true,
            type: 'GET',
            sort: true,
            filter: true,
            responsive: true,
            destroy: true,
            "autoWidth": false,
            "order": [[1, 'asc']],
            ajax: {
                url: "list",
                type: "GET",
                data: function (args) {
                    return {"args": JSON.stringify(args), "moduleName": $("#moduleName").val()};
                }
            },
            "aLengthMenu": [[10, 25, 50, -1],
                [10, 25, 50, "All"]],
            "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
            columns: [
                {
                    "className": 'details-control',
                    "orderable": false,
                    "sortable": false,
                    "searchable": false,
                    "data": null,
                    "defaultContent": ''
                },
                {
                    "mData": "module",
                    'name': "module.name",
                    "sTitle": "Module Name"
                },
                {
                    "mData": "keyName",
                    "name": "keyName",
                    "sTitle": "Key Name",
                }, {
                    "mData": "description",
                    "name": "description",
                    "sTitle": "Description"
                }, {
                    "mData": "id",
                    "orderable": false,
                    "sortable": false,
                    "searchable": false,
                    "visible": false
                }, {
                    "mData": "values",
                    "orderable": false,
                    "sortable": false,
                    "searchable": false,
                    "visible": false
                }]
        });
    });

    // Add event listener for opening and closing details
    $('#view-appConfig-tbl').on('click', 'tbody tr td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = oTable.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        }
        else {
            // Open this row
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });

    function format(d) {
        // `d` is the original data object for the row
        var tablerows = '';
        $.each(d.values, function (index, value) {
            console.log(value["Effective Date"] + '<--->' + value["Value"]);
            var tr = '<tr><td>' + value["Effective Date"] + '</td><td>' + value["Value"] + '</td></tr>';
            tablerows += tr;
        });
        return '<table class="table table-bordered" style="width: 90%;margin: 0 auto;"><thead><th>Effective Date</th><th>Values</th></thead><tbody>' + tablerows + '</tbody></table>';
    }


    $('#addrow').click(function () {
        var count = $("#configs tbody  tr").length - 1;
        var $tableBody = $('#configs').find("tbody"),
            $trLast = $tableBody.find("tr:last"),
            $trNew = $trLast.clone();

        if (!$.trim($trLast.find('input.effectiveFrom').val()) || !$.trim($trLast.find('input.confValues').val())) {
            bootbox.alert('Date effective from and values are mandatory!');
        } else {
            count++;
            $trNew.find("input").each(function () {
                $(this).attr({
                    'name': function (_, name) {
                        return name.replace(/\[.\]/g, '[' + count + ']');
                    },
                    'id': function (_, id) {
                        return id.replace(/\[.\]/g, '[' + count + ']');
                    }
                });
            });
            $trLast.after($trNew);
            $trNew.show().find('input').val('').removeAttr('disabled').addClass('dynamicInput');
            $trNew.find('input.markedForRemoval').val('false');
            dateinitialize();
        }
    });

    var regexp_alphanumericcomma = /[^a-zA-Z0-9 ,]/g;

    $('.confValues').on('keyup', function (e) {
        obj = $(this);
        if (jQuery(obj).val().match(regexp_alphanumericcomma)) {
            jQuery(obj).val(jQuery(obj).val().replace(regexp_alphanumericcomma, ''));
        }
    });

    function dateinitialize() {
        $(".datepicker").datepicker({
            format: "dd/mm/yyyy",
            autoclose: true
        });
    }

    $(document).on('click', '#deleterow', function () {
        var length = $('#configs').find("tbody tr:visible").length;
        if (length == 1) {
            bootbox.alert('First row cannot be deleted!');
        } else {
            if ($(this).data('func')) {
                deleteandreplaceindexintable($(this));
            } else {
                if ($(this).closest('tr').find('input').hasClass('dynamicInput')) {
                    console.log('Dynamic Row deleted');
                    deleteandreplaceindexintable($(this));
                }
                else {
                    console.log('Existing Row deleted');
                    $(this).closest('tr').hide().find('input.markedForRemoval').val('true');
                }

            }

        }

    })

});

function deleteandreplaceindexintable(obj) {
    obj.closest('tr').remove();
    var idx = 0;
    //regenerate index existing inputs in table row
    jQuery("#configs tbody tr").each(function () {
        jQuery(this).find("input").each(function () {
            jQuery(this).attr({
                'id': function (_, id) {
                    return id.replace(/\[.\]/g, '[' + idx + ']');
                },
                'name': function (_, name) {
                    return name.replace(/\[.\]/g, '[' + idx + ']');
                }
            });
        });
        idx++;
    });
}