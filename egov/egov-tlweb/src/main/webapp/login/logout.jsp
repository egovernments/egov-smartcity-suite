<%@ include file="/includes/taglibs.jsp"%>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices" %>

<%
if (request.getSession(false) != null) {
    session.invalidate();
}
Cookie terminate = new Cookie(TokenBasedRememberMeServices.ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY, null);
String contextPath = request.getContextPath();
terminate.setPath(contextPath != null && contextPath.length() > 0 ? contextPath : "/");
terminate.setMaxAge(0);
response.addCookie(terminate);
%>


<HTML>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 <title>Logout Page</title>
	    <link href="${pageContext.request.contextPath}/css/professionaltax.css" rel="stylesheet" type="text/css" />
<link href="/commons/css/commonegov.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/pt.js"></script>  
	

 
</head>

<BODY>

 <br>
<div class="formmainbox"><div class="formheading"></div><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="59%"><div class="logouttext"><img src="../images/disconnect.png" alt="Disconnect" width="16" height="16" align="absmiddle" /> You are logged out... </div>
              <div class="logoutmessage"> You have successfully logged out from eGov Admin System. 
		Please <a  href='<c:url value="/eGov.jsp" />' style="font-size:12px">click here</a> to go to the Home Page.</div></td>

          </tr>
        </table>
	  </div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
</div>
<div class="buttonbottom">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>

</BODY>
</HTML>




