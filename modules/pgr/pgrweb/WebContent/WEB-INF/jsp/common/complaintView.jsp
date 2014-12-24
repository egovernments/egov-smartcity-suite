<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import=" org.egov.infstr.utils.AppConfigTagUtil" %>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

<head>
	<title><s:text name="header.view.complaint" /></title>
</style>

</head>

<body class="yui-skin-sam">
	<s:form theme="simple">
	
			<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="subheader.view.complaint.Person.details"></s:text>
										
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr><tr>
				<table width="100%">
							
					<tr>
						<td class="whiteboxwk"> <s:text name="view.complaint.number"></s:text> </td>
						<td class="whitebox2wk"> </td>
						<td class="whiteboxwk"><s:text name="view.complaint.date"></s:text> </td>
						<td class="whitebox2wk"> </td>
					</tr>
					<tr>
						<td class="greyboxwk"> <s:text name="view.complaint.name"></s:text> </td>
						<td class="greybox2wk"> </td>
						<td class="greyboxwk"> <s:text name="view.complaint.email"></s:text> </td>
						<td class="greybox2wk"> </td>
					</tr>
					
					<tr>
						<td class="whiteboxwk"> <s:text name="view.complaint.address"></s:text> </td>
						<td class="whitebox2wk"> </td>
						<td class="whiteboxwk"> <s:text name="view.complaint.pinCode"></s:text> </td>
						<td class="whitebox2wk"> </td>
					</tr>
					<tr>
						<td class="greyboxwk"> <s:text name="view.complaint.phoneNumber"></s:text> </td>
						<td class="greybox2wk"> </td>
						<td class="greyboxwk"> <s:text name="view.complaint.mobileNumber"></s:text> </td>
						<td class="greybox2wk"> </td>
					</tr>
				</table></tr>
				</table>
	</s:form>
	


</body>
</html>