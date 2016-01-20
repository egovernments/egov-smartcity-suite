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
<script language="javascript">
var sm;
var sIndex,sValue,sText;
var globalSupConTypeIndex=0;
var field12 = null;
var field12Fund = null;
var globalA4 = null;

		function hideButtons(){
			//document.getElementById('modGrid').style.display='none';
			document.getElementById('modGrid').style.display='block';
			document.getElementById('submitGrid').style.display ="none";
			//document.getElementById ('text1').style.display ="none";
		}
		function hideBut(){

					document.getElementById('modGrid').style.display="none";
					document.getElementById('submitGrid').style.display ="block";
		}
function hideColumn(index,hide)
{
	var table=document.getElementById('Data_Grid');
   	for(var i=0;i<table.rows.length;i++){
   		if(hide)
   			table.rows[i].cells[index].style.display="none";
   		else
   			table.rows[i].cells[index].style.display="block";
   	}
}
function hideColumn1(index,hide)
{
	var table=document.getElementById('subledgerPaymentSearchGrid');
   	for(var i=0;i<table.rows.length;i++){
   		if(hide)
   			table.rows[i].cells[index].style.display="none";
   		else
   			table.rows[i].cells[index].style.display="block";
   	}
}

function loadWorkOrderData(obj){
	if(obj.selectedIndex==-1)return;
	clearCombo('subLedgerPaymentHeader_bankId');
	clearCombo('subLedgerPaymentHeader_branchAccountId');
	var workOrderID=obj.options[obj.selectedIndex].value;
	//PageManager.DataService.setQueryField('workOrderID',workOrderID);
	//document.getElementById("worksDetail_advanceAmount").innerHTML="";
	PageManager.DataService.callDataService('maxAdvance');
	PageManager.DataService.callDataService('WorkFundBank');
}

