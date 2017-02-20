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

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-heading">
				<div class="panel-title"><spring:message code="lbl.header" /></div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.abstractestimatenumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${workOrderEstimate.estimate.estimateNumber}"></c:out>
 					</div>
 					<c:if test="${lineEstimateRequired == true }">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.dateofproposal" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<fmt:formatDate
								value="${workOrderEstimate.estimate.estimateDate}"
								pattern="dd/MM/yyyy" />
						</div>
					</c:if>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.nameofwork" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${workOrderEstimate.estimate.name}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.workidentificationnumber" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A"
							value="${workOrderEstimate.estimate.projectCode.code}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.typeofwork" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A"
							value="${workOrderEstimate.estimate.parentCategory.name}"></c:out>
							<input type="hidden" id="typeOfWork" value="${workOrderEstimate.estimate.parentCategory.id}"/>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.subtypeofwork" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A"
							value="${workOrderEstimate.estimate.category.name}"></c:out>
						<input type="hidden" id="subTypeOfWork" value="${workOrderEstimate.estimate.category.id}"/>
					</div>
				</div>
				<div class="row add-border">
				 	<c:if test="${lineEstimateRequired == true}">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.estimatepreparedby" />
						</div>
						<div class="col-xs-3 add-margin view-content">
							<c:out default="N/A"
								value="${workOrderEstimate.estimate.createdBy.name}"></c:out>
						</div>
					</c:if>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.department" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A"
							value="${workOrderEstimate.estimate.executingDepartment.name}"></c:out>
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
						<spring:message code="lbl.loadate" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<fmt:formatDate value="${workOrderEstimate.workOrder.workOrderDate}"
							pattern="dd/MM/yyyy" />

						<input type="hidden" id="workOrderDate"
							value='<fmt:formatDate value="${workOrderEstimate.workOrder.workOrderDate}"
							pattern="yyyy-MM-dd" />' />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.workvalue" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
							minFractionDigits="2" value="${workOrderEstimate.workOrder.workOrderAmount}" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.contractor" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${workOrderEstimate.workOrder.contractor.code} - ${workOrderEstimate.workOrder.contractor.name}"></c:out>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>