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
			</div>
		</div>
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.committee.members" />
				</div>
			</div>
			<div class="panel-body custom">

				<table class="table table-bordered">
					<thead>
						<tr>
							<th class="text-center"><spring:message code="lbl.serial.no" /></th>
							<th class="text-center"><spring:message code="lbl.member.name" /></th>
							<th class="text-center"><spring:message code="lbl.designation" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${!commiteemembelist.isEmpty()}">
								<c:forEach items="${commiteemembelist}" var="mem"
									varStatus="counter">
									<tr>
										<td class="text-center"><c:out value="${counter.index+1}" /></td>
										<td class="text-center"><c:out value="${mem.councilMember.name}" /></td>
										<td class="text-center">${mem.councilMember.designation.name}</td>
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

		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.agenda.details" />
				</div>
			</div>
			<div class="panel-body custom">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th align="center"><spring:message code="lbl.serial.no" /></th>
							<th width="14%"><spring:message code="lbl.department" /></th>
							<th width="7%"><spring:message code="lbl.agenda.number" /></th>
							<th width="9%"><spring:message code="lbl.preamble.number" /></th>
							<th><spring:message code="lbl.gistofpreamble" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${!councilMeeting.meetingMOMs.isEmpty()}">
								<c:forEach items="${councilMeeting.meetingMOMs}" var="mom"
									varStatus="counter">
									<tr>
										<td class="text-center">${mom.itemNumber}</td>
										<td><c:out value="${mom.preamble.department.name}" /></td>
										<td class="text-center"><c:out value="${mom.agenda.agendaNumber}" /></td>
										<td><c:out value="${mom.preamble.preambleNumber}" /></td>
										<td><span class="more"><c:out
													value="${mom.preamble.gistOfPreamble}" /></span></td>
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

<div class="row text-center">
	<div class="add-margin">
		<a href="javascript:void(0)" class="btn btn-default"
			onclick="self.close()">Close</a>
	</div>
</div>

<script
	src="<cdn:url value='/resources/app/js/showMoreorLessContent.js?rnd=${app_release_no}'/>"></script>

<style>
.morecontent span {
	display: none;
}
</style>