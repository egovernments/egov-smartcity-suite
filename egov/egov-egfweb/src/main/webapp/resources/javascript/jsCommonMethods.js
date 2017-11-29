/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
/*
---------------------------------------------------------------------
 File Name	:  jsCommonMethods.js
 Author		:  Exilant
This is just for test
---------------------------------------------------------------------
*/
var g_maxOfNarration=250;
var gMonthBag=["JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"];
function topmenu(){
	
	
	document.write("<table bgcolor='#30DAE6' width='100%' height='46'  border='0'>");
	document.write("<tr><td width='60%' height=52 valign='bottom' nowrap  class='banner'>");
	document.write("<span class='style5'><div name='title' id='title'></span></td>");
	document.write("<td width='60%'  height=52 valign='bottom' align='left' class='normaltext'><span><div name='username' id='username'>User:&nbsp;</span></td>");
	document.write("<td width='30%' height=52 valign='bottom' nowrap class='banner'><a class='footerlocallink'  onClick='gotoHomePage()' href='#'>Home</a>&nbsp;&nbsp;");
	/*document.write("<a  class='footerlocallink' href='#'>My Preferences</a>&nbsp; <a class='footerlocallink' href='#'>Administration</a>&nbsp;<span class='footerlocalseperator'>|</span>");*/
	document.write("<a class='footerlocallink' href='#'>Help</a>&nbsp; <a class='footerlocallink' onClick='changePassword()' href='#'>ChangePassword</a>&nbsp;<a class='footerlocallink' onClick='logOut()' href='#'>Logout</a>");
	document.write("<td width='9%'><div align='right'><input name='imageField2' type='image' src='/egi/resources/erp2/images/eGovLogo.jpg' align='top' width='80' height='60' border='0'></div></td></tr>");
	document.write("</table>");
	
}

function leftmenu(){
	document.write("<tr><td ><a class='tabsback' href='#' class='tablink'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Chart Of Accounts</a></td></tr>");
	document.write("<tr><td ><a class='tabsback' href='#' class='tablink'>&nbsp;&nbsp;&nbsp;Master</a></td></tr>");
	document.write("<tr><td><a class='tabsback' href='#' class='tablink'>&nbsp;&nbsp;&nbsp;	Transactions</a></td></tr>");
	document.write("<tr><td><a class='tabsback' href='#' class='tablink'>&nbsp;&nbsp;&nbsp;Budget </a></td></tr>");
	document.write("<tr><td><a class='tabsback' href='#' class='tablink'>&nbsp;&nbsp;&nbsp;Report</a></td></tr>");												
	document.write("</table>");
}


function bottommenu()
{
	document.write("<table cellspacing=0 cellpadding=0 width='100%' border=0>");
	document.write("<tr><td background='/egi/resources/erp2/images/box_02.gif' colspan='3'><img src='/egi/resources/erp2/images/spacer.gif'></td></tr>");
	document.write("<tr><td width='1%' rowspan='2'><img src='/egi/resources/erp2/images/spacer.gif' border='0'></td><td width='84%' height=22 rowspan='2'>");
	document.write("&nbsp;&nbsp;<span class='footerglobaltext'>&copy; Copyright 2004. All rights reserved.</span></td>");
	document.write("<td width='15%' height=40><input name='imageField' type='image' src='/egi/resources/erp2/images/Exilant.jpg' width='113' height='18' border='0'></td>");
	document.write("</tr>");
	document.write("</table>");
}

function gotoHomePage(){	
	window.parent.location = "eGov.htm";
}

function changePassword()
{
	//top.location = "../administration/rjbac/user/changePassword.jsp";
	window.open("../administration/rjbac/user/changePassword.jsp","","height=650,width=900,scrollbars=yes,left=0,top=0,status=yes");
}
function logOut(){
	delCookie('currentUserId');
	delCookie('userRole');

	top.location = "../logout.jsp";
}

function getCookie(NameOfCookie){  
	if (document.cookie.length > 0) {
		begin = document.cookie.indexOf(NameOfCookie+"="); 
    		if (begin != -1) {
    			begin += NameOfCookie.length+1; 
      			end = document.cookie.indexOf(";", begin);
      		
      			if (end == -1) 
      				end = document.cookie.length;
      			
      			return unescape(document.cookie.substring(begin, end));
      		} 
 	}
	return null; 
}

 
function delCookie (NameOfCookie) {  
	if (getCookie(NameOfCookie)) {
		document.cookie = NameOfCookie + "=" +
		"; expires=Thu, 01-Jan-70 00:00:01 GMT";
	}
}

function setCookie(NameOfCookie, value, expiredays) {  
	var ExpireDate = new Date ();
	ExpireDate.setTime(ExpireDate.getTime() + (expiredays * 24 * 3600 * 1000));
  	document.cookie = NameOfCookie + "=" + escape(value) + 
  		((expiredays == null) ? "" : "; expires=" + ExpireDate.toGMTString());
}

function delCookies() {
	delCookie('expChartOfAccounts');
	delCookie('expTransaction');
	delCookie('expMaster');
	delCookie('expUserAccess');
	if (getCookie('chkFlag') != null) { delCookie('chkFlag'); }
	if (getCookie('homePage') != null) { delCookie('homePage'); }
}
 
function displayMenu()
 {
	if (getCookie('expChartOfAccounts') == null) {setCookie('expChartOfAccounts',"false",1);}
	if (getCookie('expTransaction') == null) {setCookie('expTransaction',"false",1);}
	if (getCookie('expMaster') == null) {setCookie('expMaster',"false",1);}
	if (getCookie('expUserAccess') == null) {setCookie('expUserAccess',"false",1);}
	if (getCookie('chkFlag') == null) {setCookie('chkFlag',"0",1);}
 	menuMaster(0,0,0,0);
}
 
