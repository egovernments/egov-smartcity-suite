<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.egov.utils.FinancialConstants"%>
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
	<head>
		<title>Advance Payment</title>

		<script language="javascript">
		var l_relType=2;
		var mde="";
		var bankFundis="";
		var sIndex,sValue,sText;
		var cIndex,cValue,cText;
		var vouchertypelength = '<%=FinancialConstants.VOUCHERNO_TYPE_LENGTH%>';
function loadWorkOrderData(obj){
	if(obj.selectedIndex==-1)return;

	var workOrderID=obj.options[obj.selectedIndex].value;

	PageManager.DataService.setQueryField('workOrderID',workOrderID);
	PageManager.DataService.callDataService('maxAdvance');
	PageManager.DataService.callDataService('WorkFundBank');
	PageManager.DataService.setQueryField("workOrderID",workOrderID);
	PageManager.DataService.callDataService('schemeSubSchemeForWO');
	
}
function loadWorkOrders(splCode){
	if(splCode==null || splCode=="")return;
	PageManager.DataService.setQueryField('supplierCode',splCode);
	var obj=document.getElementById('worksDetail_id');
	obj.setAttribute('exilListSource','worksDetail_id');
	clearCombo('worksDetail_id');
	PageManager.DataService.callDataService('workOrderListForAdv');
}
function displayCode()
{
var obj=document.getElementById("contractor_supplier_name");

var arr;

if(cIndex!=null)
{
		document.forms[0].contractor_supplier_name.options[cIndex]= new Option(cText,cValue);
}
arr=obj.options[obj.selectedIndex].innerHTML.split("`-`");
var conObj=document.getElementById("contractor_supplier_code");
conObj.value=arr[1];
//document.getElementById("worksDetail_code").value="";
obj.options[obj.selectedIndex].text=arr[0];
cIndex=obj.selectedIndex;
cValue=obj.options[obj.selectedIndex].value;
cText=arr[0]+"`-`"+arr[1];
document.getElementById('relation_id').value=obj.options[obj.selectedIndex].value;
loadWorkOrders(conObj.value);
}


 function hideButtons(){

		document.getElementById('modGrid').style.display="block";
		document.getElementById('submitGrid').style.display ="none";

		}
		function hideBut(){

			document.getElementById('modGrid').style.display="none";
			document.getElementById('submitGrid').style.display ="block";

		}


function clearCombo(cId)
{
   var bCtrl=document.getElementById(cId);
   for(var i=bCtrl.options.length-1;i>=0;i--)
   {
   	bCtrl.remove(i);
   }
   var maxAdvObj=document.getElementById('worksDetail_advancePayable');
   maxAdvObj.value="";
}
function onBodyLoad(){
						<jsp:include page="../VoucherTypeForULB.jsp">
											<jsp:param name="vType" value="Payment" />
											<jsp:param name="VoucherNumberNew" value="voucherHeader_voucherNumber" />
											<jsp:param name="VoucherText" value="voucherText" />
											<jsp:param name="VoucherNumberRev" value="voucherHeader_newVcNo" />
											<jsp:param name="VoucherTextRev" value="VoucherTextRev" />
											
											<jsp:param name="JVoucherNumberNew" value="jv_voucherNumber" />
											<jsp:param name="JVoucherText" value="jvoucherText" />
											<jsp:param name="JVoucherNumberRev" value="voucherHeader_newJVcNo" />
											<jsp:param name="JVoucherTextRev" value="jVoucherTextRev" />
											<jsp:param name="isPayment" value="YES"/>
											<jsp:param name="DepartmentId"   value="departmentId"/>
											<jsp:param name="DepartmentStar"  value="departmentStar" />
											
						</jsp:include>

	document.getElementById('reverseSubmit').style.display ="none";
	document.getElementById('egUser_id').value = CookieManager.getCookie('currentUserId');
	PageValidator.addCalendars();
	var drillDownCgno=PageManager.DataService.getQueryField('drillDownCgn');
	var sm=PageManager.DataService.getQueryField('showMode');

	if(drillDownCgno )
	{

		if(sm=="edit"){
		PageManager.ListService.callListService();
				mode="edit";
				//PageManager.DataService.callDataService('miscReceiptList');
				var bankObj=document.getElementById('bank_id');
				bankObj.removeAttribute('exilListSource');
				bankObj.setAttribute('exilListSource','bankAndBranch');
				var aObj = document.getElementById('accountGroup_accountNumberId');
				aObj.setAttribute('exilListSource','accountList');
				

				var wObj=document.getElementById('worksDetail_id');

				wObj.setAttribute('exilListSource','worksDetail_id');
				PageManager.DataService.setQueryField('cgNumber',drillDownCgno);
				PageManager.DataService.removeQueryField('fundValue');

				PageManager.DataService.callDataService('loadSubLedgerAdvanceData');
				

				//obj.setAttribute('exilListSource','conBillDetail_worksDetailID');
				document.getElementById('voucherHeader_voucherNumber').setAttribute('exilDataType','exilAlphaNumeric');
		  		document.getElementById('modGrid').style.display='none';
		  		document.getElementById('submitGrid').style.display ='block';
		  		document.getElementById('saveandnew').style.display ="none";
				document.getElementById('cgnLabel').style.display='block';
		  		document.getElementById('cgnText').style.display='block';
		  		document.getElementById('backbutton').style.display='none';
		  		//document.forms[0].voucherHeader_voucherNumber.disabled=true;
		 		document.getElementById('modeOfExec').value="edit";
		 		//document.getElementById('screenName').innerHTML="Advance Payment -Modify";
		 		
		 		}
		 	 else{

				window.cgnSet=true;
				window.data=true;
				disableControls(0,true);
				hideButtons();
				PageManager.ListService.callListService();
				if(sm=="modify"){
					//document.getElementById('modGrid').style.display='block';
					document.getElementById('backbutton').style.display="none";
					document.getElementById('buttonReverse').disabled=false;
					document.getElementById('revSaveClose').disabled=false;	
					document.getElementById('revClose').disabled=false;
				}else{
					document.getElementById('modGrid').style.display='none';
					document.getElementById('backbutton').style.display='block';
					document.getElementById('buttonBack').disabled=false;
					  

				}

				document.getElementById('voucherHeader_voucherNumber').setAttribute('exilDataType','exilAlphaNumeric');
				document.getElementById('cgnLabel').style.display='block';
				document.getElementById('cgnText').style.display='block';
				//document.getElementById('bankbal').style.display='none';
				document.getElementById('submitGrid').style.display ="none";
				var bankObj=document.getElementById('bank_id');
				bankObj.removeAttribute('exilListSource');
				bankObj.setAttribute('exilListSource','bankAndBranch');
				var aObj = document.getElementById('accountGroup_accountNumberId');
				aObj.setAttribute('exilListSource','accountList');
				var wObj=document.getElementById('worksDetail_id');
				wObj.setAttribute('exilListSource','worksDetail_id');
				PageManager.DataService.callDataService('loadSubLedgerAdvanceData');
				
		}

	}
	else{
		hideBut();
		 document.getElementById('backbutton').style.display="none";
		PageManager.ListService.callListService();
		loadConSupCode();
		PageManager.DataService.callDataService('cgNumber');
		//document.advanceJournal.voucherHeader_voucherNumber.focus();
	}
}

