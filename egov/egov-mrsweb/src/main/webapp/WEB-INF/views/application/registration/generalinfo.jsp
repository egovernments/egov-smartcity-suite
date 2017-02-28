<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->


<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="subheading.general.info" />
	</div>
</div>
<div class="panel-body custom-form">
	<form:hidden path="id" />
	<c:if test="${marriageRegistration.id!=null}">
		<div class="form-group">
			<div class="col-sm-3 control-label">
				<spring:message code="lbl.application.no" />
			</div>
			<div class="col-sm-3 add-margin view-content">
				<c:out value="${marriageRegistration.applicationNo}"></c:out>
			</div>
			<c:if test="${marriageRegistration.registrationNo!=null}">
				<div class="col-sm-2 control-label">
					<spring:message code="lbl.registration.no" />
				</div>
				<div class="col-sm-3 add-margin view-content">
					<c:out value="${marriageRegistration.registrationNo}"></c:out>
				</div>
			</c:if>
		</div>
	</c:if>
	<div class="form-group">
		<label class="col-sm-3 control-label"> <spring:message
				code="lbl.registrationunit" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:select path="marriageRegistrationUnit"
				id="select-registrationunit" cssClass="form-control"
				cssErrorClass="form-control error" required="required">
				<form:option value="">
					<spring:message code="lbl.default.option" />
				</form:option>
				<form:options items="${marriageRegistrationUnit}" itemValue="id"
					itemLabel="name" />
			</form:select>
			<form:errors path="marriageRegistrationUnit"
				cssClass="add-margin error-msg" />
		</div>
		<label class="col-sm-2 control-label"> <spring:message
				code="lbl.Boundary" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:hidden path="zone" id="txt-zoneid" />

			<form:input path="zone.name" id="txt-zone" type="text"
				class="form-control low-width patternvalidation" readonly="true"
				data-pattern="decimalvalue" placeholder="" autocomplete="off"
				required="required" />
			<form:errors path="zone" cssClass="add-margin error-msg" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label"> <spring:message
				code="lbl.street" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:input path="street" id="txt-street" type="text"
				class="form-control is_valid_alphabet inline-elem" placeholder=""
				autocomplete="off" required="required" />
			<form:errors path="street" cssClass="add-margin error-msg" />
		</div>
		<label class="col-sm-2 control-label"> <spring:message
				code="lbl.locality" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">

			<form:select path="locality" id="select-locality"
				cssClass="form-control" cssErrorClass="form-control error"
				required="required">
				<form:option value="">
					<spring:message code="lbl.default.option" />
				</form:option>
				<form:options items="${localitylist}" itemValue="id"
					itemLabel="name" />
			</form:select>
			<form:errors path="locality" cssClass="add-margin error-msg" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label"> <spring:message
				code="lbl.city" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:input path="city" id="txt-city" type="text"
				class="form-control is_valid_alphabet inline-elem" placeholder=""
				autocomplete="off" required="required" />
			<form:errors path="city" cssClass="add-margin error-msg" />
		</div>
		<label class="col-sm-2 control-label"> <spring:message
				code="lbl.date.of.marriage" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:input path="dateOfMarriage" id="txt-dateOfMarriage" type="text"
				class="form-control datepicker" data-inputmask="'alias': 'date'"
				data-date-today-highlight="true" data-date-end-date="0d"
				placeholder="" autocomplete="off" required="required" />
			<form:errors path="dateOfMarriage" cssClass="add-margin error-msg" />
		</div>
	</div>

	<div class="form-group">
		<%-- 	<label class="col-sm-3 control-label"> <spring:message
				code="lbl.fee.criteria" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:select path="feeCriteria" id="select-marriagefees"
				cssClass="form-control" cssErrorClass="form-control error"
				required="required">
				<form:option value="">
					<spring:message code="lbl.default.option" />
				</form:option>
				<form:options items="${feesList}" itemValue="id"
					itemLabel="criteria" />
			</form:select>
			 <form:hidden path="feeCriteria.criteria" name="feeCriteria" id="txt_feecriteria"/>
			<form:errors path="feeCriteria" cssClass="add-margin error-msg" />
		</div> --%>
		<label class="col-sm-3 control-label "> <spring:message
				code="lbl.marriage.venue" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:select path="venue" id="select-venue" cssClass="form-control "
				cssErrorClass="form-control error" required="required">
				<form:option value="">
					<spring:message code="lbl.default.option" />
				</form:option>
				<form:options items="${venuelist}" />
			</form:select>
			<%-- <form:input path="placeOfMarriage" id="txt-placeOfMarriage" type="text" class="form-control low-width is_valid_alphabet" placeholder="" autocomplete="off" required="required"/> --%>
			<form:errors path="venue" cssClass="add-margin error-msg" />
		</div>

		<label class="col-sm-2 control-label"> <spring:message
				code="lbl.marriage.fee" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:input path="feePaid" id="txt-feepaid" type="text"
				readonly="true" class="form-control low-width patternvalidation"
				data-pattern="decimalvalue" placeholder="" autocomplete="off"
				required="required" />
			<form:errors path="feePaid" cssClass="add-margin error-msg" />
		</div>
	</div>
	<div class="form-group">
		<div id="hideplaceofmrg">
			<label class="col-sm-3 control-label toggle-madatory"
				id="txt-venuelabel"> <spring:message
					code="lbl.place.of.marriage" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3">

				<form:input path="placeOfMarriage" id="txt-placeofmrg" type="text"
					class="form-control low-width patternvalidation addremoverequired"
					placeholder="" autocomplete="off" />

				<form:errors path="placeOfMarriage" cssClass="add-margin error-msg" />
			</div>
		</div>
		<label class="col-sm-2 text-right"> <spring:message
				code="lbl.marriage.photo" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-3 setimage">

			<c:choose>
				<c:when
					test="${currentState != 'NEW' && currentState != 'DATAENTRY'}">
					<form:hidden path="marriagePhotoFileStore" />
					<form:hidden class="encodedPhoto" path="encodedMarriagePhoto" />
					<img id="marriage-photo" class="add-margin marriage-img"
						height="150" width="130" />
					<input type="file" id="marriage-photo" name="marriagePhotoFile"
						class="file-ellipsis upload-file validate-file"
						data-fileto="marriage-photo">
					<form:errors path="marriagePhotoFile"
						cssClass="add-margin error-msg" />
				</c:when>
				<c:otherwise>
					<img id="marriage-photo" class="add-margin marriage-img"
						height="150" width="130" />
					<input type="file" id="marriage-photo" name="marriagePhotoFile"
						class="file-ellipsis upload-file validate-file" required="true"
						data-fileto="marriage-photo">
					<form:errors path="marriagePhotoFile"
						cssClass="add-margin error-msg" />
				</c:otherwise>
			</c:choose>
		</div>

	</div>

	<div class="form-group">
		<%--	<label class="col-sm-3 control-label"> <spring:message
				code="lbl.law" /><span class="mandatory"></span>
		</label>
	 	<div class="col-sm-3">
			<form:select path="marriageAct" id="select-marriageAct"
				cssClass="form-control" cssErrorClass="form-control error"
				required="required">
				<form:option value="">
					<spring:message code="lbl.default.option" />
				</form:option>
				<form:options items="${acts}" itemValue="id" itemLabel="name" />
			</form:select>
			<form:errors path="marriageAct" cssClass="add-margin error-msg" />
		</div> --%>

	</div>

	<c:set value="husband" var="applicant" scope="request"></c:set>
	<form:hidden path="husband.id" />
	<jsp:include page="applicantinfo.jsp">
		<jsp:param value="subheading.husband.info" name="header" />
	</jsp:include>

	<c:set value="wife" var="applicant" scope="request"></c:set>
	<form:hidden path="wife.id" />
	<jsp:include page="applicantinfo.jsp">
		<jsp:param value="subheading.wife.info" name="header" />
	</jsp:include>

		<c:if test="${isDigitalSignEnabled ne true && (marriageRegistration.status.code =='APPROVED' && pendingActions == 'Certificate Print Pending') 
		 || marriageRegistration.status.code =='APPROVED' || marriageRegistration.status.code =='DIGITALSIGNED' || currentState == 'DATAENTRY' }">
		
		<div class="form-group">
			<label class="col-sm-3 control-label"> <spring:message
					code="lbl.serial.no" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3">
				<form:input path="serialNo" id="txt-serialNo" type="text"
					class="exclude_readonly_input form-control low-width patternvalidation"
					data-pattern="number" maxlength="12" placeholder=""
					autocomplete="off" required="required" />
				<form:errors path="serialNo" cssClass="add-margin error-msg" />
			</div>
			<label class="col-sm-2 control-label"> <spring:message
					code="lbl.page.no" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3">
				<form:input path="pageNo" id="txt-pageNo" type="text"
					class="exclude_readonly_input form-control low-width  patternvalidation "
					data-pattern="number" maxlength="12" placeholder=""
					autocomplete="off" required="required" />
				<form:errors path="pageNo" cssClass="add-margin error-msg" />
			</div>
		</div>
		</c:if>
		<c:if test="${isDigitalSignEnabled eq true && marriageRegistration.status.code =='CREATED'}">
			<div class="form-group">
			<label class="col-sm-3 control-label validate-madatory"> <spring:message
					code="lbl.serial.no" /> <span class="mandatory"></span>
			</label>
			<div class="col-sm-3">
				<form:input path="serialNo" id="txt-serialNo" type="text"
					class=" form-control low-width patternvalidation addremovemandatory"
					data-pattern="number" maxlength="12" placeholder=""
					autocomplete="off" />
				<form:errors path="serialNo" cssClass="add-margin error-msg" />
			</div>
			<label class="col-sm-2 control-label validate-madatory"> <spring:message
					code="lbl.page.no" /> <span class="mandatory"></span>
			</label>
			<div class="col-sm-3">
				<form:input path="pageNo" id="txt-pageNo" type="text"
					class="form-control low-width  patternvalidation addremovemandatory"
					data-pattern="number" maxlength="12" placeholder=""
					autocomplete="off" />
				<form:errors path="pageNo" cssClass="add-margin error-msg" />
			</div>
		</div>
		</c:if>
	
</div>