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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<div id="main">
    <div class="row">
        <div class="col-md-12">
            <form:form id="estimationVerificationForm" method="post" class="form-horizontal form-groups-bordered"
                       modelAttribute="waterConnectionDetails">
                <div class="panel panel-primary" data-collapsed="0">
                    <div class="panel-heading">
                        <div class="panel-title">
                            <spring:message code="lbl.basicdetails"/>
                        </div>
                    </div>
                    <c:choose>
                        <c:when test="${not empty message}">
                            <div class="text-center">
                                <div class="alert alert-info align-center" role="alert"><spring:message
                                        code="${message}"/></div>
                                <button type="button" id="close" class="btn btn-default" onclick="window.close();">
                                    <spring:message code="lbl.close"/>
                                </button>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="panel-body">
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.ack.number"/>
                                    </div>
                                    <div class="col-xs-3 add-margin view-content">
                                            ${waterConnectionDetails.applicationNumber}
                                    </div>
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.application.date"/></div>
                                    <div class="col-xs-3 add-margin view-content">
                                        <fmt:formatDate pattern="dd/MM/yyyy" type="date"
                                                        value="${waterConnectionDetails.applicationDate}"
                                                        var="applicationDate"/>
                                            ${applicationDate}
                                    </div>
                                </div>
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.ptassesmentnumber"/>
                                    </div>
                                    <div class="col-xs-3 add-margin view-content" id='propertyIdentifier'>
                                            ${waterConnectionDetails.connection.propertyIdentifier}
                                    </div>
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.consumer.number"/></div>
                                    <div class="col-xs-3 add-margin view-content">
                                            ${waterConnectionDetails.connection.consumerCode}
                                    </div>
                                </div>
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.mobileNo"/>
                                    </div>
                                    <div class="col-xs-3 add-margin view-content" id="mobileNumber"></div>
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;"><spring:message
                                            code="lbl.email"/></div>
                                    <div class="col-xs-3 add-margin view-content" id="email"></div>
                                </div>
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin"><spring:message code="lbl.applicantname"/></div>
                                    <div class="col-xs-3 add-margin view-content" id="applicantname"></div>
                                    <div class="col-xs-3 add-margin"><spring:message code="lbl.locality"/></div>
                                    <div class="col-xs-3 add-margin view-content" id="locality"></div>
                                </div>
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin"><spring:message code="lbl.address"/></div>
                                    <div class="col-xs-3 add-margin view-content" id="propertyaddress"></div>
                                    <div class="col-xs-3 add-margin"><spring:message code="lbl.zonewardblock"/></div>
                                    <div class="col-xs-3 add-margin view-content" id="zonewardblock"></div>
                                </div>
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin"><spring:message code="lbl.aadhar"/></div>
                                    <div class="col-xs-3 add-margin view-content" id="aadhaar"></div>
                                    <div class="col-xs-3 add-margin"><spring:message code="lbl.nooffloors"/></div>
                                    <div class="col-xs-3 add-margin view-content" id="nooffloors"></div>
                                </div>
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.connectiontype"/>
                                    </div>
                                    <div class="col-xs-3 add-margin view-content">
                                            ${waterConnectionDetails.connectionType}
                                    </div>
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.usagetype"/></div>
                                    <div class="col-xs-3 add-margin view-content">
                                            ${waterConnectionDetails.usageType.name}
                                    </div>
                                </div>
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.pt.tax"/>
                                    </div>
                                    <div class="col-xs-3 add-margin view-content" id="propertytaxdue"></div>
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.due.estimationcharges"/>
                                    </div>
                                    <div class="col-xs-3 add-margin view-content">
                                            ${estimationAmount}
                                    </div>
                                </div>
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.connectiondate"/>
                                    </div>
                                    <div class="col-xs-3 add-margin view-content">
                                        <fmt:formatDate pattern="dd/MM/yyyy" type="date"
                                                        value="${waterConnectionDetails.executionDate}"
                                                        var="executionDate"/>
                                            ${executionDate}
                                    </div>
                                    <div class="col-xs-3 add-margin" style="font-size: 14px;">
                                        <spring:message code="lbl.oldconsumerno"/></div>
                                    <div class="col-xs-3 add-margin view-content">
                                        <c:out value="${waterConnectionDetails.connection.oldConsumerNumber}"
                                               default="N/A"/>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-heading">
                                <div class="panel-title">
                                    <spring:message code="lbl.connection.details"/>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin"><spring:message code="lbl.propertytype"/></div>
                                    <div class="col-xs-3 add-margin view-content"><c:out
                                            value="${waterConnectionDetails.propertyType.name}"/></div>
                                    <div class="col-xs-3 add-margin"><spring:message code="lbl.usagetype"/></div>
                                    <div class="col-xs-3 add-margin view-content">
                                        <c:out value="${waterConnectionDetails.usageType.name}"/>
                                    </div>
                                </div>
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin"><spring:message code="lbl.category"/></div>
                                    <div class="col-xs-3 add-margin view-content"><c:out
                                            value="${waterConnectionDetails.category.name}"/></div>
                                    <div class="col-xs-3 add-margin"><spring:message code="lbl.watersourcetype"/></div>
                                    <div class="col-xs-3 add-margin view-content">
                                        <c:out value="${waterConnectionDetails.waterSource.waterSourceType}"/>
                                    </div>
                                </div>
                                <div class="row add-border">
                                    <div class="col-xs-3 add-margin"><spring:message
                                            code="lbl.hscpipesize.inches"/></div>
                                    <div class="col-xs-3 add-margin view-content">
                                        <c:out value="${waterConnectionDetails.pipeSize.code}"/>
                                    </div>
                                    <div class="col-xs-3 add-margin"><spring:message
                                            code="lbl.sumpcapacity.litres"/></div>
                                    <div class="col-xs-3 add-margin view-content">
                                        <c:out value="${waterConnectionDetails.sumpCapacity}" default="N/A"/></div>
                                </div>
                                <div class="row add-border">
                                    <c:if test="${waterConnectionDetails.usageType.name.equals('Lodges')}">
                                        <div class="col-xs-3 add-margin"><spring:message code="lbl.no.of.rooms"/></div>
                                        <div class="col-xs-3 add-margin view-content">
                                            <c:out value="${waterConnectionDetails.numberOfRooms}" default="N/A"/>
                                        </div>
                                    </c:if>
                                    <div class="col-xs-3 add-margin"><spring:message
                                            code="lbl.no.of.persons"/></div>
                                    <div class="col-xs-3 add-margin view-content">
                                        <c:out value="${waterConnectionDetails.numberOfPerson}" default="N/A"/>
                                    </div>
                                    <div class="col-xs-3 add-margin"><spring:message
                                            code="lbl.bpl.cardholdername"/></div>
                                    <div class="col-xs-3 add-margin view-content">
                                        <c:out value="${waterConnectionDetails.bplCardHolderName}" default="N/A"/>
                                    </div>
                                    <c:if test="${waterConnectionDetails.connectionType == 'METERED'}">
                                        <div class="row add-border">
                                            <div class="col-xs-3 add-margin"><spring:message
                                                    code="lbl.watersupplytype"/></div>
                                            <div class="col-xs-3 add-margin view-content">
                                                <c:out value="${waterConnectionDetails.waterSupply.waterSupplyType}"/>
                                            </div>

                                            <div class="col-xs-3 add-margin"><spring:message
                                                    code="lbl.apartmentorcomplexname"/></div>
                                            <div class="col-xs-3 add-margin view-content">
                                                <c:out value="${waterConnectionDetails.buildingName}"/>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="form-group">
                                </br>
                                <div style="text-align:center">
                                    <input type="button" id="search" class="btn btn-default"
                                           onclick="return formsubmit();"
                                           value="Collect Estimation Charges"
                                           name="<spring:message code="lbl.search"/>"/>
                                    <button type="button" id="btnclose" class="btn btn-default"
                                            onclick="window.close();">
                                        <spring:message code="lbl.close"/>
                                    </button>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </form:form>
        </div>
    </div>
</div>
<script src="<cdn:url value='/resources/js/app/applicationsuccess.js?rnd=${app_release_no}'/>"></script>
<script>
    function formsubmit() {
        bootbox.confirm({
            message: 'Please confirm, if you want to proceed for estimation changes payment',
            buttons: {
                'cancel': {
                    label: 'No',
                    className: 'btn-default'
                },
                'confirm': {
                    label: 'Yes',
                    className: 'btn-danger'
                }
            },
            callback: function (result) {
                if (result) {
                    $("#estimationVerificationForm").attr('action', '/wtms/estimationcharges/collection/' + '${waterConnectionDetails.applicationNumber}');
                    $("#estimationVerificationForm").attr('method', 'get');
                    $("#estimationVerificationForm").submit();
                }
            }

        });
    }
</script>
