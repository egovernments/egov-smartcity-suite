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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row" id="page-content">
    <div class="col-md-12">
        <div class="panel" data-collapsed="0">
            <div class="panel-body">
                <c:if test="${not empty message}">
                    <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
                </c:if>
                <form:form method="get" class="form-horizontal form-groups-bordered" modelAttribute="boundary" id="boundaryViewForm">
                    <div class="panel panel-primary" data-collapsed="0">
                        <div class="panel-heading">
                            <div class="panel-title">
                                <strong><spring:message code="lbl.hdr.viewBoundary"/> ${boundary.localName}</strong>
                            </div>
                        </div>

                        <div class="panel-body custom-form">
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message code="lbl.hierarchyType"/></label>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <strong><c:out value="${boundary.boundaryType.hierarchyType.name}"/></strong>
                                    <input type="hidden" id="hierarchyType" value="<c:out value="${boundary.boundaryType.hierarchyType.id}" />"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">
                                    <spring:message code="lbl.boundaryType"/>
                                </label>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <strong><c:out value="${boundary.boundaryType.name}"/></strong>
                                    <input type="hidden" id="boundaryType" value="<c:out value="${boundary.boundaryType.id}" />"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">
                                    <spring:message code="lbl.name"/>
                                </label>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <strong><c:out value="${boundary.name}"/></strong>
                                    <input type="hidden" id="boundary" value="<c:out value="${boundary.id}" />"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">
                                    <spring:message code="lbl.code"/>
                                </label>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <strong><c:out value="${boundary.code}"/></strong>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">
                                    <spring:message code="lbl.local.name"/>
                                </label>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <strong><c:out value="${boundary.localName}" default="NA"/></strong>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">
                                    <spring:message code="lbl.parent.boundary.name"/>
                                </label>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <strong><c:out value="${boundary.parent.name}" default="NA"/></strong>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">
                                    <spring:message code="lbl.boundary.number"/>
                                </label>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <strong><c:out value="${boundary.boundaryNum}"/></strong>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">
                                    <spring:message code="lbl.fromDate"/>
                                </label>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <fmt:formatDate value="${boundary.fromDate}" pattern="dd/MM/yyyy" var="fromDate"/>
                                    <strong><c:out value="${fromDate}"/></strong>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">
                                    <spring:message code="lbl.toDate"/>
                                </label>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <fmt:formatDate value="${boundary.toDate}" pattern="dd/MM/yyyy" var="toDate"/>
                                    <strong><c:out value="${toDate}" default="NA"/></strong>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">
                                    <spring:message code="lbl.isactive"/>
                                </label>
                                <div class="col-sm-6" style="padding-top: 7px">
                                    <strong><c:out value="${boundary.active ? 'Yes' : 'No'}" default="NA"/></strong>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="text-center">
                            <c:if test="${create}">
                                <button id="buttonCreate" class="btn btn-primary">
                                    <spring:message code="lbl.create"/>&nbsp;<spring:message code="lbl.next"/>
                                </button>
                                <button id="buttonCreateSearch" class="btn btn-primary">
                                    <spring:message code="lbl.create"/>&nbsp;<spring:message code="lbl.search"/>
                                </button>
                            </c:if>
                            <c:if test="${edit}">
                                <button id="buttonEdit" class="btn btn-primary">
                                    <spring:message code="lbl.edit"/>
                                </button>
                                <button id="buttonEditSearch" class="btn btn-primary">
                                    <spring:message code="lbl.edit"/>&nbsp;<spring:message code="lbl.search"/>
                                </button>
                            </c:if>
                            <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">
                                <spring:message code="lbl.close"/>
                            </a>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<script>
    $('#buttonCreate').click(function () {
        $('#boundaryViewForm').attr('action', '/egi/boundary/create/' + $('#boundaryType').val());
    });

    $('#buttonCreateSearch').click(function () {
        $('#boundaryViewForm').attr('action', '/egi/boundary/create');
    });

    $('#buttonEdit').click(function () {
        $('#boundaryViewForm').attr('action', '/egi/boundary/update/' + $('#boundary').val());
    });

    $('#buttonEditSearch').click(function () {
        $('#boundaryViewForm').attr('action', '/egi/boundary/update/');
    });

</script>
