#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
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
