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


<%@ page import="java.sql.Connection,java.sql.PreparedStatement,java.sql.ResultSet,
org.egov.infstr.client.administration.rjbac.user.UserForm,
org.egov.infstr.utils.ServiceLocator,
org.egov.infra.admin.master.entity.User,
org.egov.lib.rjbac.user.ejb.api.UserManager,
org.egov.lib.rjbac.user.ejb.api.UserManagerHome;"%>
<%@ page import="org.apache.log4j.Logger"%>

<%
Logger LOGGER = Logger.getLogger("egovSitemeshHeader.jsp");
Integer userID= (Integer)request.getSession().getAttribute("com.egov.user.LoginUserId");
LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Egov Site mesh Header userID "+userID);
 String userName="";
 ServiceLocator serviceloc = ServiceLocator.getInstance();
 UserManagerHome uhome = (UserManagerHome)serviceloc.getLocalHome("UserManagerHome");
UserManager userManager = uhome.create();
if(userID !=null)
{
User user = userManager.getUserByID(userID);
 userName=user.getUserName();
LOGGER.info("userName"+userName);
}


%>
   
  

<table align="center" id="getAllDetails" name="getAllDetails" >
	    	<tr><td>
		<div id="main"><div id="m2"><div id="m3"> 
		<center>
		  
		<table align="center"  class="tableStyle" 	><!--class="tableStyle"-->
		          
		<tr><td colspan=4>&nbsp;</td></tr>
	
  
	<tr>
      <td  width="2"> </td>
      <td vAlign="center"  align="left" width="107">
          <p align="center">
          <a  id="imaID" name="imaID" href="//www.egovernments.org">
          <img src="<c:url value='/images/egov-logo.gif'/>" width="97" height="97" >
          </a>
      </td>
      <td  width="520" id="cityID" align="center">
        <font size="6" id="cityFontID" >City Administration </font>
      </td>
      <td align="middle"  width="124">
         <a id="orgID" name="orgID"href="//www.egovernments.org"><img src="<c:url value='/images/egov-logo.gif'/>"  width="97" height="97"  id="logoID"> </a></td>
<td  height="102" width="1"> </td>
    </tr>
	
</table>



		</center>
	</div></div></div>
   </tr></td>
   </table>
  
   	<br><br>
<Center>
