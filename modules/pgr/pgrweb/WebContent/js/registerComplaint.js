function loadTitle(type){
	
	resetComplaintTypes(type);
	
	if(type != null && type.selectedIndex != 0){
		dom.get('title').value = type.options[type.selectedIndex].text ;
	}else{
		dom.get('title').value = "" ;
	}
}

function resetComplaintTypes(type){
	for (var i=0;i<document.forms[0].complaintType.length;i++){
		if(document.forms[0].complaintType[i].id!=type.id){
			document.forms[0].complaintType[i].selectedIndex=0;		
		}
	}
}

function receivingCenterEvent(mode){
	if(mode.value == 3){
		dom.get('receivingCenter').disabled = false;
		dom.get('receivingCenterMandate').style.visibility = "visible";
	}
	else{
		dom.get('receivingCenter').disabled = true;
		dom.get('receivingCenterMandate').style.visibility = "hidden";
	}
}

function validate(){

	dom.get('lblError').innerHTML ="";

	if(document.getElementByName('compReceivingModes')[1].checked &&  dom.get('receivingCenter').value == -1){
		dom.get('jserrorid').style.display='block';
		dom.get('lblError').innerHTML ="Please select Complaint Receiving Center";
		return false;
	}
	else if(dom.get('title').value == ""){
		dom.get('jserrorid').style.display='block';
		dom.get('lblError').innerHTML ="Please enter complaint title";
		return false;
	}
	else if(dom.get('details').value == ""){
		dom.get('jserrorid').style.display='block';
		dom.get('lblError').innerHTML ="Please enter complaint details";
		return false;
	}
	else if(dom.get('firstName').value == ""){
		dom.get('jserrorid').style.display='block';
		dom.get('lblError').innerHTML ="Please enter  first name";
		return false;
	}
	else if(dom.get('address').value == ""){
		dom.get('jserrorid').style.display='block';
		dom.get('lblError').innerHTML ="Please enter address";
		return false;
	}
	else{
		dom.get('jserrorid').style.display='none';
		dom.get('lblError').innerHTML ="";
		return true;
	}
	
	return true;
	
}