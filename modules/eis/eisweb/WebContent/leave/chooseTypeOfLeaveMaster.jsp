
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,
org.egov.infstr.commons.dao.*,
		 org.egov.infstr.commons.client.*"



%>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title><bean:message key="LeaveTypeCategory"/></title>



<script>
function checkMode()
{
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		alert("<%=request.getAttribute("alertMessage")%>");

	}
}
function checkInput()
{

	if(document.forms[0].Id.options[document.forms[0].Id.selectedIndex].value == 0)
	{
		alert('<bean:message key="alertSelLeaveType"/>');
		return false;
}
}
</script>
</head>
<body onload="checkMode()" >

		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
<!-- Header Section Begins -->

<!-- Header Section Ends -->


<table width="100%" cellpadding ="0" cellspacing ="0" border = "0">
<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="table2">
<tr>
<td>

<table  width="100%" cellpadding ="0" cellspacing ="0" border = "0">
<tr><td>

<html:form  action="/leave/BeforeLeaveTypeAction?submitType=setIdForDetails" onsubmit="return checkInput()">
<table width="100%" cellpadding ="0" cellspacing ="0" border = "0"  id="table3">
 <tr>
 <td class="labelcell" colspan="2">&nbsp;</td>
</tr>

<tr>
  <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
 
<p><div class="headplacer">Leave Type Category</div></p></td>
  </tr>


<tr>
<td class="greyboxwk" align="right"><span class="mandatory">*</span><bean:message key="ChooseLeaveTypeNme"/></td>
<td class="greybox2wk"  align="center">
	<select  name="Id" id="Id" class="selectwk">
	<option value="0" selected="selected"><bean:message key="Choose"/></option>

		<%

			Map mapOfLeaveMaster =(Map) session.getAttribute("mapOfLeaveMaster");
		for (Iterator it = mapOfLeaveMaster.entrySet().iterator(); it.hasNext(); )
		{
			Map.Entry entry = (Map.Entry) it.next();
		%>
		<option value='<%= entry.getKey().toString() %>'><%=entry.getValue()%></option>

		<%
		}
		%>



	</select>
	<input type=hidden name="viewMode" id="viewMode" value="<%= session.getAttribute("mode") %> " />
</td>
</tr>
<tr><td class="labelcell5" colspan=2>&nbsp;</td></tr>
<tr ><td  align="right"></td>
<td  align="center">&nbsp;</td>
</tr>

</table>

</div>
</td></tr>

<tr>
            <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
          </tr>
</table>

</td>
</tr>
</table>
</div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
  </table>
  </div>
<div class="buttonholderwk">&nbsp;
</center>

<table align="center">
<tr>
<td>
<html:submit value="Submit" styleClass="buttonfinal"/>
<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/>
</td>
</tr>
</table>
</html:form>
</body>
</html>