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


<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>

<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<link href="<egov:url path='/resources/css/displaytag.css'/>"
	rel="stylesheet" type="text/css" />
<html>
<head>
<title><s:text name="bill.register.report" /></title>

</head>
<script>
	function doAfterSubmit(){
		document.getElementById('loading').style.display ='block';
	}
</script>
<body>
	<s:form action="billRegisterReport" name="billRegisterReport"
		theme="simple" method="post" onsubmit="javascript:doAfterSubmit()">
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<font style='color: red; font-weight: bold'>
			<p class="error-block" id="lblError"></p>
		</font>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="bill.register.report" />
			</div>
		</div>
		<table align="center" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<jsp:include page="../voucher/voucher-filter.jsp" />

			</tr>

			<tr>
				<td class="greybox"><s:text name="voucher.fromdate" /></td>
				<td class="greybox"><s:date name="fromDate" var="fromDateId"
						format="dd/MM/yyyy" /> <s:textfield name="fromDate" id="fromDate"
						value="%{fromDateId}" maxlength="10"
						onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
					href="javascript:show_calendar('billRegisterReport.fromDate',null,null,'DD/MM/YYYY');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
				</td>
				<td class="greybox"><s:text name="voucher.todate" /></td>
				<td class="greybox"><s:date name="toDate" var="toDateId"
						format="dd/MM/yyyy" /> <s:textfield name="toDate" id="toDate"
						value="%{toDateId}" maxlength="10"
						onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
					href="javascript:show_calendar('billRegisterReport.toDate',null,null,'DD/MM/YYYY');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
			</tr>
			<tr>
				<td class="bluebox"><s:text name="bill.expenditure.type" /></td>
				<td class="bluebox"><s:select name="exptype" id="exptype"
						list="dropdownData.expenditureList" headerKey=""
						headerValue="----Choose----" /></td>
				<td class="bluebox"><s:text name="bill.type" /></td>
				<td class="bluebox"><s:select name="billType" id="billType"
						list="dropdownData.billTypeList" headerKey=""
						headerValue="----Choose----" /></td>
			</tr>
		</table>
		<div class="buttonbottom">
			<s:submit method="list" value="Search" cssClass="buttonsubmit"
				onclick="return validate();" />
			<s:submit method="newform" value="Cancel" cssClass="button" />
			<input type="button" value="Close"
				onclick="javascript:window.close()" class="button" />

		</div>
		<div id="loading" class="loading"
			style="width: 700; height: 700; display: none" align="center">
			<blink style="color: red">Searching processing, Please
				wait...</blink>
		</div>
		<br>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">

			<s:if test="%{searchResult.fullListSize != 0}">
				<tr align="right">
					<td colspan="8">
						<div align="right" style="text-align: right">
							<strong><s:text name="report.amount.in.rupees" />&nbsp;&nbsp;&nbsp;</strong>
						</div>
					</td>
				</tr>
				<tr align="center">
					<td><display:table name="searchResult" export="true"
							id="searchResultid" uid="currentRowObject" cellpadding="0"
							cellspacing="0" requestURI="" sort="external" class="its"
							style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

							<display:column title="Sl.No" style="width:4%;text-align:center">
								<s:property
									value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}" />
							</display:column>

							<display:column title="Bill Number"
								style="width:10%;text-align:center" property="billNumber" />
							<display:column title="Bill Date"
								style="width:8%;text-align:center" property="billDate"
								sortProperty="billdate" sortable="true" />
							<display:column title="Voucher number"
								style="width:11%;text-align:center" property="voucherNumber" />
							<display:column title="Party Name"
								style="width:5%;text-align:center" property="partyName" />
							<display:column title="  Gross Amount"
								style="width:7%;text-align:right" property="grossAmount" />
							<display:column title="   Deduction"
								style="width:7%;text-align:right" property="deductionAmount" />
							<display:column title="Net Amount"
								style="width:7%;text-align:right" property="netAmount" />
							<display:column title="Paid Amount"
								style="width:7%;text-align:right" property="paidAmount" />
							<display:column title="Payment voucher number "
								style="width:11%;text-align:center"
								property="paymentVoucherNumber" />
							<display:column title="Cheque No and Date"
								style="width:11%;text-align:center" property="chequeNumAndDate" />
							<display:column title="Status"
								style="width:10%;text-align:center" property="status" />
							<display:caption media="pdf">
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill Register Report  
						   
						   
				   </display:caption>

							<display:setProperty name="export.pdf" value="true" />
							<display:setProperty name="export.pdf.filename"
								value="BillRegister-Report.pdf" />
							<display:setProperty name="export.excel" value="true" />
							<display:setProperty name="export.excel.filename"
								value="BillRegister-Report.xls" />
							<display:setProperty name="export.csv" value="false" />
							<display:setProperty name="export.xml" value="false" />
						</display:table></td>
				</tr>

			</s:if>
			<s:elseif test="%{searchResult.fullListSize == 0}">
				<tr>
					<td colspan="7" align="center"><font color="red">No
							record Found.</font></td>

				</tr>
			</s:elseif>
		</table>

	</s:form>

	<script>
		function validate(){
	
			
		 <s:if test="%{isFieldMandatory('fund')}"> 
				 if(null != document.getElementById('fundId') && document.getElementById('fundId').value == -1){

					document.getElementById('lblError').innerHTML = "Please Select a fund";
					return false;
				 }
			 </s:if>
			<s:if test="%{isFieldMandatory('department')}"> 
				 if(null!= document.getElementById('vouchermis.departmentid') && document.getElementById('vouchermis.departmentid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a department";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('scheme')}"> 
				 if(null!=document.getElementById('schemeid') &&  document.getElementById('schemeid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a scheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('subscheme')}"> 
				 if(null!= document.getElementById('subschemeid') && document.getElementById('subschemeid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a subscheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('functionary')}"> 
				 if(null!=document.getElementById('vouchermis.functionary') &&  document.getElementById('vouchermis.functionary').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a functionary";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('fundsource')}"> 
				 if(null !=document.getElementById('fundsourceId') &&  document.getElementById('fundsourceId').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a fundsource";
					return false;
				}
			</s:if>
			<s:if test="%{isFieldMandatory('field')}"> 
				 if(null!= document.getElementById('vouchermis.divisionid') && document.getElementById('vouchermis.divisionid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a field";
					return false;
				 }
			</s:if>
			return  true;
}
	</script>
</body>
</html>
