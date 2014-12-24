<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>Cancel Bills</title>
<SCRIPT LANGUAGE="javascript">
var cWind;
//var mode='viewBank';
function getGrid(){

		var BillNumberFrom=document.getElementById('BillNumberFrom').value;
		var BillNumberTo=document.getElementById('BillNumberTo').value;
		if(  BillNumberTo )
			if( BillNumberFrom  && BillNumberFrom > BillNumberTo )
				{
				alert('Bill Number To should be less than or equal to Bill Number From');
				document.getElementById('BillDateTo').focus();
				return false;
				}


		var strtDate = document.getElementById('BillDateFrom').value;
	 	var endDate = document.getElementById('BillDateTo').value;
		var dbDate=document.getElementById('databaseDate').value;
				if(compareDate(formatDateToDDMMYYYY1(endDate),formatDateToDDMMYYYY1(dbDate)) == -1 )
				{
				alert('Bill Date To should be less than or equal to '+dbDate);
				document.getElementById('BillDateTo').focus();
				return false;
				}
	if(strtDate.length !=0 && endDate.length !=0){

		if( compareDate(formatDateToDDMMYYYY1(strtDate),formatDateToDDMMYYYY1(endDate)) == -1 ){
		alert('Bill Date From cannot be greater than Bill Date To');
		document.getElementById('BillDateFrom').focus();
		return false;
	   }
	   }
	if(PageValidator.validateForm()){

		PageManager.DataService.removeQueryField('BillNumberFrom');
		PageManager.DataService.removeQueryField('BillNumberTo');

		PageManager.DataService.removeQueryField('BillDateFrom');
		PageManager.DataService.removeQueryField('BillDateTo');

		var BillNumberFrom=document.getElementById('BillNumberFrom').value;
		if(BillNumberFrom && BillNumberFrom.length!=0)
			PageManager.DataService.setQueryField('BillNumberFrom',BillNumberFrom);

		var BillNumberTo=document.getElementById('BillNumberTo').value;
		if(BillNumberTo && BillNumberTo.length!=0)
			PageManager.DataService.setQueryField('BillNumberTo',BillNumberTo);

		var BillDateFrom=document.getElementById('BillDateFrom').value;
		if(BillDateFrom && BillDateFrom.length!=0)
			PageManager.DataService.setQueryField('BillDateFrom',formatDate1(BillDateFrom));


		var BillDateTo=document.getElementById('BillDateTo').value;
		if(BillDateTo && BillDateTo.length!=0)
			PageManager.DataService.setQueryField('BillDateTo',formatDate1(BillDateTo));

		

		var table=document.getElementById('BillSearchGrid');
		table.style.display="block";

		PageManager.DataService.callDataService('getEgBillsForCancellation');
	}

}

function onloadTasks(){
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	PageManager.DataService.callDataService('finYearDate');
}

function beforeRefreshPage(dc){
		if(dc.values['serviceID']=='finYearDate')
		{
		var dt=dc.values['startFinDate']
		dt=formatDate2(dt);
		document.getElementById('BillDateFrom').value=dt;
		}
	var tabObj=dc.grids['BillSearchGrid'];
	if(!tabObj)	return false;
	if(tabObj.length<2){
		document.getElementById('gridSubmit').style.display = "none";
		alert("No Data");
	}else{
		document.getElementById('BillSearchGrid').style.display = "block";
		document.getElementById('gridSubmit').style.display = "block";
	}

	return true;
}

function afterRefreshPage(dc){
	addSlNo();

			var BillDateFrom=document.getElementById('BillDateFrom').value;
			var BillDateTo=document.getElementById('BillDateTo').value;
			if(BillDateFrom && BillDateFrom.length!=0)
					{
					var VouDateFrom=dc.values['BillDateFrom'];
					VouDateFrom=formatDateToDDMMYYYY5(VouDateFrom);
					document.getElementById('BillDateFrom').value=VouDateFrom;
					}

				if(BillDateTo && BillDateTo.length!=0)
				{
					var VouDateTo=dc.values['BillDateTo'];
					VouDateTo=formatDateToDDMMYYYY5(VouDateTo);
					document.getElementById('BillDateTo').value=VouDateTo;
				}

	
}

