<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<script language="javascript">
pageMode="modify";
var sameWindow=0;
var alreadyDone=new Object();
var passedAmount;

var mainGridObj=new Array();

var entityDetailArray;           
var codeObj;
var yuiflag = new Array();
var oAutoComp;

var entityDetailArray2;           
var codeObj2;
var yuiflag2 = new Array();
var oAutoComp2;

function getData()
	{
	document.getElementById('consup').focus();	
var supconType=PageManager.DataService.getQueryField('type');
//alert("supconType"+supconType);
	
	//PageManager.DataService.setQueryField("SupConTypeCode",2);
	//document.getElementById("worksDetail_relationId").setAttribute('exilListSource',"getSupConName");
	//document.getElementById('assetList').setAttribute('exilListSource',"assetList");
	
		//document.getElementById("department_id").setAttribute('exilListSource',"jvsal_department");
	PageManager.ListService.callListService();
	var typeWO=document.getElementById('consup').value;
	//alert("typeWO-->"+typeWO);
	
	if(typeWO=='contractor'){
	
		document.getElementById('trWorkTypeSubType').style.display="block";
		document.getElementById('trShowDeductionsCreate').style.display="block";
		document.getElementById('trShowDeductionsModify').style.display="block";
	
		getAllContractorName(document.getElementById('consup'));
	}
	else{
		document.getElementById('trWorkTypeSubType').style.display="none";
		document.getElementById('trShowDeductionsCreate').style.display="none";
		document.getElementById('trShowDeductionsModify').style.display="none";
	
		getAllSupplierName(document.getElementById('consup'));
	}
	
	PageManager.ListService.callListService();
	
	PageValidator.addCalendars();
	loadSelectData('../../commonyui/egov/loadComboAjax.jsp', "EGW_TYPEOFWORK","id", "code", " parentid is null ", 'dummy', 'worksDetail_workCategory');
   // added by iliyaraja 
 var mode = PageManager.DataService.getQueryField('showMode');

//alert("mode in :"+mode);
  if (mode == "modify")
	{
	   var fundcode=PageManager.DataService.getQueryField('fund');
	getschemelistbyid(fundcode);  
	var schemeCode=PageManager.DataService.getQueryField('schCode');
	getsubschemelistbyid(schemeCode);  
	//PageManager.DataService.setQueryField("SupConTypeCode",supconType);
	//document.getElementById("worksDetail_relationId").setAttribute('exilListSource',"getSupConName");
	//document.getElementById("worksDetail_relationId").setAttribute('exilListSource',"conSupList");
  	//PageManager.DataService.callDataService('getSupConName');
	var worksDetail_code=PageManager.DataService.getQueryField('worksDetail_code'); 
    PageManager.DataService.setQueryField('worksDetail_code',worksDetail_code);
	
	document.getElementById("selected_Asset").setAttribute('exilListSource',"selected_Asset");
	PageManager.DataService.callDataService('cgNumber');
	document.getElementById("Asset").style.display="none";
	document.getElementById('worksDetail_codeId').focus();
	document.getElementById('trModify').style.display='block';
	document.getElementById('trNew').style.display='none';
	document.getElementById('modeOfExec').value="edit";
	//document.getElementById('worksDetail_fundId').disabled=true;
	//document.getElementById('worksDetail_fundSourceId').disabled=true;
	//document.getElementById('scheme').disabled=true;
	//document.getElementById('subscheme').disabled=true;
	
    var type=PageManager.DataService.getQueryField('type');
       
    if(type==1)
     document.WorksDetailAdd.consup[1].checked=true;
        
	PageManager.DataService.callDataService('worksDetailModData');

	}
	
	
	else{
	pageMode="New";
	hide("block");
	getSelected(document.getElementById("consup"));
	//document.getElementById('submitButton').innerHTML="New";
	
	
		
	
	}
}

function getschemelistbyid(obj1)
{

var obj=document.getElementById('scheme');
if(obj.selectedIndex != -1)
	var scheme=obj.options[obj.selectedIndex].value;

clearCombo('scheme');
clearCombo('subscheme');
obj.removeAttribute('exilListSource');
obj.setAttribute('exilListSource','schemelist');
PageManager.DataService.setQueryField("fundId",obj1);
PageManager.DataService.callDataService("schemelist");
}
function getsubschemelist(obj)
{
	
if (obj.value!="")
{
	var opt=obj.value;
var obj=document.getElementById('subscheme');
if(obj.selectedIndex != -1)
	var subscheme=obj.options[obj.selectedIndex].value;

obj.removeAttribute('exilListSource');
obj.setAttribute('exilListSource','subschemelist');
PageManager.DataService.setQueryField("schemeId",opt);
PageManager.DataService.callDataService("subschemelist");
}
else
	{
	clearCombo('subscheme');
	}
}

function getsubschemelistbyid(obj1)
{
	//alert("obk1 value "+obj1);
if (obj1!="")
{

var obj=document.getElementById('subscheme');
obj.removeAttribute('exilListSource');
obj.setAttribute('exilListSource','subschemelist');
PageManager.DataService.setQueryField("schemeId",obj1);
PageManager.DataService.callDataService("subschemelist");
}
else
	{
	clearCombo('subscheme');
	}
}

function getschemelist(obj)
{
var opt=obj.value;
if(opt!="")
{
var obj=document.getElementById('scheme');
clearCombo('scheme');
clearCombo('subscheme');
obj.removeAttribute('exilListSource');
obj.setAttribute('exilListSource','schemelist');
PageManager.DataService.setQueryField("fundId",opt);
PageManager.DataService.callDataService("schemelist");
}
else
clearCombo("scheme");
}

function clearCombo(cId){
	var bCtrl=document.getElementById(cId);
	for(var i=bCtrl.options.length-1;i>=0;i--)
	{
		bCtrl.remove(i);
	}
}
function clearcancel(cId){
	var bCtrl=document.getElementById(cId);
	for(var i=bCtrl.options.length-1;i>=1;i--)
	{
		bCtrl.remove(i);
	}
}

