<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices" %>

<%
Cookie terminate = new Cookie(TokenBasedRememberMeServices.ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY, null);
String contextPath = request.getContextPath();
terminate.setPath(contextPath != null && contextPath.length() > 0 ? contextPath : "/");
terminate.setMaxAge(0);
response.addCookie(terminate);
response.sendRedirect("https://"+request.getServerName()+":8443/cas/logout");
%>


<HTML>
<head>
<title>Logout Page</title>
  <link rel="stylesheet" type="text/css" media="all" href="<c:url value='../../commons/css/egov.css'/>" />
 <LINK REL=stylesheet HREF="ccMenu.css" TYPE="text/css">
<head>

<BODY>


<div align=center>
<table align='middle' id="signup"  >

<tr>
	<td style="buffer:0pt; padding:0pt; margin:0pt">

<div id="main"><div id="m2"><div id="m3">
<div align="center">

<table border="1" align="center" cellspacing="0" width="393" cellpadding="5" height="147" >
<tr>
	<td width="385" height="15" class="smalllabelcell"><b>LOGOUT</b></td>
  </tr>
  <tr>
    <td width="385" height="108">
	  <font color="#0000FF"><b>You have successfully logged out from eGov Admin System.
	  <p>Please click <a id="here" name = "here" href='<c:url value="/eGov.jsp" />'><font size="2" color="#000F00">here</font> </a>to go to the Home Page.<br></p></b></font>

    </td>
  </tr>

</table>

</div>
</div></div></div>
</td></tr>
</table>
</div>
<p>
  <hr width="700">

    <p align=center>
	     <font id="footer_font"><font color="#0000FF">e</font>Gov
		Administration Module Designed and Implemented by <font color=#0000ff>
				<a href="http://www.egovernments.org">eGovernments Foundation</a></font></font>
    </P></P>


</BODY>
</HTML>




