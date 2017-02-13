<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<html>

<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>Voucher - View</title>
<style type="text/css">
@media print {
	input#button1 {
		display: none;
	}
}

@media print {
	input#button2 {
		display: none;
	}
}

@media print {
	a#sourceLink {
		display: none;
	}
}

@media print {
	div#heading {
		display: none;
	}
}

@media print {
	div.commontopyellowbg {
		display: none;
	}
}

@media print {
	div.commontopbluebg {
		display: none;
	}
}

@media print {
	div.sourceIcon {
		display: none;
	}
}

</style>
<script>
	function openSource(){
		if("<s:property value='%{voucherHeader.vouchermis.sourcePath}' escapeHtml='false'/>"=="" || "<s:property value='%{voucherHeader.vouchermis.sourcePath}'/>"=='null')
			bootbox.alert('Source is not available');
		else{
			if("<s:property value='%{voucherHeader.vouchermis.sourcePath}' escapeHtml='false'/>".indexOf('EGF') > -1
					&& "<s:property value='%{billRegister.egBillregistermis.sourcePath}' escapeHtml='false'/>".indexOf('EGF') <= -1)
				var url = '<s:property value="%{voucherHeader.vouchermis.sourcePath}" escapeHtml="false"/>'+ '&showMode=view';
			else
				var url = '<s:property value="%{voucherHeader.vouchermis.sourcePath}" escapeHtml="false"/>';
			window.open(url,'Source','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')
				
		}   
			
	}
	function checkLength(obj)
	{
		if(obj.value.length>1024)
		{
			bootbox.alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
			obj.value = obj.value.substring(1,1024);
		}
	}
</script>
</head>

