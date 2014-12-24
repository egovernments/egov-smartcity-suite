<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page import="org.egov.payroll.client.advance.AdvanceDisbursementForm"%>

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
<% AdvanceDisbursementForm adf=(AdvanceDisbursementForm)request.getAttribute("AdvanceDisbursementForm"); %>
 <table align='center' id="table2" style="width:580px;" >
<tr>
<td>
 <!--<div id="main"><div id="m2"><div id="m3" > -->
<html:form  action="/salaryadvance/advanceDisbursement">
 <table align='center' class="tableStyle2" id="mainTable" name="mainTable" style="width:580px">
 <tr>       
      <td colspan="4" class="tableheader" align="center" ><div align="center" >Advance Disbursement</div></td>
 </tr> 
	 	<tr><td>&nbsp;</td> </tr> 	
	 			
		<tr>			 
			<td class="labelcellforsingletd" colspan="2" ><div align="left" style="width:300px;FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Disbursement Method&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getDisbMethod()%></div></td> 			
			<td class="labelcellforsingletd" height="30" colspan="2" ><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Department&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=(!adf.getDepartment().equals("0") ? adf.getDepartment() : "")%></div></td> 													
		</tr>
		
	 	<tr>			
			<td class="labelcellforsingletd" height="20" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Advance Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getAdvanceType()%></div></td> 					
			<td class="labelcellforsingletd" height="30" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Fund&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getFundName()%></div></td> 
		</tr>		
	 			 	
 		<tr> 		
 			<td class="labelcellforsingletd" align="left" colspan="2"><div style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Sanctioned Amount&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getTotalAmount()%></div></td>  	
 			<td colspan="2"></td>
 		</tr>
 		 		
		<tr>			
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Voucher Number&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getVoucherNo()%></div></td> 		
			<td class="labelcellforsingletd" height="30" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Voucher Date&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getVoucherDate()%></div></td> 													
		</tr>
		
		<tr>			
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Paid By&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=(adf.getPaidBy()!=null) ? adf.getPaidBy(): "" %></div></td> 					
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Paid To&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=(adf.getPaidTo()!=null) ? adf.getPaidTo(): "" %></div></td> 					
		</tr>
		
		<tr>			
			<td class="labelcellforsingletd" height="30" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Bank&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getBank()%></div></td> 
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Bank Account&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getBankAccount()%></div></td> 
		</tr>

		<tr>			
			<td class="labelcellforsingletd" height="30" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Cheque Number&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getChequeNo()%></div></td> 
			<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 10px"><b>&nbsp;&nbsp;&nbsp;Cheque Date&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=adf.getChequeDate()%></div></td> 
		</tr>			
	
<table align="center"> 
<tr><td>&nbsp;</td></tr>
<tr  id="row2" name="row2">
<td  align="center" colspan=4>
<input type=hidden name="button" id="button"/>
<html:button styleClass="button" value="Print" property="b2" onclick="pageSetup();buttonPrint()" />
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