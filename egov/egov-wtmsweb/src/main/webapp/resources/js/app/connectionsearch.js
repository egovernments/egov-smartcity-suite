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
var tableContainer;
jQuery(document).ready(function ($) {

    $.fn.dataTable.moment('DD/MM/YYYY');

    $(":input").inputmask();
    tableContainer = $("#aplicationSearchResults");

    $('#searchapprvedapplication').click(function () {
        if ($('#citizenRole').val() == 'true') {
            if ($('#consumerCode').val() ||$('#oldConsumerNumber').val() || $('#propertyid').val() || $('#applicantName').val() || $('#doorno').val()) {
            	if($('#applicantName').val() && $('#applicantName').val().length < 3)
            		bootbox.alert("Please Enter atleast 3 characters of Assessee name");
            	else
                submitButton();
            } else {
                bootbox.alert("Please fill atleast one search criteria.");
                return false;
            }
        }
        else {
            submitButton();
        }
    });

    $('#searchwatertax').keyup(function () {
        tableContainer.fnFilter(this.value);
    });

    $(".btn-danger").click(function (event) {
        $('#searchResultDiv').hide();
    });


    function submitButton() {
        tableContainer = $("#aplicationSearchResults");
        var citizenRole = $('#citizenRole').val();
        tableContainer.dataTable().fnDestroy();
        $("#refine-search-criteria").empty();
        $('#searchResultDiv').show();
        $.ajax({
            url: "/wtms/search/waterSearch/",
            type: "POST",
            dataType: "json",
            data: {
                consumerCode: $('#consumerCode').val(),
                oldConsumerNumber: $('#oldConsumerNumber').val(),
                propertyid: $('#propertyid').val(),
                mobileNumber: $('#mobileNumber').val(),
                applicantName: $('#applicantName').val(),
                locality: $('#locality').val(),
                revenueWard: $('#revenueWard').val(),
                doorNumber: $('#doorno').val()
            },
            success: function (data) {
                var files;
                if (Object.keys(data.data)[0] != "errorCode") {
                    files = data.data;
                    tableContainer.DataTable({
                        destroy: true,
                        sort: false,
                        filter: false,
                        searching: false,
                        responsive: true,
                        data: files,
                        "sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
                        "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
                        "autoWidth": false,
                        "oTableTools": {
                            "sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
                            "aButtons": [
                                {
                                    "sExtends": "pdf",
                                    "mColumns": [0, 1, 2, 4, 5, 6, 9],
                                    "sPdfMessage": "",
                                    "sTitle": "Water Connection Report",
                                    "sPdfOrientation": "landscape"
                                },
                                {
                                    "sExtends": "xls",
                                    "mColumns": [0, 1, 2, 4, 5, 6, 9],
                                    "sPdfMessage": "Water Connection Report",
                                    "sTitle": "Water Connection Report"
                                },
                                {
                                    "sExtends": "print",
                                    "mColumns": [0, 1, 2, 4, 5, 6, 9],
                                    "sPdfMessage": "",
                                    "sTitle": "Water Connection Report"
                                }],

                        },
                        searchable: true,
                        columns: [
                            {
                                title: 'Applicant Name',
                                data: "applicantName",
                            },
                            {
                                title: 'H.S.C Number',
                                "data": function (full) {
                                    return {name: full.consumerCode};
                                },
                                "render": function (data, type, full) {
                                    return '<a href="javascript:void(0);" onclick="goToView(\'' + full.consumerCode + '\');">' + data.name + '</a>';
                                }
                            },
                            {
                                title: 'Assessment Number',
                                data: 'propertyid'
                            },
                            {
                                title: 'Address',
                                data: 'address'
                            },
                            {
                                title: 'apptype',
                                data: 'applicationType',
                                bVisible: false
                            },
                            {
                                title: 'legacy',
                                data: 'islegacy',
                                bVisible: false
                            },
                            {
                                title: 'Usage Type',
                                data: 'usage'
                            },
                            {
                                title: 'Property Tax Due',
                                class: 'text-right',
                                render: function (data, type, full) {
                                    if (citizenRole != 'true') {
                                        return full.propertyTaxDue;
                                    }
                                    else return "";
                                }
                            },
                            {
                                title: 'Status',
                                data: 'status'
                            },
                            {
                                title: 'Water Charge Due',
                                data: 'waterTaxDue'
                            },
                            {
                                title: 'conntype',
                                data: 'connectiontype',
                                bVisible: false
                            },
                            {
                                title: 'applicationCode',
                                data: "applicationcode",
                                bVisible: false
                            },
                            {
                                title: 'Actions',
                                render: function (data, type, full) {
                                    var option = "<option value=''>---- Select an Action----</option>";
                                    $.each(full.actions, function (key, value) {
                                        option += "<option>" + value + "</option>";
                                    });
                                    return ('<select class="form-control" style="width:200px;font-size: small" id="recordActions" ' +
                                        'onchange="goToAction(this,' + full.consumerCode + ',' + "'" + full.applicationType + "'" + ',' + "'" + full.applicationcode + "'" + ')" >' + option + '</select>');
                                }

                            }]
                    });
                }
                else {
                    var errormsg = data.data.errorDetails;
                    $('#aplicationSearchResults').hide();
                    $('#searchResultDiv').hide();
                    $("#refine-search-criteria").append("<strong>" +
                        "<div class='alert alert-danger align-center'>" + errormsg + "</div></strong>");
                }
            }
        })
    }
});

