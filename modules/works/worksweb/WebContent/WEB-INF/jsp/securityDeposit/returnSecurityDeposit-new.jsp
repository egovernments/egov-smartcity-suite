<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>  
<head>  
    <title><s:text name="returnsecuritydeposit.title" /></title>  
</head> 
<script src="<egov:url path='js/works.js'/>"></script>
<body onload="init();disableForm();load();">
  <script>
    function getSDAmountdeducted(){ 
    	if(document.getElementById("glcode").value>0){
    		getSDAmountDeductedValue();
    	}
    	else{
    	document.getElementById("SDdeductedAmount").innerHTML='0.0';
    	document.getElementById("securityDepositAmountdeducted").value='0.0';
    	}
    	
    }
   function getSDAmountDeductedValue(){
   		var myCodeSuccessHandler = function(req,res) {
   			document.getElementById("SDdeductedAmount").innerHTML=res.results[0].Amount;
   			document.getElementById("securityDepositAmountdeducted").value=res.results[0].Amount;
   		};
            
	    var myCodeFailureHandler = function() {
	        dom.get("sor_error").style.display='';
	        document.getElementById("sor_error").innerHTML='<s:text name="estimate.sor.invalid.sor"/>';
	        document.getElementById("SDdeductedAmount").innerHTML='0.0';
	        document.getElementById("securityDepositAmountdeducted").value='0.0';
	    };
	makeJSONCall(["Amount"],'${pageContext.request.contextPath}/securityDeposit/returnSecurityDeposit!findSDdeductedAmountAjax.action',{glcodeId:document.getElementById("glcode").value,workOrderId:document.getElementById("workOrderId").value},myCodeSuccessHandler,myCodeFailureHandler) ;
	        
   }
   
	 
function validateFields(){
	dom.get("rsd_error").style.display='none';     
	dom.get("rsd_error").innerHTML="";
	if(dom.get("glcode").value=='-1'){
    	dom.get("rsd_error").style.display='';     
		dom.get("rsd_error").innerHTML='<s:text name="rsd.glcode.notselected"/>';
		window.scroll(0,0);
		return false
    }
     if(dom.get("glcode").value!='-1' && dom.get("securityDepositAmountdeducted").value>0 && dom.get("returnSecurityDepositAmount").value<=0){
    	dom.get("rsd_error").style.display='';
		dom.get("rsd_error").innerHTML='<s:text name="rsd.returnSecurityDepositAmount.required"/>';
		window.scroll(0,0);
		return false
    }
    
    if(dom.get("glcode").value!='-1' && dom.get("securityDepositAmountdeducted").value>0 && dom.get("returnSecurityDepositAmount").value>dom.get("securityDepositAmountdeducted").value){
    	dom.get("rsd_error").style.display='';
		dom.get("rsd_error").innerHTML='<s:text name="rsd.incorrect.returnSecurityDepositAmount"/>';
		window.scroll(0,0);
		return false
    }
    
     if(document.getElementById("actionName").value=='Reject'){
       	if(dom.get("remarks").value==""){ 
       		document.returnSecurityDeposit.remarks.readonly=false;
			document.returnSecurityDeposit.remarks.disabled=false;
     	dom.get("rsd_error").style.display='';
		dom.get("rsd_error").innerHTML='<s:text name="rsd.remarks.mandatoryforrejected"/>';
			dom.get("remarks").focus();
		return false
     	}
   }
    return true;
}

/*
function disableForm(){
	
	 <s:if test="%{sourcepage=='inbox' && model.currentState.value=='NEW'}">
	        toggleFields(false,[]);
	        showElements(['approverCommentsRow']);
	 </s:if>
	 <s:elseif test="%{sourcepage=='inbox' && model.currentState.value!='REJECTED' && model.currentState.value!='END'}">
	        toggleFields(true,['departmentid','designationId','approverUserId','approverComments','id','actionName']);
	      showElements(['approverCommentsRow']);
	     		
	 </s:elseif>
	 <s:elseif test="%{sourcepage=='search' && model.currentState.value=='END'}">
	        toggleFields(true,[]);
	 </s:elseif>
	 <s:if test="%{sourcepage=='inbox'}">
		     <s:if test="%{model.currentState.value=='CREATED' || model.currentState.value=='CHECKED' || model.currentState.value=='RESUBMITTED'}">		       
				document.returnSecurityDeposit.approve.readonly=false;
				document.returnSecurityDeposit.approve.disabled=false;
				document.returnSecurityDeposit.reject.readonly=false;
				document.returnSecurityDeposit.reject.disabled=false;
				document.returnSecurityDeposit.closeButton.readonly=false;
				document.returnSecurityDeposit.closeButton.disabled=false;
			 </s:if>			
		</s:if> 
		}
	*/	
		
