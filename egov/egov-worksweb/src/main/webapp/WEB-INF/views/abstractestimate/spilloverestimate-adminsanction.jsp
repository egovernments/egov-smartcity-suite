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
<body onload="resetFormOnSubmit();">
	<input type="hidden"
		value="<spring:message code="error.abstractadminsanctiondate" />"
		id="errorAbstractAdminSanctionDate" />
	<input type="hidden"
		value="<spring:message code="error.abstractestimatedate.null" />"
		id="errorAbstractEstimateDate" />
	<input type="hidden" id="adminSanctionDateLE"
		value='<fmt:formatDate value="${abstractEstimate.lineEstimateDetails.lineEstimate.adminSanctionDate}"
		pattern="yyyy-MM-dd" />' />
	<input type="hidden"
		value="<spring:message code="error.abstractadminsanctiondatele" />"
		id="errorAbstractLEAdminSanctionDate" />
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title">
				<spring:message code="lbl.sanctiondetails" />
			</div>
		</div>
		<div class="panel-body">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th><spring:message code="lbl.adminsanctiondate" /><span
							class="mandatory"></span></th>
						<th><spring:message code="lbl.adminsanctionauthority" /><span
							class="mandatory"></span></th>
						<th><spring:message code="lbl.authority" /><span
							class="mandatory"></span></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><form:input path="approvedDate" id="approvedDate"
								data-errormsg="Admin Sanction Date of the work is mandatory!"
								data-idx="0" data-optional="1" class="form-control datepicker"
								maxlength="10" data-inputmask="'mask': 'd/m/y'"
								data-date-end-date="0d" required="required" /> <form:errors
								path="approvedDate" cssClass="add-margin error-msg" /></td>
						<td><input type="hidden" id="adminSanctionAuthorityValue"
							value="${adminSanctionAuthorityValue}" /> <select
							name="adminSanctionAuthority" id="adminSanctionAuthority"
							data-first-option="false" class="form-control"
							required="required">
								<option value="">
									<spring:message code="lbl.select" />
								</option>
								<options items="${adminSanctionAuthority}" itemValue="id"
									itemLabel="name" />
								<c:forEach items="${adminSanctionAuthority}" var="designation">
									<option value="${designation.id}">${designation.name}</option>
								</c:forEach>
						</select></td>
						<td><input type="hidden" id="authorityValueForAdmin"
							value="${approvedByValue}" /> <form:select path="approvedBy"
								id="adminSanctionDesignation" data-first-option="false"
								class="form-control" required="required">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
							</form:select></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>