<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>


<%@ include file="/includes/taglibs.jsp"%>

<%@ page language="java"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="dishonoredCheq.title" /></title>
<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true" />
<script type="text/javascript" src="/EGF/resources/javascript/helper.js?rnd=${app_release_no}"></script>
<script type="text/javascript">


  function getAccountNumbers(elem){
	  bank_branch_id=elem.options[elem.selectedIndex].value;
      populateaccountCodes({bankBranchId:bank_branch_id});
  }


  function validateFormAndSubmit(){
	var accountType = dom.get("instrumentMode").value;
	var accountDDNo = dom.get("chequeNo").value;
	var accountDDDate  = dom.get("chqDDDate").value;
	dom.get("errorDiv").innerHTML='';
	 if (accountType == 0){
		dom.get("errorDiv").style.display='';
		dom.get("errorDiv").innerHTML='Please enter Instrument mode.';
		return false;
	 }
	else if (accountDDNo == ""){
		dom.get("errorDiv").style.display='';
		dom.get("errorDiv").innerHTML='Please enter Cheque/DD Number.';
		return false;
	 }
	else if (accountDDDate == ""){
		dom.get("errorDiv").style.display='';
		dom.get("errorDiv").innerHTML='Please enter Cheque/DD Date.';
		return false;
	 }else{
		dom.get("errorDiv").style.display='none';
		dom.get("errorDiv").innerHTML='';
	}
	document.dishonorForm.action='${pageContext.request.contextPath}/brs/dishonoredCheque-list.action';
	document.dishonorForm.submit();
}

function checkSelectedChqs(obj){ 
	
}

function dishonorSelectedChq() {
	var value = new Array();
	var dishonorcheck = document.getElementsByName("selectedCheque");
	var receiptHdrId = document.getElementsByName("receiptHeaderId");
	var instrmntHdrId = document.getElementsByName("instrumentHeaderId");
	var len=dishonorcheck.length;
	var i=0,x=0;
	var flag="";
	var receiptId="";
	var instrmndId="";
	if(len >0){
		for (i = 0; i < len; i++){
			if(dishonorcheck[i].checked){
				receiptId=receiptHdrId[i].value;
				instrmndId=instrmntHdrId[i].value;
				break;
			}
		}
		if(receiptId!=""){
			value[x++]=instrmndId;
			for (var j = i+1; j < len; j++){
				if(dishonorcheck[j].checked){
					if(receiptId==receiptHdrId[j].value){
						value[x++]=instrmntHdrId[j].value
						flag=true;
					}
					else if(receiptId!=receiptHdrId[j].value){
						flag=false;
						break;	
					}
									
				}
			}

			if(flag=="false" && flag!=""){
				bootbox.alert("Please Choose only cheques from the same Receipt No.");
				return false;
			}

		}
	}
	if(value.length==0){
		bootbox.alert("Please Choose Cheques to Dishonor.");
		return false;
	}

	if((flag=="true" || flag=="") && value.length>0){
		document.dishonorForm.action='${pageContext.request.contextPath}/brs/dishonoredCheque-dishonorCheque.action?installmentIds='+value;
		document.dishonorForm.submit();
	}
}
function resetForm()
{
	
	dom.get("bankBranchId").value=-1;
	dom.get("accountCodes").value=-1;
	dom.get("instrumentMode").value=0;
	dom.get("chequeNo").value="";
	dom.get("chqDDDate").value="";
}
   
  </script>
