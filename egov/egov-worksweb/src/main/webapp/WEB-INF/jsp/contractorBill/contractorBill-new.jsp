<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>

<style type="text/css">
.yui-dt table{
  width:100%;
}
.yui-dt-col-Add{
  width:5%;
}
.yui-dt-col-Delete{
  width:5%;
}
</style>
<html>
<title><s:text name='contractorBill.header.title' /></title>

<body onload="getCurrentDate();loadBillAmount();loadWorkName();noBack();showAccountDetails();checkBudgetHeadForWorkflowBill();" onpageshow="if(event.persisted) noBack();" onunload="">
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>	

<script>
window.history.forward(1);
function noBack() {
	window.history.forward(); 
}

function checkBudgetHeadForWorkflowBill(){
	<s:if test="%{skipBudget && model.id!=null}">
	var errorMsgFlag = dom.get("allowForward").value;
	if(errorMsgFlag == 'no'){
		if(jQuery('#save').val()!=null){
			jQuery("#save").hide();
		}
		if(jQuery('#submit_for_approval').val()!=null){
			jQuery("#submit_for_approval").hide();
		}
		if(jQuery('#approval').val()!=null){
			jQuery("#approval").hide();
		}
		if(jQuery('#approve').val()!=null){
			jQuery("#approve").hide();
		}
	}
	</s:if>
	<s:if test="%{skipBudget && model.id!=null && (model.status.code=='NEW' || model.status.code=='REJECTED')}">
		var records= accountDetailsDataTable.getRecordSet();
		dom.get("coa"+records.getRecord(0).getId()).readonly=true;
		dom.get("coa"+records.getRecord(0).getId()).disabled=true;
	</s:if>
}

function defaultBudgetHead(){
	<s:if test="%{skipBudget && model.id==null && showValidationMsg!='yes'}">
		var records= accountDetailsDataTable.getRecordSet();
		dom.get("coa"+records.getRecord(0).getId()).readonly=true;
		dom.get("coa"+records.getRecord(0).getId()).disabled=true;
	</s:if>
}
function showAccountDetails()
{
	//Default only when it is non deposit works and there is only 1 asset and 1 coa
	<s:if test="%{!skipBudget && model.id==null && dropdownData.assestList.size()==1 && dropdownData.coaList.size()==1}">
		workValue();
	</s:if>
}
function populateHeaderDetails()
{
	var billDateValue = dom.get("billdate").value;
	if(dom.get("contractorBill_error").innerHTML=='<s:text name="enter.billdate.first" />' && billDateValue!='')
	{
		dom.get("contractorBill_error").innerHTML=='';
		dom.get("contractorBill_error").style.display='none';
	}
	workOrderId = dom.get("workOrderId").value;
	workOrderEstimateId = dom.get("workOrderEstimateId").value;
	billDate = dom.get("billdate").value;
	if(dom.get("billdate").value!='' && dom.get("hiddenbillDate").value!="" && dom.get("billdate").value!=dom.get("hiddenbillDate").value){
		makeJSONCall(["noMBsPresent","checkBudget","totalTenderedItemsAmt","totalWorkValue","Text","Value","FromPageNo","ToPageNo"],'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!totalWorkValue.action',
    	{workOrderId:workOrderId,billDate:billDate,workOrderEstimateId:workOrderEstimateId},totalWorkValueLoadHandler,totalWorkValueLoadFailureHandler) ;
	}
	dom.get("hiddenbillDate").value=billDate;
	var accountDetailsRecords= accountDetailsDataTable.getRecordSet();
	<s:if test="%{isRebatePremLevelBill=='yes' && tenderResponse.tenderEstimate.tenderType==percTenderType}">
		refVal = dom.get('grossAmount').value;
	</s:if>
	<s:else>
		refVal = dom.get('billamount').value;
	</s:else>
	if(accountDetailsRecords.getLength()==1)
	{
		dom.get("amount"+accountDetailsRecords.getRecord(0).getId()).value=roundTo(refVal);
		dom.get("totalAmount").innerHTML=roundTo(refVal);
		dom.get("netPayableAmount").value =roundTo(refVal);
	}
	loadCompletionDetails();
}
totalWorkValueLoadHandler = function(req,res){
  	results=res.results;
 	dom.get('billamount').value = roundTo(results[0].totalWorkValue);
 	dom.get('checkBudget').value= results[0].checkBudget;
 	var noMbsPresent =  results[0].noMBsPresent;
 	if(noMbsPresent==true || noMbsPresent=='true')
 		totalWorkValueLoadFailureHandler();
 	dom.get('tenderedItemsAmount').value = roundTo(results[0].totalTenderedItemsAmt);
 	skipBudgObj='<s:property value="%{skipBudget}"/>';
	estimate_DropDown_Length='<s:property value="%{dropdownData.workOrderEstimateList.size()}"/>';
	srcPage='<s:property value="%{sourcepage}" />';
 	show_Hide_BudgetInfo(srcPage,accountDetailsDataTable);	
 	deleteRow('mbListTable');
 	for(i=1;i<results.length;i++){
 	  if(results[i].ToPageNo!=undefined && results[i].FromPageNo!=undefined && results[i].Text!=undefined){
 	   if(results[i].ToPageNo!='')
 	     var pageNo = results[i].FromPageNo+"-"+results[i].ToPageNo;
 	   else 
 	   	 var pageNo = results[i].FromPageNo; 	   	 
 	   	 addRow('mbListTable',results[i].Text,pageNo);
   		}   		
   	}
   	loadBillAmount();
   	populateAccountDetails();
   	var errorTxt = dom.get('contractorBill_error').innerHTML;
   	//Clear error message if there are approved MBs
   	if(errorTxt.indexOf("There are no approved MBs present for bill date") !== -1)
   	{
   		dom.get('contractorBill_error').innerHTML='';
   		dom.get("contractorBill_error").style.display='none';
   	}   	
 }


