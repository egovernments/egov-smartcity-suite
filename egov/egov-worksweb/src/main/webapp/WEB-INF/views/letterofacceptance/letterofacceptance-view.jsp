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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<style>
.msheet-tr {
	background: #f7f7f7;
}

.msheet-table {
	border: 1px solid #ddd;
}

.msheet-table thead:first-child>tr:first-child th {
	background: #E7E7E7;
	color: #333;
}
</style>
<form:form name="loaViewForm" action="" role="form" modelAttribute="workOrder" id="workOrder" class="form-horizontal form-groups-bordered" method="post">
		<input type="hidden" id="defaultDepartmentId" value="${defaultDepartmentId }" />
		<div class="panel-heading">
			<ul class="nav nav-tabs" id="settingstab">
				<li class="active">
					<a data-toggle="tab" href="#letterofacceptanceheader" data-tabidx=0><spring:message code="lbl.header" /></a>
				</li>
				<c:if test="${abstractEstimate.activities.size() != 0}">
					<li>
						<a data-toggle="tab" href="#billofquantities" data-tabidx=1><spring:message code="tab.header.billofquantities" /> </a>
					</li>
				</c:if>
			</ul>
		</div>
		<div class="tab-content">
			<div class="tab-pane fade in active" id="letterofacceptanceheader">
				<jsp:include page="letterofacceptance-viewheader.jsp" />
			</div>
			<c:if test="${abstractEstimate.activities.size() != 0}">
				<div class="tab-pane fade" id="billofquantities">
					<%@ include file="letterofacceptance-viewbillofquantities.jsp"%>
				</div>
			</c:if>
		</div>
		<jsp:include page="../common/uploaddocuments.jsp"/>
		<c:if test="${!workflowHistory.isEmpty()}">
			<jsp:include page="../common/commonworkflowhistory-view.jsp"></jsp:include>
		</c:if>
	<div class="row">
		<div class="col-sm-12 text-center">
		<c:choose>
			<c:when test="${mode == 'workflowView'}">
					<jsp:include page="../common/commonworkflowmatrix.jsp"/>
					<div class="buttonbottom" align="center">
						<jsp:include page="../common/commonworkflowmatrix-button.jsp" />
					</div>
			</c:when>
			<c:otherwise>
				<c:if test="${mode == 'modify' }">
					<form:button type="submit" name="submit" id="modify" class="btn btn-primary" ><spring:message code="lbl.modify"/></form:button>
				</c:if>
				<c:if test="${workOrder.egwStatus.code != 'CANCELLED' && abstractEstimate.lineEstimateDetails != null && !abstractEstimate.lineEstimateDetails.lineEstimate.workOrderCreated }">
				<a
					href="javascript:void(0)" class="btn btn-primary"
					onclick="renderPDF()"><spring:message code="lbl.view.loapdf" /></a>
				</c:if>
				<a href='javascript:void(0)' class='btn btn-default'
					onclick='self.close()'><spring:message code='lbl.close' /></a> 
			</c:otherwise>
		</c:choose>
		</div>
	</div>
</form:form>

<script src="<cdn:url value='/resources/js/letterofacceptance/viewletterofacceptancehelper.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/js/measurementsheet/measurementsheet.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
