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
<link rel="stylesheet" href="../css/egov.css" type="text/css">
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../resources/javascript/jsCommonMethods.js"></Script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<SCRIPT LANGUAGE="javascript">
var cWind;         // temporary window reference
var count=1;      // Counter which gives number of child windows opened
var cWindows=new Object();    //HashMap where we store child window
var mode="";
var journalType="General";
window.vNum=new Object();
var countOfVnum=0;

function ButtonPress()
{
	if(PageValidator.validateForm())
	{
		chk=document.getElementById('voucherDateTo');
		if(chk.value.length>0)
		if(!PageValidator.validateFromTo(chk))
		return false;
		PageManager.DataService.removeQueryField('voucherNumber');
		PageManager.DataService.removeQueryField('fundID');
		PageManager.DataService.removeQueryField('voucherDateFrom');
		PageManager.DataService.removeQueryField('voucherDateTo');
		PageManager.DataService.removeQueryField('statusType');
		PageManager.DataService.removeQueryField('reverseType');

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
	}
	else
	return;
	
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
		document.getElementById('displayCondition').value=1;
	
	if(journalType && journalType.length!=0)
	{
		if(journalType=="General")
		{
			if(mode=="modify")
			{
				var recStatus=0;
				var isConfirmed=1;
				var name="'JVGeneral'";
				document.getElementById('inputField9').value=name; //v.name ='' PageManager.DataService.setQueryField('name',name);
				document.getElementById('inputField5').value=recStatus;// v.statusPageManager.DataService.setQueryField('statusType',recStatus);
				document.getElementById('inputField6').value=isConfirmed;//v.isConfirmed PageManager.DataService.setQueryField('isConfirmed',isConfirmed);
			}
			else if(mode=="edit")
			{
				var recStatus=0;
				var isConfirmed=0;
				var name="'JVGeneral'";
				document.getElementById('inputField9').value=name; //v.name ='' PageManager.DataService.setQueryField('name',name);
				document.getElementById('inputField5').value=recStatus;// v.statusPageManager.DataService.setQueryField('statusType',recStatus);
				document.getElementById('inputField6').value=isConfirmed;//v.isConfirmed PageManager.DataService.setQueryField('isConfirmed',isConfirmed);			
			}
			else
			{
				var name="'JVGeneral','Property Tax','Advance Payment','Asset Depreciation','Asset Capitalization','Asset Improvement','Asset Revaluation','Payment','Salary','Asset Sale'";
				document.getElementById('inputField9').value=name; //v.name ='' PageManager.DataService.setQueryField('name',name);
			}
			//PageManager.DataService.callDataService('journalSearchGeneral');
			document.journalVoucher.submit(); return;
		}


	if(journalType=="Bill")
	{	
	    var billType=document.getElementById('billType');
    	if(billType.value=="Supplier")
		{
			if(mode=="edit")
			{
			  var num=0;
			 document.getElementById('inputField5').value=num;//PageManager.DataService.setQueryField('statusNum',statusNum);
			 var isconfirmed=0;
			 document.getElementById('inputField6').value=isconfirmed;//v.isConfirmed PageManager.DataService.setQueryField('isconfirmed',isconfirmed);
			 document.getElementById('inputField8').value=''; //v.status!=4 
			 //PageManager.DataService.callDataService('journalSearchSupplierModify');

			}
			else if(mode=="modify")
		    {
			  	document.getElementById('inputField8').value=''; //v.status!=4 
			  	var num=0;
				document.getElementById('inputField5').value=num;//PageManager.DataService.setQueryField('statusNum',statusNum);
				var isconfirmed=1;
				document.getElementById('inputField7').value=num;//PageManager.DataService.setQueryField('paidAmount',num);
			 	document.getElementById('inputField6').value=isconfirmed;//PageManager.DataService.setQueryField('isconfirmed',isconfirmed);
				//PageManager.DataService.callDataService('journalSearchSupplierModify');
			}
			else {
			  //PageManager.DataService.callDataService('journalSearchSupplier');
				//document.journalVoucher.submit(); return;
				var statusNotEqual=4;
				document.getElementById('inputField8').value=statusNotEqual; //v.status!=4 
				}
			document.journalVoucher.submit(); return;
		}

		if(billType.value=="Contractor")
		{
		  //showAllColumns();
		  if(mode=="edit")
		  {
			     var num=0;
			 	 document.getElementById('inputField5').value=num;//PageManager.DataService.setQueryField('statusNum',num);
			 	 document.getElementById('inputField6').value='0';//v.isConfirmed
			 	 document.getElementById('inputField8').value=''; //v.status!=4 
				// PageManager.DataService.callDataService('journalSearchContractorModify');
		  }
		 else if(mode=="modify")
		 {
			   	 document.getElementById('inputField8').value=''; //v.status!=4 
			   	 var num=0;
		    	 document.getElementById('inputField5').value=num;//PageManager.DataService.setQueryField('statusNum',num);
			 	 var isconfirmed=1;
			 	 document.getElementById('inputField6').value=isconfirmed;//PageManager.DataService.setQueryField('isconfirmed',isconfirmed);
			 	 document.getElementById('inputField7').value=num;//PageManager.DataService.setQueryField('paidAmount',num);
				 //PageManager.DataService.callDataService('journalSearchContractor');
		 }
         else{
			 //PageManager.DataService.callDataService('journalSearchContractor');
				 var statusNotEqual=4;
				document.getElementById('inputField8').value=statusNotEqual; //v.status!=4 

			 }
			 document.journalVoucher.submit(); return;
		}

		if(billType.value=="Salary")
		{

		  if(mode=="edit")
		  {
			 var num=0;
			 document.getElementById('inputField5').value=num;//PageManager.DataService.setQueryField('statusNum',num);
			 document.getElementById('inputField6').value='0';//v.isConfirmed
			 document.getElementById('inputField8').value=''; //v.status!=4 
			 //PageManager.DataService.callDataService('journalSearchSalaryModify');
		  }
		  else if(mode=="modify")
		  {
			 var num=0;
			 document.getElementById('inputField5').value=num;//PageManager.DataService.setQueryField('statusNum',num);
			 var isconfirmed=1;
			 document.getElementById('inputField6').value=isconfirmed;//PageManager.DataService.setQueryField('isconfirmed',isconfirmed);
			 document.getElementById('inputField7').value=num;//PageManager.DataService.setQueryField('paidamount',num);
			 //PageManager.DataService.callDataService('journalSearchSalary');
		  }
		  else{
			//PageManager.DataService.callDataService('journalSearchSalary');
				var statusNotEqual=4;
				document.getElementById('inputField8').value=statusNotEqual; //v.status!=4 
			}
			document.journalVoucher.submit(); return;
	    }

	 }
	if(journalType=="FundToFund")
	{
		if(mode=="edit")
		{
			var confirm=0;
			var stat=0;
			PageManager.DataService.setQueryField('confirmNum',confirm);
			PageManager.DataService.setQueryField('statusNum',stat);

		}
		else if(mode=="modify")
		{
			var confirm=1;
			var stat=0;
			PageManager.DataService.setQueryField('confirmNum',confirm);
			PageManager.DataService.setQueryField('statusNum',stat);

		}
		PageManager.DataService.callDataService('cjSearchFTF');
	}
	if(journalType=="Contra")
	{
		var contraType=document.getElementById('contraType'); 
		if(contraType.value=='Bank To Bank')
		{
			 if(mode=="edit")
			 {
				var confirm=0;
				var stat=0;
				document.getElementById('inputField6').value=confirm;//v.isConfirmed PageManager.DataService.setQueryField('confirmNum',confirm);
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('statusNum',stat);
			 }
			else if(mode=="modify")
			{
				var confirm=1;
				var stat=0;
				document.getElementById('inputField6').value=confirm;//v.isConfirmed  PageManager.DataService.setQueryField('confirmNum',confirm);
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('statusNum',stat);
			}
			//PageManager.DataService.callDataService('cjSearchBTB');
			document.journalVoucher.submit(); return;
		}
		if(contraType.value=='Cash Withdrawal'){

			if(mode=="edit")
		 	{
			//PageManager.DataService.callDataService('cjSearchModifyBTC');
			}
			else if(mode=="modify")
			{
				var confirm=1;
				var stat=0;
				document.getElementById('inputField6').value=confirm;//v.isConfirmed  PageManager.DataService.setQueryField('confirmNum',confirm);
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('statusNum',stat);
				//PageManager.DataService.callDataService('cjSearchBTC');
			}
			//else //PageManager.DataService.callDataService('cjSearchBTC');
			document.journalVoucher.submit(); return;
		}
		if(contraType.value=='Cash Deposit'){

			 if(mode=="edit")
			 {
				//PageManager.DataService.callDataService('cjSearchModifyCTB');
			 }
			else if(mode=="modify")
			{
				var confirm=1;
				var stat=0;
				document.getElementById('inputField6').value=confirm;//v.isConfirmed  PageManager.DataService.setQueryField('confirmNum',confirm);
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('statusNum',stat);
				//PageManager.DataService.callDataService('cjSearchCTB');
			}
			//else	//PageManager.DataService.callDataService('cjSearchCTB');
				document.journalVoucher.submit(); return;
		}
		if(contraType.value=='Inter Fund Transfer'){
			 if(mode=="edit")
			 {
				var confirm=0;
				var stat=0;
				document.getElementById('inputField6').value=confirm;//v.isConfirmed  PageManager.DataService.setQueryField('confirmNum',confirm);
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('statusNum',stat);

			 }
			else if(mode=="modify"){
				var confirm=1;
				var stat=0;
				document.getElementById('inputField6').value=confirm;//v.isConfirmed  PageManager.DataService.setQueryField('confirmNum',confirm);
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('statusNum',stat);

			  }

			document.journalVoucher.submit(); return; //PageManager.DataService.callDataService('cjSearchFTF');
		}
		if(contraType.value=="PayInSlip")
		{
				if(mode=="edit")
				 {
					var confirm=0;
					var stat=0;
					document.getElementById('inputField6').value=confirm;//v.isConfirmed  PageManager.DataService.setQueryField('confirmNum',confirm);
					document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('statusNum',stat);

				 }
				else if(mode=="modify")
				{
				var confirm=1;
				var stat=0;
				document.getElementById('inputField6').value=confirm;//v.isConfirmed  PageManager.DataService.setQueryField('confirmNum',confirm);
				document.getElementById('inputField5').value=stat;// v.status='' PageManager.DataService.setQueryField('statusNum',stat);
				}
				//PageManager.DataService.callDataService('journalSearchPayInSlip');
				document.journalVoucher.submit(); return;
		}


      }
	}
	//var table=document.getElementById('journalSearchGrid');
	//table.style.display="block";

}


