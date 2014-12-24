<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="abstract.estimate.report.title" /></title>
</head>
<body onload="formatNumbers();changeDateLabels();">
<script type="text/javascript">
function formatNumbers()
{
	var estLAmt = document.getElementById("estLowerAmt");
	var estUAmt = document.getElementById("estUpperAmt");
	if(estLAmt.value!='')
	{
		estLAmt.value = Number(estLAmt.value); 
	}	
	if(estUAmt.value!='')
	{
		estUAmt.value = Number(estUAmt.value); 
	}
}
function generateParameter()
{
	var status = "";
	var search=document.getElementById("zoneId").value;
	status ="zoneId="+search;
	return status;
}
function clearHiddenWard(obj)
{
	if(obj.value=="")
	{
		document.getElementById("wardId").value="";
	}	
}
function clearHiddenBudgetHeadId(obj)
{
	if(obj.value=="")
	{
		document.getElementById("budgetHeadId").value="";
	}	
}

var budgetHeadSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("budgetHeadSearch").value=oData[0];
    dom.get("budgetHeadId").value = oData[1];
};

var budgetHeadSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="abstract.estimate.report.budget.ajax.error"/>');
};
var wardSearchSelectionHandler = function(sType, arguments) { 
    var oData = arguments[2];
    dom.get("wardSearch").value=oData[0];
    dom.get("wardId").value = oData[1];
};

var wardSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="abstract.estimate.report.ward.ajax.error"/>');
};
function validateInput()
{
	if(document.getElementById("estimateStatus").value=='-1')
	{
		dom.get("AEReport_error").innerHTML='<s:text name="ae.report.select.estimate.status"/>'; 
        dom.get("AEReport_error").style.display='';
		return false;
	}
	var upperAmt, lowerAmt,fromDate,toDate;
	lowerAmt = document.getElementById("estLowerAmt").value;
	upperAmt = document.getElementById("estUpperAmt").value;
	fromDate = document.getElementById("fromDate").value;
	toDate = document.getElementById("toDate").value;
	if((lowerAmt!='' && upperAmt=='') || (lowerAmt=='' && upperAmt!=''))
	{
		dom.get("AEReport_error").innerHTML='<s:text name="ae.report.enter.estimate.amt"/>'; 
        dom.get("AEReport_error").style.display='';
		return false;
	}
	if((lowerAmt!='' && upperAmt!='') && (parseFloat(lowerAmt)>parseFloat(upperAmt)))
	{
		dom.get("AEReport_error").innerHTML='<s:text name="ae.report.loweramt.greater.upperamt"/>'; 
        dom.get("AEReport_error").style.display='';
		return false;
	}
	if(fromDate=='' && toDate=='')
	{
		dom.get("AEReport_error").innerHTML='<s:text name="ae.report.enter.dates"/>'; 
        dom.get("AEReport_error").style.display='';
		return false;
	}
	if((fromDate!='' && toDate=='') || (fromDate=='' && toDate!=''))
	{
		dom.get("AEReport_error").innerHTML='<s:text name="ae.report.enter.fromDate.toDate"/>'; 
        dom.get("AEReport_error").style.display='';
		return false;
	}	 
	dom.get("AEReport_error").style.display="none";
	return true;
}

function validateAmts(obj)
{
	var text = obj.value;
	if(text=='')
		return;
	var msg = '<s:text name="ae.report.enter.valid.est.amt" />';
	if(isNaN(text))
	{
		alert(msg);
		obj.value="";
		return;
	}
	if(text<=0)
	{
		alert(msg);
		obj.value='';
		return;
	}
}
function changeDateLabels()
{
	var fmsg , tmsg, obj;
	fmsg='From Date';
	tmsg='To Date';
	obj = document.getElementById("estimateStatus");
	if(obj.value=='Created')
	{
		fmsg = '<s:text name="ae.report.estimate.fromDate"/>';
		tmsg = '<s:text name="ae.report.estimate.toDate"/>';
	}
	if(obj.value=='Technical Sanctioned')
	{
		fmsg = '<s:text name="ae.report.techsanc.fromDate"/>';
		tmsg = '<s:text name="ae.report.techsanc.toDate"/>';
	}
	if(obj.value=='Administrative Sanctioned')
	{
		fmsg = '<s:text name="ae.report.adminsanc.fromDate"/>';
		tmsg = '<s:text name="ae.report.adminsanc.toDate"/>';
	}
	dom.get("fDateSpan").innerHTML=fmsg;
	dom.get("tDateSpan").innerHTML=tmsg;
}

</script>
 <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
