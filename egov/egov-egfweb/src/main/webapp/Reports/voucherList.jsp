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
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">

<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../css/egov.css" type="text/css">

<SCRIPT LANGUAGE="javascript" SRC="../exility/datefunctions.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>


<script language="javascript">


function onloadTasks()
{	
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	window.fundRowIndex=-1;
}

 
function getNameList()
{
	var vouch=document.getElementById('type').value;
	PageManager.DataService.setQueryField('type',vouch);
	PageManager.DataService.callDataService('nameList');  
}


function afterRefreshPage(dc)
{
addSlNo();

//Fills the Name grid with the voucher names for the particular VoucherType 
	var nameList = dc.grids['nameList'];
	if(nameList != null)
	{		
		var comboAccEntity = document.getElementById('nameList');
		clearCombo(comboAccEntity);
		var option;
		for(var i=0; i<nameList.length; i++)
		{
			option = document.createElement('OPTION');
			option.value=nameList[i][0];
			option.text = nameList[i][1];
			comboAccEntity.add(option);			
		}
	}
	
	var grid=dc.grids['getVoucherGrid'];
	if(!grid)
	  return false;
//Checks whether the data is found or not for the selected input values on click of 'search
	if(grid.length==1)
	{						
		document.getElementById('button').style.display='none';
		document.getElementById('getVoucherGrid').style.display='none';
		bootbox.alert("No Data");
		return;
	}
	
}


// Clears the previous namelist values when the voucher type changes
function clearCombo(obj)
{
	try{
		for(var i=obj.length-1;i>=0;i--)
		    obj.remove(i);
	    }catch(err) {}
}


//To Hide the 'cgn' column 
function hideColumn(index)
{
	var table=document.getElementById('getVoucherGrid');
   	for(var i=0;i<table.rows.length;i++)
		table.rows[i].cells[index].style.display="none";
}


//To display the voucher list on click of 'search' button 
function buttonPress()
{
	
	PageManager.DataService.removeQueryField('startDate');
	PageManager.DataService.removeQueryField('endDate');
	PageManager.DataService.removeQueryField('v_no');
	PageManager.DataService.removeQueryField('name');
	PageManager.DataService.removeQueryField('fund_id');
	PageManager.DataService.removeQueryField('type');
	
	if (!PageValidator.validateForm())
				return;
				
	var sDate=document.getElementById("startDate").value;
	var eDate=document.getElementById("endDate").value;
	var vno=document.getElementById('v_no').value;
	
	if(sDate.length==0&&eDate.length==0&&vno==0)
	{
		bootbox.alert("please select voucher no or dates");
		return;
	}
           
	if(vno.length==0&&(sDate.length==0||eDate.length==0))
	{
		bootbox.alert("please select start date and end date");
		return;
	}

	if( compareDate(formatDate6(sDate),formatDate6(eDate)) == -1 )
	{
		bootbox.alert('Start Date cannot be greater than End Date');
		document.getElementById('startDate').value='';
		document.getElementById('endDate').value='';
		document.getElementById('startDate').focus();
		return false;
	}
	
			
	var vouch=document.getElementById('type').value;
	var fund=document.getElementById('fund_id').value;
	var vno=document.getElementById('v_no').value;
	var sdate=document.getElementById('startDate').value;
	var edate=document.getElementById('endDate').value;
	var vhname=document.getElementById('nameList').value;

	PageManager.DataService.setQueryField('type',vouch);
	PageManager.DataService.setQueryField('fund_id',fund);
	if(vhname!='')
		PageManager.DataService.setQueryField('name',vhname);
	
	if(vno!='')
	  PageManager.DataService.setQueryField('v_no',vno); 
	if(sdate!='')  
	  PageManager.DataService.setQueryField('startDate',sdate);
	if(edate!='')  
	  PageManager.DataService.setQueryField('endDate',edate);
	  
	
	PageManager.DataService.callDataService('getVoucherGrid');
	
	var tObj=document.getElementById("getVoucherGrid");
	tObj.style.display="block";
	
	document.getElementById('button').style.display='block';
	hideColumn(2);
	hideColumn(3);
}


function getSel(obj)
{
	var row=PageManager.DataService.getRow(obj);
	window.fundRowIndex = row.rowIndex;
}


//Displays the History Report on click of 'view history' 
function onClickView(obj)
{
	
	if(!isValidUser(2, CookieManager.getCookie('userRole')) ) return false;

	if(window.fundRowIndex<=0)
	{
		bootbox.alert(' Select voucher type ');
			return;
	}
	var table=document.getElementById('getVoucherGrid');
	var type0 = PageManager.DataService.getControlInBranch(table.rows[window.fundRowIndex], "vhId").innerHTML;
	window.open("voucherHistory.jsp?vhId="+type0,"","height=550,width=900,scrollbars=yes,left=20,top=20,status=yes");
}

	
//Displays the Voucher Details	
function getDetails(obj)
{
	var row=PageManager.DataService.getRow(obj);
	var table=document.getElementById('getVoucherGrid');
	var cgn=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"cgNumber").innerHTML;
	window.open("../HTML/VMC/JV_General_VMC.jsp?cgNumber="+cgn+"&showMode="+"view","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	
}
function addSlNo(){
	var slNo=1;
	var tabObj=document.getElementById("getVoucherGrid");
	for(var i=1;i<tabObj.rows.length;i++){
	var obj=PageManager.DataService.getControlInBranch(tabObj.rows[i],"slNo");
	obj.innerHTML=slNo;
	slNo++;
	}
}

</script>
</head>


<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onloadTasks()" >

<form name="voucher">

<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3">

