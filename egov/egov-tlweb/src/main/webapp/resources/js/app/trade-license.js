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

function showHideAgreement() {
    if (document.getElementById("showAgreementDtl").checked) {
        document.getElementById("agreementSec").style.display = "";
    } else {
        document.getElementById("agreementSec").style.display = "none";
        document.getElementById("agreementDate").value = "";
        document.getElementById("agreementDocNo").value = "";
    }
}

function getUom() {
    jQuery.ajax({
        url: "/tl/feeType/uom-by-subcategory",
        type: "GET",
        data: {
            subCategoryId: jQuery('#subCategory').val(),
            feeTypeId: jQuery('#feeTypeId').val()
        },
        cache: false,
        dataType: "json",
        success: function (response) {
            if (response.length > 0)
                jQuery('#uom').val(response[0].uom.name);
            else {
                jQuery('#uom').val('');
                bootbox.alert("No UOM mapped for the selected Sub Category");
            }
        }
    });
}

function getZoneWard() {
    $('#wardName').val("");
    $('#parentBoundary').val("");
    if ($('#boundary').val() != '-1') {
        $.ajax({
            url: "/egi/boundary/ajaxBoundary-blockByLocality",
            type: "GET",
            data: {
                locality: $('#boundary').val()
            },
            cache: false,
            dataType: "json",
            success: function (response) {
                if (response.results.boundaries.length < 1) {
                    bootbox.alert("Could not find ward for Locality : " + $('#boundary').find(":selected").text());
                    $('#boundary').val('-1');
                    return;
                }
                $.each(response.results.boundaries, function (j, boundary) {
                    if (boundary.wardId) {
                        $('#wardName').val(boundary.wardName);
                        $('#parentBoundary').val(boundary.wardId);
                    } else {
                        bootbox.alert("Could not find ward for Locality : " + $('#boundary').find(":selected").text());
                        $('#boundary').val('-1');
                    }
                });
            },
            error: function (response) {
                bootbootbox.alert("Could not find ward for Locality : " + $('#boundary').find(":selected").text());
                $('#boundary').val('-1');
            }
        });
    }
}

// Calls propertytax REST api to retrieve property details for an assessment no
function getPropertyDetails() {
    var propertyNo = $("#propertyNo").val();
    if (propertyNo != "" && propertyNo != null) {
        $.ajax({
            url: "/ptis/rest/property/" + propertyNo,
            type: "GET",
            contentType: "application/x-www-form-urlencoded",
            success: function (data) {
                if (data.errorDetails.errorCode != null && data.errorDetails.errorCode != '') {
                    bootbox.alert(data.errorDetails.errorMessage);
                    $('#propertyNo').val('');
                    $("#address").attr("readonly", false);
                    $("#boundary").attr("readonly", false);
                } else {
                    if (data.boundaryDetails != null) {
                        $("#boundary").val(data.boundaryDetails.localityId);
                        $("#wardName").val(data.boundaryDetails.wardName);
                        $('#parentBoundary').val(data.boundaryDetails.wardId);
                        $("#address").val(data.propertyAddress);
                    }
                }
            },
            error: function (e) {
                $("#propertyNo").val("");
                resetOnPropertyNumChange();
                bootbox.alert("Error getting property details");
            }
        });
    }
}

function resetOnPropertyNumChange() {
    var propertyNo = $("#propertyNo").val();
    if (propertyNo != "" && propertyNo != null) {
        $("#address").attr("readonly", true);
        $("#boundary").attr("readonly", true);
    } else {
        $("#address").attr("readonly", false);
        $("#boundary").attr("readonly", false);
    }
    $("#boundary").val(-1);
    $("#wardName").val("");
    $("#address").val("");
}


function checkLength(obj, val) {
    if (obj.value.length > val) {
        bootbox.alert('Max ' + val + ' digits allowed')
        obj.value = obj.value.substring(0, val);
    }
}