function loadDesignationFromMatrix(){  
		var dept=dom.get('departmentName').value;
  		// var dept = dom.get('approverDepartment').value;
  		 var currentState = dom.get('currentState').value;
  		 //var amountRule =  dom.get('amountRule').value;
  		 var amountRule=document.getElementById("returnSecurityDepositAmount").value;
  		// var additionalRule =  dom.get('additionalRule').value;
  		 var additionalRuleValue =  dom.get('additionalRuleValue').value; 
  		// var currentDesignation =  dom.get('currentDesignation').value;
  		var pendingAction=document.getElementById('pendingActions').value;
  		 loadDesignationByDeptAndType('ReturnSecurityDeposit',dept,currentState,amountRule,additionalRuleValue,pendingAction); 
  		 //loadDesignationByDeptAndType('ReturnSecurityDeposit','',currentState,amountRule,additionalRule,currentDesignation); 
}

function populateApprover()
{
  getUsersByDesignationAndDept(); 
}

  
function validateCancel() {
	var msg='<s:text name="rsd.cancel.confirm"/>';
	 
	if(!confirmCancel(msg)) {
		return false;
	}
	else {
		return true;
	}
}
function validateChecklist(){
	var count = parseInt( '<s:property value="%{securityDepositChecklist.size()}" />');
	var i;
	var checkListValue;
	dom.get("rsd_error").style.display='none';     
	dom.get("rsd_error").innerHTML="";
	for	(i=0;i<count;i++)
	{
		checkListValue = document.getElementById(""+i).value;
		if(checkListValue!='Yes' && checkListValue!='YES' && checkListValue!='yes')
		{
	    	dom.get("rsd_error").style.display='';     
			dom.get("rsd_error").innerHTML='<s:text name="rsd.checklist.notselected"/>';
			window.scroll(0,0);
			return false;
		}	
	}
	return true;
}
function validateForm(){
	if(!validateFields()){
	  return false; 
	}
	return true;
}

function validate(text){
	if(document.getElementById("actionName").value=='Cancel'){
		if(!validateCancel()){
			return false;
		}
	}
	if(!validateFields())
	  return false;
	if(text=='Approve' || text=='Forward'){
		if(!validateChecklist())
			return false;
	}
	enableSelect(); 
	if(text!='Approve' && text!='Reject'){
		if(!validateWorkFlowApprover(text))
			return false;
	}
	
	return true; 
}

function enableSelect(){
	for(i=0;i<document.returnSecurityDeposit.elements.length;i++){
    	document.returnSecurityDeposit.elements[i].disabled=false;
	}
}
function setCSSClasses(id,classes){
    document.getElementById(id).setAttribute('class',classes);
    document.getElementById(id).setAttribute('className',classes);

}
function showHeaderTab(){
	  var hiddenid = document.forms[0].id.value;
	  document.getElementById('header_div').style.display='';
	  setCSSClasses('headerTab','First Active');
	  setCSSClasses('checklistTab','Last');
	  hideChecklistTab();
}
function showChecklistTab(){
	$('checkList_div').show();
	document.getElementById('header_div').style.display='none';
	hideHeaderTab();
 	setCSSClasses('headerTab','First');
    setCSSClasses('checklistTab','Last Active ActiveLast');
}
function hideHeaderTab(){
	  document.getElementById('header_div').style.display='none';
}
function hideChecklistTab(){
	  document.getElementById('checkList_div').style.display='none';
}
    </script>
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
    <div class="errorstyle" id="rsd_error" style="display: none;"></div>
    <s:form action="returnSecurityDeposit" theme="simple" name="returnSecurityDeposit" >
    <s:if test="%{sourcepage!='search'}"><s:token/></s:if>
<s:push value="model">
<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
<s:hidden id="oldReturnSecurityDepositAmount" name="oldReturnSecurityDepositAmount" value="%{oldReturnSecurityDepositAmount}" />
<s:if test="%{model.id!=null}">
	<s:hidden name="id" value="%{id}" id="id"/>
	<s:hidden  name="workOrderId" id="workOrderId"/>
    <s:hidden name="mode" value="%{mode}" id="mode"/>
    <s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage"/>
 </s:if>
<s:else>
    <s:hidden name="id" value="%{null}" id="mode" />
 	<s:hidden  name="workOrderId" id="workOrderId"/>
    <s:hidden name="sourcepage" value="%{null}" id="sourcepage"/>
