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
<input type="hidden"
	value="<spring:message code="error.abstracttechnicalsanctiondate" />"
	id="errorAbstractTechnicalSanctionDate" />
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.techsanctiondetails" />
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th><spring:message code="lbl.technicalsanctionnumber" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.technicalsanctiondate" /><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.technical.authority" /></th>
					<th><spring:message code="lbl.authority" /></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><form:input
							path="estimateTechnicalSanctions[0].technicalSanctionNumber"
							id="technicalSanctionNumber"
							onkeyup="alphanumerichyphenbackslash(this);"
							data-errormsg="Technical Sanction Number of the work is mandatory!"
							data-idx="0" data-optional="1" class="form-control table-input"
							maxlength="32" required="required" /> <form:errors
							path="estimateTechnicalSanctions[0].technicalSanctionNumber"
							cssClass="add-margin error-msg" /></td>
					<td><form:input
							path="estimateTechnicalSanctions[0].technicalSanctionDate"
							id="technicalSanctionDate"
							data-errormsg="Technical Sanction Date of the work is mandatory!"
							data-idx="0" data-optional="1" class="form-control datepicker"
							maxlength="10" data-inputmask="'mask': 'd/m/y'"
							data-date-end-date="0d" required="required" /> <form:errors
							path="estimateTechnicalSanctions[0].technicalSanctionDate"
							cssClass="add-margin error-msg" /></td>
					<td>
						<select name="designation"
							id="designation" data-first-option="false" class="form-control">
								<option value="">
									<spring:message code="lbl.select" />
								</option>
								<options items="${designations}" itemValue="id" itemLabel="name" />
								<c:forEach items="${designations}" var="designation">
									<option value="${designation.id }">${designation.name }</option>
								</c:forEach>
						</select>
						<input type="hidden" id="designationValue"
							value="${designation }" /></td>
					<td><input type="hidden" id="authorityValue"
						value="${technicalSanctionBy}" /> <form:select
							path="estimateTechnicalSanctions[0].technicalSanctionBy"
							id="authority" data-first-option="false" class="form-control"
							>
							<form:option value="">
								<spring:message code="lbl.select" />
							</form:option>
						</form:select></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>