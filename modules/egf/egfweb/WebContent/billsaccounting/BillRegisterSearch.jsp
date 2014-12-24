<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<html>
<head>

<title>Bill Register</title>


<script LANGUAGE="javascript">

var cWind;         // temporary window reference
var count=1;      // Counter which gives number of child windows opened
var cWindows=new Object();    //HashMap where we store child window
var mode="";

function onBodyLoad(){
PageValidator.addCalendars();
PageManager.ListService.callListService();
mode = PageManager.DataService.getQueryField('showMode');
//alert('mode = '+ mode);
if(mode=='view')
{
	document.getElementById("department_id").setAttribute('exilListSource',"jvsal_department");
	PageManager.ListService.callListService();
	
//document.getElementById("billNoList").setAttribute('exilListSource',"billNumberList");
PageManager.ListService.callListService();

}
else if(mode=='modify')
{
//document.getElementById("billNoList").setAttribute('exilListSource',"billNumberListModify");
PageManager.ListService.callListService();
//document.getElementById('screenName').innerHTML="Bill Register Modify";

}

}

function addSlNo(){
	var slNo=1;
	var tabObj=document.getElementById("billRegisterSearchGrid");
	for(var i=1;i<tabObj.rows.length;i++){
		var obj=PageManager.DataService.getControlInBranch(tabObj.rows[i],"slNo");
		obj.innerHTML=slNo;
		slNo++;
	}

}
function afterRefreshPage(dc){
	addSlNo();
}
function beforeRefreshPage(dc){
	var tabObj=dc.grids['billRegisterSearchGrid'];

	if(!tabObj)	return false;
	if(tabObj.length<2){
			alert("No Data");}

	for(var i=1;i<tabObj.length;i++){
	tabObj[i][5]=formatDate(tabObj[i][5]);
	}

	return true;

}

function ButtonPress()
{
			hideColumn(1);
			if(document.getElementById('expenditure_Type').value==""){ alert("select Expenditure Type"); return;}
			var expType=document.getElementById('expenditure_Type').value;
			if(expType && expType.length!=0)
				PageManager.DataService.setQueryField('expType',expType);
			if(expType.length=='0')
			{
			PageManager.DataService.removeQueryField('expType');
			}

			var billType=document.getElementById('bill_Type').value;
		
			if(billType && billType.length!=0)
				PageManager.DataService.setQueryField('billType',billType);
			if(billType.length=='0')
			{
			PageManager.DataService.removeQueryField('billType');
			}
			var deptId = document.getElementById('department_id').value;
			if(deptId && deptId.length!=0)
			PageManager.DataService.setQueryField('deptId',deptId);

			if(deptId.length=='0')
			{
			PageManager.DataService.removeQueryField('deptId');
			
			}
			/*var billNo=document.getElementById('billNoList').value;
			if(billNo && billNo.length!=0)
			PageManager.DataService.setQueryField('billNo',billNo);
			if(billNo.length=='0')
			{
			PageManager.DataService.removeQueryField('billNo');
			}
			*/
			var billDate=document.getElementById('bill_Date').value;
			if(billDate && billDate.length!=0)
			PageManager.DataService.setQueryField('billDate',formatDate1(billDate));

			if(billDate.length=='0')
			{
			PageManager.DataService.removeQueryField('billDate');
			alert("Enter From Date");
			return false;
			}
			
			
			var billDateTo=document.getElementById('bill_DateTo').value;
			if(billDateTo && billDateTo.length!=0)
			PageManager.DataService.setQueryField('billDateTo',formatDate1(billDateTo));

			if(billDateTo.length=='0')
			{
			PageManager.DataService.removeQueryField('billDateTo');
			alert("Enter To Date");
			return false;
			}
			
			if( compareDate(formatDate6(billDate),formatDate6(billDateTo)) == -1 )
			{
			alert('Start Date cannot be greater than End Date');
			document.getElementById('bill_Date').focus();
			return false;
			}
			
			 mode = PageManager.DataService.getQueryField('showMode');
			if(mode=="modify")
			PageManager.DataService.callDataService('billRegisterSearchModify');
			else
			PageManager.DataService.callDataService('billRegisterSearchId');
	var table=document.getElementById('billRegisterSearchGrid');
	table.style.display="block";
} // Button Press

