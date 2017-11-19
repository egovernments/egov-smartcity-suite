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
<html>
<head>
	<title><s:text name="create.wp"/></title>
</head>
<body onload="populateDetails();load();noBack();" onpageshow="if(event.persisted) noBack();" onunload="">
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<!-- <script src="../resources/js/jquery-1.7.2.min.js"></script> -->
<script type="text/javascript" src="<c:url value='/resources/js/prototype.js'/>"></script>
<script type="text/javascript">

/* var jq=jQuery.noConflict(true);
jq("#loadingMask").remove(); */

window.history.forward(1);
function noBack() {
	window.history.forward(); 
}
function load(){
<s:if test="%{sourcepage!='search' && getNextAction()!='END'}">
	loadDesignationFromMatrix();
</s:if>
<s:if test="%{sourcepage=='inbox' && !(model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED')}">
       //toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
       toggleFields(true,['approverDepartment','approverDesignation','approverPositionId','approverComments',
		                     'Forward','Reject','button2','Approve']); 
      links=document.workspackageForm.getElementsByTagName("a"); 
	  for(i=0;i<links.length;i++){	
		if(links[i].id.indexOf("header_")!=0 && links[i].id!='aprdDatelnk')
     				links[i].onclick=function(){return false;};     		  
		}

	  enabledivChilderns("buttons");
 </s:if>
}


function populateDetails()
{
	if(dom.get("wpDate").value=='') {
		 <s:if test="%{model.wpDate==null}">
			document.workspackageForm.wpDate.value=getCurrentDate();
		 </s:if>
	}
	document.forms[0].hiddenDate.value=document.workspackageForm.wpDate.value;	
	dom.get('department').disabled=true;	
}
function enableSelect(){
   	for(i=0;i<document.workspackageForm.elements.length;i++){
    document.workspackageForm.elements[i].disabled=false;
	}
}

function disableSelect(){
   	for(i=0;i<document.workspackageForm.elements.length;i++){
    document.workspackageForm.elements[i].disabled=true;
	}
}

function disableLinks() {
	var links=document.workspackageForm.getElementsByTagName("a");
	for(i=0;i<links.length;i++){
  			links[i].onclick=function(){return false;};
	}
}

function enableButtons() { 
	if(dom.get('Save')!=null) {
		dom.get('Save').disabled=false;
	}
	if(dom.get('Forward')!=null) {
		dom.get('Forward').disabled=false;
	}
	if(dom.get('closeButton')!=null) {
		dom.get('closeButton').disabled=false;
	}
	if(dom.get('Reject')!=null) {
		dom.get('Reject').disabled=false;
	}
	if(dom.get('Cancel')!=null) {
		dom.get('Cancel').disabled=false; 
	}
	if(dom.get('pdfButton')!=null) {
		dom.get('pdfButton').disabled=false;
	}
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
     document.forms[0].hiddenDate.value=document.forms[0].wpDate.value;
     dom.get('hiddenDept').value=dom.get('department').value;
  }
function dateChange(){
    var pacDate=document.forms[0].wpDate.value;
	var hiddenpacDate=document.forms[0].hiddenDate.value;
	if(pacDate!='' && hiddenpacDate!= '' && hiddenpacDate!=pacDate){
	     if(dom.get("estimateListTable")!=null && dom.get("estimateListTable").rows.length>0){
	      	dom.get("popUpDiv").innerHTML="";
	        dom.get("popUpDiv").innerHTML='<s:text name="reset.on.date"/>'+dom.get("msgDiv").innerHTML ;
	       	popup('popUpDiv');
		 }	
	}
}
function validateBeforeSubmit() {
	  if(dom.get("wpDate").value==""){
	  	 dom.get("wp_error").innerHTML='<s:text name="wp.wpDate.is.null"/>'; 
         dom.get("wp_error").style.display='';
		 return false;
	  }
	  if(dom.get("name").value==""){
	  	 dom.get("wp_error").innerHTML='<s:text name="wp.name.is.null"/>'; 
         dom.get("wp_error").style.display='';
		 return false;
	  }
	  if(dom.get("department").value==-1){
	  	 dom.get("wp_error").innerHTML='<s:text name="wp.userDepartment.is.null"/>'; 
         dom.get("wp_error").style.display='';
		 return false;
	  }
	  if(dom.get("tenderFileNumber").value==""){
	  	 dom.get("wp_error").innerHTML='<s:text name="wp.tenderFileNumber.is.null"/>';  
         dom.get("wp_error").style.display='';
		 return false;
	  }
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
	   dom.get("wp_error").style.display='none';
	   dom.get("wp_error").innerHTML='';

	
	  //jq(".commontopyellowbg").prepend('<div id="loadingMask" style="display:none;overflow:none;scroll:none;" ><img src="/egi/images/bar_loader.gif"> <span id="message">Please wait....</span></div>')
	  //doLoadingMask();
	   
	   return true;
}

