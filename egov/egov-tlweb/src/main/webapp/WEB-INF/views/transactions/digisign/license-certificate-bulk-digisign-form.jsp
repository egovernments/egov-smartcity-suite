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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="row">
    <div class="col-md-12">
        <form:form role="form" action="" class="form-horizontal form-groups-bordered" id="bulkdigisignform"
                   modelAttribute="applicationType" method="POST">
            <c:choose>
                <c:when test="${not empty message}">
                    <div class="alert alert-info view-content">
                        <spring:message code="${message}"/>
                    </div>
                    <div class="text-center">
                        <a href="javascript:void(0)" class="btn btn-default"
                           onclick="self.close()"><spring:message code="lbl.close"/></a>
                    </div>
                </c:when>
                <c:otherwise>
                    <input type="hidden" id="licenseIds" name="licenseIds">
                    <input type="hidden" id="licenseTypeId" name="licenseAppTypeId">
                    <div class="panel panel-primary" data-collapsed="0">
                        <div class="panel-heading">
                            <div class="panel-title">
                                <strong><spring:message code="lbl.digitalSignature"/></strong>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label text-right"> <spring:message
                                    code='lbl.licenseapptype'/></label>
                            <div class="col-sm-4 add-margin">
                                <form:select path="" id="appType" cssClass="form-control"
                                             cssErrorClass="form-control error">
                                    <form:option value="0">All</form:option>
                                    <form:options items="${applicationType}" itemValue="id" itemLabel="name"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="row" id="searchlicense">
                            <div class="text-center">
                                <button type="button" id="btnsearch" class="btn btn-primary">
                                    <spring:message code="lbl.search"/>
                                </button>
                                <button type="button" class="btn btn-default" data-dismiss="modal"
                                        onclick="window.close();">
                                    <spring:message code="lbl.close"/>
                                </button>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="main-content">
                                <div class="row">
                                    <c:if test="${not empty licenses}">
                                        <div class="col-md-12">
                                            <table class="table table-bordered datatable" id="digSignDetailsTab"
                                                   name="digSignDetailsTab">
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="lbl.digitalSignature.type"/></th>
                                                    <th><spring:message code="search.licensee.no"/></th>
                                                    <th><spring:message code="license.applicationnumber"/></th>
                                                    <th><spring:message code="lbl.category"/></th>
                                                    <th><spring:message code="lbl.subcategory"/></th>
                                                    <th><spring:message code="lbl.amount"/></th>
                                                    <th><spring:message code="lbl.view"/></th>
                                                    <th style="text-align: center">
                                                        <input type="checkbox" id="selectAll"/><spring:message
                                                            code="lbl.digitalSignature.select"/></th>
                                                </tr>
                                                </thead>

                                                <c:forEach items="${licenses}" var="license" varStatus="counter">
                                                    <tr>
                                                        <td class="col-md-2">${license.state.natureOfTask}</td>
                                                        <td>${license.licenseNumber}</td>
                                                        <td>${license.applicationNumber}</td>
                                                        <td>${license.category.name}</td>
                                                        <td> ${license.tradeName.name}</td>
                                                        <td> ${license.getLatestAmountPaid()}</td>
                                                        <td>
                                                            <a name="view" class="btn btn-secondary"
                                                               onclick="openLicenseForDigitalSign('${license.id}')">
                                                                <spring:message code='lbl.view'/><i
                                                                    class="fa fa-eye"></i>
                                                            </a>
                                                        </td>
                                                        <td>
                                                            <input type="checkbox" class="check-box"
                                                                   id="${license.id}" name="rowCheckBox"/>
                                                        </td>
                                                    </tr>
                                                </c:forEach>

                                            </table>
                                            <div class="text-center">
                                                <button type="submit" onclick="return submitform();"
                                                        class="btn btn-primary"
                                                        id="submitButton">
                                                    <spring:message code="lbl.sign"/>
                                                </button>

                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${licenses != null && empty licenses}">
                                        <div class="text-center col-sm-12 alert alert-warning">
                                            <spring:message code="lbl.norecord.found"/>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </form:form>
    </div>
</div>
<script type="text/javascript"
        src="<cdn:url value='/resources/js/app/license-certificate-bulk-digisign-form.js?rnd=${app_release_no}'/>"></script>