totalWorkValueLoadFailureHandler= function(){
	var billdt = dom.get('billdate');
    dom.get('contractorBill_error').innerHTML='There are no approved MBs present for bill date '+billdt.value;
    billdt.value='';
    dom.get("contractorBill_error").style.display='';
    resetAmountsAndMBTable();
}

function resetAmountsAndMBTable()
{
	var t1 = dom.get('tenderedAmtTot');
	var t2 = dom.get('nonTendLumpSumTot');
	var t3 = dom.get('billamount');
	var t4 = dom.get('rebPremAmount');
	var t5 = dom.get('grossAmount');
	var t6 = dom.get('tenderedItemsAmount');
	if(t1!=null)
		t1.value='0.00';
	if(t2!=null)
		t2.value='0.00';
	if(t3!=null)
		t3.value='0.00';
	if(t4!=null)
		t4.value='0.00';
	if(t5!=null)
		t5.value='0.00';
	if(t6!=null)
		t6.value='0.00';
	dom.get('netPayableAmount').value='0.00';
	deleteRow('mbListTable');
}

  function deleteRow(tableID) {
    var table = dom.get(tableID);
     var rowCount = table.rows.length;
	for(var i=0; i<rowCount; i++) {
         var row = table.rows[i];
          table.deleteRow(i);
            rowCount--;
             i--;
    }
  }
function validateCancel() {
	var msg='<s:text name="bill.cancel.confirm"/>';
	var estNo='<s:property value="model.billnumber"/>'; 
	if(!confirmCancel(msg,estNo)) {
		return false;
	}
	else {
		return true;
	}
}
 function addRow(tableID,text,pageno) {
	 var table = dom.get(tableID);
	 var rowCount = table.rows.length;
	 var row = table.insertRow(rowCount);
	 var cell0 = row.insertCell(0);
	 cell0.className= 'whitebox3wk';
	 cell0.width='4%';
	 cell0.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;'+eval(rowCount + 1);
	 var cell1 = row.insertCell(1);
	 cell1.className= 'whitebox3wk';
	 cell1.width='13%';
	 cell1.colspan='2';
	 cell1.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;'+text;
	 var cell2 = row.insertCell(2);
	 cell2.className= 'whitebox3wk';
	 cell2.width='14%';
	 cell2.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;'+pageno;
}

