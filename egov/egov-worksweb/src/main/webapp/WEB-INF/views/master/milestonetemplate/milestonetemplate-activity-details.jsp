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
			<spring:message code="lbl.milestonetemplate.activityDetails" />
		</div>
	</div>
	<input type="hidden" value="${milestoneTemplate.tempMilestoneTemplateActivities.size() }"
		id="detailsSize" />
	<div class="panel-body">
		<div class="col-sm-12 text-right ">
			<button id="addRowBtn" type="button" class="btn btn-primary "
				onclick="addMilestoneTemplateActivity()">
				<spring:message code="lbl.addrow" />
			</button>
		</div>
		<div class="col-sm-12 text-right "></div>
		<table class="table table-bordered" id="tblmilestonetemplate">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.stageordernumber" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.stagedescription" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.percentage.stage" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.action" /></th>
				</tr>
			</thead>
			<tbody id="milestoneTemplateDetailsTbl">
				<c:choose>
					<c:when test="${milestoneTemplate.tempMilestoneTemplateActivities.size() == 0}">
						<tr id="milestoneTemplateRow">
							<td><span class="spansno">1</span> 
								<form:hidden
									path="tempMilestoneTemplateActivities[0].id"
									class="form-control table-input hidden-input"/></td>
							<td><form:input path="tempMilestoneTemplateActivities[0].stageOrderNo"
									data-idx="0"
									data-optional="0"
									data-errormsg="Stage Order Number is mandatory!"
									class="form-control stageordernumber table-input" required = "required" />
								<form:errors path="tempMilestoneTemplateActivities[0].stageOrderNo"
									cssClass="add-margin error-msg" /></td>
							<td><form:textarea path="tempMilestoneTemplateActivities[0].description" 
									class="form-control table-input patternvalidation" 
									data-optional="0"
									data-errormsg="Description of stage is mandatory!"
									data-pattern="alphanumericwithallspecialcharacters" 
									maxlength = "1024" required = "required" />
								<form:errors path="tempMilestoneTemplateActivities[0].description" 
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempMilestoneTemplateActivities[0].percentage"
									data-idx="0"
									data-optional="0"
									data-errormsg="Percentage of stage is mandatory!"
									class="form-control table-input text-right percentage removeDefaultValues patternvalidation" 
									data-pattern="decimalvalue" required = "required"
									onkeyup="calculateTotalMilestoneTemplateActivityPercentage();" /> 
								<form:errors path="tempMilestoneTemplateActivities[0].percentage"
									cssClass="add-margin error-msg" /></td>
							<td><span class="add-padding"
								onclick="deleteMilestoneTemplateActivity(this);"><i class="fa fa-trash"
									data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${milestoneTemplate.tempMilestoneTemplateActivities}" var="milestoneTemplateActivity"
							varStatus="item">
							<tr id="milestoneTemplateRow">
								<td><span class="spansno"><c:out
											value="${item.index + 1}" /></span> <form:hidden
										path="tempMilestoneTemplateActivities[${item.index}].id"
										value="${milestoneTemplateActivity.id}"
										class="form-control table-input hidden-input" /></td>
								<td><form:input
										path="tempMilestoneTemplateActivities[${item.index}].stageOrderNo"
										data-idx="0"
										data-optional="0"
										data-errormsg="Stage Order Number is mandatory!"
										class="form-control table-input text-right stageordernumber removeDefaultValues"
										maxlength="1024" required = "required" /> 
									<form:errors
										path="tempMilestoneTemplateActivities[${item.index}].stageOrderNo"
										cssClass="add-margin error-msg" /></td>
								<td><form:textarea
										path="tempMilestoneTemplateActivities[${item.index}].description"
										data-idx="0"
										data-optional="0"
										data-errormsg="Description of stage is mandatory!"
										class="form-control table-input description removeDefaultValues"
										maxlength="1024" required = "required" /> 
									<form:errors
										path="tempMilestoneTemplateActivities[${item.index}].description"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="tempMilestoneTemplateActivities[${item.index}].percentage"
										data-idx="0" data-optional="0"
										data-errormsg="Percentage of stage is mandatory!" 
										data-pattern="decimalvalue"
										class="form-control table-input text-right percentage removeDefaultValues patternvalidation"
										onkeyup="calculateTotalMilestoneTemplateActivityPercentage();"
										required = "required" />
									<form:errors path="tempMilestoneTemplateActivities[${item.index}].percentage"
										cssClass="add-margin error-msg" /></td>
								<td><span class="add-padding"
									onclick="deleteMilestoneTemplateActivity(this);"><i class="fa fa-trash"
										data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				<tfoot>
					<c:set var="total" value="${0}" scope="session"/>
					<c:if test="${milestoneTemplate.getTempMilestoneTemplateActivities() != null}">
						<c:forEach items="${milestoneTemplate.getTempMilestoneTemplateActivities()}" var="milestoneTemplateDetails">
							<c:set var="total" value="${total + milestoneTemplateDetails.percentage}"/>
						</c:forEach>
					</c:if>
					<tr>
						<td></td>
						<td colspan="2" class="text-right"><spring:message code="lbl.total" /></td>
						<td class="text-right"> <span id="totalPercentageValue"><c:out value="${total}"/></span> </td>
					</tr>
				</tfoot>
			</tbody>
		</table>
	</div>
</div>