$(document).on("keypress", 'form', function (e) {
    var code = e.keyCode || e.which;
    if (code == 13) {
        e.preventDefault();
        return false;
    }
});

jQuery('.patternvalidationclass').on("input", function () {
    var regexp_alphabetdotcomma = /[^a-zA-Z1-9 .,]/g;
    if (jQuery($(this)).val().match(regexp_alphabetdotcomma)) {
        jQuery($(this)).val(jQuery($(this)).val().replace(regexp_alphabetdotcomma, ''));
    }

});

function goToView(consumerCode) {
    openWindow("/wtms/application/view/" + consumerCode);
}

function goToAction(obj, consumerCode, applicationType, applicationNumber) {
    if (obj.options[obj.selectedIndex].innerHTML == 'View DCB Screen')
        openWindow("/wtms/viewDcb/consumerCodeWis/" + consumerCode);
    else if (obj.options[obj.selectedIndex].innerHTML == 'View water tap connection')
        openWindow("/wtms/application/view/" + consumerCode);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Change of use')
        openWindow("/wtms/application/changeOfUse/" + consumerCode);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Enter Meter Reading')
        openWindow("/wtms/application/meterentry/" + consumerCode);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Reconnection')
        openWindow("/wtms/application/reconnection/" + consumerCode);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Additional connection')
        openWindow("/wtms/application/addconnection/" + consumerCode);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Closure of Connection')
        openWindow("/wtms/application/close/" + consumerCode);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Download Reconnection Proceeding')
        openWindow("/wtms/application/ReconnacknowlgementNotice?pathVar=" + applicationNumber);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Download Closure Proceeding')
        openWindow("/wtms/application/acknowlgementNotice?pathVar=" + applicationNumber);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Download Regularise Connection Proceedings')
        openWindow("/wtms/application/regulariseconnection/proceedings-view/" + applicationNumber);
    else if (obj.options[obj.selectedIndex].innerHTML == 'Collect Charge') {
        var url = '/wtms/application/generatebill/' + consumerCode + "?applicationTypeCode=" + applicationType;
        $('#waterSearchRequestForm').attr('method', 'post');
        $('#waterSearchRequestForm').attr('action', url);
        $('#waterSearchRequestForm').attr('name', 'myform');
        document.forms["myform"].submit();
    }
}

function openWindow(url) {
    $('#waterSearchRequestForm').attr('method', 'get');
    $('#waterSearchRequestForm').attr('action', url);
    window.location = url;
}