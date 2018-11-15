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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>


<form:form role="form" method="post"
           modelAttribute="waterConnectionDetails" id="waterConnectionForm"
           cssClass="form-horizontal form-groups-bordered"
           enctype="multipart/form-data">
    <div class="container-fluid">
        <spring:hasBindErrors name="waterConnectionDetails">
            <div class="alert alert-danger col-md-12 col-md-offset-0">
                <form:errors path="*"/>
            </div>
        </spring:hasBindErrors>
    </div>

    <div class="page-container" id="page-container">
        <input type="hidden" id="mode"  name="mode" value="${mode}"/>
        <input type="hidden" name="approvalPositionExist" id="approvalPositionExist" value="${approvalPositionExist}"/>
        <input type="hidden" name="wfstateDesc" id="wfstateDesc" value="${waterConnectionDetails.state.value}"/>
        <input type="hidden" name="statuscode" id="statuscode" value="${waterConnectionDetails.status.code}"/>
        <input type="hidden" name="isCommissionerLoggedIn" id="isCommissionerLoggedIn" value="${isCommissionerLoggedIn}"/>
        <input type="hidden" name="wfstate" id="wfstate" value="${waterConnectionDetails.state.id}"/>
        <input type="hidden" name="waterconnectiondetailid" id="waterconnectiondetailid" value="${waterConnectionDetails.id}"/>
        <input type="hidden" id="closerConnection" value="${waterConnectionDetails.closeConnectionType}"/>
        <input type="hidden" id="applicationType" value="${waterConnectionDetails.applicationType.code}"/>
        <input type="hidden" id="currentUser" value="${currentUser}"/>
        <input type="hidden" id="waterTaxDueforParent" value="${waterTaxDueforParent}" name="waterTaxDueforParent"/>
        <input type="hidden" id="typeOfConnection" value="${typeOfConnection}"/>
        <input type="hidden" id="meterFocus" value="${meterFocus}"/>
        <input type="hidden" id="isSanctionedDetailEnable" value="${isSanctionedDetailEnable}"/>
        <input type="hidden" id="proceedWithoutDonation" value="${proceedWithoutDonation}"/>
        <input type="hidden" id="executionDate" value="${waterConnectionDetails.executionDate}"/>
        <input type="hidden" id="source" value="${waterConnectionDetails.source}"/>
        <input type="hidden" id="currentDemand" value="${currentDemand}"/>
        <input type="hidden" id="pendingActions" value="${pendingActions}"/>
        <input type="hidden" id="workFlowAction" name="workFlowAction" value="${workFlowAction}"/>
        <input type="hidden" id="ownerPosition" name="ownerPosition" value="${ownerPosition}" />
        <input type="hidden" id="date" name="date" value="${waterConnectionDetails.applicationDate}" />
        <c:choose>
            <c:when test="${not empty message}">
                <div class="text-center">
                    <div class="alert alert-danger alert-center">
                        <spring:message code="${message}"></spring:message>
                    </div>
                    <div class="panel panel-primary" data-collapsed="0">
                        <div class="panel-heading">
                            <div class="panel-title">
                                <spring:message code="lbl.basicdetails"/>
                            </div>
                        </div>
                        <jsp:include page="commonappdetails-view.jsp"></jsp:include>
                    </div>

                    <button type="button" id="close" class="btn btn-default" onclick="window.close();">
                        <spring:message code="lbl.close"/>
                    </button>
                </div>
            </c:when>
            <c:otherwise>
                <div class="panel panel-primary" data-collapsed="0">
                    <div class="panel-heading">
                        <div class="panel-title">
                            <spring:message code="lbl.basicdetails"/>
                        </div>
                    </div>
                    <jsp:include page="commonappdetails-view.jsp"></jsp:include>
                </div>
                <c:if test="${waterConnectionDetails.status.code != 'CREATED'}">
                    <jsp:include page="connectiondetails-view.jsp"></jsp:include>
                </c:if>
                <c:if test="${(waterConnectionDetails.status.code =='CREATED'  )&& (mode=='fieldInspection' || mode == 'addDemand' || mode=='editDemand')}">
                    <div class="panel panel-primary" data-collapsed="0">
                        <div class="panel panel-body">
                            <jsp:include page="connectiondetails.jsp"></jsp:include>
                        </div>
                    </div>
                      	  <jsp:include page="documentdetails-view.jsp"></jsp:include>
                    <c:choose>
                        <c:when
                                test="${waterConnectionDetails.applicationType.code=='REGLZNCONNECTION' && waterConnectionDetails.fieldInspectionDetails.id!=null}">
                            <jsp:include page="estimationdetails-view.jsp"></jsp:include>
                        </c:when>
                        <c:otherwise>
                            <jsp:include page="estimationdetails.jsp"></jsp:include>
                        </c:otherwise>
                    </c:choose>
                </c:if>

                <c:if test="${waterConnectionDetails.status.code =='CREATED' && mode=='edit' }">
                    <div class="panel panel-primary" data-collapsed="0">
                        <jsp:include page="connectiondetails.jsp"></jsp:include>
                    </div>
                    <jsp:include page="documentdetails-view.jsp"></jsp:include>
                </c:if>

                <c:if test="${waterConnectionDetails.status.code =='CREATED' && mode=='' && waterConnectionDetails.state.value =='Rejected'}">
                    <div class="panel panel-primary" data-collapsed="0">
                        <jsp:include page="connectiondetails.jsp"></jsp:include>
                    </div>
                    <jsp:include page="documentdetails-view.jsp"></jsp:include>
                </c:if>
                <div class="panel panel-primary" data-collapsed="0">
                    <div class="panel-heading">
                        <div class="panel-title">
                            <spring:message code="lbl.apphistory"/>
                        </div>
                    </div>
                    <jsp:include page="applicationhistory-view.jsp"></jsp:include>
                </div>
                <c:if test="${ (isSanctionedDetailEnable == true && waterConnectionDetails.status.code == 'ESTIMATIONAMOUNTPAID') }">
                    <jsp:include page="sanctiondetails.jsp"></jsp:include>
                </c:if>
                <c:if test="${waterConnectionDetails.status.code == 'WORKORDERGENERATED'}">
                    <jsp:include page="tapexecutiondetails-form.jsp"></jsp:include>
                </c:if>
                <c:if test="${(waterConnectionDetails.status.code =='CLOSERINITIATED'  ||   waterConnectionDetails.status.code =='CLOSERINPROGRESS'
                ||waterConnectionDetails.status.code =='CLOSERSANCTIONED') }">
                    <jsp:include page="closerForm-details.jsp"></jsp:include>
                    <jsp:include page="closuredocumentdetails-view.jsp"></jsp:include>
                </c:if>

                <c:if test="${(waterConnectionDetails.status.code =='RECONNECTIONINPROGRESS' || waterConnectionDetails.status.code =='RECONNECTIONSANCTIONED'
                ||waterConnectionDetails.status.code =='RECONNECTIONINITIATED') }">
                    <jsp:include page="reconnection-details.jsp"></jsp:include>
                    <jsp:include page="closuredocumentdetails-view.jsp"></jsp:include>
                </c:if>

                <jsp:include page="../common/commonWorkflowMatrix.jsp"/>
                <jsp:include page="../common/commonWorkflowMatrix-button.jsp"/>
                <c:if test="${hasJuniorOrSeniorAssistantRole}">
                    <jsp:include page="application-reassignment.jsp"></jsp:include>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</form:form>
<script>
    function showeditDcb(obj) {
        window.location = "/wtms/application/editDemand/" + obj;
    }

    function generateDemandNotice(obj) {
        window.location = "/wtms/application/regulariseconnection/demandnote-view/" + obj;
    }

    if ($("#applicationType").val() === 'REGLZNCONNECTION' && $("#statuscode").val() === 'CREATED' && 'Rejected' != $("#currentState").val()
        && $("#executionDate").val() === '') {
        $(".workAction").hide();
        $("#editDCB").hide();
    } else if ($("#applicationType").val() === 'REGLZNCONNECTION' && $("#statuscode").val() === 'CREATED' && $("#executionDate").val() != ''
        && $("#currentDemand").val == null) {
        $(".workAction").hide();
        $("#save").hide();
    } else {
        $(".workAction").show();
    }
</script>
<script src="<cdn:url value='/resources/js/app/applicationsuccess.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/js/app/newconnectionupdate.js?rnd=${app_release_no}'/>"></script>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>