function onLoadTasks() {
	//bootbox.alert("loading");
	PageValidator.addCalendars();
	// added by iliyaraja
	var formSubmitted='<%= request.getParameter("formSubmitted") %>';
	if(formSubmitted == null || formSubmitted == 'null')
		PageManager.DataService.callDataService('finYearDate');
	insertDates1('voucherHeader_voucherDate');
 	var drillDownCgno=PageManager.DataService.getQueryField('drillDownCgn');
	sm=PageManager.DataService.getQueryField('showMode');
	//bootbox.alert("mode:"+sm+" drill "+drillDownCgno);
	hideColumn(13,true);
	hideColumn(3,true);
	if(drillDownCgno )
	{ //bootbox.alert(sm+"111");
		if(sm=="edit"){
		document.getElementById('filterRow3').style.display ="none";
		document.getElementById('filterRow4').style.display ="none";
	 		window.cgnSet=true;
			window.data=true;
			window.searchData=true;

			//var wObj=document.getElementById('worksDetail_id');
			//wObj.setAttribute('exilListSource','worksDetail_id');
			var rowsToHide=document.getElementById('filterRow1');
			rowsToHide.style.display="none";
			rowsToHide=document.getElementById('filterRow2');
			rowsToHide.style.display="none";
			PageManager.DataService.callDataService('getWorkDetailIdList');
			var obj=document.getElementById('trnHeader');
			obj.style.display="block";


			document.getElementById('voucherHeader_voucherNumber').setAttribute('exilDataType','exilAlphaNumeric');
			document.getElementById('modGrid').style.display='none';
			document.getElementById('submitGrid').style.display ='block';
			document.getElementById('saveandnew').style.display ="none";
			document.getElementById('saveandnewlb').style.display ="none";
			document.getElementById('saveandnewrb').style.display ="none";
			document.getElementById('cgnLabel').style.display='block';
			document.getElementById('cgnText').style.display='block';
			document.getElementById('backbutton').style.display='none';
//			document.forms[0].voucherHeader_voucherNumber.disabled=true;
			subLedgerPayment_Header.pay_type.disabled=true;
			//subLedgerPayment_Header.worksDetail_id.disabled=true;
			subLedgerPayment_Header.payTo.disabled=true;

			document.getElementById('screenName').innerHTML="Pay Supplier/Contractor -Modify";
			document.getElementById('modeOfExec').value="edit";
		}
		else{
			window.cgnSet=true;
			window.data=true;
			window.searchData=true;
			disableControls(0,true);
			//var wObj=document.getElementById('worksDetail_id');
				//wObj.setAttribute('exilListSource','worksDetail_id');
				//santhu
			document.getElementById('bankbal').style.display='block';
			//bootbox.alert(document.getElementById('bankbal').style.display);
			document.getElementById('submitGrid').style.display ="none";
			document.getElementById('saveandnew').style.display ="none";
			var rowsToHide=document.getElementById('filterRow1');
			rowsToHide.style.display="none";
			rowsToHide=document.getElementById('filterRow2');
			rowsToHide.style.display="none";
			PageManager.DataService.callDataService('getWorkDetailIdList');

			var obj=document.getElementById('trnHeader');
			obj.style.display="block";
			document.getElementById('cgnLabel').style.display='block';
			document.getElementById('cgnText').style.display='block';
			document.getElementById('voucherHeader_voucherNumber').setAttribute('exilDataType','exilAlphaNumeric');
			if(sm=="view"){
				document.getElementById('modGrid').style.display='none';
				document.getElementById('backbutton').style.display='block';
				document.getElementById('filterRow3').style.display ="none";

			}
			else if(sm=="modify"){
				document.getElementById('modGrid').style.display='block';
				document.getElementById('backbutton').style.display="none";
					}


	 	}
		document.getElementById('paySupConHeader').style.display='none';
	}

	if(!drillDownCgno)
	{  document.getElementById('payTo').disabled="true";
		var i=0;
		if(sm=="createPay"){
			document.getElementById('filterRow3').style.display ="none";
			document.getElementById('filterRow4').style.display ="none";
			var fundid=PageManager.DataService.getQueryField('funid');
			var fundsourceid=PageManager.DataService.getQueryField('fundsourceid');
			var worksid=PageManager.DataService.getQueryField('worksdetaild');
			worksid=worksid.substring(0,(worksid.length-1))
			var type=PageManager.DataService.getQueryField('type');
			document.getElementById('filterRow2').style.display='none';
			var obj1=document.getElementsByName("pay_type")
			var typeval=PageManager.DataService.getQueryField('typeval');
			if(PageManager.DataService.getQueryField('typeval')=="Contractor")
				obj1[0].checked=true;
			else
				obj1[1].checked=true;

			var pt=document.getElementById("payTo");
			pt.options.length=1;
			pt.options[0]=new Option(PageManager.DataService.getQueryField("conorsupidname"),PageManager.DataService.getQueryField("conorsupid"))
			//document.getElementById("payTo").value=PageManager.DataService.getQueryField("conorsupid");
			document.getElementById('pay_type').value=PageManager.DataService.getQueryField('type');
			//document.getElementById('worksDetail_id').value=PageManager.DataService.getQueryField('worksidname');
			document.getElementById('fund_id').value=PageManager.DataService.getQueryField('funid');
			document.getElementById('fundSource_id').value=PageManager.DataService.getQueryField('fundsourceid');
			document.getElementById('fund_name').value=PageManager.DataService.getQueryField('fundname');
			document.getElementById('fundSource_name').value=PageManager.DataService.getQueryField('fsname');
			document.getElementById('voucherDateFrom').value=formatDateToDDMMYYYY4(PageManager.DataService.getQueryField('dt1'));
			document.getElementById('voucherDateTo').value=formatDateToDDMMYYYY4(PageManager.DataService.getQueryField('dt2'));
			//PageManager.DataService.setQueryField('id',PageManager.DataService.getQueryField("conorsupid"));
			PageManager.DataService.setQueryField('fundid',fundid);
			PageManager.DataService.setQueryField('worksDetailId',worksid);
			document.getElementById('pay_type').value=PageManager.DataService.getQueryField('typeval');
			PageManager.DataService.setQueryField('fromDate',PageManager.DataService.getQueryField("dt1"));
			PageManager.DataService.setQueryField('toDate',PageManager.DataService.getQueryField("dt2"));

			if(typeval=='Contractor'){
			PageManager.DataService.callDataService('contractorSubLedgerPayment');
			}
			else
			{
			PageManager.DataService.callDataService('supplierSubLedgerPayment');
			}
			document.getElementById('trnHeader').style.display='block';
			/*document.getElementById('pay_type').disabled =true;
			//document.getElementById('worksDetail_id').disabled =true;
			document.getElementById('fund_name').disabled =true;
			document.getElementById('fundSource_name').disabled =true;
			document.getElementById('payTo').disabled =true;
			document.getElementById('voucherDateFrom').disabled =true;
			document.getElementById('voucherDateTo').disabled =true;
			*/
			document.getElementById('bSelect').focus();
		}
		if(sm=="searchPay"){
				document.getElementById('voucherDateFrom').focus();
				document.getElementById('secondrow').style.display='none';
				document.getElementById('fourthrow').style.display='none';
				document.getElementById('payTo').style.display='none';
				document.getElementById('fundSource_name').style.display='none';

				field12='<%=request.getParameter("inputField12") %>'; //bootbox.alert("field12:"+field12);
				if(field12!=null && field12!='null' )
				{
					var a=new Array();
					a=field12.split("`--`");
				 	if(a[0] && a[0] !='null' ) document.getElementById('voucherDateFrom').value=formatDateToDDMMYYYY5(a[0]);
					if(a[1] && a[1] !='null' ) document.getElementById('voucherDateTo').value=formatDateToDDMMYYYY5(a[1]);
					if(a[2] && a[2] !='null' ) document.getElementById('SupConType').selectedIndex=a[2];//bootbox.alert("field12:"+field12);
					//if(a[4] && a[4] !='null' ) document.getElementById('subledgerPay_fund').selectedIndex=a[2];
					globalA4=a[4];
					PageManager.DataService.setQueryField('SupConTypeCode',a[2]);
					PageManager.DataService.callDataService('getSupConName');
				}
			}
	 	 	hideBut();
	 	 	document.getElementById('backbutton').style.display="none";

			window.cgnSet=false;
			window.data=false;
			window.searchData=false;
			PageManager.DataService.setQueryField('type','Cashier');
			var type_code = PageManager.DataService.getQueryField('chk_type');
			//bootbox.alert(type_code);
			if (!type_code) type_code="Contractor";
			var obj=document.getElementsByName("pay_type")
			for(var i=0;i<obj.length;i++) {
					if(type_code.toLowerCase()==obj[i].value.toLowerCase())	{
						if(obj[i].checked==false)
							obj[i].checked=true;
						break;
					}
			}

			PageManager.DataService.setQueryField('tableNameForId','relation');
			if(sm=="createPay" && typeval=='Contractor'){
			//bootbox.alert(typeval);
			PageManager.DataService.setQueryField('relationTypeId',"2");
			}
			else
			{
			PageManager.DataService.setQueryField('relationTypeId',"1");
			}
			PageManager.DataService.callDataService('subLedgerPayment');

	}
}



	function afterRefreshPage(dc) {
		/*if(!window.data) {
			getData();
			getAccountNumber();
			window.data=true;
		}*/
		if(dc.values['serviceID']=='finYearDate')
			{
			var dt=dc.values['startFinDate']
			dt=formatDate2(dt);
			document.getElementById('voucherDateFrom').value=dt;
			}
		if(dc.values['serviceID']=="getSupConName")
		{
			var csnameObj=document.getElementById("SupConName");
			if(field12!=null && field12!='null' )
			{
				var a=new Array();
				a=field12.split("`--`");
				if(a[0] && a[0] !='null') document.getElementById("voucherDateFrom").value = formatDateToDDMMYYYY5(a[0]);
				//if(a[1] && a[1] !='null') document.getElementById("voucherDateFrom").value = formatDateToDDMMYYYY5(a[1]);
				if(a[2] && a[2] !='null' && a[2] !='')
				{
					 document.getElementById("SupConType").selectedIndex=a[2];

				//try{
						clearCombo('SupConName');
						var conSupList=new Array();
						conSupList=dc.grids['conSupList'];
						var k;
						for(var i=1;i<=conSupList.length;i++)
						{
							csnameObj.options[i]=new Option(conSupList[i-1][1],conSupList[i-1][0]);
							if(a[3] && a[3] !='null' || a[3] !='')
							{
								if(a[3]==conSupList[i-1][0]){ csnameObj.selectedIndex=i;}
							}
						}

					//}catch(err) {}
				}
				field12=null;
			}
			else
			{
				clearCombo('SupConName');
				var conSupList=new Array();
				conSupList=dc.grids['conSupList'];
				for(var i=1;i<=conSupList.length;i++)
					csnameObj.options[i]=new Option(conSupList[i-1][1],conSupList[i-1][0]);
				csnameObj.selectedIndex=0;
			}
		}

		if(dc.values['serviceID']=="AccountFundMap1")
		{//bootbox.alert("service id:"+dc.values['fund1_id']);
		   document.getElementById("fundSub_id").value=dc.values['fund1_id'];
			//PageManager.DataService.setQueryField('fund1_id',;
		}

		if(dc.values['serviceID']=="loadSubLedgerData")
		{
			document.getElementById('relationTypeId').value=dc.values['payTo'];
		}
		var sm=PageManager.DataService.getQueryField('showMode');
		if (sm=='edit' && dc.values['serviceID']=='loadSubLedgerData' )
		{
			document.getElementById("voucherHeader_voucherNumberPrefix").style.display="block";
			var vnum=dc.values['voucherHeader_voucherNumber'];
			var subVnum=vnum.substring(0,2);
			document.getElementById("voucherHeader_voucherNumber").value=vnum.substring(2,vnum.length);
			document.getElementById("voucherHeader_voucherNumberPrefix").value=subVnum;

			document.getElementById("jv_voucherNumberPrefix").style.display="block";
			if(dc.values['jv_voucherNumber'])
			{
				var vnum=dc.values['jv_voucherNumber'];
				var subVnum=vnum.substring(0,2);
				document.getElementById("jv_voucherNumber").value=vnum.substring(2,vnum.length);
				document.getElementById("jv_voucherNumberPrefix").value=subVnum;
			}
			if(document.getElementById("oldVoucherDate").value == "")
			{
				document.getElementById("oldVoucherDate").value=formatDateToDDMMYYYY6(dc.values['voucherHeader_voucherDate']);
		 	}
		 	//bootbox.alert(document.getElementById("oldVoucherDate").value);
		}

		addSlNo();

		if(dc.values['serviceID']=="subLedgerPayment")
		{
			var dt=dc.values['databaseDate']
			document.getElementById('voucherHeader_voucherDate').value=dt;
			//bootbox.alert(dc.values['relationTypeId']);
			document.getElementById('relationTypeId').value=dc.values['relationTypeId'];

			//filling fundsource dropdown
			var fsReq='<%=request.getParameter("fundSource_name1")%>'; 			//bootbox.alert("fsReq:"+fsReq);
			var fsobj=document.getElementById('fundSource_name');
			clearCombo('fundSource_name');
			var fs=new Array();
			fsList=dc.grids['fundSourceNameList'];
			for(var i=1;i<=fsList.length;i++)
			{
				fsobj.options[i]=new Option(fsList[i-1][1],fsList[i-1][0]);
				if(fsReq && fsReq !=null && fsReq !='null')
				{
					if(fsList[i-1][1]==fsReq){ fsobj.selectedIndex=i;}
				}
			}
		//for filling fund dropdown
			var fsobj=document.getElementById('subledgerPay_fund');
			clearCombo('subledgerPay_fund');
			var fsList=new Array();
			fsList=dc.grids['fundNameList'];
			fsobj.selectedIndex=0; //bootbox.alert("a4:"+a4);
			for(var i=1;i<=fsList.length;i++)
			{
				fsobj.options[i]=new Option(fsList[i-1][1],fsList[i-1][0]);
				if(globalA4 && globalA4 !=null && globalA4 !='null')
				{
					if(fsList[i-1][0]==globalA4 ){ fsobj.selectedIndex=i; globalA4 =null;}
				}

			}



		}

			if(dc.values['serviceID']=='contraBalNumberF')
			{
			document.getElementById('bank_bal').value=dc.values['balAvailable'];
			}
		if(dc.values['serviceID']=="supplierSubLedgerPayment" || dc.values['serviceID']=="contractorSubLedgerPayment"){
		document.getElementById('voucherHeader_voucherDate').value=document.getElementById('databaseDate').value;
		}

		//var dt=dc.values['databaseDate'];
			   //  if(dt && (dc.values['serviceID']=='ReceiptPropertyTaxData' && dc.values['serviceID']!='getPTFDetails')){
		 			//document.getElementById('voucherHeader_voucherDate').value=document.getElementById('databaseDate').value;

		 		//}
		var drillDownCgno=PageManager.DataService.getQueryField('drillDownCgn');
		if(drillDownCgno && (dc.values['serviceID']=="loadSubLedgerData"))
		{
			var paymentType=dc.values['pay_type'];
			//bootbox.alert(paymentType);
			var obj=document.getElementsByName("pay_type");
			for(var i=0;i<obj.length;i++) {
					if(paymentType==obj[i].value.toLowerCase())	{
						if(obj[i].checked==false)
							obj[i].checked=true;
						break;
					}
			}
		}

		/*if(dc.values['serviceID']=='contractorSubLedgerPayment' || dc.values['serviceID']=='supplierSubLedgerPayment' ){
			var control=document.getElementById("pay_hide");
			if(control.value.toLowerCase()=="contractor") {
				hideColumn(1,true);
				hideColumn(2,true);
			} else {
				hideColumn(1,false);
				hideColumn(2,false);
			}
		}else */
		if(dc.values['serviceID']=='loadSubLedgerData'){
			calcTotal();
			var voucherHeader_voucherDate=dc.values['voucherHeader_voucherDate'];
			var voucherDate=formatDateToDDMMYYYY6(voucherHeader_voucherDate);
			document.getElementById('voucherHeader_voucherDate').value=voucherDate;
			loadBankBalance();
		}
		var voucherHeader_voucherDate=dc.values['voucherHeader_voucherDate'];
		if(dc.values['serviceID']!='contraBalNumberF' && dc.values['serviceID']!='contraAccNumber'){
	if(voucherHeader_voucherDate){
	var voucherDate=formatDateToDDMMYYYY6(voucherHeader_voucherDate);
	document.getElementById('voucherHeader_voucherDate').value=voucherDate;
			}
	}

	var chequeDetail_chequeDate=dc.values['chequeDetail_chequeDate'];
	if(chequeDetail_chequeDate){
	chequeDetail_chequeDate=formatDateToDDMMYYYY6(chequeDetail_chequeDate);
	document.getElementById('chequeDetail_chequeDate').value=chequeDetail_chequeDate;
	}

	if(sm=="createPay"){
		if(PageManager.DataService.getQueryField("dt1")!="" && PageManager.DataService.getQueryField("dt2")=="")

		document.getElementById('voucherDateTo').value=document.getElementById('dat').value;

		//var wd=document.getElementById('worksDetail_id');
		//wd.options.length=0;
		//wd.options[0]=new Option(PageManager.DataService.getQueryField("worksidname"),PageManager.DataService.getQueryField("worksdetaild"));
		//wd.selectedIndex=0;

		}
		if(sm=="view" || sm=="modify"){//santhu
		document.getElementById('bankbal').style.display='block';
		/*	var contorsupp=dc.values['cont_supp_type'];
			var obj1=document.getElementsByName("pay_type");
			if(contorsupp==1)
				obj1[1].checked=true;
			else
				obj1[0].checked=true;*/
		}
	}



	function beforeRefreshPage(dc) {
		/*	if(dc.values['serviceID']=='finYearDate')
			{
			var dt=dc.values['startFinDate']
			dt=formatDate2(dt);
			document.getElementById('voucherDateFrom').value=dt;
			}
*/
		if('supplierSubLedgerPayment'==dc.values['serviceID'] || 'contractorSubLedgerPayment'==dc.values['serviceID'])
				{

					var obj=document.getElementById('trnHeader');
					if(dc.grids['Data_Grid']) {
						var len=dc.grids['Data_Grid'].length;
						if(len>=2) {
							obj.style.display="block";
							var tabObj=dc.grids['Data_Grid'];
							for(var i=1;i<len;i++)
							{

								tabObj[i][2]=formatDate(tabObj[i][2]);
							}
						} else {
							obj.style.display="none";
							bootbox.alert('No Bills to pay');
						}
					}
		}

		if('getWorkDetailIdList'==dc.values['serviceID'])
		{
			var obj=dc.grids['WorkDetailIdList'];
			var len=dc.grids['WorkDetailIdList'].length;
			var workOrderID='';
			for(var i=0;i<len;i++)
			{
				workOrderID=workOrderID+obj[i][0]+",";

			}
			workOrderID=workOrderID.substring(0,(workOrderID.length-1))

			PageManager.DataService.setQueryField('workOrderID',workOrderID);
			PageManager.DataService.callDataService('loadSubLedgerData');
		}
		if(dc.values['glCodeName'])
				{
				  glNameCode=dc.values['glCodeName'];
				 showEntry();
				}
		if(!window.cgnSet) {
			if(isNaN(parseInt(dc.values['voucherHeader_cgn']))) return;
			dc.values['voucherHeader_cgn']='SPH'+dc.values['voucherHeader_cgn'];
			window.cgnSet=true;
		}
		if(dc.values['serviceID']=='loadSubLedgerData'){
			dc.values['subLedgerPaymentHeader_typeOfPayment']=dc.values['typeOfPayment'];
			dc.values['subLedgerPaymentHeader_branchAccountId']=dc.values['branchAccountId'];
		}
		if(dc.values['serviceID']=='loadSubLedgerData'){

					if(dc.grids['Data_Grid'].length==2)
					{
							//bootbox.alert("grid length is 1");

							subLedgerPayment_Header.billSelect.disabled=true;

					}
			}


		if(!window.data){
			var pay_code=PageManager.DataService.getQueryField('pay_id');
			if(pay_code) {
				dc.values['payTo']=pay_code;
			}
			window.data=true;
		}
	if(dc.values['serviceID']=='contraBalNumberF')
		PageManager.DataService.removeQueryField('voucherHeader_voucherDate');
	}

	function hide() {
		var obj=document.getElementById('trnHeader');
		obj.style.display="none";
		loadWorkOrderList();
	}
