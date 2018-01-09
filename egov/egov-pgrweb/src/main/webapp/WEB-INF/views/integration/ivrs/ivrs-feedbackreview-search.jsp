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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="row">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
        </c:if>
        <form:form class="form-horizontal form-groups-bordered" action=""
                   id="searchform" modelAttribute="ivrsFeedbackSearchRequest" method="get">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title"><spring:message code="title.searchrated.grievance"/>
                    </div>
                </div>
                <div class="panel-body custom-form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right"><spring:message code="lbl.complaint.number"/>
                        </label>
                        <div class="col-sm-3 add-margin" style="margin-bottom: 15px;">
                            <input type="text" name="crn" id="complaintNumber" class="form-control patternvalidation"
                                   data-pattern="alphanumericwithhyphen"
                                   placeholder="<spring:message code='lbl.complaint.number'/>"/>
                            <form:errors path="crn" cssClass="error-msg"/>
                        </div>
                        <label class="col-sm-2 control-label text-right">
                            <spring:message code="title.complaintType"/><span class="mandatory"></span>
                        </label>
                        <div class="col-sm-3 add-margin" style="margin-bottom: 15px;">
                            <form:select path="complaintId" id="complaintId" cssClass="form-control"
                                         cssErrorClass="form-control error" required="required">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${complaintType}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            <form:errors name="categories" cssClass="error-msg"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right">
                            <spring:message code='lbl.created.from'/>
                        </label>
                        <div class="col-sm-3 add-margin" style="margin-bottom:15px;">
                            <form:input path="fromDate" id="fromDate" format="dd/MM/yyyy"
                                        class="form-control datepicker"
                                        data-date-end-date="0d" maxlength="10"/>
                        </div>
                        <label class="col-sm-2 control-label text-right">
                            <spring:message code='lbl.created.to'/>
                        </label>
                        <div class="col-sm-3 add-margin" style="margin-bottom:15px;">
                            <form:input path="toDate" id="toDate" format="dd/MM/yyyy"
                                        class="form-control datepicker"
                                        data-date-end-date="0d" maxlength="10"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label text-right">
                            <spring:message code='lbl.routerescaltion.ward'/>
                        </label>
                        <div class="col-sm-3 add-margin" style="margin-bottom:15px;">
                            <form:select path="locationId" id="location"
                                         class="form-control">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                                <form:options items="${ward}" itemValue="id"
                                              itemLabel="name"/>
                            </form:select>

                        </div>
                        <label class="col-sm-2 control-label text-right">
                            <spring:message code='lbl.locality'/>
                        </label>
                        <div class="col-sm-3 add-margin" style="margin-bottom:15px;">
                            <form:select path="childLocationId"
                                         id="childLocation" cssClass="form-control">
                                <form:option value="">
                                    <spring:message code="lbl.select"/>
                                </form:option>
                            </form:select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="text-center">
                    <button type="submit" id="btnsearch" class="btn btn-primary">
                        <spring:message code="lbl.search"/>
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal" onclick="window.close();">
                        <spring:message code="lbl.close"/>
                    </button>
                </div>
            </div>
        </form:form>
        <div class="row display-hide report-section">
            <table class="table table-bordered table-hover multiheadertbl" id="searchGrievance" width="200%">
                <tbody>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<link rel="stylesheet"
      href="<cdn:url  value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>">
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script
        src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script src="<cdn:url  value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url value='/resources/js/app/ivrs-feedbackreview-search.js?rnd=${app_release_no}'/>"></script>