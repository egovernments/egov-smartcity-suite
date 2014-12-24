<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>  
<head>  
    <title><s:text name="retention.money.refund.title" /></title>  
</head> 
<script src="<egov:url path='js/works.js'/>"></script>
  <script>
    function getRMOutstanding(){ 
    	if(document.getElementById("glcode").value>0){
    		getRMOutstandingValue();
    	}
    	else{
    	document.getElementById("RMOutstandingAmt").innerHTML='0.0';
    	document.getElementById("retentionMoneyOutstanding").value='0.0';
    	}
    	
    }
   function getRMOutstandingValue(){
   		var myCodeSuccessHandler = function(req,res) {
   			document.getElementById("RMOutstandingAmt").innerHTML=res.results[0].Amount;
   			document.getElementById("retentionMoneyOutstanding").value=res.results[0].Amount;
   		};
            
	    var myCodeFailureHandler = function() {
	        dom.get("sor_error").style.display='';
	        document.getElementById("sor_error").innerHTML='<s:text name="estimate.sor.invalid.sor"/>';
	        document.getElementById("RMOutstandingAmt").innerHTML='0.0';
	        document.getElementById("retentionMoneyOutstanding").value='0.0';
	    };
	makeJSONCall(["Amount"],'${pageContext.request.contextPath}/retentionMoney/retentionMoneyRefund!findRMOutstandingAmountAjax.action',{glcodeId:document.getElementById("glcode").value,workOrderId:document.getElementById("workOrderId").value},myCodeSuccessHandler,myCodeFailureHandler) ;
	        
   }
   
	 
function validateFields(){
	dom.get("rsd_error").style.display='none';     
	dom.get("rsd_error").innerHTML="";
	if(dom.get("glcode").value=='-1'){
    	dom.get("rsd_error").style.display='';     
		dom.get("rsd_error").innerHTML='<s:text name="rmr.glcode.notselected"/>';
		window.scroll(0,0);
		return false;
    }
     if(dom.get("glcode").value!='-1' && dom.get("retentionMoneyOutstanding").value>0 && dom.get("retentionMoneyBeingRefunded").value<=0){
    	dom.get("rsd_error").style.display='';
		dom.get("rsd_error").innerHTML='<s:text name="retention.money.refund.amount.required"/>';
		window.scroll(0,0);
		return false;
    }
    
    if(dom.get("glcode").value!='-1' && dom.get("retentionMoneyOutstanding").value>0 && dom.get("retentionMoneyBeingRefunded").value>dom.get("retentionMoneyOutstanding").value){
    	dom.get("rsd_error").style.display='';
		dom.get("rsd_error").innerHTML='<s:text name="retention.money.refund.incorrect.amount"/>';
		window.scroll(0,0);
		return false;
    }
    
     if(document.getElementById("actionName").value=='Reject'){
       	if(dom.get("remarks").value==""){ 
       		document.retentionMoneyRefund.remarks.readonly=false;
			document.retentionMoneyRefund.remarks.disabled=false;
     	dom.get("rsd_error").style.display='';
		dom.get("rsd_error").innerHTML='<s:text name="rsd.remarks.mandatoryforrejected"/>';
			dom.get("remarks").focus();
		return false;
     	}
   }
    return true;
}

		
function loadDesignationFromMatrix(){  
		var dept=dom.get('departmentName').value;
  		// var dept = dom.get('approverDepartment').value;
  		 var currentState = dom.get('currentState').value;
  		 //var amountRule =  dom.get('amountRule').value;
  		 var amountRule=document.getElementById("retentionMoneyBeingRefunded").value;
  		// var additionalRule =  dom.get('additionalRule').value;
  		 var additionalRuleValue =  dom.get('additionalRuleValue').value; 
  		// var currentDesignation =  dom.get('currentDesignation').value;
  		var pendingAction=document.getElementById('pendingActions').value;
  		 loadDesignationByDeptAndType('RetentionMoneyRefund',dept,currentState,amountRule,additionalRuleValue,pendingAction); 
  		 //loadDesignationByDeptAndType('retentionMoneyRefund','',currentState,amountRule,additionalRule,currentDesignation); 
}

function populateApprover()
{
  getUsersByDesignationAndDept(); 
}

  
function validateCancel() {
	var msg='<s:text name="rmr.cancel.confirm"/>';
	 
	if(!confirmCancel(msg)) {
		return false;
	}
	else {
		return true;
	}
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
	enableSelect(); 
	if(text!='Approve' && text!='Reject'){
		if(!validateWorkFlowApprover(text))
			return false;
	}
	
	return true; 
}

function enableSelect(){
	for(i=0;i<document.retentionMoneyRefund.elements.length;i++){
    	document.retentionMoneyRefund.elements[i].disabled=false;
	}
}
</script>
<body onload="disableForm();load()">

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
    <s:form action="retentionMoneyRefund" theme="simple" name="retentionMoneyRefund" >
    <s:if test="%{sourcepage!='search'}"><s:token/></s:if>
