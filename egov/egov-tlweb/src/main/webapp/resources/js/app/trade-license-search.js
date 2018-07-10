/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
var searchwins = [];
$(document).ready(function () {
    $('#toggle-searchmore').click(function () {
        if ($(this).html() == "More..") {
            $(this).html('Less..');
            $('.show-search-more').show();
        } else {
            $(this).html('More..');
            $('.show-search-more').hide();
        }

    });

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

    $.fn.dataTable.ext.errMode = function () {
        $('.loader-class').modal('hide');
    };

    $("#btnsearch").click(
        function () {
            $.each(searchwins, function (i, val) {
                var window = val;
                window.close();
            });
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
                $('.loader-class').modal('show', {backdrop: 'static'});
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
                            leftColumns: 2
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
                                    inactive: inactive,
                                    applicationTypeId: $('#appType').val(),
                                    natureOfBusinessId: $('#natureOfBusiness').val()
                                }
                            }
                        },
                        "initComplete": function (settings, json) {
                            $('.loader-class').modal('hide');
                        },
                        "bDestroy": true,
                        "autoWidth": true,
                        "order": [[2, 'asc']],
                        "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
                        aaSorting: [],
                        columns: [
                            {
                                "data": null,
                                "render": function (data, type, row) {
                                    var option = "<option value=''>---- Select an Action----</option>";
                                    $.each(JSON.parse(row.actions), function (key, value) {
                                        option += "<option>" + value.key + "</option>";
                                    });
                                    return ('<select class="form-control" style="width:200px;font-size: small" id="recordActions" onchange="goToAction(this,\'' + row.uid + '\',' + row.licenseId + ')" >' + option + '</select>');
                                },
                                "sortable": false,
                                "orderable": false
                            }, {
                                "data": function (row) {
                                    return {name: row.applicationNumber, id: row.uid};
                                },
                                "render": function (data, type, row) {
                                    return '<a href="javascript:void(0);" onclick="goToView(\''+ row.uid +'\');" data-hiddenele="uid" data-eleval="'
                                        + data.id + '">' + data.name + '</a>';
                                },
                                "name": "applicationNumber"
                            }, {
                                "data": "tlNumber",
                                "name": "licenseNumber",
                            }, {
                                "data": "oldLicenseNumber",
                                "name": "oldLicenseNumber",
                            }, {
                                "data": "status",
                                "name": "status.id",
                            }, {
                                "data": "active",
                                "name": "isActive",
                            }, {
                                "data": "expiryDate",
                                "name": "dateOfExpiry",
                            }, {
                                "data": "ownerName"
                            }, {
                                "data": "tradeTitle",
                                "name": "nameOfEstablishment",
                            }, {
                                "data": "tradeOwner",
                                "name": "licensee.applicantName",
                            }, {
                                "data": "category",
                                "name": "categoryName",
                            }, {
                                "data": "subCategory",
                                "name": "tradeName.name",
                            }, {
                                "data": "mobileNumber",
                                "name": "licensee.mobilePhoneNumber",
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
    openWindow("/tl/license/show/" + id, 'vt' + id);
}

function goToAction(obj, uid, id) {
    if (obj.options[obj.selectedIndex].innerHTML == 'View Trade')
        openWindow("/tl/license/show/" + uid, 'vt' + uid);
    else if (obj.options[obj.selectedIndex].innerHTML == 'View DCB')
        openWindow("/tl/dcb/view/" + uid, 'vdcb' + uid);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Modify Legacy License')
        openWindow("/tl/legacylicense/update/" + id, 'mll' + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Collect Fees')
        openWindow("/tl/integration/licenseBillCollect.action?licenseId=" + id, 'cf' + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Print Certificate')
        openWindow("/tl/viewtradelicense/viewTradeLicense-generateCertificate.action?model.id=" + id, 'pc' + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Print Provisional Certificate')
        openWindow("/tl/license/generate-provisionalcertificate/" + uid, 'ppc' + uid);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Renew License')
        openWindow("/tl/newtradelicense/newTradeLicense-beforeRenew.action?model.id=" + id, 'rl' + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Generate Demand Notice')
        openWindow("/tl/demand-notice/generate/" + id, 'gdn' + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Closure')
        openWindow("/tl/license/closure/" + id, 'cl' + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Generate Demand')
        openWindow("/tl/demand/generate/" + id, 'gd' + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Print Acknowledgment')
        openWindow("/tl/license/acknowledgement/" + uid);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Closure Endorsement Notice')
        openWindow("/tl/license/closure/endorsementnotice/" + id, 'cen' + id);
    $(obj).val('');
}

function openWindow(url, name) {
    var windRef = window.open(url, name, 'scrollbars=yes,width=1000,height=700,status=yes');
    searchwins.push(windRef);
    windRef.focus()
}