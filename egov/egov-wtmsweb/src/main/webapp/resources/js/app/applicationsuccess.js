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

$(document).ready(function () {
    loadPropertyDetails();

    function loadPropertyDetails() {
        propertyID = $('#propertyIdentifier').html()
        if (propertyID != '') {
            $.ajax({
                url: "/ptis/rest/property/" + propertyID,
                type: "GET",
                dataType: "json",
                success: function (response) {
                    var applicantName = '';
                    for (i = 0; i < response.ownerNames.length; i++) {
                        if (applicantName == '')
                            applicantName = response.ownerNames[i].ownerName;
                        else
                            applicantName = applicantName + ', ' + response.ownerNames[i].ownerName;
                    }
                    $("#applicantname").html(applicantName);
                    $("#nooffloors").html(response.propertyDetails.noOfFloors);
                    if (response.ownerNames[0].mobileNumber) {
                        $("#mobileNumber").html(response.ownerNames[0].mobileNumber);
                        var mobileNumber = response.ownerNames[0].mobileNumber;
                        var mobNumberwithmask = mobileNumber.slice(-4),
                            countNum = '';

                        for (var i = (mobileNumber.length) - 4; i > 0; i--) {
                            countNum += '*';
                        }
                        $("#mobileNumber").html(countNum + mobNumberwithmask);
                    }
                    $("#email").html(response.ownerNames[0].emailId);
                    $("#propertyaddress").html(response.propertyAddress);
                    var boundaryData = '';
                    if (response.boundaryDetails.zoneName != null && response.boundaryDetails.zoneName != '')
                        boundaryData = response.boundaryDetails.zoneName;
                    if (response.boundaryDetails.wardName != null && response.boundaryDetails.wardName != '') {
                        if (boundaryData == '')
                            boundaryData = response.boundaryDetails.wardName;
                        else
                            boundaryData = boundaryData + " / " + response.boundaryDetails.wardName;
                    }
                    if (response.boundaryDetails.blockName != null && response.boundaryDetails.blockName != '') {
                        if (boundaryData == '')
                            boundaryData = response.boundaryDetails.blockName;
                        else
                            boundaryData = boundaryData + " / " + response.boundaryDetails.blockName;
                    }
                    if (response.ownerNames[0].aadhaarNumber != '' && response.ownerNames[0].aadhaarNumber != null) {
                        $("#aadhaar").html(response.ownerNames[0].aadhaarNumber);
                        var aadhaarNum = response.ownerNames[0].aadhaarNumber;
                        var aadhaarwithmask = aadhaarNum.slice(-4),
                            countNum = '';

                        for (var i = (aadhaarNum.length) - 4; i > 0; i--) {
                            countNum += '*';
                        }
                        $("#aadhaar").html(countNum + aadhaarwithmask);
                    }
                    $("#locality").html(response.boundaryDetails.localityName);
                    $("#zonewardblock").html(boundaryData);
                    $("#propertytaxdue").html(response.propertyDetails.taxDue);

                },
                error: function (response) {
                    console.log("failed");
                }
            });
        }
    }


    var mode = $("#mode").val();
    if (mode == 'inbox') {
        $("#propertytaxdue").addClass("error-msg");
    }

    $('#addConnection').click(function () {
        openWindow('/wtms/application/addconnection/' + $('#consumerCode').val());
    });

    $('#changeConnection').click(function () {
        openWindow('/wtms/application/changeOfUse/' + $('#consumerCode').val());
    });

    $('#closureConnection').click(function () {
        openWindow('/wtms/application/close/' + $('#consumerCode').val());
    });

    $('#dcbscreen-view').click(function () {
        openWindow('/wtms/viewDcb/consumerCodeWis/' + $('#consumerCode').val());
    });

    $('#meter-entry').click(function () {
        openWindow('/wtms/application/meterentry/' + $('#consumerCode').val());
    });

    $('#viewEstimationNotice').click(function () {
        var url = '/wtms/application/estimationNotice/view/' + $('#applicationCode').val();
        $('#waterConnectionSuccess').attr('method', 'get');
        $('#waterConnectionSuccess').attr('action', url);
        $('#waterConnectionSuccess').attr('mode', 'search');
        window.open(url, 'window', 'scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
    });

    $('#viewWorkOrder').click(function () {
        openWindow('/wtms/application/workorder/view/' + $('#applicationCode').val());
    });

    $('#re-connection').click(function () {
        openWindow('/wtms/application/reconnection/' + $('#consumerCode').val());
    });

    $('#viewRecOonnAck').click(function () {
        openWindow('/wtms/application/ReconnacknowlgementNotice/view/' + $('#applicationCode').val());
    });

    $('#viewClosureAck').click(function () {
        openWindow('/wtms/application/acknowlgementNotice/view/' + $('#applicationCode').val());
    });


    function openWindow(url) {
        $('#waterConnectionSuccess').attr('method', 'get');
        $('#waterConnectionSuccess').attr('action', url);
        $('#waterConnectionSuccess').attr('mode', 'search');
        window.location = url;
    }

});

$('#payBtn').click(function () {
    var url = '/wtms/application/generatebill/' + $('#applicationCode').val() + "?applicationTypeCode=" + $('#applicationTypeCode').val();
    $('#waterConnectionSuccess').attr('method', 'post');
    $('#waterConnectionSuccess').attr('action', url);
})

$('#payEstimation').click(function () {
    var url = '/wtms/estimationcharges/collection/' + $('#applicationCode').val();
    $('#waterConnectionSuccess').attr('method', 'get');
    $('#waterConnectionSuccess').attr('action', url);
})
