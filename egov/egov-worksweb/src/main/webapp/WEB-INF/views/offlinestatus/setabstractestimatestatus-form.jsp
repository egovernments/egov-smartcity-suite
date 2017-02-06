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
<input type="hidden" value="<spring:message code="error.offlinestatus.status.aedate" />" id="errorStatusAEDate" />
<input type="hidden" value="<spring:message code="error.offlinestatus.status.date" />" id="errorStatusDate" />
<input type="hidden" value="<spring:message code="error.offlinestatus.status.date.null" />" id="errorStatusDateNull" />
<input type="hidden" value="<spring:message code="error.offlinestatus.status.date.between.null" />" id="errorStatusDateIntermediate" />
<form:form id="offlineStatuses" name="offlineStatuses" role="form" action="/egworks/offlinestatus/offlinestatus-saveabstractestimate" modelAttribute="abstractEstimate" class="form-horizontal form-groups-bordered">
	<div class="row">
	<spring:hasBindErrors name="abstractEstimate">
		<div class="alert alert-danger col-md-10 col-md-offset-1">
			<form:errors path="*" cssClass="add-margin" />
			<br />
		</div>
	</spring:hasBindErrors>
	</div>
	<input type="hidden" value="<spring:message code="error.offlinestatus.status.date" />" id="errorStatusDates" />
	<input type="hidden" name="abstractEstimate" value="${abstractEstimate.id}" />
	<input type="hidden" id="adminSanctionDate" value='<fmt:formatDate value="${abstractEstimate.approvedDate}" pattern="yyyy-MM-dd" />' />
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title">
				<c:choose>
					<c:when test="${offlineStatusSize != 6 }">
						<spring:message code="lbl.setofflinestatusforae" />
					</c:when>
					<c:otherwise>
						<spring:message code="lbl.viewofflinestatusforae" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="panel-body">
		<div class="form-group">
			<c:if test="${lineEstimateRequired }">
				<div class="col-sm-2 add-margin text-right"><spring:message code="lbl.lineestimatenumber"/></div>
				<div class="col-sm-2 add-margin view-content">
				<c:if test="${abstractEstimate.lineEstimateDetails != null }" >
					<a style="cursor: pointer;" onclick="openLineEstimate();">
					<c:out
							default="N/A"
							value="${abstractEstimate.lineEstimateDetails.lineEstimate.lineEstimateNumber}"></c:out></a> <input type="hidden" value="${abstractEstimate.lineEstimateDetails.lineEstimate.id}"
						name="lineEstimateId" id="lineEstimateId" />
						</c:if>
				</div>
			</c:if>
			<div class="col-sm-2 add-margin text-right"><spring:message code="lbl.abstractestimatenumber"/></div>
			<div class="col-sm-2 add-margin view-content">
				<a style="cursor: pointer;" onclick="openAbstractEstimate();"><c:out
						default="N/A"
						value="${abstractEstimate.estimateNumber}"></c:out></a> <input type="hidden" value="${abstractEstimate.id}"
					name="abstractEstimateId" id="abstractEstimateId" />
			</div>
			<div class="col-sm-2 add-margin text-right"><spring:message code="lbl.abstractestimatedate"/></div>
			<div class="col-sm-2 add-margin view-content">
				<fmt:formatDate value="${abstractEstimate.estimateDate}" pattern="dd/MM/yyyy" />
			</div>
		</div>
			
			<jsp:include page="/WEB-INF/views/offlinestatus/setofflinestatus-estimate.jsp" />
		
	</div>
</div>
	
<div class="row">
	<div class="col-sm-12 text-center">
	<c:if test="${offlineStatusSize != 6 }" >
		<form:button type="submit" name="submit" id="save"
			class="btn btn-primary" value="Save" onclick="return validateForm();">
			<spring:message code="lbl.save" />
		</form:button>
	</c:if>
		<form:button type="button" class="btn btn-default" id="button2"
			onclick="window.close();">
			<spring:message code="lbl.close" />
		</form:button>
	</div>
</div>
</form:form>
<script>
	$('#save').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>