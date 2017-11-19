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

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>

<title>Search Result</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">  
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">   
<link rel="stylesheet" href="../resources/css/jquerymobile/jquery.mobile-1.3.1.min.css" />
<script src="../resources/js/jquery-1.7.2.min.js"></script>
<script src="../resources/js/jquerymobile/jquery.mobile-1.3.1.min.js"></script>
<style>
@media screen and (min-width: 20em) {
	   .my-custom-class th.ui-table-priority-1,
	   .my-custom-class td.ui-table-priority-1 {
	     display: table-cell;
	   }
	}
	/* Show priority 2 at 480px (30em x 16px) */
	@media screen and (min-width: 30em) {
	   .my-custom-class  th.ui-table-priority-2,
	   .my-custom-class td.ui-table-priority-2 {
	     display: table-cell;
	   }
	}
</style>
<script>
function  openBackWindow()
{
	var queryString = "";
	var count = 0;
	var estNo = document.getElementById("estimateNumber").value;
	var fromDate = document.getElementById("fromDate").value;
	var toDate = document.getElementById("toDate").value;
	var typeId = document.getElementById("natureOfWork.id").value;
	var executingDepartmentId = document.getElementById("execDeptId").value;
	if(estNo!='')
	{
		queryString="?estimateNumber="+estNo;
		count++;
	}
	if(fromDate!='')
	{
		if(count>0)
			queryString = queryString+"&fromDate="+fromDate;
		else
			queryString = "?fromDate="+fromDate;
		count++;
	}
	if(toDate!='')
	{
		if(count>0)
			queryString = queryString+"&toDate="+toDate;
		else
			queryString = "?toDate="+toDate;
		count++;
	}
	if(typeId!='' && typeId!='-1')
	{
		if(count>0)
			queryString = queryString+"&natureOfWork.id="+typeId;
		else
			queryString = "?natureOfWork.id="+typeId;
		count++;
	}
	if(executingDepartmentId!='' && executingDepartmentId!='-1')
	{
		if(count>0)
			queryString = queryString+"&execDeptId="+executingDepartmentId;
		else
			queryString = "?execDeptId="+executingDepartmentId;
		count++;
	}
	if(count>0)
		window.open('${pageContext.request.contextPath}/mobile/uploadEstimatePhotos!search.action'+queryString,'_self');
	else
		window.open('${pageContext.request.contextPath}/mobile/uploadEstimatePhotos!search.action','_self');
}
</script>
</head>

<body>
	<form action="#" method="get" name="tablet" id="tablet"	data-ajax="false">
	<s:push value="model">
	<s:hidden id="estimateNumber" name="estimateNumber" />
	<s:hidden id="fromDate" name="fromDate" />
	<s:hidden id="toDate" name="toDate" />
	<s:hidden id="natureOfWork.id" name="natureOfWork.id" />
	<s:hidden id="execDeptId" name="execDeptId" />
	<div data-role="page" id="searchResultDiv" data-add-back-btn="true" class="pageclass">
	<div data-theme="b" data-role="header" data-position="fixed">
			<a href="#" onclick="openBackWindow();" data-ajax="false" id="backbtn" data-icon="arrow-l">Back</a>
			<h5>Search Result</h5>
	</div>
	<s:if test="%{estimateList.size!=0}">
		<s:text name="estimate.search.only.hundred.items"/>
	</s:if>	
	 <div align="center" style="padding: 10px," data-role="content" class="ui-content" role="main"></div>
 		<s:if test="%{hasActionMessages()}"><div align="center" class="errorstyle"><h3><s:actionmessage /> <h3> </div></s:if>
		<s:if test="%{estimateList.size!=0}">
			<table data-role="table" id="table-custom-2"
				data-mode="columntoggle"
				class="ui-body-d ui-shadow table-stripe ui-responsive"
				data-column-btn-theme="b"
				data-column-btn-text="Columns to display..."
				data-column-popup-theme="a">
				<thead>
					<tr data-mini="true" class="ui-bar-d">
						<th>Estimate Number</th>
						<th data-mini="true" data-priority="2">Estimate Value</th>
						<th data-mini="true" data-priority="1">Name Of Work</th>
						<th data-mini="true" data-priority="3">Estimate Date</th>
						<th data-mini="true" data-priority="4">Nature Of Work</th>
						<th>Capture Photo</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="estimateList" status="status">
						<tr data-mini="true">
							<th><s:property value="%{estimateNumber}" /></th>
							<td data-mini="true"><s:property value="%{totalAmount.formattedString}" /></td>
							<td data-mini="true"><s:property value="%{name}" /></td>
							<td data-mini="true"><s:date name="estimateDate" format="dd/MM/yyyy" /></td>
							<td data-mini="true"><s:property value="%{natureOfWork.name}" /></td>
							<td data-mini="true"><a data-mini="true" href="${pageContext.request.contextPath}/mobile/uploadEstimatePhotos!upload.action?estId=<s:property value="%{id}"/>"
									data-rel="external" data-ajax="false"><img src="../css/jquerymobile/images/camera-icon-small.png" border="0" /></a></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</s:if>
		<s:else>
			<p align="center"><strong>No search result</strong></p>
		</s:else>
	</div>
	</s:push>
	</form>
</body>
</html>
