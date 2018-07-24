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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<form:form method="post" action="/eventnotification/schedule/create/"
	modelAttribute="notificationSchedule" id="createScheduleform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.schedule.notification" />
					</div>
				</div>
				<form:hidden id="mode" path="" value="${mode}" />
				<form:hidden id="category" name="category" path="category"
					value="${notificationSchedule.category.id}" />
				<form:hidden id="draftId" name="details.draftId" path=""
					value="${notificationSchedule.details.draftId}" />
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.schedule.templatename" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="templateName" id="templateName"
								name="templateName"
								class="form-control text-left patternvalidation" maxlength="100"
								value="${templateName}" readonly="true" />
							<form:errors path="templateName" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.schedule.type" />:</label>
						<div class="col-sm-3 add-margin">
							<form:select path="draftType" id="draftType" name="draftType"
								cssClass="form-control" cssErrorClass="form-control error"
								required="required" readonly="true">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${draftList}" itemLabel="name"
									itemValue="id" />
							</form:select>
							<form:errors path="draftType" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.event.startdate" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin text-center">
							<form:input path="details.startDt" id="startDt"
								name="details.startDt" class="form-control datepicker"
								title="Please enter a valid date"
								pattern="\d{1,2}/\d{1,2}/\d{4}" data-inputmask="'mask': 'd/m/y'"
								required="required" data-date-start-date="0d" />
							<form:errors path="details.startDt" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.event.starttime" />:<span class="mandatory"></span></label>
						<div class="col-sm-2 add-margin text-center">
							<form:select path="details.startHH" id="startHH"
								name="details.startHH" cssClass="form-control"
								cssErrorClass="form-control error" required="required">
								<form:option value="">
									<spring:message code="lbl.hours" />
								</form:option>
								<form:options items="${hourList}" />
							</form:select>
							<form:errors path="details.startHH" cssClass="error-msg" />
						</div>
						<label class="col-sm-1 control-label text-right">:</label>
						<div class="col-sm-2 add-margin text-center">
							<form:select path="details.startMM" id="startMM"
								name="details.startMM" cssClass="form-control"
								cssErrorClass="form-control error" required="required">
								<form:option value="">
									<spring:message code="lbl.minutes" />
								</form:option>
								<form:options items="${minuteList}" />
							</form:select>
							<form:errors path="details.startMM" cssClass="error-msg" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.schedule.status" />:</label>
						<div class="col-sm-3 add-margin">
							<form:input path="status" id="status" name="status"
								class="form-control text-left patternvalidation" maxlength="100"
								readonly="true" value="${status}" />
							<form:errors path="status" cssClass="error-msg" />
						</div>
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.schedule.repeatevery" />:<span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin text-center">
							<form:select path="scheduleRepeat" id="scheduleRepeat"
								name="scheduleRepeat" cssClass="form-control"
								cssErrorClass="form-control error" required="required">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${repeatList}" itemLabel="name"
									itemValue="id" />
							</form:select>
							<form:errors path="scheduleRepeat" cssClass="error-msg" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label text-right"><spring:message
								code="lbl.schedule.notificationpreview" />:<span
							class="mandatory"></span></label>
						<div class="col-sm-10 add-margin">
							<form:textarea path="messageTemplate" id="messageTemplate"
								name="messageTemplate"
								class="form-control text-left patternvalidation" maxlength="500"
								required="required" value="${messageTemplate}" readonly="true" />
							<form:errors path="messageTemplate" cssClass="error-msg" />
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="text-center">
				<button type='submit' class='btn btn-primary' id="buttonSubmit">
					<spring:message code='lbl.schedule.button' />
				</button>
				<a href='javascript:void(0)' class='btn btn-default'
					onclick="self.close()"><spring:message code='lbl.close' /></a>
			</div>
		</div>
</form:form>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />

<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url  value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/schedule-create.js?rnd=${app_release_no}'/>"></script>
