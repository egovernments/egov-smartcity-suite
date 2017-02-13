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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
<form:form id="createLetterOfAcceptanceForm" class="form-horizontal form-groups-bordered" modelAttribute="workOrder" role="form" action="loa-save" method="post" enctype="multipart/form-data">
	<input type="hidden" id="defaultDepartmentId" value="${defaultDepartmentId }" />
	<input id="cancelConfirm" type="hidden" value="<spring:message code="msg.cancel.loa.confirm" />" />
	<div class="new-page-header"><spring:message code="title.loa.create" /></div>
	<input type="hidden" name="mode" id ="mode" value ="${mode}">
	<input type="hidden" name="workOrder" value="${workOrder.id}"/>
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
				<%@ include file="letterofacceptance-header.jsp"%>
			</div>
			<c:if test="${abstractEstimate.activities.size() != 0}">
				<div class="tab-pane fade" id="billofquantities">
					<%@ include file="letterofacceptance-billofquantities.jsp"%>
				</div>
			</c:if>
		</div>
		<c:if test="${!workflowHistory.isEmpty() && mode != null}">
			<jsp:include page="../common/commonworkflowhistory-view.jsp"></jsp:include>
		</c:if>
		<c:choose>
			<c:when test="${abstractEstimate.spillOverFlag && abstractEstimate.workOrderCreated }">
			<form:hidden path="" id="workFlowAction" name="workFlowAction"/>
				<div class="col-sm-12 text-center">
					<form:button type="submit" name="submit" id="save" class="btn btn-primary" value="Save" ><spring:message code="lbl.save"/></form:button>
					<form:button type="button" class="btn btn-default" id="button2" onclick="window.close();"><spring:message code="lbl.close"/></form:button>
				</div>
		    </c:when>
			<c:otherwise>
			<jsp:include page="../common/commonworkflowmatrix.jsp"/>
			<jsp:include page="../common/commonworkflowmatrix-button.jsp" />
		    </c:otherwise>
	  </c:choose>
</form:form>

<script src="<cdn:url value='/resources/js/letterofacceptance/letterofacceptance.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/js/measurementsheet/measurementsheet.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
