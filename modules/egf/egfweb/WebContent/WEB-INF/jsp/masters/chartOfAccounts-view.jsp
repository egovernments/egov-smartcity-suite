<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="chartOfAccount"/></title>
    <script>
    	function submitForm(){
    		var id = '<s:property value="coaId"/>';
    		document.chartOfAccountsForm.action+='?model.id='+id;
    		return true;
    	}
    </script>
</head>  
	<body>  
		<jsp:include page="../budget/budgetHeader.jsp"/>
		<div class="formmainbox"><div class="subheadnew"><s:text name="chartOfAccount"/></div>
		<s:actionmessage theme="simple"/>
		<s:actionerror/>  
		<s:fielderror />  
		<s:form name="chartOfAccountsForm" action="chartOfAccounts" theme="simple" >
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="chartOfAccountsTable">
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.accountCode"/>:</strong></td>
			    <td width="22%" class="bluebox"><s:property value="model.glcode"/></td>
				<td class="bluebox"><strong><s:text name="chartOfAccount.name"/>:</strong></td>
			    <td class="bluebox"><s:property value="model.name"/></td>
			</tr>
			<tr>
			    <td width="20%" class="greybox">&nbsp;</td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.description"/>:</strong></td>
			    <td width="22%" class="greybox"><s:property value="model.desc"/></td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.type"/>:</strong></td>
			    <td class="greybox">
			    	<s:if test="model.type == 'I'">
			    		<s:text name="chartOfAccount.income"/>
			    	</s:if>
			    	<s:if test="model.type == 'E'">
			    		<s:text name="chartOfAccount.expense"/>
			    	</s:if>
			    	<s:if test="model.type == 'A'">
			    		<s:text name="chartOfAccount.asset"/>
			    	</s:if>
			    	<s:if test="model.type == 'L'">
			    		<s:text name="chartOfAccount.liability"/>
			    	</s:if>
			    </td>
			</tr>
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.classification"/>:</strong></td>
			    <td width="22%" class="bluebox"><s:property value="model.classification"/></td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.purpose"/>:</strong></td>
			    <td class="bluebox"><s:property value="accountcodePurpose.name"/></td>
			</tr>
			<tr>
			    <td width="20%" class="greybox">&nbsp;</td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.accountDetailType"/>:</strong></td>
			    <td width="22%" class="greybox">
			    <s:iterator value="chartOfAccounts.chartOfAccountDetails" status="status">
			    	<s:property value="detailTypeId.name"/><s:if test="!#status.last">,</s:if>
			    </s:iterator>
			    </td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.activeForPosting"/>:</strong></td>
			    <td class="greybox"><s:if test="%{getIsActiveForPosting() == true}"><s:text name="yes"/></s:if><s:else><s:text name="no"/></s:else></td>
			</tr>
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
				<td width="10%" class="bluebox"><strong><s:text name="Function Required"/>:</strong></td>
			    <td width="22%" class="bluebox"><s:if test="%{getFunctionReqd() == true}"><s:text name="yes"/></s:if><s:else><s:text name="no"/></s:else></td>
				<td width="10%" class="bluebox"><strong><s:text name="Budget Required"/>:</strong></td>
			    <td class="bluebox"><s:if test="%{budgetCheckReq() == true}"><s:text name="yes"/></s:if><s:else><s:text name="no"/></s:else></td>
			</tr>
		</table>
		<br/><br/>
		<s:if test="%{coaId !=null || coaId!=''}">
			<s:if test="%{shouldAllowCreation()}">
				<s:submit name="Add" value="Add" method="addNewCoa" cssClass="buttonsubmit" onclick="document.chartOfAccountsForm.action+='?parentId=%{coaId}'"/>
			</s:if>
			<s:submit name="Modify" value="Modify" method="modify" cssClass="buttonsubmit" onclick="return submitForm();"/>
		</s:if>
		<s:submit value="Close" onclick="javascript: self.close()" cssClass="buttonsubmit"/>
		</s:form>  
	</body>  
</html>