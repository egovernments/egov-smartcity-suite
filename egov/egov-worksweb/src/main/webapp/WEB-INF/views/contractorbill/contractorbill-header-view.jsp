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
	      <div class="panel-body"> 	
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
						<input type="hidden" id="billtype" value="${contractorBillRegister.billtype }" />
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
						<c:out default="N/A" value="${workOrderEstimate.workOrder.contractor.name}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.contractor.code" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${workOrderEstimate.workOrder.contractor.code}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.loanumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<a style="cursor:pointer;" onclick="openLetterOfAcceptance();"><c:out default="N/A"
								value="${workOrderEstimate.workOrder.workOrderNumber}"></c:out></a> <input
							type="hidden" value="${workOrderEstimate.workOrder.id}" name="workOrderId" id="workOrderId" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.agreement.amount" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
								minFractionDigits="2" value="${workOrderEstimate.workOrder.workOrderAmount}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.fund" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${contractorBillRegister.egBillregistermis.fund.name}" /> 
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.abstractestimatenumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<a style="cursor: pointer;" onclick="openAbstractEstimate();"><c:out default="N/A"
							value="${workOrderEstimate.estimate.estimateNumber}"></c:out></a> <input type="hidden" value="${workOrderEstimate.estimate.id}"
						name="abstractEstimateId" id="abstractEstimateId" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.function" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${contractorBillRegister.egBillregistermis.function.name}" /> 
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.department" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${contractorBillRegister.egBillregistermis.egDepartment.name}" />
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
						<c:out default="N/A" value="${workOrderEstimate.estimate.projectCode.code}" />
					</div>
				</div>
				<c:choose>
					<c:when test="${billAssetValue !=null }">
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.assetcode" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${billAssetValue.asset.code}" /> 
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.assetname" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${billAssetValue.asset.name}" />
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.assetcode" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								N/A 
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.assetname" />
							</div>
							<div class="col-xs-3 add-margin view-content">
								N/A
							</div>
						</div>
					</c:otherwise>
				</c:choose>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.createdby" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${createdbybydesignation} - ${contractorBillRegister.createdBy.name }" />
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
	<c:if test="${workOrderEstimate.workOrderActivities.size() == 0 }">
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
				</div>
	</c:if>
	<c:if test="${workOrderEstimate.workOrderActivities.size() > 0 }">
		</br>
		</br>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.mb.referencenumber" /></th>
					<th><spring:message code="lbl.mb.pagenumber" /></th>
					<th><spring:message code="lbl.mb.date" /></th>
					<th><spring:message code="lbl.mbamount" /></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${mbDetails.size() != 0}">
						<c:forEach items="${mbDetails}" var="mbDtls" varStatus="item">
								<tr><c:set var="slNo" value="${slNo + 1}" />
									<td><span class="spansno"><c:out value="${slNo}" /></span></td>
									<td><a href='javascript:void(0)' onclick="viewMB('<c:out value="${mbDtls.id}"/>')"><c:out value="${mbDtls.mbRefNo}"/></a></td>
									<td><c:out value="${mbDtls.fromPageNo} - ${mbDtls.toPageNo}"></c:out></td>
									<td><fmt:formatDate value="${mbDtls.mbDate}" pattern="dd/MM/yyyy" /></td>
									<td class="text-right"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${mbDtls.mbAmount}" /></fmt:formatNumber></td>
								</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose> 
			</tbody>
			<tfoot>
				<c:set var="mbtotal" value="${0}" scope="session" />
				<c:if test="${mbDetails.size() != 0}">
					<c:forEach items="${mbDetails}" var="mb">
							<c:set var="mbtotal"	value="${mbtotal + mb.mbAmount }" />
					</c:forEach>
				</c:if>
				<tr>
				<td colspan="4" class="text-right"><spring:message code="lbl.total" /></td>
				<td class="text-right"><div id="mbTotalAmount"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${mbtotal}" /></fmt:formatNumber></div>
				</td>
				</tr>
			</tfoot>
		</table>
	</c:if>		
</div>