</s:else>
<div class="formmainbox">
<div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>  
    
 <div class="rbcontent2">   

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><div id="header">
				<ul id="Tabs">
					<li id="headerTab" class="First Active"><a id="header_1" href="#" onclick="showHeaderTab();">Header</a></li>
					<li id="checklistTab" class="Last"><a id="header_7" href="#" onclick="showChecklistTab();"><s:text name="returnsecuritydeposit.checklist" /></a></li>
				</ul>
		</div></td>
    </tr>

    <tr>
    	<td>
    		<div id="header_div">
				<%@ include file="returnSecurityDeposit-header.jsp"%>
			</div>	
		</td>		
    </tr>
     <tr>
       	 <td>
			<div id="checkList_div" style="display:none;">
				<%@ include file="returnSecurityDeposit-checkList.jsp"%>
			</div>
       	</td>
     </tr>
	<tr> 
		<td>
			<div id="manual_workflow">
				<c:set var="approverHeadCSS" value="headingwk" scope="request" />
				<c:set var="approverCSS" value="bluebox" scope="request" />
				<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
				<%@ include file="/commons/commonWorkflow.jsp"%>
			</div>
		</td>
    </tr>
	<tr>
		<td  class="shadowwk"></td>
	</tr>	
	<tr>
		<td  class="mandatory" align="right">* <s:text name="message.mandatory" /></td>
	</tr>
</table>

</div>
<div class="rbbot2"><div></div></div>


<div class="buttonholderwk" id="buttons">
					<input type="hidden" name="actionName" id="actionName" />					
					
		
		<s:if test="%{(sourcepage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW'  
			|| model.egwStatus.code=='REJECTED') && mode !='view' || hasErrors() || hasActionMessages()}">
				<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
					<s:submit type="submit" cssClass="buttonfinal"
					value="Save" id="Save" name="Save"
					method="save"
					onclick="document.returnSecurityDeposit.actionName.value='Save';return validateForm();" />
				</s:if>
				<s:iterator value="%{getValidActions()}" var="name">
				<s:if test="%{name!=''}">
						<s:submit type="submit" cssClass="buttonfinal"
							value="%{name}" id="%{name}" name="%{name}"
							method="save"
							onclick="document.returnSecurityDeposit.actionName.value='%{name}';return validate('%{name}');" />
				</s:if>
			</s:iterator>  
		</s:if>
	
	 
					
					<!-- 			
						<s:iterator value="%{validActions}">
							<s:if test="%{description!=''}">
								<s:if test="%{description=='CANCEL'}">
									<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" 
										name="%{name}" method="cancel" 
										onclick="document.returnSecurityDeposit.actionName.value='%{name}';return validate('cancel','%{name}');"/>
							  	</s:if>								
								<s:else>
									<s:submit type="submit" cssClass="buttonfinal"
										value="%{description}" id="%{name}" name="%{name}"
										method="save"
										onclick="document.returnSecurityDeposit.actionName.value='%{name}';return validate('noncancel','%{name}');" />
								</s:else>
							</s:if>
						</s:iterator>
						-->
						<!--  s:submit type="submit" cssClass="buttonfinal" value="SAVE" id="save" name="save" method="save" /  -->
									
					
					<!--   Action buttons have to displayed only if the page is directed from the inbox   -->
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="<s:if test="%{sourcepage=='search'}">window.close()</s:if><s:else>confirmClose('<s:text name='rsd.close.confirm'/>');</s:else>"/>
						</div>
			</s:push>
	
</div>
</div>
</div>
</s:form> 


<SCRIPT type="text/javascript"> 

	function disableForm(){
	 <s:if test="%{(sourcepage=='inbox' && egwStatus.code=='FINANCIALLY_SANCTIONED')}">
	         toggleFields(true,['approverComments']);  
	         showElements(['approverComments']);
	 </s:if>
	 <s:elseif test="%{sourcepage=='inbox' && egwStatus.code=='NEW'}">
	       		toggleFields(false,[]);
			    showElements(['approverComments']);
	 </s:elseif>
	 <s:elseif test="%{sourcepage=='inbox' && egwStatus.code!='REJECTED' && egwStatus.code!='END'}">
	        toggleFields(true,['approverDepartment','approverDesignation','approverPositionId','approverComments']);
	 </s:elseif>
        	
	 <s:if test="%{sourcepage=='inbox'}">
		     <s:if test="%{egwStatus.code=='CREATED' || egwStatus.code=='CHECKED' || egwStatus.code=='RESUBMITTED' || egwStatus.code=='APPROVED' || egwStatus.code=='FINANCIALLY_SANCTIONED'}">		       
				document.returnSecurityDeposit.closeButton.readonly=false;
				document.returnSecurityDeposit.closeButton.disabled=false;
				if(dom.get('Approve')!=null)
					dom.get('Approve').disabled=false;
				if(dom.get('Forward')!=null)
					dom.get('Forward').disabled=false;
				dom.get('Reject').disabled=false;
			 </s:if>			
		</s:if> 
		}
	function load(){
		<s:if test="%{mode=='search' || mode=='view' || sourcepage=='search'}">
	        for(var i=0;i<document.forms[0].length;i++) {
	      		document.forms[0].elements[i].disabled =true;
	      	}
	      	document.returnSecurityDeposit.closeButton.readonly=false;
			document.returnSecurityDeposit.closeButton.disabled=false;	
		</s:if>	
    }
	</SCRIPT>

</body>
</html>