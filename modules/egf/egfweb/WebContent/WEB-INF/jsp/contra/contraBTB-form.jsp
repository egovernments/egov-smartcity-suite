	<%@include file="../voucher/vouchertrans-filter-new.jsp"%>
		<tr>
		<td class="bluebox" colspan="6">
		<table width="100%"  cellspacing="0" cellpadding="0" border="0" >
		<tr>
		<th  class="bluebgheadtd" width="52%" colspan="2"><STRONG><s:text name="contra.fromBank.header"/></STRONG></th>
		<th  class="bluebgheadtd" width="10%"></th>
		<th class="bluebgheadtd" width="38%" colspan="2"><STRONG><s:text name="contra.toBank.header"/></STRONG></th>
		</tr>
		</table>
		</td>
		</tr>
		<tr>
		<td class="greybox"></td>
		<egov:ajaxdropdown id="fromBankId" fields="['Text','Value']" dropdownId="fromBankId" url="/voucher/common!ajaxLoadBanks.action" />
		  <td class="greybox"><s:text name="contra.fromBank"/>
		  <span class="greybox"><span class="mandatory">*</span></span></td>
		  <s:hidden name="temp" value="contraBean.fromBankId" />
		  <td class="greybox"><s:select name="contraBean.fromBankId"   id="fromBankId" list="%{fromBankBranchMap}"  headerKey="-1" headerValue="----Choose----" onChange="loadFromAccNum(this);"  /></td>
		  <egov:ajaxdropdown id="fromAccountNumber" fields="['Text','Value']" dropdownId="fromAccountNumber" url="/voucher/common!ajaxLoadAccountNumbers.action" />
		
		<egov:ajaxdropdown id="toBankId" fields="['Text','Value']" dropdownId="toBankId" url="/voucher/common!ajaxLoadBanks.action" />
		  <td class="greybox"><s:text name="contra.toBank"/>
		  <span class="greybox"><span class="mandatory">*</span></span></td>
		  <td class="greybox"><s:select name="contraBean.toBankId"   id="toBankId" list="%{toBankBranchMap}"  headerKey="-1" headerValue="----Choose----" onChange="loadToAccNum(this);"  /></td>
		  <egov:ajaxdropdown id="toAccountNumber" fields="['Text','Value']" dropdownId="toAccountNumber" url="/voucher/common!ajaxLoadAccountNumbers.action" />
 		</tr>
	
	<tr>
	<td class="bluebox"></td>
	<td class="bluebox"><s:text name="contra.fromBankAccount"/>
		<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox"><s:select  name="contraBean.fromBankAccountId" value="%{contraBean.fromBankAccountId}" id="fromAccountNumber" list="dropdownData.fromAccNumList" listKey="id" listValue="accountnumber" headerKey="-1" headerValue="----Choose----" onChange="populatefromNarration(this);loadFromBalance(this)"/>
		<s:textfield name="fromAccnumnar" id="fromAccnumnar" value="%{fromAccnumnar}" readonly="true" tabindex="-1" />
		</td>
		<td class="bluebox"><s:text name="contra.toBankAccount"/>
		<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox"><s:select  name="contraBean.toBankAccountId"  id="toAccountNumber" list="dropdownData.toAccNumList" listKey="id" listValue="accountnumber" headerKey="-1" headerValue="----Choose----" onChange="populatetoNarration(this);loadToBalance(this)"/>
		<s:textfield name="toAccnumnar" id="toAccnumnar" value="%{toAccnumnar}" readonly="true" tabindex="-1"/>
		</td>
	</tr>
	<tr>
	<td class="greybox"></td>
	<egov:updatevalues id="fromBankBalance" fields="['Text']" url="/payment/payment!ajaxGetAccountBalance.action"/>
	<td class="greybox"><s:text name="contra.fromBankBalance"/> (Rs.)
		<span class="greybox"><span class="mandatory">*</span></span></td>
		<td class="greybox" >
		<s:textfield name="contraBean.fromBankBalance" id="fromBankBalance"  readonly="true" tabindex="-1" cssStyle="text-align:right"  />
		</td>
		<egov:updatevalues id="toBankBalance" fields="['Text']" url="/payment/payment!ajaxGetAccountBalance.action"/>
		<td class="greybox"><s:text name="contra.toBankBalance"/>  (Rs.)
		<span class="greybox"><span class="mandatory">*</span></span></td>
		<td class="greybox" >
		<s:textfield name="contraBean.toBankBalance" id="toBankBalance"  readonly="true" tabindex="-1" cssStyle="text-align:right" />
		</td>
	</tr>
		
<tr>
<td class="bluebox"></td>
	<td class="bluebox"><s:text name="contra.modeOfCollection"/>
		<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox">
		<s:radio name="contraBean.modeOfCollection" id="modeOfCollection" list="%{modeOfCollectionMap}" onclick="toggleChequeAndRefNumber(this)"/>
		</td>
		<td></td>
		<td></td>
	</tr>
		
<div id="chequeGrid" >
	<td class="greybox"></td>
	<td class="greybox"><span id="mdcNumber"><s:text name="contra.chequeNumber" /></span>
		<span class="greybox"><span class="mandatory">*</span></span></td>
		<td class="greybox" ><s:textfield  name="contraBean.chequeNumber" id="chequeNum"  value="%{contraBean.chequeNumber}"/>	</td>
		<td class="greybox" ><span id="mdcDate"><s:text name="contra.chequeDate" /></span></td>
			<td class="greybox"  ><s:textfield name="contraBean.chequeDate" id="chequeDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
					<a href="javascript:show_calendar('cbtbform.chequeDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				
	</div>	

	<tr>
				<td class="bluebox"></td>
		<td class="bluebox"><s:text name="contra.amount"/> (Rs.)
		<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox" ><s:textfield  name="amount" id="amount" cssStyle="text-align:right" />
		</td>
		</tr>
		
		<tr>
		<td class="greybox"></td>
		<td class="greybox"><s:text name="voucher.narration"/></td>
				<td class="greybox" colspan="3"><s:textarea name="description" id="description"  style="width:580px"/></td>
		</tr>	
		
		