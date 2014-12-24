<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
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