function edit(){
				var cgn=PageManager.DataService.getQueryField('drillDownCgno');
		 		if(cgn){
		 			//document.forms[0].voucherHeader_cgn.disabled=true;
		 			document.forms[0].voucherHeader_voucherNumber.disabled=true;
		 			document.getElementById('submitGrid').style.display ="block";
					document.getElementById('modeOfExec').value="edit";
					//enableControls(0,false);
		 	 	}
		}

function afterRefreshPage(dc)
{

		if (dc.values['serviceID']=='loadSubLedgerAdvanceData' )
		{
			var ctr= document.getElementById('accountGroup_accountNumberId'); //alert(ctr);
			accntMod(ctr);
		}
		var sm=PageManager.DataService.getQueryField('showMode');
		if (sm=='view' && dc.values['serviceID']=='loadSubLedgerAdvanceData' )
		{
				//document.getElementById("voucherHeader_voucherNumberPrefix").style.display="block";
			/*	var vnum=dc.values['voucherHeader_voucherNumber'];
				var subVnum=vnum.substring(0,vouchertypelength);

				   document.getElementById("voucherHeader_voucherNumber").value=vnum.substring(vouchertypelength,vnum.length);
				   document.getElementById("voucherHeader_voucherNumberPrefix").value=subVnum;

				document.getElementById("jv_voucherNumberPrefix").style.display="block";
				var vnum=dc.values['jv_voucherNumber'];
				var subVnum=vnum.substring(0,vouchertypelength);

				 document.getElementById("jv_voucherNumber").value=vnum.substring(vouchertypelength,vnum.length);
				 document.getElementById("jv_voucherNumberPrefix").value=subVnum;*/


				   var voucherHeader_voucherDate=dc.values['voucherHeader_voucherDate'];
				   var voucherDate=formatDateToDDMMYYYY4(voucherHeader_voucherDate);
				   document.getElementById('voucherHeader_voucherDate').value=voucherDate;
				    if(document.getElementById("oldVoucherDate").value == "")
					{
						document.getElementById("oldVoucherDate").value=formatDateToDDMMYYYY4(dc.values['voucherHeader_voucherDate']);
					}
				//alert(document.getElementById("oldVoucherDate").value);
		 		loadBankBalance();
		 		
		}

			if(dc.values['databaseDate']!='' && dc.values['serviceID']=='cgNumber')
			{
			var dt=dc.values['databaseDate']
			document.getElementById('voucherHeader_voucherDate').value=dt;
			document.getElementById('chequeDetail_chequeDate').value=dt;

			}
			if(dc.values['serviceID']=='contraBalNumberF')
			{
			document.getElementById('bank_bal').value=dc.values['balAvailable'];
			}
var voucherHeader_voucherDate=dc.values['voucherHeader_voucherDate'];
if(dc.values['serviceID']!='contraBalNumberF' && dc.values['serviceID']!='accNumber'){
	if(voucherHeader_voucherDate){
	var voucherDate=formatDateToDDMMYYYY4(voucherHeader_voucherDate);
	document.getElementById('voucherHeader_voucherDate').value=voucherDate;
		}
	}
	var chequeDetail_chequeDate=dc.values['chequeDetail_chequeDate'];
	if(chequeDetail_chequeDate){
	chequeDetail_chequeDate=formatDateToDDMMYYYY4(chequeDetail_chequeDate);
	document.getElementById('chequeDetail_chequeDate').value=chequeDetail_chequeDate;
	}

	if(mde=="edit" && dc.values['serviceID']=='loadSubLedgerAdvanceData')
	{
	document.getElementById('worksDetail_oldAmount').value=dc.values['contracotrBillDetail_advanceA'];
	document.getElementById('worksDetail_oldWorksdetailId').value=dc.values['worksDetail_id'];

	}
	if( dc.values['serviceID']=='AccountFundMap1')
	{
		
		 bankFundis=document.getElementById('fund1_id').value;
		
	}
	
	/*if(dc.values['serviceID']=="loadSubLedgerAdvanceData")
	{
	var fund=dc.values['fund_id'];
	var schm=dc.values['scheme'];
	schemelistforvm(fund);
	subschemelistforvm(schm);

	
	}*/
	
	if(dc.values['serviceID']=='loadSubLedgerAdvanceData')
	{
		var table = document.getElementById('TABLE1');
		var paymentType = document.getElementById('subLedgerPaymentHeader_typeOfPayment');
		var codeLabel = 'Contractor / Supplier Code<SPAN class="leadon">*</SPAN>';
		var refLabel = 'Work Order / PO Ref Code<SPAN class="leadon">*</SPAN>';
		if(dc.values['typeOfPayment'].toLowerCase() == 'contractor'){
			codeLabel = 'Contractor Code<SPAN class="leadon">*</SPAN>';
			refLabel = 'Work Order Ref No.<SPAN class="leadon">*</SPAN>';
			l_relType=2;
		}
		if(paymentType.options[paymentType.selectedIndex].value.toLowerCase() == 'supplier'){
			codeLabel = 'Supplier Code<SPAN class="leadon">*</SPAN>';
			refLabel = 'PO Ref No.<SPAN class="leadon">*</SPAN>';
			l_relType=1;
		}
		table.rows[2].childNodes[0].childNodes[0].innerHTML = codeLabel;
		table.rows[2].childNodes[2].childNodes[0].innerHTML = refLabel;
	}
}



