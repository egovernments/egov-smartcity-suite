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
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<c:if test="${mode == 'view' }">
					<div class="panel-title" style="text-align:center;" ><spring:message code="hdr.viewmilestonetemplate" /></div>
				</c:if>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.templatecode" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${milestoneTemplate.code}" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.templatestatus" />
					</div>
					<div id="status" class="col-xs-3 add-margin view-content">
						<c:choose>
							<c:when test="${milestoneTemplate.status == true}">
								<c:out value="ACTIVE" />
							</c:when>
							<c:otherwise>
								<c:out value="INACTIVE" />
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.templatename" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${milestoneTemplate.name}" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.description" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${milestoneTemplate.description}" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.typeofwork" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${milestoneTemplate.typeOfWork.name}" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.subtypeofwork" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:choose>
							<c:when test="${milestoneTemplate.subTypeOfWork != null}">
								<c:out value="${milestoneTemplate.subTypeOfWork.name}" />
							</c:when>
							<c:otherwise>
								<c:out value="NA" />
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.milestonetemplate.activityDetails" />
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
				<c:forEach	var="mta" items = "${milestoneTemplate.milestoneTemplateActivities }">
					<tr>
						<td><c:out value="${mta.stageOrderNo}"></c:out></td>
						<td><c:out value="${mta.description}"></c:out></td>
						<td class="text-right"><fmt:formatNumber type="number" pattern="#0.00#" value="${mta.percentage}" /></td>
					</tr>
				</c:forEach>
			</thead>
			<tbody>
				
			</tbody>
		</table>
	</div>
</div>