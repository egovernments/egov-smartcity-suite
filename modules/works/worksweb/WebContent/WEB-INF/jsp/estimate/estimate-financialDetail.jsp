<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>   
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<style>
#warning {
  display:none;
  color:blue;
} 
</style>
<script>

// added by thanooj 22-12-2010
// to validate the view Budget Details Button event.
function openViewBudget(){
	var deptId = document.abstractEstimateForm.executingDepartment.value;
	var boundaryId = document.abstractEstimateForm.wardID.value;
	var finYear  = '<s:property value="%{currentFinancialYearId}"/>';
	var functionid  = document.getElementById('function').value;
	var functionaryid  = document.getElementById('functionary').value;
	var fundid  = document.getElementById('fund').value;
	var budgetheadid  = document.getElementById('budgetGroup').value;
	var schemeid  = document.getElementById('scheme').value;
	var subschemeid  = document.getElementById('subScheme').value;
	clearMessage('financialDetails_error'); 
	clearMessage('worktypeerror');	 	
	if(deptId=='-1'){
		showMessage('worktypeerror','<s:text name="estimate.executingDept.null" />');
		document.getElementById('executingDepartment').focus();
	}else{
		if(fundid=='-1'){
			showMessage('financialDetails_error','<s:text name="financial.fund.null" />');
			document.getElementById('fund').focus();
		}else{
			if(functionid=='-1'){
				showMessage('financialDetails_error','<s:text name="budgetfolio.function.mandatory" />');
				document.getElementById('function').focus();
			}else{
				if(budgetheadid=='-1'){
					showMessage('financialDetails_error','<s:text name="budgetfolio.budgetGroup.mandatory" />');
					document.getElementById('budgetGroup').focus();
				}else{
					dom.get("financialDetails_error").style.display='none';
					window.open("${pageContext.request.contextPath}/estimate/ajaxFinancialDetail!showBudgetDetails.action?deptId="+deptId+"&boundaryId="+boundaryId+"&finYear="+finYear+"&functionid="+functionid+"&functionaryid="+functionaryid+"&fundid="+fundid+"&budgetheadid="+budgetheadid+"&schemeid="+schemeid+"&subschemeid="+subschemeid,'viewbudget',"width=600,height=300,resizable=0,left=250,top=250");
				}
			}
		}
	}
}

function clearDepositCode(){
	<s:if test="%{depositCodeId==-1}">
		document.abstractEstimateForm.depositCodeSearch.value='';
	</s:if>
}

