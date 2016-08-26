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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 
<div class="main-content">
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
							${councilMeeting.committeeType.name}</div>
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
					<div class="panel-body">
						<div class="panel-heading">
							<div class="panel-title">
								<spring:message code="lbl.agenda.details" />
							</div>
						</div>
						
						<p>The length of the companies collection is : ${fn:length(councilMeeting.meetingMOMs)}</p>
						
						<table class="table table-bordered">
							<thead>
								<th align="center"><spring:message code="lbl.serial.number" /></th>
								<th><spring:message code="lbl.agenda.item" /></th>
								<th><spring:message code="lbl.agenda.number" /></th>
								<th><spring:message code="lbl.preamble.number" /></th>
								<th><spring:message code="lbl.resolution.comment" /></th>
								<th><spring:message code="lbl.mom.action" /></th>
								<th><spring:message code="lbl.department" /></th>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${!councilMeeting.meetingMOMs.isEmpty()}">
										<c:forEach items="${councilMeeting.meetingMOMs}" var="mom"
											varStatus="counter">
											<tr>
												<div class="row add-margin">
													<td align="center">
													  ${counter.count} 
													  <input type="hidden" name="meetingMOMs[${counter.index}].preamable.id" value="${mom.preamble.id}" />
													</td>
													<td><c:out value="${mom.preamble.gistOfPreamble}" /></td>
													<td><c:out value="${mom.agenda.agendaNumber}" /></td>
													<td><c:out value="${mom.preamble.preambleNumber}" /></td>
													<td><input type="textarea" class="form-control"
														data-unique
														name="meetingMOMs[${counter.index}].resolutionDetail"
														minlength="5" maxlength="512"
														value="${mom.resolutionDetail}" /></td>
													<td>
													<form:select path="meetingMOMs[${counter.index}].resolutionStatus"
																	id="resolutionStatus" cssClass="form-control"
																	cssErrorClass="form-control error">
																	<form:option value="${mom.resolutionStatus}">
																		<spring:message code="lbl.select" />
																	</form:option>
																	<form:options items="${resolutionStatus}"
																		itemValue="id" itemLabel="code" />
																</form:select>
																<form:errors
																	path="meetingMOMs[${counter.index}].resolutionStatus"
																	cssClass="error-msg" />
														</div>
														
													</td>
													<td><c:out value="${mom.preamble.department.name}" /></td>
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
	</div>
</div>
<input type="hidden" name="councilMeeting" value="${councilMeeting.id}" />

<div class="row display-hide agenda-section">
	<div class="col-md-6 table-header text-left">Sumoto Resolution</div>
	<!-- <div class="col-md-6 text-right pull-right"><button type="button" class="btn btn-primary" id="add-agenda">Add Row</button></div> -->

		<div class="col-md-12 form-group report-table-container">
		<table id="sumotoTable" class="table table-bordered">
			<thead>
				
					<%-- <th><spring:message code="lbl.serial.no" /></th> --%>
					<%-- <th><spring:message code="lbl.preamble.number" /></th> --%>
					<th><spring:message code="lbl.department" /></th>
					<%-- <th><spring:message code="lbl.wardnumber" /></th> --%>
					<th><spring:message code="lbl.gist.sumoto" /></th>
					<th><spring:message code="lbl.amount"/></th>
					<th><spring:message code="lbl.resolution.comment" /></th>
					<th><spring:message code="lbl.mom.status" /></th>			
				
			</thead>
			 <tbody data-existing-len="${fn:length(councilMeeting.meetingMOMs)}"> 
			<%-- <tbody>
			<c:choose>
									<c:when test="${!councilMeeting.meetingMOMs.isEmpty()}">
										<c:forEach items="${councilMeeting.meetingMOMs}" var="mom"
											varStatus="counter">
			<td></td>
			<td><form:select path="meetingMOMs[${counter.index}].preamble.department"
																	id="department" cssClass="form-control"
																	cssErrorClass="form-control error">
																	<form:option value="">
																		<spring:message code="lbl.select" />
																	</form:option>
																	<form:options items="${departments}"
																		itemValue="id" itemLabel="name" />
																</form:select>
																
														</td>
			<td><input type="textarea" class="form-control"
														data-unique
														name="meetingMOMs[${counter.index}].preamble.gistOfPreamble"
														minlength="5" maxlength="512"
														value="" /></td>
			<td><input type="textarea" class="form-control"
														data-unique
														name="meetingMOMs[${counter.index}].preamble.sanctionAmount"
														minlength="5" maxlength="512"
														value="" /></td>
			<td><input type="textarea" class="form-control"
														data-unique
														name="meetingMOMs[${counter.index}].resolutionDetail"
														minlength="5" maxlength="512"
														value="" /></td>
			<td><form:select path="meetingMOMs[${counter.index}].resolutionStatus"
																	id="resolutionStatus" cssClass="form-control"
																	cssErrorClass="form-control error">
																	<form:option value="">
																		<spring:message code="lbl.select" />
																	</form:option>
																	<form:options items="${resolutionStatus}"
																		itemValue="id" itemLabel="code" />
																</form:select>
																<form:errors
																	path="meetingMOMs[${counter.index}].resolutionStatus"
																	cssClass="error-msg" />
														</div></td>
														</c:forEach>
									</c:when>
									<c:otherwise>
										<div class="col-md-3 col-xs-6 add-margin">
											<spring:message code="lbl.noAgenda.Detail" />
										</div>
									</c:otherwise>
								</c:choose>
			</tbody> --%>
			</tbody>
		</table>
		
	</div>
</div>
<div class="form-group">
	<div class="text-center">
		<button type='submit' class='btn btn-primary' id="buttonSubmit">
			<spring:message code='lbl.update' />
		</button>
		<button type="button" id="add-sumoto" class='btn btn-primary'><spring:message code='lbl.AddSumoto'/></button>
		<a href='javascript:void(0)' class='btn btn-default'
			onclick='self.close()'><spring:message code='lbl.close' /></a>
	</div>
</div>
<script>
	$('#buttonSubmit').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>

<script type="text/javascript"
	src="<c:url value='/resources/app/js/councilMom.js'/>"></script>
	<%-- <script type="text/javascript"
	src="<c:url value='/resources/app/js/councilAgenda.js'/>"></script> --%>