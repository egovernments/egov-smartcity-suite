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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<%@ include file="/includes/taglibs.jsp"%>


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
						<span class="mandatory"></span>
					</div>
					<div class="col-xs-3 add-margin view-content">
						<form:select id="propType"
							path="property.propertyDetail.propertyTypeMaster.code"
							name="propType" cssClass="form-control" cssStyle="width: 100%">
							<form:option value="">--select--</form:option>
							<c:forEach items="${propTypeList}" var="propType">
								<form:option value="${propType.code}">${propType.type}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.propertytype" />
						<span class="mandatory"></span>
					</div>
					<div class="col-xs-3 add-margin view-content">
						<form:input type="hidden" id="propTypeCategory" path=""
							value="${property.propertyDetail.categoryType}" />
						<form:select id="propTypeCategoryId"
							path="property.propertyDetail.categoryType"
							name="propTypeCategoryId" cssClass="form-control"
							cssStyle="width: 100%"
							value="${property.propertyDetail.categoryType}">
						</form:select>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.extentofsite" />
						<span class="mandatory"></span>
					</div>
					<div class="col-xs-3 add-margin">
						<form:input type="text" cssClass="form-control"
							path="property.propertyDetail.sitalArea.area" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.cv.locality" />
						<span class="mandatory"></span>
					</div>
					<div class="col-xs-3 add-margin view-content">
						<form:select path="basicProperty.propertyID.locality.id"
							id="locality" cssStyle="width: 100%" cssClass="form-control"
							name="locality" disabled="true">
							<form:option value="">--select--</form:option>
							<c:forEach items="${localityList}" var="locality">
								<form:option value="${locality.id}">${locality.name}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.cv.zone" />
						<span class="mandatory"></span>
					</div>
					<div class="col-xs-3 add-margin view-content">
						<c:if test=""></c:if>
						<c:choose>
							<c:when test="${isZoneActive}">
								<form:select path="basicProperty.propertyID.zone.id" id="zone"
									name="zone" cssClass="form-control" cssStyle="width: 50%"
									disabled="true">
									<form:option value="">--select--</form:option>
									<c:forEach items="${zones}" var="zone">
										<form:option value="${zone.id}">${zone.name}</form:option>
									</c:forEach>
								</form:select>
							</c:when>
							<c:otherwise>
								<form:select path="basicProperty.propertyID.zone" id="zone"
									name="zone" cssClass="form-control" cssStyle="width: 50%">
									<form:option value="">--select--</form:option>
									<c:forEach items="${zones}" var="zone">
										<form:option value="${zone.id}">${zone.name}</form:option>
									</c:forEach>
								</form:select>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.cv.revWard" />
						<span class="mandatory"></span>
					</div>
					<div class="col-xs-3 add-margin view-content">
						<form:input type="hidden" id="ward"
							path="basicProperty.propertyID.ward.id"
							value="${basicProperty.propertyID.ward.id}" />
						<form:select id="wardId" path="basicProperty.propertyID.ward.id"
							cssStyle="width: 100%" name="wardId" cssClass="form-control"
							onchange="populateBlock();"
							value="${basicProperty.propertyID.ward.id}" disabled="true">
						</form:select>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.cv.revBlock" />
						<span class="mandatory"></span>
					</div>
					<div class="col-xs-3 add-margin view-content">
						<form:input type="hidden" id="block"
							path="basicProperty.propertyID.area.id"
							value="${basicProperty.propertyID.area.id}" />

						<form:select id="blockId" path="basicProperty.propertyID.area.id"
							cssClass="form-control" style="width: 50%" name="blockId"
							value="${basicProperty.propertyID.area.id}" disabled="true">
						</form:select>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.cv.electionWard" />
						<span class="mandatory"></span>
					</div>
					<div class="col-xs-3 add-margin view-content">
						<form:select id="electionWard"
							path="basicProperty.propertyID.electionBoundary.id"
							name="electionWard" cssClass="form-control"
							cssStyle="width: 100%" disabled="true">
							<form:option value="">--select--</form:option>
							<c:forEach items="${electionWardList}" var="electionWard">
								<form:option value="${electionWard.id}">${electionWard.name}</form:option>
							</c:forEach>
						</form:select>
					</div>
				</div>

			</div>

		</div>
	</div>
</div>

<div id="floorDetailsdiv">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0"
				style="text-align: left">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.cv.flrDtls" />
					</div>
				</div>
				<div class="panel-body">
					<div align="center"
						class="overflow-x-scroll floors-tbl-freeze-column-div">
						<%@ include file="flrdetails-edit.jsp"%>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="vacLandDetailsdiv">
	<%@ include file="vaclanddetails-edit.jsp"%>
</div>


