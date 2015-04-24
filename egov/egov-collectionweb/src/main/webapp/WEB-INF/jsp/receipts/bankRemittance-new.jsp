<%@ include file="/includes/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="bankRemittance.title"/></title>
<script>

jQuery.noConflict();
jQuery(document).ready(function() {
  	 
     jQuery(" form ").submit(function( event ) {
    	 doLoadingMask();
    });
     doLoadingMask();
 });

jQuery(window).load(function () {
	undoLoadingMask();
});


var newServiceName="###########";
var newFundName="###########";
function handleReceiptSelectionEvent(serviceName,fundName,obj)
{
	
	isSelected=document.getElementsByName('receiptIds');
	dom.get("multipleserviceselectionerror").style.display="none";
	dom.get("selectremittanceerror").style.display="none";

	dom.get("button32").disabled=false;
	dom.get("button32").className="buttonsubmit";

	for (i = 0; i < isSelected.length; i++)
	{
		if (isSelected[i].checked == true) {
			document.getElementsByName('serviceNameArray')[i].value=document.getElementsByName('serviceNameTempArray')[i].value;
			document.getElementsByName('fundCodeArray')[i].value=document.getElementsByName('fundCodeTempArray')[i].value;
			document.getElementsByName('departmentCodeArray')[i].value=document.getElementsByName('departmentCodeTempArray')[i].value;	
			document.getElementsByName('totalCashAmountArray')[i].value=document.getElementsByName('totalCashAmountTempArray')[i].value;
			document.getElementsByName('totalChequeAmountArray')[i].value=document.getElementsByName('totalChequeAmountTempArray')[i].value;
			document.getElementsByName('totalCardAmountArray')[i].value=document.getElementsByName('totalCardAmountTempArray')[i].value;
			document.getElementsByName('totalOnlineAmountArray')[i].value=document.getElementsByName('totalOnlineAmountTempArray')[i].value;
			document.getElementsByName('receiptDateArray')[i].value=document.getElementsByName('receiptDateTempArray')[i].value;
		}
		else if(isSelected[i].checked == false)
		{
			document.bankRemittanceForm.serviceNameArray[i].value="";
			document.bankRemittanceForm.fundCodeArray[i].value="";
			document.bankRemittanceForm.departmentCodeArray[i].value="";
			document.bankRemittanceForm.totalCashAmountArray[i].value="";
			document.bankRemittanceForm.totalChequeAmountArray[i].value="";
			document.bankRemittanceForm.totalCardAmountArray[i].value="";
			document.bankRemittanceForm.totalOnlineAmountArray[i].value="";
			document.bankRemittanceForm.receiptDateArray[i].value="";
		}
	}

	var serviceNameArray=document.getElementsByName('serviceNameArray');
	for(j=0; j<serviceNameArray.length; j++)
	{
		if(document.getElementsByName('serviceNameArray')[j].value!="")
		{
			for (k = 0; k < isSelected.length; k++)
			{
				if (isSelected[k].checked == true)
				{
					if((document.getElementsByName('serviceNameArray')[j].value==document.getElementsByName('serviceNameTempArray')[k].value) && (document.getElementsByName('fundCodeArray')[j].value==document.getElementsByName('fundCodeTempArray')[k].value) && (document.getElementsByName('departmentCodeArray')[j].value==document.getElementsByName('departmentCodeTempArray')[k].value)){}
					else
					{
						dom.get("multipleserviceselectionerror").style.display="block";
						dom.get("button32").disabled=true;
						dom.get("button32").className="button";
						return false;
					}
				}
			}
		}
	}
	if(serviceName!="" && fundName!="" && obj==true)
	{
		newServiceName=serviceName;
		newFundName=fundName;
	}
	populatebankBranchMaster({serviceName:newServiceName,fundName:newFundName});
}




// Check if at least one receipt is selected
function isChecked(chk) {
	if (chk.length == undefined) {
 		if (chk.checked == true) {
  			return true;
 		} else {
 	 		return false;
 		}
 	} else {
 		for (i = 0; i < chk.length; i++)
		{
			if (chk[i].checked == true ) {
				return true;
			}
		}
		return false;
 	}
}

