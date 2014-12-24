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
<title>Cheque Deposit</title>

</head>

<body onload="onbodyload();">

<s:form action="payInSlip" theme="simple" name="payinform" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="PayInSlip" />
			</jsp:include>
		<div class="formmainbox"><div class="formheading"/><div class="subheadnew">Cheque Deposit</div>
		<div id="listid" style="display:block">
		<br/>
<div align="center">
<font  style='color: red ;'> 
<p class="error-block" id="lblError" style="font:bold" ></p>
</font>
<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
		</span>
		</span>
	<table border="0" width="100%">
		<tr>

		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			<td class="bluebox"><s:text name="payin.number"/><span class="mandatory">*</span></td>
			<td class="bluebox"><s:textfield name="voucherNumber" id="payinNumber" /></td></s:if>
			<td class="bluebox"><s:text name="payin.date"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="34%"><input type=text name="voucherDate" id="voucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')" value='<s:date name="voucherDate" format="dd/MM/yyyy"/>'/>
			<a href="javascript:show_calendar('payinform.voucherDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A>(dd/mm/yyyy)</td>
		</tr>
	<%@include file="payInSlip-form.jsp"%>
	</table>

			

		<div align="center">
		<div class="buttonbottom" id="searchbuttondiv">
		<table border="0" width="100%" align="center"><tr></tr>
			<tr align="center">
				
			<td><s:submit type="submit" cssClass="buttonsubmit" value="Search" id="search" name="search" onclick="return validatePayinSearch()" method="search" />
<s:submit type="submit" cssClass="buttonsubmit" value="Cancel" id="cancel" name="cancel" onclick="return cancelFunc()"  />
				
			</td></tr>
		</table>
	</div>

     <div id="labelAD" align="center">
	 		<table width="100%" border=0 id="chequeDetails"><th>Cheque Detail</th></table>
	</div>
	<div class="yui-skin-sam" align="center">
       <div id="billDetailTable"></div>
     </div>
     <script>
		
		makeVoucherDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="80%"
	 </script>
	
		<table align="center" id="totalAmtTable">
			<tr ><td width="455"></td>
				<td >Total Amount</td>
				  <td ><s:textfield name="totalAmount" id="totalAmount" /></td>
				</td>
				</tr>
		</table><br/>
<div class="subheadsmallnew" id="savebuttondiv1"/></div>
	<div class="mandatory" align="left" id="mandatorymarkdiv">* Mandatory Fields</div>
<div class="buttonbottom" id="savebuttondiv2">
			<table border="0" width="100%" id="submitTable"><tr></tr>
			<tr>
				<td/><td>
				<s:submit type="submit" cssClass="buttonsubmit" value="Save & Close" id="save&close" name="save&close" method="create" onclick="return validateCheque('saveclose')"/>
				<s:submit type="submit" cssClass="buttonsubmit" value="Save & New" id="save&new" name="save&new" method="create" onclick="return validateCheque('savenew')"/>
				<s:submit type="submit" cssClass="buttonsubmit" value="Save & View" id="save&view" name="save&view" method="create" onclick="return validateCheque('saveview')"/>
				
				
				<input type="submit" value="Close" onclick="javascript:window.close()" class="buttonsubmit"/>
				
			</tr>
		</table>
</div>
<br><br>
		<s:hidden type="hidden" id="selectedInstr" name="selectedInstr"/>
		<s:hidden type="hidden" id="name" name="name" value="Pay In Slip"/>
		<s:hidden type="hidden" id="type" name="type" value="Contra"/>
		<s:hidden name="contraBean.saveMode"  id="saveMode"/>
	</div>
</div>
</div>

</s:form>
<script >
	function validatePayinSearch(){
	document.getElementById('lblError').innerHTML ="";
	var voucherDateFrom = document.getElementById('voucherDateFrom').value;
	var voucherDateTo = document.getElementById('voucherDateTo').value;
	if(voucherDateFrom.length!=0 && voucherDateTo.length!=0)
		if( compareDate(formatDate6(voucherDateFrom),formatDate6(voucherDateTo)) == -1 )
			{
				document.getElementById('lblError').innerHTML = "Voucher from date can not be greater than voucher to date";
				document.getElementById('voucherDateFrom').focus();
				return false;
			}
			if(!validateMIS()){
				return false;
			}
	return true;
}
function onbodyload(){
	document.getElementById("submitTable").style.display="none";
	document.getElementById("chequeDetails").style.display="none";
	document.getElementById("billDetailTable").style.display="none";
	document.getElementById("totalAmtTable").style.display="none";
	document.getElementById("reversenumanddate").style.display="none";
	document.getElementById("reversenumanddate").style.display="none"
	document.getElementById("savebuttondiv1").style.display="none"
	document.getElementById("mandatorymarkdiv").style.display="none"  
	document.getElementById("savebuttondiv2").style.display="none"
<s:iterator value="iHeaderList" status="stat">
	document.getElementById("submitTable").style.display="block";
	document.getElementById("chequeDetails").style.display="block";
	document.getElementById("billDetailTable").style.display="block";
	document.getElementById("totalAmtTable").style.display="block";
	document.getElementById("savebuttondiv1").style.display="block"
	document.getElementById("mandatorymarkdiv").style.display="block"
	document.getElementById("savebuttondiv2").style.display="block"
	disableHeader(true);
</s:iterator>
	var saveMode='<s:property value="contraBean.saveMode"/>';
	var result='<s:property value="contraBean.result"/>';
	if(result == 'success'){
	var voucherNumber = '<s:property value='%{voucherHeader.voucherNumber}'/>' ;
		if(saveMode == 'saveclose'){
			alert("Payinslip voucher created sucessfully with voucher number =  "+voucherNumber);
				window.close();
		} else if(saveMode == 'saveview'){
				alert("Payinslip voucher created sucessfully with voucher number =  "+voucherNumber );
				window.open('../voucher/preApprovedVoucher!loadvoucherview.action?vhid=<s:property value='%{voucherHeader.id}'/>','Search','resizable=yes,scrollbars=yes,left=300,top=40,width=900, height=700');
			}else if(saveMode=='savenew'){
				alert("Payinslip voucher created sucessfully with voucher number =  "+voucherNumber );
				document.forms[0].action = "${pageContext.request.contextPath}/contra/payInSlip!newform.action";
				document.forms[0].submit();
			}
		
	}
}
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
function cancelFunc(){

for(var i=0;i<document.forms[0].length;i++)
		{
			
			document.forms[0].elements[i].disabled=false;				
			}	
	document.getElementById("submitTable").style.display="none";
	document.getElementById("chequeDetails").style.display="none";
	document.getElementById("billDetailTable").style.display="none";
	document.getElementById("totalAmtTable").style.display="none";
	document.getElementById("savebuttondiv1").style.display="none"
	document.getElementById("mandatorymarkdiv").style.display="none"  
	document.getElementById("savebuttondiv2").style.display="none"
	return false;
}

</script>

</body>

</html>