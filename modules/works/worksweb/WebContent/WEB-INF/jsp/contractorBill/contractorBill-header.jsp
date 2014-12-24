<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<style>
#warning {
  display:none;
  color:blue;
}
</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script>
function showFields()
{
	var obj=dom.get("billtype");
	var selectedbillType=obj.options[obj.selectedIndex].text;
	if(selectedbillType==dom.get("billtype")[1].value){
	   dom.get("sequenceNoLabel").style.display='';
	   dom.get("sequenceNoText").style.display='';
	   dom.get("sequenceNoText").value='';
	   dom.get("completiondateLabel").style.display='none';
	   dom.get("completiondateText").style.display='none';
	   dom.get("completionDate").value='';
	}
	else if(selectedbillType==dom.get("billtype")[2].value){
	dom.get("sequenceNoLabel").style.display='none';
	dom.get("sequenceNoText").style.display='none';
	dom.get("completiondateLabel").style.display='';
	dom.get("completiondateText").style.display='';
	}else{
	dom.get("completiondateLabel").style.display='none';
	dom.get("completiondateText").style.display='none';
	dom.get("sequenceNoLabel").style.display='none';
	dom.get("sequenceNoText").style.display='none';
	dom.get("completionDate").value='';
	}
	loadCompletionDetails();
}

function calculateRetMoney(){
	var obj=dom.get("billtype");
	var selectedbillType=obj.options[obj.selectedIndex].text;
	<s:if test="%{isRebatePremLevelBill=='yes' && tenderResponse.tenderEstimate.tenderType==percTenderType}">
		refVal = dom.get('grossAmount').value;
	</s:if>
	<s:else>
		refVal = dom.get('billamount').value;
	</s:else>
	if(refVal==0 || refVal=='' || dom.get('billamount').value==0 || dom.get('billamount').value==''){
		retentionMoneyDataTable.updateCell(retentionMoneyDataTable.getRecord(0),retentionMoneyDataTable.getColumn('creditamount'),"");
		setRetMoneyTextStyle();
		return false;
	}
	if((dom.get("isRetMoneyAutoCalculated").value).toLowerCase()=='yes'){
		if(selectedbillType==dom.get("billtype")[1].value){
			calculatedVal = roundTo(refVal * dom.get("percDeductForRetMoneyPartBill").value/100);
			retentionMoneyDataTable.updateCell(retentionMoneyDataTable.getRecord(0),retentionMoneyDataTable.getColumn('creditamount'),""+calculatedVal);
		}else if(selectedbillType==dom.get("billtype")[2].value){
			if((dom.get("retMoneyFinalBillPerOnValue").value).toLowerCase()=='billvalue'){
				calculatedVal= roundTo(refVal * dom.get("percDeductForRetMoneyFinalBill").value/100);
				retentionMoneyDataTable.updateCell(retentionMoneyDataTable.getRecord(0),retentionMoneyDataTable.getColumn('creditamount'),""+calculatedVal);
			}else if((dom.get("retMoneyFinalBillPerOnValue").value).toLowerCase()=='workvalue'){
				workOrderId = dom.get("workOrderId").value;
				workOrderEstimateId = dom.get("workOrderEstimateId").value;
				billDate = dom.get("billdate").value;
				if(workOrderEstimateId!='' && dom.get("billdate").value!=''){
					makeJSONCall(["cumulativeBillValue"],'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!totalCumulativeBillValue.action',
			    	{billDate:billDate,workOrderEstimateId:workOrderEstimateId},culmBillValueLoadHandler,culmBillValueLoadFailureHandler) ;
				}
				
			}else{
				dom.get("contractorBill_error").innerHTML='Please set the configuration parameter for Retention Money.';
			    dom.get("contractorBill_error").style.display='';
			}
		}else{
			retentionMoneyDataTable.updateCell(retentionMoneyDataTable.getRecord(0),retentionMoneyDataTable.getColumn('creditamount'),"");
		}
		
	}
	setRetMoneyTextStyle();	
	calculateTotal();
}

