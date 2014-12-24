<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
<head>
	<title><s:text name="tenderfile.title"/></title>
</head>
<body onload="populateDetails();load();toggleSpillOverWorks();init();">  
<script src="<egov:url path='js/helper.js'/>"></script>
<script src="<egov:url path='js/works.js'/>"></script> 
<script type="text/javascript">

function load(){
<s:if test="%{abstractEstimateList.size>0}">
	dom.get("tenderFileDetailsTable").style.display='';
	dom.get("tenderFileIndentDetailsTable").style.display='none';
</s:if>
<s:if test="%{indentList.size>0}">
	dom.get("tenderFileDetailsTable").style.display='none';
	dom.get("tenderFileIndentDetailsTable").style.display='';
</s:if>

<s:if test="%{sourcepage=='search'}">
       toggleFields(true,[]); 
      links=document.tenderFileForm.getElementsByTagName("a"); 
	  for(i=0;i<links.length;i++){	
		if(links[i].id.indexOf("header_")!=0 && links[i].id!='aprdDatelnk')
     				links[i].onclick=function(){return false;};     		  
			}
    
 </s:if>
  
<s:if test="%{sourcepage=='search'}">
 enabledivChilderns("buttons");
 disableSelect();
</s:if>
<s:if test="%{model.id!=null}">
 	dom.get('department').disabled=true;
 	dom.get('fileDate').disabled=true;
 	dom.get("tenderFileDateImg").style.display='none';
</s:if>

<s:if test="%{model.egwStatus != null && sourcepage=='inbox'}">
	<s:if test="%{model.egwStatus.code == 'CREATED' || model.egwStatus.code=='APPROVED' || model.egwStatus.code == 'RESUBMITTED' || model.egwStatus.code=='CHECKED' }">
		disableSelect();
	</s:if>
</s:if>

}


designationLoadHandler = function(req,res){ 
  results=res.results;
  dom.get("designation").value=results[0].Designation;
}

designationLoadFailureHandler= function(){
    dom.get("tenderFile_error").style.display='';
	document.getElementById("tenderFile_error").innerHTML='<s:text name="unable.des"/>';
}

function setEstimateType(){
	if(document.forms[0].isSpillOverWorks!=null) {
		if(document.forms[0].isSpillOverWorks.value=='true' || (document.forms[0].isSpillOverWorks.length>1 && document.forms[0].isSpillOverWorks[0].value=='true') ) {
			dom.get("estimateType").value='spillovertype';
		}	
		else {
			dom.get("estimateType").value='normaltype';
		}			
	}
	else {
		dom.get("estimateType").value='notype';
	}
}

