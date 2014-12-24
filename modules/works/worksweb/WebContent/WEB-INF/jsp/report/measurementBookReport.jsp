<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
<script src="<egov:url path='js/works.js'/>"></script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="mb.report.title" /></title>
</head>
<style>
.ui-button
{
	position: absolute;
	height: 2.0em;
}
</style>
<body onload="init();">
<script type="text/javascript">

function validateInput() {
	var mbStatus = document.getElementById("mbStatus").value;
	var fromDate = document.getElementById("fromDate").value;
	var toDate = document.getElementById("toDate").value;

	dom.get("lblError").style.display="";
	document.getElementById('lblError').innerHTML = "";

	if(mbStatus == "" || mbStatus == -1 || mbStatus == 0){
		document.getElementById('lblError').innerHTML = '<s:text name="measurementbook.report.select.status" />';
		document.getElementById("mbStatus").focus;
		return false;
	} 
	
	if(fromDate == ""){
		document.getElementById('lblError').innerHTML = '<s:text name="report.search.fromdate.required" />';
		document.getElementById("fromDate").focus;
		return false;
	}
	else if(toDate == ""){
		document.getElementById('lblError').innerHTML = '<s:text name="report.search.todate.required" />';
		document.getElementById("toDate").focus;
		return false;
	}
	
	dom.get("lblError").style.display="none";
	return true;
}

function clearHiddenBudgetHeadId(obj) {
	if(obj.value=="") {
		document.getElementById("budgetHeadId").value="";
	}	
}

var budgetHeadSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("budgetHeadSearch").value=oData[0];
    dom.get("budgetHeadId").value = oData[1];
};

var budgetHeadSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="measurementbook.report.budget.ajax.error"/>');
};
var jurisdictionSearchSelectionHandler = function(sType, arguments) { 
    var oData = arguments[2];
    dom.get("jurisdictionSearch").value=oData[0];
    dom.get("jurisdictionId").value = oData[1];
};

var jurisdictionSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="measurementbook.report.jurisdiction.ajax.error"/>');
}; 

function clearHiddenJurisdiction(obj) {
	if(obj.value=="") {		
		document.getElementById("jurisdictionId").value="";
	}	
}

function clearHiddenWONumber(obj) {
	if(obj.value=="") {
		document.getElementById("workOrderId").value="";
	}	
}

var workOrderNoSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("workOrderNoSearch").value=oData[0];
    dom.get("workOrderId").value = oData[1];
};

var workOrderNoSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="report.search.workOrderNumber.failure"/>');
};

function clearHiddenEstimateNumber(obj) {
	if(obj.value=="") {
		document.getElementById("estimateId").value="";
	}	
}

var estimateNoSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("estimateNoSearch").value=oData[0];
    //dom.get("estimateId").value = oData[1];
};

var estimateNoSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="report.search.estimateNumber.failure"/>');
};


</script>
 <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
