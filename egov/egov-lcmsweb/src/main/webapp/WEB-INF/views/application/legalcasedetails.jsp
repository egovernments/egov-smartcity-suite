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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.courttype" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:select path="casetypeMaster" data-first-option="false"
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>

			<form:options items="${courtTypeList}" itemValue="id"
				id="courtTypeDropdown" itemLabel="code" />
		</form:select>
		<form:errors path="casetypeMaster" cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.petitiontype" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:select path="petitiontypeMaster" data-first-option="false"
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>

			<form:options items="${petitiontypeList}" itemValue="id"
				id="courtTypeDropdown" itemLabel="code" />
		</form:select>
		<form:errors path="petitiontypeMaster" cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right">LC Number<span class="mandatory"></span>: </label>
<div class="col-sm-2 add-margin text-center"><form:select path="lcNumberType" data-first-option="false" id="lcNumberType" name="lcNumberType"
			cssClass="form-control" required="required" onChange="checkLCType();">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>
			<form:options items="${lcNumberTypes}" />
		</form:select></div>
		<div class="show-ManualLcNumber">
	<div class="col-sm-2 add-margin text-center">
	<form:input path="lcNumber" class="form-control patternvalidation"
			data-pattern="string" maxlength="50" id="lcNumber" /></div>
		<div class="col-sm-2 add-margin text-center" >
		<form:select path="wpYear" data-first-option="false" cssClass="form-control" id="wpYear">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>
		<form:options items="${wPYearList}" />
		</form:select></div>
		
	</div>
	<form:errors path="lcNumber" cssClass="add-margin error-msg" />
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.caseNumber" /><span class="mandatory"></span>:</label>
	
	<div class="col-sm-2 add-margin text-center"><form:input class="form-control patternvalidation"
			data-pattern="string" maxlength="50" id="casenumber" required="required"
			path="casenumber" /></div>
	<div class="col-sm-2 add-margin text-center">
		<form:select path="wpYear" data-first-option="false" cssClass="form-control">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>
		<form:options items="${wPYearList}" />
		</form:select>
		<form:errors path="casenumber" cssClass="add-margin error-msg" />
	</div>
	
	
</div>
<div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.casedate" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:input path="caseDate" class="form-control datepicker"
			title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}"
			data-date-end-date="-1d" id="caseDate"
			data-inputmask="'mask': 'd/m/y'" required="required" />
		<form:errors path="caseDate" cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.court" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:select path="courtMaster" data-first-option="false"
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>

			<form:options items="${courtsList}" itemValue="id" id="courtDropdown"
				itemLabel="name" />
		</form:select>
		<form:errors path="courtMaster" cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.casecatagory" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:select path="casetypeMaster" data-first-option="false"
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>

			<form:options items="${caseTypeList}" itemValue="id"
				id="courtTypeDropdown" itemLabel="code" />
		</form:select>
		<form:errors path="casetypeMaster" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">

	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.case.receivingdate" />:</label>
	<div class="col-sm-3 add-margin">
		<form:input path="caseReceivingDate" class="form-control datepicker"
			title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}"
			data-date-end-date="-1d" id="caseReceivingDate"
			data-inputmask="'mask': 'd/m/y'" />
		<form:errors path="caseReceivingDate" cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.caDue.date" /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="caDueDate" class="form-control datepicker"
			title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}"
			data-date-end-date="-1d" id="caDueDate"
			data-inputmask="'mask': 'd/m/y'" />
		<form:errors path="caDueDate" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.section" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:select path="functionaryCode" data-first-option="false"
			cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>

			<form:options items="${sectionlist}"  />
		</form:select>
		<form:errors path="functionaryCode" cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right" id="persons"><spring:message
			code="lbl.previouscaseNumber" />:</label>
	<div class="col-sm-3 add-margin" id="personsdiv">
		<form:input class="form-control patternvalidation"
			data-pattern="string" maxlength="50" id="appealNum" path="appealNum"/>
			<spring:message code="lbl.textmessage.previouscaseno" />
	<form:errors path="appealNum" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right" id="persons"><spring:message
			code="lbl.title" /><span class="mandatory"></span>:</label>
			<div class="col-sm-3 add-margin" >
		<form:textarea class="form-control" path="caseTitle" 
			id="caseTitle" name="caseTitle" />
		<form:errors path="caseTitle" cssClass="add-margin error-msg" />
	</div>
			</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right" id="persons"><spring:message
			code="lbl.prayer" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin" id="personsdiv">
		<form:textarea class="form-control" maxlength="1024"
			id="prayer" path="prayer" />
		<form:errors path="prayer" cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right" id="persons"><spring:message
			code="lbl.fieldbycarp" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
	<form:checkbox id="activeid" path="isfiledbycorporation"
				value="isfiledbycorporation" required="required" />
			<form:errors path="isfiledbycorporation" />
		</div>
</div>

<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>