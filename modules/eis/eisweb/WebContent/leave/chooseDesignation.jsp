
<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.util.*" 
%>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Designation Leave Mapping</title>

	

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
	if(document.genericBeforeForm.Id.options[document.genericBeforeForm.Id.selectedIndex].value == 0)
	{
		alert('<bean:message key="alertSelDesg"/>');
		return false;
	}
}
</script>
</head>
<div align="center">
</div>
<Center>
<!-- Tab Navigation Begins -->
<table align='center'>
<tr>
<td align="center">
<!-- Tab Navigation Begins -->
<center>
</center>
<!-- Tab Navigation Ends -->
				</td>
				</tr>
				</table>
		<!-- Tab Navigation Ends -->




<!-- Header Section Begins -->

<!-- Header Section Ends -->

<table width="100%" cellpadding ="0" cellspacing ="0" border = "0"  id="table2" >
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->



<!-- Body Begins -->

<div align="center">
<center>
<body onload="checkMode()">

		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
<center>

<table width="90%" cellpadding ="0" cellspacing ="0" border = "0" id="table2">
<tr><td>

<html:form  action="/leave/BeforeLeaveMasterAction?submitType=forward" onsubmit="return checkInput()">
<table width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="table3">
 <tr>
 <td class="labelcell" colspan="2">&nbsp;</td>
</tr>
<tr>
  <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
 
<p><div class="headplacer">Designation Leave Mapping</div></p></td>
  </tr>
<tr>
<td class="greyboxwk" align="right"><span class="mandatory">*</span><bean:message key="ChooseDesignation"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="greybox2wk"  align="center">
	<select  name="Id" id="Id" class="selectwk">
	<option value='0'><bean:message key="Choose"/></option>
		<%

		Map mapOfDesignation = (Map)session.getAttribute("mapOfDesignation");
		for (Iterator it = mapOfDesignation.entrySet().iterator(); it.hasNext(); )
		{
			Map.Entry entry = (Map.Entry) it.next();
		%>
		<option value='<%= entry.getKey().toString() %>'><%=entry.getValue()%></option>

		<%
		}
		%>



	</select>

</td>
</tr>
<tr><td >&nbsp;</td></tr>
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
 </div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
</div>
</td>
</tr>
</table>

  
<div class="buttonholderwk">&nbsp;

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
</div>

</html>