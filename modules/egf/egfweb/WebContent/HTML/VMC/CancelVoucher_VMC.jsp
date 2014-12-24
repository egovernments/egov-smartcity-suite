<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>Cancel Voucher</title>	
<SCRIPT LANGUAGE="javascript">
var cWind;
//var mode='viewBank';
function getGrid(){

		var voucherNumberFrom=document.getElementById('voucherNumberFrom').value;
		var voucherNumberTo=document.getElementById('voucherNumberTo').value;
		if(  voucherNumberTo )
			if( voucherNumberFrom  && voucherNumberFrom > voucherNumberTo )
				{
				alert('Voucher Number To should be less than or equal to Voucher Number From');
				document.getElementById('voucherDateTo').focus();
				return false;
				}


		var strtDate = document.getElementById('voucherDateFrom').value;
	 	var endDate = document.getElementById('voucherDateTo').value;
		var dbDate=document.getElementById('databaseDate').value;
				if(compareDate(formatDateToDDMMYYYY1(endDate),formatDateToDDMMYYYY1(dbDate)) == -1 )
				{
				alert('Voucher Date To should be less than or equal to '+dbDate);
				document.getElementById('voucherDateTo').focus();
				return false;
				}
	if(strtDate.length !=0 && endDate.length !=0){

		if( compareDate(formatDateToDDMMYYYY1(strtDate),formatDateToDDMMYYYY1(endDate)) == -1 ){
		alert('Voucher Date From cannot be greater than Voucher Date To');
		document.getElementById('voucherDateFrom').focus();
		return false;
	   }
	   }
	if(PageValidator.validateForm()){

		PageManager.DataService.removeQueryField('voucherNumberFrom');
		PageManager.DataService.removeQueryField('voucherNumberTo');

		PageManager.DataService.removeQueryField('voucherDateFrom');
		PageManager.DataService.removeQueryField('voucherDateTo');
		PageManager.DataService.removeQueryField('deptId');
		var voucherNumberFrom=document.getElementById('voucherNumberFrom').value;
		if(voucherNumberFrom && voucherNumberFrom.length!=0)
			PageManager.DataService.setQueryField('voucherNumberFrom',voucherNumberFrom);

		var voucherNumberTo=document.getElementById('voucherNumberTo').value;
		if(voucherNumberTo && voucherNumberTo.length!=0)
			PageManager.DataService.setQueryField('voucherNumberTo',voucherNumberTo);

		var voucherDateFrom=document.getElementById('voucherDateFrom').value;
		if(voucherDateFrom && voucherDateFrom.length!=0)
			PageManager.DataService.setQueryField('voucherDateFrom',formatDate1(voucherDateFrom));


		var voucherDateTo=document.getElementById('voucherDateTo').value;
		if(voucherDateTo && voucherDateTo.length!=0)
			PageManager.DataService.setQueryField('voucherDateTo',formatDate1(voucherDateTo));

		var fundId=document.getElementById('fund_id');
		if(fundId)
			PageManager.DataService.setQueryField('fund_id',fundId.value);
		else{
			PageManager.DataService.removeQueryField(fundId.value);
		}
        var voucherHeader_Type=document.getElementById('voucherHeader_Type');
		if(voucherHeader_Type)
			PageManager.DataService.setQueryField('voucherHeader_Type',voucherHeader_Type.value);
		else{
			PageManager.DataService.removeQueryField(voucherHeader_Type.value);
		}
		var deptId=document.getElementById('department_id').value;
		if(deptId&& deptId.length!=0)
			PageManager.DataService.setQueryField('deptId',deptId);

		var table=document.getElementById('voucherSearchGrid');
		table.style.display="block";

		PageManager.DataService.callDataService('unConfirmedVouchersForCancellation');
	}

}

function onloadTasks(){
	
	PageValidator.addCalendars();
	document.getElementById("department_id").setAttribute('exilListSource',"jvsal_department");
	PageManager.ListService.callListService();
// added by iliyaraja
	PageManager.DataService.callDataService('finYearDate');
	PageManager.DataService.callDataService('cgNumber');
	var vcrFrom = PageManager.DataService.getQueryField('vcrFrom');
	if(vcrFrom)	PageManager.DataService.setQueryField('voucherDateFrom', vcrFrom);
	var vcrTo = PageManager.DataService.getQueryField('vcrTo');
	if(vcrTo)	PageManager.DataService.setQueryField('voucherDateTo', vcrTo);
	var vcrCancel = PageManager.DataService.getQueryField('vcrCancel');
	document.getElementById('voucherHeader_Type').value='';
	if(vcrCancel) PageManager.DataService.callDataService('unConfirmedVouchersForCancellation');
	document.getElementById('voucherNumberFrom').focus();
}

