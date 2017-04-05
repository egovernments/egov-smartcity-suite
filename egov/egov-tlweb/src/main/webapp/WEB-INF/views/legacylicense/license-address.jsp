<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) 2017  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code='license.location.lbl' />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code='license.propertyNo.lbl' /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="assessmentNo" id="propertyNo"
			value="${tradeLicense.assessmentNo}" class="form-control"
			onblur="getPropertyDetails();" maxlength="15" placeholder=""
			autocomplete="off" />
	</div>

	<label class="col-sm-2 control-label text-right"><spring:message
			code='lbl.locality' /> <span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="boundary" id="boundary" class="form-control"
			required="true">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${boundary}" itemValue="id" itemLabel="name" />
		</form:select>
		<form:errors path="boundary" cssClass="error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right">
		<spring:message code='baseregister.ward' />
		<span class="mandatory"></span>
	</label>
	<div class="col-sm-3 add-margin">
		<form:select path="parentBoundary" id="parentBoundary"
			class="form-control" required="true" data-selected-id="${tradeLicense.parentBoundary.id}">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${parentBoundary}" itemValue="id" itemLabel="name" />
		</form:select>
		<form:errors path="parentBoundary" cssClass="error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right">
		<spring:message code='license.ownerShipType.lbl' /> 
		<span class="mandatory"></span>
	</label>
	<div class="col-sm-3 add-margin">
		<form:select path="ownershipType" id="ownershipType"
			class="form-control " required="true">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${ownershipType}" />
		</form:select>
		<form:errors path="ownershipType" cssClass="error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right">
	 	<spring:message code='license.address' />
	 	<span class="mandatory"></span>
	</label>
	<div class="col-sm-3 add-margin">

		<form:textarea path="address" id="address" maxlength="250"
			onblur="checkLength(this,250)" class="form-control"
			required="required" />
		<form:errors path="address" cssClass="error-msg" />

	</div>
</div>
<script>
    var parentBoundary = '${parentBoundary.id}';
   
</script>
