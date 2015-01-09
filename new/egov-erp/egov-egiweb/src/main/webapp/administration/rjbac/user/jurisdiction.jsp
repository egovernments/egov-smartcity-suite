<%@page contentType="text/html" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/adminBndry" prefix="adminBndry" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Jurisdiction Page</title>
		<meta http-equiv="refresh" content="text/html">
		<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">
	</head>
	<body bgcolor="#FFFFFF" >
		

		<html:form action="SubmitUser" >
			<html:hidden property="bndryType" />
			<html:hidden property="firstName" />
			<html:hidden property="middleName" />
			<html:hidden property="lastName" />
			<html:hidden property="salutation" />
			<html:hidden property="userName" />
			<html:hidden property="pwd" />
			<html:hidden property="pwdReminder" />
			<html:hidden property="topBoundaryID" />
			<html:hidden property="departmentId" />
			<html:hidden property="roleId" />
			<center>
			<table class="tableStyle" width="734" border="1">
			<tr>
				<td class="tableheader" align="middle" width="728" height="6">
				Jurisdiction
				</td>
			</tr>
			<tr>
				<td width="730" height="300">
					<table border="1" width="730">
					<adminBndry:select />
					<tr bgColor="#dddddd">
						<td class="tablesubcaption" vAlign="bottom" align="left" width="100%" colSpan="2" height="23">
						<p align="center">
						Need More Levels of Jurisdiction
						<input type=submit name="submit" value="YES">&nbsp;&nbsp;&nbsp;&nbsp;<input type=submit name="submit" value="NO">
						</td>
					</tr>
					</table>
				</td>
			</tr>
			</table>
			</center>
		</html:form>
		
	</body>
</html>