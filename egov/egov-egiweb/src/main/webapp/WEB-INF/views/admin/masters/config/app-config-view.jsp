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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<link rel="stylesheet"	href="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css'/>">
<div class="row" id="page-content">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-danger" role="alert"><spring:message code="${message}"/></div>
        </c:if>

        <form id="searchAppconfigForm" method="post" class="form-horizontal form-groups-bordered">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title">
                        <strong><spring:message code="title.searchAppConfig"></spring:message></strong>
                    </div>
                </div>

                <div class="panel-body custom-form">
                    <div class="form-group">
                        <label class="col-sm-3 control-label"><spring:message code="lbl.module"/></label>
                        <div class="col-sm-6 add-margin">
                            <select id="moduleName" class="form-control">
                                <option value=""> <spring:message code="lbl.select"/> </option>
                                <c:forEach items="${modules}" var="module">
                                    <option value="${module.name}">${module.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="text-center">
                    <button type="button" class="btn btn-primary" id="search-view-btn"><spring:message code="lbl.search"/></button>
                    <button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
                    <button type="button" class="btn btn-default" data-dismiss="modal" onclick="self.close()">
                        <spring:message code="lbl.close"/>
                    </button>
                </div>
            </div>
            <table class="table table-bordered" id="view-appConfig-tbl"></table>
        </form>
    </div>
</div>
<style>
td.details-control,tr.shown td.details-control{
	position:relative;
}
td.details-control:before {
    content: "\f055";
    font-family: FontAwesome;
    position:absolute;
    top: 8px;
    font-size: 16px;
}
tr.shown td.details-control:before {
    content: "\f056"; 
    font-family: FontAwesome;
    position:absolute;
    top: 8px;
    font-size: 16px;
}
.table tbody thead:first-child > tr:first-child th {
    background: #fff;
    color: #333;
    font-family: semibold;
}
.table thead:first-child > tr:first-child th:first-child{
	border-left: 1px solid #ccc;
}
</style>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js'/>"></script>
<script	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js'/>"></script>
<script	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js'/>"></script>
<script src="<cdn:url  value='/resources/js/app/appconfig.js?rnd=${app_release_no}'/>"></script>