<body onload="refreshInbox()">
	<s:form action="preApprovedVoucher" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Voucher-View" />
		</jsp:include>
		<span class="mandatory1"> <s:actionerror /> <s:fielderror />
			<s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">Voucher View</div>
			<table border="0" width="100%" cellspacing="0">
				<tr>
					<td width="10%" class="greybox"><b>Voucher Number :  </b></td>
					<td width="25%" class="greybox"><s:property
							value="%{voucherHeader.voucherNumber}" /></td>
					<td width="10%" class="greybox"><b>  Date :</b></td>
					<td width="25%" class="greybox"><s:date
							name="voucherHeader.voucherDate" format="dd/MM/yyyy" /></td>
				</tr>
			</table>
			<jsp:include page="voucherViewHeader.jsp" />
			<table align="center" id="sourceIcon">
				<tr>
					<td class="bluebox"><a href="#" id="sourceLink"
						onclick=" return openSource();">Source</a></td>

				</tr>
			</table>


		</div>

		<div align="center">
			<br />
			<table border="1" width="100%" cellspacing="0">
				<tr>
					<th colspan="5"><div class="subheadsmallnew">Account
							Details</div></th>
				</tr>
				<tr>
					<th class="bluebgheadtd" width="18%">Function Name</th>
					<th class="bluebgheadtd" width="17%">Account&nbsp;Code</th>
					<th class="bluebgheadtd" width="19%">Account Head</th>
					<th class="bluebgheadtd" width="17%">Debit&nbsp;Amount(Rs)</th>
					<th class="bluebgheadtd" width="16%">Credit&nbsp;Amount(Rs)</th>
				</tr>


				<s:iterator var="p" value="%{billDetails.tempList}" status="s">
					<tr>
						<td width="18%" class="bluebox setborder" style="text-align: center"><s:property
								value="function" /></td>
						<td width="17%" class="bluebox setborder" style="text-align: center"><s:property
								value="glcode" /></td>
						<td width="19%" class="bluebox setborder"><s:property
								value="accounthead" /></td>
						<td width="17%" class="bluebox setborder"
							style="text-align: right"><s:text name="format.number">
								<s:param value="%{debitamount}" />
							</s:text></td>
						<td width="16%" class="bluebox setborder"
							style="text-align: right"><s:text name="format.number">
								<s:param value="%{creditamount}" />
							</s:text></td>
						<c:set var="db" value="${db+debitamount}" />
						<c:set var="cr" value="${cr+creditamount}" />
					</tr>
				</s:iterator>
				<tr>
					<td class="greybox setborder" style="text-align: right" colspan="3" />
					<b>Total</b>
					</td>
					<td class="greybox setborder" style="text-align: right"><fmt:formatNumber
							value="${db}" pattern="#0.00" /></td>
					<td class="greybox setborder" style="text-align: right"><fmt:formatNumber
							value="${cr}" pattern="#0.00" /></td>
				</tr>
			</table>
			<s:hidden name="methodName" id="methodName" value="%{methodName}" />
			<s:hidden name="actionName" id="actionName" value="%{actionName}" />
		</div>
		<br />
		<s:if test="%{billDetails.subLedgerlist.size()>0}">
			<div align="center">
				<br />
				<table border="1" width="100%" cellspacing="0">
					<tr>
						<th colspan="5"><div class="subheadsmallnew">Sub-ledger
								Details</div></th>
					</tr>
					<tr>
						<th class="bluebgheadtd" width="18%">Function Name</th>
						<th class="bluebgheadtd" width="18%">Account Code</th>
						<th class="bluebgheadtd" width="17%">Detailed Type</th>
						<th class="bluebgheadtd" width="19%">Detailed Key</th>
						<th class="bluebgheadtd" width="17%">Amount(Rs)</th>
					</tr>
					<s:iterator var="p" value="%{billDetails.subLedgerlist}" status="s">
						<tr>
							<td width="17%" class="bluebox setborder" style="text-align: center"><s:property
									value="functionDetail" /></td>
							<td width="17%"  class="bluebox setborder" style="text-align: center"><s:property 
									value="glcode.glcode" /></td>
							<td width="19%" class="bluebox setborder"><s:property
									value="detailType.description" /></td>
							<td width="17%" class="bluebox setborder"><s:property
									value="detailKey" /></td>
							<td width="16%" class="bluebox setborder"
								style="text-align: right"><s:text name="format.number">
									<s:param value="%{amount}" />
								</s:text></td>

						</tr>
					</s:iterator>
				</table>
			</div>
		</s:if>
		<div id="wfHistoryDiv">
			<s:if test="%{from=='Receipt'}">
				<s:if test="%{receiptVoucher.state.id!=null}">
					<jsp:include page="../workflow/workflowHistory.jsp" />
				</s:if>
			</s:if>
			<s:if test="%{from=='Contra'}">
				<s:if test="%{contraVoucher.state.id!=null}">
					<jsp:include page="../workflow/workflowHistory.jsp" />
				</s:if>
			</s:if>
			<s:if test="%{from=='Journal Voucher'}">
				<s:if test="%{voucherHeader.state.id!=null}">
					<jsp:include page="../workflow/workflowHistory.jsp" />

				</s:if>
			</s:if>
		</div>
		<div align="center" class="buttonbottom">
			<s:if test="%{from=='Receipt'}">
				<s:iterator value="%{getValidActions()}" var="p" status="s">
					<s:submit type="submit" cssClass="buttonsubmit"
						value="%{description}" id="wfBtn%{#s.index}" name="%{name}"
						method="approve"
						onclick="document.getElementById('actionName').value='%{name}';return true" />
				</s:iterator>
			</s:if>
			<s:if test="%{from=='Contra'}">
				<s:iterator value="%{getValidActions()}" var="p" status="s">
					<s:submit type="submit" cssClass="buttonsubmit"
						value="%{description}" id="wfBtn%{#s.index}" name="%{name}"
						method="approve"
						onclick="document.getElementById('actionName').value='%{name}';return true" />
				</s:iterator>
			</s:if>
			<s:if test="%{from=='Journal Voucher'}">
				<s:iterator value="%{getValidActions('')}" var="p" status="s">
					<s:submit type="submit" cssClass="buttonsubmit"
						value="%{description}" id="wfBtn%{#s.index}" name="%{name}"
						method="approve"
						onclick="document.getElementById('actionName').value='%{name}';return true" />
				</s:iterator>
			</s:if>
			<input name="button" type="button" class="buttonsubmit" id="button1"
				value="Print" onclick="window.print()" />&nbsp; <input
				type="button" id="button2" value="Close"
				onclick="javascript:window.close()" class="button" />
		</div>
		<s:hidden id="vhid" name="vhid" value="%{voucherHeader.id}" />
		<s:hidden id="id" name="id" value="%{voucherHeader.id}" />
		<s:hidden id="contraId" name="contraId" value="%{contraVoucher.id}" />



	</s:form>

</body>

</html>
