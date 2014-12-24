<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
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