function navigateMenu(chkFlag) {
		if ( chkFlag == 1 )	{
		setCookie('chkFlag',"1",1);
		window.location.href = 	"118 IP Store.htm";
		} 
		if ( chkFlag == 2 )	{
		setCookie('chkFlag',"2",1);
		//window.location.href="101 Project Search.htm";				
		window.open("jou_vou.htm","mainFrame");	
		
		//document.mainFrame.location.href="jou_vou.htm";				
		
		} 
		if ( chkFlag == 3 )	{
		setCookie('chkFlag',"3",1);
		window.open("jou_vou.htm","mainFrame");
		} 
		if ( chkFlag == 4 )	{
		setCookie('chkFlag',"4",1);
		window.open("jou_vou.htm","mainFrame");
		} 
		if ( chkFlag == 5 )	{
		setCookie('chkFlag',"5",1);
		window.open("jou_vou.htm","mainFrame");
		} 
		if ( chkFlag == 6 )	{
		setCookie('chkFlag',"6",1);
		window.open("jou_vou.htm","mainFrame");
		} 
		if ( chkFlag == 7 )	{
		setCookie('chkFlag',"7",1);
		window.open("jou_vou.htm","mainFrame");
		}
		if ( chkFlag == 8 )	{
		setCookie('chkFlag',"8",1);
		window.location.href="#";
		} 		
 }
								
 function menuMaster(intFlag1, intFlag2, intFlag3,intFlag4)
 {

	var menuCount = 0;
	
	var t=document.getElementById('1');

	if (intFlag1 == 1)
	{
		if (getCookie('expChartOfAccounts') == "true" )
		 setCookie('expChartOfAccounts',"false",1);
		else
		 setCookie('expChartOfAccounts',"true",1);
	}
	else if (intFlag2 == 1)
	{
		if (getCookie('expTransaction') == "true" )
		 setCookie('expTransaction',"false",1);
		else
		 setCookie('expTransaction',"true",1);
	}
	else if (intFlag3 == 1)
	{
		if (getCookie('expMaster') == "true" )
		 setCookie('expMaster',"false",1);
		else
		 setCookie('expMaster',"true",1);
	}
	else if (intFlag4 == 1)
	{
		if (getCookie('expUserAccess') == "true" )
		 setCookie('expUserAccess',"false",1);
		else
		 setCookie('expUserAccess',"true",1);
	}

		clear();
	
	   //bootbox.alert(getCookie('expChartOfAccounts'));bootbox.alert(getCookie('expTransaction'));bootbox.alert(getCookie('expUserAccess'));bootbox.alert(getCookie('expMaster'));
		
//bootbox.alert('1');																	// Master Block
			x=t.insertRow(menuCount);
			
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			
			y.innerHTML='<a onClick="menuMaster(1,0,0,0)" href="#" class="normaltext">&nbsp;&nbsp;&nbsp;Chart Of Accounts</a>';
			
			y.id = 'menutabsback';
			
			
		
		
		
//bootbox.alert('2');																	// Transaction Bloack
			x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			y.innerHTML='<a onClick="menuMaster(0,1,0,0)" href="#" class="normaltext">&nbsp;&nbsp;&nbsp;Transaction</a>';
			y.id = 'menutabsback';
			
		if( getCookie('expTransaction') == "true")
		{
			var x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			

			if (getCookie('chkFlag') != 2 ) 
				{
					y.innerHTML='<a onClick="navigateMenu(2);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Journal Voucher</a>';
				}
			else
				{
					y.innerHTML=y.innerHTML='<a onClick="navigateMenu(2);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Journal Voucher</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="imageField" type="image" src="/egi/resources/erp2/images/check.gif" width="12" height="12" border="0">';
				}
				y.id = 'menutabsback';
			
			var x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			y.id = 'menutabsback';
			
			
			if (getCookie('chkFlag') != 3 ) 
				{
					y.innerHTML='<a onClick="navigateMenu(3);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bank Receipt</a>';
				}
			else
				{
					y.innerHTML=y.innerHTML='<a onClick="navigateMenu(3);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bank Receipt</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="imageField" type="image" src="/egi/resources/erp2/images/check.gif" width="12" height="12" border="0">';
				}
			
			
		}
	
//bootbox.alert('3');																	// Master Block
			x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			y.innerHTML='<a onClick="menuMaster(0,0,1,0)" href="#" class="normaltext">&nbsp;&nbsp;&nbsp;Master</a>';
			y.id = 'menutabsback';
			
		if( getCookie('expMaster') == "true")
		{
			//bootbox.alert('in child master block');
			var x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			y.id = 'menutabsback';
			
			if (getCookie('chkFlag') != 4 ) 
				{
					y.innerHTML='<a onClick="navigateMenu(4);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bank</a>';
				}
			else
				{
					y.innerHTML=y.innerHTML='<a onClick="navigateMenu(4);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bank</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="imageField" type="image" src="/egi/resources/erp2/images/check.gif" width="12" height="12" border="0">';
				}
			
			
			var x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			y.id = 'menutabsback';
			
			if (getCookie('chkFlag') != 5 ) 
				{
					y.innerHTML='<a onClick="navigateMenu(5);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;User</a>';
				}
			else
				{
					y.innerHTML=y.innerHTML='<a onClick="navigateMenu(5);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;User</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="imageField" type="image" src="/egi/resources/erp2/images/check.gif" width="12" height="12" border="0">';
				}
				
			
			var x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			y.id = 'menutabsback';
			
			if (getCookie('chkFlag') != 6 ) 
				{
					y.innerHTML='<a onClick="navigateMenu(6);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Role</a>';
				}
			else
				{
					y.innerHTML=y.innerHTML='<a onClick="navigateMenu(6);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Role</a>&nbsp;&nbsp;&nbsp;&nbsp;<input name="imageField" type="image" src="/egi/resources/erp2/images/check.gif" width="12" height="12" border="0">';
				}
			

		}		
		
//bootbox.alert('4');																			// Employee Block	
			x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			y.innerHTML='<a onClick="menuMaster(0,0,0,1)" href="#" class="normaltext">&nbsp;&nbsp;&nbsp;Employee</a>';
			y.id = 'menutabsback';
			
		if( getCookie('expUserAccess') == "true")
		{
			//bootbox.alert('in child master block');
			var x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			y.id = 'menutabsback';
			
			if (getCookie('chkFlag') != 7 ) 
				{
					y.innerHTML='<a onClick="navigateMenu(7);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Search</a>';
				}
			else
				{
					y.innerHTML=y.innerHTML='<a onClick="navigateMenu(7);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Search</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="imageField" type="image" src="/egi/resources/erp2/images/check.gif" width="12" height="12" border="0">';
				}
			
			//y.innerHTML='<a href="301 Employee Search.htm" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Search</a>';
			
			var x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			y.id = 'menutabsback';

			if (getCookie('chkFlag') != 8 ) 
				{
					y.innerHTML='<a onClick="navigateMenu(8);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Resume Upload</a>';
				}
			else
				{
					y.innerHTML=y.innerHTML='<a onClick="navigateMenu(8);" href="#" class="tablink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Resume Upload</a>&nbsp;&nbsp;&nbsp;<input name="imageField" type="image" src="/egi/resources/erp2/images/check.gif" width="12" height="12" border="0">';
				}


}		
		
//bootbox.alert('5');																		// Report Block
			x=t.insertRow(menuCount);
			menuCount = menuCount + 1;
			var y=x.insertCell(0);
			y.innerHTML='<a onClick="menuMaster(0,0,0,0)" href="#" class="normaltext">&nbsp;&nbsp;&nbsp;Report</a>';
			y.id = 'menutabsback';			
 }

 function clear()
 {
   for(i=0;i<30;i++)
   	{
		try
		{
      		document.getElementById('1').deleteRow(0)
      	}
		catch(nn){}
   }
 }
 
 function navigateTo(destPage)
 {
	 if ( destPage == 'Project New' )
	 {
	 	window.location.href = '103 Project Add Blank.htm';
	 }
 }
function DataTrim(str)
{
	//Left trim the passed string
	while ((str.substring(0,1) == ' ') || (str.substring(0,1) == '\r'))
         str = str.substring(1,str.length);
  	//right trim the passed string
    while ((str.substring(str.length-1,str.length) == ' ') || (str.substring(str.length-1,str.length) == '\r'))
         str = str.substring(0,str.length-1);
    return str;
}
function insertDates(id)
		 {
				var d=new Date();
				var control=document.getElementsByName(id);
				var dstr=d.toString();
				var mon=dstr.substring(4,7);
				var dat=dstr.substring(8,10);
				var year=dstr.substring(28,34);
				for(var i=0;i<control.length;i++)
					control[i].value=DataTrim(dat)+"-"+DataTrim(mon)+"-"+DataTrim(year);
		 }
		 
		 function insertDates1(id)
		 {
				var d=new Date();
				var control=document.getElementsByName(id);
				var dstr=d.toString();
				var mon=dstr.substring(4,7);
				var dat=dstr.substring(8,10);
				var year=dstr.substring(28,34);
				for(var i=0;i<control.length;i++)
					control[i].value=DataTrim(dat)+"/"+DataTrim(mon)+"/"+DataTrim(year);
		 }
/**Pushpendra Singh 06-Jan-2005
* arguments 
* table ID containing Grid
* and field names of table to be checked for non empty as array of string*/
function addNewRow(TableID, checkFields1)
{	
	var e = window.event;	
	if(e.keyCode == 113 ) 
	{
		var index, inIndex, rowLength;
		var table = document.getElementById(TableID);
		if(!table || !table.rows) return false;			
		
		var checkFields = checkFields1.split(",");							
		rowLength = table.rows.length;
		var controlObj;
		PageManager.DataService.addNewRow(TableID);
		PageManager.DataService.getControlInBranch(table.rows[rowLength],checkFields[0]).focus();
		return true;
	}
	return false;
}
		
//This function is to add new row 
function addNewRowFromButton(TableID, checkFields1)
{
	var e = window.event;	
	var index, inIndex, rowLength;
	var table = document.getElementById(TableID);
	
	if(!table || !table.rows) return false;			

	var checkFields = checkFields1.split(",");							
	rowLength = table.rows.length;
	var controlObj;
	PageManager.DataService.addNewRow(TableID);
	PageManager.DataService.getControlInBranch(table.rows[rowLength],checkFields[0]).focus();
	return true;
	
}

