<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<html>
<title><s:text name='page.title.estimate'/></title>
<body  onload="spillOverWorks();loadRcDetails();loadDesignation();refreshInbox();setCurrentdate();toggleBudgetDetails();" class="yui-skin-sam"> 
<script src="<egov:url path='js/works.js'/>"></script> 
<script src="<egov:url path='js/helper.js'/>"></script>
<script> 

var globalRcIdList=new Array();

function hideApproverInfo(){
	<s:if test="%{(getValidActions()!=null) && getValidActions().contains('Approve')}">
		if(document.getElementById('approverDetials')!=null){
			hideElements(['approverDetials']);
		}
	</s:if>
}

function getWorkflowAdditionalRule(){
	var wardName=document.getElementById("wardSearch").value;

  	if (wardName.length == 0) {
        document.getElementById("wardID").value = -1;
    }

	var deptId = document.getElementById("executingDepartment").value;
	var type = document.getElementById("type").value;
	var wardId = document.getElementById("wardID").value;
	
	var myWorkflowAdditionalRuleSuccessHandler = function(req,res) {
		document.getElementById('additionalRuleValue').value=res.results[0].value;
		if(document.getElementById("approverDepartment")!=null && document.getElementById("approverDesignation")!=null){
			document.getElementById("approverDepartment").value=-1;
			document.getElementById("approverDesignation").value=-1;
		}		
    };
            
	var myWorkflowAdditionalRuleFailureHandler = function() {
	    document.getElementById("wardID").value=-1;
	    document.getElementById("wardSearch").value='';
	    document.getElementById("executingDepartment").value=-1;
	    document.getElementById("type").value=-1;
	};

	 if (wardId!=-1 && deptId!=-1 && type!=-1){
	 	makeJSONCall(["value"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!getWorkFlowAdditionalRule.action',{wardId:wardId,departmentId:deptId,typeId:type},myWorkflowAdditionalRuleSuccessHandler,myWorkflowAdditionalRuleFailureHandler) ;
	 }
}

function preparedByDesignationForEstimate(elem){
	var temp, result;
	if(elem.type=='hidden')
	{
		result = document.getElementById("preparedByTF").value.split('-');
		
	}
	else
	{
		temp=elem.options[elem.selectedIndex].innerHTML; 
		result=temp.split('-');
		
	}
	if(result.length>1 && result[1]!="" && result[1]!=null && result[1]!=undefined)
		return result[1];
	else
		return "";
}

function loadRcDetails(){
 	<s:if test="%{!estimateRateContractList.isEmpty()}">
 		 document.abstractEstimateForm.isRateContract.checked=true;
 		 document.getElementById("rctypeHeader").style.display="block";
		 document.getElementById("rctypeDropDown").style.display="block";
		 document.getElementById('estimateRCtab').style.display="block";
		 document.getElementById('baseSORTable').style.display='none';
		 document.getElementById('sorTableforRC').style.display="block";
		 document.getElementById('nonSorTableforRC').style.display="block"; 
		 document.getElementById('addnonSorButtn').style.display="none";  
		 
		 sorDataTable.showColumn("Contractor");
	     sorDataTable.showColumn("RCNumber");
		 nonSorDataTable.showColumn("Contractor");
	     nonSorDataTable.showColumn("RCNumber");
	     document.abstractEstimateForm.codeSearch.disabled=true;
		 document.getElementById('codeSubmitButton').disabled=true;
		 document.getElementById('searchTemplateButton').disabled=true;
		 if(nonSorDataTable.getRecordSet().getLength()==0){
		 nonSorDataTable.showTableMessage ('<s:text name="estimate.ratecontract.nonsor.initial.table.message"/>');
		 }
	 </s:if>
	 <s:else>
		 document.abstractEstimateForm.isRateContract.checked=false;
	 </s:else>
 }

function enableViewBalanceLink(){
	var links=document.abstractEstimateForm.getElementsByTagName("a");
	for(i=0;i<links.length;i++){
		if(links[i].id=='viewBalanceLink' || links[i].id=='viewBalanceImageLink'){
		  	   links[i].onclick=function(){return openViewBudget();};
		}
	}
}

function spillOverWorks(){
	<s:if test="%{isSpillOverWorks}">
		if(dom.get("emergencyWorks")!=null){
			dom.get("emergencyWorks").style.display='none';
		}
		if(dom.get("spillOverWorks")!=null){
			dom.get("spillOverWorks").style.display='block';
		}
		if(dom.get("manual_workflow")!=null){
			dom.get("manual_workflow").style.display='none';
		}
	</s:if>
	<s:else>
		var obj=dom.get("isSpillOverWorks");
		checkSpillOverWorks(obj);
	</s:else>
}

function showDesignation(elem, eventtype){
	//Added to show employee name and designation in preparedby dropdown
	dom.get("worktypeerror").style.display='none';
	document.getElementById("worktypeerror").innerHTML=""; 
	if(elem.value != -1){
		var preparedByDesgn=preparedByDesignationForEstimate(elem);
		if(preparedByDesgn!="")
		 	dom.get("designation").value=preparedByDesgn;
		else{
			if(eventtype!="onload")
			{
				dom.get("worktypeerror").style.display='';
			 	document.getElementById("worktypeerror").innerHTML='Unable to load designation';
			}	
		} 
	}
	else
		dom.get("designation").value="";	
}

function loadDesignation(){
	showDesignation(dom.get("preparedBy"),"onload");
}

function hideSORTab(){
  document.getElementById('estimate_sor').style.display='none';
  document.getElementById('baseSORTable').style.display='none';
  document.getElementById('sorHeaderTable').style.display='none';
  document.getElementById('sorTable').style.display='none';
  document.getElementById('nonSorHeaderTable').style.display='none';
  document.getElementById('nonSorTable').style.display='none';
  document.getElementById('sorTableforRC').style.display="none";
  document.getElementById('nonSorTableforRC').style.display="none"; 
}

function showSORTab(){
  clearMessage('sor_error') 
  if(dom.get('estimateDate').value==''){
  	 showMessage('sor_error','Fill in the estimate date before adding work details');
  }
  document.getElementById('estimate_sor').style.display='';
  if(document.abstractEstimateForm.isRateContract.checked!=true)
  	document.getElementById('baseSORTable').style.display='';
  else{
  	document.getElementById('sorTableforRC').style.display='';
  	document.getElementById('nonSorTableforRC').style.display=''; 
  	document.getElementById('addnonSorButtn').style.display="none";
  }
  document.getElementById('sorHeaderTable').style.display=''; 
  document.getElementById('sorTable').style.display='';
  document.getElementById('nonSorHeaderTable').style.display='';
  document.getElementById('nonSorTable').style.display='';
  document.getElementById('sorTab').setAttribute('class','Active');
  document.getElementById('sorTab').setAttribute('className','Active');   
  hideHeaderTab();
  hideOverheadsTab();
  hideAssetTab();
  hideFinancialDetailsTab();
  hideChecklistTab();
  $('doc_div').hide();
  setCSSClasses('assetTab','');
  setCSSClasses('sorTab','Active');
  setCSSClasses('headerTab','First BeforeActive'); 
  setCSSClasses('overheadsTab','');
  setCSSClasses('docTab','');  
  setCSSClasses('financialDetailsTab','');  
  setCSSClasses('checklistTab','Last');
  disableTables();
}

function showHeaderTab(){
  var hiddenid = document.forms[0].id.value;
  document.getElementById('estimate_header').style.display='';
  setCSSClasses('assetTab','');
  setCSSClasses('docTab','');  
  setCSSClasses('financialDetailsTab','');
  setCSSClasses('sorTab','');
  setCSSClasses('headerTab','First Active');
  setCSSClasses('overheadsTab','');
  setCSSClasses('checklistTab','Last');
  hideSORTab();
  hideOverheadsTab();
  hideAssetTab();
  hideFinancialDetailsTab();
  hideChecklistTab();
  $('doc_div').hide();
  disableTables();
}

function hideHeaderTab(){
  document.getElementById('estimate_header').style.display='none';
}

function showOverheadsTab(){
    hideSORTab();
    hideHeaderTab();
    hideAssetTab();
    hideFinancialDetailsTab();
    hideChecklistTab();
    clearMessage('overheads_error');
  	if(dom.get('estimateDate').value==''){
  	 	showMessage('overheads_error','Fill in the estimate date before adding over head details');
  }
    document.getElementById('estimate_overheads').style.display='';
    setCSSClasses('headerTab','First');
    setCSSClasses('sorTab','BeforeActive');
    setCSSClasses('overheadsTab','Active');
	setCSSClasses('assetTab','');
	setCSSClasses('docTab','');  
    setCSSClasses('financialDetailsTab','');
    setCSSClasses('checklistTab','Last');
	document.getElementById('overheadsHeaderTable').style.display=''; 
    document.getElementById('overheadTable').style.display='';
    $('doc_div').hide();
    disableTables();    
}

function showAssetTab(){
    hideSORTab();
    hideHeaderTab();
    hideOverheadsTab();
    hideFinancialDetailsTab();
    hideChecklistTab();
    clearMessage('asset_error');
    var elem = document.getElementById('type');
  	if(elem.options[elem.selectedIndex].value=='-1'){
  	 	showMessage('asset_error','Select the nature of work before adding asset details');
  }
    document.getElementById('estimate_asset').style.display='';
    setCSSClasses('headerTab','First');
    setCSSClasses('sorTab','');
    
    setCSSClasses('overheadsTab','BeforeActive');
    setCSSClasses('assetTab','Active');
	setCSSClasses('docTab','');  
    setCSSClasses('financialDetailsTab',''); 
    setCSSClasses('checklistTab','Last');
    
   
	document.getElementById('assetsHeaderTable').style.display='';
    document.getElementById('assetTable').style.display='';
    $('doc_div').hide();
	setAssetTableMessage();
	disableTables();    
}

function hideAssetTab(){
  document.getElementById('assetsHeaderTable').style.display='none';
  document.getElementById('assetTable').style.display='none';
  document.getElementById('estimate_asset').style.display='none';
}

var htmldefault ='';
function showDocTab() {
	document.getElementById('estimate_asset').style.display='';
    setCSSClasses('headerTab','First');
    setCSSClasses('sorTab','');    
    setCSSClasses('overheadsTab','');
    setCSSClasses('assetTab','BeforeActive');    
    setCSSClasses('docTab','Active');    
    setCSSClasses('financialDetailsTab',''); 
    setCSSClasses('checklistTab','Last');
    
   	hideHeaderTab();
  	hideOverheadsTab();
  	hideAssetTab();
  	hideSORTab();
  	hideFinancialDetailsTab();
  	hideChecklistTab();
  	
	disableTables();
	if(htmldefault =='') {
		myEditor.setEditorHTML(unescape($F('documentId')));
		//$('msgpost_toolbar').insert({bottom:$('printButton')});
		htmldefault = 'initialized';
	}
	
	$('doc_div').show();
}


function showFinancialDetailsTab() {
	document.getElementById('financialDetails_div').style.display='';
	document.getElementById('financialDetailTable').style.display='';
	document.getElementById('financialSourceHeaderTable').style.display='';
	setCSSClasses('headerTab','First');
    setCSSClasses('sorTab','');    
    setCSSClasses('overheadsTab','');
    setCSSClasses('assetTab','');    
    setCSSClasses('docTab','BeforeActive');  
    setCSSClasses('financialDetailsTab','Active'); 
    setCSSClasses('checklistTab','Last');
    hideHeaderTab();
  	hideOverheadsTab();
  	hideAssetTab();
  	hideSORTab();
  	hideChecklistTab();
  	$('doc_div').hide();
}

function hideFinancialDetailsTab(){
  document.getElementById('financialDetailTable').style.display='none';
  document.getElementById('financialSourceHeaderTable').style.display='none';  
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



function showChecklistTab(){
	$('checkList_div').show();
	hideHeaderTab();
  	hideOverheadsTab();
  	hideAssetTab();
  	hideSORTab();
  	hideFinancialDetailsTab();

 	setCSSClasses('headerTab','First');
    setCSSClasses('sorTab','');
    setCSSClasses('overheadsTab','');
    setCSSClasses('assetTab','');
	setCSSClasses('docTab','');  
    setCSSClasses('financialDetailsTab','BeforeActive'); 
    setCSSClasses('checklistTab','Last Active ActiveLast');
 	$('doc_div').hide();
}

function hideChecklistTab(){  
$('checkList_div').hide();
}

 
function validateDataBeforeSubmit(abstractEstimateForm) {
	setupDocNumberBeforeSave();
	setDocumentValues();
    return validateHeaderBeforeSubmit(abstractEstimateForm) && validateMultiYearEstimateForm() && validateDataBeforeSave(abstractEstimateForm);
}

function enableFields(){
	for(i=0;i<document.abstractEstimateForm.elements.length;i++){
	        document.abstractEstimateForm.elements[i].disabled=false;
	}   
	setAssetStatusHiddenField();
	setDocumentValues();
}

function refreshInbox() {
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
        x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}

function setCurrentdate(){
	var estdate=document.getElementById('estimateDate').value;
	if(estdate=='') {
		document.getElementById('estimateDate').value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	}else{	
		document.getElementById('estimateDate').value=estdate;
	}
}

function validateCancel() {
	var msg='<s:text name="estimate.cancel.confirm"/>';
	var estNo='<s:property value="model.estimateNumber"/>';
	if(!confirmCancel(msg,estNo)) {
		return false;
	}
	else {
		return true;
	}
}
function viewDocument(){
	  viewDocumentManager(dom.get("docNumber").value); 
}

function loadDesignationFromMatrix(){
	var value=document.getElementById("executingDepartment").value;
	if(value=="-1"){
		alert("Please select the executing department");
		document.getElementById("approverDepartment").value=-1;
		return false;
	}
	   var currentState=document.getElementById('currentState').value;
       //var amountRuleValue=document.getElementById('amountRuleValue').value;
       var amountRuleValue=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
       var additionalRuleValue=document.getElementById('additionalRuleValue').value;
       var pendingAction=document.getElementById('pendingActions').value;
       var dept=document.getElementById('executingDepartment').options[document.getElementById('executingDepartment').selectedIndex].text;

  	   loadDesignationByDeptAndType('AbstractEstimate',dept,currentState,null,additionalRuleValue,pendingAction);
}

function populateApprover()
{
  getUsersByDesignationAndDept();
}


function validate(text){
    if(document.abstractEstimateForm.necessity.value.length<=0){
  	    dom.get("worktypeerror").style.display='';
        dom.get("worktypeerror").innerHTML='<s:text name="estimate.necessity.null" />';
        return false;
	}
	if(document.abstractEstimateForm.scopeOfWork.value.length<=0){
  	    dom.get("worktypeerror").style.display='';
        dom.get("worktypeerror").innerHTML='<s:text name="estimate.scopeOfWork.null" />';
        return false;
	}

	if(document.getElementById("actionName").value=='Cancel'){
		if(!validateCancel()){
		return false;
		}
	}
	if(text!='Approve' && text!='Reject' ){
		var errorDivId = 'worktypeerror';
		if(!validateWorkFlowApprover(text,errorDivId))
			return false;
	}
	enableFields(); 
	return true;
}

function setFinancingSource(obj) {
	var record = financialSourceDataTable.getRecord(parseInt('0'));    
    var column = financialSourceDataTable.getColumn('Name');
    var columnPerc = financialSourceDataTable.getColumn('fundSource'); 
    for(i=0; i < fundSourceDropdownOptions.length; i++) {
        if (fundSourceDropdownOptions[i].value == obj.value) {
        	dom.get(columnPerc.getKey()+record.getId()).value = obj.value;
            financialSourceDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
        }
    } 
}

function toggleBudgetDetails(){
    var elem = document.getElementById('type');
	var appConfigValuesToSkipBudget='<s:property value="%{appConfigValuesToSkipBudget}"/>';
	appConfigValuesToSkipBudget=appConfigValuesToSkipBudget.split('[');
	appConfigValuesToSkipBudget=appConfigValuesToSkipBudget[1].split(']');
  	appConfigValuesToSkipBudget=appConfigValuesToSkipBudget[0].split(', ');
  	
    for(i=0;i<appConfigValuesToSkipBudget.length;i++){
    	if(appConfigValuesToSkipBudget[i]==elem.options[elem.selectedIndex].text) {
    		document.getElementById('budgetGrpLblBudget').style.display='none';
    		document.getElementById('budgetGrpBudget').style.display='none';
    		document.getElementById('coaLblDepositWorks').style.display='';
    		document.getElementById('coaDepositWorks').style.display='';
    		document.getElementById('depositCodeRow').style.display='';
    		break;
    	}
    	else {
    		document.getElementById('budgetGrpLblBudget').style.display='';
    		document.getElementById('budgetGrpBudget').style.display='';
    		document.getElementById('coaLblDepositWorks').style.display='none';
    		document.getElementById('coaDepositWorks').style.display='none';
    		document.getElementById('depositCodeRow').style.display='none';
    	}
    }
}
				
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
<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
<s:hidden id="amountRuleValue" name="amountRuleValue" value="%{amountRuleValue}" />
<input type="hidden" name="actionName" id="actionName"/>
<s:hidden id="estimateId" name="estimateId" value='%{model.id}'/> 
<s:hidden id="allowFutureDate" name="allowFutureDate" value="%{allowFutureDate}" />
	
	
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2"><div class="datewk">
	  <s:if test="%{not model.projectCode}">
	       <div class="estimateno">Estimate No: <s:if test="%{not model.estimateNumber}">&lt; Not Assigned &gt;</s:if><s:property value="model.estimateNumber" /></div>
	  </s:if>
	  <s:else>
	       <div class="estimateno">Project Code: <s:property value="model.projectCode.code" /> </div>
	  </s:else>
	 <!-- <span class="bold"><s:text name="message.today" /></span> <egov:now/>--></div>
	  
<s:hidden name="model.documentNumber" id="docNumber" />
	  
	  
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><div id="header">
				<ul id="Tabs">
					<li id="headerTab" class="First Active"><a id="header_1" href="#" onclick="showHeaderTab();">Header</a></li>
		 			<li id="sorTab" class=""><a id="header_2" href="#" onclick="showSORTab();">Work Details</a></li>					
					<li id="overheadsTab" class="Befor"><a id="header_3" href="#" onclick="showOverheadsTab();">Overheads</a></li>
					<li id="assetTab" class="Befor"><a id="header_4" href="#" onclick="showAssetTab();">Asset Info</a></li>
					<li id="docTab" class="Befor"><a id="header_5" href="#" onclick="showDocTab();">Enclosures</a></li>
					<li id="financialDetailsTab" class="Befor"><a id="header_6" href="#" onclick="showFinancialDetailsTab();">Financial Details</a></li>
					<li id="checklistTab" class="Last"><a id="header_7" href="#" onclick="showChecklistTab();"><s:text name="estimate.tab.checklist" /></a></li>
				</ul>
            </div></td>
          </tr>
      	<tr><td>&nbsp;</td></tr>
           <tr>
            <td>
            <div id="estimate_overheads" style="display:none;">
                 <%@ include file="estimate-overheads.jsp"%>                
            </div>
            </td>
          </tr>
          <tr>
            <td>
            <div id="estimate_asset" style="display:none;">
                 <%@ include file="estimate-asset.jsp"%>                
            </div>
            </td>
          </tr>      
          <tr>
            <td>
            <div id="estimate_header">
           <%@ include file="estimate-header.jsp"%>
           <%@ include file="estimate-rateContract.jsp"%>
           <%@ include file="estimate-multiYearEstimate.jsp"%>  
            </div>            
            </td> 
          </tr>            
          <tr>
            <td>
            <div id="estimate_sor" style="display:none;"> 
            	<%@ include file="estimate-template.jsp"%> 
                <%@ include file="estimate-sor.jsp"%>            
            	<%@ include file="estimate-nonSor.jsp"%>
            	<%@ include file="estimate-measurementSheet.jsp"%>
            </div>
            </td>
          </tr>
          <tr>
            <td>
	            <div id="doc_div" style="display:none;">
	            	<%@ include file="abstractEstimate-doctemplate.jsp"%>
	            </div>
            </td>
          </tr>
           <tr>
            <td>
	            <div id="financialDetails_div" style="display:none;">
	            	<%@ include file="estimate-financialDetail.jsp"%>
	            </div>
            </td>
          </tr>
          
        <tr>
            <td>
	            <div id="checkList_div" style="display:none;">
						<%@ include file="ajaxEstimate-searchCheckList.jsp"%>
	            </div>
	            <br>
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
                <td width="17%" class="whiteboxwk"><span class="mandatory">*</span><span class="epstylewk"><s:text name="estimate.preparedBy" />:</span></td>
                <td width="17%" class="whitebox2wk">
                <s:if test="%{(sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED') || sourcepage=='search' }" >
	                <s:textfield id="preparedByTF" value="%{preparedByTF}" type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" />
	                <s:hidden name="estimatePreparedBy" id="preparedBy" value="%{estimatePreparedBy.idPersonalInformation}"/>
	            </s:if>
	            <s:else>
	            	<s:select headerKey="-1" 
	                			headerValue="%{getText('estimate.default.select')}" 
	                			name="estimatePreparedBy" 
	                			value="%{estimatePreparedBy.idPersonalInformation}" 
	                			id="preparedBy" cssClass="selectwk" 
	                			list="dropdownData.preparedByList"  listKey="id" 
	                			listValue='employeeName+ "-" +desigId.designationName' 
	                			onchange='showDesignation(this,"onchange");'/>
	            </s:else>    			
                </td>
                <td width="12%" class="whiteboxwk"><s:text name="estimate.designation" />:</td>
                <td width="54%" class="whitebox2wk"><s:textfield name="designation" type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" id="designation" size="45" tabIndex="-1" /></td>
              </tr>	     
            </table></td>
          </tr>


	<tr><td>&nbsp;</td></tr>
          <s:if test="%{showResolutionDetails==true}">
           <tr> 
		    <td>
		    	<div id="draft_resolution">
					<%@ include file="estimate-resolutionData.jsp"%>
  				</div>
 		    </td>
            </tr>
          </s:if>

          
          <tr><td>&nbsp;</td></tr>
	 	 <s:if test="%{mode != 'view' && sourcepage!='search'}" >
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
            <td><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
          </tr>
        </table>
        <div class="rbbot2"><div></div></div>
      </div>     
	
</div>
  </div>
</div>
<s:if test="%{model.id==null|| model.currentState.value=='NEW' || !isSpillOverWorks}">
	<div id="emergencyWorks" class="buttonholderwk">
<!-- Action buttons have to displayed only if the page is directed from the inbox -->	
<s:if test="%{hasErrors() || sourcepage=='inbox' || model.currentState==null || model.currentState.value=='NEW' 
|| model.currentState.value=='REJECTED'}">

 		<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
 			<s:submit type="submit" cssClass="buttonfinal"
				value="Save" id="Save" name="Save"
				method="moveEstimate"
				onclick="document.abstractEstimateForm.actionName.value='Save';return validate('Save');" />
 		</s:if>
 		<s:iterator value="%{getValidActions()}" var="action">
			<s:if test="%{action!=''}">
				<s:submit type="submit" cssClass="buttonfinal"
					value="%{action}" id="%{action}" name="%{action}"
					method="moveEstimate"
					onclick="document.abstractEstimateForm.actionName.value='%{action}';return validate('%{action}');"/>
			</s:if>
		</s:iterator>  
 

<!-- 	<s:iterator value="%{validActions}"> 
	  <s:if test="%{description!=''}">
	  	<s:if test="%{description=='CANCEL' && model.estimateNumber!=null}">
			<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="cancel" onclick="return validateCancel();document.abstractEstimateForm.actionName.value='%{name}'"/>
	  	</s:if>
	    <s:else>
	  	  <s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="moveEstimate" onclick="document.abstractEstimateForm.actionName.value='%{name}';return validate('%{name}');"/>
	  </s:else>
	  </s:if>
	</s:iterator>	 -->
	</s:if>

<s:if test="%{model.id==null}">
	  <input type="button" class="buttonfinal" value="CLEAR" id="button" name="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!newform.action','_self');"/>
</s:if>
  <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="return confirmClose('<s:text name='estimate.close.confirm'/>')"/>
  <s:if test="%{model.id!=null && model.estimateNumber!=null}">
  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimatePDF.action?estimateID=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton"/>
  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!viewMeasurementSheetPdf.action?sourcepage=msheetPDF&id=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW MEASUREMENT SHEET PDF" id="mSheetPdfButton" name="mSheetPdfButton"/>
  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!viewBillOfQuantitiesPdf.action?sourcepage=boqPDF&id=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW BOQ PDF" id="BOQPdfButton" name="BOQPdfButton"/>
   </s:if>
	   
	     <s:if test="%{model.currentState!=null && model.currentState.previous!=null && (model.currentState.value=='TECH_SANCTIONED' || 
  	model.currentState.previous.value=='ADMIN_SANCTIONED' || model.currentState.value=='FINANCIALLY_SANCTIONED' || model.currentState.value=='ADMIN_CHECKED' )}">
		<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!viewTechSanctionEstimatePDF.action?id=<s:property value='%{model.id}'/>&sourcepage=techSanctionPDF');" class="buttonpdf" value="VIEW TECHSANCTIONED PDF" id="tsPdfButton" name="tsPdfButton"/>
	</s:if>
	   
  <s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (model.currentState.value=='ADMIN_SANCTIONED' || model.currentState.value=='CANCELLED'))}">
  	<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" /> 
  </s:if>
  <s:else>
  	<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();return false;" />
  </s:else>
 
	 <s:if test="%{model.currentState!=null && (model.currentState.value=='FINANCIALLY_SANCTIONED'  || 
	 model.currentState.value=='ADMIN_CHECKED' || model.currentState.previous.value=='ADMIN_SANCTIONED')}">

  <s:if test="%{appConfigValuesToSkipBudget.contains(model.type.name) && model.budgetApprNo!=null}">
   <input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewDepositFolio.action?estimateId=<s:property value='%{model.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" value="View Deposit Folio" id="depositfolioreportButton" name="depositfolioreportButton"/>    			
  
 </s:if>
  <s:elseif test="%{model.budgetApprNo!=null}"> <input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewBudgetFolio.action?estimateId=<s:property value='%{model.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" 
     value="View Budget Folio" id="viewBudgetFolio" name="viewBudgetFolio"/>
 </s:elseif>
    			
 </s:if>
   <s:if test="%{sourcepage=='search'}">
   		<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue=<s:property value='%{state.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonfinal" value="History" id="history" name="History"/>
   </s:if>
  <s:if test="%{showDraftResolution==true}">
	  	<input type="button" class="buttonpdf" value="View Draft Resolution" id="draftResoluButton" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!viewDraftResolution.action?id=<s:property value='%{model.id}'/>');" /> 
  </s:if> 
  
