/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

jQuery.noConflict();
jQuery(document).ready(function () {

    jQuery('.add-attachment').click(function () {
        jQuery(this).parent().before('<div class="col-sm-3 add-margin"> <input type="file" class="form-control" required> </div>');
    });

    jQuery('.motorcheck').click(function () {
        jQuery('.motorpart').toggle();
    });
});

function showHideAgreement(){
    if(document.getElementById("showAgreementDtl").checked){
        document.getElementById("agreementSec").style.display="";
    } else {
        document.getElementById("agreementSec").style.display="none";
        document.getElementById("agreementDate").value="";
        document.getElementById("agreementDocNo").value="";
    }
}



// Calls propertytax REST api to retrieve property details for an assessment no
function callPropertyTaxRest() {
    var propertyNo = jQuery("#propertyNo").val();
    if (propertyNo != "" && propertyNo != null) {
        jQuery.ajax({
            url: "/ptis/rest/property/" + propertyNo,
            type: "GET",
            contentType: "application/x-www-form-urlencoded",
            success: function (data) {
                if (data.errorDetails.errorCode != null && data.errorDetails.errorCode != '') {
                    bootbox.alert(data.errorDetails.errorMessage);
                    jQuery('#propertyNo').val('');
                    jQuery("#address").attr("readonly", false);
                    jQuery("#boundary").attr("readonly", false);
                } else {
                    if (data.boundaryDetails != null) {
                        jQuery("#boundary").val(data.boundaryDetails.localityId);
                        jQuery("#wardName").val(data.boundaryDetails.wardName);
                        jQuery('#parentBoundary').val(data.boundaryDetails.wardId);
                        jQuery("#address").val(data.propertyAddress);
                    }
                }
            },
            error: function (e) {
                jQuery("#propertyNo").val("");
                resetOnPropertyNumChange();
                bootbox.alert("Error getting property details");
            }
        });
    }
}

function resetOnPropertyNumChange() {
    var propertyNo = jQuery("#propertyNo").val();
    if (propertyNo != "" && propertyNo != null) {
        jQuery("#address").attr("readonly", true);
        jQuery("#boundary").attr("readonly", true);
    } else {
        jQuery("#address").attr("readonly", false);
        jQuery("#boundary").attr("readonly", false);
    }
    jQuery("#boundary").val(-1);
    jQuery("#wardName").val("");
    jQuery("#address").val("");
}

function checkLength(obj, val) {
    if (obj.value.length > val) {
        bootbox.alert('Max ' + val + ' digits allowed')
        obj.value = obj.value.substring(0, val);
    }
}

function formatCurrency(obj) {
    if (obj.value == "") {
        return;
    } else {
        obj.value = (parseFloat(obj.value)).toFixed(2);
    }
}