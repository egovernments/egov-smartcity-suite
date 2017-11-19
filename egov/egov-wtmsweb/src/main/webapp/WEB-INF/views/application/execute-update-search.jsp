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
  <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
  <%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
  <%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
  
  <div class="row">
  	<div class="col-md-12">
  		<form:form method="get" id="executeWaterApplicationForm" modelAttribute="executeWaterApplicationDetails"
  			cssClass="form-horizontal form-groups-bordered">
  				<div class="panel panel-primary" data-collapsed="0">
  					<div class="panel-heading">
  						<div cass="panel-title">
  							<spring:message code="title.AppliactionSearch"></spring:message>
  						</div>
  					</div>
  					<div class="panel-body">
  					
  						<div class="form-group">
							<label class="col-sm-2 control-label text-right">
								<span class="mandatory"></span><spring:message code="lbl.applicationtype" />
							</label>
							<div class="col-sm-3 add-margin">
								<form:select path="applicationType" id="applicationType" cssClass="form-control"  data-first-option="false" required="required">
									<form:option value=""/>
									<form:options items="${applicationTypeList}" itemLabel="name" itemValue="name"/>
								</form:select>
								<form:errors path="applicationType" cssClass="add-margin error-msg" />
							</div>
	
							<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.revenue.ward" />
							</label>
							<div class="col-sm-3 add-margin">
								<form:select path="revenueWard" id="revenueWard" class="form-control" data-first-option="false">
									<form:option value="" />
									<form:options items="${revenueWardList}" name="revenueWard" id="revenueWard" itemValue="name" itemLabel="name"/>
								</form:select>
								<form:errors path="revenueWard" cssClass="add-margin error-msg" />
							</div>
						</div>
  					
  						<div class="form-group">
  							<label class="col-sm-2 control-label text-right">
  								<spring:message code="lbl.application.no"/>
  							</label>
  							<div class="col-sm-3 add-margin">
  								<form:input path="applicationNumber" id="applicationNumber" cssClass="form-control" maxlength="15"/>
  								<form:errors path="applicationNumber" cssClass="add-margin error-msg"/>
  							</div>
  							
  							<label class="col-sm-2 control-label text-right">
  								<spring:message code="lbl1.consumer.number"/>
  							</label>
  							<div class="col-sm-3 add-margin">
  								<form:input path="consumerNumber" id="consumerNumber" cssClass="form-control" maxlength="15"/>
  								<form:errors path="consumerNumber" cssClass="add-margin error-msg"/>
  							</div>
  						</div>
  						
  						<div class="form-group">
							<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.dailyReport.fromDate" />
							</label>
							<div class="col-sm-3 add-margin">
								<form:input path="fromDate" id="fromDate" cssClass="form-control datepicker" maxlength="15"/>
								<form:errors path="fromDate" cssClass="add-margin error-msg" />
							</div>
	
							<label class="col-sm-2 control-label text-right">
								<spring:message code="lbl.dailyReport.toDate" />
							</label>
							<div class="col-sm-3 add-margin">
								<form:input path="toDate" id="toDate" cssClass="form-control datepicker" maxlength="15"/>
								<form:errors path="toDate" cssClass="add-margin error-msg" />
							</div>
						</div>
						
						<div class="form-group">
							<div class="text-center">
								<button type="button" id="search" class="btn btn-primary">
									<spring:message code="lbl.submit"/>
								</button>
								
								<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">
									<spring:message code="lbl.close" />
								</a>
							</div>
						</div>
  					</div>
  				</div>
  		</form:form>
  	</div>
  </div>
  <table class="table table-bordered table-hover multiheadertbl"
			id="search-result-table" width="200%">
			<tbody>
			</tbody>
			<tfoot>
			</tfoot>
  </table>
	<div class="text-center">
		<button type="button" id="update" class="btn btn-primary updation">
			<spring:message code="lbl.update"></spring:message>
		</button>
	</div>
	
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>" />
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/js/app/executeWaterTapApplication.js?rnd=${app_release_no}'/>"></script>
 
  
  
  