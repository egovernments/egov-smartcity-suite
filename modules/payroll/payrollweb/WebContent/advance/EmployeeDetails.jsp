<%@ include file="/includes/taglibs.jsp" %>

<%@page  import="java.util.*,java.text.*,
org.egov.payroll.model.Advance,
org.egov.pims.model.BankDet"%>
<html>
	<head>	
		<title>Disbursement-Employee-wise Breakup</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<meta http-equiv="KEYWORDS" content="">
		<meta http-equiv="DESCRIPTION" content="">
		<META http-equiv=Pragma content=no-cache>
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="Cache-Control" content="no-cache">

		

<script language='javascript'>

function onBodyLoad()
{
	<% 
		NumberFormat nf=new DecimalFormat("##############.00");
		ArrayList advanceSanctionList=(ArrayList)request.getSession().getAttribute("advSanctionList"); 	
	%>
	var paymentType="<%=request.getParameter("disbMethod")%>"
	if(paymentType=="cash")
	{
		var table=document.getElementById('details');
		for(var i=0;i<table.rows.length;i++)
		{
			table.rows[i].cells[3].style.display="none";
			table.rows[i].cells[4].style.display="none";
		}
	}    
}

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

<body onload="onBodyLoad()" onKeyDown ="CloseWindow(event);">
<form  action="EmployeeDetails.jsp">
<table align='center' id="table2">
<tr>  
<td>
 <table align='center' class="tableStyle" style="width:580px" id="mainTable" name="mainTable">
 <tr>
      <td colspan="5" class="tableheader" align="center"><span id="screenName">Employee-wise Disbursement details<span></td> 
 </tr>
 	<tr>	 		
       	<td colspan="5">&nbsp;</td> 	
	</tr>

 <tr>
 <td> 
	 <table cellpadding="0" cellspacing="0" align="center" id="details" name="details" style="width:580px">
	 		<tr>				
				<td class="thStlyle"><div align="center" style="FONT-SIZE: 10px"><bean:message key="EmployeeCode"/> </div></td>
				<td class="thStlyle"><div align="center" style="FONT-SIZE: 10px"><bean:message key="EmployeeName"/> </div></td>
				<td class="thStlyle"><div align="center" style="FONT-SIZE: 10px"><bean:message key="SanctionNo"/> </div></td>
				<td class="thStlyle"><div align="center" style="FONT-SIZE: 10px"><bean:message key="BankName"/> </div></td>	
				<td class="thStlyle"><div align="center" style="FONT-SIZE: 10px"><bean:message key="AccountNumber"/> </div></td>
				<td class="thStlyle"><div align="center" style="FONT-SIZE: 10px"><bean:message key="SanctionAmount"/> </div></td>				
			</tr>
			<% 
			if(advanceSanctionList!=null && advanceSanctionList.size()>0)
			{				
				BankDet bd=null;			
				for(Iterator it = advanceSanctionList.iterator(); it.hasNext(); ) 
				{
					String bank="",account="";
					Advance salAdvance=(Advance)it.next();					
					for (Iterator it1 = salAdvance.getEmployee().getEgpimsBankDets().iterator(); it1.hasNext(); ) 
					{
						bd=(BankDet)it1.next();
						bank=bd.getBank();
						account=bd.getAccountNumber();
					}					
		%>	
			<tr height="20">
				<td class="tdStlyle"><div align="left" style="FONT-SIZE: 10px;width:75px"><%=salAdvance.getEmployee().getEmployeeCode()%></div> </td>
				<td class="tdStlyle"><div align="left" style="FONT-SIZE: 10px;width:105px"><%=salAdvance.getEmployee().getEmployeeFirstName()%></div></td>
				<td class="tdStlyle"><div align="left" style="FONT-SIZE: 10px;width:77px"><%=salAdvance.getSanctionNum()%></div></td>
				<td class="tdStlyle"><div align="left" style="FONT-SIZE: 10px;width:160px"><%=bank%></div></td>
				<td class="tdStlyle"><div align="left" style="FONT-SIZE: 10px;width:85px"><%=account%></div></td>
				<td class="tdStlyle"><div align="left" style="FONT-SIZE: 10px;width:80px;text-align:right"><%=nf.format(salAdvance.getAdvanceAmt())%></div> </td>				
			</tr>			
			<%	
				}
			}
			%>	
	 </table> 
 </td>
 </tr>
		 <tr><td>&nbsp;</td></tr>
		 <tr>
		 <td>
		 	<table align="center">
			 <tr id="row2" name="row2">
				<td align="center" colspan=4>		
					<html:button styleClass="button" value="Print" property="b2" onclick="pageSetup();buttonPrint()" />	
					<html:button styleClass="button" value="Close" property="b1" onclick="window.close();" />
				</td>
			</tr>
			</table>
			</td>
			</tr>
 </table>
 </td>
 </tr>
</form>
</body>
</html>
