<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="EGF" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<link href="/EGF/cssnew/budget.css" rel="stylesheet" type="text/css" />
<link href="/EGF/cssnew/commonegovnew.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/EGF/cssnew/tabber.css" TYPE="text/css">
<script type="text/javascript" src="/EGF/javascript/tabber.js"></script>
<script type="text/javascript" src="/EGF/javascript/RemitRevoceryHelper.js"></script>
<script type="text/javascript" src="/EGF/javascript/tabber2.js"></script>
<title> <s:text name="remit.recovery.create.title"/></title>
<script>

function loadBank(fundId){
	populatebank({fundId:fundId.options[fundId.selectedIndex].value,typeOfAccount:"PAYMENTS,RECEIPTS_PAYMENTS"})	
}
function loadBankForFund(fundId){
	populatebank({fundId:fundId.options[fundId.selectedIndex].value})	
}
function validateFund(){
	var fund = document.getElementById('fundId').value;
	var bank = document.getElementById('bank');
	if(fund == -1 && bank.options.length==1){
		alert("Please select a Fund")
		return false;
	}
	return true;
}
function populateAccNumbers(bankBranch){
	var fund = document.getElementById('fundId');
	id = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1]
	populatebankaccount({branchId:id,fundId:fund.options[fund.selectedIndex].value})	
}
function populateAccNumbersForId(bankBranchId){
	var fund = document.getElementById('fundId');
	populatebankaccount({branchId:bankBranchId,fundId:fund.options[fund.selectedIndex].value})	
}
function onLoadTask(){ 
}
function populateAccNum(branch){
	var fundObj = document.getElementById('fundId');
	var bankbranchId = branch.options[branch.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0,index);
	var brId=bankbranchId.substring(index+1,bankbranchId.length);
	
	var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';
	
	populateaccountNumber({fundId: fundObj.options[fundObj.selectedIndex].value,bankId:bankId,branchId:brId,typeOfAccount:vTypeOfAccount})
}
function checkLength(obj)
{
	if(obj.value.length>1024)
	{
		alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
}

function populateUser(){
	
	var desgFuncry = document.getElementById("designationId").value;
	var array = desgFuncry.split("-");
	var functionary = array[1];
	var desgId = array[0];
	if(desgId==""){ // when user doesnot selects any value in the designation drop down.
		desgId=-1;
	}
	populateapproverUserId({departmentId:document.getElementById("departmentid").value,
	designationId:desgId,functionaryName:functionary})
		
}
function validateApproveUser(name,value){
	document.getElementById("actionName").value= name;
<s:if test="%{wfitemstate !='END'}">
	 if( (value == 'Approve' || value=='Send for Approval' || value == 'Forward' || value == 'Save And Forward') && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		alert("Please Select the user");
		return false;
	}
</s:if>
	return true;
}

function printVoucher(){
	document.forms[0].action='../report/billPaymentVoucherPrint!print.action?id=<s:property value="paymentheader.id"/>';
	document.forms[0].submit();
}
	function validate(obj,name,value)
		{
		document.getElementById('lblError').innerHTML = "";
			if(!validateMIS())
			  return false;
		if(document.getElementById('balanceAvl') && document.getElementById('balanceAvl').style.display=="block" )
			{
			if(obj.id!='wfBtn1') // in case of Reject
				{
			if(parseFloat(document.getElementById('totalAmount').value) > parseFloat(document.getElementById('availableBalance').value))
				{

				var insuffiecientBankBalance ='<s:text name="insuffiecientBankBalance"/>';
					alert(insuffiecientBankBalance);
					return false;
				}
				}
			}  
			if(!validateApproveUser(name,value))    
				return false;
			return true;
		}

</script>
</head>
<body >
	<s:form action="remitRecovery" theme="simple" name="remittanceForm"  >
	<s:push value="model">
		<jsp:include page="../budget/budgetHeader.jsp">
        	<jsp:param name="heading" value="Remittance Recovery" />
		</jsp:include>
		<span class="mandatory">
			<s:actionerror/>  
			<s:fielderror />
			<s:actionmessage />
			<div align="center" class="error-block" id="lblError" style="font:bold;text-align:center"></div>
		</span>
		
		<div class="formmainbox"><div class="subheadnew"><s:text name="remit.recovery.create.title"/></div>
		<div id="budgetSearchGrid" style="display:block;width:100%;border-top:1px solid #ccc;" >
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
				<td>
				<div align="left"><br/>
  					<table border="0" cellspacing="0" cellpadding="0" width="100%">
        			<tr>
        			<td> 
		            <div class="tabber">
		            <div class="tabbertab" id="searchtab">
               			<h2><s:text name="remit.recovery.header"/></h2>
	                	<span>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						
							<tr><td align="center" colspan="6" class="serachbillhead"><s:text name="remit.recovery.header"/></td></tr>
							 <tr>
						   		 <td class="bluebox">&nbsp;</td>
							<td class="bluebox"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
								<td class="bluebox"><s:textfield name="voucherNumber" id="vouchernumber" /></td>
							 <td class="bluebox" width="18%"><s:text name="voucher.date"/><span class="mandatory">*</span></td>
							<s:date name='voucherDate' id="voucherDateId" format='dd/MM/yyyy'/>
							<td class="bluebox" width="34%">
							<div name="daterow" >
							<s:textfield  name="voucherDate" id="voucherDate" maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="15" value="%{voucherDateId}"/><A href="javascript:show_calendar('forms[0].voucherDate',null,null,'DD/MM/YYYY');" style="text-decoration:none" align="left"><img img width="18" height="18" border="0" align="absmiddle" alt="Date" src="${pageContext.request.contextPath}/image/calendaricon.gif" /></A> </div></td> 
				 			<tr>
							<jsp:include page="../voucher/vouchertrans-filter-new.jsp"/>
							
							</tr>
							<tr>
		<td  class="greybox"></td>
		<td class="greybox"><s:text name="bank"/>
		<span class="greybox"><span class="mandatory">*</span></span></td>
		<egov:ajaxdropdown id="bankId" fields="['Text','Value']" dropdownId="bankId" url="/voucher/common!ajaxLoadBanksByFundAndType.action" />
		<td class="greybox"><s:select name="commonBean.bankId"  id="bankId" list="dropdownData.bankList" listKey="bankBranchId" listValue="bankBranchName" headerKey="" headerValue="----Choose----" onChange="populateAccNum(this);"  /></td>
	 <egov:ajaxdropdown id="accountNumber" fields="['Text','Value']" dropdownId="accountNumber" url="voucher/common!ajaxLoadBankAccounts.action" />
		<td class="greybox"  width="22%"><s:text name="account.number"/><span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="greybox" width="22%"><s:select  name="commonBean.accountNumberId" id="accountNumber" list="dropdownData.accNumList" listKey="id" listValue="chartofaccounts.glcode+'--'+accountnumber+'--'+accounttype" headerKey="" headerValue="----Choose----" onChange="populateNarration(this);populateAvailableBalance(this);" />
		<s:textfield name="commonBean.accnumnar" id="accnumnar"  readonly="true" tabindex="-1"/></td>
	</tr>
	  <tr>
  	<td class="bluebox">&nbsp;</td>
    <td class="bluebox">Payment Amount</td>
    <td class="bluebox"><label name="remitAmount" id="remitAmount"/></td>
    <egov:updatevalues id="availableBalance" fields="['Text']" url="/payment/payment!ajaxGetAccountBalance.action"/>
		<td class="bluebox" id="balanceText" style="display:none" width="18%"><s:text name="balance.available"/></td>
		<td class="bluebox" id="balanceAvl"  style="display:none" width="32%"><s:textfield name="commonBean.availableBalance" id="availableBalance" readonly="readonly" style="text-align:right"  value="%{commonBean.availableBalance}"/></td>
  </tr>
  <tr>
	<td class="greybox">&nbsp;</td>
	<td class="greybox">Mode of Payment</td>
    <td class="greybox"><s:radio name="modeOfPayment" id="paymentMode" list="%{modeOfCollectionMap}"/></td>
	<td class="greybox"/>
	<td class="greybox"/>
	</tr>
  <tr>
    <td class="bluebox">&nbsp;</td>
    <td class="bluebox">Narration:</td>
    <td class="bluebox" colspan="4"><textarea name="narration" id="narration" type="text" style="width:580px;"></textarea></td>
	<td></td>
  </tr>  
</table>
        		</span>                  
              		</div>	
          			<div class="tabbertab" id="contractortab">
           			<h2><s:text name="remit.recovery.detais"/></h2>
                	<span>
					<table align="center" border="0" cellpadding="0" cellspacing="0" class="newtable">
						<tr><td colspan="6"><div class="subheadsmallnew"><s:text name="remit.recovery.detais"/></div></td></tr>
						<tr>
							<td colspan="6">
							<div  style="float:left; width:100%;">
							
							<jsp:include page="remitRecoveryPayment-form.jsp"/>
							<s:hidden  name="remittanceBean.recoveryId" />
								<div class="yui-skin-sam" align="center">
      								 <div id="recoveryDetailsTableNew"></div>
    							 </div>
     						<script>
								populateRecoveryDetailsForPayment();
								document.getElementById('recoveryDetailsTableNew').getElementsByTagName('table')[0].width="80%"
							 </script><br>
							
							<table align="center" id="totalAmtTable">
							<tr >
							<td width="514"></td>
							<td >Total Amount</td>
							<td ><s:textfield name="remittanceBean.totalAmount" id="totalAmount" size="10"  style='text-align:right' readonly="true" value="0"/></td>
							</tr>
							</table>
							
						</div>
						</td>
					</tr>
					</table>                    
               		</span>                
                	</div>
                
			</div> <!-- tabber div -->
			</td>
       		</tr>
      		</table>
		</div>
		</td>
		</tr>
		</table>
	</div>
	</div>
	<s:if test="%{wfitemstate !='END'}">
	
				<%@include file="../voucher/workflowApproval.jsp"%>
	</s:if>
	</div>
	</div>
	<table>
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" >Comments</td>
		<td class="bluebox" colspan="4"><s:textarea name="comments" id="comments" cols="100" rows="3" onblur="checkLength(this)" value="%{getComments()}"/></td>
	</tr>
	</table>
	
	
	

<s:if test="%{showApprove}">

<s:if test="%{paymentheader.state.value != 'NEW'}">
	<s:if test="%{paymentheader.state.id!=null}">
		<div id="labelAD" align="center">
 			<div class="subheadsmallnew"><strong><s:text name="inbox.payment.history"/></strong></div>
		</div>
	  	<div id="wfHistoryDiv">
		  	<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
		        <c:param name="stateId" value="${paymentheader.state.id}"></c:param>
		    </c:import>
	  	</div>
	</s:if>
</s:if>
</s:if>
	<div  class="buttonbottom" id="buttondiv">
		<s:hidden  name="paymentid" value="%{paymentheader.id}"/>
		
		<s:hidden  name="actionname" id="actionName" value="%{action}"/>
		<s:if test="%{showButtons}">
				<s:hidden  name="scriptName" id="scriptName" value="paymentHeader.nextDesg"/>
		<s:iterator value="%{getValidActions()}" var="p"  status="s">
		  <s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="wfBtn%{#s.index}" name="%{name}" method="sendForApproval" onclick="return validate(this,'%{name}','%{description}')"/>
		</s:iterator>
		</s:if>
		<s:if test="%{wfitemstate !='END'}">
		</s:if>
		<s:else>
		<s:submit cssClass="button" id="printPreview" value="Print Preview"  onclick="printVoucher()"/>
		</s:else>
		<s:if test="%{showCancel}">
		  <s:submit type="submit" cssClass="buttonsubmit" value="Cancel Payment"  id="cancelPayment" name="cancel" method="sendForApproval" onclick="document.getElementById('actionName').value='cancelPayment';" />
		</s:if>
		<input type="submit" id="closeButtonNew" value="Close"  onclick="javascript:window.close()" class="button"/>
	</div>
	<script type="text/javascript">
	//alert('<s:property value="fund.id"/>');                               
	calcTotalForPayment();
	</script>
	</s:push>
	<SCRIPT type="text/javascript">

		var frmIndex=0;
		for(var i=0;i<document.forms[frmIndex].length;i++)
		document.forms[frmIndex].elements[i].disabled =true;
		disableYUIAddDeleteButtons(true);
		if(document.getElementById("closeButton"))
			document.getElementById("closeButton").disabled=false;
		if(document.getElementById("closeButtonNew"))
			document.getElementById("closeButtonNew").disabled=false;
		if(document.getElementById("comments"))
			document.getElementById("comments").disabled=false;
		if(document.getElementById("paymentid"))
			document.getElementById("paymentid").disabled=false;
		if(document.getElementById("actionName"))
			document.getElementById("actionName").disabled=false;
		if(document.getElementById("printPreview"))
			document.getElementById("printPreview").disabled=false;
		if(document.getElementById("cancelPayment"))
			document.getElementById("cancelPayment").disabled=false;		
		if(null != document.getElementById("departmentid") ){
			document.getElementById("departmentid").disabled=false;    
			document.getElementById("designationId").disabled=false;
			document.getElementById("approverUserId").disabled=false;
			
		}
				
	<s:if test="%{showMode!='view'}">	
	 <s:if test="%{validateUser('balancecheck')}">
			if(document.getElementById('balanceText'))
			{
				document.getElementById('balanceText').style.display='block';
				document.getElementById('balanceAvl').style.display='block';
				var x=document.getElementById('availableBalance').value;
				x=parseFloat(x);
				document.getElementById('availableBalance').value=x.toFixed(2);
			}
	</s:if>	
	</s:if>
		
		
		if(document.getElementById("wfBtn0"))
		{
		document.getElementById("wfBtn0").disabled=false;
		}
		if(document.getElementById("wfBtn1"))
		{
		document.getElementById("wfBtn1").disabled=false;
		}
		if(document.getElementById("wfBtn2"))
		{
		document.getElementById("wfBtn3").disabled=false;
		}
		
		
		
</SCRIPT>

<s:if test="%{showMode!='view'}">	
<s:if test="%{validateUser('balancecheck')}">   
		
<SCRIPT>
			if(document.getElementById('balanceText'))
			{
				document.getElementById('balanceText').style.display='block';
				document.getElementById('balanceAvl').style.display='block';
			}
		
</SCRIPT>
	</s:if>	
	
	<s:if test="%{balance=='-1'}">
	<script>
	alert("FundFlow Report not Generated to check Bank Balance. Please generate Report First");
	for(var i=0;i<document.forms[0].length;i++)
	if(document.forms[0].elements[i].id!='closeButtonNew')
		document.forms[0].elements[i].disabled =true;
	</script>
</s:if>
</s:if>
	</s:form>
	
</body>

</html>