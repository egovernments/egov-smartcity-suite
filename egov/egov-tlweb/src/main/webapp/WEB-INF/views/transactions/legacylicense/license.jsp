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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<script>
    $(document).ready(function () {

      /*   <c:if test="%{hasErrors()}"> 
        if ($('#subCategory').val() != '-1') {
            getUom();
        }
     		  </c:if> */ 
     		  
    });

</script>
<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code='license.tradedetail' />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code='search.license.establishmentname' /> <span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="nameOfEstablishment" id="nameOfEstablishment"
			value="${nameOfEstablishment}" class="form-control newpatternvalidation"
			maxlength="250" onBlur="checkLength(this,250)" required="true" />
		<form:errors path="nameOfEstablishment" cssClass="error-msg" />
	</div>

	<label class="col-sm-2 control-label text-right"><spring:message
			code='license.tradeType.lbl' /> <span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="natureOfBusiness" id="natureOfBusiness"
			cssClass="form-control" ErrorClass="form-control error"
			required="true">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${natureOfBusiness}" itemValue="id"
				itemLabel="name" />
		</form:select>
		<form:errors path="nameOfEstablishment" cssClass="error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code='license.category.lbl' /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:select path="category" id="category" cssClass="form-control "
			required="true">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${category}" itemValue="id" itemLabel="name" />
		</form:select>
		<form:errors path="category" cssClass="error-msg tradelicenceerror" />
	</div>

	<label class="col-sm-2 control-label text-right"><spring:message
			code='license.subCategory.lbl' /><span class="mandatory"></span></label>${tradeName}
	<div class="col-sm-3 add-margin">
		<form:select path="tradeName" id="subCategory"
			Class="form-control select2" ErrorClass="form-control error"
			required="true" data-selected-id="${tradeLicense.tradeName.id}" data-selected-text="${tradeLicense.tradeName.name}">
			<form:option value="">
				<spring:message code="lbl.select" />
			</form:option>
			<form:options items="${tradeName}" itemValue="id" itemLabel="name"/>
		</form:select>
		<form:errors path="tradeName" cssClass="error-msg tradelicenceerror"/>
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code='license.uom.lbl' /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="tradeName.licenseSubCategoryDetails[0].uom.name"
			id="uom" value="${tradeName.licenseSubCategoryDetails[0].uom.name}"
			required="true" readOnly="true" class="form-control" />
		<form:errors path="tradeName.licenseSubCategoryDetails[0].uom.name"
			cssClass="error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code='license.premises.lbl' /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="tradeArea_weight" id="tradeArea_weight"
			value="${tradeArea_weight}" maxlength="8"
			class="form-control patternvalidation" required="true"
			data-pattern="number" />
		<form:errors path="tradeArea_weight" cssClass="error-msg tradelicenceerror" />
	</div>
</div>

<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message
				code='license.remarks' /></label>
		<div class="col-sm-3 add-margin">
			<form:input path="remarks" id="remarks" value="${remarks}"
				maxlength="250" class="form-control" />
			<form:errors path="remarks" cssClass="error-msg" />
		</div>
	
		<label class="col-sm-2 control-label text-right">
			<spring:message code='license.startdate' /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3 add-margin">
			<fmt:formatDate type="date" value="${commencementDate}"
				pattern="dd/MM/yyyy" var="commencementDateFrmttd" />
			<form:input path="commencementDate" id="startDate"
				value="${commencementDateFrmttd}" 
				format="dd/MM/yyyy"
				class="form-control datepicker"
				data-date-end-date="0d" required="true" maxlength="10" />
			<form:errors path="commencementDate" cssClass="error-msg" />
		</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right">
		<spring:message code='license.traderCheckbox.lbl' />
	</label>
	<div class="col-sm-3 add-margin">
	     <c:if test="${tradeLicense.agreementDate !=null}">
			<c:set value="${true}" var="showdetail"/>
			</c:if>
		<input type="checkbox" name="" onclick="showHideAgreement()"
			id="showAgreementDtl" theme="simple" <c:if test="${showdetail}">
			checked="checked"</c:if>
			/>
	</div>
</div>

<div id="agreementSec" style="display: none">
	<div class="panel-heading custom_form_panel_heading">
		<div class="panel-title">
			<spring:message code='license.AgreementDetails.lbl' />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label text-right"><spring:message
				code='license.agreementDate.lbl' /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3 add-margin">
			<fmt:formatDate type="date" value="${trdaeLicense.agreementDate}"
				pattern="dd/MM/yyyy" var="agreementDateFrmttd" />
			<form:input path="agreementDate" id="agreementDate"
				value="${agreementDateFrmttd}" 
				Class="form-control datepicker" maxlength="10" />
			<form:errors path="agreementDate" cssClass="error-msg" />
		</div>

		<label class="col-sm-2 control-label text-right"><spring:message
				code='license.agreementDocNo.lbl' /><span class="mandatory"></span></label>
		<div class="col-sm-3 add-margin">
			<form:input path="agreementDocNo" id="agreementDocNo"
				value="${tradeLicense.agreementDocNo}" maxlength="50"
				Class="form-control patternvalidation"
				data-pattern="alphanumerichyphenbackslash" />
			<form:errors path="agreementDocNo" cssClass="error-msg" />
		</div>
	</div>
</div>
