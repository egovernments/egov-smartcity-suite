<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/egov-authz.tld" prefix="egov-authz" %>


<html>
<title><s:text name='page.title.milestone.template'/></title>
<body onload="populateDesignation();" class="yui-skin-sam">

<script src="<egov:url path='js/works.js'/>"></script>
<script>

function enableFieldsForModify(){
	if(validateModfiy()){
    	id=dom.get('id').value;
    	document.milestoneTemplateForm.action='${pageContext.request.contextPath}/masters/milestoneTemplate!edit.action?id='+id+'&mode=modify&sourcepage=search';
    	document.milestoneTemplateForm.submit();
    }
    else{
		return false;
    }
}

function validateModfiy() {
	var code='<s:property value="%{model.code}"/>';
	var msg='<s:text name="milestone.template.modify.confirm"/>';
	if(!confirmCancel(msg,code)) {
		return false;
	}
	else {
	    return true;
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

function validateMilestoneTemplateFormAndSubmit() {
    clearMessage('milestonetemplateerror')
	links=document.milestoneTemplateForm.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    if(errors) {
        dom.get("milestonetemplateerror").style.display='';
    	document.getElementById("milestonetemplateerror").innerHTML='<s:text name="contractor.validate_x.message" />';
    	return false;
    }
    if(!validateHeaderBeforeSubmit(document.milestoneTemplateForm)){
    	return false;
    }
    
    return true;
}

function validateCancel() {
	var msg='<s:text name="milestone.temmplate.cancel.confirm"/>';
	var code='<s:property value="model.code"/>'; 
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
	if(obj!="cancel"){
		if(!validateUser(text))
			return false;
	}
	if(!validateMilestoneTemplateFormAndSubmit())
	  return false;
	if(obj=="cancel"){
	   if(!validateCancel())
	      return false;
	}
	if(obj=="reject"){
		var remarks=document.getElementById("approverComments").value;
		if(trim(remarks)==""){
			dom.get("milestonetemplateerror").style.display='';
    		document.getElementById("milestonetemplateerror").innerHTML='<s:text name="milestone.temmplate.remarks.null" />';
    		return false;
		}
	}
	enableSelect();
	return true;
}

</script>
<div id="milestonetemplateerror" class="errorstyle" style="display:none;"></div>
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
    <s:form theme="simple" name="milestoneTemplateForm" >
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
<s:hidden name="scriptName" id="scriptName" value="MilestoneTemplate"></s:hidden>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	 <%@ include file='milestoneTemplate-header.jsp'%>
	 <%@ include file='milestoneTemplateActivity.jsp'%>
	 <div id="manual_workflow">
		<%@ include file="../workflow/workflow.jsp"%> 	 
	</div>
	<div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div>
		<div class="rbbot2"><div></div></div>
    </div>     
</div>
  </div>
 
</div>
	<div class="buttonholderwk">
		
	  <p>
		<input type="hidden" name="actionName" id="actionName" />
		<s:if test="%{mode=='modify' && (model.egwStatus.code=='APPROVED' || model.egwStatus.code=='NEW')}">
			<!-- egov authorization tag-->
			<egov-authz:authorize actionName="createMilestoneTemplate">
				<s:submit type="submit" cssClass="buttonfinal"
					value="SAVE" id="save" name="save"
					method="save"
					onclick="document.milestoneTemplateForm.actionName.value='save';return validate('noncancel','save');" />
				<s:submit type="submit" cssClass="buttonfinal"
					value="SAVE & SUBMIT" id="submit_for_approval" name="submit_for_approval"
					method="save"
					onclick="document.milestoneTemplateForm.actionName.value='submit_for_approval';return validate('noncancel','submit_for_approval');" />
			</egov-authz:authorize>
		</s:if>
		<s:elseif test="%{(mode!='view') && (sourcepage=='inbox' || model.egwStatus==null || hasErrors())}">
				<s:iterator value="%{validActions}">
					<s:if test="%{description!=''}">
						<s:if test="%{description=='CANCEL'}">
							<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" 
								name="%{name}" method="cancel" 
								onclick="document.milestoneTemplateForm.actionName.value='%{name}';return validate('cancel','%{name}');"/>
						</s:if>								
						<s:elseif test="%{description=='REJECT'}">
							<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" 
								name="%{name}" method="reject" 
								onclick="document.milestoneTemplateForm.actionName.value='%{name}';return validate('reject','%{name}');"/>
						</s:elseif>								
						<s:else>
							<s:submit type="submit" cssClass="buttonfinal"
								value="%{description}" id="%{name}" name="%{name}"
								method="save"
								onclick="document.milestoneTemplateForm.actionName.value='%{name}';return validate('noncancel','%{name}');" />
						</s:else>
				 	</s:if>
				</s:iterator>
		</s:elseif>
 		<s:if test="%{mode=='view' && sourcepage=='search' && model.egwStatus.code=='APPROVED'}">
 			<egov-authz:authorize actionName="createMilestoneTemplate">
				<input type="button" class="buttonfinal" value="MODIFY" id="modifyButton" name="button" onclick="enableFieldsForModify()"/>&nbsp;
			</egov-authz:authorize>
		</s:if>  	
		<s:if test="%{model.id==null}" >
			<input type="button" class="buttonfinal" value="CLEAR" id="clear" name="clear" onclick="this.form.reset();">&nbsp;
		</s:if>
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
	  </p>
		
</div> 

</s:push>
</s:form>
 <script type="text/javascript">
  <s:if test="%{mode=='view'}">
		disableSelect();
		enableButtons();
  </s:if>
  
  	<s:if test="%{sourcepage=='inbox' && (model.egwStatus.code=='CREATED'||model.egwStatus.code =='RESUBMITTED')}">
	      hideElements(['workflowDetials']);
	      showElements(['approverCommentsRow']);
	      disableSelect();
	      enableButtons();
	</s:if>
	<s:if test="%{sourcepage=='inbox' && (model.egwStatus.code=='NEW' ||model.egwStatus.code=='REJECTED') }">
	      showElements(['approverCommentsRow']);
	 </s:if>
	 <s:if test="%{sourcepage!='inbox' && model.id!=null && model.egwStatus.code!='NEW'}">
	 	hideElements(['workflowDetials']);
	    hideElements(['approverCommentsRow']);
	 </s:if>
	 <s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
	 	showElements(['approverCommentsRow']);
	 </s:if>
	 <s:if test="%{mode=='modify' && (model.egwStatus.code=='NEW' || model.egwStatus.code=='APPROVED')}">
	 	showElements(['workflowDetials']);
	 	showElements(['approverCommentsRow']);
	 </s:if>
</script>           

</body>
</html>