function hideColumn(index){
	var table=document.getElementById('journalSearchGrid');
   	for(var i=0;i<table.rows.length;i++){
		table.rows[i].cells[index].style.display="none";
   	}
}
function showColumn(index){
	var table=document.getElementById('journalSearchGrid');
   	for(var i=0;i<table.rows.length;i++){
		table.rows[i].cells[index].style.display="block";
   	}
}
function showAllColumns()
{
	var table=document.getElementById('journalSearchGrid');
   	for(var i=0;i<table.rows.length;i++)
   	{
   		for(var j=0;j<table.rows[i].cells.length;j++)
   		{
			table.rows[i].cells[j].style.display="block";

		}
   	}
}
function onloadTasks()
{
	PageManager.ListService.callListService();
// added by iliyaraja
	PageManager.DataService.callDataService('finYearDate');
	journalType=PageManager.DataService.getQueryField('journalType');
	var sm=PageManager.DataService.getQueryField('showMode')==null?"":PageManager.DataService.getQueryField('showMode');
	sm=(sm=="view")?"View":(sm=="edit")?"Modify":(sm=="modify")?"Reverse":"";
	switch (journalType) {
		case "General":document.getElementById('screenName').innerHTML=sm+" Journal Voucher - Search";break;
		case "Bill":document.getElementById('screenName').innerHTML=sm+" Bills - Search";break;
		case "Contra":document.getElementById('screenName').innerHTML=sm+" Contra - Search";break;
	}
	//if(document.getElementById('contraTypeHead'))
	document.getElementById('contraTypeHead').style.display="none";
	document.getElementById('billTypeHead').style.display="none";
	mode = PageManager.DataService.getQueryField('showMode');
	PageValidator.addCalendars();
	var contraType=document.getElementById('contraType');
	var billType=document.getElementById('billType');
	if(journalType=="Contra")
	{
		document.getElementById('contraTypeHead').style.display="block";
		 			contraType.setAttribute('exilMustEnter','true');
		document.getElementById('contraType').focus();
	}
	if(journalType=="Bill")
	{
		document.getElementById('billTypeHead').style.display="block";
		billType.setAttribute('exilMustEnter','true');
		document.getElementById('billType').focus();
	}
	if(journalType=="Contra" && mode=="view" || mode=="modify" || mode=="edit")
	{
		contraType.remove(4);//remove Fund To Fund Transfer in the dropdown
	}

	//var table=document.getElementById('journalSearchGrid');
	//bootbox.alert(table);
}

