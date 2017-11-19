<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<title><s:text name='page.title.estimate.template'/></title>
<body class="yui-skin-sam">

<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<script>
function enableFieldsForModify(){
    id=dom.get('id').value;
    document.estimateTemplateForm.action='${pageContext.request.contextPath}/estimate/estimateTemplate-edit.action?mode=edit&id='+id;
    document.estimateTemplateForm.submit();
}

function validateCancel() {
	var msg='<s:text name="estimate.template.modify.confirm"/>';
	if(!confirmCancel(msg,'')) {
		return false;
	}
	else {
	    return true;
	}
}

function validateEstimateTemplateFormAndSubmit() {
    clearMessage('estimatetemplateerror')
	links=document.estimateTemplateForm.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    if(errors) {
        dom.get("estimatetemplateerror").style.display='';
    	document.getElementById("estimatetemplateerror").innerHTML='<s:text name="contractor.validate_x.message" />';
    	return false;
    }
    if(!validateHeaderBeforeSubmit(document.estimateTemplateForm)){
    	return false;
    }
    else {
    	mode=dom.get('mode').value;
    	if(mode=='edit'){
    	 if(validateCancel()){
    	  document.estimateTemplateForm.action='${pageContext.request.contextPath}/estimate/estimateTemplate-save.action';
    	  document.estimateTemplateForm.submit();
    	 }
    	}
    	else{
    	document.estimateTemplateForm.action='${pageContext.request.contextPath}/estimate/estimateTemplate-save.action';
    	document.estimateTemplateForm.submit();
    	}
   	}
   	return true;
}
</script>

<div class="new-page-header">
	<s:text name="estimate.sor.createEstimate"/>	
</div>
<div id="estimatetemplateerror" class="alert alert-danger" style="display:none;"></div>
<div id="templatecodeerror" class="alert alert-danger" style="display:none;">
	<s:text name="estimateTemplate.code.isunique"/>
</div>
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
<s:form theme="simple" name="estimateTemplateForm" cssClass="form-horizontal form-groups-bordered">
	    <s:token/>
<s:push value="model">

<s:if test="%{model.id!=null}">
	<s:hidden name="id" value="%{id}" id="id"/>
    <s:hidden name="mode" value="%{mode}" id="mode"/>
    <s:hidden  name="model.id" value="%{model.id}"id="id"/> 
</s:if> 
<s:else>
    <s:hidden name="id" value="%{null}" id="mode" />
</s:else>
	
	<%@ include file='estimateTemplate-header.jsp'%>
	<%@ include file='estimateTemplate-sor.jsp'%>
	<%@ include file='estimateTemplate-nonSor.jsp'%>
	
<div class="row">
	<div class="col-xs-12 text-center buttonholdersearch">
		<s:if test="%{mode!='view'}">
			<input type="submit" class="btn btn-primary" value="Save" id="saveButton" name="button" onclick="return validateEstimateTemplateFormAndSubmit()"/>&nbsp;
		</s:if>
		<egov-authz:authorize actionName="editEstimateTemplate">
		<s:if test="%{mode=='view'}">
			<input type="button" class="btn btn-primary" value="Modify" id="modifyButton" name="button" onclick="enableFieldsForModify()"/>&nbsp;
		</s:if>
		</egov-authz:authorize>
		<s:if test="%{model.id==null}" >
			<input type="button" class="btn btn-default" value="Clear" id="button" name="clear" onclick="this.form.reset();">&nbsp;
		</s:if>
			<input type="button" class="btn btn-default" value="Close" id="closeButton" name="closeButton" onclick="window.close();" />
	</div>
</div>
</s:push>
</s:form>
</body>
</html>
