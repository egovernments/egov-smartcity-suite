<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
			<input type="hidden" value="${lineEstimate.spillOverFlag }" id=spillOverFlag />
			<input type="hidden" value="${mode}" id="mode"/>
			<input type="hidden" value="${fieldsRequiredMap}" id="fieldsRequiredMap"/>
			<input type="hidden" name="budgetAppropriationDate" id="budgetAppropriationDate" class="form-control datepicker" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" value='<fmt:formatDate value="${budgetAppropriationDate}" pattern="dd/MM/yyyy"/>' >
			<input type="hidden" id="errorcouncilresolutiondate" value="<spring:message code='error.councilResolutiondate.lessthan.budgetappropriation' />" />
			<input type="hidden" id="errorcontractcommiteeapprovaldate" value="<spring:message code='error.contractCommitteeApprovalDate.lessthan.budgetappropriation' />" />
			<input type="hidden" id="errorstandingcommitteapprovaldate" value="<spring:message code='error.standingCommitteeApprovalDate.lessthan.budgetappropriation' />" />
			<input type="hidden" id="errorgovernmentapprovaldate" value="<spring:message code='error.governmentApprovalDate.lessthan.budgetappropriation' />" />
			<input type="hidden" id="nominationLimit" value="${nominationLimit}">
			<input type="hidden" id="nominationName" value="${nominationName}">
			<input type="hidden" id="budgetControlType" value='${budgetControlType }'/>
			<input type="hidden" value="${hiddenfields}" id="hiddenfields" />
			<div class="row">
				<div class="col-md-12">
					<c:if test="${mode != 'view' && mode != 'readOnly' }">
						<jsp:include page="lineestimateheader.jsp"/>
						<jsp:include page="lineestimatedetails.jsp"/> 
					</c:if>
					<c:if test="${mode == 'view' || mode == 'readOnly' }">
						<jsp:include page="lineestimateheader-view.jsp"/>
						<jsp:include page="lineestimatedetails-view.jsp"/>
					</c:if>
					<jsp:include page="../common/uploaddocuments.jsp"/>
				</div>
			</div>
		<c:if test="${nextStatus == 'ADMINISTRATIVE_SANCTIONED'}" >
			<jsp:include page="lineestimateadminsanctiondetails.jsp"></jsp:include>
			<jsp:include page="lineestimate-workflowfields.jsp"></jsp:include>
		</c:if>
		<c:if test="${lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' }">
			<jsp:include page="lineestimateadminsanctiondetails.jsp"></jsp:include>
			<jsp:include page="lineestimatetechnicalsanctiondetails.jsp"/>
			<jsp:include page="lineestimateworkflowfields-view.jsp"/>
		</c:if>
		<c:if test="${!workflowHistory.isEmpty()}">
			<jsp:include page="../common/commonworkflowhistory-view.jsp"></jsp:include>
		</c:if>
		<c:if test="${mode != 'readOnly' }">
			<jsp:include page="../common/commonworkflowmatrix.jsp"/>
		</c:if>
		<div class="buttonbottom" align="center">
			<jsp:include page="../common/commonworkflowmatrix-button.jsp" />
		</div>
		</form:form>  
	
<script src="<cdn:url value='/resources/js/lineestimate.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