function ModeChangeGrid(){
	var table=document.getElementById('journalSearchGrid');
	document.getElementById('gridA').innerHTML="CG Number";
	document.getElementById('gridB').innerHTML="Voucher Date";
	document.getElementById('gridC').innerHTML="Voucher Number";
	showAllColumns();

	var a=document.getElementsByName('colETD');
	for(var i=0; i<a.length; i++)
		a[i].align="left";
	if(journalType=="General"){
		hideColumn(6);
		hideColumn(1);
		hideColumn(2);
		hideColumn(7);

		document.getElementById('gridB').innerHTML="Voucher Number";
		document.getElementById('gridC').innerHTML="Voucher Date";
		document.getElementById('gridD').innerHTML="Fund Name";
		document.getElementById('gridE').innerHTML="Type";
		document.getElementById('gridF').innerHTML="Amount";

	}
	if(journalType=="Bill")
	{
		 var billType=document.getElementById('billType');
	    if(billType.value=="Supplier")
		{
			hideColumn(1);
			hideColumn(6);
			document.getElementById('gridB').innerHTML="Voucher Number";
			document.getElementById('gridC').innerHTML="Supplier";
			document.getElementById('gridD').innerHTML="Bill Number";
			document.getElementById('gridE').innerHTML="Bill Amount";
			document.getElementById('gridF').innerHTML="Passed Amount";
			for(var i=0; i<a.length; i++)
				a[i].align="right";
		}
		if(billType.value=="Contractor")
		{
			hideColumn(1);
			document.getElementById('gridB').innerHTML="Voucher Number";
			document.getElementById('gridC').innerHTML="Contractor";
			document.getElementById('gridD').innerHTML="Work Order Reference";
			document.getElementById('gridE').innerHTML="Bill Amount";
			document.getElementById('gridF').innerHTML="Passed Amount";
			for(var i=0; i<a.length; i++)
			a[i].align="right";
		}
		if(billType.value=='Salary')
		{

			hideColumn(4);
			hideColumn(5);
			hideColumn(1);
			hideColumn(6);
			document.getElementById('gridE').innerHTML="Month";
			document.getElementById('gridB').innerHTML="Voucher Number";
		}
	}
	if(journalType=="FundToFund")
	{
				hideColumn(1);
				hideColumn(4);
				hideColumn(6);
					document.getElementById('gridB').innerHTML="Voucher Number";
				document.getElementById('gridD').innerHTML="From Fund";
				document.getElementById('gridE').innerHTML="To Fund";
	}
	if(journalType=="Contra")
	{
		var contraType=document.getElementById('contraType');
		if(contraType.value=='Bank To Bank')
		{
			hideColumn(1);
			hideColumn(6);
			document.getElementById('gridB').innerHTML="Voucher Number";
			document.getElementById('gridC').innerHTML="From Bank";
			document.getElementById('gridD').innerHTML="To Bank";
			document.getElementById('gridE').innerHTML="Cheque Number";
		}
		if(contraType.value=='Cash Withdrawal')
		{
			hideColumn(1);
			hideColumn(4);
			hideColumn(6);
				document.getElementById('gridB').innerHTML="Voucher Number";
			document.getElementById('gridD').innerHTML="From Bank";
			document.getElementById('gridE').innerHTML="To Cash";
		}
		if(contraType.value=='Cash Deposit')
		{
			hideColumn(1);
			hideColumn(4);
			hideColumn(6);
				document.getElementById('gridB').innerHTML="Voucher Number";
			document.getElementById('gridD').innerHTML="From Cash";
			document.getElementById('gridE').innerHTML="To Bank";
		}
		if(contraType.value=="PayInSlip")
	  	{
			hideColumn(1);
			hideColumn(2);
			hideColumn(6);
				document.getElementById('gridB').innerHTML="Voucher Number";
			document.getElementById('gridB').innerHTML="Pay-In Slip Number";
			document.getElementById('gridC').innerHTML="Pay-In Slip Date";
			document.getElementById('gridD').innerHTML="Cheque Numbers";
			document.getElementById('gridE').innerHTML="Bank Name";
	 	}

   }
}

