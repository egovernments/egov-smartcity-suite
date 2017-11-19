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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.templatecode" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${milestoneTemplate.code}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.templatename" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${milestoneTemplate.name}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.templatedescription" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${milestoneTemplate.description}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.status" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:choose>
							<c:when test="${milestoneTemplate.status != 1}">
								<c:out default="INACTIVE" value="INACTIVE"></c:out>
							</c:when>
							<c:otherwise>
								<c:out default="ACTIVE" value="ACTIVE"></c:out>
							</c:otherwise>
						</c:choose>

					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.typeofwork" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A" value="${milestoneTemplate.typeOfWork.description}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.subtypeofwork" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out default="N/A"
							value="${milestoneTemplate.subTypeOfWork.description}"></c:out>
					</div>
				</div>
			</div>
		</div>

		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="hdr.milestonetemplateactivaity" />
				</div>
			</div>
			<div class="panel-body">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th><spring:message code="lbl.stageordernumber" /></th>
							<th><spring:message code="lbl.description" /></th>
							<th><spring:message code="lbl.percentage" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach
							items="${milestoneTemplate.getMilestoneTemplateActivities()}"
							var="milestoneDtls">
							<tr>
								<td><c:out default="N/A"
										value="${milestoneDtls.stageOrderNo}"></c:out></td>
								<td><c:out default="N/A"
										value="${milestoneDtls.description}"></c:out></td>
								<td align="right"><fmt:formatNumber groupingUsed="false" maxFractionDigits="2"
							minFractionDigits="2" value="${milestoneDtls.percentage}" />
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-sm-12 text-center">
		<input type="submit" name="closeButton" id="closeButton" value="Close"
			Class="btn btn-default" onclick="window.close();" />
	</div>
</div>