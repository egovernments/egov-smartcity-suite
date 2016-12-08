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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<div class="panel panel-primary" data-collapsed="0">
	<div class="row">
		<div class="col-md-12">
			<div class="panel-heading">
				<div class="panel-title"><spring:message code="lbl.header" /></div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-md-2 col-xs-6 add-margin">
						<spring:message code="lbl.estimateno" />
						:
					</div>
						<div class="col-md-2 col-xs-6 add-margin view-content">
						<a href="javascript:void(0)"
							onclick='viewAbstractEstimate(<c:out value="${workOrderEstimate.estimate.id }"/>)'><c:out
								value="${workOrderEstimate.estimate.estimateNumber}" /></a>
						</div>
					<input type="hidden" name="mode" value="${mode}" id="mode" />
					<form:hidden path="estimateNumber" name="estimateNumber"
						value="${workOrderEstimate.estimate.estimateNumber}" />
					<div class="col-md-2 col-xs-6 add-margin">
						<spring:message code="lbl.lineestimateno" />
						:
					</div>
					<c:if
						test="${workOrderEstimate.estimate.lineEstimateDetails != null}">
						<div class="col-md-2 col-xs-6 add-margin view-content">
							<a href='javascript:void(0)'
								onclick="viewLineEstimate('<c:out value="${workOrderEstimate.estimate.lineEstimateDetails.lineEstimate.id}"/>')">
								<c:out
									value="${workOrderEstimate.estimate.lineEstimateDetails.lineEstimate.lineEstimateNumber}" />
							</a>
						</div>
					</c:if>
					<div class="col-md-2 col-xs-6 add-margin">
						<spring:message code="lbl.workidentificationo" />
						:
					</div>
					<div class="col-md-2 col-xs-6 add-margin view-content">${workOrderEstimate.estimate.projectCode.code}</div>
					<form:hidden path="" name="code" id="code"
						value="${workOrderEstimate.estimate.projectCode.code}" />
				</div>
	
				<div class="row add-border">
					<div class="col-md-2 col-xs-6 add-margin">
						<spring:message code="lbl.nameofwork" />
						:
					</div>
					<div class="col-md-2 col-xs-6 add-margin view-content">
						<c:out
							value="${workOrderEstimate.estimate.name}"></c:out>
					</div>
					<div class="col-md-2 col-xs-6 add-margin">
						<spring:message code="lbl.loano" />
						:
					</div>
					<div class="col-md-2 col-xs-6 add-margin view-content">
						<a href="javascript:void(0)"
							onclick='viewLOA(<c:out value="${workOrderEstimate.workOrder.id }"/>)'><c:out
								value="${workOrderEstimate.workOrder.workOrderNumber }" /></a>
					</div>
					<div class="col-md-2 col-xs-6 add-margin">
						<spring:message code="lbl.agreement.amount" />
						:
					</div>
					<div class="col-md-2 col-xs-6 add-margin view-content">
						&#8377
						<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
							minFractionDigits="2"
							value="${workOrderEstimate.workOrder.workOrderAmount}" />
							<input type="hidden" id="workOrderAmount" value="${workOrderEstimate.workOrder.workOrderAmount }"/>
					</div>
				</div>
				
				<div class="row add-border">
					<div class="col-md-2 col-xs-6 add-margin">
						<spring:message code="lbl.contractorname" />
						:
					</div>
					<div class="col-md-2 col-xs-6 add-margin view-content">
						<c:out
							value="${workOrderEstimate.workOrder.contractor.name}"></c:out>
					</div>
					<div class="col-md-2 col-xs-6 add-margin">
						<spring:message code="lbl.advance.paid" />
						:
					</div>
					<div class="col-md-2 col-xs-6 add-margin view-content">
						&#8377
						<c:if test="${advancePaidTillNow != null }">
							<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
								minFractionDigits="2"
								value="${advancePaidTillNow }" />
						</c:if>
						<c:if test="${advancePaidTillNow == null }">
							0.00
						</c:if>
							<input type="hidden" id="advancePaidTillNow" value="${advancePaidTillNow }"/>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>