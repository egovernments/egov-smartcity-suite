<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
<head>
	<title><s:text name="create.wp"/></title>
</head>
<body onload="populateDetails();load();">
<script src="<egov:url path='js/helper.js'/>"></script>
<script src="<egov:url path='js/works.js'/>"></script>
<script type="text/javascript">

function load(){
<s:if test="%{sourcepage=='search'}">
       toggleFields(true,[]);
      links=document.workspackageForm.getElementsByTagName("a"); 
	  for(i=0;i<links.length;i++){	
		if(links[i].id.indexOf("header_")!=0 && links[i].id!='aprdDatelnk')
     				links[i].onclick=function(){return false;};     		  
			}
    
 </s:if>
  
<s:if test="%{sourcepage=='search'}">
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
	<s:if test="%{createdBySelection.toLowerCase().equals('no')}">
		dom.get('department').disabled=true;
		dom.get('preparedBy').disabled=true;
	</s:if>
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
function setupPreparedByList(elem){
	deptId=elem.options[elem.selectedIndex].value;
    populatepreparedBy({executingDepartment:deptId});
    if(dom.get("estimateListTable")!=null && dom.get("estimateListTable").rows.length>0){
        dom.get("popUpDiv").innerHTML="";
     	dom.get("popUpDiv").innerHTML='<s:text name="reset.on.dept"/>'+dom.get("msgDiv").innerHTML ;
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
	        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
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
		         <td width="21%" class="greybox2wk"><s:select id="department" name="userDepartment" headerKey="-1" headerValue="---select---" 
		         cssClass="selectwk" list="%{dropdownData.departmentList}"  listKey="id" listValue="deptName" value="%{userDepartment.id}"
		         onChange="setupPreparedByList(this);clearDesignation(this);"/>
		         <div id="ajaxCall" style="display: none;">
		       		  <egov:ajaxdropdown id="preparedBy" fields="['Text','Value','Designation']" dropdownId='preparedBy' 
		        	  optionAttributes='Designation' url='estimate/ajaxEstimate!usersInExecutingDepartment.action'/>
		         </div>
		         </td>
		         <td width="15%" class="greyboxwk"><span class="mandatory">*</span><s:text name="wp.date"/>:</td>
		         <td width="53%" class="greybox2wk"><s:date name="model.packageDate" var="packageDateFormat" format="dd/MM/yyyy"/>
		         <s:hidden name="hiddenDate" id="hiddenDate" ></s:hidden> <s:hidden name="hiddenDept" id="hiddenDept" value="%{userDepartment.id}"></s:hidden>
        		 <s:textfield name="packageDate" value="%{packageDateFormat}" id="packageDate" cssClass="selectboldwk" 
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="dateChange()"/>
        		 <a href="javascript:show_calendar('forms[0].packageDate',null,null,'DD/MM/YYYY');" id="dateHref"
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="${pageContext.request.contextPath}/image/calendar.png" id="wpDateImg" alt="Calendar" width="16" height="16" 
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
		         
		         <td width="15%" class="whiteboxwk">&nbsp;</td>
		         <td width="53%" class="whitebox2wk">&nbsp;</td>
		   </tr>
		   <tr>
				 <td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="wp.emp"/>:</td>
		         <td width="21%" class="greybox2wk">
		         <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="empId" 
		         id="preparedBy" value="%{empId}" cssClass="selectwk" 
		         list="dropdownData.preparedByList" listKey="id" listValue="employeeName" onchange='showDesignation(this);'/></td>
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
    
    
	 <%-- tr> 
		    <td>
		    <div id="manual_workflow">
		   		<%@ include file="../workflow/workflow.jsp"%> 
		    </div>
		    </td>
            </tr>	
     <tr  --%>
     
    <td><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
          </tr>     
   </div><!-- end of rbroundbox2 -->
   <div class="rbbot2"><div></div></div>
   </div><!-- end of insidecontent -->
   </div><!-- end of formmainbox -->
   <div class="buttonholderwk" id="buttons">
   <input type="hidden" name="actionName" id="actionName"/> 
   <s:if test="%{hasErrors() || sourcepage=='inbox' || model.currentState==null || 
	model.currentState.value=='NEW' || model.currentState.value=='REJECTED'}">
   <s:iterator value="%{validActions}"> 
	  <s:if test="%{description!=''}">
	  	<s:if test="%{description=='CANCEL' && model.wpNumber!=null}">
			<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="cancel" onclick="return validateCancel();enableSelect();document.forms[0].actionName.value='%{name}'"/>
	  	</s:if>
	    <s:else>
	  	    <s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="save" onclick="enableSelect();document.forms[0].actionName.value='%{name}';"/>
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
	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='wp.close.confirm'/>');"/>
	
	
  <s:if test="%{sourcepage=='search' || (sourcepage=='inbox' && (model.currentState.value=='APPROVED' || model.currentState.value=='CANCELLED'))}">
  	<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocumentManager(dom.get('docNumber').value);return false;" />
  </s:if>
  <s:else>
	<input type="submit" class="buttonadd" value="Upload Document" id="worksDocUploadButton" onclick="showDocumentManager();return false;" />
  </s:else>
	
</div>
</s:form>
</body>
</html>