<div class="errorstyle" id="lblError" style="display: none;"></div>
<s:form name="measurementBookReportForm" action="measurementBookReport" onSubmit="return validateInput();showWaiting();" theme="simple">
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
	<table cellspacing="0" cellpadding="0" width="100%" border="0" >
		<tr>
        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
            <div class="headplacer"><s:text name="search.criteria" /></div></td>
        </tr>
		<tr>
			<td width="11%" class="whiteboxwk">
				<span class="mandatory">*</span><s:text name="measurementbook.report.status" />:
			</td>
			<td width="21%" class="whitebox2wk">
				<s:select id="mbStatus" name="mbStatus" cssClass="selectwk" list="%{mbStatusList}" value="%{mbStatus}" 
				headerKey="-1" headerValue="%{getText('default.dropdown.select')}" listKey="code" listValue="description"/>
			</td>
			
			<td width="11%" class="whiteboxwk">
				<s:text name="measurementbook.report.mbRefNo" />:
			</td>
			<td width="21%" class="whitebox2wk">
				<s:textfield id="mbRefNo" name="mbRefNo" value="%{mbRefNo}" cssClass="selectboldwk" />
			</td>						
		</tr>
		
		<tr>
			<td width="11%" class="greyboxwk">
				<s:text name="measurementbook.report.woNo" />:
			</td>
			<td width="21%" class="greybox2wk">
				<div class="yui-skin-sam">
	                <div id="workOrderNoSearch_autocomplete">
	                <div><s:textfield id="workOrderNoSearch" type="text" name="workOrderNo" value="%{workOrderNo}" onBlur="clearHiddenWONumber(this)" class="selectwk"/>
	                <s:hidden id="workOrderId" name="workOrderId" value="%{workOrderId}"/></div>
	                <span id="workOrderNoSearchResults"></span> 
	                </div>
	           </div>
       		 <egov:autocomplete name="workOrderNoSearch" width="20" field="workOrderNoSearch" url="ajaxMBReport!searchWONumberAjax.action?"  queryQuestionMark="false" results="workOrderNoSearchResults" handler="workOrderNoSearchSelectionHandler" forceSelectionHandler="workOrderNoSelectionEnforceHandler"/>
			</td>
		
			<td width="11%" class="greyboxwk">
				<s:text name="measurementbook.report.estNo" />:
			</td>
			<td width="21%" class="greybox2wk">
				<div class="yui-skin-sam">
	                <div id="estimateNoSearch_autocomplete">
	                <div><s:textfield id="estimateNoSearch" type="text" name="estimateNo" value="%{estimateNo}" onBlur="clearHiddenEstimateNumber(this)" class="selectwk"/>
	                <s:hidden id="estimateId" name="estimateId" value="%{estimateId}"/></div>
	                <span id="estimateNoSearchResults"></span>
	                </div>
	           </div>
       		 <egov:autocomplete name="estimateNoSearch" width="20" field="estimateNoSearch" url="ajaxMBReport!searchEstimateNumberAjax.action?"  queryQuestionMark="false" results="estimateNoSearchResults" handler="estimateNoSearchSelectionHandler" forceSelectionHandler="estimateNoSelectionEnforceHandler"/>
			</td>				
		</tr>
		
		<tr>
			<td width="11%" class="whiteboxwk">
				<s:text name="label.contractorName" />:
			</td>
			<td width="31%" class="whitebox2wk" colspan="3">
				<s:select id="contractorId" name="contractorId"
										cssClass="selectwk"
										list="%{contractorForApprovedWorkOrder}"
										headerKey="-1"															
										headerValue=" "
										value="%{contractorId}"/>	
			</td>								
		</tr>
		<tr>		
            <td class="greyboxwk"><s:text name="label.jurisdiction"/>:</td>
			<td class="greybox2wk">
				<div class="yui-skin-sam">
	                <div id="jurisdictionSearch_autocomplete">
	                <div><s:textfield id="jurisdictionSearch" type="text" name="jurisdictionName" value="%{jurisdictionName}" onBlur="clearHiddenJurisdiction(this)" class="selectwk"/>
	                <s:hidden id="jurisdictionId" name="jurisdictionId" value="%{jurisdictionId}"/></div>
	                <span id="jurisdictionSearchResults"></span>
	                </div>
                </div>
                <egov:autocomplete name="jurisdictionSearch" width="25" field="jurisdictionSearch" url="${pageContext.request.contextPath}/estimate/wardSearch!searchAjax.action?" queryQuestionMark="false" results="jurisdictionSearchResults" handler="jurisdictionSearchSelectionHandler" forceSelectionHandler="jurisdictionSelectionEnforceHandler"/>
			</td>
			<td class="greyboxwk"><s:text name="label.budgetHead"/>:</td>
			<td class="greybox2wk"><div class="yui-skin-sam">
	                <div id="budgetHeadSearch_autocomplete">
	                <div><s:textfield id="budgetHeadSearch" type="text" name="budgetHeadName" value="%{budgetHeadName}" onBlur="clearHiddenBudgetHeadId(this)" class="selectwk"/>
	                <s:hidden id="budgetHeadId" name="budgetHeadId" value="%{budgetHeadId}"/></div>
	                <span id="budgetHeadSearchResults"></span>
	                </div>
	                </div>
	                <egov:autocomplete name="budgetHeadSearch" width="30" field="budgetHeadSearch" url="ajaxAbstractEstimateReport!searchBudgetHeadAjax.action?"  queryQuestionMark="false" results="budgetHeadSearchResults" handler="budgetHeadSearchSelectionHandler" forceSelectionHandler="budgetHeadSelectionEnforceHandler"/>
	         </td>
		
		</tr>
		 
		<tr>
			<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="report.search.fromdate" />:</td>
			<td class="whitebox2wk"><s:date name="fromDate" var="fromBillDate"	format="dd/MM/yyyy" />
										<s:textfield name="fromDate" id="fromDate"
											cssClass="selectwk" value="%{fromBillDate}"
											onfocus="javascript:vDateType='3';"
											onkeyup="DateFormat(this,this.value,event,false,'3')" />
										<a	href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
											onmouseover="window.status='Date Picker';return true;"
											onmouseout="window.status='';return true;"> <img
												src="${pageContext.request.contextPath}/image/calendar.png"
												alt="Calendar" width="16" height="16" border="0"
												align="absmiddle" />
										</a>
									<span id='errorfromDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
			</td>
			<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="report.search.todate" />:</td>
			<td class="whitebox2wk"><s:date name="toDate" var="toBillDate"	format="dd/MM/yyyy" />
													<s:textfield name="toDate" id="toDate"
														value="%{toBillDate}" cssClass="selectwk"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a	href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
										<span id='errortoDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
			</td>
		</tr>
		<tr>
			<td width="11%" class="greyboxwk">
	   			<s:text name="measurementbook.report.woPreparedBy"/> :
	   		</td>
	        <td width="21%" class="greybox2wk">
		         <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="workOrderPreparedBy" 
		         id="preparedBy" cssClass="selectwk" 
		         list="dropdownData.woPreparedByList" listKey="id" listValue='name' value="%{workOrderPreparedBy}"/>
		    </td>
		    <td class="greyboxwk"></td>
		     <td class="greybox2wk"></td>
	    </tr>
		 <tr>
                <td  colspan="4" class="shadowwk"> </td>               
         </tr>
		<tr>
            <td colspan="4"><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
          </tr>
	</table>
	<div class="buttonholdersearch" align = "center">
      	<s:submit value="Save" cssClass="buttonfinal" value="SEARCH" id="saveButton" onclick="return validateInput();" method="searchList" name="button" />
      	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
    </div>
    <div class="errorstyle" id="error_search" style="display: none;"></div>
	<div id="loading"  style="display:none;color:red;font:bold" align="center">
				<span>Processing, Please wait...</span> 
	</div> <br>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">

		<tr><td><%@ include file='measurementBookReport-searchResults.jsp'%></td></tr>
		
	</table>	
	</div><div class="rbbot2"><div></div></div>
	</div>
	</div>
	</div>
</s:form>
</body>
</html>