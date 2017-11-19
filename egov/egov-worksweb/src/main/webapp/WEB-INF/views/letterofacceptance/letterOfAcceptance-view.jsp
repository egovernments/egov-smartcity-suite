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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<form:form name="loaViewForm" action="" role="form"
	modelAttribute="workOrder" id="workOrder"
	class="form-horizontal form-groups-bordered" method="post">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0"
				style="text-align: left">
				<div class="panel-heading">
					<div class="panel-title">
						<c:choose>
							<c:when test="${mode == 'modify' }">
								<spring:message code="hdr.letterofacceptance.modify" />
							</c:when>
							<c:otherwise>
								<spring:message code="hdr.letterofacceptance" />
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div>
					<spring:hasBindErrors name="workOrder">
		        		<div class="alert alert-danger col-md-10 col-md-offset-1">
				      			<form:errors path="*" cssClass="error-msg add-margin" /><br/>
				      	</div>
		        	</spring:hasBindErrors>
		        </div>
				<div class="panel-body">
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.loanumber" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A" value="${workOrder.workOrderNumber}"></c:out>
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.dateofagreement" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<fmt:formatDate value="${workOrder.workOrderDate}"
								pattern="dd/MM/yyyy" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.estimatenumber" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A"
								value="${lineEstimateDetails.estimateNumber}"></c:out>
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.workidentificationnumber" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A"
								value="${lineEstimateDetails.projectCode.code}"></c:out>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.nameofwork" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A" value="${lineEstimateDetails.nameOfWork}"></c:out>
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.department" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<form:hidden path="id" name="id" value="${id}"
								class="form-control table-input hidden-input" />
							<c:out default="N/A"
								value="${lineEstimateDetails.lineEstimate.executingDepartment.name}"></c:out>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.file.no" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A" value="${workOrder.fileNumber}"></c:out>
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.file.date" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<fmt:formatDate value="${workOrder.fileDate}"
								pattern="dd/MM/yyyy" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.estimateamount" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:choose>
								<c:when
									test="${lineEstimateDetails.actualEstimateAmount == '0.0'}">
									<c:out default="N/A" value="N/A" />
								</c:when>
								<c:otherwise>
									<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
										minFractionDigits="2"
										value="${lineEstimateDetails.actualEstimateAmount}" />
								</c:otherwise>
							</c:choose>
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.tender.finalized.percentage" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:choose>
								<c:when test="${workOrder.tenderFinalizedPercentage == '0.0'}">
									<c:out default="N/A" value="N/A" />
								</c:when>
								<c:otherwise>
									<fmt:formatNumber
										value="${workOrder.tenderFinalizedPercentage}" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.agreement.amount" />
						</div>
						<div class="col-xs-3 add-margin view-content" id="agreementAmount">
							<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
								minFractionDigits="2" value="${workOrder.workOrderAmount}" />
						</div>
					</div>
					<c:if test="${mode == 'modify' }">
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.revised.value" /><span class="mandatory"></span>
							</div>
							<div class="col-sm-3">
								<div class="input-group" style="margin-bottom: 0;">
								    <div class="input-group-btn number-sign">
						               <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><span class="sign-text">+</span> &nbsp;<span class="caret"></span></button>
						               <ul class="dropdown-menu">
						                  <li><a href="javascript:void(0);">+</a></li>
						                  <li><a href="javascript:void(0);">-</a></li>                  
						               </ul>
						               <form:hidden path="percentageSign" name="percentageSign" id="percentageSign" />
						            </div>
						            <input name="revisedValue" id="revisedValue" type="text" class="form-control patternvalidation" maxlength="8" required="required" value="${revisedValue }" />
						        </div> 
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.revised.agreement.amount" /><span class="mandatory"></span>
							</div>
							<div class="col-sm-3 add-margin">
								 <input name="revisedWorkOrderAmount" id="workOrderAmount"  type="text" class="form-control text-right patternvalidation" maxlength="12" data-pattern="decimalvalue" required="required" readonly="true"/>
							</div>
						</div>
					</c:if>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.nameofagency" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A" value="${workOrder.contractor.name}" />
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.contractor.code" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A" value="${workOrder.contractor.code}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.emd.amount" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:choose>
								<c:when test="${workOrder.emdAmountDeposited == '0.0'}">
									<c:out default="N/A" value="N/A" />
								</c:when>
								<c:otherwise>
									<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
										minFractionDigits="2" value="${workOrder.emdAmountDeposited}" />
								</c:otherwise>
							</c:choose>
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.bank.guarantee" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A" value="${workOrder.bankGuarantee}" />
						</div>
					</div>
					<div class="row add-border">
					<div class="col-xs-3 add-margin">
							<spring:message code="lbl.additional.security.deposit" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:choose>
								<c:when test="${workOrder.securityDeposit == '0.0'}">
									<c:out default="N/A" value="N/A" />
								</c:when>
								<c:otherwise>
									<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
										minFractionDigits="2" value="${workOrder.securityDeposit}" />
								</c:otherwise>
							</c:choose>
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.contract.period" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A" value="${workOrder.contractPeriod}"></c:out>
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.dlp" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
								minFractionDigits="2" value="${workOrder.defectLiabilityPeriod}" />
						</div>
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.engineer.incharge" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A" value="${workOrder.engineerIncharge.name}" />
						</div>
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.preparedby" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A" value="${workOrder.createdBy.name}" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="${mode}" id="mode" />
	<c:if test="${!workOrder.documentDetails.isEmpty() }">
		<jsp:include page="uploadDocuments.jsp" />
	</c:if>
	<div class="row">
	<div class="col-sm-12 text-center">
		<c:if test="${mode == 'modify' }">
			<form:button type="submit" name="submit" id="modify" class="btn btn-primary" ><spring:message code="lbl.modify"/></form:button>
		</c:if>
		<a href='javascript:void(0)' class='btn btn-default'
			onclick='self.close()'><spring:message code='lbl.close' /></a> <a
			href="javascript:void(0)" class="btn btn-primary"
			onclick="renderPDF()"><spring:message code="lbl.view.loapdf" /></a>
	</div>
</div>
</form:form>

<script
	src="<cdn:url value='/resources/js/viewletterofacceptancehelper.js?rnd=${app_release_no}'/>"></script>
