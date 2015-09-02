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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import=" org.w3c.dom.html.*,
  				 java.util.*"
%>
<HTML>
<HEAD>

	<TITLE>Login Page</TITLE>
	<SCRIPT language=JavaScript type=text/javascript>
	function appendURL()
	{
		var loc = window.location;
		var uname = document.loginform.j_username.value + '<:1>' + loc;
		document.loginform.j_username.value = uname;
	}

		function curserPosition()
		{
			document.loginform.username.focus();
		}

		function validation()
		{
			if(document.loginform.username.value == "")
			{
				alert("Pls Fill in Username");
				return false;
			}
		}
    </SCRIPT>

</HEAD>
<LINK REL=stylesheet HREF="../../commons/css/egov.css" TYPE="text/css">
<LINK REL=stylesheet HREF="../../commons/css/ccMenu.css" TYPE="text/css">
<BODY onload="curserPosition()">

<div align="center">
<table align='center'>
<tr>
<td style="buffer:0pt; padding:0pt; margin:0pt">
<div id="main"><div id="m2"><div id="m3">
	<table>
		<tr height="90" valign="center">
			<td vAlign="center"  align="left"  width="250">
			<p align="center">
			<a  id="imaID" name="imaID" href="//ndmc.gov.in">
			<img src="../../commons/images/logo-1.jpg" width="70" height="70" border="0">
			</a>
			</td>

			<td vAlign="center"  align="center"  width="252"><p align="center"></td>
			<td  width="100%"  align="center"><font size = "6" id="header_font" >eGov Payroll System<br></font></td>

			<td align="middle"  width="250">
			<a id="orgID" name="orgID" href="//www.egovernments.org"><img src="../../commons/images/egovlogo-pwr-trans.gif" border="0" width="70" height="70" align="right"   id="logoID"> </a></td>
			<td bgColor="#FFFFFF" height="102" width="1"> </td>
		  <tr>
	</table>
</div></div></div>
</td>
</tr>
</table>
</div>


<Center>
<form method="POST" action="/login/j_security_check" name="loginform" id="formid" onSubmit="appendURL();return validation();">

<center>
	<div align="center">
	<table align='center'>
	<tr>
	<td style="buffer:0pt; padding:0pt; margin:0pt">
	<div id="main"><div id="m2"><div id="m3">

	<table align="center" cellspacing="0" cellpadding="5">
	 <tr >
	 	<td>
	 	 <p align="center"><b>
		  <font color="#FF0000">The username or password you entered is wrong.
        Please re-enter.</font> </b></p>
        <table align="center" border="1" cellspacing="0" bgColor="ccccff" width="442" cellpadding="5">
		  <tr>
			<td align="center" width="428"  colspan="2"><b>LOGIN&nbsp;</b></td>
		  </tr>
		</table>
		<table align="center" border="1" cellspacing="0" width="439" cellpadding="0" >
		  <tr>
			<td width="425">
			  <table border="0" cellspacing="0" width="439" cellpadding="5" id="sellertable">
			    <tr>
			      <td width="200">
			        <p align="right"><b><FONT face="Mangal" size="3"> <bean:message key="userName"/> </FONT></b></p>
			      </td>
			      <td width="200">
			        <p><input type="text" name="j_username" id ="username" size="20"></p>
			      </td>
			    </tr>
			    <tr>
			      <td width="200">
			        <p align="right"><b><FONT face="Mangal" size="3"> <bean:message key="password"/> </FONT></b></p>
			      </td>
			      <td width="200">
			        <input type="password" name="j_password"  maxLength="16" size="20">
			      </td>
			    </tr>
			    <tr align="center">
			      <td  height="24" colspan="2">
			        <input type="submit" name="submit" value = "Submit">
			      </td>
			    </tr>
			  </table>
			</td>
	      </TR>
        </TABLE>
       </td>
      </tr>
	</table>
	</div></div></div>
	</td>
	</tr>
	</table>
	</div>
</center>
</form>

<p>
  <hr width="700">

    <p align=center>
	     <font id="footer_font"><font color="#0000FF">e</font>Gov<font color="#0000FF">e</font>rn
		Property Tax Module Designed and Implemented by <font color=#0000ff>
				<a href="http://www.egovernments.org">eGovernments Foundation</a></font></font>
    </P></P>

</BODY>
</HTML>