//This function will recalculate the debit and credit amount on Deleting a row  
function colAllTotalTwoCol(objname1,objname2,target1,target2)
{	
	var a = document.getElementsByName(objname1);
	count = 0;
	for(var i = 0; i<=a.length -1;i++) if(a[i].value)
		count += parseFloat(a[i].value);
		count=Math.round(count*100)/100 ;
		document.getElementById(target1).value = count;

	var b = document.getElementsByName(objname2);
	count = 0;
	for(var i = 0; i<=b.length -1;i++) if(b[i].value)
		count += parseFloat(b[i].value);
		count=Math.round(count*100)/100 ;
		document.getElementById(target2).value = count;
 }
 
 function colAllTotal(objname1,objname2,target1,target2)
{
colAllTotalTwoCol(objname1,objname2,target1,target2);
}

 //This function will recalculate the debit or credit amount on Deleting a row  
 function colAllTotalSingleCol(objname1,target1)
 {	
 	var a = document.getElementsByName(objname1);
 	count = 0;
 	for(var i = 0; i<=a.length -1;i++) if(a[i].value)
 		count += parseFloat(a[i].value);
 		count=Math.round(count*100)/100 ;
 		document.getElementById(target1).value = count;
 }
  
//This function will delete the row on Click of Delete Row
function deleteRowTable(obj)
{
	var tbl= document.getElementById(obj); 
	var isSelected=false;
	for(var i=1;i<tbl.rows.length;i++)
	{
		if(tbl.rows[i].cells[0].className == "rowRev")
		{
			isSelected=true;
			if(i>1)
			{
			   tbl.deleteRow(i);
			  //colAllTotal('voucherDetail_debitAmount','voucherDetail_creditAmount','debitTotal','creditTotal');
			}
			else
			{			 
				bootbox.alert("You cannot delete this row"); 
			 return false;
			}
		}
	 } 
	 if(!isSelected)
	 {
		 bootbox.alert("Select the row to be deleted.");
	 return;
	 }
   
}

//This function is to highlight the row thats to be deleted.
function changeColor(currObj,obj)
{
	var table=document.getElementById(obj);
	
	for(var i=1;i<table.rows.length;i++)
	{
		rowSel=true;
		getControlInBranch(table.rows[i],'selectTd').className='normaltext';
		table.rows[i].cells[0].className='normaltext';
	}
	currObj.className='rowRev';
	getControlInBranch(table.rows[PageManager.DataService.getRow(currObj).rowIndex],'selectTd').className='rowRev';
}

/**Pushpendra Singh 15-Feb-2005
* arguments 
* formId, id of the form to Reset
* and focusToId, id of control to be focused onto after reset */
function ResetForm(formId, focusToId){
	var pass=true
	var first=-1	
	
	var frm = document.getElementById(formId);
	if (frm == null) return false;
		for (i=0;i<frm.length;i++){
		
			var tempobj=frm.elements[i]			
			
			if(tempobj.name == "voucherHeader_cgn"){/* do nothing */}			
			else if (tempobj.type=="text"){				
				eval(tempobj.value="")
				if (first==-1) {first=i}
			}
			else if (tempobj.type=="checkbox") {
				eval(tempobj.checked=0)
				if (first==-1) {first=i}
			}
			else if (tempobj.col!="") {
				eval(tempobj.value="")
				if (first==-1) {first=i}
			}
		}	
	
	var focusTo = document.getElementById(focusToId);
	
	if(focusTo == null){
		if(frm.length > 0)
			frm.elemets[0].focus();
	}
	else
		focusTo.focus();
		
	return false
}

 function addSlNo(id)
		{
			
			//bootbox.alert('sdfkj')
			
			var tab=document.getElementById(id);
			var lastRow=tab.rows.length;
			var last;
			var control=PageManager.DataService.getControlInBranch(tab.rows[lastRow-2].cells[0],"slNo");
			control.disabled=false;
			var lastData=control.value;
			control.disabled=true;
			if(isNaN(lastData))
			   last=1;	
			else
		  	   last=parseInt(lastData)+1;

			tab.rows[lastRow-1].cells[0].childNodes[0].value=last;
		}


function getSelectedRadioObject(buttonGroup) {
   
   if (buttonGroup[0]) { // if the button group is an array (one button is not an array)
      for (var i=0; i<buttonGroup.length; i++) {
         if (buttonGroup[i].checked) {
            return buttonGroup[i];
         }
      }
   } else {
      if (buttonGroup.checked) { return buttonGroup; } // if the one button is checked, return zero
   }
   
   return null;
}

function getSelectedRadio(buttonGroup) {
   // returns the array number of the selected radio button or -1 if no button is selected
   if (buttonGroup[0]) { // if the button group is an array (one button is not an array)
      for (var i=0; i<buttonGroup.length; i++) {
         if (buttonGroup[i].checked) {
            return i
         }
      }
   } else {
      if (buttonGroup.checked) { return 0; } // if the one button is checked, return zero
   }
   // if we get to this point, no radio button is selected
   return -1;
} // Ends the "getSelectedRadio" function

function getSelectedRadioValue(buttonGroup) {
   // returns the value of the selected radio button or "" if no button is selected
   var i = getSelectedRadio(buttonGroup);
   if (i == -1) {
      return "";
   } else {
      if (buttonGroup[i]) { // Make sure the button group is an array (not just one button)
         return buttonGroup[i].value;
      } else { // The button group is just the one button, and it is checked
         return buttonGroup.value;
      }
   }
} // Ends the "getSelectedRadioValue
 		function checkNarration(ctrlName)
		 {
				var narration=document.getElementById(ctrlName);
				if(narration.value.length>g_maxOfNarration){
					bootbox.alert("Narration Cannot Be More Than "+g_maxOfNarration+" Characters");
				  narration.focus();
				  return false;
				}
				return true;							
		 }	

function chkNarration(ctrlName)
{
	if(event.keyCode !=8 && event.keyCode !=46)
	{
		var narration=document.getElementById(ctrlName);
		if(narration.value.length>=g_maxOfNarration){
			bootbox.alert("Narration Cannot Be More Than "+g_maxOfNarration+" Characters");
		 event.returnValue=false;
		}
	}
return true;	
}
function redirectHTML(type, qString)
{
	switch(type.toLowerCase()) {
		case "property tax feild":window.location="PT_Field.htm"+qString;break;
		case "property tax office":window.location="PT_Office.htm"+qString;break;
		case "property tax bank":window.location="PT_Bank.htm"+qString;break;		
		case "other tax field":window.location="OT_Field.htm"+qString;break;
		case "other tax office":window.location="OT_Office.htm"+qString;break;			
		case "miscellaneous":window.location="MiscReceipt.htm"+qString;break;
		case "bank payment":window.location="DirectBankPayment.htm"+qString;break;
		case "cash payment":window.location="DirectCashPayment.htm"+qString;break;
		case "supplier journal":window.location="SupplierJournal.htm"+qString;break;
		case "contractor journal":window.location="ContractorJournal.htm"+qString;break;
		case "salary journal":window.location="JV_Salary.htm"+qString;break;
		case "advance payment":window.location="AdvanceJournal.htm"+qString;break;
		case "supplier/contractor payment":window.location="SubLedgerPayment.htm"+qString;break;	
		case "salary payment":window.location="SubledgerSalaryPayment.htm"+qString;break;			
		case "general journal voucher":window.location="JV_General.htm"+qString;break;
		case "bank to bank":window.location="JV_Contra_BToB.htm"+qString;break;
		case "cash withdrawal":window.location="JV_Contra_BToC.htm"+qString;break;
		case "cash deposit":window.location="JV_Contra_CToB.htm"+qString;break;
		case "inter fund transfer":window.location="JV_Contra_FToF.htm"+qString;break;
		case "pay-in slip":window.location="PayInSlip.htm"+qString;break;
	}
}