function beforeRefreshPage(dc){

	if(dc.values['serviceID'] == 'journalSearchPayInSlip')
	{
	  var objt=dc.grids['journalSearchGrid'];
	  for(k=1;k<objt.length;k++)
	  {
	  	window.vNum[k]=objt[k][1];
		countOfVnum++;
	  }

	  PageManager.DataService.callDataService('jSChequePayInSlip');

	}

	if(dc.values['serviceID'] == 'jSChequePayInSlip')
	{
		i=1;
		var str;
		var table=document.getElementById('journalSearchGrid');
		var obj=dc.grids['chequeNumber'];
		for(k=1;k<=countOfVnum;k++)
		{
		 str="";
		  for(j=0;j<obj.length;j++)
		 {
		   	if(window.vNum[k]==obj[j][1])
		 	{
		 	str=str+obj[j][0]+",";
		 	}
		 }
		 	var objec=PageManager.DataService.getControlInBranch(table.rows[k],"colD");
			if(objec != null)
			objec.innerHTML=str;
		}
	}
/*	var t=document.getElementById('gridC').innerHTML;
	var d=document.getElementById('gridH').innerHTML;
	var tabObj=dc.grids['journalSearchGrid'];
	if(!tabObj)	return false;
	if(tabObj.length<2){
			bootbox.alert("No Data");
	}
	if(t && (t=='Voucher Date' || t=='Pay-In Slip Date')||d=='Voucher Date' ){
	 	for(var i=1;i<tabObj.length;i++){
	 			tabObj[i][2]=formatDate(tabObj[i][2]);
		}
	}
	
	var newArray=new Array();
	newArray[0]=tabObj[0];
	var index=1;
	var jType=document.getElementById('journalType');
	if(jType && jType.value=='PayInSlip'){
		var c=0,i=0,j=0,k=0;
		for(i=1;i<tabObj.length-c-1;i++){
			for(j=i+1;j<tabObj.length-c;j++){
				if(tabObj[i][0]==tabObj[j][0] && tabObj[i][1]==tabObj[j][1]){
					tabObj[i][3]=tabObj[i][3]+","+tabObj[j][3];
					newArray[i]=tabObj[i];

					  for(k=j;k<tabObj.length-c-1;k++){
						tabObj[k][0]=tabObj[k+1][0];
						tabObj[k][1]=tabObj[k+1][1];
						tabObj[k][2]=tabObj[k+1][2];
						tabObj[k][3]=tabObj[k+1][3];
						tabObj[k][4]=tabObj[k+1][4];
						tabObj[k][5]=tabObj[k+1][5];
						tabObj[k][6]=tabObj[k+1][6];

					}
					j=j-1;
					c++;
				}
				else
					newArray[i]=tabObj[i];
			}
		}
		newArray[i]=tabObj[i];
		dc.grids['journalSearchGrid']=newArray;


	}
*/
	return true;
}


