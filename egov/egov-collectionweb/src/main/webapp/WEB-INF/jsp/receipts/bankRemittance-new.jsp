
<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ include file="/includes/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="bankRemittance.title" /></title>
<script type="text/javascript">
	jQuery.noConflict();
	jQuery(document).ready(function() {
		jQuery('#finYearId').prop("disabled", true); 
		jQuery(" form ").submit(function(event) {
			doLoadingMask();
		});
		doLoadingMask();
	});

	jQuery(window).load(function() {
		undoLoadingMask();
	});

	var newServiceName = "###########";
	var newFundName = "###########";
	function handleReceiptSelectionEvent(obj) {

		isSelected = document.getElementsByName('receiptIds');
		dom.get("multipleserviceselectionerror").style.display = "none";
		dom.get("selectremittanceerror").style.display = "none";

		dom.get("button32").disabled = false;
		dom.get("button32").className = "buttonsubmit";

		for (i = 0; i < isSelected.length; i++) {
			if (isSelected[i].checked == true) {
				document.getElementsByName('serviceNameArray')[i].value = document
						.getElementsByName('serviceNameTempArray')[i].value;
				document.getElementsByName('fundCodeArray')[i].value = document
						.getElementsByName('fundCodeTempArray')[i].value;
				document.getElementsByName('departmentCodeArray')[i].value = document
						.getElementsByName('departmentCodeTempArray')[i].value;
				document.getElementsByName('totalCashAmountArray')[i].value = document
						.getElementsByName('totalCashAmountTempArray')[i].value;
				document.getElementsByName('totalChequeAmountArray')[i].value = document
						.getElementsByName('totalChequeAmountTempArray')[i].value;
				document.getElementsByName('totalCardAmountArray')[i].value = document
						.getElementsByName('totalCardAmountTempArray')[i].value;
				document.getElementsByName('totalOnlineAmountArray')[i].value = document
						.getElementsByName('totalOnlineAmountTempArray')[i].value;
				document.getElementsByName('receiptDateArray')[i].value = document
						.getElementsByName('receiptDateTempArray')[i].value;
			} else if (isSelected[i].checked == false) {
				document.bankRemittanceForm.serviceNameArray[i].value = "";
				document.bankRemittanceForm.fundCodeArray[i].value = "";
				document.bankRemittanceForm.departmentCodeArray[i].value = "";
				document.bankRemittanceForm.totalCashAmountArray[i].value = "";
				document.bankRemittanceForm.totalChequeAmountArray[i].value = "";
				document.bankRemittanceForm.totalCardAmountArray[i].value = "";
				document.bankRemittanceForm.totalOnlineAmountArray[i].value = "";
				document.bankRemittanceForm.receiptDateArray[i].value = "";
			}
		}

		//TODO: uncomment the validation after go live
		/* var receiptDateArray=document.getElementsByName('receiptDateArray');
		for(j=0; j<receiptDateArray.length; j++)
		{
			if(document.getElementsByName('receiptDateArray')[j].value!="")
			{
				for (k = 0; k < isSelected.length; k++)
				{
					if (isSelected[k].checked == true)
					{
						if((document.getElementsByName('receiptDateArray')[j].value==document.getElementsByName('receiptDateArray')[k].value)){}
						else
						{
							dom.get("multipleserviceselectionerror").style.display="block";
							dom.get("button32").disabled=true;
							dom.get("button32").className="button";
							window.scroll(0,0);
							return false;
						}
					}
				}
			}
		} */
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
			for (i = 0; i < chk.length; i++) {
				if (chk[i].checked == true) {
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
			for (j = 0; j < chk.length; j++) {
				chk[j].checked = checked;
			}
		}
	}

	function validate() {
		dom.get("bankselectionerror").style.display = "none";
		dom.get("accountselectionerror").style.display = "none";
		dom.get("selectremittanceerror").style.display = "none";
		dom.get("approvalSelectionError").style.display = "none";

		<s:if test="showRemittanceDate">
		if(dom.get("remittanceDate")!=null && dom.get("remittanceDate").value=="")
			{
			bootbox.alert("Please Enter Date of Remittance");
			return false;
			}
		</s:if>
		if (!isChecked(document.getElementsByName('receiptIds'))) {
			dom.get("selectremittanceerror").style.display = "block";
			return false;
		} else {
			doLoadingMask('#loadingMask');
			document.bankRemittanceForm.action = "bankRemittance-create.action";
			document.bankRemittanceForm.submit();
		}
		

	}

	function onChangeBankAccount(branchId) {
		populateaccountNumberId({
			branchId : branchId,
		});
	}

	function searchDataToRemit() {
		if (dom.get("bankBranchMaster").value!=null && dom.get("bankBranchMaster").value == -1) {
			dom.get("bankselectionerror").style.display = "block";
			return false;
		}
		if (dom.get("accountNumberId").value!=null && dom.get("accountNumberId").value == -1) {
			dom.get("bankselectionerror").innerHTML="";
			dom.get("accountselectionerror").style.display = "block";
			return false;
		}
		jQuery('#finYearId').prop("disabled", false);
		document.bankRemittanceForm.action = "bankRemittance-listData.action?bankAccountId="+dom.get("accountNumberId").value;
		document.bankRemittanceForm.submit();
		}

	function onChangeDeparment(approverDeptId) {
		var receiptheaderId = '<s:property value="model.id"/>';
		if (document.getElementById('designationId')) {
			populatedesignationId({
				approverDeptId : approverDeptId,
				receiptheaderId : receiptheaderId
			});
		}
	}

	function onChangeDesignation(designationId) {
		var approverDeptId;
		if (document.getElementById('approverDeptId')) {
			approverDeptId = document.getElementById('approverDeptId').value;
		}
		if (document.getElementById('positionUser')) {
			populatepositionUser({
				designationId : designationId,
				approverDeptId : approverDeptId
			});
		}
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


	//DeSelect all receipts
	function deSelectAll() {
		// DeSelect all checkboxes
		changeSelectionOfAllReceipts(false);

	 	// Set all amounts to zero
		totalAmount = 0;
		cashAmount = 0;
		chequeAmount = 0;
		ddAmount = 0;
		cardAmount = 0;

		// Refresh the summary section
		refreshSummary();

		// Enable/disable buttons
		enableButtons();
	}

	// Select all receipts
	function selectAll() {
		// Select all checkboxes
		changeSelectionOfAllReceipts(true);
	}

	function setCheckboxStatuses(isSelected) {
		if(isSelected == true) {
			selectAll();
		} else {
			deSelectAll();
		}
	}

	var isDatepickerOpened=false;
	jQuery(document).ready(function() {
	var nowTemp = new Date();
    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
    
     jQuery( "#remittanceDate" ).datepicker({ 
   	 format: 'dd/mm/yyyy',
   	 endDate: nowTemp, 
   	 autoclose:true,
        onRender: function(date) {
     	    return date.valueOf() < now.valueOf() ? 'disabled' : '';
     	  }
	  }).on('changeDate', function(ev) {
		  var string=jQuery(this).val();
		  if(!(string.indexOf("_") > -1)){
			  isDatepickerOpened=false; 
		  }
	  }).data('datepicker');
	 });
</script>
</head>
<body>
	<span align="center" style="display: none" id="selectremittanceerror">
		<li><font size="2" color="red"><b><s:text
						name="bankremittance.error.norecordselected" /> </b></font></li>
	</span>
	<span align="center" style="display: none"
		id="multipleserviceselectionerror">
		<li><font size="2" color="red"><b><s:text
						name="bankremittance.error.multipleserviceselectionerror" /> </b></font></li>
	</span>
	<span align="center" style="display: none" id="bankselectionerror">
		<li><font size="2" color="red"><b><s:text
						name="bankremittance.error.nobankselected" /> </b></font></li>
	</span>
	<span align="center" style="display: none" id="accountselectionerror">
		<li><font size="2" color="red"><b><s:text
						name="bankremittance.error.noaccountNumberselected" /> </b></font></li>
	</span>
	<span align="center" style="display: none" id="approvalSelectionError">
		<li><font size="2" color="red"><b><s:text
						name="bankremittance.error.noApproverselected" /> </b></font></li>
	</span>
	<s:form theme="simple" name="bankRemittanceForm">
	<s:push value="model">
			<s:token />
		<s:if test="%{hasErrors()}">
	    <div id="actionErrorMessages" class="errorstyle">
	      <s:actionerror/>
	      <s:fielderror/>
	    </div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
		    <div id="actionMessages" class="messagestyle">
		    	<s:actionmessage theme="simple"/>
		    </div>
		</s:if>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="bankRemittance.title" />
			</div>
					<div align="center">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="4%" class="bluebox">&nbsp;</td>
						<td class="bluebox"><s:text
								name="bankremittance.bank" />:</td>
						<td class="bluebox"><s:select
								headerValue="--Select--" headerKey="-1"
								list="dropdownData.bankBranchList" listKey="id"
								id="bankBranchMaster" listValue="branchname"
								label="bankBranchMaster" name="branchId"
								value="%{branchId}"
								onChange="onChangeBankAccount(this.value)" /> <egov:ajaxdropdown
								id="accountNumberIdDropdown" fields="['Text','Value']"
								dropdownId='accountNumberId'
								url='receipts/ajaxBankRemittance-accountListOfService.action'
								selectedValue="%{accountNumberId}" /></td>
						<td class="bluebox"><s:text
								name="bankremittance.accountnumber" />:</td>
						<td class="bluebox"><s:select
								headerValue="--Select--" headerKey="-1"
								list="dropdownData.accountNumberList" listKey="id"
								id="accountNumberId" listValue="accountnumber"
								label="accountNumberMaster" name="accountNumberId"
								value="%{accountNumberId}" /></td>
								</tr>
						<tr>
						<td width="4%" class="bluebox">&nbsp;</td>
						<td class="bluebox"><s:text
								name="bankremittance.financialyear" />:</td>
						<td class="bluebox"><s:select
								headerKey="-1"
								list="dropdownData.financialYearList" listKey="id"
								id="finYearId" listValue="finYearRange"
								label="finYearRange" name="finYearId" 
								value="%{finYearId}"
								 /> 
								</td>
						</tr>
					</table>
					<div class="buttonbottom">
							<input name="search" type="button" class="buttonsubmit"
							id="search" value="Search" onclick="return searchDataToRemit()" />
					</div>
					<logic:notEmpty name="paramList">
						<display:table name="paramList" uid="currentRow" pagesize="30"
							style="border:1px;width:100%" cellpadding="0" cellspacing="0"
							export="false" requestURI="" excludedParams="serviceNameArray fundCodeArray departmentCodeArray totalCashAmountArray totalChequeAmountArray totalCardAmountArray totalATMAmountArray totalATMAmountTempArray departmentCodeTempArray totalOnlineAmountTempArray receiptDateTempArray serviceNameTempArray totalCardAmountTempArray totalCashAmountTempArray totalChequeAmountTempArray">
							<display:column headerClass="bluebgheadtd" class="blueborderfortd"
								title="Select<input type='checkbox' name='selectAllReceipts' value='on' onClick='setCheckboxStatuses(this.checked);handleReceiptSelectionEvent(this.checked);'/>"
								style="width:5%; text-align: center">
								<input name="receiptIds" type="checkbox" id="receiptIds"
									value="${currentRow.id}"
									onClick="handleReceiptSelectionEvent(this.checked)" />
								<input type="hidden" name="serviceNameTempArray"
									id="serviceNameTempArray" value="${currentRow.SERVICENAME}" />
								<input type="hidden" name="fundCodeTempArray"
									id="fundCodeTempArray" value="${currentRow.FUNDCODE}" />
								<input type="hidden" name="departmentCodeTempArray"
									id="departmentCodeTempArray"
									value="${currentRow.DEPARTMENTCODE}" />
								<input type="hidden" name="totalCashAmountTempArray"
									id="totalCashAmountTempArray"
									value="${currentRow.SERVICETOTALCASHAMOUNT}" />
								<input type="hidden" name="totalChequeAmountTempArray"
									id="totalChequeAmountTempArray"
									value="${currentRow.SERVICETOTALCHEQUEAMOUNT}" />
								<input type="hidden" name="totalCardAmountTempArray"
									id="totalCardAmountTempArray"
									value="${currentRow.SERVICETOTALCARDPAYMENTAMOUNT}" />
								<input type="hidden" name="totalOnlineAmountTempArray"
									id="totalOnlineAmountTempArray"
									value="${currentRow.SERVICETOTALONLINEPAYMENTAMOUNT}" />
								<input type="hidden" name="receiptDateTempArray"
									id="receiptDateTempArray" value="${currentRow.RECEIPTDATE}" />
								<input type="hidden" name="serviceNameArray"
									id="serviceNameArray" />
								<input type="hidden" name="fundCodeArray" id="fundCodeArray" />
								<input type="hidden" name="departmentCodeArray"
									id="departmentCodeArray" />
								<input type="hidden" name="totalCashAmountArray"
									id="totalCashAmountArray" />
								<input type="hidden" name="totalChequeAmountArray"
									id="totalChequeAmountArray" />
								<input type="hidden" name="totalCardAmountArray"
									id="totalCardAmountArray" />
								<input type="hidden" name="totalOnlineAmountArray"
									id="totalOnlineAmountArray" />
								<input type="hidden" name="receiptDateArray"
									id="receiptDateArray" />

							</display:column>

							<display:column headerClass="bluebgheadtd"
								class="blueborderfortd" title="Date"
								style="width:10%;text-align: center"
								value="${currentRow.RECEIPTDATE}" format="{0,date,dd/MM/yyyy}" />
							<display:column headerClass="bluebgheadtd"
								class="blueborderfortd" title="Service Name"
								style="width:20%;text-align: center"
								value="${currentRow.SERVICENAME}" />

							<display:column headerClass="bluebgheadtd"
								class="blueborderfortd" title="Fund"
								style="width:10%;text-align: center"
								value="${currentRow.FUNDNAME}" />

							<display:column headerClass="bluebgheadtd"
								class="blueborderfortd" title="Department"
								style="width:10%;text-align: center"
								value="${currentRow.DEPARTMENTNAME}" />

							<display:column headerClass="bluebgheadtd"
								class="blueborderfortd" title="Total Cash Collection"
								style="width:10%;text-align: center">
								<div align="center">
									<c:if test="${not empty currentRow.SERVICETOTALCASHAMOUNT}">
										<c:out value="${currentRow.SERVICETOTALCASHAMOUNT}" />
									</c:if>
									&nbsp;
								</div>
							</display:column>

							<display:column headerClass="bluebgheadtd"
								class="blueborderfortd" title="Total Cheque Collection"
								style="width:10%;text-align: center">
								<div align="center">
									<c:if test="${not empty currentRow.SERVICETOTALCHEQUEAMOUNT}">
										<c:out value="${currentRow.SERVICETOTALCHEQUEAMOUNT}" />
									</c:if>
									&nbsp;
								</div>
							</display:column>
							<s:if test="showCardAndOnlineColumn">
							<display:column headerClass="bluebgheadtd"
								class="blueborderfortd" title="Total Card Collection"
								style="width:10%;text-align: center">
								<div align="center">
									<c:if
										test="${not empty currentRow.SERVICETOTALCARDPAYMENTAMOUNT && showCardAndOnlineColumn}">
										<c:out value="${currentRow.SERVICETOTALCARDPAYMENTAMOUNT}" />
									</c:if>
									&nbsp;
								</div>
							</display:column>
							<display:column headerClass="bluebgheadtd"
								class="blueborderfortd" title="Total Online Collection"
								style="width:10%;text-align: center">
								<div align="center">
									<c:if
										test="${not empty currentRow.SERVICETOTALONLINEPAYMENTAMOUNT}">
										<c:out value="${currentRow.SERVICETOTALONLINEPAYMENTAMOUNT}" />
									</c:if>
									&nbsp;
								</div>
							</display:column>
							</s:if>
						</display:table>
					</div>
					<br />
					<div id="loadingMask"
						style="display: none; overflow: hidden; text-align: center">
						<img src="/collection/resources/images/bar_loader.gif" alt=""/> <span
							style="color: red">Please wait....</span>
					</div>
					<s:if test="showRemittanceDate">
					<div align="center">
					<table>
					<tr>
					<td class="bluebox" colspan="7"> &nbsp;</td>
					<td class="bluebox" ><s:text name="bankremittance.remittancetdate"/><span class="mandatory"/></td>
					<td class="bluebox"><s:textfield id="remittanceDate" name="remittanceDate" value="%{remittanceDate}" readonly="true" data-inputmask="'mask': 'd/m/y'"  onfocus = "waterMarkTextIn('remittanceDate','DD/MM/YYYY');"/><div>(DD/MM/YYYY)</div></td>
					</tr>
					</table>
					</div>
					</s:if>
					<div align="left" class="mandatorycoll">
						<s:text name="common.mandatoryfields" />
					</div>
					<div class="buttonbottom">
						<input name="button32" type="button" class="buttonsubmit"
							id="button32" value="Remit to Bank" onclick="return validate()" />
						&nbsp; <input name="buttonClose" type="button" class="button"
							id="button" value="Close" onclick="window.close()" />
					</div>
					</logic:notEmpty>
					<s:if test="%{isListData}">
					<logic:empty name="paramList">
						<div class="formmainbox">
							<table width="90%" border="0" align="center" cellpadding="0"
								cellspacing="0">
								<tr>
									<div>&nbsp;</div>
									<div class="billhead2">
										<b><s:text name="bankRemittance.norecordfound" /></b>
									</div>
								</tr>
							</table>
							<br />
						</div>
						<div class="buttonbottom">
							<input name="buttonClose" type="button" class="button"
								id="buttonClose" value="Close" onclick="window.close()" />
						</div>
					</logic:empty>
					</s:if>
		</div>
		</s:push>
	</s:form>
</body>
</html>
