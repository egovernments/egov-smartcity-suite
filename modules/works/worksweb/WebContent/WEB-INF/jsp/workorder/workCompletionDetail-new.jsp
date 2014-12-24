<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<title><s:text name="page.title.workorderCompletionExtension" /></title>
	
	<body onload="init();" class="yui-skin-sam">

<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
<script>

function init(){
	disableHeader();

}

	function disableHeader(){
		toggleForSelectedFields(true,['status','execDept','estimateNumber','typeOfWork','subtypeOfWork','workName','projectcode','workOrderNumber','contractor']);
	}
function disableFields(){
	for(i=0;i<document.workCompletionForm.elements.length;i++){
	   	document.workCompletionForm.elements[i].disabled=true;
		document.workCompletionForm.elements[i].readonly=true;
	}
}
var test;
function enableFields(actionNameVar){
  test=String(actionNameVar).toLowerCase();
	for(i=0;i<document.workCompletionForm.elements.length;i++){
       	document.workCompletionForm.elements[i].disabled=false;
       	document.workCompletionForm.elements[i].readonly=false;	       
	}
}	
function enableButtons(){

	if(dom.get('saveButton')!=null){
		dom.get('saveButton').disabled=false;
	}
	if(dom.get('modifyButton')!=null){
		dom.get('modifyButton').disabled=false;
	}
	if(dom.get('clear')!=null){
		dom.get('clear').disabled=false;
	}

	if(dom.get('closeButton')!=null){
		dom.get('closeButton').disabled=false;
	}

	if(dom.get('backButton')!=null){
		dom.get('backButton').disabled=false;
	}

	if(dom.get('historyButton')!=null){
		dom.get('historyButton').disabled=false;
	}

	if(dom.get('submit_for_approval')!=null){
		dom.get('submit_for_approval').disabled=false;
	}
	if(dom.get('approval')!=null){
		dom.get('approval').disabled=false;
	}
	if(dom.get('reject')!=null){
		dom.get('reject').disabled=false;
	}
	if(dom.get('save')!=null){
		dom.get('save').disabled=false;
	}
	if(dom.get('departmentid')!=null){
		dom.get('departmentid').disabled=false;
	}
	if(dom.get('approverComments')!=null){
		dom.get('approverComments').disabled=false;
	}
	if(dom.get('approverUserId')!=null){
		dom.get('approverUserId').disabled=false;
	}
	if(dom.get('designationId')!=null){
		dom.get('designationId').disabled=false;
	}
}

function validateWorkCompletionFormAndSubmit() {
	 clearMessage('workCompletionerror')
	links=document.workCompletionForm.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    var workCompletionDate = document.workCompletionForm.workCompletionDate.value;
    if (workCompletionDate == '') {    	
    	dom.get("workCompletionerror").style.display='';     
    	document.getElementById("workCompletionerror").innerHTML='<s:text name="workCompletionDate.null" />';
    	window.scroll(0,0);
    	return false;
    }
    if(!checkDateFormat(dom.get("workCompletionDate"))){
  		dom.get("workCompletionerror").innerHTML='<s:text name="invalid.fieldvalue.workCompletionDate"/>'; 
         dom.get("workCompletionerror").style.display='';
         window.scroll(0,0);
 	 	return false;
 	}
    	var sdate = document.getElementById('origWorkCompletionDate').value;
		if(compareDate(formatDate6(sdate),formatDate6(workCompletionDate)) == -1)
		{
			dom.get("workCompletionerror").style.display='';
			document.getElementById("workCompletionerror").innerHTML='<s:text name="workCompletionDate.validate" /><br>';
			window.scroll(0,0);
			return false;
		}
    if(errors) {
        dom.get("workCompletionerror").style.display='';
    	document.getElementById("workCompletionerror").innerHTML='<s:text name="contractor.validate_x.message" />';
    	return false;
    }
	 return true;
	
}

