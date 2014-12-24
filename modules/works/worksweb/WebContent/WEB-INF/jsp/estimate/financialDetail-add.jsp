
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>   
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
<title><s:text name='page.title.financial.detail'/></title>
<body  onload="populateDesignation();disableBudgetFolioReportButton();clearDepositCode();">
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
var abstractestimateId = document.financialDetailForm.estimateId.value;
var functionid  = document.financialDetailForm.function.value;
var functionaryid  = document.financialDetailForm.functionary.value;
var fundid  = document.financialDetailForm.fund.value;
var budgetheadid  = document.financialDetailForm.budgetGroup.value;
var schemeid  = document.financialDetailForm.scheme.value;
var subschemeid  = document.financialDetailForm.subScheme.value;

if(fundid=='-1'){

	     dom.get("financialDetails_error").style.display='';
         dom.get("financialDetails_error").innerHTML='<s:text name="financial.fund.null" />';

		document.financialDetailForm.fund.focus();
}else{
if(functionid=='-1')
{

dom.get("financialDetails_error").style.display='';
         dom.get("financialDetails_error").innerHTML='<s:text name="budgetfolio.function.mandatory" />';

document.financialDetailForm.function.focus();
}else{
if(budgetheadid=='-1'){

dom.get("financialDetails_error").style.display='';
         dom.get("financialDetails_error").innerHTML='<s:text name="budgetfolio.budgetGroup.mandatory" />';

document.financialDetailForm.budgetGroup.focus();
}else{
dom.get("financialDetails_error").style.display='none';
window.open("${pageContext.request.contextPath}/estimate/ajaxFinancialDetail!showBudgetDetails.action?estimateId="+abstractestimateId+"&functionid="+functionid+"&functionaryid="+functionaryid+"&fundid="+fundid+"&budgetheadid="+budgetheadid+"&schemeid="+schemeid+"&subschemeid="+subschemeid,'viewbudget',"width=600,height=300,resizable=0,left=250,top=250");
}
}
}
}
var warnings=new Array();
warnings['improperDepositCodeSelection']='<s:text name="estimate.depositCode.warning.improperDepositCodeSelection"/>'

function warn(type){
    dom.get(type+"Warning").innerHTML=warnings[type]
    dom.get(type+"Warning").style.display='';
    YAHOO.lang.later(3000,null,function(){dom.get(type+"Warning").style.display='none';});
}

function clearDepositCode(){
	<s:if test="%{depositCodeId==-1}">
		document.financialDetailForm.depositCodeSearch.value='';
	</s:if>
}

