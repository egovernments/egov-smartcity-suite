	<jsp:include page="../voucher/vouchertrans-filter-new.jsp"/>
	<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<tr>
		<td  class="greybox"></td>
		<td class="greybox"><s:text name="bank"/>
		<span class="greybox"><span class="mandatory">*</span></span></td>
		<egov:ajaxdropdown id="bankId" fields="['Text','Value']" dropdownId="bankId" url="/voucher/common!ajaxLoadBanksByFundAndType.action" />
		<td class="greybox"><s:select name="commonBean.bankId" id="bankId" list="dropdownData.bankList" listKey="bankBranchId" listValue="bankBranchName" headerKey="" headerValue="----Choose----" onChange="populateAccNum(this);"  /></td>
	 	<td class="greybox" ><s:text name="amount"/><span class="mandatory">*</span></td>
		<td class="greybox"><s:textfield  name="commonBean.amount" id="amount"  maxlength="18"  onblur="validateDigitsAndDecimal(this);" cssStyle="text-align:right"/></td>
	</tr>
	
	<tr>
		<td  class="bluebox" width="10%"></td>
		<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']" dropdownId="accountNumber" url="voucher/common!ajaxLoadBankAccounts.action" />
		<td class="bluebox"  width="22%"><s:text name="account.number"/><span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox" width="22%"><s:select  name="commonBean.accountNumberId" id="accountNumber" list="dropdownData.accNumList" listKey="id" listValue="accountnumber+'-'+accounttype" headerKey="" headerValue="----Choose----" onChange="populateNarration(this);populateAvailableBalance(this);" />
		<s:textfield name="accnumnar" id="accnumnar" value="%{commonBean.accnumnar}" readonly="true" tabindex="-1"/></td>
		<egov:updatevalues id="availableBalance" fields="['Text']" url="/payment/payment!ajaxGetAccountBalance.action"/>
		<td class="bluebox" id="balanceText" style="display:none" width="18%"><s:text name="balance.available"/></td>
		<td class="bluebox" id="balanceAvl"  style="display:none" width="32%"><s:textfield name="commonBean.availableBalance" id="availableBalance" readonly="readonly" style="text-align:right" value="%{commonBean.availableBalance}"/></td>
              
		
	</tr>
	<td class="greybox"></td>
	<td class="greybox"><s:text name="modeofpayment"/>
		<span class="greybox"><span class="mandatory">*</span></span></td>
		<td class="greybox">
		<s:radio name="commonBean.modeOfPayment" id="modeOfPayment" list="%{modeOfPaymentMap}" />
		</td>
		<td class="greybox"><s:text name="paidto"/><span class="mandatory">*</span></td>
		<td class="greybox"><s:textfield name="commonBean.paidTo" id="paidTo" maxlength="250"/> </td>
	</tr>
	<tr>
	<td class="bluebox"></td>
	<td class="bluebox"><s:text name="document.number"/><span class="bluebox"><span class="mandatory">*</span></span></td>
	<td class="bluebox"><s:textfield name="commonBean.documentNumber" id="commonBean.documentNumber"/> </td>
	<td class="bluebox"><s:text name="document.date"/><span class="bluebox"><span class="mandatory">*</span></span></td>
	<s:date name='commonBean.documentDate' id="commonBean.documentDateId" format='dd/MM/yyyy'/>
	<td  class="bluebox"><s:textfield name="commonBean.documentDate"  id="documentDate" onkeyup="DateFormat(this,this.value,event,false,'3')" value="%{commonBean.documentDateId}"/>
	<a href="javascript:show_calendar('dbpform.documentDate');"	style="text-decoration: none">&nbsp;<img tabIndex="-1"
										src="${pageContext.request.contextPath}/image/calendaricon.gif"		border="0" /></A>
	</td>
	</tr>
	<s:if test="%{instrumentHeaderList.size()>0}">
	  <s:iterator var="p" value="instrumentHeaderList" status="s">  
		<tr >
			<td class="greybox"></td>
			<td class="greybox">Cheque Number</td>
			<td class="greybox">
				<s:property value="%{instrumentNumber}" />
			</td>
			<td class="greybox">Cheque Date</td>
			<td class="greybox">
				<s:date name="%{instrumentDate}" format="dd/MM/yyyy"/>
			</td>
		</tr>
		<tr>
			<td class="bluebox"></td>
			<td class="bluebox">Party Name</td>
			<td class="bluebox"> <s:property value="%{payTo}" /></td>
		</tr></s:iterator>
	</s:if>
	<tr>
		<td class="greybox"></td>
		<td class="greybox"><s:text name="voucher.narration"/></td>
				<td class="greybox" colspan="3"><s:textarea name="description" id="description"  style="width:580px"/></td>
		</tr>	
	</table>
	<div id="budgetSearchGrid">
      <div align="center">
	  <br>
        <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-right: 0px solid rgb(197, 197, 197);" class="tablebottom">
          <tbody><tr>
            <td colspan="6">
		<div id="labelAD" align="center">
	 		<div class="subheadsmallnew"><strong>Account Details</strong></div>
	</div>
	<div class="yui-skin-sam" align="center">
       <div id=billDetailTable></div>
     </div>
     <script>
	    makeVoucherDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="100%";
	 </script>
	 </td></tr></tbody></table></div></div>
	 
	<div id="codescontainer"></div>
	
	 <div id="labelSL" align="center">
	<div id="budgetSearchGrid">
      <div align="center">
	  <br>
        <table cellspacing="0" cellpadding="0" border="0" width="100%" style="border-right: 0px solid rgb(197, 197, 197);" class="tablebottom">
          <tbody><tr>
            <td colspan="6">
	<div class="subheadsmallnew"><strong>Sub-Ledger Details</strong></div>
	</div>
	
		<div class="yui-skin-sam" align="center">
	       <div id="subLedgerTable"></div>
	    
	   
		<script>
		makeSubLedgerTable();
		document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="100%"
		</script>
		 </td></tr></tbody></table></div></div></div>
 
	