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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row" id="page-content">
<form:form mothod="post" class="form-horizontal form-groups-bordered" modelAttribute="appConfig" id="appConfigForm">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
        </c:if>
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title">
                        <strong><spring:message code="lbl.hdr.modifyAppconfig"/></strong>
                    </div>
                </div>

                <div class="panel-body custom-form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.AppconfigKeyName"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-6">
                            <form:input path="keyName" id="keyName" type="text" class="form-control low-width"
                                        placeholder="" autocomplete="off" readonly="true" required="required"/>
                            <form:errors path="keyName" cssClass="add-margin error-msg"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.description"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-6">
                            <form:input path="description" id="description" type="text" cssClass="form-control low-width"
                                        placeholder="" autocomplete="off" required="required"/>
                            <form:errors path="description" cssClass="add-margin error-msg"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><spring:message code="lbl.module"/><span
                                class="mandatory"></span></label>
                        <div class="col-sm-6">
                            <form:input path="module.name" id="module" type="text" cssClass="form-control low-width"
                                        placeholder="" autocomplete="off" required="" readonly="true" value="${appConfig.module.name}"/>
                        </div>
                    </div>

                    
                </div>
            </div>
        
    </div>
    <div class="col-md-12">
    <button type="button" class="btn btn-secondary pull-right" id="addrow"><i class="fa fa-plus-circle" aria-hidden="true"></i> &nbsp;Add Row</button>
	  <table class="table table-bordered" id="configs">
	      <thead>
	          <th><spring:message code="lbl.date"/></th>
	          <th><spring:message code="lbl.values"/></th>
	          <th></th>
	      </thead>
		<tbody>
			<c:choose>
	          <c:when test="${not empty appConfig.confValues}">
	              <c:forEach items="${appConfig.confValues}" var="configValue" varStatus="counter">
	                  <tr>
	                      <td>
	                          <fmt:formatDate value="${configValue.effectiveFrom}" var="effectiveDt" pattern="dd/MM/yyyy"/>
	                          <form:input path="confValues[${counter.index}].effectiveFrom" cssClass="form-control datepicker effectiveFrom" value="${effectiveDt}"
	                                      id="confValues[${counter.index}].effectiveFrom" data-date-start-date="0d" required="required" disabled="true"/>
	                          <form:errors path="confValues[${counter.index}].effectiveFrom" cssClass="add-margin error-msg"/>
	                      </td>
	                      <td>
	                          <form:input path="confValues[${counter.index}].value" cssClass="form-control confValues" value="${configValue.value}"
	                                      id="confValues[${counter.index}].value" required="required"/>
	                          <form:errors path="confValues[${counter.index}].value" cssClass="add-margin error-msg"/>
                              <form:hidden path="confValues[${counter.index}].markedForRemoval" id="confValues[${counter.index}].markedForRemoval"
                                           value="${configValue.markedForRemoval}" class="markedForRemoval"/>
	                      </td>
	                      <td>
	                          <span class="add-padding"><i class="fa fa-trash" aria-hidden="true" id="deleterow"></i></span>
	                      </td>
	                  </tr>
	              </c:forEach>
	          </c:when>
	      </c:choose>
		</tbody>
	    </table>
	</div>
	<div class="col-md-12 text-center">
	    <div class="add-margin">
	        <button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
	        <a href="/egi/app/config/update" class="btn btn-default">Search</a>
	        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="self.close()">
	          <spring:message code="lbl.close"/>
	        </button>
	    </div>
	</div>
	</form:form>
</div>
<script src="<cdn:url  value='/resources/js/app/appconfig.js'/>"></script>