function hide(val)
{
   document.getElementById('worksDetail').style.display=val;
	//document.getElementById('screenName').innerHTML="Create Procurement Order";
	document.getElementById('trModify').style.display="none";
	//document.getElementById('worksDetail_levelOfWork').style.display=val;
	//document.getElementById('worksDetail_relationId').style.display=val;
	document.getElementById('worksDetail_fundId').style.display=val;
	document.getElementById('worksDetail_fundSourceId').style.display=val;
	document.getElementById('trNew').style.display='block';
	clearcancel('worksDetail_isActive');
}
function afterRefreshPage(dc){
	
	//alert("fund dc "+dc.values['fundId']);
			if(dc.values['databaseDate']!='' && dc.values['serviceID']=='cgNumber')
			{
			var dt=dc.values['databaseDate']
			document.getElementById('worksDetail_orderDate1').value=dt;
			}
			//alert(dc.values['serviceID']);
			//alert(":"+dc.values['worksDetail_orderDate']+":"+dc.values['serviceID']);
			if(dc.values['serviceID']=="worksDetailModData")
			{
		var worksDetail_orderDate=dc.values['worksDetail_orderDate'];
		
		worksDetail_orderDate=formatDate2(worksDetail_orderDate);
		document.getElementById('worksDetail_orderDate1').value=worksDetail_orderDate;
		
			var orderTo_NameVal;
			if(dc.values['po_Type']=='2' && dc.values['worksDetail_code']!="")
			{
				document.getElementById('trWorkTypeSubType').style.display="block";
				document.getElementById('trShowDeductionsCreate').style.display="block";
				document.getElementById('trShowDeductionsModify').style.display="block";
				
				document.getElementById('hideOrderedToForCont').style.display="block";
				document.getElementById('hideOrderedToForSup').style.display="none";
			
				orderTo_NameVal=dc.values['orderToName'];
				document.getElementById('accEntityList').value=orderTo_NameVal;
			}
			else{
				
				document.getElementById('trWorkTypeSubType').style.display="none";
				document.getElementById('trShowDeductionsCreate').style.display="none";
				document.getElementById('trShowDeductionsModify').style.display="none";
				
				document.getElementById('hideOrderedToForCont').style.display="none";
				document.getElementById('hideOrderedToForSup').style.display="block";
				orderTo_NameVal=dc.values['orderToName'];
				document.getElementById('accEntityList2').value=orderTo_NameVal;
			}
		
		}


	var type=document.getElementById('relationId');
	control=document.getElementById('worksDetail_type');

	if(dc.values['serviceID']=='worksDetailModData'){
		var worksDetail_sanctionDate=dc.values['worksDetail_sanctionDate'];
		worksDetail_sanctionDate=formatDate2(worksDetail_sanctionDate);
		document.getElementById('worksDetail_sanctionDate1').value=worksDetail_sanctionDate;
		document.getElementById('totalVal').value=dc.values['worksDetail_totalValue']
		//var type=dc.values['worksDetail_type']='CapitalWorks';
		getAssetList(document.getElementById('worksDetail_type'));
		//getSubCategoryType('worksDetail_workCategory','worksDetail_subCategory');
		document.getElementById("worksDetail_subCategory").value=dc.values['worksDetail_subCategory'];
	
	var worksDetail_levelOfWor=dc.values['worksDetail_levelOfWork'];
	
	for(var i=0;i<document.getElementById('worksDetail_levelOfWork').options.length;i++)
		{
	if(worksDetail_levelOfWor==document.getElementById('worksDetail_levelOfWork').options[i].value)
	level=document.getElementById('worksDetail_levelOfWork').options[i].text;
		}

	
	if(level == 'ward' || level == 'Ward'){
	document.getElementById('wardName').innerHTML="Ward No"+"<span class='leadon'>"+"*"+"</span>";
	var ward=document.getElementById('worksDetail_wardId');
	ward.style.display="block";
	
	}
	else
		{
	document.getElementById('wardName').innerHTML="";
	var ward=document.getElementById('worksDetail_wardId');
	ward.style.display="none";
	document.getElementById('worksDetail_wardId').removeAttribute('exilMustEnter');
	document.getElementById('worksDetail_wardId').value=null;
	dc.values['worksDetail_wardId']=null;
		}

	getRelationType();
	}
	/*
	if(dc.values['serviceID']=='getRelationId')
	{
	  PageManager.DataService.setQueryField("SupConTypeCode",dc.values['relationId']);
  	    PageManager.DataService.callDataService('getSupConName');
	}
	*/
	
	if(dc.values['worksDetail_isActive']=='0')
		document.getElementById('worksDetail_isActive').checked=false;
	else if(dc.values['worksDetail_isActive']=='1')
		document.getElementById('worksDetail_isActive').checked=true;

		if(dc.values['worksDetail_type']){
		document.getElementById('typeName').innerHTML="Type Of Work<span class='leadon'>*</span>";
		control.style.display="block";
		}
		//We need to disable some fields if there is a bill already paid for the Procurement order
		if(dc.grids['worksDetailbill_list'] && dc.grids['worksDetailbill_list'].length > 1 ){
		
		alert("Already Billed");

			document.getElementById('worksDetail_advanceAmount').disabled=true;
			//document.getElementById('worksDetail_advancePayable').disabled=true;
			document.getElementById('worksDetail_securityDeposit').disabled=true;
			document.getElementById('worksDetail_retention').disabled=true;

			//document.getElementById('worksDetail_relationId').disabled=true;
			document.getElementById('worksDetail_wardId').disabled=true;
			document.getElementById('worksDetail_fundId').disabled=true;
			document.getElementById('worksDetail_fundSourceId').disabled=true;
			document.getElementById('scheme').disabled=true;
			document.getElementById('subscheme').disabled=true;
			document.getElementById('consupDiv').disabled=true;
			document.getElementById('accEntityList').disabled=true;
			document.getElementById('worksDetail_workCategory').disabled=true;
			document.getElementById('worksDetail_wardId').disabled=true;
			document.getElementById('worksDetail_subCategory').disabled=true;
			document.getElementById('worksDetail_type').disabled=true;
			document.getElementById('isFixedAsset').disabled=true;
			document.getElementById('worksDetail_levelOfWork').disabled=true;
			document.getElementById('worksDetail_codeId').disabled=true;
			clearcancel('worksDetail_isActive');
		}

	
	/*
	if(dc.values['serviceID']=='getSupConName')
	{
		if(dc.values['SupConTypeCode']=='1')
		{
		document.getElementById('trWorkTypeSubType').style.display="none";
		document.getElementById('trShowDeductionsCreate').style.display="none";
		document.getElementById('trShowDeductionsModify').style.display="none";
		}
		else
		{
		document.getElementById('trWorkTypeSubType').style.display="block";
		document.getElementById('trShowDeductionsCreate').style.display="block";
		document.getElementById('trShowDeductionsModify').style.display="block";
		}
			
			
	}
	*/
	
}//after ref page

function onClickCancel(){
	//window.location="WorksDetailEnq_VMC.jsp";
	window.location="WorksDetailAdd_VMC.jsp";
}