function checkChequeDate(chqObjName){
/*
var todayString=document.getElementById("databaseDate").value;
var dbArr=todayString.split("-");
var nMonth=0;
for(var i=0;i<12;i++){
		 if(gMonthBag[i] == dbArr[1].toUpperCase()){
			nMonth=i;break;
		 }
}
var today=new Date(dbArr[2],nMonth,dbArr[0]);


var chqDate=document.getElementById(chqObjName);
var chqArr=today.toString().split(" ");
var nMonth=0;
for(var i=0;i<12;i++){
		 if(gMonthBag[i] == chqArr[1].toUpperCase()){
			nMonth=i;break;
		 }
}
var oldDate=new Date(chqArr[5],nMonth,chqArr[2]);
oldDate.setMonth(oldDate.getMonth()-3);
var newDate=new Date(chqArr[5],nMonth,chqArr[2]);
newDate.setMonth(newDate.getMonth()+3);
if(!checkFrontDate(chqDate,oldDate,"Cheque")) return false;
if(!checkBackDate(chqDate,newDate,"Cheque")) return false;
*/
return true;
}
function checkBackDate(eleObj,currentDate,msgText)
{
	      //checks whether the data  contained is past date .i.e date less than current date 
          var nDate=eleObj.value;
		  var chqArr=nDate.split("-");
		  var nMonth=0;
		  for(var i=0;i<12;i++){
		 		if(gMonthBag[i] == chqArr[1].toUpperCase()){
					nMonth=i;
					break;
		 		}
		  }
	      var vDate=new Date(chqArr[2],nMonth,chqArr[0]);
          var curDateStr = currentDate.toString();
          var curDateAry = curDateStr.split(' ');
          var testDateAry= vDate.toString().split(' ');
		  //testDateAry contains date to check and curDateAry contains current date
          if(parseInt(testDateAry[5]) > parseInt(curDateAry[5]))
          {
        	  bootbox.alert("Not a valid  "+msgText+" date");
			  eleObj.focus();
              return false;
          }    
          else if(parseInt(testDateAry[5]) == parseInt(curDateAry[5]))
          {
                if(vDate.getMonth()> currentDate.getMonth())
                {
                	bootbox.alert("Not a valid  "+msgText+" date");
						eleObj.focus();
                        return false;
                }
                else if(vDate.getMonth() == currentDate.getMonth())
                {
                        if(parseInt(testDateAry[2]) > parseInt(curDateAry[2]))
                        {
                        	bootbox.alert("Not a valid  "+msgText+" date");
							  eleObj.focus();
                              return false;
                        }
                }  
          }           
          return true;                    
}
function checkFrontDate(eleObj,currentDate,msgText)
{
  	      //checks whether the data  contained is future date .i.e date greater than current date 
  		 
		  var nDate=eleObj.value;
		  var chqArr=nDate.split("-");
		  var nMonth=0;
		  for(var i=0;i<12;i++){
		 		if(gMonthBag[i] == chqArr[1].toUpperCase()){
					nMonth=i;
					break;
		 		}
		  }
	      var vDate=new Date(chqArr[2],nMonth,chqArr[0]);
          var curDateStr = currentDate.toString();
          var curDateAry = curDateStr.split(' ');
          var testDateAry= vDate.toString().split(' ');
		  //testDateAry contains date to check and curDateAry contains current date
		  if(parseInt(testDateAry[5]) < parseInt(curDateAry[5]))
          {
			  bootbox.alert("Not a valid "+msgText+" date");
			  eleObj.focus();
              return false;
          }    
          else if(parseInt(testDateAry[5]) == parseInt(curDateAry[5]))
          {
                if(vDate.getMonth()< currentDate.getMonth())
                {
                	bootbox.alert("Not a valid  "+msgText+" date");
						eleObj.focus();
                        return false;
                }
                else if(vDate.getMonth()== currentDate.getMonth())
                {
                        if(parseInt(testDateAry[2]) < parseInt(curDateAry[2]))
                        {
                        	bootbox.alert("Not a valid  "+msgText+" date");
							  eleObj.focus();
                              return false;
                        }
                }  
          }           
          return true;                    
                     
}

function isValidUser(level, role){	
	var ret = true;
	switch(level){
		case 1:
			if(role == 'FO') ret = true;
			break;			
		case 2:
			if(role == 'FO' || role == 'AM') ret = true;			
			break;	
		case 3:
			if(role == 'FO' || role == 'AM' || role == 'UR') ret = true;
			break;
	}
	
	if (ret == false)
		bootbox.alert('Permission denied. Contact administrator');
	return ret;
}


function formatDateToDDMMYYYY6(dt){
	var date = dt.substring(0, 11);			
	if( !(date == null || date == '') ){			
		var array = date.split("-");
		switch(array[1]){
			case "jan": array[1] = '01';break;
			case "feb": array[1] = '02';break;
			case "mar": array[1] = '03';break;
			case "apr": array[1] = '04';break;
			case "may": array[1] = '05';break;
			case "jun": array[1] = '06';break;
			case "jul": array[1] = '07';break;
			case "aug": array[1] = '08';break;
			case "sep": array[1] = '09';break;
			case "oct": array[1] = '10';break;
			case "nov": array[1] = '11';break;
			case "dec": array[1] = '12';break;
		}		
		dt = array[0] + '/' + array[1] + '/' + array[2];			
	}	
	return dt;	
}


function formatDateToDDMMYYYY7(dt){
	var date = dt.substring(0, 11);			
	if( !(date == null || date == '') ){			
		var array = date.split("-");
		switch(array[1]){
			case "jan": array[1] = '01';break;
			case "feb": array[1] = '02';break;
			case "mar": array[1] = '03';break;
			case "apr": array[1] = '04';break;
			case "may": array[1] = '05';break;
			case "jun": array[1] = '06';break;
			case "jul": array[1] = '07';break;
			case "aug": array[1] = '08';break;
			case "sep": array[1] = '09';break;
			case "oct": array[1] = '10';break;
			case "nov": array[1] = '11';break;
			case "dec": array[1] = '12';break;
		}		
		dt = array[2] + '/' + array[1] + '/' + array[0];			
	}	
	return dt;	
}

function formatDateToDDMMYYYY4(dt){
	var date = dt.substring(0, 11);			
	if( !(date == null || date == '') ){			
		var array = date.split("-");
		switch(array[1]){
			case "Jan": array[1] = '01';break;
			case "Feb": array[1] = '02';break;
			case "Mar": array[1] = '03';break;
			case "Apr": array[1] = '04';break;
			case "May": array[1] = '05';break;
			case "Jun": array[1] = '06';break;
			case "Jul": array[1] = '07';break;
			case "Aug": array[1] = '08';break;
			case "Sep": array[1] = '09';break;
			case "Oct": array[1] = '10';break;
			case "Nov": array[1] = '11';break;
			case "Dec": array[1] = '12';break;
		}		
		dt = array[0] + '/' + array[1] + '/' + array[2];			
	}	
	return dt;	
}

function formatDateToDDMMYYYY(dt){
	var date = dt.substring(0, 11);			
	if( !(date == null || date == '') ){			
		var array = date.split("-");
		switch(array[1]){
			case "Jan": array[1] = '01';break;
			case "Feb": array[1] = '02';break;
			case "Mar": array[1] = '03';break;
			case "Apr": array[1] = '04';break;
			case "May": array[1] = '05';break;
			case "Jun": array[1] = '06';break;
			case "Jul": array[1] = '07';break;
			case "Aug": array[1] = '08';break;
			case "Sep": array[1] = '09';break;
			case "Oct": array[1] = '10';break;
			case "Nov": array[1] = '11';break;
			case "Dec": array[1] = '12';break;
		}		
		dt = array[0] + '-' + array[1] + '-' + array[2];			
	}	
	return dt;	
}

function formatDateToDDMMYYYY2(dt){
	var date = dt.substring(0, 11);			
	if( !(date == null || date == '') ){			
		var array = date.split("/");
		switch(array[1]){
			case "Jan": array[1] = '01';break;
			case "Feb": array[1] = '02';break;
			case "Mar": array[1] = '03';break;
			case "Apr": array[1] = '04';break;
			case "May": array[1] = '05';break;
			case "Jun": array[1] = '06';break;
			case "Jul": array[1] = '07';break;
			case "Aug": array[1] = '08';break;
			case "Sep": array[1] = '09';break;
			case "Oct": array[1] = '10';break;
			case "Nov": array[1] = '11';break;
			case "Dec": array[1] = '12';break;
		}		
		dt = array[1] + '/' + array[0] + '/' + array[2].substring(0,4);			
	}	
	return dt;	
}

