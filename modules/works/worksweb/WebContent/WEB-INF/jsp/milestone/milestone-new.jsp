<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<html>
<title><s:text name='page.title.milestone'/></title>
<body onload="disableHeader();roundWorkOrderVallue();" class="yui-skin-sam">

<script src="<egov:url path='js/works.js'/>"></script>
<script>

function disableHeader(){
	toggleForSelectedFields(true,['status','execDept','estimateNumber','natureOfWork','typeOfWork','subtypeOfWork','description','preparedBy','projectcode','workOrderNumber','contractor','workOrderAmount']);
}

function roundWorkOrderVallue(){
	document.milestoneForm.workOrderAmount.value=roundTo(document.milestoneForm.workOrderAmount.value);
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

	if(dom.get('worksDocUploadButton')!=null){
		dom.get('worksDocUploadButton').disabled=false;
	}

	if(dom.get('docViewButton')!=null){
		dom.get('docViewButton').disabled=false;
	}
}

function validateMilestoneFormAndSubmit() {
    clearMessage('milestoneerror')
	links=document.milestoneForm.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    if(errors) {
        dom.get("milestoneerror").style.display='';
    	document.getElementById("milestoneerror").innerHTML='<s:text name="contractor.validate_x.message" />';
    	return false;
    }
    
    return true;
}

function validateCancel() {
	var msg='<s:text name="milestone.cancel.confirm"/>';
	var code='<s:property value="model.workOrderEstimate.workOrder.workOrderNumber"/>'; 
	if(!confirmCancel(msg,code)) {
		return false;
	}
	else {
		return true;
	}
}

function trim(str) {
        return str.replace(/^\s+|\s+$/g,"");
}

function validate(obj,text){
/*	if(obj!="cancel"){
		if(!validateUser(text))
			return false;
	} */
	if(!validateMilestoneFormAndSubmit())
	  return false;
/*	if(obj=="cancel"){
	   if(!validateCancel())
	      return false;
	}
	if(obj=="reject"){
		var remarks=document.getElementById("approverComments").value;
		if(trim(remarks)==""){
			dom.get("milestoneerror").style.display='';
    		document.getElementById("milestoneerror").innerHTML='<s:text name="milestone.remarks.null" />';
    		return false;
		}
	}*/
	
	enableSelect();
	return true;
}

</script>
<div id="milestoneerror" class="errorstyle" style="display:none;"></div>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:property value="%{model.workOrderEstimate.workOrder.workOrderNumber}"/> &nbsp; <s:actionmessage theme="simple"/>
        	
        </div>
    </s:if>
    <s:form theme="simple" name="milestoneForm" >
    <s:token/>
<s:push value="model">

	
<s:if test="%{model.id!=null}">
	<s:hidden name="id" value="%{id}" id="id"/>
    <s:hidden name="mode" value="%{mode}" id="mode"/>
</s:if> 
<s:else>
    <s:hidden name="id" value="%{null}" id="id" />
</s:else>

<s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage"/>
<s:hidden name="workOrderEstimate" value="%{model.workOrderEstimate.id}" id="workOrderEstimate"/>
<s:hidden name="scriptName" id="scriptName" value="Milestone"></s:hidden>
<s:hidden name="documentNumber" id="docNumber" />

<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	 <%@ include file='milestone-header.jsp'%>
	 <%@ include file='milestone-template.jsp' %>
	 <%@ include file='milestoneActivity.jsp'%>
	 <!--
	 <div id="manual_workflow">
		<%@ include file="../workflow/workflow.jsp"%> 	 
	</div>  
	-->
	<div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div>
		<div class="rbbot2"><div></div></div>
    </div>     
</div>
  </div>
 
</div>
	<div class="buttonholderwk">
		
	  <p>
		<input type="hidden" name="actionName" id="actionName" />					
		<s:if test="%{(sourcepage=='inbox' || model.id==null || model.egwStatus.code=='NEW') && (sourcepage=='inbox' || model.egwStatus==null)}">
		<!--
				<s:iterator value="%{validActions}">
					<s:if test="%{description!=''}">
						<s:if test="%{description=='CANCEL'}">
							<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" 
								name="%{name}" method="cancel" 
								onclick="document.milestoneForm.actionName.value='%{name}';return validate('cancel','%{name}');"/>
						</s:if>								
						<s:elseif test="%{description=='REJECT'}">
							<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" 
								name="%{name}" method="reject" 
								onclick="document.milestoneForm.actionName.value='%{name}';return validate('reject','%{name}');"/>
						</s:elseif>								
						<s:else>
							<s:submit type="submit" cssClass="buttonfinal"
								value="%{description}" id="%{name}" name="%{name}"
								method="save"
								onclick="document.milestoneForm.actionName.value='%{name}';return validate('noncancel','%{name}');" />
						</s:else>
				 	</s:if>
				</s:iterator>
		-->
					<s:submit type="submit" cssClass="buttonfinal"
						value="APPROVE" id="save" name="save"
						method="save"
						onclick="document.milestoneForm.actionName.value='save';return validate('noncancel','save');" />
		</s:if><!--
		<s:if test="%{model.id==null}" >
			<input type="button" class="buttonfinal" value="CLEAR" id="clear" name="clear" onclick="window.open('${pageContext.request.contextPath}/milestone/ajaxMilestone!checkMilestone.action?woEstimateId=<s:property value='%{model.workOrderEstimate.id}'/>','_self');" />&nbsp;
		</s:if>
		-->
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
		<!--
		<s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (egwStatus.code=='APPROVED' || egwStatus.code=='CANCELLED'))}">
  			<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocumentManager(dom.get('docNumber').value);return false;" />
  		</s:if>
  		<s:else>
			<input type="submit" class="buttonadd" value="Upload Document" id="worksDocUploadButton" onclick="showDocumentManager();return false;" />
  		</s:else>
		
	  -->
	  </p>
		
</div> 

</s:push>
</s:form>
 <script type="text/javascript">
 	<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
 		//showElements(['approverCommentsRow']);
 	</s:if>
  	<s:if test="%{sourcepage=='inbox' && (model.egwStatus.code=='CREATED'||model.egwStatus.code =='RESUBMITTED')}">
	      //hideElements(['workflowDetials']);
	      //showElements(['approverCommentsRow']);
	      disableSelect();
	      enableButtons();
	</s:if>
	<s:if test="%{sourcepage=='inbox' && (model.egwStatus.code=='NEW' ||model.egwStatus.code=='REJECTED') }">
	      //showElements(['approverCommentsRow']);
	 </s:if>
	 <s:if test="%{sourcepage!='inbox' && model.id!=null && model.egwStatus.code!='NEW'}">
	 	//hideElements(['workflowDetials']);
	   // hideElements(['approverCommentsRow']);
	 </s:if>
</script>           

</body>
</html>