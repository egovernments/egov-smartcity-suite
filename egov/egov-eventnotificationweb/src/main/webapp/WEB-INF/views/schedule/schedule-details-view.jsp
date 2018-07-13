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
<form:form method="post" action="/eventnotification/schedule/delete/${notificationSchedule.id}" modelAttribute="notificationSchedule"
	id="deleteScheduleform" cssClass="form-horizontal form-groups-bordered">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.schedule.notification.details" />
					</div>
				</div>
				<input type="hidden" id="mode" value="${mode}" /> <input
					type="hidden" id="id" name="id" value="${notificationSchedule.id}" />
				<input type="hidden" id="scheduleDelConfirm"
					value='<spring:message code="msg.schedule.delete.confirm" />' /> <input
					type="hidden" id="scheduleDelSuccess"
					value='<spring:message code="msg.schedule.delete.success" />' /> <input
					type="hidden" id="scheduleDelError"
					value='<spring:message code="msg.schedule.delete.error" />' />
				<div class="panel-body">
					<div class="form-group">
						<label class="col-xs-3 control-label text-right"><spring:message
								code="lbl.schedule.templatename" />:</label>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${notificationSchedule.templateName}" />
						</div>
						<label class="col-xs-3 control-label text-right"><spring:message
								code="lbl.schedule.type" />:</label>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${notificationSchedule.draftType.name}" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-3 control-label text-right"><spring:message
								code="lbl.event.startdate" />:</label>
						<div class="col-xs-3 add-margin view-content">
							<fmt:formatDate pattern="dd/MM/yyyy"
								value="${notificationSchedule.details.startDt}"
								var="startDate" />
							<c:out value="${startDate}" />
						</div>
						<label class="col-xs-3 control-label text-right"><spring:message
								code="lbl.event.starttime" />:</label>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${notificationSchedule.details.startHH}" />
							:
							<c:out value="${notificationSchedule.details.startMM}" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-3 control-label text-right"><spring:message
								code="lbl.schedule.status" />:</label>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${notificationSchedule.status}" />
						</div>
						<label class="col-xs-3 control-label text-right"><spring:message
								code="lbl.schedule.repeatevery" />:</label>
						<div class="col-xs-3 add-margin view-content">
							<c:out value="${notificationSchedule.scheduleRepeat.name}" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-xs-3 control-label text-right"><spring:message
								code="lbl.schedule.notificationpreview" />:</label>
						<div class="col-xs-9 add-margin view-content">
							<c:out value="${notificationSchedule.messageTemplate}" />
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="text-center">
				<c:choose>
					<c:when test="${scheduleEditable == true}">
						<button type='button' class='btn btn-primary' id="buttonEdit">
							<spring:message code='lbl.edit' />
						</button>
					</c:when>
					<c:otherwise>
						<button type='button' class='btn btn-primary' id="buttonEdit"
							disabled="disabled">
							<spring:message code='lbl.edit' />
						</button>
					</c:otherwise>
				</c:choose>

				<c:choose>
					<c:when test="${notificationSchedule.status == 'Disabled'}">
						<button type='button' class='btn btn-primary' id="buttonDelete"
							disabled="disabled">
							<spring:message code='lbl.schedule.delete.button' />
						</button>
					</c:when>
					<c:otherwise>
						<button type='submit' class='btn btn-primary' id="buttonDelete">
							<spring:message code='lbl.schedule.delete.button' />
						</button>
					</c:otherwise>
				</c:choose>

				<a href='javascript:void(0)' class='btn btn-default' id='buttonClose'><spring:message code='lbl.close' /></a>
			</div>
		</div>
</form:form>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />

<script type="text/javascript"
	src="<cdn:url  value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/schedule-details-view.js?rnd=${app_release_no}'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/window-reload-and-close.js' />"></script>
