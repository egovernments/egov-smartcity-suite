<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head>
<title>
	View Scheme Details
</title>
<SCRIPT LANGUAGE="javascript">
var cWind;         // temporary window reference
var count=1;      // Counter which gives number of child windows opened
var cWindows=new Object();    //HashMap where we store child window
var mode="";
function onloadTasks()
		 {
			 PageValidator.addCalendars();
			var tabRow=document.getElementById('mainTable');
			//tabRow.rows[2].style.display="none";
			PageManager.ListService.callListService();
			mode = PageManager.DataService.getQueryField('showMode');

		 }

function fillDate1(objName)
	{
		PageValidator.showCalendar('selectedDate');
		document.getElementById(objName).value=document.getElementById('selectedDate').value;
		document.getElementById('selectedDate').value = "";
	}

function ModeChange(){

	/*Added by siddhu*/
	var tabRow=document.getElementById('mainTable');
	
	var table=document.getElementById('searchDetailGrid');
		table.style.display="none";
	//for(var i=b.childNodes.length-1; i>=0; i--) b.removeChild(b.childNodes[i]);

	// var data=a.options[a.selectedIndex].text;
	// b.style.visibility='hidden';
	// c.style.visibility='hidden';
	//tabRow.rows[2].style.display="none";

	

}
function ButtonPress()
{
	//alert("mode "+mode);

	if(!PageValidator.validateForm())	return false;
	/*added by siddhu*/
	PageManager.DataService.removeQueryField('fundID');
	PageManager.DataService.removeQueryField('voucherDateFrom');
	PageManager.DataService.removeQueryField('voucherDateTo');
	hideColumn(1);
			var type=null;
			var typeObj=document.getElementById('fund_id');
			if(typeObj.selectedIndex==-1)return;
			type=typeObj.options[typeObj.selectedIndex].value;
			paymentType=type;
				var voucherDateFrom=document.getElementById('voucherDateFrom').value;
				if(voucherDateFrom && voucherDateFrom.length!=0)
					PageManager.DataService.setQueryField('voucherDateFrom',formatDate1(voucherDateFrom));

				var voucherDateTo=document.getElementById('voucherDateTo').value;
				if(voucherDateTo && voucherDateTo.length!=0)
				PageManager.DataService.setQueryField('voucherDateTo',formatDate1(voucherDateTo));
				
				var fundID=document.getElementById('fund_id').value;
				if(fundID&& fundID.length!=0)
					PageManager.DataService.setQueryField('fundID',fundID);


				if( compareDate(formatDate6(voucherDateFrom),formatDate6(voucherDateTo)) == -1 )
				{
					alert('Start Date cannot be greater than End Date');
					document.getElementById('voucherDateFrom').focus();
					return false;
				}
			//if(type && (type=="Bank Payment"))
			//{
				/*if(mode=="modify")
				{
				var stat=0;
				PageManager.DataService.setQueryField('reverseNum',stat);
				var con=1;
				PageManager.DataService.setQueryField('confirmNum',con);

				}
				if(mode=="edit")
				{
				var stat=0;
				PageManager.DataService.setQueryField('reverseNum',stat);
				var con=0;
				PageManager.DataService.setQueryField('confirmNum',con);
				}*/
				//alert(fundID);
				PageManager.DataService.setQueryField('fundID',fundID);
				PageManager.DataService.callDataService('schemeSearch');

			//}
			
			var table=document.getElementById('searchDetailGrid');
			table.style.display="block";

}

function beforeRefreshPage(dc){
		var tabObj=dc.grids['searchDetailGrid'];
		if(!tabObj)		return false;
		if(tabObj.length<2){
			alert("No Data");
		}
		/*for(var i=1;i<tabObj.length;i++)
		{
				tabObj[i][2]=formatDate(tabObj[i][2]);
				alert(tabObj[i][2]);
		}*/

		return true;
}
/*function formatDate(dateText){
	dateText=dateText.substring(0,10);
	var dateArray= dateText.split('-');
	var newDate=dateArray[2]+"-"+dateArray[1]+"-"+dateArray[0];
	return newDate;
}*/


function changeColor(){
	var table=document.getElementById('searchDetailGrid');
		for(var i=1;i<table.rows.length;i++)
		{
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
	//changeColor();
	var voucherDateFrom=document.getElementById('voucherDateFrom').value;
			var voucherDateTo=document.getElementById('voucherDateTo').value;
			if(voucherDateFrom && voucherDateFrom.length!=0)
					{
					var VouDateFrom=dc.values['voucherDateFrom'];
					//alert("VouDateFrom"+formatDateToDDMMYYYY5(VouDateFrom)+" VouDateTo"+formatDateToDDMMYYYY5(VouDateTo));
					VouDateFrom=formatDateToDDMMYYYY5(VouDateFrom);
					document.getElementById('voucherDateFrom').value=VouDateFrom;
					}

				if(voucherDateTo && voucherDateTo.length!=0)
				{
					var VouDateTo=dc.values['voucherDateTo'];
					VouDateTo=formatDateToDDMMYYYY5(VouDateTo);
					document.getElementById('voucherDateTo').value=VouDateTo;
				}
	hideColumn(1);
	return true;
}
function addSlNo(){
	var slNo=1;
	var tabObj=document.getElementById("searchDetailGrid");
	for(var i=1;i<tabObj.rows.length;i++){
	var obj=PageManager.DataService.getControlInBranch(tabObj.rows[i],"slNo");
	obj.innerHTML=slNo;
	slNo++;
	}
}

function getDetails(obj){
	var row=PageManager.DataService.getRow(obj);
	var table=document.getElementById('searchDetailGrid');
	var cgn1=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"cgNumber");
	var schemecodes=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"schemeCode");
	cgn1=cgn1.innerHTML;
	var cgn2=cgn1.substring(0,3);
	var typeObj=document.getElementById('fund_id');
	type=typeObj.options[typeObj.selectedIndex].value;
	
	mode = PageManager.DataService.getQueryField('showMode');
		cWind=window.open("AddScheme.jsp?code="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");

	cWindows[count++]=cWind;
}
function hideColumn(index){
	var table=document.getElementById('searchDetailGrid');
   	for(var i=0;i<table.rows.length;i++){
		table.rows[i].cells[index].style.display="none";
   	}
}

