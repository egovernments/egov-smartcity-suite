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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>

<style>

div.dataTables_processing { z-index: 1; }

</style>

<div class="row">
	<div class="col-md-12">




		<form:form class="form-horizontal form-groups-bordered" action=""
			id="waterSearchRequestForm" modelAttribute="dCBReportResult"
			method="get">

			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<c:choose>
							<c:when test="${reportType == 'zoneWise'}">
								<strong><spring:message code="title.zonewisedcbreport" /></strong>
							</c:when>
							<c:when test="${reportType == 'wardWise'}">
								<strong><spring:message code="title.wardwisedcbreport" /></strong>
							</c:when>
							<c:when test="${reportType == 'blockWise'}">
								<strong><spring:message code="title.blockwisedcbreport" /></strong>
							</c:when>
							<c:when test="${reportType == 'localityWise'}">
								<strong><spring:message
										code="title.localitywisedcbreport" /></strong>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<form:hidden path="mode" id="mode" value="${mode}" />
				<form:hidden path="boundaryId" id="boundaryId" value="${boundaryId}" />
				<form:hidden path="selectedModeBndry" id="selectedModeBndry"
					value="${selectedModeBndry}" />
				<form:hidden path="reportType" id="reportType" value="${reportType}" />
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-3 control-label text-right"><spring:message
								code="lbl.connectiontype" /><span class="mandatory"></span> </label>
						<div class="col-sm-3 add-margin">
							<form:select path="connectionType" id="connectionType"
								data-first-option="false" cssClass="form-control"
								required="required">
								<form:option value="">
									<spring:message code="lbl.select" />
								</form:option>
								<form:options items="${connectionTypes}" />
							</form:select>
							<form:errors path="connectionType"
								cssClass="add-margin error-msg" />
						</div>

					</div>
					<c:choose>
						<c:when test="${reportType == 'zoneWise'}">
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"> <spring:message
										code="lbl.zone" /> <span class="mandatory"></span>
								</label>
								<div class="col-sm-6 add-margin">
									<form:select path="zones" multiple="true" size="10" id="zones"
										cssClass="form-control" cssErrorClass="form-control error"
										required="required">
										<form:options items="${zones}" itemValue="id" itemLabel="name" />
									</form:select>

									<form:errors path="zones" cssClass="error-msg" />
								</div>
								<spring:message code="lbl.zones.pressCntrlToSelectMultipleZone" />
							</div>
						</c:when>
						<c:when test="${reportType == 'wardWise'}">
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"> <spring:message
										code="lbl.ward" /> <span class="mandatory"></span>
								</label>
								<div class="col-sm-6 add-margin">
									<form:select path="wards" multiple="true" size="10" id="wards"
										cssClass="form-control" cssErrorClass="form-control error"
										required="required">
										<form:options items="${wards}" itemValue="id" itemLabel="name" />
									</form:select>

									<form:errors path="wards" cssClass="error-msg" />
								</div>
								<spring:message code="lbl.wards.pressCntrlToSelectMultipleWard" />
							</div>
						</c:when>
						<c:when test="${reportType == 'blockWise'}">
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"> <spring:message
										code="lbl.block" /><span class="mandatory"></span>
								</label>
								<div class="col-sm-6 add-margin">
									<form:select path="blocks" multiple="true" size="10"
										id="blocks" cssClass="form-control"
										cssErrorClass="form-control error" required="required">
										<form:options items="${blocks}" itemValue="id"
											itemLabel="name" />
									</form:select>

									<form:errors path="blocks" cssClass="error-msg" />
								</div>
								<spring:message
									code="lbl.blocks.pressCntrlToSelectMultipleBlock" />
							</div>
						</c:when>
						<c:when test="${reportType == 'localityWise'}">
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"> <spring:message
										code="lbl.locality" /> <span class="mandatory"></span>
								</label>
								<div class="col-sm-6 add-margin">
									<form:select path="localitys" multiple="true" size="10"
										id="localitys" cssClass="form-control"
										cssErrorClass="form-control error" required="required">
										<form:options items="${localitys}" itemValue="id"
											itemLabel="name" />
									</form:select>

									<form:errors path="localitys" cssClass="error-msg" />
								</div>
								<spring:message
									code="lbl.localitys.pressCntrlToSelectMultipleLocality" />
							</div>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</div>
			</div>


			<div class="row">
				<div class="text-center">
					<button type="submit" id="btnsearch" class="btn btn-primary">
						<spring:message code="lbl.search" />
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="window.close();">
						<spring:message code="lbl.close" />
					</button>
				</div>
			</div>

		</form:form>
	</div>


</div>

<div class="row display-hide report-section">
	<div class="col-md-12 table-header text-left"><spring:message code="lbl.drill.report" /></div>
	<div class="col-md-12 form-group report-table-container">
		<table
			class="table table-bordered datatable dt-responsive table-hover multiheadertbl"
			id="tbldcbdrilldown">
			<thead>
				<tr>
					<th rowspan="2"><spring:message code="lbl.number" /> </th>
					<th colspan="4"><spring:message code="lbl.demand" /> </th>
					<th colspan="3"><spring:message code="lbl.collection" /></th>
					<th colspan="3"><spring:message code="lbl.balance" /> </th>
				</tr>

				<tr>
					<th><spring:message code="lbl.userName" /> </th>
					<th><spring:message code="lbl.arrear" /> </th>
					<th><spring:message code="lbl.current" /> </th>
					<th><spring:message code="lbl.total" /> </th>
					<th><spring:message code="lbl.arrear" /> </th>
					<th><spring:message code="lbl.current" /> </th>
					<th><spring:message code="lbl.total" /> </th>
					<th><spring:message code="lbl.arrear" /> </th>
					<th><spring:message code="lbl.current" /> </th>
					<th><spring:message code="lbl.total" /> </th>
				</tr>
			</thead>
			<tfoot id="report-footer">
				<tr>
					<td colspan="2" align="center"><spring:message code="lbl.total" /></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</tfoot>
		</table>
	</div>
</div>
<div id="report-backbutton" class="col-xs-12 text-center">
	<div class="form-group">
		<buttton class="btn btn-primary" id="backButton"> <spring:message code="lbl.back" /></buttton>
	</div>
</div>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/dCBReport.js?rnd=${app_release_no}'/>"></script>


