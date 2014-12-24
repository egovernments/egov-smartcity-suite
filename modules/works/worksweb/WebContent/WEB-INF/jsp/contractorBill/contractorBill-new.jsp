<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

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
<script src="<egov:url path='js/works.js'/>"></script>	
<script src="<egov:url path='js/helper.js'/>"></script>	
<body onload="init();getCurrentDate();loadBillAmount();loadWorkName();loadRetPercentage();populateStdDedAmountAndPercentage();populateWorkRecordedAmount();">


<script>
function populateHeaderDetails()
{
	workOrderId = dom.get("workOrderId").value;
	workOrderEstimateId = dom.get("workOrderEstimateId").value;
	billDate = dom.get("billdate").value;
	if(dom.get("billdate").value!='' && dom.get("hiddenbillDate").value!="" && dom.get("billdate").value!=dom.get("hiddenbillDate").value){
		makeJSONCall(["checkBudget","totalWorkValue","Text","Value","FromPageNo","ToPageNo"],'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!totalWorkValue.action',
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
  	var ajaxWorkValue = roundTo(results[0].totalWorkValue);
  	
  	//Find all withheld debitamount
	var eles = [];
	var inputs = document.getElementsByTagName("input");
	for(var i = 0; i < inputs.length; i++) {
	    if((inputs[i].name.indexOf('releaseWithHeldAmountDeductions[') != -1) && (inputs[i].name.indexOf('debitamount') != -1)) {
	        eles.push(inputs[i]);
	    }
	}
	var withHeldDebitAmts = 0;
	for(var j=0; j < eles.length; j++)
	{
		withHeldDebitAmts = withHeldDebitAmts+getNumber(eles[j].value);
	}
	
 	dom.get('billamount').value = ajaxWorkValue+roundTo(withHeldDebitAmts);
 	dom.get('workRecordedAmount').value= ajaxWorkValue;
 	dom.get('checkBudget').value= results[0].checkBudget;
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
   	calculateRetMoney();
   	calculateStatutoryDeduction();
 }


totalWorkValueLoadFailureHandler= function(){
    dom.get('contractorBill_error').innerHTML='Error in data loading';
}

function spillOverWorks(){
	<s:if test="%{isSpillOverWorks}">
		if(dom.get("manual_workflow")!=null){
			dom.get("manual_workflow").style.display='none';
		}
	</s:if>
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
	setCSSClasses('checklistTab','');
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
	
	<s:if test="%{workOrderEstimate.totalWorkValue!=null}">	
		dom.get("workOrderAmount").value=roundTo('<s:property value="%{workOrderEstimate.totalWorkValue}"/>');
	</s:if>
	
	<s:if test="%{dropdownData.workOrderEstimateList.size()==1}">	
		dom.get("workOrderAmount").value=roundTo('<s:property value="%{dropdownData.workOrderEstimateList[0].totalWorkValue}"/>');
	</s:if>
	<s:if test="%{disp =='yes'}">
		document.getElementById('bal').style.display='';
		document.getElementById('addbtn').style.display='none';
	</s:if>	
	//populateDesignation();
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
	 <s:if test='isMBWithPartRate()==true'>
		var selectedbillType=dom.get("billtype").options[dom.get("billtype").selectedIndex].text;
		if(selectedbillType==dom.get("billtype")[2].value){
			dom.get("contractorBill_error").innerHTML='Final Bill cannot be created as MB has part Rate associated with it. Please select Running Bill';
			dom.get("contractorBill_error").style.display='';
			return false;
		}
	</s:if>
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
	 if(dom.get('netPayableAmount').value>0){
		 if(dom.get('netPayableCode').value==0)
		 {
		 	dom.get("contractorBill_error").innerHTML='Please Select the Net payable Code'; 
	        dom.get("contractorBill_error").style.display='';
			return false; 
		 } 
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

function loadDesignationFromMatrix(){
  	
  		// var dept=document.getElementById('approverDepartment').options[document.getElementById('approverDepartment').selectedIndex].text;
  		 var currentState = dom.get('currentState').value;
  		 var amountRuleValue =  dom.get('amountRule').value;
  		 var additionalRuleValue =  dom.get('additionalRuleValue').value;
  		 var pendingAction=document.getElementById('pendingActions').value;
  		 var dept=dom.get('departmentName').value;
  		 <s:if test="%{isSpillOverWorks}">
			dept='';
			additionalRuleValue='spillOverWorks';
		 </s:if>
		 
  		 loadDesignationByDeptAndType('ContractorBillRegister',dept,currentState,amountRuleValue,additionalRuleValue,pendingAction);
}

function populateApprover()
{
  getUsersByDesignationAndDept();
}

function validateForm(type){
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
	if(type!='Approve' && type!='Reject' ){
		if(!validateWorkFlowApprover(type))
			return false;
	}
	if(document.getElementById("actionName").value=='Cancel'){
	   if(!validateCancel())
	      return false;
	}
	if(dom.get("contractorBill_error").style.display=='')
		return false;
	setupAdvanceAdjustmentValues();
	return true;
} 

function populateAccountDetails() { 
	workOrderEstimateId = dom.get("workOrderEstimateId").value;
	if(dom.get("workOrderEstimateId").value!=''){
		makeJSONCall(["checkBudget","Text","Value"],'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!getDebitAccountCodes.action',
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
   	accountDetailsDataTable.deleteRows(0,accountDetailsDataTable.getRecordSet().getLength());
 	accountDetailsDataTable.getColumn('coa').dropdownOptions=descriptionOptions;
 	coaDropdownOptions=descriptionOptions;
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
    	var obj=dom.get("coa"+accountDetailsRecords.getRecord(0).getId());
   		obj.remove(0);
		dom.get("description"+accountDetailsRecords.getRecord(0).getId()).value=obj.options[obj.selectedIndex].text.split('-')[1];
		dom.get('addRowImgDiv').style.display="none";
		dom.get("amount"+accountDetailsRecords.getRecord(0).getId()).value=roundTo(refVal);
		dom.get("delete"+accountDetailsRecords.getRecord(0).getId()).style.display="none";
	}
 }

accountDetailsLoadFailureHandler= function(){
    dom.get("accountDetails_error").style.display='block';
	dom.get("accountDetails_error").innerHTML='Unable to load Account Details';
}
function ismaxlength(obj){
var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
if (obj.getAttribute && obj.value.length>mlength)
obj.value=obj.value.substring(0,mlength)
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
	<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
	<s:hidden name="checkBudget" id="checkBudget"/>
	<s:hidden name="workOrderId" id="workOrderId"/>
	<s:hidden name="model.documentNumber" id="docNumber" />
	<s:hidden name="isRetMoneyAutoCalculated" id="isRetMoneyAutoCalculated"/>
	<s:hidden name="isRetMoneyEditable" id="isRetMoneyEditable"/>
	<s:hidden name="percDeductForRetMoneyPartBill" id="percDeductForRetMoneyPartBill"/>
	<s:hidden name="percDeductForRetMoneyFinalBill" id="percDeductForRetMoneyFinalBill"/>
	<s:hidden name="retMoneyFinalBillPerOnValue" id="retMoneyFinalBillPerOnValue"/>
	<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
	<s:hidden name="isSpillOverWorks" id="isSpillOverWorks"/>
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
							<a id="header_6" href="#" onclick="showccTab();"><s:text name="contractorBill.tab.completioncertificate" /></a>
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
		<s:if test="%{sourcepage!='search' && mode!='search'}" >
	 	 <tr> 
		    <td>
		    <div id="manual_workflow">
				    <c:set var="approverHeadCSS" value="headingwk" scope="request" />
				    <c:set var="approverCSS" value="bluebox" scope="request" />
					<%@ include file="/commons/commonWorkflow.jsp"%>
		    </div>
         					
		    </td>
            </tr>	
         </s:if> 
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
		<s:if test="%{(sourcepage=='inbox' || model.currentState==null || model.currentState.value=='NEW'  
						|| model.currentState.value=='REJECTED')}">

			<s:if test="%{model.id==null || model.currentState.value=='NEW'}">
 				<s:submit type="submit" cssClass="buttonfinal"
						value="Save" id="Save" name="Save"
						method="save"
						onclick="enableFields();document.ContractorBillForm.actionName.value='Save';return validateForm('Save');" />
 			</s:if>
 			<s:iterator value="%{getValidActions()}" var="name">
				<s:if test="%{name!=''}">
					<s:submit type="submit" cssClass="buttonfinal"
						value="%{name}" id="%{name}" name="%{name}"
						method="save"
						onclick="enableFields();document.ContractorBillForm.actionName.value='%{name}';return validateForm('%{name}');" />
				</s:if>
			</s:iterator>
		</s:if>
		<s:if test="%{(id==null}"> 
			<input type="button" class="buttonfinal" value="CLEAR" id="button" name="button" onclick="window.open('${pageContext.request.contextPath}/contractorBill/contractorBill!newform.action?workOrderId=<s:property value='%{workOrderId}'/>','_self');" />
		</s:if>		
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="confirmClose('<s:text name='bill.close.confirm'/>');" />
		<s:if test="%{model.id != null}"> 	
			<input type="button" onclick="window.open('${pageContext.request.contextPath}/contractorBill/contractorBillPDF.action?egbillRegisterId=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
			<s:if test="%{model.billtype =='Final Bill'}"> 	
			<input type="button" onclick="window.open('${pageContext.request.contextPath}/contractorBill/contractorBill!viewCompletionCertificate.action?id=<s:property value='%{model.id}'/>&workOrderId=<s:property value='%{workOrderId}'/>');" class="buttonpdf" value="View Completion Certificate" id="ccButton" name="ccButton" />
			</s:if>
			<s:else>
				<input type="button" onclick="window.open('${pageContext.request.contextPath}/contractorBill/contractorBill!viewContractCertificate.action?id=<s:property value='%{model.id}'/>&workOrderId=<s:property value='%{workOrderId}'/>');" class="buttonpdf" value="View Contract Certificate" id="contractcertButton" name="contractcertButton" />
			</s:else>
			<s:if test="%{isSecurityDeposit}">
				<input type="button" onclick="window.open('${pageContext.request.contextPath}/contractorBill/contractorBill!viewTransferEntryMemo.action?id=<s:property value='%{model.id}'/>&workOrderId=<s:property value='%{workOrderId}'/>');" class="buttonpdf" value="View Transfer Entry Memo" id="pdfButtontem" name="temButton" />
			</s:if>
		</s:if>
		<s:if test="%{mode =='view' ||  sourcepage=='search' || mode=='search'}">
			<input type="submit" class="buttonadd" value="View Document" id="viewDocButton" onclick="viewDocumentManager(dom.get('docNumber').value);return false;" />
		</s:if>	
	  	<s:else>
			<input type="submit" class="buttonadd" value="Upload Document" id="billDocUploadButton" onclick="showDocumentManager();return false;" />
	  	</s:else> 	
	</div>
	</s:push>
	</s:form>
<script>
	
function enableButtons(){
			if(dom.get("Approve")!=null){
			dom.get("Approve").disabled=false;
			dom.get("Approve").readonly=false;
		}
		if(dom.get("Cancel")!=null){
			dom.get("Cancel").disabled=false;
			dom.get("Cancel").readonly=false;
		}		
		if(dom.get("Forward")!=null){
			dom.get("Forward").disabled=false;
			dom.get("Forward").readonly=false;
		}
		if(dom.get("Reject")!=null){
			dom.get("Reject").disabled=false;
			dom.get("Reject").readonly=false;
		}
		if(dom.get("Cancel")!=null){
			dom.get("Cancel").disabled=false;
			dom.get("Cancel").readonly=false;
		}
		if(dom.get("approverComments")!=null){
			dom.get("approverComments").disabled=false;
			dom.get("approverComments").readonly=false;
		}
		if(dom.get("approverPositionId")!=null){
			dom.get("approverPositionId").disabled=false;
			dom.get("approverPositionId").disabled=false;
		}
		if(dom.get("approverDesignation")!=null){
			dom.get("approverDesignation").disabled=false;
			dom.get("approverDesignation").readonly=false;
		}
		if(dom.get("approverDepartment")!=null){
			dom.get("approverDepartment").disabled=false;
			dom.get("approverDepartment").readonly=false;
		}
		if(dom.get("pdfButton")!=null){
			dom.get("pdfButton").disabled=false;
			dom.get("pdfButton").readonly=false;
		}
		if(dom.get("contractcertButton")!=null){
			dom.get("contractcertButton").disabled=false;
			dom.get("contractcertButton").readonly=false;
		}
		if(dom.get("ccButton")!=null){
			dom.get("ccButton").disabled=false;
			dom.get("ccButton").readonly=false;
		}
		if(dom.get("pdfButtontem")!=null){
			dom.get("pdfButtontem").disabled=false;
			dom.get("pdfButtontem").readonly=false;
		}
		if(dom.get("billDocUploadButton")!=null){
			dom.get("billDocUploadButton").disabled=false;
			dom.get("billDocUploadButton").readonly=false;
		}
		if(dom.get("viewDocButton")!=null){
			dom.get("viewDocButton").disabled=false;
			dom.get("viewDocButton").readonly=false;
		}
		if(dom.get("closeButton")!=null){
			dom.get("closeButton").disabled=false;
			dom.get("closeButton").readonly=false;
		}
		if(dom.get("button")!=null){
			dom.get("button").disabled=false;
			dom.get("button").readonly=false;
		}
}
	
function disableTables(){
		links=document.ContractorBillForm.getElementsByTagName("a"); 
        disableLinks(links,['contractorBill_checklist','contractorBill_header','contractorBill_details']);
        accountDetailsDataTable.removeListener('cellClickEvent');
        statutoryDeductionsDataTable.removeListener('cellClickEvent');
        standardDeductionsDataTable.removeListener('cellClickEvent');
        customDeductionsDataTable.removeListener('cellClickEvent');
        retentionMoneyDataTable.removeListener('cellClickEvent');
	}
	<s:if test="%{model.id==null || ((model.id !=null || sourcepage=='inbox') && (model.currentState.value=='REJECTED' || model.currentState.value=='NEW'))}">
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
    <s:if test="%{sourcepage=='inbox' && (model.currentState.value=='NEW' || model.currentState.value=='REJECTED')}">
        //toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
      	//hideElements(['workflowDetials']);
      	//<s:if test="%{sourcepage=='inbox'}">
      	//showElements(['approverCommentsRow']) 
      	//</s:if>
 		toggleForSelectedFields(true,[]);
 		enableFields();
      	</s:if>
	<s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && model.currentState.value!='REJECTED' && model.currentState.value!='NEW')}">
 		disableTables();
 		disableFields();
 		enableButtons();
 </s:if>
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