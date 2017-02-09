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

$(document).ready(function () {

    $("#natureOfBusiness").change(function () {
        if ($("#natureOfBusiness option:selected").text() == 'Permanent')
            $(".sameForPermanentAndTemporary").show();
        else
            $(".sameForPermanentAndTemporary").hide();
    });

    $("#licenseAppType").change(function () {
        if ($("#licenseAppType option:selected").text() == 'New')
            $(".sameForNewAndRenew").show();
        else
            $(".sameForNewAndRenew").hide();
    });

    $("#natureOfBusiness").trigger('change');
    $("#licenseAppType").trigger('change');

    $('#feeType').click(function () {
        if ($('#subCategory').val() == "") {
            bootbox.alert("Please Choose Sub Category");
            return false;
        }
    });

    $('#unitOfMeasurement').click(function () {
        if ($('#feeType').val() == "") {
            bootbox.alert("Please Choose Fee Type");
            return false;
        }
    });

    $('#financialYear').change(function () {
        var finId = $(this).val();
        var finRange = $("#fin" + finId).val().split("-");
        var frmDate = finRange[0].split("/");
        var toDate = finRange[1].split("/");
        var effectiveFrom = new Date(frmDate[2], parseInt(frmDate[1]) - 1, parseInt(frmDate[0]));
        var effectiveTo = new Date(toDate[2], parseInt(toDate[1]) - 1, parseInt(toDate[0]));
        $("#effectiveFrom").val(finRange[0]);
        $("#effectiveTo").datepicker('setDate', effectiveTo);
    });

    $('#feeType').change(function () {
        $.ajax({
            url: "/tl/feeType/uom-by-subcategory?feeTypeId=" + $('#feeType').val() + "&subCategoryId=" + $('#subCategory').val() + "",
            type: "GET",
            async: false,
            dataType: "json",
            success: function (response) {
                $('#unitOfMeasurement').empty();
                $('#unitOfMeasurement').append($("<option value=''>Select</option>"));
                $.each(response, function (index, value) {
                    $('#unitOfMeasurement').append("<option value=" + value.uom.id + ">" + value.uom.name + "</option>");
                    $('#rateType').val(value.rateType);
                });
                $("#unitOfMeasurement").prop("selectedIndex", 1);
            },
            error: function () {
            }
        });
    });

    $('#subCategory').change(function () {
        $('#unitOfMeasurement').empty();
        $('#unitOfMeasurement').append($("<option value=''>Select</option>"));
        $('#rateType').val("");
        $.ajax({
            url: "/tl/feeType/feetype-by-subcategory",
            type: "GET",
            async: false,
            data: {
                subCategoryId: $('#subCategory').val()
            },
            dataType: "json",
            success: function (response) {
                var feeType = $('#feeType')
                feeType.find("option:gt(0)").remove();
                $.each(response, function (index, value) {
                    feeType.append($('<option>').text(value.feeType.name).attr('value', value.feeType.id));
                });

            },

        })
    });

    $('#licenseCategory').change(function () {
        var results = [];
        $('#feeType').empty();
        $('#feeType').append($("<option value=''>Select</option>"));
        $('#unitOfMeasurement').empty();
        $('#unitOfMeasurement').append($("<option value=''>Select</option>"));
        $('#rateType').val("");
        $.ajax({
            url: "/tl/licensesubcategory/subcategories-by-category",
            type: "GET",
            async: false,
            data: {
                categoryId: $('#licenseCategory').val()
            },
            dataType: "json",
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
        })
    });

    $('#result tbody').on('click', 'tr td .delete-row', function () {
        var id = $(this).closest('tr').find('td:eq(0) .detailId').val();
        var idx = $(this).closest('tr').index();
        var obj = $(this);
        if (idx == 0) {
            bootbox.alert('Cannot delete first row!');
        } else if ((idx < ($('#result tbody tr').length - 1))) {
            bootbox.alert('Try to delete from last row!');
        } else {
            bootbox.confirm("Do you want to delete this fee data ? ", function (result) {
                if (result) {

                    if (obj.data('func')) {
                        obj.closest('tr').remove();
                    } else {
                        if (obj.closest('tr').hasClass('dynamicInput'))
                            obj.closest('tr').remove();
                        else
                            obj.closest('tr').hide().find('input.markedForRemoval').val('true');

                    }
                }
            });
        }
    });

    $("#addrow").click(
        function (event) {
            var rowCount = $('#result tbody tr').length;
            var valid = true;
            //validate all rows before adding new row
            $('#result tbody tr').each(function (index, value) {
                $('#result tbody tr:eq(' + index + ') td input[type="text"]').each(function (i, v) {
                    if (!$.trim($(v).val())) {
                        valid = false;
                        bootbox.alert("Enter all values for existing rows!", function () {
                            $(v).focus();
                        });
                        return false;
                    }
                });
            });
            if (valid) {
                //Create new row
                var newRow = $('#result tbody tr:first').clone();
                newRow.find("input").each(function () {
                    $(this).attr({
                        'name': function (_, name) {
                            return name.replace(/\[.\]/g, '[' + rowCount + ']');
                        }
                    });
                });
                $('#result tbody').append(newRow);
                $('#result tbody tr:last').addClass('dynamicInput');
                var prev_tovalue = $('#result tbody tr:eq(' + (rowCount - 1) + ')').find('input.tovalue').val();
                var currentRowObj = $('#result tbody tr:last');
                currentRowObj.find('input').val('');
                currentRowObj.find('input.fromvalue').val(prev_tovalue);
                currentRowObj.find('input.markedForRemoval').val('false');
                patternvalidation();
            }
        });

    var oTable;
    $('#search').click(function (e) {
        $('.report-section').removeClass('display-hide');
        oTable = $('#resultTable').DataTable({
            ajax: {
                url: "search?categoryId=" + $('#licenseCategory').val()
                + "&subcategoryId=" + $("#subCategory").val()
                + "&financialYearId=" + $("#financialYear").val(),
                type: "POST"
            },
            dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l>" +
            "<'col-md-3 col-xs-6 text-right'B><'col-md-4 col-xs-6 text-right'p>>",
            "autoWidth": false,
            "bDestroy": true,
            buttons: [{
                extend: 'pdf',
                title: 'License Fee Matrix',
                filename: 'License Fee Matrix',
                orientation: 'landscape',
                footer: true,
                pageSize: 'A3',
                exportOptions: {
                    columns: ':visible'
                }
            }, {
                extend: 'excel',
                filename: 'License Fee Matrix',
                footer: true,
                exportOptions: {
                    columns: ':visible'
                }
            }, {
                extend: 'print',
                title: 'License Fee Matrix',
                filename: 'License Fee Matrix',
                footer: true,
                exportOptions: {
                    columns: ':visible'
                }
            }],
            columns: [
                {
                    "bSortable": false,
                    "visible": false
                },
                {
                    "className": 'details-control',
                    "sOrderable": false,
                    "bSortable": false,
                    "data": null,
                    "defaultContent": '<i class="fa fa-plus-circle fa-lg"></i>'
                },
                {
                    "data": "natureOfBussiness",
                    "sClass": "text-left"
                }, {
                    "data": "licenseAppType",
                    "sClass": "text-left"
                }, {
                    "data": "licenseCategory",
                    "sClass": "text-left"
                }, {
                    "data": "subCategory",
                    "sClass": "text-left"
                }, {
                    "data": "uom",
                    "sClass": "text-left"
                }, {
                    "data": "feeType",
                    "sClass": "text-right"
                }, {
                    "data": "financialYear",
                    "sClass": "text-right"
                }, {
                    "data": "details",
                    "visible": false,
                    "sort": false
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
                }
            ]
        });
    });

    $("#resultTable").on('click', 'tbody tr td span i.edit', function (event) {
        var id = oTable.row($(this).closest('tr')).data().id;
        var url = '/tl/feematrix/update/' + id;
        window.open(url, id, 'width=900, height=700, top=300, left=260,scrollbars=yes');

    });

    $("#resultTable").on('click', 'tbody tr td span i.view', function (event) {
        var id = oTable.row($(this).closest('tr')).data().id;
        var url = '/tl/feematrix/view/' + id;
        window.open(url, id, 'width=900, height=700, top=300, left=260,scrollbars=yes');

    });

    // Add event listener for opening and closing details
    $('#resultTable').on('click', 'tbody tr td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = oTable.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        }
        else {
            row.child(populateChildTable(row.data())).show();
            tr.addClass('shown');
        }
    });

    function populateChildTable(d) {
        // `d` is the original data object for the row
        var tablerows = '';
        $.each(d.details, function (index, value) {
            var tr = '<tr><td class="text-right">' + value["uomFrom"] + '</td><td class="text-right">' + value["uomTo"] + '</td><td class="text-right">' + value["amount"] + '</tr>';
            tablerows += tr;
        });
        return '<table class="table table-bordered" style="width: 90%;margin: 0 auto;">' +
            '<thead><th>UOM From</th><th>UOM To</th><th>Amount</th></thead><tbody>' + tablerows + '</tbody></table>';
    }

    if ($("#licenseCategory").val() != '') {
        $("#licenseCategory").trigger('change');
        $("#subCategory").val(subCategory).trigger('change');
        $('#subCategory').trigger('change');
        $('#feeType').val(feeType);
        $('#feeType').trigger('change');
        $('#unitOfMeasurement').val(uom);
    }
});

