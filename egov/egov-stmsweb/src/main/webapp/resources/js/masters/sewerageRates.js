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

    var isSubmitForm = false;
    $("#submitform").click(function () {
        if (isSubmitForm) {
            return true;
        }
        if ($("#sewerageRatesMasterform").valid()) {
            if (!validateEffectiveDate()) {
                return false;
            }
            else {
                if ($('#fromDate').val() != undefined)
                    fromDateAndCombinationValidation();
            }
        }
        return false;
    });

    function fromDateAndCombinationValidation() {
        $.ajax({
            url: '/stms/masters/fromDateValidationWithLatestActiveRecord',

            type: "GET",
            data: {
                propertyType: $('#propertyType').val(),
                fromDate: $('#fromDate').val(),
            },
            dataType: 'json',
            success: function (response) {
                if (response != "true") {
                    bootbox.alert(" The effecive from date should not be less than " + response);
                    return false;
                }
                else {
                    if (!sewerageMasterCombination()) {
                        return false;
                    }
                    else {
                        isSubmitForm = true;
                        $('#submitform').trigger('click');
                    }
                }
            },
            error: function (response) {
                console.log("failed");
            }
        });
    }

    function sewerageMasterCombination() {
        $.ajax({
            url: '/stms/masters/ajaxexistingseweragevalidate',
            type: "GET",
            data: {
                propertyType: $('#propertyType').val(),
                fromDate: $('#fromDate').val(),
                monthlyRate: $('#monthlyRate').val()
            },
            dataType: 'json',
            success: function (response) {
                if (response > 0) {
                    if (!overwriteSewerageRate(response))
                        return false;
                } else {
                    isSubmitForm = true;
                    $('#submitform').trigger('click');
                }
            },
            error: function (response) {
                console.log(response);
            }
        });
    }

    function overwriteSewerageRate(res) {
        bootbox.confirm("With entered combination monthly rate is present. Do you want to overwrite it?", function (result) {
            if (result) {
                isSubmitForm = true;
                $('#submitform').trigger('click');
            }
        });
        return false;
    }

    $('#propertyType option').each(function () {     // remove  propety type mixed option and underscore
        var $this = $(this);
        $this.text($this.text().replace(/_/g, ' '));
        if ($this.text() == "MIXED")
            $(this).remove();
    });

    function validateEffectiveDate() {
        var fromdate = $('#fromDate').val();
        var todaysDate = getTodayDate();
        if (compareDate(fromdate, todaysDate) == 1) {
            bootbox.alert($("#err-validate-effective-date").text());
            $(this).val("");
            return false;
        } else {
            return true;
        }
    }

    $('#monthlyRate').keyup(function (e) {    // validate two decimal points
        var regex = /^\d+(\.\d{0,2})?$/g;
        if (!regex.test(this.value)) {
            $(this).val($(this).getNum());
        }
    });

    jQuery.fn.getNum = function () {
        var val = $.trim($(this).val());
        if (val.indexOf(',') > -1) {
            val = val.replace(',', '.');
        }
        var num = parseFloat(val);
        var num = num.toFixed(2);
        if (isNaN(num)) {
            num = '';
        }
        return num;
    }

    var datatbl = $('#sewerage_master_rates_table');
    var prevdatatable;
    $('#search').click(function (e) {
        if ($("#sewerageRatesViewForm").valid()) {
            $('#sewerage_master_rates_table_wrapper').show();
            datatbl.dataTable({
                "ajax": {
                    url: "/stms/masters/search-sewerage-rates?" + $("#sewerageRatesViewForm").serialize(),
                    type: "GET"
                },
                "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-xs-12 col-md-3 col-right' <'export-data'T>><'col-md-3 col-xs-6 text-right' p>>",
                "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                "autoWidth": false,
                "bDestroy": true,
                "oTableTools": {
                    "sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
                    "aButtons": [
                        {
                            "sExtends": "xls",
                            "mColumns": [1, 2, 3]
                        },
                        {
                            "sExtends": "pdf",
                            "mColumns": [1, 2, 3]
                        },
                        {
                            "sExtends": "print"
                        }]
                },
                "columns": [
                    {"title": "S.No", "className": "text-left", "width": "7%"},
                    {"data": "id", "visible": false},
                    {"data": "propertyType", "title": "Property Type", "className": "text-left"},
                    {"data": "monthlyRate", "title": "Monthly Rate", "className": "text-left"},
                    {"data": "fromDate", "title": "Effective From Date", "className": "text-left"},
                    {
                        "data": "isActive",
                        "title": "Status",
                        "render": function (data, meta, row, type) {
                            return (data ? "ACTIVE" : "INACTIVE");
                        }
                    },
                    {"data": "modifiedDate", "title": "Modified Date"},
                    {
                        "data": "",
                        "title": "Actions",
                        "render": function (data, type, row, meta) {
                            var editAction = '<span class="add-padding"><i class="fa fa-edit history-size" class="tooltip-secondary" data-toggle="tooltip" title="Edit"></i></span>';
                            var viewAction = '<span class="add-padding"><i class="fa fa-eye history-size" class="tooltip-secondary" data-toggle="tooltip" title="View"></i></span>';
                            return ((row.isActive && row.isEditable) ? editAction + viewAction : viewAction);
                        }
                    }
                ],
                "fnRowCallback": function (nRow, aData, iDisplayIndex, oSettings) {
                    $("td:first", nRow).html(iDisplayIndex + 1);
                    return nRow;
                }
            });
        } else {
            $('#sewerage_master_rates_table_wrapper').hide();
        }
        e.stopPropagation();
    });

    $("#sewerage_master_rates_table").on('click', 'tbody tr td i.fa-edit', function (e) {
        var sewerageRatesId = datatbl.fnGetData($(this).parent().parent().parent(), 1);
        window.open("update/" + sewerageRatesId, '' + sewerageRatesId + '', 'width=900, height=700, top=300, left=150, scrollbars=yes');
    });

    $("#sewerage_master_rates_table").on('click', 'tbody tr td i.fa-eye', function (e) {
        var sewerageRatesId = datatbl.fnGetData($(this).parent().parent().parent(), 1);
        window.open("rateView/" + sewerageRatesId, '' + sewerageRatesId + '', 'width=900, height=700, top=300, left=150, scrollbars=yes');
    });

    $('#fromDate').datepicker('setEndDate', $('#effectiveEndDate').val());

    $("#propertyType").change(function () {
        $.ajax({
            url: "/stms/masters/fromDateValues-by-propertyType",
            type: "GET",
            data: {
                propertyType: $('#propertyType').val()
            },
            dataType: "json",
            success: function (response) {
                $('#effectiveFromDate').empty();
                $('#effectiveFromDate').append($("<option value=''>Select from below</option>"));
                $.each(response, function (index, value) {
                    var date = new Date(value);
                    var day = date.getDate();
                    var month = date.getMonth() + 1;
                    if (day < 10) {
                        day = '0' + day;
                    }
                    if (month < 10) {
                        month = '0' + month;
                    }
                    date = day + '/' + month + '/' + date.getFullYear();
                    $('#effectiveFromDate').append($('<option>').text(date).attr('value', date));
                });
            },
            error: function (response) {
                console.log("failed");
            }
        });
    });

    $(".btn-addNewRow").click(function () {
        if ($('#sewerageRatesMasterform').valid()) {
            if (checkUniqueNumberOfClosets()) {
                var currentIndex = $("#sewerageDetailMasterTable tr").length;
                addNewRowToTable(currentIndex);
            }
        }
    });

    function addNewRowToTable(currentIndex) {
        $("#sewerageDetailMasterTable tbody")
            .append(
                '<tr> <td> <input id="id' + (currentIndex) + '" type="hidden"> ' +
                '<input type="text" class="form-control patternvalidation donationRatesNoOfClosets" maxlength="8" ' +
                'style="text-align: center; font-size: 12px;" ' +
                'id="sewerageDetailMasterNoOfClosets' + (currentIndex - 1) + '" ' +
                'name="sewerageDetailMaster[' + (currentIndex - 1) + '].noOfClosets" ' +
                'data-pattern="number" required="required"/></td>' +
                '<td><input class="form-control patternvalidation donationRatesAmount" data-pattern="decimalvalue" ' +
                'maxlength="8" style="text-align: right; font-size: 12px;" ' +
                'id="sewerageDetailMasterAmount' + (currentIndex - 1) + '" ' +
                'name="sewerageDetailMaster[' + (currentIndex - 1) + '].amount" type="text" ' +
                'required="required"/></td>' +
                '<td><span class="add-padding"><i class="fa fa-trash delete-row" aria-hidden="true"></i></span></td></tr>');
        patternvalidation();
        $('#sewerageDetailMasterTable tbody tr:last').addClass('dynamicInput');
        var currentRowObj = $('#sewerageDetailMasterTable tbody tr:last');
        currentRowObj.find('input.markedForRemoval').val('false');
    }

    function checkUniqueNumberOfClosets() {
        var donationCollection = [];
        var isValidation = true;
        $('#sewerageDetailMasterTable > tbody  > tr').each(function (i, obj) {
            var noOfClosets = $(this).find("input[id='sewerageDetailMaster["+i+"].noOfClosets']").val();
            var markedForRemoval = $(this).find("input[id='sewerageDetailMaster["+i+"].markedForRemoval']").val();
            if (noOfClosets != "" && markedForRemoval == "false") {
                if (noOfClosets === "0") {
                    var textbox = noOfClosets;
                    bootbox.alert('Number of closets should be more than 0', function () {
                        setTimeout(function () {
                            textbox.focus();
                        }, 400);
                    });
                    isValidation = false;
                    return false;
                }
                else {
                    if (i == 0) {
                        donationCollection.push(noOfClosets);
                    }
                    else {
                        if (donationCollection.indexOf($(this).val()) === -1) {
                            donationCollection.push(noOfClosets);
                        }
                        else {
                            isValidation = false;
                            var textfield = $(this);
                            bootbox.alert('Entered Number Of Closets ' + noOfClosets + ' is a duplicate value. Please enter different value.', function () {

                                setTimeout(function () {
                                    textfield.focus();
                                }, 400);
                            });
                            return false;
                        }
                    }
                }
            }
        });
        return isValidation;
    }

    $('#sewerageDetailMasterTable tbody').on('click', 'tr td .delete-row', function () {
        var idx = $(this).closest('tr').index();
        var obj = $(this);
        if (idx == 0) {
            bootbox.alert('Cannot delete first row!');
        } else if ((idx < ($('#sewerageDetailMasterTable tbody tr:visible').length -1))) {
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
});



