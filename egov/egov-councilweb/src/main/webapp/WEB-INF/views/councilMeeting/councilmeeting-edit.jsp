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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<form:form role="form" action="../update"
	modelAttribute="councilMeeting" id="councilMeetingform"
	cssClass="form-horizontal form-groups-bordered"
	enctype="multipart/form-data">
	<%@ include file="councilmeeting-form.jsp"%>

	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.agenda.details" />
					</div>
				</div>
				<div class="panel-body">

					<table class="table table-bordered">
						<thead>
							<tr>
								<th align="center"><spring:message code="lbl.serial.number" /></th>
								<th><spring:message code="lbl.gistofpreamble" /></th>
								<th width="7%"><spring:message code="lbl.agenda.number" /></th>
								<th width="9%"><spring:message code="lbl.preamble.number" /></th>
								<th width="14%"><spring:message code="lbl.department" /></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${!councilMeeting.meetingMOMs.isEmpty()}">
									<c:forEach items="${councilMeeting.meetingMOMs}" var="mom"
										varStatus="counter">
										<tr>
											<td align="center">${mom.itemNumber}</td>
											<td><span class="more"><c:out
														value="${mom.preamble.gistOfPreamble}" /></span></td>
											<td class="text-center"><c:out
													value="${mom.agenda.agendaNumber}" /></td>
											<td><c:out value="${mom.preamble.preambleNumber}" /></td>
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
	<div class="form-group">
		<div class="text-center">
			<button type='submit' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.update' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>

	<input type="hidden" name="councilMeeting" value="${councilMeeting.id}" />

</form:form>

<script
	src="<cdn:url value='/resources/app/js/councilMeeting.js?rnd=${app_release_no}'/>"></script>
<script
	src="<cdn:url value='/resources/app/js/showMoreorLessContent.js?rnd=${app_release_no}'/>"></script>
<script>
	$('#buttonSubmit').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>
<style>
.morecontent span {
	display: none;
}
</style>