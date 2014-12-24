<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="lwc.report.title" /></title>
</head>
<body>
<script type="text/javascript">

function validateInput() {
	
	var fromDate = document.getElementById("fromDate").value;
	var toDate = document.getElementById("toDate").value;
	dom.get("lblError").style.display="";
	document.getElementById('lblError').innerHTML = "";
	if(fromDate == ""){
		document.getElementById('lblError').innerHTML = '<s:text name="report.search.fromdate.required" />';
		return false;
	}
	else if(toDate == ""){
		document.getElementById('lblError').innerHTML = '<s:text name="report.search.todate.required" />';
		return false;
	}
	
	dom.get("lblError").style.display="none";
	return true;
}


</script>
 <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
<div class="errorstyle" id="lblError" style="display: none;"></div>
<s:form name="labourWelfareCessReportForm" action="labourWelfareCessReport" onSubmit="return validateInput();showWaiting();" theme="simple">
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
	<div id="loading"  style="display:none;color:red;font:bold" align="center">
				<span>Processing, Please wait...</span> 
	</div> <br>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">

		<tr><td><%@ include file='labourWelfareCessReport-searchResults.jsp'%></td></tr>
		
	</table>
	
	</div><div class="rbbot2"><div></div></div>
	</div>
	</div>
	</div>
</s:form>
</body>
</html>