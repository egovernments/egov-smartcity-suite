<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="payment.report.title" /></title>
</head>
<script src="<egov:url path='js/works.js'/>"></script>	
<script src="<egov:url path='js/helper.js'/>"></script>
<style>
.ui-button
{
	position: absolute;
	height: 2.0em;
}
</style>
<script type="text/javascript">
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

function validateInput() {
	

	if(document.getElementById('jurisdictionId').value=="" && document.getElementById('budgetHeadId').value=="" && document.getElementById('billType').value==-1 && 
			document.getElementById('contractorId').value==-1 && document.getElementById('fromDate').value=="" && document.getElementById('toDate').value=="")
		{
			document.getElementById("PaymentReport_error").innerHTML='<s:text name="workorder.report.validation.error"/>'; 
	        document.getElementById("PaymentReport_error").style.display='';
			return false;
		}
		else
		{
			document.getElementById("PaymentReport_error").innerHTML='';
			document.getElementById("PaymentReport_error").style.display="none";
			return true;
		}
		dom.get("PaymentReport_error").style.display="none";
		return true;
}

</script>

<body onload="init()">
<s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
</s:if>
<div class="errorstyle" id="PaymentReport_error" style="display: none;"></div>
<s:form name="paymentReportForm" action="paymentReport" onsubmit="return validateInput();" theme="simple">
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
	<table cellspacing="0" cellpadding="0" width="100%" border="0" >
		<tr>
        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
            <div class="headplacer"><s:text name="workorder.report.search" /></div></td>
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
			<td class="whiteboxwk"><s:text name="payment.report.fromdate" />:</td>
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
			<td class="whiteboxwk"><s:text name="payment.report.todate" />:</td>
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
			<td class="greyboxwk"><s:text name="payment.report.contractor" />:</td>
			 <td class="greybox2wk" colspan="3" >
					<div class="yui-skin-sam">
					<s:select id="contractorId" name="contractorId"		cssClass="selectwk"
							list="%{contractorForApprovedWorkOrder}"  headerKey="-1"
							headerValue="" value="%{contractorId}" />
													
					</div>
			</td>  
		</tr>
		<tr>
			<td class="whiteboxwk"><s:text name="payment.report.billtype" />:</td>
			<td class="whitebox2wk">
				<s:select id="billType" name="billType" cssClass="selectwk" headerKey="-1" headerValue="%{getText('estimate.default.select')}" list="#{'Running Bill':'Running Bill','Final Bill':'Final Bill'}" />
			</td>
		</tr>
		<tr>
			<td class="greyboxwk" colspan="4">&nbsp;</td>
		</tr>
		</table>
		<div class="buttonholdersearch" align = "center">
      		<s:submit value="Save" cssClass="buttonfinal" value="SEARCH" id="saveButton" name="button" onClick="return validateInput();" method="searchList" />
      		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
    	</div>
    	<div class="errorstyle" id="error_search" style="display: none;"></div>
    	    
    		<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr><td><%@ include file='paymentReport-searchResults.jsp'%></td></tr>
		
			</table>
		</div>
        </div>
        </div>
        </div>
        </s:form>
</body>
</html>
        