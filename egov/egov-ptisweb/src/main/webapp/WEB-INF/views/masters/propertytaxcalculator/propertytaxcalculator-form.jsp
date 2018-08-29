
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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<div id="dcbError" class="errorstyle" style="color: red"></div>
<form:form role="form" action="" modelAttribute="floorDetails"
	method="POST" id="propertyTaxCalculator"
	class="form-horizontal form-groups-bordered">
	<input type="hidden" id="floorId" value="${floorId}" />
	<input type="hidden" id="classificationId" value="${classificationId}" />
	<input type="hidden" id="usageId" value="${usageId}" />
	<input type="hidden" id="occupancyId" value="${occupancyId}" />
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<spring:message code="lbl.meassurements" />
					</div>
				</div>
				<br>
				<div class="form-group text-right">
					<label class="col-sm-3 control-label"> <spring:message
							code="lbl.revenue.zone" /> :<span class="mandatory1">*</span>
					</label>
					<div class="col-sm-3 add-margin">
						<form:select path="zoneId" id="zoneId" class="form-control">
						    <form:option value="">Select</form:option>
							<form:options items="${zoneId}" />
						</form:select>
					</div>
					<br>
				</div>
				<br>
				<table class="table table-striped table-bordered" id="floorDetails">
					<thead>
						<tr>

							<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
									code="lbl.title.structureclassification" /></th>
							<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
									code="lbl.usage.nature" /></th>
							<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
									code="lbl.floorno" /></th>
							<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
									code="lbl.occupancy" /></th>
							<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
									code="lbl.construction.date" /></th>
							<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
									code="lbl.constructed.plinth.area" /></th>
							<th class="text-center floor-toggle-mandatory"><span></span>&nbsp;<spring:message
									code="lbl.building.plan.plinth.area" /></th>
							<th class="text-center"><spring:message code="lbl.action" /></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<!-- 							<td></td> -->
							<td><form:select path="floorTemp[0].classificationId"
									id="floorTemp[0].classificationId" class="form-control">
									<form:option value="">Select</form:option>
									<form:options items="${classificationId}" />
								</form:select></td>
							<td><form:select path="floorTemp[0].usageId"
									id="floorTemp[0].usageId" class="form-control">
									<form:option value="">Select</form:option>
									<form:options items="${usageId}" />
								</form:select></td>
							<td><form:select path="floorTemp[0].floorId"
									id="floorTemp[0].floorId" class="form-control">
									<form:option value="">Select</form:option>
									<form:options items="${floorId}" />
								</form:select></td>
							<td><form:select path="floorTemp[0].occupancyId"
									id="floorTemp[0].occupancyId" class="form-control">
									<form:option value="">Select</form:option>
									<form:options items="${occupancyId}" />
								</form:select></td>
							<td><form:input path="floorTemp[0].constructionDate"
									id="floorTemp[0].constructionDate" type="text"
									class="form-control datepicker constructionDate"
									data-date-end-date="0d" required="required" /></td>
							<td><form:input path="floorTemp[0].constructedPlinthArea"
									id="floorTemp[0].constructedPlinthArea" type="text"
									required="required" class="form-control" /></td>
							<td><form:input path="floorTemp[0].plinthAreaInBuildingPlan"
									id="floorTemp[0].plinthAreaInBuildingPlan"
									type="text" required="required" class="form-control" /></td>
							<td class="text-center"><a href="javascript:void(0);"
								class="btn-sm btn-danger" id="deleteFloorRow"><i
									class="fa fa-trash"></i></a></td>
						</tr>
					</tbody>

				</table>

				<div class="text-right add-padding">
					<button type="button" class="btn btn-sm btn-primary"
						id="addFloorRow">ADD FLOOR</button>
				</div>

			</div>
			<div class="row">
				<div class="text-center">
					<button type="button" id="calculateTax" class="btn btn-primary">
						Calculate</button>
					<button type="button" id="btnReset" class="btn btn-default">Reset</button>
				</div>
			</div>
		</div>
	</div>
</form:form>
<br>
<div class='view-content text-center' id="arv" style='font-size: 150%'></div>
<br>
<table width="150%" class="table table-sm table-bordered display-hide"
	align="center" id="taxResult">
	<thead>
		<tr>
			<th>Tax Description</th>
			<th class="text-right">Amount</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/calculatePropertyTax.js?rnd=${app_release_no}'/>"></script>
