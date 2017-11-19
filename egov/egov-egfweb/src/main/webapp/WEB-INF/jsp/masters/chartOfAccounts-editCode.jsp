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
<title>Detailed Chart Of Accounts</title>

<script type="text/javascript">
		function validateAndSubmit(obj){
			if(document.getElementById('glCode').value == null || document.getElementById('glCode').value==''){
				bootbox.alert("Please enter account code");
				return false;
			}
			var value = obj.value;
				document.chartOfAccountsForm.action='${pageContext.request.contextPath}/masters/chartOfAccounts-modifySearch.action';
	    		document.chartOfAccountsForm.submit();
			return true;
		}

</script>
</head>
<body class="yui-skin-sam">
	<jsp:include page="../budget/budgetHeader.jsp" />
	<s:actionmessage theme="simple" />
	<s:actionerror />
	<s:fielderror />
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="chartOfAccount.detailed" />
		</div>
		<s:form name="chartOfAccountsForm" id="chartOfAccountsForm"
			action="chartOfAccounts" theme="simple">

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				id="chartOfAccountsTable">
				<tr>
					<td width="30%" class="bluebox">&nbsp;</td>
					<td class="bluebox" align="right"><strong><s:text
								name="chartOfAccount.accountCode" />:</strong><span class="mandatory1">*</span></td>
					<td class="bluebox">
						<div id="myAutoComplete" style="width: 20em; padding-bottom: 2em;">
							<input type="text" name="glCode" id="glCode" />
							<div id="myContainer" style="width: 50em;"></div>
						</div>
					</td>
					<td width="30%" class="bluebox">&nbsp;</td>
				</tr>
			</table>
	</div>
	<div class="buttonbottom" style="padding-bottom: 10px;">
		<input type="submit" class="buttonsubmit" value="Search and Modify"
			id="Search" name="Search" onclick="return validateAndSubmit(this);" />
		<input type="button" value="Close" onclick="javascript:window.close()"
			class="button" />
	</div>
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

</body>

</html>
