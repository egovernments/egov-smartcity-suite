#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------
<%@page language = "java"
   import = "java.util.*"
 %>
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="/EGF/commoncss/egov.css" type="text/css">
	<TITLE>Login Page</TITLE>
	<SCRIPT language=JavaScript type=text/javascript>
		function onBodyLoad(){	
			document.forms[0].setAttribute("AutoComplete","off");
			document.getElementById('username').focus();
		}	
		function validation()
		{

			if(document.loginform.username.value == "")
			{
				alert("Pls Fill in Username");
				return false;
			}
		}

function appendURL()
{
	var loc = window.location;
	var uname = document.forms[0].j_username.value + '<:1>' + loc;
	document.forms[0].j_username.value = uname;


}

</SCRIPT>
</HEAD>
<body onLoad="onBodyLoad()" bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
<form method="POST" action="/EGF/HTML/j_security_check" name="loginform" id="formid" onSubmit="appendURL();return validation();">

<center>
	<table width="100%" border=0 cellpadding="0" cellspacing="0" align="center"><!------------Content begins here ------------------>			
							<tr width=100%>
								<td width=100%>									
									<table  border=0 cellpadding="3" cellspacing="0" width=100%>
										
										<tr >
											<td >&nbsp;</td>
											<td ></td>
											<td ></td>
											<td >&nbsp;</td>
										</tr>	
										<tr >
											<td >&nbsp;</td>
											<td ></td>
											<td ></td>
											<td >&nbsp;</td>
										</tr>	
										<tr >
											<td >&nbsp;</td>
											<td ></td>
											<td ></td>
											<td >&nbsp;</td>
										</tr>	
										<tr >
											<td >&nbsp;</td>
											<td ></td>
											<td ></td>
											<td >&nbsp;</td>
										</tr>	
										<tr >
											<td >&nbsp;</td>
											<td ></td>
											<td ></td>
											<td >&nbsp;</td>
										</tr>	
										<tr >
											<td >&nbsp;</td>
											<td  class="labelcell"><div align="right" exilDataType="exilAlphaNumeric">User Id</div></td>
											<td  class="fieldcell"><input type="text" name="j_username" id ="username" size="20"></td>
											<td >&nbsp;</td>
										</tr>	
										<tr >
											<td >&nbsp;</td>
											<td  class="labelcell"><div align="right">Password</div></td>
											<td  class="fieldcell"><input type="password" name="j_password"  maxLength="128" size="20"></td>
											<td >&nbsp;</td>
										</tr>
										<tr >
											<td >&nbsp;</td>
											<td >&nbsp;</td>
											<td   align="middle" class="normaltext">
												<input type="submit" class="button" name="Login" value="Login" >
											</td>	
											<td >&nbsp;</td>				
										</tr>			
										<tr  >
											<td >&nbsp;</td>
											
										</tr>	
									</table>
																		
							</td>						
					</tr>									
			</table>
			</center>
		</form>
	</body>
</html>

