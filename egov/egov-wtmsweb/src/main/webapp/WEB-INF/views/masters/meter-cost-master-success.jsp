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


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.joda.org/joda/time/tags" prefix="joda"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<form:form  method ="post" action="" class="form-horizontal form-groups-bordered" modelAttribute="meterCost" id="meterCostform" >
<div class="row">
	<div class="col-md-12">
		<c:if test="${not empty message}">
            <div class= role="alert">${message}</div>
        </c:if>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title"><spring:message code="title.meterCost.master"/></div>
	</div>
<div class="panel-body">
	<div class="row add-border">
		<div class="col-md-3 col-xs-3 add-margin"><spring:message code="lbl.hscpipesize"/> </div>
			<div class="col-md-3 col-xs-3 add-margin view-content">
				<c:out value="${meterCost.pipeSize.code}"/>
				</div>
				<div class="col-md-3 col-xs-3 add-margin"><spring:message code="lbl.makeofthemeter"/></div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<c:out value="${meterCost.meterMake}"/>
					</div>
					</div>
					<div class="row add-border">
						<div class="col-md-3 col-xs-3 add-margin"><spring:message code="lbl.metercost"/></div>
							<div class="col-md-3 col-xs-3 add-margin view-content">
								<c:out value="${meterCost.amount}"/>
							</div>
						<div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.status"/></div>
							<div class="col-md-3 col-xs-6 add-margin view-content">
								<c:choose>
									<c:when test="${meterCost.active == 'true'}">
										<c:out value="ACTIVE" />
									</c:when> 
									<c:otherwise>
										<c:out value="INACTIVE" />
									</c:otherwise>
								</c:choose>
							</div> 
							</div>
						</div>
					</div>
				</div>
			</div>
<input type="hidden" value="${mode}" id="mode" />
		<div class="row text-center">
			<div class="row">
				<c:if test="${mode == 'create'}"> 
					<button type="button" class="btn btn-primary" id="addnewid"><spring:message code="lbl.addnew" /></button> 
				</c:if>
				<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
			</div>
		</div>
</form:form>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>" type="text/javascript"></script>
<script src="<cdn:url value='/resources/js/app/meter-cost-master.js?rnd=${app_release_no}'/>"></script>