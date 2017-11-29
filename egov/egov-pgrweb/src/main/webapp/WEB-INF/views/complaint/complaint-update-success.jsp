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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div id="main" class="printable">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-heading">
                    <div class="panel-title text-center no-float">
                        <strong><spring:message code="msg.complaint.reg.success"/> : <strong>${complaint.crn}</strong> </strong>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="row add-border">
                        <div class="col-md-3 col-xs-6 add-margin">
                            <spring:message code="lbl.complaintDate"/>
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin view-content" id="ct-date">
                            <fmt:formatDate value="${complaint.createdDate}" pattern="dd-MM-yyyy hh:mm:ss" var="complaintDate"/>
                            ${complaintDate}
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin">
                            <spring:message code="lbl.complainant.name"/>
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin view-content" id="ct-name">
                            <c:choose>
                                <c:when test="${not empty complaint.complainant.name}">
                                    ${complaint.complainant.name}
                                </c:when>
                                <c:otherwise>
                                    ${complaint.complainant.userDetail.name}
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-3 col-xs-6 add-margin">
                            <spring:message code="lbl.phoneNumber"/>
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin view-content" id="ct-mobno">
                            ${complaint.complainant.mobile}
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin">
                            <spring:message code="lbl.email"/>
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin view-content" id="ct-email">
                            ${complaint.complainant.email}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary" data-collapsed="0">
                <div class="panel-body">
                    <div class="row add-border">
                        <div class="col-md-3 col-xs-6 add-margin">
                            <spring:message code="lbl.complaintType"/>
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin view-content" id="ct-type">
                            ${complaint.complaintType.name}
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin">
                            <spring:message code="lbl.compDetails"/>
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin view-content" id="ct-details">
                            ${complaint.details}
                        </div>
                    </div>
                    <div class="row add-border">
                        <div class="col-md-3 col-xs-6 add-margin">
                            <spring:message code="lbl.complaintLocation"/>
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin view-content" id="ct-location">
                            <span class="map-tool-class btn-secondary" data-toggle="tooltip" title="Locate on map" data-work="Locate on map" onclick="jQuery('#complaint-locate').modal('show', {backdrop: 'static'});"><i class="fa fa-map-marker"></i></span>
                            <span id="address_locate">${complaint.childLocation.name} - ${complaint.location.name}</span>
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin">
                            <spring:message code="lbl.landmark"/>
                        </div>
                        <div class="col-md-3 col-xs-6 add-margin view-content" id="ct-landmark">
                            ${complaint.landmarkDetails}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="row text-center">
    <div class="add-margin">
        <button type="submit" class="btn btn-default printbtn"><spring:message code="lbl.print"/></button>
        <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
    </div>
</div>

<script src="<cdn:url  value='/resources/global/js/jquery/plugins/jQuery.print.js' context='/egi'/>"></script>

<!-- Modal 6 (Long Modal)-->
<div class="modal fade" id="complaint-locate">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-body">

                <div class="row">

                    <div class="col-md-12">
                        <div class="panel panel-primary" data-collapsed="0"><!-- to apply shadow add class "panel-shadow" -->
                            <!-- panel head -->
                            <div class="panel-heading">
                                <div class="panel-title" id="show_address_in_map"><spring:message code="lbl.complaintLocation"/></div>

                            </div>

                            <!-- panel body -->
                            <div class="panel-body no-padding">

                                <script src="https://maps.googleapis.com/maps/api/js?key=${sessionScope.googleApiKey}"></script>

                                <div id="normal" class="img-prop"></div>

                            </div>

                        </div>
                    </div>

                </div>


            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="lbl.close"/></button>
            </div>
        </div>
    </div>
</div>