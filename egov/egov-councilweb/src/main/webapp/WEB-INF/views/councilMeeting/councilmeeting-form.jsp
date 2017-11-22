<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="title.councilmeeting.details" />
		</div>
	</div>
	<input type="hidden" name="committeeType" id="committypeid" value="${councilMeeting.committeeType.id}"/>
	<div class="panel-body">
		<div class="form-group">
			<div class="col-sm-3 control-label text-right">
				<spring:message code="lbl.meeting.type" />
			</div>
			<div class="col-sm-3 add-margin">
				<form:select path="meetingType" id="meetingType"
					cssClass="form-control" required="required"
					cssErrorClass="form-control error">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${meetingType}" itemValue="id"
				itemLabel="name"/>
				</form:select>
				<form:errors path="meetingType" cssClass="error-msg" />
			</div>
			<label class="col-sm-2 control-label text-right"><spring:message
					code="lbl.meeting.date" /> <span class="mandatory"></span> </label>
			<div class="col-sm-3 add-margin">
				<form:input type="text" cssClass="form-control datepicker"
					path="meetingDate" id="meetingDate" data-date-start-date="0d"
					required="required" />
				<form:errors path="meetingDate" cssClass="error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message
					code="lbl.meeting.time" /> <span class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:select path="meetingTime" id="meetingTime"
					cssClass="form-control" required="required"
					cssErrorClass="form-control error">
					<form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${meetingTimingMap}" />
				</form:select>
				<form:errors path="meetingTime" cssClass="error-msg" />
			</div>
			<label class="col-sm-2 control-label text-right"><spring:message
					code="lbl.meeting.place" /><span class="mandatory"></span> </label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="meetingLocation" id="meetingLocation" cols="5"
					rows="2" class="form-control patternvalidation"
					data-pattern="alphanumericwithspace" required="required"
					minlength="5" maxlength="32" />

				<form:errors path="meetingLocation" cssClass="error-msg" />
			</div>
		</div>

		<form:hidden path="id" id="id" value="${councilMeeting.id}" />
		<form:hidden path="meetingNumber" id="meetingNumber"
			value="${councilMeeting.meetingNumber}" />
	</div>
</div>

