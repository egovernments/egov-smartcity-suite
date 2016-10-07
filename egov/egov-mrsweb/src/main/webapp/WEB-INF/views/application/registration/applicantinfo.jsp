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

<div class="row add-margin view-content">
	<div class="col-sm-3 text-center"><spring:message code="${param.header}"/></div>
	<div class="col-sm-9 text-center"></div>
</div>
<div class="row">
	<div class="col-sm-7">
	<div class="row">
	<div class="form-group">
		<label class="col-sm-5 text-right" style="padding-right: 5px;">
			<spring:message code="lbl.fullname"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-5" style="padding-left: 26px;">
			<form:input path="${applicant}.name.firstName" id="txt-firstName" type="text" class="form-control is_valid_alphabet inline-elem" maxlength="30" autocomplete="off" required="required" style="width: 33%" placeholder="First Name" />
			<form:input path="${applicant}.name.middleName" id="txt-middleName" type="text" class="form-control is_valid_alphabet inline-elem" maxlength="20" autocomplete="off"  style="width: 33%" placeholder="Middle Name"/>
			<form:input path="${applicant}.name.lastName" id="txt-lastName" type="text" class="form-control is_valid_alphabet inline-elem" maxlength="20" autocomplete="off" style="width: 30%" placeholder="Last Name"/>
            <form:errors path="${applicant}.name.firstName" cssClass="add-margin error-msg"/>
            <form:errors path="${applicant}.name.middleName" cssClass="add-margin error-msg"/>
            <form:errors path="${applicant}.name.lastName" cssClass="add-margin error-msg"/>
		</div>
		<div class="col-sm-2"></div>
	</div>
	</div>
	</div>
	<div class="col-sm-5">
	<div class="row">
		<div class="form-group">
			<label class="col-sm-5 text-right" style="padding-right: 25px;">
				<spring:message code="lbl.signature"/>
			</label>
			<div class="col-sm-5">
			<c:choose>
					<c:when test="${currentState != 'NEW'}">
						<form:hidden  path="${applicant}.signatureFileStore"/>
						<img class="add-border" id="${applicant}-signature" height="150" width="130" name="${applicant}.signature">
						<script>
							var applicant = '<c:out value="${applicant}" />';
							var strData = '';
		
							if (applicant == 'husband') {
								strData = '<c:out value="${registration.husband.encodedSignature}" />';
							} else {
								strData = '<c:out value="${registration.wife.encodedSignature}" />';
							} 
							
							if (strData != null && strData.length > 0) {
								$('#'+applicant+'-signature').prop('src', "data:image/jpg;base64," + strData);
							}
							
						</script>
						<input type="file" id="${applicant}.signatureFile" name="${applicant}.signatureFile" class="file-ellipsis upload-file">
					</c:when>
					<c:otherwise>
						<img class="add-border" id="${applicant}-signature" height="150" width="130" name="${applicant}.signature">
						<input type="file" id="${applicant}.signatureFile" name="${applicant}.signatureFile" class="file-ellipsis upload-file" required="required">
					</c:otherwise>
			</c:choose>
			</div>
		</div>
	</div>
	</div>
