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
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div id="message" class="success">
                <spring:message code="${message}"/>
            </div>
        </c:if>
        <form:form method="post" class="form-horizontal form-groups-bordered"
                   modelAttribute="crossHierarchyRequest" id="crossHierarchyForm" action="crosshierarchyFormForUpdate"
                   name="crossHierarchyForm">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title">
                        <strong><spring:message code="title.crosshierarchy.mapping"/></strong>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label"><spring:message
                            code="lbl.boundaryType"/><span
                            class="mandatory"></span></label>
                    <div class="col-sm-6">
                        <form:input path="" id="boundaryType" value="${boundaryType.hierarchyType.name}"
                                    type="text" class="form-control low-width" placeholder=""
                                    autocomplete="off" required="required" readonly="true"/>
                        <form:hidden path="boundaryType" id="boundaryType" value="${boundaryType.id}"/>
                        <div class="error-msg eithererror all-errors display-hide"></div>
                    </div>
                </div>

                <div class="panel-body custom-form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><spring:message
                                code="lbl.boundary"/><span
                                class="mandatory"></span></label>

                        <div class="col-sm-6">
                            <form:input path="" id="boundary" type="text"
                                        value="${boundary.name}" class="form-control low-width"
                                        placeholder="" autocomplete="off" readonly="true"
                                        required="required"/>
                            <form:hidden path="boundary" id="boundary" value="${boundary.id}"/>
                        </div>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 text-center">
                    <div class="panel panel-primary" data-collapsed="0">
                        <div class="panel-heading">
                            <div class="panel-title">
                                <spring:message code="title.location.mapping"/>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-xs-5">
                                    <div><spring:message code="lbl.crosshierarchy.location"/></div>
                                    <form:select path="" multiple="true" size="10" name="from[]"
                                                 id="multiselect" cssClass="form-control"
                                                 cssErrorClass="form-control error">
                                        <form:options items="${boundaries}" itemValue="id"
                                                      itemLabel="name"/>
                                    </form:select>

                                </div>
                                <div class="col-xs-2">
                                    <div>&nbsp;</div>

                                    <button type="button" id="multiselect_rightSelected"
                                            class="btn btn-block btn-default">
                                        <i class="glyphicon glyphicon-chevron-right"></i>
                                    </button>
                                    <button type="button" id="multiselect_leftSelected"
                                            class="btn btn-block btn-default">
                                        <i class="glyphicon glyphicon-chevron-left"></i>
                                    </button>

                                </div>
                                <div class="col-xs-5">
                                    <div><spring:message code="lbl.mapped.location"/></div>

                                    <form:select path="boundaries" multiple="true" size="10" name="to[]"
                                                 id="multiselect_to" cssClass="form-control"
                                                 cssErrorClass="form-control error" required="required">
                                        <form:options items="${mappedBoundary}" itemValue="id"
                                                      itemLabel="name"/>
                                    </form:select>

                                    <form:errors path="boundaries" cssClass="error-msg"/>
                                </div>
                            </div>
                            <div>&nbsp;</div>
                            <div>&nbsp;</div>
                            <div class="form-group">
                                <div class="text-center">
                                    <button type="button" class="btn btn-primary" id="crosshierarchysave"><spring:message
                                            code="lbl.save"/></button>
                                    <button type="button" class="btn btn-default" id="searchbtn"><spring:message code="lbl.search"/></button>
                                    <button type="button" class="btn btn-default" data-dismiss="modal"
                                            onclick="self.close()">
                                        <spring:message code="lbl.close"/>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/multiselect.js'/>"></script>
<script src="<cdn:url  value='/resources/js/app/cross-hierarchy.js'/>"></script>