function formatDateToDDMMYYYY5(dt){
	var date = dt.substring(0, 11);			
	if( !(date == null || date == '') ){			
		var array = date.split("/");
		switch(array[1]){
			case "Jan": array[1] = '01';break;
			case "Feb": array[1] = '02';break;
			case "Mar": array[1] = '03';break;
			case "Apr": array[1] = '04';break;
			case "May": array[1] = '05';break;
			case "Jun": array[1] = '06';break;
			case "Jul": array[1] = '07';break;
			case "Aug": array[1] = '08';break;
			case "Sep": array[1] = '09';break;
			case "Oct": array[1] = '10';break;
			case "Nov": array[1] = '11';break;
			case "Dec": array[1] = '12';break;
		}		
		dt = array[0] + '/' + array[1] + '/' + array[2].substring(0,4);			
	}	
	return dt;	
}
//ip-dd/mon/yyyy
//op=dd-mm-yyyy
function formatDateToDDMMYYYY1(dt){
	var date = dt.substring(0, 11);			
	if( !(date == null || date == '') ){			
		var array = date.split("/");
		switch(array[1]){
			case "Jan": array[1] = '01';break;
			case "Feb": array[1] = '02';break;
			case "Mar": array[1] = '03';break;
			case "Apr": array[1] = '04';break;
			case "May": array[1] = '05';break;
			case "Jun": array[1] = '06';break;
			case "Jul": array[1] = '07';break;
			case "Aug": array[1] = '08';break;
			case "Sep": array[1] = '09';break;
			case "Oct": array[1] = '10';break;
			case "Nov": array[1] = '11';break;
			case "Dec": array[1] = '12';break;
		}		
		dt = array[0] + '/' + array[1] + '/' + array[2].substring(0,4);			
	}	
	return dt;	
}
function compareDate(dt1, dt2){			
/*******		Return Values [0 if dt1=dt2], [1 if dt1<dt2],  [-1 if dt1>dt2]     *******/
	var d1, m1, y1, d2, m2, y2, ret;
	dt1 = dt1.split('/');
	dt2 = dt2.split('/');
	ret = (eval(dt2[2])>eval(dt1[2])) ? 1 : (eval(dt2[2])<eval(dt1[2])) ? -1 : (eval(dt2[1])>eval(dt1[1])) ? 1 : (eval(dt2[1])<eval(dt1[1])) ? -1 : (eval(dt2[0])>eval(dt1[0])) ? 1 : (eval(dt2[0])<eval(dt1[0])) ? -1 : 0 ;										
	return ret;
}

/**
*	Input is mm/dd/yyyy
*	Output is dd-Mon-yyyy
*/
function formatDate(dt){
	if(dt==null || dt==''  || dt=="" )return '';
	var array = dt.split("/");
	var mon=array[0];
	var day=array[1];
	var year=array[2].substring(0,4);			
		switch(mon){
			case "1": mon = 'Jan';break;
			case "2": mon = 'Feb';break;
			case "3": mon = 'Mar';break;
			case "4": mon = 'Apr';break;
			case "5": mon = 'May';break;
			case "6": mon = 'Jun';break;
			case "7": mon = 'Jul';break;
			case "8": mon = 'Aug';break;
			case "9": mon = 'Sep';break;
			case "10": mon = 'Oct';break;
			case "11": mon = 'Nov';break;
			case "12": mon = 'Dec';break;
		}		
		dt = day+"-"+mon+"-"+year;			
		return dt;	
}
/**
*	Input is dd/mm/yyyy for mon=1,2
*	Output is dd-Mon-yyyy
*/
function formatDate4(dt){
	if(dt==null || dt==''  || dt=="" )return '';
	var array = dt.split("/");
	var mon=array[1];
	var day=array[0];
	var year=array[2].substring(0,4);			
		switch(mon){
			case "1": mon = 'Jan';break;
			case "2": mon = 'Feb';break;
			case "3": mon = 'Mar';break;
			case "4": mon = 'Apr';break;
			case "5": mon = 'May';break;
			case "6": mon = 'Jun';break;
			case "7": mon = 'Jul';break;
			case "8": mon = 'Aug';break;
			case "9": mon = 'Sep';break;
			case "10": mon = 'Oct';break;
			case "11": mon = 'Nov';break;
			case "12": mon = 'Dec';break;
		}		
		dt = day+"-"+mon+"-"+year;			
		return dt;	
}
//ip=dd/mm/yyyy for mon-01,02
//op=dd-Mon-yyyy
function formatDate5(dt){
	if(dt==null || dt==''  || dt=="" )return '';
	var array = dt.split("/");
	var mon=array[1];
	var day=array[0];
	var year=array[2].substring(0,4);			
		switch(mon){
			case "01": mon = 'Jan';break;
			case "02": mon = 'Feb';break;
			case "03": mon = 'Mar';break;
			case "04": mon = 'Apr';break;
			case "05": mon = 'May';break;
			case "06": mon = 'Jun';break;
			case "07": mon = 'Jul';break;
			case "08": mon = 'Aug';break;
			case "09": mon = 'Sep';break;
			case "10": mon = 'Oct';break;
			case "11": mon = 'Nov';break;
			case "12": mon = 'Dec';break;
		}		
		dt = day+"-"+mon+"-"+year;			
		return dt;	
}


/**
*	Input is dd/mm/yyyy
*	Output is dd-mm-yyyy
*/
function formatDate6(dt){
	if(dt==null || dt==''  || dt=="" )return '';
	var array = dt.split("/");
	var mon=array[1];
	var day=array[0];
	var year=array[2].substring(0,4);			
	dt = day+"/"+mon+"/"+year;			
		return dt;	
}

/**
*	Input is dd/mm/yyyy
*	Output is mm-dd-yyyy
*/
function formatDate7(dt){
	if(dt==null || dt==''  || dt=="" )return '';
	var array = dt.split("/");
	var mon=array[1];
	var day=array[0];
	var year=array[2].substring(0,4);			
	dt = mon+"-"+day+"-"+year;			
		return dt;	
}
function formatDate8(dt){
	if(dt==null || dt==''  || dt=="" )return '';
	var array = dt.split("/");
	var mon=array[1];
	var day=array[0];
	var year=array[2].substring(0,4);			
		switch(mon){
			case "1": mon = '01';break;
			case "2": mon = '02';break;
			case "3": mon = '03';break;
			case "4": mon = '04';break;
			case "5": mon = '05';break;
			case "6": mon = '06';break;
			case "7": mon = '07';break;
			case "8": mon = '08';break;
			case "9": mon = '09';break;
			case "10": mon = '10';break;
			case "11": mon = '11';break;
			case "12": mon = '12';break;
		}		
		dt = day+"/"+mon+"/"+year;			
		return dt;	
}
/**
*	Input is dd/mm/yyyy
*	Output is dd/Mon/yyyy
*/
function formatDate1(dt){
	if(dt==null || dt==''  || dt=="" )return '';
	var array = dt.split("/");
	var mon=array[1];
	var day=array[0];
	var year=array[2].substring(0,4);			
		switch(mon){
			case "01": mon = 'Jan';break;
			case "02": mon = 'Feb';break;
			case "03": mon = 'Mar';break;
			case "04": mon = 'Apr';break;
			case "05": mon = 'May';break;
			case "06": mon = 'Jun';break;
			case "07": mon = 'Jul';break;
			case "08": mon = 'Aug';break;
			case "09": mon = 'Sep';break;
			case "10": mon = 'Oct';break;
			case "11": mon = 'Nov';break;
			case "12": mon = 'Dec';break;
		}		
		dt = day+"/"+mon+"/"+year;			
		return dt;	
}
//input=mm/dd/yyyy
//op=dd/mm/yyyy

function formatDate2(dt){
	if(dt==null || dt==''  || dt=="" )return '';
	var array = dt.split("/");
	var mon=array[0];
	var day=array[1];
	var year=array[2].substring(0,4);			
		switch(mon){
			case "1": mon = '01';break;
			case "2": mon = '02';break;
			case "3": mon = '03';break;
			case "4": mon = '04';break;
			case "5": mon = '05';break;
			case "6": mon = '06';break;
			case "7": mon = '07';break;
			case "8": mon = '08';break;
			case "9": mon = '09';break;
			case "10": mon = '10';break;
			case "11": mon = '11';break;
			case "12": mon = '12';break;
		}		
		dt = day+"/"+mon+"/"+year;			
		return dt;	
}