</div>
<div class="row">
	<div class="col-sm-7">
	<div class="row">
		<div class="form-group">
			<label class="col-sm-5 text-right" style="padding-right: 5px;">
				<spring:message code="lbl.othername"/>
			</label>
			<div class="col-sm-5" style="padding-left: 26px;">
				<form:input path="${applicant}.otherName" id="txt-placeOfMarriage" type="text" class="form-control low-width is_valid_alphabet" maxlength="20" placeholder="" autocomplete="off"/>
	            <form:errors path="${applicant}.otherName" cssClass="add-margin error-msg"/>
			</div>
			<div class="col-sm-2"></div>
		</div>
	</div>
	<div class="row">
		<div class="form-group">
			<label class="col-sm-5 text-right" style="padding-right: 5px;">
				<spring:message code="lbl.religion"/><span class="mandatory"></span>
			</label>
			<div class="col-sm-5" style="padding-left: 26px;">
				<form:select path="${applicant}.religion.id" id="select-marriageAct" cssClass="form-control" 
							cssErrorClass="form-control error" required="required">
	                 <form:option value=""> <spring:message code="lbl.default.option"/> </form:option>
	                 <form:options items="${religions}" itemValue="id" itemLabel="name"/>
	            </form:select>
	            <form:errors path="${applicant}.religion.id" cssClass="add-margin error-msg"/>
			</div>
			<div class="col-sm-2"></div>
		</div>
	</div>
	<div class="row">
		<div class="form-group">
			<label class="col-sm-5 text-right" style="padding-right: 5px;">
				<spring:message code="lbl.religiontype"/>
			</label>
			<div class="col-sm-5" style="padding-left: 26px;">
				<form:radiobuttons path="${applicant}.religionPractice" items="${religionPractice}" name="religionPractice" element="span" />
			</div>
			<div class="col-sm-2"></div>
		</div>
	</div>
	<div class="row">
		<div class="form-group">
			<label class="col-sm-5 text-right" style="padding-right: 5px;">
				<spring:message code="lbl.ageason.marriage"/><span class="mandatory"></span>
			</label>
			<div class="col-sm-5" style="padding-left: 26px;">
				<form:input path="${applicant}.ageInYearsAsOnMarriage" id="txt-ageInYearsAsOnMarriage" type="text" class="form-control inline-elem patternvalidation" data-pattern="number" style="width: 40%;" maxlength="2" placeholder="Years" autocomplete="off" required="required"/>
				<form:input path="${applicant}.ageInMonthsAsOnMarriage" id="txt-ageInYearsAsOnMarriage" type="text" class="form-control inline-elem patternvalidation month-field" data-pattern="number" style="width: 40%;" maxlength="2" placeholder="Months" autocomplete="off" required="required"/>
	            <form:errors path="${applicant}.ageInYearsAsOnMarriage" cssClass="add-margin error-msg"/>
	            <form:errors path="${applicant}.ageInMonthsAsOnMarriage" cssClass="add-margin error-msg"/>
			</div>
			<div class="col-sm-2"></div>
		</div>
	</div>
	</div>
	<div class="col-sm-5">
		<div class="row">
			<label class="col-sm-5 text-right" style="padding: 25prequired="required"x;">
				<spring:message code="lbl.photo"/><span class="mandatory"></span>
			</label>
			
			<div class="col-sm-6 setimage">
				<c:choose>
					<c:when test="${currentState != 'NEW'}">
						<form:hidden  path="${applicant}.photoFileStore"/>
							<img class="add-border" id="${applicant}-photo" height="150" width="130" name="${applicant}.photo">
							<script>
								var applicant = '<c:out value="${applicant}" />';
								var strData = '';
			
								if (applicant == 'husband') {
									strData = '<c:out value="${registration.husband.encodedPhoto}" />';
								} else {
									strData = '<c:out value="${registration.wife.encodedPhoto}" />';
								} 
								
								if (strData != null && strData.length > 0) {
									$('#'+applicant+'-photo').prop('src', "data:image/jpg;base64," + strData);
									$('#'+applicant+'-photo').attr('data-exist');
								}
								
							</script>
							<input type="file" id="${applicant}-photo" name="${applicant}.photoFile" class="file-ellipsis upload-file"  >
					</c:when>
					<c:otherwise>
						<img class="add-border" id="${applicant}-photo" height="150" width="130" name="${applicant}.photo">
						<input type="file" id="${applicant}-photo" name="${applicant}.photoFile" class="file-ellipsis upload-file" required="true">
					</c:otherwise> 
				</c:choose>
				
				
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
					<form:select path="${applicant}.maritalStatus" id="select-maritalstatus" cssClass="form-control" 
								cssErrorClass="form-control error" required="required">
		                 <form:option value=""> <spring:message code="lbl.default.option"/> </form:option>
		                 <form:options items="${maritalStatusList}"/>
		            </form:select>
		            <form:errors path="${applicant}.maritalStatus" cssClass="add-margin error-msg"/>
				</div>
			</div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="row">
			<div class="form-group">
				<label class="col-sm-6 control-label">
					<spring:message code="lbl.occupation"/><span class="mandatory"></span>
				</label>
				<div class="col-sm-6">
					<form:input path="${applicant}.occupation" id="txt-occupation" type="text" class="form-control low-width is_valid_alphabet" maxlength="20" placeholder="" autocomplete="off" required="required"/>
	            	<form:errors path="${applicant}.occupation" cssClass="add-margin error-msg"/>
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
					<form:textarea path="${applicant}.contactInfo.residenceAddress" id="txt-residenceAddress" type="text" class="form-control low-width patternvalidation" data-pattern="regexp_alphabetspecialcharacters" maxlength="256" placeholder="" autocomplete="off" required="required"/>
                    <form:errors path="${applicant}.contactInfo.residenceAddress" cssClass="add-margin error-msg"/>
				</div>
			</div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="row">
			<div class="form-group">
				<label class="col-sm-6 control-label">
					<spring:message code="lbl.office.address"/><span class="mandatory"></span>
				</label>
				<div class="col-sm-6">
					<form:textarea path="${applicant}.contactInfo.officeAddress" id="txt-officeAddress" type="text" class="form-control low-width patternvalidation" data-pattern="regexp_alphabetspecialcharacters" maxlength="256" placeholder="" autocomplete="off" required="required"/>
                    <form:errors path="${applicant}.contactInfo.officeAddress" cssClass="add-margin error-msg"/>
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
					<spring:message code="lbl.phoneno"/><span class="mandatory"></span>
				</label>
				<div class="col-sm-6">
					<form:input path="${applicant}.contactInfo.mobileNo" id="txt-phoneNo" type="text" cssClass="form-control low-width patternvalidation" data-pattern="number" maxlength="10" placeholder="" autocomplete="off" required="required" />
                    <form:errors path="${applicant}.contactInfo.mobileNo" cssClass="add-margin error-msg"/>
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
					<form:input path="${applicant}.contactInfo.email" id="${applicant}email" type="text" cssClass="form-control low-width" maxlength="128" placeholder="" autocomplete="off" />
					<span class=""></span>
                    <form:errors path="${applicant}.contactInfo.email" cssClass="add-margin error-msg"/>
				</div>
			</div>
		</div>
	</div>
</div>