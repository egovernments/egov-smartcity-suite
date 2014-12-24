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
<title>Contra - Cash Deposit</title>

</head>

	
<body onload="onloadtask();">
<s:form action="contraCTB" theme="simple" name="cashDepositForm" >
<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Cash Deposit" />
			</jsp:include>
			
			<span class="mandatory">
			<font  style='color: red ; font-weight:bold '> 
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage /></font>
			</span>
<div class="formmainbox">
<div class="formheading"/><div class="subheadnew">Cash Deposit</div>
<div id="listid" style="display:block"><br/>
<div align="center">
<font  style='color: red ; font-weight:bold '> 
<p class="error-block" id="lblError" ></p></font>

	<%@include file="contraCTB-form.jsp"%>
     <table border="0" width="100%">
	    <tr>
		<td class="bluebox"><s:text name="contra.cashInHand"/>
		<s:textfield name="contraBean.cashInHand" id="cashInHand" readonly="true" /></td>
	  </tr>	
       </table>
   
	  <table border="0" width="100%">
	<tr>
		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			<td class="greybox"><s:text name="reversalVoucherNumber"/><span class="mandatory">*</span></td>
			<td class="greybox"><s:textfield name="reversalVoucherNumber" id="reversalVoucherNumber" /></td>
		</s:if>
			<td class="greybox"><s:text name="reversalVoucherDate"/><span class="mandatory">*</span></td>
			<td class="greybox"><s:textfield name="reverseVoucherDate"  id="reversalVoucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('cashDepositForm.reversalVoucherDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A>(dd/mm/yyyy)</td>
		</tr>	
</table>
	<div align="center">
		
		<table border="0" width="80%" align="center"  class="buttonbottom"><tr></tr>
			<tr>
				<td >
				<s:submit type="submit" cssClass="buttonsubmit" value="Reverse & View"  method="reverseAndView" onclick="return validateReverseInput()"/>
				<s:submit type="submit" cssClass="buttonsubmit" value="Reverse & Close" method="reverseAndClose" onclick="return validateReverseInput()"/>
				<s:reset name="button" type="submit" cssClass="buttonsubmit" id="button" value="Cancel"/>
				<input type="submit" value="Close" onclick="javascript:window.close()" cssClass="buttonsubmit" class="button"/>
				</td>
			</tr>
		</table>
		
	</div>
</div>
</div>
</div>
</div>
<input type="hidden" id="voucherTypeBean.voucherName" name="voucherTypeBean.voucherName" value="CashToBank"/>
<input type="hidden" id="voucherTypeBean.voucherType" name="voucherTypeBean.voucherType" value="Contra"/>
<input type="hidden" id="voucherTypeBean.voucherNumType" name="voucherTypeBean.voucherNumType" value="Contra"/>
<input type="hidden" id="voucherTypeBean.cgnType" name="voucherTypeBean.cgnType" value="CTB"/>
<input type="hidden" id="voucherHeader.id" name="voucherHeader.id" value='<s:property value="voucherHeader.id"/>'/>
<s:hidden name="contraBean.saveMode"  id="saveMode"/>
<s:hidden name="contraBean.result"  id="result"/>
<s:hidden name="contraBean.mode"  id="mode"/>
<s:hidden id="cgn" name="cgn"></s:hidden>
<s:hidden id="vouchermis.sourcePath" name="vouchermis.sourcePath" value="../contra/contraCTB!loadCTBVoucher.action?vhid="></s:hidden>
</s:push>
</s:form>
<script>

function populateAccNum(branch){

	var bankbranchId = branch.options[branch.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0,index);
	var brId=bankbranchId.substring(index+1,bankbranchId.length);
	populateaccountNumber({bankId:bankId,branchId:brId})
}
function populateNarration(accnumObj){
	var accnum =  accnumObj.options[accnumObj.selectedIndex].value;
	var url = '../voucher/common!loadAccNumNarrationAndFund.action?accnum='+accnum;
	YAHOO.util.Connect.asyncRequest('POST', url, postType, null);

}
var postType = {
success: function(o) {
		var narrationfund= o.responseText;
		var index=narrationfund.indexOf("-");
		document.getElementById('accnumnar').value=narrationfund.substring(o,index);	
		var fundid = narrationfund.substring(index+1,narrationfund.length);	
		document.getElementById('fundId').value = fundid;
		document.getElementById('fundId').disabled =true;
		populateschemeid({fundId:fundid})
    },
    failure: function(o) {
    	alert('failure');
    }
}

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
function onloadtask(){
	document.getElementById('fundId').disabled =true;
	var currentTime = new Date()
	var month = currentTime.getMonth() + 1
	var day = currentTime.getDate()
	var year = currentTime.getFullYear()
	document.getElementById('voucherDate').value = day + "/" + month + "/" + year ;
	for(var i=0;i<document.forms[0].length;i++)
		{
			if(document.getElementById('mode').value == "view")
			{
					document.forms[0].elements[i].disabled =true;
				}					
			}	
}
function validateReverseInput(){
	document.getElementById('lblError').innerHTML = "";
	
	if(document.getElementById('reversalVoucherNumber') && document.getElementById('reversalVoucherNumber').value.trim().length == 0){
		document.getElementById('lblError').innerHTML = "Please enter Reverse Voucher number";
		return false;
	}
	if(document.getElementById('reversalVoucherDate').value.trim().length == 0){
		document.getElementById('lblError').innerHTML = "Please enter  voucher date ";
		return false;
	}
}
</script>

</body>

</html>