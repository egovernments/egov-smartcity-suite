<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>Approve Voucher</title>
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
		chk=document.getElementById('voucherDateTo');
		if(chk.value.length>0)
		    if(!PageValidator.validateFromTo(chk)) return false;



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

		var voucherType=document.getElementById('voucherType').value;
		if(voucherType)
		PageManager.DataService.setQueryField('voucherType',voucherType);
		
		var fundId=document.getElementById('fund_id').value;
		if(fundId)
		PageManager.DataService.setQueryField('fund_id',fundId);
		var deptId=document.getElementById('department_id').value;
		if(deptId&& deptId.length!=0)
			PageManager.DataService.setQueryField('deptId',deptId);

		var table=document.getElementById('voucherSearchGrid');
		table.style.display="block";

		PageManager.DataService.callDataService('unConfirmedVouchers');
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
	var vcrType = PageManager.DataService.getQueryField('voucherType');
	if(vcrType)	PageManager.DataService.setQueryField('voucherType', vcrType);
	var vcrTo = PageManager.DataService.getQueryField('vcrTo');
	if(vcrTo)	PageManager.DataService.setQueryField('voucherDateTo', vcrTo);
	var vcrConfirm = PageManager.DataService.getQueryField('vcrConfirm');

	if(vcrConfirm) PageManager.DataService.callDataService('unConfirmedVouchers');
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

	/*var vcrFrom = PageManager.DataService.getQueryField('vcrFrom');
	if(vcrFrom)	document.getElementById('voucherDateFrom').value=vcrFrom;
	var vcrTo = PageManager.DataService.getQueryField('vcrTo');
	if(vcrTo)	document.getElementById('voucherDateTo').value=vcrTo;

	PageManager.DataService.removeQueryField('voucherDateFrom');
	PageManager.DataService.removeQueryField('voucherDateTo');*/

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
if(dc.values['serviceID'] == 'unConfirmedVouchers')
	{
		hideColumn(2);
		hideColumn(8);

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
	//var vhid = document.getElementById('voucherHeader_id').value;
	var row=PageManager.DataService.getRow(obj);
	var table=document.getElementById('voucherSearchGrid');
	var vhid=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"voucherHeader_id");
	vhid=vhid.value;
	url = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+vhid;
	window.open(url,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	return;
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
	PageManager.UpdateService.submitForm('confirmVoucher');
	}