function disableBudgetFolioReportButton(){
	if(document.getElementById('coa')!=null && document.getElementById('depositCodeId')!=null){
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
    var date=document.financialDetailForm.estimateDate.value;
    populatescheme({fundId:id,estimateDate:date});
    populatesubScheme({schemeId:id,estimateDate:date});
}

function setupSubSchemes(elem){
	var id=elem.options[elem.selectedIndex].value;
    var date=document.financialDetailForm.estimateDate.value;
	populatesubScheme({schemeId:id,estimateDate:date});
}

budgetLoadFailureHandler=function(){
   showMessage('financialDetails_error','Unable to load budget head information');
}

function setupBudgetGroups(elem){
	var id=elem.options[elem.selectedIndex].value;
    //changes for year end process
    //var date=document.financialDetailForm.estimateDate.value;
    <s:if test="%{!appConfigValuesToSkipBudget.contains(abstractEstimate.type.name)}">
    	var date=document.financialDetailForm.financialYearStartDate.value;
    	makeJSONCall(["Text","Value"],'${pageContext.request.contextPath}/estimate/ajaxFinancialDetail!loadBudgetGroups.action',
    	{functionId:id,estimateDate:date},budgetGroupDropdownSuccessHandler,budgetLoadFailureHandler) ;
    </s:if>
}

<!-- on march24 to enable fields before submit -->
function enableFields(){
	for(i=0;i<document.financialDetailForm.elements.length;i++){
	        document.financialDetailForm.elements[i].disabled=false;
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

function validateDataBeforeSave(financialDetailForm) {
	clearMessage('financialDetails_error');
	if(dom.get('function').value =='-1'){
    	showMessage('financialDetails_error','Please select the Function');
    	return false;
    }
     <s:if test="%{!appConfigValuesToSkipBudget.contains(abstractEstimate.type.name)}">
     	if(dom.get('budgetGroup').value =='-1'){
	    	showMessage('financialDetails_error','Please select the Budget Head');
	    	return false;
   		 }
     </s:if>
    var depositCode = '';
    if(financialDetailForm.depositCodeSearch!=null)
    	depositCode=financialDetailForm.depositCodeSearch.value;      
    if (depositCode.length == 0) {
      financialDetailForm.depositCodeId.value = -1;
     }
}

</script>

<s:if test="%{hasErrors()}">
        <div class="errorstyle">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:actionmessage theme="simple"/>
        </div>
    </s:if>
 <s:form  theme="simple" name="financialDetailForm" onsubmit="return validateDataBeforeSave(this);">   
<div class="errorstyle" id="financialDetails_error" style="display:none;"></div>
<div class="formmainbox"><div class="insidecontent">

  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
   	<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                  <div class="headplacer" align="left"><s:text name='page.title.financial.information'/></div></td>
              </tr>
              <tr>
              	<td><s:hidden name="estimateDate" value="%{model.abstractEstimate.estimateDate}"/>
              	<s:hidden name="financialYearStartDate" /></td>
              	<td><s:hidden name="estimateId" value="%{model.abstractEstimate.id}"/></td>
              </tr>
              <tr>
                <td class="greyboxwk"><s:text name='estimate.executing.department'/> : </td>
                <td class="greybox2wk"><span class="bold"><input type="text" name="execDept" id="executingDepartment" value='<s:property value="%{abstractEstimate.executingDepartment.deptName}" />'  cssClass="selectwk" disabled="true"/></span></td>
                <td class="greyboxwk"><s:text name='estimate.financial.name'/> : </td>
                <td class="greybox2wk"><span class="bold"><input type="text" name="name" id="name" value='<s:property value="%{abstractEstimate.name}" />'  cssClass="selectwk" disabled="true"/></span></td>
              </tr>
              <tr>
                <td class="whiteboxwk"><s:text name='estimate.financial.field'/> : </td>
                <td class="whitebox2wk"><span class="bold"><input type="text" id="ward" name="ward" value='<s:property value="%{abstractEstimate.ward.name}" />' class="selectwk" disabled="true"/></span></td>
                <td class="whiteboxwk">&nbsp;</td>
                <td class="whitebox2wk">&nbsp;</td>
              </tr>
              <tr>
                <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name='estimate.financial.fund'/> : </td>
                <td width="21%" class="greybox2wk">
                    <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" onchange="setupSchemes(this);disableBudgetFolioReportButton();"/>
                 	<egov:ajaxdropdown id="schemeDropdown" fields="['Text','Value']" dropdownId='scheme' url='estimate/ajaxFinancialDetail!loadSchemes.action' selectedValue="%{fund.id}"/>
                 </td>
                <td width="15%" class="greyboxwk"><span class="mandatory">*</span><s:text name='estimate.financial.function'/> : </td>
                <s:if test="%{skipBudget==false}">
                <td width="53%" class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" listKey="id" listValue="name" value="%{function.id}" onChange="setupBudgetGroups(this);"/>
                <egov:ajaxdropdown id="budgetGroupDropdown" fields="['Text','Value']" dropdownId='budgetGroup' url='estimate/ajaxFinancialDetail!loadBudgetGroups.action' selectedValue="%{scheme.id}"/></td>
                </s:if>
                <s:else>
                 	<td width="53%" class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" listKey="id" listValue="name" value="%{function.id}" /></td>
                </s:else>
              </tr>
              
              <tr>
                <td width="11%" class="whiteboxwk"><s:text name='estimate.financial.functionary'/> : </td>
                <td width="21%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="functionary" id="functionary" cssClass="selectwk" list="dropdownData.functionaryList" listKey="id" listValue="name" value="%{functionary.id}" /></td>
               
                <s:if test="%{skipBudget==false}">
	                <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name='estimate.financial.budgethead'/> : </td>
	                <td width="53%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="budgetGroup" id="budgetGroup" cssClass="selectwk" list="dropdownData.budgetGroupList" listKey="id" listValue="name" value="%{budgetGroup.id}" />&nbsp;&nbsp;<a href="javascript:openViewBudget();"><img src='${pageContext.request.contextPath}/image/book_open.png' alt="View Data" width="16" height="16" border="0"/></a><a href="javascript:openViewBudget();">View Balance</a>
	                
	                </td>
	            </s:if>
	            <s:else>
	            	<td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.deposit.accountCode" />:</td>
					<td width="21%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="coa" id="coa" cssClass="selectwk" list="dropdownData.coaList"
										listKey="id" listValue='glcode  + " : " + name' value="%{coa.id}" onchange="disableBudgetFolioReportButton();"/></td>
	            </s:else>
              </tr>
              <s:if test="%{skipBudget==true}">
	              <tr>
	                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="estimate.deposit.code" />:</td>
	                <td class="greybox2wk">
	                <div class="yui-skin-sam">
	                <div id="depositCodeSearch_autocomplete">
	                <div><s:textfield id="depositCodeSearch" type="text" name="code" onkeypress="return validateFundSelection()" value="%{abstractEstimate.depositCode.code?abstractEstimate.depositCode.code+'-'+abstractEstimate.depositCode.codeName:''}" class="selectwk"  onblur="disableBudgetFolioReportButton();"/><s:hidden id="depositCodeId" name="depositCodeId" value="%{abstractEstimate.depositCode.id}"/></div>
	                <span id="depositCodeSearchResults"></span>
	                </div>
	                </div>
	                <egov:autocomplete name="depositCodeSearch" width="20" field="depositCodeSearch" url="depositCodeSearch!searchAjax.action?" queryQuestionMark="false" results="depositCodeSearchResults" paramsFunction="depositCodeSearchParameters" handler="depositCodeSearchSelectionHandler" forceSelectionHandler="depositCodeSelectionEnforceHandler"/>
	                <span class='warning' id="improperDepositCodeSelectionWarning"></span>
	                </td> 
	                <td class="greyboxwk"></td>
	                <td class="greybox2wk"></td>
	              </tr>
	          </s:if>
              <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                  <div class="headplacer" align="left"><s:text name='page.title.scheme.information'/></div></td>
              </tr>
              <tr>
                <td class="whiteboxwk"><s:text name='estimate.financial.scheme'/> : </td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="scheme" id="scheme" cssClass="selectwk" list="dropdownData.schemeList" listKey="id" listValue="name" value="%{scheme.id}" onChange="setupSubSchemes(this);"/>
                <egov:ajaxdropdown id="subSchemeDropdown" fields="['Text','Value']" dropdownId='subScheme' url='estimate/ajaxFinancialDetail!loadSubSchemes.action' selectedValue="%{scheme.id}"/></td>
                <td class="whiteboxwk"><s:text name='estimate.financial.subscheme'/> : </td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="subScheme" id="subScheme" cssClass="selectwk" list="dropdownData.subSchemeList" listKey="id" listValue="name" value="%{subScheme.id}" /></td>
              </tr>
              <tr>
                <td colspan="4" class="shadowwk"></td>
              </tr>             
              
    </table>
    <%@ include file='estimate-financialSource.jsp'%>
 	<tr><td>&nbsp;</td></tr>
    	 <%@ include file="workflowApproval.jsp"%>   
  	<div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div>
       </div>	
	<div class="rbbot2"><div></div></div>
    
</div>
  </div>
</div> 
	<div class="buttonholderwk">
			<input type="hidden" name="actionName" id="actionName"/>
			<s:if test="%{(abstractEstimate.currentState.value=='TECH_SANCTIONED' || 
			abstractEstimate.currentState.value=='REJECTED') && 
			(abstractEstimate.currentState.nextAction=='Pending Budgetary Appropriation' || 
			abstractEstimate.currentState.nextAction=='Pending Deposit Code Appropriation')}">
	  			<s:submit value="Save " cssClass="buttonfinal" value="SAVE" id="saveButton" name="saveButton" method="save" onclick="document.financialDetailForm.actionName.value='save'"/>
	  			&nbsp;
	  			<s:submit value="budget_details_save" cssClass="buttonfinal" value="SAVE & SUBMIT" id="saveAndSubmitButton" name="saveAndSubmitButton" method="saveAndSubmit" onclick="document.financialDetailForm.actionName.value='budget_details_save';return validateApprovalUser();"/>
	  			&nbsp;
	  		</s:if>
	  		<s:if test="%{model.id==null}">
				<input type="button" name="button" id="button" value="CLEAR"  class="buttonfinal" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail.action?estimateId=<s:property value='%{abstractEstimate.id}'/>','_self');" />
				&nbsp;
			</s:if>
			<s:if test="%{(model.currentState.value=='TECH_SANCTIONED' && 
			(model.currentState.nextAction!='Pending Budgetary Appropriation'|| model.currentState.nextAction!='Pending Deposit Code Appropriation')) || 
			(model.currentState.value=='RESUBMITTED' && (model.currentState.nextAction=='Pending Budgetary Appropriation Check' || 
			model.currentState.nextAction=='Pending Budgetary Appropriation Approval' || model.currentState.nextAction=='Pending Deposit Code Appropriation Check' || 
			model.currentState.nextAction=='Pending Deposit Code Appropriation Approval')) || model.currentState.value=='ADMIN_SANCTIONED' || 
			model.currentState.value=='BUDGETARY_APPR_CHECKED' || model.currentState.value=='ADMIN_CHECKED' || model.currentState.value=='BUDGETARY_APPROPRIATION_DONE' || 
			model.currentState.value=='DEPOSIT_CODE_APPR_CHECKED' || model.currentState.value=='DEPOSIT_CODE_APPR_DONE'}">
			<s:if test="%{model.abstractEstimate.budgetApprNo!=null && skipBudget!=true}">
					<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/financialDetail!viewBudgetFolio.action?estimateId=<s:property value='%{abstractEstimate.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonadd" value="View Budget Folio" id="viewBudgetFolio" name="viewBudgetFolio"/>    			
				&nbsp;
			</s:if>	  
	  		</s:if> 
	  		<s:if test="%{skipBudget==true}"> 
	  			<input type="button" onclick="return viewDepositReport()" class="buttonadd" value="View Deposit Folio" id="depositfolioreportButton" name="depositfolioreportButton"/>    			
			</s:if>
	  		<input type="submit" name="closebutton" id="closeButton" value="CLOSE"  class="buttonfinal" onclick="window.close();" />
	</div>
	
	<script>
		<s:if test="%{(abstractEstimate.currentState.value=='TECH_SANCTIONED' && 
		abstractEstimate.currentState.nextAction!='Pending Budgetary Appropriation' &&
		 abstractEstimate.currentState.nextAction!='Pending Deposit Code Appropriation') || 
		(abstractEstimate.currentState.value=='RESUBMITTED' && 
		(abstractEstimate.currentState.nextAction=='Pending Budgetary Appropriation Check' || 
		abstractEstimate.currentState.nextAction=='Pending Budgetary Appropriation Approval' || 
		abstractEstimate.currentState.nextAction=='Pending Deposit Code Appropriation Check' || 
		abstractEstimate.currentState.nextAction=='Pending Deposit Code Appropriation Approval')) || 
		abstractEstimate.currentState.value=='BUDGETARY_APPR_CHECKED' || 
		abstractEstimate.currentState.value=='BUDGETARY_APPROPRIATION_DONE' || 
		abstractEstimate.currentState.value=='DEPOSIT_CODE_APPR_CHECKED' || 
		abstractEstimate.currentState.value=='DEPOSIT_CODE_APPR_DONE' || 
		abstractEstimate.currentState.value=='ADMIN_CHECKED' || abstractEstimate.currentState.value=='ADMIN_SANCTIONED'}">
		financialSourceDataTable.removeListener('cellClickEvent');
			for(i=0;i<document.financialDetailForm.elements.length;i++){
	                        document.financialDetailForm.elements[i].disabled=true;
				document.financialDetailForm.elements[i].readonly=true;
			} 
			document.financialDetailForm.closebutton.readonly=false;
			document.financialDetailForm.closebutton.disabled=false;
		</s:if>
		<s:if test="%{appConfigValuesToSkipBudget.contains(abstractEstimate.type.name)}">
				if(document.financialDetailForm.budgetGroup!=null)
					document.financialDetailForm.budgetGroup.disabled=true;
		</s:if>
	</script>
</s:form>

</body>
</html>


