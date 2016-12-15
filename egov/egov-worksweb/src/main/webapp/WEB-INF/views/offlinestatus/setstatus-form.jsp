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
<input type="hidden" value="<spring:message code="error.offlinestatus.status.loadate" />" id="errorStatusLOADate" />
<input type="hidden" value="<spring:message code="error.offlinestatus.status.date" />" id="errorStatusDate" />
<form:form id="offlineStatuses" name="offlineStatuses" role="form" action="/egworks/offlinestatus/offlinestatus-save" modelAttribute="workOrder" class="form-horizontal form-groups-bordered">
	<div class="row">
	<spring:hasBindErrors name="workOrder">
		<div class="alert alert-danger col-md-10 col-md-offset-1">
			<form:errors path="*" cssClass="add-margin" />
			<br />
		</div>
	</spring:hasBindErrors>
	</div>
	<input type="hidden" value="<spring:message code="error.offlinestatus.status.date" />" id="errorStatusDates" />
	<input type="hidden" value="<spring:message code="error.offlinestatus.status.date.null" />" id="errorStatusDateNull" />
	<input type="hidden" value="<spring:message code="error.offlinestatus.status.date.between.null" />" id="errorStatusDateIntermediate" />
	<input type="hidden" name="workOrder" value="${workOrder.id}" />
	<input type="hidden" id="workOrderDate" value='<fmt:formatDate value="${workOrder.workOrderDate}" pattern="yyyy-MM-dd" />' />
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title">
				<c:choose>
					<c:when test="${offlineStatusSize != 6 }">
						<spring:message code="lbl.setofflinestatusforloa" />
					</c:when>
					<c:otherwise>
						<spring:message code="lbl.viewstatus" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="panel-body">
		<div class="form-group">
			<div class="col-sm-3 add-margin text-right"><spring:message code="lbl.loanumber"/></div>
			<div class="col-sm-3 add-margin view-content">
				
				<a style="cursor: pointer;" onclick="openLOA();">
				<c:out
						default="N/A"
						value="${workOrder.workOrderNumber}"></c:out></a> <input type="hidden" value="${workOrder.id}"
					name="workOrderId" id="workOrderId" />
			</div>
			<div class="col-sm-3 add-margin text-right"><spring:message code="lbl.loadate"/></div>
			<div class="col-sm-3 add-margin view-content">
				<fmt:formatDate value="${workOrder.workOrderDate}" pattern="dd/MM/yyyy" />
			</div>
		</div>
	
		<jsp:include page="/WEB-INF/views/common/setofflinestatus.jsp" />
		
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