</div>
</s:if>
<s:if test="%{model.id==null|| model.currentState.value=='NEW' || isSpillOverWorks}">
	<div  id="spillOverWorks" class="buttonholderwk">
	<!-- Action buttons have to displayed only if the page is directed from the inbox -->	
	<s:if test="%{hasErrors() || sourcepage=='inbox' || model.currentState==null || model.currentState.value=='NEW' 
	|| model.currentState.value=='REJECTED'}">
	 		<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
	 			<s:submit type="submit" cssClass="buttonfinal"
					value="Save" id="Save" name="Save"
					method="moveEstimate"
					onclick="document.abstractEstimateForm.actionName.value='Save';return validate('Save');" />
	 		</s:if>
	 		 <s:submit type="submit" cssClass="buttonfinal"
					value="Approve" id="Approve" name="Approve"
					method="moveEstimate"
					onclick="document.abstractEstimateForm.actionName.value='Approve';return validate('Approve');" />
			 <s:if test="%{model.egwStatus.code=='NEW'}">
			  		<s:submit type="submit" cssClass="buttonfinal"
					value="Cancel" id="Cancel" name="Cancel"
					method="moveEstimate"
					onclick="document.abstractEstimateForm.actionName.value='Cancel';return validate('Cancel');" />
			 </s:if> 
	<!-- 	<s:iterator value="%{validActions}"> 
		  <s:if test="%{description!=''}">
		  	<s:if test="%{description=='CANCEL' && model.estimateNumber!=null}">
				<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="cancel" onclick="return validateCancel();document.abstractEstimateForm.actionName.value='%{name}'"/>
		  	</s:if>
		    <s:else>
		  	  <s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="moveEstimate" onclick="document.abstractEstimateForm.actionName.value='%{name}';return validate('%{name}');"/>
		  </s:else>
		  </s:if>
		</s:iterator>	 -->
		</s:if>
	
	<s:if test="%{model.id==null}">
		  <input type="button" class="buttonfinal" value="CLEAR" id="button" name="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!newform.action','_self');"/>
	</s:if>
	  <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="return confirmClose('<s:text name='estimate.close.confirm'/>')"/>
	  <s:if test="%{model.id!=null && model.estimateNumber!=null}">
	  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimatePDF.action?estimateID=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton"/>
	  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!viewMeasurementSheetPdf.action?sourcepage=msheetPDF&id=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW MEASUREMENT SHEET PDF" id="mSheetPdfButton" name="mSheetPdfButton"/>
	  	<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!viewBillOfQuantitiesPdf.action?sourcepage=boqPDF&id=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW BOQ PDF" id="BOQPdfButton" name="BOQPdfButton"/>
	   </s:if>
	   
	   
	     <s:if test="%{model.currentState!=null && model.currentState.previous!=null && (model.currentState.value=='TECH_SANCTIONED' || 
 			  	model.currentState.previous.value=='ADMIN_SANCTIONED' || model.currentState.value=='FINANCIALLY_SANCTIONED' || model.currentState.value=='ADMIN_CHECKED' )}">
			<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!viewTechSanctionEstimatePDF.action?id=<s:property value='%{model.id}'/>&sourcepage=techSanctionPDF');" class="buttonpdf" value="VIEW TECHSANCTIONED PDF" id="tsPdfButton" name="tsPdfButton"/>
	</s:if>
	   
	   
	   
	  <s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (model.currentState.value=='ADMIN_SANCTIONED' || model.currentState.value=='CANCELLED'))}">
	  	<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" /> 
	  </s:if>
	  <s:else>
	  	<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();return false;" />
	  </s:else>
	 
	 <s:if test="%{model.currentState!=null && (model.currentState.value=='FINANCIALLY_SANCTIONED'  || 
	  model.currentState.value=='ADMIN_CHECKED' || model.currentState.previous.value=='ADMIN_SANCTIONED')}">
	
	  <s:if test="%{appConfigValuesToSkipBudget.contains(model.type.name) && model.budgetApprNo!=null}">
	   <input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewDepositFolio.action?estimateId=<s:property value='%{model.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" value="View Deposit Folio" id="depositfolioreportButton" name="depositfolioreportButton"/>    			
	  
	 </s:if>
	  <s:elseif test="%{model.budgetApprNo!=null}"> <input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewBudgetFolio.action?estimateId=<s:property value='%{model.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" 
	     value="View Budget Folio" id="viewBudgetFolio" name="viewBudgetFolio"/>
	 </s:elseif>
	    			
	 </s:if>
	   <s:if test="%{sourcepage=='search'}">
	   		<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue=<s:property value='%{state.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonfinal" value="History" id="history" name="History"/>
	   </s:if>
	  <s:if test="%{showDraftResolution==true}">
	  	<input type="button" class="buttonpdf" value="View Draft Resolution" id="draftResoluButton" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!viewDraftResolution.action?id=<s:property value='%{model.id}'/>');" /> 
	  </s:if> 
	  
	</div>
