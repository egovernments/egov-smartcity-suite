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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="row" id="page-content">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
        </c:if>
        <form:form mothod="post" class="form-horizontal form-groups-bordered" modelAttribute="appConfig" id="appConfigForm">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title">
                        <strong><spring:message code="lbl.hdr.createAppconfig"/></strong>
                    </div>
                </div>
                <div class="panel-body custom-form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.module"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-6 add-margin">
                            <form:select path="module" id="appModuleName" cssClass="form-control" cssErrorClass="form-control error" required="required">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${modules}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            <div class="error-msg positionerror all-errors display-hide"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.AppconfigKeyName"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-6">
                            <form:input path="keyName" id="keyName" type="text" class="form-control low-width" autocomplete="off" required="required"/>
                            <form:errors path="keyName" cssClass="add-margin error-msg"/>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.description"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-6">
                            <form:input path="description" id="description" type="text" class="form-control low-width" autocomplete="off" required="required"/>
                            <form:errors path="description" cssClass="add-margin error-msg"/>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <table class="table table-bordered" id="configs">
                            <tr>
                                <th><spring:message code="lbl.date"/><span class="mandatory"></span></th>
                                <th><spring:message code="lbl.values"/><span class="mandatory"></span></th>
                                <th><spring:message code="lbl.operation"/></th>
                            </tr>
                            <c:choose>
                                <c:when test="${not empty appConfig.confValues}">
                                    <c:forEach items="${appConfig.confValues}" var="configValue"
                                               varStatus="counter">
                                        <tr>
                                            <td class="blueborderfortd">
                                                <fmt:formatDate value="${configValue.effectiveFrom}" var="effectiveDt" pattern="dd/MM/yyyy"/>
                                                <form:input path="confValues[${counter.index}].effectiveFrom" cssClass="form-control datepicker" value="${effectiveDt}"
                                                       id="confValues[${counter.index}].effectiveFrom" required="required"/>
                                                <form:errors path="confValues[${counter.index}].effectiveFrom" cssClass="add-margin error-msg"/>
                                            </td>
                                            <td class="blueborderfortd">
                                                <form:input path="confValues[${counter.index}].value" cssClass="form-control low-width" value="${configValue.value}"
                                                       id="confValues[${counter.index}].value" required="required"/>
                                                <form:errors path="confValues[${counter.index}].value" cssClass="add-margin error-msg"/>
                                            </td>
                                            <td id="rowadddelete">
                                                <input type="button" class="btn btn-primary" value="Add" name="Add" id="add">
                                                <input type="button" class="btn btn-primary" name="Delete" value="Delete" id="delete">
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr id="Floorinfo">
                                        <td class="blueborderfortd">
                                            <form:input path="confValues[0].effectiveFrom" cssClass="form-control datepicker"
                                                   id="confValues[0].effectiveFrom" required="required"/>
                                            <form:errors path="confValues[0].effectiveFrom" cssClass="add-margin error-msg"/>
                                        </td>
                                        <td class="blueborderfortd">
                                            <form:input path="confValues[0].value" cssClass="form-control low-width"
                                                   id="confValues[0].value" required="required"/>
                                            <form:errors path="confValues[0].value" cssClass="add-margin error-msg"/>
                                        </td>
                                        <td id="rowadddelete">
                                            <input type="button" class="btn btn-primary" value="Add" name="Add" id="add">
                                            <input type="button" class="btn btn-primary" name="Delete" value="Delete" id="delete">
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </table>
                    </div>
                    <div class="col-md-12 text-center">
                        <div class="add-margin">
                            <button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
                            <button type="reset" class="btn btn-default">
                                <spring:message code="lbl.reset"/>
                            </button>
                            <button type="button" class="btn btn-default"
                                    data-dismiss="modal" onclick="self.close()">
                                <spring:message code="lbl.close"/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>
<script src="<cdn:url cdn='${applicationScope.cdn}'  value='/resources/js/app/appconfig.js?rnd=${app_release_no}'/>"></script>