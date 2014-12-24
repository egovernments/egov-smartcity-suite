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
<body onload="onLoadTask_reverse">
<s:form  action="contraBTB" theme="simple" name="cbtbform"  >
<s:push value="model">
<jsp:include page="../budget/budgetHeader.jsp">
<jsp:param value="Bank to Bank Transfer" name="heading"/>
</jsp:include>
<div class="formmainbox"><div class="formheading"/><div class="subheadnew">Create Bank to Bank Transfer</div>
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
		<td width="10%"  class="bluebox">
		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			<td class="bluebox" width="22%" ><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="22%" ><s:textfield name="voucherNumber" id="voucherNumber" /></td></s:if>
			<s:hidden name="id"/>
			<td class="bluebox" width="18%" ><s:text name="voucher.date"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="34%" ><input type:text name="voucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')" value='<s:date name="voucherDate" format="dd/MM/yyyy"/>'/>
			<a href="javascript:show_calendar('cbtbform.voucherDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A>(dd/mm/yyyy)</td>
		</tr>
	<%@include file="contraBTB-form.jsp"%>
	<tr>
		<td class="bluebox"></td>
		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			
			<td class="bluebox"><s:text name="reversalVoucherNumber"/><span class="mandatory">*</span></td>
			<td class="bluebox"><s:textfield name="reversalVoucherNumber" id="reversalVoucherNumber" /></td></s:if>
			<td class="bluebox"><s:text name="reversalVoucherDate"/><span class="mandatory">*</span></td>
			<td class="bluebox"><s:textfield name="reversalVoucherDate"  id="reversalVoucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('cbtbform.reversalVoucherDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A>(dd/mm/yyyy)</td>
		</tr>
	</table>
	</div>	
<%@include file="../voucher/ReverseButtons.jsp"%>
<input type="hidden" id="voucherTypeBean.voucherName" name="voucherTypeBean.voucherName" value="BankToBank"/>
<input type="hidden" id="voucherTypeBean.voucherType" name="voucherTypeBean.voucherType" value="Contra"/>
<input type="hidden" id="voucherTypeBean.voucherNumType" name="voucherTypeBean.voucherNumType" value="Contra"/>
<input type="hidden" id="voucherTypeBean.cgnType" name="voucherTypeBean.cgnType" value="BTB"/>
</s:push><s:token/>
</s:form>
<SCRIPT type="text/javascript">
 function onLoadTask_reverse() {
		       
				var button = '<s:property value="button"/>';
				alert(button);
				if (button != null && button != "") {
				if (document.getElementById("Errors").innerHTML == '') {
				alert('<s:text name="contra.reverse.transaction.success"/>');
						if (button == "Reverse_Close") {
							window.close();
						} else if (button == "Reverse_View") {
							var vhId = '<s:property value="vhId"/>';
							document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="
									+ vhId;
							document.forms[0].submit();
						} else if (button == "Reverse_New") {
							document.forms[0].button.value = '';
							document.forms[0].action = "contraBTB!newform.action";
							document.forms[0].submit();
						}
					}
				}
				else
				{
				alert('disabling');
				disableControls(0,true);
				}
				document.getElementById('button').disabled=false;
				document.getElementById('Save_View').disabled=false;
				document.getElementById('Save_Close').disabled=false;
				document.getElementById('Close').disabled=false;
				//document.getElementById('reversalVouhernumber').disabled=false;
				document.getElementById('reversalVoucherDate').disabled=false;
				var revVoucherNumberObj=document.getElementById('reversalVoucherNumber');
				if(revVoucherNumberObj!=null && revVoucherNumberObj!=undefined)
				{
				revVoucherNumberObj.disabled=false;
				}
				 
			
			}

</SCRIPT>
</body>
</html>