// Changes selection of all receipts to given value (checked/unchecked)
function changeSelectionOfAllReceipts(checked) {
	chk = document.getElementsByName('receiptIds');
	if (chk.length == undefined) {
		chk.checked = checked;
 	} else {
 		for (j = 0; j < chk.length; j++)
		{
 			chk[j].checked = checked;
		}
 	}
}


function validate()
{
	dom.get("bankselectionerror").style.display="none";
	dom.get("accountselectionerror").style.display="none";
	dom.get("selectremittanceerror").style.display="none";
	dom.get("approvalSelectionError").style.display="none";

	if (!isChecked(document.getElementsByName('receiptIds')))
	{
		dom.get("selectremittanceerror").style.display="block";
		return false;
	}
	else
	{
		if(dom.get("bankBranchMaster").value==0)
		{
			dom.get("bankselectionerror").style.display="block";
			return false;
		}
		if(dom.get("accountNumberMaster").value==0)
		{
			dom.get("accountselectionerror").style.display="block";
			return false;
		}
		if(document.getElementById('positionUser') != null && document.getElementById('positionUser').value == -1){
				dom.get("approvalSelectionError").style.display="block";
				return false;
		}	

		doLoadingMask('#loadingMask');
		document.bankRemittanceForm.action="bankRemittance!create.action";
		document.bankRemittanceForm.submit();
	}

}

function onChangeBankAccount(branchId)
{
	populateaccountNumberMaster({branchId:branchId,serviceName:newServiceName,fundName:newFundName});
}

function onChangeDeparment(approverDeptId)
{
	var receiptheaderId='<s:property value="model.id"/>';
	if(document.getElementById('designationId')){
		populatedesignationId({approverDeptId:approverDeptId,receiptheaderId:receiptheaderId});
	}
}

function onChangeDesignation(designationId)
{
	var approverDeptId;
	if(document.getElementById('approverDeptId')){
		approverDeptId=document.getElementById('approverDeptId').value;
	}
	if(document.getElementById('positionUser')){
		populatepositionUser({designationId:designationId,approverDeptId:approverDeptId});
	}
}
</script>
</head>
<span align="center" style="display:none" id="selectremittanceerror">
  <li>
     <font size="2" color="red"><b><s:text name="bankremittance.error.norecordselected"/>  </b></font>
  </li>
</span>
<span align="center" style="display:none" id="multipleserviceselectionerror">
  <li>
     <font size="2" color="red"><b><s:text name="bankremittance.error.multipleserviceselectionerror"/>  </b></font>
  </li> 
</span>
<span align="center" style="display:none" id="bankselectionerror">
  <li>
     <font size="2" color="red"><b><s:text name="bankremittance.error.nobankselected"/>  </b></font>
  </li>
</span>
<span align="center" style="display:none" id="accountselectionerror">
  <li>
     <font size="2" color="red"><b><s:text name="bankremittance.error.noaccountNumberselected"/>  </b></font>
  </li>
</span>
<span align="center" style="display:none" id="approvalSelectionError">
  <li>
     <font size="2" color="red"><b><s:text name="bankremittance.error.noApproverselected"/>  </b></font>
  </li>
</span>

<body>