function showHeaderTab(){
   	var hiddenid = document.forms[0].id.value;

	document.getElementById('contractorBill_header').style.display=''; 
	setCSSClasses('detailsTab','');
	setCSSClasses('headerTab','First Active');
	setCSSClasses('ccTab','Last');  
	setCSSClasses('checklistTab','');
	hideDetailsTab();
	hideChecklistTab();
	hideccTab();
}
function showccTab(){
   	document.getElementById('mandatary').style.visibility = 'visible'; 
   	var hiddenid = document.forms[0].id.value;
	document.getElementById('contractorBill_comCert').style.display=''; 
	setCSSClasses('detailsTab','');
	setCSSClasses('headerTab','First'); 
	setCSSClasses('ccTab','Last Active ActiveLast'); 
	setCSSClasses('checklistTab','BeforeActive');
	hideDetailsTab();
	hideChecklistTab();
	hideHeaderTab();
}
var lastBillType = '';

function showChecklistTab(){
	var hiddenid = document.forms[0].id.value;  
	$('contractorBill_checklist').show();
	document.getElementById('mandatary').style.visibility = 'hidden'; 
	
	setCSSClasses('headerTab','First'); 
	setCSSClasses('detailsTab','BeforeActive');
	setCSSClasses('ccTab','Last');
	setCSSClasses('checklistTab','Active');
	hideHeaderTab();
	hideDetailsTab();
	hideccTab();
	var billTypeVal = $F('billtype');
	billTypeVal=billTypeVal.split(" ")[0];
	//var workOrderId = dom.get("workOrderId").value;
	var workOrderEstimateId = $F('workOrderEstimateId');
	
	if(lastBillType != billTypeVal) {
	    lastBillType = billTypeVal;
		var actionUrl = '${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!billCheckListDetails.action';
		var params    = 'billType=' + billTypeVal + '&workOrderEstimateId=' + workOrderEstimateId;
		<s:if test="%{sourcepage!='search' &&  sourcepage!='inbox' && id==null}">
		 var ajaxCall = new Ajax.Updater('contractorBill_checklist',actionUrl,
		{parameters:params}		
		);
		</s:if>
		}
}

function hideChecklistTab(){
	$('contractorBill_checklist').hide(); 
}

function hideccTab(){
	$('contractorBill_comCert').hide(); 
}

function hideHeaderTab(){
	document.getElementById('contractorBill_header').style.display='none';
}

function hideDetailsTab(){
	document.getElementById('contractorBill_details').style.display='none';
}

function showDetailsTab(){ 
	document.getElementById('mandatary').style.visibility = 'visible'; 
	document.getElementById('bal').style.display='none';

	if(document.getElementById("disp").value =="yes"){
		document.getElementById('bal').style.display='';
		document.getElementById('addbtn').style.display='none';	
	}

	document.getElementById('contractorBill_details').style.display='';
	document.getElementById('detailsTab').setAttribute('class','Active');
	document.getElementById('detailsTab').setAttribute('className','Active');
	hideHeaderTab();
	hideChecklistTab();
	hideccTab();
	setCSSClasses('detailsTab','Active');
	setCSSClasses('headerTab','First BeforeActive');
	setCSSClasses('checklistTab','');
	setCSSClasses('ccTab','Last');
}

function setCSSClasses(id,classes){
    document.getElementById(id).setAttribute('class',classes);
    document.getElementById(id).setAttribute('className',classes);
}

function validateDataBeforeSubmit(contractorBillForm) {

	
	clearMessage('contractorBill_error');	

	links=document.ContractorBillForm.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    
    if(errors) {
        dom.get("contractorBill_error").style.display='';
    	document.getElementById("contractorBill_error").innerHTML='<s:text name="contractorBill.validate_x.message" />';
    	return false;
    }	

  	clearMessage('contractorBill_error');
    return true;
}


function enableFields(){
	for(i=0;i<document.ContractorBillForm.elements.length;i++){
		document.ContractorBillForm.elements[i].disabled=false;
		document.ContractorBillForm.elements[i].readonly=false;	       
	}   
}

function disableFields(){
	for(i=0;i<document.ContractorBillForm.elements.length;i++){
	   	document.ContractorBillForm.elements[i].disabled=true;
		document.ContractorBillForm.elements[i].readonly=true;
	}
	document.ContractorBillForm.closeButton.readonly=false;
	document.ContractorBillForm.closeButton.disabled=false;	
	links=document.ContractorBillForm.getElementsByTagName("a"); 
	for(i=0;i<links.length;i++){	
    	if(links[i].id.indexOf("header_")!=0)
		links[i].onclick=function(){return false;};     		  
	}
}

