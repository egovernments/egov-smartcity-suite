<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page contentType="text/html" %>
<%@page import="java.util.*,org.egov.lib.admbndry.*,
				org.egov.infstr.client.delegate.*,
				org.egov.lib.rjbac.user.*,
				org.egov.lib.rjbac.role.*" %>
<%@ include file="/includes/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<title>View User Page</title>
		<LINK rel="stylesheet" type="text/css" href="../../../css/egov.css">
	</head>
	
	<body bgcolor="#FFFFFF">
		
		<html:form action="DeleteUpdateUser"  >
		<center>
			<table class="eGovTblMain" width="734" border="1">
				<tr>
					<td class="eGovTDMain" align="middle" width="728" height="6">View User Details
					</td>
				</tr>
				<tr>
					<td width="730" height="375">
						<table border="1" width="730">
							<%
								//sets HierarchyTypeName in session
								String hrchyTypeName = "ADMINISTRATION" ;
								session.setAttribute("hrchyTypeName",hrchyTypeName);
								//Gets the User object from session
								User user = (User)session.getAttribute("USER");
								String userIdStr=user.getId().toString();
								Integer topBndryId = user.getTopBoundaryID();
								List topBndriesList = new ArrayList();
								ApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
								UserDelegate userDelegate = (UserDelegate)context.getBean("userDelegate");
								topBndriesList = userDelegate.getTopBondaries();
								String topBndryName = "";
								for(Iterator itr =topBndriesList.iterator();itr.hasNext();)
								{
									Boundary topBndry = (Boundary)itr.next();
									if(topBndry.getId().toString().equals(topBndryId.toString()))
									{
										topBndryName = topBndry.getName();
										break;
									}
								}	
							%>
							<tr>
								<td class="eGovTblContentSubHd" vAlign="center" align="middle" width="100%" colSpan="2" height="26">
								<p align="center"><bean:message key="UserDetailLabel"/><bean:message key="UserDetailLabel.ll"/></td>
							</tr>
							<tr>
								<td class="eGovTblContent" width="40%" height="23" ><bean:message key="firstName"/><bean:message key="firstName.ll" /><font class="ErrorText">*</font></td>
								<td align="left" width="40%" height="23"class="eGovTblContent" >&nbsp;&nbsp;<%=user.getFirstName()%>
								</td>
							</tr>
							<tr>
								<td class="eGovTblContent" width="40%" height="23" ><bean:message key="middleName"/><bean:message key="middleName.ll" /></td>
								<td class="eGovTblContent" align="left" width="40%" height="23" >&nbsp;&nbsp;<%=(user.getMiddleName()==null)?"":user.getMiddleName()%>
							</tr>
							<tr>
								<td class="eGovTblContent" width="40%" height="23" ><bean:message key="lastName"/><bean:message key="lastName.ll" /></font></td>
								<td class="eGovTblContent" align="left" width="40%" height="23" >&nbsp;&nbsp;<%=(user.getLastName()==null)?"":user.getLastName()%>
							</tr>
							
							<tr>
								<td class="eGovTblContent" width="40%" height="23" ><bean:message key="salute"/><bean:message key="salute.ll" /></td>
								<td class="eGovTblContent" align="left" width="40%" height="23" >&nbsp;&nbsp;<%=(user.getSalutation()==null)?"":user.getSalutation()%>
								</td>
							</tr>
							<tr>
								<td class="eGovTblContent" width="40%" height="23" >
									<bean:message key="dob"/><bean:message key="dob.ll" />
								</td>
								<td class="eGovTblContent" align="left" width="40%" height="23" >&nbsp;&nbsp;
									&nbsp;(<I><font class="ErrorText">dd-mm-yyyy</font></I>)
								</td>  
							</tr>
							
							<tr>
								<td class="eGovTblContent" width="40%" height="23" ><bean:message key="userName"/><bean:message key="userName.ll" />
								<font class="ErrorText">*</font>
								<td class="eGovTblContent" align="left" width="40%" height="23" >&nbsp;&nbsp;<%=user.getUserName()%>
								</td>
							</tr>
							
							
							<tr>
								<td class="eGovTblContent" width="40%" height="23" ><bean:message key="belongsto"/><bean:message key="belongsto.ll" />
									<font class="ErrorText">*</font>
								</td>
								<td class="eGovTblContent" align="left" width="40%" height="23" >&nbsp;&nbsp;<%=topBndryName%>
								</td>
							</tr>
							<tr>
								<td class="eGovTblContent" width="40%" height="23" >Department
									<font class="ErrorText">*</font>
								</td>
								<td class="eGovTblContent" align="left" width="40%" height="23" >&nbsp;&nbsp;<%=user.getDepartment().getDeptName()%>
								</td>
							</tr>
							<%
								//Gets the set of roles for the User to be updated
								Set userrole = user.getRoles();
								Role role = null;
								for(Iterator itr = userrole.iterator();itr.hasNext();)
								{
									role = (Role)itr.next();
								}
							%>
							<tr>
								<td class="eGovTblContent" width="40%" height="23" >User Role
									<font class="ErrorText">*</font>
								</td>
								<td class="eGovTblContent" align="left" width="40%" height="23" >&nbsp;&nbsp;<%=(role==null)?"":role.getRoleName()%>
								</td>
							</tr>
									
							<tr bgColor="#dddddd">
								<td vAlign="bottom" align="left" width="100%" colSpan="2" height="23">
								<p align="center">User Jurisdictions
								</td>
							</tr>	
							
							<% 
								//Gets the Jurisdiction Map from the session and Displays the existing Jurisdictions for the User
								Map juriscnMap = (Map)session.getAttribute("jursidcnMap");
								
								for(Iterator itr = juriscnMap.keySet().iterator(); itr.hasNext();)
								{
									BoundaryType bType =(BoundaryType)itr.next();
									Set bndrySet =(Set)juriscnMap.get(bType);
							%>		
							<tr>
								<td class="eGovTblContent" width="40%" height="23" ><%=bType.getName()%>
									<font class="ErrorText">*</font>
								</td>
								<td class="eGovTblContent" align="left" width="40%" height="23" >&nbsp;&nbsp;
							<%		
									for(Iterator bndrySetItr = bndrySet.iterator(); bndrySetItr.hasNext();)
									{
										Boundary bndry =(Boundary)bndrySetItr.next();
							%>
									<%=bndry.getName()%>&nbsp;
							<%		
									}
							%>
								</td>
							</tr>	
							<%
								}
							%>
							<tr bgColor="#dddddd">
								<td vAlign="bottom" align="left" width="100%" colSpan="2" height="23">
								<p align="center">
									<input type=button value="EDIT" onclick="window.location = '/DeleteUpdateUser.do?bool=EDIT&userid=<%=userIdStr%>';">&nbsp;&nbsp;&nbsp;&nbsp;
									<input type=button  value="DELETE" onclick="window.location = '/DeleteUpdateUser.do?bool=DELETE&userid=<%=userIdStr%>';">
								</td>
							</tr>
							<tr bgColor="#dddddd">
								<td vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
									<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font>
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