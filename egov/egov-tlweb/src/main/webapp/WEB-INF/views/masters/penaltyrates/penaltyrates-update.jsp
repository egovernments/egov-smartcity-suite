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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<form:form role="form" modelAttribute="penaltyRate" cssClass="form-horizontal form-groups-bordered">
    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty message}">
                <div class="alert alert-success" role="alert">
                    <spring:message code="${message}"/>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    <spring:message code="${error}" arguments="${penaltyRate.licenseAppType.name}"/>
                </div>
            </c:if>
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-body">
                    <div class="panel-heading">
                        <div class="col-md-12 panel-title text-left">
                            &nbsp;
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-5 control-label text-right">
                            <spring:message code="lbl.licenseAppType"/> <span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:label path="licenseAppType" class="form-control text-left">${penaltyRate.licenseAppType.name}</form:label>
                            <form:hidden path="licenseAppType" id="licenseAppType" required="required"/>
                        </div>
                    </div>
                    <div class="panel-heading">
                        <div class="col-md-12 panel-title text-left">
                            <spring:message code="lbl.penaltyrate.details"/>
                        </div>
                    </div>
                    <div class="col-sm-12">
                        <table class="table table-bordered fromto" id="result" data-from="<spring:message code='lbl.from'/>"
                               data-to="<spring:message code='lbl.to'/>">
                            <thead>
                            <th class="text-center"><spring:message code="lbl.from"/></th>
                            <th class="text-center"><spring:message code="lbl.to"/></th>
                            <th class="text-center" colspan="2"><spring:message code="lbl.penaltyrate"/></th>
                            </thead>
                            <tbody>
                            <c:if test="${not empty penaltyRate.penaltyRates}">
                                <c:forEach items="${penaltyRate.penaltyRates}" var="detail" varStatus="vs">
                                    <c:set var="display" value="table-row"/>
                                    <c:if test="${detail.markedForRemoval}">
                                        <c:set var="display" value="none"/>
                                    </c:if>
                                    <tr style="display:${display}">
                                        <td>
                                            <form:input path="penaltyRates[${vs.index}].fromRange" value="${detail.fromRange}"
                                                        cssClass="form-control fromRange patternvalidation fromvalue text-right"
                                                        pattern="-?\d*" data-pattern="numerichyphen" data-fromto="from"
                                                        maxlength="8" required="true"/>
                                            <input type="hidden" name="penaltyRates[${vs.index}].licenseAppType" class="licapptype"
                                                   value="${detail.licenseAppType.id}">
                                            <form:errors path="penaltyRates[${vs.index}].fromRange" cssClass="error-msg"/>
                                        </td>
                                        <td>
                                            <form:input path="penaltyRates[${vs.index}].toRange" value="${detail.toRange}"
                                                        cssClass="form-control patternvalidation tovalue text-right"
                                                        pattern="-?\d*" data-pattern="numerichyphen" data-fromto="to"
                                                        maxlength="8" required="true"/>
                                            <form:errors path="penaltyRates[${vs.index}].toRange" cssClass="error-msg"/>
                                        </td>
                                        <td>
                                            <form:input path="penaltyRates[${vs.index}].rate" value="${detail.rate}"
                                                        cssClass="form-control patternvalidation text-right"
                                                        data-pattern="number" maxlength="8" required="true"/>
                                            <form:errors path="penaltyRates[${vs.index}].rate" cssClass="error-msg"/>
                                            <form:hidden path="penaltyRates[${vs.index}].markedForRemoval"
                                                         id="penaltyRates[${vs.index}].markedForRemoval"
                                                         value="${detail.markedForRemoval}" cssClass="markedForRemoval"/>
                                        </td>
                                        <td><span class="add-padding"><i class="fa fa-trash delete-row" aria-hidden="true"></i></span></td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="4">
                                    <button type="button" class="btn btn-secondary pull-right" id="addrow">
                                        <i class="fa fa-plus-circle" aria-hidden="true"></i> &nbsp;
                                        <spring:message code="lbl.add.more"/>
                                    </button>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                    <div class="form-group text-center">
                        <button type="submit" class="btn btn-primary" id="save-btn"><spring:message code="lbl.save"/></button>
                        <button type="button" class="btn btn-default" onclick="location.reload()"><spring:message code="lbl.reset"/></button>
                        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();">
                            <spring:message code="lbl.close"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form:form>
<script src="<cdn:url  value='/resources/global/js/egov/patternvalidation.js?rnd=${app_release_no}' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/js/app/license-penalty-rates.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/app/value-range-checker.js?rnd=${app_release_no}'/>"></script>