<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page isErrorPage="true"%>
<%@ taglib prefix="s"    uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>Error Page</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title>COC Property Tax Error Page</title>
</head>
<body>

<div class="formmainbox"><div class="formheading"></div><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            
             <%  if(request.getAttribute("errorReason")==null)
              {%>
              <td width="59%"><div class="logouttext">
               <img src="/image/error.png" width="128" height="128" alt="Error" /><div class="oopstext">oops!</div>
                Sorry your request cannot be processed!
                 <span class="bold">Error Occured...</span></div></td>
                <%}
                else
                {%>
                  <td width="20"><div class="logouttext">
               <font size="4"> <%= request.getAttribute("errorReasonVal")%></font></div>
               <% }%><br />
               </td>
          </tr>
        </table>
	  </div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
</div>

                <s:property value="exception.message"/>
        <s:actionerror/>
        <s:fielderror />
        <s:property value="exceptionStack"/>
        
<c:forEach var="st" items="${pageContext.exception.stackTrace}">
  ${st}
</c:forEach>        

</body>
</html>
