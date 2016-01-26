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
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<%@ page import="com.exilant.eGov.src.transactions.brs.DishonoredChequeForm"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

<LINK rel="stylesheet" type="text/css" href="../commoncss/egov.css">

<script>

function buttonPrint()
{       
	document.getElementById("row2").style.display="none";
	window.print();   
	document.getElementById("row2").style.display="block";
}

function onBodyLoad()
 {	
 		
 //bootbox.alert("Inside onBodyLoad-in view jsp");
 		
  		
 			
	
}	

</script>

</head>

<body onload="onBodyLoad();">
<% DishonoredChequeForm disForm=(DishonoredChequeForm)request.getAttribute("DishonoredChequeForm"); %>

 <table align='center' id="table2" style="width:620px;" >
<tr>
<td>
 <!--<div id="main"><div id="m2"><div id="m3" > -->
<html:form  action="/brs/DishonoredChequeEntries.do">


 <table align='center' class="tableStyle2" id="mainTable" name="mainTable" style="width:620px">
<!-- <tr>
       
      <td colspan="4" class="tableheader" align="center" ><div align="center" >Dishonored Cheque - Reversal Voucher Detail</div></td> 

 </tr> -->
	
 
	<tr><td>&nbsp;</td> </tr>
	
	<tr>			
		<td class="labelcellforsingletd" height="20" colspan="2"><div align="left" style="FONT-SIZE:12px"><b><u><%=disForm.getVouHName()%>&nbsp;&nbsp;&nbsp;:</div></td> 		
	</tr>
	
	
	<tr>			
		<td class="labelcellforsingletd" height="20" colspan="2"><div align="left" style="FONT-SIZE: 12px"><b>&nbsp;&nbsp;&nbsp;Voucher Number&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=disForm.getVoucherNumber()%></div></td> 		
		<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 12px"><b>&nbsp;&nbsp;&nbsp;Voucher Date&nbsp;&nbsp;:&nbsp;&nbsp;</b><%=disForm.getVouDate()%></div></td> 
	
	</tr>

	
	<tr>			
		<td class="labelcellforsingletd" height="20" colspan="2"><div align="left" style="FONT-SIZE: 12px"><b>&nbsp;&nbsp;&nbsp;Fund &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=disForm.getFund()%></div></td> 		
		<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 12px"><b>&nbsp;&nbsp;&nbsp;Reason For Reversal &nbsp;&nbsp;:&nbsp;&nbsp;</b><%=disForm.getReason()%></div></td> 
			
	</tr>
		
	<tr><td>&nbsp;</td> </tr>			
					
			 		 
	<tr id="detailRow" name="detailRow">
	<td colspan=4 align="center">
	<table cellpadding="0" cellspacing="0" align="center" id="details" name="details" style="width:620px;">
		<tr> 				
			<td  class="thStlyle"><div align="center" style="FONT-SIZE: 12px">Account Code</div></td>
			<td  class="thStlyle"><div align="center" style="FONT-SIZE: 12px">Description</div></td>
			<td  class="thStlyle"><div align="center" style="FONT-SIZE: 12px">Debit</div></td>	
			<td  class="thStlyle"><div align="center" style="FONT-SIZE: 12px">Credit</div></td>	
		</tr>
		
	<% 
		if(disForm.getReversalAccCode()!=null)
		{
			for(int i=0; i<disForm.getReversalAccCode().length;i++)
			{
	%>	 
		<tr> 							
			<td class="tdStlyle"><div align="left" name="reversalAccCode" id="reversalAccCode" style="width:110px;FONT-SIZE:12px"><%=disForm.getReversalAccCode()[i]%></div></td>
			<td class="tdStlyle"><div nowrap align="left" name="reversalDescn" id="reversalDescn" style="width:110px;FONT-SIZE: 12px"><%=disForm.getReversalDescn()[i]%></div></td>	
			
			<td class="tdStlyle" align="right"><div align="left" name="reversalDebitAmount" id="reversalDebitAmount" style="width:65px;text-align:right;FONT-SIZE: 12px"><%=disForm.getReversalDebitAmount()[i]%></div> </td>				
			<td class="tdStlyle" align="right"><div align="left" name="reversalCreditAmount" id="reversalCreditAmount" style="width:65px;text-align:right;FONT-SIZE: 12px"><%=disForm.getReversalCreditAmount()[i]%> </div></td>				
		</tr>
		<%
		}
		}
		%>
		</table>
	</td>
	</tr>
	
	<tr align="center" id="totalRowForRev" name="totalRowForRev">
		<td colspan=4>
		<table cellpadding="0" cellspacing="0" style="width:620px">	
			<tr>
				<td class="tdStlyle" ><div style="width:67px"></div></td>
				<td class="thStlyle" align="right"><div style="width:122px;FONT-SIZE: 12px">TOTAL</div></td>
				<td class="tdStlyle" align="right"><div align="left" name="passedAmount" id="passedAmount" style="width:55px;text-align:right;FONT-SIZE:12px"><b><%=disForm.getPassedAmount()%></div></td>
				<td class="tdStlyle" align="right"><div align="left" name="passedAmount" id="passedAmount" style="width:55px;text-align:right;FONT-SIZE:12px"><b><%=disForm.getPassedAmount()%></b></div></td>				
			</tr>			
		</table>
	</td>
 	</tr>
	
	
	<!-- 		********** FOR BANK CHARGES START ******************		-->

	
	<tr><td>&nbsp;</td> </tr>
	
	<tr id="bankEntry" name="bankEntry" style="display:none">
	<td>
	
	<% 
	if(disForm.getReversalAccCodeBC()!=null)
	{
				
	%>	
	
	<tr>			
		<td class="labelcellforsingletd" height="20" colspan="2"><div align="left" style="FONT-SIZE:12px"><b><u><%=disForm.getVouHNameBC()%>&nbsp;&nbsp;&nbsp;:</div></td> 		
	</tr>
	
	
	<tr>			
		<td class="labelcellforsingletd" height="20" colspan="2"><div align="left" style="FONT-SIZE: 12px"><b>&nbsp;&nbsp;&nbsp;Voucher Number&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=disForm.getVoucherNumberBC()%></div></td> 		
		<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 12px"><b>&nbsp;&nbsp;&nbsp;Voucher Date&nbsp;&nbsp;:&nbsp;&nbsp;</b><%=disForm.getVouDateBC()%></div></td> 
	
	</tr>
	<tr>			
		<td class="labelcellforsingletd" height="20" colspan="2"><div align="left" style="FONT-SIZE: 12px"><b>&nbsp;&nbsp;&nbsp;Ref Number&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;<%=disForm.getRefNo()%></div></td> 		
		<td class="labelcellforsingletd" colspan="2"><div align="left" style="FONT-SIZE: 12px"><b>&nbsp;&nbsp;&nbsp;Ref Date&nbsp;&nbsp;:&nbsp;&nbsp;</b><%=disForm.getRefDate()%></div></td> 
		
	</tr>
		
	<tr>			
		<td class="labelcellforsingletd" colspan="2"><div nowrap align="left" style="FONT-SIZE:12px"><b>&nbsp;&nbsp;&nbsp;Reason For Reversal &nbsp;&nbsp;:&nbsp;&nbsp;</b><%=disForm.getReasonBC()%></div></td> 
			
	</tr>
	
	<tr><td>&nbsp;</td> </tr>
		
	<tr id="detailRowBank" name="detailRowBank">
	<td colspan=4 align="center">
	<table cellpadding="0" cellspacing="0" align="center" id="detailsBank" name="detailsBank" style="width:620px;">
		<tr> 				
			<td class="thStlyle"><div align="center" style="FONT-SIZE: 12px">Account Code</div></td>
			<td class="thStlyle"><div align="center" style="FONT-SIZE: 12px">Description</div></td>
			<td class="thStlyle"><div align="center" style="FONT-SIZE: 12px">Debit</div></td>	
			<td class="thStlyle"><div align="center" style="FONT-SIZE: 12px">Credit</div></td>	
		</tr>
		
	<% 
		if(disForm.getReversalAccCodeBC()!=null)
		{
			for(int i=0; i<disForm.getReversalAccCodeBC().length;i++)
			{
	%>	 
		<tr> 							
			<td class="tdStlyle"><div align="left" name="reversalAccCodeBC" id="reversalAccCodeBC" style="width:110px;FONT-SIZE: 12px"><%=disForm.getReversalAccCodeBC()[i]%></div></td>
			<td class="tdStlyle"><div nowrap align="left" name="reversalDescnBC" id="reversalDescnBC" style="width:110px;FONT-SIZE: 12px"><%=disForm.getReversalDescnBC()[i]%></div></td>	
			
			<td class="tdStlyle" align="right"><div align="left" name="reversalDebitAmountBC" id="reversalDebitAmountBC" style="width:65px;text-align:right;FONT-SIZE: 12px"><%=disForm.getReversalDebitAmountBC()[i]%></div> </td>				
			<td class="tdStlyle" align="right"><div align="left" name="reversalCreditAmountBC" id="reversalCreditAmountBC" style="width:65px;text-align:right;FONT-SIZE: 12px"><%=disForm.getReversalCreditAmountBC()[i]%> </div></td>				
		</tr>
		<%
		}
		}
		%>
		</table>
	</td>
	</tr>	
	
	<tr align="center" id="totalRowForBk" name="totalRowForBk">
	 	<td colspan=4>
	 		<table cellpadding="0" cellspacing="0" style="width:620px">	
	 			<tr>
	 				<td class="tdStlyle" ><div style="width:67px"></div></td>
	 				<td class="thStlyle" align="right"><div style="width:122px;FONT-SIZE: 12px">TOTAL</div></td>
	 				<td class="tdStlyle" align="right"><div align="left" name="bankTotalAmt" id="bankTotalAmt" style="width:40px;text-align:right;FONT-SIZE: 12px"><b><%=disForm.getBankTotalAmt()%></div></td>
	 				<td class="tdStlyle" align="right"><div align="left" name="bankTotalAmt" id="bankTotalAmt" style="width:40px;text-align:right;FONT-SIZE: 12px"><b><%=disForm.getBankTotalAmt()%></b></div></td>				
	 			</tr>			
	 		</table>
	 		</td>
 	</tr>
	
	<%
	}
	%>

	</td>
	</tr>
	
	<tr><td>&nbsp;</td> </tr>

	<!-- 		********** FOR BANK CHARGES END ******************		-->
	
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
