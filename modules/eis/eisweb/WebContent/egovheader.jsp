<%@ page import= "org.egov.infstr.utils.*,
 		java.util.*"
%>
<%@ include file="/includes/taglibs.jsp" %>

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
          <a  id="imaID" name="imaID" href="">
          <img src="<c:url value="/images/bmplogo-trans.gif" />" width="75" height="80" >
          </a>
      </td>
      <td  width="520" id="cityID" align="center">
        <font size="6" id="cityFontID" >City Administration </font>
      </td>
      <td align="middle"  width="124">
         <a id="orgID" name="orgID" href=""><img src="<c:url value="/images/egov-logo.gif" />"  width="97" height="97"  id="logoID"> </a></td>
<td  height="102" width="1"> </td>
    </tr>

</table>



		</center>
	</div></div></div>
   </tr></td>
   </table>

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
       <a id="logouthref" name="logouthref" href="#" onclick="javascript:top.location='<c:url value="/logout.do"/>'" /> Log out</a>

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
       <p ><b><font id="welcome_font"><span id="headerusername">Welcome &nbsp;Guest &nbsp;
       </span></font></b></p>
     </td>
     <td width="50%" align="right" >
     <p ><b><font id="welcome_font"><span id="headerusername">
       <a id="loginhref" name="loginhref" href="<c:url value="/login/securityLogin.jsp"/>">Login</a>
       </span></font></b></p>
       </td></tr>
   <%
   	}

  %>
  </tbody>
</table>
<Center>