culmBillValueLoadHandler = function(req,res){
  	results=res.results;
 	var culmBillValue = roundTo(results[0].cumulativeBillValue);
 	<s:if test="%{model.id==null}">
 		<s:if test="%{isRebatePremLevelBill=='yes' && tenderResponse.tenderEstimate.tenderType==percTenderType}">
			refVal = dom.get('grossAmount').value;
		</s:if>
		<s:else>
			refVal = dom.get('billamount').value;
		</s:else>
 		culmBillValue = getNumber(culmBillValue) + getNumber(refVal);
 	</s:if>
 	calculatedVal= roundTo(culmBillValue * dom.get("percDeductForRetMoneyFinalBill").value/100);
	retentionMoneyDataTable.updateCell(retentionMoneyDataTable.getRecord(0),retentionMoneyDataTable.getColumn('creditamount'),""+calculatedVal);
	setRetMoneyTextStyle();

}


culmBillValueLoadFailureHandler= function(){
    dom.get('contractorBill_error').innerHTML='Error in retention money data loading';
}


function loadCompletionDetails(){
	var obj=dom.get("billtype");
	var selectedbillType=obj.options[obj.selectedIndex].text;
	if(dom.get("billtype")[2].value==selectedbillType){
	  if(dom.get("workOrderEstimateId").value!="" && dom.get("billdate").value!=""){
		document.getElementById('ccTable').style.display='';
		<s:if test="%{model.id==null}">
		var actionUrl = '${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!completionInfo.action';
		var params    = 'workOrderEstimateId=' + dom.get("workOrderEstimateId").value;
		var updatePage = 'ccTable';
		var ajaxCall = new Ajax.Updater(updatePage,actionUrl,{parameters:params}		
		);
		</s:if>
		}
		else{ 
			  dom.get("cc_error").innerHTML='Please Select the Estimate,Bill Date and Bill Type to Generate Completion Certificate'; 
     		  dom.get("cc_error").style.display='';
			  return ;
          }
	}
	else{
	 	dom.get("cc_error").innerHTML='Only for '+dom.get("billtype")[2].value+' Completion Certificate will be generated'; 
     	dom.get("cc_error").style.display='';
     	document.getElementById('ccTable').style.display='none';
	 	return;
	 }
	dom.get("cc_error").innerHTML=''; 
    dom.get("cc_error").style.display='none';
}

function loadBillAmount(){
	<s:if test="%{isRebatePremLevelBill=='yes' && tenderResponse.tenderEstimate.tenderType==percTenderType}">
			dom.get("trRebatePrem").style.display='';
			dom.get("trGrossAmt").style.display='';
			var negotiatedPerc = '<s:property value="%{tenderResponse.percNegotiatedAmountRate}"/>';
			var absPerc = Math.abs(negotiatedPerc);
			dom.get("rebPremAmount").value=roundTo(dom.get("billamount").value * absPerc/100);
			if(negotiatedPerc<0){
				dom.get("tdRebateAmount").style.display='';
				dom.get("tdPremAmount").style.display='none';
				dom.get("grossAmount").value=roundTo(dom.get("billamount").value - dom.get("rebPremAmount").value);
			}else{
				dom.get("tdRebateAmount").style.display='none';
				dom.get("tdPremAmount").style.display='';
				dom.get("grossAmount").value=roundTo(getNumber(dom.get("billamount").value) + getNumber(dom.get("rebPremAmount").value));
			}
	</s:if>
	<s:else>
		dom.get("trRebatePrem").style.display='none';
		dom.get("trGrossAmt").style.display='none';
	</s:else>
}