</s:if>
</s:push>
</s:form>
<script>hideSORTab()</script>
   
<script>
    function disableTables(){
      <s:if test="%{(sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED') || sourcepage=='search' }" >
        multiYearEstimateDataTable.removeListener('cellClickEvent');
        overheadsTable.removeListener('cellClickEvent');
        assetsTable.removeListener('cellClickEvent');
        financialSourceDataTable.removeListener('cellClickEvent');
        contractorsDataTable.removeListener('cellClickEvent');  
        rateContractDataTable.removeListener('cellClickEvent');
    </s:if>
    }

	function enableButtons(){
		
		if(dom.get("pdfButton")!=null){
			document.abstractEstimateForm.pdfButton.readonly=false;
			document.abstractEstimateForm.pdfButton.disabled=false;
		}
		if(dom.get("mSheetPdfButton")!=null){
			document.abstractEstimateForm.mSheetPdfButton.readonly=false;
			document.abstractEstimateForm.mSheetPdfButton.disabled=false;
		}
		if(dom.get("BOQPdfButton")!=null){
			document.abstractEstimateForm.BOQPdfButton.readonly=false;
			document.abstractEstimateForm.BOQPdfButton.disabled=false;
		}
		if(dom.get("closeButton")!=null){
			document.abstractEstimateForm.closeButton.readonly=false;
			document.abstractEstimateForm.closeButton.disabled=false;
		}
		
		if(dom.get("viewBudgetFolio")!=null){
			document.abstractEstimateForm.viewBudgetFolio.readonly=false;
			document.abstractEstimateForm.viewBudgetFolio.disabled=false;
		}	
		
		if(dom.get("depositfolioreportButton")!=null){
			document.abstractEstimateForm.depositfolioreportButton.readonly=false;
			document.abstractEstimateForm.depositfolioreportButton.disabled=false;
		}
		
		if(dom.get("draftResoluButton")!=null){
			document.abstractEstimateForm.draftResoluButton.readonly=false;
			document.abstractEstimateForm.draftResoluButton.disabled=false;
		}
		
		
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
		if(dom.get("docViewButton")!=null){
			document.abstractEstimateForm.docViewButton.readonly=false;
			document.abstractEstimateForm.docViewButton.disabled=false;
		}
		if(dom.get("docUploadButton")!=null){
			document.abstractEstimateForm.docUploadButton.readonly=false;
			document.abstractEstimateForm.docUploadButton.disabled=false;	
		}
		if(dom.get("history")!=null){
		document.abstractEstimateForm.history.disabled=false;
		document.abstractEstimateForm.history.readonly=false;
		}
		
		if(dom.get("tsPdfButton")!=null){
			document.abstractEstimateForm.tsPdfButton.disabled=false;
			document.abstractEstimateForm.tsPdfButton.readonly=false; 
	}
	}
	
	function disableSelect(){
		for(i=0;i<document.abstractEstimateForm.elements.length;i++){
		if( document.abstractEstimateForm.elements[i].id!='checkListremarks' && document.abstractEstimateForm.elements[i].name!='selectedchecklistValue') {
	      document.abstractEstimateForm.elements[i].disabled=true;
		  document.abstractEstimateForm.elements[i].readonly=true;
		  }
		}   
	}