function getCurrentDate() {
    var billDate=document.ContractorBillForm.billdate;	
	if(billDate.value=='') {
		billDate.value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	}
	dom.get("hiddenbillDate").value=billDate.value;
	dom.get('billamount').value = roundTo(dom.get('billamount').value);
 	dom.get("totalAmount").innerHTML=roundTo(calculateTotal());	
 	showFields();
	var obj=dom.get("billtype");
	var selectedbillType=obj.options[obj.selectedIndex].text;
	if(dom.get("billtype")[2].value==selectedbillType){
		document.getElementById('ccTable').style.display='';
	}
	else document.getElementById('ccTable').style.display='none';
	//dom.get('workOrderAmount').value=roundTo('<s:property value="%{workOrder.workOrderAmount}" />');
	<s:if test="%{workOrderEstimate.workOrder.workOrderAmount!=null}">	
		dom.get("workOrderAmount").value=roundTo('<s:property value="%{workOrderEstimate.workOrder.workOrderAmount}"/>');
	</s:if>
	
	<s:if test="%{dropdownData.workOrderEstimateList.size()==1 && id==null}">	
		dom.get("workOrderAmount").value=roundTo('<s:property value="%{dropdownData.workOrderEstimateList[0].workOrder.workOrderAmount}"/>');
	</s:if>
	<s:if test="%{disp =='yes'}">
		document.getElementById('bal').style.display='';
		document.getElementById('addbtn').style.display='none';
	</s:if>	
	<s:if test="%{sourcepage!='search'}">
		populateDesignation();
	</s:if>
	}
	
	
function validateBillHeader()
{
	var currentDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	estimate_id = document.getElementById('estimateId').value;
	if(estimate_id=='' || estimate_id=='-1'){
		dom.get("contractorBill_error").style.display=''; 
        document.getElementById("contractorBill_error").innerHTML='<s:text name="mbheader.workOrderEstimate.null" />';  
		window.scroll(0,0);
		return false;
	}  
	
	if(dom.get('billamount').value<=0)
	{
		dom.get("contractorBill_error").innerHTML='No pending bills for work order'; 
        dom.get("contractorBill_error").style.display='';
		return false;
	 }
	 if(dom.get('billtype').value==-1)
	 {
		dom.get("contractorBill_error").innerHTML='Please Select the Bill Type'; 
        dom.get("contractorBill_error").style.display='';
		return false;
	 }
	 if(dom.get('billNumber').value=='')
	 {
		dom.get("contractorBill_error").innerHTML='Please Enter the Contractor Bill Number'; 
        dom.get("contractorBill_error").style.display='';
		return false;
	 }
	 
	 var errorMsg='<s:text name="contractorBill.create.billDate.notFound" />';
	 var errorMsg1='<s:text name="contractorBill.create.billDate.greaterthan.currenDate" />';
	 if(!validate_Date(dom.get('billdate'),currentDate,errorMsg,errorMsg1))
	  return false;
	 
	 errorMsg='<s:text name="partyBillDate.not.found" />';
	 errorMsg1='<s:text name="partyBillDate.greaterthan.currentDate" />';
	 if(!validate_Date(dom.get('conbillDate'),currentDate,errorMsg,errorMsg1))
	  return false;
	 
	 if(dom.get("billtype").value==dom.get("billtype")[2].value){
		 errorMsg='<s:text name="completionDate.not.found" />';
		 errorMsg1='<s:text name="completionDate..greaterthan.currentDate" />';	 
		 if(!validate_Date(dom.get('completionDate'),currentDate,errorMsg,errorMsg1))
		   return false;
	 }
	 if(!compareWCDateAndLatestMBDate()){
		 return false;
	 }
	 
	 if(dom.get('mbListTable').rows.length==0)
	 {
	 	dom.get("contractorBill_error").innerHTML='Bill Cannot be created with zero MB Entries'; 
        dom.get("contractorBill_error").style.display='';
		return false;
	 }
	 if(dom.get('netPayableAmount').value=='' || dom.get('netPayableAmount').value<0)
	 {
		dom.get("contractorBill_error").innerHTML='The Net payable cannot be less than 0'; 
        dom.get("contractorBill_error").style.display='';
		return false;
	 }
	 if(dom.get('netPayableCode').value==0)
	 {
	 	dom.get("contractorBill_error").innerHTML='Please Select the Net payable Code'; 
        dom.get("contractorBill_error").style.display='';
		return false;
	 }
	 dom.get("contractorBill_error").style.display='none';
	 dom.get("contractorBill_error").innerHTML='';
	return true;
}