//ip=mm/dd/yyyy
//op=dd-Mon-yyyy
function formatDate3(dt){
	if(dt==null || dt==''  || dt=="" )return '';
	var array = dt.split("/");
	var mon=array[0];
	var day=array[1];
	var year=array[2].substring(0,4);			
		switch(mon){
			case "01": mon = 'Jan';break;
			case "02": mon = 'Feb';break;
			case "03": mon = 'Mar';break;
			case "04": mon = 'Apr';break;
			case "05": mon = 'May';break;
			case "06": mon = 'Jun';break;
			case "07": mon = 'Jul';break;
			case "08": mon = 'Aug';break;
			case "09": mon = 'Sep';break;
			case "10": mon = 'Oct';break;
			case "11": mon = 'Nov';break;
			case "12": mon = 'Dec';break;
		}		
		dt = day+"-"+mon+"-"+year;			
		return dt;	
}

function disableControls(frmIndex, isDisable){		
			for(var i=0;i<document.forms[frmIndex].length;i++)			
				document.forms[frmIndex].elements[i].disabled =isDisable;														
}

function back(obj){
var aa=	obj.opener.focus();
obj.close();
}

function closeChilds(obj,count){
	for(i=1;i<count;i++){
		var cWind=obj[i];
		cWind.close();
	} 
}
function ButtonPress(name)
{
}
function onClickCancel()
{
}
function showglEntry()
{
}
function onClickNew()
{
}
function onClickModify()
{
}

