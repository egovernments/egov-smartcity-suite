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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="panel-heading ">
	<div class="panel-title" style="font-weight: bold">Assigned
		Department</div>
</div>
<table class="table table-striped table-bordered" id="caffidavitdetails">
	<thead>
		<tr>
			<th class="text-center"><spring:message code="lbl.isprimarydept" /></th>
			<th class="text-center"><spring:message code="lbl.department" /><span class="mandatory"></span></th>
			<th class="text-center"><spring:message code="lbl.assignto" /><span class="mandatory"></span></th>
			<th class="text-center"><spring:message code="lbl.dateofreceptofpwr" /><span
				class="mandatory"></span></th>
		</tr>
	</thead>
	<tbody>
		<tr class="">
			<td class="text-center"><form:checkbox
					path="legalCaseDepartment[0].isPrimaryDepartment"
					name="legalCaseDepartment[0].isPrimaryDepartment"
					id="legalCaseDepartment[0].isPrimaryDepartment" /></td>

			<td class="text-center"><form:input id="departmentName"
					type="text" class="form-control " autocomplete="off"
					path="legalCaseDepartment[0].department.name"
					name="legalCaseDepartment[0].department.name"
					value="${legalCaseDepartment[0].department.name}" required="required" />
				<input type="hidden" id="departmentId" value="" /> <c:forEach
					items="${departments}" var="department">
					<a onclick="setDepartmentId(<c:out value="${department.id}"/>)"
						href="javascript:void(0)"
						class="btn btn-secondary btn-xs tag-element freq-ct"><c:out
							value="${department.name }" /> </a>
				</c:forEach></td>

			<td class="text-right"><form:input id="positionName" type="text"
					class="form-control " autocomplete="off" required="required"
					path="legalCaseDepartment[0].position.name"
					name="legalCaseDepartment[0].position.name"
					value="${legalCaseDepartment[0].position.name}"/>
				<input type="hidden" id="positionId" value="" /> <c:forEach
					items="${departments}" var="position">
					<a onclick="setPositionId(<c:out value="${position.id}"/>)"
						href="javascript:void(0)"
						class="btn btn-secondary btn-xs tag-element freq-ct"><c:out
							value="${position.name }" /> </a>
				</c:forEach></td>
			<td class="text-right"><form:input type="text"
					name="legalCaseDepartment[0].dateofreceiptofpwr"
					path="legalCaseDepartment[0].dateofreceiptofpwr"
					class="form-control datepicker" data-date-end-date="0d"
					id="legalCaseDepartment[0].dateofreceiptofpwr"
					data-inputmask="'mask': 'd/m/y'"  required="required"/></td>

		</tr>


	</tbody>
</table>