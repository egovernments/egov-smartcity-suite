<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>

<script type="text/javascript" src="<egov:url path='js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />
<script src="<egov:url path='js/workProgressAbstractReportHelper.js'/>"></script>
<script src="<egov:url path='js/workProgressAbstractReport3Helper.js'/>"></script>
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
	width			: 140%;
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
<title><s:text name='work.progress.abstract.report3.title' /></title>
	<script type="text/javascript">
	jQuery.noConflict();
	var ratioArray=new Array(120,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65);
	jQuery(document).ready(function() {
	    jQuery('.table-header-fix').fixheadertable({
	         caption : "<s:text name='work.progress.abstract.report3.title' />",
	         height  : 400,
	         resizeCol	 : true, 
	         whiteSpace     : 'normal',
	         width : 1125 ,	    
	         wrapper        : false,
	       	 colratio : ratioArray
	    });
	    jQuery('.body').css('width', '1125px');
	    jQuery('.body').css('overflow', 'auto');
	    jQuery(".body").scroll(function() {
	    	var scroll = (-(jQuery('.body').scrollLeft()))+"px";
		    jQuery( ".headtable" ).css('margin-left', scroll);
		});
	    jQuery('.t_fixed_header_main_wrapper').css('overflow', 'hidden');
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
	
function disableMasking(){
	document.getElementById('loadingMask').remove();
	generateSubHeader();
}
//TODO - Move the search result to new jsp and include the new jsp in this	
</script>

<body >
	<div class="errorstyle" id="workProgress_error"	style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="workProgressAbstract3Form" id="workProgressAbstract3Form" theme="simple">
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
							<%@ include file='workProgressAbstract3-searchResults.jsp'%>
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