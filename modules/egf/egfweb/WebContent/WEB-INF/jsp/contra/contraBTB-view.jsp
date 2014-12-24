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
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/ContraBTBHelper.js"></script>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
</head>
<body onload="onloadTask_view()">
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
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
	<table border="0" width="100%" cellspacing="0" cellpadding="0">
		<tr>
		<td class="bluebox" width="10%"></td>
			<td class="bluebox" width="22%"><s:text name="voucher.number"/></td>
			<td class="bluebox" width="22%"><s:textfield name="voucherNumber" id="voucherNumber" /></td>
			<s:hidden name="id"/>
			<td class="bluebox" width="18%"><s:text name="voucher.date"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="38%"><input type="text" name="voucherDate" onkeyup="DateFormat(this,this.value,event,false,'3')" value='<s:date name="voucherDate" format="dd/MM/yyyy"/>'/>
			<a href="javascript:show_calendar('cbtbform.voucherDate');" style="text-decoration:none">&nbsp;<img tabIndex="-1" src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></A>(dd/mm/yyyy)</td>
		</tr>
	<%@include file="contraBTB-form.jsp"%>	
	
	</table>
	</div>	
	<div class="buttonbottom" >
<input type="button" id="closeButton" value="Close" onclick="javascript:window.close()" class="button"/>
</div>

<input type="hidden" id="voucherTypeBean.voucherName" name="voucherTypeBean.voucherName" value="BankToBank"/>
<input type="hidden" id="voucherTypeBean.voucherType" name="voucherTypeBean.voucherType" value="Contra"/>
<input type="hidden" id="voucherTypeBean.voucherNumType" name="voucherTypeBean.voucherNumType" value="Contra"/>
<input type="hidden" id="voucherTypeBean.cgnType" name="voucherTypeBean.cgnType" value="BTB"/>
</s:push>
</s:form>
<SCRIPT type="text/javascript">

		function onloadTask_view()
		{
		disableControls(0,true);
		document.getElementById("closeButton").disabled=false;
		}


</SCRIPT>
</body>
</html>
