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
<script type="text/javascript"
	src="/EGF/resources/javascript/fundFlow.js?rnd=${app_release_no}">
</script>
<title><s:text name="trialbalancereport" /></title>
<script type="text/javascript">
var toDateStr="";
	function validate(exportValue) {
		document.getElementById('exportType').value=exportValue;
		document.getElementById('error').innerHTML="";
		var fromDate=document.getElementById('fromDate').value;
		var toDate=document.getElementById('toDate').value;
		
		var reportType = document.getElementById('reportType').value;
		//bootbox.alert(reportType);
		var today = new Date();
var dd = today.getDate();
var mm = today.getMonth()+1; //January is 0!
var yyyy = today.getFullYear();
today = dd+'/'+mm+'/'+yyyy;


				if (reportType == 'daterange') {
			if (document.getElementById('fromDate').value == '') {
				bootbox.alert('Please select From Date');
				return false;
			}  if (document.getElementById('toDate').value == '') {
				bootbox.alert('Please select To Date');
				return false;
			}  if (document.getElementById('fundId').value == '') {
				bootbox.alert('Please select Fund');
				return false;
			}  
 
			 if( compareDate(formatDate6(document.getElementById('fromDate').value),formatDate6(document.getElementById('toDate').value)) == -1 ){
				bootbox.alert('Start Date cannot be greater than End Date');
				document.getElementById('fromDate').value='';
				document.getElementById('toDate').value='';
				document.getElementById('fromDate').focus();
				return false;
			}
		} else if (reportType == 'asondate') {
			if (document.getElementById('toDate').value == '') {
				bootbox.alert('Please select As On Date');
				return false;
			}
			document.getElementById('fromDate').value='';
			      
		}
		 if(exportValue=='html'){
				//doAfterSubmit();
		} 

		//doAfterSubmit();
		document.forms[0].action='${pageContext.request.contextPath}/report/trialBalance-search.action';
		document.forms[0].submit();
		
   return true;
	}
	function doAfterSubmit(){
		document.getElementById('loading').style.display ='block';
	}
	
	
	function changeLable() {
			if(toDateStr=="")
			{
			toDateStr=document.getElementById('toDatelbl').innerHTML;
			}
			var reportType = document.getElementById('reportType').value;
			if (reportType == 'asondate') {
				document.getElementById('toDatelbl').innerHTML="As On Date";
				document.getElementById('fundStar').style.display="none";
				document.getElementById('fromDate').value="";
				document.getElementById('fromDate').disabled=true;
			}else
			{
			document.getElementById('fundStar').style.display="block";
			document.getElementById('fromDate').disabled=false;   
			if(toDateStr!="")
			{
			document.getElementById('toDatelbl').innerHTML=toDateStr;
			}
			else
			{
			document.getElementById('toDatelbl').innerHTML="To Date";
			}
			}

		}
	function onLoad(){
		document.getElementById('loading').style.display ='none';                             
	}
	function showDetails(glcode,startDate,endDate){           
		var deptId = '<s:property value="departmentId"/>';
		var functionaryId = '<s:property value="functionaryId"/>';
		var functionName = '<s:property value="functionName"/>';
		var functionId = '<s:property value="functionId"/>';
		var fieldId = '<s:property value="divisionId"/>';	   
		var fundId='<s:property value="fundId"/>';
                                             
		
		 var functionCode1=functionName+"~"+functionId;

		// bootbox.alert(functionCode1);
		if(functionId==0){
			functionCode1="";
			functionId="";
			}                   
		
		window.open('/EGF/report/generalLedgerReport-searchDrilldown.action?fromBean=1&glCode1='+glcode+'&fund_id='+fundId+'&startDate='+startDate+'&endDate='+endDate+'&departmentId='+deptId+'&functionaryId='+functionaryId+'&functionCodeId='+functionId+'&functionCode='+functionCode1+'&fieldId='+fieldId,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
</script>
</head>
<body onload="onLoad();">

	<s:form name="trialBalance" action="trialBalance" theme="simple">
		<div class="formmainbox">
			<div class="subheadnew">Trial Balance</div>
			<s:push value="model">
				<jsp:include page="../budget/budgetHeader.jsp">
					<jsp:param name="heading" value='trialbalancereport' />
				</jsp:include>
				<span class="mandatory1" id="error"> <s:actionerror /> <s:fielderror />
					<s:actionmessage />
				</span>
				<span class="subheadnew"><s:property value="ulbName" /></span>
				<table id="header" width="100%" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td class="bluebox"></td>
						<td class="bluebox"><s:text name="reporttype" />
						<td class="bluebox"><s:select name="reportType"
								id="reportType"
								list="#{'daterange':'Date Range','asondate':'As On Date'}"
								onChange="changeLable()" /></td>
						<td colspan="2" class="bluebox"></td>
					</tr>
					<tr>
						<td class="greybox"></td>
						<s:date name="fromDate" format="dd/MM/yyyy" var="tempFromDate" />
						<s:date name="toDate" format="dd/MM/yyyy" var="tempToDate" />
						<td class="greybox"><div id="fromDatelbl">
								<s:text name="fromdate" />
								<span class="greybox"><span class="mandatory1">*</span></span>
							</div></td>
						<td class="greybox"><s:textfield name="fromDate"
								id="fromDate"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								value="%{tempFromDate}" /> <a
							href="javascript:show_calendar('forms[0].fromDate');"
							style="text-decoration: none">&nbsp;<img tabIndex="-1"
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>
						</td>
						<td class="greybox"><div id="toDatelbl">
								<s:text name="todate" />
								<span class="greybox"><span class="mandatory1">*</span></span>
							</div></td>
						<td class="greybox"><s:textfield name="toDate" id="toDate"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								value="%{tempToDate}" /> <a
							href="javascript:show_calendar('forms[0].toDate');"
							style="text-decoration: none">&nbsp;<img tabIndex="-1"
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>
						</td>
					</tr>
					<tr>
						<td class="bluebox"></td>
						<td class="bluebox"><div id="fundlbl">
								<s:text name="voucher.fund" />
								<span id="fundStar" class="mandatory1">*</span>
							</div>
						<td class="bluebox"><s:select name="fundId" id="fundId"
								list="dropdownData.fundList" listKey="id" listValue="name"
								headerKey="" headerValue="----Choose----" value="%{fundId}" /></td>
						<td class="bluebox"><s:text name="voucher.department" />
						<td class="bluebox"><s:select name="departmentId"
								id="departmentid" list="dropdownData.departmentList"
								listKey="id" listValue="name" headerKey=""
								headerValue="----Choose----" value="departmentId" /></td>
					</tr>
					<tr>
						<td class="greybox"></td>
						<td class="greybox"><s:text name="voucher.function" />
						<td class="greybox"><s:select name="functionId"
								id="functionId" list="dropdownData.functionList" listKey="id"
								listValue="name" headerKey="" headerValue="----Choose----"
								value="functionId" style="width:180px" /></td>
						<td class="greybox"><s:text name="voucher.functionary" />
						<td class="greybox"><s:select name="functionaryId"
								id="functionaryId" list="dropdownData.functionaryList"
								listKey="id" listValue="name" headerKey=""
								headerValue="----Choose----" value="functionaryId"
								style="width:180px" /></td>
					</tr>
					<tr>
						<td class="bluebox"></td>
						<td class="bluebox"><s:text name="voucher.field" />
						<td class="bluebox"><s:select name="divisionId"
								id="divisionId" list="dropdownData.fieldList" listKey="id"
								listValue="name" headerKey="" headerValue="----Choose----"
								value="divisionId" /></td>
					</tr>
				</table>

			</s:push>
		</div>
		<div class="buttonbottom">
			<s:hidden name="exportType" id="exportType" />
			<s:submit value="View HTML" method="search" cssClass="button"
				onClick="return validate('html');" />
			<s:submit value="Export EXCEL" method="search" cssClass="button"
				onClick="return validate('xls');" />
			<s:submit value="Export PDF" method="search" cssClass="button"
				onClick="return validate('pdf');" />
			<input type="button" value="Close"
				onclick="javascript:window.close()" class="button" />
		</div>
	</s:form>
	<s:if test="%{al.size!=0}">

		<script>
			document.getElementById('loading').style.display ='none';
		</script>
		<br />
		<table border="0" cellspacing="0" cellpadding="0" class="tablebottom"
			width="100%">
			<tr>
				<th class="bluebgheadtd" width="100%" colspan="12"><strong
					style="font-size: 15px;"><s:property value="heading" /> </strong></th>
			</tr>
			<tr>
				<td class="bluebox" colspan="4"><strong><s:text
							name="report.run.date" />:<s:date name="todayDate"
							format="dd/MM/yyyy" /></strong></td>
				<td colspan="12">
					<div class="blueborderfortd" align="right">
						<strong> <s:text name="report.amount.in" />
							Rupees&nbsp;&nbsp;&nbsp;&nbsp;
						</strong>
					</div>
				</td>
			</tr>
			<s:if test='%{reportType == "daterange"}'>
				<tr>
					<th class="bluebgheadtd">Sl.No.</th>
					<th class="bluebgheadtd">Account Number</th>
					<th class="bluebgheadtd">Account Head</th>
					<th class="bluebgheadtd">Opening Balance(Rs)</th>
					<th class="bluebgheadtd">Debit(Rs)</th>
					<th class="bluebgheadtd">Credit(Rs)</th>
					<th class="bluebgheadtd">Closing Balnce(Rs)</th>

				</tr>
				<s:iterator var="p" value="al" status="s">
					<tr>

						<td style="text-align: center" class="blueborderfortd"><s:if
								test='%{accCode != "   Total  "}'>
								<s:property value="#s.index+1" />
							</s:if> <s:else>
								<s:property value="" />
							</s:else></td>

						<td style="text-align: center" class="blueborderfortd"><s:if
								test='%{accCode != "   Total  "}'>
								<a href="javascript:void(0);"
									onclick='return showDetails(<s:property value="accCode"/>,"<s:date name="%{fromDate}" format="dd/MM/yyyy"/>","<s:date name="%{toDate}" format="dd/MM/yyyy"/>")'><s:property
										value="accCode" /></a>&nbsp;</div>
							</s:if> <s:else>
								<s:property value="accCode" />
							</s:else></td>
						<td style="text-align: left" class="blueborderfortd"><s:property
								value="accName" /></td>
						<td style="text-align: right" class="blueborderfortd"><s:property
								value="openingBal" /></td>
						<td style="text-align: right" class="blueborderfortd"><s:property
								value="debit" /></td>
						<td style="text-align: right" class="blueborderfortd"><s:property
								value="credit" /></td>
						<td style="text-align: right" class="blueborderfortd"><s:property
								value="closingBal" /></td>

					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<th class="bluebgheadtd">Sl.No.</th>
					<th class="bluebgheadtd">Account Number</th>
					<th class="bluebgheadtd">Account Head</th>
					<s:iterator value="fundList" status="stat">
						<th class="bluebgheadtd" colspan="1"><s:property value="name" />(Rs)</th>
					</s:iterator>
					<th class="bluebgheadtd">Total(Rs)</th>
				</tr>

				<s:iterator var="p" value="al" status="s">
					<tr>
						<td style="text-align: center" class="blueborderfortd"><s:if
								test='%{accCode != "Total"}'>
								<s:property value="#s.index+1" />
							</s:if> <s:else>
								<s:property value="" />
							</s:else></td>
						<td style="text-align: center" class="blueborderfortd"><s:if
								test='%{accCode != "Total"}'>
								<a href="javascript:void(0);"
									onclick='return showDetails(<s:property value="accCode"/>,"<s:date name="%{finStartDate}" format="dd/MM/yyyy"/>","<s:date name="%{toDate}" format="dd/MM/yyyy"/>")'><s:property
										value="accCode" /> </a> &nbsp;</div>
							</s:if> <s:else>
								<s:property value="accCode" />
							</s:else></td>
						<td style="text-align: left" class="blueborderfortd"><s:property
								value="accName" /></td>
						<s:iterator value="fundList" status="stat">
							<td style="text-align: right" class="blueborderfortd"><s:set
									var="fnId" value="%{id+'_amount'}" /> <s:property
									value="%{fundWiseMap.get(#fnId)}" /></td>
						</s:iterator>

						<td style="text-align: right;" class="blueborderfortd"><s:property
								value="amount1" /></td>

					</tr>
				</s:iterator>
			</s:else>

		</table>
	</s:if>
</body>
</html>