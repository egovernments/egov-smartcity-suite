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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import= "org.egov.infstr.utils.*,
 		java.util.*"
%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Legal Case Management System</title>
 <link rel="stylesheet" type="text/css" href="<egov:url path='/css/professionaltax.css'/>"/>
		<script type="text/javascript" src="<egov:url path='/js/pt.js'/>"></script>
	
</head>
<body>

<div class="navibar"><div align="right">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%
		if (request.getUserPrincipal() != null) //user has logged in
		{

	   %>
	   <tr>
		 <td width="50%" align="left">
		   <p ><b><font id="welcome_font">
		   <span id="headerusername">Welcome &nbsp;
		   <%=EgovUtils.getPrincipalName(request.getUserPrincipal().getName()) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 </span> <td  width="50%" align="right">
		  <p ><b><font id="welcome_font">
		  <a id="logouthref" name="logouthref" href="#" onclick="javascript:top.location='/egi/logout.do';top.opener.location = '/egi/logout.do';" > Log out</a>

			 </font></b></p>
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
		</tr>
	   <%
		}

	  %>
	
  </table></div>
</div>
<div class="formmainbox"><div class="formheading"></div><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="41%"><img src="images/iconPropertytax.jpg" alt="Login" width="368" height="292" /></td>
            <td width="59%">
            <div class="welcomestylel">Welcome to eGov- Professional <br />
              Tax Management System</div>
            <div class="welcomestylearrow"><img src="<egov:url path='images/arrow_left.png'/>" alt="Left" width="16" height="16" align="absmiddle" /> Use the links on the left to navigate 
            through the application.</div></td>
          </tr>
        </table>
	  </div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
</div>
<div class="buttonbottom">City Administration System Designed and Implemented by <a href="#" onclick=window.open("http://www.egovernments.org/")>eGovernments Foundation</a> All Rights Reserved </div>
</body>
</html>