<!-- Voucher selection table -->
<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr>
	<td colspan="6" class="tableheader" valign="center"  ><span id="screenName">VOUCHER SELECTION</span><span class="headerwhite2"></span></td>
</tr>
	
<tr>
	<td colspan="4"></td>
</tr>

<tr width="100%">
	<td width="20%">
		<DIV class="labelcell" align=right>Type<span class="leadon">*</span></DIV> 
	</td>
	<td class="smallfieldcell" width="30%" align="left">
		<select name="type" id="type" class="fieldinput" exilMustEnter="true" exilListSource="voucherTypeList"  onChange="getNameList()"></select>
	</td>
	<td class="labelcell" align=right>Name</td>
	<td class="smallfieldcell" align="left">
		<SELECT class="fieldinput" name="nameList" id="nameList" exilMustEnter="true"></SELECT>
	</td>


</tr>

<tr  width="100%">
	<td>
		<div align="right" valign="center" class="labelcell">Fund<span class="leadon">*</span></div>
	</td>
	<td class="smallfieldcell" align="left" width="40%">
		<SELECT class="fieldinput" id="fund_id" name="fund_id" exilMustEnter="true" exilListSource="fundNameList"></SELECT>
	</td>
	<td><DIV class="labelcell" align=right>Voucher Number</DIV>  </td>
	<td class="smallfieldcell">
		<input name="v_no" id="v_no" exilDataType="exilAlphaNumeric" size=20>
	</td>
</tr>

<tr  width="100%">
	<td><DIV class=labelcell align=right>From Voucher Date</DIV>  </td>
	<td class="smallfieldcell" align="left">
		   <input class="datefieldinput" name="startDate" id="startDate" maxlength="11" size="12" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilPastDate" exilCalendar="true">
	</td>
	<td><DIV class=labelcell align=right>To voucher Date</DIV> </td>
	<td class="smallfieldcell" align="left">
		    <input class="datefieldinput" name="endDate" id="endDate" maxlength="11" size="12" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilPastDate" exilCalendar="true">
	</td>
</tr>

<!-- 'search' and 'close' buttons starts here -->
<tr  width="100%">
	<td align=middle colspan=4> 
		<table border="0" cellpadding="0" cellspacing="0">
		<br>
		<tr>
			<td>
			<input type=button class=button onclick="buttonPress()" href="#" value="Search">
			<input type=button class=button onclick="window.close();" href="#" value="Close">
			</td>
					
		</tr>
		
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		</table>
	  </td>
</tr>

<tr >
	 <td height="25" colspan="6" valign="bottom" class="smalltext"><p class="smalltext">
	 <span class="leadon">*    </span>Mandatory<br>
	- select the from date and to date mandatory or input the voucher number</p>
	  </td>
</tr>
</table>


<!-- Voucher List table which is initially hidden --> 
<table width="100%" border="0" cellpadding="0" cellspacing="0">

<td>
   <table width="80%" border="1" align=center cellpadding="0" cellspacing="0" id="getVoucherGrid" name="getVoucherGrid" style="DISPLAY: none">
	<h2 align=center>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		
	&nbsp;&nbsp;<font color=#3670A7><u style=dash>VOUCHER LIST</u></font></h2>
	 
	<tr>
		<td class="thStlyle"><div align="center" valign="center" >Sl No</div></td>
		<td class="thStlyle" ><div align="center" valign="center" > VoucherDate</div></td>
		<td class="thStlyle" ><div align="center" valign="center" > VoucherId</div></td>
		<td class="thStlyle" ><div align="center" valign="center" > Cgn</div></td>
		<td class="thStlyle"><div align="center" valign="center" > VoucherNumber</div></td>
		<td class="thStlyle"><div align="center" valign="center" > Type</div></td>
		<td class="thStlyle"><div align="center" valign="center" > Name </div></td>
		<td class="thStlyle"><div align="center" valign="center" > Fund</div></td>
		<td class="thStlyle"><div align="center" valign="center" > Select</div></td>
	</tr>
	
	<tr class="labelcell">
		<td class="tdStlyle"><div id="slNo" name="slNo" exilTrimLength="8">&nbsp;</div></td>
	       	<td class="tdStlyle"><div name="vhDate"  id="vhDate" >&nbsp;</div></td>
	       	<td class="tdStlyle"><div name="vhId"  id="vhId" >&nbsp;</div></td>
	       	<td class="tdStlyle"><div name="cgNumber"  id="cgNumber">&nbsp;</div></td>
	       	<td class="tdStlyle"><A onClick="getDetails(this)" href="#"><div id="vhNo" name="vhNo">&nbsp;</div></A></td>
		<td class="tdStlyle"><div name="vhType"  id="vhType">&nbsp;</div></td>
		<td class="tdStlyle"><div name="vhName"  id="vhName">&nbsp;</div></td>
		<td class="tdStlyle"><div name="fundName"  id="fundName">&nbsp;</div></td>
		<td class="tdStlyle"><input align="center" type=radio name="operateOn" onClick="getSel(this);"></td>
	</tr>
   </table>
  
</td>


<td>
   <tr>
   		<td colspan="4"><br></td>
   </tr>
   
   <!-- 'view history' button starts here -->
   <tr >
		<td colspan="4" align="middle"><!-- Buttons Start Here -->
	  	<table border="0" cellpadding="0" cellspacing="0" id="button" style="DISPLAY: none" >
		<tr>		
			<td>
			<input type=button class=button onclick="onClickView();" href="#" value="View History">
			</td>
			
		</tr>
		</table>
		</td>
   </tr>
   
</td>
	
</table>

</div></div></div>
 	</td></tr>
	</table>

</form>

</body>
</html>