function clearCombo(cId)
{
   var bCtrl=document.getElementById(cId);
	try{
	   for(var i=bCtrl.options.length-1;i>=0;i--)
	   {
	   	bCtrl.remove(i);
	   }
	   }catch(err) {}
   //document.getElementById("worksDetail_advanceAmount").innerHTML="";
}

	/*function check(obj)	{
		if(obj.value=="Cash") {
			subLedgerPayment_Header.chequeDetail_chequeNumber.value="";
			subLedgerPayment_Header.chequeDetail_chequeDate.value="";
			subLedgerPayment_Header.chequeDetail_chequeNumber.removeAttribute('exilMustEnter');
			subLedgerPayment_Header.chequeDetail_chequeDate.removeAttribute('exilMustEnter');
			subLedgerPayment_Header.chequeDetail_chequeNumber.disabled=true;
			subLedgerPayment_Header.chequeDetail_chequeDate.disabled=true;
			document.getElementById("subLedgerPaymentHeader_bankId").removeAttribute('exilMustEnter');
			document.getElementById("subLedgerPaymentHeader_branchAccountId").removeAttribute('exilMustEnter');
			document.getElementById("showHide").rows[4].style.display="none";
			document.getElementById("showHide").rows[5].style.display="none";
		} else {
			subLedgerPayment_Header.chequeDetail_chequeNumber.disabled=false;
			subLedgerPayment_Header.chequeDetail_chequeDate.disabled=false;
			subLedgerPayment_Header.chequeDetail_chequeNumber.setAttribute('exilMustEnter','true');
			subLedgerPayment_Header.chequeDetail_chequeDate.setAttribute('exilMustEnter','true');
			document.getElementById("subLedgerPaymentHeader_bankId").setAttribute('exilMustEnter','true');
			document.getElementById("subLedgerPaymentHeader_branchAccountId").setAttribute('exilMustEnter','true');
			document.getElementById("showHide").rows[4].style.display="block";
			document.getElementById("showHide").rows[5].style.display="block";
		}
	}*/



	function addSlNo(){
		var slNo=1;
		var tabObj=document.getElementById("Data_Grid");
		for(var i=1;i<tabObj.rows.length;i++){
			var obj=PageManager.DataService.getControlInBranch(tabObj.rows[i],"slNo");
			obj.innerHTML=slNo;
			slNo++;
		}
	}

	function getGrid() {
			 		var dbDate=document.getElementById('databaseDate').value;
					var vhDate=document.getElementById('voucherDateTo').value;
					if(compareDate(formatDateToDDMMYYYY1(vhDate),formatDateToDDMMYYYY1(dbDate)) == -1 )
					{
						bootbox.alert('Bill Date to should be less than or equal to '+dbDate);
					document.getElementById('voucherDateTo').focus();
					return false;
					}
					var strtDate = document.getElementById('voucherDateFrom').value;
	 				var endDate = document.getElementById('voucherDateTo').value;
					if(strtDate.length !=0 && endDate.length !=0)
					{
							if( compareDate(formatDateToDDMMYYYY1(strtDate),formatDateToDDMMYYYY1(endDate)) == -1 ){
								bootbox.alert('Voucher Date From cannot be greater than Voucher Date To');
							document.getElementById('voucherDateFrom').focus();
							return false;
	   				}
	   				}


	if(sm=='searchPay')
	{
		var obj =document.getElementById('SupConName');
			if (obj.selectedIndex==-1) id='';
			else{
			var id = obj.options[obj.selectedIndex].value;
			}

	var t1="null";var t2="null";var t3="null";var t4="null";var t5="null";
	if(subLedgerPayment_Header.voucherDateFrom.value !='') 	t1=formatDate1(subLedgerPayment_Header.voucherDateFrom.value);
	if(subLedgerPayment_Header.voucherDateTo.value !='')  t2=formatDate1(subLedgerPayment_Header.voucherDateTo.value);
	if(subLedgerPayment_Header.SupConType.value !='')  t3=subLedgerPayment_Header.SupConType.value;
	if(id !='') t4=id;
	if(subLedgerPayment_Header.subledgerPay_fund.value !='')  t5=subLedgerPayment_Header.subledgerPay_fund.value; //bootbox.alert("t5:"+t5);
	subLedgerPayment_Header.inputField12.value=t1+"`--`"+t2+"`--`"+t3+"`--`"+t4+"`--`"+t5;
	subLedgerPayment_Header.displayCondition.value=1;
	document.getElementById('paySupConHeader').style.display='block';
	document.subLedgerPayment_Header.submit(); return;
	}
	else{
		var chk=document.getElementById('payTo');
		if(!PageValidator.validateElement(chk)) return;

		<!--Added by siddhu-->
			var fundList=document.getElementById("fund_id");
			if(!PageValidator.validateElement(fundList)) return;
			//var fundid=fundList.options[fundList.selectedIndex].value;
			var fundid=fundList.value;
			PageManager.DataService.setQueryField('fundid',fundid);

			//var wrkDetObj=document.getElementById("worksDetail_id");
			//if(!PageValidator.validateElement(wrkDetObj)) return;
			//var wrkDetId=wrkDetObj.options[wrkDetObj.selectedIndex].value;
			//PageManager.DataService.setQueryField('worksDetailId',wrkDetId);
		<!--Added by siddhu-->

		chk=document.getElementById('voucherDateTo');
		if(chk.value.length>0)
		    if(!PageValidator.validateFromTo(chk)) return;
		var control= document.getElementById('payTo');
		var payToObj=document.getElementById("payTo");
		if(payToObj.selectedIndex==-1)return;
		var splId=payToObj.options[payToObj.selectedIndex].value;
		//PageManager.DataService.setQueryField('id',splId);
		PageManager.DataService.setQueryField('fromDate',formatDate1(subLedgerPayment_Header.voucherDateFrom.value));
		PageManager.DataService.setQueryField('toDate',formatDate1(subLedgerPayment_Header.voucherDateTo.value));

		var ctrl= document.getElementById('pay_hide');
		if(ctrl.value.toLowerCase()=="contractor"){
			PageManager.DataService.callDataService('contractorSubLedgerPayment');
		} else {
			PageManager.DataService.callDataService('supplierSubLedgerPayment');
		}
		PageManager.DataService.removeQueryField('fromDate');
		PageManager.DataService.removeQueryField('toDate');
		PageManager.DataService.removeQueryField('fundid');
		PageManager.DataService.removeQueryField('worksDetailId');
	}
}
	function selUnSelAll() { // For Selecting/notSelecting all Bills.
		var table=document.getElementById('Data_Grid');
		var chkBox,hchkBox;
		hchkBox=PageManager.DataService.getControlInBranch(table.rows[0],"bSelect");
		for(var i=1;i<table.rows.length;i++) {
			chkBox=PageManager.DataService.getControlInBranch(table.rows[i],"billSelect");
			if(hchkBox.checked) chkBox.checked=true;
			else chkBox.checked=false;
		}
			calcTotal();
	}

	function getAccountNumber()	{
	sIndex=null;
	document.getElementById("acc_Desc").value="";
 		var control= document.getElementById('subLedgerPaymentHeader_bankId');
		if(control.selectedIndex != -1 && control.value.length>0) {
			var obj=document.getElementById('subLedgerPaymentHeader_branchAccountId');
			for(i=obj.length-1;i>=0;i--) obj.remove(i);
 			obj.setAttribute('exilListSource','accountListFrom');
 			PageManager.DataService.setQueryField('branchId',control.value);
 			//PageManager.DataService.setQueryField('fundValue',document.getElementById('fund_id').value);
			PageManager.DataService.callDataService('contraAccNumber');
		}
 	}

	function calcTotal() {
	    var chkBox;
		var tAmountObj;
		var tAmount=0,temp;
		var table=document.getElementById('Data_Grid');
		for(var i=1;i<table.rows.length;i++) {
			chkBox=PageManager.DataService.getControlInBranch(table.rows[i],"billSelect");
			if(chkBox.checked) {
				obj=PageManager.DataService.getControlInBranch(table.rows[i],"billSelectHidden");
				obj.value="ON";
				tAmountObj=PageManager.DataService.getControlInBranch(table.rows[i],"slph_paidAmount");
				temp=parseFloat(tAmountObj.value);
				if(!isNaN(temp)) tAmount+=temp;
			} else {
			obj=PageManager.DataService.getControlInBranch(table.rows[i],"billSelectHidden");
			obj.value="OFF";
			}
		}
		tAmountObj=document.getElementById('total');
		if(!isNaN(tAmount)){
			tAmount=Math.round(tAmount*100)/100 ;
			tAmountObj.value=tAmount;}
	}

	function verify(obj) { // Verification for Total Amount entered not to exceed then the limit.
		var row=PageManager.DataService.getRow(obj);
		var table=document.getElementById('Data_Grid');
		var or=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"otherRecoveries");
		var net=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"net");
		var ePay=PageManager.DataService.getControlInBranch(table.rows[row.rowIndex],"earlierPayment");
		if(parseFloat(obj.value) > (parseFloat(net.innerHTML)-parseFloat(ePay.innerHTML))) {
			bootbox.alert('Cannot pay Amount more than : '+(parseFloat(net.innerHTML)-parseFloat(ePay.innerHTML)));
			obj.value=parseFloat(net.innerHTML)-parseFloat(ePay.innerHTML);
			obj.focus();
			return;
		}
		if(parseFloat(obj.value)<=0) {
			bootbox.alert('Enter amount greater than zero');
			obj.value=parseFloat(net.innerHTML)-parseFloat(ePay.innerHTML);
			obj.focus();
			return;
		}
		calcTotal();
	}

	function getInHandId() {
		var control= document.getElementById('paidBy');
		if(control.selectedIndex==-1)return;
		PageManager.DataService.setQueryField('id',control.options[control.selectedIndex].value);
		//PageManager.DataService.callDataService('inHandId');
		if(sm=="createPay"){
		//var worksid=PageManager.DataService.getQueryField('worksdetaild');
		//	worksid=worksid.substring(0,(worksid.length-1))
	//PageManager.DataService.setQueryField('workOrderID',worksid);
	//document.getElementById("worksDetail_advanceAmount").innerHTML="";
		//	PageManager.DataService.callDataService('maxAdvance');
	PageManager.DataService.callDataService('GetBankForFund');

}
	}

	function ButtonPress(name) {
		document.getElementById('payTo').disabled="false";
		 var sm=PageManager.DataService.getQueryField('showMode');
		 //bootbox.alert(sm)
		 if(sm!='modify')
		 {
			 if(!checkFund())
				return false;
		     if(!checkBalance())
		  		   return false;
		  }
		   if(sm=='modify')
		 {
			if(!checkFundReverse())
				return false;
		 }
		var drillDownCgno=PageManager.DataService.getQueryField('drillDownCgn');
		try {
			if(!drillDownCgno)
			{
					document.getElementById('pay_type').disabled =false;
					//document.getElementById('worksDetail_id').disabled =false;
					document.getElementById('fund_name').disabled =false;
					document.getElementById('fundSource_name').disabled =false;
					document.getElementById('payTo').disabled =false;
					document.getElementById('voucherDateFrom').disabled =false;
					document.getElementById('voucherDateTo').disabled =false;
					if(!PageValidator.validateForm()) return;
					var modeOfPym=getSelectedRadioValue(document.getElementsByName('subLedgerPaymentHeader_typeOfPayment'));
					if(modeOfPym.toLowerCase()=="cheque") {
						if(!checkChequeDate("chequeDetail_chequeDate"))return;
					}
					if(subLedgerPayment_Header.subLedger_narration.value.length>250) {
						bootbox.alert('Enter Narration less than 250 Charaters');
							subLedgerPayment_Header.subLedger_narration.focus();
							return;
					}
					if(parseFloat(subLedgerPayment_Header.total.value)<=0) {
						bootbox.alert("Select some bills to pay");
						return;
					}

			}
		}
		catch(e)
		{bootbox.alert('Nothing to Pay'); return;}
		if(drillDownCgno)
		{
						disableControls(0,false);

		}
				document.getElementById('voucherDateTo').setAttribute('exilDataType','');
		 		var dbDate=document.getElementById('databaseDate').value;
				var vhDate=document.getElementById('voucherHeader_voucherDate').value;
				if(compareDate(formatDateToDDMMYYYY1(vhDate),formatDateToDDMMYYYY1(dbDate)) == -1 )
				{
					bootbox.alert('Voucher Date should be less than or equal to '+dbDate);
				document.getElementById('voucherHeader_voucherDate').focus();
				return false;
				}
				var sm=PageManager.DataService.getQueryField('showMode');
				if(sm=='modify')
				{
				var effDate=document.getElementById('voucherHeader_effDate').value;
				if(compareDate(formatDateToDDMMYYYY1(effDate),formatDateToDDMMYYYY1(dbDate)) == -1 ){
					bootbox.alert('Voucher Date should be less than or equal to '+dbDate);
				document.getElementById('voucherHeader_effDate').focus();
				return false;
				}
				}
			document.getElementById('pay_hide').value=getSelectedRadioValue(document.getElementsByName('pay_type'));
		PageManager.UpdateService.submitForm('subLedgerPaymentHeader');
		//bootbox.alert(PageManager.DataService.getQueryField('typeval'));
		//PageManager.DataService.setQueryField('pay_type',PageManager.DataService.setQueryField('typeval'));
	}
	function afterUpdateService(dc)	{
		window.close();
	}
		function disableControls(frmIndex, isDisable){
			for(var i=0;i<document.forms[frmIndex].length;i++)
			{
				document.forms[frmIndex].elements[i].disabled =isDisable;
				if(PageManager.DataService.getQueryField('showMode')=='view' && 
					document.forms[frmIndex].elements[i].parentNode.parentNode.getAttribute('id')=='backbutton')
					document.forms[frmIndex].elements[i].disabled =false;
				if(PageManager.DataService.getQueryField('showMode')=='modify' 
						&& (document.forms[frmIndex].elements[i].parentNode.parentNode.getAttribute('id')=='modGrid' ||
						 document.forms[frmIndex].elements[i].parentNode.parentNode.getAttribute('id')=='submitGrid1' ))
					document.forms[frmIndex].elements[i].disabled =false;
			}
		}

	function reverse(){
				var cgn=PageManager.DataService.getQueryField('drillDownCgn');
		 		if(cgn){
		 			document.getElementById('reverseRecord').style.display='block';
		 			var vcObj=document.getElementById('voucherHeader_newVcNo');
		 			vcObj.setAttribute('exilMustEnter','true');
		 			vcObj.disabled=false;
		 			var vdObj=document.getElementById('voucherHeader_effDate')
		 			vdObj.setAttribute('exilMustEnter','true');
		 			vdObj.disabled=false;
		 			var jvcObj=document.getElementById('voucherHeader_newJVcNo');
					jvcObj.disabled=false;
		 			var bcObj=document.getElementById('diffDebit')
		 			bcObj.disabled=false;
		 			document.getElementById('modeOfExec').value="modify";
		 			document.getElementById('submitGrid').style.display ="none";
		 			document.getElementById('submitGrid1').style.display ="block";
		 			var canbut = document.getElementsByName('can');

						  for(var i=0;i<canbut.length;i++)
						   {
							 canbut[i].style.display = "none";
	   						}
		 	 	}
		}

		function showglEntry()
				{
				  hideColumns(0);
				  displayColumn(1);
				  displayColumn(2);
				  displayColumn(3);
				  displayColumn(4);
				  displayColumn(5);

					if(!PageValidator.validateForm())
						return false;
					PageManager.DataService.setQueryField("showGlEntries","show");
					PageManager.DataService.setQueryField("showJVGlEntries","no");

					var accNumberObj=document.getElementById("subLedgerPaymentHeader_branchAccountId");
					var accNumberType=accNumberObj.options[accNumberObj.selectedIndex].value
					PageManager.DataService.setQueryField("accountNumberId",accNumberType);
					var payType=document.getElementById("pay_type").value;
					//bootbox.alert(payType+"gl");
					PageManager.DataService.setQueryField("fund_id",document.getElementById("fund_id").value);
					PageManager.DataService.setQueryField("fund1_id",document.getElementById("fundSub_id").value);
					//bootbox.alert(document.getElementById("fundSub_id").value+"FUND1_ID");
					PageManager.DataService.setQueryField("pay_type",payType);
					PageManager.DataService.callDataService('subLedgerPaymentHeader');
				}
				function showEntry()
				{
					var trObj;
					var tObj=document.getElementById("showEntries");
					var cr=0,dr=0;
					tObj.style.display="block";
					var tableObj=document.getElementById("entries");
					for(var i=tableObj.rows.length-1;i>=2;i--)
					{
						tableObj.deleteRow(i);
					}
					var Obj=PageManager.DataService.getControlInBranch(tableObj.rows[1],"display_Code");
					trObj=PageManager.DataService.getRow(Obj);
					var iRow=glNameCode.split(";");
					var rowContents=null;tempObj=null;
					for(var i=1;i<(iRow.length)-1;i++)
					{
						rowContents=iRow[i].split("^");
						if(i==1){
						newRow=trObj;
						tempObj1=PageManager.DataService.getControlInBranch(newRow,'display_CodeType');
						tempObj1.innerHTML=rowContents[0];
						tempObj2=PageManager.DataService.getControlInBranch(newRow,'display_Code');
						tempObj2.innerHTML=rowContents[1];
						tempObj3=PageManager.DataService.getControlInBranch(newRow,'display_Head');
						tempObj3.innerHTML=rowContents[2];
						tempObj5=PageManager.DataService.getControlInBranch(newRow,'display_Narration');
						tempObj5.innerHTML=" -";
						}else{
						var newRow=trObj.cloneNode(true);
						newRow=tableObj.tBodies[0].appendChild(newRow);
						tempObj1=PageManager.DataService.getControlInBranch(newRow,'display_CodeType');
						tempObj1.innerHTML=rowContents[0];
						tempObj2=PageManager.DataService.getControlInBranch(newRow,'display_Code');
						tempObj2.innerHTML=rowContents[1];
						tempObj3=PageManager.DataService.getControlInBranch(newRow,'display_Head');
						tempObj3.innerHTML=rowContents[2];
						tempObj5=PageManager.DataService.getControlInBranch(newRow,'display_Narration');
						tempObj5.innerHTML=" -";
						}

					}
					var totalamount=document.getElementById('total').value;

					for(var i=1;i<tableObj.rows.length;i++)
					{
						var typeObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"display_CodeType");
						var type=typeObj.innerHTML;
						if(type =='credit')
						{
							var trObj=PageManager.DataService.getRow(typeObj);
							col1=PageManager.DataService.getControlInBranch(trObj,'display_Credit');
							col1.innerHTML=totalamount;
							col2=PageManager.DataService.getControlInBranch(trObj,'display_Debit');
							col2.innerHTML="0";
						}
					}
					for(var i=1;i<tableObj.rows.length;i++)
					{
						var typeObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"display_CodeType");
						var type=typeObj.innerHTML;
						if(type =='debit')
						{
							var trObj=PageManager.DataService.getRow(typeObj);
							col1=PageManager.DataService.getControlInBranch(trObj,'display_Credit');
							col1.innerHTML="0";
							col2=PageManager.DataService.getControlInBranch(trObj,'display_Debit');
							col2.innerHTML=totalamount;
						}
					}

					 var dSum=0;
					 var cSum=0;
					for(var i=1;i<tableObj.rows.length;i++)
					{

					  var dObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"display_Debit");
					  var cObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"display_Credit");
					 if(!isNaN(parseFloat(dObj.innerHTML)))
					 {
					   var sum=(parseFloat(dObj.innerHTML));
					   dSum=dSum+sum;
					 }
					 if(!isNaN(parseFloat(cObj.innerHTML)))
					 {
					   var sum=(parseFloat(cObj.innerHTML));
					   cSum=cSum+sum;
					 }
					}
					var newRow=trObj.cloneNode(true);
					newRow=tableObj.tBodies[0].appendChild(newRow);
					objt1=PageManager.DataService.getControlInBranch(newRow,'display_Code');
					objt2=PageManager.DataService.getControlInBranch(newRow,'display_Head');
					objt3=PageManager.DataService.getControlInBranch(newRow,'display_Debit');
					objt4=PageManager.DataService.getControlInBranch(newRow,'display_Credit');
					objt5=PageManager.DataService.getControlInBranch(newRow,'display_Narration');
					objt1.innerHTML=" ";
					objt2.innerHTML=" ";
					objt3.innerHTML=dSum;
					objt4.innerHTML=cSum;
					objt5.innerHTML=" ";

			}

			function hideColumns(index)
			{
				var table=document.getElementById('entries');
			   	for(var i=0;i<table.rows.length;i++){
					table.rows[i].cells[index].style.display="none";
			   	}
			}
