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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.milestonedetails" />
		</div>
	</div>
	<input type="hidden" value="${milestone.activities.size() }"
		id="detailsSize" />
	<div class="panel-body">
		<table class="table table-bordered" id="tblmilestone">
			<thead>
				<tr>
					<th><spring:message code="lbl.stageordernumber" /></th>
					<th><spring:message code="lbl.stagedescription" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.stagepercentage" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.schedulestartdate" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.scheduleenddate" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.action" /></th>
				</tr>
			</thead>
			<tbody id="milestoneDetailsTbl">
				<c:choose>
					<c:when test="${milestone.activities.size() == 0}">
						<tr id="milestoneRow">
							<td width="10%"><form:input path="activities[0].stageOrderNo"
									id="stageOrderNo" name="activities[0].stageOrderNo" value="1"
									data-errormsg="Stage Order Number is mandatory!" data-idx="0"
									data-optional="0" class="form-control table-input stageOrderNo readonlyfields"
									maxlength="5" /> <form:errors
									path="activities[0].stageOrderNo"
									cssClass="add-margin error-msg" /></td>
							<td width="40%"><form:input path="activities[0].description"
									name="activities[0].description" id="description" value=""
									data-errormsg="Stage Description is mandatory!" data-idx="0"
									data-optional="0" class="form-control table-input description readonlyfields"
									 /> <form:errors
									path="activities[0].description"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="activities[0].percentage"
									name="activities[0].percentage" id="percentage" value=""
									data-errormsg="Percentage is mandatory!" data-idx="0"
									data-optional="0"
									class="form-control table-input text-right percentage readonlyfields"
									onkeyup="validatePercentage();"
									onblur="calculatePercentageTotal();"
									onchange="calculatePercentageTotal();"  />
								<form:errors path="activities[0].percentage"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="activities[0].scheduleStartDate"
									name="scheduleStartDate" value=""
									data-errormsg="Schedule Start Date is mandatory!" data-idx="0"
									data-optional="0"
									class="form-control datepicker scheduleStartDate"
									maxlength="10" data-date-format="dd/mm/yyyy"
									data-inputmask="'mask': 'd/m/y'"  /> <form:errors
									path="activities[0].scheduleStartDate"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="activities[0].scheduleEndDate"
									name="scheduleEndDate" value="${scheduleEndDate}"
									data-date-format="dd/mm/yyyy"
									data-errormsg="Schedule End Date is mandatory!" data-idx="0"
									data-optional="0"
									class="form-control datepicker scheduleEndDate" maxlength="10"
									data-inputmask="'mask': 'd/m/y'" /> <form:errors
									path="activities[0].scheduleEndDate"
									cssClass="add-margin error-msg"  /></td>
							<td><span class="add-padding"
								onclick="deleteMilestone(this);"><i class="fa fa-trash"
									data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${milestone.getActivities()}"
							var="activities" varStatus="item">
							<tr id="milestoneRow">
								<form:input path="activities[${item.index}].stageOrderNo"
									id="stageOrderNo" name="activities[${item.index}].stageOrderNo"
									value="1" data-errormsg="Stage Order Number is mandatory!" maxlength="5"
									data-idx="0" data-optional="0" class="form-control table-input readonlyfields"
									required="required" />
								<form:errors path="activities[${item.index}].stageOrderNo"
									cssClass="add-margin error-msg" />
								</td>
								<td><form:input
										path="activities[${item.index}].description"
										name="activities[${item.index}].description" id="description"
										value="" data-errormsg="Description is mandatory!"
										data-pattern="decimalvalue" data-idx="0" data-optional="0"
										class="form-control table-input text-right description readonlyfields"
										onkeyup="calculateEstimatedAmountTotal();"  required="required" />
									<form:errors path="activities[${item.index}].description"
										cssClass="add-margin error-msg" /></td>
								<td><form:input path="activities[${item.index}].percentage"
										id="percentage" name="activities[${item.index}].percentage"
										value="${activities.percentage}" onkeypress=""
										data-errormsg="Percentage is mandatory!"
										data-pattern="decimalvalue" data-idx="0" data-optional="0"
										class="form-control table-input text-right percentage readonlyfields"
										onkeyup="validatePercentage();" required="required" /> <form:errors
										path="activities[${item.index}].percentage"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="activities[${item.index}].scheduleStartDate"
										name="activities[${item.index}].scheduleStartDate"
										value="${activities.scheduleStartDate}"
										data-errormsg="Schedule Start Date is mandatory!"
										data-date-format="dd/mm/yyyy" data-pattern="decimalvalue"
										data-idx="0" data-optional="0"
										class="form-control table-input text scheduleStartDate"
										required="required" /> <form:errors
										path="activities[${item.index}].scheduleStartDate"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="activities[${item.index}].scheduleEndDate"
										name="activities[${item.index}].scheduleEndDate"
										value="${activities.scheduleEndDate}"
										data-errormsg="Schedule End Date is mandatory!"
										data-date-format="dd/mm/yyyy" data-pattern="decimalvalue"
										data-idx="0" data-optional="0"
										class="form-control table-input text scheduleEndDate"
										required="required" /> <form:errors
										path="activities[${item.index}].scheduleEndDate"
										cssClass="add-margin error-msg" /></td>
								<td><span class="add-padding"
									onclick="deleteMilestone(this);"><i class="fa fa-trash"
										data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
			<tfoot>
				<c:set var="percentage" value="${0}" scope="session" />
				<c:if test="${milestone.getActivities() != null}">
					<c:forEach items="${milestone.getActivities()}" var="milestoneDtls">
						<c:set var="percentage" value="${percentage }" />
					</c:forEach>
				</c:if>
				<tr>
					<td colspan="2" class="text-right"><spring:message
							code="lbl.total" /></td>
					<td class="text-right"><span id="totalPercentage"><c:out
								value="${percentage}" /></span></td>
					<td></td>
				</tr>
			</tfoot>
		</table>
		<div class="col-sm-12 text-center">
			<button id="addRowBtn" type="button" class="btn btn-primary"
				onclick="addMilestone()">
				<spring:message code="lbl.addrow" />
			</button>
		</div>
	</div>
</div>
