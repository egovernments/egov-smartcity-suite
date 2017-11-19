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
<%@ taglib uri="/WEB-INF/taglibs/cdn.tld" prefix="cdn"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<input type="hidden" value="<spring:message code="error.date.todate.fromdate" />" id="errorToDateAndFromDate" />
		<form:form name="estimatePhotographSearchRequest" role="form" action="" modelAttribute="estimatePhotographSearchRequest" id="estimatePhotographSearchRequest" class="form-horizontal form-groups-bordered">
			<div class="row">
				<div class="col-md-12">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title" style="text-align:center;"><spring:message code="lbl.searchestimatephotograph" /></div>
						</div>
						<div class="panel-body">
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.workidentificationnumber" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="workIdentificationNumber" class="form-control" id="workIdentificationNumber" placeholder="Type first 3 letters of Work Identification Number" />
									<form:errors path="workIdentificationNumber" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.workordernumber" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="workOrderNumber" id="workOrderNumber" class="form-control" placeholder="Type first 3 letters of Work Order Number"/>
									<form:errors path="workOrderNumber" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.abstractestimate.createdby" /></label>
								<div class="col-sm-3 add-margin">
									<form:select path="estimateCreatedBy" data-first-option="false" id="aeCreatedByUsers" class="form-control">
										<form:option value=""><spring:message code="lbl.select" /></form:option>
										<form:options items="${aeCreatedByUsers}" itemValue="id" itemLabel="username" />
									</form:select>
									<form:errors path="estimateCreatedBy" cssClass="add-margin error-msg" />
								</div>
								<label class="col-sm-2 control-label text-right"><spring:message code="lbl.contractor.name" /></label>
								<div class="col-sm-3 add-margin">
									<form:input path="contractorName" id="contractorName" class="form-control" placeholder="Type first 3 letters of Contractor Name"/>
									<form:errors path="contractorName" cssClass="add-margin error-msg" />
								</div>
							</div>
						
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 text-center">
					<button type='button' class='btn btn-primary' id="btnsearch">
						<spring:message code='lbl.search' />
					</button>
					<a href='javascript:void(0)' class='btn btn-default'
						onclick='self.close()'><spring:message code='lbl.close' /></a>
				</div>
			</div>
		</form:form>  
<div class="display-hide report-section">
	<div class="table-header text-left">
		<spring:message code="title.estimatephotograph.search" /></div>
	<div class="form-group report-table-container">
		<table class="table table-bordered table-hover" 
			id="resultTable">
			<thead style="cursor:pointer;">
				<tr>
					<th><spring:message code="lbl.slNo" /></th>
					<th><spring:message code="lbl.workidentificationnumber" /></th>
					<th><spring:message code="lbl.nameofwork" /></th>
					<th><spring:message code="lbl.workordernumber" /></th>
					<th><spring:message code="lbl.contractor.name" /></th>
					<th><spring:message code="lbl.estimateamount" /></th>
					<th><spring:message code="lbl.workcompletion" /></th>
				</tr>
			</thead>
			<tbody >
			</tbody>
		</table>
	</div>
</div>
<script>
	$('#btnsearch').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
</script>
<script src="<cdn:url value='/resources/js/estimatephotograph/searchestimatephotograph.js?rnd=${app_release_no}'/>"></script>