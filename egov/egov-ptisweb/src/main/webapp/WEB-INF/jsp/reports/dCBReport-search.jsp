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
	<title><s:text name='dcbreport.search' /></title>
</head>
<body onload="setDefaultCourtCaseValue();">
	<div id="dcbError" class="errorstyle" style="display:none;"></div>
	<div class="row">
		<div class="col-md-12">
			<div class="panel-body">
				<s:form name="dcbForm" action="dCBReport" theme="simple"
					cssClass="form-horizontal form-groups-bordered">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title text-left"><s:text name="lbl.title.dcbReportPT.report"/></div> 
						</div>
						<div class="panel-body custom-form">
						<s:hidden id="mode" name="mode" value="%{mode}"/> 
						<s:hidden id="boundaryId" name="boundaryId" value=""/> 
						<s:hidden id="selectedModeBndry" name="selectedModeBndry" value="%{selectedModeBndry}"/> 
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"><s:text
										name="Ward" /> :</label>
								<div class="col-sm-3 add-margin" style="margin-bottom:15px;">
									<s:select headerKey="-1"
										headerValue="%{getText('default.select')}" name="wardId"
										id="wardId" listKey="key" listValue="value"
										list="wardBndryMap" cssClass="form-control" value="%{wardId}" />
								</div>
								
								<label class="col-sm-2 control-label text-right"><s:text
										name="ownership.type" /> :</label>
								<div class="col-sm-3 add-margin" style="margin-bottom:15px;">
									<s:select headerKey="-1" headerValue="%{getText('default.select')}" name="propTypes" multiple="true"
										id="propTypes" listKey="id" listValue="type" list="dropdownData.PropTypeMaster" value="%{propTypes}"
										cssClass="form-control"/> 
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-3 control-label text-right"><s:text
										name="dcbReport.courtCase.lbl" /> :</label>
								<div class="col-sm-3 add-margin" style="margin-bottom:15px;">
									<s:checkbox name="courtCase" id="courtCase" value="%{courtCase}" onclick="checkCourtCase(this);"/>
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
			<br />
			<div class="row display-hide report-section">
				<s:text name="reports.note.text" />
				<br />
				<input type="hidden" value="${sessionScope.citymunicipalityname}, ${sessionScope.districtName} District" id="pdfTitle"/>
				<div class="col-md-12 table-header text-left"><s:text name="lbl.hdr.dcbReportPT.details"/></div> 
				<div class="col-md-12 form-group report-table-container">
					<table class="table table-bordered table-hover multiheadertbl" id="tbldcbdrilldown">
						<thead>
                            <tr>
                             <th rowspan="2">Name</th>
                             <th rowspan="2">Door No</th>
							 <th rowspan="2">Owner Name</th>
							 <th rowspan="2">Assessment Count</th>
                             <th colspan="7">Demand</th>
                             <th colspan="7">Collection</th>
                             <th colspan="5">Balance</th>
                            </tr>

							<tr>
								<th>Arrear
									Property Tax</th>
								<th>Penalty
									On Arrear</th>
								<!-- <th>Arrear
									LibraryCess</th> -->
								<th>Arrear
									Total</th>
								<th>Current
									Property Tax</th>
								<th>Penalty
									On Current</th>
								<!-- <th>Current
									LibraryCess</th> -->
								<th>Current Total</th>
								<th>Total
									Demand</th>
								<th>Arrear
									Property Tax</th>
								<!-- <th>Arrear
									LibraryCess</th> -->
								<th>Penalty
									On Arrear</th>
								<th>Arrear
									Total</th>
								<th>Current
									Property Tax</th>
								<!-- <th>Current
									LibraryCess</th> -->
								<th>Penalty
									On Current</th>
								<th>Current
									Total</th>
								<th>Total
									Collection</th>
								<th>Arrear
									Property Tax</th>
								<th>Penalty
									On Arrear</th>
								<th>Current Property Tax</th>
								<th>Penalty
									On Current</th>
								<th>Total PropertyTax Balance</th>
							</tr>
						</thead>
						 <tfoot id="report-footer">
							<tr>
								<td>Total</td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							<!-- 	<td></td>
								<td></td>
								<td></td>
								<td></td> -->
							</tr>
						</tfoot> 
					</table>
				</div>
			</div>

			<div id="report-backbutton" class="col-xs-12 text-center">
				<div class="form-group"> <buttton class="btn btn-primary" id="backButton" > Back</buttton></div>
			</div>
		</div>
	</div>
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
<script type="text/javascript" src="<cdn:url value='/resources/javascript/dCBReport.js?rnd=${app_release_no}'/>"></script>
</body>
</html>