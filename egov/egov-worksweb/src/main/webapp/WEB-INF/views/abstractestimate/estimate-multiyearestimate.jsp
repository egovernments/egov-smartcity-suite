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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:if test="${errorMsg != ''}">
	<div class="panel-heading">
		<div class="add-margin error-msg" style="text-align: center;">
			<strong><c:out value="${financialyearError}" /></strong>
		</div>
	</div>
</c:if>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="header.yearwiseestimate" />
		</div>
	</div>
	<input type="hidden"
		value="${abstractEstimate.multiYearEstimates.size() }"
		id="detailsSize" />
	<div class="panel-body">
		<table class="table table-bordered" id="tblyearestimate">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.year" /><span class="mandatory"></span></th>
					<th><spring:message code="lbl.percentage" /><span
						class="mandatory"></span></th>
					<c:if test="${abstractEstimate.lineEstimateDetails != null && abstractEstimate.lineEstimateDetails.estimateNumber == '' }">
					<th><spring:message code="lbl.action" /></th>
					</c:if>
				</tr>
			</thead>
			<tbody id="multiYeaeEstimateTbl">
				<c:choose>
					<c:when test="${abstractEstimate.multiYearEstimates.size() == 0}">
						<tr id="yearEstimateRow">
							<form:hidden path="multiYearEstimates[0].id" name="multiYearEstimates[0].id" value="${abstractEstimate.multiYearEstimates[0].id}" class="form-control table-input hidden-input" />
							<td><span class="spansno">1</span></td>
							
							<td><form:select path="multiYearEstimates[0].financialYear"	data-first-option="false" id="multiYearEstimates[0].financialYear" data-errormsg="Financial Year is mandatory!" class="form-control dropdownYear disablefield" data-idx="0" data-optional="0">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<form:options items="${finYear}" itemValue="id"	itemLabel="finYearRange" />
								</form:select> <form:errors path="multiYearEstimates[0].financialYear" cssClass="add-margin error-msg" />
							</td>
							
							<td><form:input path="multiYearEstimates[0].percentage" name="multiYearEstimates[0].percentage" value="${abstractEstimate.multiYearEstimates[0].percentage}"	data-errormsg="Percentage is mandatory!" onkeyup="validateQuantity();" data-pattern="decimalvalue" data-idx="0" data-optional="0"
									class="form-control table-input text-right inputYearEstimatePercentage disablefield" />
								<form:errors path="multiYearEstimates[0].percentage" cssClass="add-margin error-msg" /></td>
							<c:if test="${abstractEstimate.lineEstimateDetails != null && abstractEstimate.lineEstimateDetails.estimateNumber == '' }">
							<td><button type="button" class="btn btn-xs btn-secondary delete-row" onclick="deleteMultiYearEstimate(this);">
									<span class="glyphicon glyphicon-trash"></span> Delete
								</button>
							</td>
							</c:if>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${abstractEstimate.getMultiYearEstimates()}" var="yearEstimateDtls" varStatus="item">
							<tr id="yearEstimateRow">
								<form:hidden path="multiYearEstimates[${item.index}].id" name="multiYearEstimates[${item.index}].id" value="${multiYearEstimates.id}" class="form-control table-input hidden-input" />
								<td><span class="spansno">
								<c:out value="${item.index + 1}" /></span> 
								<td>
								<form:select path="multiYearEstimates[${item.index}].financialYear" data-first-option="false" id="multiYearEstimates[${item.index}].financialYear" data-errormsg="Financial Year is mandatory!" data-idx="0" data-optional="0" class="form-control dropdownYear disablefield">
										<form:option value="">
											<spring:message code="lbl.select" />
										</form:option>
										<form:options items="${finYear}" itemValue="id"	itemLabel="finYearRange" />
									</form:select> <form:errors	path="multiYearEstimates[${item.index}].financialYear"	cssClass="add-margin error-msg" />
								</td>
							 <td>
								<form:input	path="multiYearEstimates[${item.index}].percentage" data-errormsg="Percentage is mandatory!" data-pattern="decimalvalue" data-idx="0" data-optional="0" onkeyup="validateQuantity();" class="form-control inputYearEstimatePercentage table-input text-right disablefield" />
									<form:errors path="multiYearEstimates[${item.index}].percentage" cssClass="add-margin error-msg" />
								</td> 
								<c:if test="${abstractEstimate.lineEstimateDetails != null && abstractEstimate.lineEstimateDetails.estimateNumber == '' }">
								<td>
									<button type="button" class="btn btn-xs btn-secondary delete-row" onclick="deleteRow('tblyearestimate',this);"><span class="glyphicon glyphicon-trash"></span>Delete</button>
								</td>
								</c:if>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<c:set var="total" value="${0}" scope="session" />
				<c:if test="${abstractEstimate.getMultiYearEstimates() != null}">
					<c:forEach items="${abstractEstimate.getMultiYearEstimates()}"
						var="multiYearEstimates">
						<c:set var="total"
							value="${total + multiYearEstimates.percentage}" />
					</c:forEach>
				</c:if>
				<tr>
					<td colspan="2" class="text-right"><spring:message
							code="lbl.total" /></td>
					<td class="text-right"><span id="estimateTotal">
					<fmt:formatNumber groupingUsed="false" minFractionDigits="2" maxFractionDigits="2"><c:out value="${total}"/></fmt:formatNumber></span></td>
				</tr>
			</tfoot>
		</table>
		<c:if test="${(abstractEstimate.lineEstimateDetails != null && abstractEstimate.lineEstimateDetails.estimateNumber == '') || abstractEstimate.lineEstimateDetails == null }">
		<div class="col-sm-12 text-center">
			<button id="addRowBtn" type="button" class="btn btn-primary"
				onclick="addRow('tblyearestimate','yearEstimateRow')">
				<spring:message code="lbl.addrow" />
			</button>
		</div>
		</c:if>
	</div>
</div>
