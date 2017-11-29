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

<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript" src="<egov:url path='resources/js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../resources/css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../resources/css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />
<script src="<egov:url path='resources/js/workProgressAbstractReportHelper.js'/>"></script>
<style type="text/css">

th.pagetableth {
	background-color: #E8EDF1;
	height: 30px;
	overflow: hidden;
	border-left: 1px solid #D1D9E1;
	color: #00639B;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 13px;
	font-weight: bold;
	line-height: 14px;
	padding: 3px;
	text-align: left;
	word-wrap: break-word;
}

td.pagetabletd{
	color: #333333;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	text-align: left;
	line-height: 13px;
	overflow: hidden;
	border: 1px solid #D1D9E1;
	padding: 3px;
}
		
div.t_fixed_header {
	position		: relative;
	margin 			: 0; 
	width			: 110%;
}

div.t_fixed_header.ui .headtable th {
	padding			: 5px;
	text-align 		: center;
	border-width 	: 0 1px 0 0;
	border-style 	: solid;
	line-height		: 12px;
	font-size		: 11px;
	word-wrap       : normal;
}
.ui-state-default a, .ui-state-default a:link { 
	 text-decoration: underline;
	 font-size: 11px; 
}

.ui-state-default a:visited {
	color: #2e6e9e;
} 

div.t_fixed_header div.body {
	padding			: 0;
	width			: 100%;
	overflow-x		: hidden;
}

div.t_fixed_header_main_wrapper {
	position 		: relative; 
	overflow 		: auto; 
}

