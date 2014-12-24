<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="emp.reversion"/></title>

<script language="JavaScript"  type="text/JavaScript">

var codeSelectionHandler = function(sType, arguments) {
		var oData = arguments[2];
		var posDetails = oData[0];
		var splitResult = posDetails.split("~");
		document.getElementById('empPos').value = splitResult[0];
		document.getElementById('position').value = splitResult[1];
	}

	var codeSelectionEnforceHandler = function(sType, arguments) {
		warn('impropercodeSelection');
	}

function showError(msg)
{
	document.getElementById("wf_error").style.display = '';	
	dom.get("wf_error").innerHTML = msg;
	window.scroll(0,0);
	return false;
}

function refreshInbox()
{
	if(opener.top.document.getElementById('inboxframe')!=null)
	{    	
		if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
		{    		
		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
		}
	
	}
}

function validateForm(actionName)
{		
	document.getElementById("wf_error").style.display = 'none';
	document.getElementById("workFlowAction").value   = actionName;
	if(!validation(actionName))
		return false ;
	if(actionName == "Forward" || actionName == "Approve")
	{
    	if(!validateWorkFlowApprover(actionName,'wf_error'))
    			return false ;
    }
	if(actionName=='Reject')
	{
		var confirMess;
 		if(document.revProcess.currentState.value=='Rejected')
 		{
 			confirMess='<s:text name="cancel.reversion"/>';
 		}	
 		else
 		{
 			confirMess='<s:text name="reject.reversion"/>';
 		}
		if(confirm(confirMess))
		{
			return true;
		}
		else
		{
			return false;
		}	
	}	

	return true;	    
}

function validation(obj)
{
	if(""==document.getElementById('deptId').value || document.getElementById('deptId').value=='0' )
	{
		showError('<s:text name="rev.select.dept"/>');
		return false;
	}

	if(""==document.getElementById('designationId').value || document.getElementById('designationId').value=='0' )
	{
		showError('<s:text name="rev.select.desig"/>');
		return false;
	}

	if(""==document.getElementById('empPos').value  )
	{
		showError('<s:text name="rev.enter.postname"/>');
		return false;
	}


	if(""==document.getElementById('requestDateId').value  )
	{
		showError('<s:text name="rev.enter.requestdate"/>');
		return false;
	}

	if(""==document.getElementById('reversionEffDateId').value  )
	{
		showError('<s:text name="rev.enter.effdate"/>');
		return false;
	}
	
	
	<s:if test="%{getNextAction()!='END'}">
	if(obj!='Reject')
	{	
		if(!callAlertForDepartment())
		{
			return false;
		}
		if(!callAlertForDesignation())
		{
			return false;
		}
	}	
	</s:if>		

	return true;
}


function loadDesignationFromMatrix()
{
		<s:if test="%{getNextAction()!='END' && mode!='view'}">	
	      var currentState=document.getElementById('currentState').value;
	      var amountRule=document.getElementById('amountRule').value;
	      var additionalRule=document.getElementById('additionalRule').value;
	      var pendingAction=document.getElementById('pendingActions').value;
	      var dept="";
	      loadDesignationByDeptAndType('EmpPosition',dept,currentState,amountRule,additionalRule,pendingAction); 
	    </s:if>
}

function populateApprover()
{
	getUsersByDesignationAndDept();
}

function generateParameter()
{
	var deptId = document.getElementById('deptId').value;
	var desigId = document.getElementById('designationId').value;
	return "approverDeptId="+deptId+"&approverDesigId="+desigId;
}



