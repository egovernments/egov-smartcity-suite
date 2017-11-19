<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<%@ page import="org.egov.payment.client.PaymentAdviceForm"%>
<%@ page buffer = "30kb" %>
<html>

<title>Payment Advice</title>



<script>
function buttonPrint()
{       
	document.getElementById("row2").style.display="none";
	window.print();   
	document.getElementById("row2").style.display="block";
}

</script>
</head>

<body>
<% PaymentAdviceForm paf=(PaymentAdviceForm)request.getAttribute("PaymentAdviceForm"); %>

 <html:form  action="/payment/paymentAdvice">
 <table align='center' class="tableStyle" id="table3" name="table3">
 
 <tr><td>&nbsp;</td></tr>
	<%				
			if(paf.getChqNo()!=null && paf.getChqNo().length>0)
			{
		%>		
		<tr>
		<td colspan=4 align="center">
		<table cellpadding="0" cellspacing="0" align="center" id="vhList" name="vhList">
		<tr>
			<td class="thStlyle"><div align="center">Sl No</div></td>
			<td class="thStlyle"><div align="center">Cheque Number</div></td>
			<td class="thStlyle"><div align="center">Cheque Date</div></td>
			<td class="thStlyle"><div align="center">Party Name</div></td>
			<td class="thStlyle"><div align="center">Account No</div></td>
			<td class="thStlyle"><div align="center">Amount</div></td>
		</tr>
			<%
			for(int i=0; i<paf.getChqNo().length;i++)
			{
			%>	
			
		<tr>
			<td class="tdStlyle"><div align="left" id="slNo" name="slNo"><%= i+1 %></div> </td>
			<td class="tdStlyle"><div align="left" id="chqNo" name="chqNo"><%= paf.getChqNo()[i] %></div> </td>
			<td class="tdStlyle"><div align="left" id="chqDate" name="chqDate"><%= paf.getChqDate()[i] %></div> </td>
			<td class="tdStlyle"><div align="left" id="partyName" name="partyName"><%= paf.getPartyName()[i] %></div> </td>
			<td class="tdStlyle"><div align="left" id="accountNo" name="accountNo" ><%= paf.getAccountNo()[i] %></div> </td>			
			<td class="tdStlyle"><div align="left" id="amount" name="amount" style="text-align:right"><%= paf.getAmount()[i] %></div> </td>
		</tr>
			<%
			}
			%>      
		</table>
		</td>
		</tr>
		<%
			}
		%>
	<tr  id="row2" name="row2">
	<td  align="center" colspan=4>
	<input type=hidden name="button" id="button"/>
	<html:button styleClass="button" value=" Print " property="b1" onclick="buttonPrint()"/>
	<html:button styleClass="button" value=" Close " property="b3" onclick="window.close()"/>
	<html:reset styleClass="button" value="Back" property="b4" onclick="history.go(-1)"/>
	</td>
	</tr>

 </table> 
 </html:form>


  </body>
</html>	
