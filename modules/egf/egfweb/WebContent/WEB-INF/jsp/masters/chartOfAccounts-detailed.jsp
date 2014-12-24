<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title>Add Detailed Chart Of Accounts</title>
    <link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css"/>
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/assets/skins/sam/autocomplete.css" />
<script type="text/javascript" src="/egi/commonyui/yui2.7/animation/animation-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/autocomplete/autocomplete-min.js"></script>
	<script type="text/javascript">
		function validate(){
			if(document.getElementById('glCode').value == null || document.getElementById('glCode').value==''){
				alert("Please enter Parent GlCode");
				return false;
			}
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

		var callback = {
			     success: function(o) {
					document.getElementById('newGlcode').value = ltrim(rtrim(o.responseText));
			        },
			     failure: function(o) {
			     }
		} 

		function generateGlCode(){
			value = document.getElementById('glCode').value;
			document.getElementById('generatedGlcode').value = value.split("-")[0]; 
			var transaction = YAHOO.util.Connect.asyncRequest('GET', 'chartOfAccounts!ajaxNextGlCode.action?parentGlcode='+value.split("-")[0], callback, null);
		}
	</script>
</head>  
	<body  class="yui-skin-sam">  
		<jsp:include page="../budget/budgetHeader.jsp"/>
		<s:actionmessage theme="simple"/>
		<s:actionerror/>  
		<s:fielderror />
		<div class="formmainbox"><div class="subheadnew">Add Detailed Chart Of Accounts</div>  
		<s:form name="chartOfAccountsForm" action="chartOfAccounts" theme="simple" >
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="chartOfAccountsTable">
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
			    <td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.parent"/>:<span class="mandatory">*</span></strong></td>
				<td  class="bluebox">
					<div id="myAutoComplete" style="width:15em;padding-bottom:2em;"> 
						<input type="text" name="glCode" id="glCode" onblur="generateGlCode();"/>
						<div id="myContainer"></div> 
					</div> 
				</td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.glCode"/>:<span class="mandatory">*</span></strong></td>
			    <td class="bluebox" width="10%">
			    	<input type="text" readonly="readonly" name="generatedGlcode" id="generatedGlcode" size="10"/>
			    	<input type="text" name="newGlcode" id="newGlcode" size="5" maxlength='<s:property value="glCodeLengths[4l]"/>'/>
			    </td>
			</tr>
			<tr>
			    <td width="20%" class="greybox">&nbsp;</td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.name"/>:<span class="mandatory">*</span></strong></td>
			    <td class="greybox"><input type="text" id="model.name" name="model.name" onKeyDown="textCounter('model.name',100)" onKeyUp="textCounter('model.name',100)" onblur="textCounter('model.name',100)"/></td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.description"/>:</strong></td>
			    <td width="22%" class="greybox"><input type="text" id="model.desc" name="model.desc" onKeyDown="textCounter('model.desc',250)" onKeyUp="textCounter('model.desc',250)" onblur="textCounter('model.desc',250)"/></td>
			</tr>
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.purpose"/>:</strong></td>
			    <td class="bluebox"><s:select list="dropdownData.purposeList"  listKey="id" listValue="name" name="purposeId" headerKey="0" headerValue="--- Select ---" value="model.purpose" ></s:select></td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.accountDetailType"/>:</strong></td>
			    <td width="22%" class="bluebox"><s:select list="dropdownData.accountDetailTypeList"  listKey="id" listValue="name" name="accountDetailTypeList" multiple="true" value="%{accountDetailTypeList.{id}}"></s:select></td>
			</tr>
			<tr>
			    <td width="20%" class="greybox">&nbsp;</td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.functionRequired"/>:</strong></td>
			    <td width="22%" class="greybox"><s:checkbox name="functionRequired"></s:checkbox></td>
				<td width="10%" class="greybox"><strong><s:text name="chartOfAccount.budgetRequired"/>:</strong></td>
			    <td class="greybox"><s:checkbox name="budgetCheckRequired"></s:checkbox></td>
			</tr>
			<tr>
			    <td width="20%" class="bluebox">&nbsp;</td>
				<td width="10%" class="bluebox"><strong><s:text name="chartOfAccount.activeForPosting"/>:</strong></td>
			    <td class="bluebox"><s:checkbox name="activeForPosting"></s:checkbox></td>
			</tr>
		</table>
		<br/><br/>
		<div class="buttonbottom"> 
			<s:submit name="Save" value="Save" method="saveDetailCode" cssClass="buttonsubmit" onclick="return validate();"/>
			<s:submit value="Close" onclick="javascript: self.close()" cssClass="buttonsubmit"/>
		</div>
		</s:form>  
<script>
	var allGlcodes = [];
	<s:iterator value="allChartOfAccounts">
		allGlcodes.push("<s:property value="glcode"/>-<s:property value="name.replaceAll('\n',' ')"/>")
	</s:iterator>
	YAHOO.example.BasicLocal = function() { 
		    var oDS = new YAHOO.util.LocalDataSource(allGlcodes); 
		    // Optional to define fields for single-dimensional array 
		    oDS.responseSchema = {fields : ["state"]}; 
		 
		    var oAC = new YAHOO.widget.AutoComplete("glCode", "myContainer", oDS); 
		    oAC.prehighlightClassName = "yui-ac-prehighlight"; 
			oAC.queryDelay = 0;
		    oAC.useShadow = true;
			oAC.useIFrame = true; 
			oAC.maxResultsDisplayed = 15;
		     
		    return { 
		        oDS: oDS, 
		        oAC: oAC 
		    }; 
		}(); 
</script>
	</body>  
</html>