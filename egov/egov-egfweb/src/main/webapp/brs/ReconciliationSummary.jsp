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
<%@ page import="java.util.*,
		org.apache.log4j.Logger,
		java.text.DecimalFormat,
		com.exilant.eGov.src.transactions.brs.*,
		java.math.BigDecimal"
%>
<html>

	<title>Reconciliation Summary</title>
	
	<script>
	
	function getAccountNumbers()
	{
		document.forms[0].action = "../brs/ReconciliationSummary.do?submitType=getAccountNumbersForRS";
		if(document.reconciliationSummaryForm.bankId.options[document.reconciliationSummaryForm.bankId.selectedIndex].value != 0)
		{
			
			document.forms[0].submit();
		}
	
	}
	function subType(arg)
	{
		if(arg == "view")
		{
			if(document.reconciliationSummaryForm.bankId.options[document.reconciliationSummaryForm.bankId.selectedIndex].value == 0)
			{
				bootbox.alert("Select Bank");
				return;
			}
			if(document.reconciliationSummaryForm.accId.options[document.reconciliationSummaryForm.accId.selectedIndex].value == 0)
			{
				bootbox.alert("Select Account Number");
				return;
			}
			
			/*
			if(!isNaN(parseFloat(document.reconciliationSummaryForm.balAsPerStatement.value)))
			{
				if(parseFloat(document.reconciliationSummaryForm.balAsPerStatement.value) < 0)
				{
				bootbox.alert("Enter Bank Statement Balance");
				return;
				}
			}
			else
			{
				bootbox.alert("Enter Numeric value for  Bank Statement Balance");
				return;
			}
			*/
			if(document.reconciliationSummaryForm.balAsPerStatement.value == "")
			{
				bootbox.alert("Enter Bank Statement Balance");
				return;
			}
			
			if(isNaN(parseFloat(document.reconciliationSummaryForm.balAsPerStatement.value)))
			{
				bootbox.alert("Enter Numeric value for  Bank Statement Balance");
				return;
			}
			if(document.reconciliationSummaryForm.bankStatementDate.value == "")
			{
				bootbox.alert("Enter Bank Statement Date");
				return;
			}
			else
			{
				var dat=validateDate(document.reconciliationSummaryForm.bankStatementDate.value);
				if (!dat) 
				{
					bootbox.alert('Invalid date format : Enter Date as dd/mm/yyyy');
					document.reconciliationSummaryForm.bankStatementDate.focus();
					return;
				}
			}
			document.forms[0].action = "../brs/ReconciliationSummary.do?submitType=brsSummary";
			document.forms[0].submit();

		}
		
	}
	function onClickCancel()
	{
	
		window.location="../brs/ReconciliationSummary.jsp";		
	}
	function getData()
	{
		
		var obj="<%= (session.getAttribute("accNumberList3")) %>";
		var obj1="<%= (request.getAttribute("unreconciledCheques")) %>";
		<%
		ArrayList unreconciledCheques=(ArrayList)request.getAttribute("unreconciledCheques");
		if(unreconciledCheques!=null)
		{
		%>
			document.getElementById('searchBrs').style.display ='none';
			document.getElementById('gridBankRec').style.visibility = 'visible';
		<%
		}
		else
		{
		%>
		document.reconciliationSummaryForm.bankId.focus();
		<%
		}
		%>
		
	
	}
function printPage()
  {
   
  // bootbox.alert(document.reconciliationSummaryForm.bankId.options[document.reconciliationSummaryForm.bankId.selectedIndex].text);
  // bootbox.alert(document.reconciliationSummaryForm.accId.options[document.reconciliationSummaryForm.accId.selectedIndex].text);
  // bootbox.alert(document.reconciliationSummaryForm.balAsPerStatement.value);
  // bootbox.alert(document.reconciliationSummaryForm.bankStatementDate.value);
  
  
   document.getElementById('bankBranch').value=document.reconciliationSummaryForm.bankId.options[document.reconciliationSummaryForm.bankId.selectedIndex].text;
   document.getElementById('bankAccNo').value=document.reconciliationSummaryForm.accId.options[document.reconciliationSummaryForm.accId.selectedIndex].text;
   document.getElementById('bankStBalance').value=document.reconciliationSummaryForm.balAsPerStatement.value;
   document.getElementById('bankStDate').value=document.reconciliationSummaryForm.bankStatementDate.value;
   
   document.getElementById("main").style.display ="none";
   document.getElementById("m2").style.display ="none";
   document.getElementById("m3").style.display ="none";
   
   document.getElementById("hideRow3").style.display ="block";
   document.getElementById("hideRow4").style.display ="block";
   document.getElementById("hideRow5").style.display ="block";
   
   document.getElementById("hideRowFirst").style.display ="none";
   document.getElementById("hideRowSecond").style.display ="none";
   document.getElementById("hideRow1").style.display ="none";
   document.getElementById("printrow").style.display ="none";
   window.print();
   document.getElementById("hideRow1").style.display ="block";
   document.getElementById("printrow").style.display ="block";
	
  }
	</script>
	
