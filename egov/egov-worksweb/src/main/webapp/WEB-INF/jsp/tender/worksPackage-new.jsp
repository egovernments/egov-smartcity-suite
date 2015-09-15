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
<head>
	<title><s:text name="create.wp"/></title>
</head>
<body onload="populateDetails();load();noBack();" onpageshow="if(event.persisted) noBack();" onunload="">
<script src="<egov:url path='resources/js/works.js'/>"></script>

<script type="text/javascript">
jQuery("#loadingMask").remove();

window.history.forward(1);
function noBack() {
	window.history.forward(); 
}
function load(){
<s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (model.egwStatus!=null && !(model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED')))}">
       toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
       
      links=document.workspackageForm.getElementsByTagName("a"); 
	  for(i=0;i<links.length;i++){	
		if(links[i].id.indexOf("header_")!=0 && links[i].id!='aprdDatelnk')
     				links[i].onclick=function(){return false;};     		  
		}

	  enabledivChilderns("buttons");
 </s:if>
}


designationLoadHandler = function(req,res){ 
  results=res.results;
  dom.get("designation").value=results[0].Designation;
}

designationLoadFailureHandler= function(){
    dom.get("wp_error").style.display='';
	document.getElementById("wp_error").innerHTML='<s:text name="unable.des"/>';
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
	if(dom.get("packageDate").value=='') {
		 <s:if test="%{model.packageDate==null}">
			document.workspackageForm.packageDate.value=getCurrentDate();
		 </s:if>
	}
	document.forms[0].hiddenDate.value=document.workspackageForm.packageDate.value;
	/*<s:if test="%{createdBySelection.toLowerCase().equals('no')}">
		dom.get('department').disabled=true;
		dom.get('preparedBy').disabled=true;
	</s:if>*/
	<s:else>
		dom.get("ajaxCall").style.display='';
	</s:else>
	<s:if test="%{editableDate.toLowerCase().equals('yes')}">
		dom.get('packageDate').disabled=false;
		dom.get("wpDateImg").style.display='';
	</s:if>
	<s:else>
		dom.get('packageDate').disabled=true;
		dom.get("wpDateImg").style.display='none';
	</s:else>
}
function enableSelect(){
   	for(i=0;i<document.workspackageForm.elements.length;i++){
    document.workspackageForm.elements[i].disabled=false;
	}
}

function disableSelect(){
   	for(i=0;i<document.workspackageForm.elements.length;i++){
    document.workspackageForm.elements[i].disabled=false;
	}
}

function disableLinks() {
	var links=document.workspackageForm.getElementsByTagName("a");
	for(i=0;i<links.length;i++){
  			links[i].onclick=function(){return false;};
	}
}

function enableButtons() {
	if(dom.get('save')!=null) {
		dom.get('save').disabled=false;
	}
	if(dom.get('submit_for_approval')!=null) {
		dom.get('submit_for_approval').disabled=false;
	}
	if(dom.get('clearButton')!=null) {
		dom.get('clearButton').disabled=false;
	}
	if(dom.get('closeButton')!=null) {
		dom.get('closeButton').disabled=false;
	}
	if(dom.get('worksDocUploadButton')!=null) {
		dom.get('worksDocUploadButton').disabled=false;
	}
	if(dom.get('reject')!=null) {
		dom.get('reject').disabled=false;
	}
	if(dom.get('cancel')!=null) {
		dom.get('cancel').disabled=false;
	}
	if(dom.get('pdfButton')!=null) {
		dom.get('pdfButton').disabled=false;
	}
	if(dom.get('docViewButton')!=null) {
		dom.get('docViewButton').disabled=false;
	}

}

function setupPreparedByList(elem){
	deptId=elem.options[elem.selectedIndex].value;
    populatepreparedBy({executingDepartment:deptId,employeeCode:dom.get("loggedInUserEmployeeCode").value});
    if(dom.get("estimateListTable")!=null && dom.get("estimateListTable").rows.length>0){
        dom.get("popUpDiv").innerHTML="";
     	dom.get("popUpDiv").innerHTML='<s:text name="reset.on.dept"/>'+dom.get("msgDiv").innerHTML ;
		popup('popUpDiv')
	}
	
}

function populatepreparedBy(params){
	makeJSONCall(['Text','Value','Designation'],'/egworks/estimate/ajaxEstimate!usersInExecutingDepartment.action',params,preparedBySuccessHandler,preparedByFailureHandler) ;
}

preparedBySuccessHandler=function(req,res){ 
	enablePreparedBy(); 
	preparedByDropdown=dom.get("preparedBy");
	var resLength =res.results.length+1;
	var dropDownLength = preparedByDropdown.length;
	for(i=0;i<res.results.length;i++){
	preparedByDropdown.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
	if(res.results[i].Value=='null') preparedBy.Dropdown.selectedIndex = i;
		preparedByDropdown.options[i+1].Designation=res.results[i].Designation;
	}
	while(dropDownLength>resLength)
	{
		preparedByDropdown.options[res.results.length+1] = null;
		dropDownLength=dropDownLength-1;
	}
	document.getElementById('preparedBy').value='<s:property value="%{empId}" />';
	if(res.results.length == 1) {
		disablePreparedBy();
		document.getElementById('designation').value='<s:property value="%{designation}" />';
	}
}
preparedByFailureHandler=function(){
	alert('Unable to load preparedBy');
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
     document.forms[0].hiddenDate.value=document.forms[0].packageDate.value;
     dom.get('hiddenDept').value=dom.get('department').value;
  }
function dateChange(){
    var pacDate=document.forms[0].packageDate.value;
	var hiddenpacDate=document.forms[0].hiddenDate.value;
	if(pacDate!='' && hiddenpacDate!= '' && hiddenpacDate!=pacDate){
	     if(dom.get("estimateListTable")!=null && dom.get("estimateListTable").rows.length>0){
	      	dom.get("popUpDiv").innerHTML="";
	        dom.get("popUpDiv").innerHTML='<s:text name="reset.on.date"/>'+dom.get("msgDiv").innerHTML ;
	       	popup('popUpDiv');
		 }	
	}
}
function validateBeforeSubmit()
{
	   if(dom.get("estimateListTable")==null){
	  	dom.get("wp_error").innerHTML='<s:text name="estimates.null"/>'; 
        dom.get("wp_error").style.display='';
		return false;
	  }
	  else if(dom.get("estimateListTable").rows.length<=0)
	  {
	  	 dom.get("wp_error").innerHTML='<s:text name="estimates.null"/>'; 
         dom.get("wp_error").style.display='';
		 return false;
	  }
	  if(dom.get("packageDate").value==""){
	  	 dom.get("wp_error").innerHTML='<s:text name="wp.packageDate.is.null"/>'; 
         dom.get("wp_error").style.display='';
		 return false;
	  }
	  if(dom.get("name").value==""){
	  	 dom.get("wp_error").innerHTML='<s:text name="wp.name.is.null"/>'; 
         dom.get("wp_error").style.display='';
		 return false;
	  }
	  if(dom.get("preparedBy").value==-1){
	  	 dom.get("wp_error").innerHTML='<s:text name="wp.preparedBy.is.null"/>'; 
         dom.get("wp_error").style.display='';
		 return false;
	  }
	  if(dom.get("department").value==-1){
	  	 dom.get("wp_error").innerHTML='<s:text name="wp.userDepartment.is.null"/>'; 
         dom.get("wp_error").style.display='';
		 return false;
	  }
	   dom.get("wp_error").style.display='none';
	   dom.get("wp_error").innerHTML='';

	   jQuery(".commontopyellowbg").prepend('<div id="loadingMask" style="display:none;overflow:none;scroll:none;" ><img src="/egi/images/bar_loader.gif"> <span id="message">Please wait....</span></div>')
	   doLoadingMask();
	   
	   return true;
}

function rollbackChanges(){
	document.workspackageForm.packageDate.value=document.forms[0].hiddenDate.value;
	if(dom.get('hiddenDept').value!="")
		dom.get('department').value=dom.get('hiddenDept').value;
}
function validateCancel() {
	var msg='<s:text name="wp.cancel.confirm"/>';
	var estNo='<s:property value="model.wpNumber"/>'; 
	if(!confirmCancel(msg,estNo)) {
		return false;
	}
	else {
		return true;
	}
}

function validate(text){
	if(!validateUser(text))
		return false;
	enableSelect();
	return true;
}

function uniqueCheckOntenderFileNumber(obj)
{
	tenderFileNumber = dom.get('tenderFileNumber').value;
	var id=null;
	if(dom.get('id')!=null){
		id = dom.get('id').value;
	}else{
	id=0;
	}
	
	if(tenderFileNumber!=''){
		populatewp_error({tenderFileNumber:tenderFileNumber,id:id});
	}else{
		dom.get("wp_error").style.display = "none";
	}
} 

function disableDepartment(){
	document.getElementById('department').disabled = true;
}

function enableDepartment(){
	document.getElementById('department').disabled = false;
}

function disablePreparedBy(){
	document.getElementById('preparedBy').disabled = true;
}

function enablePreparedBy(){
	document.getElementById('preparedBy').disabled = false;
}


</script>
<div class="errorstyle" id="wp_error" style="display: none;"> 
<s:text name="wp.tenderfilenumber.isunique"/>
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
        	<s:property value="%{model.wpNumber}"/> &nbsp; <s:actionmessage theme="simple"/>
        </div>
    </s:if>
    
<s:form name="workspackageForm" theme="simple" onsubmit="return validateBeforeSubmit();">
<s:token/>
<s:if test="%{model.wpNumber!=null}">
	<s:hidden name="id" id="id" value="%{model.id}"/>
</s:if>

	<s:hidden name="model.documentNumber" id="docNumber" />
	
	<div class="formmainbox"></div>
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">          
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egi/resources/erp2/images/arrow.gif" /></div>
	            <div class="headplacer"><s:text name="wp.header"/></div></td>
	        </tr>
	        <tr>
				 <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="wp.name"/>:</td>
		         <td width="21%" class="whitebox2wk">
		         <s:textarea name="model.name" cols="32" rows="4" cssClass="selectwk" id="name" /></td>
		         <td width="15%" class="whiteboxwk"><s:text name="wp.des"/>:</td>
		         <td width="53%" class="whitebox2wk">
		         <s:textarea name="model.description" cols="35" rows="4" cssClass="selectwk" id="description" /></td>
			</tr>
		    <tr>
		         <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="wp.dept"/>:</td>
		         <td width="21%" class="greybox2wk"><s:select id="department" name="department" headerKey="-1" headerValue="---select---" 
		         cssClass="selectwk" list="%{dropdownData.departmentList}"  listKey="id" listValue="deptName" value="%{department.id}"
		         onChange="setupPreparedByList(this);clearDesignation(this);"/>
		         <s:hidden name="loggedInUserEmployeeCode" id="loggedInUserEmployeeCode"/>   
		          <s:if test="%{dropdownData.departmentList.size==1}" >
	                <script>
	                	disableDepartment(); 
	                </script>
                  </s:if>
		         </td>
		         <td width="15%" class="greyboxwk"><span class="mandatory">*</span><s:text name="wp.date"/>:</td>
		         <td width="53%" class="greybox2wk"><s:date name="model.packageDate" var="packageDateFormat" format="dd/MM/yyyy"/>
		         <s:hidden name="hiddenDate" id="hiddenDate" ></s:hidden> <s:hidden name="hiddenDept" id="hiddenDept" value="%{department.id}"></s:hidden>
        		 <s:textfield name="packageDate" value="%{packageDateFormat}" id="packageDate" cssClass="selectboldwk" 
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="dateChange()"/>
        		 <a href="javascript:show_calendar('forms[0].packageDate',null,null,'DD/MM/YYYY');" id="dateHref"
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="/egi/resources/erp2/images/calendar.png" id="wpDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a>
       			 </td>
		   </tr>
		   <s:hidden name="scriptName" id="scriptName" value="WorksPackage"></s:hidden>
		   
		   <tr>
		         <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="wp.tenderfilenumber"/>:</td>
		         <td width="21%" class=whitebox2wk>
		         	<s:textfield name="tenderFileNumber" value="%{tenderFileNumber}" id="tenderFileNumber" cssClass="selectwk" onblur="uniqueCheckOntenderFileNumber(this);" />
		         </td>
				 <egov:uniquecheck id="wp_error" fields="['Value']" url='tender/ajaxWorksPackage!tenderFileNumberUniqueCheck.action' />
		         <td width="11%" class="whiteboxwk">&nbsp;</td>
		         <td width="21%" class=whitebox2wk>&nbsp;</td>
		   </tr>
		   <tr>
				 <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="wp.emp"/>:</td>
		         <td width="21%" class="greybox2wk">
		         <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="empId" 
		         id="preparedBy" value="%{empId}" cssClass="selectwk" 
		         list="dropdownData.preparedByList" listKey="id" listValue="employeeName" onchange='showDesignation(this);'/></td>
		         <s:if test="%{dropdownData.preparedByList.size==1}" >
	                <script>
	               	 disablePreparedBy(); 
	                </script>
                  </s:if>
		         <td width="15%" class="greyboxwk"><s:text name="wp.desg"/>:</td>
		         <td width="53%" class="greybox2wk"><s:textfield name="designation" type="text"  readonly="true"
		          cssClass="selectboldwk" id="designation" size="45" tabIndex="-1" value="%{designation}"/></td>
			</tr>
			<tr>
		         <td width="11%" class="whiteboxwk">&nbsp;</td>
		         <td width="21%" class=whitebox2wk>&nbsp;</td>
		         <td width="15%" class="whiteboxwk">&nbsp;</td>
		         <td width="53%" class="whitebox2wk">&nbsp;</td>
		   </tr>
		   </table>
		</td>
        </tr>
         <tr>
            <td colspan="4">
            <div id="wp_details">
           		<%@ include file="worksPackage-Details.jsp"%>  
            </div>        
            </td> 
          </tr> 
    </table>
    <tr><td>&nbsp;</td></tr>
    
    
	 <tr> 
		    <td>
		    <div id="manual_workflow">
		   		<%@ include file="../workflow/workflow.jsp"%> 
		    </div>
		    </td>
    </tr>	
     <tr>
     
    <td><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
          </tr>     
   </div><!-- end of rbroundbox2 -->
   <div class="rbbot2"><div></div></div>
   </div><!-- end of insidecontent -->
   </div><!-- end of formmainbox -->
   <div class="buttonholderwk" id="buttons">
   <input type="hidden" name="actionName" id="actionName"/> 
   <s:if test="%{(hasErrors() || sourcepage=='inbox' || model.egwStatus==null || 
	model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED' || model.egwStatus.code=='CREATED') 
	&& (sourcepage=='inbox' || model.egwStatus==null)}">
  
   <s:iterator value="%{validActions}"> 
	  <s:if test="%{description!=''}">
	  	<s:if test="%{description=='CANCEL' && model.wpNumber!=null}">
			<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="cancelWorkflow" onclick="return validateCancel();enableSelect();document.forms[0].actionName.value='%{name}'"/>
	  	</s:if>
	    <s:else>
	  	    <s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="save" onclick="document.forms[0].actionName.value='%{name}';return validate('%{name}');"/>
	 </s:else>
	  </s:if>
	</s:iterator>
</s:if>
	<s:if test="%{model.id==null}"> 
	  <input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="button"
	   onclick="window.open('${pageContext.request.contextPath}/tender/worksPackage!newform.action','_self');"/>
	 </s:if>
	 <s:if test="%{model.id!=null}">
	   <input type="button" onclick="window.open('${pageContext.request.contextPath}/tender/worksPackage!viewWorksPackagePdf.action?id=<s:property value='%{model.id}'/>');" 
	   class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
	 </s:if>
	 <s:if test="%{sourcepage!='search'}">
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='wp.close.confirm'/>');"/>
	</s:if>
	<s:else>
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
	</s:else>
	
  <s:if test="%{sourcepage=='search' 
  	|| (sourcepage=='inbox' && (model.egwStatus!=null && (model.egwStatus.code=='APPROVED' || model.egwStatus.code=='CANCELLED')))}">
  	<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocumentManager(dom.get('docNumber').value);return false;" />
  </s:if>
  <s:else>
	<input type="submit" class="buttonadd" value="Upload Document" id="worksDocUploadButton" onclick="showDocumentManager();return false;" />
  </s:else>
	
</div>
</s:form>
<script>
if(dom.get("departmentid")!=null) {
	if(dom.get("departmentid").value!=-1) {
		populateDesignation();
	}
}
<s:if test="%{sourcepage=='inbox' && (model.egwStatus!=null && (model.egwStatus.code!='NEW' || model.egwStatus.code!='REJECTED'))}">
	disableSelect();
	enableButtons();
	showElements(['approverCommentsRow']);

</s:if>
<s:if test="%{sourcepage=='search'}">
	hideElements(['workflowDetials']);
	hideElements(['approverCommentsRow']);
	disableSelect();
	disableLinks();
	enableButtons();	
</s:if>
<s:if test="%{sourcepage=='inbox' && (model.egwStatus!=null && model.egwStatus.code=='CHECKED')}">
	disableSelect();
	disableLinks();
	enableButtons();
	hideElements(['workflowDetials']);
	showElements(['approverCommentsRow']);
</s:if>
</script>
</body>
</html>
