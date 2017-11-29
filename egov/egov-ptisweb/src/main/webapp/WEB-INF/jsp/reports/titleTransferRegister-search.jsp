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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><s:text name='titleTransferRegReport.search' /></title>
</head>
<body>
	<div id="titleTransferError" class="errorstyle" style="display:none;"></div>
	<div class="row">
		<div class="col-md-12">
			<div class="panel-body">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
					<s:fielderror/>
				</div>			
			</s:if>
				<s:form name="titleTransferRegForm" action="titleTransferRegister" theme="simple"
					cssClass="form-horizontal form-groups-bordered">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title text-left"><s:text name='titleTransferRegReport.search' /></div>
						</div>
						<div class="panel-body custom-form">
						
						<s:hidden id="finYearStartDate" name="finYearStartDate" value="%{finYearStartDate}"/> 
						
							<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="Zone" /> :</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1"
										headerValue="%{getText('default.select')}" name="zoneId"
										id="zoneId" listKey="id" listValue="name"
										list="dropdownData.zoneList" cssClass="form-control" value="%{zoneId}"/>
								
								</div>
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="Ward" /> :</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1"
										headerValue="%{getText('default.select')}" name="wardId"
										id="wardId" listKey="id" listValue="name"
										list="dropdownData.wardList" cssClass="form-control" value="%{wardId}" 
										onchange="populateBlock()"/>
										<egov:ajaxdropdown id="areaId" fields="['Text','Value']"
											dropdownId="areaId" url="common/ajaxCommon-areaByWard.action" />
								</div>
							</div>
							
							
							<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="block" /> :</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1"
										headerValue="%{getText('default.select')}" name="areaId"
										id="areaId" listKey="key" listValue="value"
										list="dropdownData.blockList" cssClass="form-control" value="%{areaId}" 
										/>
								</div>
							</div>
							
							
							<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="titleTransferRegReport.fromdate" /> :</label>
								 <s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy" />		
								<div class="col-sm-3 add-margin">
									<s:textfield id="fromDate"	name="fromDate" value="%{fromDate}"
										onkeyup="DateFormat(this,this.value,event,false,'3')"
										placeholder="DD/MM/YYYY"
										cssClass="form-control datepicker"
										data-inputmask="'mask': 'd/m/y'"
										onblur="validateDateFormat(this);" />
								</div>
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="titleTransferRegReport.todate" /> :</label>
								 <s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy" />		
								<div class="col-sm-3 add-margin">
									<s:textfield id="toDate" name="toDate" value="%{toDate}"
										onkeyup="DateFormat(this,this.value,event,false,'3')"
										placeholder="DD/MM/YYYY"
										cssClass="form-control datepicker"
										data-inputmask="'mask': 'd/m/y'"
										onblur="validateDateFormat(this);" />
								</div>
							</div>
							
						</div>
					</div>
				</s:form>

				<div class="row">
					<div class="text-center">
						<button type="button" id="btnsearch" class="btn btn-primary">
							Search</button>
						<button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
							Close</button>
					</div>
				</div>
			</div>

			<div class="row display-hide report-section">
			<input type="hidden" value="${sessionScope.citymunicipalityname}, ${sessionScope.districtName} District" id="pdfTitle"/>
			<div class="col-md-12 table-header text-left" id="reportTitle">Title Transfer Register Report Details</div>
				<div class="col-md-12 form-group report-table-container">
					<table class="table table-bordered table-hover multiheadertbl" id="tblTitleTransfer">
						<thead>
							<tr>
								<th>Assessment No</th>
								<th>Owner Name</th>
								<th>Door No</th>
								<th>Location</th>
								<th>Property Tax</th>
								<th>Old Title</th>
								<th>Changed Title</th>
								<th>Date of Transfer</th>
								<th>Commissioner Orders</th>
								<th>Mutation Fee</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/jquery.dataTables.min.css' context='/egi'/>"/>
<link rel="stylesheet" href="<cdn:url value='/resources/global/css/jquery/plugins/datatables/dataTables.bootstrap.min.css' context='/egi'/>">
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script
	src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"
	type="text/javascript"></script>
<script type="text/javascript" src="<cdn:url value='/resources/javascript/titleTransferRegister.js'/>"></script>
</html>