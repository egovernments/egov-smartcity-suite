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
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page  import="com.exilant.eGov.src.reports.*,java.io.*,javax.naming.InitialContext"%>
<html>
<head>
<title>Cheque Details</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">

<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print" />
<SCRIPT LANGUAGE="javascript">

</SCRIPT>
<title> Cheque Details</title>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0"  >
<jsp:useBean id = "crReportBean" scope ="request" class="com.exilant.eGov.src.reports.ChequeRegisterBean"/>
<jsp:setProperty name ="crReportBean" property="vhId"/>
<jsp:setProperty name ="crReportBean" property="chequeNo"/>
<jsp:setProperty name ="crReportBean" property="pymtNature"/>
<form name="chqRegDet" action = "ChequeRegisterDetail.jsp?" method = "post">
<input type="hidden" name="vhId" id="vhId" value="<%=request.getParameter("vhId")%>">
<input type="hidden" name="chequeNo" id="chequeNo" value="<%=request.getParameter("chequeNo")%>">
<input type="hidden" name="pymtNature" id="pymtNature" value="<%=request.getParameter("pymtNature")%>">
<br>
<br>

<%    
   	 ChequeRegister chequeReg = new ChequeRegister();
   	 try{
   	   		request.setAttribute("links",chequeReg.getChequeDetails(crReportBean));
	    } 
	    catch(Exception e) 
	    { 
	   	 //System.out.println("Error:"+"connecting to database failed");
	    }

%>	

<div class ="normaltext"><b><font size=2>Cheque Details</font></b></div>
	<div id="tbl-container" style="width:620px">
	<display:table style="width:580px" cellspacing="0" name="links"  id="currentRowObject" export="false" class="its" >
		<div STYLE="display:table-header-group" >
			<display:column property="nameOfPayee" title="Name of&nbsp;the Payee" group="1"/>
			<display:column property="billNo" title="Bill&nbsp;No" />
			<display:column property="billAmount" title="Bill&nbsp;Amount" class="textAlign" />
			<display:column property="payeeAmount" title="Payee&nbsp;Amount" class="textAlign"/>
		 	<display:column property="chequeNo" title="Cheque No" group="2" />
			<display:column property="chequeAmount" title="Cheque&nbsp;Amount" class="textAlign3" group="3" />
		<display:footer>		
			<tr>
				<td style="border-left: solid 0px #000000" colspan="10"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>  		
			</tr>
  		</display:footer> 
  	</div> 		
	</display:table>	
	</div>
	
	<div id="buttonrow" name="buttonrow">
	<table border="0" cellpadding="0" cellspacing="0" >
		<tr>			
			<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>
			<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=6></td>
			<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick=window.close() href="#">Close</A></td>
			<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=6></td>
		</tr>		 
	</table>
	</div>
</form>
</body>
</html>
