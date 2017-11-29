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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<script src="<cdn:url  value='/resources/js/app/complaintype.js?rnd=${app_release_no}'/>"></script>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title text-center no-float">
					<c:if test="${not empty message}">
					<strong><spring:message code="${message}" /></strong>
					</c:if>
				</div>
			</div>
			<form:form id="complaintTypeViewForm" method="get"
				class="form-horizontal form-groups-bordered" modelAttribute="complaintType">
				<div class="panel-body">
					<div class="row">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.complaintTypeName" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-name">
							<c:out value="${complaintType.name }"></c:out>
							<input id="compTypeCode" type="hidden"
								value="<c:out value="${complaintType.code}" />" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.local.name" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${complaintType.localName}"></c:out>
						</div>
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.complaintTypeCode" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-code">
							<c:out value="${complaintType.code}"></c:out>
						</div>
					</div>
					<div class="row">
					
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.category" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<c:out value="${complaintType.category.name}" default="NA"/>
						</div>
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.department" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-dept">
							<c:out value="${complaintType.department.name}" default="NA"></c:out>
						</div>
						
					</div>
										
					<div class="row">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.isactive" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-isactive">
							<c:choose>
								<c:when test="${complaintType.isActive == true}">
						           Yes</c:when>
								<c:otherwise>No</c:otherwise>
							</c:choose>
						</div>
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.complaintTypeDesc"/>
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-isactive">
							<c:choose>
								<c:when test="${complaintType.description != null}">${complaintType.description}</c:when>
								<c:otherwise><spring:message code="msg.not.applicable"/></c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.slahrs" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-slahrs">
							<c:out value="${complaintType.slaHours}"></c:out>
						</div>
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message	code="lbl.hasFinImpact"></spring:message>
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-hasfinimpact">
							<c:choose>
								<c:when test="${complaintType.hasFinancialImpact == true}">
						           Yes</c:when>
								<c:otherwise>No</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="row text-center">
						<div class="row">
							<div class="text-center">
								<button type="submit" id="buttonCreate" class="btn btn-primary">
									<spring:message code="lbl.create" />
								</button>
								<button type="submit" id="buttonEdit" class="btn btn-primary">
									<spring:message code="lbl.edit" />
								</button>
								<a href="javascript:void(0)" class="btn btn-default"
									onclick="self.close()"><spring:message code="lbl.close" /></a>
							</div>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>

