<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>">
	
</script>

<form:form name="bulkBoundaryRequest-form"
	class="form-horizontal form-groups-bordered"
	id="bulkBoundaryRequest-form" modelAttribute="PropertyMVInfo"
	method="POST">
	<div class="panel panel-primary" data-collapsed="0">
		<div class="panel-heading">
			<div class="panel-title">
				<spring:message code="lbl.bulkBoundaryUpdation.title" />
			</div>
		</div>

		<div class="panel-body custom-form">
			<div class="form-group">
				<form:label path="" class="col-sm-4 control-label">
					<spring:message code="lbl.assessment" />
				</form:label>

				<div class="col-sm-3 add-margin">
					<form:input id="propertyId" path="propertyId" type="text"
						cssClass="form-control is_valid_number" autocomplete="off"
						maxlength="10" />
				</div>
			</div>

			<div class="form-group">
				<label for="Field-2" class="col-sm-4 control-label"><spring:message
						code="lbl.doorNumber" /></label>

				<div class="col-sm-3 add-margin">
					<form:input id="houseNo" path="houseNo" type="text"
						cssClass="form-control " autocomplete="off" maxlength="10" />
				</div>
			</div>

			<div class="form-group">
				<label for="Field-3" class="col-sm-4 control-label"><spring:message
						code="lbl.zone" /></label>
				<div class="col-sm-3 add-margin">
					<form:select id="revenueZoneId" path="zone" value="${zone}"
						cssClass="form-control" multiple="false"
						cssErrorClass="form-control error">

						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${zonelist}" itemValue="id" itemLabel="name" />
					</form:select>

					<form:errors path="zone" cssClass="error-msg" />

				</div>
			</div>

			<div class="form-group">
				<label for="Field-7" class="col-sm-4 control-label"><spring:message
						code="lbl.locality" /></label>
				<div class="col-sm-3 add-margin">
					<form:select id="localityId" path="locality" multiple="false"
						cssClass="form-control localityClass" value="${locality}"
						cssErrorClass="form-control error">

						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${localityList}" itemValue="id"
							itemLabel="name" />
					</form:select>

					<form:errors path="locality" cssClass="error-msg" />

				</div>
			</div>


			<div class="form-group">
				<label for="Field-6" class="col-sm-4 control-label"><spring:message
						code="lbl.elec.wardno" /></label>
				<div class="col-sm-3 add-margin">

					<form:select id="electionWardId" path="electionWard"
						value="${electionWard}" cssClass="form-control" multiple="false"
						cssErrorClass="form-control error">

						<form:option value="">
							<spring:message code="lbl.select" />
						</form:option>
						<form:options items="${electionWardList}" itemValue="id"
							itemLabel="name" />
					</form:select>

					<form:errors path="electionWard" cssClass="error-msg" />

				</div>
			</div>


			<div class="row">
				<div class="text-center">
					<button type="button" class="btn btn-primary add-margin"
						id="bulkBoundarySearchBtn">
						<spring:message code="lbl.button.search" />
					</button>
					<button type="button" class="btn btn-primary add-margin"
						id="updateBtn">
						<spring:message code="lbl.update" />
					</button>
					<button type="button" onclick="self.close()"
						class="btn btn-primary add-margin">
						<spring:message code="lbl.close" />
					</button>
				</div>
			</div>
		</div>


	</div>
	<div>
		<div class="panel-body">
			<table
				class="table table-bordered datatable dt-responsive table-hover"
				id="bulkBoundarytable">
				<thead>
					<tr>
						<th><input type="checkbox" name="select_all"
							class="allCheckBoxClass" /></th>
						<th align="center" class="bluebgheadtd"><spring:message
								code="lbl.assessment" /></th>
						<th align="center" class="bluebgheadtd"><spring:message
								code="lbl.doorNumber" /></th>
						<th align="center" class="bluebgheadtd" style="width: 5%"><spring:message
								code="lbl.ownerName" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%"><spring:message
								code="lbl.revenue.zone" /></th>
						<th align="center" class="bluebgheadtd" style="width: 7%"><spring:message
								code="lbl.locality" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%"><spring:message
								code="lb.block" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%"><spring:message
								code="lbl.ward" /></th>
						<th align="center" class="bluebgheadtd" style="width: 15%"><spring:message
								code="lbl.elec.wardno" /></th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</form:form>



<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/js/app/bulkboundary.js?rnd=${app_release_no}'/>"></script>