<s:form theme="simple" name="bankRemittanceForm">
<s:token/>
	<div class="formmainbox"><div class="subheadnew"><s:text name="bankRemittance.title"/></div>
		<logic:notEmpty name="paramList">
			<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="tablebottom">
				<div align="center">
					<display:table name="paramList" uid="currentRow" pagesize="30" style="border:1px;width:100%" cellpadding="0" cellspacing="0" export="false" requestURI="" excludedParams="*">
						<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Submit" style="width:5%;text-align: center">
						<input name="receiptIds" type="checkbox" id="receiptIds"
						value="${currentRow.id}"
						onClick="handleReceiptSelectionEvent('${currentRow.SERVICENAME}','${currentRow.FUNDNAME}',this.checked)"/>
						<egov:ajaxdropdown id="bankBranchMasterDropdown" fields="['Text','Value']" dropdownId='bankBranchMaster'
		                 url='receipts/ajaxBankRemittance!bankBranchList.action' selectedValue="%{bankbranch.id}"/>
						<input type="hidden" name="serviceNameTempArray" id="serviceNameTempArray" value="${currentRow.SERVICENAME}"/>
						<input type="hidden" name="fundCodeTempArray" id="fundCodeTempArray" value="${currentRow.FUNDCODE}"/>
						<input type="hidden" name="departmentCodeTempArray" id="departmentCodeTempArray" value="${currentRow.DEPARTMENTCODE}"/>		
						<input type="hidden" name="totalCashAmountTempArray" id="totalCashAmountTempArray" value="${currentRow.SERVICETOTALCASHAMOUNT}"/>
						<input type="hidden" name="totalChequeAmountTempArray" id="totalChequeAmountTempArray" value="${currentRow.SERVICETOTALCHEQUEAMOUNT}"/>
						<input type="hidden" name="totalCardAmountTempArray" id="totalCardAmountTempArray" value="${currentRow.SERVICETOTALCARDPAYMENTAMOUNT}"/>
						<input type="hidden" name="totalOnlineAmountTempArray" id="totalOnlineAmountTempArray" value="${currentRow.SERVICETOTALONLINEPAYMENTAMOUNT}"/>
						<input type="hidden" name="receiptDateTempArray" id="receiptDateTempArray" value="${currentRow.VOUCHERDATE}"/>
						<input type="hidden" name="serviceNameArray" id="serviceNameArray" />
						<input type="hidden" name="fundCodeArray" id="fundCodeArray" />
						<input type="hidden" name="departmentCodeArray" id="departmentCodeArray" />
						<input type="hidden" name="totalCashAmountArray" id="totalCashAmountArray" />
						<input type="hidden" name="totalChequeAmountArray" id="totalChequeAmountArray" />
						<input type="hidden" name="totalCardAmountArray" id="totalCardAmountArray" />
						<input type="hidden" name="totalOnlineAmountArray" id="totalOnlineAmountArray" />
						<input type="hidden" name="receiptDateArray" id="receiptDateArray" />

					</display:column>

					<display:column headerClass="bluebgheadtd" class="blueborderfortd"
						title="Date" style="width:10%;text-align: center" value="${currentRow.VOUCHERDATE}" format="{0,date,dd/MM/yyyy}"/>
					<display:column headerClass="bluebgheadtd" class="blueborderfortd"
						title="Service Name" style="width:20%;text-align: center" value="${currentRow.SERVICENAME}"/>

					<display:column headerClass="bluebgheadtd" class="blueborderfortd"
						title="Fund" style="width:10%;text-align: center" value="${currentRow.FUNDNAME}"/>

					<display:column headerClass="bluebgheadtd" class="blueborderfortd"
						title="Department" style="width:10%;text-align: center" value="${currentRow.DEPARTMENTNAME}"/>
					
					<display:column headerClass="bluebgheadtd" class="blueborderfortd"
						title="Total Cash Collection" style="width:10%;text-align: center" ><div align="center"><c:if test="${not empty currentRow.SERVICETOTALCASHAMOUNT}"><c:out value="${currentRow.SERVICETOTALCASHAMOUNT}"/></c:if>&nbsp;</div></display:column>
					
					<display:column headerClass="bluebgheadtd" class="blueborderfortd"
						title="Total Cheque Collection" style="width:10%;text-align: center" ><div align="center"><c:if test="${not empty currentRow.SERVICETOTALCHEQUEAMOUNT}"><c:out value="${currentRow.SERVICETOTALCHEQUEAMOUNT}"/></c:if>&nbsp;</div></display:column>
					
					<display:column headerClass="bluebgheadtd" class="blueborderfortd"
						title="Total Card Collection" style="width:10%;text-align: center" ><div align="center"><c:if test="${not empty currentRow.SERVICETOTALCARDPAYMENTAMOUNT}"><c:out value="${currentRow.SERVICETOTALCARDPAYMENTAMOUNT}"/></c:if>&nbsp;</div></display:column>
					
					<display:column headerClass="bluebgheadtd" class="blueborderfortd"
						title="Total Online Collection" style="width:10%;text-align: center" ><div align="center"><c:if test="${not empty currentRow.SERVICETOTALONLINEPAYMENTAMOUNT}"><c:out value="${currentRow.SERVICETOTALONLINEPAYMENTAMOUNT}"/></c:if>&nbsp;</div></display:column>

			</display:table></div>
			<br />

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<td width="4%" class="bluebox2">&nbsp;</td>
				<td width="15%" class="bluebox2"><s:text name="bankremittance.bank"/><span class="mandatory">*</span>:</td>
				<td width="36%" class="bluebox2">
					<s:select headerValue="--Select--"  headerKey="0" list="dropdownData.bankBranchList" listKey="id" id="bankBranchMaster" 
					listValue="branchname" label="bankBranchMaster" name="bankBranchMaster" value="%{bankbranch.id}" onChange="onChangeBankAccount(this.value)"/>
					<egov:ajaxdropdown id="accountNumberMasterDropdown" fields="['Text','Value']" dropdownId='accountNumberMaster' 
					url='receipts/ajaxBankRemittance!accountList.action' selectedValue="%{bankaccount.id}"/>
				</td>
				<td width="15%" class="bluebox2"><s:text name="bankremittance.accountnumber"/><span class="mandatory">*</span>:</td>
				<td width="30%" class="bluebox2">
					<s:select headerValue="--Select--"  headerKey="0"
	                list="dropdownData.accountNumberList" listKey="id" id="accountNumberMaster" listValue="accountnumber"
	                label="accountNumberMaster" name="accountNumberMaster" value="%{bankaccount.id}"/>
				</td>
		   </table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">   
		<tr> 
			<div class="subheadnew">
				<s:text name="approval.authority.information"/>
			</div>
		</tr>
		<tr>
			<td width="4%" class="bluebox2">&nbsp;</td>
			<td width="15%" class="bluebox2"> Approver Department: <s:if test="%{model.id==null}"><span class="mandatory">*</span></s:if></td>
			<td width="20%" class="bluebox2"><s:select headerKey="" headerValue="%{getText('challan.select')}" name="approverDeptId" id="approverDeptId" cssClass="selectwk" list="dropdownData.approverDepartmentList" listKey="id" listValue="deptName" 
