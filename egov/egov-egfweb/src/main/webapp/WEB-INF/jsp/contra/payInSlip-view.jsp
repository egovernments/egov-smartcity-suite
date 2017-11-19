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
<html>

<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>

<script type="text/javascript"
	src="/EGF/resources/javascript/calender.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>Pay In</title>

</head>

<body onload="onbodyload();">

	<s:form action="payInSlip" theme="simple" name="payinform">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="PayInSlip" />
			</jsp:include>
			<div class="formmainbox">
				<div class="formheading" />
				<div class="subheadnew">Pay in</div>
				<div id="listid" style="display: block">
					<br />
					<div align="center">
						<font style='color: red;'>
							<p class="error-block" id="lblError"></p>
						</font> <span class="mandatory"> <s:actionerror /> <s:fielderror />
							<s:actionmessage />
						</span>
						<table border="0" width="100%">
							<tr>


								<td class="bluebox"><s:text name="payin.number" /><span
									class="mandatory">*</span></td>
								<td class="bluebox"><s:textfield name="voucherNumber"
										id="payinNumber" /></td>
								<td class="bluebox"><s:text name="payin.date" /><span
									class="mandatory">*</span></td>
								<td class="bluebox" width="34%"><input type=text
									name="voucherDate" id="voucherDate"
									onkeyup="DateFormat(this,this.value,event,false,'3')"
									value='<s:date name="voucherDate" format="dd/MM/yyyy"/>' /> <a
									href="javascript:show_calendar('payinform.voucherDate');"
									style="text-decoration: none">&nbsp;<img tabIndex="-1"
										src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>(dd/mm/yyyy)</td>
							</tr>
							<%@include file="payInSlip-form.jsp"%>
						</table>

						<div align="center">

							<div id="labelAD" align="center">
								<table width="80%" border=0 id="chequeDetails">
									<th>Cheque Detail</th>
								</table>
							</div>
							<div class="yui-skin-sam" align="center">
								<div id="billDetailTable"></div>
							</div>
							<script>
		
		makeVoucherDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="80%"
	 </script>

							<table align="center">
								<tr>
									<td width="455"></td>
									<td>Total Amount</td>
									<td><s:textfield name="totalAmount" id="totalAmount" /></td>
									</td>
								</tr>
							</table>
							<br />
							<table border="0" width="100%" id="buttonTable">
								<tr></tr>
								<tr align="center">
									<input type="submit" value="Close"
										onclick="javascript:window.close()" class="buttonsubmit" />

								</tr>
							</table>
							<input type="hidden" id="selectedInstr" name="selectedInstr" /> <input
								type="hidden" id="name" name="name" value="Pay In Slip" /> <input
								type="hidden" id="type" name="type" value="Contra" />
						</div>
					</div>
				</div>
		</s:push>
	</s:form>

	<script>
	
function onbodyload(){
<s:iterator value="iHeaderList" status="stat">
	document.getElementById("buttonTable").style.display="block";
	document.getElementById("chequeDetails").style.display="block";
	document.getElementById("billDetailTable").style.display="block";

</s:iterator>
document.getElementById("reversenumanddate").style.display="none";
for(var i=0;i<document.forms[0].length;i++)
	{
		
				if(document.forms[0].elements[i].value != 'Close'){
document.forms[0].elements[i].disabled =true;
					}	
		
				
								
		}	
	}
	

</script>

</body>

</html>