else
	alert("Select some bills to submit");
}
function afterUpdateService(dc){
	var dateFrom = document.getElementById('voucherDateFrom').value;
	var dateTo = document.getElementById('voucherDateTo').value;
	window.location = "ConfirmVoucher_VMC.jsp?vcrConfirm=vcrConfirm&vcrFrom="+dateFrom+"&vcrTo="+dateTo;
	return true;
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
<input type="hidden" name="selectedDate" id="selectedDate">
<input type="hidden" name="startFinDate" id="startFinDate">

	<table width="100%" border=0 cellpadding="3" cellspacing="0">
		<table align='center' class="tableStyle" id="table3"> 

		<!--
		<tr  width="100%">
			<td align="right" valign="center" class="normaltext" width="25%"><div align="right"> Type</div></td>
		  	<td class="fieldinput" width="25%">
				<select class="fieldinput" name="type" id="type" width="20" >
				<option value="jvGeneral" selected>JV General</option>
				<option value="jvSupplier">JV Supplier</option>
				<option value="jvContractor">JV Contractor</option>
				<option value="jvSalary">JV Salary</option>
				<option value="jvPayInSlip">JV Pay-In Slip</option>
				<option value="jvContra">JV Contra Journal</option>
				</select>
		  	</td>
		  	<td width="25%"></td>
		  	<td width="25%"></td>
		</tr>
		-->
		<tr >
		<td align="right" valign="center" ><div class="labelcell">Voucher Number From &nbsp;</div></td>
		<td class="fieldcell">
			<input class="fieldinput" name="voucherNumberFrom" id="voucherNumberFrom" exilDataType="exilAnyChar" size=20>
		</td>
		<td align="right" valign="center" class="labelcell"><div align="right"> To &nbsp;</div></td>
		<td class="fieldcell">
			<input class="fieldinput" name="voucherNumberTo" id="voucherNumberTo" exilDataType="exilAnyChar" size=20 >
		</td>
		</tr>
		<tr >
			<td width="25%"   align="right"><div class="labelcell" id="vDate" ><br>Voucher Date From<SPAN class=leadon>* &nbsp;</SPAN></div></td>
		  	<!--<td width="25%" class="fieldinput">
		  		<input class="smallfieldcell" name="voucherDateFrom" id="voucherDateFrom" onkeyup="DateFormat(this,this.value,event,false,'3')" exilMustEnter="true" exilDataType="exilAnyDate"  exilMustEnter="true">
			    <A onclick="fillDate1('voucherDateFrom')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A>
			</td>-->
			<td width="40%" class="smallfieldcell"><INPUT class="smallfieldcell" id="voucherDateFrom" name="voucherDateFrom" size="15" onkeyup="DateFormat
			(this,this.value,event,false,'3')" exilDataType="exilAnyDate"  SIZE="20" exilMustEnter="true" > <A onclick="fillDate1('voucherDateFrom')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A></td>

		 	<td width="25%" align="right" valign="center" class="labelcell"><div align="right"><br>To<SPAN class=leadon>*&nbsp;</SPAN></div></td>
			<!--<td width="25%" class="fieldinput">
				<input class="smallfieldcell" name="voucherDateTo" id="voucherDateTo" onkeyup="DateFormat(this,this.value,event,false,'3')" exilMustEnter="true" exilDataType="exilAnyDate" exilMustEnter="true">
			    <A onclick="fillDate1('voucherDateTo')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A>
			</td>-->
			<td class="smallfieldcell" width="40%"><INPUT class="dateFieldInput"  id="voucherDateTo" name="voucherDateTo" size="15" onkeyup="DateFormat
			(this,this.value,event,false,'3')" exilDataType="exilAnyDate"  SIZE="20" exilMustEnter="true" > <A onclick="fillDate1('voucherDateTo')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A></td>
		</tr>
		<tr>
			<td align="right"><div  valign="center"class="labelcell" ><br>Voucher Type <SPAN class=leadon>* &nbsp;</SPAN></div></td>
			<td class="smallfieldcell">
				<SELECT class="fieldinput" id="voucherType" name="voucherType" exilListSource="voucherTypeList" exilMustEnter="true"></SELECT>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr >
			<td align="right"><div  valign="center"class="labelcell" ><br>Fund &nbsp;</div></td>
			<td class="smallfieldcell">
				<SELECT class="fieldinput" id="fund_id" name="fund_id" exilListSource="fundNameList"></SELECT>
			</td>
			<td align="right" valign="center" ><div align="right" class="labelcell" ><br>Department &nbsp;</div>
	</td>
	<td  class="smallfieldcell">
	<SELECT name="department_id" id="department_id"
	 class="fieldinput">
	</SELECT>

	</td> 
		</tr>
		<tr >
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0" ID="Table1">
					<tr>
						<td align="right">
						
						<input type=button class=button onclick=getGrid() href="#"value="Search">
						
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" id=TABLE1>
		<tr >
			<td>
				<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" name="voucherSearchGrid" id="voucherSearchGrid" style="DISPLAY: none">
					<tr class="tableheader">
						<td width="5%" height="34" class="thStlyle"><div align="center" class="thStlyle">Sl No</div></td>
						<td width="12%" class="thStlyle"><div align="center" id="gridA">CG Number</div></td>
						<td width="10%" class="thStlyle"><div align="center" id="gridA">CG NumberNew</div></td>
						<td width="20%" class="thStlyle"><div align="center" id="gridB">Voucher Number</div></td>
						<td width="15%" class="thStlyle"><div align="center" id="gridC">Voucher Date</div></td>
						<td width="20%" class="thStlyle"><div align="center" id="gridC">Name Type</div></td>
						<td class="thStlyle" ><div align="center" id="gridE">Narration</div></td>
						<td width="8%" class="thStlyle"><div align="center" id="gridF">Approve</div><div align="center">All <input type="checkbox" name="isConfirmedAll" id="isConfirmedAll" onClick="confirmAll()"></div></td><!--<td width="10%" class="columnheader"><div align="center" id="gridF">Reject</div><div align="center">All <input type="checkbox" name="isRejectedAll" id="isRejectedAll" onClick="rejectAll(this)"></div></td>-->
				  	    <td width="1%" class="thStlyle"><div align="center" id="gridG">ModeOfPay</div></td>
				  	</tr>
				 	<tr class="tdStlyle">
						<td><div id="slNo" name="slNo">&nbsp;</div></td>
						<td><div id="voucherHeader_cgn" name="voucherHeader_cgn"></div><input size=1 type="hidden" name="voucherHeader_id" id="voucherHeader_id"></td>
						<td><div id="cgnum" name="cgnum">&nbsp;</div></td>
						<td><A onclick=getDetails(this) href="#"><div id="voucherHeader_voucherNumber" name="voucherHeader_voucherNumber">&nbsp;</div></A></td>
						<td><div id="voucherHeader_voucherDate" name="voucherHeader_voucherDate">&nbsp;</div></td>
						<td name="colETD" id="colETD"><div id="voucherHeader_name" name="voucherHeader_name">&nbsp;</div></td>
						<td style="width:200px;word-wrap:break-word"><div id="voucherHeader_description" name="voucherHeader_description" >&nbsp;</div></td>
						<td align=middle><input type="checkbox" name="voucherHeader_isConfirmed" id="voucherHeader_isConfirmed" onClick="confirm1()"></td><!--align=middle><input type="checkbox" name="voucherHeader_isRejected" id="voucherHeader_isRejected" onClick="reject(this)"></td>-->
					     <td><div id="modeofpay" name="modeofpay">&nbsp;</div></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr class="row1">
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
</table>

</form>
</center>
</body>
</html>