function loadWorkName(){
var obj=dom.get('estimateId');
<s:if test="%{dropdownData.workOrderEstimateList.size()>1}">
  if(dom.get('estimateId').value!="-1"){
		<s:if test="%{id==null}"> 
		 <s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
		if('<s:property value="%{estimate.id}"/>'==obj.value){
			dom.get("workName").value='<s:property value="%{estimate.nameJS}"/>';
			dom.get("workOrderEstimateId").value='<s:property value="%{id}"/>';
			dom.get("workOrderAmount").value=roundTo('<s:property value="%{totalWorkValue}"/>');
			dom.get("function").value='<s:property value="%{estimate.financialDetails[0].function.name}"/>';
			dom.get("projectCode").value='<s:property value="%{estimate.projectCode.code}"/>';
			dom.get("ward").value='<s:property value="%{estimate.ward.name}"/>';
			workOrderId = dom.get("workOrderId").value;
			workOrderEstimateId = dom.get("workOrderEstimateId").value;
			billDate = dom.get("billdate").value;
			if(dom.get("billdate").value!='' ){
				makeJSONCall(["checkBudget","totalWorkValue","Text","Value","FromPageNo","ToPageNo"],'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!totalWorkValue.action',
		    	{workOrderId:workOrderId,billDate:billDate,workOrderEstimateId:workOrderEstimateId},totalWorkValueLoadHandler,totalWorkValueLoadFailureHandler) ;
			} 
		}
	</s:iterator>
		</s:if>
	}
</s:if>

}

function populateWorkName(obj){
  if(obj.value=="-1"){
  		dom.get("workName").value='';
  		dom.get("workOrderAmount").value='';
  		dom.get("projectCode").value='';
  		dom.get("function").value='';
  		dom.get("ward").value='';
  		dom.get("workOrderEstimateId").value='';
  		dom.get("billamount").value='';
  		dom.get("rebPremAmount").value='';
  		dom.get("grossAmount").value='';
  		populateAccountDetails();
  	}
  	else{
  		dom.get("contractorBill_error").style.display='none'; 
    	document.getElementById("contractorBill_error").innerHTML='';
    }
	<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
		if('<s:property value="%{estimate.id}"/>'==obj.value){
			dom.get("workName").value='<s:property value="%{estimate.nameJS}"/>';
			dom.get("workOrderEstimateId").value='<s:property value="%{id}"/>';
			dom.get("workOrderAmount").value=roundTo('<s:property value="%{totalWorkValue}"/>');
			dom.get("function").value='<s:property value="%{estimate.financialDetails[0].function.name}"/>';
			dom.get("projectCode").value='<s:property value="%{estimate.projectCode.code}"/>';
			dom.get("ward").value='<s:property value="%{estimate.ward.name}"/>';
			workOrderId = dom.get("workOrderId").value;
			workOrderEstimateId = dom.get("workOrderEstimateId").value;
			billDate = dom.get("billdate").value;
			if(dom.get("billdate").value!='' ){
				makeJSONCall(["checkBudget","totalWorkValue","Text","Value","FromPageNo","ToPageNo"],'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!totalWorkValue.action',
		    	{workOrderId:workOrderId,billDate:billDate,workOrderEstimateId:workOrderEstimateId},totalWorkValueLoadHandler,totalWorkValueLoadFailureHandler) ;
			} 
			populateAccountDetails(); 
		}
	</s:iterator>
	loadCompletionDetails();
}

function checkPartRate(obj){
<s:if test='isMBWithPartRate()==true'>
		var selectedbillType=obj.options[obj.selectedIndex].text;
	if(selectedbillType==dom.get("billtype")[2].value){
		dom.get("contractorBill_error").innerHTML='Final Bill cannot be created as MB has part Rate associated with it. Please select Running Bill';
		dom.get("contractorBill_error").style.display='';
		dom.get("completiondateLabel").style.display='none';
		dom.get("completiondateText").style.display='none';
		dom.get("sequenceNoLabel").style.display='none';
		dom.get("sequenceNoText").style.display='none';
		dom.get("completionDate").value='';
		return false;
	}
	else {
		dom.get("contractorBill_error").innerHTML='';
		dom.get("contractorBill_error").style.display='none';
	
	}
</s:if>
showFields();
calculateRetMoney();
	
}
function calculateTotalBillAmount()
{
	var recordedVal= getNumber(dom.get('workRecordedAmount').value);
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
	dom.get('billamount').value = roundTo(recordedVal + withHeldDebitAmts);
}

