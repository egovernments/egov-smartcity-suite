<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/egov-authz.tld" prefix="egov-authz" %>
<html>
<title><s:text name='page.title.estimate.template'/></title>
<body class="yui-skin-sam">

<script src="<egov:url path='js/works.js'/>"></script>
<script>
function enableFieldsForModify(){
    id=dom.get('id').value;
    document.estimateTemplateForm.action='${pageContext.request.contextPath}/estimate/estimateTemplate!edit.action?id='+id+'&mode=edit';
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
    	return;
    }
    if(!validateHeaderBeforeSubmit(document.estimateTemplateForm)){
    	return;
    }
    else {
    	mode=dom.get('mode').value;
    	if(mode=='edit'){
    	 if(validateCancel()){
    	  document.estimateTemplateForm.action='${pageContext.request.contextPath}/estimate/estimateTemplate!save.action';
    	  document.estimateTemplateForm.submit();
    	 }
    	}
    	else{
    	document.estimateTemplateForm.action='${pageContext.request.contextPath}/estimate/estimateTemplate!save.action';
    	document.estimateTemplateForm.submit();
    	}
   	}
}

</script>
<div id="estimatetemplateerror" class="errorstyle" style="display:none;"></div>
<div id="templatecodeerror" class="errorstyle" style="display:none;">
<s:text name="estimateTemplate.code.isunique"/>
</div>
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
    <s:form theme="simple" name="estimateTemplateForm" >
    <s:token/>
<s:push value="model">

	
<s:if test="%{model.id!=null}">
	<s:hidden name="id" value="%{id}" id="id"/>
    <s:hidden name="mode" value="%{mode}" id="mode"/>
</s:if> 
<s:else>
    <s:hidden name="id" value="%{null}" id="mode" />
</s:else>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2"><div class="datewk">
	 <%@ include file='estimateTemplate-header.jsp'%>
	<%@ include file='estimateTemplate-sor.jsp'%>
	<%@ include file='estimateTemplate-nonSor.jsp'%>
	  <div class="rbbot2"><div></div></div>
      </div>     
	
</div>
  </div>
</div>
<div class="buttonholderwk">
		
	  <p>
	    <s:if test="%{mode!='view'}">
			<input type="button" class="buttonfinal" value="SAVE" id="saveButton" name="button" onclick="validateEstimateTemplateFormAndSubmit()"/>&nbsp;
		</s:if>
		<egov-authz:authorize actionName="editEstimateTemplate">
		<s:if test="%{mode=='view'}">
			<input type="button" class="buttonfinal" value="MODIFY" id="modifyButton" name="button" onclick="enableFieldsForModify()"/>&nbsp;
		</s:if>
		</egov-authz:authorize>
		<s:if test="%{model.id==null}" >
			<input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="this.form.reset();">&nbsp;
		</s:if>
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
	  </p>
		
</div>
</s:push>
</s:form>
</body>
</html>