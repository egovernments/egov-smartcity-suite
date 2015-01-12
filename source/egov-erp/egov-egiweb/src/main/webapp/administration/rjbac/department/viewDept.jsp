<%@page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%
Integer bl = null;
if(request.getSession().getAttribute("billinglocation")!="")
{
bl = (Integer)request.getSession().getAttribute("billinglocation");
}
%>
<html>
	<head>
		<title>View Department</title>
		<script>
			function goBack() {
				document.forms[0].action = "/egi/SetupDepartment.do?bool=VIEW";
				document.forms[0].submit();
			}

			function bodyonload() {
				var billinglocation ="<%= bl%>";
				if(billinglocation==1) {
					document.getElementById("billingLocation").checked=true;
				}
			}
		</script>
	</head>
	<body onload="bodyonload()" >
		<html:form action="Department?bool=CREATE">
			<table align="center" class="tableStyle">
				<tr>
					<td class="tableheader" align="middle" width="728" height="6">View Department</td>
				</tr>
			</table>
			<br/><br/>			
			<table align="center"  class="tableStyle">
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Name<font class="ErrorText">*</font></td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="deptName" readonly="true" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Code</td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="deptCode" readonly="true" styleClass="ControlText"  />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Details</td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="deptDetails" readonly="true" styleClass="ControlText"  />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Address</td>
					<td class="labelcell"  align="left" width="40%" height="23" >
						<html:textarea rows="5" cols="20" readonly="true" styleClass="ControlText"  property="deptAddress" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >IsBillingLocation</td>
					<td class="labelcell"  align="left" width="40%" height="23" >
						<input type="checkbox" name="billingLocation" id="billingLocation" disabled="true" value="on">
					</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center" class="tableStyle">
				<tr>
					<td align="middle" width="728"><input type="button" value="  Back  " onclick="goBack()"></td>
				</tr>
			</table>
		</html:form>
	</body>
</html>