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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<div class="row" id="page-content">
	<form:form method="post" action=""
					class="form-horizontal form-groups-bordered"
					id="form-statustimeogmrg" modelAttribute="registration">
					
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title">
								<strong><spring:message code="lbl.handicapped.report.title" /></strong>
							</div>
						</div>
  
						<div class="panel-body custom-form">
							<div class="form-group">
								<div class="col-sm-3 control-label">
								<label>Applicant Type</label>
								</div>
								<div class="col-sm-2">
										<select name="applicantType"  id="applicantType" class="form-control">
		   									<option>Both</option>
		   									<option>Husband</option>
		   									<option>Wife</option>
										</select>
								</div>
							</div>
						</div>
					</div>
				<div class="form-group">
					<div class="text-center">
						<button type="button" class="btn btn-primary"
							id="btn_handicapped_search">
							<spring:message code="lbl.search" />
						</button>
						<button type="reset" class="btn btn-default">
							<spring:message code="lbl.reset" />
						</button>
						<a href="javascript:void(0)" class="btn btn-default"
							onclick="self.close()"><spring:message code="lbl.close" /></a>
					</div>
				</div>
	</form:form>
</div>
<br>
<div class="row display-hide report-section"
	id="regstatustable_container">
<div class="row">
	<div class="col-md-6 col-xs-6 table-header"><spring:message code='lbl.searchresult'/></div>
	<div class="col-md-12 add-margin text-center error-msg display-hide" id="search-exceed-msg">Result contains more than 1000 records, please refine your search criteria.</div>
	<div class="col-md-12" id="searchResultDiv">	
<table class="table table-bordered table-hover multiheadertbl"
		id="handicappedSearchResults">
		<thead>
				<th>Serial No.</th>
				<th>Application No.</th>
				<th>Registration No.</th>
				<th>Registration Date</th>
				<th>Zone</th>
				<th>status</th>
				<th>Marriage Date</th>
				<th>Husband Name</th>
				<th>Wife Name</th>
			</thead>
			<tbody>
			</tbody>
	</table>
	</div>
</div>
</div>

<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>

<script
	src="<cdn:url value='/resources/js/app/handicapped-marriage.js?rnd=${app_release_no}'/> "></script>