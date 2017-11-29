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
<c:if test="${(lineEstimate.status.code == 'ADMINISTRATIVE_SANCTIONED' && mode == 'view') || lineEstimate.status.code == 'TECHNICAL_SANCTIONED' }">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title"><spring:message code="lbl.techsanctiondetails" /></div>
		</div>
		<input type="hidden" id="errorTechDate" value="<spring:message code='error.technicalsanctiondate' />" />
		<input type="hidden" id="errorActualAmount" value="<spring:message code='error.actualamount' />" />
		<input type="hidden" id="errorActualAmountContinued" value="<spring:message code='error.actualamount.continued' />" />
		<input type="hidden" id="adminSanctionDate" class="form-control datepicker" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" value='<fmt:formatDate value="${lineEstimate.adminSanctionDate }" pattern="dd/MM/yyyy"/>' />
		<div class="panel-body">
			<table class="table table-bordered" id="tblestimate">
				<thead>
					<tr>
						<th><spring:message code="lbl.technicalsanctionnumber"/>
							<c:if test="${mode != 'readOnly' }">
								<span class="mandatory"></span>
							</c:if>
						</th>
						<th><spring:message code="lbl.technicalsanctiondate"/>
							<c:if test="${mode != 'readOnly' }">
								<span class="mandatory"></span>
							</c:if>
						</th>
						<c:if test="${mode == 'readOnly' }">
						<th><spring:message code="lbl.technicalsanctionauthority"/>
						</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<c:if test="${mode != 'readOnly' }">
								<form:input path="technicalSanctionNumber" id="technicalSanctionNumber" onkeyup="calculateActualEstimatedAmountTotal();" onblur="calculateActualEstimatedAmountTotal();" data-errormsg="Technical Sanction Number of the work is mandatory!" data-idx="0" data-optional="0" class="form-control table-input" maxlength="32" required="required" />
								<form:errors path="technicalSanctionNumber" cssClass="add-margin error-msg" />
							</c:if>
							<c:if test="${lineEstimate.status.code == 'TECHNICAL_SANCTIONED' && mode == 'readOnly' }">
								<c:out value="${lineEstimate.technicalSanctionNumber }"></c:out>
							</c:if>
						</td>
						<td>
							<c:if test="${mode != 'readOnly' }">
								<form:input path="technicalSanctionDate" id="technicalSanctionDate" data-errormsg="Technical Sanction Date of the work is mandatory!" data-idx="0" data-optional="0" class="form-control datepicker" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" required="required" />
								<form:errors path="technicalSanctionDate" cssClass="add-margin error-msg" />
							</c:if>
							<c:if test="${lineEstimate.status.code == 'TECHNICAL_SANCTIONED' && mode == 'readOnly' }">
								<fmt:formatDate value="${lineEstimate.technicalSanctionDate }" pattern="dd/MM/yyyy"/>
							</c:if>
						</td>
						<c:if test="${mode == 'readOnly' && lineEstimate.technicalSanctionBy != null }">
 							<td><c:out default="N/A" value="${technicalsanctionbydesignation } - ${lineEstimate.technicalSanctionBy.name }"></c:out></td>
 						</c:if>
 						<c:if test="${mode == 'readOnly' && lineEstimate.technicalSanctionBy == null }">
 							<td><c:out default="N/A" value="N/A"></c:out></td>
 						</c:if>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</c:if>