</script>
</head>
<body onload="refreshInbox();">
	<div class="errorcss" id="wf_error" style="display:none;"></div>
	<s:form theme="simple" name="revProcess">
	
	<s:token/>
	<s:push value="model">
	<s:actionmessage />
	<s:if test="%{hasErrors()}">
		<div class="errorcss" id="fieldError">
			<s:fielderror cssClass="errorcss"/>
			<s:actionerror cssClass="errorcss"/>
		</div>
	</s:if>
	<s:hidden id="revCurrentState" name="revCurrentState" value="%{revCurrentState}"/>
	<s:hidden id="mode" name="mode" value="%{mode}"/>
	<s:hidden id="empId" name="empId" value="%{empId}"/>
	<s:hidden id="id" name="id" value="%{id}"/>
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<s:token/>
	<div class="rbcontent2">
	
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tbody><tr><td>&nbsp;</td></tr>
			<tr>
				<td  class="headingwk">
					<div class="arrowiconwk">
						<img src="${pageContext.request.contextPath}/common/image/arrow.gif" />
					</div>
					<div class="headplacer">
						<s:text name="emp.details" />
					</div>
				</td>
				<td></td>
			</tr>
		</tbody>
	</table>
	<br/>
	
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tbody>	
					<tr>
						
							<td class="whiteboxwk"><s:text name="emp.code" /></td>
							<td class="whitebox2wk">
								<s:textfield name="empCode" value="%{empDetails.employeeCode}" readonly="true"/>
							</td>
									
							<td class="whiteboxwk"><s:text name="emp.name" /></td>
							<td class="whitebox2wk">
								<s:textfield name="empName" value="%{empDetails.employeeName}" readonly="true" size="40"/>
							</td>
						</tr>
						
						<tr>
							<td class="greyboxwk"><s:text name="emp.dept"/></td>
							<td class="greybox2wk">
								<s:textfield name="empDept" value="%{empDetails.deptId.deptName}" readonly="true" size="40"/>
							</td>
							<td class="greyboxwk"><s:text name="emp.desig"/></td>
							<td class="greybox2wk">
								<s:textfield name="empDesig" value="%{empDetails.desigId.designationName}" readonly="true" size="40"/>
							</td>
						</tr>	
						
						<tr>
							<td class="whiteboxwk"><s:text name="emp.pos" /></td>
							<td colspan=3" class="whitebox2wk">
								<s:textfield name="empPos" value="%{empDetails.position.name}" readonly="true" size="40"/>
							</td>
						</tr>
			</tbody>
		</table>
		<br/>
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tbody><tr><td>&nbsp;</td></tr>
				<tr>
					<td  class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/common/image/arrow.gif" />
						</div>
						<div class="headplacer">
							<s:text name="reversion.details" />
						</div>
					</td>
					<td></td>
				</tr>
			</tbody>
		</table>
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tbody>	
				<s:hidden id="employee" name="employee" value="%{empId}"/>
					<tr>
							<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="emp.dept" /></td>
							<td class="whitebox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.departmentlist" listKey="id"
									listValue="deptName" id="deptId"
									name="dept"  value="%{dept.id}" cssClass="selectwk"/>
							</td>
									
							<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="emp.desig" /></td>
							<td class="whitebox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.designationlist" listKey="designationId"
									listValue="designationName" id="designationId"
									name="designationId" value="%{designationId}" cssClass="selectwk">
								</s:select>
							</td>
					</tr>
					<tr>
							<td class="greyboxwk"><span class="mandatory">*</span><s:text name="emp.pos"/></td>
							<s:hidden id="position" name="position" value="%{position.id}"/>
							<td class="greybox2wk" width="20%" valign="top" align="left">
								<div class="yui-skin-sam">
									<div id="posSearch_autocomplete" class="yui-ac">
										<s:textfield id="empPos" name="empPos" value="%{position.name}"
											 size="20" cssClass="selectwk" />
										<div id="codeSearchResults"></div>
									</div>
								</div> 
								<egovtags:autocomplete name="empPos" field="empPos"
													url="${pageContext.request.contextPath}/common/employeeSearch!getVacantPositionsList.action" 
													queryQuestionMark="true" results="codeSearchResults"
													handler="codeSelectionHandler" paramsFunction="generateParameter"
													forceSelectionHandler="codeSelectionEnforceHandler" />
										<span class='warning' id="impropercodeSelectionWarning"></span>
							</td>
							<td class="greyboxwk"><s:text name="emp.fund" /></td>
							<td class="greybox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.fundlist" listKey="id"
									listValue="name" id="fundId"
									name="fund" value="%{fund.id}" cssClass="selectwk">
								</s:select>
							</td>
					</tr>
					<tr>
							<td class="whiteboxwk"><s:text name="emp.function" /></td>
							<td class="whitebox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.functionlist" listKey="id"
									listValue="name" id="functionId"
									name="function" value="%{function.id}" cssClass="selectwk">
								</s:select>
							</td>
							<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="req.date"/> </td>   
							<td class="whitebox2wk">
									<s:date id="requestDateId"  name='requestDate' format='dd/MM/yyyy' var="rqstDate"/>
									<s:textfield  id="requestDate"  name="requestDate" value="%{rqstDate}" onblur = "validateDateFormat(this);" size="10"
									onkeyup="DateFormat(this,this.value,event,false,'3') " cssClass="selectwk"/>
							</td>
					</tr>
					<tr>
							<td class="greyboxwk"><span class="mandatory">*</span><s:text name="rev.eff.date"/> </td>   
							<td class="greybox2wk">
									<s:date id="reversionEffFromId"  name='reversionEffFrom' format='dd/MM/yyyy' var="revEffFrom"/>
									<s:textfield  id="reversionEffFrom"  name="reversionEffFrom" value="%{revEffFrom}" onblur = "validateDateFormat(this);" size="10"
									onkeyup="DateFormat(this,this.value,event,false,'3') " cssClass="selectwk"/>
							</td>
							<td class="greyboxwk"><s:text name="emp.remarks" /></td>
							<td class="greybox2wk"><s:textarea id="remarks" name="remarks" value="%{remarks}" 
								style="width: 265px; height: 56px;"/>
					</tr>	
			</tbody>
		</table>			
	</div>	
	</div>
	</div>
	</div>
	<div id="buttonDiv" align="center">		
	<s:if test="%{mode!='view'}">
	   	<div id="approverInfo">
	        <c:set var="approverHeadTDCSS" value="headingwk" scope="request" />
	        <c:set var="approverHeaderCss" value="headplacerlbl" scope="request"/>
	        <c:set var="headerImgCss" value="arrowiconwk" scope="request"/>
	        <c:set var="headerImgUrl" value="../common/image/arrow.gif" scope="request"/>
	        <c:set var="approverOddCSS" value="whiteboxwk" scope="request" />
	        <c:set var="approverOddTextCss" value="whitebox2wk" scope="request" />
	        <c:set var="approverEvenCSS" value="greyboxwk" scope="request" />
	        <c:set var="approverEvenTextCSS" value="greybox2wk" scope="request" />
	        <s:if test='%{getNextAction()!=END}'>
	        	<%@ include file="/commons/commonWorkflow-eis.jsp" %> 
	        	
	   		</s:if>
		</div>
	</s:if>	
  	</div> 
	<br/>
   <div class="buttondiv" align="center">
   	<table cellpadding="0" cellspacing="0" border="0">
	    <tr>	
	    <s:if test="%{mode!='view'}">      	
				<s:iterator value="%{getValidActions()}" var="p">
					<td>
						<s:submit type="submit" cssClass="buttonfinal" value="%{p}" id="%{p}" name="%{p}" method="save" onclick=" return validateForm('%{p}');"/>
					</td>
				</s:iterator>					
			<td>&nbsp;&nbsp;</td>
	  	</s:if>		
	  	<td>	  			
	  		<input name="closeBut" type="button" class=buttonfinal id="close" onclick="window.close()" value="Close"/>
	  	</td> 
	   </tr>
    </table>
    </div>  
	</s:push>    
	<s:if test="%{getNextAction()!='END' || getNextAction()==null}">
		<div align="right" class="mandatory">* Mandatory Fields &nbsp;  </div>
	</s:if>
	</s:form>    

</body>
</html>