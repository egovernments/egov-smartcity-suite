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

<form:form method="post" action=""
	class="form-horizontal form-groups-bordered" modelAttribute="event"
	id="eventForm">
	<input type="hidden" name="mode" value="${mode}" />
	<input type="hidden" name="eventId" id="eventId" value="${event.id}" />
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-body custom-form ">
					<c:if test="${not empty message}">
						<div role="alert"><c:out value="${message}"/></div>
					</c:if>
					<div class="panel-heading">
						<div class="panel-title">
							<spring:message code="title.event.view" />
						</div>
					</div>
					<div class="panel-body">
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.eventType" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${event.eventType}" />
							</div>

							<div class="col-xs-3 add-margin">
								
							</div>
							<div class="col-xs-3 add-margin view-content">
								
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.name" />
							</div>
							<div class="col-xs-9 add-margin view-content">
								<c:out value="${event.name}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.description" />
							</div>
							<div class="col-xs-9 add-margin view-content">
								<c:out value="${event.description}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.message" />
							</div>
							<div class="col-xs-9 add-margin view-content">
								<c:out value="${event.message}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.startdate" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<fmt:formatDate pattern="dd/MM/yyyy" value="${event.eventDetails.startDt}" var="startDate" />
								<c:out value="${startDate}" />
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.starttime" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${event.startTime}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.enddate" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<fmt:formatDate pattern="dd/MM/yyyy" value="${event.eventDetails.endDt}" var="endDate" />
								<c:out value="${endDate}" />
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.endtime" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${event.endTime}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.host" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${event.eventhost}" />
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.location" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${event.eventlocation}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.address" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${event.address}" />
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.Wallpaper" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:choose>
								    <c:when test="${empty event.filestore}">
								        
								    </c:when>
								    <c:otherwise>
								        <a href="/egi/downloadfile?fileStoreId=${event.filestore.fileStoreId}&moduleName=Eventnotification">${event.filestore.fileName }</a><br />
								    </c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.status" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								<c:out value="${event.status}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.ispaid" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								<c:choose>
									<c:when test="${event.ispaid == true}">
		    							<spring:message code="lbl.event.yes" />
				    				</c:when>
				    				<c:otherwise>
				    					<spring:message code="lbl.event.no" />
				    				</c:otherwise>
			    				</c:choose>
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.cost" />
							</div>
							<div class="col-sm-3 add-margin view-content">
								<c:out value="${event.cost}" />
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.event.url" />
							</div>
							<div class="col-sm-9 add-margin view-content">
								<c:out value="${event.url}" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="text-center">
					<button type='button' class='btn btn-primary' id="buttonSubmit">
						<spring:message code='lbl.edit.button' />
					</button>
					<a href="javascript:void(0)" class="btn btn-default"
						onclick="self.close()"><spring:message code="lbl.close" /></a>
				</div>
			</div>
		</div>
	</div>
</form:form>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>" />
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" />
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/js/app/eventViewResult.js?rnd=${app_release_no}' />"></script>
