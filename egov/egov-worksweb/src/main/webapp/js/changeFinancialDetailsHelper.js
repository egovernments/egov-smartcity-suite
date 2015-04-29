function defaultApproverDept() {
	if(document.getElementById('approverDepartment')!=null){
		document.getElementById('approverDepartment').value=dom.get('loggedInUserDept').value;
		loadDesignationFromMatrix();	
	}
}

function populateApprover()
{
  getUsersByDesignationAndDept();
}

function depositCodeSearchParameters(){
	if(dom.get('fund').value !='-1'){
		return "fundId="+dom.get('fund').value;
    }
}

function loadDesignationFromMatrix(){
	 var dept=dom.get('departmentName').value;
	 var currentState = dom.get('currentState').value;
	 var amountRule =  dom.get('amountRule').value;
	 var additionalRuleValue =  dom.get('additionalRuleValue').value; 
	 var pendingAction=document.getElementById('pendingActions').value;
	 loadDesignationByDeptAndType('ChangeFDHeader',dept,currentState,amountRule,additionalRuleValue,pendingAction);
}

function enableFields(){
	for(i=0;i<document.changeFinancialDetailsForm.elements.length;i++){
	        document.changeFinancialDetailsForm.elements[i].disabled=false; 
	} 
}

function clearHiddenIdIfEmpty(obj)
{
	if(obj.value=='')
		document.getElementById('depositCode').value='';	
}

function validateFundSelection() {
	clearMessage('changeFD_error');
    var fundElem = document.getElementById('fund');
	var fundId = fundElem.options[fundElem.selectedIndex].value;
	if(fundId =='-1'){
    	showMessage('changeFD_error','Please Choose The Fund Before Selecting the Deposit Code.');
    }
}

function setupSubSchemes(elem){
	var id=elem.options[elem.selectedIndex].value;
	populatesubScheme({schemeId:id});
}

function populateIsChecked(obj){
	   if(obj.checked){
		 	obj.value=true;
		 	obj.checked=true;
		}
		else if(!obj.checked){
			dom.get("checkedAll").checked=false;
		 	dom.get("checkedAll").value=false;
		 	obj.value=false;
		 	obj.checked=false;
		}
}
var depositCodeSearchSelectionHandler = function(sType, arguments) { 
    var oData = arguments[2];
    dom.get("depositCode").value = oData[1];
}