<div class="errorstyle" id="AEReport_error" style="display: none;"></div>
<s:form name="abstractEstimateReportForm" action="abstractEstimateReport" onSubmit="return validateInput();" theme="simple">
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
	<table cellspacing="0" cellpadding="0" width="100%" border="0" >
		<tr>
        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
            <div class="headplacer"><s:text name="abstract.estimate.report.search" /></div></td>
        </tr>
		<tr>
			<td class="greyboxwk"><span class="mandatory">*</span><s:text name="abstract.estimate.report.estimate.status" />:</td>
			<td class="greybox2wk">
				<s:select id="estimateStatus" name="estimateStatus" onchange="changeDateLabels()" cssClass="selectwk" disabled="%{fDisabled}"
						list="dropdownData.estimateStatusList" headerKey="-1" headerValue="%{getText('list.default.select')}"
						value="%{estimateStatus}"  />
			</td>
			<td class="greyboxwk"><s:text name="abstract.estimate.report.budget.head"/>:</td>
			<td class="greybox2wk"><div class="yui-skin-sam">
	                <div id="budgetHeadSearch_autocomplete">
	                <div><s:textfield id="budgetHeadSearch" type="text" name="budgetHeadName" value="%{budgetHeadName}" onBlur="clearHiddenBudgetHeadId(this)" class="selectwk"/><s:hidden id="budgetHeadId" name="budgetHeadId" value="%{budgetHeadId}"/></div>
	                <span id="budgetHeadSearchResults"></span>
	                </div>
	                </div>
	                <egov:autocomplete name="budgetHeadSearch" width="20" field="budgetHeadSearch" url="ajaxAbstractEstimateReport!searchBudgetHeadAjax.action?"  queryQuestionMark="false" results="budgetHeadSearchResults" handler="budgetHeadSearchSelectionHandler" forceSelectionHandler="budgetHeadSelectionEnforceHandler"/>
	         </td>
		
		</tr>
		<tr>
			<td class="whiteboxwk"><s:text name="abstract.estimate.report.zone"/>:</td>
			<td class="whitebox2wk">
				<s:select id="zoneId" name="zoneId" cssClass="selectwk" disabled="%{fDisabled}"
						list="dropdownData.zoneList" listKey="id" listValue="name" 
						headerKey="-1" headerValue="%{getText('list.default.select')}"
						value="%{zoneId}"  />
			</td>
			<td class="whiteboxwk"><s:text name="abstract.estimate.report.ward"/>:</td>
			<td class="whitebox2wk">
				<div class="yui-skin-sam">
	                <div id="wardSearch_autocomplete">
	                <div><s:textfield id="wardSearch" type="text" name="wardName" value="%{wardName}" onBlur="clearHiddenWard(this)" class="selectwk"/><s:hidden id="wardId" name="wardId" value="%{wardId}"/></div>
	                <span id="wardSearchResults"></span>
	                </div>
                </div>
                <egov:autocomplete name="wardSearch" width="20" field="wardSearch" url="ajaxAbstractEstimateReport!searchWardAjax.action?" paramsFunction="generateParameter" queryQuestionMark="false" results="wardSearchResults" handler="wardSearchSelectionHandler" forceSelectionHandler="wardSelectionEnforceHandler"/>
			</td>
		</tr>
		<tr>
			<td class="greyboxwk"><s:text name="abstract.estimate.report.include.hq.estimates" />:</td>
			<td class="greybox2wk"><s:checkbox name="includeCityEstimates" /></td>	
			<td class="greyboxwk"><s:text name="abstract.estimate.report.estimate.amount.between" />:</td>
			<td class="greybox2wk"><s:textfield name="estimateLowerAmt" id="estLowerAmt" onblur="validateAmts(this)"  style="width:65px;text-align:right"/>&nbsp;--&nbsp;<s:textfield name="estimateUpperAmt" id="estUpperAmt" onblur="validateAmts(this)" style="width:65px;text-align:right" /></td>
		</tr>
		<tr>
			<td class="whiteboxwk"><span class="mandatory">*</span><span id="fDateSpan"><s:text name="abstract.estimate.report.from.date" /></span>:</td>
			<td class="whitebox2wk"><s:date name="fromDate" var="fromEstimateDate"	format="dd/MM/yyyy" />
										<s:textfield name="fromDate" id="fromDate"
											cssClass="selectwk" value="%{fromEstimateDate}"
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
			<td class="whiteboxwk"><span class="mandatory">*</span><span id="tDateSpan"><s:text name="abstract.estimate.report.to.date" /></span>:</td>
			<td class="whitebox2wk"><s:date name="toDate" var="toEstimateDate"	format="dd/MM/yyyy" />
													<s:textfield name="toDate" id="toDate"
														value="%{toEstimateDate}" cssClass="selectwk"
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
			<td class="greyboxwk" colspan="4">&nbsp;</td>
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
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">

		<tr><td><%@ include file='abstractEstimateReport-searchResults.jsp'%></td></tr>
		
	</table>
	</div>
	</div>
	</div>
	</div>
</s:form>
</body>
</html>