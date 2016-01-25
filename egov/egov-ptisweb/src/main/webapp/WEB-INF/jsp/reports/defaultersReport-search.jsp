<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><s:text name='defaultersReport.title' /></title>
</head>
<body>
	<div id="defaultersReportError" class="errorstyle" style="display:none;"></div>
	<div class="row">
		<div class="col-md-12">
			<div class="panel-body">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
					<s:fielderror/>
				</div>			
			</s:if>
				<s:form name="defaultersReportForm" action="defaultersReport" theme="simple"
					cssClass="form-horizontal form-groups-bordered">
					<div class="panel panel-primary" data-collapsed="0">
						<jsp:useBean id="now" class="java.util.Date"/>
						<div class="panel-heading">
							<div class="panel-title text-left"><s:text name='defaultersReport.title' /></div>
						</div>
						<div class="panel-body custom-form">
							<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="Ward" /> :</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1"
										headerValue="%{getText('default.select')}" name="wardId"
										id="wardId" listKey="id" listValue="name"
										list="dropdownData.wardList" cssClass="form-control" value="%{wardId}"/>
								
								</div>
							</div>
							
							<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="fromamt" /> :</label>
								<div class="col-sm-3 add-margin">
									<s:textfield name="fromDemand" id="fromDemand" class="form-control" maxlength="10" onblur="validNumber(this);checkZero(this,'From Demand');" />
								</div>
								
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="toamt" /> :</label>
								<div class="col-sm-3 add-margin">
									<s:textfield name="toDemand" id="toDemand" class="form-control" maxlength="10" onblur="validNumber(this);checkZero(this,'To Demand');" />
								</div>
							</div>
							
							<div class="form-group">
								<label for="field-1" class="col-sm-2 control-label text-right"><s:text
										name="defaultersReport.limit" /> :</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1"
										headerValue="%{getText('default.select')}" name="limit"
										id="limit" 
										list="dropdownData.limitList" cssClass="form-control" value="%{limit}"/>
								
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
				<div class="col-md-12 table-header text-left"><s:text name="defaultersReport.details" /></div>
				<div class="col-md-12 table-header text-left"><s:text name="defaultersReport.message" /> <fmt:formatDate value="${now}" pattern="dd/MM/yyyy" /></div>
				<div class="col-md-12 form-group report-table-container">
					<table class="table table-bordered table-hover multiheadertbl" id="tblDefaultersReport">
						<thead>
							<tr>
								<th>Sl No</th>
								<th>Assessment No</th>
								<th>Owner Name</th>
								<th>Revenue Ward</th>
								<th>Door No</th>
								<th>Locality</th>
								<th>Mobile Number</th>
								<th>Arrears Amount</th>
								<th>Current Amount</th>
								<th>Total</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/javascript/defaultersReport.js'/>"></script>
</html>