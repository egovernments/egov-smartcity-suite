<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/egov-authz.tld" prefix="egov-authz" %>

<html>
<title><s:text name='page.title.milestone.track'/></title>
<body onload="init();" class="yui-skin-sam">

<script src="<egov:url path='js/works.js'/>"></script>
<script>

function init(){

<s:if test="%{mode!='view'}">
	//populateDesignation();
</s:if>

disableHeader();

}

function disableHeader(){
	toggleForSelectedFields(true,['status','execDept','estimateNumber','typeOfWork','subtypeOfWork','workName','projectcode','workOrderNumber','contractor']);
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

function validateTrackMilestoneFormAndSubmit() {
	dom.get("total").value=getNumber(roundTo(getNumericValueFromInnerHTML("totalCompletionPerc")));
    clearMessage('trackmilestoneerror')
	links=document.trackMilestoneForm.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    if(errors) {
        dom.get("trackmilestoneerror").style.display='';
    	document.getElementById("trackmilestoneerror").innerHTML='<s:text name="contractor.validate_x.message" />';
    	return false;
    }
	if(validateCompletionDate()){
        dom.get("trackmilestoneerror").style.display='';
    	document.getElementById("trackmilestoneerror").innerHTML='<s:text name="trackmilestoneActivity.completionDate.null" />';
    	return false;
	}
	
	if(document.trackMilestoneForm.isProjectCompleted.checked){
		if(!validateProjectCompletionFlag()){
	        dom.get("trackmilestoneerror").style.display='';
	    	document.getElementById("trackmilestoneerror").innerHTML='<s:text name="trackmilestoneActivity.projectcompletion.checkbox.error" /><br/>';
	    	document.getElementById("trackmilestoneerror").innerHTML+='&nbsp;&nbsp;&nbsp;<s:text name="trackmilestoneActivity.projectcompletion.checkbox.error1" /><br/>';
	    	document.getElementById("trackmilestoneerror").innerHTML+='&nbsp;&nbsp;&nbsp;<s:text name="trackmilestoneActivity.projectcompletion.checkbox.error2" />';
	    	return false;
		}
    }
    return true;
}

function validateCancel() {
	var msg='<s:text name="milestone.track.cancel.confirm"/>';
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
/*	if(obj!="cancel"){
		if(!validateUser(text))
			return false;
	}*/
	if(!validateTrackMilestoneFormAndSubmit())
	  return false;
/*	if(obj=="cancel"){
	   if(!validateCancel())
	      return false;
	}
	if(obj=="reject"){
		var remarks=document.getElementById("approverComments").value;
		if(trim(remarks)==""){
			dom.get("trackmilestoneerror").style.display='';
    		document.getElementById("trackmilestoneerror").innerHTML='<s:text name="milestone.track.remarks.null" />';
    		return false;
		}
	}*/
	enableSelect();
	return true;
}

</script>
<div id="trackmilestoneerror" class="errorstyle" style="display:none;"></div>
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
    <s:form theme="simple" name="trackMilestoneForm" >
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
<s:hidden name="scriptName" id="scriptName" value="TrackMilestone"></s:hidden>
<s:hidden name="milestone" value="%{model.milestone.id}" id="milestone"/>
<s:hidden name="total" id="total" value="%{model.total}"></s:hidden>
<s:hidden name="stateValue" id="stateValue" value="%{stateValue}" />

<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	 <%@ include file='trackMilestone-header.jsp'%>
	 <%@ include file='trackMilestoneActivity.jsp'%>
	<s:if test="%{mode!='view'}">
	<!--  <div id="manual_workflow">
		<%@ include file="../workflow/workflow.jsp"%> 	 
	</div> -->
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
		<s:if test="%{mode=='modify' && (model.egwStatus.code=='APPROVED' || model.egwStatus.code=='NEW')}">
			<egov-authz:authorize actionName="TrackMilestoneNewform">
				<s:submit type="submit" cssClass="buttonfinal"
					value="APPROVE" id="save" name="save"
					method="save"
					onclick="document.trackMilestoneForm.actionName.value='save';return validate('noncancel','save');" />
<!-- 				<s:submit type="submit" cssClass="buttonfinal"
					value="SAVE & SUBMIT" id="submit_for_approval" name="submit_for_approval"
					method="save"
					onclick="document.trackMilestoneForm.actionName.value='submit_for_approval';return validate('noncancel','submit_for_approval');" /> -->
			</egov-authz:authorize>
		</s:if>
		<s:elseif test="%{(mode!='view') && (sourcepage=='inbox' || model.egwStatus==null)}">
			<!-- 	<s:iterator value="%{validActions}">
					<s:if test="%{description!=''}">
						<s:if test="%{description=='CANCEL'}">
							<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" 
								name="%{name}" method="cancel" 
								onclick="document.trackMilestoneForm.actionName.value='%{name}';return validate('cancel','%{name}');"/>
						</s:if>								
						<s:elseif test="%{description=='REJECT'}">
							<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" 
								name="%{name}" method="reject" 
								onclick="document.trackMilestoneForm.actionName.value='%{name}';return validate('reject','%{name}');"/>
						</s:elseif>								
						<s:else>
							<s:submit type="submit" cssClass="buttonfinal"
								value="%{description}" id="%{name}" name="%{name}"
								method="save"
								onclick="document.trackMilestoneForm.actionName.value='%{name}';return validate('noncancel','%{name}');" />
						</s:else>
				 	</s:if>
				</s:iterator> -->

				<s:submit type="submit" cssClass="buttonfinal"
					value="APPROVE" id="save" name="save"
					method="save"
					onclick="document.trackMilestoneForm.actionName.value='save';return validate('noncancel','save');" />
		</s:elseif>
		<s:if test="%{mode=='view'}" >
			<input type="button" class="buttonfinal" value="BACK" id="backButton" name="backButton" onclick="history.back();" />
		</s:if>
		<s:if test="%{model.id==null && mode!='view'}" >
			<input type="button" class="buttonfinal" value="CLEAR" id="clear" name="clear" onclick="this.form.reset();">&nbsp;
		</s:if>
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
		<!--
		<s:if test="%{mode=='view'}" >
			<input type="button" onclick="window.open('${pageContext.request.contextPath}/milestone/trackMilestone!workflowHistory.action?stateValue=<s:property value='%{stateValue}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');" class="buttonfinal" value="HISTORY" id="historyButton" name="historyButton"/>
		</s:if>
	  -->
	  </p>
		
</div> 

</s:push>
</s:form>
 <script type="text/javascript">
  <s:if test="%{mode=='view'}">
		disableSelect();
		enableButtons();
  </s:if>
  
  	<s:if test="%{mode!='view' && sourcepage=='inbox' && (model.egwStatus.code=='CREATED'||model.egwStatus.code =='RESUBMITTED')}">
	      //hideElements(['workflowDetials']);
	      //showElements(['approverCommentsRow']);
	      disableSelect();
	      enableButtons();
	</s:if>
	<s:if test="%{mode!='view' && sourcepage=='inbox' && (model.egwStatus.code=='NEW' ||model.egwStatus.code=='REJECTED') }">
	      //showElements(['approverCommentsRow']);
	 </s:if>
	 <s:if test="%{mode!='view' && sourcepage!='inbox' && model.id!=null && model.egwStatus.code!='NEW'}">
	 	//hideElements(['workflowDetials']);
	    //hideElements(['approverCommentsRow']);
	 </s:if>
	 <s:if test="%{mode!='view' && (model.id==null || model.egwStatus.code=='NEW')}">
	 	//showElements(['approverCommentsRow']);
	 </s:if>
	 <s:if test="%{mode=='modify' && (model.egwStatus.code=='NEW' || model.egwStatus.code=='APPROVED')}">
	 	//showElements(['workflowDetials']);
	 	//showElements(['approverCommentsRow']);
	 </s:if>


</script>           

</body>
</html>