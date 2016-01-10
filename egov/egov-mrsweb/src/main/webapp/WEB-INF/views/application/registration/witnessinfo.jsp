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


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="row">
	<span class="bold"><spring:message code="${param.header}"/></span>
</div>
<div class="row">
	<div class="col-sm-7">
	<div class="row">
	<div class="form-group">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.fullname"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-6">
			<form:input path="param.witness.name.firstName" id="txt-firstName" type="text" class="form-control low-width is_valid_alphabet" maxlength="30" autocomplete="off" required="required"/>
            <form:errors path="param.witness.name.firstName" cssClass="add-margin error-msg"/>
		</div>
	</div>
	</div>
	</div>
	<div class="col-sm-5">
	<div class="row">
		<label class="col-sm-6 control-label">
			<spring:message code="lbl.photo"/><span class="mandatory"></span>
		</label>		
		<div class="col-sm-6">
		</div>
	</div>
	</div>
</div>
<div class="row">
	<div class="col-sm-7">
	<div class="row">
		<div class="form-group">
			<label class="col-sm-6 control-label">
				<spring:message code="lbl.othername"/>
			</label>
			<div class="col-sm-6">
				<form:input path="param.witness.otherName" id="txt-placeOfMarriage" type="text" class="form-control low-width is_valid_alphabet" maxlength="20" placeholder="" autocomplete="off" required="required"/>
	            <form:errors path="param.witness.otherName" cssClass="add-margin error-msg"/>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="form-group">
			<label class="col-sm-6 control-label">
				<spring:message code="lbl.religion"/>
			</label>
			<div class="col-sm-6">
				<form:select path="param.witness.religion.id" id="select-marriageAct" cssClass="form-control" 
							cssErrorClass="form-control error" required="required">
	                 <form:option value=""> <spring:message code="lbl.default.option"/> </form:option>
	                 <form:options items="${religions}" itemValue="id" itemLabel="name"/>
	            </form:select>
	            <form:errors path="param.witness.religion.id" cssClass="add-margin error-msg"/>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="form-group">
			<label class="col-sm-6 control-label">
				<spring:message code="lbl.religiontype"/>
			</label>
			<div class="col-sm-6">
				<form:radiobuttons path="param.witness.religionPractice" items="${religionPractice}"  required="required" name="param.witness.religionPractice" element="span"  />
			</div>
		</div>
	</div>
	<div class="row">
		<div class="form-group">
			<label class="col-sm-6 control-label">
				<spring:message code="lbl.ageason.marriage"/><span class="mandatory"></span>
			</label>
			<div class="col-sm-6">
				<form:input path="param.witness.ageInYearsAsOnMarriage" id="txt-ageInYearsAsOnMarriage" type="text" class="form-control low-width is_valid_numeric" style="display: inline; width: 203px;" maxlength="2" placeholder="Years" autocomplete="off" required="required"/>
				<form:input path="param.witness.ageInMonthsAsOnMarriage" id="txt-ageInYearsAsOnMarriage" type="text" class="form-control low-width is_valid_numeric" style="display: inline; width: 203px;" maxlength="2" placeholder="Months" autocomplete="off" required="required"/>
	            <form:errors path="param.witness.ageInYearsAsOnMarriage" cssClass="add-margin error-msg"/><form:errors path="param.witness.ageInMonthsAsOnMarriage" cssClass="add-margin error-msg"/>
			</div>
		</div>
	</div>
	</div>
	<div class="col-sm-5">
		<div class="row">
			<div class="col-sm-12 text-center">
				<img src="/egi/resources/global/images/logo@2x.png" height="90" width="100">
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-sm-6">
		<div class="row">
			<div class="form-group">
				<label class="col-sm-6 control-label">
					<spring:message code="lbl.applicant.status"/><span class="mandatory"></span>
				</label>
				<div class="col-sm-6">
					<form:select path="param.witness.presentRelation" id="select-relationStatus" cssClass="form-control" 
								cssErrorClass="form-control error" required="required">
		                 <form:option value=""> <spring:message code="lbl.default.option"/> </form:option>
		                 <form:options items="${relationStatus}" itemValue="id" itemLabel="name"/>
		            </form:select>
		            <form:errors path="param.witness.presentRelation" cssClass="add-margin error-msg"/>
				</div>
			</div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="row">
			<div class="form-group">
				<label class="col-sm-6 control-label">
					<spring:message code="lbl.occupation"/>
				</label>
				<div class="col-sm-6">
					<form:input path="param.witness.occupation" id="txt-placeOfMarriage" type="text" class="form-control low-width is_valid_alphabet" maxlength="20" placeholder="" autocomplete="off" required="required"/>
	            	<form:errors path="param.witness.occupation" cssClass="add-margin error-msg"/>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-sm-6">
		<div class="row">
			<div class="form-group">
				<label class="col-sm-6 control-label">
					<spring:message code="lbl.phoneno"/>
				</label>
				<div class="col-sm-6">
					<form:textarea path="param.witness.contactInfo.residenceAddress" id="txt-phoneno" type="text" class="form-control low-width" data-pattern="alphanumericwithspecialcharacters" maxlength="256" placeholder="" autocomplete="off" />
                    <form:errors path="param.witness.contactInfo.residenceAddress" cssClass="add-margin error-msg"/>
				</div>
			</div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="row">
			<div class="form-group">
				<label class="col-sm-6 control-label">
					<spring:message code="lbl.email"/>
				</label>
				<div class="col-sm-6">
					<form:textarea path="param.witness.contactInfo.officeAddress" id="txt-email" type="text" class="form-control low-width" data-pattern="alphanumericwithspecialcharacters" maxlength="256" placeholder="" autocomplete="off" />
                    <form:errors path="param.witness.contactInfo.officeAddress" cssClass="add-margin error-msg"/>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-sm-6">
		<div class="row">
			<div class="form-group">
				<label class="col-sm-6 control-label">
					<spring:message code="lbl.residence.address"/><span class="mandatory"></span>
				</label>
				<div class="col-sm-6">
					<form:textarea path="param.witness.contactInfo.residenceAddress" id="name" type="text" class="form-control low-width" data-pattern="alphanumericwithspecialcharacters" maxlength="256" placeholder="" autocomplete="off" />
                    <form:errors path="param.witness.contactInfo.residenceAddress" cssClass="add-margin error-msg"/>
				</div>
			</div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="row">
			<div class="form-group">
				<label class="col-sm-6 control-label">
					<spring:message code="lbl.office.address"/>
				</label>
				<div class="col-sm-6">
					<form:textarea path="param.witness.contactInfo.officeAddress" id="name" type="text" class="form-control low-width" data-pattern="alphanumericwithspecialcharacters" maxlength="256" placeholder="" autocomplete="off" />
                    <form:errors path="param.witness.contactInfo.officeAddress" cssClass="add-margin error-msg"/>
				</div>
			</div>
		</div>
	</div>
</div>