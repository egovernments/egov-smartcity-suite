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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<form:hidden path="id" name="id" value="${id}" class="form-control table-input hidden-input"/>
				<spring:hasBindErrors name="contractorBillRegister">
		       		<div class="alert alert-danger col-md-10 col-md-offset-1">
			      			<form:errors path="*" cssClass="error-msg add-margin" /><br/>
			      	</div>
		       	</spring:hasBindErrors>
		       	
			<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.billnumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${contractorBillRegister.billnumber }" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.billdate" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<fmt:formatDate value="${contractorBillRegister.billdate}" pattern="dd/MM/yyyy" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.billtype" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${contractorBillRegister.billtype }" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.party.billnumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${contractorBillRegister.egBillregistermis.partyBillNumber}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.party.billdate" />
					</div>
					<div class="col-xs-3 add-margin view-content">
					<c:choose>
						<c:when test="${contractorBillRegister.egBillregistermis.partyBillDate != null }">
							<fmt:formatDate value="${contractorBillRegister.egBillregistermis.partyBillDate}" pattern="dd/MM/yyyy" />
						</c:when>
					<c:otherwise>
						<td><c:out default="N/A" value="N/A" />
					</c:otherwise>
					</c:choose>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.partyname" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${workOrder.contractor.name}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.contractor.code" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${workOrder.contractor.code}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.loanumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${workOrder.workOrderNumber}" /> 
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.agreement.amount" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${workOrder.workOrderAmount}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.fund" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${lineEstimateDetails.lineEstimate.fund.name}" /> 
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.estimatenumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${lineEstimateDetails.estimateNumber}" /> 
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.function" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${lineEstimateDetails.lineEstimate.function.name}" /> 
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.department" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${lineEstimateDetails.lineEstimate.executingDepartment.name}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.narration" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${contractorBillRegister.narration}" /> 
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.workidentificationnumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${lineEstimateDetails.projectCode.code}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.mb.referencenumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<form:hidden path="mbHeader.id"  value="${contractorBillRegister.mbHeader.id}" /> 
						<c:out value="${contractorBillRegister.mbHeader.mbRefNo}" /> 
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.mb.pagenumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${contractorBillRegister.mbHeader.fromPageNo}" /> - <c:out value="${contractorBillRegister.mbHeader.toPageNo}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.mb.date" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<fmt:formatDate value="${contractorBillRegister.mbHeader.mbDate}" pattern="dd/MM/yyyy" />
					</div>
					<c:if test="${contractorBillRegister.billtype == 'Final Bill'}">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.workcompletion.date" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<fmt:formatDate value="${contractorBillRegister.workOrderEstimate.workCompletionDate}" pattern="dd/MM/yyyy" />
					</div>
					</c:if>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.createdby" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${createdbybydesignation} - ${contractorBillRegister.createdBy.name }" />
					</div> 
				</div>
