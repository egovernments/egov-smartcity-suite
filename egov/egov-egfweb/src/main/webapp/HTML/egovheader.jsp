<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@page import="org.egov.infstr.utils.EgovUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="org.egov.infstr.utils.database.utils.*,java.sql.Connection,java.sql.PreparedStatement,java.sql.ResultSet"%>
   
  





<%


String cityurl=(String)session.getAttribute("cityurl");
String cityname=(String)session.getAttribute("citymunicipalityname");
String citynamelocal=(String)session.getAttribute("citynamelocal");

String cityUrl="Hubli-Dharwad-headerLeftLogo";
if(cityurl!=null)

cityUrl="http://www."+cityurl+".gov.in";
%>

<div align="center">
<table align='center'>
<tr>
<td style="buffer:0pt; padding:0pt; margin:0pt">
<div id="paymain"><div id="paym2"><div id="m3">

	<table>
	<tr height="90" valign="center">
		<td vAlign="center"  align="left"  width="250">
		<a href="<%=cityUrl%>"><img src='<c:url value="../images/${cityurl}.gif" />' align="left" border="0" width="90" height="90"></a>
		</td>

		<td vAlign="center"  align="center"  width="252"><p align="center"></p></td>
		<td  width="100%"  align="center"><font size = "6" id="header_font" >
		 <table border="0" cellSpacing="0" height="56" width="500" style="font-weight: normal; font-size: 10pt; text-decoration: none; font-color: black">
		          <tr>
		            <td align="center">
		              <p class="MsoNormal"><span lang="KN" style="font-family: Tunga">
						<font size="6"></font></span><font size="5">
						<span lang="KN">&nbsp;</span></font><span lang="KN" style="font-family: Tunga"><font size="6"><%=citynamelocal%> </font></span></td>
		          </tr>
		          <tr>
		            <td align="center" height="27">
		              <font size="5"><%=cityname%></font><font style="font-size: 22pt"></fontt><font style="font-size: 10pt"></fontt></font></font>
		            </td>
        </table>
		</font></td>

		<td align="middle"  width="250">
		<a id="orgID" name="orgID" href="http://www.egovernments.org"><img src="<c:url value="/images/egovlogo-pwr-trans.gif"/>" border="0" width="70" height="70" align="right"   id="logoID"> </a></td>
		<td bgColor="#FFFFFF" height="102" width="1"> </td>
	  <tr>
	</table>

</div></div></div>
</td>
</tr>
</table>
</div>
<div id="#m3">
<table align="center" height=20 width="80%" summary="" border=0>
  <tbody>

   <%
   	if (request.getUserPrincipal() != null) //user has logged in
   	{
   	
   %>
   <tr>
     <td width="50%" align="left">
       <p ><b><font id="welcome_font"><span id="headerusername">Welcome &nbsp;
       <%=EgovUtils.getPrincipalName(request.getUserPrincipal().getName()) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <td  width="50%" align="right">
      <p ><b><font id="welcome_font"><span id="headerusername">
       <a id="homehref" name="homehref" href="<c:url value="/BnDSecureHome.jsp"/>" ><bean:message key="home"/></a>
       <a id="logouthref" name="logouthref" href="<c:url value="/logout.jsp"/>"><bean:message key="logout"/></a>
  
      	 </span></font></b></p>  
     </td>
     
     </tr>
  <%
   	}
   	else //user has not logged in
   	{
   	
   %>
   <tr>
     <td  width="50%" align="left">
       <p ><b><font id="welcome_font"><span id="headerusername">Welcome &nbsp;Citizen &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       </span></font></b></p> 
     </td>
     <td width="50%" align="right" >
     <p ><b><font id="welcome_font"><span id="headerusername">
       <a id="loginhref" name="loginhref" href="<c:url value="/index.jsp"/>"><bean:message key="bdhome"/></a>
       </span></font></b></p> 
       </td></tr>
   <%
   	}
   	
  %>
  </tbody>
</table>
</div>
