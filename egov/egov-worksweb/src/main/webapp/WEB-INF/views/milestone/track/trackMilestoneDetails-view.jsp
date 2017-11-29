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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
					<th><spring:message code="lbl.stagedescription" /></th>
					<th><spring:message code="lbl.percentage.stage" /></th>
					<th><spring:message code="lbl.schedulestartdate" /></th>
					<th><spring:message code="lbl.scheduleenddate" /></th>
					<th><spring:message code="lbl.currentstatus.stage" /></th>
					<th><spring:message code="lbl.completed.percentage.stage" /></th>
					<th><spring:message code="lbl.actual.percentage.stage" /></th>
					<th><spring:message code="lbl.completion.date" /></th>
					<th><spring:message code="lbl.reason.for.delay" /></th>
				</tr>
			</thead>
			<tbody id="milestoneDetailsTbl">
				<c:forEach items="${trackMilestone.getActivities()}" var="activity"
					varStatus="item">
					<tr id="milestoneRow">
						<td><fmt:formatNumber 
								value="${activity.milestoneActivity.stageOrderNo}" /></td>
						<td><c:out value="${activity.milestoneActivity.description}" />
						</td>
						<td align="right"><fmt:formatNumber groupingUsed="false"
								maxFractionDigits="2" minFractionDigits="2"
								value="${activity.milestoneActivity.percentage}" /> <%-- <c:out value="${activity.milestoneActivity.percentage}" /> --%>
						</td>
						<td><fmt:formatDate
								value="${activity.milestoneActivity.scheduleStartDate}"
								pattern="dd/MM/yyyy" /></td>
						<td><fmt:formatDate
								value="${activity.milestoneActivity.scheduleEndDate}"
								pattern="dd/MM/yyyy" /></td>
						<td><c:out value="${activity.status}" /></td>
						<td align="right"><fmt:formatNumber groupingUsed="false"
								maxFractionDigits="2" minFractionDigits="2"
								value="${activity.completedPercentage}" /> <%-- <c:out value="${activity.completedPercentage}" /> --%>
						</td>
						<td align="right"><fmt:formatNumber groupingUsed="false"
								maxFractionDigits="2" minFractionDigits="2"
								value="${(activity.milestoneActivity.percentage/100) * activity.completedPercentage}" />
							<%-- <c:out value="${ (activity.milestoneActivity.percentage/100) * activity.completedPercentage}" /> --%>
						</td>
						<td><fmt:formatDate value="${activity.completionDate}"
								pattern="dd/MM/yyyy" /></td>
						<td><c:out value="${activity.remarks}" /></td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<c:set var="percentage" value="${0}" scope="session" />
				<c:if test="${trackMilestone.milestone.getActivities() != null}">
					<c:forEach items="${trackMilestone.milestone.getActivities()}"
						var="activity">
						<c:set var="percentage"
							value="${percentage + activity.percentage }" />
					</c:forEach>
				</c:if>
				<c:set var="totalPercentage" value="${0}" scope="session" />
				<c:if test="${trackMilestone.getActivities() != null}">
					<c:forEach items="${trackMilestone.getActivities()}" var="activity">
						<c:set var="totalPercentage"
							value="${totalPercentage + ((activity.milestoneActivity.percentage/100) * activity.completedPercentage) }" />
					</c:forEach>
				</c:if>
				<tr>
					<td colspan="2" class="text-right"><spring:message
							code="lbl.total" /></td>
					<td class="text-right"><span id="totalPercentage"><fmt:formatNumber
								groupingUsed="false" maxFractionDigits="2" minFractionDigits="2"
								value="${percentage}" />
							<%-- <c:out
								value="${percentage}" /> --%></span></td>
					<td colspan="4" class="text-right"><spring:message
							code="lbl.workcompletionpercentage" /></td>
					<td class="text-right"><span id="totalCompletedPercentage">
							<fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
								minFractionDigits="2" value="${totalPercentage}" />
							<%-- <c:out
								value="${totalPercentage}" /> --%>
					</span></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>