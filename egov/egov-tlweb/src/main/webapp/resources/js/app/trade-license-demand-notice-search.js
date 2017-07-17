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

var reportdatatable;
var drillDowntableContainer = $("#tblSearchTradefornotice");
$(document).ready(function () {


    // Instantiate the license number Bloodhound suggestion engine
    var licensenoengine = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/tl/search/autocomplete',
            replace: function (url, query) {
                return url + '?searchParamValue=' + query + '&searchParamType=LicenseNumber';
            },
            filter: function (data) {
                // Map the remote source JSON array to a JavaScript object array
                return $.map(data, function (licenseNumber) {
                    return {
                        name: licenseNumber
                    }
                });
            }
        }
    });
    // Initialize the Bloodhound suggestion engine
    licensenoengine.initialize();

    // Instantiate the Typeahead UI
    $('#licenseNumber').typeahead({
        hint: false,
        highlight: true,
        minLength: 3
    }, {
        displayKey: 'name',
        source: licensenoengine.ttAdapter()
    }).on('typeahead:selected', function () {

    });
    // Instantiate the old license number Bloodhound suggestion engine
    var oldlicensenoengine = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/tl/search/autocomplete',
            replace: function (url, query) {
                return url + '?searchParamValue=' + query + '&searchParamType=OldLicenseNumber';
            },
            filter: function (data) {
                // Map the remote source JSON array to a JavaScript object array
                return $.map(data, function (oldLicenseNumber) {
                    return {
                        name: oldLicenseNumber
                    }
                });
            }
        }
    });
    // Initialize the Bloodhound suggestion engine
    oldlicensenoengine.initialize();

    // Instantiate the Typeahead UI
    $('#oldLicenseNumber').typeahead({
        hint: false,
        highlight: true,
        minLength: 3
    }, {
        displayKey: 'name',
        source: oldlicensenoengine.ttAdapter()
    }).on('typeahead:selected', function () {

    });
    $('#subCategory').change(function () {
        $('#subCategoryId').val($('#subCategory').val());
    });

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

    $('#btngeneratePDF')
        .click(
            function (e) {
                if ($('form').valid()) {

                    var action = 'generate';
                    $('#demandnoticesearchForm').attr('method', 'get');
                    $('#demandnoticesearchForm').attr('action', action);
                    document.forms["demandnoticesearchForm"].submit();

                }
            });


    $("#btnsearch").click(
        function () {


            var valid = 0;

            $('form').find('input[type=text],input[type="checkbox"], select').each(function () {
                if ($(this).attr('type') == 'checkbox') {
                    if ($(this).is(":checked"))
                        valid += 1;
                } else {
                    if ($(this).val() != "")
                        valid += 1;
                }
            });

            if (valid > 0) {

                var licenseNumber = $('#licenseNumber').val();
                var oldLicenseNumber = $('#oldLicenseNumber').val();
                var category = $('#category').val();
                var subCategory = $('#subCategory').val();
                var wardId = $('#wardId').val();
                var localityId = $('#localityId').val();
                var status = $('#status').val();

                if (!licenseNumber && !oldLicenseNumber && !category && !subCategory && !wardId && !localityId && !status) {
                    bootbox.alert("Atleast one search criteria is mandatory!");
                    return false;
                }

                $('.report-section').show();

                reportdatatable = drillDowntableContainer
                    .dataTable({
                        ajax: {
                            type:"POST",
                            data: {
                                licenseNumber: licenseNumber,
                                oldLicenseNumber: oldLicenseNumber,
                                categoryId: category,
                                subCategoryId: subCategory,
                                wardId: wardId,
                                localityId: localityId,
                                statusId: status,

                            }
                        },
                        "bDestroy": true,
                        "autoWidth": false,
                        "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
                        aaSorting: [],
                        columns: [{
                            "data": "applicationNumber",
                            "sTitle": "Application No."
                        },
                            {
                                "data": "tlNumber",
                                "sTitle": "TL Number"
                            }, {
                                "data": "oldTLNumber",
                                "sTitle": "Old TL Number"
                            }, {
                                "data": "category",
                                "sTitle": "Category"
                            }, {
                                "data": "subCategory",
                                "sTitle": "Sub-Category"
                            }, {
                                "data": "tradeTitle",
                                "sTitle": "Title of Trade"
                            }, {
                                "data": "tradeOwner",
                                "sTitle": "Trade Owner"
                            },
                            {
                                "data": "wardname",
                                "sTitle": "ward No."
                            },
                            {
                                "data": "tlArrearFee",
                                "sTitle": "License Fee (Arrears)"
                            }, {
                                "data": "tlCurrentFee",
                                "sTitle": "License Fee (Current)"
                            }, {
                                "data": "tlArrearPenalty",
                                "sTitle": "Penalty (Arrears)"
                            },
                            {
                                "data": "status",
                                "sTitle": "Status"
                            },
                            {
                                "data": "ownerName",
                                "sTitle": "Owner Name"
                            },
                            {
                                "data": function (row) {
                                    return "Generate Notice";
                                },
                                "render": function (data, type, row) {
                                    return '<a href="javascript:void(0);" onclick="goToView(' + row.licenseId + ');" data-hiddenele="licenseId" data-eleval="'
                                        + data.id + '">' + "Generate Notice" + '</a>';
                                },
                                "sTitle": "Action"
                            }]
                    });
            } else {
                $('.report-section').hide();
                bootbox.alert('Atleast one search criteria is mandatory!');
            }


        }
    );
});
function goToView(id) {
    window.open("generate/" + id, 'dn' + id, 'scrollbars=yes,width=1000,height=700,status=yes');
} 


