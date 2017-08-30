<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) 2016  eGovernments Foundation
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

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<link rel="stylesheet" href="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css'/>">
<div class="row" id="page-content">
    <div class="col-md-12">
        <div class="panel" data-collapsed="0">
            <div class="panel-body">
                <form:form id="boundarySearchForm" class="form-horizontal form-groups-bordered">
                    <div class="panel panel-primary" data-collapsed="0">
                        <div class="panel-heading">
                            <div class="panel-title">
                                <strong><spring:message code="lbl.hdr.searchBoundary"/></strong>
                            </div>
                        </div>

                        <div class="panel-body custom-form">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">
                                    <spring:message code="lbl.hierarchyType"/>
                                    <span class="mandatory"></span>
                                </label>
                                <div class="col-sm-6 add-margin">
                                    <select id="hierarchyTypeSelect" class="form-control" onchange="populateBoundaryTypes(this);" required="required">
                                        <option value=""><spring:message code="lbl.select"/></option>
                                        <c:forEach items="${hierarchyTypes}" var="hierarchyType">
                                            <option value="${hierarchyType.id}">${hierarchyType.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label"><spring:message
                                        code="lbl.boundaryType"/><span class="mandatory"></span></label>
                                <div class="col-sm-6 add-margin">
                                    <egov:ajaxdropdown id="boundaryTypeAjax" fields="['Text','Value']"
                                                       dropdownId="boundaryType" url="boundarytype/ajax/boundarytypelist-for-hierarchy"/>
                                    <select id="boundaryType" class="form-control" required="required">
                                        <option value=""><spring:message code="lbl.select"/></option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="text-center">
                        <button id="searchBtn" type="button" class="btn btn-primary"><spring:message code="lbl.search"/></button>
                        <button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
                        <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
                    </div>

                </form:form>
            </div>
        </div>
    </div>
</div>
<table class="table table-bordered datatable" id="view-boundaries">
</table>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js'/>"></script>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js'/>"></script>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js'/>"></script>
<script src="<cdn:url  value='/resources/js/app/boundary-search.js?rnd=${app_release_no}'/>"></script>
