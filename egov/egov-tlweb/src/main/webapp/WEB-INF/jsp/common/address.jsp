<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>
jQuery(document).ready(function(){
	 jQuery('#boundary').change(function() {
		getZoneWard();
	});
	<s:if test="%{hasErrors() || mode=='view' || mode=='edit'}">
	 if(jQuery('#boundary').val()!='-1'){
		getZoneWard();
	 }
	</s:if> 
});

function getZoneWard(){
    jQuery('#wardName').val("");
    jQuery('#parentBoundary').val("");
	jQuery.ajax({
		url: "/egi/boundary/ajaxBoundary-blockByLocality.action",
		type: "GET",
		data: {
			locality : jQuery('#boundary').val()
		},
		cache: false,
		dataType: "json",
		success: function (response) {
			jQuery.each(response.results.boundaries, function (j, boundary) {
				if (boundary.wardId) {
					jQuery('#wardName').val(boundary.wardName);
					jQuery('#parentBoundary').val(boundary.wardId);
				}
			});
		}, 
		error: function (response) {
			bootbox.alert("No boundary details mapped for locality")
		}
	});
}
</script>

<div class="panel-heading custom_form_panel_heading">
    <div class="panel-title"><s:text name='license.location.lbl' /></div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.propertyNo.lbl' /></label>
    <div class="col-sm-3 add-margin">
        <s:textfield name="assessmentNo" id="propertyNo" value="%{assessmentNo}" maxlength="15" onkeypress="return numbersonly(this, event)" onblur="checkLength(this,15);callPropertyTaxRest();" onchange="resetOnPropertyNumChange();" class="form-control"/>
    </div>
  
    <label class="col-sm-2 control-label text-right"><s:text name='license.locality.lbl' /><span class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:select name="boundary" id="boundary" list="dropdownData.localityList"
	listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{boundary.id}" class="form-control" />
    </div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.division' /><span class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:textfield name="ward" id="wardName" value="%{parentBoundary.name}"  readonly="true" class="form-control"/>
        <s:hidden name="parentBoundary" id="parentBoundary" value="%{parentBoundary.id}"/>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.ownerShipType.lbl' /><span class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:select headerKey="-1" headerValue="%{getText('default.select')}" name="ownershipType"
										id="ownershipType" listKey="key" listValue="value"
										list="ownerShipTypeMap" cssClass="form-control" value="%{ownershipType}"  />
    </div>
    <label class="col-sm-2 control-label text-right"><s:text name='license.address' /><span class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
         <s:textarea name="address" id="address" maxlength="250" onblur="checkLength(this,250)" class="form-control"  />
    </div>
</div>
<script>
    if(jQuery("#propertyNo") && jQuery("#propertyNo").val() !== "") {
        resetOnPropertyNumChange();
        callPropertyTaxRest();
    }
</script>