<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
<title><s:text name='page.title.estimate'/></title>
<body onload="showHideMap();setDefaults();toggleWorkOrder();setRCDetailsOnLoad();refreshInbox();noBack();showAlertMessage();" onpageshow="if(event.persisted) noBack();" onunload="" class="yui-skin-sam">
<script src="<egov:url path='js/works.js'/>"></script>
<script src="../js/jquery-1.7.2.min.js"></script>

<script>
var jq = jQuery.noConflict(true);

window.history.forward(1);
function noBack() {
	window.history.forward(); 
}
designationLoadHandler = function(req,res){  
  results=res.results;
  dom.get("designation").value=results[0].Designation;
}

designationLoadFailureHandler= function(){
    dom.get("errorstyle").style.display='';
	document.getElementById("errorstyle").innerHTML='<s:text name="unable.des"/>';
}

function showHideMap()
{
	var lat = document.getElementById("lat").value ;
	var lon = document.getElementById("lon").value ;
	if(lat!='' && lon!='')
	{
		document.getElementById("latlonDiv").style.display="";
	}
	else
	{
		document.getElementById("latlonDiv").style.display="none";
		var status = '<s:property value="%{egwStatus.code}" />';
		if(status==null || status=='' || status =='NEW' || status=='REJECTED')
		{
			return;
		}
		else
		{
			document.getElementById("mapAnchor").style.display="none";
			document.getElementById("mapAnchor").onclick=function(){return false;};
		}	
	}		
}
function showDesignation(elem){
	var empId = elem.options[elem.selectedIndex].value;
    makeJSONCall(["Designation"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!designationForUser.action',{empID:empId},designationLoadHandler,designationLoadFailureHandler) ;
}

function disablePreparedBy(){
	document.getElementById('preparedBy').disabled = true;
}

function enablePreparedBy(){
	document.getElementById('preparedBy').disabled = false;
}


function hideSORTab(){
  document.getElementById('estimate_sor').style.display='none';
  document.getElementById('baseSORTable').style.display='none';
  document.getElementById('sorHeaderTable').style.display='none';
  document.getElementById('sorTable').style.display='none';
  document.getElementById('nonSorHeaderTable').style.display='none';
  document.getElementById('nonSorTable').style.display='none';
}

function showSORTab(){
  clearMessage('sor_error')
  document.getElementById('estimate_sor').style.display='';
  document.getElementById('baseSORTable').style.display='';
  document.getElementById('sorHeaderTable').style.display='';
  document.getElementById('sorTable').style.display='';
  document.getElementById('nonSorHeaderTable').style.display='';
  document.getElementById('nonSorTable').style.display='';
  document.getElementById('sorTab').setAttribute('class','Active');
  document.getElementById('sorTab').setAttribute('className','Active');   
  hideHeaderTab();
  hideOverheadsTab();
  hideAssetTab();
  hideWorkOrderTab();
  setCSSClasses('assetTab','');
  setCSSClasses('sorTab','Active');
  setCSSClasses('headerTab','First BeforeActive');
  setCSSClasses('overheadsTab','');
  setCSSClasses('workOrderTab','Last');   
  disableTables();
}

function showHeaderTab(){
  var hiddenid = document.forms[0].id.value;
  document.getElementById('estimate_header').style.display='';
  document.getElementById('financialDetails').style.display='';
  setCSSClasses('assetTab','');
  setCSSClasses('workOrderTab','Last');  
  setCSSClasses('sorTab','');
  setCSSClasses('headerTab','First Active');
  setCSSClasses('overheadsTab','');
  hideSORTab();
  hideOverheadsTab();
  hideAssetTab();
  hideWorkOrderTab();
  disableTables();
}

function hideWorkOrderTab(){
  document.getElementById('wo_div').style.display='none';
}
	
function hideHeaderTab(){
  document.getElementById('estimate_header').style.display='none';
  document.getElementById('financialDetails').style.display='none';
}

function showOverheadsTab(){
    hideSORTab();
    hideHeaderTab();
    hideAssetTab();
    hideWorkOrderTab();
    clearMessage('overheads_error');
    document.getElementById('estimate_overheads').style.display='';
    setCSSClasses('headerTab','First');
    setCSSClasses('sorTab','BeforeActive');
    setCSSClasses('overheadsTab','Active');
	setCSSClasses('assetTab','');
	setCSSClasses('workOrderTab','Last');
	document.getElementById('overheadsHeaderTable').style.display='';
    document.getElementById('overheadTable').style.display='';
    disableTables();    
}

function showAssetTab(){
    hideSORTab();
    hideHeaderTab();
    hideOverheadsTab();
    hideWorkOrderTab();
    clearMessage('asset_error');
    document.getElementById('estimate_asset').style.display='';
    setCSSClasses('headerTab','First');
    setCSSClasses('sorTab','');
    
    setCSSClasses('overheadsTab','BeforeActive');
    setCSSClasses('assetTab','Active');
	setCSSClasses('workOrderTab','Last');
    
   
	document.getElementById('assetsHeaderTable').style.display='';
    document.getElementById('assetTable').style.display='';
	setAssetTableMessage();
	disableTables();    
	makeAssetClickable();
}

/**
 * This function makes Asset code clickable for search screens and
 * estimates in workflow.
 */
function makeAssetClickable(){
	var inputTags=document.getElementById("assetTable").getElementsByTagName('input');
		for(var i=2 ;i<=inputTags.length; i+=3){
			inputTags[i].disabled=false;
			inputTags[i].readonly=true;
		}
}

function hideAssetTab(){
  document.getElementById('assetsHeaderTable').style.display='none';
  document.getElementById('assetTable').style.display='none';
  document.getElementById('estimate_asset').style.display='none';
}

function showWorkOrderTab() {
	document.getElementById('wo_div').style.display='';
    setCSSClasses('headerTab','First');
    setCSSClasses('sorTab','');    
    setCSSClasses('overheadsTab','');
    setCSSClasses('assetTab','BeforeActive');    
    setCSSClasses('workOrderTab','Last Active ActiveLast');    
   
   	hideHeaderTab();
  	hideOverheadsTab();
  	hideAssetTab();
  	hideSORTab();
  	
	disableTables();
}


function setCSSClasses(id,classes){
    document.getElementById(id).setAttribute('class',classes);
    document.getElementById(id).setAttribute('className',classes);

}
function hideOverheadsTab(){
  document.getElementById('overheadsHeaderTable').style.display='none';
  document.getElementById('overheadTable').style.disply='none';
  dom.get("overheads_error").style.display='none'
}

function validateDataBeforeSubmit(abstractEstimateForm) {
	setupDocNumberBeforeSave();
    return validateHeaderBeforeSubmit(abstractEstimateForm);
}

function enableFields(){
	for(i=0;i<document.abstractEstimateForm.elements.length;i++){
	        document.abstractEstimateForm.elements[i].disabled=false;
	}   
	setAssetStatusHiddenField();
}

function refreshInbox() {
	<s:if test="%{id != null}">
	     var x=opener.top.opener;
	     if(x==null){
	         x=opener.top;
	     }
	     if(x.document.getElementById('inboxframe') != null) {
	     	x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	     	x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	     }
	</s:if>	
}

function setDefaults(){
	
    <s:if test="%{sourcepage!='search' && !showAlertMsg}">
		populateDesignation();
	</s:if>	
	var estDate=document.getElementById('estimateDate').value;
	if(estDate=='') {
		document.getElementById('estimateDate').value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	}else{	
		document.getElementById('estimateDate').value=estDate;
	}	
	document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
	<s:if test="%{rateContract != null && rateContract.id != null}">
		document.getElementById("woEstAmtSpan").innerHTML = document.getElementById("estimateValue").value;
		var tempWorkOrderValue = eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML);
		document.getElementById("workOrderAmount").value=roundTo(tempWorkOrderValue);
		document.getElementById("woAmtSpan").innerHTML=roundTo(tempWorkOrderValue);
	</s:if>
	document.getElementById('estimateDate').disabled=true;
	document.getElementById('wardSearch').disabled=true;
}

function validateCancel() {
	var msg='<s:text name="estimate.cancel.confirm"/>';
	var estNo='<s:property value="model.estimateNumber"/>';
	if(!confirmCancel(msg,estNo)) {
		return false;
	}
	else {
		dom.get("preparedBy").disabled=false;
		return true;
	}
}

function viewDocument(){
	  viewDocumentManager(dom.get("docNumber").value); 
}

function validateEstimateAndRCFund(text){
	var estimateFund = document.getElementById('fund').value;
	var rcFund = dom.get("rcFund").value;
	if(text == 'submit_for_approval' || text == 'save'){
		if(rcFund!= '' && estimateFund != rcFund){
			showMessage('worktypeerror','<s:text name="dw.bpa.estimate.rateContract.fund.validate" />');
			return false;
		}
	}
	return true;
}

function validate(text){
	<s:if test="%{model.id==null || model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED'}">
		if(document.getElementById("actionName").value=='submit_for_approval'){
			if(document.getElementById("estimateValue").value!=''){
				var estmValue=document.getElementById("estimateValue").value;
				var parts=estmValue.split(".");
				if(parts.length>1){
					if(parts[1]>0){
						dom.get("worktypeerror").style.display='';
				        dom.get("worktypeerror").innerHTML='<s:text name="abstractEstimate.estimateValue.warningMessgae" />';
				        window.scroll(0,0);
				        return false;
					}
				}
			}
		}
	</s:if>
	<s:if test="%{model.id != null}">
		if(document.getElementById("actionName").value=='submit_for_approval') {
			if (!validateRcDetails()) {
				return false;
			}
			if (!validateWorkOrderDetails()) {
				return false;
			}
		}
		if(document.getElementById("actionName").value=='save') {
			if ((dom.get("defectLiabilityPeriod").value != null && dom.get("defectLiabilityPeriod").value != "" && dom.get("defectLiabilityPeriod").value != "0.00")
					|| (dom.get("contractPeriod").value != null && dom.get("contractPeriod").value != "")
					|| (dom.get("assignedTo1").value != null && dom.get("assignedTo1").value != "-1")
					|| (dom.get("engineerIncharge").value != null && dom.get("engineerIncharge").value != "-1") ) {
				if (!validateWorkOrderDetails()) {
					return false;
				}	
			}
		}
		
	</s:if>

	<s:if test="%{model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED'}">
		if(!validateEstimateAndRCFund(text))
			return false;
			
	</s:if>
	
	if(!validateDataBeforeSubmit(document.abstractEstimateForm))
		return false;
	
	if(!validateUser(text))
		return false;
	enableFields();
	return true;
}

function validateWorkOrderDetails() {
	if (dom.get("contractPeriod").value == null || dom.get("contractPeriod").value == "") {
    	showMessage('worktypeerror','<s:text name="contractPeriod.null"/>');
    	window.scroll(0,0);
    	return false;		
	}
	if (dom.get("defectLiabilityPeriod").value == null || dom.get("defectLiabilityPeriod").value == "" || dom.get("defectLiabilityPeriod").value == "0.00") {
    	showMessage('worktypeerror','<s:text name="defectLiabilityPeriod.null"/>');
    	window.scroll(0,0);
    	return false;		
	}
	if (dom.get("assignedTo1").value == null || dom.get("assignedTo1").value == "-1") {
    	showMessage('worktypeerror','<s:text name="lde.engineerIncharge.notselected"/>');
    	window.scroll(0,0);
    	return false;		
	}
	if (dom.get("engineerIncharge").value == null || dom.get("engineerIncharge").value == "-1") {
    	showMessage('worktypeerror','<s:text name="engineerIncharge1.notselected"/>');
    	window.scroll(0,0);	
    	return false;		
	}
	return true;
}

function validateRcDetails() {
	if (dom.get("rateContractId").value == null || dom.get("rateContractId").value == "") {
    	showMessage('worktypeerror','<s:text name="dw.bpa.estimate.ratecontract.null"/>');
    	window.scroll(0,0);
    	return false;				
	}
	if (dom.get("rcExecDept").value != null && dom.get("executingDepartment").value != null 
			&& dom.get("rcExecDept").value != dom.get("executingDepartment").value) {
    	showMessage('worktypeerror','<s:text name="ratecontract.estimate.department.mismatch"/>');
    	window.scroll(0,0);
    	return false;
	}
	if (dom.get("estimateDate").value != null 
			&& !(dom.get("estimateDate").value >= dom.get("rcStartDate").value && dom.get("estimateDate").value <=  dom.get("rcEndDate").value)) {
    	showMessage('worktypeerror','<s:text name="ratecontract.estimate.date.error"/>');
    	window.scroll(0,0);
    	return false;
	}
	
	if (dom.get("parentCategory").value != null && dom.get("parentCategory").value != "-1"
		 && dom.get("rcTypeOfWork").value != null && dom.get("parentCategory").value != dom.get("rcTypeOfWork").value) {
    	showMessage('worktypeerror','<s:text name="dw.bpa.estimate.rateContract.typeOfWork.validate"/>');
    	window.scroll(0,0);
    	return false;
	}
	if (dom.get("category").value != null && dom.get("category").value != "-1" 
		&& dom.get("rcSubTypeOfWork").value != null && dom.get("rcSubTypeOfWork").value != "" 
			&& dom.get("category").value != dom.get("rcSubTypeOfWork").value) {
    	showMessage('worktypeerror','<s:text name="dw.bpa.estimate.rateContract.subtypeOfWork.validate"/>');
    	window.scroll(0,0);
    	return false;
	}
	return true;
}

function toggleWorkOrder() {
	<s:if test="%{abstractEstimate != null && abstractEstimate.rateContract != null}">
		showWorkOrderDetails();		
	</s:if>
	<s:else>
		hideWorkOrderDetails();
	</s:else>
}

function showWorkOrderDetails() {
	document.getElementById('workorder_details').style.display = 'block';
	document.getElementById('workorder_msg').style.display = 'none';	
}

function hideWorkOrderDetails() {
	document.getElementById('workorder_details').style.display = 'none';
	document.getElementById('workorder_msg').style.display = 'block';	
}

function setRCDetailsOnLoad() {
	<s:if test="%{rateContract != null && rateContract.id != null}">
		dom.get("contractorName").innerHTML = '<s:property value="%{rateContract.contractor.name}"/> / <s:property value="%{rateContract.contractor.code}"/>';
		dom.get("rcNumber").innerHTML = '<s:property value="%{rateContract.rcNumber}"/>';
		dom.get("validFrom").innerHTML = '<s:date name="%{rateContract.indent.startDate}" var="startDateFormat" format="dd/MM/yyyy"/><s:property value="startDateFormat"/>';
		dom.get("validTo").innerHTML = '<s:date name="%{rateContract.indent.endDate}" var="endDateFormat" format="dd/MM/yyyy"/><s:property value="endDateFormat"/>';
		dom.get("rateContractId").value = '<s:property value="%{rateContract.id}"/>';
		dom.get("rcAmount").innerHTML = roundTo('<s:property value="%{rateContract.rcAmount}"/>');
		dom.get("rcExecDept").value = '<s:property value="%{rateContract.indent.department.id}"/>';
		dom.get("rcTypeOfWork").value = '<s:property value="%{rateContract.indent.typeOfWork.id}"/>';
		dom.get("rcSubTypeOfWork").value = '<s:property value="%{rateContract.indent.subTypeOfWork.id}"/>';
		dom.get("rcStartDate").value = '<s:date name="%{rateContract.indent.startDate}" var="startDateFormat" format="dd/MM/yyyy"/><s:property value="startDateFormat"/>';
		dom.get("rcEndDate").value = '<s:date name="%{rateContract.indent.endDate}" var="endDateFormat" format="dd/MM/yyyy"/><s:property value="endDateFormat"/>';
		dom.get("rcFund").value = '<s:property value="%{rateContract.indent.fund.id}"/>';
		setUtilizedAndBalance('<s:property value="%{rateContract.id}"/>', '<s:property value="%{id}"/>', '<s:property value="%{rateContract.rcAmount}"/>', true);
	</s:if>
}

function showAlertMessage() {
	<s:if test="%{showAlertMsg}">
		var skipBpaAmtValidation = confirm('<s:property value="%{alertMsg}"/>?');
		if (skipBpaAmtValidation) {
			doLoadingMask();
			dom.get("skipBpaAmtValidation").value = skipBpaAmtValidation;
			document.abstractEstimateForm.actionName.value = 'submit_for_approval';
			document.abstractEstimateForm.action = '/egworks/estimate/bpaAbstractEstimate!moveEstimate.action';
			document.abstractEstimateForm.submit();
		}
	</s:if>
}

function update(elemValue) {	
	if(elemValue!="" || elemValue!=null) {
		var a = elemValue.split("`~`");
		if (a.indexOf('fromRateContract') != "-1") {
			var contName=a[0];
			var rcNo=a[1];
			var validFr=a[2];
			var validTo=a[3];
			var rateContId = a[4];
			var rateContAmount = a[5];
			var rcExecDept = a[6];
			var rcTypeOfWork = a[7];
			var rcSubTypeOfWork = a[8];
			var rcStartDate = a[9];
			var rcEndDate = a[10];
			var rcFund = a[11];
			var utilizedRcAmount = a[13];
			var balanceRcAmount = a[14];
			var estimateId = '<s:property value="%{id}"/>';
			var estimateSubTypeOfWork = dom.get("category").value;
			if (estimateSubTypeOfWork != null && estimateSubTypeOfWork != "" && estimateSubTypeOfWork != "-1"
				&& rcSubTypeOfWork != null && rcSubTypeOfWork != "" && rcSubTypeOfWork != "-1") {
				if (rcSubTypeOfWork != estimateSubTypeOfWork) {
					showMessage('worktypeerror','<s:text name="dw.bpa.estimate.rateContract.subtypeOfWork.validate"/>');
			    	window.scroll(0,0);
			    	return false;
				}
			}
			if(contName !=null && contName != '') {
				dom.get("contractorName").innerHTML = contName;
			}
			if (rcNo != null && rcNo != '') {
				dom.get("rcNumber").innerHTML = rcNo;
			}
			if (validFr != null && validFr != '') {
				dom.get("validFrom").innerHTML = validFr;
			}
			if (validTo != null && validTo != '') {
				dom.get("validTo").innerHTML = validTo;
			}
			if (rateContId != null && rateContId != '') {
				dom.get("rateContractId").value = rateContId;
			}
			if (rateContAmount != null && rateContAmount != '') {
				dom.get("rcAmount").innerHTML = roundTo(rateContAmount);
			}
			if (rcExecDept != null && rcExecDept != '') {
				dom.get("rcExecDept").value = rcExecDept;
			}
			if (rcTypeOfWork != null && rcTypeOfWork != '') {
				dom.get("rcTypeOfWork").value = rcTypeOfWork;
			}
			if (rcSubTypeOfWork != null && rcSubTypeOfWork != '') {
				dom.get("rcSubTypeOfWork").value = rcSubTypeOfWork;
			}
			if (rcStartDate != null && rcStartDate != '') {
				dom.get("rcStartDate").value = rcStartDate;
			}
			if (rcEndDate != null && rcEndDate != '') {
				dom.get("rcEndDate").value = rcEndDate;
			}
			if (rcFund != null && rcFund != '') {
				dom.get("rcFund").value = rcFund;
			}
			populateData(utilizedRcAmount, balanceRcAmount, false);
		} else {
			var records= assetsTable.getRecordSet();
			var row_id=a[0];
			var asset_id=a[1];
			var asset_code=a[2];
			var asset_name=a[3];
			dom.get("code"+records.getRecord(getNumber(row_id)).getId()).value=asset_code;
			dom.get("name"+records.getRecord(getNumber(row_id)).getId()).value=asset_name;
			dom.get("asset"+records.getRecord(getNumber(row_id)).getId()).value=asset_id;
			dom.get("code"+records.getRecord(getNumber(row_id)).getId()).disabled=true;		
			dom.get("name"+records.getRecord(getNumber(row_id)).getId()).disabled=true;
		}
	}
}

function IntimateCitizenAndAgency() {
	var flag = confirm("This action will intimate citizen and agency requesting them to remit excess amount in the treasury. Do you want to continue ?"); 
	if(flag) {
		doLoadingMask();
		jQuery.ajax({
			url : '${pageContext.request.contextPath}/estimate/ajaxEstimate!sendBpaAmountValidationMsg.action?estimateId=<s:property value="id"/>&estimateAmount=<s:property value="totalAmount"/>',
			dataType : "text",
			success : function(bpaSucessMsg) {
				undoLoadingMask();
				var msgs = bpaSucessMsg.split("~");
				for (var i = 0; i < msgs.length; i++) {
					alert(msgs[i]);
				}
			}
		});
	}
}

jq(document).on('click', '#wpView', function(){
	var wpId = jq(this).attr("data-wpId");
    var url="${pageContext.request.contextPath}/tender/worksPackage!edit.action?id="+wpId+"&sourcepage=search";
    window.open(url,'','height=650,width=980,scrollbars=yes,status=yes');
});

jq(document).on('click', '#woView', function(){
	var woId = jq(this).attr("data-woId");
	var url = "${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+woId+"&mode=search";
	window.open(url,'', 'height=650,width=980,scrollbars=yes,status=yes');
});

</script>
<div id="worktypeerror" class="errorstyle" style="display:none;"></div>
	<s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:property value="%{estimateNumber}"/> &nbsp; <s:actionmessage theme="simple"/>
        </div>
    </s:if>
      <s:form theme="simple" name="abstractEstimateForm" onsubmit="return validateDataBeforeSubmit(this);">
       <s:if test="%{sourcepage!='search'}">
      	<s:token/>
      </s:if>
<s:push value="model">
<s:if test="%{model.estimateNumber!=null}">
	<s:hidden name="id"/>
</s:if>

<s:hidden name="mode" id="mode"/>
<s:hidden name="appDetailsId" id="appDetailsId"/>
<s:hidden name="sourcepage"/>
<s:hidden id="skipBpaAmtValidation" name="skipBpaAmtValidation"/> 
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2"><div class="datewk">
	  <s:if test="%{not model.projectCode}">
	       <div class="estimateno"><s:text name="label.estimateno" />: <s:if test="%{not model.estimateNumber}">&lt; <s:text name="message.notAssigned" /> &gt;</s:if><s:property value="model.estimateNumber" /></div>
	  </s:if>
	  <s:else>
	       <div class="estimateno">
	      <s:text name="label.estimateno" />:  <s:property value="model.estimateNumber" /> </div>
	       <div class="estimateno" style="text-align: right"> 
	       <s:text name="label.project.code" />: <s:property value="model.projectCode.code" /> </div>
	  </s:else>
	  </div>
	  <s:if test="%{model.projectCode}">
	 	<div class="datewk" style="position: relative;">
	 		<s:if test="%{wpDetails.size!=0}">
	 			<div class="estimateno" style="padding-top: 10px;width:10%;"> <s:text name="label.estimate.works.package" />: </div>
	 			<div class="estimateno" style="padding-top: 10px;width: 22%;white-space: wrap;">
	 				<s:iterator value="wpDetails" var="wpDetails" status="wpStatus"> 
			 			<a href="javascript:void(0)" id="wpView" data-wpId='<s:property value="#wpDetails[0]"/>'><s:property value="%{#wpDetails[1]}"/></a>
			 			<s:if test="!#wpStatus.last">,</s:if>
			 	 	</s:iterator>
			 	</div>
		 	 </s:if>
		 	 <s:if test="%{woDetails.size!=0}">
		 		<div class="estimateno" style="padding-top: 10px;text-align: left;width:8%;"> <s:text name="label.estimate.work.order" />: </div>
		 	 	<div class="estimateno" style="padding-top: 10px;padding-left:5px; width:25%;white-space: wrap;">
		 	 		<s:iterator value="woDetails" var="woDetails" status="woStatus">
						 <a href="javascript:void(0)" id="woView" data-woId='<s:property value="#woDetails[0]"/>'><s:property value="%{#woDetails[1]}"/></a>
						 <s:if test="!#woStatus.last">,</s:if>
		 	 		</s:iterator>
		 	 	</div>
		 	 </s:if>
		 	 <s:if test="%{wpDetails.size!=0 || woDetails.size!=0 || paymentReleased != 0.0}">
		 	 	 <div class="estimateno" style="position: absolute; right:0px;top:26px;text-align: right;width: 33%">
			 		 <s:text name="label.estimate.payments.released" />: <s:property value="paymentReleased"/>
		 	 	 </div>
		 	 </s:if>
		 	 <s:elseif  test="%{paymentReleased != 0.0}">
		 	 	<div class="estimateno" style="padding-top: 10px;">
			 		<s:text name="label.estimate.payments.released" />:<s:property value="paymentReleased"/>
		 	 	</div>
		 	 </s:elseif>
	  	</div>
	  </s:if>
<s:hidden name="model.documentNumber" id="docNumber" />

	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><div id="header">
				<ul id="Tabs">
					<li id="headerTab" class="First Active"><a id="header_1" href="#" onclick="showHeaderTab();"><s:text name="estimate.header" /></a></li>
		 			<li id="sorTab" class=""><a id="header_2" href="#" onclick="showSORTab();"><s:text name="label.works.details" /></a></li>					
					<li id="overheadsTab" class="Befor"><a id="header_4" href="#" onclick="showOverheadsTab();"><s:text name="label.overheads" /></a></li>
					<li id="assetTab" class="Befor"><a id="header_4" href="#" onclick="showAssetTab();"><s:text name="label.asset.info" /></a></li>
					<li id="workOrderTab" class="Last"><a id="header_5" href="#" onclick="showWorkOrderTab();"><s:text name="page.title.workorder.detail" /></a></li>
				</ul>
            </div></td>
          </tr>
      	<tr><td>&nbsp;</td></tr>
           <tr>
            <td>
            <div id="estimate_overheads" style="display:none;">
                 <%@ include file="bpa-estimate-overheads.jsp"%>                
            </div>
            </td>
          </tr>
          <tr>
            <td>
            <div id="estimate_asset" style="display:none;">
                 <%@ include file="bpa-estimate-asset.jsp"%>                
            </div>
            </td>
          </tr>      
          <tr>
            <td>
            <div id="estimate_header">
            	<%@ include file="bpa-estimate-header.jsp"%>   
             	<%@ include file="bpa-estimate-financialDetails.jsp"%>
           	</div>            
            </td> 
          </tr>            
          <tr>
            <td>
            <div id="estimate_sor" style="display:none;"> 
                <%@ include file="bpa-estimate-sor.jsp"%>            
            	<%@ include file="bpa-estimate-nonSor.jsp"%>
            </div>
            </td>
          </tr>
          <tr>
            <td>
	            <div id="wo_div" style="display:none;">
            		<div id="workorder_details" style="display: none">
            			<%@ include file="bpa-estimate-workorder.jsp"%>
            		</div>
            		<div class="errorstyle" id="workorder_msg" style="display: none; text-align:center">
            			<s:text name="bpa.estimate.wo.addRc.msg"/>
            		</div>
	            </div>
            </td>
          </tr>
        <tr>
            <td><table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
            <tr>
	            <td width="17%" class="whiteboxwk"><s:text name="estimate.value" />:</td>
                <td width="17%" class="whitebox2wk"><s:textfield name="estimateValue" value="%{estimateValue}"  id="estimateValue" cssClass="selectamountwk" readonly="true" align="right" />
              </td>
	            <td class="whiteboxwk">&nbsp;</td>
	            <td class="whiteboxwk">&nbsp;</td>
            </tr>
            
              <tr>
                <td width="17%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.preparedBy" />:</td>
                <td width="17%" class="whitebox2wk">
                <s:hidden name="loggedInUserEmployeeCode" id="loggedInUserEmployeeCode"/>                
                <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="estimatePreparedBy" value="%{estimatePreparedBy.idPersonalInformation}" id="preparedBy" cssClass="selectwk" list="dropdownData.preparedByList" listKey="id" listValue="employeeName" onchange='showDesignation(this);'/>     
                </td>
                <s:if test="%{dropdownData.preparedByList.size==1}" >
	                <script>
	                	disablePreparedBy();
	                </script>
                </s:if>
                <td width="12%" class="whiteboxwk"><s:text name="estimate.designation" />:</td>
                <td width="54%" class="whitebox2wk"><s:textfield name="designation" value="%{estimatePreparedByView.desigId.designationName}" type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" id="designation" size="45" tabIndex="-1" /></td>

              </tr>	     
            </table></td>
          </tr>
          
          <tr><td>&nbsp;</td></tr>
	  <tr> 
		    <td>
		    <div id="manual_workflow" >
		    <s:if test="%{sourcepage!='search'}">
		         <%@ include file="bpa-workflowApproval.jsp"%>   
		    </s:if>
		    </div>
		    </td>
            </tr>	
                   
         <tr>
            <td><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
          </tr>
        </table>
        <div class="rbbot2"><div></div></div>
      </div>     
	
</div>
  </div>
</div>
<div class="buttonholderwk">
<input type="hidden" name="actionName" id="actionName"/> 
<!-- Action buttons have to displayed only if the page is directed from the inbox -->	
<s:if test="%{(hasErrors() || sourcepage=='inbox' || egwStatus==null || egwStatus.code=='NEW' 
|| egwStatus.code=='REJECTED') && (sourcepage=='inbox' || egwStatus==null || hasErrors())}">
	<s:iterator value="%{validActions}"> 
	  <s:if test="%{description!=''}">
	  	<s:if test="%{description=='CANCEL' && estimateNumber!=null}">
			<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="cancel" onclick="return validateCancel();document.abstractEstimateForm.actionName.value='%{name}';document.abstractEstimateForm.skipBpaAmtValidation.value=false"/>
	  	</s:if>
	  	<!-- Not showing Save & Submit when estimate is not yet saved because we are enabling rate contract details only when the estimate is saved-->
	    <s:elseif test="%{description!='SAVE & SUBMIT' || (description=='SAVE & SUBMIT' && model.id!=null)}">
	  	  <s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="moveEstimate" onclick="document.abstractEstimateForm.actionName.value='%{name}';document.abstractEstimateForm.skipBpaAmtValidation.value=false;return validate('%{name}');"/>
	  	</s:elseif>
	  </s:if>
	</s:iterator>	
