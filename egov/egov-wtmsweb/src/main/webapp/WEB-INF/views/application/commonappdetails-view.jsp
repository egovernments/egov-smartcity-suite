<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib  uri="http://www.joda.org/joda/time/tags" prefix="joda"%>
	<div class="panel-body">
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.consumer.number"/>  
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:choose>
					<c:when test="${not empty waterConnectionDetails.connection.consumerCode}">
						<strong><c:out value="${waterConnectionDetails.connection.consumerCode}" /></strong>
					</c:when>
					<c:otherwise>N/A</c:otherwise>
				</c:choose>
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.mobilenumber"/>
			</div>
			<div class="col-xs-3 add-margin view-content">
				<strong><c:out value="${waterConnectionDetails.connection.mobileNumber}" /></strong>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.ptassesmentnumber"/>
			</div>
			<div class="col-xs-3 add-margin view-content">
				<strong><c:out value="${waterConnectionDetails.connection.propertyIdentifier}" /></strong>
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.bpanumber"/>
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:choose>
					<c:when test="${not empty waterConnectionDetails.connection.bpaIdentifier}">
						<strong><c:out value="${waterConnectionDetails.connection.bpaIdentifier}" /></strong>
					</c:when>
					<c:otherwise>N/A</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.applicantname"/>  
			</div>
			<div class="col-xs-3 add-margin view-content">
				<strong><c:out value="${applicantDetails.applicantName}" /></strong>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.address" />
			</div>
			<div class="col-xs-9 add-margin view-content">
				<strong><c:out value="${applicantDetails.address}" /></strong>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.connectiontype"/>
			</div>
			<div class="col-xs-3 add-margin view-content">
				<strong><c:out value="${waterConnectionDetails.connectionType}" /></strong>
			</div>
		
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.usagetype" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<strong><c:out value="${waterConnectionDetails.usageType.name}" /></strong>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.zonewardblock"/>
			</div>
			<div class="col-xs-3 add-margin view-content">
				<strong><c:out value="${applicantDetails.zoneWardBlock}" /></strong>
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.pt.due"/>
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:choose>
					<c:when test="${not empty applicantDetails.propertyTaxDue}">
						<strong><c:out value="${applicantDetails.propertyTaxDue}" /></strong>
					</c:when>
					<c:otherwise>N/A</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.current.due"/>
			</div>
			<div class="col-xs-3 add-margin view-content">
				<strong><c:out value="${waterConnectionDetails.demand.baseDemand}" /></strong>
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.arrear.due"/>
			</div>
			<div class="col-xs-3 add-margin view-content">
				<strong>N/A</strong>
			</div>
		</div>
	</div>
	
<script  type="text/javascript"  src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"  src="<c:url value='/resources/global/js/egov/custom.js' context='/egi'/>"></script>
