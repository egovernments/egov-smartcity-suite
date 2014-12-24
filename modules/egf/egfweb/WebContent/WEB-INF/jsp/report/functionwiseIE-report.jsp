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
    <title>Functionwise Income/Expense Subsidary Report</title>
</head>
	<body>  
		<s:form action="functionwiseIE" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Functionwise Income/Expense Subsidary Report" />
			</jsp:include>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="formheading"></div>
			<table align="center" width="80%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="bluebox" width="30%"><s:text name="report.income.expense"/><span class="mandatory">*</span></td>
					<td class="bluebox"><s:select name="incExp" id="incExp" list="#{'-1':'---Select---','I':'Income','E':'Expenditure'}" /> </td>
				</tr>
				<tr>
					<td class="greybox" width="30%"><s:text name="report.fromdate"/><span class="mandatory">*</span></td>
					<td class="greybox"><s:textfield name="startDate" id="startDate" maxlength="20"/><a href="javascript:show_calendar('forms[0].startDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a><br/>(dd/mm/yyyy)</td>
					<td class="greybox" width="30%"><s:text name="report.todate"/><span class="mandatory">*</span></td>
					<td class="greybox"><s:textfield name="endDate" id="endDate" maxlength="20"/><a href="javascript:show_calendar('forms[0].endDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				</tr>
				<jsp:include page="report-filter.jsp"/>
				<tr>
					<td align="right"></td>  
					<td><s:submit method="search" value="Submit" cssClass="buttonsubmit" /></td>
					<td><input type="submit" value="Close" onclick="javascript:window.close()" class="button"/></td>
				</tr>
			</table>
			<br/>
		</s:form>  
	</body>  
</html>