function drillDownToCoreTransactions(val,mode)
{
val1=val.substring(0,3);
 switch(val1)
 {
  case 'JVG':
  window.open("/EGF/HTML/VMC/JV_General_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'SJV' :
  window.open("/EGF/HTML/VMC/SupplierJournal_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'CJV' :
  window.open("/EGF/HTML/VMC/ContractorJournal_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'SAL' :
  window.open("/EGF/HTML/VMC/JV_Salary_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'PYS':
  window.open("/EGF/HTML/VMC/PayInSlip_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'BTB':
  window.open("/EGF/HTML/VMC/JV_Contra_BToB_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'BTC':
  window.open("/EGF/HTML/VMC/JV_Contra_BToC_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'CTB':
  window.open("/EGF/HTML/VMC/JV_Contra_CToB_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'FTF':
  window.open("/EGF/HTML/VMC/JV_Contra_FToF_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'DBP':
  window.open("/EGF/HTML/VMC/DirectBankPayment_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
   case 'BBP':
	window.open("/EGF/payment/payment.do?submitType=beforeViewAndModifyPayment&cgNumber="+val+"&mode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
  break;
  case 'DCP' :
  window.open("/EGF/HTML/VMC/DirectCashPayment_VMC.jsp?cgNumber="+val+"&showMode=viewCash","","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'SPH':
  window.open("/EGF/payment/payment.do?submitType=beforeViewAndModifyPayment&cgNumber="+val+"&mode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
  break;
  case 'SSP' :
  window.open("/EGF/payment/payment.do?submitType=beforeViewAndModifyPayment&cgNumber="+val+"&mode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
  break;
  case 'ASP' :
  window.open("/EGF/HTML/VMC/AdvanceJournal_VMC.jsp?drillDownCgn="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'MSR':
  window.open("/EGF/HTML/VMC/miscReceipt_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'OJV' :
  window.open("/EGF/HTML/VMC/ContingencyJournal_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=0,top=0,status=yes");
 }
}
// This is for NN. but some files incorporated to AP.
function callme(val,val1)
{
	var mode="view";
	switch(val1)
	{
		
		case 'JVG':
		//window.open("../HTML/JV_General.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		  window.open("../HTML/VMC/JV_General_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'SJV' :
		window.open("../HTML/SupplierJournal.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'CJV' :
		window.open("../HTML/ContractorJournal.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'SAL' :
		window.open("../HTML/JV_Salary.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'PYS':
		window.open("../HTML/VMC/PayInSlip_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		//window.open("../HTML/PayInSlip.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'BTB':
		//window.open("../HTML/JV_Contra_BToB.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		  window.open("../HTML/VMC/JV_Contra_BToB_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'BTC':
		//window.open("../HTML/JV_Contra_BToC.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		  window.open("../HTML/VMC/JV_Contra_BToC_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'CTB':
		//window.open("../HTML/JV_Contra_CToB.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		window.open("../HTML/VMC/JV_Contra_CToB_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'FTF':
		window.open("../HTML/JV_Contra_FToF.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'DBP':
		//window.open("../HTML/DirectBankPayment.htm?cgNumber="+val+"&showMode=viewBank","","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		window.open("../HTML/VMC/DirectBankPayment_VMC.jsp?cgNumber="+val+"&showMode=viewBank","","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'DCP' :
		//window.open("../HTML/DirectCashPayment.htm?cgNumber="+val+"&showMode=viewCash","","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		  window.open("../HTML/VMC/DirectCashPayment_VMC.jsp?cgNumber="+val+"&showMode=viewCash","","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'SPH':
		window.open("../HTML/SubLedgerPayment.htm?drillDownCgn="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'SSP' :
		window.open("../HTML/SubledgerSalaryPayment.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'ASP' :
		window.open("../HTML/AdvanceJournal.htm?drillDownCgn="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'MSR':
		//window.open("../HTML/MiscReceipt.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		  window.open("../HTML/VMC/miscReceipt_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'PRB' :
		case 'PRO' :
		case 'PRF' :
		window.open("../HTML/PT_Field.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'OTF':
		window.open("../HTML/OT_Field.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'OTO':
		window.open("../HTML/OT_Office.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
	}

}

function callmeAP(val,val1)
{
 var mode="view";
 window.open("/EGF/voucher/preApprovedVoucher!loadvoucherviewByCGN.action?cgn="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
 
 /*
 switch(val1)
 {
 
  case 'JVG':
  window.open("../HTML/VMC/JV_General_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'SJV' :
  window.open("../HTML/VMC/SupplierJournal_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'CJV' :
  window.open("../HTML/VMC/ContractorJournal_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'SAL' :
  window.open("../HTML/VMC/JV_Salary_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'PYS':
  window.open("../HTML/VMC/PayInSlip_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'BTB':
  window.open("../HTML/VMC/JV_Contra_BToB_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'BTC':
  window.open("../HTML/VMC/JV_Contra_BToC_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'CTB':
  window.open("../HTML/VMC/JV_Contra_CToB_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'FTF':
  window.open("../HTML/VMC/JV_Contra_FToF_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'DBP':
  window.open("../HTML/VMC/DirectBankPayment_VMC.jsp?cgNumber="+val+"&showMode=view","","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
   case 'BBP':
	window.open("../payment/payment.do?submitType=beforeViewAndModifyPayment&cgNumber="+val+"&mode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
  break;
  case 'DCP' :
  window.open("../HTML/VMC/DirectCashPayment_VMC.jsp?cgNumber="+val+"&showMode=viewCash","","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'SPH':
  window.open("../payment/payment.do?submitType=beforeViewAndModifyPayment&cgNumber="+val+"&mode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
  break;
  case 'SSP' :
  window.open("../payment/payment.do?submitType=beforeViewAndModifyPayment&cgNumber="+val+"&mode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
  break;
  case 'ASP' :
  window.open("../HTML/VMC/AdvanceJournal_VMC.jsp?drillDownCgn="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'MSR':
  window.open("../HTML/VMC/miscReceipt_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
  break;
  case 'OJV' :
  window.open("../HTML/VMC/ContingencyJournal_VMC.jsp?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=0,top=0,status=yes");
 }*/
}

/*function CloseWindow(obj)
{
	if(window.event.keyCode==88 && window.event.altKey){
	 	var aa=	obj.opener.focus();
		obj.close();
		}
	}
*/
/**
* This function will open the respective windows on shotcutkeys.
* We are using Alt-1,Atl-2,.....Alt-0.
* Alt-X will close the window
*/	
function CloseWindow(obj){

	if(!obj) 
	   var obj = window.event;
	var code;
	if (obj.keyCode) 
	   code = obj.keyCode;
	else if (obj.which) 
	   code = obj.which;

	if(obj.altKey){
		//var code=window.event.keyCode;
		switch(code){
		case 88:
		//var aa=obj.opener.focus();		
		this.close();
		break;
		case 49:window.open("PT_Field.htm?showMode=new","","height=650,width=900,scrollbars=yes,left=0,top=0,status=yes");break;
		case 50:window.open("MiscReceipt.htm?showMode=new","","height=650,width=900,scrollbars=yes,left=0,top=0,status=yes");break;
		case 51:window.open("DirectBankPayment.htm?showMode=paymentBank","","height=650,width=900,scrollbars=yes,left=0,top=0,status=yes");break;
		case 83:ButtonPress('saveclose');break;
		case 78:ButtonPress('savenew');break;
		//cancel
		case 67:onClickCancel();break;
		//Show GL
		case 71:showglEntry();break;
		//Add new
		case 65:onClickNew();break;
		//modify
		case 77:onClickModify();break;
		//View
		case 86:onClickView();break;
		}
	}
}
	
function newWindow(url)
{
   newwindow=window.open(url,'name',"height=600,width=900,scrollbars=yes,left=100,top=100,status=no,toolbar=no");
	if (window.focus) {newwindow.focus()}
	return false;

}
// *************  Ajax Related Functions ************* //
// *************       	Start            ************* // 

//XMLHttpRequest is a request object used in Ajax. We create a Request Object here.

//used in menu tree
function initiateRequest() {
       if (window.XMLHttpRequest) {
           return new XMLHttpRequest();
       } else if (window.ActiveXObject) {
           isIE = true;
           return new ActiveXObject("Microsoft.XMLHTTP");
       }
   }
   
//We clear the table whenever we get a new set of values.

function clearTable(completeTable) {
    if (completeTable) {
      completeTable.setAttribute("bordercolor", "white");
      completeTable.setAttribute("border", "0");
      completeTable.style.visible = false;
      for (loop = completeTable.childNodes.length-1;loop >=0 ;loop--) {
       completeTable.removeChild(completeTable.childNodes[loop]);
       
       
      }
    }
}

/* The response object has the set of values. ^ (separator) is used to separate the html stuff. The remaining
   string has the set of codes separated by + (separator). We split it and paint it on the screen. */

function paintCodes(responseText,autorow,menu,completeTable)
{	clearTable(completeTable);
        var a = responseText.split("^");
	var codes = a[0];
	codeArray=codes.split("+");
	var accCodes=codeArray[0];
		
	if (accCodes != null)
	{
    //	var autorow = document.getElementById("auto-row");
    //	var menu = document.getElementById("menu-popup");
         
    	menu.style.top = getElementY(autorow) + "px";
    	  	
      //completeTable = document.getElementById("completeTable");
    	completeTable.setAttribute("bordercolor", "white");
        completeTable.setAttribute("bordercolor", "black");
        completeTable.setAttribute("border", "1");
       }//if
    else 
    {
        clearTable(completeTable);
        
    }
	var i=0;
	while(codeArray[i] != null)
	{
	var accountcodes = codeArray[i];
	appendCodes(accountcodes,completeTable);
	i++;
	
	}
	
}//paintCodes


//This function will find exactly where the next element has to be painted.

function getElementY(element){
	var targetTop = 0;
	if (element.offsetParent) {
		while (element.offsetParent) {
			targetTop += element.offsetTop;
            element = element.offsetParent;
		}
	} else if (element.y) {
		targetTop += element.y;
		    }
	return targetTop;
}

//The values are appended in the table.

function appendCodes(accountcodes,completeTable) {
    var accountcodes;
    var row;
    var nameCell;
    if (isIE) {
        row = completeTable.insertRow(completeTable.rows.length);
        nameCell = row.insertCell(0);
    } else {
        row = document.createElement("tr");
        nameCell = document.createElement("td");
        row.appendChild(nameCell);
        completeTable.appendChild(row);
    }
  //  nameCell.setAttribute("align","right");
    var linkElement = document.createElement("a");
    linkElement.appendChild(document.createTextNode(accountcodes));
    nameCell.appendChild(linkElement);
}


// *************       	End            ************* // 

function getControlInBranch(obj,controlName)
{			
	if (!obj || !(obj.getAttribute)) return null;
	// check if the object itself has the name
	if (obj.getAttribute('name') == controlName) return obj;

	// try its children
	var children = obj.childNodes;
	var child;
	if (children && children.length > 0){ 
		for(var i=0; i<children.length; i++){
			child=this.getControlInBranch(children[i],controlName);
			if(child) return child;
		}
	}
	return null;
}

function validateDate(val)
{
	if (!val) return null;
	var parts = val.split("/");
	if (parts.length != 3) return null;
	var dd = parseInt(parts[0],10);
	if(isNaN(dd)) return null;
	var lendd=parts[0];
	if(lendd.length != 2) return null;

	var mm=parseInt(parts[1]-1);
	if(isNaN(mm)) return null;
	if (mm < 0 && mm > 11) return null;
	var lenmm=parts[1];
	if(lenmm.length != 2) return null;

	var yyyy = parseInt(parts[2]);
	if (isNaN(yyyy)) return null;
	var lenyyyy=parts[2];
	if(lenyyyy.length != 4) return null;

	var maxdd = 0;
	if((yyyy % 4 == 0) && ((!(yyyy % 100 == 0)) || (yyyy % 400 == 0)))
	{
		switch(mm+1)
		{
			case 1: maxdd = 31; break;  
			case 2: maxdd = 29; break;
			case 3: maxdd = 31; break;
			case 4: maxdd = 30; break;
			case 5: maxdd = 31; break;
			case 6: maxdd = 30; break;
			case 7: maxdd = 31; break;
			case 8: maxdd = 31; break;
			case 9: maxdd = 30; break;
			case 10: maxdd = 31; break;
			case 11: maxdd = 30; break;
			case 12: maxdd = 31; break;

		 }
		 if(dd > maxdd) return null;					 

	}
	else 
	{
		switch(mm+1)
		{
			case 1: maxdd = 31; break;  
			case 2: maxdd = 28; break;
			case 3: maxdd = 31; break;
			case 4: maxdd = 30; break;
			case 5: maxdd = 31; break;
			case 6: maxdd = 30; break;
			case 7: maxdd = 31; break;
			case 8: maxdd = 31; break;
			case 9: maxdd = 30; break;
			case 10: maxdd = 31; break;
			case 11: maxdd = 30; break;
			case 12: maxdd = 31; break;

		 }
		if(dd > maxdd) return null;

	}
	return val;
}
function checkDate(obj)
{
	var dat=validateDate(obj.value);
	if (!dat) 
	{
		bootbox.alert('Invalid date format : Enter Date as dd/mm/yyyy');
		obj.focus();
		return;
	}
}
function getRow(obj)
{
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

/*
 * This function will check whether the entered field is unique or not
 */
 
 function checkUnique(tablename,fieldname,fieldvalue)
 {
	var type = "checkUniqueness";
	var link = "/EGF/inventory/commons/uniqueChecking.jsp?type=" + type+"&tablename=" + tablename+"&fieldname=" + fieldname+ "&fieldvalue=" + fieldvalue+ " ";
		
	var request = initiateRequest();
	var isUnique;
	request.open("GET", link , false);
	request.send(null);
	if (request.status == 200) 
	{	

 	    var response=request.responseText;
  	    var result = response.split("/");  	    
	    if(result[0]=="true")
	    {
		isUnique="true";
	    }	
	    else if(result[0]=="false")
	    {
	    	bootbox.alert("Entered "+fieldname+" already exists");	
		isUnique="false";
	    }		

	}
	
	return isUnique;
}




/*
 * This function returns absolue left and top position of the object
 */
		function findPos(obj) 
		{
			var curleft = curtop = 0;
			if (obj.offsetParent) 
			{
				curleft = obj.offsetLeft;
				curtop = obj.offsetTop;
				while (obj = obj.offsetParent) 
				{	//bootbox.alert(obj.nodeName);
					curleft =curleft + obj.offsetLeft;
					curtop =curtop + obj.offsetTop; //bootbox.alert(curtop);
				}
			}
			return [curleft,curtop];
		}

function IsNumeric(sText)
{
	var ValidChars = "0123456789.";
	var IsNumber=true;
	var Char;

	for (i = 0; i < sText.length && IsNumber == true; i++) 
	{ 
	    Char = sText.charAt(i); 
	    if (ValidChars.indexOf(Char) == -1) 
	    {
	       IsNumber = false;
	    }
	}
	return IsNumber;
}
// Format amount round of 2 decimal point
	function formatAmount(amount)
	{
 		//bootbox.alert("formatAmount");

 		var i = parseFloat(amount);
 		
 		if(isNaN(i)) 
 		{
 		//bootbox.alert('isNaN');
 		 i = 0.00; 
 		}
		
		 var minus = '';
 			if(i < 0) { minus = '-'; }
 				i = Math.abs(i);
 			i = parseInt((i + .005) * 100);
 			i = i / 100;
 			s = new String(i);
 		if(s.indexOf('.') < 0) { s += '.00'; }
 		if(s.indexOf('.') == (s.length - 2)) { s += '0'; }
 		s = minus + s;
 		return s;
	}

/* checking for duplicate account code and Function Code Combination */
function checkForFunction_AccountCode(table,funId,glCode){ //,checkEntityDuplication){
	//bootbox.alert('inside function checkForFunction_AccountCode');
	var funcIdtemp1,glcodeTemp1,funcIdtemp2,glcodeTemp2;
	var tableObj = document.getElementById(table);
	//bootbox.alert("length:"+tableObj.rows.length);
	

	for(i=1; i<tableObj.rows.length && tableObj.rows.length>2; i++){ 
		funcIdtemp1=getControlInBranch(tableObj.rows[i],funId);
		glcodeTemp1=getControlInBranch(tableObj.rows[i],glCode);
		if(funcIdtemp1!="" && glcodeTemp1!="")continue;
		//bootbox.alert('glcodeTemp1.getAttribute(id):'+ glcodeTemp1.getAttribute('id'));
		if(glcodeTemp1.value == '') { bootbox.alert("delete empty row :"+i); return false;}
		
		for(j=i+1; j<tableObj.rows.length; j++){ 
			funcIdtemp2=getControlInBranch(tableObj.rows[j],funId);
			//bootbox.alert("funcIdtemp1:"+funcIdtemp1.value+";;;"+"funcIdtemp2:"+funcIdtemp2.value);
			glcodeTemp2=getControlInBranch(tableObj.rows[j],glCode);
			 if((glcodeTemp1.value  == glcodeTemp2.value)  && (funcIdtemp1.value == funcIdtemp2.value)){
				 bootbox.alert('Same Account Code & Function Name can not appear more than once...CHECK ACCOUNT : ' + glcodeTemp1.value);
				return false;
	  		}
	 	}
	}
	return true;
	

}
/* checking for duplicate account code and Function Code and tdsCode Combination */
function checkForFunction_AccountCode_dedGrid(table,funId,glCode,tdsCode){ //,checkEntityDuplication){
	//bootbox.alert('inside function checkForFunction_AccountCode');
	var funcIdtemp1,glcodeTemp1,funcIdtemp2,glcodeTemp2,tds1,tds2;
	var tableObj = document.getElementById(table);
	//bootbox.alert("length:"+tableObj.rows.length);
	

	for(i=1; i<tableObj.rows.length && tableObj.rows.length>2; i++){ 
		funcIdtemp1=getControlInBranch(tableObj.rows[i],funId);
		glcodeTemp1=getControlInBranch(tableObj.rows[i],glCode);
		if(funcIdtemp1!="" && glcodeTemp1!="")continue;
		tds1=getControlInBranch(tableObj.rows[i],tdsCode);
		//bootbox.alert('glcodeTemp1.getAttribute(id):'+ glcodeTemp1.getAttribute('id'));
		//if(glcodeTemp1.value == '') { bootbox.alert("delete empty row: "+i); return false;}
		
		for(j=i+1; j<tableObj.rows.length; j++){ 
			funcIdtemp2=getControlInBranch(tableObj.rows[j],funId);
			//bootbox.alert("funcIdtemp1:"+funcIdtemp1.value+";;;"+"funcIdtemp2:"+funcIdtemp2.value);
			glcodeTemp2=getControlInBranch(tableObj.rows[j],glCode);
			tds2=getControlInBranch(tableObj.rows[j],tdsCode);
			if(tds1!=null && tds1.value!=''){
				if((tds1.value  == tds2.value)  && (funcIdtemp1.value == funcIdtemp2.value)){
					bootbox.alert('Same tds Code & Function Name can not appear more than once...CHECK type : ' + tds1.value);
					return false;
				}
			}else{
				if((glcodeTemp1.value  == glcodeTemp2.value)  && (funcIdtemp1.value == funcIdtemp2.value)){
					bootbox.alert('Same Account Code & Function Name can not appear more than once...CHECK ACCOUNT : ' + glcodeTemp1.value);
					return false;
				}
			}
	 	}
	}
	return true;
	

}
function CheckVoucherNumberType(vNumnew,bNumnew,Type)
{

if(Type=='Auto')
	{	
	var mode=PageManager.DataService.getQueryField("showMode");
	if(mode=='new')
	document.getElementById(vNumnew).value=document.getElementById(bNumnew).value;
	}
}

function validateNames(obj)
{
	regex='\,,?,:,@,#,$,%,^,&,*,(,),[,]';
	var x=obj.value;
	for(var i=0;i<x.length;i++)
		{
		for(var j=0;j<regex.length;j++)
			{
			if(x.charAt(i)==regex.charAt(j))
			{
				bootbox.alert("Please Enter only A-Z,a-z,0-9,.");
			obj.focus();
			return;
			}
			}
		


		}
}

function convertDate(obj)
{
	if(obj.value=='')
		return;
	var temp = obj.value.split('/');
	if(temp[2].length==4)
		return;
	else if(temp[2].length==2)
	{
		var t= new Date();
		var s = t.getYear().toString().substring(0,2);
		t.setYear(s+temp[2]);
		t.setMonth(temp[1]-1);
		t.setDate(temp[0]);
		var j = (Number(t.getMonth())+Number(1));
		var m =j<9?'0'+j:j
		var k=t.getDate()+"/"+m+"/"+t.getYear();
		obj.value=k;
	}
	else
	{
		bootbox.alert('Invalid date');
		return;
	}	
}
//returns contextpath text
 function getContext() {
	var base = document.getElementsByTagName('base')[0];
	if (base && base.href && (base.href.length > 0)) {
		base = base.href;
	} else {
		base = document.URL;
	}
	var strLenWithContext=base.indexOf("/",base.indexOf("/", base.indexOf("//") + 2) +1)+1 ;//- base.indexOf("/", base.indexOf("//") + 2) +1;
	var strLenWithoutContext=base.indexOf("/", base.indexOf("//") + 2) +1;
	return base.substring(strLenWithoutContext,strLenWithContext-1);
}
 // validate the text field to accept only numbers(0-9) on key up event.
 function validateOnlyNumber(obj){
		var fieldValue = obj.value;
		if(null == fieldValue.match(/^\d+$/)){
			obj.value = fieldValue.substring(0,fieldValue.length-1) ;
		}
}
 
 // validate the text field to not accept numbers.
 function validateIgnoreNumber(obj){
	
		var fieldValue = obj.value;
		if(null != fieldValue.match(/\d/)){
			obj.value = fieldValue.substring(0,fieldValue.length-1) ;
		}
}
function validateDigitsAndDecimal(obj)
{
	if(isNaN(obj.value.trim()))
	{
		bootbox.alert("Invalid Amount")
		obj.value=0;
		obj.focus();
	}
	else
	{
		if(parseFloat(obj.value.trim())<0)
		{
			bootbox.alert("Negetive Amount is not allowed");
			obj.value=0;
			obj.focus();
		}
		else
		{
			obj.value=obj.value.replace("+","");	
			obj.value=obj.value.trim();
			if(isNaN(parseFloat(obj.value)))
			{
				obj.value=0;	
			}
			else
			{
				var resultNum = parseFloat(obj.value);
				obj.value=amountConverter(resultNum);
			}
		}
	}
}
 function validateDecimal(obj){
		var fieldValue = obj.value;

		if(null == fieldValue.match(/(^-*\d+$)|(^-*\d+\.\$)|(^-*\d+\.\d+$)/)){
			obj.value = fieldValue.substring(0,fieldValue.length-1) ;
		}
		
}
function amountConverter(amt) {
	var formattedAmt = amt.toFixed(2);
	return formattedAmt;
}