function checkPassedAmt()
{
	if(pageMode == "modify")
	{
		PageManager.DataService.setQueryField('workDetailId',document.getElementById('worksDetail_ID').value);
		PageManager.DataService.callDataService('checkPassedAmount');

	}
}

function ButtonPress(name){

var typeWO=document.getElementById('consup');
//alert(typeWO.checked);

selectedAssetList('selected_Asset');
if(name.toLowerCase()=='savenew')
   sameWindow=1;
	//   ***1 only FO***,  ***2 FO***,  ***Admin, 3 All***    //
	if(!isValidUser(2, CookieManager.getCookie('userRole')) ) return false;

	document.getElementById('egUser_id').value = CookieManager.getCookie('currentUserId');
	var pAmount=parseFloat(document.getElementById('worksDetail_advancePayable').value);
	var totVal=parseFloat(document.getElementById('worksDetail_totalValue').value);
	var wodate=document.getElementById('worksDetail_orderDate1').value;
	document.getElementById('worksDetail_orderDate').value=formatDate5(wodate);
	var sanctiondate=document.getElementById('worksDetail_sanctionDate1').value;
	document.getElementById('worksDetail_sanctionDate').value=formatDate5(sanctiondate);

	if(parseFloat(totVal)<=0){
		alert('Total Value Should Be Greater Than Zero');
		return false;
	}
	if(parseFloat(pAmount) > parseFloat(totVal)){
			alert('Max Advance Amount Should Be Less Than Or Equal to Total Value');
			return false;
	}
		 		var dbDate=document.getElementById('databaseDate').value;
				var woDate=document.getElementById('worksDetail_orderDate1').value;
				if(compareDate(formatDateToDDMMYYYY1(woDate),formatDateToDDMMYYYY1(dbDate)) == -1 )
				{
				alert('Work Order Date should be less than or equal to '+dbDate);
				document.getElementById('worksDetail_orderDate1').focus();
				return false;
				}
	if(pageMode == "modify")
	{
		var totalVal=document.getElementById('totalVal').value;
		var totalValue =document.getElementById('worksDetail_totalValue').value;
		if(totalVal!=totalValue)
			document.getElementById('worksDetail_remarks').setAttribute('exilMustEnter','true');
		var aAmount=parseFloat(document.getElementById('worksDetail_advanceAmount').value);
		/*if(parseFloat(aAmount) > parseFloat(pAmount))
		{
			alert('Advance Amount Already Issued  is more than Given Advance Payable Amount');
			return false;
		}*/
		if(totVal<parseFloat(passedAmount))
		{
			alert('Total value is less than Bills Passed Amount');
			return false;
		}

		document.getElementById('worksDetail_advanceAmount').disabled=false;
		document.getElementById('worksDetail_advancePayable').disabled=false;
		document.getElementById('worksDetail_securityDeposit').disabled=false;
		document.getElementById('worksDetail_retention').disabled=false;

		//document.getElementById('worksDetail_relationId').disabled=false;
		document.getElementById('worksDetail_wardId').disabled=false;
		document.getElementById('worksDetail_fundId').disabled=false;
		document.getElementById('worksDetail_fundSourceId').disabled=false;
		
		if(typeWO.value=='contractor' && typeWO.checked){
			//alert("For contractor");
			if(!(document.getElementById('accEntityList').value!="" && document.getElementById('worksDetail_relationId').value!=""))
			{
			alert("Enter Valid Ordered To !!! ");
			document.getElementById('accEntityList').focus();
			return;
			}
		}
		else{
			//alert("For supplier");
			if(!(document.getElementById('accEntityList2').value!="" && document.getElementById('worksDetail_relationId').value!=""))
			{
			alert("Enter Valid Ordered To !!! ");
			document.getElementById('accEntityList2').focus();
			return;
			}
		}
		//filterSelectedTds();
		//PageManager.UpdateService.submitForm('worksDetailUpdate');
		PageManager.UpdateService.submitForm('ProcOrderTransaction');

	}
	else
	{
		var orderDate=document.getElementById('worksDetail_orderDate1').value;
		
		if(typeWO.value=='contractor' && typeWO.checked){
			//alert("For contractor");
			if(!(document.getElementById('accEntityList').value!="" && document.getElementById('worksDetail_relationId').value!=""))
			{
			alert("Enter Valid Ordered To !!! ");
			document.getElementById('accEntityList').focus();
			return;
			}
		}
		else{
			//alert("For supplier");
			if(!(document.getElementById('accEntityList2').value!="" && document.getElementById('worksDetail_relationId').value!=""))
			{
			alert("Enter Valid Ordered To !!! ");
			document.getElementById('accEntityList2').focus();
			return;
			}
		}
		
		//filterSelectedTds();
		
		//		PageManager.UpdateService.submitForm('worksDetailInsert');
		PageManager.UpdateService.submitForm('ProcOrderTransaction');
	}
}
function beforeRefreshPage(dc){

	if(dc.values['serviceID']=='checkPassedAmount')
	{
		if(dc.values['passedAmount'])
		{
			passedAmount=dc.values['passedAmount'];
		}

	}
	var ward=dc.values['worksDetail_wardId'];
	if(ward){
	document.getElementById('wardName').innerHTML="Ward No";
	var ward=document.getElementById('worksDetail_wardId');
	ward.style.display="block";
	}


		var type=dc.values['worksDetail_type'];
		if(type== 'Supplies'){
		var type=document.getElementById('worksDetail_type');
		document.getElementById('typeName').innerHTML="";
		type.style.display="none";
		}
		return true;
}



function afterUpdateService(dc)
	{
		//alert("hai");
		//alert("value "+document.getElementById('worksDetail_fundId'));

	str = "DC came back with success=" + dc.success + " and other fields are\n";
	if(dc.values['modeOfExec']=='edit')
		var  worksDetail_id=dc.values['worksDetail_ID'];
	else	
		var  worksDetail_id=dc.values['wDetail_createId'];
		
	if(sameWindow == 1){
	  window.location="WorksDetailAdd_VMC.jsp";
      }else{
	//window.location="WorksDetailEnq.htm";
	
	
	if(dc.values['consup']=='contractor')
	{
	//alert("Work Order For Contractor");
	var woType=2;
	window.location="WorksDetailEnq_VMC.jsp?worksDetail_code="+worksDetail_id+"&po_Type="+woType+"&showMode=view";
	
	}
	else
	{
	var poType=1;
	window.location="WorksDetailEnq_VMC.jsp?worksDetail_code="+worksDetail_id+"&po_Type="+poType+"&showMode=view";
	}
	
			}
	sameWindow=0;
	return true;
}


