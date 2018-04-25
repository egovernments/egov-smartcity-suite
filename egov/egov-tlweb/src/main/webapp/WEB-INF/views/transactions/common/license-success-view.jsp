<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row printable">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert">
                <spring:message code="${message}"
                                arguments="${tradeLicense.licenseAppType.name},${approverName}
                                ,${initiatorPosition},${ownerPosition},${tradeLicense.licenseNumber}
                                ,${tradeLicense.applicationNumber}"/></div>
        </c:if>
        <form:form action="" modelAttribute="tradeLicense" theme="simple">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading  custom_form_panel_heading subheadnew">
                    <div class="panel-title">
                        <c:choose>
                            <c:when test="${tradeLicense.status.statusCode eq 'ACK'}">
                                <spring:message code='title.license.acknowldgement'
                                                arguments="${tradeLicense.licenseAppType.name}"/>
                            </c:when>
                            <c:otherwise>
                                <spring:message code='page.title.viewtrade'/>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="panel-body">

                    <div class="row add-border">
                        <div class="col-xs-3 add-margin" style="font-size: 14px;">
                            <spring:message code="license.applicationnumber"/>
                        </div>
                        <div class="col-xs-3 add-margin view-content">
                                ${tradeLicense.applicationNumber}
                        </div>
                        <div class="col-xs-3 add-margin" style="font-size: 14px;">
                            <spring:message code="dateofapplication.lbl"/></div>
                        <div class="col-xs-3 add-margin view-content">
                            <fmt:formatDate pattern="dd/MM/yyyy" type="date" value="${tradeLicense.applicationDate}"
                                            var="applicationDate"/>
                                ${applicationDate}
                        </div>
                    </div>

                    <div class="row add-border">
                        <div class="col-xs-3 add-margin">
                            <spring:message code='license.licensenumber'/>
                        </div>
                        <div class="col-xs-3 add-margin view-content">
                            <c:out value="${tradeLicense.licenseNumber}" default="N/A"/>
                        </div>
                        <div class="col-xs-3 add-margin">
                            <spring:message code='licensee.applicantname'/>
                        </div>
                        <div class="col-xs-3 add-margin view-content">
                                ${tradeLicense.licensee.applicantName}
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xs-3 add-margin">
                            <spring:message code='licensee.address'/>
                        </div>
                        <div class="col-xs-3 add-margin view-content">
                                ${tradeLicense.licensee.address}
                        </div>
                        <div class="col-xs-3 add-margin">
                            <spring:message code='search.license.establishmentname'/>
                        </div>
                        <div class="col-xs-3 add-margin view-content">
                                ${tradeLicense.nameOfEstablishment}
                        </div>
                    </div>
                </div>
            </div>

        </form:form>
    </div>
</div>
<div class="row">
    <div class="text-center">
        <c:choose>
            <c:when test="${tradeLicense.status.statusCode eq 'ACK'}">
                <button type="submit" class="btn btn-default printbtn"><spring:message code="lbl.print"/></button>
            </c:when>
        </c:choose>
        <button type="button" id="btnclose" class="btn btn-default printbtn" onclick="window.close();">
            <spring:message code="lbl.close"/>
        </button>
    </div>
</div>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/jQuery.print.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>