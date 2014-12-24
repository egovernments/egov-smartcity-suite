var BUDGETDETAILLIST='budgetDetailList'
var budgetDetailsTable;

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
