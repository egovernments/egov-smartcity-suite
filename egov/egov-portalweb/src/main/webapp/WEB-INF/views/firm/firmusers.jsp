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
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.firmusers" />
		</div>
	</div>
	<input type="hidden" value="${firm.tempFirmUsers.size() }"
		id="detailsSize" />
	<div class="panel-body">
		<div class="col-sm-12 text-right ">
			<button id="addRowBtn" type="button" class="btn btn-primary "
				onclick="addFirmUsers()">
				<spring:message code="lbl.addrow" />
			</button>
		</div>
		<div class="col-sm-12 text-right "></div>
		<table class="table table-bordered" id="tblfirm">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.name" /><span	class="mandatory"></span></th>
					<th><spring:message code="lbl.mobileNo" /><span	class="mandatory"></span></th>
					<th><spring:message code="lbl.emailid" /><span	class="mandatory"></span></th>
					<th><spring:message code="lbl.actions" /></th>
				</tr>
			</thead>
			<tbody id="firmDetailsTbl">
				<c:choose>
					<c:when test="${firm.tempFirmUsers.size() == 0}">
						<tr id="firmRow">
							<td><span class="spansno">1</span> <form:hidden
									path="tempFirmUsers[0].id" name="tempFirmUsers[0].id"
									value="${tempFirmUsers[0].id}"
									class="form-control table-input hidden-input" /></td>
							<td><form:input path="tempFirmUsers[0].name"
									name="tempFirmUsers[0].name" data-idx="0"
									data-optional="1"
									class="form-control table-input text-left patternvalidation"
									data-pattern="decimalvalue" required="required" /> <form:errors
									path="tempFirmUsers[0].name" maxlength="100"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempFirmUsers[0].mobileNumber"
									name="tempFirmUsers[0].mobileNumber" data-idx="0"
									data-optional="1" onblur="validateMobileNumber(this);"
									class="form-control table-input text-left mobileNumber is_valid_number patternvalidation"
									data-pattern="decimalvalue" required="required" maxlength="15" /> <form:errors
									path="tempFirmUsers[0].mobileNumber"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempFirmUsers[0].emailId"
									name="tempFirmUsers[0].emailId" data-idx="0"
									data-optional="1" onblur="validateEmailId(this);" maxlength="100"
									class="form-control table-input text-left emailId patternvalidation"
									data-pattern="decimalvalue"  required="required" /> <form:errors
									path="tempFirmUsers[0].emailId" cssClass="add-margin error-msg" /></td>
							<td>
								<div class="text-left">
									<button type="button"
										onclick="deleteFirmUsers(this);"
										class="btn btn-xs btn-danger delete-row" data-optional="0">
										<span class="glyphicon glyphicon-trash"></span> Delete
									</button>
								</div>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${firm.tempFirmUsers}" var="firmUsers"
							varStatus="item">
							<tr id="firmRow">
								<td><span class="spansno"><c:out
											value="${item.index + 1}" /></span> <form:hidden
										path="tempFirmUsers[${item.index}].id"
										name="tempFirmUsers[${item.index}].id" value="${firmUsers.id}"
										class="form-control table-input hidden-input " /></td>
								<td><form:input path="tempFirmUsers[${item.index}].name"
									name="tempFirmUsers[${item.index}].name" data-idx="0"
									data-optional="1" maxlength="100"
									class="form-control table-input text-left patternvalidation"
									data-pattern="decimalvalue" required="required" /> <form:errors
									path="tempFirmUsers[${item.index}].name"
									cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="tempFirmUsers[${item.index}].mobileNumber"
										name="tempFirmUsers[${item.index}].mobileNumber" maxlength="15" data-idx="0"
										data-optional="1" onblur="validateMobileNumber(this);"
										class="form-control table-input text-left mobileNumber is_valid_number patternvalidation"
										data-pattern="decimalvalue" required="required" /> <form:errors
										path="tempFirmUsers[0].mobileNumber"
										cssClass="add-margin error-msg" /></td>
								<td><form:input path="tempFirmUsers[${item.index}].emailId"
										name="tempFirmUsers[${item.index}].emailId"
										data-idx="0" data-optional="1" maxlength="100"
										class="form-control table-input text-left emailId patternvalidation"
										data-pattern="decimalvalue" required="required" /> <form:errors
										path="tempFirmUsers[${item.index}].emailId"
										onblur="validateEmailId(this);"
										cssClass="add-margin error-msg" /></td>

								<td>
								<div class="text-left">
									<button type="button"
										onclick="deleteFirmUsers(this);"
										class="btn btn-xs btn-danger delete-row" data-optional="0">
										<span class="glyphicon glyphicon-trash"></span> Delete
									</button>
								</div>
							</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>