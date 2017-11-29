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
<title>Dishonor Cheque Workflow</title>
</head>
<body onload="refreshInbox()">
	<s:form action="dishonorChequeWorkflow"
		form="dishonorChequeWorkflowMsgForm" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="dishonor workflow page" />
		</jsp:include>
		<span class="mandatory"> <s:actionmessage />
		</span>
		<br />
		<s:hidden name="mode" id="mode" value="%{mode}"></s:hidden>
		<s:if test="%{mode == 'print'}">
			<input type="button" class="button" id="print" value="Print Preview"
				action="journalVoucherPrint" onclick="dishonorChequePrint()" />
		</s:if>
		<input type="button" value="Close" onclick="javascript:window.close()"
			class="button" />
	</s:form>
	<script>
function dishonorChequePrint(){
	                           
		var reversalVhId= '<s:property value="%{paymentVoucher.id}"/>';
 		var bankChargesVhId='<s:property value="%{bankChargesReversalVoucher.id}"/>';
 		
 		var reversalAmount='<s:property value="%{dishonorChequeView.instrumentHeader.instrumentAmount}"/>';
 		var bankChargesAmount='<s:property value="%{dishonorChequeView.bankChargesAmt}"/>';
	 	window.open("../brs/DishonoredChequeEntries.do?submitType=beforePrintDishonoredCheque&reversalVhId="+reversalVhId+
	 		 	"&bankChargesVhId="+bankChargesVhId+"&reversalAmount="+reversalAmount+"&bankChargesAmount="+bankChargesAmount
	 		 	,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	     
}               
   
function printEJV(){
	var id = '<s:property value="voucherHeader.id"/>';
	window.open("${pageContext.request.contextPath}/report/expenseJournalVoucherPrint!print.action?id="+id,'Print','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
function printJV(){
	var id = '<s:property value="voucherHeader.id"/>';
	window.open("${pageContext.request.contextPath}/voucher/journalVoucherPrint!print.action?id="+id,'Print','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
</script>
</body>
</html>
