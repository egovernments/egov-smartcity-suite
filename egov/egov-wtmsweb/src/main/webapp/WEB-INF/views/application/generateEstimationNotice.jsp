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

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">
					<strong><spring:message code='title.watertaxSearch' />
					</strong>
				</div>
			</div>
			
			<div class="panel-body">
 			<input type="hidden"  path="validMessage" id="validMessage" name="validMessage" value="${validMessage}" />
				<form:form  class="form-horizontal form-groups-bordered"
					id="waterSearchForm" modelAttribute="searchNoticeDetails" method="post" action="">
					<div class="form-group">
  							<label class="col-sm-2 control-label text-right">
  								<spring:message code="lbl.application.no"/>
  							</label>
  							<div class="col-sm-3 add-margin">
  								<form:input path="applicationNumber" id="applicationNumber" cssClass="form-control patternvalidation" data-pattern="alphanumericwithhyphen" maxlength="15"/>
  								<form:errors path="applicationNumber" cssClass="add-margin error-msg"/>
  							</div>
  							
  							<label class="col-sm-2 control-label text-right">
  								<spring:message code="lbl1.consumer.number"/>
  							</label>
  							<div class="col-sm-3 add-margin">
  								<form:input path="hscNo" id="hscNo" cssClass="form-control patternvalidation" data-pattern="number" maxlength="15"/>
  								<form:errors path="hscNo" cssClass="add-margin error-msg"/>
  							</div>
  						</div>
					
					<br/>
					<div class="form-group">
						<div class="text-center">
							<button type="button" class="btn btn-primary" id="submitButtonId" >
								<spring:message code="lbl.submit" />
							</button>
							<input type="button" class="btn btn-default" onclick="customReset();" value="Reset"/>
							<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">
									<spring:message code="lbl.close" /></a>
						</div>
				</div>
				</form:form>
			</div>

			<div class="row display-hide report-section">
				<div class="col-md-12 table-header text-left">Estimation
					Search Result</div>
				<div class="col-md-12 form-group report-table-container">
					<table class="table table-bordered table-hover multiheadertbl"
						id="resultTable">
						<thead>
							<tr>
								<th>
									<spring:message code="lbl.assesmentnumber" />   
								</th>
								<th>
								    <spring:message code="lbl1.consumer.number" />
								</th>
								<th>
									<spring:message code="lbl.nameofowner" /> 
								</th>
								<th>
								    <spring:message code="lbl.estimationnotice.no" />
								</th>
								<th>
								    <spring:message code="lbl.estimationnotice.date" />
								</th>
								<th>
								    <spring:message code="lbl.actions" />
								</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>

		</div>
	</div>
</div>
<script>
	function customReset() {
		$("#hscNo").val('');
		$("#applicationNumber").val('');
		
	}
</script>
<script src="<cdn:url value='/resources/js/app/estimationNotice.js?rnd=${app_release_no}'/>"
	type="text/javascript"></script>
	
<link rel="stylesheet" href="/egi/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css"/>
<link rel="stylesheet" href="/egi/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css">
<script type="text/javascript" src="/egi/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/egi/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="/egi/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js"></script>
<script type="text/javascript" src="/egi/resources/global/js/jquery/plugins/datatables/TableTools.min.js"></script>
<script type="text/javascript" src="/egi/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js"></script>

	

