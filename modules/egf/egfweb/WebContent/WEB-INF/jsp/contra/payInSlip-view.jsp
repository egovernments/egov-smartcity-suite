<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>

<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
	
		<script type="text/javascript" src="/EGF/javascript/calender.js"></script>
		<script type="text/javascript" src="/EGF/script/calendar.js" ></script>
		<script type="text/javascript" src="/EGF/javascript/dateValidation.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Pay In</title>

</head>

<body onload="onbodyload();">

<s:form action="payInSlip" theme="simple" name="payinform" >
<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="PayInSlip" />
			</jsp:include>
		<div class="formmainbox"><div class="formheading"/><div class="subheadnew">Pay in</div>
		<div id="listid" style="display:block">
		<br/>
<div align="center">
<font  style='color: red ;'> 
<p class="error-block" id="lblError" ></p>
</font>
<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<table border="0" width="100%">
		<tr>

		
			<td class="bluebox"><s:text name="payin.number"/><span class="mandatory">*</span></td>
			<td class="bluebox"><s:textfield name="voucherNumber" id="payinNumber" /></td>
			<td class="bluebox"><s:text name="payin.date"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="34%"><input type=text name="voucherDate" id="voucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')" value='<s:date name="voucherDate" format="dd/MM/yyyy"/>'/>
			<a href="javascript:show_calendar('payinform.voucherDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A>(dd/mm/yyyy)</td>
		</tr>
	<%@include file="payInSlip-form.jsp"%>
	</table>

		<div align="center">

     <div id="labelAD" align="center">
	 		<table width="80%" border=0 id="chequeDetails"><th>Cheque Detail</th></table>
	</div>
	<div class="yui-skin-sam" align="center">
       <div id="billDetailTable"></div>
     </div>
     <script>
		
		makeVoucherDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="80%"
	 </script>
	
		<table align="center">
			<tr ><td width="455"></td>
				<td >Total Amount</td>
				  <td ><s:textfield name="totalAmount" id="totalAmount" /></td>
				</td>
				</tr>
		</table><br/>
			<table border="0" width="100%" id="buttonTable"><tr></tr>
			<tr align="center">
				<input type="submit" value="Close"  onclick="javascript:window.close()" class="buttonsubmit"/>
				
			</tr>
		</table>
		<input type="hidden" id="selectedInstr" name="selectedInstr"/>
		<input type="hidden" id="name" name="name" value="Pay In Slip"/>
		<input type="hidden" id="type" name="type" value="Contra"/>
	</div>
</div>
</div>
</s:push>
</s:form>

<script >
	
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