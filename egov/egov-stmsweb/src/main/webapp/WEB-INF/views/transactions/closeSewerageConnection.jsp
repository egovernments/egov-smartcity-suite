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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div id="main">
<div class="row">
	<div class="col-md-12">
		<form:form role="form" method="post"   
			modelAttribute="sewerageApplicationDetails" id="closeSewerageConnectionform"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
			<form:hidden path="applicationType" id="applicationType" value="${sewerageApplicationDetails.applicationType.id}"/> 
			<form:hidden path="connection.status" id="connection.status" value="${sewerageApplicationDetails.connection.status}"/>
			<form:hidden path="connectionDetail.id" id="connectionDetail" value="${sewerageApplicationDetails.connectionDetail.id}"/> 
			<form:hidden path="" id="workFlowAction" name="workFlowAction"/> 
			<form:hidden path="" id="appStatus" value="${sewerageApplicationDetails.status.code}"/> 
	 	    <input type="hidden" name="shscNumber" id="shscNumber" value="${shscNumber}">  
			<input type="hidden" name="validateIfPTDueExists" id="validateIfPTDueExists" value="${validateIfPTDueExists}"> 
			<input type="hidden" name="approvalPosOnValidate" id="approvalPosOnValidate" value="${approvalPosOnValidate}">  
			<input type="hidden" name="ptAssessmentNo" id="ptAssessmentNo" value="${ptAssessmentNo}">  
			<jsp:include page="commonApplicationDetails-view.jsp"/> 
			<jsp:include page="connectionDetails-view.jsp"></jsp:include> 
			 
			<div class="panel panel-primary">
				<div class="panel-heading ">
					<div class="panel-title" style="color: orange;" align="left">
						<strong><spring:message code="title.reasonForDisconnection" /></strong>
					</div>
				</div>
				<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.remarks"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
						
						<c:choose>
						<c:when test="${sewerageApplicationDetails!=null && (sewerageApplicationDetails.status != null 
							&& (sewerageApplicationDetails.status.code != 'CREATED' && sewerageApplicationDetails.status.code != 'REJECTED' ))}">
								<form:textarea cssClass="form-control patternvalidation" path="closeConnectionReason"  cols="20" id="closeConnectionReason" data-pattern="alphanumericwithspace" maxlength="512" readonly="true"/>
						</c:when>
						<c:otherwise>
							<form:textarea cssClass="form-control patternvalidation" path="closeConnectionReason"  cols="20" id="closeConnectionReason" data-pattern="alphanumericwithspace" maxlength="512" required="required"/>
							<form:errors path="closeConnectionReason" cssClass="add-margin error-msg"/>
						</c:otherwise>	
						</c:choose>	
				 		</div>
				 		<c:if test="${sewerageApplicationDetails!=null && sewerageApplicationDetails.appDetailsDocument[0].fileStore == null}">
				 		<label class="col-sm-2 control-label text-right">
							<spring:message code="lbl.attachdocument"/>
						</label>
						<div class="col-sm-3 add-margin">
							<input type="file" id="fileStoreId" name="files" class="file-ellipsis upload-file"> 
							<div class="add-margin error-msg" ><font size="2">
								<spring:message code="lbl.mesg.document"/>	
								</font></div>
						</div>
						</c:if>
						<c:if test="${sewerageApplicationDetails!=null && sewerageApplicationDetails.appDetailsDocument[0].fileStore != null}">
							<c:choose>
								<c:when test="${not empty documentNamesList}">
									<c:forEach var="document" varStatus="serialNo" items="${documentNamesList}">
										<div class="row">
											<div class="col-sm-3 add-margin"></div>
											<div class="col-sm-3 add-margin"></div>
								
											<label class="col-sm-3 control-label"><spring:message  code="lbl.fileattached"/></label> 
											<div class="col-sm-3 add-margin">
											<c:forEach var="file" items="${document.getFileStore()}">
												<a href="/egi/downloadfile?fileStoreId=${file.fileStoreId}&moduleName=STMS" target="_blank"> 
												<c:out value="${file.fileName}"/></a>
											</c:forEach>
											</div>
										</div> 
									</c:forEach>
								</c:when>
								<c:otherwise>
								
								</c:otherwise>
							</c:choose>
					</c:if>
				</div>
			</div>
			<jsp:include page="../common/commonWorkflowMatrix.jsp"/>
	 		<jsp:include page="../common/commonWorkflowMatrix-button.jsp"/>
		</form:form>
	</div>					
</div>					
</div>
<script src="<cdn:url  value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script>

$(document).ready(function()
{
	var appStatus = $('#appStatus').val(); 
	if(appStatus == 'CLOSERSANCTIONED'){ 
		$(".show-row").hide(); 
		$('#approverDetailHeading').hide();
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
	} else {
		$(".show-row").show(); 
		$('#approverDetailHeading').show();
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
	}
});
</script>