<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>

<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
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

</style>
</head>

<body onload="refreshInbox()">
<s:form action="preApprovedVoucher" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Voucher-View" />
			</jsp:include>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
		<div class="formmainbox"><div class="subheadnew">Voucher View</div>
		<table border="0" width="100%" cellspacing="0">
		<tr>
			<td width="25%" class="greybox">Voucher Number:</td>
			<td width="25%" class="greybox"><s:property value="%{voucherHeader.voucherNumber}"/></td>
			<td width="25%" class="greybox">Date:</td>
			<td width="25%" class="greybox"><s:date name="voucherHeader.voucherDate" format="dd/MM/yyyy"/></td>
		</tr>
		</table>
		<jsp:include page="voucherViewHeader.jsp"/>
		<table align="center">
		<tr>
		<td class="bluebox"><a href="#" onclick="openSource()" id="sourceLink"/>Source</a></td>
			
		</tr>
		</table>
	<div align="center">
		<table border="1" width="100%" cellspacing="0">
			<tr>
                <th colspan="5"><div class="subheadsmallnew">Account Details</div></th>
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
		<s:hidden  name="methodName" id="methodName" value="%{methodName}"/>
		<s:hidden  name="actionName" id="actionName" value="%{actionName}"/>
	</div>
	<br/>
	<s:if test="%{billDetails.subLedgerlist.size()>0}">
	<div align="center">
		<table border="1" width="100%" cellspacing="0">
			<tr>
                <th colspan="5"><div class="subheadsmallnew">Sub-ledger Details</div></th>
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
					<td width="17%"  class="bluebox"><s:property value="functionDetail"/></td>
					<td width="17%"  class="bluebox"><s:property value="glcode.glcode"/></td>
					<td width="19%"  class="bluebox"><s:property value="detailType.description"/></td>
					<td width="17%"  class="bluebox"><s:property value="detailKey"/></td>
					<td width="16%"  class="bluebox" style="text-align:right"><s:text name="format.number" ><s:param value="%{amount}"/></s:text></td>
					
				</tr>
		</s:iterator>
		</table>
	</div>
	</s:if>
	<div id="commentsdiv">
		<td class="bluebox" id="commentslabel" ><strong>Comments</strong></td>
		<td class="bluebox" colspan="4"><s:textarea name="comments" id="comments" cols="100" rows="3" onblur="checkLength(this)"/></td>
	</div>
	<div id="wfHistoryDiv">
		<s:if test="%{from=='Receipt'}">
			<s:if test="%{receiptVoucher.state.id!=null}">
			  	<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
			        <c:param name="stateId" value="${receiptVoucher.state.id}"></c:param>
			    </c:import>
			 </s:if>
		    </s:if>
	    <s:if test="%{from=='Contra'}">
	    	<s:if test="%{contraVoucher.state.id!=null}">
			  	<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
			        <c:param name="stateId" value="${contraVoucher.state.id}"></c:param>
		        </c:import>
			</s:if>  		
  		</s:if>
	    <s:if test="%{from=='Journal Voucher'}">
		    <s:if test="%{voucherHeader.state.id!=null}">
			  	<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
			        <c:param name="stateId" value="${voucherHeader.state.id}"></c:param>
		        </c:import>
	  		</s:if>
  		</s:if>
	</div>
 	<div  class="buttonbottom">
		<s:if test="%{from=='Receipt'}">
			<s:iterator value="%{getValidActions()}" var="p"  status="s">
			<s:if test='%{(description != "Notify & Cancel")}'>
			  <s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="wfBtn%{#s.index}" name="%{name}" method="approval" onclick="document.getElementById('actionName').value='%{name}';return true"/>
			</s:if>
			</s:iterator>
		</s:if>
		<s:if test="%{from=='Contra'}">
			<s:if test='%{!wfitemstate.equalsIgnoreCase("END")}'>
				<%@include file="workflowApproval.jsp"%>
			</s:if>
<s:hidden  id="scriptName" value="ContraJournalVoucher.nextDesg"/>
			<s:iterator value="%{getValidActions()}" var="p"  status="s">
			<s:if test='%{(description != "Notify & Cancel")}'>
			  <s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="wfBtn%{#s.index}" name="%{name}" method="approval" 		onclick="document.getElementById('actionName').value='%{name}';return true"/>
			</s:if>
			</s:iterator>

		</s:if>
		<s:if test="%{from=='Journal Voucher'}">
			<s:iterator value="%{getValidActions('')}" var="p" status="s">
			<s:if test='%{(description != "Notify & Cancel")}'>
			  <s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="wfBtn%{#s.index}" name="%{name}" method="approval" onclick="document.getElementById('actionName').value='%{name}';return true"/>
			</s:if>
			</s:iterator>
		</s:if>
		<input name="button" type="button" class="buttonsubmit" id="button1" value="Print" onclick="window.print()"/>&nbsp;
		<input type="submit" id="button2" value="Close" onclick="javascript:window.close()" class="button"/>
	</div>
<s:hidden id="vhid" name="vhid" value="%{voucherHeader.id}"/>
<s:hidden id="id" name="id" value="%{voucherHeader.id}"/><s:hidden id="contraId" name="contraId" value="%{contraVoucher.id}"/>
</div>
</div>
<script>
	function openSource()
	{
		if("<s:property value='%{voucherHeader.vouchermis.sourcePath}' escape='false'/>"=="" || "<s:property value='%{voucherHeader.vouchermis.sourcePath}'/>"=='null')
			alert('Source is not available');
		else{
			var url = '<s:property value="%{voucherHeader.vouchermis.sourcePath}" escape="false"/>' + '&showMode=view'
			window.open(url,'Source','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')

		}

	}
	function checkLength(obj)
	{
		if(obj.value.length>1024)
		{
			alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
			obj.value = obj.value.substring(1,1024);
		}
	}
	if(document.getElementById('methodName').value!='' || '<%=request.getParameter("from")%>'=='null')
	{
		if(document.getElementById('wfBtn0'))
			document.getElementById('wfBtn0').style.display='none';
		if(document.getElementById('wfBtn1'))
			document.getElementById('wfBtn1').style.display='none';
		document.getElementById('commentsdiv').style.display='none';
		document.getElementById('wfHistoryDiv').style.display='none';
	}
</script>
</s:form>

</body>

</html>