function showPayButton()
{
	var button = document.getElementById('paySupConHeader');
	if(button.style.display=='none')	document.getElementById('paySupConHeader').style.display='block';
}
function setPaymentValues()
{
	 var chkBox;
	 var worksid='';
	 var j=0;
	 var id,fundid,fundsourceid,fundname,type,conorsupidname,fsname;
     var table=document.getElementById('subledgerPaymentSearchGrid');
     var isChecked=false;
	for(var i=1;i<table.rows.length;i++)
	{

		chkBox=PageManager.DataService.getControlInBranch(table.rows[i],"selectToPay");
		if(chkBox.checked)
		{
			isChecked=true;
			if(j==0)
			{
				id=PageManager.DataService.getControlInBranch(table.rows[i],"id").innerHTML;
				fundid=PageManager.DataService.getControlInBranch(table.rows[i],"fundid").innerHTML;
				fundsourceid=PageManager.DataService.getControlInBranch(table.rows[i],"fundsourceid").innerHTML;
				if(fundsourceid==null || fundsourceid=='null' || fundsourceid=='') fundsourceid= ' ';
				fundname=PageManager.DataService.getControlInBranch(table.rows[i],"fundname").innerHTML;
				type=PageManager.DataService.getControlInBranch(table.rows[i],"type").innerHTML;
				conorsupidname=PageManager.DataService.getControlInBranch(table.rows[i],"contractor").innerHTML;
				fsname=PageManager.DataService.getControlInBranch(table.rows[i],"fundsource").innerHTML;
				var worksidname=PageManager.DataService.getControlInBranch(table.rows[i],"wocode").innerHTML;
				j=1;
			}
			else
			{
				if(id != PageManager.DataService.getControlInBranch(table.rows[i],"id").innerHTML)
				{
					bootbox.alert('Please Select Bills For Same Contractor/Supplier');
					return false;
				}
				if(fundid != PageManager.DataService.getControlInBranch(table.rows[i],"fundid").innerHTML)
				{
					bootbox.alert('Please Select Bills For Same Fund');
						return false;
				}
				if(fundsourceid != PageManager.DataService.getControlInBranch(table.rows[i],"fundsourceid").innerHTML)
				{
					//bootbox.alert(fundsourceid);
					fundsourceid="";
						//bootbox.alert('Please Select Bills For Same Fundsource');
						//return false;
				}

			}
			worksid=worksid+(PageManager.DataService.getControlInBranch(table.rows[i],"worksdetailid").innerHTML)+',';

		}


	}
		if(isChecked)
		{	var mode="createPay";
			if(subLedgerPayment_Header.voucherDateFrom.value!=="")
			var dt1=formatDate5(subLedgerPayment_Header.voucherDateFrom.value);
			if(subLedgerPayment_Header.voucherDateTo.value!=="")
			var dt2=formatDate5(subLedgerPayment_Header.voucherDateTo.value);
			//bootbox.alert("id "+id+" type "+type +"fundid "+fundid+"fundsourceid"+fundsourceid);
			//bootbox.alert("worksid  "+worksid);
			cWind=window.open("SubLedgerPayment.jsp?conorsupid="+id+"&worksdetaild="+worksid+"&funid="+fundid+"&fundSource_name1="+fundsourceid+"&typeval="+type+"&conorsupidname="+conorsupidname+"&fundname="+fundname+"&fsname="+fsname+"&showMode="+mode+"&dt1="+dt1+"&dt2="+dt2+"&formSubmitted=1","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
		}
		else
		{
			bootbox.alert('Select Bills To Pay');

		}

}
function onClickCancel()
{
window.location="SubLedgerPayment.htm";
}

function loadBankBalance()
{
	var ctr= document.getElementById('subLedgerPaymentHeader_branchAccountId');
	loadFund(ctr);//Get the Fund related to this id
	//bootbox.alert("getting fund 4 id:"+document.getElementById("fund1_id").value);
	var voucherHeader_voucherDate=document.getElementById('voucherHeader_voucherDate').value;
	    if(!voucherHeader_voucherDate)
	   {
	    	bootbox.alert("Please select the Voucher Date ");
	    	return;
	   }
	   if(ctr.selectedIndex != -1){
	     var bal_from=ctr.options[ctr.selectedIndex].value;
	     PageManager.DataService.setQueryField('voucherHeader_voucherDate',voucherHeader_voucherDate);
	     PageManager.DataService.setQueryField('bal_from',bal_from);
	     PageManager.DataService.callDataService('contraBalNumberF');
		}
		accntMod(ctr);
	}
	function checkBalance()
	{

	    var amt=document.getElementById('total').value;
		var bal=document.getElementById("bank_bal").value;
		bal=isNaN(bal)?0:bal;
		var finalBal=bal-amt;
		if(finalBal<0)
		{
		  if(!confirm("WARNING:  Insufficient Balance in Bank-Account"))
		    return false;
		}
		return true;
	}

function assignChqDate()
{
	var smode=PageManager.DataService.getQueryField('showMode');
	if(smode=="createPay")
		document.getElementById("chequeDetail_chequeDate").value=document.getElementById("voucherHeader_voucherDate").value;
}
function checkFund()
{
	if(document.getElementById('fund_id').value!=document.getElementById('fundSub_id').value)
		  if(!confirm("WARNING:This will be an Inter Fund Transfer."))
			    return false;
		else{
			if(document.getElementById("jv_voucherNumber").value.length==0){
				bootbox.alert("Enter value for Journal Voucher Number..");
				return false;
			}
		}

	return true;
}
function accntMod(obj)
{
	try{//bootbox.alert("in mod:"+PageManager.DataService.getQueryField("fund1_id"));
	if(obj.selectedIndex==-1)	 return;
	 if(sIndex!=null)
	{
			obj.options[sIndex]= new Option(sText,sValue);
	}

	var text=obj.options[obj.selectedIndex].innerHTML;
    var temp=text.split(" -- ");
	sIndex=obj.selectedIndex;
	sValue=obj.options[obj.selectedIndex].value;
	sText=text;
	if(!temp[1])
		 temp[1]=" ";
	document.getElementById("acc_Desc").value=temp[1];

	if(temp[1]!=null && temp[1]!="" )
   {
     obj.options[obj.selectedIndex].text=temp[0];
   }
   }catch(err){}
}

function loadFund(obj)
{
	if(obj.selectedIndex==-1) return;
//	accntMod(obj);
	var accId=obj.options[obj.selectedIndex].value;
	//bootbox.alert(accId+"text"+obj.options[obj.selectedIndex].text);
	PageManager.DataService.setQueryField('bankIdForFund',accId);
	PageManager.DataService.callDataService('AccountFundMap1');
}
function checkFundReverse()
{
    if(document.getElementById('fund_id').value!=document.getElementById('fund1_id').value)
    {
		if(document.getElementById("voucherHeader_newJVcNo").value.length==0){
			bootbox.alert("Enter value for Journal Voucher Number.");
			document.getElementById("voucherHeader_newJVcNo").focus();
			return false;
		}

	}
	else
		document.getElementById("voucherHeader_newJVcNo").value='';

	return true;
}
//TO SHOW JOURNAL VOUCHER GL-ENTRY
function showJVglEntry()
{
//bootbox.alert("WO Fund:"+document.getElementById('fund_id').value+" Bank Fund:"+bankFundis)
	 if(document.getElementById('fund_name').value==document.getElementById('fund1_name').value)
	 {
		 bootbox.alert("There is no Journal Entry passed.");
		hideColumns(1);
		hideColumns(2);
		hideColumns(3);
		hideColumns(4);
		hideColumns(5);
		return;
	 }
	 else{
	 	hideColumns(0);
	 	displayColumn(1);
	 	displayColumn(2);
	 	displayColumn(3);
	 	displayColumn(4);
	 	displayColumn(5);

		if(!PageValidator.validateForm())
			return false;
		PageManager.DataService.setQueryField("showGlEntries","show");
		PageManager.DataService.setQueryField("showJVGlEntries","yes");
		var accNumberObj=document.getElementById("subLedgerPaymentHeader_branchAccountId");
		var accNumberType=accNumberObj.options[accNumberObj.selectedIndex].value
		PageManager.DataService.setQueryField("accountNumberId",accNumberType);
		var typeObj=document.getElementById("subLedgerPaymentHeader_typeOfPayment");
		var payType=document.getElementById("pay_type").value;
		//var payType=typeObj.options[typeObj.selectedIndex].value
		PageManager.DataService.setQueryField("typeOfPayment",payType);
		PageManager.DataService.setQueryField("fund_id",document.getElementById("fund_id").value);
		PageManager.DataService.setQueryField("fund1_id",document.getElementById("fundSub_id").value);
		PageManager.DataService.callDataService('subLedgerPaymentHeader');
	}
}
function displayColumn(index)
{
	var table=document.getElementById('entries');
   	for(var i=0;i<table.rows.length;i++){
		table.rows[i].cells[index].style.display="block";
   	}
}

function nextChqNo()
{
	var obj=document.getElementById("subLedgerPaymentHeader_branchAccountId");
	var bankBr=document.getElementById("subLedgerPaymentHeader_bankId");
	if( bankBr.selectedIndex==-1)
	{
		bootbox.alert("Select Bank Branch and Account No!!");
	  return;
	}

	if(obj.selectedIndex==-1)
	{
		bootbox.alert("Select Account No!!");
	  return;
	}
	var accNo=obj.options[obj.selectedIndex].text;
	var accNoId=obj.options[obj.selectedIndex].value;
	var sRtn =showModalDialog("SearchNextChqNo.html?accntNo="+accNo+"&accntNoId="+accNoId,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	if(sRtn!=undefined ) document.getElementById("chequeDetail_chequeNumber").value=sRtn;
}
function SupConTypeChange()
		 {	//Supplier relationTypeId -- 1
			//Contractor relationTypeId---- 2
				var obj=document.getElementById('SupConType');
				if(obj.selectedIndex==0)
				{	document.getElementById("SupConName").options.length=0;
					return;
				}
				else
				{
					var text=obj.options[obj.selectedIndex].value;
					PageManager.DataService.setQueryField('SupConTypeCode',text);
					PageManager.DataService.callDataService('getSupConName');

				}
		 }
</script>
</head>

<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoadTasks();" onKeyDown ="CloseWindow(event); "><!------------------ Header Begins Begins--------------------->
<jsp:useBean id="Bean"  class="com.exilant.eGov.src.transactions.CommonBean" />
<jsp:setProperty name="Bean" property="inputField12"/>
<jsp:setProperty name="Bean" property="showMode"/>

<form name="subLedgerPayment_Header" method="post"  >
	<input type=hidden name="fund1_id" id="fund1_id" value="" class="fieldinput" >
	<input type=hidden name="fundSub_id" id="fundSub_id" value="" class="fieldinput" >
	<input type="hidden" name="databaseDate"  id="databaseDate">
	<input type=hidden id="modeOfExec" name="modeOfExec" value="new">
	<input type="hidden" name="tType" id="tType" value="P">
	<input type="hidden" name="cgnNum" id="cgnNum">
	<input type="hidden" name="voucherHeader_id" id="voucherHeader_id">
	<input type="hidden" name="relationTypeId" id="relationTypeId" value="">
	<input type="hidden" name="dat" id="dat">
	<input type="hidden" name="oldVoucherDate" id="oldVoucherDate">
	<input type=hidden name="cvType" id="cvType" value="J">
	<input type="hidden" name="startFinDate" id="startFinDate">
	<input type=hidden id="inputField12" name="inputField12">
	<input type=hidden id="showMode" name="showMode" value="" />
	<input type=hidden id="formSubmitted" name="formSubmitted" value="1"/>
	<table width="100%" border=0 cellpadding="3" cellspacing="0" align="center">
		<tr id="secondrow" >
			<td align="right" valign="center"  ><div align="right" class="labelcell">Type<span class="leadon">*</span></div></td>
			<td  width="201" ><span class="labelcell">
			<input type="radio" value="Contractor"  id="pay_type" name="pay_type" >Contractor<input type="radio" value="Supplier"  id="pay_type" name="pay_type" onClick="getList(this);" tabindex="">Supplier
			<INPUT TYPE="hidden" ID="pay_hide" NAME="pay_hide"  exilDataType="exilAnyChar"></span></td>
			<td><div align="right" valign="center" class="labelcell" >Pay To<span class="leadon">*</span></div></td>
			<td width="297">
				<!--<select class="fieldinput" id="payTo" name="payTo"  exilMustEnter="true" ></select>-->
				<div class=smallfieldcell><select class="fieldinput" id="payTo" name="payTo" onChange="hide();"  exilMustEnter="true" exilListSource="csList" tabindex=""></select></div>
			</td>
		</tr>
		<tr   id="filterRow1" name="filterRow1">
			<td align=right><div class="labelcell">Bill Date From</div></td>
			<td ><span class=smallfieldcell><input name="voucherDateFrom" class="datefieldinput" id="voucherDateFrom"  onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" ></span>
			</td>

			<td  align=right><div align="right" valign="center" class="labelcell" >Bill
      Date&nbsp;To</div></td>
			<td width="297"><span class=smallfieldcell><input name="voucherDateTo" class="datefieldinput" 

      id="voucherDateTo" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate" exilCalendar="true"></span> </td>
      	</tr>
      	<!--Added by iliyaraja-->
     <tr  id="filterRow3" name="filterRow3" >
			<td align="right" valign="center"  width="25%" ><div class=labelcell>Type</div></td>
			<td class="fieldinput" width="25%" >
			<div class=smallfieldcell><select  id="SupConType" class="fieldinput"  name="SupConType"  onChange="SupConTypeChange()"  width ="25%" >
			<option value=""></option>
			<option value="1">Supplier</option>
			<option value="2">Contractor</option>
			</select></div>
			</td>
			<td align="right" valign="center" > <div class=labelcell>Sup/Con Name</div><span class="leadon"></span></td>
			<td class="fieldinput" width="45%" ><div class=smallfieldcell><select name="SupConName" class="combowidth2" id="SupConName"  ></select></div>
			</td>
	</tr>
	<tr  id="filterRow4" name="filterRow4" >
		<td align="right" valign="center"  width="25%" ><div class=labelcell>Fund</div></td>
			<td class="fieldinput" width="25%" >
			<div class=smallfieldcell><select  id="subledgerPay_fund" class="fieldinput"  name="subledgerPay_fund"   width ="25%"  exilListSource="fundNameLst">

			</select></div>
			</td>
			<td align="right" valign="center"  width="25%" ></td>
			<td class="fieldinput" width="45%" ></td>

	</tr>
      	<!--Added by siddhu-->
		<tr id="fourthrow" >
			<td align="right" valign="center"  ><div class=labelcell>Fund</div></td>
			<td  width="201"><!--<select class="fieldinput" id="fund_id" name="fund_id" exilMustEnter="true" exilListSource="fundLst" >	</select>-->
				<input type=hidden name="fund_id" id="fund_id" value="" class="fieldinput" >
				<div class=fieldcell><input name="fund_name" id="fund_name" class="fieldinput" readonly tabindex=-1></div>
			</td>
			<td align="right" valign="center"  width="206"><div class=labelcell>Financing Source</div></td>
			<td  width="201"><input type=hidden name="fundSource_id" id="fundSource_id" class="fieldinput" >
				<span class=smallfieldcell><select class="fieldinput" name="fundSource_name" id="fundSource_name" class="fieldinput"   ></select></span >
			</td>

		</tr><!--Added by siddhu-->
		<tr   id="filterRow2" name="filterRow2">
			<td colspan="4" align="middle" width="906"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0" ID="Table1">
					<tr>
						<td align="right"><input   type="button" class="button" value="Search"  onclick="getGrid();" /></td>
					</tr>
				</table><!-- Buttons End Here -->
			</td>
		</tr>
	</table>

	<input type="hidden" name="displayCondition" id="displayCondition" value="0">
	


		
	<!-- table width="100%" border="1" cellpadding="0" cellspacing="0" name="subledgerPaymentSearchGrid" id="subledgerPaymentSearchGrid" style="DISPLAY:none">
		<tr class="rowheader">

			<td width="20%" class="columnheader"><div align="left">Supplier/Contractor Name</div></td>
			<td width="15%" class="columnheader"><div align="center">PO/WO</div></td>
			<td width="20%" class="columnheader"><div align="center">Fund</div></td>
			<td width="15%" class="columnheader"><div align="center">FinancingSource</div></td>
			<td width="15%" class="columnheader"><div align="center">Amount</div></td>

			<td  class="columnheader"><div align="center">Id</div></td>
			<td  class="columnheader"><div align="center">Works Detailid</div></td>
			<td  class="columnheader"><div align="center">Type</div></td>
			<td  class="columnheader"><div align="center">Fund Id</div></td>
			<td  class="columnheader"><div align="center">FundSource Id</div></td>
			<td  width="5%" class="columnheader"><div align="center">Select</div></td>
		</tr>

		<tr >

			<td align=left><div id="contractor" name="contractor" >&nbsp;</div></td>
			<td><div id="wocode" name="wocode" >&nbsp;</div></td>
			<td align=left><div id="fundname" name="fundname" >&nbsp;</div></td>
			<td align=left><div id="fundsource" name="fundsource" >&nbsp;</div></td>
			<td align=right><div id="amount" name="amount" exilTrimLength="16">&nbsp;</div>

			<td align="right"><div id="id" name="id" align="center"></div></td>
			<td align="right"><div id="worksdetailid" name="worksdetailid" align="center"></div></td>
			<td align="right"><div id="type" name="type" align="center"></div></td>
			<td align="right"><div id="fundid" name="fundid" align="center"></div></td>
			<td align="right"><div id="fundsourceid" name="fundsourceid" align="center"></div></td>
			<td align="center"><input type=checkbox id="selectToPay" name="selectToPay" tabindex="9"></td>
		</tr>
	</table -->
	
	
		<!-- div id="m3" style="width: 906px; height: 231px;padding-left:0px" -->
	<div name="trnHeader" id="trnHeader" style="DISPLAY: none;">
	<TABLE width="100%"  border="0" cellpadding="0" cellspacing="0">
		<tr>
    	   	<td><div style="overflow:auto;width:790;">
        	   	<table width="100%" border="1" cellpadding="0" cellspacing="0" name="Data_Grid" id="Data_Grid">
            	   	<tr class="thStlyle">
                	   	<td width="7%" height="34" ><div align="center" >Sl No</div></td>
                	   	<td width="10%" ><div align="center" name="PO/WO Code" id="workOrderCode">WorkOrder Code</div></td>
                	   	<td width="10%" ><div align="center" name="vcNo" id="vcNo">Vc No</div></td>
                        <td width="10%" ><div align="center" name="billNumber" id="billNumber">Bill No</div></td>
                        <td width="3%" ><div align="center" name="billDt" id="billDt">Bill Date</div></td>
                        <td width="5%" ><div align="center" name="passedAmount">Passed Amount</div></td>
                        <td width="10%" ><div align="center">Advance Adjusted</div></td>
                        <td width="9%" ><div align="center">Statutory Deduction</div></td>
                        <td width="10%" ><div align="center">Other Deduction</div></td>
                        <td width="10%" ><div align="center">Net</div></td>
                        <td width="10%" ><div align="center">Earlier Payment</div></td>
                        <td width="5%" ><div align="center">Select<input type=checkbox id="bSelect" name="bSelect" onClick="selUnSelAll();" value="ON" ></div></td>
                        <td width="8%" ><div align="center">Payment Amount</div></td>
                        <td width="8%" ><div align="center">WORKDETAILID</div></td>
                        <td width="2%"  style="display:none"><div align="center"><br>IDContractor</div></td>
                    </tr>

                    <tr >
                    	<td align="middle" class="smalltext"><span id="slNo" name="slNo" align="center"></span></td>
                    	<td align="middle" class="smalltext"><span id="woCode" name="woCode" align="center"></span ></td>
                    	<td align="middle" class="smalltext"><span id="d_voucherNo" name="d_voucherNo" align="center"></span></td>
                        <td align="right" class="smalltext"><span id="billNo" name="billNo" align="center"></span><input type="hidden" id="billNoId" name="billNoId" exilDataType="exilAnyChar"></td>
                        <td align="middle" class="smalltext"><span id="billDate" name="billDate" align="center"></span></td>
                        <td align="right" class="smalltext"><span id="passedAmount" name="passedAmount"></span></td>
                        <td align="right" class="smalltext"><span id="advance" name="advance"></span></td>
                        <td align="right" class="smalltext"><span id="tds" name="tds"></span></td>
                        <td align="right" class="smalltext"><span id="otherRecoveries" name="otherRecoveries"></span></td>
                        <td align="right" class="smalltext"><span id="net" name="net"></span></td>
                        <td align="right" class="smalltext"><span id="earlierPayment" name="earlierPayment"></span></td>
                        <td align="middle" class="smalltext"><input type="hidden" name="billSelectHidden" id="billSelectHidden"><input type=checkbox id="billSelect" name="billSelect" value="1" onClick="calcTotal();" ></td>
                        <td align=right class="smalltext"><span class="fieldcell"><input id="slph_paidAmount" name="slph_paidAmount" onBlur="verify(this);" class="smallfieldinput" exilDataType="exilUnsignedDecimal" ></span></td>
                        <td align="middle" class="smalltext"><span class="fieldcell"><input id="woId" name="woId" class="fieldinput-right"></span></td>
                        <td align="middle"><input  type="hidden" id="idsofcontractor" name="idsofcontractor" class="fieldinput-right"></td>
					</tr>
					</table>
					<table width="100%">
					<tr align=right>
						<td   align=right ><span class="labelcell">Total</span></td>
						<td class="fieldcell"><span class="fieldcell"><input readonly class="fieldinput-right" id="total" name="total" value="0" tabindex=-1></span>
                        </td>
                        <td width="1%"><div align="center"></div></td>
                    </tr>
				</table>
				</div>
				
					
			
					
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td valign="top" ><!------------Content begins here ------------------>
				<table width="100%" border="0" cellpadding="3" cellspacing="0" id="showHide">
					<TBODY>
						<tr >
        					<td colspan="4" width="904">&nbsp;</td>
         				</tr>
         				<tr  >
							<td width="21%" id="cgnLabel" name="cgnLabel" style="DISPLAY: none"><div align="right" valign="center" class="labelcell" >CG&nbsp;Number</div></td>
							<td width="280" id="cgnText" name="cgnText" style="DISPLAY: none"><span class="fieldcell"><input name="voucherHeader_cgn" class="fieldinput" id="voucherHeader_cgn" readonly></span></td>
							<td width="187"><div align="right" valign="center" class="labelcell" >Voucher&nbsp;Number<span class="leadon">*</span></div></td>
							<td width="298">
							<table cellpadding="0" cellspacing="0">
						  		<tr>
						  		<td><span class="smallfieldcell"><input name="voucherHeader_voucherNumberPrefix" maxlength="2" class="fieldinput-left" id="voucherHeader_voucherNumberPrefix" readonly style="display:none" style="width:25px" SIZE="1"></span></td>
						  		<td><span class="fieldcell"><input name="voucherHeader_voucherNumber" maxlength="10" class="fieldinput-left" id="voucherHeader_voucherNumber"  exilDataType="exilAlphaNumeric" exilMustEnter="true" SIZE="20"  ></span></td>
						  		</tr>
						  	</table>
						</tr>
						<tr >
							<td width="21%"><div align="right" valign="center" class="labelcell" >Paid By<span class="leadon">*</span></div>	</td>
							<td width="280"><span class="smallfieldcell"><SELECT class="fieldinput" name="paidBy" id="paidBy" onChange="getInHandId();"  exilDataType="exilAnyChar" exilMustEnter="true" 
            exilListSource="billCollectorList"></SELECT></span></td>
							<td width="187"><div align="right" valign="center" class="labelcell">Voucher Date<span class="leadon">*</span></div></td>
							<td width="298"><span class="smallfieldcell"><input name="voucherHeader_voucherDate" class="datefieldinput" 

            id="voucherHeader_voucherDate" size="15" onkeyup="DateFormat(this,this.value,event,false,'3')"
          exilCalendar="true" onpropertychange="assignChqDate()";></span></td>
						</tr>
						<tr  style="DISPLAY: none">
							<td width="21%"><div align="right" valign="center" class="labelcell" > Mode of Payment<span class="leadon">*</span></div>	</td>
							<td  width="280"><span class="labelcell"><input type="radio" value="Cash" tabindex=""
            id="subLedgerPaymentHeader_typeOfPayment" name="subLedgerPaymentHeader_typeOfPayment"

            onClick="check(this);"
           >Cash<input type="radio" value="Cheque" tabindex=""

          checked id
           ="subLedgerPaymentHeader_typeOfPayment" name="subLedgerPaymentHeader_typeOfPayment"
          onClick="check(this);"
           >Cheque</span></td>
					<td ><div align="right" valign="center" class="labelcell"><span class=leadon></span></div>
						</td><td></td><!--<td>
						<SELECT id="fundsource_id" name="fundsource_id" exilMustEnter="true"
						class="fieldinput" exilListSource="fundsourceList"></SELECT>&nbsp;
						</td>-->
						</tr>

						<tr >
							<td width="21%"><div align="right" valign="center" class="labelcell">Bank<span class="leadon">*</span></div></td>
							<td width="280"><span class="smallfieldcell"><SELECT class="combowidth2" 
            id="subLedgerPaymentHeader_bankId" name="subLedgerPaymentHeader_bankId" onChange="getAccountNumber();"
            exilMustEnter="true" exilListSource="bankAndBranch"></SELECT></span></td>
							<td width="187"><div align="right" valign="center" class="labelcell" >Account Number<span class="leadon">*</span></div></td>
							<td width="298"><span class="smallfieldcell"><SELECT class="fieldinput" id="subLedgerPaymentHeader_branchAccountId" name="subLedgerPaymentHeader_branchAccountId" exilMustEnter="true"  onChange="loadBankBalance()"  exilListSource="accountListFrom" ></SELECT></span>
											<span class="smallfieldcell"><input type="text" id="acc_Desc" name="acc_Desc" class="fieldinput"  readonly  tabIndex=-1></span></td>
						</tr>
						<tr  id="bankbal">
							  <td><div align="right" valign="center" class="labelcell"> Bank Balance</div></td>
							  <td><span class="fieldcell">
								<input  name="bank_bal" id="bank_bal" class="fieldinput" tabindex=-1  readonly ></span>
							  </td>
							  <td><div align="right" valign="center" class="labelcell"> Bank Fund</div></td>
							  <td><span class="fieldcell">
					  			<input name="fund1_name" id="fund1_name" class="fieldinput" tabindex=-1 readonly></span>
							  </td>
					    </tr>
						<tr >
							<td width="21%"><div align="right" valign="center" class="labelcell" >Cheque No<span class="leadon">*</span></div></td>
							<td>
								<table width="25%">
									<tr><td width="280"><span class="fieldcell"><input name="chequeDetail_chequeNumber" class="fieldinput" id="chequeDetail_chequeNumber"   maxLength="10" exilDataType ="exilUnsignedInt" exilMustEnter="true" ></span></td>
										<td valign="center" title="Show Next Cheque Number"  nowrap id="chqAvail"><A onclick=nextChqNo() href="#"><IMG height=22 src="/egi/resources/erp2/images/arrowright.gif" width=22 ></A></td>
									<!--	<td bgcolor="#fe0000" valign="center" nowrap id="chqAvail"><A class=buttonprimary id=savenClose onclick=nextChqNo() href="#">Show Next Cheque No.</A></td> -->
									</tr>
								</table>
							</td>
							<td width="187"><div align="right" valign="center" class="labelcell" >Cheque Date<span class="leadon">*</span></div>	</td>
							<td width="298"><span class="smallfieldcell"><input name="chequeDetail_chequeDate" class="datefieldinput" id="chequeDetail_chequeDate"   size="15" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate" exilMustEnter="true" exilCalendar="true" ></span> </td>
						</tr>
						<tr >
						<td  width="21%" align="right" valign="center" ><div align="right" class="labelcell">Journal Voucher Number
							<br><sup>(applicable only in case of <br> inter fund transfer)</sup></div>
						</td>
							<td>
							<table cellpadding="0" cellspacing="0">
						  		<tr>
						  		<td><span class="smallfieldcell"><input name="jv_voucherNumberPrefix" maxlength="2" class="fieldinput-left" id="jv_voucherNumberPrefix" readonly style="display:none" style="width:25px" SIZE="1"></span></td>
						  		<td><span class="fieldcell"><input name="jv_voucherNumber" maxlength="10" class="fieldinput-left" id="jv_voucherNumber"  exilDataType="exilAlphaNumeric" SIZE="20"  ></span></td>
						  		</tr>
						  	</table>

							</td>

							<!--<td width="219"><div align="right" valign="center" class="labelcell" >Cash In Hand</div></td>
							<td width="226">
                            <input class="fieldinput" id="billCollector_cashInHandDesc" name="billCollector_cashInHandDesc" readonly><input type="hidden" id="billCollector_cashInHand" name="billCollector_cashInHand"></td>-->
							<td width="187"><DIV align="right" valign="center" class="labelcell">Narration</DIV></td>
							<td width="298"><TEXTAREA class=narrationfieldinput id="subLedger_narration" name="subLedger_narration" exilDataType="exilAnyChar" maxLength="250" ></TEXTAREA></td>
						</tr>

<!--<tr >
							<td ><div width="219" class="labelcell" align="right">Cheque In Hand</div></td>
							<td width="226">
								<input class="fieldinput" id="billCollector_chequeInHandDesc" name="billCollector_chequeInHandDesc" readonly >
								<input type="hidden" id="billCollector_chequeInHand" name="billCollector_chequeInHand">
                            </td>
                            <td width="221">&nbsp;</td>
							<td width="220">&nbsp;</td>
						</tr>-->
					</TBODY>
				</table>
			</td>
		</tr>
		<tr >
			<td height="25" colspan="4" valign="bottom" class="smalltext"><p class="smalltext"><span class="leadon">*</span>
				- Mandatory Fields</p></td>
		</tr><!--  Buttons for Request Form starts here -->
		<table border="0" cellpadding="0" cellspacing="0" align="center" id=TABLE1 width="100%">
		<TBODY>
		<tr >
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0" id=TABLE1 >
					<tbody>
					<tr id="modGrid">
										<td align="center" width=100% colspan=4>
										<input   type="button" class="button" value="Reverse" onclick=reverse() ; />
										</td>
								</tr>
								<tr id="backbutton" >
									<td width=100% align=center colspan=4>
									<input   type="button" class="button" value="Back"  onclick="back(window.self)" />

									</td>
								</tr>
						</tbody>
						</table>
						<table>
										<tr id="reverseRecord"  style="DISPLAY: none">
											<td  align="right"><span  align="right" valign="center" class="labelcell" style="width:175">Payment Voucher Number<SPAN class=leadon>*</SPAN></span></td>
											<td><span class=smallfieldcell><input name="voucherHeader_newVcNo" id="voucherHeader_newVcNo" maxlength="10"  style=" HEIGHT: 22" size="10"  exilDataType="exilAlphaNumeric"> </span></td>
											<td align="right" valign="center"><span  valign="center" class="labelcell">Journal Voucher Number</span>
											<td><span class=smallfieldcell><input name="voucherHeader_newJVcNo" id="voucherHeader_newJVcNo" maxlength="10"  style="HEIGHT: 22" size="10" exilDataType="exilAlphaNumeric"> </span></td>
											<td align="right"><span   valign="center" class="labelcell" style="width:100" >Voucher Date<span class="leadon">*</span></td>
											<td><span class=smallfieldcell><INPUT class   ="datefieldinput"	name="voucherHeader_effDate" id="voucherHeader_effDate"onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate"  style="WIDTH: 101px; HEIGHT: 19px" size="13"   exilCalendar="true"></span></td>
											<td><INPUT  type=hidden class="datefieldinput" name="diffDebit" id="diffDebit" style   ="WIDTH: 101px; HEIGHT: 19px" size="13"></td>
										</tr>

										<tr>
										  <td colspan=4></td>
										</tr>
						</table>
			</td>
		</tr></TBODY></table>
						<table align="center" border="0" cellpadding="0" cellspacing="0">
					 				<tr id="submitGrid1" style="display:none">
					 						<td width=100% align=center>
						 						<input type="button"  id="revsave" value="Submit" class="button"  onclick="return ButtonPress('saveclose');"/>
						 						<input type="button" id="revclose" value="Close" class="button"  onclick="window.close();" />
					 						</td>
									</tr>
							</table>
							<table border="0" cellpadding="0" cellspacing="0"  align="center">
							 <tr id="submitGrid">
							 <td>
							 <input  id="saveandnew" type="button" class="button" value="Save & New"  onclick="return ButtonPress('savenew');"/>
							 <input   type="button" class="button" value="Save & Close"  onclick="return ButtonPress('saveclose');" />
							 <input   type="button" class="button" value="Cancel"  onclick="onClickCancel()" />
							 <input   type="button" class="button" value="Close"   onclick=window.close(); />
							 <input   type="button" class="button" value="Show GlEntry"  onclick=showglEntry(); />
							  <input   type="button" class="button" value="Show JV"  onclick=showJVglEntry(); />
							 </td>
						</tr></table>

</TABLE>
							</div>
				
							<table width="100%" border=0 cellpadding="3" cellspacing="0" id="showEntries" name="showEntries" style="DISPLAY: none">
								<tr >
								<td colspan=4>
								<table width="100%" align=center border="0" cellpadding="0" cellspacing="0" >
								<tr  height="25">
								 <TD class=displaydata align=middle  ><h4>GLEntry</h4></TD></tr>
								</table>
								</td>
								</tr>
								<tr><td>
								<table width="80%" border="1" cellpadding="0" cellspacing="0" align=center id="entries" name="entries" >
								<tr class="thStlyle">
									<td  ><div align="center" valign="center" > Code Type</div></td>
									<td  ><div align="center" valign="center" > Account Code</div></td>
									<td  ><div align="center" valign="center" > Account Head</div></td>
									<td  ><div align="center" valign="center" > Debit </div></td>
									<td  ><div align="center" valign="center" > Credit</div></td>
									<td  ><div align="center" valign="center" > Narration</div></td>
								</tr>
								<tr class="labelcell">
									<td ><div name="display_CodeType"  id="display_CodeType"   readOnly maxLength="10" tabIndex=0>&nbsp;</div></td>
									<td ><div name="display_Code"  id="display_Code"   readOnly maxLength="10" tabIndex=0>&nbsp;</div></td>
									<td ><div name="display_Head"  id="display_Head"  readOnly maxLength="10" tabIndex=0>&nbsp;</div></td>
									<td ><div name="display_Debit"  id="display_Debit"  readOnly maxLength="10" tabIndex=0>&nbsp;</div></td>
									<td ><div name="display_Credit"  id="display_Credit"  readOnly maxLength="10" tabIndex=0>&nbsp;</div></td>
									<td ><div name="display_Narration" id="display_Narration"  readOnly maxLength="10" tabIndex=0>&nbsp;</div></td>
								</tr>
								</table>
								</td>
								</tr>
						  </table>
</div></div></div></td></tr></table>						  
						  
						  
						  
<!-- start -->
<%

	//    System.out.print("\n Before submit "+request.getParameter("displayCondition"));
	    LinkedList list=new LinkedList();
	    if(request.getParameter("displayCondition") !=null && request.getParameter("displayCondition").equals("1"))
	    {
		 //System.out.print("\n after submit "+request.getParameter("displayCondition"));
			 try{
				 CommonList Bills= new CommonList();
				 list=Bills.getList(Bean);
				// System.out.println("list.size():"+list.size());

					if(list!=null && list.size()>0)
					{	//System.out.println("inside if");
						request.setAttribute("subledgerPaymentSearchGrid",list);
						//System.out.println("inside if after");


					try{
 %>

				<center>
					<div  id="tbl-container" class="tbl2-container"  align="center" width="100%" >
					 <display:table name="subledgerPaymentSearchGrid" id="subledgerPaymentSearchGrid"  export="false" sort="list"  class="view" cellspacing="0" style="margin-top:0px">

					 	<%com.exilant.eGov.src.transactions.CommonBean obj=(com.exilant.eGov.src.transactions.CommonBean)pageContext.findAttribute("subledgerPaymentSearchGrid"); %>

						<display:column property="field1" title="<%=Bean.getField1()%>" style="width:6px; align=center" media="html"   />
						<display:column title="<%=Bean.getField2()%>" >
						<span id="contractor" name="contractor"> <%=obj.getField2()%></span></display:column>
						<display:column  title="<%=Bean.getField3()%>" >
						<span id="wocode" name="wocode"> <%=obj.getField3()%></span></display:column>
						<display:column  title="<%=Bean.getField4()%>" >
						<span id="fundname" name="fundname"> <%=obj.getField4()%></span></display:column>
						<display:column title="<%=Bean.getField5()%>" >
						<span id="fundsource" name="fundsource"> <%=obj.getField5()%></span></display:column>
						<display:column title="<%=Bean.getField6()%>" >
						<span id="amount" name="amount"> <%=obj.getField6()%></span></display:column>
						<display:column title="Select">
						<input  align="middle" type="checkbox" id="selectToPay" name="selectToPay" onclick="showPayButton()">
						</display:column>
						<display:column title=""  style="width=0px"><%=obj.getField7()%></display:column>
						<display:footer>

							<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
							<input   style="display:none" align="middle" type="checkbox" id="selectToPay" name="selectToPay" onclick="showPayButton()">

			  			</display:footer>
					</display:table>
					</div>
			
<%					}catch(Exception e){e.getMessage();}
			}else
			{
%>
				<center>
					<display:table   export="false" sort="list" pagesize = "15" class="view" cellspacing="0" cellpadding="10">
						<display:caption style="align:center">Nothing Found to Display</display:caption>
					</display:table>
				</center>

	<%  	 }
		  }catch(Exception e){
			 System.out.print("Exception in JSP page:" + e.getMessage());
					 		}
			}//if 1
			if(request.getParameter("displayCondition") !=null && request.getParameter("displayCondition").equals("1") &&
				list!=null && list.size()>0)
			{
	%>
			<script>
			//document.getElementById('paySupConHeader').style.display='block';
			</script>
	<%		}
	%>						  
<div name="paySupConHeader" id="paySupConHeader" style="DISPLAY: none">
	<table width="100%" border="0" align=center cellpadding="0" cellspacing="0">
	<br>
	<tr align=center>
		<td width="40%"><input   type="button" class="button" value="Pay Supplier/Contractor"  onclick="setPaymentValues();" /></td>
	</tr>
	</table>
	</div>

</form>

</body>
</html>
