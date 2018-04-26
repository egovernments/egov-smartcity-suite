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
<%@ include file="/includes/taglibs.jsp"%>

<div class="row">
	<div class="col-md-12">
	<form:form modelAttribute="event" name="searcheventForm" id="searcheventForm" class="form-horizontal form-groups-bordered">
		<div class="panel panel-primary" data-collapsed="0">

				<div class="panel-heading">
					<div class="panel-title"><spring:message code="lbl.event.search" /></div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.eventType" />:</label>
						<div class="col-sm-3 add-margin">
							<select id="eventType" name="eventType" class="form-control">
								<option value=""><spring:message code="lbl.select" /></option>
								<c:forEach var="item" items="${eventTypeList}">
									<option value="${item}">${item}</option>
								</c:forEach>
							</select>
						</div>
					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.name" />:</label>
						<div class="col-sm-3 add-margin">
							<input id="name" name="name" class="form-control text-left" maxlength="100"/>
						</div>
				</div>


				<div class="form-group">
					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.event.host" />:</label>
						<div class="col-sm-3 add-margin">
							<input id="eventhost" name="eventhost" class="form-control text-left" maxlength="20"/>
						</div>
					<label class="col-sm-2 control-label text-right"></label>
					<div class="col-sm-3 add-margin"></div>
				</div>
				<div class="row">
					<div class="text-center">
						<a href="javascript:void(0);" id="eventSearch"
							class="btn btn-primary"><spring:message code='lbl.search' /></a
					</div>
				</div>
			</div>
	</form:form>
	</div>
</div>
	<div class="row">
	<div class="col-md-12 table-header text-left">
		<spring:message code="title.event.view.all" />
		<button type='button' class='btn btn-primary' id="buttonSubmit" style="float: right;">
			<spring:message code='lbl.add.button' />
		</button>
	</div>
	<div class="col-md-12 form-group report-table-container">
		<table class="table table-bordered table-hover multiheadertbl"
			id="eventViewTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.event.code" /></th>
					<th><spring:message code="lbl.event.name" /></th>
					<th><spring:message code="lbl.event.description" /></th>
					<th><spring:message code="lbl.event.startdate" /></th>
					<th><spring:message code="lbl.event.starttime" /></th>
					<th><spring:message code="lbl.event.enddate" /></th>
					<th><spring:message code="lbl.event.endtime" /></th>
					<th><spring:message code="lbl.event.host" /></th>
					<th><spring:message code="lbl.event.location" /></th>
					<th><spring:message code="lbl.event.address" /></th>
					<th><spring:message code="lbl.event.eventType" /></th>
					<th><spring:message code="lbl.event.ispaid" /></th>
					<th><spring:message code="lbl.event.cost" /></th>
				</tr>
			</thead>
			<tbody>
			<c:if test="${not empty eventList}">
				<c:forEach var="listVar" items="${eventList}">
	    			<tr>
	    				<td><c:out value="${listVar.id}"/></td>
	    				<td><c:out value="${listVar.name}"/></td>
	    				<td><c:out value="${listVar.description}"/></td>
	    				<td>
	    					<fmt:formatDate pattern="dd/MM/yyyy" value="${listVar.eventDetails.startDt}" var="startDate" />
							<c:out value="${startDate}" />
	    				</td>
	    				<td><c:out value="${listVar.startTime}"/></td>
	    				<td>
	    					<fmt:formatDate pattern="dd/MM/yyyy" value="${listVar.eventDetails.endDt}" var="endDate" />
							<c:out value="${endDate}" />
	    				</td>
	    				<td><c:out value="${listVar.endTime}"/></td>
	    				<td><c:out value="${listVar.eventhost}"/></td>
	    				<td><c:out value="${listVar.eventlocation}"/></td>
	    				<td><c:out value="${listVar.address}"/></td>
	    				<td><c:out value="${listVar.eventType}"/></td>
	    				<td>
	    				<c:choose>
	    					<c:when test="${listVar.ispaid == true}">
	    						<spring:message code="lbl.event.yes" />
	    					</c:when>
	    					<c:otherwise>
	    						<spring:message code="lbl.event.no" />
	    					</c:otherwise>
	    				</c:choose>
	    				</td>
	    				<td><c:out value="${listVar.cost}"/></td>
	    			</tr>
				</c:forEach>
			</c:if>
			<c:if test="${empty eventList}">
				<tr class="odd">
					<td colspan="13" class="dataTables_empty" valign="top">No data available in table</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
</div>
<script>
	$('#btnsearch').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
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
	src="<cdn:url value='/resources/js/app/eventHelper.js?rnd=${app_release_no}'/>"></script>
