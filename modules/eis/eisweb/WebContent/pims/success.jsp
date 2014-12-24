<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ page import="org.egov.pims.model.PersonalInformation"%>

<html>
	<head>
		<title>Employee Master</title>

		<LINK rel="stylesheet" type="text/css" HREF="<c:url value='<%=request.getContextPath()%>/css/egov.css' />" />
		<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ccMenu.css"/>
	</head>

	<center>
		 <html:form>
		  <table border="1" class="eGovTblMain" width="754">
		    <tbody>
		      <tr>
		
		        <td colspan="6" height="30"  bgcolor="#bbbbbb" align="middle" class="tablesubcaption">
		        <%
					PersonalInformation pers =(PersonalInformation)request.getAttribute("employeeob");
			
					String code = pers.getEmployeeCode();
					String name =  pers.getEmployeeFirstName();
				%>
		          <p align="center"><bean:message key="Employee"/>  <%=name%>  <bean:message key="createdsuccessfully"/></p>
		          <p align ="center"><bean:message key="AssignedEmpCode"/><%=code%></p>
		        </td>
		        	<td></td>
		      </tr>
			</tbody>
		  </table>
		
		</html:form>
	</center>
</body>

</html>