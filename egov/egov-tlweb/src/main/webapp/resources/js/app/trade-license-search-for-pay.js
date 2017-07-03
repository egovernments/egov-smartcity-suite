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
var pay = 'Pay';
var drillDowntableContainer = $("#tblSearchTrade");
$(document).ready(function () {
    // Instantiate the application number Bloodhound suggestion engine
    var applicationnoengine = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/tl/search/autocomplete',
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
                reportdatatable = drillDowntableContainer
                    .dataTable({
                        scrollY: "300px",
                        scrollX: true,
                        scrollCollapse: true,
                        fixedColumns: {
                            leftColumns: 1
                        },
                        ajax: {
                            type: "POST",
                            data: {
                                applicationNumber: applicationNumber,
                                licenseNumber: licenseNumber,
                            }
                        },
                        "bDestroy": true,
                        "autoWidth": false,
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
                            "sTitle": "Application Number"
                        }, {
                            "data": "tlNumber",
                            "sTitle": "TL Number"
                        }, {
                            "data": "tradeOwner",
                            "sTitle": "Trade Owner"
                        }, {
                            "data": "mobileNumber",
                            "sTitle": "Mobile Number"
                        }, {
                            "data": "propertyAssmntNo",
                            "sTitle": "PT Assessment No."
                        }, {
                            "data": "arrDmd",
                            "sTitle": "Arrears Demand"
                        }, {
                            "data": "currDmd",
                            "sTitle": "Current Demand"
                        }, {
                            "data": "totColl",
                            "sTitle": "Total Collection"
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
    if (obj.options[obj.selectedIndex].innerHTML == 'Payment')
        window.open("/tl/pay/online/" + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'View DCB')
        window.open("/tl/dcb/view/" + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Renew License')
        window.open("/tl/newtradelicense/newTradeLicense-beforeRenew.action?model.id=" + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Closure')
        window.open("/tl/viewtradelicense/showclosureform.action?id=" + id);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Print Certificate')
        window.open("/tl/viewtradelicense/viewTradeLicense-generateCertificate.action?model.id=" + id, 'gc' + id, 'scrollbars=yes,width=1000,height=700,status=yes');
}