function disableBudgetFolioReportButton(){
	if(document.getElementById('coa')!=null && document.getElementById('depositCodeId')!=null && document.getElementById("depositfolioreportButton")!=null){
		 if(document.getElementById('fund').value==-1 || document.getElementById('coa').value==-1 || document.getElementById('depositCodeSearch').value=="" || document.getElementById('depositCodeId').value==""){
			document.getElementById("depositfolioreportButton").disabled=true;
			document.getElementById("depositfolioreportButton").style.backgroundColor="grey";
		}
		else{
			document.getElementById("depositfolioreportButton").disabled=false;
			document.getElementById("depositfolioreportButton").style.backgroundColor="#33CC00";
		}
	}
}
function viewDepositReport(){
	if(document.getElementById('fund').value==-1){
		  showMessage('financialDetails_error','Please Choose The Fund to view the deposit work folio.');
		  return false;
	}
	if(document.getElementById('coa').value==-1){
		showMessage('financialDetails_error','Please choose an account to view the deposit work folio.');
		return false;
	}
	if(document.getElementById('depositCodeSearch').value==""){
		showMessage('financialDetails_error','Please Enter a deposit code to view the deposit work folio.');
		return false;
	}
	else{
		clearMessage('financialDetails_error');
	 	window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewDepositFolio.action?estimateId=<s:property value='%{abstractEstimate.id}'/>'+
	 	'&fundId='+document.getElementById('fund').value+'&glcodeId='+document.getElementById('coa').value+'&depositCodeId='+document.getElementById('depositCodeId').value+
	 	'&functionId='+document.getElementById('function').value,
	 	 '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
}

function clearFinancialDetails(){
document.getElementById('fund').options.selectedIndex=-1;
}

function setupSchemes(elem){
	clearMessage('financialDetails_error');
    var fundElem = document.getElementById('fund');
	var fundId = fundElem.options[elem.selectedIndex].value;
	var id=elem.options[elem.selectedIndex].value;
    var date=document.abstractEstimateForm.estimateDate.value;
    populatescheme({fundId:id,estimateDate:date});
    populatesubScheme({schemeId:id,estimateDate:date});
}

function setupSubSchemes(elem){
	var id=elem.options[elem.selectedIndex].value;
    var date=document.abstractEstimateForm.estimateDate.value;
	populatesubScheme({schemeId:id,estimateDate:date});
}

budgetLoadFailureHandler=function(){
   showMessage('financialDetails_error','Unable to load budget head information');
}

function setupBudgetGroups(elem){
	var id=elem.options[elem.selectedIndex].value;
    //changes for year end process
    //var date=document.abstractEstimateForm.estimateDate.value;
    if(id!='-1') {
	    var elem = document.getElementById('type');
	    clearMessage('financialDetails_error');
	  	if(elem.options[elem.selectedIndex].value=='-1'){  	
	  	 	showMessage('financialDetails_error','Select the nature of work before adding Financing details');
	  	}
	  	var appConfigValuesToSkipBudget='<s:property value="%{appConfigValuesToSkipBudget}"/>';
	  	appConfigValuesToSkipBudget=appConfigValuesToSkipBudget.split('[');
		appConfigValuesToSkipBudget=appConfigValuesToSkipBudget[1].split(']');
	  	appConfigValuesToSkipBudget=appConfigValuesToSkipBudget[0].split(', ');
	    for(i=0;i<appConfigValuesToSkipBudget.length;i++){
		    if(appConfigValuesToSkipBudget[i]!=elem.options[elem.selectedIndex].text) {
		    	var date=document.abstractEstimateForm.financialYearStartDate.value;
		    	makeJSONCall(["Text","Value"],'${pageContext.request.contextPath}/estimate/ajaxFinancialDetail!loadBudgetGroups.action',
		    	{functionId:id,estimateDate:date},budgetGroupDropdownSuccessHandler,budgetLoadFailureHandler) ;
		    	break;
	   		}
	   	}
	}
	else {
		document.getElementById('budgetGroup').value='-1';
	}
}

<!-- on march24 to enable fields before submit -->
function enableFields(){
	for(i=0;i<document.abstractEstimateForm.elements.length;i++){
	        document.abstractEstimateForm.elements[i].disabled=false;
	} 
}

function validateApprovalUser(){ 
	document.getElementById('approver_error').style.display ='none';
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
}

var depositCodeSearchSelectionHandler = function(sType, arguments) { 
            var oData = arguments[2];
            dom.get("depositCodeId").value = oData[1];
            disableBudgetFolioReportButton();
        }
        
var depositCodeSelectionEnforceHandler = function(sType, arguments) {
    warn('improperDepositCodeSelection');
}

function validateFundSelection() {
	clearMessage('financialDetails_error');
    var fundElem = document.getElementById('fund');
	var fundId = fundElem.options[fundElem.selectedIndex].value;
	if(fundId =='-1'){
    	showMessage('financialDetails_error','Please Choose The Fund Before Selecting the Deposit Code.');
    }

}

function depositCodeSearchParameters(){
	if(dom.get('fund').value !='-1'){
		return "fundId="+dom.get('fund').value;
    }
}

function validateDataBeforeSave(abstractEstimateForm) {
	clearMessage('worktypeerror');
	if(dom.get('fund').value =='-1'){
    	showMessage('worktypeerror','Please Choose the Fund!');
    	return false;
    }
	if(dom.get('function').value =='-1'){
    	showMessage('worktypeerror','Please select the Function');
    	return false;
    }
    var elem = document.getElementById('type');
    var appConfigValuesToSkipBudget='<s:property value="%{appConfigValuesToSkipBudget}"/>';
    appConfigValuesToSkipBudget=appConfigValuesToSkipBudget.split('[');
	appConfigValuesToSkipBudget=appConfigValuesToSkipBudget[1].split(']');
  	appConfigValuesToSkipBudget=appConfigValuesToSkipBudget[0].split(', ');
  	var isSkipBudget=0;
    for(i=0;i<appConfigValuesToSkipBudget.length;i++){
    	if(appConfigValuesToSkipBudget[i]==elem.options[elem.selectedIndex].text) {   	
     		isSkipBudget=1;	
   		}
   	}
   	if(isSkipBudget==0) {
    	document.getElementById('depositCodeSearch').value='';
    	document.getElementById('coa').value='-1';
   		if(dom.get('budgetGroup').value =='-1'){
			showMessage('worktypeerror','Please select the Budget Head');  
	    	return false;
  		}
  	}
  	if(isSkipBudget==1) {
  		dom.get('budgetGroup').value ='-1';
  		if(document.getElementById('coa').value==-1){
			showMessage('financialDetails_error','Please Select Deposit Works Account Code');
			return false;
		}
		if(document.getElementById('depositCodeSearch').value==""){
			showMessage('financialDetails_error','Please Enter Deposit Works Code');
			return false;
		}
  	}
    var depositCode = '';
    if(abstractEstimateForm.depositCodeSearch!=null)
    	depositCode=abstractEstimateForm.depositCodeSearch.value;      
    if (depositCode.length == 0) {
      abstractEstimateForm.depositCodeId.value = -1;
     }
}
</script>
    
<div class="errorstyle" id="financialDetails_error" style="display:none;"></div>
   	<table id="financialDetailTable" width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                  <div class="headplacer" align="left"><s:text name='page.title.financial.information'/></div></td>
              </tr>
              <tr>
              	<s:hidden name="financialYearStartDate" /></td>
              </tr>
              
              <tr>
                <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name='estimate.financial.fund'/> : </td>
                <td width="21%" class="greybox2wk">
                    <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="actionFinancialDetails[0].fund.id" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name" value="%{financialDetails[0].fund.id}" onchange="setupSchemes(this);disableBudgetFolioReportButton();"/>
                 	<egov:ajaxdropdown id="schemeDropdown" fields="['Text','Value']" dropdownId='scheme' url='estimate/ajaxFinancialDetail!loadSchemes.action' selectedValue="%{fund.id}"/>
                 </td>
                <td width="15%" class="greyboxwk"><span class="mandatory">*</span><s:text name='estimate.financial.function'/> : </td>
                
                <td width="53%" class="greybox2wk" id="functionBudget"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="actionFinancialDetails[0].function.id" id="function" cssClass="selectwk" list="dropdownData.functionList" listKey="id" listValue="name" value="%{financialDetails[0].function.id}" onChange="setupBudgetGroups(this);"/>
                <egov:ajaxdropdown id="budgetGroupDropdown" fields="['Text','Value']" dropdownId='budgetGroup' url='estimate/ajaxFinancialDetail!loadBudgetGroups.action' selectedValue="%{scheme.id}"/></td>
              </tr>
               
              <tr>
                <td width="11%" class="whiteboxwk"><s:text name='estimate.financial.functionary'/> : </td>
                <td width="21%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="actionFinancialDetails[0].functionary.id" id="functionary" cssClass="selectwk" list="dropdownData.functionaryList" listKey="id" listValue="name" value="%{financialDetails[0].functionary.id}" /></td>
               
	                <td width="15%" class="whiteboxwk" id="budgetGrpLblBudget" ><span class="mandatory">*</span><s:text name='estimate.financial.budgethead'/> : </td>
	                <td width="53%" class="whitebox2wk" id="budgetGrpBudget" ><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="actionFinancialDetails[0].budgetGroup.id" id="budgetGroup" cssClass="selectwk" list="dropdownData.budgetGroupList" listKey="id" listValue="name" value="%{financialDetails[0].budgetGroup.id}" />
	                &nbsp;&nbsp;<a id="viewBalanceLink"href="javascript:openViewBudget();"><img src='${pageContext.request.contextPath}/image/book_open.png' alt="View Data" width="16" height="16" border="0"/></a><a id="viewBalanceImageLink" href="javascript:openViewBudget();">View Balance</a>
	                </td> 
	            	<td width="11%" class="whiteboxwk" id="coaLblDepositWorks" style="display:none"><span class="mandatory">*</span><s:text name="estimate.deposit.accountCode" />:</td>
					<td width="21%" class="whitebox2wk" id="coaDepositWorks" style="display:none"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="actionFinancialDetails[0].coa.id" id="coa" cssClass="selectwk" list="dropdownData.coaList"
										listKey="id" listValue='glcode  + " : " + name' value="%{financialDetails[0].coa.id}" onchange="disableBudgetFolioReportButton();"/></td>
	          
              </tr>
	              <tr id="depositCodeRow" style="display:none">
	                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.deposit.code" />:</td>
	                <td class="greybox2wk">
	                <div class="yui-skin-sam">
	                <div id="depositCodeSearch_autocomplete">
	                <div><s:textfield id="depositCodeSearch" type="text" name="code" onkeypress="return validateFundSelection()" value="%{depositCode.code?depositCode.code+'-'+depositCode.codeName:''}" class="selectwk"  onblur="disableBudgetFolioReportButton();"/>
	                <s:hidden id="depositCodeId" name="depositCodeId" value="%{depositCode.id}"/></div>
	                <span id="depositCodeSearchResults"></span>
	                </div>
	                </div>
	                <egov:autocomplete name="depositCodeSearch" width="20" field="depositCodeSearch" url="depositCodeSearch!searchAjax.action?" queryQuestionMark="false" results="depositCodeSearchResults" paramsFunction="depositCodeSearchParameters" handler="depositCodeSearchSelectionHandler" forceSelectionHandler="depositCodeSelectionEnforceHandler"/>
	                <span class='warning' id="improperDepositCodeSelectionWarning"></span>
	                </td> 
	                <td class="greyboxwk"></td>
	                <td class="greybox2wk"></td>
	              </tr>
              <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                  <div class="headplacer" align="left"><s:text name='page.title.scheme.information'/></div></td>
              </tr>
              <tr>
                <td class="whiteboxwk"><s:text name='estimate.financial.scheme'/> : </td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="actionFinancialDetails[0].scheme.id" id="scheme" cssClass="selectwk" list="dropdownData.schemeList" listKey="id" listValue="name" value="%{financialDetails[0].scheme.id}" onChange="setupSubSchemes(this);"/>
                <egov:ajaxdropdown id="subSchemeDropdown" fields="['Text','Value']" dropdownId='subScheme' url='estimate/ajaxFinancialDetail!loadSubSchemes.action' selectedValue="%{scheme.id}"/></td>
                <td class="whiteboxwk"><s:text name='estimate.financial.subscheme'/> : </td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="actionFinancialDetails[0].subScheme.id" id="subScheme" cssClass="selectwk" list="dropdownData.subSchemeList" listKey="id" listValue="name" value="%{financialDetails[0].subScheme.id}" /></td>
              </tr>
              <tr>
                <td colspan="4" class="shadowwk"></td>
              </tr>             
              
    </table>
    <%@ include file='estimate-financialSource.jsp'%>
 	<tr><td>&nbsp;</td></tr>

