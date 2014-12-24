<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="org.egov.payroll.client.advance.AdvanceDisbursementByChequeForm"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Print Cheque Disbursement</title>
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
<% AdvanceDisbursementByChequeForm adf=(AdvanceDisbursementByChequeForm)request.getAttribute("AdvanceDisbursementByChequeForm"); %>
 <table align='center' id="table2" style="width:580px;" >
<tr>
<td>
 <!--<div id="main"><div id="m2"><div id="m3" > -->
<html:form  action="/salaryadvance/disbursementByCheque">
 <table align='center' class="tableStyle2" id="mainTable" name="mainTable" style="width:580px">
 <tr>       
      <td colspan="4" class="tableheader" align="center" ><div align="center" >Advance Disbursement By Cheque</div></td>
 </tr>	
 
	 	<tr><td>&nbsp;</td> </tr>	 	
	 			 
		<tr>			
			<td class="labelcellforsingletd" colspan="2" ><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Employee Code&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getEmployee()%></div></td> 			
			<td class="labelcellforsingletd" height="30" colspan="2" ><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Employee Name&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getEmployeeName()%></div></td> 													
		</tr>
		
	 	<tr>			
			<td class="labelcellforsingletd" height="20" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Fund&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getFundName()%></div></td> 					
			<td colspan="2"></td>
		</tr>		
	 				 		 
		<tr id="detailRow" name="detailRow">
 		<td colspan=4 align="center">
 		<table cellpadding="0" cellspacing="0" align="center" id="details" name="details" style="width:550px;"> 			
 			<tr> 				
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 10px">Advance Type</div></td>
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 10px">Sanction No</div></td>
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 10px">Sanctioned By</div></td>
 				<td class="thStlyle"><div align="center" style="FONT-SIZE: 10px">Sanction Amount</div></td> 						
 			</tr>
 		<% 
 			if(adf.getAdvanceType()!=null)
 			{
 				for(int i=0; i<adf.getAdvanceType().length;i++)
 				{
 		%>	 
 			<tr> 							
 				<td class="tdStlyle"><div align="left" style="width:110px;FONT-SIZE: 10px"><%=adf.getAdvanceType()[i]%></div></td>	
 				<td class="tdStlyle"><div align="left" style="width:100px;FONT-SIZE: 10px" /> <%=adf.getSanctionNo()[i]%>  </td>
 				<td class="tdStlyle"><div align="left" style="width:130px;FONT-SIZE: 10px"><%=adf.getSanctionedBy()[i]%></div> </td>
 				<td class="tdStlyle"><div align="left" style="width:100px;text-align:right;FONT-SIZE: 10px"><%=adf.getSanctionAmount()[i]%></div></td>			
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
 		<table cellpadding="0" cellspacing="0" style="width:550px">	
 			<tr>
 				<td class="tdStlyle" ><div style="width:110px"></div></td>
 				<td class="tdStlyle" ><div style="width:100px"></div></td>
 				<td class="thStlyle"><div style="width:130px;text-align:center;FONT-SIZE: 10px">TOTAL</div></td> 				
 				<td class="tdStlyle"><div align="left" style="width:100px;text-align:right;FONT-SIZE: 10px"><%=adf.getTotalAmt()%></div></td> 				
 			</tr>			
 		</table>
 		</td>
 		</tr>
 		 		
		<tr>			
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Voucher Number&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getVoucherNo()%></div></td> 		
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Voucher Date&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getVoucherDate()%></div></td> 													
		</tr>
		
		<tr>			
			<td class="labelcellforsingletd" colspan="4"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Paid By&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=(adf.getPaidBy()!=null) ? adf.getPaidBy(): "" %></div></td> 					
		</tr>
		
		<tr>			
			<td class="labelcellforsingletd" height="30" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Bank&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getBank()%></div></td> 
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Bank Account&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getBankAccount()%></div></td> 
		</tr>

		<tr>			
			<td class="labelcellforsingletd" height="30" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Cheque Number&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getChequeNo()%></div></td> 
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Cheque Date&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getChequeDate()%></div></td> 
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