/*	
<s:if test="%{model.id!=null && model.currentState.value!='NEW'}" >
	for(i=0;i<document.abstractEstimateForm.elements.length;i++){ 
		if(document.abstractEstimateForm.elements[i].id!='designation'){
		document.abstractEstimateForm.elements[i].disabled=true;
		document.abstractEstimateForm.elements[i].readonly=true;
		}
	} 
	<s:if test="%{model.currentState.value!='REJECTED'}">
			links=document.abstractEstimateForm.getElementsByTagName("a");
			for(i=0;i<links.length;i++){
			    	if(links[i].id.indexOf("header_")!=0)
	     				links[i].onclick=function(){return false;};
				}
		</s:if> 
	</s:if> 
*/

    <s:if test="%{(sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED') || sourcepage=='search' }" > 
            disableTables();
			disableSelect();
			enableButtons();
			links=document.abstractEstimateForm.getElementsByTagName("a");			
			for(i=0;i<links.length;i++){
				if(links[i].id=="gisImage") {

	            }
	          else if(links[i].id.indexOf("header_")!=0)
     				links[i].onclick=function(){return false;};
     				
			}
	</s:if>
	
	<s:if test="%{model.currentState.value=='REJECTED' || model.currentState.value=='NEW'}">
		enableFields();
	</s:if>
	
