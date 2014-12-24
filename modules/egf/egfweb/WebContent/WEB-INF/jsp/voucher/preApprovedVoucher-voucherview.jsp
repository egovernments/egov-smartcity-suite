<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ page import="org.egov.utils.FinancialConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>

<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>

<script>
function printVoucher()
{
	<s:if test='%{type == finConstExpendTypeContingency}'>
		document.forms[0].action="../report/expenseJournalVoucherPrint!print.action";
	</s:if>
	<s:else>
		document.forms[0].action="journalVoucherPrint!print.action";		
	</s:else>
	document.forms[0].submit();
}
function openSource(){
	var url = '<s:property value='%{getSourcePath()}' />'
	window.open(url,'Source','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	
}
function validate(name,value){
	document.getElementById("actionName").value= name;
	if(name == 'notifycancel'){
		document.getElementById("actionName").value="aa_reject";
		if(!(document.getElementById('notifyOnCancel').checked)){
			alert("Enter Notification Details, Click Notify");
			return false;
		}
		if((document.getElementById('notifyOnCancel').checked) && ("" == document.getElementById("fileSummary").value)){
			alert("Notification comments is mandatory");
			return false;
		}
	}
	document.getElementById('lblError').innerHTML ="";
<s:if test="%{wfitemstate !='END'}">
	 if( (value == 'Approve' || value=='Send for Approval'|| value == 'Forward' ) && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		document.getElementById('lblError').innerHTML ="Please Select the user";
		return false;
	}
</s:if>
	return true;
}
function displayNotification(obj){
	if(obj.checked)
		document.getElementById('vouchercancelnotification').style.display='inline';
	else
		document.getElementById('vouchercancelnotification').style.display='none';
}
function loadNotificationDetails(){
	document.getElementById('vouchercancelnotification').style.display='none';
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title><s:property value="type" /> JV-Approval</title>
</head>

<body onload="refreshInbox();loadNotificationDetails()">
<s:form action="preApprovedVoucher" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="PJV-Approval" />
			</jsp:include>
<font  style='color: red ;'> 
<p class="error-block" id="lblError" style="font:bold" ></p>
</font>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
		<div class="formmainbox"><div class="subheadnew"><s:property value="type" /> JV Approval</div>
		<div id="listid" style="display:block">
		<br/>
	<div align="center">
	<table border="0" width="100%" cellspacing="0">
		<tr>
			<td width="25%" class="greybox"><s:property value="type" /> JV Number</td>
			<td width="25%" class="greybox"><s:property value="%{voucherHeader.voucherNumber}"/></td>
			<td width="25%" class="greybox">Date</td>
			<td width="25%" class="greybox"><s:date name="voucherHeader.voucherDate" format="dd/MM/yyyy"/></td>
		</tr>
	</table>
	<jsp:include page="voucherViewHeader.jsp"/>
	<div align="center">
		<table border="0" width="100%" cellspacing="0">
			<tr>
				<td width="25%" class="<c:out value='${tdclass}' />">Bill Number</td>
				<td width="25%" class="<c:out value='${tdclass}' />"><s:property value="%{getMasterName('billnumber')}"/></td>	
				<td width="25%"></td><td width="25%"></td>
			</tr>
	</table>
			<s:hidden id="vhid" name="vhid" value="%{voucherHeader.id}"/>
			<s:hidden id="id" name="id" value="%{voucherHeader.id}"/>
	<s:if test="%{type == finConstExpendTypeContingency}">
		<table> <tr class="bluebox"> <a href="#" onclick="openSource()">Source</a> </tr></table>
	</s:if>
	<br/>
	<div align="center">
		<table border="1" width="100%">
			<tr><td colspan="5"><strong>Account Details</strong></td></tr>
			<tr>
				<th class="bluebgheadtd" width="18%">Function Name</th>
				<th class="bluebgheadtd" width="17%">Account&nbsp;Code</th>
				<th class="bluebgheadtd" width="19%">Account Head</th>
				<th class="bluebgheadtd" width="17%"><s:text name="billVoucher.approve.dbtamt"/></th>
				<th class="bluebgheadtd" width="16%"><s:text name="billVoucher.approve.crdamt"/></th>
			</tr>
			<s:iterator var="p" value="%{billDetails.tempList}" status="s"> 
					<tr>
						<td width="18%"  class="bluebox"><s:property value="function"/></td>
						<td width="17%"  class="bluebox"><s:property value="glcode"/></td>
						<td width="19%"  class="bluebox"><s:property value="accounthead"/></td>
						<td width="17%"  class="bluebox" style="text-align:right"><s:text name="format.number" ><s:param value="%{debitamount}"/></s:text></td>
						<td width="16%"  class="bluebox" style="text-align:right"><s:text name="format.number" ><s:param value="%{creditamount}"/></s:text></td>
						<c:set var="db" value="${db+debitamount}"/>
						<c:set var="cr" value="${cr+creditamount}"/>
					</tr>
			</s:iterator>
			<tr>
				<td class="greybox" style="text-align:right" colspan="3"/>Total</td>
				<td class="greybox" style="text-align:right"><fmt:formatNumber value="${db}" pattern="#0.00" /></td>
				<td class="greybox" style="text-align:right"><fmt:formatNumber value="${cr}" pattern="#0.00" /></td>
			</tr>
		</table>
		<s:hidden  name="actionName" id="actionName"/>
	</div>
	<br/>
	<div align="center">
		<table border="1" width="100%">
			<tr><td colspan="4"><strong>Sub-Ledger Details</strong></td></tr>
			<tr>
				<th class="bluebgheadtd" width="18%">Account Code</th>
				<th class="bluebgheadtd" width="17%">Detail Type</th>
				<th class="bluebgheadtd" width="19%">Detail Key</th>
				<th class="bluebgheadtd" width="17%">Amount</th>
			</tr>
			<s:iterator var="p" value="%{getMasterName().tempList}" status="s"> 
				<tr>
					<td width="18%"  class="bluebox"><s:property value="glcode"/></td>
					<td width="18%"  class="bluebox"><s:property value="detailtype"/></td>
					<td width="18%"  class="bluebox"><s:property value="detailkey"/></td>
					<td width="18%"  class="bluebox" style="text-align:right"><s:text name="format.number" ><s:param value="%{amount}"/></s:text></td>
				</tr>
			</s:iterator>
		</table>
	</div>
	<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
		<%@include file="workflowApproval.jsp"%>
	</s:if>
	<div align="center">
		<table border="0" width="100%">
			<tr>
				<td  class="bluebox">Comments</td> 
				<td  class="bluebox"><s:textarea name="comments" id="comments" cols="150" rows="3" onblur="checkLength(this)"/></td>
			</tr>
			<br/>
		</table>
	</div>
	
	
	<div id="wfHistoryDiv">
	  	<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
	        <c:param name="stateId" value="${voucherHeader.state.id}"></c:param>
        </c:import>
  	</div>
  	<div align="right">
  	<table border="0" width="100%" cellspacing="0" id="notifyOnCancelTab">
		<tr>
	    	<td class="bluebox">&nbsp;</td>
	    	<td class="bluebox">&nbsp;</td>
	    	<td class="bluebox" width="10%"><span id="notifyOnCancelLabel"><strong>Notify</strong></span></td>
	    	<td class="bluebox">
	    		<s:checkbox name="notifyOnCancel" id="notifyOnCancel" onclick="displayNotification(this)"/>
	    	</td>
		</tr>
	</table>
	</div>
	<div  class="buttonbottom" id="buttondiv">
		<s:iterator value="%{getValidActions('')}" var="p">
		  <s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="%{name}" name="%{name}" method="update" onclick="return validate('%{name}','%{description}')"/>
		  <script>
		  	document.getElementById('notifyOnCancelTab').style.display='none';
		  	<s:if test="%{name=='notifycancel'}">
				document.getElementById('notifyOnCancelTab').style.display='block';
			</s:if>
		  </script>
		</s:iterator>
		<s:submit cssClass="button" id="print" value="Print Preview"  onclick="printVoucher()"/>
		<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
	</div>
	<div id="vouchercancelnotification" align="center">
		<jsp:include page="VoucherCancelNotification.jsp"/>
	</div>
</div>
</div>
</s:form>
</body>

</html>