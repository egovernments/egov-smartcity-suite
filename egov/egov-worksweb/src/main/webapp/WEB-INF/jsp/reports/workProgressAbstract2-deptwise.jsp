<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>

<script type="text/javascript" src="<egov:url path='js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />
<script src="<egov:url path='js/workProgressAbstractReportHelper.js'/>"></script>
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
	width			: <s:if test="%{isDepositCodesSelected}">318%</s:if><s:else>340%</s:else>;
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
<title><s:text name='work.progress.abstract.report2.title' /></title>
	<script type="text/javascript">
	jQuery.noConflict();
	var ratioArray=new Array(120,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,85,85,80,65,65,65,85,85,80,65,65,65,65,65);
	<s:if test="%{isDepositCodesSelected}">
		ratioArray=new Array(120,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,85,85,80,65,65,65,85,85,80,65);
	</s:if>
	jQuery(document).ready(function() {
	    jQuery('.table-header-fix').fixheadertable({
	         caption : "<s:text name='work.progress.abstract.report2.title' />",
	         height  : 400,
	         resizeCol	 : true, 
	         whiteSpace     : 'normal',
	         width : 1225 ,	    
	         wrapper        : false,
	       	 colratio : ratioArray
	    });
	});
	
	function searchOnClick()
	{
		if(!validate())
			return false;
		loadWaitingImage();
		hideResult();
		generateSubHeader("2");
		return true;
	}
	function validate() 
	{
		var finYearVal = dom.get("finYearId").value;
		var toDateVal = dom.get("toDate").value;
		var budgetHeadsDD = document.getElementById("dropDownBudgetHeads");
		var depositCodesDD = document.getElementById("dropDownDepositCodes");
		var allbudgetHeadsDD = document.getElementById("allBudgetHeads");
		var alldepositCodesDD = document.getElementById("allDepositCodes");
		var allSelected =false;
		var oneSelected = false;
		var oneSelectedDepCode = false;
		var oneSelectedAllBudgetHeads = false;
		var oneSelectedAllDepCode = false;
		if(finYearVal == -1)
		{
			document.getElementById("workProgress_error").style.display='';
		  	document.getElementById("workProgress_error").innerHTML='<s:text name="work.progress.abstract.report2.select.finyr"/>';
		  	window.scroll(0,0);
		  	return false;
		}
		if(toDateVal == "")
		{
			document.getElementById("workProgress_error").style.display='';
		  	document.getElementById("workProgress_error").innerHTML='<s:text name="work.progress.abstract.report2.validate.toDate"/>';
		  	window.scroll(0,0);
		  	return false;
		}
		if(!validateDate(toDateVal))
		{
			document.getElementById("workProgress_error").style.display='';
		  	document.getElementById("workProgress_error").innerHTML='<s:text name="work.progress.abstract.report2.invalid.toDate"/>';
		  	window.scroll(0,0);
		  	return false;
		}
		if(compareDate(dom.get("fromDate").value,toDateVal)==-1)
		{
			document.getElementById("workProgress_error").style.display='';
		  	document.getElementById("workProgress_error").innerHTML='<s:text name="work.progress.abstract.report2.invalid.fromDate.toDate"/>';
		  	window.scroll(0,0);
		  	return false;
		}		
		for (var i = 0; i < budgetHeadsDD.options.length; i++) {
		     if(budgetHeadsDD.options[i].selected ==true){
			     oneSelected = true;
			     if(i==0)
			    	 allSelected = true;
			     if(allSelected &&  i!=0)
			     {
			    	 document.getElementById("workProgress_error").style.display='';
				  	 document.getElementById("workProgress_error").innerHTML='<s:text name="work.progress.abstract.report2.invalid.select"/>';
				  	 window.scroll(0,0);
				  	 return false;
			     }    
		     }
		}
		allSelected = false;
		for (var i = 0; i < depositCodesDD.options.length; i++) {
		     if(depositCodesDD.options[i].selected ==true){
		    	 oneSelectedDepCode = true;
			     if(i==0)
			    	 allSelected = true;
			     if(allSelected &&  i!=0)
			     {
			    	 document.getElementById("workProgress_error").style.display='';
				  	 document.getElementById("workProgress_error").innerHTML='<s:text name="work.progress.abstract.report2.invalid.depositcodes.select"/>';
				  	 window.scroll(0,0);
				  	 return false;
			     }    
		     }
		}
		if(oneSelectedDepCode && oneSelected)
		{
			document.getElementById("workProgress_error").style.display='';
		  	 document.getElementById("workProgress_error").innerHTML='<s:text name="work.progress.abstract.report2.select.budgetheads.or.depositcode"/>';
		  	 window.scroll(0,0);
		  	 return false;	
		}
		for (var i = 0; i < allbudgetHeadsDD.options.length; i++) {
		     if(allbudgetHeadsDD.options[i].selected ==true){
		    	 oneSelectedAllBudgetHeads = true;
		     }
		}
		if((oneSelectedDepCode || oneSelected) && oneSelectedAllBudgetHeads)
		{
			document.getElementById("workProgress_error").style.display='';
		  	 document.getElementById("workProgress_error").innerHTML='<s:text name="work.progress.abstract.report2.select.budgetheads.or.depositcode"/>';
		  	 window.scroll(0,0);
		  	 return false;	
		}
		for (var i = 0; i < alldepositCodesDD.options.length; i++) {
		     if(alldepositCodesDD.options[i].selected ==true){
		    	 oneSelectedAllDepCode = true;
		     }
		}
		if((oneSelectedDepCode || oneSelected || oneSelectedAllBudgetHeads) && oneSelectedAllDepCode)
		{
			document.getElementById("workProgress_error").style.display='';
		  	 document.getElementById("workProgress_error").innerHTML='<s:text name="work.progress.abstract.report2.select.budgetheads.or.depositcode"/>';
		  	 window.scroll(0,0);
		  	 return false;	
		}
		if(!(oneSelectedDepCode || oneSelected || oneSelectedAllBudgetHeads || oneSelectedAllDepCode))
		{
			document.getElementById("workProgress_error").style.display='';
		  	 document.getElementById("workProgress_error").innerHTML='<s:text name="work.progress.abstract.report2.select.budgetheads.or.depositcode"/>';
		  	 window.scroll(0,0);
		  	 return false;	
		}
		document.getElementById("workProgress_error").style.display='none';
	  	document.getElementById("workProgress_error").innerHTML='';
	  	return true;
	}
	
	function setEstimateDateRange(elem){
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

function disableMasking(){
	document.getElementById('loadingMask').remove();
	generateSubHeader();
}
	
</script>

<body onload="setDateOnLoadForCurrFinYear();">
	<div class="errorstyle" id="workProgress_error"	style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="workProgressAbstractForm" id="workProgressAbstractForm" onsubmit="return validate();" theme="simple">
		<s:hidden name="subHeader" id="subHeader" />
		<s:hidden name="departmentName" id="departmentName" />
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
										src="${pageContext.request.contextPath}/image/arrow.gif" />
								</div>
								<div class="headplacer">
									<s:text name="page.subheader.search.estimate" />
								</div>
							</td>
						</tr>
						<tr>
							<td width="11%" class="whiteboxwk">
								<span class="mandatory">*</span><s:text name='workprogressabstract2.finyear'/> :
							</td>
							<td width="21%" class="whitebox2wk" colspan="3">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="finYearId" 
								id="finYearId" cssClass="selectwk" list="dropdownData.finYearList" listKey="id" 
								listValue="finYearRange" value="%{finYearId}" onchange="setEstimateDateRange(this);"/>
								
							</td>
						</tr>
						
						<tr>
							<td class="greyboxwk">
								<s:text name="workprogressabstract.fromdate" />
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
								<s:text name="workprogressabstract.todate" />
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
								<s:text name='workprogressabstract.executing.department'/> :
							</td>
							<td width="21%" class="whitebox2wk" >
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="executingDepartment" 
								id="executingDepartment" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id" 
								listValue="deptName" value="%{executingDepartment}" />
								
							</td>
							<td class="whiteboxwk"><s:text name='workprogressabstract.createdby'/> :</td >
							<td class="whitebox2wk" >
								<s:select headerKey="-1"
											headerValue="%{getText('default.dropdown.select')}"
											name="preparedBy" value="%{preparedBy}" id="preparedBy"
											cssClass="selectwk" list="dropdownData.preparedByList"
											listKey="id" listValue="employeeName" />
							</td>
							<s:if test="%{hiddenField!='no'}">
								<td width="15%" class="whiteboxwk">
									<s:text name="workprogressabstract.work.nature" />
									:
								</td>
								<td width="53%" class="whitebox2wk">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
										name="natureOfWork" id="natureOfWork" cssClass="selectwk"
										list="dropdownData.typeList" listKey="id" listValue="name"
										value="%{natureOfWork}" />
								</td>
							</s:if>
							<s:else>
								<td width="15%" class="whiteboxwk" style="display: none;">
									<s:text name="workprogressabstract.work.nature" />
									:
								</td>
								<td width="53%" class="whitebox2wk" style="display: none;">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
										name="natureOfWork" id="natureOfWork" cssClass="selectwk"
										list="dropdownData.typeList" listKey="id" listValue="name"
										value="%{natureOfWork}" />
								</td>
							</s:else>
						</tr>
						<tr>
							<td class="greyboxwk">
								<s:text name="workprogressabstract.work.type" />
								:
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
								<s:text name="workprogressabstract.work.subtype" />
								:
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
								<s:text name='workprogressabstract.fund'/> :				
							</td>
							<td width="53%" class="whitebox2wk">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
								name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" 
								listValue="name" value="%{fund}" />
							</td>
							<td width="11%" class="whiteboxwk">
								<s:text name='workprogressabstract.function'/> :
							</td>
							<td width="21%" class="whitebox2wk" colspan="3">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
				 					name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" 
				 					listKey="id" listValue="name" value="%{function}"   />
							</td>
						</tr>
						<tr>
			                <td class="whiteboxwk"><s:text name='workprogressabstract.scheme'/> : </td>
			                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="scheme" id="scheme" cssClass="selectwk" list="dropdownData.schemeList" listKey="id" listValue="name" value="%{scheme}"  onChange="setupSubSchemes(this);"/>
							<egov:ajaxdropdown id="subSchemeDropdown" fields="['Text','Value']" dropdownId='subScheme' url='reports/ajaxWorkProgress!loadSubSchemes.action' selectedValue="%{scheme.id}"/></td>
			                <td class="whiteboxwk"><s:text name='workprogressabstract.subscheme'/> : </td>
			                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="subScheme" id="subScheme" cssClass="selectwk" list="dropdownData.subSchemeList" listKey="id" listValue="name" value="%{subScheme}" /></td>
						</tr>
						<tr>
							<td width="11%" class="greyboxwk">
								<s:text name='workprogressabstract.budgethead'/> :
							</td>
							<td class="greybox2wk" >
								<s:select multiple="true" size="3" cssClass="selectwk" list="dropdownData.budgetHeadList" 
								name="dropDownBudgetHeads" id="dropDownBudgetHeads" value="%{dropDownBudgetHeads}" /> 
									<div align="right" class="mandatory"
										style="font-size: 11px; padding-right: 20px;">
									</div>
							</td>
							<td width="11%" class="greyboxwk">
								<s:text name='workprogressabstract.depositcodes'/> :
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
							<td class="whiteboxwk"><s:text name='workprogressabstract.depositCOA'/> : </td>
			                <td class="whitebox2wk" colspan="3">
								<s:select multiple="true" size="4"  name="allDepositCodes" id="allDepositCodes" cssClass="selectwk" list="dropdownData.coaList"
									listKey="id" listValue='glcode  + " : " + name' value="%{allDepositCodes}"/>
							</td>
						</tr>
						<tr>
							<td width="11%" class="greyboxwk">
								<s:text name='workprogressabstract.budgethead'/> :
							</td>
							<td class="greybox2wk" colspan="3">
								<s:select multiple="true" size="4" cssClass="selectwk" list="dropdownData.allBudgetHeadList" 
								listKey="id" listValue='name' 
								name="allBudgetHeads" id="allBudgetHeads" value="%{allBudgetHeads}" /> 
							</td>
						</tr>
						<tr style="display: none;">
			                <td class="whiteboxwk"><s:text name='workprogressabstract.depositCOA'/> : </td>
			                <td class="whitebox2wk" colspan="3">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" 
									name="coa" id="coa" cssClass="selectwk" list="dropdownData.coaList"
									listKey="id" listValue='glcode  + " : " + name' value="%{coa}"/>
							</td>
						</tr>
						<tr>
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
									style="font-size: 11px; padding-right: 20px;">
									*
									<s:text name="default.message.mandatory" />
								</div>
							</td>
						</tr>
						<tr>
						</tr>
						<tr><td colspan="4">&nbsp;</td></tr>
						<tr id="resultRow" ><td colspan="4"> 
						 <s:if test="%{searchResult.size()!=0}">
						 
						 <s:text id="spillOverEstimatesCountLabel" name="%{getText('work.progress.abstract.report2.noOfSpillOverEstimates')}"></s:text>
						 <s:text id="tenderYetToBeCalledEstimatesCountLabel" name="%{getText('work.progress.abstract.report2.noOfTenderYetToBeCalledEstimates')}"></s:text>
						 <s:text id="tenderYetToBeCalledEstimatesValueLabel" name="%{getText('work.progress.abstract.report2.tenderYetToBeCalledEstimatesValue')}"></s:text>
						 <s:text id="tenderYetToBeFinalizeWPCountLabel" name="%{getText('work.progress.abstract.report2.noOfTenderYetToBeFinalizeWP')}"></s:text>
						 <s:text id="tenderYetToBeFinalizeEstimatesCountLabel" name="%{getText('work.progress.abstract.report2.noOfTenderYetToBeFinalizeEstimates')}"></s:text>
						 <s:text id="tenderYetToBeFinalizeEstimatesValueLabel" name="%{getText('work.progress.abstract.report2.noOfTenderYetToBeFinalizeEstimate.value')}"></s:text>
						 
						 <s:text id="woYetToBeGivenTNLabel" name="%{getText('work.progress.abstract.report2.woYetToBeGivenTN')}"></s:text>
						 <s:text id="woYetToBeGivenEstimateLabel" name="%{getText('work.progress.abstract.report2.woYetToBeGivenEstimate')}"></s:text>
						 <s:text id="woYetToBeGivenEstimateValueLabel" name="%{getText('work.progress.abstract.report2.woYetToBeGivenEstimateValue')}"></s:text>
						 
						 <s:text id="noOfMBsApprovedLabel" name="%{getText('work.progress.abstract.report2.noOfMBsApproved')}"></s:text>
						 <s:text id="noOfContractorBillsApprovedLabel" name="%{getText('work.progress.abstract.report2.noOfContractorBillsApproved')}"></s:text>						 
						 <s:text id="noOfMBsInApprovedContractorBillsLabel" name="%{getText('work.progress.abstract.report2.noOfMBsInApprovedContractorBills')}"></s:text>
						 <s:text id="noOfMBsPendingForContractorBillCreateLabel" name="%{getText('work.progress.abstract.report2.noOfMBsPendingForContractorBillCreate')}"></s:text>
						 <s:text id="billsYetToBeCreatedValueLabel" name="%{getText('work.progress.abstract.report2.billsYetToBeCreatedValueLabel')}"></s:text>
						 
						 <s:text id="balBudgetNewWorksLabel" name="%{getText('work.progress.abstract.report2.balance.bdgt.new.works')}"></s:text>
						 <s:text id="balBudgetSOWorksLabel" name="%{getText('work.progress.abstract.report2.balance.bdgt.spillovr.works')}"></s:text>
						 
						 <s:text id="tenderCalledEstimateValueLabel" name="%{getText('work.progress.abstract.report2.tenderCalledEstValue')}"></s:text>
						 <s:text id="tenderFinalisedEstimateValueLabel" name="%{getText('work.progress.abstract.report2.tenderFinalisedEstValue')}"></s:text>
						 
						 <div style='font-weight:bold;text-align:center' >
							<s:property value="%{subHeader}" />
	    				</div>
						 <div> 
	  				        <display:table name="searchResult" uid="currentRowObject" cellpadding="0" cellspacing="0" 
	  				        	export="false" id="currentRow" class="table-header-fix" requestURI="">
             							
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:left" title="Department"  property="department" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" title="Estimates Prepared"
              							style="text-align:right;">    
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.estimatesPrepared!=0}">             							
	              							<a href="Javascript:viewEstimatesDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','EST_PREPARED')">
									<s:property value="#attr.currentRow.estimatesPrepared" />
											</a>
										</s:if>
										<s:else>
												<s:property value="#attr.currentRow.estimatesPrepared" />
										</s:else>
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Value of Estimate Prepared (Cr)"
              							style="text-align:right;">               							
									 <s:property  value='%{#attr.currentRow.estPreparedValue}' />
								</display:column>
								<s:if test="%{!isDepositCodesSelected}">
									<display:column  headerClass="pagetableth" class="pagetabletd" title="Budget Amount for ${finYearRange} (Cr)"
	              							style="text-align:right;">               							
										 <s:property  value='%{#attr.currentRow.budgetAmount}' />
									</display:column>
								</s:if>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${spillOverEstimatesCountLabel}" titleKey='work.progress.abstract.report2.noOfSpillOverEstimates'
              							style="text-align:right;">               							
									 <s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.spilloverWorksEstimateCount!=0}"> 
              							<a href="Javascript:viewEstimatesDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','spilloverWorksEstimate')">
											<s:property  value='%{#attr.currentRow.spilloverWorksEstimateCount}' />
										</a>     
									</s:if>
									<s:else>
										<s:property  value='%{#attr.currentRow.spilloverWorksEstimateCount}' />
									</s:else>
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Spill Over Work Value (Cr)"
              							style="text-align:right;">     
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.spillOverWorkValue!=0.0000}">
              							<a href="#" onclick="viewSpillOverWorkValue('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />')"> 
              								<s:property  value='%{#attr.currentRow.spillOverWorkValue}' />
										</a>
              						</s:if>	
              						<s:else>
              						<s:property  value='%{#attr.currentRow.spillOverWorkValue}' />
              						</s:else>          							
									 
								</display:column>
								<s:if test="%{!isDepositCodesSelected}">
									<display:column  headerClass="pagetableth" class="pagetabletd" title="Budget Available for ${finYearRange} (Cr)"
              							style="text-align:right;">               							
									 	<s:property  value='%{#attr.currentRow.budgetAvailable}' />
									</display:column>
								</s:if>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Admin Sanctioned"
              							style="text-align:right;">     
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.adminSancEstimate!=0}"> 
	              							<a href="Javascript:viewEstimatesDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','adminSanctioned')">
									<s:property value="#attr.currentRow.adminSancEstimate" />
											</a>    
										</s:if>
										<s:else>
												<s:property value="#attr.currentRow.adminSancEstimate" />
										</s:else>   							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="AS - Estimate Value (Cr)"
              							style="text-align:right;">
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.adminSancEstValue!=0}"> 
              							<a href="Javascript:viewEstimatesDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','adminSancEstValue')">
											<s:property  value='%{#attr.currentRow.adminSancEstValue}' />
										</a>     
									</s:if>
									<s:else>
										<s:property  value='%{#attr.currentRow.adminSancEstValue}' />
									</s:else>	               							
									 
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Balance No of Estimates"
              							style="text-align:right;">  
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.estimateBalance!=0}"> 
	              							<a href="Javascript:viewEstimatesDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','BALANCE_EST')">
												<s:property value="#attr.currentRow.estimateBalance" />
											</a>     
										</s:if>     
										<s:else>
									<s:property value="#attr.currentRow.estimateBalance" />
										</s:else>
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Balance Estimates Value (Cr)"
              							style="text-align:right;">         							
									 <s:property  value='%{#attr.currentRow.estBalanceValue}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Tender Called - WP"
              							style="text-align:right;">  
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderCalledWP!=0}">
              							<a href="#" onclick="showWPDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','tenderCalledWP')"> 
       								<s:property  value='%{#attr.currentRow.tenderCalledWP}' />
										</a>
              						</s:if>	
              						<s:else>
              								<s:property  value='%{#attr.currentRow.tenderCalledWP}' />
              						</s:else>
              					</display:column>
              					
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Tender Called - Estimates"
              							style="text-align:right;">  
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderCalledEst!=0}">
              						<a href="#" onclick="showEstDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />')"> 
									<s:property  value='%{#attr.currentRow.tenderCalledEst}' />
									</a>         
									</s:if>
									<s:else>
										<s:property  value='%{#attr.currentRow.tenderCalledEst}' />
									</s:else>   							
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${tenderCalledEstimateValueLabel}" titleKey='work.progress.abstract.report2.tenderCalledEstValue' 
              							style="text-align:right;">  
											<s:property  value='%{#attr.currentRow.tenderCalledEstValue}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${tenderYetToBeCalledEstimatesCountLabel}" titleKey='work.progress.abstract.report2.noOfTenderYetToBeCalledEstimates' 
              							style="text-align:right;">  
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderYetToBeCalledEstimateCount!=0}"> 
	              							<a href="Javascript:viewEstimatesDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','tenderYetToBeCalledEstimate')">
												<s:property value="#attr.currentRow.tenderYetToBeCalledEstimateCount" />
											</a>    
										</s:if>
										<s:else>
											<s:property  value='%{#attr.currentRow.tenderYetToBeCalledEstimateCount}' />
										</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${tenderYetToBeCalledEstimatesValueLabel}" titleKey='work.progress.abstract.report2.tenderYetToBeCalledEstimatesValue' 
              							style="text-align:right;">  
											<s:property  value='%{#attr.currentRow.tenderYetToBeCalledEstValue}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Tender Finalized - TN"
              							style="text-align:right;">  
									
										<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderFinalisedWP!=0}"> 
	              							<a href="Javascript:viewTenderFinalizedDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />', 'TenderFinalizedTN')">
												<s:property value="#attr.currentRow.tenderFinalisedWP" />
											</a>     
										</s:if>     
										<s:else>
												<s:property value="#attr.currentRow.tenderFinalisedWP" />
										</s:else>           							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Tender Finalized - Estimates"
              							style="text-align:right;">       
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderFinalisedEst!=0}"> 
	              							<a href="Javascript:viewTenderFinalizedDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />', 'TenderFinalizedEst')">
												<s:property value="#attr.currentRow.tenderFinalisedEst" />
											</a>     
										</s:if>     
										<s:else>
												<s:property value="#attr.currentRow.tenderFinalisedEst" />
										</s:else> 
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${tenderFinalisedEstimateValueLabel}" titleKey='work.progress.abstract.report2.tenderFinalisedEstValue' 
              							style="text-align:right;">  
											<s:property  value='%{#attr.currentRow.tenderFinalizedEstValue}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${tenderYetToBeFinalizeWPCountLabel}" titleKey='work.progress.abstract.report2.noOfTenderYetToBeFinalizeWP' 
              							style="text-align:right;">
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderYetToBeFinalizedEstCount!=0}"> 
	              							<a href="Javascript:showWPDetails('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','tenderYetToBeFinalisedWP')">
												<s:property  value='%{#attr.currentRow.tenderYetToBeFinalizedWPCount}' />
											</a>    
										</s:if>
										<s:else>
											<s:property  value='%{#attr.currentRow.tenderYetToBeFinalizedWPCount}' />
										</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${tenderYetToBeFinalizeEstimatesCountLabel}" titleKey='work.progress.abstract.report2.noOfTenderYetToBeFinalizeEstimates' 
              							style="text-align:right;">
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.tenderYetToBeFinalizedEstCount!=0}"> 
	              							<a href="Javascript:viewEstimatesDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','tenderYetToBeFinalisedEstimate')">
												<s:property  value='%{#attr.currentRow.tenderYetToBeFinalizedEstCount}' />
											</a>    
										</s:if>
										<s:else>
											<s:property  value='%{#attr.currentRow.tenderYetToBeFinalizedEstCount}' />
										</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${tenderYetToBeFinalizeEstimatesValueLabel}" titleKey='work.progress.abstract.report2.noOfTenderYetToBeFinalizeEstimate.value' 
              							style="text-align:right;">
											<s:property  value='%{#attr.currentRow.tenderYetToBeFinalizedEstValue}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Order"
              							style="text-align:right;">         
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workOrderWP!=0}">
              							<a href="#" onclick="showWODetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkOrder')"> 
              								 <s:property  value='%{#attr.currentRow.workOrderWP}' />
										</a>
              						</s:if>	
              						<s:else>
              						<s:property  value='%{#attr.currentRow.workOrderWP}' />
              						</s:else>	     							
									
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Order Estimates"
              							style="text-align:right;">    
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workOrderEst!=0}">
              							<a href="#" onclick="showWOEstDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkOrderEstimates')"> 
              								<s:property  value='%{#attr.currentRow.workOrderEst}' />
										</a>
              						</s:if>	
              						<s:else>
              						<s:property  value='%{#attr.currentRow.workOrderEst}' />
              						</s:else>	          							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Value (Cr)"
              							style="text-align:right;">    
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workOrderAmt!=0.0000}">
              							<a href="#" onclick="ViewWorkValueDrillDown('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkValue')"> 
              								<s:property  value='%{#attr.currentRow.workOrderAmt}' />
										</a>
              						</s:if>	
              						<s:else>
              						<s:property  value='%{#attr.currentRow.workOrderAmt}' />
              						</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${woYetToBeGivenTNLabel}" titleKey='work.progress.abstract.report2.woYetToBeGivenTN' 
              							style="text-align:right;"> 
              							<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.woYetToBeGivenTNCount!=0}"> 
	              							<a href="Javascript:viewTenderFinalizedDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />', 'WOYetToBeGivenTN')">
												<s:property  value='%{#attr.currentRow.woYetToBeGivenTNCount}' />
											</a>     
										</s:if>     
										<s:else>
												<s:property  value='%{#attr.currentRow.woYetToBeGivenTNCount}' />
										</s:else>    
              					</display:column>
              					
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${woYetToBeGivenEstimateLabel}" titleKey='work.progress.abstract.report2.woYetToBeGivenEstimate' 
              							style="text-align:right;"> 
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.woYetToBeGivenEstimateCount!=0}">
              							<a href="#" onclick="showWOYetToBeCreatedEstDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WOYetToBeCreatedEstimate')"> 
              								<s:property  value='%{#attr.currentRow.woYetToBeGivenEstimateCount}' />
										</a>
              						</s:if>	
              						<s:else>
              							<s:property  value='%{#attr.currentRow.woYetToBeGivenEstimateCount}' />
              						</s:else>	
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${woYetToBeGivenEstimateValueLabel}" titleKey='work.progress.abstract.report2.woYetToBeGivenEstimateValue' 
              							style="text-align:right;"> 
              								<s:property  value='%{#attr.currentRow.woYetToBeGivenEstimateValue}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Not Started - Estimates"
              							style="text-align:right;">  
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workNotStartedEst!=0}">
              							<a href="#" onclick="showWOEstDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkNotStartedEstimates')"> 
              						<s:property  value='%{#attr.currentRow.workNotStartedEst}' />
										</a>
              						</s:if>	
              						<s:else>
              								<s:property  value='%{#attr.currentRow.workNotStartedEst}' />
              						</s:else>
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Not Started-Work Value (Cr)"
              							style="text-align:right;">
              						<s:property  value='%{#attr.currentRow.workNotStartedAmt}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Started - Estimates"
              							style="text-align:right;"> 
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.workStartedEst!=0}">
              							<a href="#" onclick="showWOEstDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorkStartedEstimates')"> 
              							<s:property  value='%{#attr.currentRow.workStartedEst}' />
										</a>
              						</s:if>	
              						<s:else>
              								<s:property  value='%{#attr.currentRow.workStartedEst}' />
              						</s:else>	             							
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work Started - Work Value (Cr)"
              							style="text-align:right;">  
              						<s:property  value='%{#attr.currentRow.workStartedAmt}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Inprogress 1- 25 %"
              							style="text-align:right;"> 
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.inProgress25!=0}">
              							<a href="#" onclick="showMilestoneDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','InProgress25')"> 
              								<s:property  value='%{#attr.currentRow.inProgress25}' />
										</a>
              						</s:if>	
              						<s:else>
              							<s:property  value='%{#attr.currentRow.inProgress25}' />
              						</s:else>	            							
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Inprogress 26- 50 %"
              							style="text-align:right;">
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.inProgress50!=0}">
              							<a href="#" onclick="showMilestoneDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','InProgress50')"> 
              								<s:property  value='%{#attr.currentRow.inProgress50}' />
										</a>
              						</s:if>	
              						<s:else>
              							<s:property  value='%{#attr.currentRow.inProgress50}' />
              						</s:else>	              							
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Inprogress 51- 75 %"
              							style="text-align:right;">   
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.inProgress75!=0}">
              							<a href="#" onclick="showMilestoneDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','InProgress75')"> 
              								<s:property  value='%{#attr.currentRow.inProgress75}' />
										</a>
              						</s:if>	
              						<s:else>
              							<s:property  value='%{#attr.currentRow.inProgress75}' />
              						</s:else>	           							
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Inprogress 76- 99 %"
              							style="text-align:right;">    
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.inProgress99!=0}">
              							<a href="#" onclick="showMilestoneDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','InProgress99')"> 
              								<s:property  value='%{#attr.currentRow.inProgress99}' />
										</a>
              						</s:if>	
              						<s:else>
              							<s:property  value='%{#attr.currentRow.inProgress99}' />
              						</s:else>	          							
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Works Completed"
              							style="text-align:right;">  
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.worksCompleted!=0}">
              							<a href="#" onclick="showMilestoneDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorksCompleted')"> 
              								<s:property  value='%{#attr.currentRow.worksCompleted}' />
										</a>
              						</s:if>	
              						<s:else>
              							<s:property  value='%{#attr.currentRow.worksCompleted}' />
              						</s:else>	            							
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="No. of Works yet to be completed"
              							style="text-align:right;">
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.worksNotCompleted!=0}">
              							<a href="#" onclick="showMilestoneDetails('2','${pageContext.request.contextPath}','<s:property value='%{#attr.currentRow.department}' />','WorksNotCompleted')"> 
              								<s:property  value='%{#attr.currentRow.worksNotCompleted}' />
										</a>
              						</s:if>	
              						<s:else>
              							<s:property  value='%{#attr.currentRow.worksNotCompleted}' />
              						</s:else>	              							
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${noOfMBsApprovedLabel}" titleKey='work.progress.abstract.report2.noOfMBsApproved' 
              							style="text-align:right;">  
										<s:property  value='%{#attr.currentRow.approvedMBCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${noOfContractorBillsApprovedLabel}" titleKey='work.progress.abstract.report2.noOfContractorBillsApproved' 
              							style="text-align:right;">  
										<s:property  value='%{#attr.currentRow.approvedBillCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${noOfMBsInApprovedContractorBillsLabel}" titleKey='work.progress.abstract.report2.noOfMBsInApprovedContractorBills' 
              							style="text-align:right;">   
										<s:property  value='%{#attr.currentRow.mbCoveredByBillsCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${noOfMBsPendingForContractorBillCreateLabel}" titleKey='work.progress.abstract.report2.noOfMBsPendingForContractorBillCreate' 
              							style="text-align:right;">  
										<s:property  value='%{#attr.currentRow.billsYetToBeCreatedCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${billsYetToBeCreatedValueLabel}" titleKey='work.progress.abstract.report2.billsYetToBeCreatedValueLabel' 
              							style="text-align:right;">  
										<s:property  value='%{#attr.currentRow.billsYetToBeCreatedValue}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Total No. of completed works"
              							style="text-align:right;">              							
									 <s:property  value='%{#attr.currentRow.numberOfCompletedWorks}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Work value of completed works(Cr)"
              							style="text-align:right;">              							
									 <s:property  value='%{#attr.currentRow.valueOfCompletedWorks}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="No. of CJV Approved for ${finYearRange} Works"
              							style="text-align:right;">              							
									 <s:property  value='%{#attr.currentRow.approvedCJVCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="CJV Approved Amount for ${finYearRange} (Cr)"
              							style="text-align:right;">               							
									 <s:property  value='%{#attr.currentRow.approvedCJVAmount}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="No. of BPV Approved for ${finYearRange} Works (Concurrence)"
              							style="text-align:right;">              							
									 <s:property  value='%{#attr.currentRow.concurrenceVoucherCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Payment Released for ${finYearRange} Works (Concurrence) (Cr)"
              							style="text-align:right;">               							
									 <s:property  value='%{#attr.currentRow.concurrencePaymentAmount}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="No. of BPV Approved for ${finYearRange} Works"
              							style="text-align:right;">              							
									 <s:property  value='%{#attr.currentRow.approvedBPVCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Payment Released for ${finYearRange} Works (Cr)"
              							style="text-align:right;">               							
									 <s:property  value='%{#attr.currentRow.approvedBPVAmount}' />
								</display:column>								
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="No. of CJV Approved for Spillover Works"
              							style="text-align:right;">              							
									 <s:property  value='%{#attr.currentRow.approvedCJVForSpilloverCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="CJV Approved Amount for Spillover Works (Cr)"
              							style="text-align:right;">               							
									 <s:property  value='%{#attr.currentRow.approvedCJVForSpilloverAmount}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="No. of BPV Approved for Spillover Works (Concurrence)"
              							style="text-align:right;">              							
									 <s:property  value='%{#attr.currentRow.concurrenceVoucherForSpilloverCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Payment Released for Spillover Works (Concurrence) (Cr)"
              							style="text-align:right;">               							
									 <s:property  value='%{#attr.currentRow.concurrencePaymentForSpilloverAmount}' />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" title="No. of BPV Approved for Spillover Works"
              							style="text-align:right;">              							
									 <s:property  value='%{#attr.currentRow.approvedBPVForSpilloverCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Payment Released for Spillover Works (Cr)"
              							style="text-align:right;">               							
									 <s:property  value='%{#attr.currentRow.approvedBPVForSpilloverAmount}' />
								</display:column>								
							
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Total No. of BPV Approved for the ${finYearRange} year"
              							style="text-align:right;">              							
									 <s:property  value='%{#attr.currentRow.totalVoucherCount}' />
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="Total Payment Released for the ${finYearRange} year (Cr)"
              							style="text-align:right;">               							
									 <s:property  value='%{#attr.currentRow.totalPaymentReleased}' />
								</display:column>
								<s:if test="%{!isDepositCodesSelected}">
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${balBudgetNewWorksLabel}"
              							style="text-align:right;">              							
									 <s:property  value='%{#attr.currentRow.balanceBudget}' />
									</display:column>
									<display:column  headerClass="pagetableth" class="pagetabletd" title="${balBudgetSOWorksLabel}"
	              							style="text-align:right;">               							
										 <s:property  value='%{#attr.currentRow.balanceBudgetForSpillover}' />
									</display:column>
								</s:if>
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