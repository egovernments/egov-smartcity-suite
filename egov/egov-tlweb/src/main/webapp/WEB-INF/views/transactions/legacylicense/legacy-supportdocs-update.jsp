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

<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div class="col-md-12">
    <div class="form-group view-content header-color hidden-xs">
        <div class="col-sm-1">
            <spring:message code="doctable.sno"/>
        </div>
        <div class="col-sm-3">
            <spring:message code="doctable.docname"/>
        </div>
        <div class="col-sm-4">
            <spring:message code="doctable.attach.doc"/>
        </div>
        <div class="col-sm-3">
            <spring:message code="license.remarks"/>
        </div>
    </div>
    <c:forEach items="${documentTypes}" varStatus="stat" var="documentType">
        <div class="form-group">
            <div class="col-sm-1">
                <c:out value="${stat.index + 1}"/>
            </div>
            <div class="col-sm-3">
                <c:choose>
                    <c:when test="${documentType.mandatory}">
                        <c:out value="${documentType.name}"/><span class="mandatory"></span>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${documentType.name}"/>
                    </c:otherwise>
                </c:choose>
                <form:hidden path="licenseDocuments[${stat.index}].type"
                             id="licenseDocuments${stat.index}type.id" value="${documentType.id}"/>
            </div>
            <div class="col-sm-4">
                <input type="file" name="licenseDocuments[${stat.index}].multipartFiles" id="uploadFile${stat.index}"
                       class="file-ellipsis upload-file"/>
            </div>
            <div class="col-sm-3 add-margin">
                <form:textarea path="licenseDocuments[${stat.index}].description" class="form-control"/>
                <c:if test="${documentType.mandatory}">
                    <form:errors path="documents[${stat.index}].description"
                                 class="add-margin error-msg tradelicenceerror"/>
                </c:if>
            </div>
        </div>
    </c:forEach>
    <c:if test="${not empty tradeLicense.documents}">
        <div class="panel-group">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="panel-title">
                        <a data-toggle="collapse" aria-expanded="true" href="#collapse2" id="getdocuments">
                            <spring:message code="doctable.attach.doc"/>
                            <i class="more-less fa fa-chevron-down"></i></a>
                    </div>
                </div>
                <div id="collapse2" class="panel-collapse collapse" role="tabpanel">
                    <div class="panel-body">
                        <div class="row add-border">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <ul class="nav nav-tabs" id="settingstab">
                                    <li class="active"><a data-toggle="tab" href="#newDocs"
                                                          data-tabidx="0" aria-expanded="true">
                                        <spring:message code="lbl.new"/></a></li>
                                    <li class=""><a data-toggle="tab" href="#renewDocs"
                                                    data-tabidx="1" aria-expanded="false">
                                        <spring:message code="lbl.renew"/></a></li>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane fade active in" id="newDocs"><br/>
                                        <div id="newTbl">
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="renewDocs"><br/>
                                        <div id="renewTbl">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>
<style>
    #newTbl > table {
        margin-left: 0;
    }

    #renewTbl > table {
        margin-left: 0;
    }
</style>
<script>function toggleIcon(e) {
    $(e.target)
        .prev('.panel-heading')
        .find(".more-less")
        .toggleClass('fa-chevron-down fa-chevron-up ');
}

$('.panel-group').on('hidden.bs.collapse', toggleIcon);
$('.panel-group').on('shown.bs.collapse', toggleIcon);
</script>
<script type="text/javascript"
        src="<cdn:url  value='/resources/js/app/license-support-docs.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/app/view-support-documents.js?rnd=${app_release_no}'/>"></script>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet"
      href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
        src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>