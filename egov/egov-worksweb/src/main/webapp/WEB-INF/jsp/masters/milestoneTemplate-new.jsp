<!-- -------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %> 

<html>
<title><s:text name='page.title.milestone.template'/></title>
<body onload="populateDesignation();" class="yui-skin-sam">

<script src="<egov:url path='resources/js/works.js'/>"></script>
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
<div class="new-page-header">
    Create Milestone Template
</div>
<div id="milestonetemplateerror" class="alert alert-danger" style="display:none;"></div>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="alert alert-danger" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:property value="%{code}"/> &nbsp; <s:actionmessage theme="simple"/>
        	
        </div>
    </s:if>
    <s:form theme="simple" name="milestoneTemplateForm" cssClass="form-horizontal form-groups-bordered">
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

<%@ include file='milestoneTemplate-header.jsp'%>
<%@ include file='milestoneTemplateActivity.jsp'%>
<div id="manual_workflow">
		<%@ include file="../workflow/workflow.jsp"%> 	 
	</div>

 <div class="row">
		<div class="col-xs-12 text-center buttonholdersearch">
			
			<input type="hidden" name="actionName" id="actionName" />
		<s:if test="%{mode=='modify' && (model.egwStatus.code=='APPROVED' || model.egwStatus.code=='NEW')}">
			<!-- egov authorization tag-->
			<egov-authz:authorize actionName="createMilestoneTemplate">
				<s:submit type="submit" cssClass="btn btn-primary"
					value="Save" id="save" name="save"
					method="save"
					onclick="document.milestoneTemplateForm.actionName.value='save';return validate('noncancel','save');" />
				<s:submit type="submit" cssClass="btn btn-primary"
					value="Save & Submit" id="submit_for_approval" name="submit_for_approval"
					method="save"
					onclick="document.milestoneTemplateForm.actionName.value='submit_for_approval';return validate('noncancel','submit_for_approval');" />
			</egov-authz:authorize>
		</s:if>
		<s:elseif test="%{(mode!='view') && (sourcepage=='inbox' || model.egwStatus==null || hasErrors())}">
				<s:iterator value="%{validActions}">
					<s:if test="%{description!=''}">
						<s:if test="%{description=='CANCEL'}">
							<s:submit type="submit" cssClass="btn btn-primary" value="%{description}" id="%{name}" 
								name="%{name}" method="cancel" 
								onclick="document.milestoneTemplateForm.actionName.value='%{name}';return validate('cancel','%{name}');"/>
						</s:if>								
						<s:elseif test="%{description=='REJECT'}">
							<s:submit type="submit" cssClass="btn btn-primary" value="%{description}" id="%{name}" 
								name="%{name}" method="reject" 
								onclick="document.milestoneTemplateForm.actionName.value='%{name}';return validate('reject','%{name}');"/>
						</s:elseif>								
						<s:else>
							<s:submit type="submit" cssClass="btn btn-primary"
								value="%{description}" id="%{name}" name="%{name}"
								method="save"
								onclick="document.milestoneTemplateForm.actionName.value='%{name}';return validate('noncancel','%{name}');" />
						</s:else>
				 	</s:if>
				</s:iterator>
			</s:elseif>
	 		<s:if test="%{mode=='view' && sourcepage=='search' && model.egwStatus.code=='APPROVED'}">
	 			<egov-authz:authorize actionName="createMilestoneTemplate">
					<input type="button" class="btn btn-primary" value="Modify" id="modifyButton" name="button" onclick="enableFieldsForModify()"/>&nbsp;
				</egov-authz:authorize>
			</s:if>  	
			<s:if test="%{model.id==null}" >
				<input type="button" class="btn btn-default" value="Clear" id="clear" name="clear" onclick="this.form.reset();">&nbsp;
			</s:if>
			<input type="button" class="btn btn-default" value="Close" id="closeButton" name="closeButton" onclick="window.close();" />
		
		</div>
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
