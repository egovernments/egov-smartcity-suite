/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
var BUDGETDETAILLIST='budgetDetailList'
var budgetDetailsTable;

function getSavedData()
{
	
	if(budgetDetailsTable != null){
		element = document.getElementById('budgetDetail_budget');
		deptObj = document.getElementById('budgetDetail_executingDepartment');
		dept=deptObj.options[deptObj.selectedIndex].value;
		id = element.options[element.selectedIndex].value;
		name = element.options[element.selectedIndex].text;
	//	bootbox.alert(name);
	//	bootbox.alert(dept);
		populateFunctions();
		populateBudgetGroup(id);
	}
}

 function populateFunctions()
    {
		element = document.getElementById('budgetDetail_budget');
		deptObj = document.getElementById('budgetDetail_executingDepartment');
		deptId=deptObj.options[deptObj.selectedIndex].value;
		budgetId = element.options[element.selectedIndex].value;
		budgetName = element.options[element.selectedIndex].text;   
		//bootbox.alert(budgetName);
		//bootbox.alert(budgetId);
	//	bootbox.alert(budgetId+"   "+budgetName+" "+deptId);
		populatebudgetDetail_filtered_function({"budgetDetail.budget.id":budgetId,"budgetDetail.budget.name":budgetName,"budgetDetail.executingDepartment.id":deptId});
	}
	function populateBudgetGroup(budgetId){
  		populatebudgetDetail_filtered_budgetGroup({"budgetDetail.budget.id":budgetId});
	}
	
function updateGrid(field,index){
	if(budgetDetailsTable != null){
		len = budgetDetailsTable.getRecordSet().getLength()
		count=0;
		i=0;
		while(count < len){
			element=document.getElementById('budgetDetailList['+i+'].'+field)
			if(element){
				element.selectedIndex = index;
				count++;
			}
			i++;
		}
	}
}
function updateHiddenFields(field,index){
	if(budgetDetailsTable != null){
		len = budgetDetailsTable.getRecordSet().getLength()
		count=0;
		i=0;
		while(count < len){
			element=document.getElementById('budgetDetailList['+i+'].'+field)
			if(element){
				element.value = index;
				count++;
			}
			i++;
		}
	}
}

function setSelectedIndex(key){
	element = document.getElementsByName(key)[0]
	len = element.options.length
	for(i=0;i<len;i++){
		if(element.options[i].value == listValues[key]){ 
			element.options[i].selected = true;
		}
	}
}

function createTextFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:0.00;
		el.innerHTML = "<input type='text' id='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:right;width:90px;'/>";
	}
}

function createTextFieldFormatterOnblur(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:0.00;
		el.innerHTML = "<input type='text' id='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:right;width:90px;'onblur='populateBE(this);'/>";
	}
}

function populateBE(obj)
{
	var temp = obj.value;
	//var currRow=getRowIndex(obj);
	//document.getElementById('beAmounts['+currRow+']').value=obj.value;
	//BELOW IS THE LOGIC FOR 125% OF RE WHICH IS NOW MOVED TO ASSTBUD
	/*var budgetGroup = document.getElementById('budgetDetailList['+currRow+'].budgetGroup.id').options[document.getElementById('budgetDetailList['+currRow+'].budgetGroup.id').selectedIndex].text.split('-')[0];

	var glcodeSubMin = budgetGroup.substring(0,7);
	
	if(glcodeSubMin == "2101001" || glcodeSubMin == "2101002"){
		document.getElementById('beAmounts['+currRow+']').value=obj.value*1.25;
	}else{
		document.getElementById('beAmounts['+currRow+']').value=obj.value;
	}*/
}

function getRowIndex(obj)
{
	var temp =obj.name.split('[');
	var temp1 = temp[1].split(']');
	return temp1[0];
}

function createAnticipatoryFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:0.00;
		el.innerHTML = "<input type='text' id='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:right;width:90px;' onblur='updateREamount(this)'/>";
		
	}
}


function createIdFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:0.00;
		el.innerHTML ="<input type='hidden' id='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"].id' name='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"].id' value='"+value+"'  style='text-align:right;width:90px;' />";
    }
}
function createHiddenTextFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:0.00;
		el.innerHTML = "<input type='hidden' id='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+budgetDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:right;width:90px;'/>";
	}
}


function handleReferenceBudgets(){
	var be = document.getElementById('budget_isbereBE');
	if(be.checked == true){
		document.getElementById('referenceId').disabled = false;
	}
	var re = document.getElementById('budget_isbereRE');
	if(re.checked == true){
		document.getElementById('referenceId').disabled = true;
	}
}