</script>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onloadTasks()" onbeforeunload="closeChilds(cWindows,count)" >
<center>
<br>
<form name="schemesearch">
<input type="hidden" name="selectedDate" id="selectedDate">
<input type="hidden" name="schemename" id="schemename">

	<table align='center' class="tableStyle" id="table3"> 
<!-- <table width="100%" border=0 cellpadding="3" cellspacing="0" id="mainTable" align="center"> -->
	<tr>
			<td width="25%"><div align="center" valign="center" class="labelcell">Fund <span class="leadon">*</span></div></td>
			<td align="left" width="25%" class="smallfieldcell">
				<SELECT name="fund_id" id="fund_id" class="fieldinput" exilListSource="fundNameList" exilMustEnter="true" onChange="ModeChange()"></SELECT>
			</td>
			<td width="25%"><div align="right" valign="center" class="normaltext">&nbsp;</div></td>
			<td width="25%"><div align="right" valign="center" class="normaltext">&nbsp;</div></td>
		</tr>
	
	<tr>
		<td width="25%"><div align="center" class="labelcell"><!--<bean:message key="RecptSearch.voucherDateFrom"/>--><br>
		From Date</div></td>
		<td class="smallfieldcell" align="left" width="25%">
			<input class="datefieldinput"  name="voucherDateFrom"  id="voucherDateFrom" onkeyup="DateFormat(this,this.value,event,false,'3')"  >
			<A onclick="fillDate1('voucherDateFrom')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../images/calendar.gif"></A>
		</td>
		<td width="25%"><div align="right" class="labelcell"><!--<bean:message key="RecptSearch.voucherDateTo"/>-->
			To Date</div></td>
		<td class="smallfieldcell" width="25%">
			<input class="datefieldinput" name="voucherDateTo"  id="voucherDateTo" onkeyup="DateFormat(this,this.value,event,false,'3')" >
			<A onclick="fillDate1('voucherDateTo')" tabIndex=-1 href=#><IMG tabIndex=-1 src="../images/calendar.gif"></A>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="middle">
			<table border="0" cellpadding="0" cellspacing="0" >
				<tr>
					<td align="right">
					<input type=button class=button href="#" onClick="ButtonPress()" value="Search"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table width="100%" border="1" cellpadding="0" cellspacing="0" name="searchDetailGrid" id="searchDetailGrid" style="DISPLAY:none">
	<tr class="tableheader">
		<td width="10%" height="34" class="thStlyle"><div align="center">
		<bean:message key="RecptSearch.slNo"/><br>
Sl No</div></td>
<td width="10%" height="34" class="thStlyle"><div align="center" ><bean:message key="schemesearch.schemeId"/><br>
Scheme ID</div></td>
		<td width="15%" class="thStlyle"><div align="center"><bean:message key="schemesearch.schemeCode"/><br>
Scheme Code</div></td>
		<td width="15%" class="thStlyle"><div align="center"><bean:message key="schemesearch.schemeName"/><br>
Scheme Name</div></td>
		<td width="20%" class="thStlyle"><div align="center"><bean:message key="schemesearch.startDate"/><br>
Start Date</div></td>
		<td width="20%" class="thStlyle"><div align="center"><bean:message key="schemesearch.endDate"/><br>
End Date </div></td>
		<td width="20%" class="thStlyle"><div align="center"><bean:message key="schemesearch.isActive"/><br>
Is Active</div></td>
		

	 </tr>

	<tr class="tdStlyle">
		<td><div id="slNo" name="slNo" exilTrimLength="8">&nbsp;</div></td>
		<td><div id="cgNumber" name="cgNumber"  exilTrimLength="10">&nbsp;</div></td>
		<td><A onClick="getDetails(this)" href="#"><div id="schemeCode" name="schemeCode"  exilTrimLength="10">&nbsp;</div></A></td>
		<td><div id="sName" name="sName">&nbsp;</div></td>
		<td align="right"><div id="fromDate" name="fromDate" exilTrimLength="15">&nbsp;</div></td>
		<td align="right"><div id="toDate" name="toDate" exilTrimLength="15">&nbsp;</div></td>
		<td align="right"><div id="status" name="status" exilTrimLength="10">&nbsp;</div></td>
		
	</tr>
</table>
</form>
</center>
</body>
</html>
