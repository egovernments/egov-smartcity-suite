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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.connection.details"/>
				</div>
			</div>
			<div class="panel-body">
				<c:choose>
					<c:when test="${sewerageApplicationDetails.connectionDetail.propertyType == 'RESIDENTIAL'}">
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.propertytype"/>
							</div>
							<div class="col-xs-3 add-margin view-content">
								<spring:message code="lbl.residential"/>
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.noofclosets"/>
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${sewerageApplicationDetails.connectionDetail.noOfClosetsResidential}"/>
							</div>
						</div>
					</c:when>
					<c:when test="${sewerageApplicationDetails.connectionDetail.propertyType == 'NON_RESIDENTIAL'}">
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.propertytype"/>
							</div>
							<div class="col-xs-3 add-margin view-content">
								<spring:message code="lbl.nonresidential"/>
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.noofclosets"/>
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${sewerageApplicationDetails.connectionDetail.noOfClosetsNonResidential}"/>
							</div>
						</div>
					</c:when>
					<c:when test="${sewerageApplicationDetails.connectionDetail.propertyType == 'MIXED'}">
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.propertytype"/>
							</div>
							<div class="col-xs-3 add-margin view-content">
								<spring:message code="lbl.propertytype.mixed"/>
							</div>
							<div class="col-xs-3 add-margin">
							</div>
							<div class="col-xs-3 add-margin view-content">
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
							</div>
							<div class="col-xs-3 add-margin view-content">
								<spring:message code="lbl.residential"/>
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.noofclosets"/>
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${sewerageApplicationDetails.connectionDetail.noOfClosetsResidential}"/>
							</div>
						</div>
						<div class="row add-border">
							<div class="col-xs-3 add-margin">
							</div>
							<div class="col-xs-3 add-margin view-content">
								<spring:message code="lbl.nonresidential"/>
							</div>
							<div class="col-xs-3 add-margin">
								<spring:message code="lbl.noofclosets"/>
							</div>
							<div class="col-xs-3 add-margin view-content">
								<c:out value="${sewerageApplicationDetails.connectionDetail.noOfClosetsNonResidential}"/>
							</div>
						</div>
					</c:when>
				</c:choose>
		<%-- 		<div class="row">
					<div class="col-xs-3 add-margin"><spring:message code="lbl.donationcharge"/></div>  
					<div class="col-xs-3 add-margin view-content">
						<c:choose>
							<c:when test="${not empty sewerageApplicationDetails.donationCharges}">
								<c:out value="${sewerageApplicationDetails.donationCharges}" />
							</c:when>
							<c:otherwise>0.0</c:otherwise>
						</c:choose>
					</div>
					<div class="col-xs-3 add-margin"><spring:message code="lbl.estimationcharges"/></div>
					<div class="col-xs-3 add-margin view-content">
						<c:choose>
						<c:when test="${not empty sewerageApplicationDetails.fieldInspectionDetails.estimationCharges}">
							<c:out value="${sewerageApplicationDetails.fieldInspectionDetails.estimationCharges}" />
						</c:when>
						<c:otherwise>0.0</c:otherwise>
					</c:choose>
					</div>
				</div> --%>
			</div>
			<c:if test="${sewerageApplicationDetails.fileStore != null}">
				<div class="panel-footer"><spring:message
					code="lbl.fileattached" /></div>
				<div>
					<a href="/stms/transactions/downloadFile?applicationNumber=${sewerageApplicationDetails.applicationNumber }">${sewerageApplicationDetails.fileStore.fileName }</a>
				</div>
			</c:if>
		</div>
		</div>
	</div>
	<%-- <jsp:include page="documentdetails-view.jsp"></jsp:include> --%>