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
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row" id="page-content">
    <div class="col-md-12">
        <div class="panel" data-collapsed="0">
            <div class="panel-body">
                <c:if test="${not empty warning}">
                    <div class="alert alert-danger" role="alert"><spring:message code="${warning}"/></div>
                </c:if>
                <c:choose>
                    <c:when test="${search}">
                        <form:form id="boundaryCreateSearchForm" class="form-horizontal form-groups-bordered" method="get">
                            <div class="panel panel-primary" data-collapsed="0">
                                <div class="panel-heading">
                                    <div class="panel-title">
                                        <strong><spring:message code="lbl.hdr.searchBoundary"/></strong>
                                    </div>
                                </div>

                                <div class="panel-body custom-form">
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">
                                            <spring:message code="lbl.hierarchyType"/>
                                            <span class="mandatory"></span>
                                        </label>
                                        <div class="col-sm-6 add-margin">
                                            <select id="hierarchyTypeSelect" class="form-control" onchange="populateBoundaryTypes(this);" required="required">
                                                <option value=""><spring:message code="lbl.select"/></option>
                                                <c:forEach items="${hierarchyTypes}" var="hierarchyType">
                                                    <option value="${hierarchyType.id}">${hierarchyType.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label"><spring:message
                                                code="lbl.boundaryType"/><span class="mandatory"></span></label>
                                        <div class="col-sm-6 add-margin">
                                            <egov:ajaxdropdown id="boundaryTypeAjax" fields="['Text','Value']"
                                                               dropdownId="boundaryType" url="boundarytype/ajax/boundarytypelist-for-hierarchy"/>
                                            <select id="boundaryType" class="form-control" required="required">
                                                <option value=""><spring:message code="lbl.select"/></option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="text-center">
                                <button id="searchBtn" type="button" class="btn btn-primary"><spring:message code="lbl.search"/></button>
                                <button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
                                <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
                            </div>

                        </form:form>
                    </c:when>
                    <c:otherwise>
                        <form:form method="post" action="/egi/boundary/create" class="form-horizontal form-groups-bordered" commandName="boundary" id="boundaryCreate">
                            <div class="panel panel-primary" data-collapsed="0">
                                <div class="panel-heading">
                                    <div class="panel-title">
                                        <strong><spring:message code="lbl.hdr.createBoundary"/></strong>
                                    </div>
                                </div>

                                <div class="panel-body custom-form">
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label"><spring:message code="lbl.hierarchyType"/></label>
                                        <div class="col-sm-6" style="padding-top: 7px">
                                            <strong><c:out value="${boundaryType.hierarchyType.name}"/></strong>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label">
                                            <spring:message code="lbl.boundaryType"/>
                                        </label>
                                        <div class="col-sm-6" style="padding-top: 7px">
                                            <strong><c:out value="${boundaryType.name}"/></strong>
                                            <input type="hidden" name="boundaryType" value="<c:out value="${boundaryType.id}" />"/>
                                        </div>
                                    </div>
                                    <div class="panel-body custom-form">
                                        <c:choose>
                                            <c:when test="${not empty parentBoundary}">
                                                <div class="form-group">
                                                    <label class="col-sm-3 control-label">
                                                        <spring:message code="lbl.parent.boundary.name"/>
                                                        <span class="mandatory"></span>
                                                    </label>
                                                    <div class="col-sm-6 add-margin">
                                                        <form:select path="parent" id="hierarchyTypeSelect" cssClass="form-control" cssErrorClass="form-control error" required="true">
                                                            <form:option value=""> <spring:message code="lbl.select"/> </form:option>
                                                            <form:options items="${parentBoundary}" itemValue="id" itemLabel="name"/>
                                                        </form:select>
                                                        <form:errors path="parent" cssClass="error-msg"/>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="form-group">
                                                    <label class="col-sm-3 control-label">
                                                        <spring:message code="lbl.parent.boundary.name"/>
                                                    </label>
                                                    <div class="col-sm-6 add-margin">
                                                        <form:select path="parent"
                                                                     id="hierarchyTypeSelect" cssClass="form-control" cssErrorClass="form-control error">
                                                            <form:option value=""> <spring:message code="lbl.select"/> </form:option>
                                                            <form:options items="${parentBoundary}" itemValue="id" itemLabel="name"/>
                                                        </form:select>
                                                        <form:errors path="parent" cssClass="error-msg"/>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">
                                                <spring:message code="lbl.name"/><span class="mandatory"></span>
                                            </label>
                                            <div class="col-sm-6">
                                                <form:input path="name" id="name" type="text" class="form-control low-width patternvalidation" data-pattern="specialName" placeholder="" autocomplete="off" required="required"/>
                                                <form:errors path="name" cssClass="add-margin error-msg"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">
                                                <spring:message code="lbl.code"/><span class="mandatory"></span>
                                            </label>
                                            <div class="col-sm-6">
                                                <form:input path="code" id="code" type="text" class="form-control low-width patternvalidation" data-pattern="masterCode" placeholder="" autocomplete="off"  maxlength="25"/>
                                                <form:errors path="code" cssClass="add-margin error-msg"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">
                                                <spring:message code="lbl.local.name"/>
                                            </label>
                                            <div class="col-sm-6">
                                                <form:input path="localName" id="name" type="text" class="form-control low-width patternvalidation" data-pattern="specialName" placeholder="" autocomplete="off"/>
                                                <form:errors path="localName" cssClass="add-margin error-msg"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">
                                                <spring:message code="lbl.boundary.number"/><span class="mandatory"></span>
                                            </label>
                                            <div class="col-sm-6">
                                                <form:input path="boundaryNum" id="name" type="text" class="form-control low-width is_valid_number" placeholder="" autocomplete="off" required="required"/>
                                                <form:errors path="boundaryNum" cssClass="add-margin error-msg"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">
                                                <spring:message code="lbl.fromDate"/><span class="mandatory"></span>
                                            </label>
                                            <div class="col-sm-6">
                                                <form:input path="fromDate" id="boundaryFromDate" type="text" class="form-control low-width datepicker" data-inputmask="'mask': 'd/m/y'" placeholder="" autocomplete="off" required="required"/>
                                                <form:errors path="fromDate" cssClass="add-margin error-msg"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">
                                                <spring:message code="lbl.toDate"/>
                                            </label>
                                            <div class="col-sm-6">
                                                <form:input path="toDate" id="boundaryToDate" type="text" class="form-control low-width datepicker" data-inputmask="'mask': 'd/m/y'" placeholder="" autocomplete="off"/>
                                                <form:errors path="toDate" cssClass="add-margin error-msg"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label"><spring:message code="lbl.isactive"/></label>
                                            <div class="col-sm-6 add-margin">
                                                <form:checkbox path="active" checked="checked"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="text-center">
                                    <button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
                                    <a href="javascript:void(0)" class="btn btn-default" id="backBtnId">
                                        <spring:message code="lbl.create"/>&nbsp;<spring:message code="lbl.search"/>
                                    </a>
                                    <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
                                </div>
                            </div>
                        </form:form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<script src="<cdn:url  value='/resources/js/app/boundary-create.js?rnd=${app_release_no}'/>"></script>
