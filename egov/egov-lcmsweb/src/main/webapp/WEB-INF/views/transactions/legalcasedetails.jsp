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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<c:if test="${not empty message}">
	<div cssClass="add-margin error-msg" role="alert">${message}</div>
</c:if>
<input type="hidden" name="mode" value="${mode}" />
<spring:hasBindErrors name="legalCase">
	<c:forEach var="error" items="${errors.allErrors}">
		<form:errors path="caseNumber" cssClass="add-margin error-msg" />
		<div style="color: red;">
			<b><spring:message message="${error}" /></b> <br />
		</div>
	</c:forEach>
</spring:hasBindErrors>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.courttype" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:select path="courtMaster.courtType" data-first-option="false"
			cssClass="form-control" required="required" name="courtType"
			id="courtType">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>
		<form:options items="${courtTypeList}" itemValue="id"
				id="courtTypeDropdown" itemLabel="courtType" />
		</form:select>
		<form:errors path="courtMaster.courtType"
			cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.petitiontype" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:select path="petitionTypeMaster" id="petitionTypeMaster"
			data-first-option="false" cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>
			<form:options items="${petitiontypeList}" itemValue="id"
				id="courtTypeDropdown" itemLabel="petitionType" />
		</form:select>
		<form:errors path="petitionTypeMaster" cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.court" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:select path="courtMaster" id="courtMaster"
			data-first-option="false" cssClass="form-control" required="required">
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
		<form:select path="caseTypeMaster" data-first-option="false"
			id="caseTypeMaster" cssClass="form-control" required="required">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>
			<form:options items="${caseTypeList}" itemValue="id"
				id="courtTypeDropdown" itemLabel="caseType" />
		</form:select>
		<form:errors path="caseTypeMaster" cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.casenumber" /><span class="mandatory"></span>:</label>
	<div class="col-sm-2 add-margin text-center">
		<form:input class="form-control patternvalidation"
			data-pattern="number" maxlength="50" id="caseNumber"
			required="required" path="caseNumber" /></div>
	<div class="col-sm-1 add-margin text-center">
		<form:select path="wpYear" data-first-option="false" id="wpYear"
			cssClass="form-control">
			<form:option value="">
				<spring:message code="lbls.select" />
			</form:option>
			<form:options items="${wPYearList}" />
		</form:select>
		<form:errors path="caseNumber" cssClass="add-margin error-msg" />
	</div>
	<c:if test="${mode == 'edit'}">
		<label class="col-sm-2 control-label text-right"><spring:message
				code="lbl.lcnumber" />:</label>
		<div class="col-sm-3 add-margin text-center">
			<form:input class="form-control patternvalidation"
				data-pattern="number" maxlength="50" id="lcNumber"
				readonly="readonly" path="lcNumber" /></div>
		<form:errors path="lcNumber" cssClass="add-margin error-msg" />
	</c:if>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-left"><spring:message
			code="lbl.casedate" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin text-center">
		<form:input path="caseDate" class="form-control datepicker"
			title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}"
			data-date-end-date="-1d" id="caseDate"
			data-inputmask="'mask': 'd/m/y'" required="required" />
		<form:errors path="caseDate" cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right" id="persons"><spring:message
			code="lbl.previouscaseNumber" />:</label>
	<div class="col-sm-3 add-margin" id="personsdiv">
		<form:input class="form-control patternvalidation"
			placeholder="InCase of appeal,review petition"
			data-pattern="alphanumericwithspecialcharacters" maxlength="50"
			id="appealNum" path="appealNum" />
		<form:errors path="appealNum" cssClass="add-margin error-msg" />
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right" id="persons"><spring:message
			code="lbl.title" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin">
		<form:textarea class="form-control" path="caseTitle" id="caseTitle"
			name="caseTitle" />
		<form:errors path="caseTitle" cssClass="add-margin error-msg" />
	</div>
	<label class="col-sm-2 control-label text-right" id="persons"><spring:message
			code="lbl.prayer" /><span class="mandatory"></span>:</label>
	<div class="col-sm-3 add-margin" id="personsdiv">
		<form:textarea class="form-control" maxlength="10000" id="prayer"
			path="prayer" />
		<form:errors path="prayer" cssClass="add-margin error-msg" />
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
		<form:input path="pwrList[0].caDueDate"
			class="form-control datepicker" title="Please enter a valid date"
			pattern="\d{1,2}/\d{1,2}/\d{4}" id="caDueDate"
			data-inputmask="'mask': 'd/m/y'" />
		<form:errors path="pwrList[0].caDueDate"
			cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.officerincharge" />:</label>
	<div class="col-sm-3 add-margin">
	<input id="positionName" type="text" 
	 value="${legalCase.officerIncharge.name}"   
	class="form-control typeahead " autocomplete="off" placeholder=""/> 						 
	<form:hidden path="officerIncharge" id='positionId'/>																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																													
	 <form:errors path="officerIncharge" cssClass="add-margin error-msg" /> 
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.noticedate" /><span class="mandatory">:</span></label>
	<div class="col-sm-3 add-margin">
		<form:input path="noticeDate" class="form-control datepicker"																																																																						
			title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}"
			data-date-end-date="-1d" id="noticeDate"
			data-inputmask="'mask': 'd/m/y'" />
		<form:errors path="noticeDate" cssClass="add-margin error-msg" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right" id="persons"><spring:message
			code="lbl.fieldbycarp" /> ?:</label>
	<div class="col-sm-3 add-margin">
		<form:checkbox id="activeid" path="isFiledByCorporation"
			value="isFiledByCorporation" />
		<form:errors path="isFiledByCorporation" />
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.oldreferencenumber" />:</label>
	<div class="col-sm-3 add-margin">
		<form:input class="form-control patternvalidation" maxlength="4" id="oldrefnumber"
			path="oldReferenceNumber" data-pattern="address" />
		<form:errors path="oldReferenceNumber" cssClass="add-margin error-msg" />
	</div>
</div>

<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>