</head>
<body>
	<s:form name="dishonorForm" action="search" theme="css_xhtml"
		validate="true">
		<div style="color: green">
			<s:actionmessage />
		</div>
		<div style="color: red">
			<s:actionerror />
		</div>
		<div style="color: red">
			<div class="errorstyle" style="display: none" id="errorDiv"></div>
		</div>
		<div class="formmainbox">
			<div class="formheading">
				<div class="subheadnew">
					<s:text name="dishonoredCheq.title" />
				</div>
			</div>
			<br />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="greybox"></td>
					<td class="greybox"><s:text name="dishnrChq.bankBranch.title" />:</td>
					<td class="greybox"><s:select name="bankBranchId"
							id="bankBranchId" list="dropdownData.bankBranchList"
							headerKey="-1" headerValue="---Choose---"
							listKey="bank.id + '-' + id"
							listValue="bank.name + ' ' + branchname"
							value="%{bank.id + '-' + id}" onchange="getAccountNumbers(this);" />
						<egov:ajaxdropdown id="populateAccount" fields="['Text','Value']"
							dropdownId='accountCodes'
							url='brs/ajaxDishonored-populateAccountCodes.action'
							selectedValue="%{bank.id + '-' + id}" /></td>

					<td class="greybox"><s:text name="dishnrChq.account.title" />:</td>
					<td class="greybox"><s:select headerKey="-1"
							headerValue="---Choose---" name="accountCodes" id="accountCodes"
							list="dropdownData.accountCodeList" listKey="id" listValue='name'
							value="%{accountCodes}" /></td>
				</tr>

				<tr>
					<td class="bluebox"></td>
					<td class="bluebox"><s:text
							name="dishnrChq.instrmntMode.title" />:<span class="mandatory1">*</span></td>
					<td class="bluebox"><SELECT id="instrumentMode"
						name="instrumentMode">
							<option value="0" selected>---Choose---</option>
							<option value="dd">DD</option>
							<option value="cheque">Cheque</option>
					</SELECT></td>
				</tr>

				<tr>
					<td class="greybox"></td>
					<td class="greybox"><s:text name="dishnrChq.chqDDNo.title" />:<span
						class="mandatory1">*</span></td>
					<td class="greybox"><s:textfield name="chequeNo"
							style="width: 200px;" id="chequeNo" value="%{chequeNo}" /></td>

					<td class="greybox"><s:text name="dishnrChq.chqDDDate.title" />:<span
						class="mandatory1">*</span></td>
					<td class="greybox"><s:date name="chqDDDate" var="chqDDDate"
							format="dd/MM/yyyy" /> <s:textfield name="chqDDDate"
							value="%{chqDDDate}" id="chqDDDate" cssClass="selectboldwk"
							onfocus="javascript:vDateType='3';" maxlength="10"
							disabled="%{fDisabled}"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
						href="javascript:show_calendar('forms[0].chqDDDate',null,null,'DD/MM/YYYY');"
						onmouseover="window.status='Date Picker';return true;"
						onmouseout="window.status='';return true;"> <img
							src="/egi/resources/erp2/images/calendar.png" alt="Calendar"
							width="16" height="16" border="0" align="absmiddle" /></a></td>
				</tr>
			</table>
		</div>
		<br />
		<div class="buttonbottom">
			<table align="center">
				<tr class="buttonbottom" id="buttondiv" style="align: middle">
					<td><input type="submit" class="buttonsubmit" value="VIEW"
						id="searchButton" onclick="return validateFormAndSubmit();" />&nbsp;</td>
					<td><input type="button" class="button" value="RESET"
						id="resetbutton" name="clear" onclick="resetForm();">&nbsp;</td>
					<td><input type="button" class="button" value="CLOSE"
						id="closeButton" name="closeButton" onclick="window.close();" /></td>
				</tr>
			</table>
		</div>

		<s:if test="%{searchResult.fullListSize != 0}">
			<table align="center">
				<tr>
					<td colspan="4">
						<div class="tbl2-container" id="tbl-container">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td><display:table name="searchResult"
											id="currentRowObject" uid="currentRowObject" pagesize="30"
											class="tablebottom" style="width:100%;" cellpadding="0"
											cellspacing="0" requestURI="">

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Sl.No"
												style="width:8%;text-align:left">
												<s:property
													value="#attr.currentRowObject_rowNum + (page-1)*pageSize" />
												<s:hidden name="receiptHeaderId" id="receiptHeaderId"
													value="%{#attr.currentRowObject.receiptHeaderid}" />
												<s:hidden name="instrumentHeaderId" id="instrumentHeaderId"
													value="%{#attr.currentRowObject.instrumentHeaderid}" />
											</display:column>

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Receipt Number"
												style="width:30%;text-align:left" property="receiptNumber" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Receipt Date"
												style="width:15%;text-align:left" property="receiptDate" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="DD/Chq Number"
												style="width:15%;text-align:left"
												property="instrumentNumber" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="DD/Chq Date"
												style="width:15%;text-align:center"
												property="instrumentDate" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="DD/Chq Amount"
												style="width:25%;text-align:right"
												property="instrumentAmount" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Bank"
												style="width:30%;text-align:left" property="bankName" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Bank Account Number"
												style="width:20%;text-align:left" property="accountNumber" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Pay to"
												style="width:25%;text-align:left" property="payTo" />

											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Status"
												style="width:20%;text-align:center" property="description" />


											<display:column headerClass="bluebgheadtd"
												class="blueborderfortd" title="Select"
												style="width:3%;text-align:center">
												<s:checkbox id="selectedCheque" name="selectedCheque" />
											</display:column>
										</display:table></td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
			<div class="buttonholderwk" align="center">
				<input type="submit" class="button" value="DISHONOR"
					id="dishonorButton" name="button"
					onclick="return dishonorSelectedChq();" />
			</div>
		</s:if>
		<s:elseif test="%{searchResult.fullListSize == 0}">
			<font color="red">No record Found.</font>
			</td>
		</s:elseif>
	</s:form>
</body>
</html>