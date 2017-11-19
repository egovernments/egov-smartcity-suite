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

		<form:form id="contractorBillForm" class="form-horizontal form-groups-bordered" modelAttribute="contractorBillRegister" role="form" action="" method="post" enctype="multipart/form-data">
			<input type="hidden" name="workOrderDate" id="workOrderDate" class="form-control datepicker" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" value='<fmt:formatDate value="${workOrder.workOrderDate}" pattern="dd/MM/yyyy"/>'> 
			<form:hidden path="workOrder.id"  name="workOrder" id="workOrderId" value="${workOrder.id}" />
			<input type="hidden" id="id" value="${contractorBillRegister.id }" /> 
			<input type="hidden" name="mode" id="mode" value="${mode }" />
			<input type="hidden" name="isSpillover" id="isSpillOver" value="${contractorBillRegister.workOrderEstimate.estimate.lineEstimateDetails.lineEstimate.spillOverFlag}"/>
			<input type="hidden" name="contractorBillId" id="contractorBillId" value="${contractorBillRegister.id}" /> 
			<input type="hidden"  name="workOrderEstimateId" id="workOrderEstimateId" value="${contractorBillRegister.workOrderEstimate.id}" /> 
			<input type="hidden" id="defaultCutOffDate" value="${defaultCutOffDate }" />
					<div class="panel panel-primary" data-collapsed="0">
						
						<div class="panel-heading">
						<c:if test="${mode != 'readOnly'}">
							<div class="panel-title"><spring:message code="lbl.header" /></div>
						</c:if> 
						<c:if test="${mode == 'readOnly'}">
							<div class="panel-title"><spring:message code="hdr.contractorbill" /></div>
						</c:if>
						</div>
						<div>
							<spring:hasBindErrors name="contractorBillRegister">
					       		<div class="alert alert-danger col-md-10 col-md-offset-1">
						      			<form:errors path="*" cssClass="error-msg add-margin" /><br/>
						      	</div>
				        	</spring:hasBindErrors>
				        </div>
						<div class="panel-body">
							<c:if test="${mode == 'edit'}">
								<jsp:include page="contractorBill-header.jsp"/>
								<jsp:include page="contractorBill-mbdetails.jsp"/>
								<jsp:include page="contractorBill-debitaccountdetails.jsp"/>
								<jsp:include page="contractorBill-refund.jsp"/>
								<jsp:include page="contractorBill-creditaccountdetails.jsp"/>
							</c:if>
							<c:if test="${mode == 'view' || mode == 'readOnly' }">
								<jsp:include page="contractorBill-header-view.jsp"/>
								<jsp:include page="contractorBill-accountdetails-view.jsp"/>
							</c:if>
						<%-- 	<jsp:include page="contractorBill-details.jsp"/> --%>
						<c:if test="${!contractorBillRegister.documentDetails.isEmpty()}">
							<jsp:include page="uploadDocuments.jsp"/>
						</c:if>
						</div>
					</div>
			<c:if test="${contractorBillRegister.cancellationReason != null}">
				<div class="panel panel-primary" data-collapsed="0">
					<jsp:include page="contractorBillCancel-view.jsp"></jsp:include>
				</div>
			</c:if>
			<c:if test="${!workflowHistory.isEmpty() && mode != 'readOnly'}">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">
							<spring:message  code="lbl.apphistory"/>
						</div>
					</div>
					<jsp:include page="../common/commonWorkflowhistory-view.jsp"></jsp:include>
				</div>
			</c:if>
			<c:if test="${mode != 'readOnly'}">
			<jsp:include page="../common/commonWorkflowMatrix.jsp"/>
			<div class="buttonbottom" align="center">
				<jsp:include page="../common/commonWorkflowMatrix-button.jsp" />
			</div>
			</c:if>
			<c:if test="${mode == 'readOnly'}">
				<div class="row">
					<div class="col-sm-12 text-center">
						<input type="submit" name="closeButton"	id="closeButton" value="Close" Class="btn btn-default" onclick="window.close();" />
						<c:if test="${contractorBillRegister.billstatus == 'APPROVED' }">
						<a href="javascript:void(0)" class="btn btn-primary" onclick="renderPDF()" ><spring:message code="lbl.view.contractorbillpdf" /></a>
						</c:if>
					</div>
				</div>
			</c:if>
		</form:form>  
	
<script src="<cdn:url value='/resources/js/contractorbill.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
