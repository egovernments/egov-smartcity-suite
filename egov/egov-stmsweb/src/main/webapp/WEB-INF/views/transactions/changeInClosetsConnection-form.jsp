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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div class="row">
	<div class="col-md-12"> 
		<div class="text-right error-msg" style="font-size:14px;"><spring:message code="lbl.application.date"/> : <fmt:formatDate pattern="dd/MM/yyyy" value="${sewerageApplicationDetails.applicationDate}" /></div>
		<form:form role="form" method="post" 
			modelAttribute="sewerageApplicationDetails" id="modifySewerageConnectionform"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
			<form:hidden path="applicationType" id="applicationType" value="${sewerageApplicationDetails.applicationType.id}"/> 
			<form:hidden path="" id="applicationCode" value="${sewerageApplicationDetails.applicationType.code}"/> 
			<form:hidden path="connection.status" id="connection.status" value="${sewerageApplicationDetails.connection.status}"/>
			<form:hidden id="mode" path=""  value="${mode}"/>
	 	    <input type="hidden" name="allowIfPTDueExists" id="allowIfPTDueExists" value="${allowIfPTDueExists}"> 
	 	    <input type="hidden" name="shscNumber" id="shscNumber" value="${shscNumber}">  
			<input type="hidden" name="validateIfPTDueExists" id="validateIfPTDueExists" value="${validateIfPTDueExists}"> 
			<input type="hidden" name="approvalPosOnValidate" id="approvalPosOnValidate" value="${approvalPosOnValidate}">  
			<input type="hidden" name="ptAssessmentNo" id="ptAssessmentNo" value="${ptAssessmentNo}">  
			<form:hidden path="" id="workFlowAction" name="workFlowAction"/>
		   <form:hidden id="documentName" path="" value="${documentName}"/> 
		   
			<div class="panel-body custom-form ">
				<jsp:include page="applicantdetails.jsp"></jsp:include>
				<jsp:include page="connectiondetails.jsp"></jsp:include>
				<c:if test="${inspectionFeesCollectionRequired == 'true'}"> 
					<jsp:include page="inspectionCharges.jsp"></jsp:include>
				</c:if>
				 <jsp:include page="documentdetails.jsp"></jsp:include>
				<c:if test="${!isCitizenPortalUser}">
					<jsp:include page="../common/commonWorkflowMatrix.jsp" />
				</c:if>
			</div>
			<div class="buttonbottom" align="center">
				<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />
			</div>
		</form:form>
	</div>
</div>
<script src="<cdn:url  value='/resources/js/transactions/newconnection.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/js/transactions/documentsupload.js?rnd=${app_release_no}'/>"></script>