div.t_fixed_header div.headtable {
	border			: 0;	
	overflow-x		: hidden;
    overflow-y		: hidden;
}
</style>
<html>
<head>
<title><s:text name="contractorwiseAbstractReport.title" /></title>
</head>
<script>
jQuery.noConflict();
var ratioArray=new Array(40, 120,80,80,80,80,80,80,80,80,80,80,80,80,80,80);
jQuery(document).ready(function() {
    jQuery('.table-header-fix').fixheadertable({
         caption : "<s:text name='contractorwiseAbstractReport.title' />",
         height  : 400,
         resizeCol	 : true, 
         whiteSpace     : 'normal',
         width : 1180 ,	    
         wrapper        : false,
       	 colratio : ratioArray
    });
});
function setWorkOrderApprovedDates(elem){
	dom.get("fromDate").value = "";
	dom.get("toDate").value = "";
	var dropdownId = elem.value;
	var currFinYear = dom.get("currentFinancialYearId").value;
	var toDt = document.getElementById("toDate");
	
	if(dropdownId==currFinYear){
		toDt.readOnly = false;
	}
	else{
		toDt.readOnly = true;
	}
	var leng = dropdownId.length;
	var finYRStr = dom.get("finYearRangeStr").value;
	var index=-1;
	if(dropdownId!=-1)
	{
		index = finYRStr.search("id:"+dropdownId+"--");
		if(index!=-1)
		{
			dom.get("fromDate").value=finYRStr.substr(index+leng+5,10);
			dom.get("toDate").value=finYRStr.substr(index+leng+17,10);
		}	
	}	
}
function validate() 
{
	var finYearVal = dom.get("finYearId").value;
	var toDateVal = dom.get("toDate").value;
	if(finYearVal == -1)
	{
		document.getElementById("contractorwiseAbstractReport_error").style.display='';
	  	document.getElementById("contractorwiseAbstractReport_error").innerHTML='<s:text name="contractorwiseAbstractReport.select.finyr"/>';
	  	window.scroll(0,0);
	  	return false;
	}
	if(toDateVal == "")
	{
		document.getElementById("contractorwiseAbstractReport_error").style.display='';
	  	document.getElementById("contractorwiseAbstractReport_error").innerHTML='<s:text name="contractorwiseAbstractReport.validate.toDate"/>';
	  	window.scroll(0,0);
	  	return false;
	}
	if(!validateDate(toDateVal))
	{
		document.getElementById("contractorwiseAbstractReport_error").style.display='';
	  	document.getElementById("contractorwiseAbstractReport_error").innerHTML='<s:text name="contractorwiseAbstractReport.invalid.toDate"/>';
	  	window.scroll(0,0);
	  	return false;
	}
	if(compareDate(dom.get("fromDate").value,toDateVal)==-1)
	{
		document.getElementById("contractorwiseAbstractReport_error").style.display='';
	  	document.getElementById("contractorwiseAbstractReport_error").innerHTML='<s:text name="contractorwiseAbstractReport.invalid.fromDate.toDate"/>';
	  	window.scroll(0,0);
	  	return false;
	}
	document.getElementById("contractorwiseAbstractReport_error").style.display='none';
  	document.getElementById("contractorwiseAbstractReport_error").innerHTML='';
  	return true;	
}
function setDateOnLoadForCurrFinYear() {
	var toDt = document.getElementById("toDate");
	var finYearVal = dom.get("finYearId").value;
	var currFinYear = dom.get("currentFinancialYearId").value;

	if(finYearVal != -1 && toDt.value !=""){
		if(finYearVal==currFinYear){
			toDt.readOnly = false;
		}
		else{
			toDt.readOnly = true;
		}
	}
}
var contractorNameSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
	dom.get('contractorId').value = oData[1];
};
function formSubHeader()
{

	var  fromDateVal =dom.get("fromDate").value;
	var  toDateVal=dom.get("toDate").value;
	var  executingDepartmentVal=dom.get("executingDepartment").value;
	var  worksTypeVal=dom.get("worksType").value;
	var  worksSubTypeVal=dom.get("worksSubType").value;
	var  fundVal=dom.get("fund").value;
	var  functionVal=dom.get("function").value;
	var  schemeVal=dom.get("scheme").value;
	var  subSchemeVal=dom.get("subScheme").value;
	var  budgetHeadVal=dom.get("dropDownBudgetHeads");
	var contractorName = document.getElementById("contractorNameSearch").value;
	var contractorGrade = document.getElementById("status").value;
	if(budgetHeadVal)
		budgetHeadVal=budgetHeadVal.value;
	else
		budgetHeadVal='';
	var  depositCodesVal=dom.get("dropDownDepositCodes");
	if(depositCodesVal)
		depositCodesVal=depositCodesVal.value;
	else
		depositCodesVal='';
	var  allbudgetHeadVal=dom.get("allBudgetHeads");
	if(allbudgetHeadVal)
		allbudgetHeadVal=allbudgetHeadVal.value;
	else
		allbudgetHeadVal='';
	var  alldepositCodesVal=dom.get("allDepositCodes");
	if(alldepositCodesVal)
		alldepositCodesVal=alldepositCodesVal.value;
	else
		alldepositCodesVal='';
	var count = 0;
	var subHeaderTxt = "Report";
	var singleConditionHeader='';	
	if(fromDateVal!="")
	{
		count++;
		subHeaderTxt = "Report from " + fromDateVal +" to current date";
	}	
	if(toDateVal!="")
	{
		count++;
		subHeaderTxt = "Report as on " + toDateVal;
	}
	if(fromDateVal!="" && toDateVal!="")
	{
		count++;
		subHeaderTxt = "Report for date range " + fromDateVal + " - " +toDateVal ;
	}
	if(executingDepartmentVal!=-1)
	{
		count++;
		var deptIndex = dom.get("executingDepartment").selectedIndex;
		var deptOptions= dom.get("executingDepartment").options;
		subHeaderTxt = subHeaderTxt + " for department " + deptOptions[deptIndex].text  ;
		singleConditionHeader = "Report for department "+ deptOptions[deptIndex].text;
	}
	if(worksTypeVal!=-1)
	{
		count++;
		var worksTypeIndex = dom.get("worksType").selectedIndex;
		var worksTypeOptions= dom.get("worksType").options;
		subHeaderTxt = subHeaderTxt + " with type of work " + worksTypeOptions[worksTypeIndex].text;
		singleConditionHeader = "Report for type of work "+ worksTypeOptions[worksTypeIndex].text;
	}
	if(worksSubTypeVal!=-1)
	{
		count++;
		var worksSubTypeIndex = dom.get("worksSubType").selectedIndex;
		var worksSubTypeOptions= dom.get("worksSubType").options;
		subHeaderTxt = subHeaderTxt + " with subtype of work " + worksSubTypeOptions[worksSubTypeIndex].text;
		singleConditionHeader = "Report for subtype of work "+ worksSubTypeOptions[worksSubTypeIndex].text;
	}
	if(fundVal!=-1)
	{
		count++;
		var fundIndex = dom.get("fund").selectedIndex;
		var fundOptions= dom.get("fund").options;
		subHeaderTxt = subHeaderTxt + " under " + fundOptions[fundIndex].text;
		singleConditionHeader = "Report for fund "+ fundOptions[fundIndex].text;
	}
	if(functionVal!=-1)
	{
		count++;
		var functionIndex = dom.get("function").selectedIndex;
		var functionOptions= dom.get("function").options;
		subHeaderTxt = subHeaderTxt + " for function " + functionOptions[functionIndex].text;
		singleConditionHeader = "Report for function "+ functionOptions[functionIndex].text;
	}
	if(schemeVal!=-1)
	{
		count++;
		var schemeIndex = dom.get("scheme").selectedIndex;
		var schemeOptions= dom.get("scheme").options;
		subHeaderTxt = subHeaderTxt + " under scheme " + schemeOptions[schemeIndex].text;
		singleConditionHeader = "Report for scheme "+ schemeOptions[schemeIndex].text;
	}
	if(subSchemeVal!=-1)
	{
		count++;
		var subSchemeIndex = dom.get("subScheme").selectedIndex;
		var subSchemeOptions= dom.get("subScheme").options;
		subHeaderTxt = subHeaderTxt + " under subscheme " + subSchemeOptions[subSchemeIndex].text;
		singleConditionHeader = "Report for sub-scheme "+ subSchemeOptions[subSchemeIndex].text;
	}
	if(budgetHeadVal!='' && budgetHeadVal!=-1) {
		count++;
		var budgetHeadText="";
		var x=document.getElementById("dropDownBudgetHeads");
		  for (var i = 1; i < x.options.length; i++) {
			 if(budgetHeadVal=='All')
			 {
				 if(budgetHeadText=='')
			    	 budgetHeadText=x.options[i].text;
			     else
			    	 budgetHeadText=budgetHeadText+", "+x.options[i].text; 
			 }
			 else
			 {
				 if(x.options[i].selected ==true){
				     if(budgetHeadText=='')
				    	 budgetHeadText=x.options[i].text;
				     else
				    	 budgetHeadText=budgetHeadText+", "+x.options[i].text; 
			     }
			 }	 
		     
		}
		subHeaderTxt = subHeaderTxt + " with Budget Head(s) "+budgetHeadText;
		singleConditionHeader = "Report for Budget Head(s) "+budgetHeadText;
	}
	if(allbudgetHeadVal!='' && allbudgetHeadVal!=-1) {
		count++;
		var budgetHeadText="";
		var x=document.getElementById("allBudgetHeads");
		  for (var i = 0; i < x.options.length; i++) {
			 if(x.options[i].selected ==true){
			     if(budgetHeadText=='')
			    	 budgetHeadText=x.options[i].text;
			     else
			    	 budgetHeadText=budgetHeadText+", "+x.options[i].text; 
		     }
		}
		subHeaderTxt = subHeaderTxt + " with Budget Head(s) "+budgetHeadText;
		singleConditionHeader = "Report for Budget Head(s) "+budgetHeadText;
	}
	if(alldepositCodesVal!='' && alldepositCodesVal!=-1 ) {
		count++;
		var depositCodesText="";
		var x=document.getElementById("allDepositCodes");
		  for (var i = 0; i < x.options.length; i++) {
			  if(x.options[i].selected ==true){
				     if(depositCodesText=='')
				    	 depositCodesText=x.options[i].text;
				     else
				    	 depositCodesText=depositCodesText+", "+x.options[i].text; 
			     }
		}
		subHeaderTxt = subHeaderTxt + " with Deposit Code(s) "+depositCodesText;
		singleConditionHeader = "Report for Deposit Code(s) "+depositCodesText;
	}
	if(depositCodesVal!='' && depositCodesVal!=-1 ) {
		count++;
		var depositCodesText="";
		var x=document.getElementById("dropDownDepositCodes");
		  for (var i = 1; i < x.options.length; i++) {
			  if(depositCodesVal=='All')
			  {
				  if(depositCodesText=='')
				    	 depositCodesText=x.options[i].text;
				     else
				    	 depositCodesText=depositCodesText+", "+x.options[i].text; 
			  }
			  else
			  {
				  if(x.options[i].selected ==true){
					     if(depositCodesText=='')
					    	 depositCodesText=x.options[i].text;
					     else
					    	 depositCodesText=depositCodesText+", "+x.options[i].text; 
				     }
			  }	  
		     
		}
		subHeaderTxt = subHeaderTxt + " with Deposit Code(s) "+depositCodesText;
		singleConditionHeader = "Report for Deposit Code(s) "+depositCodesText;
	}
	if(contractorName!='')
	{
		count++;
		subHeaderTxt = subHeaderTxt + " for contractor "+contractorName;
		singleConditionHeader = "Report for contractor "+contractorName;
	}
	if(contractorGrade!=-1)
	{
		count++;
		var statusObj = document.getElementById("status");
		var text = statusObj.options[statusObj.selectedIndex].text;
		subHeaderTxt = subHeaderTxt + " for contractor class "+text;
		singleConditionHeader = "Report for contractor class "+text;
	}
	if(count==0)
		dom.get("subHeader").value = "";
	if(count==1)
		dom.get("subHeader").value = singleConditionHeader;
	if(count>1)
		dom.get("subHeader").value = subHeaderTxt;

}
function searchOnClick()
{
	if(!validate())
		return false;
	formSubHeader();
	return true;
}
function viewEstimatesTakenUpDrillDown(contractorId,contractorName) {
	if(!validate())
		return;
	formSubHeader();
	var budgetHeadParams="";
	budgetHeadParams = getBudgetHeadsParams('2'); 
	var depositCodesParams = getDepositCodesParams('2');
	var parameter=generateURLParameters(budgetHeadParams);
	var contractorGrade = document.getElementById("status").value;
	var contractorGradeCondition="";
	if(contractorGrade!=-1)
		contractorGradeCondition = "&gradeId="+contractorGrade;
	window.open("${pageContext.request.contextPath}/reports/contractorwiseAbstractReport!showEstimatesTakenUpDrillDown.action?contractorId="+contractorId+"&contractorName="+contractorName+contractorGradeCondition+parameter+depositCodesParams,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
}
function generateURLParameters(budgetHeadParams)
{
	var additionalParam = "";
	var parameter='&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value;
		parameter=parameter+budgetHeadParams;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value;

	return parameter;
}
function disableMasking(){
	document.getElementById('loadingMask').remove();
	searchOnClick();
}
</script>
<body onload="setDateOnLoadForCurrFinYear();">
	<div class="errorstyle" id="contractorwiseAbstractReport_error"	style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="contractorwiseAbstractForm" id="contractorwiseAbstractForm" onsubmit="return validate();" theme="simple">
		<s:hidden name="subHeader" id="subHeader" />
		<s:hidden name="finYearRangeStr" id="finYearRangeStr" />
		<s:hidden name="currentFinancialYearId" id="currentFinancialYearId" />
		<s:hidden name="depositCodesAppConfValue" id="depositCodesAppConfValue" />
		<s:hidden name="budgetHeadsAppConfValue" id="budgetHeadsAppConfValue" />

		<div class="formmainbox">
		<div class="insidecontent">
		<div id="printContent" class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="4" class="headingwk">
								<div class="arrowiconwk">
									<img
										src="/egworks/resources/erp2/images/arrow.gif" />
								</div>
								<div class="headplacer">
									<s:text name="page.subheader.search.estimate" />
								</div>
							</td>
						</tr>
						<tr>
							<td width="11%" class="whiteboxwk">
								<span class="mandatory">*</span><s:text name='contractorwiseAbstractReport.finyear'/> :
							</td>
							<td width="21%" class="whitebox2wk" colspan="3">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="finYearId" 
								id="finYearId" cssClass="selectwk" list="dropdownData.finYearList" listKey="id" 
								listValue="finYearRange" value="%{finYearId}" onchange="setWorkOrderApprovedDates(this);"/>
								
							</td>
						</tr>
						<tr>
							<td class="greyboxwk">
								<s:text name="contractorwiseAbstractReport.fromdate" />
							</td>
							<td class="greybox2wk">
								<s:date name="fromDate" var="fromDateFormat"
									format="dd/MM/yyyy" />
								<s:textfield name="fromDate" id="fromDate"
									cssClass="selectwk" value="%{fromDateFormat}"
									onfocus="javascript:vDateType='3';"
									readonly="true"/>

							</td>
							<td width="17%" class="greyboxwk">
								<s:text name="contractorwiseAbstractReport.todate" />
							</td>
							<td width="17%" class="greybox2wk">
								<s:date name="toDate" var="toDateFormat"
									format="dd/MM/yyyy" />
								<s:textfield name="toDate" id="toDate"
									value="%{toDateFormat}" cssClass="selectwk"
									onfocus="javascript:vDateType='3';"
									readonly="true"/>
							</td>
						</tr>
						<tr>
							<td width="11%" class="whiteboxwk">
								<s:text name='contractorwiseAbstractReport.executing.department'/> :
							</td>
							<td width="21%" class="whitebox2wk" >
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="executingDepartment" 
								id="executingDepartment" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id" 
								listValue="deptName" value="%{executingDepartment}" />
								
							</td>
							<td class="whiteboxwk">&nbsp;</td >
							<td class="whitebox2wk" >
							</td>
						</tr>
						<tr>
							<td class="greyboxwk">
								<s:text name="contractorwiseAbstractReport.work.type" />:
							</td>
							<td class="greybox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
									name="worksType" id="worksType"
									cssClass="selectwk" list="dropdownData.worksTypeList"
									listKey="id" listValue="description"
									value="%{worksType}"
									onChange="setupSubTypes(this);" />
								<egov:ajaxdropdown id="worksSubTypeDropdown"
									fields="['Text','Value']" dropdownId='worksSubType'
									url='estimate/ajaxEstimate!subcategories.action'
									selectedValue="%{category.id}" />
							</td>

							<td class="greyboxwk">
								<s:text name="contractorwiseAbstractReport.work.subtype" />:
							</td>
							<td class="greybox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="worksSubType"
									value="%{worksSubType}" id="worksSubType" cssClass="selectwk"
									list="dropdownData.worksSubTypeList" listKey="id"
									listValue="description" />
							</td>
						</tr>
						<tr>
							<td width="15%" class="whiteboxwk">
								<s:text name='contractorwiseAbstractReport.fund'/> :				
							</td>
							<td width="53%" class="whitebox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
								name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" 
								listValue="name" value="%{fund}" />
							</td>
							<td width="11%" class="whiteboxwk">
								<s:text name='contractorwiseAbstractReport.function'/> :
							</td>
							<td width="21%" class="whitebox2wk" colspan="3">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
				 					name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" 
				 					listKey="id" listValue="name" value="%{function}"   />
							</td>
						</tr>
						<tr>
			                <td class="whiteboxwk"><s:text name='contractorwiseAbstractReport.scheme'/> : </td>
			                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="scheme" id="scheme" cssClass="selectwk" list="dropdownData.schemeList" listKey="id" listValue="name" value="%{scheme}"  onChange="setupSubSchemes(this);"/>
							<egov:ajaxdropdown id="subSchemeDropdown" fields="['Text','Value']" dropdownId='subScheme' url='reports/ajaxWorkProgress!loadSubSchemes.action' selectedValue="%{scheme.id}"/></td>
			                <td class="whiteboxwk"><s:text name='contractorwiseAbstractReport.subscheme'/> : </td>
			                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="subScheme" id="subScheme" cssClass="selectwk" list="dropdownData.subSchemeList" listKey="id" listValue="name" value="%{subScheme}" /></td>
						</tr>
						<tr>
							<td width="11%" class="greyboxwk">
								<s:text name='contractorwiseAbstractReport.budgethead'/> :
							</td>
							<td class="greybox2wk" >
								<s:select multiple="true" size="3" cssClass="selectwk" list="dropdownData.budgetHeadList" 
								name="dropDownBudgetHeads" id="dropDownBudgetHeads" value="%{dropDownBudgetHeads}" /> 
									<div align="right" class="mandatory"
										style="font-size: 11px; padding-right: 20px;">
									</div>
							</td>
							<td width="11%" class="greyboxwk">
								<s:text name='contractorwiseAbstractReport.depositcodes'/> :
							</td>
							<td class="greybox2wk" >
								<s:select multiple="true" size="5"  cssClass="selectwk" list="dropdownData.depositCodeList"
								 name="dropDownDepositCodes" id="dropDownDepositCodes" value="%{dropDownDepositCodes}" /> 
									<div align="right" class="mandatory"
										style="font-size: 11px; padding-right: 20px;">
									</div>
							</td>
						</tr>
						<tr>
							<td class="whiteboxwk"><s:text name='contractorwiseAbstractReport.depositCOA'/> : </td>
			                <td class="whitebox2wk" colspan="3">
								<s:select multiple="true" size="4"  name="allDepositCodes" id="allDepositCodes" cssClass="selectwk" list="dropdownData.allDepositCodeCOAList"
									listKey="id" listValue='glcode  + " : " + name' value="%{allDepositCodes}"/>
							</td>
						</tr>
						<tr>
							<td width="11%" class="greyboxwk">
								<s:text name='contractorwiseAbstractReport.budgethead'/> :
							</td>
							<td class="greybox2wk" colspan="3">
								<s:select multiple="true" size="4" cssClass="selectwk" list="dropdownData.allBudgetHeadList" 
								listKey="id" listValue='name' 
								name="allBudgetHeads" id="allBudgetHeads" value="%{allBudgetHeads}" /> 
							</td>
						</tr>
						<tr>
							<td class="whiteboxwk"><s:text name='contractorwiseAbstractReport.contractorname'/> : </td>
			                <td class="whitebox2wk">
        						<div class="yui-skin-sam">
        							<div id="contractorNameSearch_autocomplete">
               							<div>
        									<s:textfield id="contractorNameSearch" name="contractorName" 
        										value="%{contractorName}" cssClass="selectwk" />
        									
        								</div>
        								<span id="contractorNameSearchResults"></span>
        							</div>	
        						</div>
        						<egov:autocomplete name="contractorNameSearch" width="20" 
        							field="contractorNameSearch" url="ajaxWorkProgress!searchAllContractorsForWorkOrder.action?" 
        							queryQuestionMark="false" results="contractorNameSearchResults" 
        							handler="contractorNameSearchSelectionHandler" queryLength="3"/>
        						<s:hidden id="contractorId" name="contractorId" value="%{contractorId}"/>
		        			</td>
		        			<td class="whiteboxwk">
								<s:text name='contractorwiseAbstractReport.contractorclass' />:
							</td>
							<td class="whitebox2wk">
								<s:select id="status" name="gradeId" cssClass="selectwk"
									list="%{dropdownData.gradeList}" listKey="id" listValue="grade" 
									headerKey="-1" headerValue="%{getText('default.dropdown.select')}" />
							</td>
						</tr>
						<tr>
							<td colspan="4" class="shadowwk"></td>
						</tr>	
						<tr>
							<td colspan="4">
								<div class="buttonholdersearch">
									<s:hidden name="reportType" id="reportType" value="deptwise"/>
								    <s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" onclick="return searchOnClick();" method="search"/> &nbsp;&nbsp;&nbsp;
								    <input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="this.form.reset();">&nbsp;&nbsp;&nbsp;
								    <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<div align="right" class="mandatory"
									style="font-size: 11px; padding-right: 20px;">*<s:text name="default.message.mandatory" />
								</div>
							</td>
						</tr>
						<tr>
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr id="resultRow" ><td colspan="4"> 
						 <s:if test="%{paginatedList.fullListSize!=0}">
						 
							<s:text id="nameLbl" name="%{getText('contractorwiseAbstractReport.contractorname')}"></s:text>
							<s:text id="codeLbl" name="%{getText('contractorwiseAbstractReport.contractorcode')}"></s:text>
							<s:text id="classLbl" name="%{getText('contractorwiseAbstractReport.contractorclass')}"></s:text>
							<s:text id="takenUpCnt" name="%{getText('contractorwiseAbstractReport.takenUp.estCount')}"></s:text>
							<s:text id="takenUpAmt" name="%{getText('contractorwiseAbstractReport.takenUp.amount')}"></s:text>
							<s:text id="completedCnt" name="%{getText('contractorwiseAbstractReport.completed.estCount')}"></s:text>
							<s:text id="completedAmtLbl" name="%{getText('contractorwiseAbstractReport.completed.amount')}"></s:text>
							<s:text id="inprogressCnt" name="%{getText('contractorwiseAbstractReport.inprogress.estCount')}"></s:text>
							<s:text id="inprogressAmt" name="%{getText('contractorwiseAbstractReport.inprogress.amount')}"></s:text>
							<s:text id="inprogressPmt" name="%{getText('contractorwiseAbstractReport.inprogress.payment')}"></s:text>
							<s:text id="inprogressBal" name="%{getText('contractorwiseAbstractReport.inprogress.balance')}"></s:text>
							<s:text id="notYetStartedCnt" name="%{getText('contractorwiseAbstractReport.notYetStarted.estCount')}"></s:text>
							<s:text id="notYetStartedAmt" name="%{getText('contractorwiseAbstractReport.notYetStarted.amount')}"></s:text>
							<s:text id="balanceCnt" name="%{getText('contractorwiseAbstractReport.balance.estCount')}"></s:text>
							<s:text id="balanceAmtLbl" name="%{getText('contractorwiseAbstractReport.balance.amount')}"></s:text>
						 
							 <div style='font-weight:bold;text-align:center' >
								<s:property value="%{subHeader}" />.<br><s:property value="%{reportMessage}" />
		    				</div>
							 <div> 
		  				        <display:table name="paginatedList" uid="currentRowObject" cellpadding="0" cellspacing="0" pagesize="30"
		  				        	export="false" id="currentRow" requestURI="" class="table-header-fix" 
             						style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;align:center" >
	             					<display:column headerClass="pagetableth" class="pagetabletd" title="Sl. No"
								   		titleKey="column.title.SLNo" style="text-align:left" >
								   		<s:if test="%{#attr.currentRow.contractorName!='Total' }">
								     		<s:property value="%{#attr.currentRow_rowNum + (page-1)*pageSize}"/>
								     	</s:if>	
									</display:column>		
	       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:left" title="${nameLbl}"  property="contractorName" />
	       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:left" title="${codeLbl}"  property="contractorCode" />
	       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:left" title="${classLbl}"  property="contractorClass" />
	       							<display:column  headerClass="pagetableth" class="pagetabletd" title="${takenUpCnt }"
	              							style="text-align:right;">
	              						<s:if test="%{#attr.currentRow.contractorName!='Total' && #attr.currentRow.takenUpEstimateCount!=0}">             							
	              							<a href="Javascript:viewEstimatesTakenUpDrillDown('<s:property  value='%{#attr.currentRow.contractorId}' />', '<s:property  value='%{#attr.currentRow.contractorName}' />')">
												<s:property value="#attr.currentRow.takenUpEstimateCount" />
											</a>
										</s:if>
										<s:else>
											<s:property value="#attr.currentRow.takenUpEstimateCount" />
										</s:else>	    
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${takenUpAmt }"
	              							style="text-align:right;">
	              							<s:property value="#attr.currentRow.takenUpWOAmount" />
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${completedCnt }"
	              							style="text-align:right;">               							
										<s:property  value='%{#attr.currentRow.completedEstimateCount}' />
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="Completed<br>-<br>Amount (Cr)" 
	              							style="text-align:right;">
	              						<s:property value="#attr.currentRow.completedWOAmount" />	
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${inprogressCnt }"
	              							style="text-align:right;">     
	              						<s:property  value='%{#attr.currentRow.inProgressEstimateCount}' />
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${inprogressAmt }"
	             							style="text-align:right;">
	             						<s:property value="#attr.currentRow.inProgressTenderNegotiatedAmt" />	
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${inprogressPmt }"
	              							style="text-align:right;">
	              						<s:property value="#attr.currentRow.inProgressPaymentReleasedAmt" />	
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${inprogressBal }"
	              							style="text-align:right;">
	              						<s:property value="#attr.currentRow.inProgressBalanceAmount" />	
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${notYetStartedCnt }"
	              							style="text-align:right;">  
										<s:property value="#attr.currentRow.notYetStartedEstimateCount" />
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${notYetStartedAmt }"
	              							style="text-align:right;">
	              						<s:property value="#attr.currentRow.notYetStartedWOAmount" />		
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${balanceCnt }"
	              							style="text-align:right;">  
										<s:property  value='%{#attr.currentRow.balanceEstimateCount}' />
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="Balance<br>-<br>Amount (Cr)" 
	              							style="text-align:right;">
	              						<s:property value="#attr.currentRow.balanceAmount" />
									</display:column>
								</display:table>
								<br />
								<div class="buttonholderwk" id="divButRow1" name="divButRow1">
									<s:submit cssClass="buttonpdf" value="VIEW PDF" onclick="disableMasking();" id="pdfButton" name="pdfButton" method="generatePDF"/> 
									<s:submit cssClass="buttonpdf" value="VIEW XLS" onclick="disableMasking();" id="pdfButton" name="pdfButton" method="generateXLS"/>
								</div>
							</div>
						</s:if>
						<s:else>
							<s:if test="%{resultStatus=='afterSearch'}">
								<div align="center"><font color="red">No record Found.</font></div>
							</s:if>	
						</s:else>
						</td></tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	</div>
	</div>
	</div>
</s:form>

</body>
</html>
