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


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<script language="javascript"
	src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="departmentwise.expenditure.report" /></title>
</head>

<script>
function onLoadTask(){
	//bootbox.alert("asdas");    
	document.getElementById('asset').style.display ='none';      
	//bootbox.alert("aaaa");
	document.getElementById('monthRow').style.display='none';
	document.getElementById('dateran').style.display='none';
	
	document.getElementById('fromDate').value="";
	document.getElementById('toDate').value="";	 
	document.getElementById('assetCode').value="0";
	//bootbox.alert("hiiii");  
}   

function changeType(obj){
	var type = obj.value;
	//bootbox.alert("---"+type);
	if (type == 'Month'){
		document.getElementById('monthRow').style.display ="inline";
		document.getElementById('dateran').style.display ="none";
		document.getElementById('fromDate').value="";
		document.getElementById('toDate').value="";
	}else if (type == 'daterange'){
		document.getElementById('dateran').style.display ="inline";
		document.getElementById('financialYearId').value="";
		document.getElementById('month').value="";
		document.getElementById('monthRow').style.display ="none";
	}else{
		document.getElementById('monthRow').style.display='none';
		document.getElementById('dateran').style.display='none';
		}
}

function validate(exportValue){
	var rpType=document.getElementById("reportType").value;
	var fund=document.getElementById("fundId").value;
	var finyear=document.getElementById("financialYearId").value;
	var mon=document.getElementById("month").value;
	//bootbox.alert("fundid"+fund);
	//bootbox.alert("exportValue"+exportValue);
	document.getElementById('exportType').value=exportValue;
	
	if(fund==''||fund=='0'){
		bootbox.alert("Please select a fund");
		return false;
	}
	if(rpType==''|| rpType=='0'){
		bootbox.alert("Please select report generation type");
		return false;
	}
	
	if (rpType == 'Month'){
		//bootbox.alert("Hi you have selected reporttype month");
		document.getElementById('fromDate').value="";
		document.getElementById('toDate').value="";
		if(finyear==''|| finyear=='0'){
			bootbox.alert("Please select report generation type");
			return false;
		}
		if(mon==''|| mon=='0'){
			bootbox.alert("Please select month");
			return false;
		}
		
	}
	else if(rpType == 'daterange'){
 		var fromDate =  Date.parse(document.getElementById('fromDate').value);
		var toDate =  Date.parse(document.getElementById('toDate').value);

		if(toDate=='' || fromDate==''){
			bootbox.alert("Please Enter both From Date and Todate ");
			return false;
		}
 		if(isNaN(fromDate)||isNaN(toDate)){
 			bootbox.alert("Please enter a valid date");
 			return false;
 	   	 }
 	}
	else{
 		//var fromDate =  Date.parse(document.getElementById('fromDate').value);
		//var toDate =  Date.parse(document.getElementById('toDate').value);
		document.getElementById('fromDate').value='';
		document.getElementById('toDate').value='';
	}
	return true;
		
}
function populateAssetCode(obj){
	//bootbox.alert()   
	var fund=document.getElementById("fundId").value;
	if(fund=='23'){
		document.getElementById('asset').style.display ="inline";
	}else{
		document.getElementById('asset').style.display ="none";
		document.getElementById('assetCode').value="0";
	}
}

