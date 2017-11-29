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
<html>
<title><s:text name='page.title.financial.detail'/></title>
<body  onload="setupBudgetGroups(),setSelectedBudgetHead(),populateDesignation();disableBudgetFolioReportButton();clearDepositCode();loadDepositCode();">
<style>
#warning {
  display:none;
  color:blue;
}
</style>
<script>
jQuery("#loadingMask").remove();
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

function loadDepositCode(){
	<s:if test="%{model.abstractEstimate.depositCode!=null && roadCutDepCodeFlag=='true'}">
		dom.get("fund").value="<s:property value='model.abstractEstimate.depositCode.fund.id' />";
		<s:if test="%{(dropdownData.fundList.size>1 && source=='UpdateFinancialDetail')}" >
			setupSchemes(dom.get("fund"));
		</s:if>
		disableBudgetFolioReportButton();
		dom.get("fund").disabled=true;
		dom.get("depositCodeSearch").disabled=true;
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

function setupBudgetGroups(){
	var	id = document.financialDetailForm.function.options[document.financialDetailForm.function.selectedIndex].value;
    //changes for year end process
    //var date=document.financialDetailForm.estimateDate.value;
    <s:if test="%{!appConfigValuesToSkipBudget.contains(abstractEstimate.natureOfWork.name)}">
    	//var date=document.financialDetailForm.financialYearStartDate.value;
    	makeJSONCall(["Text","Value","Glcode"],'${pageContext.request.contextPath}/estimate/ajaxFinancialDetail!loadBudgetGroups.action',
    	{functionId:id},budgetLoadSuccessHandler,budgetLoadFailureHandler) ;
    </s:if>
}

budgetLoadSuccessHandler=function(req,res){
	budgetGroupDropdown=dom.get("budgetGroup");
	var resLength =res.results.length+1;
	var dropDownLength = budgetGroupDropdown.length;
	for(i=0;i<res.results.length;i++){
		budgetGroupDropdown.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
		if(res.results[i].Value=='null') {
			budgetGroupDropdown.Dropdown.selectedIndex = i;
		}
		budgetGroupDropdown.options[i+1].Glcode=res.results[i].Glcode;
	}
	while(dropDownLength>resLength)
	{
		budgetGroupDropdown.options[res.results.length+1] = null;
		dropDownLength=dropDownLength-1;
	}
	setSelectedBudgetHead();
}
budgetLoadFailureHandler=function(){
	alert('Unable to load budgetGroup');
}
function setSelectedBudgetHead() {
	if(document.getElementById('budgetGroup'))
		document.getElementById('budgetGroup').value='<s:property value="%{budgetGroup.id}" />';
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
	var errors=false;
	if(dom.get('function').value =='-1'){
    	showMessage('financialDetails_error','Please select the Function');
    	return false;
    }
    <s:if test="%{!appConfigValuesToSkipBudget.contains(abstractEstimate.natureOfWork.name)}">
    	if(dom.get('budgetGroup').value =='-1'){
	    	showMessage('financialDetails_error','Please select the Budget Head');
	    	return false;
   		}
    </s:if>
	if (document.financialDetailForm.actionName.value=='budget_details_save') {
		<s:if test="%{!appConfigValuesToSkipBudget.contains(abstractEstimate.natureOfWork.name)}">
	    	var selectedBudgetHead = dom.get("budgetGroup").options[dom.get("budgetGroup").selectedIndex].Glcode;
	    	if (selectedBudgetHead != null && selectedBudgetHead != '-1' 
	    		&& selectedBudgetHead.startsWith('<s:property value="%{budgetHeadGlcode}"/>')) {
	    	    if (dom.get('scheme').value == null || dom.get('scheme').value == '-1') {
	    	    	showMessage('financialDetails_error','<s:text name="mandatory.scheme"/>');
	    		    return false;
	    		}
	    	    if (dom.get('subScheme').value == null || dom.get('subScheme').value == '-1') {
	    	    	showMessage('financialDetails_error','<s:text name="mandatory.subScheme"/>');
	    			return false;
	    		}
	    	}
		</s:if>	
    };
     var links=document.financialDetailForm.getElementsByTagName("span");
     for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    if(errors) {
        dom.get("financialDetails_error").style.display='';
    	document.getElementById("financialDetails_error").innerHTML='<s:text name="contractor.validate_x.message" />';
    	return false;
    }
    if(!validateMultiYearEstimateForm()) {
    	return false;
    } 
    var depositCode = '';
    if(financialDetailForm.depositCodeSearch!=null)
    	depositCode=financialDetailForm.depositCodeSearch.value;      
    if (depositCode.length == 0) {
      financialDetailForm.depositCodeId.value = -1;
     }
	dom.get("fund").disabled=false;
	dom.get("depositCodeSearch").disabled=false;

	jQuery(".commontopyellowbg").prepend('<div id="loadingMask" style="display:none;overflow:none;scroll:none;" ><img src="/egi/images/bar_loader.gif"> <span id="message">Please wait....</span></div>')
    doLoadingMask();
    
}
</script>
<s:form  theme="simple" name="financialDetailForm" onsubmit="return validateDataBeforeSave(this);">   
<s:token/>
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
<div class="errorstyle" id="financialDetails_error" style="display:none;"></div>
<div class="formmainbox"><div class="insidecontent">

  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
   	<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
                  <div class="headplacer" align="left"><s:text name='page.title.financial.information'/></div></td>
              </tr>
              <tr>
              	<td><s:hidden name="estimateDate" value="%{model.abstractEstimate.estimateDate}"/>
              	<s:hidden name="financialYearStartDate" /></td>
              	<td><s:hidden name="estimateId" value="%{model.abstractEstimate.id}"/></td>
              </tr>
              <tr>
                <td class="greyboxwk"><s:text name='estimate.executing.department'/> : </td>
                <td class="greybox2wk"><span class="bold"><input type="text" name="execDept" id="executingDepartment" value='<s:property value="%{abstractEstimate.executingDepartment.deptName}" />'  cssClass="selectwk" size="34" disabled="true"/></span></td>
                <td class="greyboxwk"><s:text name='estimate.financial.name'/> : </td>
                <td class="greybox2wk"><span class="bold"><input type="text" name="name" id="name" value='<s:property value="%{abstractEstimate.name}" />'  cssClass="selectwk" disabled="true"/></span></td>
              </tr>
              <tr>
                <td class="whiteboxwk"><s:text name='estimate.financial.field'/> : </td>
                <td class="whitebox2wk"><span class="bold"><input type="text" id="ward" name="ward" value='<s:property value="%{abstractEstimate.ward.name}" />' class="selectwk" disabled="true"/></span></td>
                <td class="whiteboxwk"><s:text name='estimate.user.department'/> : </td>
                <td class="whitebox2wk"><span class="bold"><input type="text" name="userDept" id="userDept" value='<s:property value="%{abstractEstimate.userDepartment.deptName}" />'  cssClass="selectwk" size="34" disabled="true"/></span></td>
              </tr>
              <tr>
                <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name='estimate.financial.fund'/> : </td>
                <td width="21%" class="greybox2wk">
                	<s:if test="%{(dropdownData.fundList.size==1 && source=='UpdateFinancialDetail')}" >
            			<s:select name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name"  />
                	</s:if>
                	<s:else>
                		<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="fund" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" onchange="setupSchemes(this);disableBudgetFolioReportButton();"/>
	                 	<egov:ajaxdropdown id="schemeDropdown" fields="['Text','Value']" dropdownId='scheme' url='estimate/ajaxFinancialDetail!loadSchemes.action' selectedValue="%{fund.id}"/>
                	</s:else>
                 </td>
                <td width="15%" class="greyboxwk"><span class="mandatory">*</span><s:text name='estimate.financial.function'/> : </td>
                <s:if test="%{skipBudget==false}">
                <td width="53%" class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="function" id="function" cssClass="selectwk" list="dropdownData.functionList" listKey="id" listValue="name" value="%{function.id}" onChange="setupBudgetGroups();"/>
                <egov:ajaxdropdown id="budgetGroupDropdown" fields="['Text','Value','Glcode']" dropdownId='budgetGroup' url='estimate/ajaxFinancialDetail!loadBudgetGroups.action' selectedValue="%{scheme.id}"/></td>
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
	                <td width="53%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="budgetGroup" id="budgetGroup" cssClass="selectwk" list="dropdownData.budgetGroupList" listKey="id" listValue="name" value="%{budgetGroup.id}"/></td>
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
	                <egov:autocomplete name="depositCodeSearch" width="20" field="depositCodeSearch" url="depositCodeSearch!searchDepositCodeAjax.action?" queryQuestionMark="false" results="depositCodeSearchResults" paramsFunction="depositCodeSearchParameters" handler="depositCodeSearchSelectionHandler" forceSelectionHandler="depositCodeSelectionEnforceHandler"/>
	                <span class='warning' id="improperDepositCodeSelectionWarning"></span>
	                </td> 
	                <td class="greyboxwk"></td>
	                <td class="greybox2wk"></td>
	              </tr>
	          </s:if>
              <s:if test="%{skipBudget==false && isPreviousApprAllowed}">
	              <tr>
						<td width="11%" class="greyboxwk"><s:text name="estimate.financial.appr.year" /> :</td>
						<td width="21%" class="greybox2wk">
							<s:radio name="apprYear" list="#{'previous':'Previous financial year','running':'Running financial year'}" value="%{apprYear}"/> 
						</td>
						<td colspan="2"  class="greyboxwk"/>
	              </tr>
	          </s:if>
              <tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
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
 	 	<tr>
     <td>
      <div id="multiyearestimate_details">
           <%@ include file="financialDetail-multiYearEstimate.jsp"%>  
      </div>            
     </td> 
    </tr>            
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
			<s:hidden name="sourcepage" id="sourcepage" value="%{sourcepage}"/>
			<s:hidden name="source" id="source" value="%{source}"/>
			<s:if test="%{((abstractEstimate.egwStatus.code=='TECH_SANCTIONED' || 
			abstractEstimate.egwStatus.code=='REJECTED') && 
			(abstractEstimate.currentState.nextAction=='Pending Budgetary Appropriation' || 
			abstractEstimate.currentState.nextAction=='Pending Deposit Code Appropriation'))  && (sourcepage=='inbox' || model.currentState==null)}">
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
		<s:if test="%{(abstractEstimate.egwStatus.code=='TECH_SANCTIONED' && 
		abstractEstimate.currentState.nextAction!='Pending Budgetary Appropriation' &&
		 abstractEstimate.currentState.nextAction!='Pending Deposit Code Appropriation') || 
		(abstractEstimate.egwStatus.code=='RESUBMITTED' && 
		(abstractEstimate.currentState.nextAction=='Pending Budgetary Appropriation Check' || 
		abstractEstimate.currentState.nextAction=='Pending Budgetary Appropriation Approval' || 
		abstractEstimate.currentState.nextAction=='Pending Deposit Code Appropriation Check' || 
		abstractEstimate.currentState.nextAction=='Pending Deposit Code Appropriation Approval')) || 
		abstractEstimate.egwStatus.code=='BUDGETARY_APPR_CHECKED' || 
		abstractEstimate.egwStatus.code=='BUDGETARY_APPROPRIATION_DONE' || 
		abstractEstimate.egwStatus.code=='DEPOSIT_CODE_APPR_CHECKED' || 
		abstractEstimate.egwStatus.code=='DEPOSIT_CODE_APPR_DONE' || 
		abstractEstimate.egwStatus.code=='ADMIN_CHECKED' || abstractEstimate.egwStatus.code=='ADMIN_SANCTIONED'}">
		financialSourceDataTable.removeListener('cellClickEvent');
		multiYearEstimateDataTable.removeListener('cellClickEvent');
			for(i=0;i<document.financialDetailForm.elements.length;i++){
	                        document.financialDetailForm.elements[i].disabled=true;
				document.financialDetailForm.elements[i].readonly=true;
			} 
			document.financialDetailForm.closebutton.readonly=false;
			document.financialDetailForm.closebutton.disabled=false;
		</s:if>
		<s:if test="%{appConfigValuesToSkipBudget.contains(abstractEstimate.natureOfWork.name)}">
				if(document.financialDetailForm.budgetGroup!=null)
					document.financialDetailForm.budgetGroup.disabled=true;
		</s:if>
	</script>
</s:form>

</body>
</html>