function beforeRefreshPage(dc){
		if(dc.values['serviceID']=='finYearDate')
		{
		var dt=dc.values['startFinDate']
		dt=formatDate2(dt);
		document.getElementById('voucherDateFrom').value=dt;
		}
	var tabObj=dc.grids['voucherSearchGrid'];
	if(!tabObj)	return false;
	if(tabObj.length<2){
		document.getElementById('gridSubmit').style.display = "none";
		alert("No Data");
	}else{
		document.getElementById('voucherSearchGrid').style.display = "block";
		document.getElementById('gridSubmit').style.display = "block";
	}

	return true;
}

function afterRefreshPage(dc){
	addSlNo();

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
if(dc.values['serviceID'] == 'unConfirmedVouchersForCancellation')
	{
		hideColumn(2);
		hideColumn(9);
	}

	return true;
}

function addSlNo(){
	var slNo=1;
	var tabObj=document.getElementById("voucherSearchGrid");
	for(var i=1;i<tabObj.rows.length;i++){
		var obj=PageManager.DataService.getControlInBranch(tabObj.rows[i],"slNo");
		obj.innerHTML=slNo;
		slNo++;
	}

}
function hideColumn(index){
	var table=document.getElementById('voucherSearchGrid');
   	for(var i=0;i<table.rows.length;i++){
		table.rows[i].cells[index].style.display="none";
   	}
}
function getDetails(obj){
	var row=PageManager.DataService.getRow(obj);
	var table=document.getElementById('voucherSearchGrid');
	var dateFrom = document.getElementById('voucherDateFrom').value;
	var dateTo = document.getElementById('voucherDateTo').value;
	var cgn1=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"cgnum");
	cgn1=cgn1.innerHTML;
	var modeofPay=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"modeofpay");
	modeofPay=modeofPay.innerHTML;
	var cgn2=cgn1.substring(0,3);

	 callmeAP(cgn1,''); 
	 /*
	switch(cgn2){
		case 'JVG':
		cWind=window.open("JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'SJV' :
		cWind=window.open("SupplierJournal_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'CJV' :
		window.open("ContractorJournal_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'OJV' :
		window.open("ContingencyJournal_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'SAL' :
		window.open("JV_Salary_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'PYS':
		window.open("PayInSlip_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'BTB':
		window.open("JV_Contra_BToB_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'BTC':
		window.open("JV_Contra_BToC_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'CTB':
		window.open("JV_Contra_CToB_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'FTF':
		window.open("JV_Contra_FToF.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'BBP':
		window.open("../../payment/payment.do?submitType=beforeViewAndModifyPayment&cgNumber="+cgn1+"&mode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'DBP':
	 	window.open("DirectBankPayment_VMC.jsp?cgNumber="+cgn1+"&showMode=viewBank","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'DCP':
		window.open("DirectCashPayment_VMC.jsp?cgNumber="+cgn1+"&showMode=viewCash","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'SPH':
		window.open("/payment/payment.do?submitType=beforeViewAndModifyPayment&cgNumber="+cgn1+"&mode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'SSP':
		window.open("/payment/payment.do?submitType=beforeViewAndModifyPayment&cgNumber="+cgn1+"&mode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'PRO':
		window.location="PT_Office_VMC.jsp?vcrCancel=vcrCancel&vcrFrom="+dateFrom+"&vcrTo="+dateTo+"&cgNumber="+cgn1;
		break;
		case 'PRF':
		window.open("PT_Field.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'PRB':
		window.location="PT_Bank.htm?vcrCancel=vcrCancel&vcrFrom="+dateFrom+"&vcrTo="+dateTo+"&cgNumber="+cgn1;
		break;
		case 'OTO':
		window.open("OT_Office.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'OTF':
		window.open("OT_Field.htm?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
		case 'MSR':
		window.open("miscReceipt_VMC.jsp?cgNumber="+cgn1+"&showMode=view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		break;
	}*/
}
function confirm1(){

	var cnfAll = document.getElementById('isConfirmedAll');

	var grid = document.getElementById('voucherSearchGrid');
	var chkBox;

	for(var cnt=1; cnt<grid.rows.length; cnt++)	{
		chkBox = PageManager.DataService.getControlInBranch(grid.rows[cnt], 'voucherHeader_isConfirmed');
	//	alert(chkBox+"	"+cnt);
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
	var grid = document.getElementById('voucherSearchGrid');

	for(var cnt=1; cnt<grid.rows.length; cnt++)
		PageManager.DataService.getControlInBranch(grid.rows[cnt], 'voucherHeader_isConfirmed').checked = cnfAll.checked;

}
function rejectAll(obj){
	alert('Not Implemented Yet');
	obj.checked = false;
}
function ButtonPress(name){

    var grid = document.getElementById('voucherSearchGrid');
	var cnt=grid.rows.length;
	var i=0;
	for(var cnt=1; cnt<grid.rows.length; cnt++){
		chkBox = PageManager.DataService.getControlInBranch(grid.rows[cnt], 'voucherHeader_isConfirmed');
		if(chkBox.checked){
		i++;}
		}
if(i>0){

	var answer=confirm("Are you sure you want to continue?")
		if(!answer)
		return false;
	PageManager.UpdateService.submitForm('cancelVoucher');
	}
else
	alert("Select some bills to submit");
}
function afterUpdateService(dc){
	window.location = "CancelVoucher_VMC.jsp";
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
<form name="journalVoucher">
<input type="hidden" name="databaseDate"  id="databaseDate">
<input type="hidden" name="startFinDate" id="startFinDate">
<input type="hidden" name="selectedDate" id="selectedDate">

	<table width="100%" border=0 cellpadding="3" cellspacing="0">
		<table align='center' class="tableStyle" id="table3"> 
		<tr>
		<td align="right" valign="center" ><div align="right" class="labelcell">Voucher Number From &nbsp;</div></td>
		<td class="fieldcell">
			<input class="fieldinput" name="voucherNumberFrom" id="voucherNumberFrom" exilDataType="exilAnyChar" size=20>
		</td>
		<td align="right" valign="center" ><div align="right" class="labelcell"> To &nbsp;</div></td>
		<td class="fieldcell">
			<input class="fieldinput" name="voucherNumberTo" id="voucherNumberTo" exilDataType="exilAnyChar" size=20 >
		</td>
		</tr>
		<tr >
			<td width="25%" align="right"><div align="right" id="vDate" class="labelcell">Voucher Date From<span class="leadon">* &nbsp;</span></div></td>
		  <!--	<td width="25%" class="fieldcell">
		  		<input class="smallfieldcell" name="voucherDateFrom" id="voucherDateFrom" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilDataType="exilAnyDate"  SIZE="20" exilMustEnter="true">
		  		<A onclick="fillDate1('voucherDateFrom')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A>
			</td>-->
			<td class="smallfieldcell" width="40%"><INPUT class="datefieldinput" id="voucherDateFrom" name="voucherDateFrom" size="15" onkeyup="DateFormat
			(this,this.value,event,false,'3')" exilDataType="exilAnyDate"  SIZE="20" exilMustEnter="true" > <A onclick="fillDate1('voucherDateFrom')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A></td>
		 	
		 	<td width="25%" align="right" valign="center" ><div align="right" class="labelcell">To<span class="leadon">* &nbsp;</span></div></td>
			<!--<td width="25%" class="fieldcell">
				<input class="smallfieldcell" name="voucherDateTo"  id="voucherDateTo" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate"  SIZE="20" exilMustEnter="true" >
				<A onclick="fillDate1('voucherDateTo')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A>
			</td> -->
			<td class="smallfieldcell" width="40%"><INPUT class="datefieldinput" id="voucherDateTo" name="voucherDateTo" size="15" onkeyup="DateFormat
			(this,this.value,event,false,'3')" exilDataType="exilAnyDate"  SIZE="20" exilMustEnter="true" > <A onclick="fillDate1('voucherDateTo')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A></td>
			
		</tr>

		<tr>
			<td align="right" ><div valign="center" class="labelcell">Fund &nbsp;</div></td>
			<td class="smallFieldcell" >
				<SELECT class="fieldinput" id=fund_id name="fund_id" exilListSource="fundNameList"></SELECT>
			</td>
			
					
<td align="right" valign="center" ><div align="right" class="labelcell" ><bean:message  key="SalaryRegisterSearch.department"/><br>Department &nbsp;</div>
	</td>
	<td  class="smallfieldcell">
	<SELECT name="department_id" id="department_id"
	 class="fieldinput">
	</SELECT>

	</td> 
	
		</tr>
		<tr>
			<td align="right" valign="center" width="25%" ><div class="labelcell">	Voucher Type &nbsp;</div></td>
			<td width="30%" class="smallFieldcell">
				<span class="smallfieldcell">
					<select class="option" name="voucherHeader_Type" id="voucherHeader_Type" exilListSource="voucherTypeList"  width="20"/>
				</span></td>
			<td></td>
			<td>
				<span class="smallfieldcell"><br>
			</span></td>
		</tr>
		<tr>
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0" ID="Table1">
					<tr>
						<td align="right">
						<input type=button class=button onclick=getGrid() href="#" value="Search">						
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" id=TABLE1>
		<tr>
			<td>
				<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" name="voucherSearchGrid" id="voucherSearchGrid" style="DISPLAY: none">
					<tr class="tableheader">
						<td width="5%" height="34" class="thStlyle"><div align="center" class="thStlyle">Sl No</div></td>
						<td width="12%" class="thStlyle" style="DISPLAY: none"><div align="center" id="gridA">CG Number</div></td>
						<td width="10%" class="thStlyle" style="DISPLAY: none"><div align="center" id="gridA">CG NumberNew</div></td>
						<td width="20%" class="thStlyle"><div align="center" id="gridB">Voucher Number</div></td>
						<td width="15%" class="thStlyle"><div align="center" id="gridC">Voucher Date</div></td>
						<td width="15%" class="thStlyle"><div align="center" id="gridC">Fund</div></td>
						<td width="20%" class="thStlyle"><div align="center" id="gridC">Name Type</div></td>
						<td width="20%" class="thStlyle"><div align="center" id="gridE">Narration</div></td>
						<td width="8%" class="thStlyle"><div align="center" id="gridF">Cancel</div><div align="center">All <input type="checkbox" name="isConfirmedAll" id="isConfirmedAll" onClick="confirmAll()" VALUE="ON"></div></td><!--<td width="10%" class="columnheader"><div align="center" id="gridF">Reject</div><div align="center">All <input type="checkbox" name="isRejectedAll" id="isRejectedAll" onClick="rejectAll(this)"></div></td>-->
				  	    <td width="1%" class="thStlyle"><div align="center" id="gridG">ModeOfPay</div></td>
				  	</tr>
				 	<tr class="tdStlyle">
						<td><div id="slNo" name="slNo">&nbsp;</div></td>
						<td style="DISPLAY: none"><div id="voucherHeader_cgn" name="voucherHeader_cgn"></div><input size=1 type="hidden" name="voucherHeader_id" id="voucherHeader_id"></td>
						<td style="DISPLAY: none"><div id="cgnum" name="cgnum">&nbsp;</div></td>
						<td><A onclick=getDetails(this) href="#"><div id="voucherHeader_voucherNumber" name="voucherHeader_voucherNumber">&nbsp;</div></A></td>
						<td><div id="voucherHeader_voucherDate" name="voucherHeader_voucherDate">&nbsp;</div></td>
						<td><div id="voucherHeader_fund" name="voucherHeader_fund">&nbsp;</div></td>
						<td name="colETD" id="colETD"><div id="voucherHeader_name" name="voucherHeader_name">&nbsp;</div></td>
						<td><div id="voucherHeader_description" name="voucherHeader_description">&nbsp;</div></td>
						<td align=middle><input type="checkbox" name="voucherHeader_isConfirmed" id="voucherHeader_isConfirmed" onClick="confirm1()" VALUE="ON"></td><!--align=middle><input type="checkbox" name="voucherHeader_isRejected" id="voucherHeader_isRejected" onClick="reject(this)"></td>-->
					    <td><div id="modeofpay" name="modeofpay">&nbsp;</div></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr >
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0" ID="gridSubmit" style="DISPLAY: none">
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="right">
						
						<input type=button class=button onclick=ButtonPress('saveclose') href="#" value="Submit">
						
					</tr>
				</table>
			</td>
		</tr>
	</table>
</table>
</form>
</center>
</body>
</html>