function validate_Date(obj,currentDate,errorMsg,errorMsg1){
 	if(obj.value==''){
		dom.get("contractorBill_error").innerHTML=errorMsg; 
        dom.get("contractorBill_error").style.display='';
        window.scroll(0,0);
		return false;
	 }
	 else if(compareDate(obj.value,currentDate) == -1 ){
		dom.get("contractorBill_error").innerHTML=errorMsg1; 
       	dom.get("contractorBill_error").style.display='';
       	window.scroll(0,0);
		return false;
	 }
	 return true;
}


function validateForm(type){
	if(type!='reject'){
		if(!validateBillHeader())return false;
	    if(!totalworkValue(calculateTotal()))
			return false;
		validateDataBeforeSubmit();
		if(!validateOtherDeductions())
			return false; 
		if(!checkValues(accountDetailsDataTable.getRecordSet().getLength()))
			 return false;
		if(!validateForStatutoryDeduction()){
		 return false;
		}
		if(!validateNumbers(document.getElementById("currenbilldeduction").value)){
		  return false;
		}
		if(!validateBillUser(type)) return false;
		if(dom.get("contractorBill_error").style.display=='')
			return false;
		setupAdvanceAdjustmentValues();
		enableFields();
		return true;
	}
	else{
		enableFields();
	}
} 

function validateBillUser(name){
	document.getElementById('approver_error').style.display ='none';
	if(name != 'reject' && name != 'cancel' && name!='save') {
	 	<s:if test="%{model.currentState==null || ((model.currentState.nextAction!='Pending for Approval') && !(model.currentState.value=='REJECTED' && model.currentState.nextAction=='Pending for Verification') )}" >
		if(null != document.getElementById("designationId") && document.getElementById("designationId").value == -1){
			document.getElementById('approver_error').style.display ='';	
			document.getElementById('approver_error').innerHTML ="";
			document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.designation.null"/>';
			return false;
		}
		if(null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
			document.getElementById('approver_error').style.display ='';	
			document.getElementById('approver_error').innerHTML ="";
			document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.null"/>';
			return false;
		}
	 	</s:if>
	}
	if(name=='revert')
	{
		<s:if test="%{model.currentState.nextAction!='Pending for Verification'}" >
			if(null != document.getElementById("designationId") && document.getElementById("designationId").value == -1){
				document.getElementById('approver_error').style.display ='';	
				document.getElementById('approver_error').innerHTML ="";
				document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.designation.null"/>';
				return false;
			}
			if(null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
				document.getElementById('approver_error').style.display ='';	
				document.getElementById('approver_error').innerHTML ="";
				document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.null"/>';
				return false;
			}
		</s:if>
	}	
	return true; 
}

function populateAccountDetails() { 
	workOrderEstimateId = dom.get("workOrderEstimateId").value;
	if(dom.get("workOrderEstimateId").value!=''){
		makeJSONCall(["checkBudget","showValidationMsg","Text","Value","AssetCode","AssetId"],'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!getDebitAccountCodes.action',
    	{workOrderEstimateId:workOrderEstimateId},accountDetailsLoadHandler,accountDetailsLoadFailureHandler) ;
	}else{
		accountDetailsDataTable.deleteRows(0,accountDetailsDataTable.getRecordSet().getLength());
		calculateTotal();
	}
}