</s:if>
<s:if test="%{model.id==null}">
	  <input type="button" class="buttonfinal" value="CLEAR" id="button" name="button" onclick="window.open('${pageContext.request.contextPath}/estimate/bpaAbstractEstimate!newform.action?appDetailsId=<s:property value='%{appDetailsId}'/>','_self');"/>
</s:if>
<s:if test="%{sourcepage!='search'}">
	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='estimate.close.confirm'/>');"/>
	</s:if>
	<s:else>
	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
	</s:else>
<s:if test="%{model.id!=null && estimateNumber!=null}">
  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimatePDF.action?estimateID=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton"/>
  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!viewBillOfQuantitiesXls.action?sourcepage=boqPDF&id=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW BOQ XLS" id="BOQxlsButton" name="BOQxlsButton"/>
  </s:if>
  <s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (egwStatus.code=='ADMIN_SANCTIONED' || egwStatus.code=='CANCELLED'))}">
  	<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
  </s:if>
  <s:else>
  	<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();return false;" />
  </s:else>
  <s:if test="%{budgetApprNo != null}">
 	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewBudgetFolio.action?estimateId=<s:property value='%{model.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" 
     value="View Budget Folio" id="viewBudgetFolio" name="viewBudgetFolio"/>	
 </s:if>
 
   <s:if test="%{sourcepage=='search'}">
   		<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue=<s:property value='%{state.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonfinal" value="History" id="history" name="History"/>
   </s:if>