/*	<s:if test="%{(model.currentState.value=='CREATED' || model.currentState.value=='RESUBMITTED') && model.currentState.nextAction!=''}"> 		
		disableTables();
		disableSelect();
		enableButtons();
	</s:if> */

/*
//	<s:if test="%{hasErrors() && model.currentState.value=='ADMIN_SANCTIONED'}"> 
		for(i=0;i<document.abstractEstimateForm.elements.length;i++){
			document.abstractEstimateForm.elements[i].disabled=true;
			document.abstractEstimateForm.elements[i].readonly=true;
		} 
		disableTables();
		document.abstractEstimateForm.pdfButton.readonly=false;
		document.abstractEstimateForm.pdfButton.disabled=false;
		document.abstractEstimateForm.mSheetPdfButton.readonly=false;
		document.abstractEstimateForm.mSheetPdfButton.disabled=false;
		document.abstractEstimateForm.BOQPdfButton.readonly=false;
		document.abstractEstimateForm.BOQPdfButton.disabled=false;
		document.abstractEstimateForm.closeButton.readonly=false;
		document.abstractEstimateForm.closeButton.disabled=false;
				
		if(document.abstractEstimateForm.viewBudgetFolio!=null){
			document.abstractEstimateForm.viewBudgetFolio.readonly=false;
			document.abstractEstimateForm.viewBudgetFolio.disabled=false;
		}	
		if(document.abstractEstimateForm.depositfolioreportButton!=null){
			document.abstractEstimateForm.depositfolioreportButton.readonly=false;
			document.abstractEstimateForm.depositfolioreportButton.disabled=false;
		}
	</s:if>
*/  
     <s:if test="%{sourcepage=='inbox'}">		
     document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
		enableButtons();
		<s:if test="%{appConfigValuesToSkipBudget.contains(model.type.name) }">
			
			if(dom.get("viewBudgetFolio")!=null)
		 		document.abstractEstimateForm.viewBudgetFolio.style.display="none";
		 	if(dom.get("depositfolioreportButton")!=null) {
	 			document.abstractEstimateForm.depositfolioreportButton.style.display='';
	 			document.abstractEstimateForm.depositfolioreportButton.readonly=false;
				document.abstractEstimateForm.depositfolioreportButton.disabled=false;
			}	
		</s:if>	
		<s:else>
		 	if(dom.get("viewBudgetFolio")!=null)
		 	document.abstractEstimateForm.viewBudgetFolio.style.display='';
		 	if(dom.get("depositfolioreportButton")!=null)
		 		document.abstractEstimateForm.depositfolioreportButton.style.display="none";
 		</s:else>
		