function changeColor(){
	var table=document.getElementById('journalSearchGrid');
	var j=0;
	for(var i=0;table.rows[0].cells.length;i++){//bootbox.alert((table.rows[0].cells[5].innerHTML).toUpperCase());
		var oo=table.rows[0].cells[i]
		if((oo.innerHTML).toUpperCase()=='STATUS')
		{ j=i; break;}
	}
	
	
	for(var i=1;i<table.rows.length;i++){
			var statusObj=PageManager.DataService.getControlInBranch(table.rows[i],"cgn"); //bootbox.alert(statusObj);
 			var status=table.rows[i].cells[j]; bootbox.alert(status.innerHTML);//.innerHTML; 
 			if(status.innerHTML=='Reversed')
 			{
				var trObj=PageManager.DataService.getRow(statusObj);
 				table.rows[i].className='rowRev';
 				//table.rows[i].backgrount-color="green";
 			}
 			else if(status.innerHTML=='Reversal')
 			{
 				var trObj=PageManager.DataService.getRow(statusObj);
 				table.rows[i].className='rowRev2';
 			}
 			else
 			{
				//var trObj=PageManager.DataService.getRow(statusObj);
 				//table.rows[i].className='smallfieldinput';
			}

  	 	}
}

function afterRefreshPage(dc){
	//addSlNo();
	//changeColor();
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

	//setting financialyear startdate  to dateFrom field
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
}

function addSlNo(){
	var slNo=1;
	var tabObj=document.getElementById("journalSearchGrid");
	for(var i=1;i<tabObj.rows.length;i++){
		var obj=PageManager.DataService.getControlInBranch(tabObj.rows[i],"slNo");
		obj.innerHTML=slNo;
		slNo++;
	}

}

