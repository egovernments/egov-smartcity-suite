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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<input type="hidden" id="errorCouncilResolutionDate" value="<spring:message code='error.councilresolutiondate' />" />
<input type="hidden" id="lineEstimateDate" value='<fmt:formatDate value="${lineEstimate.lineEstimateDate }" pattern="yyyy-MM-dd"/>' />
<c:if test="${mode != 'readOnly' || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' || lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' }" >
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message  code="lbl.adminsanctiondetails"/>
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered" id="tblestimate">
			<thead>
				<tr>
					<th><spring:message code="lbl.administrativesanctionnumber"/><c:if test="${mode != 'readOnly' && lineEstimate.status.code == 'BUDGET_SANCTIONED' }"><span class="mandatory"></span></c:if></th>
					<c:if test="${mode == 'readOnly' || lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' }">
					<th><spring:message code="lbl.adminsanctiondate"/></th>	
					<th><spring:message code="lbl.adminsanctionauthority"/></th>
					</c:if>
					<th><spring:message code="lbl.councilresolutionnumber"/></th>
					<th><spring:message code="lbl.councilresolutiondate"/></th>
				</tr>
			</thead>
			<tbody >
			</c:if>
				<tr>
					<c:if test="${mode != 'readOnly' && lineEstimate.status.code != 'ADMINISTRATIVE_SANCTIONED' }">
						<td>
							<form:input path="adminSanctionNumber" id="adminSanctionNumber" class="form-control table-input text-left" maxlength="32" required="required" onclick="validateadminSanctionNumber();"/>
							<form:errors path="adminSanctionNumber" cssClass="add-margin error-msg" />
						</td>
						<td>
							<form:input path="councilResolutionNumber"  id="councilResolutionNumber" name="councilResolutionNumber" value="${councilResolutionNumber}" data-errormsg="Council Resolution Number of the work is mandatory!" data-idx="0" data-optional="0" class="form-control table-input text-right" onclick="validatecouncilResolutionNumber();" maxlength="32"/>
							<form:errors path="councilResolutionNumber" cssClass="add-margin error-msg" />
						</td>
						<td>
							<form:input path="councilResolutionDate" id="councilResolutionDate" name="councilResolutionDate" value="${councilResolutionDate}" data-errormsg="Council Resolution Date of the work is mandatory!" data-idx="0" data-optional="0" class="form-control datepicker" onkeyup="testDate();" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" />
							<form:errors path="councilResolutionDate" cssClass="add-margin error-msg" />
						</td>
					</c:if>
						<c:if test="${mode == 'readOnly' || lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' }">
							<td><c:out default="N/A" value="${lineEstimate.adminSanctionNumber }"></c:out></td>
								<td><fmt:formatDate value="${lineEstimate.adminSanctionDate}" pattern="dd/MM/yyyy" /></td>
								<td><c:out default="N/A" value="${lineEstimate.adminSanctionBy }"></c:out></td>
							<td><c:out default="N/A" value="${lineEstimate.councilResolutionNumber}"></c:out></td>
							<c:choose>
								<c:when test="${lineEstimate.councilResolutionDate != null }">
									<div class="col-xs-3 add-margin view-content">
										<td><fmt:formatDate value="${lineEstimate.councilResolutionDate}" pattern="dd/MM/yyyy" /></td>
									</div>
								</c:when>
							<c:otherwise>
								<div class="col-xs-3 add-margin view-content">
									<td><c:out default="N/A" value="N/A" />
								</div>
							</c:otherwise>
							</c:choose>
						</c:if>
					</tr>
			</tbody>
		</table>
	</div>
</div>