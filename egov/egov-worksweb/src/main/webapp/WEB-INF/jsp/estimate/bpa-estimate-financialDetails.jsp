<script>

function setupBudgetGroups(){
	var	functionId = document.getElementById('function').value;
   	makeJSONCall(["ErrorMsg","Text","Value","Glcode"],'${pageContext.request.contextPath}/estimate/ajaxFinancialDetail!loadBudgetGroupsForBPAEstimate.action',
    	{functionId:functionId},budgetLoadSuccessHandler,budgetLoadFailureHandler) ;
   
}
budgetLoadSuccessHandler=function(req,res){
	budgetGroupDropdown=dom.get("budgetGroup");
	var resLength =res.results.length;
	var dropDownLength = budgetGroupDropdown.length;

	results=res.results;
	var errorMsg =results[0].ErrorMsg;
	if(errorMsg != ''){
		dom.get("worktypeerror").innerHTML=errorMsg;
	    dom.get("worktypeerror").style.display='';
	    return false;
	}
	else{
			budgetGroupDropdown.options[res.results.length] = null;
			dom.get("worktypeerror").innerHTML='';
		    dom.get("worktypeerror").style.display='none';
		if(res.results.length > 1){
			for(i=1;i<res.results.length;i++){
				if(results[0].ErrorMsg == '' && res.results[i].Text != '' && res.results[i].Text != null){
				budgetGroupDropdown.options[i]=new Option(res.results[i].Text,res.results[i].Value);
				if(res.results[i].Value=='null') {
					budgetGroupDropdown.Dropdown.selectedIndex = i;
				}
				budgetGroupDropdown.options[i].Glcode=res.results[i].Glcode;
				}
			}
		
			if(results[0].ErrorMsg == '' && res.results[1].Text != '' && res.results[1].Text != null ){
			while(dropDownLength>resLength)
			{
				budgetGroupDropdown.options[res.results.length+1] = null;
				dropDownLength=dropDownLength-1;
			}
			setSelectedBudgetHead();
			}
		}
	}
}
budgetLoadFailureHandler=function(){
	alert('<s:text name="dw.bpa.budget.group.fetch.failure" />');
}
function setSelectedBudgetHead() {
	if(document.getElementById('budgetGroup'))
		document.getElementById('budgetGroup').value='<s:property value="%{financialDetails[0].budgetGroup.id}" />';
}

function validateFinancialDetails(){
	var fundId = document.getElementById('fund').value;
	var functionId = document.getElementById('function').value;
	var budgetHeadId = document.getElementById('budgetGroup').value;

	if(fundId == -1){
		showMessage('worktypeerror','<s:text name="dw.bpa.fund.mandatory" />');
		return false;
	}
	if(functionId == -1){
		showMessage('worktypeerror','<s:text name="dw.bpa.function.mandatory" />');
		return false;
	}
	if(budgetHeadId == -1){
		showMessage('worktypeerror','<s:text name="dw.bpa.budget.group.mandatory" />');
		return false;
	}
	return true;
}
</script>
<table id="financialDetails" width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="100%" class="headingwk">
				<div class="arrowiconwk">
					<img src="${pageContext.request.contextPath}/image/arrow.gif" />
				</div>
				<div class="headplacer">
					<s:text name="dw.bpa.financial.details.estimate.header" />
				</div>
			</td>
		</tr>
		<tr>
		<td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name='estimate.financial.fund'/> : </td>
                <td width="21%" class="whitebox2wk">
                	<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="financialDetail.fund.id" id="fund" cssClass="selectwk" list="dropdownData.fundList" listKey="id" listValue="name" value="%{financialDetails[0].fund.id}" />
                </td>
         <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name='estimate.financial.function'/> : </td>
	         <td width="53%" class="whitebox2wk">
	         	<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="financialDetail.function.id" id="function" cssClass="selectwk" list="dropdownData.functionList" listKey="id" listValue="name" value="%{financialDetails[0].function.id}" onChange="setupBudgetGroups();"/>
	         	<egov:ajaxdropdown id="budgetGroupDropdown" fields="['Text','Value','Glcode']" dropdownId='budgetGroup' url='estimate/ajaxFinancialDetail!loadBudgetGroups.action' selectedValue="%{scheme.id}"/>
	         </td>
  		</tr>
		
		<tr>
			<td width="20%" class="greyboxwk"><span class="mandatory">*</span><s:text name='estimate.financial.budgethead'/> :</td>
	        <td class="greybox2wk" colspan="3"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="financialDetail.budgetGroup.id" id="budgetGroup" cssClass="selectwk" list="dropdownData.budgetGroupList" listKey="id" listValue="name" value="%{financialDetails[0].budgetGroup.id}"/></td>
		</tr>
		<tr>
               <td  colspan="4" class="shadowwk"> </td>                 
        </tr>
		
</table>
	
