
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,org.egov.pims.*,org.egov.exceptions.EGOVRuntimeException"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Search Grade Master</title>

	
<script>

function checkInput()
{
	if(document.forms[0].Id.options[document.forms[0].Id.selectedIndex].value == 0)
	{

		alert('<bean:message key="alertSelValue"/>');
		document.forms[0].Id.focus();
		return false;
	}
}

</script>
</head>
<body>
<div class="navibarshadowwk">
		</div>
		<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
			


<!-- Header Section Begins -->

<!-- Header Section Ends -->


<table align="center">
<table align='center' id="table2">
<tr>
<td>


<html:form  action="/pims/AfterGradeMasterAction?submitType=viewBasedOnGradeId" onsubmit="return checkInput()">
<table align='center' class="tableStyle" id="table3">
 <tr>
 <td class="labelcell" colspan="2">&nbsp;</td>
</tr>
<tr>
<td class="greyboxwk" ><span class="mandatory">*</span>Grade Master</td>
<td class="greybox2wk" >
	<select name="grades" id="gradesId" >
		<option value='0'><bean:message key="Choose"/></option>
			<c:forEach var="grade" items="${sessionScope.GradeMasters}">
				<option value="${grade.id}">${grade.name} ~  [${grade.orderNo}]</option>
			</c:forEach>
	</select>
	
<input type="hidden" name="viewMode" id="viewMode" value="<%= request.getAttribute("mode") %> " />
</td>
</tr>
<tr><td class="labelcell5" colspan="2">&nbsp;</td></tr>
<tr ><td  align="right"><html:submit value="Submit" styleClass="buttonfinal"/></td>

<td  align="center">&nbsp;</td>
</tr>
</table>
</html:form>
</div>
</td></tr>
</table>
</center>
</body>
</html>