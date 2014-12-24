<html>
<head>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="EGF" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/contraBTBHelper.js"></script>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
</head>
<body onload="onLoadTask_edit()">
<s:form  action="contraBTB" theme="simple" name="cbtbform"  >
<s:push value="model">
<jsp:include page="../budget/budgetHeader.jsp">
<jsp:param value="Bank to Bank Transfer" name="heading"/>
</jsp:include>
<div class="formmainbox"><div class="formheading"/><div class="subheadnew">Modify Bank to Bank Transfer</div>
		<div id="listid" style="display:block">
		<br/>
		</div></div></div>
		<div align="center">
<font  style='color: red ;'> 
<p class="error-block" id="lblError" ></p>
</font>

<span class="mandatory">
				<div id="Errors"><s:actionerror/><s:fielderror /></div>
				<s:actionmessage />
			</span>
	<table border="0" width="100%" cellspacing="0" cellpadding="0">
		<tr>
		<td class="bluebox" width="10%"></td>
		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			<td class="bluebox" width="22%"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="22%">
			<table width="100%">
			<tr>
			<td style="width:25%"><input type="text" name="voucherNumberPrefix" id="voucherNumberPrefix" readonly="true"  style="width:100%"/></td> 
			<td width="75%"><s:textfield name="voucherNumber" id="voucherNumber" /></td>
			</tr>
			</table>
			</td>
			
			</s:if>
		<s:else>
			<td class="bluebox" width="22%"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="22%">
			<table width="100%">
			<tr>
			<td style="width:25%"><input type="text" name="voucherNumberPrefix" id="voucherNumberPrefix" readonly="true"  style="width:100%"  /></td> 
			<td width="75%"><s:textfield name="voucherNumber" id="voucherNumber" readonly="true"/></td>
			</tr>
			</table>
			</td>
			<s:hidden name="voucherNumber" id="voucherNumber" />	
		</s:else>	
			<s:hidden name="id"/>
			<td class="bluebox" width="18%%"><s:text name="voucher.date"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="34%"><input type="text" name="voucherDate" id="voucherDate" value='<s:date name="voucherDate" format="dd/MM/yyyy"/>'  onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('cbtbform.voucherDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A>(dd/mm/yyyy)</td>
		</tr>
	<%@include file="contraBTB-form.jsp"%>
	</table>
	<div class="subheadsmallnew"/></div>
	<div class="mandatory" align="left">* Mandatory Fields</div>
	</div>	</div>	
<%@include file="../voucher/modifyButtons.jsp"%>
<s:hidden id="bankBalanceMandatory" name="bankBalanceMandatory" value="%{isBankBalanceMandatory()}"/>
</s:push><s:token/>
</s:form>
<SCRIPT type="text/javascript">
	
			function onLoadTask_edit() {
			   var tempVoucherNumber='<s:property value="voucherHeader.voucherNumber"/>';
			   var prefixLength='<s:property value="voucherNumberPrefixLength"/>';
			   document.getElementById('voucherNumberPrefix').value=tempVoucherNumber.substring(0,prefixLength);
			   document.getElementById('voucherNumber').value=tempVoucherNumber.substring(prefixLength,tempVoucherNumber.length);
				if('other'=='<s:property value="contraBean.modeOfCollection"/>')
				{
				document.getElementById("mdcNumber").innerHTML = '<s:text name="contra.refNumber" />';
				document.getElementById("mdcDate").innerHTML = '<s:text name="contra.refDate" />';
				}else
				{
				 <s:if test="egovCommon.isShowChequeNumber()">
				 document.getElementById("chequeGrid").style.visibility="visible";
				 </s:if>
				 <s:else>
				 document.getElementById("chequeGrid").style.visibility="hidden";
				 </s:else>
				 
				}
				
				var button = '<s:property value="button"/>';
				 if (button != null && button != "") {

					if (document.getElementById("Errors").innerHTML == '') {
					alert('<s:text name="contra.transaction.succcess"/>');
					   if (button == "Save_Close") {
							window.close();
						} else if (button == "Save_View") {
							var vhId = '<s:property value="vhId"/>';
							document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="
									+ vhId;
							document.forms[0].submit();
						} else if (button == "Save_New") {
							document.forms[0].button.value = '';
							document.forms[0].action = "contraBTB!newform.action";
							document.forms[0].submit();
						}
					}
					 
				}

			}
			
function toggleChequeAndRefNumber(obj) {

		if (obj.value == "other") {
			document.getElementById("chequeGrid").style.visibility="visible";
			document.getElementById("mdcNumber").innerHTML = '<s:text name="contra.refNumber" />';
			document.getElementById("mdcDate").innerHTML = '<s:text name="contra.refDate" />';
					
		} else {
		var x='<s:property value="egovCommon.isShowChequeNumber()"/>';
		<s:if test="egovCommon.isShowChequeNumber()">
		 document.getElementById("chequeGrid").style.visibility="visible";
		 </s:if>
		 <s:else>
		 document.getElementById("chequeGrid").style.visibility="hidden";
		 </s:else>
			document.getElementById("mdcNumber").innerHTML = '<s:text name="contra.chequeNumber" />';
			document.getElementById("mdcDate").innerHTML = '<s:text name="contra.chequeDate" />';
			
		}
	}
	
	if('<s:text name="%{isBankBalanceMandatory()}"/>'=='')
		document.getElementById('lblError').innerHTML = "bank_balance_mandatory parameter is not defined";
	
					
</SCRIPT>
</body>
</html>
