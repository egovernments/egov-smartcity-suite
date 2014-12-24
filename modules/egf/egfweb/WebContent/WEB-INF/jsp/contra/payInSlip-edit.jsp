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
<title>Cheque Deposit Modify</title>

</head>

<body onload="onbodyload();">

<s:form action="payInSlip" theme="simple" name="payinform" >
<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="PayInSlip" />
			</jsp:include>
		<div class="formmainbox"><div class="formheading"/><div class="subheadnew">Cheque Deposit Modify</div>
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

		
		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			<td class="bluebox" width="22%"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="22%">
			<table width="100%">
			<tr>
			<td style="width:25%"><input type="text" name="voucherNumberPrefix" id="voucherNumberPrefix" readonly="true"  style="width:100%"/></td> 
			<td style="width:75%"><s:textfield name="voucherNumber" id="payinNumber" /></td>
			</tr>
			</table>
			</td>
			
			</s:if>
		<s:else>
			<td class="bluebox"><s:text name="payin.number"/><span class="mandatory">*</span></td>
			<td class="bluebox"><s:textfield name="voucherNumber" id="payinNumber" readonly="true" /></td>
		</s:else>	
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
<div class="subheadsmallnew" id="savebuttondiv1"/></div>
	<div class="mandatory" align="left" id="mandatorymarkdiv">* Mandatory Fields</div>
<div class="buttonbottom" id="savebuttondiv2">
			<table border="0" width="50%" id="submitTable" align="center"><tr></tr>
			<tr>
				
				<s:submit type="submit" cssClass="buttonsubmit" value="Save & Close" id="save&close" name="save&close" method="edit" onclick="return validateCheque('saveclose')"/>
				<s:submit type="submit" cssClass="buttonsubmit" value="Save & View" id="save&view" name="save&view" method="edit" onclick="return validateCheque('saveview')"/>
				
				
				<input type="submit" value="Close" cssClass="buttonsubmit" onclick="javascript:window.close()" class="buttonsubmit"/>
				
			</tr>
		</table</div><br>
		<input type="hidden" id="selectedInstr" name="selectedInstr"/>
		<input type="hidden" id="name" name="name" value="Pay In Slip"/>
		<input type="hidden" id="type" name="type" value="Contra"/>
		<s:hidden name="contraBean.saveMode"  id="saveMode"/>
		<s:hidden id="cgn" name="cgn"></s:hidden>
	</div>
</div>
</div>
</s:push><s:token/>
</s:form>

<script >
	
function onbodyload(){
<s:iterator value="iHeaderList" status="stat">
	document.getElementById("chequeDetails").style.display="block";
	document.getElementById("billDetailTable").style.display="block";
</s:iterator>
	document.getElementById("reversenumanddate").style.display="none";
	document.getElementById("fundId").disabled=true;
	document.getElementById("vouchermis.departmentid").disabled=true;
	document.getElementById("schemeid").disabled=true;
	document.getElementById("subschemeid").disabled=true;
	document.getElementById("vouchermis.functionary").disabled=true;
	document.getElementById("fundsourceId").disabled=true;
	document.getElementById("vouchermis.divisionid").disabled=true;
	document.getElementById("voucherDate").disabled=true;
	
	document.getElementById("voucherNumId").style.display="none";
	document.getElementById("voucherDateId").style.display="none";
	
	
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
			}
	}		
			   <s:if test="%{shouldShowHeaderField('vouchernumber')}">
			   var tempVoucherNumber='<s:property value="voucherHeader.voucherNumber"/>';
			   var prefixLength='<s:property value="voucherNumberPrefixLength"/>';
			   document.getElementById('voucherNumberPrefix').value=tempVoucherNumber.substring(0,prefixLength);
			   document.getElementById('payinNumber').value=tempVoucherNumber.substring(prefixLength,tempVoucherNumber.length);
			</s:if>
	}
	
</script>

</body>

</html>