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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="title.councilmeeting" />
				</div>
			</div>
			<div class="panel-body custom">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.meeting.type" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${councilMeeting.meetingType.name}</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.meeting.number" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${councilMeeting.meetingNumber}</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.meeting.date" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						<fmt:formatDate pattern="dd/MM/yyyy"
							value="${councilMeeting.meetingDate}" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.meeting.place" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${councilMeeting.meetingLocation}</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.meeting.time" />
					</div>
					<div class="col-sm-3 add-margin view-content">
						${councilMeeting.meetingTime}</div>
				</div>
			</div>

			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.agenda.details" />
				</div>
			</div>
			<div class="panel-body custom">
				<p>The length of the companies collection is :
					${fn:length(councilMeeting.meetingMOMs)}</p>

				<table class="table table-bordered" id="agendaTable">
					<thead>
						<tr>
							<th align="center"><spring:message code="lbl.serial.no" /></th>
							<th><spring:message code="lbl.department" /></th>
							<th width="30%"><spring:message code="lbl.gistofpreamble" /></th>
							<th width="7%"><spring:message code="lbl.agenda.number" /></th>
							<th><spring:message code="lbl.preamble.number" /></th>
							<th width="12%"><spring:message code="lbl.resolution" /><span
								class="mandatory"></span></th>
							<c:if test="${!autoResolutionNoGenEnabled}">
								<th><spring:message code="lbl.resolutionNumber" /><span
								class="mandatory"></span></th>
							</c:if>
							<th width="24%"><spring:message code="lbl.comments" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${!councilMeeting.meetingMOMs.isEmpty()}">
								<c:forEach items="${councilMeeting.meetingMOMs}" var="mom"
									varStatus="counter">
									<tr>
										<td align="center">${mom.itemNumber} <input type="hidden"
											name="meetingMOMs[${counter.index}].preamable.id"
											value="${mom.preamble.id}" /> <input type="hidden"
											name="meetingMOMs[${counter.index}].itemNumber"
											value="${mom.itemNumber}" />
										</td>
										<td><c:out value="${mom.preamble.department.name}" /></td>
										<td><span class="more"><c:out
													value="${mom.preamble.gistOfPreamble}" /></span></td>
										<td><c:out value="${mom.agenda.agendaNumber}" /></td>
										<td><c:out value="${mom.preamble.preambleNumber}" /></td>
										<td><form:select
												path="meetingMOMs[${counter.index}].resolutionStatus"
												cssClass="form-control addorremoverequired"
												cssErrorClass="form-control error" required="required">
												<form:option value="">
													<spring:message code="lbl.select" />
												</form:option>
												<form:options items="${resolutionStatus}" itemValue="id"
													itemLabel="code" />
											</form:select> <form:errors
												path="meetingMOMs[${counter.index}].resolutionStatus"
												cssClass="error-msg" /></td>
										<c:if test="${!autoResolutionNoGenEnabled}">
											<td><form:input
													path='meetingMOMs[${counter.index}].resolutionNumber'
													id="meetingMOMs[${counter.index}].resolutionNumber"
													class="form-control validnum" required="required"
													cssErrorClass="form-control error" /></td>
										</c:if>
										<td>
											<div class="input-group">
												<form:textarea
													path="meetingMOMs[${counter.index}].resolutionDetail"
													id="meetingMOMs[${counter.index}].resolutionDetail"
													class="form-control text-left textarea-content addorremoverequired"
													maxlength="5000" value="${mom.resolutionDetail}" rows="5"
													required="required" />
												<form:errors
													path="meetingMOMs[${counter.index}].resolutionDetail"
													cssClass="error-msg" />
												<span class="input-group-addon" id="showModal"
													data-header="Agenda Items - Resolution comments"><span
													class="glyphicon glyphicon-pencil" style="cursor: pointer"></span></span>
											</div>

										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<div class="col-md-3 col-xs-6 add-margin">
									<spring:message code="lbl.noAgenda.Detail" />
								</div>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<input type="hidden" name="councilMeeting" value="${councilMeeting.id}" />
<input type="hidden" id="autoResolutionNoGenEnabled" value="${autoResolutionNoGenEnabled}" />

<%@ include file="upload-meeting-documents.jsp"%>

<!-- <div class="row display-hide agenda-section">
	<div class="col-md-6 table-header text-left"></div>
	<div class="col-md-6 text-right pull-right"><button type="button" class="btn btn-primary" id="add-agenda">Add Row</button></div>
		<div class="panel-body custom">	
		<div class="col-md-12 form-group report-table-container"> -->
<div class="row display-hide agenda-section">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading custom_form_panel_heading">
				<div class="panel-title">Sumoto Resolution</div>
			</div>
			<div class="panel-body custom">
				<table id="sumotoTable" class="table table-bordered">
					<thead>
						<tr>
							<%-- <th><spring:message code="lbl.serial.no" /></th> --%>
							<%-- <th><spring:message code="lbl.preamble.number" /></th> --%>
							<th width="19%"><spring:message code="lbl.department" /><span
								class="mandatory"></span></th>
							<th width="27%"><spring:message code="lbl.gist.sumoto" /><span
								class="mandatory"></span></th>
							<th width="15%"><spring:message code="lbl.amount" /></th>
							<th width="12%"><spring:message code="lbl.resolution" /></th>
							<c:if test="${!autoResolutionNoGenEnabled}">
								<th><spring:message code="lbl.resolutionNumber" /><span
								class="mandatory"></span></th>
							</c:if>
							<th width="27%"><spring:message code="lbl.comments" /></th>
						</tr>
					</thead>
					<tbody data-existing-len="${fn:length(councilMeeting.meetingMOMs)}">

					</tbody>
				</table>

				<!-- </div> -->
			</div>
		</div>
	</div>
</div>

<!-- Modal -->
<div class="modal fade" id="textarea-modal" role="dialog">
	<div class="modal-dialog modal-lg">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title" id="textarea-header"></h4>
			</div>
			<div class="modal-body">
				<textarea class="form-control textarea-content-of-modal"
					id="textarea-updatedcontent" rows="10"></textarea>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary"
					id="textarea-btnupdate" data-dismiss="modal">Update</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

			</div>
		</div>

	</div>
</div>

<script type="text/javascript"
	src="<cdn:url value='/resources/app/js/councilMom.js?rnd=${app_release_no}'/>"></script>
<script
	src="<cdn:url value='/resources/app/js/showMoreorLessContent.js?rnd=${app_release_no}'/>"></script>
<style>
.morecontent span {
	display: none;
}
</style>