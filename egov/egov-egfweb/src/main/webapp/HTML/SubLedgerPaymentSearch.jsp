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
<%@page  import="com.exilant.eGov.src.transactions.*,java.util.LinkedList, java.util.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../css/egov.css" type="text/css">
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../script/PaymentSearchFunc.js.htm"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../resources/javascript/jsCommonMethods.js"></Script>
  <SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<SCRIPT LANGUAGE="javascript">
var cWind;         // temporary window reference
var count=1;      // Counter which gives number of child windows opened
var cWindows=new Object();    //HashMap where we store child window
var mode="";
var paymentType="";
function ButtonPress()
{
		if(PageValidator.validateForm())
		{
			PageManager.DataService.removeQueryField('payType');
			PageManager.DataService.removeQueryField('voucherNumber');
			PageManager.DataService.removeQueryField('fundID');
			PageManager.DataService.removeQueryField('voucherDateFrom');
			PageManager.DataService.removeQueryField('voucherDateTo');
			//hideColumn(1);
			var type=null;
			var typeObj=document.getElementById('paymentType');
			if(typeObj.selectedIndex==-1)return;
			type=typeObj.options[typeObj.selectedIndex].value;
			paymentType=type;

				var voucherNumber=document.getElementById('voucherNumber').value;
				if(voucherNumber && voucherNumber.length!=0)
					PageManager.DataService.setQueryField('voucherNumber',voucherNumber);

				var voucherDateFrom=document.getElementById('voucherDateFrom').value;
				if(voucherDateFrom && voucherDateFrom.length!=0)
					PageManager.DataService.setQueryField('voucherDateFrom',formatDate1(voucherDateFrom));

				var voucherDateTo=document.getElementById('voucherDateTo').value;
				if(voucherDateTo && voucherDateTo.length!=0)
				PageManager.DataService.setQueryField('voucherDateTo',formatDate1(voucherDateTo));

				var fundID=document.getElementById('fund').value;
				if(fundID&& fundID.length!=0)
					PageManager.DataService.setQueryField('fundID',fundID);


				if( compareDate(formatDate6(voucherDateFrom),formatDate6(voucherDateTo)) == -1 )
				{
					bootbox.alert('Start Date cannot be greater than End Date');
					document.getElementById('voucherDateFrom').focus();
					return false;
				}

				//initial values to bean variables
			var temp=document.getElementById('voucherNumber').value;
			document.getElementById('inputFieldC').value=temp; //v.vouchernumber like '%'
			document.getElementById('inputField4').value=document.getElementById('fund').value;//v.fundid=''
			document.getElementById('inputField1').value=document.getElementById('voucherDateFrom').value; //v.voucherdate>=
			document.getElementById('inputField2').value=document.getElementById('voucherDateTo').value; //v.voucherdate<=
			document.getElementById('inputField5').value='';// v.status=''
			document.getElementById('inputField6').value='';//v.isConfirmed
			document.getElementById('inputField7').value='';//cbd.paidamount
			document.getElementById('inputField8').value=''; //v.status!=4
			document.getElementById('inputField9').value=''; //v.name =''
			document.getElementById('inputField10').value=''; //p.type ='' or m.type=''
			document.getElementById('inputField11').value=''; // m.type=''
			document.getElementById('displayCondition').value=1;

			if(type && (type=="Bank Payment"))
			{
				if(mode=="modify")
				{
				var stat=0;
				var con=1;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				if(mode=="edit")
				{
				var stat=0;
				var con=0;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				document.getElementById('inputField10').value='Direct Bank Payment';// p.type ='' or m.type='' PageManager.DataService.setQueryField('payType','Direct Bank Payment');
				document.journalVoucher.submit(); return;//PageManager.DataService.callDataService('bankPaymentSearch');

			}
			if(type && (type=="Bank Entry"))
			{
				if(mode=="modify")
				{
				var stat=0;
				var con=1;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				if(mode=="edit")
				{
				var stat=0;
				var con=0;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				document.getElementById('inputField10').value='Direct Bank Payment'; //p.type ='' or m.type='' PageManager.DataService.setQueryField('payType','Direct Bank Payment');
				document.journalVoucher.submit(); return;//PageManager.DataService.callDataService('bankEntrySearch');

			}
			if(type && (type=="Cash Payment"))
			{
				if(mode=="modify")
				{
				var stat=0;
				var con=1;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				if(mode=="edit")
				{
				var stat=0;
				var con=0;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				document.getElementById('inputField10').value='Direct Cash Payment'; //p.type ='' or m.type PageManager.DataService.setQueryField('payType','Direct Cash Payment');
				document.journalVoucher.submit(); return;//PageManager.DataService.callDataService('cashPaymentSearch');

			}
			if(type && (type=="Advance"))
			{
				if(mode=="modify")
				{
				var stat=0;
				var con=1;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				if(mode=="edit")
				{
				var stat=0;
				var con=0;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				document.getElementById('inputField10').value='Advance'; //p.type ='' or m.type PageManager.DataService.setQueryField('payType','Advance');
				document.journalVoucher.submit();  return;//PageManager.DataService.callDataService('advancePaymentSearch');

			}
			if(type && (type=="Subledger Payment"))
			{
				if(mode=="modify")
				{
				var stat=0;
				var con=1;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				if(mode=="edit")
				{
				var stat=0;
				var con=0;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				document.getElementById('inputField10').value='Supplier'; //p.type ='' or m.type PageManager.DataService.setQueryField('payType','Supplier');
				document.getElementById('inputField11').value='Contractor'; // m.type='' PageManager.DataService.setQueryField('payType1','Contractor');
				document.journalVoucher.submit(); return; //PageManager.DataService.callDataService('subledgerPaymentSearch');
			}

			if(type && (type=="Salary"))
			{
				if(mode=="modify")
				{
				var stat=0;
				var con=1;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				if(mode=="edit")
				{
				var stat=0;
				var con=0;
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('reverseNum',stat);
				document.getElementById('inputField6').value=con;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',con);
				}
				document.getElementById('inputField10').value='Salary'; //p.type =''  PageManager.DataService.setQueryField('payType','Salary');
				document.journalVoucher.submit();  return;//PageManager.DataService.callDataService('salaryPaymentSearch');

			}
			var table=document.getElementById('paymentSearchGrid');
			table.style.display="block";
		 }
}
function ModeChangeGrid()
{
	//showAllColumns();
	if(paymentType=="Bank Payment")
	{
		hideColumn(8);
		document.getElementById('cheNo').innerHTML="ChequeNo";
	}
	if(paymentType=="Bank Entry")
	{
		hideColumn(8);
		document.getElementById('cheNo').innerHTML="Ref No";
	}
	if(paymentType=="Cash Payment")
	{
		hideColumn(7);
		hideColumn(8);
		document.getElementById('cheNo').innerHTML="ChequeNo";
	}
	if(paymentType=="Advance")
	{
		hideColumn(4);
		hideColumn(5);
		hideColumn(6);
		document.getElementById('cheNo').innerHTML="ChequeNo";
	}
	if(paymentType=="Subledger Payment")
	{
		hideColumn(4);
		hideColumn(5);
		hideColumn(6);
		document.getElementById('cheNo').innerHTML="ChequeNo";
	}
	if(paymentType=="Salary")
	{
		hideColumn(4);
		hideColumn(5);
		hideColumn(6);
		document.getElementById('cheNo').innerHTML="ChequeNo";
	}

}
function onloadTasks(){
	PageValidator.addCalendars();
	mode = PageManager.DataService.getQueryField('showMode');
	PageManager.ListService.callListService();
	PageManager.DataService.callDataService('finYearDate');
}
function ModeChange(){
		//showAllColumns();
		var table=document.getElementById('paymentSearchGrid');
		var controlArray=new Array('slNo','cgNumber','voucherDate','voucherNumber','paidTo','passedAmount','billAmount','chequeNumber','paidAmount','status');

		var len=table.rows.length;
 		for (var j=2;j<len;j=j+1)
 		{
   			table.deleteRow(1);

   		}
   		if(len<2) return;
   		for (var j=0;j<controlArray.length;j++)
		{
			var row=PageManager.DataService.getControlInBranch(table.rows[1],controlArray[j]);
			row.innerHTML="&nbsp;";
			var trObj=PageManager.DataService.getRow(row);
				trObj.className='normaltext';
		}
}

function beforeRefreshPage(dc){
		//setting  date from field to financialyear start date
			var dispCond = document.getElementById("displayCondition").value ;
			if(dispCond==0)
				{
				if(dc.values['serviceID']=='finYearDate')
					{
					var dt=dc.values['startFinDate']
					dt=formatDate2(dt);
					document.getElementById('voucherDateFrom').value=dt;
					}
				}

		var tabObj=dc.grids['paymentSearchGrid'];
		if(!tabObj)	return false;
		if(tabObj.length<2){
			bootbox.alert("No Data");

		}
		for(var i=1;i<tabObj.length;i++){

				tabObj[i][2]=formatDate(tabObj[i][2]);

			}

	return true;
}

//added by rashmi
function changeColor(){
		var table=document.getElementById('paymentSearchGrid');
		for(var i=1;i<table.rows.length;i++){
			var statusObj=PageManager.DataService.getControlInBranch(table.rows[i],"status");
 			var status=statusObj.innerHTML;
			if(status=='Reversed')
			{
				var trObj=PageManager.DataService.getRow(statusObj);
				trObj.className='rowRev';
			}
			else if(status=='Reversal')
			{
				var trObj=PageManager.DataService.getRow(statusObj);
				trObj.className='rowRev2';
			}
			else
			{
				var trObj=PageManager.DataService.getRow(statusObj);
				trObj.className='normaltext';
			}

  	 	}
}
function afterRefreshPage(dc){
	addSlNo();
	changeColor();
	var voucherDateFrom=document.getElementById('voucherDateFrom').value;
		var voucherDateTo=document.getElementById('voucherDateTo').value;
		if(voucherDateFrom && voucherDateFrom.length!=0)
		{
				var VouDateFrom=dc.values['voucherDateFrom'];
				VouDateFrom=formatDateToDDMMYYYY5(VouDateFrom);
				document.getElementById('voucherDateFrom').value=VouDateFrom;
		}
		if(voucherDateTo && voucherDateTo.length!=0)
		{
			var VouDateTo=dc.values['voucherDateTo'];
			VouDateTo=formatDateToDDMMYYYY5(VouDateTo);
			document.getElementById('voucherDateTo').value=VouDateTo;
	}
	ModeChangeGrid();


}
function addSlNo(){
	var slNo=1;
	var tabObj=document.getElementById("paymentSearchGrid");
	for(var i=1;i<tabObj.rows.length;i++){
	var obj=PageManager.DataService.getControlInBranch(tabObj.rows[i],"slNo");
	obj.innerHTML=slNo;
	slNo++;
	}
}
function getDetails(obj,cgn,vStatus,v2,v3){
	var row=PageManager.DataService.getRow(obj);
	var table=document.getElementById('paymentSearchGrid');
	//var cgn1=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"colG");
	cgn1=cgn;
	var cgn2=cgn1.substring(0,3);
	var vStatus=vStatus;
	var vStatus2=v2;
	var vStatus3=v3;
	var vStatus1=vStatus.substring(0,1);
	//bootbox.alert("cgn1-------"+cgn1);
	var cgn2=cgn1.substring(0,3);
	var typeObj=document.getElementById('paymentType');
	type=typeObj.options[typeObj.selectedIndex].value;

	mode = PageManager.DataService.getQueryField('showMode');
	switch(cgn2){
	case 'SPH':
		//window.location="SubLedgerPayment.htm?drillDownCgn="+cgn1+"&showMode="+mode;
		if(vStatus3=='Reversal')
				{ 			 		
			 		cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{					
					cWind=window.open("SubLedgerPayment.htm?drillDownCgn="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		     	}

		cWindows[count++]=cWind;
		break;	
		
	case 'SSP' :
		//window.location="SubledgerSalaryPayment.htm?drillDownCgn="+cgn1+"&showMode="+mode;
		if(vStatus3=='Reversal')
				{ 		 		
					cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{			
					cWind=window.open("SubledgerSalaryPayment.htm?drillDownCgn="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		     	}

		cWindows[count++]=cWind;
		break;
	case 'ASP' :
		//window.location="AdvanceJournal.htm?drillDownCgn="+cgn1+"&showMode="+mode;
		if(vStatus3=='Reversal')
				{ 			 		
			 		cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{					
					cWind=window.open("AdvanceJournal.htm?drillDownCgn="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		     	}

		cWindows[count++]=cWind;
		break;
	
   
		
	case 'DBP':
		if(mode=="view")
		{
				if(vStatus=='Reversal')
				{ 			
			 		cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{					
					mode="viewBank";
					cWind=window.open("VMC/DirectBankPayment_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		     	}
																  	 	
                         cWindows[count++]=cWind;
	 					 break;
		}

		if(mode=="modify")
		mode="reverseBank";
		//window.location="DirectBankPayment.htm?cgNumber="+cgn1+"&showMode="+mode;
		//cWind=window.open("DirectBankPayment.htm?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		cWind=window.open("VMC/DirectBankPayment_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		cWindows[count++]=cWind;
		break;
				
	case 'DCP' :
		if(mode=="view")
		{
				if(vStatus2=='Reversal')
						{ 			 		
			 				cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		        		 }
		 else 
				{					
					mode="viewCash";
					cWind=window.open("VMC/DirectBankPayment_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		     	}
																  	 	
                         cWindows[count++]=cWind;
	 					 break;
		}

		if(mode=="modify")
		mode="reverseCash";
		//window.location="DirectCashPayment.htm?cgNumber="+cgn1+"&showMode="+mode;
		//cWind=window.open("DirectCashPayment.htm?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		cWind=window.open("VMC/DirectCashPayment_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		cWindows[count++]=cWind;
		break;
	
	
	}
}

</SCRIPT>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onloadTasks()" onbeforeunload="closeChilds(cWindows,count)"><!------------------ Header Begins Begins--------------------->
<jsp:useBean id="Bean"  class="com.exilant.eGov.src.transactions.CommonBean" scope ="request"/>
<jsp:setProperty name="Bean" property="inputFieldC"/>
<jsp:setProperty name="Bean" property="inputField6"/>
<jsp:setProperty name="Bean" property="*"/>

<form name="journalVoucher">
<input type=hidden id="inputField1" name="inputField1"/>
<input type=hidden id="inputField2"  name="inputField2" value="">
<input type=hidden id="inputFieldC" name="inputFieldC" value="">
<input type=hidden id="inputField4"  name="inputField4" value="">
<input type=hidden id="inputField5"  name="inputField5" value="">
<input type=hidden id="inputField6"  name="inputField6" value="">
<input type=hidden id="inputField7"  name="inputField7" value="">
<input type=hidden id="inputField8" name="inputField8">
<input type=hidden id="inputField9" name="inputField9">
<input type=hidden id="inputField10" name="inputField10">
<input type=hidden id="inputField11" name="inputField11">
<input type=hidden id="showMode" name="showMode" value="" />

<table><tr><td><div id="main"><div id="m2"><div id="m3">
	<table width="100%" border=0 cellpadding="3" cellspacing="0">
		<tr class=tableheader>
			<td valign="center" colspan="4" width="100%"><span>
       Payments - Search</span></td>
		</tr>
		<tr name="type1" id="type1"  >
			<td width="25%" align=right><div align="right" valign="center" class="labelcell" name="paymentLabel" id="paymentLabel">Payment Type<span class="leadon">*</span></div></td>
			<td width="25%">
				<!--select class="fieldinput" name="paymentType" id="paymentType" onChange="ModeChange()" exilListSource="paymentType" exilMustEnter="true"-->
				<span class=smallfieldcell><select class="fieldinput" style="width:150px" name="paymentType"  id="paymentType" exilMustEnter="true"  tabindex=1>
				<option value=""      selected></option>
        			<option value="Bank Payment"> Bank</option>
        			<option value="Cash Payment"> Cash</option>
					<option value="Advance"> Advance</option>
					<option value="Subledger Payment">Pay Supplier/Contractor</option>
					<option value="Salary"> Salary</option>
					<option value="Bank Entry">Bank Entry</option>
				</select></span>
				</td>

			<td width="25%" >&nbsp;</td>
			<td width="25%">&nbsp;
			</td>
		</tr>

		<tr >
			<td align=right ><div align="right" valign="center" class="labelcell">Voucher Number</div></td>
			<td>
				<span class=fieldcell><input class="fieldinput" name="voucherNumber" id="voucherNumber" exilDataType="exilAnyChar"  tabindex=1></span>
			</td>

			<td align=right><div align="right" valign="center" class="labelcell"> Fund&nbsp; </div></td>
			<td >
				<span class=smallfieldcell><SELECT CLASS="fieldinput" NAME="fund" ID="fund" exilListSource="fundNameList" tabindex=1 > </SELECT></span>
			</td>
		</tr>

		<tr >
			<td  align=right><div align="right" valign="center" class="labelcell">Voucher Date From</div></td>
			<td >
				<span class=smallfieldcell><input class="datefieldinput"  name="voucherDateFrom"  id="voucherDateFrom" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" tabindex=1></span>
			</td>

			<td align=right><div align="right" valign="center" class="labelcell">To</div></td>
			<td>
				<span class=smallfieldcell><input class="datefieldinput" name="voucherDateTo"  id="voucherDateTo" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" tabindex=1></span>
			</td>
		</tr>
		<tr >
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<input type="button"  id="revsave" value="Search" class="button" tabindex=1 onclick="ButtonPress();"/>
						</td>
					</tr>
				</table>
				
			</td>
		</tr>
	</table>
	</div></div></div></td></tr></table>
	

	<input type="hidden" name="displayCondition" id="displayCondition" value="0">

			<%

					    System.out.print("\n Before submit "+request.getParameter("displayCondition"));
					    LinkedList list=new LinkedList();
					    if(request.getParameter("displayCondition") !=null && request.getParameter("displayCondition").equals("1"))
					    {
						 System.out.print("\n after submit "+request.getParameter("displayCondition"));
							 try{
								 CommonList Bills= new CommonList();
								 list=Bills.getList(Bean);
								// System.out.println("list.size():"+list.size());

									if(list!=null && list.size()>0)
									{	System.out.println("inside if");
										request.setAttribute("paymentSearchGrid",list);
										System.out.println("inside if after");



										try{
							 %>

				<center>
					<div  id="tbl-container" class="tbl2-container"  align="center" width="100%">
					 <display:table name="paymentSearchGrid" id="paymentSearchGrid"  export="false" sort="list" pagesize = "15" class="view" cellspacing="0">


						<%com.exilant.eGov.src.transactions.CommonBean obj=(com.exilant.eGov.src.transactions.CommonBean)pageContext.findAttribute("paymentSearchGrid");
							//System.out.println("field3"+obj.getField3());

						%>
						<display:column property="field1" title="<%=Bean.getField1()%>" style="width:6px; align=center" media="html"   />
						<display:column  title="<%=Bean.getField2()%>" style="width:90px; align=center">
						<% if(obj.getField2()!=null){%>  <%=obj.getField2()%>  <%}%>
						</display:column>
						<display:column   title="<%=Bean.getField3()%>" >
						<span align=right>

						<%
							String vnoCgno=obj.getField3();
							String cgn=vnoCgno.substring(vnoCgno.indexOf("`--`")+"`--`".length(),vnoCgno.length());
							System.out.print("\n CGN:"+cgn);
							String vno=vnoCgno.substring(0,vnoCgno.indexOf("`--`"));
							if(vno==null) vno="";
						%>
						<a href="#" id="cgn" onClick="getDetails(this,'<%=cgn%>','<%=obj.getField8()%>','<%=obj.getField7()%>','<%=obj.getField6()%>')"><%=vno%></a>
						</span>

						</display:column>
						<display:column property="field4" title="<%=Bean.getField4()%>" >

						</display:column>
						<display:column property="field5"  title="<%=Bean.getField5()%>" headerClass="" >

						</display:column>
						<display:column  title="<%=Bean.getField6()%>" headerClass=""  >
						<% if(obj.getField6()!=null){%>  <%=obj.getField6()%>  <%}%>
						</display:column>

						<display:column   title="<%=Bean.getField7()%>" headerClass="" >
						<% if(obj.getField7()!=null){%>  <%=obj.getField7()%>  <%}%>
						</display:column>

						<display:column   title="<%=Bean.getField8()%>" headerClass=""  >
						<% if(obj.getField8()!=null){%>  <%=obj.getField8()%>  <%}%>
						</display:column>
						<display:column  title="<%=Bean.getField9()%>" headerClass=""   >
						<% if(obj.getField9()!=null){%>  <%=obj.getField9()%>  <%}%>
						</display:column>
					</display:table>
					</div>
				</center>
			<%					}catch(Exception e){e.getMessage();}
					}else{
			%>
							<center>
								<display:table   export="false" sort="list" pagesize = "15" class="view" cellspacing="0" cellpadding="10">
									<display:caption style="align:center">Nothing Found to Display</display:caption>
								</display:table>
							</center>

			<%  		}
				  }catch(Exception e){
					 System.out.print("Exception in JSP page:" + e.getMessage());
							 		}
				}
				if(list.size()>0)
				{
				%>

					<script>
						var table=document.getElementById('paymentSearchGrid'); // bootbox.alert(table.innerHTML);
						for (var i=0;i<table.rows.length;i++)
						{	var k=0;
							for (var j=table.rows[i].cells.length-1; j >= k ; j--)
							{
								if(table.rows[i].cells[j].innerHTML == "")
									table.rows[i].cells[j].style.display="none";

							}
						}

						//changeColor();
						var table=document.getElementById('paymentSearchGrid');
						var j=0;
						for(var i=0;table.rows[0].cells.length;i++){//bootbox.alert((table.rows[0].cells[5].innerHTML).toUpperCase());
							var oo=table.rows[0].cells[i]
							if((oo.innerHTML).toUpperCase()=='STATUS')
							{ j=i; break;}
						}

						for(var i=1;i<table.rows.length;i++)
						{
					 			var status1=table.rows[i].cells[j].innerHTML; //bootbox.alert(status1);//.innerHTML;
					 			var rev;
					 			if(status1 = 'Reversed')
					 			{ //bootbox.alert("in reversed");
					 				table.rows[i].className='rowRev'; //bootbox.alert(table.rows[i]);
					 			}
					 			else if(status1=='Reversal')
					 			{//bootbox.alert("in reversal");

					 				table.rows[i].className='rowRev2';
					 			}
					 			else
					 			{
									//var trObj=PageManager.DataService.getRow(statusObj);
					 				//table.rows[i].className='no';
								}
							//bootbox.alert("end");
					  	 	}

					</script>
				<%}%>

	<!-- table width="100%" border="1" cellpadding="0" cellspacing="0" name="paymentSearchGrid" id="paymentSearchGrid" style="DISPLAY: none">
		<tr class="rowheader">
			<td width="5%" class="columnheader"><div align="center" class="columnheader">Sl No</div></td>
			<td width="10%" class="columnheader"><div align="center">CG Number</div></td>
			<td width="20%" class="columnheader"><div align="center">Voucher Date</div></td>
			<td width="15%" class="columnheader"><div align="center">Voucher Number</div></td>
			<td width="15%" class="columnheader"><div align="center">PaidTo</div></td>
			<td width="10%" class="columnheader"><div align="center">PassedAmt</div></td>
			<td width="10%" class="columnheader"><div align="center">BillAmt</div></td>
			<td width="15%" class="columnheader"><div align="center" id="cheNo">ChequeNo</div></td>
			<td width="15%" class="columnheader"><div align="center">Paid Amount</div></td>
			<td width="20%" class="columnheader"><div align="center">Status</div></td>
		</tr>

		<tr class="normaltext">
			<td><div id="slNo" name="slNo" exilTrimLength="8">&nbsp;</div></td>
			<td><div id="cgNumber" name="cgNumber" exilTrimLength="10">&nbsp;</div></td>
			<td><div id="voucherDate" name="voucherDate" >&nbsp;</div></td>
			<td><A onclick=getDetails(this) href="#"><div id="voucherNumber" name="voucherNumber" exilTrimLength="15">&nbsp;</div></A></td>
			<td><div id="paidTo" name="paidTo" exilTrimLength="20">&nbsp;</div></td>
			<td align=right><div id="passedAmount" name="passedAmount" exilTrimLength="16">&nbsp;</div></td>
			<td align=right style="BORDER-RIGHT-WIDTH: thick"><div id="billAmount" name="billAmount" exilTrimLength="16">&nbsp;</div></td>
			<td><div id="chequeNumber" name="chequeNumber" exilTrimLength="10">&nbsp;</div></td>
			<td><div id="paidAmount" name="paidAmount" exilTrimLength="16">&nbsp;</div></td>
			<td><div id="status" name="status"  exilTrimLength="13">&nbsp;</div></td>
		</tr>
	</table -->
</form>
</body>
</html>
