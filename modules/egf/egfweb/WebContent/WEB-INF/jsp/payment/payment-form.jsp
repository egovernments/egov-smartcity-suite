<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ page import="org.egov.utils.FinancialConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<link href="/EGF/cssnew/budget.css" rel="stylesheet" type="text/css" />
<link href="/EGF/cssnew/commonegovnew.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/EGF/cssnew/tabber.css" TYPE="text/css">
<script type="text/javascript" src="/EGF/javascript/tabber.js"></script>
<script type="text/javascript" src="/EGF/javascript/tabber2.js"></script>
</head>
<script language="javascript">

</script>
</head>
<body><br>
	<s:form action="payment" theme="simple" >
		<jsp:include page="../budget/budgetHeader.jsp">
        	<jsp:param name="heading" value="Bill Payment" />
		</jsp:include>
		<span class="mandatory">
			<s:actionerror/>  
			<s:fielderror />
			<s:actionmessage />
		</span>
		<div class="formmainbox"><div class="subheadnew">Bill Payment</div>
		<div id="budgetSearchGrid" style="display:block;width:100%;border-top:1px solid #ccc;" >
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
			<td>
			<div align="left"><br/>
    			<table border="0" cellspacing="0" cellpadding="0" width="100%">
          		<tr>
          		<td> 
            		<div class="tabber">
            		<div class="tabbertab">
					<h2>Payment Details</h2>
					<span>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td align="center" colspan="6" class="serachbillhead">Payment Details</td>
						</tr>
  						<tr>
  							<td width="9%" class="bluebox"><s:hidden name="billregister.id"/></td>
							<s:if test="%{shouldShowHeaderField('fund')}">
								<td width="12%" class="bluebox"><strong><s:text name="voucher.fund"/></strong><s:if test="%{isFieldMandatory('fund')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
								<td width="20%" class="bluebox"><s:property value="%{billregister.egBillregistermis.fund.name}"/></td>
							</s:if>
							<s:if test="%{shouldShowHeaderField('fundsource')}">
								<td width="17%" class="bluebox"><strong><s:text name="voucher.fundsource"/></strong><s:if test="%{isFieldMandatory('fundsource')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
								<td width="33%" class="bluebox"><s:property value="%{billregister.egBillregistermis.fundsource.name}"/></td>
							</s:if>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<s:if test="%{shouldShowHeaderField('department')}">
								<td class="greybox"><strong><s:text name="voucher.department"/></strong><s:if test="%{isFieldMandatory('department')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
								<td class="greybox"><s:property value="%{billregister.egBillregistermis.egDepartment.deptName}"/></td>
							</s:if>
							<s:if test="%{shouldShowHeaderField('functionary')}">
								<td class="greybox"><strong><s:text name="voucher.functionary"/></strong><s:if test="%{isFieldMandatory('functionary')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
								<td class="greybox" colspan="2"><s:property value="%{billregister.egBillregistermis.functionaryid.name}"/></td>
							</s:if>
						</tr>
						<tr>
							<td class="bluebox">&nbsp;</td>
							<s:if test="%{shouldShowHeaderField('scheme')}">
								<td class="bluebox"><strong><s:text name="voucher.scheme"/></strong><s:if test="%{isFieldMandatory('scheme')}"><span class="mandatory">*</span></s:if></td>
								<td class="bluebox"><s:property value="%{billregister.egBillregistermis.scheme.name}"/></td>
							</s:if>
							<s:if test="%{shouldShowHeaderField('subscheme')}">
								<td class="bluebox"><strong><s:text name="voucher.subscheme"/></strong><s:if test="%{isFieldMandatory('subscheme')}"><span class="mandatory">*</span></s:if></td>
								<td class="bluebox"><s:property value="%{billregister.egBillregistermis.subScheme.name}"/></td>
							</s:if>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<s:if test="%{shouldShowHeaderField('field')}">
								<td class="greybox"><strong><s:text name="voucher.field"/></strong><s:if test="%{isFieldMandatory('field')}"><span class="mandatory">*</span></s:if></td>
								<td class="greybox" colspan="4"><s:property value="%{billregister.egBillregistermis.fieldid.name}"/></td>
							</s:if>
						</tr>
						<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox"><strong><s:text name="payment.mode"/></strong></td>
							<td class="bluebox">
								<s:if test="%{paymentMode == 'cash' || paymentMode == 'Cash'}"><s:text name="cash.consolidated.cheque"/></s:if>
								<s:else><s:text name="%{paymentMode}"/></s:else>
							</td>
							<td class="bluebox"><strong><s:text name="payment.amount"/></strong></td>
							<td class="bluebox" colspan="2"><span id="paymentAmountspan"/></td>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<s:if test="%{shouldShowHeaderField('vouchernumber')}">
								<td class="greybox"><s:text name="payment.voucherno"/><span class="mandatory">*</span></td>
								<td class="greybox"><s:textfield name="vouchernumber" id="vouchernumber" value="%{vouchernumber}"/></td>
							</s:if>
							<s:else>
								<td class="greybox"/>
								<td class="greybox"/>
							</s:else>
							<td class="greybox"><s:text name="payment.voucherdate"/><span class="mandatory">*</span></td>
							<td class="greybox" colspan="2"><s:textfield name="voucherdate" id="voucherdate" maxlength="20" value="%{voucherdate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].voucherdate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
						</tr>
						<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox"><s:text name="payment.bank"/><span class="mandatory">*</span></td>
							<td class="bluebox"><s:select name="bankbranch" id="bankbranch" list="dropdownData.bankbranchList" listKey="id" listValue="bank.name+'-'+branchname"  headerKey="-1" headerValue="----Choose----" onchange="loadBankAccount(this)" value="%{bankbranch}"/></td>
							<egov:ajaxdropdown id="bankaccount" fields="['Text','Value']" dropdownId="bankaccount" url="voucher/common!ajaxLoadBankAccounts.action"/>
							<td class="bluebox"><s:text name="payment.bankaccount"/><span class="mandatory">*</span></td>
							<td class="bluebox"  colspan="2"><s:select name="bankaccount" id="bankaccount" list="dropdownData.bankaccountList" listKey="id" listValue="accountnumber+'---'+accounttype"  headerKey="-1" headerValue="----Choose----" value="%{bankaccount}"/></td>
							<egov:updatevalues id="balance" fields="['Text']" url="payment/payment!ajaxGetAccountBalance.action"/>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<td class="greybox" width="15%"><s:text name="payment.narration"/></td>
							<td class="greybox" colspan="4"><s:textarea name="description" id="description" cols="70" rows="4"  onblur="checkLength(this)"/></td>
						</tr>
						
						<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox" style="visibility:hidden"><s:text name="payment.balance"/></td>
							<td class="bluebox"><s:hidden name="balance" id="balance" readonly="true"  style="text-align:right"/></td>
						</tr>
					  	<tr>
							<td colspan="6" align="center">
							<div class="buttonbottom"><!--
							 	<s:submit method="createPayment" value="Save " cssClass="buttonsubmit" onclick="return validate()"/> 
								<s:submit method="beforeSearch" value="Back " cssClass="buttonsubmit" />
								<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>  -->
								<s:hidden name="hiddenText" id="hiddenText"/>
								<s:hidden name="paymentMode" id="paymentMode" value="%{paymentMode}"/>
								<s:hidden name="contractorIds" id="contractorIds" value="%{contractorIds}"/>
								<s:hidden name="supplierIds" id="supplierIds" value="%{supplierIds}"/>
								<s:hidden name="contingentIds" id="contingentIds" value="%{contingentIds}"/>
								<s:hidden name="salaryIds" id="salaryIds" value="%{salaryIds}"/>
								<s:hidden name="pensionIds" id="pensionIds" value="%{pensionIds}"/>
								<s:hidden name="pensionCommissionRate" id="pensionCommissionRate" value="%{pensionCommissionRate}"/>
							</div>
							</td>
					  	</tr>
					  	</table>
                  	</span> 
					</div>
            		<div class="tabbertab">
                  	<h2>Bill Details</h2>
	                <span>
						<table align="center" border="0" cellpadding="0" cellspacing="0" class="newtable">
						<tr><td colspan="7"><div class="subheadsmallnew">Bill Details</div></td></tr>
						<tr>
						<td colspan="7">
							<div  style="float:left; width:100%;">
							<table id="billdetailsTable" align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr>  
								    <th class="bluebgheadtdnew">Bill Number</td>
								    <th class="bluebgheadtdnew">Bill Date</td>  
								    <th class="bluebgheadtdnew">Payee Name</td>
								    <th class="bluebgheadtdnew">Net Amount</td>
								    <th class="bluebgheadtdnew">Earlier Payment</td> 
								    <th class="bluebgheadtdnew">Payable Amount</td> 
								    <th class="bluebgheadtdnew">Payment Amount</td> 
								</tr>
								<s:if test="%{billList.size>0}">
									<s:iterator var="p" value="billList" status="s">  
										<tr>
											<td style="text-align:center"  class="blueborderfortdnew"><s:hidden name="billList[%{#s.index}].csBillId" id="csBillId%{#s.index}" value="%{csBillId}"/><s:hidden name="billList[%{#s.index}].billNumber" id="billNumber" value="%{billNumber}"/><s:property value="%{billNumber}" /></td>
						      				<td style="text-align:center" class="blueborderfortdnew"><s:hidden name="billList[%{#s.index}].billDate" id="billDate%{#s.index}" value="%{billDate}"/><s:date name="%{billDate}" format="dd/MM/yyyy"/></td>
						      				<td style="text-align:center"  class="blueborderfortdnew"><s:hidden name="billList[%{#s.index}].expType" id="expType%{#s.index}" value="%{expType}"/><s:hidden name="billList[%{#s.index}].payTo" id="payTo%{#s.index}" value="%{payTo}"/><s:property value="%{payTo}" /></td>
									     	<td style="text-align:right" class="blueborderfortdnew"><s:hidden name="billList[%{#s.index}].netAmt" id="netAmt%{#s.index}" value="%{netAmt}"/><s:text name="payment.format.number" ><s:param value="%{netAmt}"/></s:text></td>
									        <td style="text-align:right" class="blueborderfortdnew"><s:hidden name="billList[%{#s.index}].earlierPaymentAmt" id="earlierPaymentAmt%{#s.index}" value="%{earlierPaymentAmt}"/><s:text name="payment.format.number" ><s:param value="%{earlierPaymentAmt}"/></s:text></td>
									        <td style="text-align:right" class="blueborderfortdnew"><s:hidden name="billList[%{#s.index}].payableAmt" id="payableAmt%{#s.index}" value="%{payableAmt}"/><s:text name="payment.format.number" ><s:param value="%{payableAmt}"/></s:text></td>
									        <s:if test="%{expType == finConstExpendTypeContingency || expType == finConstExpendTypePension}">
												
												<td class="blueborderfortdnew"><div align="center">
												<input type="text" name='billList[<s:property value="%{#s.index}"/>].paymentAmt' value='<s:text name="payment.format.number" ><s:param value="%{paymentAmt}"/></s:text>' id='paymentAmt<s:property value="%{#s.index}"/>' style="text-align:right" readonly/>
												<!-- <s:textfield name="billList[%{#s.index}].paymentAmt" id="paymentAmt%{#s.index}" value="%{getText('payment.format.number',{'paymentAmt'})}" style="text-align:right" readonly="true"/> --> </div></td>
										   </s:if>
									        <s:else>
												<td class="blueborderfortdnew"><div align="center">
												<input type="text" name='billList[<s:property value="%{#s.index}"/>].paymentAmt' value='<s:text name="payment.format.number" ><s:param value="%{paymentAmt}"/></s:text>' id='paymentAmt<s:property value="%{#s.index}"/>' style="text-align:right"  onchange="calcGrandTotal(this)" onfocus="updateHidden(this)" />
												<!-- <s:textfield name="billList[%{#s.index}].paymentAmt" id="paymentAmt%{#s.index}" value="%{paymentAmt}" style="text-align:right" onchange="calcGrandTotal(this)" onfocus="updateHidden(this)"/> --></div></td>
									        </s:else>
									        <c:set var="totalAmt" value="${totalAmt+paymentAmt}"/>
										</tr>
									</s:iterator>
								</s:if>

								<tr>
									<td style="text-align:right" colspan="6" class="blueborderfortdnew"><strong>Grand Total</strong></td>
									<td style="text-align:right" class="blueborderfortdnew"><div align="center"><input type="text" name="grandTotal" id="grandTotal" value='<fmt:formatNumber value='${totalAmt}' pattern='#0.00' />' style="text-align:right" readonly/></div></td>
									<s:hidden name="billListSize" id="billListSize" value="%{billList.size}"/>
								</tr>
							</table>
							</div>
						</td>
						</tr>
						</table>                    
					</span>                  
	                </div>			 <!-- individual tab -->
	                <s:if test="%{enablePensionType}">
	                <div>
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="tablebottom">
							<tr>
								<th class="bluebgheadtd">
									<span class="mandatory">*</span><s:text name="commission.account.code" />
								</th>
								<th class="bluebgheadtd">
									<span class="mandatory">*</span><s:text name="commission.account.description" />
								</th>
								<th class="bluebgheadtd">
									<span class="mandatory">*</span><s:text name="commission.account.amount" />
								</th>
							</tr>
							
							<tr>
								<!-- <td class="greybox" ><s:textfield name="glCode" id="glCode" onkeyup="autocompletecodeFunctionHeader(this,event)" onblur="fillNeibrAfterSplitFunctionHeader(this)"  size="30"/>
								 <s:hidden name="glCodeId" id="glCodeId" /></td> -->
								 
								 
								<td class="greybox">
									
									<div class="yui-skin-sam">
										<div id="glCode_autocomplete">
											<div>
												<s:textfield id="glCode" name="glCode" value="%{glCode}" onblur="setGlCode(this);" />
												<s:hidden id="glCodeId" name="glCodeId" value="%{glCodeId}" />
											</div>
										<span id="glCodeResults"></span>
										</div>
									</div>
									<egov:autocomplete name="glCode" width="20" maxResults="10" field="glCode" 
									url="${pageContext.request.contextPath}/payment/payment!getGlCodes.action?"
									queryQuestionMark="false" results="glCodeResults" forceSelectionHandler="glCodeforceHandler" queryLength="3"/>
								  	
								</td>
								 
							    <td  class="bluebox">
							    		<input type="hidden" name="glCodeName" id="glCodeName"/>
										<input type="text" name="glCodeNameDisplay" id="glCodeNameDisplay" disabled/>
								</td>
							    <td  class="bluebox">
										<input type="text" name="glCodeAmount" id="glCodeAmount" onblur="setPaymentAmount();"/>
								</td>
							</tr>
						</table>
	                </div>
	                </s:if>	
	                <s:if test="%{disableExpenditureType}">
	                <div>
	                <s:text name="change.party.name"/> <s:checkbox name="changePartyName" id="changePartyName" checked="checked"/>
	                <s:textfield name="newPartyName" id="newPartyName" />
	                </div>
	                </s:if>	
			     </div> <!-- tabbber div -->
			</td>
          	</tr>
        	</table>
		</div>
		</td>
		</tr>
		</table>
		<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
				<input type="hidden" name="scriptName" id="scriptName" value="paymentHeader.nextDesg"/>
				<%@include file="../voucher/workflowApproval.jsp"%>
		</s:if>
	</div>
	</div>
	
	<div  class="buttonbottom" id="buttondiv">
		<s:hidden  name="paymentid" value="%{paymentheader.id}"/>
		<s:hidden  name="actionname" id="actionName" value="%{action}"/>
		<s:iterator value="%{getValidActions()}" var="p"  status="s">
		  <s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="wfBtn%{#s.index}" name="%{name}" method="createPayment" onclick="return validate('%{name}','%{description}')"/>
		</s:iterator>
		<s:if test="%{disableExpenditureType==true}">
			<input type="button" method="beforeSearch" value="Back " class="buttonsubmit" id="backbtnid" onclick="return back();"/>
		</s:if>
		<s:else>
		<s:submit method="beforeSearch" value="Back " cssClass="buttonsubmit" id="backbtnid" onclick="return back();"/>
		</s:else>
		<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
	</div>
	<div class="subheadsmallnew"></div>
	<script>

		function setGlCode(obj)
		{		
				var coa = obj.value.split("~");
				var grandtotal = document.getElementById('grandTotal').value;
				var commissionRate = document.getElementById('pensionCommissionRate').value
				var commissionAmountNR = grandtotal*(commissionRate/100);
				var commissionAmount = Math.round(commissionAmountNR*100)/100;
				document.getElementById("paymentAmountspan").innerHTML = ((grandtotal*1)+commissionAmount).toFixed(2);  
			    document.getElementById("glCode").value = coa[0];
			    document.getElementById("glCodeId").value= coa[2];
			    document.getElementById("glCodeName").value= coa[1];
			    document.getElementById("glCodeNameDisplay").value= coa[1];
			    document.getElementById("glCodeAmount").value= commissionAmount;
		}

		function setPaymentAmount()
		{		
				var grandtotal = document.getElementById('grandTotal').value
				var commissionAmount = document.getElementById("glCodeAmount").value;
				document.getElementById("paymentAmountspan").innerHTML = ((grandtotal*1)+(commissionAmount*1)).toFixed(2);  
		}

		var glCodeHandler = function(sType, arguments) {  
		    var oData = arguments[2];
		    dom.get("glCode").value=oData[0];
		    dom.get("glCodeId").value = oData[1];
		};

		var glCodeforceHandler = function(sType, arguments) {
			document.getElementById("glCode").value = "";
			document.getElementById("glCodeId").value = "";
		}

		function back(){
			window.location = "/EGF/payment/payment!beforeSearch.action?salaryType";
			return true;
		}
		
		var vFixedDecimal = 2;
		function loadBankAccount(obj)
		{
			var fund = 0;
			<s:if test="%{shouldShowHeaderField('fund')}">
				fund = <s:property value="%{billregister.egBillregistermis.fund.id}"/>;
			</s:if>
			var vTypeOfAccount = '<s:property value="%{typeOfAccount}"/>';
			populatebankaccount({branchId:obj.options[obj.selectedIndex].value+'&date='+new Date(), typeOfAccount:vTypeOfAccount,fundId:fund} );
			//populatebankaccount({branchId:obj.options[obj.selectedIndex].value+'&date='+new Date()});
		}
		function updateHidden(obj)
		{
			if(obj.value=='' || isNaN(obj.value))
				document.getElementById('hiddenText').value=0;
			else
				document.getElementById('hiddenText').value=obj.value;
		}
		
		function calcGrandTotal(obj)
		{
			var vBillListSize = document.getElementById('billListSize').value;
			var index = obj.id.substring(10,obj.id.length);
			var putBackAmount = parseFloat(document.getElementById('payableAmt'+index).value);
			var paymentAmount = obj.value;
			if(paymentAmount == '' || isNaN(paymentAmount)) {
				alert('Payment amount should be a numeric value.');
				obj.value = putBackAmount.toFixed(vFixedDecimal);
			}
			
			if(paymentAmount > parseFloat(document.getElementById('payableAmt'+index).value) ) {
				alert('Payment amount should not be greater than Payable amount');
				obj.value = putBackAmount.toFixed(vFixedDecimal);
			}
			
			paymentAmount = obj.value;
			var vFinalGrandTotal = 0;
			obj.value = parseFloat(paymentAmount).toFixed(vFixedDecimal);
			for(var i = 0; i < vBillListSize; i++) {
				if(index == i) vFinalGrandTotal += parseFloat(paymentAmount);
				else vFinalGrandTotal += parseFloat(document.getElementById('paymentAmt'+i).value);
			}
			document.getElementById('grandTotal').value = vFinalGrandTotal.toFixed(vFixedDecimal);
			document.getElementById('paymentAmountspan').innerHTML = document.getElementById('grandTotal').value;
		}
		
		function validateAppoveUser(name,value){
			//document.getElementById('lblError').innerHTML ="";
			document.getElementById("actionName").value= name;

			<s:if test="%{wfitemstate =='END'}">
				if(value == 'Approve' || value == 'Reject') {
					document.getElementById("approverUserId").value=-1;
					return true;
				}
			</s:if>
			<s:else>
				if( (value == 'Approve' || value == 'Forward' || value=='Save And Forward' ) && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
					alert("please select User");
					//document.getElementById('lblError').innerHTML ="Please Select the user";
					return false;
				}
			</s:else>
			
			return true;
		}
		
		
		function validate(name,value)
		{
			if(dom.get('vouchernumber') && dom.get('vouchernumber').value=='')
			{
				alert('Please Enter voucher number');
				return false;
			}
			if(dom.get('voucherdate').value=='')
			{
				alert("Please Select the Voucher Date!!");
				return false;
			}
			if(dom.get('bankbranch').options[dom.get('bankbranch').selectedIndex].value==-1)
			{
				alert("Please Select the Bank!!");
				return false;
			}
			if(dom.get('bankaccount').options[dom.get('bankaccount').selectedIndex].value==-1)
			{
				alert("Please Select the Bank Account");
				return false;
			}
			if(document.getElementById('grandTotal').value==0 || document.getElementById('grandTotal').value=='NaN')
			{
				alert('Payment Amount should be greater than zero!');
				dom.get('tabber1').onclick();
				return false;
			}
			<s:if test="%{enablePensionType}">
				if(document.getElementById('glCode').value==''){
					alert("Select Commission Account Code");
					return false;
				}
				if(document.getElementById('glCodeName').value=='undefined'){
					alert("Re-Select Commission Account Code");
					return false;
				}
				if(document.getElementById('glCodeAmount').value=='' || document.getElementById('glCodeAmount').value==0){
					alert("Commission Amount should not be 0 or empty.");
					return false;
				}
			</s:if>
			if(!validateAppoveUser(name,value))
			{
			return false;
			}
			return true;
		}
		function checkLength(obj)
		{
			if(obj.value.length>250)
			{
				alert('Max 250 characters are allowed for comments. Remaining characters are truncated.')
				obj.value = obj.value.substring(1,250);
			}
		}
		document.getElementById('paymentAmountspan').innerHTML = document.getElementById('grandTotal').value;
	</script>
	</s:form>
</body>
</html>
