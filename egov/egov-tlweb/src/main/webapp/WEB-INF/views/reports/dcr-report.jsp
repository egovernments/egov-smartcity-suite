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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row" id="page-content">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert">
                <spring:message code="${message}"/>
            </div>
        </c:if>
        <div class="panel panel-primary" data-collapsed="0">
            <div class="panel-heading">
                <div class="panel-title">
                    <spring:message code="title.dailycollection"/>
                </div>
            </div>
            <div class="panel-body custom-form">
                <form:form class="form-horizontal form-groups-bordered" id="dailyCollectionform" modelAttribute="dcrSearchRequest" method="post">


                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.fromdate"/>
                            <span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <fmt:formatDate value="${currentDate}" var="currentDate" pattern="dd/MM/yyyy"/>
                            <form:input path="fromDate" name="fromDate" id="fromDate"
                                        cssClass="form-control datepicker" value="${currentDate}"
                                        cssErrorClass="form-control error" required="required"/>
                        </div>

                        <label class="col-sm-2 control-label">
                            <spring:message code="lbl.todate"/>
                            <span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:input path="toDate" name="toDate" id="toDate"
                                        cssClass="form-control datepicker" value="${currentDate}"
                                        cssErrorClass="form-control error" required="required"/>
                        </div>

                    </div>

                    <div class="form-group">

                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.collectionOperator"/>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select name="collectionOperator" id="collectionOperator" path="" cssClass="form-control"
                                         cssErrorClass="form-control error">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${operators}" id="collectionOperator" name="collectionOperator"
                                              itemValue="name" itemLabel="name"/>
                            </form:select>
                        </div>

                        <label class="col-sm-2 control-label">
                            <spring:message code="lbl.receipt.status"/>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select id="status" name="status" path="status" cssClass="form-control" cssErrorClass="form-control error">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${status}" itemValue="description" itemLabel="description"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <spring:message code="lbl.ward"/>
                        </label>
                        <div class="col-sm-3 add-margin">
                            <form:select name="revenueWard" id="revenueward" path="revenueWard" cssClass="form-control" cssErrorClass="form-control error">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${wards}" id="revenueWard" name="revenueWard" itemValue="name" itemLabel="name"/>
                            </form:select>
                        </div>
                    </div>

                    <div class="form-group add-margin">
                        <div class="text-center">
                            <button type="button" class="btn btn-primary" id="dailyCollectionReportSearch"><spring:message code="lbl.search"/></button>
                            <a href="javascript:void(0)" class="btn btn-default" data-dismiss="modal" onclick="self.close()">
                                <spring:message code="lbl.close"/></a>
                        </div>
                    </div>
                </form:form>
                <div id="dailyCollectionReport-header" class="panel-body">
                    <br/>
                    <span class="table-header">
                    <spring:message code="lbl.dailyCollection.report.details" arguments="${fromDate},${toDate}" htmlEscape="false"/>
                </span>
                    <table class="table table-bordered table-hover multiheadertbl" id="dailyCollReport-table"
                           style="overflow-x: auto;max-width: 100%;min-width: 100%">
                        <tbody>
                        <tfoot id="report-footer">
                        <tr>
                            <td colspan="9"></td>
                            <td>Total</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        </tfoot>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/dataTables.buttons.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.bootstrap.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.flash.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/jszip.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/pdfmake.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/vfs_fonts.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.html5.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/extensions/buttons/buttons.print.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/js/app/dcr-report.js?rnd=${app_release_no}'/>"></script>
