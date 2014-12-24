<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="chartOfAccount.add"/></title>
     <script type="text/javascript">
		function validate(){
			if(document.getElementById('model.name').value == null || document.getElementById('model.name').value==''){
				alert("Please enter Name");
				return false;
			}
			if(document.getElementById('newGlcode').value == null || document.getElementById('newGlcode').value==''){
				alert("Please enter Account Code");
				return false;
			}
			return true;
		}
	</script>
</head>  
	<body>  
		<jsp:include page="../budget/budgetHeader.jsp"/>
		<s:actionmessage theme="simple"/>
		<div class="formmainbox"><div class="subheadnew"><s:text name="chartOfAccount.add"/></div>
		<s:actionerror/>  
		<s:fielderror />  
		<s:form name="chartOfAccountsForm" action="chartOfAccounts" theme="simple" >
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="chartOfAccountsTable">
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.accountCode"/>:<span class="mandatory">*</span></strong></td>
			    <td width="22%" class="bluebox">
			    	<input type="text" readonly="readonly" name="generatedGlcode" id="generatedGlcode" size="10" value='<s:property value="generatedGlcode"/>'/>
			    	<input type="text" name="newGlcode" id="newGlcode" size="5" maxlength='<s:property value="glCodeLengths[model.classification]"/>' value='<s:property value="newGlcode"/>'/>
			    </td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.name"/>:<span class="mandatory">*</span></strong></td>
			    <td class="bluebox"><input type="text" id="model.name" name="model.name" onKeyDown="textCounter('model.name',100)" onKeyUp="textCounter('model.name',100)" onblur="textCounter('model.name',100)"/></td>
			</tr>
			<tr>
			    <td width="20%" class="greybox">&nbsp;</td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.description"/>:</strong></td>
			    <td width="22%" class="greybox"><input type="text" id="model.desc" name="model.desc" onKeyDown="textCounter('model.desc',250)" onKeyUp="textCounter('model.desc',250)" onblur="textCounter('model.desc',250)"/></td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.type"/>:</strong></td>
			    <td class="greybox"><input type="text" name="type" value='<s:property value="model.type"/>' id="chartOfAccounts_type" style="border:0px;" readOnly="true"/></td>
			</tr>
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.classification"/>:</strong></td>
			    <td width="22%" class="bluebox"><input type="text" name="model.classification" value='<s:property value="model.classification"/>' style="border:0px;"/></td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.purpose"/>:</strong></td>
			    <td class="bluebox"><s:select list="dropdownData.purposeList"  listKey="id" listValue="name" name="purposeId" headerKey="0" headerValue="--- Select ---" value="model.purpose" ></s:select></td>
			</tr>
			<tr>
			    <td width="20%" class="greybox">&nbsp;</td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.accountDetailType"/>:</strong></td>
			    <td width="22%" class="greybox"><s:select list="dropdownData.accountDetailTypeList"  listKey="id" listValue="name" name="accountDetailTypeList" multiple="true" value="%{accountDetailTypeList.{id}}"></s:select></td>
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
		<input type="hidden" name="parentId" value='<s:property value="parentId"/>'/>
		<s:submit name="Save" value="Save" method="save" onclick="javascript: return validate();" cssClass="buttonsubmit" />
		<s:submit value="Close" onclick="javascript: self.close()" cssClass="buttonsubmit"/>
		<s:token/>
		</s:form>  
	</body>  
</html>