function validateCancel() {
	var msg='<s:text name="workCompletion.cancel.confirm"/>';
	var code='<s:property value="model.code"/>'; 
	if(!confirmCancel(msg,code)) {
		return false;
	}
	else {
		return true;
	}
}
function validate(text){

	if(!validateWorkCompletionFormAndSubmit())
	  return false;
	if(text!='Approve' && text!='Reject' ){
		if(!validateWorkFlowApprover(text,"workCompletionerror"))
			return false;
	}
	if(document.getElementById("actionName").value=='Cancel'){
	   if(!validateCancel())
	      return false;
	}
	
	if(text=="Reject"){
		var remarks=document.getElementById("approverComments").value;
		if(trim(remarks)==""){
			dom.get("workCompletionerror").style.display='';
    		document.getElementById("workCompletionerror").innerHTML='<s:text name="workCompletion.remarks.null" />';
    		return false;
		}
	}
	enableFields(text);
	return true;
}
function isvalidFormat(obj)
 {
 	if(!checkDateFormat(obj)){
 		dom.get("workCompletionerror").innerHTML='<s:text name="invalid.fieldvalue.workCompletionDate"/>'; 
        dom.get("workCompletionerror").style.display='';
        window.scroll(0,0);
	 	return false;
 	}
 	dom.get("workCompletionerror").style.display='none';
	dom.get("workCompletionerror").innerHTML='';
	return true;
 }
 function isNumberKey(evt)
      {
            evt = (evt) ? evt : event;
        var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode :
           ((evt.which) ? evt.which : 0));

        if(charCode==46)
			return true;
         if (charCode > 31 && (charCode < 48 || charCode > 57))
            return false;

         return true;
      }
      
      function loadDesignationFromMatrix(){  
	  		 var currentState = dom.get('currentState').value;
  		 var amountRuleValue =  dom.get('amountRule').value;
  		 var additionalRuleValue =  dom.get('additionalRule').value;
  		 var pendingAction=document.getElementById('pendingActions').value;
  		   		 var dept=dom.get('departmentName').value;
  		 loadDesignationByDeptAndType('WorkCompletionDetail','',currentState,amountRuleValue,additionalRuleValue,pendingAction); 
}
function populateApprover()
{
  getUsersByDesignationAndDept();
}
</script>
<div id="workCompletionerror" class="errorstyle" style="display:none;"></div>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:property value="%{code}"/> &nbsp; <s:actionmessage theme="simple"/>
        	
        </div>
    </s:if>
    <s:form theme="simple" name="workCompletionForm" >
    <s:token/>
<s:push value="model">
<s:if test="%{model.id!=null}">
	<s:hidden name="id" value="%{id}" id="id"/>
    <s:hidden name="mode" value="%{mode}" id="mode"/>
</s:if> 
<s:else>
    <s:hidden name="id" value="%{null}" id="mode" />
</s:else>
<s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage"/>
<s:hidden name="scriptName" id="scriptName" value="WorkCompletionDetail"></s:hidden>
<s:hidden name="stateValue" id="stateValue" value="%{stateValue}" />
<s:hidden name="mode" value="%{mode}" id="mode"/>
<s:hidden id="workOrder" name="workOrderId" value="%{workOrder.id}"/>
<s:hidden id="workOrder1" name="workOrder" value="%{workOrder.id}"/>
<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
<s:hidden name="model.documentNumber" id="docNumber" />
<s:date name="workOrder.workCompletionDate" var="origWorkCompletionDateFormat" format="dd/MM/yyyy" />
<s:hidden name="origWorkCompletionDate" id="origWorkCompletionDate" value="%{origWorkCompletionDateFormat}"/>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	 <%@ include file='workCompletionDetail-header.jsp'%>
	 <%@ include file='workCompletionDetail-details.jsp'%>
	<s:if test="%{mode!='view'}">
	 
						    	<div id="manual_workflow">
				         			<c:set var="approverHeadCSS" value="headingwk" scope="request" />
				         			<c:set var="approverCSS" value="bluebox" scope="request" />
									<%@ include file="/commons/commonWorkflow.jsp"%>
				  				</div>
       
	</s:if>
	<div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div>
		<div class="rbbot2"><div></div></div>
    </div>     
</div>
  </div>
 
