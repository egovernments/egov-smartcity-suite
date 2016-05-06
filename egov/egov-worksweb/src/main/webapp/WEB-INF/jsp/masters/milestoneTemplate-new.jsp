<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ include file="/includes/taglibs.jsp" %>

<html>
<title><s:text name='page.title.milestone.template'/></title>
<body class="yui-skin-sam">

<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<script>

function enableFieldsForModify(){
	if(validateModfiy()){
    	id=dom.get('id').value;
    	document.milestoneTemplateForm.action='${pageContext.request.contextPath}/masters/milestoneTemplate-edit.action?id='+id+'&mode=modify&sourcepage=search';
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
    <s:form action="milestoneTemplate-save" theme="simple" name="milestoneTemplateForm" cssClass="form-horizontal form-groups-bordered" >
    <s:token/>
<s:push value="model">

	
	<s:hidden name="id" value="%{id}" id="id"/>
    <s:hidden name="mode" id="mode"/>
    <s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage"/>
	
<%@ include file='milestoneTemplate-header.jsp'%>
<%@ include file='milestoneTemplateActivity.jsp'%>

 <div class="row">
		<div class="col-xs-12 text-center buttonholdersearch">
			
			<input type="hidden" name="actionName" id="actionName" />
			<s:hidden type="mode" id="mode" />
				
			<s:submit type="submit" cssClass="btn btn-primary" value="Save"	id="saveButton" name="button" method="save" />&nbsp;
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
</script>           

</body>
</html>
