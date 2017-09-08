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

<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row">
	<form:form id="sewerageSearchRequestForm" method="get"
		class="form-horizontal form-groups-bordered" modelAttribute="sewerage"
		commandName="">
		<div class="col-md-12">

			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title"></div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"> <spring:message
							code="lbl.application.number" /></label>
					<div class="col-sm-3 add-margin">
						<input type="text" name="consumerNumber" id="consumerNumber"
							class="form-control" maxlength="13"
							data-pattern="alphanumericwithspace" min="10" />
					</div>

					<label class="col-sm-2 control-label text-right"> <spring:message
							code="lbl.shsc.number" /></label>
					<div class="col-sm-3 add-margin">
						<input type="text" name="shscNumber" id="shscNumber"
							class="form-control is_valid_number" maxlength="11"
							id="app-appcodo" />
					</div>
				</div>
			</div>
		</div>

		<div class="col-md-12">
			<div class="text-center">
				<a href="javascript:void(0);" id="searchSewerageapplication"
					class="btn btn-primary"><spring:message code='lbl.search' /></a>

				<button class="btn btn-danger" type="reset">
					<spring:message code='lbl.reset' />
				</button>
				<a href="javascript:void(0);" id="closeComplaints"
					class="btn btn-default" onclick="self.close()"><spring:message
						code='lbl.close' /></a>
			</div>
		</div>
	</form:form>

	<div class="col-md-12">
		<br>
		<table class="table table-bordered datatable dt-responsive"
			id="aplicationSearchResults">

		</table>
	</div>
</div>

<link rel="stylesheet"
	href="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"
	type="text/javascript"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/moment.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url  value='/resources/global/js/jquery/plugins/datatables/datetime-moment.js' context='/egi'/>"></script>


<script
	src="<cdn:url  value='/resources/js/search/searchstmsforonlinepay.js?rnd=${app_release_no}'/>"
	type="text/javascript"></script>