function populateWorkRecordedAmount()
{
	<s:if test='%{workRecordedAmount!=null}'>
		return;
	</s:if>	
	<s:else>
		var recordedVal= getNumber(dom.get('billamount').value);
		dom.get('workRecordedAmount').value = recordedVal;
	</s:else>
}
</script>

<div class="errorstyle" id="contractorBill_error" style="display: none;"></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>	<s:hidden name="sourcepage" id="sourcepage" value="%{sourcepage}"/>	
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
		<div class="headplacer"><s:text name="contractorBill.subHeader.workOrderDetails" /></div></td>
	</tr>	
	<tr>
		<td class="greyboxwk"><s:text name='contractorBill.workOrderNo'/>: </td>
        <td class="greybox2wk"><input type="text" name="workOrderNumber" id="workOrderNumber" value='<s:property value="%{workOrder.workOrderNumber}" />'  readonly="readonly" tabIndex="-1" class="selectboldwk" /></td>
        <td class="greyboxwk"><s:text name='contractorBill.workOrderDate'/>: </td>
        <s:date name="workOrder.workOrderDate" var="workOrderDateFormat" format="dd/MM/yyyy"/>
        <td class="greybox2wk"><input type="text" name="workOrderDate" id="workOrderDate" value='<s:property value="%{workOrderDateFormat}" />'  readonly="readonly" tabIndex="-1" class="selectboldwk" /></td>
	</tr>
	<tr>
		<td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="contractorBill.estNumber" />	:</td>
		<td width="21%" class="whitebox2wk">
			<s:if test="%{dropdownData.workOrderEstimateList.size()==1}">
				<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
					<s:textfield name="estimateNo" id="estimateNo" cssClass="selectboldwk" value="%{estimate.estimateNumber}" disabled="true"/>
					<s:hidden name="estimateId" id="estimateId" value="%{estimate.id}"/>
					<s:hidden name="workOrderEstimateId" id="workOrderEstimateId" value="%{id}"/>
				</s:iterator>
			</s:if>
			<s:else>
				<s:if test="%{id==null}"> 
					<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="estimateId" value="%{estimateId}"
						id="estimateId" cssClass="selectwk" list="dropdownData.workOrderEstimateList" listKey="estimate.id" listValue="estimate.estimateNumber" 
						onchange="populateWorkName(this);"/>
						<s:hidden name="workOrderEstimateId" id="workOrderEstimateId" />
				</s:if>
				<s:else>
					<s:textfield name="estimateNo" id="estimateNo" cssClass="selectboldwk" value="%{workOrderEstimate.estimate.estimateNumber}" disabled="true"/>
					<s:hidden name="workOrderEstimateId" id="workOrderEstimateId" value="%{workOrderEstimate.id}"/>
					<s:hidden name="estimateId" id="estimateId" value="%{workOrderEstimate.estimate.id}"/>
				</s:else>
			</s:else>						
		</td>
		<td width="11%" class="whiteboxwk"><s:text name="contractorBill.estName" /> :</td>
		<td width="21%" class="whitebox2wk"><s:if test="%{dropdownData.workOrderEstimateList.size()==1}">
			<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
				<s:textfield name="workName" value="%{estimate.name}" disabled="true" id="workName" cssClass="selectboldwk"/>
			</s:iterator>
			</s:if>
			<s:else>
				<s:if test="%{id==null}">
				  <s:textfield name="workName" id="workName" readonly="true" cssClass="selectboldwk"/>
				</s:if>
				<s:else>
					<s:textfield name="workName" value="%{workOrderEstimate.estimate.name}" disabled="true" id="workName" cssClass="selectboldwk"/>
				</s:else>
			</s:else>
		</td>
	</tr>
	
	<tr>
		<td width="11%" class="greyboxwk"><s:text name="contractorBill.workOrderEstValue" />	:</td>
		<td width="21%" class="greybox2wk">
			<s:if test="%{dropdownData.workOrderEstimateList.size()==1}">
				<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
					<s:textfield name="workOrderAmount" id="workOrderAmount" cssClass="selectamountwk" disabled="true"/>					
				</s:iterator>
			</s:if>
			<s:else>
				<s:if test="%{id==null}">
					  <s:textfield name="workOrderAmount" id="workOrderAmount" readonly="true" cssClass="selectamountwk"/>
				</s:if>
				<s:else>
					<s:textfield name="workOrderAmount" disabled="true" id="workOrderAmount" cssClass="selectamountwk"/>
				</s:else>
			</s:else>						
		</td>
		<td width="11%" class="greyboxwk"><s:text name="contractorBill.function" />	:</td>
		<td width="21%" class="greybox2wk">
			<s:if test="%{dropdownData.workOrderEstimateList.size()==1}">
				<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
					<s:textfield name="function" id="function" cssClass="selectboldwk" value="%{estimate.financialDetails[0].function.name}" disabled="true"/>					
				</s:iterator>
			</s:if>
			<s:else>
				<s:if test="%{id==null}">
					  <s:textfield name="function" id="function" readonly="true" cssClass="selectboldwk"/>
				</s:if>
				<s:else>
					<s:textfield name="function" value="%{workOrderEstimate.estimate.financialDetails[0].function.name}" disabled="true" id="function" cssClass="selectboldwk"/>
				</s:else>
			</s:else>						
		</td>
	</tr>
	
	<tr>
		<td class="whiteboxwk"><s:text name='contractorBill.nameOfContractor'/>: </td>
        <td class="whitebox2wk"><input type="text" name="nameOfContractor" id="nameOfContractor" value='<s:property value="%{workOrder.contractor.name}" />'  readonly="readonly" tabIndex="-1" class="selectboldwk" /></td>
        <td class="whiteboxwk"><s:text name='contractorBill.contractorAddress'/>: </td>        
        <td class="whitebox2wk"><input type="textarea" name="contractorAddress" id="contractorAddress" value='<s:property value="%{workOrder.contractor.paymentAddress}" />' rows="2" cols="30" readonly="readonly" tabIndex="-1" class="selectboldwk" /></td>
	</tr>
	<tr>
		<td width="11%" class="greyboxwk"><s:text name="contractorBill.projectCode" />	:</td>
		<td width="21%" class="greybox2wk">
			<s:if test="%{dropdownData.workOrderEstimateList.size()==1}">
				<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
					<s:textfield name="projectCode" id="projectCode" cssClass="selectboldwk" value="%{estimate.projectCode.code}" disabled="true"/>					
				</s:iterator>
			</s:if>
			<s:else>
				<s:if test="%{id==null}">
					  <s:textfield name="projectCode" id="projectCode" readonly="true" cssClass="selectboldwk"/>
				</s:if>
				<s:else>
					<s:textfield name="projectCode" value="%{workOrderEstimate.estimate.projectCode.code}" disabled="true" id="projectCode" cssClass="selectboldwk"/>
				</s:else>
			</s:else>						
		</td>
		<td width="11%" class="greyboxwk"><s:text name="contractorBill.ward" />	:</td>
		<td width="21%" class="greybox2wk">
			<s:if test="%{dropdownData.workOrderEstimateList.size()==1}">
				<s:iterator value="dropdownData.workOrderEstimateList" status="row_status">	
					<s:textfield name="ward" id="ward" cssClass="selectboldwk" value="%{estimate.ward.name}" disabled="true"/>					
				</s:iterator>
			</s:if>
			<s:else>
				<s:if test="%{id==null}">
					  <s:textfield name="ward" id="ward" readonly="true" cssClass="selectboldwk"/>
				</s:if>
				<s:else>
					<s:textfield name="ward" value="%{workOrderEstimate.estimate.ward.name}" disabled="true" id="ward" cssClass="selectboldwk"/>
				</s:else>
			</s:else>						
		</td>
	</tr>	
	<tr>
		<td class="whiteboxwk"><s:text name='contractorBill.workOrder.engineerInCharge'/>: </td>
        <td class="whitebox2wk"><input type="text" name="engineerInCharge" id="engineerInCharge" value='<s:property value="%{workOrder.engineerIncharge.employeeName}" />' readonly="readonly" tabIndex="-1" class="selectboldwk" /></td>
        <s:if test="%{workOrder.engineerIncharge2!=null}">
	        <td class="whiteboxwk"><s:text name='contractorBill.workOrder.engineerInCharge'/> 2: </td>
	        <td class="whitebox2wk"><input type="text" name="engineerInCharge2" id="engineerInCharge2" value='<s:property value="%{workOrder.engineerIncharge2.employeeName}" />' readonly="readonly" tabIndex="-1" class="selectboldwk" /></td>
	    </s:if>
	    <s:else>
			<td colspan="2" class="whiteboxwk"> </td>    
		</s:else>
	</tr>
	<tr>
		<td colspan="4" class="shadowwk"> </td>               
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>		
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
		<div class="headplacer"><s:text name="contractorBill.billDetails" /></div></td>
	</tr>
	<tr>
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='contractorBill.billType'/>: </td>
        <td class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="billtype" id="billtype" cssClass="selectwk" list="dropdownData.billTypeList" value="%{billtype}" onchange="checkPartRate(this);"/></td>
        <td class="greyboxwk"><span class="mandatory">*</span><s:text name='contractorBill.billDate'/>: </td>        
        <td class="greybox2wk"><s:date name="billdate" var="billDateFormat" format="dd/MM/yyyy"/>
         <s:textfield name="billdate" value="%{billDateFormat}" id="billdate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" onBlur="populateHeaderDetails();calculateStatutoryDeduction();" onChange="populateHeaderDetails();calculateStatutoryDeduction();"/>
         <a href="javascript:show_calendar('forms[0].billdate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img id="billDateImage" src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
          <s:hidden id='hiddenbillDate' name='hiddenbillDate' />
         </td>
	</tr>
	<tr id="partbilltd">
		<td id="sequenceNoLabel" class="whiteboxwk"><s:text name='contractorBill.sequenceNo'/>:</td>
        <td id="sequenceNoText" class="whitebox2wk"><input type="text" name="partbillNo" id="partbillNo" tabIndex="-1" value='<s:property value="%{partbillNo}" />' readonly="readonly" class="selectboldwk" /></td>
         <td id="completiondateLabel" class="whiteboxwk"><span  class="mandatory">*</span><s:text name='contractorBill.completionDate'/>: </td>
         <td id="completiondateText" class="whitebox2wk">
         <s:date name="completionDate" var="completionDateFormat" format="dd/MM/yyyy"/>
         <s:textfield name="completionDate" value="%{completionDateFormat}" id="completionDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
         <a href="javascript:show_calendar('forms[0].completionDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
        </td>
        <td class="whiteboxwk">&nbsp;</td>        
        <td class="whitebox2wk">&nbsp;</td>
	</tr>
	
	<tr>
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='contractorBill.bill.No'/>: </td>
        <td class="greybox2wk"><input type="text" name="partyBillNumber" id="billNumber" value='<s:property value="%{partyBillNumber}" />' class="selectboldwk" /></td>
        <td class="greyboxwk"><span class="mandatory">*</span><s:text name='contractorBill.bill.Date'/>: </td>        
        <td class="greybox2wk"><s:date name="partyBillDate" var="conbillDateFor" format="dd/MM/yyyy"/>
         <s:textfield name="partyBillDate" value="%{conbillDateFor}" id="conbillDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
         <a href="javascript:show_calendar('forms[0].conbillDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
       </td>
	</tr>	
	<tr>
		<td class="whiteboxwk"><s:text name='contractorBill.billGeneratedBy'/>: </td>
	<s:if test="%{id==null}">
        	<td class="whitebox2wk"><input type="text" name="billGeneratedBy" id="billGeneratedBy" value='<s:property value="%{billGeneratedBy}" />'  readonly="readonly" tabIndex="-1" class="selectboldwk" /></td>
        </s:if>
        <s:else>
        	<td class="whitebox2wk"><input type="text" name="billGeneratedBy" id="billGeneratedBy" value='<s:property value="%{createdBy.firstName}" />' readonly="readonly" tabIndex="-1" class="selectboldwk" /></td>
        </s:else>
        <td class="whiteboxwk"><s:text name='contractorBill.totalValueOfWorkRecorded'/>: </td>        
        <td class="whitebox2wk"><input type="text" name="workRecordedAmount" id="workRecordedAmount" value='<s:property value="%{workRecordedAmount}" />' readonly="readonly" tabIndex="-1" class="selectamountwk" /></td>
	</tr>
	<tr>
		<td class="greyboxwk">&nbsp;</td>        
        <td class="greybox2wk">&nbsp;</td>		
        <td class="greyboxwk"><s:text name='contractorBill.totalBillAmount'/>: </td>        
        <td class="greybox2wk"><input type="text" name="billamount" id="billamount" value='<s:property value="%{billAmount}" />' readonly="readonly" tabIndex="-1" class="selectamountwk" />&nbsp;<span class="mandatory"><s:text name='contractorBill.inclusiveofRM'/></span></td>
	</tr>
	<tr id="trRebatePrem">
		<td class="whiteboxwk">&nbsp;</td>        
        <td class="whitebox2wk">&nbsp;</td>
        <td class="whiteboxwk" id="tdRebateAmount" style="display: none;"><s:text name='contractorBill.rebate.amount'/>: </td>
        <td class="whiteboxwk" id="tdPremAmount"><s:text name='contractorBill.premium.amount'/>: </td>        
        <td class="whitebox2wk"><input type="text" id="rebPremAmount" readonly="readonly" tabIndex="-1" class="selectamountwk" /></td>
	</tr>
	<tr id="trGrossAmt">
		<td class="greyboxwk">&nbsp;</td>        
        <td class="greybox2wk">&nbsp;</td>
        <td class="greyboxwk"><s:text name='contractorBill.bill.gross.amount'/>: </td>        
        <td class="greybox2wk"><input type="text" id="grossAmount" name="grossAmount" readonly="readonly" tabIndex="-1" class="selectamountwk" /></td>
	</tr>			
	<tr>
		<td colspan="4" class="shadowwk"> </td>               
	</tr>
	
	<tr><td>&nbsp;</td></tr>
	<tr>		
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
		<div class="headplacer"><s:text name='contractorBill.mb.details' /></div></td>
	</tr>
	<tr>
                	<td colspan="4">
                	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
						<tr>
							<td width="3%" class="tablesubheadwk">
								<s:text name='estimate.search.slno' />
							</td>
							<td colspan="2" width="13%" class="tablesubheadwk">
								<s:text name="measurementbook.mbref" />
							</td>
							<td width="15%" class="tablesubheadwk" style="WORD-BREAK:BREAK-ALL">
								<s:text name="measurementbook.frompage" />-<s:text name="measurementbook.topage" />
							</td>
						
						</tr>					
					</table>
					</td>
				</tr>
            </table>
				<% 
						int count=0;
					%>
				<s:iterator var="e" value="mbHeaderList"> 
					<% 
						count++;
					%>
				</s:iterator>
			<% if(count>10){ %>

			<div class="scrollerboxaddestimate" >
			<%}%>
			
			
			
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="mbListTable" name="mbListTable">
					<s:iterator var="e" value="mbHeaderList" status="s">	
					<tr>
						<td width="4%" class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
							<s:property value='%{#s.index+1}' />
						</td>
						<td width="13%" colspan="2" class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
							<s:property value='%{mbRefNo}' />
						</td>
						<td width="14%" class="whitebox3wk">&nbsp;&nbsp;&nbsp;
							<s:property value="fromPageNo"/>
         					<s:if test="%{toPageNo!=null && toPageNo!='' && toPageNo>0}">-<s:property value="toPageNo"/></s:if>
						</td>
												
					</tr>
					
				</s:iterator>	
						
				</table>
				
			
				<% if(count>10){ %>

				</div>
				<%}%>
		<table width="100%" align="center">		
	<tr>
		<td colspan="4" class="shadowwk"></td>
	</tr>
	
 </table>
