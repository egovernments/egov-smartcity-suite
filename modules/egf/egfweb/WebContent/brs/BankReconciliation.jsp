<%@ page language="java"  %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page import="java.util.*,
		org.apache.log4j.Logger,
		java.text.DecimalFormat,
		com.exilant.eGov.src.transactions.brs.*"
%>
<html>
<head>
	
	<title>Bank Reconciliation Statement</title>
	
	<script>
	function openDetails(val)
	{
	var mode="view";
 	window.open("/EGF/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
 	}
	
	function getAccountNumbers()
	{
		document.forms("bankReconciliationForm").action = "../brs/BankReconciliation.do?submitType=getAccountNumbers";
		if(document.bankReconciliationForm.bankId.options[document.bankReconciliationForm.bankId.selectedIndex].value != 0)
		{
			
			document.forms("bankReconciliationForm").submit();
		}
	
	}
	function calAmount(obj)
	{
		
		var rowobj=getRow(obj);
		var recAmount=0;
		var payAmount=0;
		var valuePresent=false;
		var table= document.getElementById("gridBankReconciliation");
		var val=getControlInBranch(table.rows[rowobj.rowIndex],'bankStatementChqDate').value;
		if(val == "")
		{
			if(getControlInBranch(table.rows[rowobj.rowIndex],'isCalculated').value==1)
			{
				var type=getControlInBranch(table.rows[rowobj.rowIndex],'type').innerHTML;
				if(type =="Receipt")
				{
					recAmount=parseFloat(getControlInBranch(table.rows[rowobj.rowIndex],'bankBookChequeAmount').innerHTML);
				}
				if(type =="Payment")
				{
					payAmount=parseFloat(getControlInBranch(table.rows[rowobj.rowIndex],'bankBookChequeAmount').innerHTML);
				}
			}
			document.getElementById('receipts').innerHTML=parseFloat(document.getElementById('receipts').innerHTML)+recAmount;
			document.getElementById('payments').innerHTML=parseFloat(document.getElementById('payments').innerHTML)+payAmount;
			document.getElementById('netAmount').innerHTML=parseFloat(document.getElementById('bankBookBal').innerHTML) - parseFloat(document.getElementById('receipts').innerHTML) + parseFloat(document.getElementById('payments').innerHTML);
			
			var p=0;
			var q=0;
			var r=0;
						
			p=parseFloat(document.getElementById('receipts').innerHTML);
			q=parseFloat(document.getElementById('payments').innerHTML);
			r=parseFloat(document.getElementById('netAmount').innerHTML);

			document.getElementById('receipts').innerHTML=p.toFixed(2);
			document.getElementById('payments').innerHTML=q.toFixed(2);
			document.getElementById('netAmount').innerHTML=r.toFixed(2);
			
			getControlInBranch(table.rows[rowobj.rowIndex],'isCalculated').value=0;
		}
		if(val != null && val != "" && val.length>0)
		{
			var dat=validateDate(val);
			if (dat) 
			{
				if(getControlInBranch(table.rows[rowobj.rowIndex],'isCalculated').value==0)
				{
					var type=getControlInBranch(table.rows[rowobj.rowIndex],'type').innerHTML;
					if(type =="Receipt")
					{
						recAmount=parseFloat(getControlInBranch(table.rows[rowobj.rowIndex],'bankBookChequeAmount').innerHTML);
					}
					if(type =="Payment")
					{
						payAmount=parseFloat(getControlInBranch(table.rows[rowobj.rowIndex],'bankBookChequeAmount').innerHTML);
					}
					getControlInBranch(table.rows[rowobj.rowIndex],'isCalculated').value=1;
					valuePresent=true;
				}
			}
		}
		if(valuePresent)
		{
			
			document.getElementById('receipts').innerHTML=parseFloat(document.getElementById('receipts').innerHTML)-recAmount;
			document.getElementById('payments').innerHTML=parseFloat(document.getElementById('payments').innerHTML)-payAmount;
			document.getElementById('netAmount').innerHTML=parseFloat(document.getElementById('bankBookBal').innerHTML) - parseFloat(document.getElementById('receipts').innerHTML) + parseFloat(document.getElementById('payments').innerHTML);
			
			var a=0;
			var b=0;
			var c=0;
			
			a=parseFloat(document.getElementById('receipts').innerHTML);
			b=parseFloat(document.getElementById('payments').innerHTML);
			c=parseFloat(document.getElementById('netAmount').innerHTML);
			
			document.getElementById('receipts').innerHTML=a.toFixed(2);
			document.getElementById('payments').innerHTML=b.toFixed(2);
			document.getElementById('netAmount').innerHTML=c.toFixed(2);
		}
	}
	function ButtonPress(arg)
	{
		
		if(arg == "view")
		{
			if(document.bankReconciliationForm.recToDate.value == "")
			{
				alert("Enter Reconciliation Date");
				return;
			}
			else
			{
				var dat=validateDate(document.bankReconciliationForm.recToDate.value);
				if (!dat) 
				{
					alert('Invalid date format : Enter Date as dd/mm/yyyy');
					document.bankReconciliationForm.recToDate.focus();
					return;
				}
			}
			if(document.bankReconciliationForm.bankStatementDate.value == "")
			{
				alert("Enter Bank Statement Date");
				return;
			}
			else
			{
				var dat=validateDate(document.bankReconciliationForm.bankStatementDate.value);
				if (!dat) 
				{
					alert('Invalid date format : Enter Date as dd/mm/yyyy');
					document.bankReconciliationForm.bankStatementDate.focus();
					return;
				}
			}
			if(!compareDt())
			{
				return;
			}
			if(document.bankReconciliationForm.bankId.options[document.bankReconciliationForm.bankId.selectedIndex].value == 0)
			{
				alert("Select Bank");
				return;
			}
			if(document.bankReconciliationForm.accId.options[document.bankReconciliationForm.accId.selectedIndex].value == 0)
			{
				alert("Select Account Number");
				return;
			}
			/*
			if(!isNaN(parseFloat(document.bankReconciliationForm.balAsPerStatement.value)))
			{
				
				if(parseFloat(document.bankReconciliationForm.balAsPerStatement.value) < 0)
				{
				alert("Enter Bank Statement Balance");
				return;
				}
				
			}
			else
			{
				alert("Enter Numeric value for  Bank Statement Balance");
				return;
			}
			*/
			
			if(document.bankReconciliationForm.balAsPerStatement.value == "")
			{
				alert("Enter Bank Statement Balance");
				return;
			}
			if(isNaN(parseFloat(document.bankReconciliationForm.balAsPerStatement.value)))
			{
				alert("Enter Numeric value for  Bank Statement Balance");
				return;
			}
			
			
			document.forms("bankReconciliationForm").action = "../brs/BankReconciliation.do?submitType=showBrsDetails";
			document.forms("bankReconciliationForm").submit();

		}
		if(arg == "savenew")
		{
			
			if(document.bankReconciliationForm.recToDate.value == "")
			{
				alert("Enter Reconciliation Date");
				return;
			}
			else
			{
				var dat=validateDate(document.bankReconciliationForm.recToDate.value);
				if (!dat) 
				{
					alert('Invalid date format : Enter Date as dd/mm/yyyy');
					document.bankReconciliationForm.recToDate.focus();
					return;
				}
			}
			if(document.bankReconciliationForm.bankStatementDate.value == "")
			{
				alert("Enter Bank Statement Date");
				return;
			}
			else
			{
				var dat=validateDate(document.bankReconciliationForm.bankStatementDate.value);
				if (!dat) 
				{
					alert('Invalid date format : Enter Date as dd/mm/yyyy');
					document.bankReconciliationForm.bankStatementDate.focus();
					return;
				}
			}
			
			if(!compareDt())
			{
				return;
			}
			var table= document.getElementById("gridBankReconciliation");
			var valuePresent=false;
			for(var i=1;i<table.rows.length;i++)
			{
				var val=getControlInBranch(table.rows[i],'bankStatementChqDate').value;
				if(val != null && val != "" && val.length>0)
				{
					var dat=validateDate(val);
					if (!dat) 
					{
						alert('Invalid date format : Enter Date as dd/mm/yyyy');
						getControlInBranch(table.rows[i],'bankStatementChqDate').focus();
						return;
					}
					else
					{
						if(document.bankReconciliationForm.bankId.options[document.bankReconciliationForm.bankId.selectedIndex].value == 0)
						{
							alert("Select Bank");
							return;
						}
						if(document.bankReconciliationForm.accId.options[document.bankReconciliationForm.accId.selectedIndex].value == 0)
						{
							alert("Select Account Number");
							return;
						}	
						if(compareDate(formatDate6(formatDateToDDMMYYYY4(getControlInBranch(table.rows[i],'bankBookChequeDate').innerHTML)),formatDate6(document.bankReconciliationForm.bankStatementDate.value)) == -1 )
						{
							alert('Post Dated Cheque Cannot Be Reconcilied');
							getControlInBranch(table.rows[i],'bankStatementChqDate').focus();
							return;
						
						}
						if(compareDate(formatDate6(formatDateToDDMMYYYY4(getControlInBranch(table.rows[i],'bankBookChequeDate').innerHTML)),formatDate6(document.bankReconciliationForm.recToDate.value)) == -1 )
						{
							alert('Reconciliation Date cannot be less than Cheque Date');
							getControlInBranch(table.rows[i],'bankStatementChqDate').focus();
							return;

						}
						if(compareDate(formatDate6(formatDateToDDMMYYYY4(getControlInBranch(table.rows[i],'bankBookChequeDate').innerHTML)),formatDate6(formatDateToDDMMYYYY4(getControlInBranch(table.rows[i],'bankStatementChqDate').value))) == -1 )
						{
							alert('Reconciliation Date cannot be less than Cheque Date');
							getControlInBranch(table.rows[i],'bankStatementChqDate').focus();
							return;

						}
						if(compareDate(formatDate6(formatDateToDDMMYYYY4(getControlInBranch(table.rows[i],'bankStatementChqDate').value)),formatDate6(document.bankReconciliationForm.bankStatementDate.value)) == -1 )
						{
							alert('Bank Statement Cheque Date cannot be greater than Bank Statement Date');
							getControlInBranch(table.rows[i],'bankStatementChqDate').focus();
							return;
															
						}
						valuePresent=true;
					}
				}
			}
			if(!valuePresent)
			{
				alert("No Data To Reconcile");
				return;
			}
			document.forms("bankReconciliationForm").action = "../brs/BankReconciliation.do?submitType=reconcile";
			document.forms("bankReconciliationForm").submit();
		}
		if(arg == "close")
		{
			window.close();
		}
			
	}
	function onClickCancel()
	{
		window.location="../brs/BankReconciliation.jsp";	
	}
	function getData()
	{
	
		var target="<%=(request.getAttribute("alertMessage"))%>";
		if(target!="null")
		{
			alert("<%=request.getAttribute("alertMessage")%>");
			<%	
			if(request.getAttribute("alertMessage") != null)
			{
				session.removeAttribute("accNumberList1");
				request.setAttribute("alertMessage",null);
			}
			%> 
			document.bankReconciliationForm.bankId.options.selectedIndex=0;
			document.bankReconciliationForm.accId.options.selectedIndex=0;
			document.bankReconciliationForm.balAsPerStatement.value="";
			document.bankReconciliationForm.bankStatementDate.value="";
			document.bankReconciliationForm.recToDate.value="";
		}
		<%
		ArrayList brsList=(ArrayList)request.getAttribute("brsDetails");
		if(brsList!=null && brsList.size()==0)
		{
			session.removeAttribute("accNumberList1");
		%>
			alert("No Data");
			document.bankReconciliationForm.bankId.options.selectedIndex=0;
			document.bankReconciliationForm.accId.options.selectedIndex=0;
			document.bankReconciliationForm.balAsPerStatement.value="";
			document.bankReconciliationForm.bankStatementDate.value="";
			document.bankReconciliationForm.recToDate.value="";
		<%
		 }
		 if(brsList!=null && brsList.size()>0)
		 {
		 %>
			document.getElementById('searchBrs').style.display ='none';
			var table=document.getElementById("gridBankReconciliation");
			getControlInBranch(table.rows[1],'bankStatementChqDate').focus();
						
			
		<%
		}
		else
		{
		%>
		document.bankReconciliationForm.bankId.focus();
		<%
		}
		%>
	}
	function compareDt()
	{
		if(compareDate(formatDate6(document.bankReconciliationForm.bankStatementDate.value),formatDate6(document.bankReconciliationForm.recToDate.value)) == -1 )
		{
			alert('Reconciliation Date cannot be less than Bank Statement Date');
			document.bankReconciliationForm.recToDate.focus();
			return false;
						
		}
		return true;
	
	}
	
	function getDetails(obj){
	
		var row=getRow(obj);
		var table=document.getElementById('gridBankReconciliation');
		var cgn1=getControlInBranch(table.rows[row.rowIndex],"cgnum");
		cgn1=cgn1.innerHTML;
		var cgn2=cgn1.substring(0,3);
	//alert(cgn1+"	"+cgn2);
	switch(cgn2){
		/*
		case 'CTB':
		window.open("../HTML/JV_Contra_CToB.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		//window.open("../HTML/VMC/JV_Contra_CToB_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		*/
		case 'JVG':
		window.open("../HTML/JV_General.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'SJV' :
		window.open("../HTML/SupplierJournal.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'CJV' :
		window.open("../HTML/ContractorJournal.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'SAL' :
		window.open("../HTML/JV_Salary.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'PYS':
		window.open("../HTML/PayInSlip.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'BTB':
		window.open("../HTML/JV_Contra_BToB.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'BTC':
		window.open("../HTML/JV_Contra_BToC.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'CTB':
		window.open("../HTML/JV_Contra_CToB.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'FTF':
		window.open("../HTML/JV_Contra_FToF.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'DBP':
		window.open("../HTML/DirectBankPayment.htm?cgNumber="+cgn1+"&showMode=viewBank","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'DCP':
		window.open("../HTML/DirectCashPayment.htm?cgNumber="+cgn1+"&showMode=viewCash","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'SPH':
		window.open("../HTML/SubLedgerPayment.htm?drillDownCgn="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'SSP':
		window.open("../HTML/SubledgerSalaryPayment.htm?drillDownCgn="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'ASP' :
		window.open("../HTML/AdvanceJournal.htm?drillDownCgn="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		
		/*** Reciept ***/
		case 'PRO':
		window.open("../HTML/PT_Office.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'PRF':
		window.open("../HTML/PT_Field.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'PRB':
		window.open("../HTML/PT_Bank.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'OTO':
		window.open("../HTML/OT_Office.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'OTF':
		window.open("../HTML/OT_Field.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'MSR':
		window.open("../HTML/MiscReceipt.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'OJV':
		window.open("../HTML/VMC/ContingencyJournal_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		/*** Reciept ***/
		
		} //switch
	}
</script>
	
</head>
<body bgcolor="#ffffff" onload="getData()" onKeyDown ="CloseWindow(window.self); ">
	<% 	Logger logger = Logger.getLogger(getClass().getName()); 
		logger.info(">>       INSIDE JSP   >>");
	%>
<br>

<html:form action="/brs/BankReconciliation.do">
<table align="center" class="tableStyle">

<tr><td colspan=4>&nbsp;</td></tr>
<tr>
<td class="labelcell" align="right" width="25%">Bank And Branch <SPAN class="leadon">*</SPAN></td>
<td class="smallfieldcell" align="left" width="25%">
<html:select  property="bankId" onchange="getAccountNumbers()" styleClass="bigcombowidth">
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
<td class="smallfieldcell" align="left" width="25%">
<html:select  property="accId" styleClass="combowidth">
<html:option value='0'>----choose----</html:option>
<%
	HashMap accountId =(HashMap)session.getAttribute("accNumberList1");
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
<tr >
<td class="labelcell" align="right">Bank&nbsp;Statement&nbsp;Balance<SPAN class="leadon">*</SPAN></td>
<td class="fieldcell" align="left"><html:text  style=";text-align:right;padding-right:2px;" property="balAsPerStatement"  /> </td>
<td class="labelcell" align="right">Bank&nbsp;Statement&nbsp;date<SPAN class="leadon">*</SPAN></td>
<td class="smallfieldcell" align="left"><html:text  property="bankStatementDate" onkeyup="DateFormat(this,this.value,event,false,'3')" />
<a href="javascript:show_calendar('bankReconciliationForm.bankStatementDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="../images/calendar.gif"  border=0></a>
</td>
</tr>
<tr >
<td class="labelcell" align="right">Reconciliation&nbsp;From&nbsp;Date</td>
<td class="smallfieldcell" align="left"><html:text  property="recFromDate"  onkeyup="DateFormat(this,this.value,event,false,'3')"/>
	<a href="javascript:show_calendar('bankReconciliationForm.recFromDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="../images/calendar.gif" width=24 height=22 border=0></a>
</td>
<td class="labelcell" align="right">Reconciliation&nbsp;To&nbsp;Date<SPAN class="leadon">*</SPAN></td>
<td class="smallfieldcell" align="left"><html:text  property="recToDate"  onkeyup="DateFormat(this,this.value,event,false,'3')"/>
<a href="javascript:show_calendar('bankReconciliationForm.recToDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="../images/calendar.gif" width=24 height=22 border=0></a>
</td>
</tr>
<tr>
<td colspan=4>
<div id="searchBrs" name="searchBrs">
<table align=center >
<tr>
<td align="middle" ><html:button value="Reconcile" property="b1" onclick="ButtonPress('view')" styleClass="button"/></td>
<td><html:button styleClass="button" value="Cancel" property="b1" onclick="onClickCancel()" /></td>
<td><html:button styleClass="button" value="Close" property="b3" onclick="ButtonPress('close')" /></td>
</tr>
</table>
</div>
</td>
</tr>
<%
	ArrayList al=(ArrayList)request.getAttribute("brsDetails");
	if(al!=null && al.size()>0)
	{
	BrsDetails brs;
	int i=1;
	%>
<tr>
<td colspan=4>&nbsp;
</td>
</tr>
<tr>
<td colspan=4>
<table  cellpadding="0" cellspacing="0" align="center" id="gridBankReconciliation" name="gridBankReconciliation" >
<tr >
<td class="thStlyle" width="6%"><div align="center">Sr&nbsp;No</div></td>
<td class="thStlyle" width="15%"><div align="center">Voucher Number</div></td>
<td class="thStlyle" width="15%" style="display:none"><div align="center">CGNumber</div></td>
<!--<td class="thStlyle" width="12%"><div align="center">&nbsp;Voucher&nbsp; Date</div></td>
--><td class="thStlyle" width="12%"><div align="center">&nbsp;&nbsp;&nbsp;&nbsp;Type&nbsp;&nbsp;&nbsp;&nbsp;</div></td>
<td class="thStlyle" width="15%"><div align="center">Cheque Number</div></td>
<td class="thStlyle" colspan=2 width="24%">
	<table cellpadding="0" cellspacing="0" border="0">
	<tr ><td class="thStlyle" colspan=2 ><div align="center">From Bank Book</div></td></tr>
	<tr >
	<td class="thStlyle"><div align="center">Cheque Date</div></td>
	<td class="thStlyle"><div align="center">Cheque Amount</div></td>
	</tr>
	</table>
</td>
<td class="thStlyle" width="12%">
	<table cellpadding="0" cellspacing="0" border="0">
	<tr ><td class="thStlyle"><div align="center">From Bank Statement</div></td></tr>
	<tr ><td class="thStlyle"><div align="center">Cheque Date</div></td></tr>
	</table>
</td>
</tr>
<%
	DecimalFormat df = new DecimalFormat("0.00");
	for (Iterator itr = al.iterator();itr.hasNext();)
	{
		
		brs=(BrsDetails)itr.next();
		//logger.info( "brs.getRecordId() "+brs.getRecordId());
		//logger.info( "brs.getRecordId() "+brs.getRecordId());
		
	%>
		<tr>
		<td class="tdStlyle" width="6%">
			<div align="left" id=bankReconciliation_srNo name="bankReconciliation_srNo"><%= i %></div>
			<html:hidden  property="recID" value="<%= new Integer(brs.getRecordId()).toString() %>" />
			<html:hidden  property="ihId" value="<%= brs.getInstrumentHeaderId() %>" />
			
		</td>
		<td class="tdStlyle" width="27%"><A  onclick="getDetails(this);" href="#"><font size="1px" color="#3670A7">
		<div align="left" name="voucherNumber" id="voucherNumber">
		<%	for(int j=0;j<brs.getVoucherNumbers().size();j++){ %>
		<A href="#" onclick='openDetails(<%= brs.getVoucherHeaderIds().get(j)%>);' >
		<%= brs.getVoucherNumbers().get(j)%></A>,	<%} %>
		</div> </td>
		<!-- <td class="tdStlyle" width="12%"><div align="left" name="voucherDate" id="voucherDate"><%= brs.getVoucherDate() %></div> </td>-->
		<td class="tdStlyle" width="12%"><div align="left" name="type" id="type"><%= brs.getType() %></div> </td>
		<td class="tdStlyle" width="15%"><div align="left" name="chequeNumber"><%= brs.getChequeNumber() %></div></td>
		<td class="tdStlyle" width="12%"><div align="left" name="bankBookChequeDate"><%= brs.getChequeDate() %></div></td>
		<td class="tdStlyle" width="12%"><div align="right" name="bankBookChequeAmount" id="bankBookChequeAmount"><%= df.format((Double.parseDouble(brs.getChequeAmount()))) %></div></td>
		<td class="fieldcellwithinput" width="12%"><html:text  property="bankStatementChqDate" value="" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="calAmount(this)"/>
		<input type=hidden name="isCalculated" id="name="isCalculated" value="0"></td>
		</tr>
	<%
		i=i+1;
	}
	%>
</table>

<table width="48%" align="right"  cellpadding="0" cellspacing="0">
<tr>
	<td class="tablesubcaption" colspan=3 align=center >SUMMARY</td>
</tr>
<tr>

	<td colspan=2 class="tdStlyle">Balance as per Bank Book</td>
	<td class="tdStlyle"><div align="right" id="bankBookBal" name="bankBookBal"><%= request.getAttribute("accountBalance") %> </div></td>
</tr>
<tr>

	<td colspan=3 class="tdStlyle">Amounts not reflected in Bank</td>

</tr>
<tr>

	<td colspan=2 class="tdStlyle">Receipts</td>
	<td class="tdStlyle"><div  align="right" id="receipts" name="receipts"><%= df.format((Double.parseDouble((String)request.getAttribute("unReconciledDr")))) %> </div></td>
</tr>
<tr>

	<td colspan=2 class="tdStlyle">Payments</td>
	<td class="tdStlyle"><div  align="right" id="payments" name="payments"><%= df.format((Double.parseDouble((String)request.getAttribute("unReconciledCr")))) %> </div></td>
</tr>
<tr>

	<td colspan=2 class="tdStlyle">Net Amount</td>
	<td class="tdStlyle"><div align="right" id="netAmount" name="netAmount"><%= df.format((Double.parseDouble((String)request.getAttribute("accountBalance")))-(Double.parseDouble((String)request.getAttribute("unReconciledDr")))+(Double.parseDouble((String)request.getAttribute("unReconciledCr")))) %> </div></td>
</tr>
<tr>

	<td colspan=2 class="tdStlyle">Balance as per Bank</td>
	<td class="tdStlyle"><div align="right" id="bankBal" name="bankBal"><%= df.format((Double.parseDouble((String)request.getAttribute("balanceAsPerStatement")))) %></div></td>
</tr>
</table>
</td>
</tr>
</table>
<br><br>
<table align=center>
<tr>
	<td><html:button styleClass="button" value="Save" property="b2" onclick="ButtonPress('savenew')" /></td>
	<TD><html:button styleClass="button" value="Refresh" property="b1" onclick="ButtonPress('view')" /></td>
	<TD><html:button styleClass="button" value="Cancel" property="b1" onclick="onClickCancel()" /></td>
	<td><html:button styleClass="button" value="Close" property="b3" onclick="ButtonPress('close')" /></td>
<tr>
</table>

<%
}
%>
</html:form>

</body>
</html>
