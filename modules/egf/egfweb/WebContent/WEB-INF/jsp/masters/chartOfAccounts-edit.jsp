<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="chartOfAccount.modify"/></title>
</head>  
	<body>  
		<jsp:include page="../budget/budgetHeader.jsp"/>
		<div class="formmainbox"><div class="subheadnew"><s:text name="chartOfAccount.modify"/></div>
		<s:actionmessage theme="simple"/>
		<s:actionerror/>  
		<s:fielderror />  
		<s:form name="chartOfAccountsForm" action="chartOfAccounts" theme="simple" >
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="chartOfAccountsTable">
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.accountCode"/>:</strong></td>
			    <td width="22%" class="bluebox"><input type="text" name="glcode" value='<s:property value="model.glcode"/>' id="chartOfAccounts_glcode" style="border:0px;" readOnly="true"/></td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.name"/>:<span class="mandatory">*</span></strong></td>
			    <td class="bluebox"><input type="text" name="name" value='<s:property value="model.name"/>' id="chartOfAccounts_name"/></td>
			</tr>
			<tr>
			    <td width="20%" class="greybox">&nbsp;</td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.description"/>:</strong></td>
			    <td width="22%" class="greybox"><s:textfield name="desc"/></td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.type"/>:</strong></td>
			    <td class="greybox">
				    <s:if test="model.type == 'I'">
				    	<input type="text" name="type" value='<s:text name="chartOfAccount.income"/>' id="chartOfAccounts_type" style="border:0px;" readOnly="true"/>
				    </s:if>
			    	<s:if test="model.type == 'E'">
			    		<input type="text" name="type" value='<s:text name="chartOfAccount.expense"/>' id="chartOfAccounts_type" style="border:0px;" readOnly="true"/>
			    	</s:if>
				    <s:if test="model.type == 'A'">
				    	<input type="text" name="type" value='<s:text name="chartOfAccount.asset"/>' id="chartOfAccounts_type" style="border:0px;" readOnly="true"/>
				    </s:if>
				    <s:if test="model.type == 'L'">
				    	<input type="text" name="type" value='<s:text name="chartOfAccount.liability"/>' id="chartOfAccounts_type" style="border:0px;" readOnly="true"/>
				    </s:if>
			    </td>
			</tr>
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.classification"/>:</strong></td>
			    <td width="22%" class="bluebox"><input type="text" name="model.classification" value='<s:property value="model.classification"/>' style="border:0px;"/></td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.purpose"/>:</strong></td>
			    <td class="bluebox"><s:select list="dropdownData.purposeList"  listKey="id" listValue="name" name="accountcodePurpose.id" headerKey="0" headerValue="--- Select ---" value="accountcodePurpose.id" ></s:select></td>
			</tr>
			<tr>
			    <td width="20%" class="greybox">&nbsp;</td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.accountDetailType"/>:</strong></td>
			    <td width="22%" class="greybox"><s:select list="dropdownData.accountDetailTypeList"  listKey="id" listValue="name" name="accountDetailTypeList"  multiple="true" value="accountDetailTypeList"></s:select></td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.activeForPosting"/>:</strong></td>
			    <td class="greybox"><s:checkbox name="activeForPosting"></s:checkbox></td>
			</tr>
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.functionRequired"/>:</strong></td>
			    <td width="22%" class="bluebox"><s:checkbox name="functionRequired"></s:checkbox></td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.budgetRequired"/>:</strong></td>
			    <td class="bluebox"><s:checkbox name="budgetCheckRequired"></s:checkbox></td>
			</tr>
		</table>
		<br/><br/>
		<div class="buttonbottom">
		<input type="hidden" name="model.id" value='<s:property value="model.id"/>'/>
		<s:submit name="Save" value="Save" method="update" cssClass="buttonsubmit"/>
		<s:submit value="Close" onclick="javascript: self.close()" cssClass="buttonsubmit"/>
		</div>
		<s:token/>
		</s:form>  
	</body>  
</html>