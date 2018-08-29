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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="row">
    <div class="col-md-12">
        <c:if test="${not empty message}">
            <div class="alert alert-success" role="alert"><spring:message code="${message}"/></div>
        </c:if>
        <form:form role="form" class="form-horizontal form-groups-bordered" modelAttribute="tradeLicense"
                   id="licenseClosure" method="POST" enctype="multipart/form-data">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title" style="text-align: center">
                        <spring:message code="title.closuretrade"/>
                    </div>
                </div>
                <div align="center">
                    <form:errors path="" cssClass="alert alert-warning" id="formerror"/>
                    </br>
                </div>
                <ul class="nav nav-tabs" id="settingstab">
                    <li class="active"><a data-toggle="tab" href="#tradedetails" data-tabidx="0"
                                          aria-expanded="true"><spring:message code="license.tradedetail"/></a></li>
                    <li class=""><a data-toggle="tab" href="#tradeattachments" id="getdocuments" data-tabidx="1"
                                    aria-expanded="false"><spring:message code="license.support.docs"/></a></li>
                </ul>
                <input type="hidden" id="licenseId" value="${tradeLicense.id}"/>
                <input type="hidden" name="applicationNumber" value="${tradeLicense.applicationNumber}"/>
                <div class="panel-body custom-form">
                    <div class="tab-content">
                        <div class="tab-pane fade active in" id="tradedetails">
                            <%@ include file='../legacylicense/license-view.jsp' %>
                            <br/><br/>
                            <div class="col-md-12">
                                <c:if test="${not empty licenseHistory && !tradeLicense.state.isEnded()}">
                                    <div class="panel-group">
                                        <div class="panel panel-default">
                                            <div class="panel-heading">
                                                <div class="panel-title">
                                                    <a data-toggle="collapse" aria-expanded="true" href="#collapse1"
                                                       id="licensehistory"><c:out
                                                            value='${tradeLicense.licenseAppType.name}'/>
                                                        <spring:message
                                                                code='lbl.licensehistory'/>
                                                        <i class="more-less fa fa-chevron-down"></i></a>
                                                </div>
                                            </div>
                                            <div id="collapse1" class="panel-collapse collapse" role="tabpanel">
                                                <div class="panel-body">
                                                    <div class="row add-border">
                                                        <table class="table table-bordered"
                                                               style="width:97%;margin:0 auto;">
                                                            <thead>
                                                            <tr>
                                                                <th class="bluebgheadtd"><spring:message
                                                                        code="lbl.wf.date"/></th>
                                                                <th class="bluebgheadtd"><spring:message
                                                                        code="lbl.wf.updatedby"/></th>
                                                                <th class="bluebgheadtd"><spring:message
                                                                        code="lbl.wf.currentowner"/></th>
                                                                <th class="bluebgheadtd"><spring:message
                                                                        code="lbl.wf.status"/></th>
                                                                <th class="bluebgheadtd"><spring:message
                                                                        code="lbl.remarks"/></th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <c:forEach items="${licenseHistory}" varStatus="stat"
                                                                       var="history">
                                                                <tr>
                                                                    <td class="blueborderfortd"
                                                                        style="text-align: left">
                                                                        <fmt:formatDate value="${history.date}" var="historyDate"
                                                                                        pattern="dd-MM-yyyy HH:mm a"/>
                                                                        <c:out value="${historyDate}"/></td>
                                                                    <td class="blueborderfortd"
                                                                        style="text-align: left">
                                                                        <c:out
                                                                                value="${history.updatedBy}"/></td>
                                                                    <td class="blueborderfortd"
                                                                        style="text-align: left">
                                                                        <c:out
                                                                                value="${history.user}"/></td>
                                                                    <td class="blueborderfortd"
                                                                        style="text-align: left">
                                                                        <c:out
                                                                                value="${history.status}"/></td>
                                                                    <td class="blueborderfortd"
                                                                        style="text-align: left">
                                                                        <c:out
                                                                                value="${history.comments}"/></td>
                                                                </tr>
                                                            </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="tradeattachments">
                            <br/><br/>
                            <%@include file="closure-supportdocs.jsp" %>
                        </div>
                    </div>
                </div>
            </div>
            <%@include file="../common/license-approver-detail.jsp" %>
        </form:form>
    </div>
</div>
<jsp:include page="../common/process-owner-reassignment.jsp"/>
<script type="text/javascript"
        src="<cdn:url  value='/resources/js/app/license-approval-detail.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url  value='/resources/js/app/view-support-documents.js?rnd=${app_release_no}'/>"></script>