</div>
	<div class="buttonholderwk">
		
	  <p>
		<input type="hidden" name="actionName" id="actionName" />	
		
		<s:if test="%{(sourcepage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW'  
			|| model.egwStatus.code=='REJECTED') && (mode==null || mode !='view') || hasErrors() || hasActionMessages()}">
				<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
					<s:submit type="submit" cssClass="buttonfinal"
					value="Save" id="Save" name="Save"
					method="save"
					onclick="document.workCompletionForm.actionName.value='Save';return validate('Save');" />
				</s:if>
				<s:iterator value="%{getValidActions()}" var="name">
				<s:if test="%{name!=''}">
						<s:submit type="submit" cssClass="buttonfinal"
							value="%{name}" id="%{name}" name="%{name}"
							method="save"
							onclick="document.workCompletionForm.actionName.value='%{name}';return validate('%{name}');" />
				</s:if>
			</s:iterator>  
		</s:if>
		<s:if test="%{model.id==null && mode!='view'}" >
			<input type="button" class="buttonfinal" value="CLEAR" id="clear" name="clear" onclick="this.form.reset();">&nbsp;
		</s:if>
		<input type="submit" class="buttonadd" value="Upload Document" id="wcdDocUploadButton" onclick="showDocumentManager();return false;" />
						
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
		
	  </p>
		
</div> 

</s:push>
</s:form>
      <script>
      function disableForm(){
		    <s:if test="%{(sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED')}" >
		        for(i=0;i<document.workCompletionForm.elements.length;i++){
			        	document.workCompletionForm.elements[i].disabled=true;
						document.workCompletionForm.elements[i].readonly=true;
				} 
				links=document.workCompletionForm.getElementsByTagName("a");  
				for(i=0;i<links.length;i++){   
					if(links[i].id.indexOf("header_")!=0)
     					links[i].onclick=function(){return false;};
				}
		    </s:if>
	    }
	    <s:if test="%{sourcepage=='inbox'}">
		     <s:if test="%{model.currentState.value=='CREATED' || model.currentState.value=='CHECKED' || model.currentState.value=='RESUBMITTED'}">
		     	disableForm();
				if(document.getElementById("Approve")!=null){
					document.workCompletionForm.Approve.disabled=false;
				}
				if(document.getElementById("approverPositionId")!=null){
					document.workCompletionForm.approverPositionId.disabled=false;
				}
				if(document.getElementById("approverDesignation")!=null){
					document.workCompletionForm.approverDesignation.disabled=false;
				}
				if(document.getElementById("approverDepartment")!=null){
					document.workCompletionForm.approverDepartment.disabled=false;
				}
				if(document.getElementById("Reject")!=null){
					document.workCompletionForm.Reject.disabled=false;
				}
				if(document.getElementById("Forward")!=null){
					document.workCompletionForm.Forward.disabled=false;
				}
				
				if(document.getElementById("approverComments")!=null){
					document.workCompletionForm.approverComments.disabled=false;
				}
				
				
				document.workCompletionForm.closeButton.readonly=false;
				document.workCompletionForm.closeButton.disabled=false;
				document.workCompletionForm.wcdDocUploadButton.disabled=false;
				document.workCompletionForm.wcdDocUploadButton.disabled=false;
				
				
			 </s:if>
			 <s:elseif test="%{model.currentState.value=='APPROVED' || model.currentState.value=='CANCELLED'}">
				disableForm();
				document.workCompletionForm.closeButton.readonly=false;
				document.workCompletionForm.closeButton.disabled=false;
				document.workCompletionForm.wcdDocUploadButton.disabled=false;
				document.workCompletionForm.wcdDocUploadButton.disabled=false;
			 </s:elseif>
			 <s:if test="%{model.currentState.value!='END' || hasErrors()}">
			 if(document.getElementById("approverPositionId")!=null){
		  		document.workCompletionForm.approverPositionId.readonly=false;
				document.workCompletionForm.approverPositionId.disabled=false;
			}
			if(document.getElementById("approverDesignation")!=null){
				document.workCompletionForm.approverDesignation.readonly=false;
				document.workCompletionForm.approverDesignation.disabled=false;
			}
			if(document.getElementById("approverDepartment")!=null){
				document.workCompletionForm.approverDepartment.readonly=false;
				document.workCompletionForm.approverDepartment.disabled=false;
			}
		  	</s:if>
			 <s:if test="%{model.id!=null && (model.currentState.value=='CREATED' || model.currentState.value=='CHECKED' || model.currentState.value=='RESUBMITTED')}">
				document.getElementById('approverComments').readonly=false;	
	     		document.getElementById('approverComments').disabled=false;	
			 </s:if>
		
		</s:if>	
		
      </script>    

</body>
</html>


	 