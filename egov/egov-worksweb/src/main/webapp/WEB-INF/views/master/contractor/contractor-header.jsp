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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<c:choose>
			<c:when test="${mode != 'edit' }">
				<div class="panel-title" style="text-align:center;" ><spring:message code="lbl.createcontractor" /></div>
			</c:when>
			<c:otherwise>
				<div class="panel-title" style="text-align:center;" ><spring:message code="lbl.modifycontractor" /></div>
			</c:otherwise>
		</c:choose>
	</div>
	<c:if test="${mode == 'edit' }">
		<input type="hidden" value="${contractor.id }"  name="contractor" />
	</c:if>
	<input type="hidden" value="<spring:message code='error.row.delete' />" id = "rowDeleteErrorMessage" />
	<input type="hidden" value="${defaultDepartmentId}" id="defaultDepartmentId"/>
	<input type="hidden" value="${contractorMasterMandatoryFields }" id="mandatory" />
	<input type="hidden" value="${contractorMasterHiddenFields }" id="hide" />
	<input type="hidden" value="${codeAutoGeneration }" id="codeautogeneration" />
	
	<input type="hidden" id="emailInvalidErrorMsg" value="<spring:message code='error.contractor.email.invalid' />" />
	<input type="hidden" id="bankAccountAlphaNeumericErrorMsg" value="<spring:message code='error.contractor.bankaccount.alphanumeric' />" />
	<input type="hidden" id="ifscCodeAlphaNeumericErrorMsg" value="<spring:message code='error.contractor.ifsccode.alphanumeric' />" />
	<input type="hidden" id="tinNumberAlphaNeumericErrorMsg" value="<spring:message code='error.contractor.tinnumber.alphanumeric' />" />
	<input type="hidden" id="codeAlphaNeumericErrorMsg" value="<spring:message code='error.contractor.code.alphaNumeric' />" />
	<input type="hidden" id="nameAlphaNeumericErrorMsg" value="<spring:message code='error.contractor.name.alphaNumeric' />" />
	<input type="hidden" id="registrationNumberErrorMsg" value="<spring:message code='error.contractordetail.registrationnumber.required' />" />
	<input type="hidden" id="departmentErrorMsg" value="<spring:message code='error.contractordetail.department' />" />
	<input type="hidden" id="contractorClassErrorMsg" value="<spring:message code='error.contractordetail.grade.required' />" />
	<input type="hidden" id="startDateErrorMsg" value="<spring:message code='error.contractordetail.fromdate' />" />
	<input type="hidden" id="mobileNumberErrorMsg" value="<spring:message code='error.contractor.mobileno.length' />" />
	<input type="hidden" id="contactPersonAlphaNeumericErrorMsg" value="<spring:message code='error.contractor.contactperson.alphanumeric' />" />
	<input type="hidden" id="registrationNumberAlphaNeumericErrorMsg" value="<spring:message code='error.contractordetail.registrationnumber.alphanumeric' />" />
	<input type="hidden" id="vaidateError" value="<spring:message code='sor.validate_x.message' />" />
	<input type="hidden" id="contractorCategoryErrorMsg" value="<spring:message code='error.contractordetail.category.required' />" />
	<input type="hidden" id="nameErrorLengthMsg" value="<spring:message code='error.contractor.name.length' />" />
	<input type="hidden" id="pwdApprovalCodeAlphaNeumericErrorMsg" value="<spring:message code='error.contractor.pwdapprovalcode.alphanumeric' />" />
	<input type="hidden" id="contractorDetailErrorMsg" value="<spring:message code='error.contractor.details.altleastone_details_needed' />" />
	<input type="hidden" id='panNumberMessage' value="<spring:message code='error.contractor.pannumber.alphanumeric' />" >
	<input type="hidden" id='fromDateGreaterThanEndDateError' value="<spring:message code='error.fromdate.cannot.be.grater.then.todate' />" >
	
	<div class="panel-body">
		<div class="form-group">
			<label for="code" class="col-sm-3 control-label text-right"><spring:message code="lbl.code" />
				<c:if test="${mode != 'edit' && codeAutoGeneration != 'Yes'}">
					<span class="mandatory"></span>
				</c:if>
			</label>
			<div class="col-sm-3 add-margin">
				<c:choose>
					<c:when test="${mode == 'edit' && codeAutoGeneration == 'Yes'}">
						<c:out value = "${contractor.code }" />
						<form:hidden path="code" value = "${contractor.code }"/>
					</c:when>
					<c:otherwise>
						<form:input path="code" id="contractor_code" class="form-control table-input patternvalidation" data-pattern="alphanumerichyphenbackslash" maxlength = "50" required = "required"/>
		       	 		<form:errors path="code" cssClass="error" />
					</c:otherwise>
				</c:choose>
			</div>
			<label for="name" class="col-sm-2 control-label text-right"><spring:message code="lbl.name" /><span
						class="mandatory"></span></label>
			<div class="col-sm-3 add-margin">
				<form:input path="name" id="name" class="form-control table-input patternvalidation" data-pattern="alphabetwithspace" maxlength = "100" required = "required"/>
		        <form:errors path="name" cssClass="error" />
			</div>
		</div>
		<div class="form-group">
			<label for="correspondenceAddress" class="col-sm-3 control-label text-right"><spring:message code="lbl.correspondenceaddress" /></label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="correspondenceAddress" id="correspondenceAddress" class="form-control table-input textfieldsvalidate"  maxlength = "250"/>
				<form:errors path="correspondenceAddress" cssClass="add-margin error-msg" />
			</div>
			<label for="paymentAddress" class="col-sm-2 control-label text-right"><spring:message code="lbl.paymentaddress" /></label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="paymentAddress" id="paymentAddress" class="form-control table-input textfieldsvalidate"  maxlength = "250" />
		        <form:errors path="paymentAddress" cssClass="error" />
			</div>
		</div>
		<div class="form-group">
			<label for="contactPerson" class="col-sm-3 control-label text-right"><spring:message code="lbl.contactperson" /></label>
			<div class="col-sm-3 add-margin">
				<form:input path="contactPerson" id="contactPerson" class="form-control table-input" maxlength = "100"/>
				<form:errors path="contactPerson" cssClass="add-margin error-msg" />
			</div>
			<label for="email" class="col-sm-2 control-label text-right"><spring:message code="lbl.email" /></label>
			<div class="col-sm-3 add-margin">
				<form:input path="email" id="email" class="form-control table-input" maxlength = "100" />
		        <form:errors path="email" cssClass="error" />
			</div>
		</div>
		<div class="form-group">
			<label for="narration" class="col-sm-3 control-label text-right"><spring:message code="lbl.narration" /></label>
			<div class="col-sm-3 add-margin">
				<form:textarea path="narration" id="narration" class="form-control table-input textfieldsvalidate" maxlength = "1024" />
				<form:errors path="narration" cssClass="add-margin error-msg" />
			</div>
			<label for="mobileNumber" class="col-sm-2 control-label text-right"><spring:message code="lbl.mobilenumber" /></label>
			<div class="col-sm-3 add-margin">
				<form:input path="mobileNumber" id="mobileNumber" class="form-control table-input" maxlength = "10" />
		        <form:errors path="mobileNumber" cssClass="error" />
			</div>
		</div>
		<div class="form-group">
			<label for="panNumber" class="col-sm-3 control-label text-right"><spring:message code="lbl.pannumber" /></label>
			<div class="col-sm-3 add-margin">
				<form:input path="panNumber" id="panNumber" class="form-control table-input" maxlength = "10" />
				<form:errors path="panNumber" cssClass="add-margin error-msg" />
			</div>
			<label for="tinNumber" class="col-sm-2 control-label text-right"><spring:message code="lbl.tinnumber" /></label>
			<div class="col-sm-3 add-margin">
				<form:input path="tinNumber" id="tinNumber" class="form-control table-input" maxlength = "14" />
				<form:errors path="tinNumber" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label for="bank" class="col-sm-3 control-label text-right"><spring:message code="lbl.bank" /></label>
			<div class="col-sm-3 add-margin">
				<form:select  path="bank" id="bank" cssClass="form-control" >
		           <form:option value="" > <spring:message code="lbl.select" /></form:option>
		           <form:options items="${bankList}" itemLabel="name" itemValue="id" />
		        </form:select>
				<form:errors path="bank" cssClass="add-margin error-msg" />
			</div>
			<label for="ifscCode" class="col-sm-2 control-label text-right"><spring:message code="lbl.ifsccode" /></label>
			<div class="col-sm-3 add-margin">
				<form:input path="ifscCode" id="ifscCode" class="form-control table-input" maxlength = "15" />
				<form:errors path="ifscCode" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label for="bankAccount" class="col-sm-3 control-label text-right"><spring:message code="lbl.bankaccount" /></label>
			<div class="col-sm-3 add-margin">
				<form:input path="bankAccount" id="bankAccount" class="form-control table-input" maxlength = "22" />
				<form:errors path="bankAccount" cssClass="add-margin error-msg" />
			</div>
			<label for="pwdApprovalCode" class="col-sm-2 control-label text-right"><spring:message code="lbl.pwdapprovalcode" /></label>
			<div class="col-sm-3 add-margin">
				<form:input path="pwdApprovalCode" id="pwdApprovalCode" class="form-control table-input patternvalidation" data-pattern="alphanumerichyphenbackslash" maxlength = "50" />
				<form:errors path="pwdApprovalCode" cssClass="add-margin error-msg" />
			</div>
		</div>
		<div class="form-group">
			<label for="exemptionForm" class="col-sm-3 control-label text-right"><spring:message code="lbl.exemptionform" /></label>
			<div class="col-sm-3 add-margin">
				<form:select  path="exemptionForm" id="exemptionForm" cssClass="form-control">
		           <form:option value="">
						<spring:message code="lbl.select" />
					</form:option>
					<form:options items="${exemptionFormValues}" />
		        </form:select>
		        <form:errors path="exemptionForm" cssClass="error" />
			</div>
		</div>
	</div>
</div>