accountDetailsLoadHandler = function(req,res){
  results=res.results; 
 	descriptionOptions=[{label:"-------Select------", value:"0"}];
 	var j=1;
 	for(i=1;i<results.length;i++){
  		if(results[i].Text!=undefined && results[i].Value!=undefined) {
  			descriptionOptions[j]={label:results[i].Text, value:results[i].Value}
			j++;
		}
   	}
 	assetsDropDownOptions=[{label:"-------Select------", value:"0"}];
 	var k=1;
 	for(var l=1;l<results.length;l++){
  		if(results[l].AssetCode!=undefined && results[l].AssetId!=undefined) {
  			assetsDropDownOptions[k]={label:results[l].AssetCode, value:results[l].AssetId}
			k++;
		}
   	}
   	accountDetailsDataTable.deleteRows(0,accountDetailsDataTable.getRecordSet().getLength());
 	accountDetailsDataTable.getColumn('coa').dropdownOptions=descriptionOptions;
 	coaDropdownOptions=descriptionOptions;

 	accountDetailsDataTable.getColumn('asset').dropdownOptions=assetsDropDownOptions;
 	assetDropDownOptions=assetsDropDownOptions;
 	<s:if test="%{isRebatePremLevelBill=='yes' && tenderResponse.tenderEstimate.tenderType==percTenderType}">
		refVal = dom.get('grossAmount').value;
	</s:if>
	<s:else>
		refVal = dom.get('billamount').value;
	</s:else>
 	dom.get("totalAmount").innerHTML=roundTo(refVal);
	dom.get("netPayableAmount").value =roundTo(refVal);
  	accountDetailsDataTable.addRow({SlNo:accountDetailsDataTable.getRecordSet().getLength()+1,amount:roundTo(refVal)});
  	var accountDetailsRecords= accountDetailsDataTable.getRecordSet();

  	if(results[0].checkBudget=="false"){
	  	var errorMsg =   results[0].showValidationMsg;
	  	if(errorMsg != '' && errorMsg=='yes'){
			dom.get("contractorBill_error").innerHTML='<s:text name="contractoBill.depositCOA.budgetHead.mapping.error" />';
		    dom.get("contractorBill_error").style.display='';
		    return false;
		}	
		else{
			if(accountDetailsRecords.getRecord(0) != null){
				dom.get("coa"+accountDetailsRecords.getRecord(0).getId()).readonly=true;
				dom.get("coa"+accountDetailsRecords.getRecord(0).getId()).disabled=true;
			}
		}
  	}
    if(results[0].checkBudget=="false"){
    	var obj=dom.get("coa"+accountDetailsRecords.getRecord(0).getId());
   		obj.remove(0);
		dom.get("description"+accountDetailsRecords.getRecord(0).getId()).value=obj.options[obj.selectedIndex].text.split('-')[1];
		dom.get('addRowImgDiv').style.display="none";
		dom.get("amount"+accountDetailsRecords.getRecord(0).getId()).value=roundTo(refVal);
		dom.get("delete"+accountDetailsRecords.getRecord(0).getId()).style.display="none";
	}
    calculateRetMoney();
   	calculateStatutoryDeduction();
 }

accountDetailsLoadFailureHandler= function(){
    dom.get("accountDetails_error").style.display='block';
	dom.get("accountDetails_error").innerHTML='Unable to load Account Details';
}


