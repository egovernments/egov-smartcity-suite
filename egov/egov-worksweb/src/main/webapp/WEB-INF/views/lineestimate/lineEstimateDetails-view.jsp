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

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title"><spring:message code="lbl.workdetails" /></div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered" id="tblestimate">
			<thead>
				<tr>
					<c:if test="${mode == 'readOnly' && lineEstimate.spillOverFlag == 'true' }">
						<div class="form-group">
							<label class="col-sm-3 control-label text-right"><spring:message
									code="lbl.workorder.created" /></label>
							<div class="col-sm-3 add-margin">
								<form:checkbox path="workOrderCreated" id="isWorkOrderCreated"
									value="${lineEstimate.workOrderCreated}" disabled="true" />
							</div>
							<label class="col-sm-2 control-label text-right"><spring:message
									code="lbl.bills.created" /></span> </label>
							<div class="col-sm-3 add-margin">
								<form:checkbox path="billsCreated" id="isBillsCreated"
									value="${lineEstimate.billsCreated }" disabled="true" />
							</div>
						</div>
					</c:if>

					<th><spring:message code="lbl.slNo"/></th>
					<th><spring:message code="lbl.nameofwork"/></th>
					<c:if test="${lineEstimate.status.code == 'BUDGET_SANCTIONED' || lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' 
					|| ((lineEstimate.status.code == 'CANCELLED' || lineEstimate.status.code == 'REJECTED') && (lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations != null && lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations[0].budgetUsage != null))}">
						<th><spring:message code="lbl.estimatenumber"/></th>
					</c:if>
					<c:if test="${(lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' && mode == 'view') || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' }">
					<th><spring:message code="lbl.workidentificationnumber"/></th>
					</c:if>
					<th><spring:message code="lbl.estimatedamount"/></th>					
					<c:if test="${(lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' && mode == 'view') || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' }">
						<th><spring:message code="lbl.actualamount"/>
							<c:if test="${mode != 'readOnly' }">
								<span class="mandatory"></span>
							</c:if>
						</th>
					</c:if>
					<c:if test="${lineEstimate.billsCreated =='true' && mode == 'readOnly' && lineEstimate.spillOverFlag == 'true'}">
					<th><spring:message code="lbl.grossamount"/></th>
					</c:if>
				</tr>
			</thead>
			<tbody id="lineEstimateDetailsTbl">
				<c:forEach items="${lineEstimate.getLineEstimateDetails()}" var="lineEstimateDtls" varStatus="item">
					<tr id="estimateRow">
						<td> 
							<span class="spansno"><c:out value="${item.index + 1}" /></span>
							<form:hidden path="lineEstimateDetails[${item.index}].id" name="lineEstimateDetails[${item.index}].id" value="${lineEstimateDtls.id}" class="form-control table-input hidden-input"/>
							<form:hidden path="lineEstimateDetails[${item.index}].id" id="estimateNumber${item.index}" value="${lineEstimateDtls.estimateNumber}" class="form-control table-input hidden-input"/>
						</td>
						<td>
							<c:out value="${lineEstimate.lineEstimateDetails[item.index].nameOfWork}"/>
						</td>
						<c:if test="${lineEstimate.status.code == 'BUDGET_SANCTIONED' || lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' 
						|| ((lineEstimate.status.code == 'CANCELLED' || lineEstimate.status.code == 'REJECTED') && (lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations != null && lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations[0].budgetUsage != null))}">
							<td>
								<c:out value="${lineEstimate.lineEstimateDetails[item.index].estimateNumber}"/>
							</td>
						</c:if>
						<c:if test="${(lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' && mode == 'view') || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' }">
							<td>
								<c:out value="${lineEstimate.lineEstimateDetails[item.index].projectCode.code}"/>
							</td>
						</c:if>
						<td class="text-right" id="estimateAmount${item.index}">
							<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${lineEstimate.lineEstimateDetails[item.index].estimateAmount}" />
						</td>
						<c:if test="${lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' && mode == 'view' }">
							<td class="text-right">
								<form:input path="lineEstimateDetails[${item.index }].actualEstimateAmount" id="actualEstimateAmount${item.index}" data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right estimateAmount" onkeyup="calculateActualEstimatedAmountTotal(this);" onblur="calculateActualEstimatedAmountTotal(this);" required="required"/>
							</td>
						</c:if>
						<c:if test="${lineEstimate.status.code == 'TECHNICAL_SANCTIONED' && mode == 'readOnly' }">
							<td class="text-right">
								<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${lineEstimate.lineEstimateDetails[item.index].actualEstimateAmount}" />
							</td>
						</c:if>
						<c:if test="${lineEstimate.billsCreated =='true' && mode == 'readOnly' && lineEstimate.spillOverFlag == 'true'}">
							<td class="text-right">
								<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${lineEstimate.lineEstimateDetails[item.index].grossAmountBilled}" />
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<c:set var="total" value="${0}" scope="session"/>
				<c:if test="${lineEstimate.getLineEstimateDetails() != null}">
					<c:forEach items="${lineEstimate.getLineEstimateDetails()}" var="lineEstimateDtls">
						<c:set var="total" value="${total + lineEstimateDtls.estimateAmount}"/>
					</c:forEach>
				</c:if>
				<c:set var="actualEstimateTotal" value="${0}" scope="session"/>
				<c:if test="${lineEstimate.getLineEstimateDetails() != null}">
					<c:forEach items="${lineEstimate.getLineEstimateDetails()}" var="lineEstimateDtls">
						<c:set var="actualEstimateTotal" value="${actualEstimateTotal + lineEstimateDtls.actualEstimateAmount}"/>
					</c:forEach>
				</c:if>
				<tr>
				<c:choose>
					<c:when test="${(lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' && mode == 'view') || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' }">
						<c:if test="${lineEstimate.status.code == 'BUDGET_SANCTIONED' || lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' 
						|| ((lineEstimate.status.code == 'CANCELLED' || lineEstimate.status.code == 'REJECTED') && (lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations != null &&  lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations[0].budgetUsage != null))}">
							<td colspan="4" class="text-right"><spring:message code="lbl.total" /></td>
						</c:if>
						<c:if test="${!(lineEstimate.status.code == 'BUDGET_SANCTIONED' || lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' 
						|| ((lineEstimate.status.code == 'CANCELLED' || lineEstimate.status.code == 'REJECTED') && (lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations != null && lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations[0].budgetUsage != null)))}">
							<td colspan="3" class="text-right"><spring:message code="lbl.total" /></td>
						</c:if>
						<td class="text-right"> <span id="estimateTotal"><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${total}" /></span> </td>
						<c:if test="${(lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' && mode == 'view') || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' }">
							<td class="text-right"> <span id="actualEstimateTotal"><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${actualEstimateTotal}" /></span> </td>
						</c:if>
					</c:when>
					<c:otherwise>
						<c:if test="${lineEstimate.status.code == 'BUDGET_SANCTIONED' || lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' 
						|| ((lineEstimate.status.code == 'CANCELLED' || lineEstimate.status.code == 'REJECTED') && (lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations != null &&  lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations[0].budgetUsage != null))}">
							<td colspan="3" class="text-right"><spring:message code="lbl.total" /></td>
						</c:if>
						<c:if test="${!(lineEstimate.status.code == 'BUDGET_SANCTIONED' || lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' 
						|| ((lineEstimate.status.code == 'CANCELLED' || lineEstimate.status.code == 'REJECTED') && (lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations != null && lineEstimate.lineEstimateDetails[0].lineEstimateAppropriations[0].budgetUsage != null)))}">
							<td colspan="2" class="text-right"><spring:message code="lbl.total" /></td>
						</c:if>
							<td class="text-right"> <span id="estimateTotal"><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${total}" /></span> </td>
						<c:if test="${(lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' && mode == 'view') || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' }">
							<td class="text-right"> <span id="actualEstimateTotal"><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" groupingUsed="false" value="${actualEstimateTotal}" /></span> </td>
						</c:if>
					</c:otherwise>
				</c:choose>
				</tr>
			</tfoot>
		</table>
		<div id="documentDetails">
		</div>
	</div>
</div>

