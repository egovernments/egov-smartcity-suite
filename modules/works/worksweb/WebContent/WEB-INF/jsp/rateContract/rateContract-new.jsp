<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<html>  
<head>  
<s:if test="%{mode=='edit'}">
<title><s:text name='rateContract.modify.title'/></title> 
</s:if>
<s:else>
    <title><s:text name='rateContract.create.title'/></title>
</s:else>
</head>  
<body>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>

<script type="text/javascript">
window.onload = function(){
	setCurrentdate();
	disableFields();
	loadData();
	load();
	disableForm();
}

function loadData(){

mode=dom.get('mode').value;
if(mode=='view'){
document.getElementById("indentNumber").value = '<s:property value="model.indent.indentNumber"/>';
document.getElementById("contractorName").value = '<s:property value="model.contractor.name"/>';
document.getElementById("departmentName").value = '<s:property value="model.indent.department.deptName"/>';
disableSelect();
document.getElementById("closeButton").disabled=false;
document.getElementById("rateContractPdfButton").disabled=false;
if(dom.get('viewDocButton')!=null)
	dom.get('viewDocButton').disabled=false;

}
}



function disableSelect(){
   	for(i=0;i<document.createRateContractForm.elements.length;i++){
    document.createRateContractForm.elements[i].disabled=true;
	}
}

function enableSelect(){
	for(i=0;i<document.createRateContractForm.elements.length;i++){
    	document.createRateContractForm.elements[i].disabled=false;
	}
}

function clearErrors(){
clearMessage('rateContract_error');
}

function disableFields(){
dom.get('contractorName').disabled=true;
dom.get('indentNumber').disabled=true;
dom.get('responseNumber').disabled=true;
dom.get('noticeNumber').disabled=true;
dom.get('departmentName').disabled=true;

}
function setCurrentdate(){
	var rcDate=document.getElementById("rcDate").value;
	
	var date = '<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	if(rcDate=="") 
	{
		document.getElementById("rcDate").value=date;
		
	}
	else 
	{	
		document.getElementById("rcDate").value=rcDate;
		
	}
}

function validateRateContractFormAndSubmit(){

clearMessage('rateContract_error');
dom.get('contractorName').disabled=false;
dom.get('indentNumber').disabled=false;
dom.get('responseNumber').disabled=false;
var err = false;
	 	
	 	
	 	if(!validateDateFormat(document.createRateContractForm.rcDate))
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.date.invalid" /><br>';
			disableFields();
			err = true;
		}
		
		
	 	if(document.createRateContractForm.rcDate != "")
	 	{
	 		if(checkdate())
	 		{
	 			err = true;
	 		}
	 	}
		
	 	if(document.getElementById('rcDate').value=="")
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.date.invalid" /><br>';
		  	err = true;
		}
		
		if(document.getElementById('rcName').value=="")
		{
			dom.get("rateContract_error").style.display='';
			document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.rcName.null" /><br>';
			disableFields();
		  	err = true;
		}
		if(!err)
		{
			mode=dom.get('mode').value;
			if(mode=='edit')
	    	{
	    		if(validateCancel())
	    		{
	    			document.createRateContractForm.action='${pageContext.request.contextPath}/rateContract/rateContract!save.action';
	    			document.createRateContractForm.submit();
	    		}
	    	}else
	    	{				
				document.createRateContractForm.action='${pageContext.request.contextPath}/rateContract/rateContract!save.action';
				document.createRateContractForm.submit();
	    	}
	}
	return err;
}

function validateCancel() {
	var msg='<s:text name="rateContract.template.cancel.confirm"/>';
	if(!confirmCancel(msg,'')) 
	{
		return false;
	}
	else 
	{
	    return true;
	}
}

function enableFieldsForModify(){
    id=dom.get('id').value;
    document.createRateContractForm.action='${pageContext.request.contextPath}/rateContract/rateContract!edit.action?id='+id+'&mode=edit';
    document.createRateContractForm.submit();
}

