<%@page import="org.egov.infstr.utils.EgovUtils" %>


<%@ page import="java.sql.Connection,java.sql.PreparedStatement,java.sql.ResultSet,
org.egov.infstr.client.administration.rjbac.user.UserForm,
org.egov.lib.rjbac.user.ejb.server.UserServiceImpl,
org.egov.lib.rjbac.user.User"%>


<%
Integer userID= (Integer)request.getSession().getAttribute("com.egov.user.LoginUserId");
 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Egov Site mesh Header userID "+userID);
 String userName="";
 UserServiceImpl userServiceImpl = new UserServiceImpl();
if(userID !=null)
{
User user = userServiceImpl.getUserByID(userID);
 userName=user.getUserName();
System.out.println("userName"+userName);
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