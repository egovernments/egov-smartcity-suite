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
<input type="hidden" id="lineEstimateDate" value='<fmt:formatDate value="${lineEstimate.lineEstimateDate }" pattern="dd/MM/yyyy"/>' />
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message  code="lbl.adminsanctiondetails"/>
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th><spring:message code="lbl.councilresolutionnumber"/></th>
					<th><spring:message code="lbl.councilresolutiondate"/></th>
					<th><spring:message code="lbl.administrativesanctionnumber"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.adminsanctiondate"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.admin.authority"/><span class="mandatory"></span></th></th>		
				</tr>
			</thead>
			<tbody >
				<tr>
					<td>
						<form:input path="councilResolutionNumber" id="councilResolutionNumber" name="councilResolutionNumber" value="${councilResolutionNumber}" data-errormsg="Council Resolution Number of the work is mandatory!" data-idx="0" data-optional="0" class="form-control table-input" onclick="validatecouncilResolutionNumber();" maxlength="32"/>
						<form:errors path="councilResolutionNumber" cssClass="add-margin error-msg" />
					</td>
					<td>
						<form:input path="councilResolutionDate" id="councilResolutionDate" name="councilResolutionDate" value="${councilResolutionDate}" data-errormsg="Council Resolution Date of the work is mandatory!" data-idx="0" data-optional="0" class="form-control datepicker" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" />
						<form:errors path="councilResolutionDate" cssClass="add-margin error-msg" />
					</td>
					<td>
						<form:input path="adminSanctionNumber" id="adminSanctionNumber" class="form-control table-input text-left" maxlength="32" required="required" onclick="validateadminSanctionNumber();"/>
						<form:errors path="adminSanctionNumber" cssClass="add-margin error-msg" />
					</td>
					<td>
						<form:input path="adminSanctionDate" id="adminSanctionDate" name="adminSanctionDate" value="${adminSanctionDate}" data-errormsg="Administrative Resolution Date of the work is mandatory!" required="required" data-idx="0" data-optional="0" class="form-control datepicker" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" />
						<form:errors path="adminSanctionDate" cssClass="add-margin error-msg" />
					</td>
					<td>
						<form:input path="adminSanctionBy" id="adminSanctionBy" class="form-control table-input text-left is_valid_alphabet" maxlength="100" required="required"/>
						<form:errors path="adminSanctionBy" cssClass="add-margin error-msg" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="panel-heading">
		<div class="panel-title"><spring:message code="lbl.techsanctiondetails" /></div>
	</div>
	<input type="hidden" id="errorTechDate" value="<spring:message code='error.technicalsanctiondate' />" />
	<input type="hidden" id="errorActualAmount" value="<spring:message code='error.actualamount' />" />
	<input type="hidden" id="errorGrossBilledAmount" value="<spring:message code='error.grossbilledamount' />" />
	<input type="hidden" id="errorActualAmountContinued" value="<spring:message code='error.actualamount.continued' />" />
	<input type="hidden" id="adminSanctionDate" class="form-control datepicker" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" value='<fmt:formatDate value="${lineEstimate.adminSanctionDate }" pattern="dd/MM/yyyy"/>' />
	<div class="panel-body">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th><spring:message code="lbl.technicalsanctionnumber"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.technicalsanctiondate"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.technical.authority"/><span class="mandatory"></span></th>
					<th><spring:message code="lbl.authority"/></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<form:input path="technicalSanctionNumber" id="technicalSanctionNumber" onkeyup="alphanumerichyphenbackslash(this);" data-errormsg="Technical Sanction Number of the work is mandatory!" data-idx="0" data-optional="0" class="form-control table-input" maxlength="32" required="required" />
						<form:errors path="technicalSanctionNumber" cssClass="add-margin error-msg" />
					</td>
					<td>
						<form:input path="technicalSanctionDate" id="technicalSanctionDate" data-errormsg="Technical Sanction Date of the work is mandatory!" data-idx="0" data-optional="0" class="form-control datepicker" maxlength="10" data-inputmask="'mask': 'd/m/y'" data-date-end-date="0d" required="required" />
						<form:errors path="technicalSanctionDate" cssClass="add-margin error-msg" />
					</td>
					<td>
						<input type="hidden" id="designationValue" value="${designation }" />
						<select name="designation" id="designation" data-first-option="false" class="form-control" required="required">
							<option value="">
								<spring:message code="lbl.select" />
							</option>
							<options items="${designations}" itemValue="id" itemLabel="name" />
							<c:forEach items="${designations}" var="designation">
								<option value="${designation.id }">${designation.name }</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<input type="hidden" id="authorityValue" value="${lineEstimate.technicalSanctionBy.id }" />
						<form:select path="technicalSanctionBy" id="authority" data-first-option="false" class="form-control" >
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
						</form:select>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>