function showDesignation(elem){
	var empId = elem.options[elem.selectedIndex].value;
    makeJSONCall(["Designation"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!designationForUser.action',{empID:empId},designationLoadHandler,designationLoadFailureHandler) ;
}
function refreshInbox() {
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
        x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}
function populateDetails()
{
	if(dom.get("fileDate").value=='') {
		 <s:if test="%{model.fileDate==null}">
			document.tenderFileForm.fileDate.value=getCurrentDate();
		 </s:if>
	}
	document.forms[0].hiddenDate.value=document.tenderFileForm.fileDate.value;
	<s:if test="%{createdBySelection.toLowerCase().equals('no')}">
		dom.get('department').disabled=true;
		dom.get('preparedBy').disabled=true;
	</s:if>
	<s:else>
		dom.get("ajaxCall").style.display='';
	</s:else>
	<s:if test="%{editableDate.toLowerCase().equals('yes')}">	
		dom.get('fileDate').disabled=false;
		dom.get("tenderFileDateImg").style.display='';
	</s:if>
	<s:else>
		dom.get('fileDate').disabled=true;
		dom.get("tenderFileDateImg").style.display='none';
	</s:else>
}
function enableSelect(){
   	for(i=0;i<document.tenderFileForm.elements.length;i++){
    document.tenderFileForm.elements[i].disabled=false;
	}
}

function disableSelect(){
   	for(i=0;i<document.tenderFileForm.elements.length;i++){
    	document.tenderFileForm.elements[i].disabled=true;
	}
	if(dom.get("tenderFileDateImg")!=null){
		dom.get("tenderFileDateImg").style.display='none';
	}
	if(dom.get("Approve")!=null){
		dom.get("Approve").disabled=false
	}
	if(dom.get("Cancel")!=null){
		dom.get("Cancel").disabled=false;
	}		
	if(dom.get("Forward")!=null){
		dom.get("Forward").disabled=false;
	}
	if(dom.get("Reject")!=null){
		dom.get("Reject").disabled=false;
	}
	if(dom.get("Cancel")!=null){
		dom.get("Cancel").disabled=false;
	}
	if(dom.get('approverDepartment')!=null){
		dom.get('approverDepartment').disabled=false;
	}
	if(dom.get('approverDesignation')!=null){
		dom.get('approverDesignation').disabled=false;
	}
	if(dom.get('approverPositionId')!=null){
		dom.get('approverPositionId').disabled=false;
	}
	if(dom.get("approverComments")!=null){
		dom.get("approverComments").disabled=false;
	}
	if(dom.get("closeButton")!=null){
		dom.get("closeButton").disabled=false;
	}
	if(dom.get("worksDocUploadButton")!=null){
		dom.get("worksDocUploadButton").disabled=false;
	}
	if(dom.get("docViewButton")!=null){
		dom.get("docViewButton").disabled=false;
	} 
	if(dom.get("tenderFilePdfButton")!=null){
		dom.get("tenderFilePdfButton").disabled=false;
	}  
}

function setupPreparedByList(elem){
	deptId=elem.options[elem.selectedIndex].value;
	populatepreparedBy({executingDepartment:deptId});
    if(dom.get("estimateListTable")!=null && dom.get("estimateListTable").rows.length>1){
        dom.get("popUpDiv").innerHTML="";
     	dom.get("popUpDiv").innerHTML='<s:text name="tenderfile.reset.on.dept"/>'+dom.get("msgDiv").innerHTML ;
		popup('popUpDiv')
	}
	
}
function clearDesignation(elem) {
    dom.get("designation").value='';
}

function deleteAllrows(tableID) {
    var table = dom.get(tableID);
    var rowCount = table.rows.length;
	for(var i=0; i<rowCount; i++) {
         var row = table.rows[i];
          table.deleteRow(i);
            rowCount--;
             i--;
    }
     document.forms[0].hiddenDate.value=document.forms[0].fileDate.value;
     dom.get('hiddenDept').value=dom.get('department').value;
  }
function dateChange(){
    var fileDate=document.forms[0].fileDate.value;
	var hiddenFileDate=document.forms[0].hiddenDate.value;
	if(fileDate!='' && hiddenFileDate!= '' && hiddenFileDate!=fileDate){
	     if(dom.get("estimateListTable")!=null && dom.get("estimateListTable").rows.length>0){
	      	dom.get("popUpDiv").innerHTML="";
	        dom.get("popUpDiv").innerHTML='<s:text name="tenderfile.reset.on.date"/>'+dom.get("msgDiv").innerHTML ;
	       	popup('popUpDiv');
		 }	
	}
}
function validateBeforeSubmit() {
	/*if(dom.get("estimateListTable")==null){
	  	dom.get("tenderFile_error").innerHTML='<s:text name="tenderfile.estimates.null"/>'; 
        dom.get("tenderFile_error").style.display='';
		return false;
	  }
	  else if(dom.get("estimateListTable").rows.length<=0)
	  {
	  	 dom.get("tenderFile_error").innerHTML='<s:text name="tenderfile.estimates.null"/>'; 
         dom.get("tenderFile_error").style.display='';
		 return false;
	  }*/
	  if(dom.get("fileDate").value==""){
	  	 dom.get("tenderFile_error").innerHTML='<s:text name="tenderfile.fileDate.is.null"/>'; 
         dom.get("tenderFile_error").style.display='';
		 return false;
	  }
	  
	  if(dom.get("fileName").value==""){
	  	 dom.get("tenderFile_error").innerHTML='<s:text name="tenderfile.fileName.is.null"/>'; 
         dom.get("tenderFile_error").style.display='';
		 return false;
	  }
	  
	  if(dom.get("department").value==-1){
	  	 dom.get("tenderFile_error").innerHTML='<s:text name="tenderfile.department.is.null"/>'; 
         dom.get("tenderFile_error").style.display='';
		 return false;
	  }
	  
	  if(dom.get("preparedBy").value==-1){
	  	 dom.get("tenderFile_error").innerHTML='<s:text name="tenderfile.preparedBy.is.null"/>'; 
         dom.get("tenderFile_error").style.display='';
		 return false;
	  }
	 
	   dom.get("tenderFile_error").style.display='none';
	   dom.get("tenderFile_error").innerHTML='';
	   return true;
}

function rollbackChanges(){
	document.tenderFileForm.fileDate.value=document.forms[0].hiddenDate.value;
	if(dom.get('hiddenDept').value!="")
		dom.get('department').value=dom.get('hiddenDept').value;
}
function validateCancel() {
	var msg='<s:text name="tenderfile.cancel.confirm"/>';
	var estNo='<s:property value="model.fileNumber"/>'; 
	if(!confirmCancel(msg,estNo)) {
		return false;
	}
	else {
		return true;
	}
}

function loadDesignationFromMatrix(){
 		var value=document.getElementById("department").value;
		if(value=="-1"){
  			alert("Please select department for Tender File");
	 		document.getElementById("approverDepartment").value=-1;
			return false;
		}
  	
   		 var dept=document.getElementById('department').options[document.getElementById('department').selectedIndex].text;
  		 var currentState = dom.get('currentState').value;
  		 var amountRuleValue =  dom.get('amountRuleValue').value;
  		 var additionalRuleValue =  dom.get('additionalRuleValue').value;
		 var indentTF='false';
  		if(dom.get("indentListTable")!=null){
			if(dom.get("indentListTable").rows.length>1){
				indentTF='true';
			}
			else if(dom.get("indentListTable").rows.length==1){
				indentTF='true';
			}
  		}	
  		if(indentTF=='true'){
  			additionalRuleValue ='indent';
  		}
  		else if(dept=="Public Work"){
  			if(additionalRuleValue=='ZonalPublicWork'){
  				additionalRuleValue ='ZonalPublicWork';
  			}
  			else additionalRuleValue ='HQPublicWork';
  		 }
  		 else{ 
  		 if(document.tenderFileForm.quotationFlag.checked){
  		 	var additionalRuleValue ='quotation';
  		 }else{
  		 	var additionalRuleValue ='noQuotation';
  		 }
  	}
  		 var pendingAction=document.getElementById('pendingActions').value;
  		 loadDesignationByDeptAndType('TenderFile',dept,currentState,amountRuleValue,additionalRuleValue,pendingAction); 
}

function populateApprover()
{
  getUsersByDesignationAndDept();
}

function validateTenderFileDetails(){
	
	if(dom.get('estimateListTable')==null && dom.get('indentListTable')==null){
		alert("Please select Tender file details.");
		return false;
	}
	else if(dom.get('indentListTable')!=null && dom.get('estimateListTable')!=null){
		if(dom.get("indentListTable").rows.length==0 && dom.get("estimateListTable").rows.length==0){
			alert("Please select Tender file details.");
			return false;
		}
	}
	else if(dom.get('indentListTable')!=null){
		if(dom.get("indentListTable").rows.length==0){
			alert("Please select Tender file details.");
			return false;
		}
	}
	else if(dom.get('estimateListTable')!=null){
		if(dom.get("estimateListTable").rows.length==0){
			alert("Please select Tender file details.");
			return false;
		}
	}
	
	return true;
}

function validate(text){
	if(text!='Approve' && text!='Reject' ){
		if(!validateWorkFlowApprover(text, 'tenderFile_error'))
			return false;
	}
	
	if(text=='Reject' && dom.get("approverComments")!=null){
		if(dom.get("approverComments").value==''){
		 	alert("Please enter approver comments");
		 	return false;
		 }
	}
		
/*	if(obj!="cancel"){
		if(!validateUser(text))
			return false;
	}*/
	if(document.getElementById("approverPositionId")!=null){
		document.getElementById("approverId").value=document.getElementById("approverPositionId").value;
	}
	
	if(!validateTenderFileDetails()){
		return false;
	}	
	
	if(document.getElementById("actionName").value=='Cancel'){
	   if(!validateCancel())
	      return false;
	}
	enableSelect();		
	return true;
}

function goToPath(){
	if(dom.get("department").options[dom.get("department").selectedIndex].value>0){
	window.open("${pageContext.request.contextPath}/estimate/searchEstimate.action?execDept="+
	dom.get("department").options[dom.get("department").selectedIndex].value+"&source=tenderFile"+"&tenderFileDate="+dom.get("fileDate").value+"&estimateType="+dom.get("estimateType").value,"",
	 			"height=600,width=900,scrollbars=yes,left=0,top=0,status=yes");
	}
	else
	{
		dom.get("tenderFile_error").style.display='';
		dom.get("tenderFile_error").innerHTML='<s:text name="select.dept"/>';
		return false;
	}
	 dom.get("tenderFile_error").style.display='none';
	 dom.get("tenderFile_error").innerHTML='';
}

function getIndents() {
	if(dom.get("department").options[dom.get("department").selectedIndex].value>0){
		window.open("${pageContext.request.contextPath}/rateContract/indentRateContract!search.action?dept="+
		dom.get("department").options[dom.get("department").selectedIndex].value+"&source=tenderFile&mode=search&tenderFileDate="+dom.get("fileDate").value+"&status=APPROVED","",
	 			"height=600,width=900,scrollbars=yes,left=0,top=0,status=yes");
	}
	else
	{
		dom.get("tenderFile_error").style.display='';
		dom.get("tenderFile_error").innerHTML='<s:text name="select.dept"/>';
		return false;
	}
	 dom.get("tenderFile_error").style.display='none';
	 dom.get("tenderFile_error").innerHTML='';

}

function checkQuotation(obj){ 
   if(obj.checked){
	 document.tenderFileForm.quotationFlag.value=true;
	}
	else if(!obj.checked){
	 document.tenderFileForm.quotationFlag.value=false;
	} 
	
	document.getElementById("approverDepartment").value=-1;
	document.getElementById("approverDesignation").value=-1;
	document.getElementById("approverPositionId").value=-1;
}

</script>
<div class="errorstyle" id="tenderFile_error" style="display: none;" >
</div>
<div id="blanket" style="display:none;"></div>
<div id="msgDiv" style="display:none;">(<a href="#" onclick="popup('popUpDiv');deleteAllrows('estimateListTable');">Yes</a>/<a href="#" onclick="popup('popUpDiv');rollbackChanges();">No</a>)?</div>
<div id="popUpDiv" style="display:none;" ></div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:property value="%{model.fileNumber}"/> &nbsp; <s:actionmessage theme="simple"/>
        </div>
    </s:if>
    
<s:form name="tenderFileForm" theme="simple" onsubmit="return validateBeforeSubmit();">
<s:token/>
<s:push value="model">
<!--<s:if test="%{fileNumber!=null}">
	<s:hidden name="id"/>
</s:if> -->
	<s:hidden id="additionalRuleValue" name="additionalRuleValue" value="%{additionalRuleValue}" />
	<s:hidden id="amountRuleValue" name="amountRuleValue" value="%{amountRuleValue}" />
	<s:hidden name="id" value="%{id}" id="id"/>
	<s:hidden name="tenderfileId" value="%{model.id}" id="tenderfileId"/>
	<s:hidden name="approverId" value="%{approverId}" id="approverId"/>
	<input type="hidden" id="estimateType" id="estimateType" />

	<s:hidden name="documentNumber" id="docNumber" />
	
	<div class="formmainbox"></div>
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
	<div class="datewk">
		<div class="estimateno"><s:text name="tenderfile.fileNumber" />	:<s:if test="%{not fileNumber}">&lt; <s:text name="message.notAssigned" /> &gt;</s:if>
			<s:property value="fileNumber" />
		</div>
	</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">  
          <tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
	            <div class="headplacer"><s:text name="tenderfile.header"/></div></td>
	        </tr>
	        <tr>	         
				 <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="tenderfile.fileName"/>:</td>
		         <td width="21%" class="whitebox2wk">
		         <s:textarea name="fileName" cols="32" rows="3" cssClass="selectwk" id="fileName" /></td>
		         <td width="15%" class="whiteboxwk"><s:text name="tenderfile.fileDescription"/>:</td>
		         <td width="53%" class="whitebox2wk">
		         <s:textarea name="fileDescription" cols="35" rows="3" cssClass="selectwk" id="fileDescription" /></td>
			</tr>
		    <tr>
		         <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="tenderfile.department"/>:</td>
		         <td width="21%" class="greybox2wk"><s:select id="department" name="department" headerKey="-1" headerValue="%{getText('estimate.default.select')}" 
		         cssClass="selectwk" list="%{dropdownData.departmentList}"  listKey="id" listValue="deptName" value="%{department.id}"
		         onChange="setupPreparedByList(this);clearDesignation(this);"/>
		         <div id="ajaxCall" style="display: none;">
		       		  <egov:ajaxdropdown id="preparedBy" fields="['Text','Value','Designation']" dropdownId='preparedBy' 
		        	  optionAttributes='Designation' url='estimate/ajaxEstimate!usersInExecutingDepartment.action'/>
		         </div>
		         </td>	
		         <td width="15%" class="greyboxwk"><span class="mandatory">*</span><s:text name="tenderfile.fileDate"/>:</td>
		         <td width="53%" class="greybox2wk"><s:date name="fileDate" var="fileDateFormat" format="dd/MM/yyyy"/>
		         <s:hidden name="hiddenDate" id="hiddenDate" ></s:hidden> <s:hidden name="hiddenDept" id="hiddenDept" value="%{department.id}"></s:hidden>
        		 <s:textfield name="fileDate" value="%{fileDateFormat}" id="fileDate" cssClass="selectboldwk" 
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="dateChange()"/>
        		 <a href="javascript:show_calendar('forms[0].fileDate',null,null,'DD/MM/YYYY');" id="dateHref"
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="${pageContext.request.contextPath}/image/calendar.png" id="tenderFileDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a> 
       			 </td>		         
		   </tr>
		   
		   <tr>
		   		 <td width="15%" class="whiteboxwk"><s:text name="tenderfile.remarks"/>:</td>
		         <td width="53%" class="whitebox2wk">
		         <s:textarea name="remarks" cols="35" rows="3" cssClass="selectwk" id="remarks" /></td>
		         <td class="whiteboxwk" ><s:checkbox name="quotationFlag" id="quotationFlag" value="%{quotationFlag}"   onclick="checkQuotation(this)"/></td>
				 <td class="whitebox2wk"><b><s:text name="tenderFile.isQuotation.label"/></b></td>
		   </tr>
		   
		    <tr>
		   		 <td width="15%" class="greyboxwk"><s:text name="tenderFile.newspapers"/>:</td>
		         <td colspan="3" width="85%" class="greybox2wk">		         
		         	<s:select headerValue="" headerKey="-1" multiple="true" list="dropdownData.newsPaperList" listKey="id" listValue="name" label="newsPaperId" id="newsPaperId" name="newsPaperId" value="%{newsPaperId}" />		         	
		         </td>		        
		   </tr>
		   
		    <tr>
				 <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="tenderfile.emp"/>:</td>
		         <td width="21%" class="whitebox2wk">
		         	<s:if test="%{(sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED') || sourcepage=='search' }" >
		                <s:textfield id="preparedByTF" value="%{preparedBy.employeeName}" type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" />
		                <s:hidden name="preparedBy" id="preparedBy" value="%{preparedBy.id}"/>
	            	</s:if>
	            	<s:else>
				         <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="preparedBy" 
				         id="preparedBy" value="%{preparedBy.id}" cssClass="selectwk" 
				         list="dropdownData.preparedByList" listKey="id" listValue="employeeName" onchange='showDesignation(this);'/>
			        </s:else> 
		         </td>
		         <td width="15%" class="whiteboxwk"><s:text name="tenderfile.desg"/>:</td>
		         <td width="53%" class="whitebox2wk"><s:textfield name="designation" type="text"  readonly="true"
		          cssClass="selectboldwk" id="designation" size="45" tabIndex="-1" value="%{designation}"/></td>
			</tr>
	
		   </table>
		</td>
        </tr>
        <tr><td>&nbsp;</td></tr>
         <tr>
         	<td colspan="4" align="center">
        		<input type="button" class="buttonadd" value="Search Estimate" id="searchEstimateButton" name="searchEstimateButton" onclick="goToPath();return false;"/>
        		<input type="button" class="buttonadd" value="Search Indents" id="searchIndentButton" name="searchIndentButton" onclick="getIndents();return false;"/>
         	</td>
         </tr>
         <tr>
            <td colspan="4">
            <div id="tenderFile_details">
      	<%@ include file="tenderFile-details.jsp"%>  
            </div>        
            </td> 
          </tr> 
    </table>
    <tr><td>&nbsp;</td></tr>
    
    
 	 <s:if test="%{sourcepage!='search'}" >
 	
	 	 <tr> 
		    <td>
		    	<div id="manual_workflow">
         			<c:set var="approverHeadCSS" value="headingwk" scope="request" />
         			<c:set var="approverCSS" value="bluebox" scope="request" />
					<%@ include file="/commons/commonWorkflow.jsp"%>
					 <script>
 	 </script>
  				</div>
 		    </td>
            </tr>
         </s:if> 
     <tr>
     	<td>
     		<div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div>
     	</td>
     </tr>     
   </div><!-- end of rbroundbox2 -->
   <div class="rbbot2"><div></div></div>
   </div><!-- end of insidecontent -->
   </div><!-- end of formmainbox -->
   <div class="buttonholderwk" id="buttons">
   <input type="hidden" name="actionName" id="actionName"/> 
   <s:if test="%{hasErrors() || sourcepage=='inbox' || egwStatus==null || 	egwStatus.code=='NEW' || egwStatus.code=='REJECTED'}">
			
			<s:if test="%{model.id==null || model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED'}">
 					<s:submit type="submit" cssClass="buttonfinal"
						value="Save" id="save" name="Save"
						method="save"
						onclick="enableSelect();document.tenderFileForm.actionName.value='Save';return validate('%{name}');"/>
 			</s:if>
 			<span id="spilloverApprove" style="display:none" >
 			<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
 			<s:submit type="submit" cssClass="buttonfinal"
					value="Approve" id="Approve" name="Approve"
					method="save"
					onclick="document.tenderFileForm.actionName.value='Approve';return validate('Approve');" />
			 </s:if>
			 <s:if test="%{model.egwStatus.code=='NEW'}">
			  		<s:submit type="submit" cssClass="buttonfinal"
					value="Cancel" id="Cancel" name="Cancel"
					method="save"
					onclick="document.tenderFileForm.actionName.value='Cancel';return validate('Cancel');" />
			 </s:if> 
			 </span>
			<span id="normalbuttons" >				
 			<s:iterator value="%{getValidActions()}" var="name">
				<s:if test="%{name!=''}">
					<s:submit type="submit" cssClass="buttonfinal"
						value="%{name}" id="%{name}" name="%{name}"
						method="save"
						onclick="document.tenderFileForm.actionName.value='%{name}';return validate('%{name}');" />
				</s:if>
			</s:iterator>  
			</span> 
	</s:if>
 
	<s:if test="%{model.id==null}"> 
	  <input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="button"
	   onclick="window.open('${pageContext.request.contextPath}/tender/tenderFile!newform.action','_self');"/>
	 </s:if>
	 
	<s:if test="%{model.id!=null}"> 
	  <input type="button" class="buttonpdf" value="VIEW TENDER FILE PDF" id="tenderFilePdfButton" name="tenderFilePdfbutton"
	   onclick="window.open('${pageContext.request.contextPath}/tender/tenderFile!viewTenderFilePdf.action?tenderFileId=<s:property value='%{model.id}'/>');" />
	 </s:if>
	 
	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='tenderfile.close.confirm'/>');"/>
	
	
  <s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (egwStatus.code=='APPROVED' || egwStatus.code=='CANCELLED'))}">
  	<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocumentManager(dom.get('docNumber').value);return false;" />
  </s:if>
  <s:else>
	<input type="submit" class="buttonadd" value="Upload Document" id="worksDocUploadButton" onclick="showDocumentManager();return false;" />
  </s:else>
	
</div>
</s:push>
</s:form>

<script>
<s:if test="%{sourcepage=='search'}">
	for(i=0;i<document.tenderFileForm.elements.length;i++){
		document.tenderFileForm.elements[i].disabled=true;
		document.tenderFileForm.elements[i].readonly=true;
	}
	if(dom.get("closeButton")!=null){
		dom.get("closeButton").disabled=false;
	}
	if(dom.get("docViewButton")!=null){
		dom.get("docViewButton").disabled=false;
	} 			
	if(dom.get("tenderFilePdfButton")!=null){
		dom.get("tenderFilePdfButton").disabled=false;
	}  
	 			
//	hideElements(['workflowDetials']);
</s:if>
	setEstimateType();
</script>

</body>
</html>