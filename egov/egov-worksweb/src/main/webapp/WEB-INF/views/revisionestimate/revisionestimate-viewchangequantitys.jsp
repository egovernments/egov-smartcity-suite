<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<c:if test="${revisionEstimate.changeQuantityActivities.size() != 0}">
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.change.quantity" />
		</div>
	</div>
	<div align="right" class="openCloseAll">
		<input type="button" value="Close All Measurements" class="btn btn-sm btn-secondary"
			onclick="closeAllmsheet()" /> <input type="button" class="btn btn-sm btn-secondary"
			value="Open All Measurements" onclick="openAllmsheet()" />
	</div>
	<div class="panel-body">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.categorycode" /></th>
					<th><spring:message code="lbl.code" /></th>
					<th><spring:message code="lbl.description" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.cq.rate" /></th>
					<th><spring:message code="lbl.cq.estimate.quantity" /></th>
					<th><spring:message code="lbl.consumed.quantity" /></th>
					<th><spring:message code="lbl.addition.or.reduction.qty" /></th>
					<th><spring:message code="lbl.addition.or.reduction.amount" /></th>
					<th><spring:message code="lbl.revised.estimate.qty" /></th>
					<c:if test="${isServiceVATRequired == true }">
						<th><spring:message code="lbl.service.vat" /></th>
						<th><spring:message code="lbl.service.vat.amount" /></th>
					</c:if>
					<th><spring:message code="lbl.revised.total.amount" /></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${revisionEstimate.changeQuantityActivities.size() != 0}">
						<c:forEach items="${revisionEstimate.changeQuantityActivities}" var="sorDtls" varStatus="item">
							<c:if test="${sorDtls.parent != null}">
								<tr >
									<td><span class="spansno"><c:out value="${item.index + 1}" /></span></td>
									<td>
										<c:if test="${sorDtls.schedule !=null }">
											<c:out value="${sorDtls.schedule.scheduleCategory.code}"></c:out>
										</c:if>
									</td>
									<td>
										<c:if test="${sorDtls.schedule !=null }">
											<c:out value="${sorDtls.schedule.code}"></c:out>
										</c:if>
									</td>
								 	<td>
								 		<c:choose>
								 			<c:when test="${sorDtls.schedule !=null }">
								 				<c:out value="${sorDtls.schedule.getSummary()}"></c:out>
								 				<a href="#" class="hintanchor" title="${sorDtls.schedule.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a>
								 			</c:when>
								 			<c:otherwise>
								 				<c:out value="${sorDtls.nonSor.description}"></c:out>
								 				<a href="#" class="hintanchor" title="${sorDtls.nonSor.description }"><i class="fa fa-question-circle" aria-hidden="true"></i></a>
								 			</c:otherwise>
								 		</c:choose>
								 	</td> 
								 	<td><c:out value="${sorDtls.uom.uom}"></c:out></td>
								 	<td class="text-right"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${sorDtls.estimateRate}"></c:out></fmt:formatNumber></td>
								 	<td class="text-right">
										<span class="activityEstimateQuantity_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${sorDtls.estimateQuantity }</fmt:formatNumber></span>
									</td>
									<td class="text-right">
										<span class="activityConsumedQuantity_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${sorDtls.consumedQuantity }</fmt:formatNumber></span>
									</td>
								 	<td class="text-right">
								 		<c:if test="${sorDtls.revisionType == 'REDUCED_QUANTITY' }">-</c:if>
								 		<c:out value="${sorDtls.quantity}"></c:out>
								 	<c:if test="${sorDtls.measurementSheetList.size() > 0 }">
								 		 <button class="btn btn-default openmsheet" name="changeQuantityActivities[${item.index}].msadd" id="changeQuantityActivities[${item.index}].msadd" data-idx="0" onclick="addCQMSheet(this);return false;"><i  class="fa fa-plus-circle" aria-hidden="true"></i></button>
								 	 </c:if>
								 	</td>
								 		<%@ include file="../measurementsheet/changequantity-measurementsheet-formtable-view.jsp" %>  
								 	<td class="text-right"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${sorDtls.getAmount().value}" /></fmt:formatNumber></td>
								 	<c:if test="${isServiceVATRequired == true }">
										<td class="text-right"><c:out value="${sorDtls.serviceTaxPerc}"></c:out></td>
										<td class="text-right"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2">${(sorDtls.getAmount().value) * (sorDtls.serviceTaxPerc / 100) }</fmt:formatNumber></td>
									</c:if>
									<td class="text-right">
										<c:if test="${sorDtls.revisionType == 'ADDITIONAL_QUANTITY' }">
											<span class="revisedEstimateQty revisedEstimateQty_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${sorDtls.estimateQuantity + sorDtls.quantity}</fmt:formatNumber></span>
										</c:if>
										<c:if test="${sorDtls.revisionType == 'REDUCED_QUANTITY' }">
											<span class="revisedEstimateQty revisedEstimateQty_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${sorDtls.estimateQuantity - sorDtls.quantity}</fmt:formatNumber></span>
										</c:if>
									</td>
									<td class="text-right">
										<c:if test="${sorDtls.revisionType == 'ADDITIONAL_QUANTITY' }">
											<span class="activityTotal activityTotal_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${sorDtls.rate * (sorDtls.quantity + sorDtls.estimateQuantity) }</fmt:formatNumber></span>
										</c:if>
										<c:if test="${sorDtls.revisionType == 'REDUCED_QUANTITY' }">
											<span class="activityTotal activityTotal_${item.index }"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="4">${sorDtls.rate * (sorDtls.estimateQuantity - sorDtls.quantity) }</fmt:formatNumber></span>
										</c:if>
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose> 
			</tbody>
			<tfoot>
				<c:set var="recqsortotal" value="${0}" scope="session" />
				<c:if test="${revisionEstimate.changeQuantityActivities != null}">
					<c:forEach items="${revisionEstimate.changeQuantityActivities}" var="sorDtls">
						<c:if test="${sorDtls.revisionType == 'ADDITIONAL_QUANTITY' }">
							<c:set var="recqsortotal" value="${recqsortotal + (sorDtls.rate * (sorDtls.quantity)) }" />
						</c:if>
						<c:if test="${sorDtls.revisionType == 'REDUCED_QUANTITY' }">
							<c:set var="recqsortotal" value="${recqsortotal - (sorDtls.rate * (sorDtls.quantity)) }" />
						</c:if>
						
					</c:forEach>
				</c:if>
				<c:set var="cqsortotal" value="${0}" scope="session" />
				<c:if test="${revisionEstimate.changeQuantityActivities != null}">
					<c:forEach items="${revisionEstimate.changeQuantityActivities}" var="sorDtls">
						<c:if test="${sorDtls.revisionType == 'ADDITIONAL_QUANTITY' }">
							<c:set var="cqsortotal"	value="${cqsortotal + (sorDtls.rate * (sorDtls.quantity + sorDtls.estimateQuantity)) }" />
						</c:if>
						<c:if test="${sorDtls.revisionType == 'REDUCED_QUANTITY' }">
							<c:set var="cqsortotal"	value="${cqsortotal - (sorDtls.rate * (sorDtls.estimateQuantity - sorDtls.quantity)) }" />
						</c:if>
					</c:forEach>
				</c:if>
				<tr>
				<c:if test="${isServiceVATRequired == true }">
					<td colspan="11" class="text-right"><spring:message code="lbl.total" /></td>
				</c:if>
				<c:if test="${isServiceVATRequired == false }">
					<td colspan="9" class="text-right"><spring:message code="lbl.total" /></td>
				</c:if>
				<td class="text-right"><span id="reActivityTotal"><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out default="0.00" value="${recqsortotal }" /></fmt:formatNumber></span> </td>
				<td class="text-right"></td>
				<td class="text-right">
					<span><fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${cqsortotal}" /></fmt:formatNumber></span>
				</td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
</c:if>