function addSlNo(){
	var slNo=1;
	var tabObj=document.getElementById("BillSearchGrid");
	for(var i=1;i<tabObj.rows.length;i++){
		var obj=PageManager.DataService.getControlInBranch(tabObj.rows[i],"slNo");
		obj.innerHTML=slNo;
		slNo++;
	}

}
function hideColumn(index){
	var table=document.getElementById('BillSearchGrid');
   	for(var i=0;i<table.rows.length;i++){
		table.rows[i].cells[index].style.display="none";
   	}
}
function getDetails(obj){
	var row=PageManager.DataService.getRow(obj);
	var table=document.getElementById('BillSearchGrid');
	var dateFrom = document.getElementById('BillDateFrom').value;
	var dateTo = document.getElementById('BillDateTo').value;
	var drillDown=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"billId");
	drillDown=drillDown.value;
	
	
	var ExpType=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"BillRegister_expType");
	ExpType=ExpType.innerHTML;
	var deptId=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"deptId");
	deptId=deptId.innerHTML;
	
	switch(ExpType)
		{
		case 'Salary':		cWind=window.open("SalaryBillRegister_VMC.jsp?billId="+drillDown+"&showMode=view"+"&departmentId="+deptId,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		default:		cWind=window.open("BillRegister_VMC.jsp?billNumber="+drillDown+"&showMode=view"+"&expType="+ExpType,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");	
	}
}
function confirm1(){

	var cnfAll = document.getElementById('isConfirmedAll');

	var grid = document.getElementById('BillSearchGrid');
	var chkBox;

	for(var cnt=1; cnt<grid.rows.length; cnt++)	{
		chkBox = PageManager.DataService.getControlInBranch(grid.rows[cnt], 'BillRegister_isConfirmed');
	
		if(!chkBox.checked){
			cnfAll.checked=false;
			return;
		}
	}
	cnfAll.checked=true;
}
function reject(obj){
	alert('Not Implemented Yet');
	obj.checked = false;
}
function confirmAll(){
	var cnfAll = document.getElementById('isConfirmedAll');
	var grid = document.getElementById('BillSearchGrid');

	for(var cnt=1; cnt<grid.rows.length; cnt++)
		PageManager.DataService.getControlInBranch(grid.rows[cnt], 'BillRegister_isConfirmed').checked = cnfAll.checked;

}
function rejectAll(obj){
	alert('Not Implemented Yet');
	obj.checked = false;
}
function ButtonPress(name){

    var grid = document.getElementById('BillSearchGrid');
	var cnt=grid.rows.length;
	var i=0;
	for(var cnt=1; cnt<grid.rows.length; cnt++){
		chkBox = PageManager.DataService.getControlInBranch(grid.rows[cnt], 'BillRegister_isConfirmed');
		if(chkBox.checked){
		i++;}
		}
if(i>0){

	var answer=confirm("Are you sure you want to continue?")
		if(!answer)
		return false;
	PageManager.UpdateService.submitForm('cancelBil');
	}
else
	alert("Select some bills to submit");
}
function afterUpdateService(dc){
	window.location = "CancelBills_VMC.jsp";
}
function fillDate1(objName)
	{
		PageValidator.showCalendar('selectedDate');
		document.getElementById(objName).value=document.getElementById('selectedDate').value;
		document.getElementById('selectedDate').value = "";
	}

</SCRIPT>

</head>
<body bgcolor="#ffffff" onKeyDown ="CloseWindow(window.self);" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onloadTasks()" ><!------------------ Header Begins Begins--------------------->
<center>
<br>
<form name="cancelBil">
<input type="hidden" name="databaseDate"  id="databaseDate">
<input type="hidden" name="startFinDate" id="startFinDate">
<input type="hidden" name="selectedDate" id="selectedDate">

	<table align='center' class="tableStyle" id="table3"> 

<!--	<table width="100%" border=0 cellpadding="3" cellspacing="0"> -->
		<tr>
			<td class="tableheader" valign="center" colspan="4"><SPAN >Cancellation of Bills</SPAN></td>
		</tr>
		<tr>
		<td align="right" valign="center"><div align="right"  class="labelcell">Bill Number From &nbsp;</div></td>
		<td class="fieldcell">
			<input class="fieldinput" name="BillNumberFrom" id="BillNumberFrom" exilDataType="exilAnyChar" size=20>
		</td>
		<td align="right" valign="center"><div align="right"  class="labelcell"> To &nbsp;</div></td>
		<td class="fieldcell">
			<input class="fieldinput" name="BillNumberTo" id="BillNumberTo" exilDataType="exilAnyChar" size=20 >
		</td>
		</tr>
		<tr>
			<td width="25%" align="right"><div align="right"  class="labelcell" id="vDate" >Bill Date From<span class="leadon">* &nbsp;</span></div></td>
		  	<td width="25%" class="smallfieldcell">
		  		<input class="datefieldinput" name="BillDateFrom" id="BillDateFrom" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilDataType="exilAnyDate"  SIZE="20" >
		  		<A onclick="fillDate1('BillDateFrom')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A>
			</td>
		 	<td width="25%" align="right" valign="center" ><div align="right" class="labelcell">To &nbsp;<span class="leadon"></span></div></td>
			<td width="25%" class="smallfieldcell">
				<input class="datefieldinput" name="BillDateTo"  id="BillDateTo" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate"  SIZE="20"  >
				<A onclick="fillDate1('BillDateTo')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A>
			</td>
		</tr>

		<tr>
			<td></td>
			<td >
				&nbsp;</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0" ID="Table1">
					<tr>
						<td align="right">
						<input type=button class=button onclick=getGrid() href="#" value="Search"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" id=TABLE1>
		<tr>
			<td>
				<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" name="BillSearchGrid" id="BillSearchGrid" style="DISPLAY: none">
					<tr class="tableheader">
						<td width="5%" height="34" class="thStlyle"><div align="center">Sl No</div></td>
						<td width="12%" class="thStlyle"><div align="center" id="gridB">Bill Number</div></td>
						<td width="15%" class="thStlyle"><div align="center" id="gridC">Bill Date</div></td>
						<td width="15%" class="thStlyle"><div align="center" id="gridC">Fund</div></td>
						<td width="10%" class="thStlyle"><div align="center" id="gridC">Name</div></td>
						<td width="13%" class="thStlyle"><div align="center" id="gridC">Department</div></td>
						

						<td width="10%" class="thStlyle"><div align="center" id="gridE">BillType</div></td>
						<td width="15%" class="thStlyle"><div align="center" id="gridC">BillAmount</div></td>
						<td width="5%" class="thStlyle"><div align="center" id="gridF">Cancel</div><div align="center">All <input type="checkbox" name="isConfirmedAll" id="isConfirmedAll" onClick="confirmAll()" VALUE="ON"></div></td>
				  	</tr>
				 	<tr class="tdStlyle">
						<td><div id="slNo" name="slNo">&nbsp;</div></td>
						
						<td><A onclick=getDetails(this) href="#"><div id="BillRegister_BillNumber" name="BillRegister_BillNumber">&nbsp;</div></A></td>
						<td style="Display: none"><Input type="Hidden"   id="billId" name="billId" ></td>
						<td><div id="BillRegister_BillDate" name="BillRegister_BillDate">&nbsp;</div></td>
						<td><div id="Fund" name="Fund">&nbsp;</div></td>
						<td><div id="BillRegister_expType" name="BillRegister_expType"></div></td>
						<td ><div id="BillRegister_Department" name="BillRegister_Department">&nbsp;</div><div  id="deptId" name="deptId" style="Display: none"></div></td>

						<td><div id="BillRegister_Type" name="BillRegister_Type">&nbsp;</div></td>
						<td><div id="BillRegister_Amount" name="BillRegister_Amount">&nbsp;</div></td>
											

						<td align=middle><input type="checkbox" name="BillRegister_isConfirmed" id="BillRegister_isConfirmed" onClick="confirm1()" VALUE="ON"></td>
				
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0" ID="gridSubmit" style="DISPLAY: none">
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="right">
						<input type=button class=button onclick=ButtonPress('saveclose') href="#" value="Submit"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

</form>
</center>
</body>
</html>