</head>
<body bgcolor="#ffffff"  onLoad="getData()"  onKeyDown ="CloseWindow(window.self); ">
	<% 	Logger logger = Logger.getLogger(getClass().getName()); 
		logger.info(">>       INSIDE JSP   >>");
	%>
<br>

<html:form  action="/brs/ReconciliationSummary.do">

<table align="center" class="tableStyle"  cellpadding="0" cellspacing="0">


<tr id="hideRowFirst">
<td  class="labelcell" align="right" width="25%">Bank And Branch<SPAN class="leadon">*</SPAN></td>
<td class="smallfieldcell" align="center" width="25%"><html:select  property="bankId" onchange="getAccountNumbers()" styleClass="bigcombowidth">
<html:option value='0'>----choose----</html:option>
<%
	HashMap bankId =(HashMap)session.getAttribute("bankBranchList");
	if((bankId!=null)&&(!bankId.isEmpty()))
	{
	TreeSet ts=new TreeSet(bankId.keySet());
	if((ts!=null)&&(!ts.isEmpty()))
	{
		Iterator bankIdIterator = ts.iterator();
		while (bankIdIterator.hasNext())
		{
			String keyVal=(String)bankIdIterator.next();
%>
		<html:option value='<%= keyVal %>'><%= bankId.get(keyVal) %></html:option>

<%
		}
	}
    }
%>
</html:select>
</td>
<td class="labelcell" align="right" width="25%">Account Number<SPAN class="leadon">*</SPAN></td>
<td class="smallfieldcell" align="center" width="25%">
<html:select  property="accId" styleClass="bigcombowidth">
<html:option value='0'>----choose----</html:option>
<%
	HashMap accountId =(HashMap)session.getAttribute("accNumberList3");
	if((accountId!=null)&&(!accountId.isEmpty()))
	{
	TreeSet ts=new TreeSet(accountId.keySet());
	if((ts!=null)&&(!ts.isEmpty()))
	{
		Iterator accountIdIterator = ts.iterator();
		while (accountIdIterator.hasNext())
		{
		String keyVal=(String)accountIdIterator.next();
%>
		<html:option value='<%= keyVal %>'><%= accountId.get(keyVal) %></html:option>

<%
		}
	}
	}

%>
</html:select>
</td>
</tr>
<tr id="hideRowSecond">
<td class="labelcell" align="right">Bank&nbsp;Statement&nbsp;Balance<SPAN class="leadon">*</SPAN></DIV></td>
<td class="fieldcell" align="center"><html:text  style=";text-align:right;padding-right:2px;" property="balAsPerStatement"/> </td>
<td class="labelcell" align="right">Bank&nbsp;Statement&nbsp;date<SPAN class="leadon">*</SPAN></td>
<td class="smallfieldcell" align="center"><html:text  property="bankStatementDate"  onkeyup="DateFormat(this,this.value,event,false,'3')" />
<a href="javascript:show_calendar('reconciliationSummaryForm.bankStatementDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="/egi/resources/erp2/images/calendar.gif" width=24 height=22 border=0></a>
</td>
</tr>
</table>

<tr ><td colspan="4">&nbsp;</td>
<tr>
<td colspan=4>
<div id="searchBrs" name="searchBrs">
<table align=center >
<tr >
	<td align="middle" ><html:button value="View Summary" styleClass="button" property="b1" onclick="subType('view')" /></td>
	<TD><html:button value="Cancel" styleClass="button" property="b1" onclick="onClickCancel()" /></td>
	<td><html:button value="Close" styleClass="button" property="b3" onclick="window.close();" /></td>
<tr>
</table>
</div>
</td>
</tr>