</script>
<body onload="onLoadTask();">
	<div class="subheadnew">
		<s:text name="departmentwise.expenditure.report" />
	</div>
	<s:form name="departmentwiseExpenditureReport"
		action="departmentwiseExpenditureReport" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value='departmentwiseExpenditureReport' />
		</jsp:include>
		<span class="mandatory" id="error"> <s:actionerror /> <s:fielderror />
			<s:actionmessage />
		</span>


		<table id="header" width="100%" cellpadding="0" cellspacing="0"
			border="0">

			<tr>
				<td class="greybox" width="6%"></td>
				<td class="greybox" width="6%">
					<div id="fundlbl">
						<s:text name="report.fund" />
						<span id="fundStar" class="mandatory">*</span>
					</div>
				<td class="greybox"><s:select name="fundId" id="fundId"
						list="dropdownData.fundDropDownList" listKey="id" listValue="name"
						headerKey="" headerValue="----Choose----"
						onChange="populateAssetCode(this);" value="%{deptReport.fundId}" /></td>

				<td class="greybox" width="6%">
					<div id="reportlbl" width="4%">
						<s:text name="reporttype" />
						<span id="repStar" class="mandatory">*</span>
					</div>
				<td class="greybox"><s:select name="reportType"
						id="reportType" list="#{'daterange':'Date Range','Month':'Month'}"
						headerKey="0" headerValue="----Choose----"
						onChange="changeType(this)" value="%{deptReport.reportType}" /></td>
				<td class="greybox" width="4%"></td>
				<td class="greybox" width="4%"></td>
				<td class="greybox" width="4%"></td>
			</tr>

		</table>

		<div id="asset">
			<table id="header" width="100%" cellpadding="0" cellspacing="0"
				border="0">
				<tr>
					<td class="bluebox" width="6%"></td>
					<td class="bluebox" width="6%">
						<div id="explbl">
							Expenditure on<span class="mandatory">
						</div>
					</td>
					<td class="bluebox"><s:select name="assetCode" id="assetCode"
							list="#{'0':'---Choose---','412':'CWIP','410':'Fixed Asset'}"
							headerKey="0" value="%{deptReport.assetCode}" /></td>
					<td class="bluebox" width="6%">
					<td class="bluebox" width="4%">
					<td class="bluebox" width="4%">
					<td class="bluebox" width="4%">
					<td class="bluebox" width="4%">
				</tr>
			</table>
		</div>

		<div id="dateran">
			<table id="header" width="100%" cellpadding="0" cellspacing="0"
				border="0">
				<tr>
					<td class="bluebox" width="6%"></td>
					<td class="bluebox" width="6%"><s:text name="report.fromdate" /><span
						class="mandatory">*</span></td>
					<td class="bluebox"><s:date name="fromDate"
							format="dd/MM/yyyy" var="fromDateId" /> <s:textfield
							name="fromDate" id="fromDate" value="%{fromDateId}"
							maxlength="10"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
						href="javascript:show_calendar('departmentwiseExpenditureReport.fromDate',null,null,'DD/MM/YYYY');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
					</td>
					<td class="bluebox" width="6%">
					<td class="bluebox" width="6%"></td>
					<td class="bluebox"><s:text name="report.todate" /><span
						class="mandatory">*</span></td>
					<td class="bluebox"><s:date name="toDate" format="dd/MM/yyyy"
							var="toDateId" /> <s:textfield name="toDate" id="toDate"
							value="%{toDateId}" maxlength="10"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
						href="javascript:show_calendar('departmentwiseExpenditureReport.toDate',null,null,'DD/MM/YYYY');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
					</td>
					<td class="bluebox" width="4%">
					<td class="bluebox" width="4%">
					<td class="bluebox" width="4%">
				</tr>
			</table>
		</div>

		<div id="monthRow">
			<table id="header" width="100%" cellpadding="0" cellspacing="0"
				border="0">
				<tr>
					<td class="bluebox" width="6%"></td>
					<td class="bluebox" width="6%">

						<div id="monlbl">
							<s:text name="report.month" />
							<span class="mandatory">*</span>
						</div>
					</td>
					<td class="bluebox"><s:select name="month" id="month"
							list="#{'Select':'---Choose---','01':'January','02':'February','03':'March','04':'April','05':'May','06':'June','07':'July','08':'August','09':'September','10':'October','11':'November','12':'December'}"
							headerKey="0" value="%{deptReport.month}" /></td>
					<td class="bluebox" width="6%">
					<td class="bluebox" width="4%">
					<td class="bluebox"><s:text name="report.financialYear" />:<span
						class="mandatory">*</span> <s:select name="financialYearId"
							id="financialYearId" list="dropdownData.financialYearList"
							listKey="id" listValue="finYearRange" headerKey="0"
							headerValue="----Choose----"
							value="%{deptReport.financialYearId}" /></td>
					<td class="bluebox" width="4%">
					<td class="bluebox" width="4%">
					<td class="bluebox" width="4%">
				</tr>
			</table>
		</div>
		<div class="buttonbottom">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<s:hidden name="exportType" id="exportType"
						value="%{deptReport.exportType}" />
					<s:submit value="Export EXCEL" method="search" cssClass="button"
						onClick="return validate('xls');" />
					<s:submit value="Export PDF" method="search" cssClass="button"
						onClick="return validate('pdf');" />
					<s:submit value="View HTML" method="search" cssClass="button"
						onClick="return validate('html');" />

					<input type="button" value="Close"
						onclick="javascript:window.close()" class="button" />
				</tr>
			</table>
		</div>


	</s:form>
</body>
</html>
