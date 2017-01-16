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
			<spring:message code="lbl.overheadrate" />
		</div>
	</div>
	<input type="hidden" value="${overhead.tempOverheadRateValues.size() }"
		id="detailsSize" />
	<div class="panel-body">
		<div class="col-sm-12 text-right ">
			<button id="addRowBtn" type="button" class="btn btn-primary "
				onclick="addOverhead()">
				<spring:message code="lbl.addrow" />
			</button>
		</div>
		<div class="col-sm-12 text-right "></div>
		<table class="table table-bordered" id="tbloverhead">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.lumpsumamount" /></th>
					<th><spring:message code="lbl.percentage" /></th>
					<th><spring:message code="lbl.startdate" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.enddate" /></th>
					<th><spring:message code="lbl.action" /></th>
				</tr>
			</thead>
			<tbody id="overheadDetailsTbl">
				<c:choose>
					<c:when test="${overhead.tempOverheadRateValues.size() == 0}">
						<tr id="overheadRow">
							<td><span class="spansno">1</span> <form:hidden
									path="tempOverheadRateValues[0].id" name="tempOverheadRateValues[0].id"
									value="${tempOverheadRateValues[0].id}"
									class="form-control table-input hidden-input" /></td>
							<td><form:input path="tempOverheadRateValues[0].lumpsumAmount"
									id="lumpsumamount"
									name="tempOverheadRateValues[0].lumpsumAmount" data-idx="0"
									onkeyup="validateLumpsumAmount();" data-optional="1"
									class="form-control table-input text-right lumpsumAmount removeDefaultValues patternvalidation" data-pattern="decimalvalue"  />
								<form:errors path="tempOverheadRateValues[0].lumpsumAmount"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempOverheadRateValues[0].percentage"
									name="tempOverheadRateValues[0].percentage"
									id="percentage" data-idx="0" onkeyup="validatePercentage();"
									data-optional="1"
									class="form-control table-input text-right percentage removeDefaultValues patternvalidation" data-pattern="decimalvalue" />
								<form:errors path="tempOverheadRateValues[0].percentage"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempOverheadRateValues[0].validity.startDate"
									name="startDate" data-errormsg="Start Date is mandatory!"
									data-idx="0" data-optional="0"
									class="form-control datepicker StartDate" maxlength="10"
									data-date-format="dd/mm/yyyy" data-inputmask="'mask': 'd/m/y'"
									required="required" /> <form:errors
									path="tempOverheadRateValues[0].validity.startDate"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempOverheadRateValues[0].validity.endDate"
									name="endDate" data-date-format="dd/mm/yyyy" data-idx="0"
									data-optional="1" class="form-control datepicker EndDate"
									maxlength="10" data-inputmask="'mask': 'd/m/y'" /> <form:errors
									path="tempOverheadRateValues[0].validity.endDate"
									cssClass="add-margin error-msg" /></td>
							<td><span class="add-padding"
								onclick="deleteOverhead(this);"><i class="fa fa-trash"
									data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${overhead.tempOverheadRateValues}" var="overheadRates"
							varStatus="item">
							<tr id="overheadRow">
								<td><span class="spansno"><c:out
											value="${item.index + 1}" /></span> <form:hidden
										path="tempOverheadRateValues[${item.index}].id"
										name="tempOverheadRateValues[${item.index}].id"
										value="${overheadRates.id}"
										class="form-control table-input hidden-input" /></td>
								<td><form:input
										path="tempOverheadRateValues[${item.index}].lumpsumAmount"
										id="lumpsumamount"
										name="tempOverheadRateValues[${item.index}].lumpsumAmount"
										onkeyup="validateLumpsumAmount();" data-idx="0"
										data-optional="1"
										class="form-control table-input text-right lumpsumAmount removeDefaultValues patternvalidation" data-pattern="decimalvalue"
										maxlength="1024" /> <form:errors
										path="tempOverheadRateValues[${item.index}].lumpsumAmount"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="tempOverheadRateValues[${item.index}].percentage"
										name="tempOverheadRateValues[${item.index}].percentage" id="percentage"
										data-idx="0" onkeyup="validatePercentage();" data-optional="1" data-pattern="decimalvalue"
										class="form-control table-input text-right percentage removeDefaultValues patternvalidation" />
									<form:errors path="overheadRates[${item.index}].percentage"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="tempOverheadRateValues[${item.index}].validity.startDate"
										name="startDate" data-errormsg="Start Date is mandatory!"
										data-idx="0" data-optional="0"
										class="form-control datepicker StartDate" maxlength="10"
										data-date-format="dd/mm/yyyy" data-inputmask="'mask': 'd/m/y'"
										required="required" /> <form:errors
										path="tempOverheadRateValues[${item.index}].validity.startDate"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="tempOverheadRateValues[${item.index}].validity.endDate"
										name="endDate" data-date-format="dd/mm/yyyy" data-idx="0"
										data-optional="1" class="form-control datepicker EndDate"
										maxlength="10" data-inputmask="'mask': 'd/m/y'" /> <form:errors
										path="tempOverheadRateValues[${item.index}].validity.endDate"
										cssClass="add-margin error-msg" /></td>
								<td><span class="add-padding"
									onclick="deleteOverhead(this);"><i class="fa fa-trash"
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