function beforeRefreshPage(dc)
{
	if(dc.values['serviceID']=='contraBalNumberF')
		PageManager.DataService.removeQueryField('voucherHeader_voucherDate');

	if(dc.values['glCodeName'])
		{
		  glNameCode=dc.values['glCodeName'];
		 showEntry();
	}
	if(dc.values['serviceID']=='loadSubLedgerAdvanceData'){
		dc.values['contracotrBillDetail_advanceAmount']=dc.values['contracotrBillDetail_advanceA'];
		document.getElementById("oldAmount").value=dc.values['contracotrBillDetail_advanceA'];
		dc.values['subLedgerPaymentHeader_typeOfPayment']=dc.values['typeOfPayment'];
	}
	if(isNaN(parseInt(dc.values['voucherHeader_cgn']))) return;
	dc.values['voucherHeader_cgn'] = 'ASP' + dc.values['voucherHeader_cgn'];



}
			function checkNarration(narration)
			{
				if(narration.value.length>=250){
				  alert("Narration Cannot Be More Than " + 250 + " Characters");
				  return false;
				}
				return true;
			}

			function changeLabelView()
			{
				var table = document.getElementById('TABLE1');
				var paymentType = document.getElementById('subLedgerPaymentHeader_typeOfPayment');
				var codeLabel = 'Contractor / Supplier Code<SPAN class="leadon">*</SPAN>';
				var refLabel = 'Work Order / PO Ref Code<SPAN class="leadon">*</SPAN>';
				if(paymentType.options[paymentType.selectedIndex].value.toLowerCase() == 'contractor'){
					codeLabel = 'Contractor Code<SPAN class="leadon">*</SPAN>';
					refLabel = 'Work Order Ref No.<SPAN class="leadon">*</SPAN>';
					l_relType=2;
				}
				if(paymentType.options[paymentType.selectedIndex].value.toLowerCase() == 'supplier'){
					codeLabel = 'Supplier Code<SPAN class="leadon">*</SPAN>';
					refLabel = 'PO Ref No.<SPAN class="leadon">*</SPAN>';
					l_relType=1;
				}
				table.rows[2].childNodes[0].childNodes[0].innerHTML = codeLabel;
				table.rows[2].childNodes[2].childNodes[0].innerHTML = refLabel;
			}
			
			function changeLabel()
			{
				loadConSupCode();

				var table = document.getElementById('TABLE1');
				var paymentType = document.getElementById('subLedgerPaymentHeader_typeOfPayment');
				var codeLabel = 'Contractor / Supplier Code<SPAN class="leadon">*</SPAN>';
				var refLabel = 'Work Order / PO Ref Code<SPAN class="leadon">*</SPAN>';
				if(paymentType.options[paymentType.selectedIndex].value.toLowerCase() == 'contractor'){
					codeLabel = 'Contractor Code<SPAN class="leadon">*</SPAN>';
					refLabel = 'Work Order Ref No.<SPAN class="leadon">*</SPAN>';
					l_relType=2;
				}
				if(paymentType.options[paymentType.selectedIndex].value.toLowerCase() == 'supplier'){
					codeLabel = 'Supplier Code<SPAN class="leadon">*</SPAN>';
					refLabel = 'PO Ref No.<SPAN class="leadon">*</SPAN>';
					l_relType=1;
				}

				table.rows[2].childNodes[0].childNodes[0].innerHTML = codeLabel;

				table.rows[2].childNodes[2].childNodes[0].innerHTML = refLabel;
				document.getElementById('contractor_supplier_code').value = '';
				clearCombo('worksDetail_id');
			}

			function loadConSupCode(){
				var typeId;
				var typeObj = document.getElementById('subLedgerPaymentHeader_typeOfPayment');
				if(typeObj.value=='contractor')
					typeId=2;
				else
					typeId=1;
				var obj = document.getElementById('contractor_supplier_name');
				obj.setAttribute('exilListSource','popupGrid');
				PageManager.DataService.setQueryField('relationType',typeId);
				PageManager.DataService.callDataService('codeAndDescriptionCombo');
			}


			function openSearch(obj)
			{
				var a = new Array(2);
				var tableName;
				/************/
				var ctrl = document.getElementById('subLedgerPaymentHeader_typeOfPayment');
				var tableName = ctrl.options[ctrl.selectedIndex].text;

				var sRtn = showModalDialog("Search.html?tableNameForCode=relation&relationType="+l_relType,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
				if(sRtn != '')
				{
					a = sRtn.split("`~`");

			 		var x = PageManager.DataService.getControlInBranch(obj.parentNode,'contractor_supplier_code');
			 		var y = document.getElementById('relation_id');
			 		x.value = a[0];
			 		y.value = a[2];
			 		loadWorkOrders(a[0]);
				}
			}

			function getAccountNumbers()
			{
				var ctrl = document.getElementById('bank_id');
				var branchid = ctrl.options[ctrl.selectedIndex].value;
				var a = Array(2);
				var obj = document.getElementById('accountGroup_accountNumberId');
				removeCombo(obj);
				sIndex=null;//Need to assign null to avoid concatination of description with previous Bank related Accnt No.
				a = branchid.split('-');
				branchid = a[1];
				document.getElementById("acc_Desc").value="";
				

				obj.setAttribute('exilListSource','accountList');
				PageManager.DataService.setQueryField('branchId',branchid);
			//	PageManager.DataService.setQueryField('fundValue',document.getElementById('fund_id').value);
				PageManager.DataService.callDataService('accNumber');

			}



		function ButtonPress(name)
		{
			if(name.toLowerCase()=='savenew')
			str="new";
			if(name.toLowerCase()=='saveclose')
			str="close";
			if(name.toLowerCase()=='saveview')
			str="view";
		   var drillDownCgno=PageManager.DataService.getQueryField('drillDownCgn');
		   if(drillDownCgno )
			{
				disableControls(0,false);
	        }
	        else
	        {
			if(!PageValidator.validateForm())
				return false;
			if(!validate())
				return false;
			if(Type!='Auto')
			{
			if(!checkFund())
				return false;
			}
			var sm=PageManager.DataService.getQueryField('showMode');
			if(sm!='modify')
				{
					if(!checkBalance())
			      		return false;
			      
				}
			}

		 		var dbDate=document.getElementById('databaseDate').value;
				var vhDate=document.getElementById('voucherHeader_voucherDate').value;
				if(compareDate(formatDateToDDMMYYYY1(vhDate),formatDateToDDMMYYYY1(dbDate)) == -1 )
				{
				alert('Voucher Date should be less than or equal to '+dbDate);
				document.getElementById('voucherHeader_voucherDate').focus();
				return false;
				}
				var sm=PageManager.DataService.getQueryField('showMode');
				if(sm=='modify')
				{

				//	if(!checkFundReverse())
				//	return false;
					var effDate=document.getElementById('voucherHeader_effDate').value;
					if(compareDate(formatDateToDDMMYYYY1(effDate),formatDateToDDMMYYYY1(dbDate)) == -1 ){
					alert('Voucher Date should be less than or equal to '+dbDate);
					document.getElementById('voucherHeader_effDate').focus();
					return false;

				}
				}
			if(sm=='edit'){
			    if(!checkBalance())
			      return false;
				if(!validate())
				return false;
				if(Type!='Auto')
				{
				if(!checkFund())
				return false;
				}
			}
			PageManager.UpdateService.submitForm('advanceJournal');
		}

		function validate(){
			var maxAdvObj=document.getElementById('worksDetail_advancePayable');
			var advObj=document.getElementById('contracotrBillDetail_advanceAmount');
			var advPaidObj=document.getElementById('worksDetail_advanceAmount');
			if(!checkChequeDate("chequeDetail_chequeDate")) return false;
			if(!checkNarration(document.getElementById('voucherHeader_narration')))
				return false;
			if(parseFloat(advObj.value)<=0){
				alert('Enter Transaction  Amount Greater Than Zero');
				return false;
			}
			
			if((parseFloat(advObj.value)+parseFloat(advPaidObj.value))>parseFloat(maxAdvObj.value)){
				alert('Advance crossed Max advance.Max:'+maxAdvObj.value);
				return false;
			}
			return true;
		}

		function afterUpdateService(dc)
		{
			 disableControls(0,true);
			 
			var voucherHeader_voucherNumber=dc.values['voucherHeader_voucherNumber'];
			var fieldid=document.getElementById("field_name");
			var field_name=fieldid.options[fieldid.selectedIndex].text;
			var fund_name=dc.values['fund_name'];
			var voucherHeader_voucherDate=document.forms[0].voucherHeader_voucherDate.value;
			var voucherHeader_createdby=dc.values['current_loginName'];

			var scheme=" ";
			var schemeid=document.getElementById("scheme");
			if(schemeid.value!='' && schemeid.value!=null)
				scheme=schemeid.options[schemeid.selectedIndex].text;

			var subscheme=" ";
			var subschemeid=document.getElementById("subscheme");
			if(subschemeid.value!='' && subschemeid.value!=null)
				subscheme=subschemeid.options[subschemeid.selectedIndex].text;

			var narration=document.getElementById("voucherHeader_narration").value;


			if(str=="new")	window.location = "AdvanceJournal_VMC.jsp"
			if(str=="close") window.close();
			var showMode='<%=request.getParameter("showMode")%>';
			if(str=="view")	window.location = "JV_voucherview_VMC.jsp?field_name="+field_name+"&scheme="+scheme+"&subscheme="+subscheme+"&narration="+narration+"&voucherHeader_voucherNumber="+voucherHeader_voucherNumber+"&fund_name="+fund_name+"&reportName=ADVANCE PAYMENT VOUCHER"+"&voucherHeader_voucherDate="+voucherHeader_voucherDate+"&voucherHeader_createdby="+voucherHeader_createdby+"&showMode="+showMode;
			

		}

		function onClickCancel()
		{
			window.location = "AdvanceJournal_VMC.jsp"
		}
		function reverse()
			{
			var cgn=PageManager.DataService.getQueryField('drillDownCgn');
			if(cgn){
				document.getElementById('reverseRecord').style.display='block';
				var vcObj=document.getElementById('voucherHeader_newVcNo');
				//vcObj.setAttribute('exilMustEnter','true');
				vcObj.disabled=false;
				var jvcObj=document.getElementById('voucherHeader_newJVcNo');
				jvcObj.disabled=false;
				var vdObj=document.getElementById('voucherHeader_effDate')
				vdObj.setAttribute('exilMustEnter','true');
				vdObj.disabled=false;
				var bcObj=document.getElementById('diffDebit')
				bcObj.disabled=false;
				document.getElementById('modeOfExec').value="modify";
				document.getElementById('reverseSubmit').style.display ="block";
			}
		}
		function showglEntry()
		{
			hideColumn(0);
			displayColumn(1);
			displayColumn(2);
			displayColumn(3);
			displayColumn(4);
			displayColumn(5);
			if(!PageValidator.validateForm())
				return false;
			PageManager.DataService.setQueryField("showGlEntries","show");
			PageManager.DataService.setQueryField("showJVGlEntries","no");
			var accNumberObj=document.getElementById("accountGroup_accountNumberId");
			var accNumberType=accNumberObj.options[accNumberObj.selectedIndex].value
			PageManager.DataService.setQueryField("accountNumberId",accNumberType);
			var typeObj=document.getElementById("subLedgerPaymentHeader_typeOfPayment");
			var payType=typeObj.options[typeObj.selectedIndex].value
			PageManager.DataService.setQueryField("typeOfPayment",payType);
			PageManager.DataService.setQueryField("fund_id",document.getElementById("fund_id").value);
			//PageManager.DataService.setQueryField("fund1_id",bankFundis);
			PageManager.DataService.setQueryField("fund1_id",document.getElementById('fund1_id').value);
			PageManager.DataService.callDataService('advanceJournal');
		}

		function showJVglEntry()
		{
		
				 if(document.getElementById('fund_name').value==document.getElementById('fund1_name').value)
	    		 {
	    			alert("There is no Journal Entry passed.");
	    			hideColumn(1);
	    			hideColumn(2);
	    			hideColumn(3);
	    			hideColumn(4);
	    			hideColumn(5);
	    			return;
				 }
				 else{
				 	hideColumn(0);
				 	displayColumn(1);
				 	displayColumn(2);
				 	displayColumn(3);
				 	displayColumn(4);
				 	displayColumn(5);

					if(!PageValidator.validateForm())
						return false;
					PageManager.DataService.setQueryField("showGlEntries","show");
					PageManager.DataService.setQueryField("showJVGlEntries","yes");
					var accNumberObj=document.getElementById("accountGroup_accountNumberId");
					var accNumberType=accNumberObj.options[accNumberObj.selectedIndex].value
					PageManager.DataService.setQueryField("accountNumberId",accNumberType);
					var typeObj=document.getElementById("subLedgerPaymentHeader_typeOfPayment");
					var payType=typeObj.options[typeObj.selectedIndex].value
					PageManager.DataService.setQueryField("typeOfPayment",payType);
					PageManager.DataService.setQueryField("fund_id",document.getElementById("fund_id").value);
					PageManager.DataService.setQueryField("fund1_id",document.getElementById("fund1_id").value);
					PageManager.DataService.callDataService('advanceJournal');
				}
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
			var totalamount=document.getElementById('contracotrBillDetail_advanceAmount').value;

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

	function hideColumn(index)
	{
		var table=document.getElementById('entries');
	   	for(var i=0;i<table.rows.length;i++){
			table.rows[i].cells[index].style.display="none";
	   	}
	}

	function displayColumn(index)
		{
			var table=document.getElementById('entries');
		   	for(var i=0;i<table.rows.length;i++){
				table.rows[i].cells[index].style.display="block";
		   	}
	}
		function loadBankBalance()
		{
			var ctr= document.getElementById('accountGroup_accountNumberId');
			var voucherHeader_voucherDate=document.getElementById('voucherHeader_voucherDate').value;
			    if(!voucherHeader_voucherDate)
			   {
			    	alert("Please select the Voucher Date ");
			    	return;
			   }
			   if(ctr.selectedIndex != -1){
			     var bal_from=ctr.options[ctr.selectedIndex].value;
			     PageManager.DataService.setQueryField('voucherHeader_voucherDate',voucherHeader_voucherDate);
			     PageManager.DataService.setQueryField('bal_from',bal_from);
			     PageManager.DataService.callDataService('contraBalNumberF');
				}
	}
	function checkBalance()
	{
	    var amt=document.getElementById("contracotrBillDetail_advanceAmount").value;
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

	function checkFund()
	{
	
	    if(document.getElementById('fund_id').value!=document.getElementById('fund1_id').value)
	    {
			  if(!confirm("WARNING:This will be an Inter Fund Transfer."))
				    return false;
			else{
				if(document.getElementById("jv_voucherNumber").value.length==0){
					alert("Enter value for Journal Voucher Number.");
					return false;
				}
			}
		}
		else{
			document.getElementById("jv_voucherNumber").value='';
			}
	
		return true;
	}

	function checkFundReverse()
		{
	
		    if(document.getElementById('fund_id').value!=document.getElementById('fund1_id').value)
		    {
					if(document.getElementById("voucherHeader_newJVcNo").value.length==0){
						alert("Enter value for Journal Voucher Number.");
						document.getElementById("voucherHeader_newJVcNo").focus();
						return false;
					}

			}
			else
				document.getElementById("voucherHeader_newJVcNo").value='';

			return true;
	}

	function loadFund(obj){
				if(obj.selectedIndex==-1) return;
				bankFundis="";
				accntMod(obj);
				var accId=obj.options[obj.selectedIndex].value;
				PageManager.DataService.setQueryField('bankIdForFund',accId);
				PageManager.DataService.callDataService('AccountFundMap1');
				loadBankBalance();
				
		}
		function getschemelist()
				{
					if (document.getElementById('fund1_id').value!="")
					{
						var obj=document.getElementById('scheme');
						clearCombo('scheme');
						clearCombo('subscheme');
						obj.removeAttribute('exilListSource');
						obj.setAttribute('exilListSource','schemelist');
						PageManager.DataService.setQueryField("fundId",document.getElementById('fund1_id').value);
						PageManager.DataService.callDataService("schemelist");
					}
					else
					clearCombo('subscheme');
				}
				function getsubschemelist(obj)
				{
					var opt=obj.value;
					if(opt!="")
					{
					var obj=document.getElementById('subscheme');
					obj.removeAttribute('exilListSource');
					obj.setAttribute('exilListSource','subschemelist');
					PageManager.DataService.setQueryField("schemeId",opt);
					PageManager.DataService.callDataService("subschemelist");
				}
				else
					clearCombo('subscheme');

				}
				
function schemelistforvm(fund)	 
{
var opt=fund;
if(opt!="")
{
var obj=document.getElementById('scheme');
obj.removeAttribute('exilListSource');
obj.setAttribute('exilListSource','schemelist');
PageManager.DataService.setQueryField("fundId",opt);
PageManager.DataService.callDataService("schemelist");
}

}
function subschemelistforvm(schm)
{
var opt=schm;
if(opt!="")
{
var obj=document.getElementById('subscheme');
obj.removeAttribute('exilListSource');
obj.setAttribute('exilListSource','subschemelist');
PageManager.DataService.setQueryField("schemeId",opt);
PageManager.DataService.callDataService("subschemelist");
}
}		 

function accntMod(obj)
{
	
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
	try{
	   if(temp[1]!=null && temp[1]!="" )
	   {
	     obj.options[obj.selectedIndex].text=temp[0];
	   }
   }catch(err){}
}
function removeCombo(obj)
{
  for(var i=obj.options.length;i>=0;i--)
      obj.remove(i);
}

function nextChqNo()
{
	var obj=document.getElementById("accountGroup_accountNumberId");
	var bankBr=document.getElementById("bank_id");
	if( bankBr.selectedIndex==-1)
	{
	  alert("Select Bank Branch and Account No!!");
	  return;
	}

	if(obj.selectedIndex==-1)
	{
	  alert("Select Account No!!");
	  return;
	}
	var accNo=obj.options[obj.selectedIndex].text;
	var accNoId=obj.options[obj.selectedIndex].value;
	var sRtn =showModalDialog("../SearchNextChqNo.html?accntNo="+accNo+"&accntNoId="+accNoId,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	if(sRtn!=="undefined")
	document.getElementById("chequeDetail_chequeNumber").value=sRtn;
}

function fillDate1(objName)
	{
	PageValidator.showCalendar('selectedDate');
	document.getElementById(objName).value=document.getElementById('selectedDate').value;
	document.getElementById('selectedDate').value = "";
	}
function checkfund()
{
var tempfund=document.getElementById("fund_name").value;

if(tempfund=="")
alert("Select Fund First");

}
function checkscheme()
{
var tempfund=document.getElementById("scheme").value;

if(tempfund=="")
alert("Select Scheme First");
}
	
	</script>


	</head>
	<body onLoad="onBodyLoad()" onKeyDown="CloseWindow(window.self);"
		bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0"
		leftmargin="0" marginheight="0" marginwidth="0">
		<!------------------ Header Begins Begins--------------------->

		<form name="advanceJournal">
			<input type=hidden id="modeOfExec" name="modeOfExec" value="new">
			<input type="hidden" name="egUser_id" id="egUser_id">
			<input type="hidden" name="relation_id" id="relation_id">
			<input type="hidden" name="budgetCheckRequired"
				id="budgetCheckRequired" value="true">
			<input type="hidden" name="tType" id="tType" value="P">
			<input type="hidden" name="cgnNum" id="cgnNum">
			<input type="hidden" name="oldAmount" id="oldAmount">
			<input type="hidden" name="voucherHeader_id" id="voucherHeader_id">
			<input type="hidden" name="oldVoucherDate" id="oldVoucherDate">
			<input type="hidden" name="selectedDate" id="selectedDate">
			<input type=hidden name=databaseDate id=databaseDate>
			<input type=hidden name="worksDetail_advancePayable"
				id="worksDetail_advancePayable">
			<input type=hidden name="worksDetail_advanceAmount"
				id="worksDetail_advanceAmount">
			<input type=hidden name="worksDetail_oldAmount"
				id="worksDetail_oldAmount">
			<input type=hidden name="worksDetail_oldWorksdetailId"
				id="worksDetail_oldWorksdetailId">
			<input type=hidden name="cvType" id="cvType" value="J">
			<table class="tableStyle" align=center>
				<tr>
					<td>
						<table align="center" width="100%">
							<tr>
								<td>
									<table width="100%" border="0" cellpadding="1" cellspacing="0">
										<tr>
											<td valign="top" class="labelcell">
												<!------------Content begins here ------------------>
												<table width="100%" border="0" cellpadding="3"
													cellspacing="0" name="TABLE1" id=TABLE1>
													<TBODY>

														<tr>
															<td id="cgnLabel" name="cgnLabel" align="right"
																valign="center" class="labelcell" style="DISPLAY: none">
																<div align="right">
																	<bean:message key="AdvancePymt.cgNumber" />
																	<br>
																	CG&nbsp;Number&nbsp;&nbsp;
																</div>
															</td>
															<td class="fieldcell" id="cgnText" name="cgnText"
																style="DISPLAY: none">
																<input name="voucherHeader_cgn" class="fieldinput"
																	id="voucherHeader_cgn" readOnly maxlength="16"
																	exilMustEnter="true" exilDataType="exilAlphaNumeric">
															</td>
															<td align="right" valign="center" class="labelcell"
																id="VoucherText">
																<div align="right">
																	<bean:message key="AdvancePymt.voucherNumber" />
																	<br>
																	Voucher Number
																	<span class="leadon">*</span>
																</div>
															</td>
															<td width="33%">
																<table cellpadding="0" cellspacing="0" WIDTH="100">
																	<tr>
																		<td class=tinyfieldcell>
																			<input name="voucherHeader_voucherNumberPrefix"
																				maxlength="5" class="tinyCell"
																				id="voucherHeader_voucherNumberPrefix" readonly
																				style="display: none" style="height:20px" SIZE="1">
																		</td>
																		<td class=fieldcell>
																			<input name="voucherHeader_voucherNumber"
																				maxlength="16" class="fieldinput-left"
																				id="voucherHeader_voucherNumber"
																				exilMustEnter="true" exilDataType="exilAlphaNumeric"
																				SIZE="20">
																		</td>
																	</tr>
																</table>
															</td>
														</tr>

														<tr>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.type" />
																	<br>
																	Type Of Party
																	<span class="leadon">*</span>
																</div>
															</td>
															<td class="smallfieldcell">
																<SELECT id="subLedgerPaymentHeader_typeOfPayment"
																	name="subLedgerPaymentHeader_typeOfPayment"
																	class="combowidth1" onChange="changeLabel()"
																	exilMustEnter="true">
																	<option value="contractor" selected>
																		Contractor
																	</option>
																	<option value="supplier">
																		Supplier
																	</option>
																</SELECT>
															</td>
															<td align="left" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.voucherDate" />
																	<br>
																	Voucher Date
																	<SPAN class=leadon>*</SPAN>
																</div>
															</td>
															<td class=smallfieldcell>
																<INPUT class=datefieldinput
																	id="voucherHeader_voucherDate"
																	name="voucherHeader_voucherDate" size="13"
																	maxlength="11"
																	onkeyup="DateFormat(this,this.value,event,false,'3')"
																	exilMustEnter="true" exilDataType="exilAnyDate">
																&nbsp;
																<A onclick="fillDate1('voucherHeader_voucherDate')"
																	tabIndex=-1 href=#><IMG tabIndex=-1
																		src="../../images/calendar.gif">
																</A>
															</td>
														</tr>
														<tr>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.contractor" />
																	<br>
																	Contractor Code
																	<SPAN class=leadon>*</SPAN>
																</div>
															</td>
															<td class=smallfieldcell>
																<select name="contractor_supplier_name"
																	class="combowidth2" id="contractor_supplier_name"
																	maxlength="50" onChange="displayCode()"
																	exilMustEnter="true"></select>
																<input name="contractor_supplier_code"
																	id="contractor_supplier_code" class="accfieldinput"
																	tabIndex=2 readOnly></input>
															</td>


															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.workOrderRef" />
																	<br>
																	Work Order Ref No.
																	<SPAN class=leadon>*</SPAN>
																</div>
															</td>
															<td class=smallfieldcell>
																<select class="combowidth1" id="worksDetail_id"
																	name="worksDetail_id"
																	onChange="loadWorkOrderData(this)" exilMustEnter="true"
																	exilDataType="exilAlphaNumeric">
																</select>
																<DIV></DIV>
															</td>
														</tr>

														<tr>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	Department &nbsp;
																	<SPAN id="departmentStar" class=leadon>* &nbsp;</SPAN>
																</div>
															</td>
															<td align="left" width="260" class="smallfieldcell">
																<SELECT class="combowidth1" id="departmentId"
																	name="departmentId" exilListSource="departmentNameList"></SELECT>
															</td>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	Functionary
																</div>
															</td>
															<td align="left" width="260" class="smallfieldcell">
																<SELECT class="combowidth1" id="functionaryId"
																	name="functionaryId" exilListSource="functionaryName"></SELECT>
															</td>
														</tr>


														<tr>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.bank" />
																	<br>
																	Bank
																	<SPAN class=leadon>*</SPAN>
																</div>
															</td>
															<td class=smallfieldcell>
																<SELECT id="bank_id" name="bank_id" class="combowidth2"
																	onChange="getAccountNumbers()" exilMustEnter="true"
																	exilListSource="bankAndBranch">

																	<OPTION value="" selected>
																		&nbsp;
																	</OPTION>
																</SELECT>
																&nbsp;
															</td>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.accountNo" />
																	<br>
																	Account Number
																	<SPAN class=leadon>*</SPAN>
																</div>
															</td>
															<td class=smallfieldcell>
																<SELECT id="accountGroup_accountNumberId"
																	name="accountGroup_accountNumberId" class="combowidth1"
																	onChange="loadFund(this);loadBankBalance(this);"
																	exilMustEnter="true"></SELECT>
																<input type="text" id="acc_Desc" name="acc_Desc"
																	class="fieldinput" style="WIDTH: 150px" readonly>
															</td>
														</tr>

														<tr id="bankbal">
															<td>
																<div align="right" valign="center" class="labelcell">
																	<bean:message key="AdvancePymt.balance" />
																	<br>
																	Bank Balance
																</div>
															</td>
															<td class=fieldcell>
																<input name="bank_bal" id="bank_bal" class="fieldinput"
																	readonly>
															</td>
															<td class=fieldcell>
																<div align="right" valign="center" class="labelcell">
																	<bean:message key="AdvancePymt.fund" />
																	<br>
																	Bank Fund
																</div>
															</td>
															<td class=fieldcell>
																<input type="hidden" name="fund1_id" id="fund1_id"
																	class="fieldinput">
																<input name="fund1_name" id="fund1_name"
																	class="fieldinput" readonly>

															</td>
														</tr>
														<tr>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.FundName" />
																	<br>
																	Fund Name
																	<SPAN class=leadon></SPAN>
																</div>
															</td>
															<td class=fieldcell>
																<!--<SELECT id="fund_id" name="fund_id"  class="fieldinput" exilMustEnter="true" exilListSource="fundNameList">
								<OPTION value="" selected>&nbsp;</OPTION>
							</SELECT>&nbsp;-->
																<input type="hidden" name="fund_id" id="fund_id"
																	class="fieldinput">
																<input name="fund_name" id="fund_name"
																	class="fieldinput" onchange="getschemelist(this)"
																	readonly>
															</td>
															<!--		<td align="right" valign="center" class="labelcell"><div align="right">Function<SPAN class=leadon>*</SPAN></div>
						</td>
						<td>
                        <SELECT id="department_id" name="department_id"  class="fieldinput" exilMustEnter="true" exilListSource="departmentNameList">
                        <OPTION value="" selected>&nbsp;</OPTION>
                        </SELECT>&nbsp;
						</td> -->
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.financingSource" />
																	<br>
																	Financing Source
																	<SPAN class=leadon></SPAN>
																</div>
															</td>
															<td class=fieldcell>
																<input type=hidden name="fundSource_id"
																	id="fundSource_id" class="fieldinput">
																<input name="fundSource_name" id="fundSource_name"
																	class="fieldinput" readonly>
															</td>

														</tr>

														<tr>
															<td align="right" valign="center">
																<DIV align=right class="labelcell">
																	Scheme&nbsp;
																</DIV>
															</td>
															<td class=fieldcell>
																<!-- <SELECT  id="scheme" name="scheme"  class="fieldinput" onclick="checkfund()" onChange= "getsubschemelist(this)">
								        </SELECT>&nbsp;-->
																<input type="hidden" name="scheme" id="scheme"
																	class="fieldinput">
																<input id="schemeid" name="schemeid" class="fieldinput"
																	readOnly>

															</td>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	Sub Scheme&nbsp;
																</DIV>
															</td>
															<td class=fieldcell>
																<!--	<SELECT  id="subscheme" name="subscheme" onclick="checkscheme()" class="fieldinput">
										</SELECT>&nbsp;-->
																<input type="hidden" id="subscheme" name="subscheme"
																	class="fieldinput">

																<input id="subschemeid" name="subschemeid"
																	class="fieldinput" readOnly>

															</td>

														</tr>
														<tr>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.chequeNo" />
																	<br>
																	Cheque No.
																	<SPAN class=leadon>*</SPAN>
																</div>
															</td>
															<td>
																<table>
																	<tr>
																		<td class=fieldcell>
																			<input name="chequeDetail_chequeNumber"
																				class="fieldinput" id="chequeDetail_chequeNumber"
																				maxlength="10" exilMustEnter="true"
																				exilDataType="exilUnsignedInt">
																		</td>
																		<td valign="center" title="Show Next Cheque Number"
																			nowrap id="chqAvail">
																			<A onclick=nextChqNo() href="#"><IMG height=22
																					src="../../images/arrowright.gif" width=22>
																			</A>
																		</td>
																	</tr>
																</table>
															</td>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.chequeDate" />
																	<br>
																	Cheque Date
																	<SPAN class=leadon>*</SPAN>
																</div>
															</td>
															<td class=smallfieldcell>
																<input name="chequeDetail_chequeDate"
																	class="datefieldinput" id="chequeDetail_chequeDate"
																	size=13 maxlength="11"
																	onkeyup="DateFormat(this,this.value,event,false,'3')"
																	exilMustEnter="true" exilDataType="exilAnyDate">
																&nbsp;
																<A onclick="fillDate1('chequeDetail_chequeDate')"
																	tabIndex=-1 href=#><IMG tabIndex=-1
																		src="../../images/calendar.gif">
																</A>
															</td>
														</tr>
														<tr>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.advanceAmount" />
																	<br>
																	Advance&nbsp;Amount
																	<SPAN class=leadon>*</SPAN>
																</div>
															</td>
															<td class=smallfieldcell>
																<INPUT class="fieldinput-right"
																	id="contracotrBillDetail_advanceAmount"
																	name="contracotrBillDetail_advanceAmount"
																	maxlength="16" exilMustEnter="true"
																	exilDataType="exilUnsignedDecimal"
																	exilMax="9999999999999.99" exilMin="1">
																&nbsp;
															</td>
															<td align="right" valign="center" class="labelcell">
																<div align="right">
																	<bean:message key="AdvancePymt.narration" />
																	<br>
																	Narration
																</div>
															</td>
															<td class=fieldcelldesc>
																<TEXTAREA class=narrationfieldinput
																	id=voucherHeader_narration name=voucherHeader_narration
																	rows=3 cols=40 exilDataType="exilAnyChar"></TEXTAREA>
																&nbsp;
															</td>
														</tr>

														<tr id="jvinfo">
															<td width="18%" align="right" valign="center"
																class="labelcell" id="jvoucherText">
																<div align="right">
																	<bean:message key="AdvancePymt.journalVoucherNo" />
																	<br>
																	Journal Voucher Number **
																</div>
															</td>
															<!--<td><input name="jv_voucherNumber" maxlength="16" class="fieldinput-left" id="jv_voucherNumber"  exilDataType="exilAlphaNumeric" SIZE="20" >-->
															<td>
																<table>
																	<tr>
																		<td class=tinyfieldcell>
																			<input name="jv_voucherNumberPrefix" maxlength="2"
																				class="tinyfieldinput" id="jv_voucherNumberPrefix"
																				readonly style="display: none">
																		</td>
																		<td class=fieldcell>
																			<input name="jv_voucherNumber" maxlength="16"
																				class="fieldinput-left" id="jv_voucherNumber"
																				exilDataType="exilAlphaNumeric" SIZE="10">
																		</td>
																	</tr>
																</table>
															</td>

															<td align="right" valign="center">
																<DIV align=right class="labelcell">
																	<bean:message key="AdvancePymt.field" />
																	<br>
																	Field&nbsp;
																</DIV>
															</td>
															<td class=smallfieldcell>
																<SELECT id="field_name" name="field_name"
																	class="combowidth1" exilListSource="field_name">
																</SELECT>
															</td>
															</td>
															</td>
														</tr>
														<tr>
															<td align="middle" colspan="4">
																&nbsp;
															</td>
														</tr>

														<td colspan="4" valign="bottom" class="smalltext">
															<p class="smalltext">
																<span class="leadon">*</span> - Mandatory
																Fields&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																<span class="labelcell">**</span>-Only applicable for
																Inter Fund Transfer
															</p>
															<br>
												</table>
											</TD>
										</TR>
										<!--  Buttons for Request Form starts here -->
										<table border="0" cellpadding="0" cellspacing="0"
											align="center" id=TABLE1 width="100%">
											<TBODY>
												<tr>
													<td colspan="4" align="middle">
														<!-- Buttons Start Here -->
														<table border="0" cellpadding="0" cellspacing="0"
															id=TABLE1>
															<tbody>
																<tr id="modGrid">

																	<td>
																		<input type=button class=button class=buttonprimary
																			onclick=reverse(); href="#" value="Reverse"
																			id="buttonReverse">
																	</td>


																</tr>
																<tr>
																	<td colspan="4" align="middle">
																		<table border="0" cellpadding="0" cellspacing="0"
																			id="buttons">
																			<tr id="backbutton">
																				<td align="center">
																					<input type=button class=button id="buttonBack"
																						onclick="back(window.self)" href="#" value="Back">
																				</td>

																			</tr>

																		</table>
																	</td>
																</tr>

																<tr>
																	<td colspan=5></td>
																</tr>
																<tr id="reverseRecord" style="DISPLAY: none">
																	<td class=fieldcell id="VoucherTextRev">
																		<span align="right" valign="center" class="labelcell">Payment
																			Voucher<br /> Number<SPAN class=leadon>*</SPAN> <input
																				name="voucherHeader_newVcNo"
																				id="voucherHeader_newVcNo" maxlength="16"
																				style="WIDTH: 140px; HEIGHT: 22px" size="10"
																				exilDataType="exilAlphaNumeric"> </span>
																	</td>
																	<td class=fieldcell id="jVoucherTextRev">
																		<span align="right" valign="center" class="labelcell">Journal
																			Voucher<br /> Number <input
																				name="voucherHeader_newJVcNo"
																				id="voucherHeader_newJVcNo" maxlength="16"
																				style="WIDTH: 140px; HEIGHT: 22px" size="10"
																				exilDataType="exilAlphaNumeric"> </span>
																	</td>
																	<td></td>
																	<td class=smallfieldcell>
																		<span align="right" valign="center" class="labelcell"><bean:message
																				key="AdvancePymt.voucherDate" />
																			<br>Voucher Date<span class="leadon">*</span> <INPUT
																				class="datefieldinput" name="voucherHeader_effDate"
																				id="voucherHeader_effDate"
																				onkeyup="DateFormat(this,this.value,event,false,'3')"
																				exilDataType="exilAnyDate"
																				style="WIDTH: 101px; HEIGHT: 22px" size="13">
																		</span><A onclick="fillDate1('voucherHeader_effDate')"
																			tabIndex=-1 href=#><IMG tabIndex=-1
																				src="../../images/calendar.gif">
																		</A>
																	</td>
																	<td></td>
																	<td>
																		<!--<span  align="right" valign="center" class="labelcell" >Bank Charge-->

																		<INPUT type=hidden class="datefieldinput"
																			name="diffDebit" id="diffDebit"
																			style="WIDTH: 101px; HEIGHT: 19px" size="13">
																		</span>
																	</td>
																	<td></td>
																</tr>

																<tr>
																	<td colspan=4></td>
																</tr>
															</tbody>
														</table>
													</td>
												</tr>
											</TBODY>
										</table>
										<!-- Buttons End Here -->
										<tr>
										</tr>
										<table border="0" cellpadding="0" cellspacing="0" width="100%">
											<tr>
											</tr>
										</table>
										<table border="0" cellpadding="0" cellspacing="0"
											align="center">
											<tr id="submitGrid">
												<td align="right" id="saveandnew">
													<input type=button class=button
														onclick="return ButtonPress('savenew');" href="#"
														value="Save & New">
												</td>

												<td>
													<input type=button class=button
														onclick="return ButtonPress('saveview');" href="#"
														value="Save & View">
												</td>


												<td>
													<input type=button class=button
														onclick="return ButtonPress('saveclose');" href="#"
														value="Save & Close">
												</td>
												<td>
													<input class=button type=button onclick=onClickCancel()
														href="#" value="Cancel">
												</td>
												<td>
													<input type=button class=button onClick=window.close();
														href="#" value="Close">
												</td>
												<td>
													<input type=button class=button onclick="showglEntry();"
														href="#" value="Show GLEntry">
												</td>
												<td>
													<input type=button class=button onclick="showJVglEntry();"
														href="#" value="Show JV">
												</td>
											</tr>
										</table>
										<table align="center" border="0" cellpadding="0"
											cellspacing="0">
											<tr id="reverseSubmit">
												<td align="right">
													<input type=button class=button
														onclick="return ButtonPress('saveclose');" href="#"
														value="Save & Close" id="revSaveClose">
												</td>
												<td>
													<input type=button class=button onClick=window.close();
														href="#" value="Close" id="revClose">
												</td>
												<td>
											</tr>
										</table>
										<!-- </TR></table></TR></TD></TR></TD></TR></TABLE><!--</TD></TR></TBODY></TABLE>-->

										<table width="80%" align=center border=0 cellpadding="3"
											cellspacing="0" id="showEntries" name="showEntries"
											style="DISPLAY: none">
											<tr>
												<td colspan=4>
													<table width=100% align=center border="0" cellpadding="0"
														cellspacing="0">
														<tr class="tableStyle">
															<TD class=tableStyle align=center>
																<h4>
																	GLEntry
																</h4>
															</TD>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td>
													<table width="100%" border="1" cellpadding="0"
														cellspacing="0" align=center id="entries" name="entries">
														<tr>
															<td class="thStlyle">
																<div align="center" valign="center">
																	Code Type
																</div>
															</td>
															<td class="thStlyle">
																<div align="center" valign="center">
																	Account Code
																</div>
															</td>
															<td class="thStlyle">
																<div align="center" valign="center">
																	Account Head
																</div>
															</td>
															<td class="thStlyle">
																<div align="center" valign="center">
																	Debit
																</div>
															</td>
															<td class="thStlyle">
																<div align="center" valign="center">
																	Credit
																</div>
															</td>
															<td class="thStlyle">
																<div align="center" valign="center">
																	Narration
																</div>
															</td>
														</tr>
														<tr class="labelcell">
															<td class="labelcell">
																<div name="display_CodeType" id="display_CodeType"
																	readOnly maxLength=10 tabIndex=0>
																	&nbsp;
																</div>
															</td>
															<td class="labelcell">
																<div name="display_Code" id="display_Code" readOnly
																	maxLength=10 tabIndex=0>
																	&nbsp;
																</div>
															</td>
															<td class="labelcell">
																<div name="display_Head" id="display_Head" readOnly
																	maxLength=10 tabIndex=0>
																	&nbsp;
																</div>
															</td>
															<td class="labelcell">
																<div name="display_Debit" id="display_Ddebit" readOnly
																	maxLength=16 tabIndex=0 style="text-align: right">
																	&nbsp;
																</div>
															</td>
															<td class="labelcell">
																<div name="display_Credit" id="display_Credit" readOnly
																	maxLength=16 tabIndex=0 style="text-align: right">
																	&nbsp;
																</div>
															</td>
															<td class="labelcell">
																<div name="display_Narration" id="display_Narration"
																	readOnly maxLength=10 tabIndex=0>
																	&nbsp;
																</div>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>


										<!---------------- Footer begins here ---------->
										<!---------------- Footer ends here ---------->

										</td>
										</tr>
									</table>

									</form>
	</body>
</html>
