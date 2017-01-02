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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>


<div class="row" id="page-content">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert">
                <spring:message code="${message}"/>
            </div>
        </c:if>
        <form:form role="form" method="post" modelAttribute="licenseSubCategory" class="form-horizontal form-groups-bordered">
        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-heading">
                <div class="panel-title"><strong><spring:message code="title.subcategory.view"/></strong></div>
            </div>
            <div class="panel-body custom-form">
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <spring:message code="licenseCategory.category.lbl"/><span class="mandatory"></span>
                    </label>
                    <div class="col-sm-3 add-margin">
                        <form:input path="category.name" cssClass="form-control" cssErrorClass="form-control error" disabled="true"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label text-right">
                        <spring:message code="lbl.name"/><span id="mandatory" class="mandatory"></span>
                    </label>
                    <div class="col-sm-3 add-margin">
                        <form:input path="name" id="name" cssClass="form-control" cssErrorClass="form-control error" disabled="true"/>
                        <form:errors path="name" cssClass="error-msg"/>
                    </div>
                    <label for="code" class="col-sm-2 control-label text-right">
                        <spring:message code="lbl.code"/><span id="mandatory" class="mandatory"></span>
                    </label>
                    <div class="col-sm-3 add-margin">
                        <form:input path="code" id="code" cssClass="form-control" cssErrorClass="form-control error" disabled="true"/>
                        <form:errors path="code" cssClass="error-msg"/>
                    </div>
                </div>
                <div class="panel-heading">
                    <div class="col-md-12 panel-title text-left">
                        <strong>
                            <spring:message code="title.subcategory.view.details"/>
                        </strong>
                    </div>
                </div>
                <div class="col-md-12">
                    <table class="table table-bordered " id="subcat">
                        <thead>
                        <tr>
                            <th><spring:message code="lbl.feetype"/><span class="mandatory"></span></th>
                            <th><spring:message code="lbl.rateType"/><span class="mandatory"></span></th>
                            <th><spring:message code="license.uom.lbl"/><span class="mandatory"></span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${licenseSubCategory.licenseSubCategoryDetails}" var="licenseSubCategoryDetail" varStatus="item">
                            <tr>
                                <td>
                                    <form:input path="licenseSubCategoryDetails[${item.index}].feeType.name" cssClass="form-control feeType" disabled="true"/>
                                </td>
                                <td>
                                    <form:input path="licenseSubCategoryDetails[${item.index}].rateType" cssClass="form-control rateType" disabled="true"/>
                                </td>
                                <td>
                                    <form:input path="licenseSubCategoryDetails[${item.index}].uom.name" cssClass="form-control uom" disabled="true"/>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="text-center">
            <button type="button" class="btn btn-default" data-dismiss="modal" onclick="self.close()">
                <spring:message code="lbl.close"/>
            </button>
        </div>
    </div>
    </form:form>
</div>
<script src="<cdn:url  value='/resources/js/app/subcategory.js?rnd=${app_release_no}'/>"></script>
