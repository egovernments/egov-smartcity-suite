<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,

org.egov.pims.empLeave.model.*,
 org.egov.pims.utils.*,
 org.egov.commons.CFinancialYear"
%>
<%!
	public Map getFinMap(List list)
	{
		Map finMap = new HashMap();
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			CFinancialYear cFinancialYear = (CFinancialYear)iter.next();
			finMap.put(cFinancialYear.getId(), cFinancialYear.getFinYearRange());
		}
		return finMap;
	}

	public Map getCalendarMap(List list)
	{
		Map finMap = new HashMap();
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			CalendarYear CalYear = (CalendarYear)iter.next();
			finMap.put(CalYear.getId(), CalYear.getCalendarYear());
		}
		return finMap;
	}

%>
<%
Map finMap=null;
if(EisManagersUtill.getEmpLeaveService().isLeaveCalendarBased())
{
	
		List calendarMstrList=EisManagersUtill.getEmpLeaveService().getAllCalendarYearList();
		 finMap = getCalendarMap(calendarMstrList);
}
else
{
 	List fYMasterList=EisManagersUtill.getCommonsService().getAllFinancialYearList();
     finMap = getFinMap(fYMasterList);
}

%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title><bean:message key="ChooseFinancialYr"/></title>



<script>
var mode= "view";
function checkMode()
{
	//alert("Inside checkmode="+mode);
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		alert("<%=request.getAttribute("alertMessage")%>");

	}


}
function checkInput()
{

	if(document.forms[0].financialId.options[document.forms[0].financialId.selectedIndex].value == "0")
	{
		alert('<bean:message key="alertSelFinacialYr"/>');
		document.forms[0].financialId.focus();
		return false;
	}
	mode="<%=(request.getParameter("mode"))%>";
	document.forms[0].action="<%=request.getContextPath()%>/leave/BeforeHolidaysMasterAction.do?submitType="+mode;
//	document.forms[0].submit();
}
function setMode()
{

	if(document.forms[0].financialId.options[document.forms[0].financialId.selectedIndex].value == "0")
	{
		alert('<bean:message key="alertSelFinacialYr"/>');
		document.forms[0].financialId.focus();
		return false;
	}
	mode="<%=(request.getParameter("mode"))%>";
	//alert("mode="+mode);
	document.forms[0].action="<%=request.getContextPath()%>/leave/BeforeHolidaysMasterAction.do?submitType="+mode;
	document.forms[0].submit();
}
</script>
</head>

<body onload="checkMode()">
<center>

<html:form  action="/leave/BeforeHolidaysMasterAction">
 <div class="navibarshadowwk"></div>
<div class="formmainbox"><div class="insidecontent">
<div class="rbroundbox2">
<div class="rbtop2"><div></div></div>
<div class="rbcontent2">

<table width ="95%" cellspacing="0" border="0" cellpadding ="0">
<tr>
<td>
<table width ="95%" cellspacing="0" border="0" cellpadding ="0">

<tr>
<td colspan="7" class="headingwk"><div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
<div class="headplacer"><bean:message key="Calender"/></div></td>
</tr>
<tr>
<td class="whiteboxwk" colspan="4">
<% if(EisManagersUtill.getEmpLeaveService().isLeaveCalendarBased())
		 {%>

		Choose Calendar year<SPAN class="mandatory">*</SPAN>
		 <%}else{%>
  <bean:message key="ChooseFinancialYr"/><SPAN class="mandatory">*</SPAN></td>
<%}%>

  	<td class="whitebox2wk">
	<select  name="financialId"   id="financialId"  class="stylewk">
	 <option value="<%= 0 %>" selected="selected"><bean:message key="Choose"/></option>
		<%
		for (Iterator it = finMap.entrySet().iterator(); it.hasNext(); )
		{
				Map.Entry entry = (Map.Entry) it.next();
		%>
		<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
		<%
		}
		%>
	</select>
	</td>
</tr>
<tr><td class="labelcell5" colspan=2>&nbsp;</td></tr>
<tr ><td  align="right">
</td>
<td  align="center">&nbsp;</td>
</tr>

</table>
</td>
		</tr>
		
		<tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
		</table>
		</div>
		<div class="rbbot2"><div></div></div>
		</div>
		</div>
		</div>
		<td colspan="4" >
		<div class="buttonholderwk">
		<html:submit value="Submit" onclick="return checkInput();" styleClass="buttonfinal"/>
		<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()"/>
		</div>
		</td>
		
</html:form>

</center>
</body>
</html>