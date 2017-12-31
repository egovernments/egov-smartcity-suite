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

var reportdatatable;
var drillDowntableContainer = $("#tblSearchTrade");
$(document).ready(function () {
    // Instantiate the application number Bloodhound suggestion engine
    var applicationnoengine = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: 'autocomplete',
            replace: function (url, query) {
                return url + '?searchParamValue=' + query + '&searchParamType=ApplicationNumber';
            },
            filter: function (data) {
                // Map the remote source JSON array to a JavaScript object array
                return $.map(data, function (applicationNumber) {
                    return {
                        name: applicationNumber
                    }
                });
            }
        }
    });

    // Initialize the Bloodhound suggestion engine
    applicationnoengine.initialize();

    // Instantiate the Typeahead UI
    $('#applicationNumber').typeahead({
        hint: false,
        highlight: true,
        minLength: 3
    }, {
        displayKey: 'name',
        source: applicationnoengine.ttAdapter()
    }).on('typeahead:selected', function () {

    });

    // Instantiate the license number Bloodhound suggestion engine
    var licensenoengine = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: 'autocomplete',
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
            url: 'autocomplete',
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

    // Instantiate the trade title Bloodhound suggestion engine
    var tradetitleengine = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: 'autocomplete',
            replace: function (url, query) {
                return url + '?searchParamValue=' + query + '&searchParamType=TradeTitle';
            },
            filter: function (data) {
                // Map the remote source JSON array to a JavaScript object array
                return $.map(data, function (tradeTitle) {
                    return {
                        name: tradeTitle
                    }
                });
            }
        }
    });

    // Initialize the Bloodhound suggestion engine
    tradetitleengine.initialize();

    // Instantiate the Typeahead UI
    $('#tradeTitle').typeahead({
        hint: false,
        highlight: true,
        minLength: 3
    }, {
        displayKey: 'name',
        source: tradetitleengine.ttAdapter()
    }).on('typeahead:selected', function () {

    });
    // Instantiate the trade owner Bloodhound suggestion engine
    var tradeownerengine = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: 'autocomplete',
            replace: function (url, query) {
                return url + '?searchParamValue=' + query + '&searchParamType=TradeOwnerName';
            },
            filter: function (data) {
                // Map the remote source JSON array to a JavaScript object array
                return $.map(data, function (tradeOwnerName) {
                    return {
                        name: tradeOwnerName
                    }
                });
            }
        }
    });

    // Initialize the Bloodhound suggestion engine
    tradeownerengine.initialize();

    // Instantiate the Typeahead UI
    $('#tradeOwnerName').typeahead({
        hint: false,
        highlight: true,
        minLength: 3
    }, {
        displayKey: 'name',
        source: tradeownerengine.ttAdapter()
    }).on('typeahead:selected', function () {

    });

    // Instantiate the property assessment Bloodhound suggestion engine
    var propassessnoengine = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: 'autocomplete',
            replace: function (url, query) {
                return url + '?searchParamValue=' + query + '&searchParamType=PropertyAssessmentNo';
            },
            filter: function (data) {
                // Map the remote source JSON array to a JavaScript object array
                return $.map(data, function (propertyAssessmentNo) {
                    return {
                        name: propertyAssessmentNo
                    }
                });
            }
        }
    });

    // Initialize the Bloodhound suggestion engine
    propassessnoengine.initialize();

    // Instantiate the Typeahead UI
    $('#propertyAssessmentNo').typeahead({
        hint: false,
        highlight: true,
        minLength: 3
    }, {
        displayKey: 'name',
        source: propassessnoengine.ttAdapter()
    }).on('typeahead:selected', function () {

    });

