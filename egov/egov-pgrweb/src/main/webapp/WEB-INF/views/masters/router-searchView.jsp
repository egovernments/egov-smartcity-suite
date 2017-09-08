<%--
  ~ eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) <2017>  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~ 	Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~         derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~ 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~ 	For any further queries on attribution, including queries on brand guidelines,
  ~         please contact contact@egovernments.org
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<div class="row">
    <div class="col-md-12">
        <div class="" data-collapsed="0">
            <form:form id="searchrouter" method="post" class="form-horizontal form-groups-bordered"
                       modelAttribute="complaintRouter">
                <div class="panel panel-primary" data-collapsed="0">
                    <div class="panel-heading ">
                        <div class="panel-title">
                            <strong><spring:message code="lbl.router.heading.search"/></strong>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label class="col-sm-3 control-label"><spring:message code="lbl.router.complaintType"/>
                            </label>
                            <div class="col-sm-6">
                                <input id="com_type" type="text" class="form-control typeahead is_valid_alphabet"
                                       placeholder="" autocomplete="off"/>
                                <form:hidden path="complaintType" id="complaintTypeId"/>
                                <form:errors path="complaintType" cssClass="add-margin error-msg"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label"><spring:message
                                    code="lbl.router.boundaryType"/></label>
                            <div class="col-sm-6 add-margin">
                                <select id="boundary_type_id" class="form-control">
                                    <option value=""><spring:message code="lbl.select"/></option>
                                    <c:forEach items="${boundaryTypes}" var="boundaryType">
                                        <option value="${boundaryType.id}"> ${boundaryType.name}</option>
                                    </c:forEach>
                                </select>
                                <input type="hidden" id="hiddenBoundaryTypeId" name="boundaryTypeId" value="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label"><spring:message code="lbl.router.boundary"/></label>
                            <div class="col-sm-6">
                                <input id="com_boundry" type="text" class="form-control typeahead" placeholder=""
                                       autocomplete="off"/>
                                <form:hidden path="boundary" id="boundaryId"/>
                                <form:errors path="boundary" cssClass="error-msg"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="text-center">
                                <button type="button" id="routerSearch" class="btn btn-primary">
                                    <spring:message code="lbl.router.search"/>
                                </button>
                                <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">
                                    <spring:message code="lbl.close"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <table class="table table-bordered datatable dt-responsive" id="com_routing_search"></table>
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
<script type="text/javascript" src="<cdn:url
        value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url  value='/resources/js/app/complaintrouterviewsearch.js?rnd=${app_release_no}'/>"></script>