/*		     <s:if test="%{model.currentState.value=='TECH_SANCTION_CHECKED' || 
		     ((model.currentState.value=='CREATED' || 
		     (model.currentState.value=='RESUBMITTED' && 
		     model.currentState.nextAction!='Pending Budgetary Appropriation Check' && model.currentState.nextAction!='Pending Budgetary Appropriation Approval' && model.currentState.nextAction!='Pending Admin Sanction'
		     && model.currentState.nextAction!='Pending Deposit Code Appropriation Check' && model.currentState.nextAction!='Pending Deposit Code Appropriation Approval' && model.currentState.nextAction!='Pending Admin Sanction Check')) 
		     && model.currentState.nextAction!='')}">
		     	disableTables();
				document.abstractEstimateForm.tech_sanction.readonly=false;
				document.abstractEstimateForm.tech_sanction.disabled=false;
				document.abstractEstimateForm.reject.readonly=false;
				document.abstractEstimateForm.reject.disabled=false;
				
			 </s:if> 
			 
			 <s:if test="%{(model.currentState.value=='TECH_SANCTIONED' || model.currentState.value=='REJECTED') 
			 && (model.currentState.nextAction=='Pending Budgetary Appropriation' || model.currentState.nextAction=='Pending Deposit Code Appropriation')}">
		          	disableTables();				
				document.abstractEstimateForm.reject.readonly=false;
				document.abstractEstimateForm.reject.disabled=false;	
				document.abstractEstimateForm.budget_details_save.readonly=false;
				document.abstractEstimateForm.budget_details_save.disabled=false;						
			  </s:if>
			 <s:if test="%{(model.currentState.value=='TECH_SANCTIONED' && 
			 model.currentState.nextAction!='Pending Budgetary Appropriation' && model.currentState.nextAction!='Pending Deposit Code Appropriation'
			 && model.currentState.nextAction!='Pending Admin Sanction Check' && model.currentState.nextAction!='Pending Admin Sanction') || 
			 (model.currentState.value=='RESUBMITTED' && (model.currentState.nextAction=='Pending Budgetary Appropriation Check' || model.currentState.nextAction=='Pending Budgetary Appropriation Approval' || 
			 model.currentState.nextAction=='Pending Deposit Code Appropriation Check' || model.currentState.nextAction=='Pending Deposit Code Appropriation Approval')) 
			 || ((model.currentState.value=='BUDGETARY_APPR_CHECKED' && model.currentState.nextAction=='Pending Budgetary Appropriation Approval') ||
			 (model.currentState.value=='DEPOSIT_CODE_APPR_CHECKED' && model.currentState.nextAction=='Pending Deposit Code Appropriation Approval'))}">
				disableTables();
 				 document.abstractEstimateForm.budget_appropriation.readonly=false;
				 document.abstractEstimateForm.budget_appropriation.disabled=false;
				 document.abstractEstimateForm.reject.readonly=false;
				 document.abstractEstimateForm.reject.disabled=false;
				 
				 if(document.abstractEstimateForm.viewBudgetFolio!=null){
					document.abstractEstimateForm.viewBudgetFolio.readonly=false;
					document.abstractEstimateForm.viewBudgetFolio.disabled=false;
				 }	
				 if(document.abstractEstimateForm.depositfolioreportButton!=null){
					document.abstractEstimateForm.depositfolioreportButton.readonly=false;
					document.abstractEstimateForm.depositfolioreportButton.disabled=false;
				 }	
			</s:if>
				
			 <s:if test="%{model.currentState.value=='TECH_SANCTIONED'}"> 
		         disableTables();
		 		 document.abstractEstimateForm.budget_appropriation.readonly=false;
				 document.abstractEstimateForm.budget_appropriation.disabled=false;
				 document.abstractEstimateForm.reject.readonly=false;
				 document.abstractEstimateForm.reject.disabled=false;
				 document.abstractEstimateForm.financialDetailButton.readonly=false;
				 document.abstractEstimateForm.financialDetailButton.disabled=false;	

			 </s:if>
			 <s:if test="%{model.currentState.value=='BUDGETARY_APPROPRIATION_DONE' || model.currentState.value=='DEPOSIT_CODE_APPR_DONE' || model.currentState.value=='ADMIN_CHECKED'}">
		          		disableTables();
				document.abstractEstimateForm.admin_sanction.readonly=false;
				document.abstractEstimateForm.admin_sanction.disabled=false;
				document.abstractEstimateForm.reject.readonly=false;
				document.abstractEstimateForm.reject.disabled=false;
				 <s:if test="%{appConfigValuesToSkipBudget.contains(model.type.name) }">
				 if(document.abstractEstimateForm.viewBudgetFolio!=null)
		 			document.abstractEstimateForm.viewBudgetFolio.style.display="none";
		 		if(document.abstractEstimateForm.depositfolioreportButton!=null) {
	 				document.abstractEstimateForm.depositfolioreportButton.style.display='';
	 				document.abstractEstimateForm.depositfolioreportButton.readonly=false;
					document.abstractEstimateForm.depositfolioreportButton.disabled=false;
				}	
				 </s:if>	
		  		<s:else>
		 			document.abstractEstimateForm.viewBudgetFolio.style.display='';
		 			if(document.abstractEstimateForm.depositfolioreportButton!=null)
		 				document.abstractEstimateForm.depositfolioreportButton.style.display="none";
		 			
		 		</s:else>
			  </s:if>
			
			  <s:if test="%{model.currentState.value=='ADMIN_SANCTIONED'}">
		          	disableTables();
					if(document.abstractEstimateForm.viewBudgetFolio!=null) {
					document.abstractEstimateForm.viewBudgetFolio.readonly=false;
					document.abstractEstimateForm.viewBudgetFolio.disabled=false;
					}
					if(document.abstractEstimateForm.depositfolioreportButton!=null) {	
						document.abstractEstimateForm.depositfolioreportButton.readonly=false;
						document.abstractEstimateForm.depositfolioreportButton.disabled=false;
					}
			  </s:if> */
			  </s:if>
	/* <s:if test="%{sourcepage=='search' && (model.currentState.value=='FIN_DETAIL_SAVED' || model.currentState.value=='FIN_SANCTIONED' || model.currentState.value=='ADMIN_SANCTIONED')}">
	    		disableTables();
				document.abstractEstimateForm.financialDetailButton.readonly=false;
				document.abstractEstimateForm.financialDetailButton.disabled=false;
     </s:if>	
		
	<s:if test="%{model.id==null && model.currentState.value=='CREATED'}"> 
		document.abstractEstimateForm.cancel.visible=false;
		document.abstractEstimateForm.pdfButton.visible=false;
		document.abstractEstimateForm.mSheetPdfButton.visible=false;
		document.abstractEstimateForm.BOQPdfButton.visible=false;
		document.getElementById('approverCommentsRow').style.display="none";
	</s:if>
	<s:if test="%{model.currentState.nextAction=='Pending Admin Sanction'}"> 
		//enableResolutionFields();
	</s:if>
	<s:if test="%{model.id!=null && (model.currentState.value=='CREATED' || model.currentState.value=='RESUBMITTED')}">
		document.getElementById('approverCommentsRow').style.display='';
	</s:if>
	
	<s:if test="%{sourcepage=='inbox' && model.currentState.value!='ADMIN_SANCTIONED'}" >
	     document.getElementById('approverCommentsRow').style.display='';
	     document.getElementById('approverComments').readonly=false;	
	     document.getElementById('approverComments').disabled=false;
	     
	   
	</s:if>
	
	<s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (model.currentState.value=='ADMIN_SANCTIONED' || model.currentState.value=='CANCELLED'))}"> 
  		document.abstractEstimateForm.docViewButton.readonly=false;
		document.abstractEstimateForm.docViewButton.disabled=false;
  	</s:if>
  	<s:else>
  		document.abstractEstimateForm.docUploadButton.readonly=false;
		document.abstractEstimateForm.docUploadButton.disabled=false;
  	</s:else>
	<s:if test="%{(sourcepage=='inbox' && model.currentState.value!='END') || hasErrors()}">
  		document.abstractEstimateForm.departmentid.readonly=false;
		document.abstractEstimateForm.departmentid.disabled=false;
		document.abstractEstimateForm.designationId.readonly=false;
		document.abstractEstimateForm.designationId.disabled=false;
		document.abstractEstimateForm.approverUserId.readonly=false;
		document.abstractEstimateForm.approverUserId.disabled=false;
		
  	</s:if>  */