function rollbackChanges(){
	document.workspackageForm.wpDate.value=document.forms[0].hiddenDate.value;
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
	/* if(!validateUser(text))
		return false; */
	if(!validateBeforeSubmit())
		return false;
	enableSelect();
	document.workspackageForm.action='${pageContext.request.contextPath}/tender/worksPackage-save.action';
	document.workspackageForm.submit();
	return true;
}

function onSubmit() {
	action = document.getElementById("workFlowAction").value;
	return validate(action);
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
		<div class="datewk" align="left"> 
			<div class="estimateno">
				<s:text name="wp.no" /> :
				<s:if test="%{not wpNumber}">&lt; <s:text name="message.notAssigned" /> &gt;</s:if>
				<s:property value="model.wpNumber" />
			</div>
		</div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">          
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
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
		         cssClass="selectwk" list="%{dropdownData.departmentList}"  listKey="id" listValue="name" value="%{department.id}" />		          
		          <s:if test="%{dropdownData.departmentList.size==1}" >
	                <script>
	                	disableDepartment(); 
	                </script>
                  </s:if>
		         </td>
		         <td width="15%" class="greyboxwk"><span class="mandatory">*</span><s:text name="wp.date"/>:</td>
		         <td width="53%" class="greybox2wk"><s:date name="model.wpDate" var="wpDateFormat" format="dd/MM/yyyy"/>
		         <s:hidden name="hiddenDate" id="hiddenDate" ></s:hidden> <s:hidden name="hiddenDept" id="hiddenDept" value="%{department.id}"></s:hidden>
        		 <s:textfield name="wpDate" value="%{wpDateFormat}" id="wpDate" cssClass="selectboldwk" 
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="dateChange()"/>
        		 <a href="javascript:show_calendar('forms[0].wpDate',null,null,'DD/MM/YYYY');" id="dateHref"
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="/egworks/resources/erp2/images/calendar.png" id="wpDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a>
       			 </td>
		   </tr>
		   <s:hidden name="scriptName" id="scriptName" value="WorksPackage"></s:hidden>
		   
		   <tr>
		         <td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="wp.tenderfilenumber"/>:</td>
		         <td width="21%" class=whitebox2wk>
		         	<s:textfield name="tenderFileNumber" value="%{tenderFileNumber}" id="tenderFileNumber" cssClass="selectwk" onblur="uniqueCheckOntenderFileNumber(this);" />
		         </td>
				 <egov:uniquecheck id="wp_error" fields="['Value']" url='tender/ajaxWorksPackage-tenderFileNumberUniqueCheck.action' />
		         <td width="11%" class="whiteboxwk">&nbsp;</td>
		         <td width="21%" class=whitebox2wk>&nbsp;</td>
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
           		<%@ include file="worksPackage-details.jsp"%>  
            </div>        
            </td> 
          </tr> 
    </table>
    <tr><td>&nbsp;</td></tr>
    
    
	 <tr> 
		    <td>
		    <div id="manual_workflow">
		   		<s:if test="%{sourcepage!='search'}">
				<%@ include file="../workflow/commonWorkflowMatrix.jsp"%> 
				<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
		  </s:if>
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
 <%--   <input type="hidden" name="actionName" id="actionName"/> 
   <s:if test="%{(hasErrors() || sourcepage=='inbox' || model.egwStatus==null || 
	model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED' || model.egwStatus.code=='CREATED') 
	&& (sourcepage=='inbox' || model.egwStatus==null)}">
   <input type="submit" class="buttonfinal" value="SAVE" id="save" name="save" onclick="document.forms[0].actionName.value='save';return validate('save');" />	
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
</s:if> --%>
	<%-- <s:if test="%{model.id==null}"> 
	  <input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="button"
	   onclick="window.open('${pageContext.request.contextPath}/tender/worksPackage-newform.action','_self');"/>
	 </s:if> --%>
	 <s:if test="%{model.id!=null}">
	   <input type="button" onclick="window.open('${pageContext.request.contextPath}/tender/worksPackage-viewWorksPackagePdf.action?id=<s:property value='%{model.id}'/>');" 
	   class="buttonpdf" value="VIEW PDF" id="pdfButton" name="pdfButton" />
	 </s:if>
	 <s:if test="%{sourcepage=='search'}" >
		<input type="button" class="btn btn-default" value="Close" id="closeButton" name="closeButton" onclick="window.close();"/>&nbsp;
	</s:if>
	<%--  <s:if test="%{sourcepage!='search'}">
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='wp.close.confirm'/>');"/>
	</s:if>
	<s:else>
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
	</s:else>	 --%>
</div>
</s:form>
<script>

<s:if test="%{sourcepage=='inbox' && (model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED')}">
	enableSelect(); 
	enableButtons();
	//showElements(['approverComments']);

</s:if>
<s:if test="%{sourcepage=='search'}">
	//hideElements(['workflowDetials']);
	disableSelect();
	disableLinks();
	enableButtons();	
</s:if>
<s:if test="%{sourcepage=='inbox' && model.egwStatus.code=='CHECKED'}">
	disableSelect();
	disableLinks();
	enableButtons();
	//hideElements(['workflowDetials']);
	showElements(['approverComments']);
</s:if>
</script>
<script src="<c:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
</body>
</html>
