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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:if test="${not empty message}">
                    <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
                </c:if>
				
				<div class="panel-body custom-form">				
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.registrationunitname"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-6">
							<form:input path="name" id="name" type="text" cssClass="form-control patternvalidation" 
                        	  data-pattern="alphanumericwithspacehyphenunderscore" maxlength="50" placeholder="" autocomplete="off" required="required"/>
                            <form:errors path="name" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.full.address"/><span class="mandatory"></span>
						</label>
						<div class="col-sm-6">
							<form:textarea path="address" id="address" type="text" class="form-control low-width" data-pattern="alphanumericwithspecialcharacters" maxlength="100" placeholder="" autocomplete="off" />
                            <form:errors path="address" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
					<label class="col-sm-3 control-label">
						<spring:message code="lbl.zone"/><span class="mandatory"></span>
					</label>
			<div class="col-sm-3">
			<form:select path="zone" id="select-zones" cssClass="form-control" 
						cssErrorClass="form-control error" required="required">
                 <form:option value=""> <spring:message code="lbl.default.option"/> </form:option>
                 <form:options items="${zones}" itemValue="id" itemLabel="name"/>
             </form:select>
            <form:errors path="zone" cssClass="add-margin error-msg"/>
			</div>
			</div>
			<div class="form-group">
			<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.isactive" /></label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isActive" checked="checked" />
							<form:errors path="isActive" cssClass="error-msg" />
						</div>
					</div>
						<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.IsMainRegistrationUnit" /></label>
						<div class="col-sm-3 add-margin">
							<form:checkbox path="isMainRegistrationUnit" />
							<form:errors path="isMainRegistrationUnit" cssClass="error-msg" />
						</div>
					<input type="hidden" name="marriageRegistrationUnit" value="${marriageRegistrationUnit.id}" />
					<form:hidden path="code" id="code" value="${marriageRegistrationUnit.code}" />
				</div>
				</div>
			