<s:push value="model">
<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
<s:hidden id="oldRetentionMoneyRefundAmount" name="oldRetentionMoneyRefundAmount" value="%{oldRetentionMoneyRefundAmount}" />
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
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="4" class="headingwk">
				<div class="arrowiconwk">
					<img src="${pageContext.request.contextPath}/image/arrow.gif" />
				</div>
				<div class="headplacer"><s:text name="retention.money.refund.header" /></div>
			</td>
		</tr>
      	 	<tr>
				<td class="whiteboxwk"><s:text name="retention.money.refund.workname" />:</td>
          		<td class="whitebox2wk"><s:property value="%{model.workOrder.workOrderEstimates.get(0).estimate.name}" /></td>
          		<td class="whiteboxwk"><s:text name="retention.money.refund.workOrderEstValue" />:</td>
          		<td class="whitebox2wk"><s:textfield readonly="true" name="totalWorkValue"  id="totalWorkValue" align="right"  value="%{totalWorkValue}" /></td>
     		 </tr>
  	    	<tr>
				<td class="greyboxwk"><s:text name="retention.money.refund.workordernum" />:</td>
          		<td class="greybox2wk"><s:property value="%{model.workOrder.workOrderNumber}" /></td>
              	<td class="greyboxwk"><s:text name="retention.money.refund.workorderDate" />:</td>
              	<td class="greybox2wk"><s:date name="workOrder.workOrderDate" var="workOrderDateFormat" format="dd/MM/yyyy"/>
              	<s:property value="%{workOrderDateFormat}" /></td>
     		</tr>	
     		<tr>
				<td class="whiteboxwk"><s:text name="retention.money.refund.contractor" />:</td>
  	        	<td class="whitebox2wk"><s:property value="%{model.contractor.name}" /></td>
      	        <td class="whiteboxwk"><s:text name="retention.money.refund.owner" />:</td>
          	    <td class="whitebox2wk"><s:property value="%{model.workOrder.owner}" /></td>
     		 </tr>
     		 <tr>
				<td class="greyboxwk"><s:text name="work.completionDate" />:</td>
	  	        <td class="greybox2wk"><s:date name="model.workOrder.workOrderEstimates.get(0).workCompletionDate" var="workCompletionDateFormat" format="dd/MM/yyyy"/>
	            <s:property value="%{workCompletionDateFormat}" /></td>
	            <td class="greyboxwk"><s:text name="retention.money.refund.outstandingamount" />:</td>
	          	<td class="greybox2wk"><div id="RMOutstandingAmt"><s:property value="%{model.retentionMoneyOutstanding}" /></div>
	          	<s:hidden  name="retentionMoneyOutstanding" id="retentionMoneyOutstanding" value="%{model.retentionMoneyOutstanding}"/></td>
     		 </tr>		
     		 <tr>
				<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="retention.money.refund.accounthead" />:</td>
	            <td class="whitebox2wk">
	            	<s:select id="glcode" name="glcode" headerKey="-1"  headerValue="%{getText('accounthead.default.select')}"
					cssClass="selectwk" list="%{dropdownData.rmcoaList}" value="%{model.glcode.id}"
					listKey="id" listValue='glcode  + " ~ " + name'
					onchange= "getRMOutstanding()"/></td>
				<td class="whiteboxwk"></td>
				<td class="whitebox2wk"></td>	
           	</tr>
      	 
      		<tr>
				<td class="greyboxwk"><span class="mandatory">*</span><s:text name="retention.money.refund.returnamount" />:</td>
          		<td class="greybox2wk"><s:textfield name="retentionMoneyBeingRefunded"  id="retentionMoneyBeingRefunded"  cssClass="amount" value="%{retentionMoneyBeingRefunded}" /></td>
              	<td class="greyboxwk">                		
              	<s:text name="retention.money.refund.remarks" />:</td>
          		<td class="greybox2wk"><s:textfield name="remarks"  id="remarks"   value="%{remarks}" /></td>
      		</tr>
     </table>
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
					onclick="document.retentionMoneyRefund.actionName.value='Save';return validateForm();" />
				</s:if>
				<s:iterator value="%{getValidActions()}" var="name">
				<s:if test="%{name!=''}">
						<s:submit type="submit" cssClass="buttonfinal"
							value="%{name}" id="%{name}" name="%{name}"
							method="save"
							onclick="document.retentionMoneyRefund.actionName.value='%{name}';return validate('%{name}');" />
				</s:if>
			</s:iterator>  
		</s:if>
					
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="<s:if test="%{sourcepage=='search'}">window.close()</s:if><s:else>confirmClose('<s:text name='retention.money.refund.close.confirm'/>');</s:else>"/>
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
				document.retentionMoneyRefund.closeButton.readonly=false;
				document.retentionMoneyRefund.closeButton.disabled=false;
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
	      	document.retentionMoneyRefund.closeButton.readonly=false;
			document.retentionMoneyRefund.closeButton.disabled=false;	
		</s:if>	
    }
	</SCRIPT>

</body>
</html>