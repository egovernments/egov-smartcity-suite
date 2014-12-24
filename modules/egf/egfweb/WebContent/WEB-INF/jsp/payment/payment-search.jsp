<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<html>  
<head>  
	<link rel="stylesheet" type="text/css" href="/EGF/cssnew/ccMenu.css"/>
	<s:if test="%{disableExpenditureType == true && enablePensionType == false}"><title>Salary Bill Payment Search</title></s:if>
	<s:elseif test="%{disableExpenditureType == true && enablePensionType == true}"><title>Pension Bill Payment Search</title></s:elseif>
    <s:else><title>Bill Payment Search</title></s:else>
</head>
	<body>  
		<s:form action="payment" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Bill Payment Search" />
			</jsp:include>
			<span class="mandatory" id="errorSpan">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox">
			<div class="subheadnew">
			<s:if test="%{disableExpenditureType == true && enablePensionType == false}">Salary Bill Payment Search</s:if>
			<s:elseif test="%{disableExpenditureType == true && enablePensionType == true}">Pension Bill Payment Search</s:elseif>
			<s:else>Bill Payment Search</s:else>
			</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="bluebox" width="30%"><s:text name="payment.billnumber"/> </td>
					<td class="bluebox"><s:textfield name="billNumber" id="billNumber" maxlength="25" value="%{billNumber}" /></td>
				</tr>
				<tr>
					<td class="greybox" width="30%"><s:text name="payment.billdatefrom"/> </td>
					<td class="greybox"><s:textfield name="fromDate" id="fromDate" maxlength="20" value="%{fromDate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].fromDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a><br/>(dd/mm/yyyy)</td>
					<td class="greybox" width="30%"><s:text name="payment.billdateto"/> </td>
					<td class="greybox"><s:textfield name="toDate" id="toDate" maxlength="20" value="%{toDate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].toDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				</tr>
				<tr>
					<td class="bluebox" width="30%"><s:text name="payment.expendituretype"/> </td>
					<td class="bluebox"><s:select name="expType" id="expType" list="#{'-1':'----Choose----','Purchase':'Purchase','Works':'Works','Expense':'Expense'}" value="%{expType}"/></td>
				</tr>
				<jsp:include page="../voucher/vouchertrans-filter.jsp"/>
			</table>
			<div  class="buttonbottom">
				<s:submit method="search" value="Search" id="searchBtn" cssClass="buttonsubmit" onclick="return search()"/>
				<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
		</div>
		<s:if test="%{!validateUser('createpayment')}">
		<script>
			document.getElementById('searchBtn').disabled=true;
			document.getElementById('errorSpan').innerHTML='<s:text name="payment.invalid.user"/>';
			if(document.getElementById('vouchermis.departmentid'))
			{
				var d = document.getElementById('vouchermis.departmentid');
				d.options[d.selectedIndex].text='----Choose----';
				d.options[d.selectedIndex].text.value=-1;
			}
		</script>
		</s:if>
		<s:if test="%{validateUser('deptcheck')}">
			<script>
				if(document.getElementById('vouchermis.departmentid'))
				{
					document.getElementById('vouchermis.departmentid').disabled=true;
				}
			</script>
		</s:if>
		<s:hidden name="disableExpenditureType" id="disableExpenditureType" value="%{disableExpenditureType}"/>
		<s:hidden name="enablePensionType" id="enablePensionType" value="%{enablePensionType}"/>
		</s:form>
		<script>
			function loadBank(obj){}
			function search()
			{
				if(document.getElementById('vouchermis.departmentid'))
					document.getElementById('vouchermis.departmentid').disabled=false;
				return true;
			}
			<s:if test="%{disableExpenditureType == true && enablePensionType == false}">
				var element = document.getElementById('expType');
				var len = element.options.length;
				element.options.length = 0;
				element.options[element.length] = new Option('Salary', 'Salary');
				element.disabled = true;
			</s:if>
			<s:if test="%{disableExpenditureType == true && enablePensionType == true}">
				var element = document.getElementById('expType');
				var len = element.options.length;
				element.options.length = 0;
				element.options[element.length] = new Option('Pension', 'Pension');
				element.disabled = true;
			</s:if>
		</script>
	</body>  
</html>