</script>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<div class="errorstyle" id="contractorBill_error" style="display: none;"></div>
	
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:property value="%{billnumber}" />
			&nbsp;
			<s:actionmessage theme="simple" />
		</div>
	</s:if>

	<s:form theme="simple" name="ContractorBillForm" >
	<s:token/>
	<s:push value="model">
	<s:hidden name="id" id="id"/>
	<s:hidden name="checkBudget" id="checkBudget"/>
	<s:hidden name="workOrderId" id="workOrderId"/>
	<s:hidden name="model.documentNumber" id="docNumber" />
	<s:hidden name="isRetMoneyAutoCalculated" id="isRetMoneyAutoCalculated"/>
	<s:hidden name="isRetMoneyEditable" id="isRetMoneyEditable"/>
	<s:hidden name="percDeductForRetMoneyPartBill" id="percDeductForRetMoneyPartBill"/>
	<s:hidden name="percDeductForRetMoneyFinalBill" id="percDeductForRetMoneyFinalBill"/>
	<s:hidden name="retMoneyFinalBillPerOnValue" id="retMoneyFinalBillPerOnValue"/>
	<s:hidden name="isRCEstimate" id="isRCEstimate" value="%{isRCEstimate}" />
	<s:hidden name="showValidationMsg" id="showValidationMsg" value="%{showValidationMsg}" />
	<s:hidden name="allowForward" id="allowForward" value="%{allowForward}" />
	
	
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
	<div class="datewk">
	<div class="estimateno">
		<s:text name="contractorBill.billNo" />:
		<s:if test="%{not billnumber}">&lt; 
			<s:text	name="message.notAssigned" /> &gt;
		</s:if>
		<s:property value="billnumber" />
	</div>
	</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<div id="header">
					<ul id="Tabs">
						<li id="headerTab" class="First Active">
							<a id="header_1" href="#" onclick="showHeaderTab();"><s:text name="contractorBill.tab.header" /></a>
						</li>
						<li id="detailsTab" class="">
							<a id="header_2" href="#" onclick="showDetailsTab();"><s:text name="contractorBill.tab.accountDetails" /></a>
						</li>
						<li id="checklistTab" class="">
							<a id="header_4" href="#" onclick="showChecklistTab();"><s:text name="contractorBill.tab.checklist" /></a>
						</li>
						<li id="ccTab" class="Last">
							<a id="header_4" href="#" onclick="showccTab();">Completion Certificate</a>
						</li>
					</ul>
				</div>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<div id="contractorBill_header">
					<%@ include file="contractorBill-header.jsp"%>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="contractorBill_details" style="display: none;">
					<%@ include file="contractorBill-details.jsp"%>
				</div>
			</td>
		</tr>
		
			<tr>
			<td>
				<div  id="contractorBill_checklist" style="display: none;">
				<s:if test="%{sourcepage=='search' || sourcepage=='inbox' || id!=null}">
				<%@ include file="ajaxContractorBill-searchChecklist.jsp"%>
				</s:if>
		       		</div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="contractorBill_comCert" style="display: none;">
					<%@ include file="contractorBill-completionCertificate.jsp"%>
				</div>
			</td>
		</tr>
		
		 <tr><td>&nbsp;</td></tr>
	 	 <tr> 
		    <td>
		    <div id="manual_workflow">
		   		<%@ include file="../workflow/workflow.jsp"%> 
		    </div>
		    </td>
            </tr>	
         
			<s:hidden name="scriptName" id="scriptName" value="works.contractorbill"></s:hidden>
			<s:hidden name="workflowFunctionaryId" id="workflowFunctionaryId" ></s:hidden>
		<tr>
			<td>
				<div id="mandatary" align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">*
					<s:text name="message.mandatory" />
				</div>
			</td>
		</tr>
		</table>
	</div>
	<div class="rbbot2"><div></div></div>
	</div>
	</div>
	</div>
	<div id="button_submit" class="buttonholderwk">
		<input type="hidden" name="actionName" id="actionName" />
		<s:if test="%{((sourcepage=='inbox' || model.status==null || model.status.code=='NEW'  
						|| model.status.code=='REJECTED') || hasErrors() || hasActionMessages()) }">
		<s:iterator value="%{validActions}">
			<s:if test="%{description!=''}">
				<s:if test="%{description == 'CANCEL' && model.id != null}">
					<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" 
							 method="cancel" onclick="enableFields('%{name}');
							 document.ContractorBillForm.actionName.value='%{name}';return validateCancel();"/>
 							</s:if>
						<s:else>
					
							<s:submit type="submit" cssClass="buttonfinal"
								value="%{description}" id="%{name}" name="%{name}"
								method="save"
								onclick="document.ContractorBillForm.actionName.value='%{name}';
								return validateForm('%{name}');" />
						</s:else>
					</s:if>
				 </s:iterator>
			  </s:if>
		<s:if test="%{(id==null}"> 
			<input type="button" class="buttonfinal" value="CLEAR" id="button" name="button" onclick="window.open('${pageContext.request.contextPath}/contractorBill/contractorBill!newform.action?workOrderId=<s:property value='%{workOrderId}'/>','_self');" />
		</s:if>		
		<s:if test="%{sourcepage!='search'}">
			<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="confirmClose('<s:text name='bill.close.confirm'/>');" />
		</s:if>
		<s:else>
			<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();"/>
		</s:else>
		<s:if test="%{model.id != null}"> 	
			<input type="button" onclick="window.open('${pageContext.request.contextPath}/contractorBill/contractorBillPDF.action?egbillRegisterId=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
			<s:if test="%{model.billtype =='Final Bill'}"> 	
			<input type="button" onclick="window.open('${pageContext.request.contextPath}/contractorBill/contractorBill!viewCompletionCertificate.action?id=<s:property value='%{model.id}'/>&workOrderId=<s:property value='%{workOrderId}'/>');" class="buttonpdf" value="View Completion Certificate" id="ccButton" name="ccButton" />
			</s:if>
			<input type="submit" class="buttonadd" value="View Document" id="viewDocButton" onclick="viewDocumentManager(dom.get('docNumber').value);return false;" />
		</s:if>	
	  	<s:else>
			<input type="submit" class="buttonadd" value="Upload Document" id="billDocUploadButton" onclick="showDocumentManager();return false;" />
	  	</s:else> 	
		
		
	</div>
	</s:push>
	</s:form>
	<script>
	<s:if test="%{model.id==null || ((model.id !=null || sourcepage=='inbox') && (model.status.code=='REJECTED' || model.status.code=='NEW'))}">
		setRetMoneyTextStyle();
	</s:if>
	<s:if test="%{sourcepage=='search' || sourcepage=='inbox'}">
		   load();
    </s:if>
