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

<div class="row">
	<div class="col-md-12 table-header text-left">
		<spring:message code="lbl.schedule.draft.view" />
	</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="draftViewTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.schedule.code" /></th>
					<th><spring:message code="lbl.schedule.templatename" /></th>
					<th><spring:message code="lbl.schedule.type" /></th>
					<th><spring:message code="lbl.schedule.module" /></th>
					<th><spring:message code="lbl.schedule.category" /></th>
					<th><spring:message code="lbl.schedule.template" /></th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty draftList}">
					<c:forEach var="listVar" items="${draftList}">
						<tr>
							<td><c:out value="${listVar.id}" /></td>
							<td><c:out value="${listVar.name}" /></td>
							<td><c:out value="${listVar.draftType.name}" /></td>
							<td><c:out value="${listVar.category.name}" /></td>
							<td><c:out value="${listVar.subCategory.name}" /></td>
							<td><c:out value="${listVar.message}" /></td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty draftList}">
					<tr class="odd">
						<td colspan="13" class="dataTables_empty" valign="top"><spring:message
								code="lbl.norecords" /></td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>
<div class="row">
	<div class="col-md-12 table-header text-left">
		<spring:message code="lbl.schedule.view" />
	</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="scheduleViewTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.schedule.code" /></th>
					<th><spring:message code="lbl.schedule.templatename" /></th>
					<th><spring:message code="lbl.schedule.type" /></th>
					<th><spring:message code="lbl.schedule.status" /></th>
					<th><spring:message code="lbl.schedule.startdate" /></th>
					<th><spring:message code="lbl.schedule.starttime" /></th>
					<th><spring:message code="lbl.schedule.repeatevery" /></th>
					<th><spring:message code="lbl.schedule.template" /></th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty scheduleList}">
					<c:forEach var="listVar" items="${scheduleList}">
						<tr>
							<td><c:out value="${listVar.id}" /></td>
							<td><c:out value="${listVar.templateName}" /></td>
							<td><c:out value="${listVar.draftType.name}" /></td>
							<td><c:out value="${listVar.status}" /></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy"
									value="${listVar.details.startDt}" var="startDate" /> <c:out
									value="${startDate}" /></td>
							<td><c:out value="${listVar.details.startHH}" />:<c:out
									value="${listVar.details.startMM}" /></td>
							<td><c:out value="${listVar.scheduleRepeat.name}" /></td>
							<td><c:out value="${listVar.messageTemplate}" /></td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty scheduleList}">
					<tr class="odd">
						<td colspan="8" class="dataTables_empty" valign="top"><spring:message
								code="lbl.norecords" /></td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>

<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/schedule-view.js?rnd=${app_release_no}'/>"></script>