</div>
</s:push>
</s:form>
<script>hideSORTab()</script>
   
<script>
    function disableTables(){
    <s:if test="%{model.egwStatus.code!='NEW' && model.egwStatus.code!='REJECTED'}" >
        sorDataTable.removeListener('cellClickEvent');
        nonSorDataTable.removeListener('cellClickEvent');
        overheadsTable.removeListener('cellClickEvent');
        assetsTable.removeListener('cellClickEvent');
    </s:if>
    }
	<s:if test="%{model.id!=null && model.egwStatus.code!='NEW'}" >
		for(i=0;i<document.abstractEstimateForm.elements.length;i++){ 
			if(document.abstractEstimateForm.elements[i].id!='designation'){
			document.abstractEstimateForm.elements[i].disabled=true;
			document.abstractEstimateForm.elements[i].readonly=true;
			} 
		} 
	</s:if>
	<s:if test="%{model.egwStatus.code!='REJECTED'}">
			links=document.abstractEstimateForm.getElementsByTagName("a");
			for(i=0;i<links.length;i++){
					if(!(links[i].id=='mapAnchor' || links[i].id=='addRcAnchor' || links[i].id=='rcAssignment' || links[i].id=='addnonSorRow' || links[i].id=='appRequestNo'))
					{
						if(links[i].id.indexOf("header_")!=0)
		     				links[i].onclick=function(){return false;};
					}	
				}
	</s:if> 

    <s:if test="%{(sourcepage=='search' && model.egwStatus.code!='NEW')|| (sourcepage=='inbox' && (model.egwStatus.code!='NEW' && model.egwStatus.code!='REJECTED') || (model.egwStatus.code!='NEW' && hasErrors())}" > 
            disableTables();
			for(i=0;i<document.abstractEstimateForm.elements.length;i++){
	        	document.abstractEstimateForm.elements[i].disabled=true;
				document.abstractEstimateForm.elements[i].readonly=true;
			}   
			document.abstractEstimateForm.pdfButton.readonly=false;
			document.abstractEstimateForm.pdfButton.disabled=false;
			document.abstractEstimateForm.BOQxlsButton.readonly=false;
			document.abstractEstimateForm.BOQxlsButton.disabled=false;
			document.abstractEstimateForm.closeButton.readonly=false;
			document.abstractEstimateForm.closeButton.disabled=false;
			links=document.abstractEstimateForm.getElementsByTagName("a");
			for(i=0;i<links.length;i++){
		    	if(links[i].id.indexOf("header_")!=0)
     				links[i].onclick=function(){return false;};
			}
	</s:if>	
	
	<s:if test="%{(model.egwStatus.code=='CREATED' || model.egwStatus.code=='RESUBMITTED') && model.currentState.nextAction!=''}"> 		
		disableTables()
		for(i=0;i<document.abstractEstimateForm.elements.length;i++){
        	document.abstractEstimateForm.elements[i].disabled=true;
			document.abstractEstimateForm.elements[i].readonly=true;
		}   
	</s:if>

	<s:if test="%{hasErrors() && model.egwStatus.code=='ADMIN_SANCTIONED'}"> 
		for(i=0;i<document.abstractEstimateForm.elements.length;i++){
			document.abstractEstimateForm.elements[i].disabled=true;
			document.abstractEstimateForm.elements[i].readonly=true;
		} 
		disableTables();
		document.abstractEstimateForm.pdfButton.readonly=false;
		document.abstractEstimateForm.pdfButton.disabled=false;
		document.abstractEstimateForm.BOQxlsButton.readonly=false;
		document.abstractEstimateForm.BOQxlsButton.disabled=false;
		document.abstractEstimateForm.closeButton.readonly=false;
		document.abstractEstimateForm.closeButton.disabled=false;
		
		if(document.abstractEstimateForm.viewBudgetFolio!=null){
			document.abstractEstimateForm.viewBudgetFolio.readonly=false;
			document.abstractEstimateForm.viewBudgetFolio.disabled=false;
		}	
	</s:if>  
     <s:if test="%{sourcepage=='inbox'}">
		     <s:if test="%{model.id!=null}">		
				var tempEstimateValue=Math.round(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
		 		document.getElementById("estimateValue").value=roundTo(tempEstimateValue);
		 		<s:if test="%{rateContract != null && rateContract.id != null}">
		 			document.getElementById("woEstAmtSpan").innerHTML = document.getElementById("estimateValue").value;
					var tempWorkOrderValue = eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML);
					document.getElementById("workOrderAmount").value=roundTo(tempWorkOrderValue);
					document.getElementById("woAmtSpan").innerHTML=roundTo(tempWorkOrderValue);
				</s:if>
			</s:if>		
 			document.abstractEstimateForm.closeButton.readonly=false;
			document.abstractEstimateForm.closeButton.disabled=false;	
			document.abstractEstimateForm.pdfButton.readonly=false;
			document.abstractEstimateForm.pdfButton.disabled=false;
			document.abstractEstimateForm.BOQxlsButton.readonly=false;
			document.abstractEstimateForm.BOQxlsButton.disabled=false;	

			if(document.abstractEstimateForm.submit_for_approval!=null){	    
				document.abstractEstimateForm.submit_for_approval.readonly=false;
				document.abstractEstimateForm.submit_for_approval.disabled=false;
			}
			if(document.abstractEstimateForm.save!=null){
				document.abstractEstimateForm.save.readonly=false;
				document.abstractEstimateForm.save.disabled=false;
			}
				
          	disableTables();
			if(document.abstractEstimateForm.viewBudgetFolio!=null) {
				document.abstractEstimateForm.viewBudgetFolio.readonly=false;
				document.abstractEstimateForm.viewBudgetFolio.disabled=false;
			}
			if(document.abstractEstimateForm.reject!=null) {
				document.abstractEstimateForm.reject.readonly=false;
				document.abstractEstimateForm.reject.disabled=false;
			}
			if(document.abstractEstimateForm.admin_sanction!=null) {
				document.abstractEstimateForm.admin_sanction.readonly=false;
				document.abstractEstimateForm.admin_sanction.disabled=false;
			}
	 </s:if>
	 <s:if test="%{model.egwStatus.code=='REJECTED' || model.egwStatus.code=='NEW'}">
		enableFields();
		disableDamageFeeSORs();
	</s:if>
	<s:if test="%{model.id==null && model.egwStatus.code=='CREATED'}"> 
		document.abstractEstimateForm.cancel.visible=false;
		document.abstractEstimateForm.pdfButton.visible=false;
		document.abstractEstimateForm.BOQxlsButton.visible=false;
		if(document.getElementById('approverCommentsRow')!=null)
			document.getElementById('approverCommentsRow').style.display="none";
	</s:if>
	
	<s:if test="%{model.id!=null && (model.egwStatus.code=='CREATED' || model.egwStatus.code=='RESUBMITTED')}">
		if(document.getElementById('approverCommentsRow')!=null)
			document.getElementById('approverCommentsRow').style.display='';
	</s:if>
	
	<s:if test="%{sourcepage=='inbox' && model.egwStatus.code!='ADMIN_SANCTIONED'}" >
	     document.getElementById('approverCommentsRow').style.display='';
	     document.getElementById('approverComments').readonly=false;	
	     document.getElementById('approverComments').disabled=false;
	     
	   
	</s:if>
	
	<s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (model.egwStatus.code=='ADMIN_SANCTIONED' || model.egwStatus.code=='CANCELLED'))}"> 
  		document.abstractEstimateForm.docViewButton.readonly=false;
		document.abstractEstimateForm.docViewButton.disabled=false;
		
  	</s:if>
  	<s:else>
  		document.abstractEstimateForm.docUploadButton.readonly=false;
		document.abstractEstimateForm.docUploadButton.disabled=false;
  	</s:else>
	<s:if test="%{(sourcepage=='inbox' 
		&& (model.egwStatus.code!='ADMIN_SANCTIONED' || model.egwStatus.code!='CANCELLED')) || hasErrors()}">
  		document.abstractEstimateForm.departmentid.readonly=false;
		document.abstractEstimateForm.departmentid.disabled=false;
		document.abstractEstimateForm.designationId.readonly=false;
		document.abstractEstimateForm.designationId.disabled=false;
		document.abstractEstimateForm.approverUserId.readonly=false;
		document.abstractEstimateForm.approverUserId.disabled=false;
		
  	</s:if>
	<s:if test="%{sourcepage=='search'}">            
     	var tempEstimateValue=Math.round(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
        document.getElementById("estimateValue").value=roundTo(tempEstimateValue);
        <s:if test="%{rateContract != null && rateContract.id != null}">
        	document.getElementById("woEstAmtSpan").innerHTML = document.getElementById("estimateValue").value;
			var tempWorkOrderValue = eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML);
			document.getElementById("workOrderAmount").value=roundTo(tempWorkOrderValue);
			document.getElementById("woAmtSpan").innerHTML=roundTo(tempWorkOrderValue);
		</s:if>
        document.getElementById('docViewButton').style.visibility='';
        document.getElementById('history').style.visibility='';
        document.abstractEstimateForm.closeButton.readonly=false;
		document.abstractEstimateForm.closeButton.disabled=false;
		document.abstractEstimateForm.history.readonly=false;
		document.abstractEstimateForm.history.disabled=false;
      	bodyOnLoad();
        load();
         
      	sorDataTable.removeListener('cellClickEvent');
        nonSorDataTable.removeListener('cellClickEvent');
        assetsTable.removeListener('cellClickEvent');
        overheadsTable.removeListener('cellClickEvent');
     
  	</s:if>
  	
  	
    function bodyOnLoad() {
                
        for(var i=0;i<document.forms[0].length;i++) {
      		document.forms[0].elements[i].disabled =true;
      	}	
	    document.abstractEstimateForm.closeButton.readonly=false;
		document.abstractEstimateForm.closeButton.disabled=false;
		
	    document.abstractEstimateForm.docViewButton.readonly=false;
		document.abstractEstimateForm.docViewButton.disabled=false;
		
	    document.abstractEstimateForm.pdfButton.readonly=false;
		document.abstractEstimateForm.pdfButton.disabled=false;
		document.abstractEstimateForm.BOQxlsButton.readonly=false;
		document.abstractEstimateForm.BOQxlsButton.disabled=false;
		 document.abstractEstimateForm.history.readonly=false; 
		document.abstractEstimateForm.history.disabled=false;
		if(document.abstractEstimateForm.viewBudgetFolio)
			document.abstractEstimateForm.viewBudgetFolio.disabled=false;
    }
    function load(){
        links=document.abstractEstimateForm.getElementsByTagName("a");
		for(i=0;i<links.length;i++){
		   	if(links[i].id=='addnonSorRow')
		  	   links[i].onclick=function(){return false;};
		}
    }
    
    function disableDamageFeeSORs() {
    	<s:iterator id="soriterator" value="SORActivities" status="row_status">
    	 	var record = sorDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    	    var column = sorDataTable.getColumn('quantity');  
    	    dom.get("sor"+column.getKey()+record.getId()).value = roundTo('<s:property value="quantity"/>');
    	    <s:iterator id="damagefeeSorIterator" value="damageFeeSORList" status="damagefeesor_status">  
    		    <s:if test="%{code.equals(schedule.code) && category.code.equals(schedule.category.code)}">
    		    	dom.get("sor"+column.getKey()+record.getId()).readonly='true';
    		    	dom.get("sor"+column.getKey()+record.getId()).disabled='true';
    		    </s:if>
    	    </s:iterator>
    	</s:iterator> 	
    }

</script>
</body>

</html>