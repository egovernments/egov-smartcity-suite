<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Print Remittance Recovery</title>

<LINK rel="stylesheet" type="text/css" href="../css/egov.css">

<script>

function pageSetup()
{
	document.body.leftMargin=0.50;
	document.body.rightMargin=0.50;
	document.body.topMargin=0.50;
	document.body.bottomMargin=0.50;	
}

function buttonPrint()
{       
	document.getElementById("row2").style.display="none";
	window.print();   
	document.getElementById("row2").style.display="block";
}


</script>

</head>

<body >
<% RemitRecoveryForm rrf=(RemitRecoveryForm)request.getAttribute("RemitRecoveryForm"); %>

 <table align='center' id="table2" style="width:580px;" >
<tr>
<td>
 <!--<div id="main"><div id="m2"><div id="m3" > -->
<html:form  action="/deduction/remitRecovery">
 <table align='center' class="tableStyle2" id="mainTable" name="mainTable" style="width:580px">
 <tr>
       
      <td colspan="4" class="tableheader" align="center" ><div align="center" >Remittance Recovery</div></td> 

 </tr>
	
 
	 	<tr><td>&nbsp;</td> </tr>	
	 	<tr>			
			<td class="labelcellforsingletd" height="20" colspan="2"><div align="left" style="FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Fund&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=rrf.getFund()%></div></td> 		
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Recovery Name&nbsp;&nbsp;:&nbsp;&nbsp;</b><%=rrf.getRecovery()%></div></td> 
														
		</tr>		
	 	<%
	 	String[] m_names = {"","January", "February", "March","April", "May", "June", "July", "August", "September","October", "November", "December"};
		String month=m_names[Integer.parseInt(rrf.getMonth().toString())];
		%>		
		<tr>			
			<td class="labelcellforsingletd" colspan="2" ><div align="left" style="FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Month&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=month%></div></td> 			
			<td class="labelcellforsingletd" height="30" colspan="2" ><div align="left" style="FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Year&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=rrf.getYear()%></div></td> 													
		</tr>			
			 		 
		<tr id="detailRow" name="detailRow">
 		<td colspan=4 align="center">
 		<table cellpadding="0" cellspacing="0" align="center" id="details" name="details" style="width:580px;">
 			<tr> 				
 				<td colspan="3" class="thStlyle"><div align="center" style="FONT-SIZE: 9px">Party</div></td>
 				<td colspan="2" class="thStlyle"><div align="center" style="FONT-SIZE: 9px">Reference</div></td>
 				<td colspan="2" class="thStlyle"><div align="center" style="FONT-SIZE: 9px">Amount</div></td>	
 			</tr>
 			<tr> 				
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 9px">Name</div></td>
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 9px">PAN/GIR #</div></td>
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 9px">Address</div></td>
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 9px">Doc No</div></td>
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 9px">Date</div></td>
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 9px">Deducted</div></td>
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 9px">Remitted</div></td>				
 			</tr>
 		<% 
 			if(rrf.getPartyName()!=null)
 			{
 				for(int i=0; i<rrf.getPartyName().length;i++)
 				{
 		%>	 
 			<tr> 							
 				<td class="tdStlyle"><div align="left" name="partyName" id="partyName" style="width:110px;FONT-SIZE: 9px"><%=rrf.getPartyName()[i]%></div></td>	
 				<td class="tdStlyle"><div align="left" name="partyPAN" id="partyPAN" style="width:62px;FONT-SIZE: 9px" styleClass="narrationfieldinput2" readonly="true" tabindex="-1"/> <%=rrf.getPartyPAN()[i]%>  </td>
 				<td class="tdStlyle"><div align="left" name="partyAddress" id="partyAddress" style="width:130px;FONT-SIZE: 9px"><%=rrf.getPartyAddress()[i]%></div> </td>
 				<td class="tdStlyle"><div align="left" name="refNo" id="refNo" style="width:70px;FONT-SIZE: 9px"><%=rrf.getRefNo()[i]%></div></td>
 				<td class="tdStlyle"><div align="left" name="refDate" id="refDate" style="width:58px;FONT-SIZE: 9px"><%=rrf.getRefDate()[i]%></div></td>
 				<td class="tdStlyle"><div align="left" name="dedAmount" id="dedAmount" style="width:65px;text-align:right;FONT-SIZE: 9px"><%=rrf.getDedAmount()[i]%></div> </td>				
 				<td class="tdStlyle"><div align="left" name="remittAmt" id="remittAmt" style="width:65px;text-align:right;FONT-SIZE: 9px"><%=rrf.getRemittAmt()[i]%> </div></td>				
 			</tr>
 			<%
 			}
 			}
 			%>
 			</table>
 		</td>
 		</tr>		
 	
 		<tr align="center" id="totalRow" name="totalRow">
 		<td colspan=4>
 		<table cellpadding="0" cellspacing="0" style="width:580px">	
 			<tr>
 				<td class="tdStlyle" ><div style="width:110px"></div></td>
 				<td class="tdStlyle" ><div style="width:62px"></div></td>
 				<td class="tdStlyle" ><div style="width:130px"></div></td>
 				<td class="tdStlyle" ><div style="width:70px"></div></td>
 				<td class="thStlyle"><div style="width:58px;FONT-SIZE: 9px">TOTAL</div></td>
 				<td class="tdStlyle"><div align="left" name="totalDedAmt" id="totalDedAmt" style="width:65px;text-align:right;FONT-SIZE: 9px"><%=rrf.getTotalDedAmt()%></div></td>
 				<td class="tdStlyle"><div align="left" name="totalRemittAmt" id="totalRemittAmt" style="width:65px;text-align:right;FONT-SIZE: 9px"><b><%=rrf.getTotalRemittAmt()%></b></div></td>				
 			</tr>			
 		</table>
 		</td>
 		</tr>
 		
 		<tr>			
			<td class="labelcellforsingletd" height="30" colspan="4"><div align="left" style="FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Remit To&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=rrf.getRemitTo()%></div></td> 														
		</tr>
		<tr>			
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Voucher Number&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=rrf.getPymntVhNo()%></div></td> 		
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Voucher Date&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=rrf.getPymntVhDate()%></div></td> 
													
		</tr>
		<tr>			
			<td class="labelcellforsingletd" height="30" colspan="4"><div align="left" style="width:500px;FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Bank&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=rrf.getBank()%></div></td> 
		</tr>
		<tr>
			<td class="labelcellforsingletd" colspan="4"><div align="left" style="FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Bank Account&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=rrf.getBankAccount()%></div></td> 
		</tr>

		<tr>			
			<td class="labelcellforsingletd" height="30" colspan="2"><div align="left" style="FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Cheque Number&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=rrf.getChequeNo()%></div></td> 
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 9px"><b>&nbsp;&nbsp;&nbsp;Cheque Date&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=rrf.getChequeDate()%></div></td> 
		</tr>

			
	
<table align="center"> 
<tr><td>&nbsp;</td></tr>
<tr  id="row2" name="row2">
<td  align="center" colspan=4>
<input type=hidden name="button" id="button"/>
<html:button styleClass="button" value="Print" property="b2" onclick="buttonPrint()" />
<html:button styleClass="button" value="Close" property="b3" onclick="window.close();" />
</td>
</tr>
</table>
 </table>   
 </html:form >
 <!--</div> </div></div>-->
  </td></tr>
  </table>
  </body>
</html>	