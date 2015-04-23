function showPropertyHeaderTab(){
		document.getElementById('property_header').style.display='';
		setCSSClasses('propertyHeaderTab','First Active');
		setCSSClasses('objectionDetailTab','');
		setCSSClasses('approvalTab','Last');
		hideObjectionHeaderTab();
		hideApprovalHeaderTab();
		
	}
	function showObjectionHeaderTab(){
		document.getElementById('objection_header').style.display='';
		setCSSClasses('propertyHeaderTab','First BeforeActive');
		setCSSClasses('objectionDetailTab','Active');
		setCSSClasses('approvalTab','Last');
		hidepropertyHeaderTab();
		hideApprovalHeaderTab();
		
	}
	
	function showApprovalTab(){
		document.getElementById('approval_header').style.display='';
		setCSSClasses('propertyHeaderTab','First');
		setCSSClasses('objectionDetailTab','BeforeActive');
		setCSSClasses('approvalTab','Last Active ActiveLast');
		hidepropertyHeaderTab();
		hideObjectionHeaderTab();
  		
	} 
	
	function setCSSClasses(id,classes){
    		 document.getElementById(id).setAttribute('class',classes);
   		    document.getElementById(id).setAttribute('className',classes);
	}
	function hidepropertyHeaderTab(){
			document.getElementById('property_header').style.display='none';
	}
	function hideObjectionHeaderTab(){
			document.getElementById('objection_header').style.display='none';
	}
	function hideApprovalHeaderTab(){
			document.getElementById('approval_header').style.display='none';
			
	}
function checkLength(obj){
	if(obj.value.length>1024)
	{
		alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
}

function validateRecordObjection(obj){
	document.getElementById("workflowBean.actionName").value=obj.value;
	document.getElementById("lblError").style.display='none';
	if(dom.get('recievedOn').value==''){
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML  = "Please enter Objection Received Date/ &#2325;&#2371;&#2346;&#2351;&#2366; &#2310;&#2325;&#2381;&#2359;&#2375;&#2346; &#2346;&#2381;&#2352;&#2366;&#2346;&#2381;&#2340; &#2333;&#2366;&#2354;&#2381;&#2351;&#2366;&#2330;&#2381;&#2351;&#2366; &#2340;&#2366;&#2352;&#2326;&#2375;&#2330;&#2368; &#2344;&#2379;&#2306;&#2342; &#2325;&#2352;&#2366;";
		return false;
	}
	else if(dom.get('recievedBy').value==''){
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML  = "Please enter Objection Received By/ \u0915\u0943\u092a\u092f\u093e \u0906\u0915\u094d\u0937\u0947\u092a \u092f\u093e \u0938\u094d\u0935\u0940\u0915\u093e\u0930 \u0915\u0947\u0932\u0947 \u0928\u094b\u0902\u0926 \u0915\u0930\u093e";
		return false;
	}
	else if(dom.get('details').value==''){
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML  = "Please enter Objection Details/ &#2325;&#2371;&#2346;&#2351;&#2366; &#2310;&#2325;&#2381;&#2359;&#2375;&#2346;&#2366;&#2330;&#2381;&#2351;&#2366; &#2357;&#2367;&#2360;&#2381;&#2340;&#2371;&#2340; &#2357;&#2367;&#2357;&#2352;&#2339;&#2366;&#2330;&#2368; &#2344;&#2379;&#2306;&#2342; &#2325;&#2352;&#2366;";
		return false;
	}else{
		
		return validateApproval(obj);
	}
	
}

function validateHearingDate(obj){
	document.getElementById("lblError").style.display='none';
	document.getElementById("workflowBean.actionName").value=obj.value;
	
	if( dom.get('plannedHearingDt').value == ""){
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML ='Please enter Planned Hearing Date';
		return false;
	}else{
		
		return validateApproval(obj);
	}
	
}

function validateRecordHearing(obj){
	
	document.getElementById("lblError").style.display='none';
	document.getElementById("workflowBean.actionName").value=obj.value;
	
	if( dom.get('hearingDetails').value == ""){
		
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML ='Please enter Hearing Details';
		return false;
	}
	else if(dom.get('inspectionRequiredtrue').checked == false && dom.get('inspectionRequiredfalse').checked == false ){
		
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML ='Please choose if Inspection Required or not';
		return false;
	}
	else {
		return validateApproval(obj);
	}
}


function validateRecordInspection(obj){
	
	document.getElementById("lblError").style.display='none';
	document.getElementById("workflowBean.actionName").value=obj.value;
	
	if( dom.get('inspectionRemarks').value == ""){
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML ='Please enter Inspection Remark';
		return false;
	}else{
		
		return validateApproval(obj);
	}
}

function validateObjectionOutcome(obj){
	document.getElementById("lblError").style.display='none';
	document.getElementById("workflowBean.actionName").value=obj.value;
	
	if(dom.get('objectionRejectedtrue').checked == false && dom.get('objectionRejectedfalse').checked == false ){
		
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML ='Please choose if Objection Accepted or Rejected';
		return false;
	}
	else if(dom.get('dateOfOutcome').value == ''){
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML ='Please enter Outcome Date';
		return false;
	}
	else if(dom.get('outcomeRemarks').value == ''){
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML ='Please enter Outcome Remarks';
		return false;
	} 
	else { 
		return validateApproval(obj);
	}
}

function validateApproval(obj){
	 if(obj.value.toUpperCase()=="REJECT" || obj.value.toUpperCase()=="SAVE" || obj.value.toUpperCase()=="APPROVE")
	{
		return true;
	}
	else if(document.getElementById("approverUserId") && document.getElementById("approverUserId").value=="-1")
	{	
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML ='Please select approver ';
		return false;
	}else
	{
		return true;
	}
	
	
}

function showDocumentManager(){
		var docNum= document.getElementById("docNumber").value;
  		var url;
  		if(docNum==null||docNum==''||docNum=='To be assigned'){
       			 url="/egi/docmgmt/basicDocumentManager.action?moduleName=ptis";
  		} else {
       			 url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+docNum+"&moduleName=ptis";
 		 }
     		 window.open(url,'docupload','width=1000,height=400');
}