<s:if test="%{sourcepage=='search'}">
		//document.getElementById('save').style.visibility='hidden';
               // document.getElementById('submit_for_approval').style.visibility='hidden';            
               document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
               
                document.getElementById('docViewButton').style.visibility='';
                 document.getElementById('history').style.visibility='';
               // document.getElementById('cancel').style.visibility='hidden';
               	bodyOnLoad();
                load(); 
		disableTables();
        enableButtons();
 /*       <s:if test="%{model.currentState!=null && model.currentState.previous!=null && (model.currentState.value=='TECH_SANCTIONED' && 
  model.currentState.nextAction!='Pending Budgetary Appropriation') || 
 (model.currentState.value=='RESUBMITTED' && (model.currentState.nextAction=='Pending Budgetary Appropriation Check' || model.currentState.nextAction=='Pending Budgetary Appropriation Approval'
  || model.currentState.nextAction=='Pending Deposit Code Appropriation Check' || model.currentState.nextAction=='Pending Deposit Code Appropriation Approval')) || 
 (model.currentState.previous.value=='ADMIN_SANCTIONED' || model.currentState.value=='BUDGETARY_APPR_CHECKED' || 
 model.currentState.value=='ADMIN_CHECKED' || model.currentState.value=='BUDGETARY_APPROPRIATION_DONE' || 
 model.currentState.value=='DEPOSIT_CODE_APPR_CHECKED' || model.currentState.value=='DEPOSIT_CODE_APPR_DONE') }">
        if(document.abstractEstimateForm.viewBudgetFolio!=null){
        	document.abstractEstimateForm.viewBudgetFolio.readonly=false;
			document.abstractEstimateForm.viewBudgetFolio.disabled=false;
		}
		</s:if>
			 document.abstractEstimateForm.docViewButton.readonly=false;
			document.abstractEstimateForm.docViewButton.disabled=false;
		 <s:if test="%{appConfigValuesToSkipBudget.contains(model.type.name) }">
		 if(document.abstractEstimateForm.viewBudgetFolio!=null)
		 	document.abstractEstimateForm.viewBudgetFolio.style.display="none";
		 	if(document.abstractEstimateForm.depositfolioreportButton!=null) {
		 		document.abstractEstimateForm.depositfolioreportButton.style.display='';
		 		document.abstractEstimateForm.depositfolioreportButton.readonly=false;
				document.abstractEstimateForm.depositfolioreportButton.disabled=false;
			}
		 </s:if>	
		  <s:else>
		 	document.abstractEstimateForm.viewBudgetFolio.style.display='';
		 	if(document.abstractEstimateForm.depositfolioreportButton!=null) 
		 		document.abstractEstimateForm.depositfolioreportButton.style.display="none";
		 </s:else>
		 
		 <s:if test="%{appConfigValuesToSkipBudget.contains(model.type.name) }">
		 if(document.abstractEstimateForm.depositfolioreportButton!=null) {
		 	document.abstractEstimateForm.depositfolioreportButton.style.display='';
		 	document.abstractEstimateForm.depositfolioreportButton.readonly=false;
			document.abstractEstimateForm.depositfolioreportButton.disabled=false;
		 }
		 </s:if>	*/
  	</s:if>
    function bodyOnLoad() {
        for(var i=0;i<document.forms[0].length;i++) {
      		document.forms[0].elements[i].disabled =true;
      	}	
		enableButtons();
    }
    function load(){
        links=document.abstractEstimateForm.getElementsByTagName("a");
		for(i=0;i<links.length;i++){
		   	if(links[i].id=='addnonSorRow')
		  	   links[i].onclick=function(){return false;};
		}
    }

