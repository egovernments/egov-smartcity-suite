function updateGridData(){

	document.getElementById("lblError").innerHTML="";

	if(document.getElementById("fromChqNo").value.trim() == ""){
		document.getElementById("lblError").innerHTML="Please enter from cheque number";
		return false;
	}
	if(document.getElementById("toChqNo").value.trim() == ""){
		document.getElementById("lblError").innerHTML="Please enter to cheque number";
		return false;
	}
	if(document.getElementById("fromChqNo").value.trim().length != document.getElementById("toChqNo").value.trim().length){
		
		document.getElementById("lblError").innerHTML="from cheque number and to cheque number length should be same";
		return false;
	}
	
	if(document.getElementById("receivedDate").value.trim() == ""){
		document.getElementById("lblError").innerHTML="Please enter received date";
		return false;
	}
	var deptSelectedValue = new Array();
	var deptSelectedText = new Array();
	var deptObj = document.getElementById("departmentList");
	
	for(var i=0 ; i<deptObj.length;i++){
		if($("#departmentList option")[i]['selected'] == true){
			deptSelectedValue.push(deptObj.options[i].value);
			deptSelectedText.push(deptObj.options[i].text);
		}
	}
	if(deptSelectedValue == ""){
		document.getElementById("lblError").innerHTML="Please enter select a department";
		return false;
	}
	
// validate invalid cheque range.
	var fromchqNum = parseInt(document.getElementById("fromChqNo").value.trim()*1);
	var tochqNum = parseInt(document.getElementById("toChqNo").value.trim()*1);
	if(fromchqNum >= tochqNum){
		document.getElementById("lblError").innerHTML="from cheque number should be less than to cheque number";
		return false;
	}
   for(var i=0 ; i<deptSelectedValue.length;i++){

	for(var j=0 ; j<chequeRangeArray.length;j++){
		var tokens = chequeRangeArray[j].split("-");
		if( (fromchqNum<parseInt(tokens[0]*1) && tochqNum < parseInt(tokens[0]*1)) || (fromchqNum>parseInt(tokens[0]*1) && fromchqNum > parseInt(tokens[1]*1)) ){
		continue;
	}else if( fromchqNum == parseInt(tokens[0]*1) && tochqNum == parseInt(tokens[1]*1) && deptSelectedValue[i] == tokens[2]){
		
		document.getElementById("lblError").innerHTML="Cheque Range is already assigned for department :"+deptSelectedText[i] ;
		return false;
	}else if(fromchqNum == parseInt(tokens[0]*1) && tochqNum == parseInt(tokens[1]*1)){
		continue;
	}else{
		document.getElementById("lblError").innerHTML="Invalid cheque range" ;
		return false;
	}
		
	}

	}
	

	for(var i=0 ; i<deptSelectedValue.length;i++){
		chequeDetailsGridTable.addRow({SlNo:chequeDetailsGridTable.getRecordSet().getLength()+1});
		document.getElementById(CHQDETAILSLIST+'['+chqDetailsIndex+'].fromChqNo').value =document.getElementById("fromChqNo").value.trim();
		document.getElementById(CHQDETAILSLIST+'['+chqDetailsIndex+'].toChqNo').value =document.getElementById("toChqNo").value.trim();
		document.getElementById(CHQDETAILSLIST+'['+chqDetailsIndex+'].deptName').innerHTML = deptSelectedText[i];
		document.getElementById(CHQDETAILSLIST+'['+chqDetailsIndex+'].receivedDateL').innerHTML = document.getElementById("receivedDate").value;
		document.getElementById(CHQDETAILSLIST+'['+chqDetailsIndex+'].receivedDate').value = document.getElementById("receivedDate").value;
		document.getElementById(CHQDETAILSLIST+'['+chqDetailsIndex+'].nextChqPresent').value ="No";
		document.getElementById(CHQDETAILSLIST+'['+chqDetailsIndex+'].isExhusted').value ="No";
		document.getElementById(CHQDETAILSLIST+'['+chqDetailsIndex+'].isExhustedL').value ="No";
		document.getElementById(CHQDETAILSLIST+'['+chqDetailsIndex+'].isExhustedL').innerHTML ="No";
		document.getElementById(CHQDETAILSLIST+'['+chqDetailsIndex+'].deptId').value =deptSelectedValue[i];
		chqDetailsIndex = chqDetailsIndex +1;
		chequeRangeArray.push(document.getElementById("fromChqNo").value.trim()+"-"+document.getElementById("toChqNo").value.trim()+"-"+deptSelectedValue[i]);
	}
	clearHeaderData();
	return true;
}

function clearHeaderData(){
		
	document.getElementById("fromChqNo").value="";
	document.getElementById("toChqNo").value="";
	document.getElementById("receivedDate").value="";
	var deptObj = document.getElementById("departmentList");
	while (deptObj.selectedIndex != -1 ) { 
		deptObj.options[deptObj.selectedIndex].selected = false; 
		
	 }     
}
// used to check the cheque range overlapping on blur of from cheque and to cheque number in the grid.
function validateCheque(obj){
	document.getElementById("save").disabled=true;
	var count = 0;
	document.getElementById("lblErrorGrid").innerHTML="";
	if(!obj.readOnly){
		var index = obj.id.substring(18,19);// to get index e.g"0" from the string chequeDetailsList[0]
		if(document.getElementById(CHQDETAILSLIST+'['+index+'].fromChqNo').value.length != document.getElementById(CHQDETAILSLIST+'['+index+'].toChqNo').value.length){
			document.getElementById("lblErrorGrid").innerHTML="From Cheque No. and To Cheque No. length should be same";
			return false;
			
		}
		var fromchqNum = document.getElementById(CHQDETAILSLIST+'['+index+'].fromChqNo').value*1;
		var tochqNum = document.getElementById(CHQDETAILSLIST+'['+index+'].toChqNo').value*1;
		var deptId = document.getElementById(CHQDETAILSLIST+'['+index+'].deptId').value;
		var deptName= document.getElementById(CHQDETAILSLIST+'['+index+'].deptName').innerHTML;
		chequeRangeArray.splice(index,1,fromchqNum+"-"+tochqNum+"-"+deptId);
		
		if(parseInt(fromchqNum) >= parseInt(tochqNum)){
			document.getElementById("lblErrorGrid").innerHTML="from cheque number should be less than to cheque number";
			return false;
		}


		for(var j=0 ; j<chequeRangeArray.length;j++){
		var tokens = chequeRangeArray[j].split("-");
		if( (fromchqNum<parseInt(tokens[0]*1) && tochqNum < parseInt(tokens[0]*1)) || (fromchqNum>parseInt(tokens[0]*1) && fromchqNum > parseInt(tokens[1]*1)) ){
		continue;
	}else if( fromchqNum == parseInt(tokens[0]*1) && tochqNum == parseInt(tokens[1]*1) && deptId == tokens[2]*1){
		
		count = count +1;
		if(count >1){
			document.getElementById("lblErrorGrid").innerHTML="Cheque Range is already assigned for department :"+deptName ;
			return false;
		}
		continue;
	}else if(fromchqNum == parseInt(tokens[0]*1) && tochqNum == parseInt(tokens[1]*1)){
		continue;
	}else{
		document.getElementById("lblErrorGrid").innerHTML="Invalid cheque range" ;
		return false;
	}
		
	}

	}
	document.getElementById("save").disabled=false;

    
}
if(!Array.indexOf){
  Array.prototype.indexOf = function(obj){
   for(var i=0; i<this.length; i++){
    if(this[i]==obj){
     return i;
    }
   }
   return -1;
  }
}

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}