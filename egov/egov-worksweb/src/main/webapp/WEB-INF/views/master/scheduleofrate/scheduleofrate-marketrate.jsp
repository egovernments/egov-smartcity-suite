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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.marketratedetails" />
		</div>
	</div>
	<input type="hidden" value="${scheduleOfRate.tempMarketRates.size() }"
		id="detailsSize" />
	<div class="panel-body">
		<div class="col-sm-12 text-right ">
			<button id="addRowBtn" type="button" class="btn btn-primary "
				onclick="addMarketRate()">
				<spring:message code="lbl.addsormarketrate" />
			</button>
		</div>
		<div class="col-sm-12 text-right "></div>
		<table class="table table-bordered" id="tblmarketrate">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.rate" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.fromdate" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.todate" /></th>
					<th><spring:message code="lbl.action" /></th>
				</tr>
			</thead>
			<tbody id="marketRateDetailsTbl">
				<c:choose>
					<c:when test="${scheduleOfRate.tempMarketRates.size() == 0}">
						<tr id="marketRateRow">
							<td><span class="marketratespansno">1</span> 
								<form:hidden
									path="tempMarketRates[0].id"
									class="form-control table-input hidden-input"/></td>
							<td><form:input path="tempMarketRates[0].marketRate.value"
									data-idx="0"
									data-optional="0"
									data-errormsg="Rate is mandatory!"
									class="form-control marketrate table-input patternvalidation" 
									data-pattern="decimalvalue"
									required = "required" />
								<form:errors path="tempMarketRates[0].marketRate.value"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempMarketRates[0].validity.startDate"
									data-errormsg="From Date is mandatory!"
									data-idx="0" data-optional="0"
									class="form-control datepicker marketratefromdate" maxlength="10"
									data-date-format="dd/mm/yyyy" data-inputmask="'mask': 'd/m/y'"
									required="required" /> <form:errors
									path="tempMarketRates[0].validity.startDate"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempMarketRates[0].validity.endDate"
									data-date-format="dd/mm/yyyy" data-idx="0"
									data-optional="1" class="form-control datepicker marketratetodate"
									maxlength="10" data-inputmask="'mask': 'd/m/y'" /> <form:errors
									path="tempMarketRates[0].validity.endDate"
									cssClass="add-margin error-msg" /></td>
							<td><span class="add-padding"
								onclick="deleteMarketRateRow(this);"><i class="fa fa-trash"
									data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${scheduleOfRate.tempMarketRates}" var="marketRate"
							varStatus="item">
							<tr id="marketRateRow">
								<td><span class="marketratespansno"><c:out
										value="${item.index + 1}" /></span> 
									<form:hidden
										path="tempMarketRates[${item.index}].id"
										value="${marketRate.id}"
										class="form-control table-input hidden-input" /></td>
								<td><form:input
										path="tempMarketRates[${item.index}].marketRate.value"
										data-idx="0"
										data-optional="0"
										data-errormsg="Rate is mandatory!"
										class="form-control table-input text-right marketrate patternvalidation removeDefaultValues"
										data-pattern="decimalvalue"
										required = "required" /> 
									<form:errors
										path="tempMarketRates[${item.index}].marketRate.value"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="tempMarketRates[${item.index}].validity.startDate"
										name="startDate" data-errormsg="From Date is mandatory!"
										data-idx="${item.index}" data-optional="0"
										class="form-control datepicker marketratefromdate" maxlength="10"
										data-date-format="dd/mm/yyyy" data-inputmask="'mask': 'd/m/y'"
										required="required" /> <form:errors
										path="tempMarketRates[${item.index}].validity.startDate"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="tempMarketRates[${item.index}].validity.endDate"
										name="endDate" data-date-format="dd/mm/yyyy" data-idx="${item.index}"
										data-optional="1" class="form-control datepicker marketratetodate"
										maxlength="10" data-inputmask="'mask': 'd/m/y'" /> <form:errors
										path="tempMarketRates[${item.index}].validity.endDate"
										cssClass="add-margin error-msg" /></td>
								<td><span class="add-padding"
									onclick="deleteMarketRateRow(this);"><i class="fa fa-trash"
										data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>