/*	<s:if test="%{sourcepage=='inbox' && (model.currentState.value=='TECH_SANCTIONED' || model.currentState.value=='REJECTED' )&& 
	model.currentState.nextAction=='Pending Deposit Code Appropriation'}">
		if(document.abstractEstimateForm.viewBudgetFolio!=null)
			document.abstractEstimateForm.viewBudgetFolio.style.display="none";
		if(document.abstractEstimateForm.depositfolioreportButton!=null)
			document.abstractEstimateForm.depositfolioreportButton.style.display="none";
		 multiYearEstimateDataTable.removeListener('cellClickEvent');
		document.abstractEstimateForm.reject.readonly=false;
		document.abstractEstimateForm.reject.disabled=false;			
	</s:if>
	<s:if test="%{sourcepage=='inbox' && (model.currentState.value=='TECH_SANCTIONED' ||model.currentState.value=='REJECTED' 
	|| model.currentState.value=='RESUBMITTED')  && model.currentState.nextAction=='Pending Admin Sanction'}"> 
		//enableResolutionFields();
		document.abstractEstimateForm.admin_sanction.readonly=false;
		document.abstractEstimateForm.admin_sanction.disabled=false;
		document.abstractEstimateForm.reject.readonly=false;
		document.abstractEstimateForm.reject.disabled=false;
		 multiYearEstimateDataTable.removeListener('cellClickEvent');
		 <s:if test="%{appConfigValuesToSkipBudget.contains(model.type.name) }">
			if(document.abstractEstimateForm.viewBudgetFolio!=null)
				document.abstractEstimateForm.viewBudgetFolio.style.display="none";
			if(document.abstractEstimateForm.depositfolioreportButton!=null) {
				document.abstractEstimateForm.depositfolioreportButton.readonly=false;
				document.abstractEstimateForm.depositfolioreportButton.disabled=false;
			}
		 </s:if>
		 <s:else>
		 	document.abstractEstimateForm.viewBudgetFolio.style.display='';
		 	if(document.abstractEstimateForm.depositfolioreportButton!=null) 
		 		document.abstractEstimateForm.depositfolioreportButton.style.display="none";
		 </s:else>
					
	</s:if>
	<s:if test="%{sourcepage=='inbox' && (model.currentState.value=='TECH_SANCTIONED'  ||model.currentState.value=='REJECTED' || 
	model.currentState.value=='RESUBMITTED') && model.currentState.nextAction=='Pending Admin Sanction Check'}">
	  
	 	document.abstractEstimateForm.admin_sanction.readonly=false;
		document.abstractEstimateForm.admin_sanction.disabled=false;
		document.abstractEstimateForm.reject.readonly=false;
		document.abstractEstimateForm.reject.disabled=false;
		 multiYearEstimateDataTable.removeListener('cellClickEvent');
		 <s:if test="%{appConfigValuesToSkipBudget.contains(model.type.name)}">
		 if(document.abstractEstimateForm.viewBudgetFolio!=null){
			document.abstractEstimateForm.viewBudgetFolio.style.display="none";
		 }
		 if(document.abstractEstimateForm.depositfolioreportButton!=null) 
		 	document.abstractEstimateForm.depositfolioreportButton.style.display='';
		</s:if>	
		 <s:else>
		 	document.abstractEstimateForm.viewBudgetFolio.style.display='';
		 	if(document.abstractEstimateForm.depositfolioreportButton!=null) 
		 		document.abstractEstimateForm.depositfolioreportButton.style.display="none";
		 </s:else>
	</s:if> */
enableViewBalanceLink();
hideApproverInfo();	

<s:if test="%{showResolutionDetails==true && sourcepage!='search'}">
          enableResolutionFields();
 </s:if>
</script>
</body>

</html>
