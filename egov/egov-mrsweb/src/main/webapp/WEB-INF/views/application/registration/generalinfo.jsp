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

<div class="panel-heading">
	<div class="panel-title">
		<spring:message code="subheading.general.info"/>
	</div>
</div>
<div class="panel-body custom-form">
<form:hidden path="id"/>
	<div class="form-group">
		<label class="col-sm-3 control-label">
			<spring:message code="lbl.registrationunit"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:select path="marriageRegistrationUnit.id" id="select-registrationunit" cssClass="form-control" 
						cssErrorClass="form-control error" required="required">
                 <form:option value=""> <spring:message code="lbl.default.option"/> </form:option>
                 <form:options items="${marriageRegistrationUnit}" itemValue="id" itemLabel="name"/>
             </form:select>
            <form:errors path="marriageRegistrationUnit.id" cssClass="add-margin error-msg"/>
		</div>
		<label class="col-sm-2 control-label">
			<spring:message code="lbl.zone"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:hidden path="zone" id="txt-zoneid" />
			
			<form:input path="zone.name" id="txt-zone" type="text" class="form-control low-width patternvalidation" readonly="true" data-pattern="decimalvalue" placeholder="" autocomplete="off" required="required"/>
            <form:errors path="zone" cssClass="add-margin error-msg"/>
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label">
			<spring:message code="lbl.date.of.marriage"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:input path="dateOfMarriage" id="txt-dateOfMarriage" type="text" class="form-control low-width datepicker today" data-date-today-highlight="true" data-date-end-date="0d" placeholder="" autocomplete="off" required="required"/>
            <form:errors path="dateOfMarriage" cssClass="add-margin error-msg"/>
		</div>
		<label class="col-sm-2 control-label">
			<spring:message code="lbl.place.of.marriage"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:input path="placeOfMarriage" id="txt-placeOfMarriage" type="text" class="form-control low-width is_valid_alphabet" placeholder="" autocomplete="off" required="required"/>
            <form:errors path="placeOfMarriage" cssClass="add-margin error-msg"/>
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label">
			<spring:message code="lbl.fee.criteria"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:select path="feeCriteria.id" id="select-marriagefees" cssClass="form-control" 
						cssErrorClass="form-control error" required="required">
                 <form:option value=""> <spring:message code="lbl.default.option"/> </form:option>
                 <form:options items="${feesList}" itemValue="id" itemLabel="criteria"/>
             </form:select>
            <%--  <form:hidden path="feeCriteria.criteria" name="feeCriteria" id="txt_feecriteria"/> --%>
               <form:errors path="feeCriteria.id" cssClass="add-margin error-msg"/>
		</div>
		<label class="col-sm-2 control-label">
			<spring:message code="lbl.marriage.fee"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:input path="feePaid" id="txt-feepaid" type="text" class="form-control low-width patternvalidation" data-pattern="decimalvalue" placeholder="" autocomplete="off" required="required"/>
            <form:errors path="feePaid" cssClass="add-margin error-msg"/>
		</div>
	</div>
	<div class="form-group">
	<label class="col-sm-3 control-label">
			<spring:message code="lbl.law"/><span class="mandatory"></span>
		</label>
		<div class="col-sm-3">
			<form:select path="marriageAct.id" id="select-marriageAct" cssClass="form-control" 
						cssErrorClass="form-control error" required="required">
                 <form:option value=""> <spring:message code="lbl.default.option"/> </form:option>
                 <form:options items="${acts}" itemValue="id" itemLabel="name"/>
             </form:select>
            <form:errors path="marriageAct.id" cssClass="add-margin error-msg"/>
		</div>
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
</div>