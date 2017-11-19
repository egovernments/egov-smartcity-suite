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
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

		<form:form name="lineEstimateForm" action="" role="form" modelAttribute="lineEstimate" id="lineEstimate" class="form-horizontal form-groups-bordered" method="POST" enctype="multipart/form-data">
			<form:hidden path="" name="removedLineEstimateDetailsIds" id="removedLineEstimateDetailsIds" value="${removedLineEstimateDetailsIds }" class="form-control table-input hidden-input"/>
			<form:hidden path="" name="lineEstimateId" id="lineEstimateId" value="${lineEstimate.id}" class="form-control table-input hidden-input"/>
			<input type="hidden" value="${lineEstimate.status.code }" id=lineEstimateStatus />
			<input type="hidden" value="${mode}" id="mode"/>
			<input type="hidden" value="${hiddenfields}" id="hiddenfields" />
			<div class="row">
				<div class="col-md-12">
					<c:if test="${mode != 'view' && mode != 'readOnly' }">
						<jsp:include page="lineEstimateHeader.jsp"/>
						<jsp:include page="lineEstimateDetails.jsp"/>
						<jsp:include page="uploadDocuments.jsp"/>
					</c:if>
					<c:if test="${mode == 'view' || mode == 'readOnly' }">
						<jsp:include page="lineEstimateHeader-view.jsp"/>
						<jsp:include page="lineEstimateDetails-view.jsp"/>
					</c:if>
				</div>
			</div>
		<c:if test="${lineEstimate.status.code == 'BUDGET_SANCTIONED' || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' || lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' }" >
			<jsp:include page="lineEstimateAdminSanctionDetails.jsp"></jsp:include>
		</c:if>
		<c:if test="${lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' }">
			<jsp:include page="lineEstimateTechnicalSanctionDetails.jsp"/>
		</c:if>
			<c:if test="${mode == 'view' || mode == 'readOnly' }">
				<c:if test="${!lineEstimate.documentDetails.isEmpty() }">
					<jsp:include page="uploadDocuments.jsp" />
				</c:if>
			</c:if>
			<c:if test="${!workflowHistory.isEmpty()}">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message  code="lbl.apphistory"/>
					</div>
				</div>
				<jsp:include page="../common/commonWorkflowhistory-view.jsp"></jsp:include>
			</div>
		</c:if>
		<c:if test="${mode != 'readOnly' }">
			<jsp:include page="../common/commonWorkflowMatrix.jsp"/>
		</c:if>
		
		<div class="buttonbottom" align="center">
			<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />
		</div>
		</form:form>  
	
<script src="<cdn:url value='/resources/js/lineestimate.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