// Instantiate the mobile number Bloodhound suggestion engine
    var mobilenoengine = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: 'autocomplete',
            replace: function (url, query) {
                return url + '?searchParamValue=' + query + '&searchParamType=MobileNo';
            },
            filter: function (data) {
                // Map the remote source JSON array to a JavaScript object array
                return $.map(data, function (mobileNo) {
                    return {
                        name: mobileNo
                    }
                });
            }
        }
    });

    // Initialize the Bloodhound suggestion engine
    mobilenoengine.initialize();

    // Instantiate the Typeahead UI
    $('#mobileNo').typeahead({
        hint: false,
        highlight: true,
        minLength: 3
    }, {
        displayKey: 'name',
        source: mobilenoengine.ttAdapter()
    }).on('typeahead:selected', function () {

    });

    $('#category').change(function () {
        var val = $(this).val();
        if (val !== '') {
            var results = [];
            $.ajax({
                type: "GET",
                url: '/tl/licensesubcategory/by-category',
                data: {categoryId: val},
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
            });
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
                    if ($.trim($(this).val()))
                        valid += 1;
                }
            });

            if (valid > 0) {
                $('.report-section').show();
                var applicationNumber = $('#applicationNumber').val();
                var licenseNumber = $('#licenseNumber').val();
                var oldLicenseNumber = $('#oldLicenseNumber').val();
                var category = $('#category').val();
                var subCategory = $('#subCategory').val();
                var tradeTitle = $('#tradeTitle').val();
                var tradeOwnerName = $('#tradeOwnerName').val();
                var propertyAssessmentNo = $('#propertyAssessmentNo').val();
                var mobileNo = $('#mobileNo').val();
                var ownerName = $('#ownerName').val();
                var status = $('#status').val();
                var expiryYear = $('#expiryYear').val();
                var inactive = $('#inactive').is(":checked");
                reportdatatable = drillDowntableContainer
                    .dataTable({
                        processing: true,
                        serverSide: true,
                        sort: true,
                        filter: true,
                        scrollY: "500px",
                        scrollX: true,
                        scrollCollapse: true,
                        fixedColumns: {
                            leftColumns: 1
                        },
                        ajax: {
                            type: "POST",
                            data: function (args) {
                                return {
                                    "args": JSON.stringify(args),
                                    applicationNumber: applicationNumber,
                                    licenseNumber: licenseNumber,
                                    oldLicenseNumber: oldLicenseNumber,
                                    categoryId: category,
                                    subCategoryId: subCategory,
                                    tradeTitle: tradeTitle,
                                    tradeOwnerName: tradeOwnerName,
                                    propertyAssessmentNo: propertyAssessmentNo,
                                    mobileNo: mobileNo,
                                    ownerName: ownerName,
                                    statusId: status,
                                    expiryYear: expiryYear,
                                    inactive: inactive
                                }
                            }
                        },
                        "bDestroy": true,
                        "autoWidth": false,
                        "order": [[1, 'asc']],
                        "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
                        aaSorting: [],
                        columns: [{
                            "data": function (row) {
                                return {name: row.applicationNumber, id: row.licenseId};
                            },
                            "render": function (data, type, row) {
                                return '<a href="javascript:void(0);" onclick="goToView(' + row.licenseId + ');" data-hiddenele="licenseId" data-eleval="'
                                    + data.id + '">' + data.name + '</a>';
                            },
                            "sTitle": "Application Number",
                            "name": "applicationNumber"
                        }, {
                            "data": "tlNumber",
                            "name": "licenseNumber",
                            "sTitle": "TL Number"
                        }, {
                            "data": "oldTLNumber",
                            "name": "oldLicenseNumber",
                            "sTitle": "Old TL Number"
                        }, {
                            "data": "category",
                            "name": "categoryName",
                            "sTitle": "Category"
                        }, {
                            "data": "subCategory",
                            "name": "tradeName.name",
                            "sTitle": "Sub-Category"
                        }, {
                            "data": "tradeTitle",
                            "name": "nameOfEstablishment",
                            "sTitle": "Title of Trade"
                        }, {
                            "data": "tradeOwner",
                            "name": "licensee.applicantName",
                            "sTitle": "Trade Owner"
                        }, {
                            "data": "mobileNumber",
                            "name": "licensee.mobilePhoneNumber",
                            "sTitle": "Mobile Number"
                        }, {
                            "data": "propertyAssmntNo",
                            "name": "assessmentNo",
                            "sTitle": "Property Assessment Number"
                        }, {
                            "data": "expiryYear",
                            "name": "dateOfExpiry",
                            "sTitle": "Financial Year"
                        }, {
                            "data": "status",
                            "name": "status.id",
                            "sTitle": "Application Status"
                        }, {
                            "data": "active",
                            "name": "isActive",
                            "sTitle": "License Active"
                        }, {
                            "data": "ownerName",
                            "orderable": false,
                            "sortable": false,
                            "sTitle": "Owner Name"
                        }, {
                            "sTitle": "Actions",
                            "render": function (data, type, row) {
                                var option = "<option value=''>Select from Below</option>";
                                $.each(JSON.parse(row.actions), function (key, value) {
                                    option += "<option>" + value.key + "</option>";
                                });
                                return ('<select class="dropchange" id="recordActions" onchange="goToAction(this,' + row.licenseId + ')" >' + option + '</select>');
                            }
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
    window.open("/tl/viewtradelicense/viewTradeLicense-view.action?id=" + id, '', 'scrollbars=yes,width=1000,height=700,status=yes');
}

function goToAction(obj, id) {
    if (obj.options[obj.selectedIndex].innerHTML == 'View Trade')
        window.open("/tl/viewtradelicense/viewTradeLicense-view.action?id=" + id, 'vt' + id, 'scrollbars=yes,width=1000,height=700,status=yes');
    else if (obj.options[obj.selectedIndex].innerHTML == 'Modify Legacy License')
        window.open("/tl/legacylicense/update/" + id, 'mll' + id, 'scrollbars=yes,width=1000,height=700,status=yes');
    else if (obj.options[obj.selectedIndex].innerHTML == 'Collect Fees')
        window.open("/tl/integration/licenseBillCollect.action?licenseId=" + id, 'cf' + id, 'scrollbars=yes,width=1000,height=700,status=yes');
    else if (obj.options[obj.selectedIndex].innerHTML == 'Print Certificate')
        window.open("/tl/viewtradelicense/viewTradeLicense-generateCertificate.action?model.id=" + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Print Provisional Certificate')
        window.open("/tl/viewtradelicense/generate-provisional-certificate.action?model.id=" + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Renew License')
        window.open("/tl/newtradelicense/newTradeLicense-beforeRenew.action?model.id=" + id, 'rl' + id, 'scrollbars=yes,width=1000,height=700,status=yes');
    else if (obj.options[obj.selectedIndex].innerHTML == 'Generate Demand Notice')
        window.open("/tl/demand-notice/generate/" + id, 'dn' + id, 'scrollbars=yes,width=1000,height=700,status=yes');
    else if (obj.options[obj.selectedIndex].innerHTML == 'Closure')
        window.open("/tl/viewtradelicense/showclosureform.action?id=" + id, 'vt' + id, 'scrollbars=yes,width=1000,height=700,status=yes');
    else if (obj.options[obj.selectedIndex].innerHTML == 'Generate Demand')
        window.open("/tl/demand/generate/" + id, 'gd' + id, 'scrollbars=yes,width=1000,height=700,status=yes');
    else if (obj.options[obj.selectedIndex].innerHTML == 'Print Acknowledgment')
        window.open("/tl/license/acknowledgement/" + id);
    else if (obj.options[obj.selectedIndex].innerHTML =='Closure Endorsement Notice')
        window.open("/tl/license/closure/endorsementnotice/" + id);
    $(obj).val('');
}