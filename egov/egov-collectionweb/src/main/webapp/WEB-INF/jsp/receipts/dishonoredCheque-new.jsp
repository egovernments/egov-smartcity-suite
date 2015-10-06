<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="dishonorcheque.title"/></title>
<script type="text/javascript">
function onChangeBankAccount(branchId)
{
	populateaccountNumberMaster({bankBranch:branchId});
}
function validate(){
	if(document.getElementById("instrumentModes").value=="0")
	{
		alert("Please Select Instrument Mode First!");
		document.getElementById("instrumentModes").focus();
		return false;
	}
	
	if(document.getElementById("chequeNumber").value=="")
	{
		alert("Please Enter Cheque/DD Number First !!!");
		document.getElementById("chequeNumber").focus();
		return false;
	}
	if(document.getElementById("chequeDate").value == "" && document.getElementById("chequeDate").value.length==0)
	{	
		alert("Please Enter Cheque/DD Date !!!");
		document.DishonoredChequeForm.chequeDate.focus();
		return false;
		
	}
	if(document.getElementById("chequeDate").value != null && document.getElementById("chequeDate").value != "" && document.getElementById("chequeDate").value.length>0)
	{	
		var dat=validateDate(document.DishonoredChequeForm.chequeDate.value);
		if (!dat){
		alert('Invalid date format : Enter Date as dd/mm/yyyy');
		document.DishonoredChequeForm.chequeDate.focus();
		return;
		}
	}
	
}
</script>
</head>
<body>
<div class="formmainbox">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
				</div>
			</s:if>
			<center>
					<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<s:form action="dishonoredCheque" name="DishonoredChequeForm" theme="simple">
							<s:token/>
								<div class="formheading"></div>
								
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="4%" class="bluebox">&nbsp;</td>
									<td width="15%" class="bluebox"><s:text name="dishonorcheque.bankbranch"/>:</td>
									<td width="36%" class="bluebox">
										<s:select headerValue="--Select--"  headerKey="0" list="dropdownData.bankBranchList" listKey="id" id="bankBranch" 
										listValue="branchname" label="bankBranch" name="bankBranch" value="%{branchname - id}" onChange="onChangeBankAccount(this.value)"/>
										<egov:ajaxdropdown id="accountNumberMasterDropdown" fields="['Text','Value']" dropdownId='accountNumberMaster' 
										url='receipts/dishonoredCheque-getAccountNumbers.action' selectedValue="%{bankaccount.id}"/>
									</td>
									<td width="15%" class="bluebox"><s:text name="dishonorcheque.accountnumber"/>:</td>
									<td width="30%" class="bluebox">
										<s:select headerValue="--Select--"  headerKey="0"
						                list="dropdownData.accountNumberList" listKey="id" id="accountNumberMaster" listValue="accountnumber"
						                label="accountNumberMaster" name="accountNumberMaster" value="%{bankaccount.id}"/>
									</td>
									</tr>
									<tr>
									<td width="4%" class="bluebox">&nbsp;</td>
									<td width="15%" class="bluebox"><s:text name="dishonorcheque.instrumentmode"/><span class="mandatory">*</span>:</td>
									<td width="36%" class="bluebox">
										<s:select headerValue="--Select--"  headerKey="0" list="instrumentModesMap" listKey="id" id="instrumentModes" 
										listValue="value" label="instrumentModes" name="instrumentModes" />
									</td>
									</tr>
									 <tr>
								      <td width="4%" class="bluebox">&nbsp;</td>
								      <td width="21%" class="bluebox"><s:text name="dishonorcheque.cheque.dd.number"/><span class="mandatory"/></td>
									  <td width="24%" class="bluebox"><s:textfield id="chequeNumber" name="chequeNumber" /> </td>
								      <td width="21%" class="bluebox"><s:text name="dishonorcheque.cheque.dd.date"/><span class="mandatory"/></td>
								      <s:date name="chequeDate" var="cdFormat1" format="dd/MM/yyyy"/>
									  <td width="30%" class="bluebox"><s:textfield id="chequeDate" name="chequeDate" value="%{cdFormat1}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].chequeDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"  ><img src="/egi/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" /></a><div class="highlight2" style="width: 80px">DD/MM/YYYY</div></td>
								    </tr>
			   				</table>				
						</s:form>
					</table>
					<div class="buttonbottom">
					  <input name="button32" type="button" class="buttonsubmit" id="buttonview" value="View" onclick="return validate()"/>&nbsp;
					  <input name="button32" type="button" class="button" id="button" value="Close" onclick="window.close();"/>
					</div>
								
			<div align="left" class="mandatory" style="font-size: 11px">
			<s:text name="common.mandatoryfields"/>
			</div>
			</center>
		</div>
</body>
</html>