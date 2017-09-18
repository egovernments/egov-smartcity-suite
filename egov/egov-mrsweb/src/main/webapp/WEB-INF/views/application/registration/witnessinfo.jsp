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
	<c:if test="${not empty param.header}">
		<div class="panel-title">
			<spring:message code="${param.header}" />
		</div>
	</c:if>
	<div class="panel-title">
		<spring:message code="${param.subhead}" />
	</div>
</div>
<div class="form-group">
	<div class="col-sm-6">
		<label class="col-sm-6 text-right"> <spring:message
				code="lbl.fullname" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-6">
			<form:input path="${witness}.name.firstName" type="text"
				class="form-control is_valid_alphabet inline-elem" maxlength="30"
				placeholder="First Name" autocomplete="off" required="required"
				style="width: 33%" />
			<form:input path="${witness}.name.middleName" type="text"
				class="form-control is_valid_alphabet inline-elem" maxlength="20"
				placeholder="Middle Name" autocomplete="off" style="width: 30%" />
			<form:input path="${witness}.name.lastName" type="text"
				class="form-control is_valid_alphabet inline-elem" maxlength="20"
				placeholder="Last Name" autocomplete="off" style="width: 30%" />
			<form:errors path="${witness}.name.firstName"
				cssClass="add-margin error-msg" />
			<form:errors path="${witness}.name.middleName"
				cssClass="add-margin error-msg" />
			<form:errors path="${witness}.name.lastName"
				cssClass="add-margin error-msg" />
		</div>

		<label class="col-sm-6 text-right"> <spring:message
				code="lbl.witness.info" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-6">
			<form:select path="${witness}.witnessRelation"
				id="${witness}.witnessRelation" class="form-control inline-elem"
				style="width: 30%" cssErrorClass="form-control error"
				required="required">
				<form:options items="${witnessRelation}" />
			</form:select>
			<form:input path="${witness}.relativeName"
				id="${witness}.relativeName" type="text"
				class="form-control is_valid_alphabet inline-elem" maxlength="70"
				style="width: 65%" placeholder="" autocomplete="off"
				required="required" />


			<form:errors path="${witness}.witnessRelation"
				cssClass="add-margin error-msg" />
			<form:errors path="${witness}.relativeName"
				cssClass="add-margin error-msg" />
		</div>
		<label class="col-sm-6 text-right"> <spring:message
				code="lbl.applicant.aadhaarNo" />
		</label>
		<div class="col-sm-6">
			<form:input path="${witness}.aadhaarNo" id="${witness}.aadhaarNo"
				type="text" cssClass="form-control low-width patternvalidation"
				data-pattern="number"  maxlength="12" placeholder=""
				autocomplete="off" />
			<form:errors path="${witness}.aadhaarNo"
				cssClass="add-margin error-msg" />
		</div>

		<label class="col-sm-6 text-right"> <spring:message
				code="lbl.occupation" />
		</label>
		<div class="col-sm-6">
			<form:input path="${witness}.occupation" id="${witness}.occupation"
				type="text" class="form-control low-width is_valid_alphabet"  
				maxlength="60" placeholder="" autocomplete="off" />
			<form:errors path="${witness}.occupation"
				cssClass="add-margin error-msg" />
		</div>
		<label class="col-sm-6 text-right"> <spring:message
				code="lbl.relationship.applicant" />
		</label>
		<div class="col-sm-6">
			<form:input path="${witness}.relationshipWithApplicant"
				id="${witness}.relationshipWithApplicant" type="text"
				class="form-control low-width is_valid_alphabet" maxlength="30"
				placeholder="" autocomplete="off" />
			<form:errors path="${witness}.relationshipWithApplicant"
				cssClass="add-margin error-msg" />
		</div>
		<label class="col-sm-6 text-right"> <spring:message
				code="lbl.age" /><span class="mandatory"></span>
		</label>
		<div class="col-sm-6">
			<form:input path="${witness}.age" id="${witness}.age" type="text"
				data-names="witness"
				class="form-control patternvalidation age-field" maxlength="2"
				data-pattern="number" placeholder="" autocomplete="off"
				required="required" />
			<form:errors path="${witness}.age" cssClass="add-margin error-msg" />
		</div>

	</div>
	<div class="col-sm-6">
		<label class="col-sm-4 text-right"> <spring:message
				code="lbl.photo" />
		</label>
		<div class="col-sm-6 setimage">

			<c:choose>
				<c:when
					test="${(currentState eq 'NEW' && nextActn eq 'Junior/Senior Assistance approval pending') || (currentState != 'NEW' && currentState != 'DATAENTRY')}">
					<form:hidden path="${witness}.photoFileStore" />
					<form:hidden class="encodedPhoto" path="${witness}.encodedPhoto" />
					<img id="${witness}.photo" class="add-margin marriage-img"
						height="150" width="130" />
					<input type="file" id="${witness}-photo"
						name="${witness}.photoFile" class="file-ellipsis upload-file validate-file"
						data-fileto="${witness}.photo" accept="image/*">
				</c:when>
				<c:otherwise>
					<img id="${witness}.photo" class="add-margin marriage-img"
						height="150" width="130" />
					<input type="file" id="${witness}-photo"
						name="${witness}.photoFile" class="file-ellipsis upload-file validate-file"
						data-fileto="${witness}.photo" accept="image/*">
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 text-right"> <spring:message
			code="lbl.residence.address" /><span class="mandatory"></span>
	</label>
	<div class="col-sm-3">
		<form:textarea path="${witness}.contactInfo.residenceAddress"
			id="${witness}.contactInfo.residenceAddress" type="text"
			class="form-control low-width patternvalidation"
			data-pattern="regexp_alphabetspecialcharacters" maxlength="250"
			placeholder="" autocomplete="off" required="required" />
		<form:errors path="${witness}.contactInfo.residenceAddress"
			cssClass="add-margin error-msg" />
	</div>
	<%-- <label class="col-sm-2 text-right"> <spring:message
			code="lbl.office.address" /><span class="mandatory"></span>
	</label>
	<div class="col-sm-3">
		<form:textarea path="${witness}.contactInfo.officeAddress"
			id="${witness}.contactInfo.officeAddress" type="text"
			class="form-control low-width patternvalidation"
			data-pattern="regexp_alphabetspecialcharacters" maxlength="250"
			placeholder="" autocomplete="off" required="required" />
		<form:errors path="${witness}.contactInfo.officeAddress"
			cssClass="add-margin error-msg" />
	</div> --%>
</div>