function checkdate(){
	rcDate = document.getElementById('rcDate').value;
	date = formatDate6("<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>");
	if(compareDate(formatDate6(date),formatDate6(rcDate)) == 1 )
	{
		dom.get("rateContract_error").style.display='';
		document.getElementById("rateContract_error").innerHTML+='<s:text name="rateContract.date.validate" /><br>';
		return true;
	}
}

function validateForm(){
	if(!validateRateContractFormAndSubmit()){
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
	  
	if(text!='Approve' && text!='Reject' ){
		if(!validateWorkFlowApprover(text))
			return false;
	}
	enableSelect(); 
	if(!validateRateContractFormAndSubmit())
	  return false;
	
	return true; 
}

 function loadDesignationFromMatrix(){ 
 		 var dept=dom.get('departmentName').value;
  		 //var dept = dom.get('approverDepartment').value;
  		 var currentState = dom.get('currentState').value;
  		 var amountRule =  dom.get('amountRule').value;
  		 var additionalRule =  dom.get('additionalRule').value;
  		 var pendingAction=document.getElementById('pendingActions').value;
  		 //var currentDesignation =  dom.get('currentDesignation').value;
  		 loadDesignationByDeptAndType('RateContract',dept,currentState,amountRule,additionalRule,pendingAction); 
}

function populateApprover()
{
  getUsersByDesignationAndDept(); 
}


</script>

<div class="errorstyle" id="rateContract_error" style="display:none;"></div>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	 <s:actionmessage theme="simple"/>
        </div>
    </s:if>
<s:form theme="simple" name="createRateContractForm" > 
<s:token/>
<s:push value="model">
<s:hidden name="documentNumber" id="docNumber" />
<s:if test="%{model.id!=null}">
	<s:hidden name="id" value="%{id}" id="id"/>
    <s:hidden name="mode" value="%{mode}" id="mode"/>
    <s:hidden name="rcStatus" value="%{model.egwStatus.code}" id="rcStatus"/>
    <s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage"/>
 </s:if>
<s:else>
    <s:hidden name="id" value="%{null}" id="mode" />
    <s:hidden name="rcStatus" value="%{null}" id="rcStatus"/>
    <s:hidden name="sourcepage" value="%{null}" id="sourcepage"/>
</s:else>
<div class="formmainbox">
	<div class="insidecontent">
  		<div class="rbroundbox2">
		<div class="rbtop2"><div></div></div>  
     	<div class="rbcontent2">  
     	
     	<div class="datewk">
 		<div class="estimateno">Rate Contract No: <s:if test="%{not model.rcNumber}">&lt; Not Assigned &gt;</s:if><s:property value="model.rcNumber" /></div>
	  </div>
	  	<table  width="100%" border="0" cellspacing="0" cellpadding="0">
 		<tr>
            <td colspan="6" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div><div class="headplacer"><s:text name='createRateContract.template.header'/></div></td>
        </tr>
        
        <tr><td>&nbsp;</td></tr>
 			<s:hidden name="responseNumberTemp" id="responseNumberTemp" value="%{responseNumberTemp}"/>
 			<s:hidden name="noticeNumberTemp" id="noticeNumberTemp" value="%{noticeNumberTemp}"/>
 		<tr>
 			<td  class="whiteboxwk"><span class="mandatory">*</span><s:text name="rateContract.rcDate" />:</td>
			<td class="whitebox2wk"><s:date name="rcDate" var="rcDateFormat" format="dd/MM/yyyy"/><s:textfield name="rcDate" value="%{rcDateFormat}" id="rcDate" cssClass="selectwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
             <s:if test="%{mode!='view'}">
             <a href="javascript:show_calendar('forms[0].rcDate',null,null,'DD/MM/YYYY');"  id="rcDateImg"  name="rcDateImg" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
             </s:if>
                    <span id='errorrcDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>
            </td>
            <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="rateContract.rcName" />:</td>
            <td class="whitebox2wk"><s:textfield id="rcName" name="rcName" value="%{rcName}" cssClass="selectwk" /></td>
           <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="Contractor name" />:</td>
           <s:hidden name="contractor" id="contractor" value="%{contractor.id}"/>
       		<td class="whitebox2wk"><s:textfield id="contractorName" name="contractorName" value="%{contractor.name}" cssClass="selectwk"/>
            </td>
        </tr>
        <tr>
       	<td class="greyboxwk"><s:text name="rateContract.rcDescription" />:</td>
       	<td class="greybox2wk"><s:textarea id="description" name="description" cols="40" cssClass="selectwk" /></td>
       	<td class="greyboxwk"><s:text name="rateContract.rcIndent" />:</td>
       	<s:hidden name="indent" id="indent" value="%{indent.id}"/>
       	<td class="greybox2wk"><s:textfield id="indentNumber" name="indentNumber" value="%{indent.indentNumber}" cssClass="selectwk"/></td>
       	<td class="greyboxwk"><s:text name="rateContract.brNumber" />:</td>
       	<td class="greybox2wk">
       		<s:if test="%{model.responseNumber!=null && model.responseNumber!=''}" >
       			<s:textfield id="responseNumber" name="responseNumber" value="%{responseNumber}" cssClass="selectwk"/>
       		</s:if>
       		<s:else>
       			<s:textfield id="responseNumber" name="responseNumber" value="%{responseNumberTemp}" cssClass="selectwk"/>
       		</s:else>
       	</td>
        </tr>
        <tr>
       	<td class="whiteboxwk"><s:text name="rateContract.remark" />:</td>
       	<td class="whitebox2wk"><s:textarea id="remarks" name="remarks" cols="40" cssClass="selectwk" /></td>
       	<td class="whiteboxwk"><s:text name="rateContract.noticeNumber" />:</td>
       	<td class="whitebox2wk">
       		<s:if test="%{noticeNumber!=null && noticeNumber!=''}" >
       			<s:textfield id="noticeNumber" name="noticeNumber" value="%{noticeNumber}" cssClass="selectwk"/>
       		</s:if>
       		<s:else>
       			<s:textfield id="noticeNumber" name="noticeNumber" value="%{noticeNumberTemp}" cssClass="selectwk"/>
       		</s:else>
       	</td>
       	<td class="whiteboxwk"><s:text name="rateContract.department" />:</td>
       	 <td class="whitebox2wk"><s:textfield id="departmentName" name="departmentName" value="%{indent.department.deptName}" cssClass="selectwk"/></td>
       	
       	</tr>
 	
		</table>
		<div>&nbsp;</div>
	 <table  width="100%" border="0" cellspacing="0" cellpadding="0">
	     <tr>
	       	<td>
	          		<%@ include file='rateContract-sor.jsp'%>
			</td>
	  	</tr>
	  	
	  	 <s:if test="%{mode != 'view' && mode !='search' }" >
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
		</s:if>	
	</table>
		 <div class="rbbot2"><div></div></div>
		</div>
		</div>
	</div>
</div>
<div class="buttonholderwk">
	  <p>
	  <input type="hidden" name="actionName" id="actionName"/>
	  
	  <s:if test="%{mode!='edit'}"> 
		<s:if test="%{(sourcepage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW'  
			|| model.egwStatus.code=='REJECTED') && mode !='view' || hasErrors() || hasActionMessages()}">
				<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
					<s:submit type="submit" cssClass="buttonfinal"
					value="Save" id="Save" name="Save"
					method="save"
					onclick="document.createRateContractForm.actionName.value='Save';return validateForm();" />
				</s:if>
				<s:iterator value="%{getValidActions()}" var="name">
				<s:if test="%{name!=''}">
						<s:submit type="submit" cssClass="buttonfinal"
							value="%{name}" id="%{name}" name="%{name}"
							method="save"
							onclick="document.createRateContractForm.actionName.value='%{name}';return validate('%{name}');" />
				</s:if>
			</s:iterator>  
		</s:if>
	 </s:if>
	 
 	<s:if test="%{model.id!=null && (mode=='view' || mode=='edit' || sourcepage=='inbox' || model.egwStatus!=null)}" >
		<input type="button" class="buttonpdf"  value="VIEW RATE CONTRACT PDF" onclick="window.open('${pageContext.request.contextPath}/rateContract/rateContract!viewRateContractPDF.action?id=<s:property value='%{model.id}'/>');" id="rateContractPdfButton" name="rateContractPdfButton"/>
	</s:if>
	<s:if test="%{model.id==null}">
		<input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="clearButton" onclick="this.form.reset(); setCurrentdate(); disableFields(); clearErrors();">&nbsp;
	</s:if>
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='rateContract.close.confirm'/>');" />
	<s:if test="%{mode =='view' ||  sourcepage=='search' || mode=='search'}">
		<input type="submit" class="buttonadd" value="View Document" id="viewDocButton" onclick="viewDocumentManager(dom.get('docNumber').value);return false;" />
	</s:if>	
	<s:else>
		<input type="submit" class="buttonadd" value="Upload Document" id="UploadDocButton" onclick="showDocumentManager();return false;" />
	</s:else> 	
	
	</p>