function getDetails(obj,cgn,vStatus,v2,v3,v4){
	var row=PageManager.DataService.getRow(obj); 
	var table=document.getElementById('journalSearchGrid');
	var cgn1=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"colG"); 
	cgn1=cgn;
	var cgn2=cgn1.substring(0,3);
	var vStatus=vStatus;
	var vStatus2=v2;
	var vStatus3=v3;
	var vStatus4=v4;
	var vStatus1=vStatus.substring(0,1);
	switch(cgn2){
	case 'JVG':
	//window.location="JV_General.htm?cgNumber="+cgn1+"&showMode="+mode;
	cWind=window.open("JV_General.htm?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	cWindows[count++]=cWind;
	break;
	case 'SJV' :
	//window.location="SupplierJournal.htm?cgNumber="+cgn1+"&showMode="+mode;
	if(vStatus2=='Reversal')
				{ 			 		
			 		
					cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{					
					
					cWind=window.open("SupplierJournal.htm?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=950,scrollbars=yes,left=20,top=20,status=yes");
				}
	
	cWindows[count++]=cWind;
	break;
	case 'CJV' :
	//window.location="ContractorJournal.htm?cgNumber="+cgn1+"&showMode="+mode;
	if(vStatus=='Reversal')
				{ 			 		
			 		
					cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{					
					
					cWind=window.open("ContractorJournal.htm?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=930,scrollbars=yes,left=20,top=20,status=yes");
	    	}

	cWindows[count++]=cWind;
	break;
	case 'SAL' :
	//window.location="JV_Salary.htm?cgNumber="+cgn1+"&showMode="+mode;
	if(vStatus4=='Reversal')
				{ 			 		
			 		
					cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{					
					
					cWind=window.open("JV_Salary.htm?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
				}

	cWindows[count++]=cWind;
	break;
	case 'PYS':
	//window.location="PayInSlip.htm?cgNumber="+cgn1+"&showMode="+mode+"&vStatus="+vStatus1;
	if(vStatus4=='Reversal')
				{ 			 		
			 		
					cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{					
					
					//cWind=window.open("PayInSlip.htm?cgNumber="+cgn1+"&showMode="+mode+"&vStatus="+vStatus1,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
					cWind=window.open("VMC/PayInSlip_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode+"&vStatus="+vStatus1,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
				}

		cWindows[count++]=cWind;
		break;
	case 'BTB':
	//window.location="JV_Contra_BToB.htm?cgNumber="+cgn1+"&showMode="+mode;
	//cWind=window.open("JV_Contra_BToB.htm?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	if(vStatus3=='Reversal')
				{ 			 		
			 		
					cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{					
					
					cWind=window.open("VMC/JV_Contra_BToB_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
				}

	cWindows[count++]=cWind;
	break;
	case 'BTC':
	//window.location="JV_Contra_BToC.htm?cgNumber="+cgn1+"&showMode="+mode;
	//cWind=window.open("JV_Contra_BToC.htm?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	if(vStatus4=='Reversal')
				{ 			 		
			 		
					cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{					
					
					cWind=window.open("VMC/JV_Contra_BToC_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
				}
		cWindows[count++]=cWind;
		break;
	case 'CTB':
	//window.location="JV_Contra_CToB.htm?cgNumber="+cgn1+"&showMode="+mode;
	//cWind=window.open("JV_Contra_CToB.htm?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	if(vStatus4=='Reversal')
				{ 			 		
			 		
					cWind=window.open("VMC/JV_General_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		         }
		 else 
				{					
					
					cWind=window.open("VMC/JV_Contra_CToB_VMC.jsp?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
				}
		cWindows[count++]=cWind;
		break;
	case 'FTF':
	//window.location="JV_Contra_FToF.htm?cgNumber="+cgn1+"&showMode="+mode;
	cWind=window.open("JV_Contra_FToF.htm?cgNumber="+cgn1+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	cWindows[count++]=cWind;
	break;
	case 'RBE':
	//window.location="BankEntry.htm?cgNumber="+cgn1;
	cWind=window.open("BankEntry.htm?cgNumber="+cgn1,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
	cWindows[count++]=cWind;
	break;
	}
}

</SCRIPT>

</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onloadTasks()" onbeforeunload="closeChilds(cWindows,count)" ><!------------------ Header Begins Begins--------------------->
<table align="center" id="table2"><tr><td><div id="main"><div id="m2"><div id="m3">
<jsp:useBean id="billRptBean"  class="com.exilant.eGov.src.transactions.CommonBean" scope ="request"/>
<jsp:setProperty name="billRptBean" property="inputFieldC"/>
<jsp:setProperty name="billRptBean" property="inputField6"/>

<jsp:setProperty name="billRptBean" property="*"/>
<form name="journalVoucher">
<input type=hidden id=reportType/>
<input type=hidden id="inputField1" name="inputField1"/>
<input type=hidden id="inputField2"  name="inputField2" value="">
<input type=hidden id="inputFieldC" name="inputFieldC" value="">
<input type=hidden id="inputField4"  name="inputField4" value="">
<input type=hidden id="inputField5"  name="inputField5" value="">
<input type=hidden id="inputField6"  name="inputField6" value="">
<input type=hidden id="inputField7"  name="inputField7" value="">
<input type=hidden id="showMode" name="showMode" value="" />
<input type=hidden id="journalType" name="journalType" value="" />
<input type=hidden id="inputField8" name="inputField8">
<input type=hidden id="inputField9" name="inputField9">
<input type="hidden" name="startFinDate" id="startFinDate">

	<table width="100%" border=0 cellpadding="3" cellspacing="0" align ="center">
		<tr class=tableheader>
			<td  valign="center" colspan=4><span  id="screenName" >Search</span></td>

		</tr>
		<tr  width="100%" id="billTypeHead">
		  	<td><div align="right" valign="center" class="labelcell" width="20%" >	Bill Type<span class="leadon">*</span></div></td>
		  	<td width="30%" >
				<!--select class="fieldinput" name="billType" id="billType" exilListSource="billType"  width="20"-->
				<select class="fieldcellwithinputwide" name="billType" id="billType"   width="20" tabindex=1>
				<option value=""></option>
				<option value="Contractor">Contractor</option>
				<option value="Supplier">Supplier</option>
				<option value="Salary">Salary</option>
				</select>
			</td>
			<td  width="20%">&nbsp;</td>
			<td width="30%">&nbsp;</td>
		</tr>
		<tr  width="100%" id="contraTypeHead" >
			<td ><div align="right" valign="center" class="labelcell" width="20%" >Contra Type<span class="leadon">*</span></td></div>
		  	<td  width="30%" ><span class="smallfieldcell">
				<!--select class="fieldinput" name="contraType" id="contraType" exilListSource="contraType" width="20" -->
				<select class="fieldcellwithinputwide" name="contraType" id="contraType"  width="20" tabindex=1>
				<option value=""></option>
				<option value="Bank To Bank">Bank To Bank</option>
				<option value="Cash Withdrawal">Cash Withdrawal</option>
				<option value="Cash Deposit">Cash Deposit</option>
				<option value="Inter Fund Transfer">Inter Fund Transfer</option>
				<option value="PayInSlip">Cheque Deposit</option>
				</select></span>
		  	</td>
		  	<td width="20%">&nbsp;</td>
		  	<td  width="30%">&nbsp;</td>
		</tr>
		<tr  width="100%">
		<td  width="20%">

			<div align="right" valign="center"  ></div> <div align="right"class="labelcell" id="vFrom">Voucher Number </div></td>
		  	<td width="30%">
				<input class="fieldcellwithinputwide" name="voucherNumber" id="voucherNumber" exilDataType="exilAnyChar" size=20 tabindex=1>
		  	</td>
		  	<td  width="20%">
		  	<div class="labelcell" align="right" valign="center" style="align:right"  width="20%"><div align="right"> Fund</div></td>
		  	<td  width="30%"><span class="smallfieldcell">
		  		<SELECT CLASS="fieldcell" NAME="fund" ID="fund" exilListSource="fundNameList" tabindex=1 > </SELECT></span>
		  	</td>
		</tr>
		<tr  width="100%">
			<td ><div align="right" valign="center" class="labelcell" width="20%"id="vDate">Voucher Date From</div></td>
		  	<td  width="30%">
		  		<input class="fieldcellwithinputwide"  name="voucherDateFrom"  id="voucherDateFrom" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" size=20 tabindex=1>
			</td>
		 	<td ><div align="right" valign="center" class="labelcell" width="20%">To</div></td>
			<td  width="30%">
					<input class="fieldcellwithinputwide" name="voucherDateTo"  id="voucherDateTo" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" size=20 tabindex=1>
			</td>
		</tr>
		<tr  width="100%">
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0" ID="Table1">
					<tr>
						<td >
						<input   type="button" class="button" value="Search" tabindex=1 onclick=ButtonPress(); /></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</div></div></div></td></tr></table>

	<table width="100%"  border="0" cellpadding="0" cellspacing="0">
		<tr  >
			<td>
				<input type="hidden" name="displayCondition" id="displayCondition" value="0">
				
			<%
						
					    System.out.print("\n Before submit "+request.getParameter("displayCondition"));
					    LinkedList list=new LinkedList();
					    if(request.getParameter("displayCondition") !=null && request.getParameter("displayCondition").equals("1"))
					    {
						 System.out.print("\n after submit "+request.getParameter("displayCondition"));
							 try{
								 CommonList Bills= new CommonList();	
								 list=Bills.getList(billRptBean);
								// System.out.println("list.size():"+list);
	
									if(list!=null && list.size()>0) 
									{
										request.setAttribute("journalSearchGrid",list);
										try{
							 %>	
					<div  id="tbl-container" class="tbl2-container"  align="center">	
					 <display:table name="journalSearchGrid" id="journalSearchGrid"  export="false" sort="list" pagesize = "15" class="view" cellspacing="0"> 
						
						<display:caption></display:caption>
						<%
							com.exilant.eGov.src.transactions.CommonBean obj=(com.exilant.eGov.src.transactions.CommonBean)pageContext.findAttribute("journalSearchGrid");
							
						%>
						<display:column property="field1" title="<%=billRptBean.getField1()%>" style="width:6px; align=center" media="html"   />		
						<display:column  title="<%=billRptBean.getField2()%>" style="width:90px; align=center">
						<% if(obj.getField2()!=null){%>  <%=obj.getField2()%>  <%}%>
						</display:column>
						<display:column  title="<%=billRptBean.getField3()%>" >	
						<span>
						
						<%
							String vnoCgno=obj.getField3();
							String cgn=vnoCgno.substring(vnoCgno.indexOf("`--`")+"`--`".length(),vnoCgno.length());	
							//System.out.print("\n CGN:"+cgn);
							String vno=vnoCgno.substring(0,vnoCgno.indexOf("`--`"));
							if(vno==null) vno="";
						%>
						<a href="#" id="cgn" onClick="getDetails(this,'<%=cgn%>','<%=obj.getField10()%>','<%=obj.getField9()%>','<%=obj.getField8()%>','<%=obj.getField7()%>')"><%=vno%></a>
						</span>
						
						</display:column>
						<display:column property="field4" title="<%=billRptBean.getField4()%>" >
						
						</display:column>	
						<display:column property="field5"  title="<%=billRptBean.getField5()%>" headerClass="" >
						
						</display:column>	
						<display:column  title="<%=billRptBean.getField6()%>" headerClass=""  >	
						<% if(obj.getField6()!=null){%>  <%=obj.getField6()%>  <%}%>
						</display:column>

						<display:column   title="<%=billRptBean.getField7()%>" headerClass="" >	
						<% if(obj.getField7()!=null){%>  <%=obj.getField7()%>  <%}%>
						</display:column>

						<display:column   title="<%=billRptBean.getField8()%>" headerClass=""  >
						<% if(obj.getField8()!=null){%>  <%=obj.getField8()%>  <%}%>
						</display:column>
						<display:column  title="<%=billRptBean.getField9()%>" headerClass=""   >
						<% if(obj.getField9()!=null){%>  <%=obj.getField9()%>  <%}%>
						</display:column>
						<display:column  title="<%=billRptBean.getField10()%>" headerClass=""   >
						<% if(obj.getField10()!=null){%>  <%=obj.getField10()%>  <%}%>
						</display:column>
						<display:column  title="<%=billRptBean.getField11()%>" headerClass=""   >
						<% if(obj.getField11()!=null){%>  <%=obj.getField11()%>  <%}%>
						</display:column>

						
						 <display:setProperty name="export.pdf" value="true" />
					</display:table>
					</div>

			<%					}catch(Exception e){}		
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
						var table=document.getElementById('journalSearchGrid');  //bootbox.alert(table.innerHTML);
						for (var i=0;i<table.rows.length;i++)
						{	var k=0;
							for (var j=table.rows[i].cells.length-1; j >= k ; j--)
							{
								if(table.rows[i].cells[j].innerHTML == "") 
									table.rows[i].cells[j].style.display="none";
									
							}
						}
						
						//changeColor();
						var table=document.getElementById('journalSearchGrid');
						var j=0;
						for(var i=0;table.rows[0].cells.length;i++){//bootbox.alert((table.rows[0].cells[5].innerHTML).toUpperCase());
							var oo=table.rows[0].cells[i]
							if((oo.innerHTML).toUpperCase()=='STATUS')
							{ j=i; break;}
						}
						
						
						for(var i=1;i<table.rows.length;i++){
								var statusObj=PageManager.DataService.getControlInBranch(table.rows[i],"cgn"); //bootbox.alert(statusObj);
					 			var status=table.rows[i].cells[j];// bootbox.alert(status.innerHTML);//.innerHTML; 
					 			if(status.innerHTML=='Reversed')
					 			{
									var trObj=PageManager.DataService.getRow(statusObj);
					 				table.rows[i].className='rowRev'; //bootbox.alert(table.rows[i]);
					 			}
					 			else if(status.innerHTML=='Reversal')
					 			{
					 				var trObj=PageManager.DataService.getRow(statusObj);
					 				table.rows[i].className='rowRev2';
					 			}
					 			else
					 			{
									var trObj=PageManager.DataService.getRow(statusObj);
					 				//table.rows[i].className='no';
								}
					
					  	 	}

					</script>
				<%}%>	
			</td>
		</tr>
	</table> 
</form>
</body>
</html>