onChange="onChangeDeparment(this.value)" /> 
		<egov:ajaxdropdown id="designationIdDropdown" fields="['Text','Value']" dropdownId='designationId'
			         url='receipts/ajaxBankRemittance!approverDesignationList.action' selectedValue="%{designationId}"/>
			</td>

			
		      	<td width="15%" class="bluebox2"><s:text name="challan.approve.designation"/><s:if test="%{model.id==null}"><span class="mandatory">*</span></s:if></td>
			  <td width="20%" class="bluebox2"><s:select headerKey="" headerValue="--Select--" name="designationId" id="designationId" cssClass="selectwk"  list="dropdownData.designationMasterList" listKey="designationId" listValue="designationName" onChange="onChangeDesignation(this.value)"/>
			  <egov:ajaxdropdown id="positionUserDropdown" fields="['Text','Value']" dropdownId='positionUser'
			         url='receipts/ajaxBankRemittance!positionUserList.action' selectedValue="%{position.id}"/>	 
			 </td>
			 <td width="15%" class="bluebox2"><s:text name="challan.approve.userposition"/><s:if test="%{model.id==null}"><span class="mandatory">*</span></s:if></td>
				<td width="20%" class="bluebox2">
					<s:select headerValue="--Select--"  headerKey="-1"
	                list="dropdownData.postionUserList" listKey="position.id" id="positionUser" listValue="position.name"
	                label="positionUser" name="positionUser" value="%{position.id}"/>
				</td>
		</tr>		
		</table>
		<div id="loadingMask" style="display:none;overflow:hidden;text-align: center"><img src="${pageContext.request.contextPath}/images/bar_loader.gif"/> <span style="color: red">Please wait....</span></div>
		
			<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/></div>
			<div class="buttonbottom">
				<input name="button32" type="button" class="buttonsubmit" id="button32" value="Remit to Bank" onclick="return validate()"/> &nbsp;
				<input name="buttonClose" type="button" class="button" id="button" value="Close" onclick="window.close()" /></div>
			</div>
			</logic:notEmpty>
			</div>
			<logic:empty name="paramList">
				<div class="formmainbox">
				<table width="90%" border="0" align="center" cellpadding="0"
					cellspacing="0" >
					<tr>
						<div>&nbsp;</div>
						<div class="billhead2"><b><s:text name="bankRemittance.norecordfound" /></b></div>
					</tr>
				</table>
				<br />
				</div>
				<div class="buttonbottom">
				<input name="buttonClose" type="button" class="button"
					id="buttonClose" value="Close" onclick="window.close()" /></div>
				
			</logic:empty>
		</s:form>
</body>
</html>

