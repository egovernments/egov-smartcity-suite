function validateData(){
	if(document.getElementById('asOnDate').value ==''){
		alert("Please enter a valid date")
		return false;
	}
	var asOnDate =  Date.parse(document.getElementById('asOnDate').value);
	if(asOnDate == ''){
		alert("Please enter a valid date")
		return false;
	}
	var recovery =  document.getElementById('recovery').value;
	if(recovery == -1){
		alert("Please select a Recovery Code")
		return false;
	}
	var fund =  document.getElementById('fund').value;
	if(fund == -1){
		alert("Please select a Fund")
		return false;
	}
	return true;	
}
var entitiesArray;
var entities;
function loadEntities(){
	var element = document.getElementById("recovery").value;
	if(element != -1){
		var	url = "/EGF/report/pendingTDSReport!ajaxLoadEntites.action?recoveryId="+element;
		var req2 = initiateRequest();
		req2.onreadystatechange = function(){
		  if (req2.readyState == 4){
			  if (req2.status == 200){
				var entity=req2.responseText;
				var a = entity.split("^");
				var eachEntity = a[0];
				entitiesArray=eachEntity.split("+");
				entities = new YAHOO.widget.DS_JSArray(entitiesArray);
			  }
		  }
	 	};
		req2.open("GET", url, true);
		req2.send(null);
	}
}
var oAutoCompEntity;
function autocompleteEntities(obj,myEvent){
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	var posSrc=findPos(src); 
	target.style.width="600px";	
	var coaCodeObj=obj;
	var key = window.event ? window.event.keyCode : myEvent.charCode;  
	if(key != 40 ){
		if(key != 38 ){
			oAutoCompEntity = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', entities);
			oAutoCompEntity.queryDelay = 0;
			oAutoCompEntity.prehighlightClassName = "yui-ac-prehighlight";
			oAutoCompEntity.useShadow = true;
			oAutoCompEntity.maxResultsDisplayed = 15;
			oAutoCompEntity.useIFrame = true;
			if(entities){
				entities.applyLocalFilter = true;
				entities.queryMatchContains = true;
			}
			oAutoCompEntity.minQueryLength = 0;
			oAutoCompEntity.formatResult = function(oResultData, sQuery, sResultMatch) {
				var data = oResultData.toString();
			    return data.split("`~`")[0];
			};
		}
	}
}

function splitValues(element){
	list = element.value.split("`~`")
	document.getElementById("partyName").value = list[0];
	document.getElementById("detailKey").value = list[1];
}