#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ page import="org.egov.utils.FinancialConstants" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>

<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>

<script>
function printEJV(){
	var id = '<s:property value="voucherHeader.id"/>';
	window.open("${pageContext.request.contextPath}/report/expenseJournalVoucherPrint!print.action?id="+id,'Print','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
function printJV(){
	var id = '<s:property value="voucherHeader.id"/>';
	window.open("${pageContext.request.contextPath}/voucher/journalVoucherPrint!print.action?id="+id,'Print','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}
function openSource(){
	if("<s:property value='%{voucherHeader.vouchermis.sourcePath}' escape='false'/>"=="" || "<s:property value='%{voucherHeader.vouchermis.sourcePath}'/>"=='null')
		alert('Source is not available');
	else{
		var url = '<s:property value="%{voucherHeader.vouchermis.sourcePath}" escape="false"/>'+ '&showMode=view' 
		window.open(url,'Source','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')

	}   
	//window.open(url,'Source','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	
}
function validate(name,value){
	document.getElementById("actionName").value= name;
	document.getElementById('lblError').innerHTML ="";
<s:if test="%{wfitemstate !='END'}">
	 if( (value == 'Approve' || value=='Send for Approval'|| value == 'Forward' ) && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		document.getElementById('lblError').innerHTML ="Please Select the user";
		return false;
	}
</s:if>
	return true;
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<s:if test="%{type=='default'}">
<title>General JV-Approval</title>
</s:if>
<s:else>
<title><s:property value="type" /> JV-Approval</title>
</s:else>
</head>

<body onload="refreshInbox()">
<s:form action="preApprovedVoucher" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="PJV-Approval" />
			</jsp:include>
			<s:token/>
<font  style='color: red ;'> 
<p class="error-block" id="lblError" style="font:bold" ></p>
</font>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
		<s:if test="%{type=='default'}">	
		<div class="formmainbox"><div class="subheadnew">General JV Approval</div>
		</s:if>
		<s:else>
		<div class="formmainbox"><div class="subheadnew"><s:property value="type" /> JV Approval</div>
		</s:else>
		<div id="listid" style="display:block">
		<br/>
	<div align="center">
	<table border="0" width="100%" cellspacing="0">
		<tr>
			<s:if test="%{type=='default'}">
			<td width="25%" class="greybox">General JV Number</td>
			</s:if>
			<s:else>
			<td width="25%" class="greybox"><s:property value="type" /> JV Number</td>
			</s:else>
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
	
		<table> <tr class="bluebox"> <a href="#" onclick=" return openSource();">Source</a> </tr></table>
	
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
	<div  class="buttonbottom" id="buttondiv">
		<s:iterator value="%{getValidActions('')}" var="p">
		  <s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="%{name}" name="%{name}" method="update" onclick="return validate('%{name}','%{description}')"/>
		</s:iterator>
		<s:if test="%{type == finConstExpendTypeContingency}">
			<input type="button" class="button" id="print" value="Print Preview" action="expenseJournalVoucherPrint" method="print" onclick="printEJV()"/>
		</s:if> 
		<s:else>
			<input type="button" class="button" id="print" value="Print Preview" action="journalVoucherPrint" method="print" onclick="printJV()"/>
		</s:else>
		
		<input type="button" value="Close" onclick="javascript:window.close()" class="button"/>
	</div>
	
</div>
</div>
</s:form>
</body>

</html>
