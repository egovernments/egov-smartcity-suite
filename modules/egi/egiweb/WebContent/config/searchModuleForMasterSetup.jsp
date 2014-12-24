<%@ include file="/includes/taglibs.jsp"%>
<%@page import="java.util.*"%>
<%
	String mode = request.getParameter("mode");
%>

<html>
	<head>
		<title>Search App Config Module</title>
		<script language="JavaScript" type="text/JavaScript">
			function checkOnSubmit() {
				if(document.getElementById('moduleName').value==""){
					alert('Select a module');
					document.getElementById('moduleName').focus();
					return false;
				}
				var mode = '<%= mode %>'
				document.forms[0].action ="${pageContext.request.contextPath}/config/MasterSetUpAction.do?submitType=populateExistingDetails&mode="+mode;
				document.forms[0].submit();
			}	  		
 		</script>
	</head>
	<body>
		<html:form action="/config/MasterSetUpAction">
			<table style="width: 700;" class="tableStyle" cellpadding="0" cellspacing="0" border="1" id="employee">
				<tr>
					<%if("view".equals(mode)){ %>
					<td colspan="6" height=30 align="center" class="tableheader">
						<p align="center">
							<b>View &nbsp;&nbsp;&nbsp;</b>
						</p>
					</td>
					<%} %>
					<%if("create".equals(mode)){ %>
					<td colspan="6" height=30 align="center" class="tableheader">
						<p align="center">
							<b>Create &nbsp;&nbsp;&nbsp;</b>
						</p>
					</td>
					<%} %>
				</tr>
				<tr>
					<td height="15" class="tablesubcaption" colspan="6" align="center"></td>
				</tr>

				<tr>
					<td class="labelcell">
						Select Module Name
						<font color="red">*</font>
					</td>
					<td class="labelcell">
						<html:select styleClass="fieldcell" property="moduleName" styleId="moduleName">
							<html:option value="">-----------Select-----------</html:option>
							<c:forEach var="moduleObj" items="${masterDataForm.moduleSet}">
								<html:option value="${moduleObj}">${moduleObj}</html:option>
							</c:forEach>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="labelcell" colspan="2">
						&nbsp;
					</td>
				</tr>
			</table>

			<table align='center' id="table2">
				<tr>
					<td class="labelcell"></td>
					<%if("view".equals(mode)){ %>
					<td>
						<input type="button" name="view" value="   View   " onclick="return checkOnSubmit();" />
					</td>
					<%} %>
					<%if("create".equals(mode)){ %>
					<td>
						<input type="button" name="create" value="  Update  " onclick="return checkOnSubmit();" />
					</td>
					<%} %>
				</tr>
			</table>
		</html:form>
	</body>
</html>
