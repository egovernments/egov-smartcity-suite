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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.contractordetails" />
		</div>
	</div>
	<input type="hidden" value="${contractor.tempContractorDetails.size() }"
		id="detailsSize" />
	<div class="panel-body">
		<div class="col-sm-12 text-right ">
			<button id="addRowBtn" type="button" class="btn btn-primary "
				onclick="addContractorDetails()">
				<spring:message code="lbl.addcontractordetail" />
			</button>
		</div>
		<div class="col-sm-12 text-right "></div>
		<table class="table table-bordered" id="tblContractorDetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th></label><spring:message code="lbl.department" /><label for="department"></label><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.registrationno" /><label for="registrationNumber"></label></th>
					<th><spring:message code="lbl.category" /><label for="category"></label></th>
					<th><spring:message code="lbl.contractorclass" /><label for="grade"></label></th>
					<th><spring:message code="lbl.status" /><label for="status"></label><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.fromdate" /><label for="fromdate"></label><span
						class="mandatory"></span></th>
					<th><spring:message code="lbl.todate" /><label for="todate"></label></th>
					<th><spring:message code="lbl.action" /></th>
				</tr>
			</thead>
			<tbody id="contractorDetailsTbl">
				<c:choose>
					<c:when test="${contractor.tempContractorDetails.size() == 0}"> 
						<tr id="contractorDetailRow">
							<td><span class="spansno">1</span> <form:hidden
									path="tempContractorDetails[0].id" name="tempContractorDetails[0].id"
									value="${tempContractorDetails[0].id}"
									class="form-control table-input hidden-input" /></td>
							<td><form:select path="tempContractorDetails[0].department"
									id="department"
									name="tempContractorDetails[0].department" data-idx="0"
									data-optional="1"
									data-errormsg="Department is mandatory!"
									class="form-control table-input department removeDefaultValues patternvalidation" required = "required">
									<form:option value="" > <spring:message code="lbl.select" /></form:option>
						           	<form:options items="${departmentList}" itemLabel="name" itemValue="id" />
						        </form:select>
								<form:errors path="tempContractorDetails[0].department"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempContractorDetails[0].registrationNumber"
									name="tempContractorDetails[0].registrationNumber"
									id="registrationNumber" data-idx="0"
									data-optional="1"
									maxlength="50"
									class="form-control table-input registrationNumber removeDefaultValues patternvalidation" data-pattern="alphanumerichyphenbackslash" />
								<form:errors path="tempContractorDetails[0].registrationNumber"
									cssClass="add-margin error-msg" /></td>
							<td><form:select path="tempContractorDetails[0].category" name="tempContractorDetails[0].category" 
									class="form-control table-input patternvalidation category" 
									data-pattern="alphanumericwithallspecialcharacters" 
									maxlength = "100" >
									<form:option value="" > <spring:message code="lbl.select" /></form:option>
									<c:forEach items = "${contractorDetailsCategoryValues }" var = "category">
										<form:option value="${category}">${category}</form:option>
									</c:forEach>
						        </form:select>
								<form:errors path="tempContractorDetails[0].category" 
									cssClass="add-margin error-msg" /></td>
							<td><form:select path="tempContractorDetails[0].grade" name="tempContractorDetails[0].grade" 
									class="form-control table-input patternvalidation grade" 
									data-pattern="alphanumericwithallspecialcharacters" 
									maxlength = "100" >
									<form:option value="" > <spring:message code="lbl.select" /></form:option>
						           <form:options items="${contractorClassList}" itemLabel="grade" itemValue="id" />
						        </form:select>
								<form:errors path="tempContractorDetails[0].grade" 
									cssClass="add-margin error-msg" /></td>
							<td><form:select path="tempContractorDetails[0].status" name="tempContractorDetails[0].status"
									class="form-control table-input patternvalidation status" 
									data-optional="0"
									data-errormsg="Status is mandatory!"
									data-pattern="alphanumericwithallspecialcharacters" 
									maxlength = "100" required = "required" >
						           <form:options items="${statusList}" itemLabel="code" itemValue="id" />
						        </form:select>
								<form:errors path="tempContractorDetails[0].status" 
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempContractorDetails[0].validity.startDate"
									name="tempContractorDetails[0].validity.startDate" data-errormsg="From Date is mandatory!"
									data-idx="0" data-optional="0"
									class="form-control datepicker fromdate" maxlength="10"
									data-date-format="dd/mm/yyyy" data-inputmask="'mask': 'd/m/y'"
									required="required" /> <form:errors
									path="tempContractorDetails[0].validity.startDate"
									cssClass="add-margin error-msg" /></td>
							<td><form:input path="tempContractorDetails[0].validity.endDate"
									name="tempContractorDetails[0].validity.endDate" data-date-format="dd/mm/yyyy" data-idx="0"
									data-optional="1" class="form-control datepicker todate"
									maxlength="10" data-inputmask="'mask': 'd/m/y'" /> <form:errors
									path="tempContractorDetails[0].validity.endDate"
									cssClass="add-margin error-msg" /></td>
							<td><span class="add-padding"
								onclick="deleteContractorDetail(this);"><i class="fa fa-trash"
									data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						 <c:forEach items="${contractor.tempContractorDetails}" var="contractorDtls"
							varStatus="item">
							<tr id="contractorRow">
								<td><span class="spansno"><c:out
										value="${item.index + 1}" /></span> <form:hidden
										path="tempContractorDetails[${item.index}].id"
										name="tempContractorDetails[${item.index}].id"
										value="${contractorDtls.id}"
										class="form-control table-input hidden-input" /></td>
								<td><form:select
										path="tempContractorDetails[${item.index}].department"
										id="department"
										name="tempContractorDetails[${item.index}].department"
										data-idx="${item.index}"
										data-optional="1"
										data-errormsg="Department is mandatory!"
										class="form-control table-input department removeDefaultValues patternvalidation" 
										maxlength="1024" > 
										<form:option value="" > <spring:message code="lbl.select" /></form:option>
						           		<form:options items="${departmentList}" itemLabel="name" itemValue="id" />
						           	</form:select>
									<form:errors
										path="tempContractorDetails[${item.index}].department"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="tempContractorDetails[${item.index}].registrationNumber"
										name="tempContractorDetails[${item.index}].registrationNumber" id="registrationNumber"
										data-idx="${item.index}"  data-optional="1" data-pattern="alphanumerichyphenbackslash"
										maxlength="50"
										class="form-control table-input registrationNumber removeDefaultValues patternvalidation" />
									<form:errors path="tempContractorDetails[${item.index}].registrationNumber"
										cssClass="add-margin error-msg" /></td>
								<td><form:select
										path="tempContractorDetails[${item.index}].category"
										name="tempContractorDetails[${item.index}].category"
										data-idx="${item.index}"
										class="form-control table-input category removeDefaultValues"
										maxlength="100" > 
										<form:option value="" > <spring:message code="lbl.select" /></form:option>
										<c:forEach items = "${contractorDetailsCategoryValues }" var = "category">
											<form:option value="${category}">${category}</form:option>
										</c:forEach>
							        </form:select>
									<form:errors
										path="tempContractorDetails[${item.index}].category"
										cssClass="add-margin error-msg" /></td>
								<td><form:select
										path="tempContractorDetails[${item.index}].grade"
										name="tempContractorDetails[${item.index}].grade"
										data-idx="${item.index}"
										class="form-control table-input grade removeDefaultValues"
										maxlength="1024" > 
										<form:option value="" > <spring:message code="lbl.select" /></form:option>
							           <form:options items="${contractorClassList}" itemLabel="grade" itemValue="id" />
							        </form:select>
									<form:errors
										path="tempContractorDetails[${item.index}].grade"
										cssClass="add-margin error-msg" /></td>
								<td><form:select
										path="tempContractorDetails[${item.index}].status"
										name="tempContractorDetails[${item.index}].status"
										data-idx="${item.index}"
										data-optional="0"
										data-errormsg="Status is mandatory!"
										class="form-control table-input status removeDefaultValues"
										maxlength="1024" required = "required" > 
										<form:options items="${statusList}" itemLabel="code" itemValue="id" />
						        	</form:select>
									<form:errors
										path="tempContractorDetails[${item.index}].status"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="tempContractorDetails[${item.index}].validity.startDate"
										name="startDate" data-errormsg="From Date is mandatory!"
										data-idx="${item.index}" data-optional="0"
										class="form-control datepicker fromdate" maxlength="10"
										data-date-format="dd/mm/yyyy" data-inputmask="'mask': 'd/m/y'"
										required="required" /> <form:errors
										path="tempContractorDetails[${item.index}].validity.startDate"
										cssClass="add-margin error-msg" /></td>
								<td><form:input
										path="tempContractorDetails[${item.index}].validity.endDate"
										name="endDate" data-date-format="dd/mm/yyyy" data-idx="${item.index}"
										data-optional="1" class="form-control datepicker todate"
										maxlength="10" data-inputmask="'mask': 'd/m/y'" /> <form:errors
										path="tempContractorDetails[${item.index}].validity.endDate"
										cssClass="add-margin error-msg" /></td>
								<td><span class="add-padding"
									onclick="deleteContractorDetail(this);"><i class="fa fa-trash"
										data-toggle="tooltip" title="" data-original-title="Delete!"></i></span>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>
