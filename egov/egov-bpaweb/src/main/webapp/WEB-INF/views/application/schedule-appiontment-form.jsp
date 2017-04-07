<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2017>  eGovernments Foundation
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

<%@page import="org.python.modules.jarray"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.schedule.doc.scrutiny" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.purpose" /> <span class="mandatory"></span> </label>
	<div class="col-sm-3 add-margin">
		<form:select path="purpose" id="purpose" required="required"
			cssClass="form-control" cssErrorClass="form-control error">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${appointmentPurpose}" />
		</form:select>
		<form:errors path="purpose" cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.appmnt.date" /> <span class="mandatory"></span> </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control datepicker"
			ta-inputmask="'mask': 'd/m/y'" data-date-start-date="0d"
			maxlength="50" id="appointmentDate" path="appointmentDate"
			required="required" />
		<form:errors path="appointmentDate" cssClass="add-margin error-msg" />
	</div>

</div>
<div class="form-group">

	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.appmnt.time" /> <span class="mandatory"></span> </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" maxlength="50" id="appointmentTime"
			path="appointmentTime" required="required" />
		<form:errors path="appointmentTime" cssClass="add-margin error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.appmnt.location" /> <span class="mandatory"></span> </label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation"
			data-pattern="string" maxlength="50" id="appointmentLocation"
			path="appointmentLocation" required="required" />
		<form:errors path="appointmentLocation"
			cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">

	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.remarks" /> <span class="mandatory"></span> </label>
	<div class="col-sm-3 add-margin">
		<form:textarea path="remarks" id="remarks"
			class="form-control patternvalidation"
			data-pattern="alphanumericwithspace" required="required"
			maxlength="256" cols="5" rows="4" />
		<form:errors path="remarks" cssClass="add-margin error-msg" />
	</div>
	<c:if test="${ mode eq 'postponeappointment' }">
		<div class="form-group">
			<label class="col-sm-2 control-label text-right"><spring:message
					code="lbl.postpone.reason" /> <span class="mandatory"></span> </label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="postponementReason" id="postponementReason"
					class="form-control patternvalidation"
					data-pattern="alphanumericwithspace" required="required"
					maxlength="256" cols="5" rows="4" />
				<form:errors path="postponementReason"
					cssClass="add-margin error-msg" />
			</div>
		</div>
	</c:if>
</div>