</div>
</s:push>
</s:form>


<SCRIPT type="text/javascript"> 
	
	 function disableForm(){
	 
	 <s:if test="%{sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'}">
	  	dom.get("rcDateImg").style.display='none';
	 </s:if>
	 
	 <s:if test="%{(sourcepage=='inbox' && model.currentState.value=='CHECKED')}">
	         toggleFields(true,['approverComments']);  
	         showElements(['approverComments']);
	 </s:if>
	 <s:elseif test="%{sourcepage=='inbox' && model.currentState.value=='NEW'}">
	        toggleFields(false,[]);
	        	document.createRateContractForm.contractorName.readonly=true;
				document.createRateContractForm.contractorName.disabled=true;
				
				document.createRateContractForm.indentNumber.readonly=true;
				document.createRateContractForm.indentNumber.disabled=true;
				
				document.createRateContractForm.responseNumber.readonly=true; 
				document.createRateContractForm.responseNumber.disabled=true;
				
				document.createRateContractForm.noticeNumber.readonly=true;
				document.createRateContractForm.noticeNumber.disabled=true;
				
				document.createRateContractForm.departmentName.readonly=true;
				document.createRateContractForm.departmentName.disabled=true;
				
	        showElements(['approverComments']);
	 </s:elseif>
	 <s:elseif test="%{sourcepage=='inbox' && model.currentState.value!='REJECTED' && model.currentState.value!='END'}">
	        toggleFields(true,['approverDepartment','approverDesignation','approverPositionId','approverComments']);
	 </s:elseif>
	 
	 <s:if test="%{sourcepage=='inbox'}">
		     <s:if test="%{model.currentState.value=='CREATED' || model.currentState.value=='CHECKED' || model.currentState.value=='RESUBMITTED'}">		       
				document.createRateContractForm.closeButton.readonly=false;
				document.createRateContractForm.closeButton.disabled=false;
				document.createRateContractForm.rateContractPdfButton.readonly=false;
				document.createRateContractForm.rateContractPdfButton.disabled=false;
				if(dom.get('Approve')!=null)
					dom.get('Approve').disabled=false;
				if(dom.get('Forward')!=null)
					dom.get('Forward').disabled=false;
				dom.get('Reject').disabled=false;
				if(dom.get('UploadDocButton')!=null)
					dom.get('UploadDocButton').disabled=false;
				
				
			 </s:if>			
		</s:if> 
		}
	function load(){
	<s:if test="%{mode=='search' || mode='view'}">
        for(var i=0;i<document.forms[0].length;i++) {
      		document.forms[0].elements[i].disabled =true;
      		dom.get("rcDateImg").style.display='none';
      	}
      	document.createRateContractForm.closeButton.readonly=false;
		document.createRateContractForm.closeButton.disabled=false;	
		document.createRateContractForm.rateContractPdfButton.readonly=false;
		document.createRateContractForm.rateContractPdfButton.disabled=false;
		if(dom.get('viewDocButton')!=null)
			dom.get('viewDocButton').disabled=false;
      </s:if>	
    }
	</SCRIPT>
</body>


</body>
</html>    