<s:if test="%{model.id !=null}">
var obj=dom.get("billtype");
	obj.disabled=true;
	obj.readonly=true;
dom.get("billdate").disabled=true; 
document.getElementById('billDateImage').style.display='none';
</s:if>
    function load(){
    <s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && model.currentState.value=='REJECTED' && model.currentState.nextAction=='Pending for Verification' )}">
        toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
      	hideElements(['workflowDetials']);
      	<s:if test="%{sourcepage=='inbox'}">
      	showElements(['approverCommentsRow']) 
      	</s:if>
      	links=document.ContractorBillForm.getElementsByTagName("a"); 
        disableLinks(links,['contractorBill_checklist','contractorBill_header','contractorBill_details']);
        accountDetailsDataTable.removeListener('cellClickEvent');
        statutoryDeductionsDataTable.removeListener('cellClickEvent');
        standardDeductionsDataTable.removeListener('cellClickEvent');
        customDeductionsDataTable.removeListener('cellClickEvent');
        retentionMoneyDataTable.removeListener('cellClickEvent');
 </s:if>
<s:elseif test="%{sourcepage=='inbox' && model.status.code!='REJECTED' && model.status.code!='NEW'}">
     toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
     links=document.ContractorBillForm.getElementsByTagName("a");
     showElements(['approverCommentsRow']) 
     disableLinks(links,['contractorBill_checklist','contractorBill_header','contractorBill_details']);
     accountDetailsDataTable.removeListener('cellClickEvent');
     statutoryDeductionsDataTable.removeListener('cellClickEvent');
     standardDeductionsDataTable.removeListener('cellClickEvent');
     customDeductionsDataTable.removeListener('cellClickEvent');
     retentionMoneyDataTable.removeListener('cellClickEvent');
 </s:elseif>
 <s:elseif test="%{sourcepage=='inbox' && model.currentState.value=='REJECTED' && model.currentState.nextAction!=null && model.currentState.nextAction!=''}">
	 toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
	 links=document.ContractorBillForm.getElementsByTagName("a");
	 showElements(['approverCommentsRow']) 
	 disableLinks(links,['contractorBill_checklist','contractorBill_header','contractorBill_details']);
	 accountDetailsDataTable.removeListener('cellClickEvent');
	 statutoryDeductionsDataTable.removeListener('cellClickEvent');
	 standardDeductionsDataTable.removeListener('cellClickEvent');
	 customDeductionsDataTable.removeListener('cellClickEvent');
	 retentionMoneyDataTable.removeListener('cellClickEvent');
</s:elseif> 
 <s:elseif test="%{sourcepage=='inbox' && model.currentState.value=='REJECTED' && (model.currentState.nextAction==null || model.currentState.nextAction=='')}">
      toggleForSelectedFields(true,[]);
      showElements(['approverCommentsRow']);
  </s:elseif>
<s:if test="%{sourcepage=='search' || sourcepage=='inbox'}">
 enabledivChilderns("button_submit");
</s:if>


}



function setRetMoneyTextStyle(){
			if((dom.get("isRetMoneyEditable").value).toLowerCase()=='yes'){
		    	document.getElementById("creditamount" + retentionMoneyDataTable.getRecordSet().getRecord(0).getId()).disabled=false;
		    }else{
		    	document.getElementById("creditamount" + retentionMoneyDataTable.getRecordSet().getRecord(0).getId()).disabled=true;
		    }
}
    </script>
		</body>
	
</html>
