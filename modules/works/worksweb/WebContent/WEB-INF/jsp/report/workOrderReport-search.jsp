<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="workorder.report.title" /></title>
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
function clearHiddenWOId(obj)
{
	if(obj.value=="")
	{
		document.getElementById("workOrderNumber").value="";
	}	
}

function clearHiddenTenderFileId(obj)
{
	if(obj.value=="")
	{
		document.getElementById("tenderFileRefNumber").value=""; 
	}	
}

function clearHiddenTenderNoticeId(obj)
{
	if(obj.value=="")
	{
		document.getElementById("tenderNumber").value="";
	}	
}
var woSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("workorderSearch").value=oData[0];
    dom.get("workOrderNumber").value = oData[0];
};

var tenderfileSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("tenderfileSearch").value=oData[0];
    dom.get("tenderFileRefNumber").value = oData[0];
};

var tendernoticeSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("tendernoticeSearch").value=oData[0];
    dom.get("tenderNumber").value = oData[0];
};

var workorderSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="workorder.report.wo.ajax.error"/>');
};

var tenderfileSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="workorder.report.tenderfile.ajax.error"/>');
};

var tendernoticeSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="workorder.report.tendernotice.ajax.error"/>');
};

function validateInput()
{
	if(document.getElementById('workOrderNumber').value=="" && document.getElementById('tenderNumber').value=="" && document.getElementById('tenderFileRefNumber').value=="" && 
		document.getElementById('contractorId').value==-1 && document.getElementById('fromDate').value=="" && document.getElementById('toDate').value=="")
	{
		document.getElementById("WOReport_error").innerHTML='<s:text name="workorder.report.validation.error"/>'; 
        document.getElementById("WOReport_error").style.display='';
		return false;
	}
	else
	{
		document.getElementById("WOReport_error").innerHTML='';
		document.getElementById("WOReport_error").style.display="none";
		return true;
	}
}
</script>
<body onload="init()">
<s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
</s:if>
<div class="errorstyle" id="WOReport_error" style="display: none;"></div>
<s:form name="workOrderReportForm" action="workOrderReport" onsubmit="return validateInput();" theme="simple">
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
			<td class="greyboxwk"><s:text name="workorder.report.workorder.number" />:</td>
			<td class="greybox2wk">
				<div class="yui-skin-sam">
					<div id="workorderSearch_autocomplete">
	                	<div><s:textfield id="workorderSearch" type="text" name="workorderNumber" value="%{workorderNumber}" onBlur="clearHiddenWOId(this)" class="selectwk" style="width:150px"/><s:hidden id="workOrderNumber" name="workOrderNumber" value="%{workOrderNumber}" /></div>
	                	<span id="workorderSearchResults"></span>
	                </div>
	             </div>
                 <egov:autocomplete name="workorderSearch" field="workorderSearch" url="ajaxWorkOrderReport!searchWOAjax.action?"  queryQuestionMark="false" results="workorderSearchResults" handler="woSearchSelectionHandler" forceSelectionHandler="workorderSelectionEnforceHandler"/>
			</td>
			
			
			<td class="greyboxwk"><s:text name="workorder.report.tendernotice.number" />:</td>
			<td class="greybox2wk">
					<div class="yui-skin-sam">
					<div id="tendernoticeSearch_autocomplete">
	                	<div><s:textfield id="tendernoticeSearch" type="text" name="tenderNoticeNumber" value="%{tenderNoticeNumber}" onBlur="clearHiddenTenderNoticeId(this)" class="selectwk" style="width:150px"/><s:hidden id="tenderNumber" name="tenderNumber" value="%{tenderNumber}" /></div>
	                 	<span id="tenderNoticeSearchResults"></span>
	                </div>
                </div>
                <egov:autocomplete name="tendernoticeSearch" field="tendernoticeSearch" url="ajaxWorkOrderReport!searchTenderNoticeAjax.action?"  queryQuestionMark="false" results="tenderNoticeSearchResults" handler="tendernoticeSearchSelectionHandler" forceSelectionHandler="tendernoticeSelectionEnforceHandler"/>
								
			</td>
			
	     </tr>
	     <tr>
	     	<td class="whiteboxwk"><s:text name="workorder.report.tenderfile.number"/>:</td>
			<td class="whitebox2wk">
					<div class="yui-skin-sam">
					<div id="tenderfileSearch_autocomplete">
	                	<div><s:textfield id="tenderfileSearch" type="text" name="tenderfileNumber" value="%{tenderfileNumber}" onBlur="clearHiddenTenderFileId(this)" class="selectwk" style="width:150px"/><s:hidden id="tenderFileRefNumber" name="tenderFileRefNumber" value="%{tenderFileRefNumber}" /></div>
	                 	<span id="tenderfileSearchResults"></span>
	                </div>
                </div>
                <egov:autocomplete name="tenderfileSearch" field="tenderfileSearch" url="ajaxWorkOrderReport!searchTenderFileAjax.action?"  queryQuestionMark="false" results="tenderfileSearchResults" handler="tenderfileSearchSelectionHandler" forceSelectionHandler="tenderfileSelectionEnforceHandler"/>
				
			</td>
		</tr>
		<tr>
			<td class="greyboxwk"><s:text name="workorder.report.contractor" />:</td>
			 <td class="greybox2wk" colspan="3" >
					<div class="yui-skin-sam">
					<s:select id="contractorId" name="contractorId"		cssClass="selectwk"
							list="%{contractorForApprovedWorkOrder}"  headerKey="-1"
							headerValue="" value="%{contractorId}" />
													
					</div>
			</td>  
		</tr>	
		<tr>
			<td width="15%" class="whiteboxwk"><s:text name="workorder.report.fromDate" />:</td>
			<td class="whitebox2wk">
				<div class="yui-skin-sam">
						
				<s:date name="fromDate" var="fromWODateFormat"	format="dd/MM/yyyy" />
									<s:textfield name="fromDate" id="fromDate"	cssClass="selectwk" value="%{fromWODateFormat}" 
										onfocus="javascript:vDateType='3';"
										onkeyup="DateFormat(this,this.value,event,false,'3')" />
									<a href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
										onmouseover="window.status='Date Picker';return true;"
										onmouseout="window.status='';return true;"> <img
											src="${pageContext.request.contextPath}/image/calendar.png"
											alt="Calendar" width="16" height="16" border="0"
											align="absmiddle" />
									</a>
									<span id='errorfromDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>	
				</div>
			</td>
			<td width="15%" class="whiteboxwk"><s:text name="workorder.report.toDate" />:</td>
			<td class="whitebox2wk">
				<div class="yui-skin-sam">
				<s:date name="toDate" var="toWODateFormat"	format="dd/MM/yyyy" />
									<s:textfield name="toDate" id="toDate"	cssClass="selectwk" value="%{toWODateFormat}" 
										onfocus="javascript:vDateType='3';"
										onkeyup="DateFormat(this,this.value,event,false,'3')" />
									<a href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
										onmouseover="window.status='Date Picker';return true;"
										onmouseout="window.status='';return true;"> <img
											src="${pageContext.request.contextPath}/image/calendar.png"
											alt="Calendar" width="16" height="16" border="0"
											align="absmiddle" />
									</a>
									<span id='errortoDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>	
				</div>
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

		<tr><td><%@ include file='workOrderReport-searchResults.jsp'%></td></tr>
		
	</table>
	  </div>
	  </div>
	  </div>
	  </div>
</s:form>
</body>
</html>