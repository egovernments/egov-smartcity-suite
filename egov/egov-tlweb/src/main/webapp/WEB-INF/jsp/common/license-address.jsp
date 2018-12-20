<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="panel-heading custom_form_panel_heading">
    <div class="panel-title"><s:text name='license.location.lbl'/></div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.propertyNo.lbl'/></label>
    <div class="col-sm-3 add-margin">
        <s:textfield name="assessmentNo" id="propertyNo" value="%{assessmentNo}" maxlength="15"
                     onblur="getPropertyDetails();" class="form-control"/>
    </div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.locality.lbl'/><span class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:select name="boundary" id="boundary" list="dropdownData.localityList"
                  listKey="id" listValue="name" headerKey="" headerValue="%{getText('default.select')}" required="true"
                  value="%{boundary.id}" class="form-control"/>
        <div id="locality_error" class="error-msg" style="display:none;" align="left"></div>
        <s:fielderror fieldName="model.boundary" cssClass="error-msg"/>
    </div>
    <label class="col-sm-2 control-label text-right"><s:text name='license.division'/><span class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <select name="parentBoundary" id="parentBoundary" class="form-control" required="true">
            <option value=""><s:text name='default.select'/></option>
        </select>
        <s:fielderror fieldName="model.parentBoundary" cssClass="error-msg"/>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='lbl.admin.ward'/></label>
    <div class="col-sm-3 add-margin">
        <select name="adminWard" id="adminWard" class="form-control">
            <option value=""><s:text name='default.select'/></option>
        </select>
        <s:fielderror fieldName="model.adminWard" cssClass="error-msg"/>
    </div>
    <label class="col-sm-2 control-label text-right"><s:text name='license.ownerShipType.lbl'/><span
            class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:select headerKey="-1" headerValue="%{getText('default.select')}" name="ownershipType"
                  id="ownershipType" listKey="key" listValue="value"
                  list="ownerShipTypeMap" cssClass="form-control" value="%{ownershipType.name}" required="true"/>
        <div id="ownership_error" class="error-msg" style="display:none;" align="left"></div>
        <s:fielderror fieldName="model.ownershipType" cssClass="error-msg"/>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.address'/><span
            class="mandatory"></span></label>
    <div class="col-sm-5 add-margin">
        <s:textarea name="address" id="address" maxlength="250" onblur="checkLength(this,250)" class="form-control" rows="3" required="true"/>
        <div id="address_error" class="error-msg" style="display:none;" align="left"></div>
        <s:fielderror fieldName="model.address" cssClass="error-msg"/>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.traderCheckbox.lbl'/></label>
    <div class="col-sm-3 add-margin">
        <s:checkbox theme="simple" key="showAgreementDtl" onclick="showHideAgreement()" id="showAgreementDtl"
                    disabled="%{sDisabled}"/>
    </div>
</div>

<div id="agreementSec" style="display: none;">
    <div class="panel-heading custom_form_panel_heading">
        <div class="panel-title"><s:text name='license.AgreementDetails.lbl'/></div>
    </div>

    <div class="form-group">
        <label class="col-sm-3 control-label text-right"><s:text name='license.agreementDate.lbl'/><span
                class="mandatory"></span></label>
        <div class="col-sm-3 add-margin">
            <s:date name="agreementDate" format="dd/MM/yyyy" var="agreementDateFrmttd"/>
            <s:textfield name="agreementDate" cssClass="form-control datepicker" data-date-end-date="0d"
                         id="agreementDate" maxlength="10" value="%{agreementDateFrmttd}"/>
            <s:fielderror fieldName="model.agreementDate" cssClass="error-msg"/>
        </div>

        <label class="col-sm-2 control-label text-right"><s:text name='license.agreementDocNo.lbl'/><span
                class="mandatory"></span></label>
        <div class="col-sm-3 add-margin">
            <s:textfield name="agreementDocNo" maxlength="50" id="agreementDocNo" value="%{agreementDocNo}"
                         cssClass="form-control patternvalidation" data-pattern="alphanumerichyphenbackslash"/>
            <s:fielderror fieldName="model.agreementDocNo" cssClass="error-msg"/>
        </div>
    </div>
</div>
<script>
    var parentBoundary = '${parentBoundary.id}';
    var adminWard = '${adminWard.id}';
    $(document).ready(function () {
        if ($('#boundary').val() != '') {
            $('#boundary').trigger('blur');
        }

        if ($("#propertyNo") && $("#propertyNo").val() !== "") {
            getPropertyDetails();
        }
    });
</script>