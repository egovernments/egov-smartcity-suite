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

<input type="hidden" name="username" value="${stakeHolder.username}">
<input type="hidden" name="password" value="${stakeHolder.password}">
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.applicant.name" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="name"
			class="form-control text-left patternvalidation"
			data-pattern="alphanumeric" maxlength="100" required="required" />
		<form:errors path="name" cssClass="error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.code" /></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control" path="code"
			id="code"/>
		<form:errors path="code" cssClass="error-msg" />
	</div>
</div>

<div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.stakeholder.type" /> <span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="stakeHolderType" id="stakeHolderType" required="required"
			cssClass="form-control" cssErrorClass="form-control error">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${stakeHolderTypes}" />
		</form:select>
		<form:errors path="stakeHolderType" cssClass="error-msg" />
	</div>
<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.isActive" /></label>
	<div class="col-sm-3 add-margin">
		<form:radiobutton path="isActive" value="true" checked="checked" /> <spring:message code="lbl.yes" />
		<form:radiobutton path="isActive" value="false" /> <spring:message code="lbl.no" />
		<form:errors path="isActive" cssClass="error-msg" />
	</div>	
</div>

<div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.gender" /> <span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="gender" id="gender" required="required"
			cssClass="form-control" cssErrorClass="form-control error">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${genderList}" />
		</form:select>
		<form:errors path="gender" cssClass="error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.dob" /> <span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control datepicker" path="dob"
			id="birthDate" data-date-end-date="-18y" required="required" />
		<form:errors path="dob" cssClass="error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.mobileNo" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control patternvalidation mobileno-field"
			data-pattern="number" data-inputmask="'mask': '9999999999'"
			placeholder="Mobile Number" path="mobileNumber" minlength="10"
			maxlength="10" id="mobileNumber" required="required" />
		<form:errors path="mobileNumber" cssClass="error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.emailid" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control patternvalidation"
			data-pattern="regexp_alphabetspecialcharacters" path="emailId"
			id="emailId" maxlength="128" required="required" /><span class=""></span>
		<form:errors path="emailId" cssClass="error-msg" />
	</div>
</div>

<%-- <div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.user.id" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control" path="username"
			id="userId" required="required" />
		<form:errors path="username" cssClass="error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.pwd" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control" path="password"
			id="password" required="required" />
		<form:errors path="password" cssClass="error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.verify.pwd" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control" path="" id="verifyPwd"
			required="required" />
		<form:errors path="" cssClass="error-msg" />
	</div>
</div> --%>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.business.lic.no" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control"
			path="businessLicenceNumber" maxlength="64" id="businessLicenceNumber" required="required" />
		<form:errors path="businessLicenceNumber" cssClass="error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.business.lic.due" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control datepicker"
			path="businessLicenceDueDate" id="businessLicenceDueDate" required="required" />
		<form:errors path="businessLicenceDueDate" cssClass="error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.coa.enrol.no" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control"
			path="coaEnrolmentNumber" maxlength="64" id="coaEnrolmentNo" required="required" />
		<form:errors path="coaEnrolmentNumber" cssClass="error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.coa.renew.date" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control datepicker"
			path="coaEnrolmentDueDate" id="coaRenewalDueDate" required="required" />
		<form:errors path="coaEnrolmentDueDate" cssClass="error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"> <spring:message code="lbl.enrol.with.local.body" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:radiobutton path="isEnrolWithLocalBody" value="true" checked="checked" /> <spring:message code="lbl.yes" />
		<form:radiobutton path="isEnrolWithLocalBody" value="false" /> <spring:message code="lbl.no" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.tin.no" /></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control" maxlength="11" path="tinNumber"
			id="tinNumber" />
		<form:errors path="tinNumber" cssClass="error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.aadhar" /></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control patternvalidation"
			path="aadhaarNumber" data-pattern="number" maxlength="12" id="aadhaarNumber" />
		<form:errors path="aadhaarNumber"  cssClass="error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-left">(or)<span class="mandatory"></span></label>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.pan" /></label>
	<div class="col-sm-3 add-margin">
		<form:input type="text" cssClass="form-control" path="pan"
			id="panNumber" maxlength="10"/>
		<form:errors path="pan" cssClass="error-msg" />
	</div>
</div>

<c:set value="correspondenceAddress" var="address" scope="request"></c:set>

<form:hidden path="correspondenceAddress.user" value="${stakeHolder.id}" />
<%-- <form:hidden path="address[0].type" value="PRESENTADDRESS" /> --%>
<jsp:include page="address-info.jsp">
	<jsp:param value="lbl.comm.address" name="subhead" />
</jsp:include>
<div class="form-group">
<div class="col-sm-6">
<label class="col-sm-12 control-label text-right">
<input type="checkbox" id="isAddressSame" > is communication address same as permanent address ?
</label>
</div>
</div>
<c:set value="permanentAddress" var="address" scope="request"></c:set>
<form:hidden path="permanentAddress.user" value="${stakeHolder.id}" />
<%-- <form:hidden path="address[1].type" value="PERMANENT" /> --%>
<jsp:include page="address-info.jsp">
	<jsp:param value="lbl.permt.address" name="subhead" />
</jsp:include>




<div class="form-group">
	<label class="col-sm-3 control-label text-right"> <spring:message code="lbl.behalf.org" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:radiobutton path="isOnbehalfOfOrganization" class="isOnbehalfOfOrganization" value="true" /> <spring:message code="lbl.yes" />
		<form:radiobutton path="isOnbehalfOfOrganization" class="isOnbehalfOfOrganization" value="false" checked="checked" /> <spring:message code="lbl.no" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If Yes please specify
	</div>
</div>
<div id="showhide" class="hide">
	<div class="form-group">
		<label class="col-sm-3 control-label text-right toggle-madatory"><spring:message
				code="lbl.nameof.org" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<form:input type="text" cssClass="form-control addremoverequired"
				path="organizationName" maxlength="128" id="organizationName" />
			<form:errors path="organizationName" cssClass="error-msg" />
		</div>
		<label class="col-sm-2 control-label text-right toggle-madatory"><spring:message
				code="lbl.contactNo" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<form:input type="text" cssClass="form-control patternvalidation addremoverequired"
				path="organizationMobNo" data-pattern="number" maxlength="11" id="organizationMobNo" />
			<form:errors path="organizationMobNo" cssClass="error-msg" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label text-right toggle-madatory"><spring:message
				code="lbl.addressof.org" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<form:textarea path="organizationAddress" id="organizationAddress"
				type="text" class="form-control low-width patternvalidation addremoverequired"
				data-pattern="regexp_alphabetspecialcharacters" maxlength="128"
				placeholder="" autocomplete="off" />
			<form:errors path="organizationAddress" cssClass="error-msg" />
		</div>
		<label class="col-sm-2 control-label text-right toggle-madatory"><spring:message
				code="lbl.org.url" /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<form:input type="text" cssClass="form-control addremoverequired"
				path="organizationUrl" maxlength="64" id="organizationUrl" />
			<form:errors path="organizationUrl" cssClass="error-msg" />
		</div>
	</div>
</div>