function selectLevel(){
	var worksDetail_levelOfWork=document.getElementById('worksDetail_levelOfWork');
	var level = worksDetail_levelOfWork.options[worksDetail_levelOfWork.selectedIndex].text;
	if(level == 'ward' || level == 'Ward'){
	document.getElementById('wardName').innerHTML="Ward No"+"<span class='leadon'>"+"*"+"</span>";
	var ward=document.getElementById('worksDetail_wardId');
	ward.style.display="block";
	}else{
	
		var ward=document.getElementById('worksDetail_wardId');
		ward.style.display="none";
	document.getElementById('worksDetail_wardId').removeAttribute('exilMustEnter');
		document.getElementById('wardName').innerHTML="";
		if(!ward.selectedIndex == -1){
			ward.value=null;
		}else{
			ward.value = null;
			//ward.options[0].value=null;
		}
	}
}
  function getRelationType(){
  		
 	var control=document.getElementById('worksDetail_relationId');
  	if(control.value!="")
  	{
  	var relType=control.value;
	//alert(relType);
	
	PageManager.DataService.setQueryField("relationId",relType);
  	PageManager.DataService.callDataService('getRelationId');
  	
	}
	
}

function openSearch3(obj)
	{
			var a = new Array(2);
			var sRtn;
			var str = "tdsdec";
			sRtn= showModalDialog("Search.html?screenName="+str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
			if(sRtn != '')
			{
				a = sRtn.split("`~`");
				var x = PageManager.DataService.getControlInBranch(obj.parentNode,'workstdscode');
				x.value = a[0];
				//var y = PageManager.DataService.getControlInBranch(obj.parentNode.parentNode,'coa_name');
				//y.value = a[1];
				var z = PageManager.DataService.getControlInBranch(obj.parentNode.parentNode,'worksDetail_workstds');
				z.value = a[2];
				//alert("glcode++++"+z.value);
				x.focus()


			}
	}
	
	function getSelected(obj)
	{

	   hide("block");
	    control=document.getElementById('worksDetail_type');
	   if(obj.value =='supplier'){
	      	document.getElementById('hideTDS').style.display="none";
	  	document.getElementById('hideOrderedToForSup').style.display="block";
	  	document.getElementById('hideOrderedToForCont').style.display="none";
	  	
	  	document.getElementById('trWorkTypeSubType').style.display="none";
		document.getElementById('trShowDeductionsCreate').style.display="none";
		document.getElementById('trShowDeductionsModify').style.display="none";
	  	
	   	document.getElementById('typeName').innerHTML="";
		control.style.display="none";
		document.getElementById("Asset").style.display="none";
		document.getElementById('tdsText').style.display="none";
		document.getElementById('tdsCaption').style.display="none";
		control.options[0].setAttribute('selected',true);
		control.removeAttribute('exilMustEnter');

		//PageManager.DataService.setQueryField("SupConTypeCode",1);
		// document.getElementById("worksDetail_relationId").setAttribute('exilListSource',"conSupList");
  	   // PageManager.DataService.callDataService('getSupConName');
  	    //PageManager.DataService.setQueryField("assetId",1);
  	   // PageManager.DataService.setQueryField("assetId1",3);
	   //PageManager.DataService.callDataService("assetList");
		}
		else if(obj.value =='contractor'){
		document.getElementById('hideOrderedToForCont').style.display="block";
		document.getElementById('hideOrderedToForSup').style.display="none";
		
		document.getElementById('trWorkTypeSubType').style.display="block";
		document.getElementById('trShowDeductionsCreate').style.display="block";
		document.getElementById('trShowDeductionsModify').style.display="block";
			
		document.getElementById('typeName').innerHTML="Type Of Work<span class='leadon'>*</span>";
		document.getElementById("Asset").style.display="NONE";
		control.style.display="block";
		control.setAttribute('exilMustEnter',"true");
		document.getElementById('tdsText').style.display="none";
		document.getElementById('tdsCaption').style.display="none";

		//PageManager.DataService.setQueryField("SupConTypeCode",2);
  	    //PageManager.DataService.callDataService('getSupConName');
	}

	}
	/*
	  * param=1 if type of work="capitalworks" ===>select list of asset with status=1 or 3
	  * param=3 if type of work="Repairworks/Other Service" ===>select list of asset with status=3
	*/
	function getAssetList(obj)
	{
	   var opt=obj.value;
	   var param="";
	  // alert();
	   if(opt.toLowerCase()=="1")
	      param=1;
	   else
	      //param="3,4";
	      param="select distinct id from egw_status where upper(moduletype)='ASSET' and (upper(description)=upper('Capitalized') or (upper(description)=upper('Revaluated')))";

	      document.getElementById("Asset").style.display="none";
	      //document.getElementById("assetList").setAttribute("exilListSource",'assetList');
	   PageManager.DataService.setQueryField("assetId",param);
	   PageManager.DataService.callDataService("assetList");

	}
	function selectedAssetList(objName)
	{
	  var fromObj=document.getElementById(objName);
		for(var i=0;i<fromObj.length;i++)
		{
			fromObj.options[i].setAttribute('selected',true);
		}
	}
    function transferRight(){
		var fromObj=document.getElementById('assetList');
		var toObj=document.getElementById('selected_Asset');
		for(var i=0;i<fromObj.length;i++){
			try{
				if(fromObj.options[i].selected){
					if(alreadyDone[fromObj.options[i].value]!=null)continue;
					alreadyDone[fromObj.options[i].value]=fromObj.options[i].value;
					var optObj=fromObj.options[i].cloneNode(true);
					toObj.appendChild(optObj);
				}
			}catch(e){}
		}
	}
	function removeRight()
	{
		var toObj=document.getElementById('selected_Asset');
		for(var i=toObj.length-1;i>=0;i--)
		{
			if(toObj.options[i].selected)
			{
				alreadyDone[toObj.options[i].value]=null;
				toObj.remove(i);
			}
		}
	}
	function clearList()
	{
	  var toObj=document.getElementById('selected_Asset');
		for(var i=toObj.length-1;i>=0;i--)
		{
			alreadyDone[toObj.options[i].value]=null;
			toObj.remove(i);

		}
	}

function checkfund()
{
var tempfund=document.getElementById("worksDetail_fundId").value;
//alert(tempfund);
if(tempfund=="")
alert("Select Fund First");

}
function checkscheme()
{
var tempfund=document.getElementById("scheme").value;
//alert(tempfund);
if(tempfund=="")
alert("Select Scheme First");
}


/*  To get the date and populate it in the field  */
	function fillDate1(objName)
	{
		PageValidator.showCalendar('selectedDate');
		document.getElementById(objName).value=document.getElementById('selectedDate').value;
		document.getElementById('selectedDate').value = "";
	}
//loads sub category type 
function getSubCategoryType(objName1,objName2)
{
	loadSelectData('../../commonyui/egov/loadComboAjax.jsp', "EGW_TYPEOFWORK","id", "code", " parentid=#1 ", objName1, objName2);	
}

function showDeductions(){

var typeWO=document.getElementById('consup').value;
	//alert("typeWO-->"+typeWO);
	
	if(typeWO=='contractor')
	{
		//alert("Inside new");
		document.getElementById('hideTDS').style.display="block";
		clearDeductionList();
		
		PageManager.DataService.removeQueryField('workType_Id',workType_Id);
		PageManager.DataService.removeQueryField('subType_Id',subType_Id);
		
		var workType_Id=document.getElementById("worksDetail_workCategory").value;
		var subType_Id=document.getElementById("worksDetail_subCategory").value;
		
		//alert(workType_Id);
		//alert(subType_Id);
		
		if(workType_Id && subType_Id)
		{
			//alert("Inside work type and sub type");
			PageManager.DataService.setQueryField('workType_Id',workType_Id);
			PageManager.DataService.setQueryField('subType_Id',subType_Id);
		
			PageManager.DataService.callDataService("tds_ListForAutomatic");
			document.getElementById('tds_List').setAttribute('exilListSource',"tds_ListForAutomatic");
		
		}
		else if(workType_Id)
		{
			//alert("Inside Only work type");
			PageManager.DataService.setQueryField('workType_Id',workType_Id);

			PageManager.DataService.callDataService("tds_ListForAutomatic");
			document.getElementById('tds_List').setAttribute('exilListSource',"tds_ListForAutomatic");
		}
		else if(subType_Id)
		{
			//alert("Inside Only sub type");
			PageManager.DataService.setQueryField('subType_Id',subType_Id);

			PageManager.DataService.callDataService("tds_ListForAutomatic");
			document.getElementById('tds_List').setAttribute('exilListSource',"tds_ListForAutomatic");
		}
		else
		{
			//alert("Inside tds_ListForAll");
			PageManager.DataService.callDataService("tds_ListForAll");
			document.getElementById('tds_List').setAttribute('exilListSource',"tds_ListForAll");
		}
		
	}
	else
	document.getElementById('hideTDS').style.display="none";
	

}
function clearDeductionList()
{
	  var toObj=document.getElementById('tds_List');
		for(var i=toObj.length-1;i>=0;i--)
		{
			alreadyDone[toObj.options[i].value]=null;
			toObj.remove(i);

		}
}
function getAllContractorName(obj)
{
	
	//alert("Inside getAllContractorName");
	//alert(obj.value);
	var str1=obj.value;
	var str2='contractor';
	
	if (str1.toLowerCase() == str2.toLowerCase())
	{
             
       var type='getAllActivePartyName';
       var relationTypeId=2;
       
       var url = "../../commons/Process.jsp?type=" +type+ "&relationTypeId="+relationTypeId;
       var req2 = initiateRequest();
       req2.onreadystatechange = function()
       {
             
              
              if (req2.readyState == 4)
              {
                  if (req2.status == 200)
                  {
                  	var codes2=req2.responseText;
                  	var a = codes2.split("^");
			var codes = a[0]; 
			entityDetailArray=codes.split("+");
			//alert("entityDetailArray"+entityDetailArray);
			codeObj = new YAHOO.widget.DS_JSArray(entityDetailArray);
					
                  }
              }
        };
        req2.open("GET", url, true);
        req2.send(null);
        }
}
function getAllSupplierName(obj)
{
	
	//alert(obj.value);
	var str1=obj.value;
	var str2='supplier';
	
	if (str1.toLowerCase() == str2.toLowerCase())
	{
            
       var type='getAllActivePartyName';
       var relationTypeId=1;
       
       var url = "../../commons/Process.jsp?type=" +type+ "&relationTypeId="+relationTypeId;
       var req2 = initiateRequest();
       req2.onreadystatechange = function()
       {
              if (req2.readyState == 4)
              {
                  if (req2.status == 200)
                  {
                  	var codes2=req2.responseText;
                  	var a = codes2.split("^");
			var codes = a[0]; 
			entityDetailArray2=codes.split("+");
			//alert(entityDetailArray2);
			codeObj2 = new YAHOO.widget.DS_JSArray(entityDetailArray2);
					
                  }
              }
        };
        req2.open("GET", url, true);
        req2.send(null);
        }
}
function autocompletecodeForContractor(obj)
{
	// set position of dropdown
	//var oAutoComp;
	var src = obj;
	var target = document.getElementById('codescontainer');
	var posSrc=findPos(src);
	target.style.left=posSrc[0];
	target.style.top=posSrc[1]+25;
	target.style.width=500;
	
	if(obj.name=='accEntityList') target.style.left=posSrc[0]-10;	
	var currRow=PageManager.DataService.getRow(obj);
	var coaCodeObj = obj;
	
	//alert("rowIndex"+yuiflag[currRow.rowIndex]);
	if(yuiflag[currRow.rowIndex] == undefined)
 	{
		
		if(event.keyCode != 40 )
		{
			if(event.keyCode != 38 )
			{
				oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.useIFrame = true;
			}
		}
		yuiflag[currRow.rowIndex]=1;
		
  	}
  	
  	//oAutoComp=null;
			
		//alert("oAutoComp---->"+oAutoComp);
}
function autocompletecodeForSupplier(obj)
{
	// set position of dropdown
	//var oAutoComp;
	var src = obj;
	var target = document.getElementById('codescontainer');
	var posSrc=findPos(src);
	target.style.left=posSrc[0];
	target.style.top=posSrc[1]+25;
	target.style.width=500;
	
	if(obj.name=='accEntityList2') target.style.left=posSrc[0]-10;	
	var currRow=PageManager.DataService.getRow(obj);
	var coaCodeObj2 = obj;
	
	//alert("rowIndex"+yuiflag[currRow.rowIndex]);
	if(yuiflag[currRow.rowIndex] == undefined)
 	{
		
		if(event.keyCode != 40 )
		{
			if(event.keyCode != 38 )
			{
				oAutoComp2 = new YAHOO.widget.AutoComplete(coaCodeObj2,'codescontainer', codeObj2);
				oAutoComp2.queryDelay = 0;
				oAutoComp2.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp2.useShadow = true;
				oAutoComp2.useIFrame = true;
			}
		}
		yuiflag2[currRow.rowIndex]=1;
		
  	}
}
function fillNeibrAfterSplit(obj)
 {  
   	
   	var temp = obj.value; 
	temp = temp.split("`-`");
	
	var currRow=getRow(obj);
	yuiflag[currRow.rowIndex] = undefined;
	
	//alert("temp[0]"+temp[0]);
	//alert("temp[1]"+temp[1]);
	
	if(temp[1]==null)
	{
	document.getElementById("worksDetail_relationId").value="";
	return ;
	}
	else 
	{
	obj.value=temp[0];
	document.getElementById("worksDetail_relationId").value=temp[1];
	}
}
function clearAllOldValue()
{
	document.getElementById('worksDetail_relationId').value='';
	document.getElementById('accEntityList').value='';
	document.getElementById('accEntityList2').value='';
}

</script>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="getData();" onKeyDown ="CloseWindow(window.self);">
<center>
<br>
<form name="WorksDetailAdd">
<input type="hidden" name="egUser_id" id="egUser_id">
<input type="hidden" name="worksDetail_ID" id="worksDetail_ID" >
<input type="hidden" name="worksDetail_lastModified" id="worksDetail_lastModified" >
<input type="hidden" name="tableName" id="tableName" value="worksDetail">
<input  type="hidden" name="relationId" id="relationId" >
<input  type="hidden" name="worksDetail_advanceAmount" id="worksDetail_advanceAmount" >
<input  type="hidden" name="worksDetail_orderDate" id="worksDetail_orderDate" >
<input  type="hidden" name="worksDetail_sanctionDate" id="worksDetail_sanctionDate" >
<input  type="hidden" name="databaseDate" id="databaseDate" >
<input  type="hidden" name="totalVal" id="totalVal" >
<input type=hidden id="modeOfExec" name="modeOfExec" value="new">
<input type="hidden" name="selectedDate" id="selectedDate">
<input type="hidden" id="dummy" name="dummy" value="dummy">

<input type="hidden" name="worksDetail_relationId" id="worksDetail_relationId">
<input type="hidden" name="orderToName" id="orderToName">

<a name="28"></a>

<!--table width="100%" height="74%" border="0" cellpadding="0" cellspacing="0">
	  </table-->
	 
	<table width="100%" border=0 cellpadding="6" cellspacing="0">
		 <table align='center' class="tableStyle" id="table3">		
			<tr>
			<td valign="top"><!------------Content begins here ------------------>
		<!--table id="DispList" name="DispList" >

		</table-->
					<table width="100%" border=0 cellpadding="3" cellspacing="0" id="worksDetail" name="worksDetail" >
        					<TBODY>
							
							<tr>
								  <td width="184"><div align="right" class="labelcell">Type<span class="leadon">*</span></div></td>
								  <td width="279" colspan="3"><div align="left" class="labelcellforsingletd" id="consupDiv"> Contractor&nbsp;&nbsp;<input type=radio name="consup" id="consup" value="contractor" checked onClick="getSelected(this);getAllContractorName(this);clearAllOldValue();">
								  Supplier&nbsp;&nbsp;<input type=radio name="consup" id="consup" value="supplier" onClick="getSelected(this);getAllSupplierName(this);clearAllOldValue();"></div>
								   </td>
							</tr>
							<tr>
							  <td width="184"><div align="right" class="labelcell"><bean:message key="WorksDetailAdd.OrderNo"/><br>Order No<span class="leadon">*</span></div></td>
							   <td width="279" class="fieldcell">
								<input name="worksDetail_codeId" class="fieldinput" id="worksDetail_codeId" size="10" exilMustEnter="true" exilDataType="exilAlphaNumeric" >
							  </td>
							    <td width="139" class="labelcell"><div align="right" ><bean:message key="WorksDetailAdd.NameofWork"/><br>Name of Work<span class="leadon">*</span></div></td>
							   <td width="364" class="fieldcell">
								<input name="worksDetail_name" class="fieldinput" id="worksDetail_name" size="10" exilMustEnter="true" exilDataType="exilAnyChar" >
							  </td>
								</td>
							</tr>
							<tr >
							  <td width="184" class="labelcell"><div align="right" ><bean:message key="WorksDetailAdd.OrderDate"/><br>Order Date<span class="leadon">*</span></div></td>
							 <td width="279" class="smallfieldcell"><input name="worksDetail_orderDate1" class="datefieldinput" id="worksDetail_orderDate1" tabIndex="" maxlength="11" onkeyup="DateFormat(this,this.value,event,false,'3')" exilMustEnter="true"  exilDataType="exilAnyDate" > <A onclick="fillDate1('worksDetail_orderDate1')"  href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A></td>
							  <!-- <td width="279"><input name="worksDetail_orderDate1" id="worksDetail_orderDate1" class="datefieldinput"size="10" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilMustEnter="true" exilDataType="exilAnyDate" exilCalendar="true">-->
							 <td width="139" class="labelcell"><div align="right" ><bean:message key="WorksDetailAdd.Totalvalue"/><br>Total Value<span class="leadon">*</span></div></td>
							   <td width="364" class="fieldcell">
								<input name="worksDetail_totalValue" maxlength="16" class="fieldinput-right" id="worksDetail_totalValue" size="10" exilMustEnter="true" exilDataType="exilUnsignedDecimal" onblur='checkPassedAmt()'>
							  </td>
								</td>
							</tr>
							<tr>
							  <td width="184" class="labelcell"><div align="right"><bean:message key="WorksDetailAdd.AdvancePayableAmount"/><br>Advance  Payable  Amount</div></td>
							<td width="279" class="fieldcell"><input name="worksDetail_advancePayable" maxlength="16" class="fieldinput-right" id="worksDetail_advancePayable" size="10" exilDataType="exilUnsignedDecimal"> </td>
							<td width="139" class="labelcell"><div align="right" ><bean:message key="WorksDetailAdd.AuthorizedBy"/><br>Authorized By</div></td>
							<td width="364" class="fieldcell"><input name="worksDetail_authorizedBy" class="fieldinput" id="worksDetail_authorizedBy" size="250" exilDataType="exilAnyChar"> </td>
							</tr>

							<tr>
								<td width="184" class="labelcell"><div align="right" valign="center"><bean:message key="WorksDetailAdd.LevelofWork"/><br>Level Of Work<span class="leadon">*</span></div></td>
								<td width="279" class="smallfieldcell">
									<select class="fieldinput" name="worksDetail_levelOfWork" id="worksDetail_levelOfWork"  onChange="selectLevel();" exilMustEnter="true"  exilListSource="levelOfWork" >
									</select>
								</td>
								<td width="139" class="labelcell"><div align="right" valign="center"  id="wardName" ></div></td>
								<td width="364" class="smallfieldcell">
									<select class="fieldinput" name="worksDetail_wardId" id="worksDetail_wardId" style="DISPLAY:  none" exilMustEnter="true"  exilListSource="selectWards">
									</select>
								</td>
					</tr>
								<tr id="trWorkTypeSubType">
								<td width="184" class="labelcell"><div align="right" valign="center" >Work Type</div></td>
								<td width="279" class="smallfieldcell">
									<select class="fieldinput" name="worksDetail_workCategory" id="worksDetail_workCategory"    onchange="getSubCategoryType('worksDetail_workCategory','worksDetail_subCategory')">
									</select>
								</td>
								<td width="139" class="labelcell"><div align="right" valign="center">Sub Type</div></td>
								<td width="364" class="smallfieldcell">
									<select class="fieldinput" name="worksDetail_subCategory" id="worksDetail_subCategory" >
									</select>
								</td>
								</tr>

				<tr id="hideOrderedToForCont">
				<td width="184" height="29" class="labelcell"><div align="right" valign="center"><bean:message key="WorksDetailAdd.OrderedTo"/><br>Ordered To<span class="leadon">*</span></div></td>
					
				<td class="smallfieldcell">
				<input class="fieldinputlarge" style="text-align:left;width: 250" name="accEntityList" id="accEntityList" autocomplete="off" onkeyup="autocompletecodeForContractor(this);" onblur="fillNeibrAfterSplit(this);">
				 </td>
				 
				 <td width="139" height="29" class="labelcell"><div align="right"  id="typeName" name="typeName"></div>
				 </td>
					<td width="364" height="29" class="smallfieldcell">
										<SELECT  id="worksDetail_type" name="worksDetail_type" style="DISPLAY: none" onchange="clearList();getAssetList(this);" exilListSource="worksTypeList"></SELECT>
					</td>

					</tr>
					<tr>
					<td>
					<div id="codescontainer"></div>
					</td>
					</tr>
					
					<tr id="hideOrderedToForSup" style="display:none">
					<td width="184" height="29" class="labelcell"><div align="right" valign="center"><bean:message key="WorksDetailAdd.OrderedTo"/><br>Ordered To<span class="leadon">*</span></div></td>
					<td class="smallfieldcell">
					<input class="fieldinputlarge" style="text-align:left;width: 250" name="accEntityList2" id="accEntityList2" autocomplete="off" onkeyup="autocompletecodeForSupplier(this);" onblur="fillNeibrAfterSplit(this);">
					 </td>
					 </tr>
					 
					 <tr id="Asset" style="Display:none">
						<td width="184" class="labelcell"><div align="right" valign="center">Asset:</div></td>
						<td width="279" class="fieldcell">
							<select name="assetList" id="assetList"    multiple size="5" style="width:250" ><!--exilListSource="assetList" --><option></option>
							</select>
						</td>
						<td width="139" ><input type=button value=">" id="selButton" name="buttonSet" onClick="transferRight()" style="WIDTH: 81px; HEIGHT: 24px">
			  						     <input type=button value="<" id="unSelButton" name="buttonSet" onClick="removeRight()" style="WIDTH: 81px; HEIGHT: 24px"></td>
						</td>
						<td width="364" class="fieldcell"><select name="selected_Asset" id="selected_Asset"   multiple size="5" style="width:250"><option></option>
							</select>
						</td>
					</tr>

			<tr><!--<td width="124" ><div align="right" valign="center" class="normaltext" >TDS<span class="leadon">*</span></div></td>
					<td width="274" >
						<select class="fieldinput" name="worksDetail_tdsId" id="worksDetail_tdsId" exilListSource="selectTds" exilDataType="exilUnsignedInt">
						</select>
					</td>
					<td width="259" ><div align="right" class="normaltext">Capital Work in Progress<span class="leadon">*</span></div></td>

					<td width="223" >
						<select class="fieldinput" name="worksDetail_glCodeId2" id="worksDetail_glcodeId2" onchange="getGlCodeId2()">
						</select>
					</td> -->
					<tr>
						<td width="184" class="labelcell"><div align="right" valign="center" >Department</div></td>
								<td width="279" class="smallfieldcell">
									<SELECT  id="department_id" name="department_id" class="fieldinput"  exilListSource="departmentNameList" >
           								</SELECT>
								</td>
					</tr>
					<td align="right" valign="center" class="labelcell">
            							<DIV align=right>Fund&nbsp;Name<SPAN class=leadon>*</SPAN><br><bean:message key="WorksDetailAdd.FundName"/></DIV>
									</td>
									<td class="smallfieldcell">
                    					<SELECT  id="worksDetail_fundId" name="worksDetail_fundId" class="fieldinput" exilMustEnter="true" exilListSource="fundNameList" onChange="getschemelist(this)">
           								</SELECT>
									</td>
					<td align="right" valign="center" class="labelcell">
            							<DIV align=right>Financing Source<br><bean:message key="WorksDetailAdd.FinancingSource"/></DIV>
									</td>
									<td class="smallfieldcell" >
                    					<SELECT  id="worksDetail_fundSourceId" name="worksDetail_fundSourceId" class="fieldinput" exilListSource="fundSourceNameList">
           								</SELECT>
									</td><!--	<td width="184" ><div align="right" class="normaltext"></div></td>
					<td width="279" ><div align="right" class="normaltext"></div></td> -->
					</tr>
					<tr>
									<td align="right" valign="center" class="labelcell">
								         <DIV align=right >Scheme&nbsp;</DIV>
									</td>
									<td class="smallfieldcell">
								        <SELECT id="scheme" name="scheme"  class="fieldinput" onclick="checkfund()"  onChange= "getsubschemelist(this)">
								        </SELECT>
									</td>
									<td align="right" valign="center" class="labelcell">
										 <DIV align=right>Sub Scheme&nbsp;</DIV>
									</td>
									<td class="smallfieldcell">
										<SELECT  id="subscheme" name="subscheme" onclick="checkscheme()" class="fieldinput">
										</SELECT>
									</td>

					</tr>

						<tr>
							  <td width="184" class="labelcell"><div align="right"> <bean:message key="WorksDetailAdd.Retention"/><br>Retention</div></td>
							<td width="279" class="fieldcell"><input name="worksDetail_retention" class="fieldinput" id="worksDetail_retention" size="250" exilDataType="exilUnsignedDecimal" > </td>
							 <td width="139" class="labelcell" ><div align="right"><bean:message key="WorksDetailAdd.SecurityDeposit"/><br>Security Deposit</div></td>
							<td width="364" class="fieldcell"><input name="worksDetail_securityDeposit" class="fieldinput" id="worksDetail_securityDeposit" size="250" exilDataType="exilAnyChar"> </td>
							</tr>
							<tr>

							<td width="184" class="labelcell"><div align="right" ><bean:message key="WorksDetailAdd.SancationNo"/><br>Sanction No</div></td>
							<td width="279" class="fieldcell"><input name="worksDetail_sanctionno" class="fieldinput" id="worksDetail_sanctionno" size="250" exilDataType="exilAnyChar"  > </td>

					<td width="139" class="labelcell"><div align="right"><bean:message key="WorksDetailAdd.Status"/><br>Status</div></td>
								<!--<td width="364"><input type="checkbox" name="worksDetail_isActive" id="worksDetail_isActive" value="0" ></td>-->
                                <td class=smallfieldcell>
			                             <select class="combowidth1" style="width=80" name="worksDetail_isActive" id="worksDetail_isActive">
			                                      <option value="1">Active</option>
			                                      <option value="0">Cancel</option>			
			                             </select>
		                       </td>

							</tr>

						 <tr>
                 <td width="184" class="labelcell"><div align="right"><bean:message key="WorksDetailAdd.SancationDate"/><br>Sanction Date</div></td>
				 <td width="279" class="smallfieldcell"><input name="worksDetail_sanctionDate1" id="worksDetail_sanctionDate1" class="datefieldinput"
                     size="10" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilDataType="exilAnyDate" >
                       <A onclick="fillDate1('worksDetail_sanctionDate1')" tabIndex="" href=#><IMG tabIndex=-1 src="../../images/calendar.gif"></A></td>
							<td width="139" style="DISPLAY: none" id="tdsCaption" name="tdsCaption" class="labelcell" ><div align="right"> TDS</div></td>
							 <td style="DISPLAY: none"  id="tdsText" name="tdsText" class="fieldcell"><input type="hidden" class="fieldinput-left" size=5 id="worksDetail_workstds" name="worksDetail_workstds" maxlength="50" style="WIDTH: 60px; HEIGHT: 19px" readOnly>
							 	<INPUT class=fieldinput-left  id="workstdscode" style   ="WIDTH: 111px; HEIGHT: 19px"   maxLength=20 size=17 name="workstdscode" id="workstdscode" exilDataType="exilAnyChar" exilInGrid="true">
								<IMG id=IMG1  onclick=openSearch3(this) height=22 src="../../images/plus1.gif"  width=25 align=top border=0>
							</td>
							<TD class="normaltext"></TD><TD class="normaltext"></TD>

						</tr>

                        <tr>

							<td width="184" class="labelcell"><div align="right"><bean:message key="WorksDetailAdd.Narration"/><br> Narration</div></td>
							<td width="279" class="fieldcelldesc"><TEXTAREA name="worksDetail_bankGuarantee" id="worksDetail_bankGuarantee" exilDataType="exilAnyChar"></TEXTAREA> </td>
								<td width="139" class="labelcell"><div align="right" ><bean:message key="WorksDetailAdd.Remarks"/><br>Remarks</div></td>
								<td width="364" class="fieldcelldesc"><TEXTAREA name="worksDetail_remarks"  id="worksDetail_remarks" exilDataType="exilAnyChar"></TEXTAREA> </td>
<tr>
	<td class="labelcell"><div align="right">Fixed Asset</div></td>
	<td><input type="checkbox" name="isFixedAsset" id="isFixedAsset"></input> </td>
</tr>
						</tr>
						
			<tr style="display:none" id="hideTDS">
				<td align="right" valign="center" class="labelcell">
				 <DIV align=right >Deductions&nbsp;</DIV>
				</td>
				<td class="smallfieldcell">
				<SELECT   id="tds_List" name="tds_List"  multiple  style="WIDTH: 139px; HEIGHT: 122px">
				</SELECT>
				</td>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
			
			</tr>

                         <tr>

				<td height="25" colspan="4" valign="bottom" class="smalltext" width="898"><p class="smalltext">&nbsp;</p>
				</td>
			</tr>
				
			<tr>
							<td colspan="4" align="middle" width="898"><!-- Buttons Start Here -->
								<table border="0" cellpadding="0" cellspacing="0">
						<tr name="trModify" id="trModify">
						<td align="right">
						<input type=button class=button id=submitButton onclick=ButtonPress('saveclose') href="#" value="Submit"></td>
						
						<td align="right">
						<input type=button class=button onclick=onClickCancel(); href="#" value="Cancel"></td>
						
						<td align="right">
						<input type=button class=button onclick=window.close() href="#" value="Close"></td>
						
						<td align="center" style="display:none" name="trShowDeductionsModify" id="trShowDeductionsModify">
						<input align="center" type=button class=button id=submitButton onclick="showDeductions();" href="#" value="Show&nbsp;Deductions">
						</td>
						
						</tr>
						<tr name="trNew" id="trNew">
						<td align="right">
						<input type=button class=button id=savenNew onclick=ButtonPress('savenew') href="#" value="Save &amp; New"></td>
						
						<td align="right">
						<input type=button class=button id=savenClose onclick=ButtonPress('saveclose') href="#" value="Save &amp; Close"></td>
						
						<td align="right">
						<input type=button class=button onclick=onClickCancel(); href="#" value="Cancel"></td>
						
						<td align="right">
						<input type=button class=button onclick=window.close() href="#" value="Close"></td>
						
						<td align="center" style="display:none" name="trShowDeductionsCreate" id="trShowDeductionsCreate">
						<input align="center" type=button class=button id=submitButton onclick="showDeductions();" href="#" value="Show&nbsp;Deductions">
						</td>
						</tr>

						</table><!-- Buttons End Here -->
					</td>
				</tr>
			</table></TD></TR></TBODY></TABLE></TD></TR></TABLE><!------------ Content ends here ------------------></TD></TR></TABLE></TD><!------------Right Navigation Ends here------------------></TR></TABLE><!---------------- Footer begins here ----------><!---------------- Footer ends here ---------->
</form>
</center>
</body>
</html>
<!-- title="worksDetail_relationId" onChange="getRelationType();"-->