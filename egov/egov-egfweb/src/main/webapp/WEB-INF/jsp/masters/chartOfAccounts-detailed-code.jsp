#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<html>  
<head>  
    <title>Detailed Chart Of Accounts</title>
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css"/>
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/assets/skins/sam/autocomplete.css" />
<script type="text/javascript" src="/egi/commonyui/yui2.7/animation/animation-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/autocomplete/autocomplete-min.js"></script>

	<script type="text/javascript">
		function addNew(){
			window.open('/EGF/masters/chartOfAccounts!addNew.action','','height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
			return true;
		}
		function validate(){
			if(document.getElementById('glCode').value == null || document.getElementById('glCode').value==''){
				alert("Please enter account code");
				return false;
			}
			return true;
		}

</script>
</head>  
	<body  class="yui-skin-sam">  
		<jsp:include page="../budget/budgetHeader.jsp"/>
		<s:actionmessage theme="simple"/>
		<s:actionerror/>  
		<s:fielderror />  
		<div class="formmainbox"><div class="subheadnew"><s:text name="chartOfAccount.detailed"/></div>
		<s:form name="chartOfAccountsForm" action="chartOfAccounts" theme="simple" >
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="chartOfAccountsTable">
			<tr>
				<td width="30%" class="bluebox">&nbsp;</td>
				<td width="20%" class="bluebox"><strong><s:text name="chartOfAccount.accountCode"/>:</strong><span class="mandatory">*</span></td>
			    <td  class="bluebox">
					<div id="myAutoComplete" style="width:20em;padding-bottom:2em;"> 
						<input type="text" name="glCode" id="glCode"/>
						<div id="myContainer" style="width:50em;"></div> 
					</div> 
				</td>
			</tr>
		</table>
		<br/><br/>
		<div class="buttonbottom" style="padding-bottom:10px;"> 
			<s:submit name="Search" value="Search and Modify" method="searchAndModify" cssClass="buttonsubmit" onclick="return validate();"/>
			<s:submit name="Search" value="Search and View" method="searchAndView" cssClass="buttonsubmit" onclick="return validate();"/>
			<input type="button" name="add" value="Add New" method="addNew" class="buttonsubmit" onClick="return addNew();"/>
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
