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
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading"></div>
	<div class="panel-body">
		<c:if test="${measurementsPresent}">
			<div align="right">
				<input type="button" value="Close All Measurements"
					class="btn btn-sm btn-secondary" onclick="closeAllViewmsheet()" />
				<input type="button" class="btn btn-sm btn-secondary"
					value="Open All Measurements" onclick="openAllViewmsheet()" />
			</div>
		</c:if>
		<table class="table table-bordered" id="tblsor">
			<thead>
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.schedulecategory" /></th>
					<th><spring:message code="lbl.sorcode" /></th>
					<th><spring:message code="lbl.estimatedquantity" /></th>
					<th><spring:message code="lbl.description" /></th>
					<th><spring:message code="lbl.uom" /></th>
					<th><spring:message code="lbl.workorderrate" /></th>
					<th><spring:message code="lbl.workorderamount" /></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${abstractEstimate.activities.size() != 0}">
						<c:forEach items="${abstractEstimate.getSORActivities()}"
							var="sorDtls" varStatus="item">
							<tr>
								<td><span class="spansno"><c:out
											value="${item.index + 1}" /></span></td>
								<c:set var="slNo" value="${item.index + 1}" />
								<td><c:out
										value="${sorDtls.schedule.scheduleCategory.code}"></c:out></td>
								<td><c:out value="${sorDtls.schedule.code}"></c:out></td>
								<td class="text-right"><c:out value="${sorDtls.quantity}"></c:out>
									<c:if test="${sorDtls.measurementSheetList.size() > 0 }">
										<button class="btn btn-default"
											name="sorActivities[${item.index}].msadd"
											id="sorActivities[${item.index}].msadd" data-idx="0"
											onclick="addMSheet(this);return false;">
											<i class="fa fa-plus-circle" aria-hidden="true"></i>
										</button>
									</c:if></td>
								<%@ include
									file="../measurementsheet/sor-measurementsheet-formtableview.jsp"%>
								<td><c:out value="${sorDtls.schedule.getSummary()}"></c:out>
									<a href="#" class="hintanchor"
									title="${sorDtls.schedule.description }"><i
										class="fa fa-question-circle" aria-hidden="true"></i></a></td>
								<td><c:out value="${sorDtls.uom.uom}"></c:out></td>
								<td class="text-right"><fmt:formatNumber
										groupingUsed="false" minFractionDigits="2"
										maxFractionDigits="2">
										<c:out value="${sorDtls.estimateRate}"></c:out>
									</fmt:formatNumber></td>
								<td class="text-right"><fmt:formatNumber
										groupingUsed="false" minFractionDigits="2"
										maxFractionDigits="2">
										<c:out value="${sorDtls.getAmount().value}"></c:out>
									</fmt:formatNumber></td>
							</tr>
						</c:forEach>
						<c:forEach items="${abstractEstimate.getNonSORActivities()}"
							var="nonSorDtls" varStatus="item">
							<tr>
								<c:set var="slNo" value="${ slNo + 1 }" />
								<td><span class="spansno"><c:out value="${slNo}" /></span></td>
								<td></td>
								<td></td>
								<td class="text-right"><c:out
										value="${nonSorDtls.quantity}"></c:out> <c:if
										test="${nonSorDtls.measurementSheetList.size() > 0 }">
										<button class="btn btn-default"
											name="nonSorActivities[${item.index}].msadd"
											id="nonSorActivities[${item.index}].msadd" data-idx="0"
											onclick="addMSheet(this);return false;">
											<i class="fa fa-plus-circle" aria-hidden="true"></i>
										</button>
									</c:if></td>
								<%@ include
									file="../measurementsheet/nonsor-measurementsheet-formtableview.jsp"%>
								<td><c:out value="${nonSorDtls.nonSor.description}"></c:out></td>
								<td><c:out value="${nonSorDtls.uom.uom}"></c:out></td>
								<td class="text-right"><fmt:formatNumber
										groupingUsed="false" minFractionDigits="2"
										maxFractionDigits="2">
										<c:out value="${nonSorDtls.estimateRate}"></c:out>
									</fmt:formatNumber></td>
								<td class="text-right"><fmt:formatNumber
										groupingUsed="false" minFractionDigits="2"
										maxFractionDigits="2">
										<c:out value="${nonSorDtls.getAmount().value}"></c:out>
									</fmt:formatNumber></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<c:set var="total" value="${0}" scope="session" />
				<c:if test="${abstractEstimate.getActivities() != null}">
					<c:forEach items="${abstractEstimate.getSORActivities()}" var="sor">
						<c:set var="total" value="${total + sor.getAmount().value }" />
					</c:forEach>
					<c:forEach items="${abstractEstimate.getNonSORActivities()}"
						var="nonSor">
						<c:set var="total" value="${total + nonSor.getAmount().value}" />
					</c:forEach>
				</c:if>
				<tr>
					<td colspan="7" class="text-right"><spring:message
							code="lbl.total" /></td>
					<td class="text-right"><span id="boqTotal"><fmt:formatNumber
								groupingUsed="false" minFractionDigits="2" maxFractionDigits="2">${total}</fmt:formatNumber></span></td>
				</tr>
				<tr>
					<td colspan="7" class="text-right"><spring:message
							code="lbl.tenderfinalizedpercentage" /></td>
					<td class="text-right"><fmt:formatNumber groupingUsed="false"
							minFractionDigits="2" maxFractionDigits="2">
							<c:out value="${workOrder.tenderFinalizedPercentage}"></c:out>
						</fmt:formatNumber></td>
				</tr>
				<tr>
					<td colspan="7" class="text-right"><spring:message
							code="lbl.agreement.value" /></td>
					<td class="text-right"><fmt:formatNumber groupingUsed="false"
							minFractionDigits="2" maxFractionDigits="2">
							<c:out value="${workOrder.workOrderAmount}"></c:out>
						</fmt:formatNumber></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>