<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title><s:property value="type" /> JV-Create</title>
</head>
<script>
	function checkBillIdBillview(){
		if(document.getElementById('id').value!=''){
			document.getElementById('aa_approve').disabled=true;
		}else{
			document.getElementById('aa_approve').disabled=false;
		}
		if('<s:property value="voucherHeader.id"/>' ==''){
			document.getElementById('print').disabled=true;
		}else{
			document.getElementById('print').disabled=false;
		}
	}
	
	function checkLength(obj){
		if(obj.value.length>1024)
		{
			alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
			obj.value = obj.value.substring(1,1024);
		}
	}
	
	function printEJV(){
		var id = '<s:property value="voucherHeader.id"/>';
		window.open("${pageContext.request.contextPath}/report/expenseJournalVoucherPrint!print.action?id="+id,'Print','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
	function printJV(){
		var id = '<s:property value="voucherHeader.id"/>';
		window.open("${pageContext.request.contextPath}/voucher/journalVoucherPrint!print.action?id="+id,'Print','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
function openSource(){
	var url = '<s:property value='%{getSourcePath()}' />'
	window.open(url,'Source','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')
}
function validateApproverUser(name,value){
	
	document.getElementById("actionName").value= name;
	<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
		if(!validateUser(name,value)){
		return false;
		}
	</s:if>
}
</script>
<body onload="checkBillIdBillview()">
<s:form action="preApprovedVoucher" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Bill Voucher -Create" />
			</jsp:include>
<font  style='color: red ;'> 
<p class="error-block" id="lblError" style="font:bold" ></p>
</font>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
		<div class="formmainbox"><div class="subheadnew">Generate <s:property value="type" /> Bill Voucher</div>
		<div id="listid" style="display:block">
		<br/>
	<s:token/>
	<jsp:include page="voucherViewHeader.jsp"/>

			<s:hidden id="billid" name="billid" value="%{egBillregister.id}"/> 
			<s:hidden id="vhid" name="vhid" value="%{voucherHeader.id}"/>
			<s:hidden id="id" name="id" value="%{voucherHeader.id}"/>

		<table> <tr class="bluebox"> <a href="#" onclick="openSource()">Source</a> </tr></table>

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
	<div align="center">
	<table border="1" width="100%">
		<tr><td colspan="5"><strong>Bill Payee Details</strong></td></tr>
		<tr>
			<th class="bluebgheadtd" width="18%">Account Code</th>
			<th class="bluebgheadtd" width="17%">Detail Type</th>
			<th class="bluebgheadtd" width="19%">Detail Key</th>
			<th class="bluebgheadtd" width="17%"><s:text name="billVoucher.approve.dbtamt"/></th>
			<th class="bluebgheadtd" width="16%"><s:text name="billVoucher.approve.crdamt"/></th>
		</tr>
		<s:iterator var="p" value="%{billDetails.payeeList}" status="s"> 
				<tr>
					<td width="17%"  class="bluebox"><s:property value="glcode"/></td>
					<td width="19%"  class="bluebox"><s:property value="detailtype"/></td>
					<td width="17%"  class="bluebox"><s:property value="detailkey"/></td>
					<td width="16%"  class="bluebox" style="text-align:right"><s:text name="format.number" ><s:param value="%{debitamount}"/></s:text></td>
					<td width="16%"  class="bluebox" style="text-align:right"><s:text name="format.number" ><s:param value="%{creditamount}"/></s:text></td>
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
				<td  class="bluebox" ><s:textarea name="comments" id="comments" cols="150" rows="3" onblur="checkLength(this)"/></td>
				<td><s:hidden id="methodName" name="methodName" value="save"/></td>
			</tr>
			<br/>
		</table>
	</div>
	<div  class="buttonbottom" id="buttondiv">
		<s:iterator value="%{getValidActions('')}" var="p">
			<s:if test="%{(description !='Cancel') && (description !='Notify & Cancel')}">
					<s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="%{name}" name="%{name}" method="save" onclick="return validateApproverUser('%{name}','%{description}')"/>
			</s:if>
		</s:iterator>
		<s:if test="%{egBillregister.expendituretype == finConstExpendTypeContingency}">
			<input type="button" class="button" id="print" value="Print Preview" action="expenseJournalVoucherPrint" method="print" onclick="printEJV()"/>
		</s:if> 
		<s:else>
			<input type="button" class="button" id="print" value="Print Preview" action="journalVoucherPrint" method="print" onclick="printJV()"/>
		</s:else>
		<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
	</div>
	
</div>
</div>
<s:if test="%{hasErrors()}">
<script>
document.getElementById('id').value='';
	</script>
</s:if>

</s:form>
</body>

</html>