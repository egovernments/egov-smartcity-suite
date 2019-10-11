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
<!-- Assessment Details -->
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.cv.assmtDetails" />
				</div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.category.ownership" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${property.propertyDetail.propertyTypeMaster.type}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.propertytype" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${property.propertyDetail.categoryType}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.extentofsite" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out
							value="${property.propertyDetail.sitalArea.area.toString()}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.cv.locality" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out
							value="${property.basicProperty.address.areaLocalitySector}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.cv.zone" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${property.basicProperty.propertyID.zone.name}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.cv.revWard" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${property.basicProperty.propertyID.ward.name}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.cv.revBlock" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out value="${property.basicProperty.propertyID.area.name}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.cv.electionWard" />
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:out
							value="${property.basicProperty.propertyID.electionBoundary.name}"></c:out>
					</div>
				</div>

			</div>

		</div>
	</div>
</div>

<c:set var="type"
	value="${property.propertyDetail.propertyTypeMaster.type}" />
<c:if test="${type =='Vacant Land'}">
	<%@ include file="vaclanddetails.jsp"%>
</c:if>
<c:if test="${type !='Vacant Land'}">
	<%@ include file="flrdetails.jsp"%>
</c:if>