function hideColumn(index){
	var table=document.getElementById('billRegisterSearchGrid');
   	for(var i=0;i<table.rows.length;i++){
		table.rows[i].cells[index].style.display="none";
   				}
}

function getDetails(obj){
	var row=PageManager.DataService.getRow(obj);
	var table=document.getElementById('billRegisterSearchGrid');
	var billNo=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"colH");
	var expType=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"colA");
	//alert("expType ::"+ expType+ "name ::"+ expType.innerHTML);
	billNo=billNo.innerHTML;
	//if(document.getElementById('expenditure_Type').value == 'Expense')
	if(expType != null)
	{
		if(expType.innerHTML == 'Expense')
		{cWind=window.open("../billsaccounting/cbill.do?submitType=beforeViewModify&mode="+mode+"&expType="+expType.innerHTML+"&billId="+billNo,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		cWindows[count++]=cWind;
		}
		else if(expType.innerHTML == 'Works')
		{
	cWind=window.open("../billsaccounting/worksBill.do?submitType=beforeViewModify&mode="+mode+"&expType="+expType.innerHTML+"&billId="+billNo,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		cWindows[count++]=cWind;
		}
		else if(expType.innerHTML == 'Purchase')
		{
	cWind=window.open("../billsaccounting/purchaseBill.do?submitType=beforeViewModify&mode="+mode+"&expType="+expType.innerHTML+"&billId="+billNo,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		cWindows[count++]=cWind;
		}
	}
	}

function fillDate1(objName)
	{
		PageValidator.showCalendar('selectedDate');
		document.getElementById(objName).value=document.getElementById('selectedDate').value;
		document.getElementById('selectedDate').value = "";
	}

</script>
</head>
<body onLoad="onBodyLoad();" bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0"  onKeyDown ="CloseWindow(window.self);">

<form name="BillsRegisterSearch">
<input type="hidden" name="selectedDate" id="selectedDate">

<table width="100%" border=0 cellpadding="3"  class=tableStyle cellspacing="0" id="billDetailView" name="billDetailView" >




<td align="right" valign="center" ><div align="right" valign="center" class="labelcell" ><bean:message key="BillRegister.expenditureType"/><br>Expenditure Type<SPAN class="leadon">*</SPAN></div></td>
<td  class="smallfieldcell">
<select class="combowidth1" name="expenditure_Type" id="expenditure_Type"  exilMustEnter="true">
<option value="" selected>&nbsp;</option>
<option value="Expense">Expense</option>
<option value="Purchase">Purchase</option>
<option value="Works">Works</option>

</select>
</td>
<td align="right" valign="center"><div align="right" valign="center" class="labelcell" ><bean:message key="BillRegister.billType"/><br>Bill Type</div></td>
<td  class="smallfieldcell" >
<select   class="combowidth1" name="bill_Type" id="bill_Type"   exilMustEnter="true"  >
<option value="" selected>&nbsp;</option><option value="Running Bill">Running Bill</option>
<option value="Final Bill">Final Bill</option>
</select>
</td>
<td >&nbsp;</td>
</tr>





<td align="right" valign="center"><div align="right" class="labelcell"><bean:message key="BillRegister.billDate"/><br>Bill Date From <SPAN class="leadon">*</SPAN></div></td>
<td   class="smallfieldcell" ><input name="bill_Date" id="bill_Date" class="fieldinput"  onkeyup="DateFormat(this,this.value,event,false,'3')" exilMustEnter="true" exilDataType="exilAnyDate" >
<a href="javascript:show_calendar('forms[0].bill_Date');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG tabIndex=-1 src="../images/calendar.gif"></A>
</td>

<td align="right" valign="center"><div align="right" class="labelcell"><bean:message key="BillRegister.billDate"/><br> To <SPAN class="leadon">*</SPAN></div></td>
<td class="smallfieldcell" ><input name="bill_DateTo" id="bill_DateTo" class="datefieldinput"   onkeyup="DateFormat(this,this.value,event,false,'3')" exilMustEnter="true" exilDataType="exilAnyDate" >
<A href="javascript:show_calendar('forms[0].bill_DateTo');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><IMG tabIndex=-1 src="../images/calendar.gif"></A>
</td>
<td >&nbsp;</td>
</tr >
<tr>
<td align="right" valign="center" ><div align="right" class="labelcell" ><bean:message  key="SalaryRegisterSearch.department"/><br>Department &nbsp;</div>
	</td>
	<td  class="smallfieldcell">
	<SELECT name="department_id" id="department_id" exilListSource="departmentList"
	 class="fieldinput">
	</SELECT>

	</td> 
	</tr>
<tr class="row2" width="100%">
	<td colspan="5" align="middle"><!-- Buttons Start Here -->
<table border="0" cellpadding="0" cellspacing="0" ID="Table1">
	<tr>
	<td align="center">
	<input type=button class=button onClick="ButtonPress()" href="#" value="Search">
	<td>&nbsp;</td>
	<td align="center">
	<input type=button class=button onClick="window.close()" href="#" value="close">
	</td>
	</tr>
</table>
</td>
</tr>
</table>

<table width="100%"  border="0" cellpadding="0" cellspacing="0">
<tr class="row1" >
<td>
<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" name="billRegisterSearchGrid" id="billRegisterSearchGrid" style="DISPLAY:none">
<tr class="rowheader">
<td width="5%" height="34" class="thStlyle"><div align="center" class="thStlyle">Sl No</div></td>
<td width="5%" class="thStlyle"><div align="center" id="gridH">Bill Id</div></td>
<td width="10%" class="thStlyle"><div align="center" id="gridA">Expenditure Type</div></td>
<td width="10%" class="thStlyle"><div align="center" id="gridB">Bill Type</div></td>
<td width="25%" class="thStlyle"><div align="center" id="gridC">WO/PO Name</div></td>
<td width="13%" class="thStlyle"><div align="center" id="gridD">Bill/File Number</div></td>
<td width="11%" class="thStlyle"><div align="center" id="gridE">Bill Date</div></td>
<td width="14%" class="thStlyle"><div align="center" id="gridF">Bill Amount</div></td>
<td width="14%" class="thStlyle"><div align="center" id="gridG">Passed Amount</div></td>
<td width="14%" class="thStlyle"><div align="center" id="gridI">Bill Status</div></td>
</tr>

<tr class="labelcell">
<td><div id="slNo" name="slNo" exilTrimLength="8">&nbsp;</div></td>
<td><div id="colH" name="colH" exilTrimLength="5">&nbsp;</div></td>
<td><div id="colA" name="colA" exilTrimLength="15">&nbsp;</div></td>
<td><div id="colB" name="colB" exilTrimLength="15">&nbsp;</div></td>
<td><div id="colC" name="colC" >&nbsp;</div></td>
<td><A onClick="getDetails(this)" href="#"><div id="colD" name="colD" exilTrimLength="15">&nbsp;</div></A></td>
<td><div id="colE" name="colE" exilTrimLength="15">&nbsp;</div></td>
<td align="right"><div id="colF" name="colF" exilTrimLength="16">&nbsp;</div></td>
<td align="right"><div id="colG" name="colG" exilTrimLength="16">&nbsp;</div></td>
<td><div id="colI" name="colI" exilTrimLength="10">&nbsp;</div></td>
</tr>
</table>
</td>
</tr>
</table>
</form>


</body>
</html>
