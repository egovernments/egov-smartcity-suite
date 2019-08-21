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

<%@ page contentType="text/html" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
    <div class="col-md-12">

        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-heading">
                <div class="panel-title">
                    <c:if test="${!citizenRole}">
                    <strong>
                            <spring:message code='title.watertaxSearch'/>
                        </c:if>
                        <c:if test="${citizenRole}">
                        <strong><spring:message code='lbl.headerforsearch.connection'/></strong>
                        </c:if>
                </div>
            </div>
            <div class="panel-body">
                <form:form class="form-horizontal form-groups-bordered"
                           id="waterSearchRequestForm"
                           modelAttribute="connectionSearchRequest" action="">
                    <input type="hidden" id="citizenRole" value="${citizenRole}"/>
                    <c:if test="${!citizenRole }">
                        <div class="form-group">
                            <label class="col-md-4 control-label"> <spring:message
                                    code="lbl.mobileNo"/></label>
                            <div class="col-md-4 add-margin">
                                <form:input path="mobileNumber" type="text" id="mobileNumber"
                                            class="form-control is_valid_number" maxlength="10"
                                            data-inputmask="'mask': '9999999999'" min="10"/>
                            </div>
                        </div>
                    </c:if>
                    <div class="form-group">
                        <label class="col-md-4 control-label"><spring:message
                                code='lbl1.citizeconsumer.number'/></label>
                        <div class="col-md-4 add-margin">
                            <input type="text" name="consumerCode" id="consumerCode"
                                   class="form-control patternvalidation" data-pattern="number"
                                   maxlength="15" id="app-appcodo"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label"> <spring:message code="lbl.assesmentnumber"/></label>
                        <div class="col-md-4 add-margin">
                            <input type="text" name="propertyid" id="propertyid"
                                   class="form-control patternvalidation"
                                   data-pattern="number" maxlength="16" id="assessmentno"/>
                            <form:errors path="propertyid" cssClass="error-msg"/>
                        </div>
                    </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"> <spring:message code="lbl.oldconsumerno"/></label>
                            <div class="col-md-4 add-margin">
                                <input type="text" name="oldConsumerNumber" id="oldConsumerNumber"
                                       class="form-control patternvalidation" 
                                       maxlength="50" id="app-oldcode"/>
                            </div>
                        </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label"> <spring:message
                                code="lbl.assessee.name"/></label>
                        <div class="col-md-4 add-margin">
                            <input type="text" name="applicantName" id="applicantName"
                                   class="form-control patternvalidation" maxlength="100"/>
                            <form:errors path="applicantName" id="applicantNameError" cssClass="error-msg"/>
                        </div>
                    </div>
                    <c:if test="${!citizenRole}">
                        <div class="form-group">
                            <label class="col-md-4 control-label"><spring:message code='lbl.locality'/> </label>
                            <div class="col-md-4 add-margin">
                                <input type="text" name="locality" id="locality"
                                       class="form-control patternvalidationclass" maxlength="32"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label"><spring:message code="lbl.revenue.ward"/></label>
                            <div class="col-md-4 add-margin">
                                <select name="revenueWard" id="revenueWard" class="form-control"
                                        data-first-option="false">
                                    <option value="${ward.name}"></option>
                                    <c:forEach items="${revenueWards}" var="ward">
                                        <option value="${ward.name}">${ward.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:if>
                    <div class="form-group">
                        <label class="col-md-4 control-label"><spring:message code="lbl.doornumber"/></label>
                        <div class="col-md-4 add-margin">
                            <form:input path="doorNumber" type="text" name="doorNumber"
                                        class="form-control " id="doorno"/>
                            <form:errors path="doorNumber" id="doorNumberError" cssClass="error-msg"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="text-center">
                            <a href="javascript:void(0);" id="searchapprvedapplication"
                               class="btn btn-primary"><spring:message code='lbl.search'/></a>
                            <c:if test="${!citizenRole}">
                                <button class="btn btn-danger" type="reset">
                                    <spring:message code='lbl.reset'/>
                                </button>
                                <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">
									<spring:message code="lbl.close" /></a>
                            </c:if>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-md-6 col-xs-6 table-header">
        <spring:message code='lbl.searchresult'/>
    </div>
    <div class="col-md-6 col-xs-6 add-margin text-right">
        <span class="inline-elem"><spring:message code='lbl.search'/></span>
        <span class="inline-elem"><input type="text" id="searchwatertax" class="form-control input-sm"></span>
    </div>
    <div class="row display-hide report-section" id="searchResultDiv">
        <div class="col-md-12 form-group report-table-container">
            <table class="table table-bordered table-hover" id="aplicationSearchResults">
                <thead>
                <tr>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<strong>
    <div class="align-center" id="refine-search-criteria" style="text-align: center"></div>
</strong>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
        type="text/javascript"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
        type="text/javascript"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"
        type="text/javascript"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"
        type="text/javascript"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
        type="text/javascript"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/moment.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/js/app/connectionsearch.js?rnd=${app_release_no}'/>"
        type="text/javascript"></script>