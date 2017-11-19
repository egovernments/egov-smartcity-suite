<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<title>Add Detailed Chart Of Accounts</title>
<script type="text/javascript">
		function validateAndSubmit(){
			if(document.getElementById('glCode').value == null || document.getElementById('glCode').value==''){
				bootbox.alert("Please enter Parent GlCode");
				return false;
			}
			if(document.getElementById('model.name').value == null || document.getElementById('model.name').value==''){
				bootbox.alert("Please enter Name");
				return false;
			}
			if(document.getElementById('newGlcode').value == null || document.getElementById('newGlcode').value==''){
				bootbox.alert("Please enter Account Code");
				return false;
			}
			document.chartOfAccountsForm.action = '${pageContext.request.contextPath}/masters/chartOfAccounts-create.action';
			document.chartOfAccountsForm.submit();
				
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
			if(value.split("-").length>1){
			document.getElementById('generatedGlcode').value = value.split("-")[0]; 
			var transaction = YAHOO.util.Connect.asyncRequest('GET', 'chartOfAccounts-ajaxNextGlCode.action?parentGlcode='+value.split("-")[0], callback, null);
			document.getElementById('glCode').readOnly = true
			}
			/* else {
			bootbox.alert("Invalid Parent Account Code selected. Please select from auto complete");
			} */
		}
	</script>
</head>
<body class="yui-skin-sam">
	<jsp:include page="../budget/budgetHeader.jsp" />
	<s:actionmessage theme="simple" />
	<s:actionerror />
	<s:fielderror />
	<div class="formmainbox">
		<div class="subheadnew">Add Detailed Chart Of Accounts</div>
		<s:form name="chartOfAccountsForm" action="chartOfAccounts"
			theme="simple">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				id="chartOfAccountsTable">
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.parent" />:<span class="mandatory1">*</span></strong></td>
					<td class="bluebox">
						<div id="myAutoComplete" style="width: 15em; padding-bottom: 2em;">
							<input type="text" name="glCode" id="glCode"
								onblur="generateGlCode();" />
							<div id="myContainer"></div>
						</div>
					</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.glCode" />:<span class="mandatory1">*</span></strong></td>
					<td class="bluebox" width="10%"><input type="text"
						readonly="readonly" name="generatedGlcode" id="generatedGlcode"
						size="10" /> <input type="text" name="newGlcode" id="newGlcode"
						size="2" maxlength='<s:property value="glCodeLengths[4l]"/>' /></td>
				</tr>
				<tr>
					<td width="20%" class="greybox">&nbsp;</td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.name" />:<span class="mandatory1">*</span></strong></td>
					<td class="greybox"><input type="text" id="model.name"
						name="model.name" onKeyDown="textCounter('model.name',100)"
						onKeyUp="textCounter('model.name',100)"
						onblur="textCounter('model.name',100)" /></td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.description" />:</strong></td>
					<td width="22%" class="greybox"><input type="text"
						id="model.desc" name="model.desc"
						onKeyDown="textCounter('model.desc',250)"
						onKeyUp="textCounter('model.desc',250)"
						onblur="textCounter('model.desc',250)" /></td>
				</tr>
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.purpose" />:</strong></td>
					<td class="bluebox"><s:select list="dropdownData.purposeList"
							listKey="id" listValue="name" name="purposeId" headerKey=""
							headerValue="--- Select ---" value="model.purpose"></s:select></td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.accountDetailType" />:</strong></td>
					<td width="22%"><s:select
							list="dropdownData.accountDetailTypeList" listKey="id"
							listValue="name" name="accountDetailTypeList" multiple="true"
							size="5" value="%{accountDetailTypeList.{id}}"></s:select></td>
				</tr>
				<tr>
					<td width="20%" class="greybox">&nbsp;</td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.functionRequired" />:</strong></td>
					<td width="22%" class="greybox"><s:checkbox
							name="functionRequired"></s:checkbox></td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.budgetRequired" />:</strong></td>
					<td class="greybox"><s:checkbox name="budgetCheckRequired"></s:checkbox></td>
				</tr>
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.activeForPosting" />:</strong></td>
					<td class="bluebox"><s:checkbox name="activeForPosting"></s:checkbox></td>
				</tr>
			</table>
			<br />
			<br />
			<div class="buttonbottom">
				<input type="submit" class="buttonsubmit" value="Save" id="Save"
					name="Save" onclick="return validateAndSubmit();" /> <input
					type="button" value="Close" onclick="javascript:window.close()"
					class="button" />
			</div>
			<s:token />
		</s:form>
		<script type="text/javascript">
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
			oAC.maxResultsDisplayed = 10;
		     
		    return { 
		        oDS: oDS, 
		        oAC: oAC 
		    }; 
		}(); 
</script>
	</div>
</body>
</html>