<tr id="hideRow3" style="display:none">
<td colspan="4" class="tableheader" align="center" ><div align="center" >Reconciliation Summary</div></td> 
</tr>
<tr ><td colspan="4">&nbsp;</td> </tr>
<tr id="hideRow4" style="display:none">
<td class="labelcell" align="right">Bank And Branch</DIV></td>
<td class="fieldcell" align="center">
<input name="bankBranch" class="fieldinput-right" id="bankBranch" readOnly>
</td>

<td class="labelcell" align="right">Account Number</td>
<td class="fieldcell" align="center">
<input name="bankAccNo" class="fieldinput-right" id="bankAccNo" readOnly>
</td>
</tr>


<tr id="hideRow5" style="display:none">
<td class="labelcell" align="right">Bank&nbsp;Statement&nbsp;Balance</DIV></td>
<td class="fieldcell" align="center">
<input name="bankStBalance" class="fieldinput-right" id="bankStBalance" readOnly>
</td>

<td class="labelcell" align="right">Bank&nbsp;Statement&nbsp;date</td>
<td class="smallfieldcell" align="center">
<input name="bankStDate" class="fieldinput-right" id="bankStDate" readOnly>
</td>
</tr>



<%  DecimalFormat df = new DecimalFormat("0.00");
		String val=((String)request.getAttribute("accountBalance"));
		if(val != null)
		{
			
	
	%>
<tr>
<td colspan=4>&nbsp;
</td>
</tr>
<tr>
<td colspan=4>
<table align=center  cellpadding="0" cellspacing="0" ID="gridBankRec" name="gridBankRec" style="VISIBILITY: hidden">
		<tr style="HEIGHT: 22px">
				<td colspan=3  width="70%" class="thStlyle"><div align="center" >Particulars</div></td>
				<td width="15%" class="thStlyle"><div align="center" >Amount (Rs)</div></td>
				<td width="15%" class="thStlyle" ><div align="center" >Amount (Rs)</div></td>
		</tr>
		 <tr >
		     <td colspan=3 class="thStlyle" valign="center" >&nbsp;&nbsp;&nbsp; Balance as per Bank Book</td>
		     <td class="tdStlyle"><div align="right" name="accountBalanceDebit" id="accountBalanceDebit" readOnly>&nbsp;</div></td>
		     <td class="tdStlyle"><div align="right" name="accountBalanceCredit" id="accountBalanceCredit" readOnly><%= request.getAttribute("accountBalance") %> </div></td>
		  </tr>
		  <tr>
		  <td colspan=3 class="tdStlyle" valign="center">&nbsp;&nbsp;&nbsp;Add: Cheques/DD issued but not presented in bank</td>
		  <td class="tdStlyle"><div align="right" name="addAmountDebit" id="addAmountDebit" readOnly>&nbsp;</div></td>
		  <td class="tdStlyle"><div align="right" name="addAmountCredit" id="addAmountCredit" readOnly><%= df.format((Double.parseDouble((String)request.getAttribute("unReconciledCr")))) %> </div></td>
		  </tr>
	<%--	  <%
		  	ArrayList al=(ArrayList)request.getAttribute("unreconciledCheques");
			if(al!=null && al.size()>0)
			{
				BrsDetails brs;
				for (Iterator itr = al.iterator();itr.hasNext();)
				{
					brs=(BrsDetails)itr.next();
					String txnType=brs.getTxnType(); 
					String type=brs.getType();
					if(txnType.trim().equalsIgnoreCase("Cr") && !type.trim().equalsIgnoreCase("P"))
					{
		%>				
					<tr>
					  <td class="tdStlyle" width="40%" valign="center"></td>
					  <td class="tdStlyle" width="10%"><%= brs.getChequeNumber() %></td>
					  <td class="tdStlyle" width="20%"><%= brs.getChequeDate() %></td>
					  <td class="tdStlyle" width="15%"><div align="right" readOnly><%= df.format((Double.parseDouble(brs.getChequeAmount()))) %> </div></td>
					  <td class="tdStlyle" width="15%"><div readOnly> &nbsp; </div></td>
		  			</tr>
		
		<%
					}
				}
			}
		  
		  %> --%>
		  <tr>
		  <td colspan=3 class="tdStlyle" valign="center">&nbsp;&nbsp;&nbsp;Add: Other instruments issued but not presented in bank</td>
		  <td class="tdStlyle"><div align="right" name="addAmountDebit" id="addAmountDebit" readOnly>&nbsp;</div></td>
		  <td class="tdStlyle"><div align="right" name="addAmountCredit" id="addAmountCredit" readOnly><%= df.format((Double.parseDouble((String)request.getAttribute("unReconciledCrOthers")))) %> </div></td>
		  </tr>
		  <tr>
		  <td colspan=3 class="tdStlyle" valign="center">&nbsp;&nbsp;&nbsp;Add: Credit given by Bank either for interest or for any other account<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; but not accounted for in Bank Book</td>
		  <td class="tdStlyle"><div align="right" name="addOthersAmountDebit" id="addOthersAmountDebit" readOnly>&nbsp;</div></td>
		  <td class="tdStlyle"><div align="right" name="addOthersAmountCredit" id="addOthersAmountCredit" readOnly><%= df.format((Double.parseDouble((String)request.getAttribute("unReconciledCrBrsEntries")))) %> </div></td>
		  </tr>
	<%--	  <%
			ArrayList al1=(ArrayList)request.getAttribute("unreconciledCheques");
			if(al1!=null && al1.size()>0)
			{
				BrsDetails brs;
				for (Iterator itr = al1.iterator();itr.hasNext();)
				{
					brs=(BrsDetails)itr.next();
					String txnType=brs.getTxnType(); 
					String type=brs.getType();
					if(txnType.trim().equalsIgnoreCase("Dr") && type.trim().equalsIgnoreCase("R"))
					{
		%>				
					<tr>
					  <td class="tdStlyle" width="40%" valign="center"></td>
					  <td class="tdStlyle" width="10%"><%= brs.getChequeNumber() %></td>
					  <td class="tdStlyle" width="20%"><%= brs.getChequeDate() %></td>
					  <td class="tdStlyle" width="15%"><div align="right" readOnly><%= df.format((Double.parseDouble(brs.getChequeAmount()))) %> </div></td>
					  <td class="tdStlyle" width="15%"><div readOnly> &nbsp; </div></td>
					</tr>

		<%
					}
				}
			}
		  		  
		  %>--%>
		  <tr >
		  <td colspan=3 class="thStlyle" align="middle" valign="center"><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sub-total</i></td>
		  <td class="tdStlyle"><div align="right" name="subTotalAmountDebit" id="subTotalAmountDebit" readOnly>&nbsp;</div></td>
		  <td class="tdStlyle"><div align="right" name="subTotalAmountCredit" id="subTotalAmountCredit" readOnly><%= df.format((Double.parseDouble((String)request.getAttribute("accountBalance"))) + (Double.parseDouble((String)request.getAttribute("unReconciledCr")))+(Double.parseDouble((String)request.getAttribute("unReconciledCrOthers"))))%></div></td>
		  </tr>
		  <tr class="normaltext">
			<td colspan=3 class="tdStlyle" valign="center">&nbsp;&nbsp;&nbsp; Less: Cheques Deposited but not cleared</td>
			<td class="tdStlyle"><div align="right" name="lessAmountDebit" id="lessAmountDebit" readOnly>&nbsp;</div></td>
			<td class="tdStlyle"><div align="right" name="lessAmountCredit" id="lessAmountCredit" readOnly><%= df.format((Double.parseDouble((String)request.getAttribute("unReconciledDr")))) %> </div></td>
		 </tr>
		  <%-- <%  
		 
			ArrayList al2=(ArrayList)request.getAttribute("unreconciledCheques");
			if(al2!=null && al2.size()>0)
			{
				BrsDetails brs;
				for (Iterator itr = al2.iterator();itr.hasNext();)
				{
					brs=(BrsDetails)itr.next();
					String txnType=brs.getTxnType(); 
					String type=brs.getType();
					if(txnType.trim().equalsIgnoreCase("Dr") && !type.trim().equalsIgnoreCase("R"))
					{
					
		%>				
				 	<tr>
					  <td class="tdStlyle" width="40%" valign="center"></td>
					  <td class="tdStlyle" width="10%"><%= brs.getChequeNumber() %></td>
					  <td class="tdStlyle" width="20%"><%= brs.getChequeDate() %></td>
					  <td class="tdStlyle" width="15%"><div align="right" readOnly><%= df.format((Double.parseDouble(brs.getChequeAmount()))) %> </div></td>
					  <td class="tdStlyle" width="15%"><div readOnly> &nbsp; </div></td>
					</tr>

		<%
					}
				}
			}
		 		  		  
		 %> --%>
		  <tr class="normaltext">
			<td colspan=3 class="tdStlyle" valign="center">&nbsp;&nbsp;&nbsp; Less: OtherInstruments Deposited but not cleared</td>
			<td class="tdStlyle"><div align="right" name="lessAmountDebit" id="lessAmountDebit" readOnly>&nbsp;</div></td>
			<td class="tdStlyle"><div align="right" name="lessAmountCredit" id="lessAmountCredit" readOnly><%= df.format((Double.parseDouble((String)request.getAttribute("unReconciledDrOthers")))) %> </div></td>
		 </tr>
		  <tr >
			 <td colspan=3 class="tdStlyle" valign="center">&nbsp;&nbsp;&nbsp; Less: Service Charges / Bank Charges or any other charge levied by<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; the Bank but not accounted for Bank book</td>
			<td class="tdStlyle"><div align="right" name="lessOthersAmountDebit" id="lessOthersAmountDebit" readOnly>&nbsp;</div></td>
		          <td class="tdStlyle"><div align="right" name="lessOthersAmountCredit" id="lessOthersAmountCredit" readOnly><%= df.format((Double.parseDouble((String)request.getAttribute("unReconciledDrBrsEntries")))) %> </div></td>
		   </tr>
	<%-- 	   <%
			ArrayList al3=(ArrayList)request.getAttribute("unreconciledCheques");
			if(al3!=null && al3.size()>0)
			{
				BrsDetails brs;
				for (Iterator itr = al3.iterator();itr.hasNext();)
				{
					brs=(BrsDetails)itr.next();
					String txnType=brs.getTxnType(); 
					String type=brs.getType();
					if(txnType.trim().equalsIgnoreCase("Cr") && type.trim().equalsIgnoreCase("P"))
					{
		%>				
					<tr>
					  <td class="tdStlyle" width="40%" valign="center"></td>
					  <td class="tdStlyle" width="10%"><%= brs.getChequeNumber() %></td>
					  <td class="tdStlyle" width="20%"><%= brs.getChequeDate() %></td>
					  <td class="tdStlyle" width="15%"><div align="right" readOnly><%= df.format((Double.parseDouble(brs.getChequeAmount()))) %> </div></td>
					  <td class="tdStlyle" width="15%"><div readOnly> &nbsp; </div></td>
					</tr>

		<%
					}
				}
			}
		   		 		  		  
		  %>--%>
		 
		 
		   <tr >
		 <td colspan=3 class="thStlyle" align="middle" valign="center"><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Net-total</i></td>
		 <td class="tdStlyle"><div align="right" name="totalAmountDebit" id="totalAmountDebit" readOnly>&nbsp;</div></td>
		 <td class="tdStlyle"><div align="right" name="totalAmountCredit" id="totalAmountCredit" readOnly><%= df.format((Double.parseDouble((String)request.getAttribute("accountBalance"))) + (Double.parseDouble((String)request.getAttribute("unReconciledCr")))+(Double.parseDouble((String)request.getAttribute("unReconciledCrOthers")))-((Double.parseDouble((String)request.getAttribute("unReconciledDr")))+(Double.parseDouble((String)request.getAttribute("unReconciledDrOthers")))))%></div></td>
		 </tr>
		<tr >
		   <td colspan=3 class="thStlyle" valign="center">&nbsp;&nbsp;&nbsp; Bank Balance as per Bank Statement</td>
			<td class="tdStlyle"><div align="right" name="bankBalanceDebit" id="bankBalanceDebit" readOnly>&nbsp;</div></td>
		 <td class="tdStlyle"><div align="right" name="bankBalanceCredit" id="bankBalanceCredit" readOnly><%= df.format((Double.parseDouble((String)request.getAttribute("balanceAsPerStatement")))) %></div></td>
		 </tr>
	</table>
	</td>
	</tr>
	</table>
	<br><br>
	<table align=center>
		<tr id="hideRow1" class="row1">
			<TD><html:button value="Refresh" styleClass="button" property="b1" onclick="subType('view')" /></td>
			<TD><html:button value="Cancel" styleClass="button" property="b1" onclick="onClickCancel()" /></td>
			<td><html:button value="Close" styleClass="button" property="b3" onclick="window.close();" /></td>
		</tr>
	</table>
		<br>
	<table align=center>
		<tr id="printrow" class="row2">
		<td>
		<html:button value="PRINT" styleClass="button" property="b1" onclick="printPage();" />
		</td>
		</tr>
	</table>
	<%
	 }
	%>
</html:form>
</body>
</html>
