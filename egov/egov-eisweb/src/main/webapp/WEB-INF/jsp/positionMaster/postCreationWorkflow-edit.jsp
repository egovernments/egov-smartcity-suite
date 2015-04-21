<!-- #-------------------------------------------------------------------------------
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
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="post.creation"/></title>

<script language="JavaScript"  type="text/JavaScript">

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
	if(document.getElementById('mode').value=='modify')
	{	
		document.getElementById('postName').focus();
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
 		if(document.postCreation.currentState.value=='Rejected')
 		{
 			confirMess='<s:text name="post.cancel.position"/>';
 		}	
 		else
 		{
 			confirMess='<s:text name="post.reject.position"/>';
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
		showError('<s:text name="post.select.dept"/>');
		return false;
	}

	if(""==document.getElementById('designationId').value || document.getElementById('designationId').value=='0' )
	{
		showError('<s:text name="post.select.desig"/>');
		return false;
	}

	if(""==document.getElementById('postName').value  )
	{
		showError('<s:text name="post.enter.postname"/>');
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

function validatePostName()
{
	var posName = document.getElementById('postName').value;
	populatepostnameunique({positionName:posName});
}


</script>
</head>
<body onload="refreshInbox();">
	<div class="errorcss" id="wf_error" style="display:none;"></div>
	<s:form theme="simple" name="postCreation">
	
	<s:token/>
	<s:push value="model">
	<s:actionmessage />
	<s:if test="%{hasErrors()}">
		<div class="errorcss" id="fieldError">
			<s:fielderror cssClass="errorcss"/>
			<s:actionerror cssClass="errorcss"/>
		</div>
	</s:if>
	<s:hidden id="postCurrentState" name="postCurrentState" value="%{postCurrentState}"/>
	<s:hidden id="mode" name="mode" value="%{mode}"/>
	<s:hidden id="id" name="id" value="%{id}"/>
	<div class="errorcss" style="display:none" id="postnameunique" >
		<s:text name="post.position.exists"/>  
	</div>
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
						<s:text name="post.details" />
					</div>
				</td>
				<td></td>
			</tr>
		</tbody>
	</table>
	<br/>
	
	 <div id="leftNaviProposal"   style="overflow-y:auto;overflow-x:auto  "> 
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tbody>	
					<tr>
						
							<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="post.dept" /></td>
							<td class="whitebox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.departmentlist" listKey="id"
									listValue="deptName" id="deptId"
									name="deptId"  value="%{deptId.id}" cssClass="selectwk"/>
							</td>
									
							<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="post.desig" /></td>
							<td class="whitebox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.designationlist" listKey="designationId"
									listValue="designationName" id="designationId"
									name="designationId" value="%{designationId}" cssClass="selectwk">
								</s:select></td>
						</tr>
						
						<tr>
							<td class="greyboxwk"><span class="mandatory">*</span><s:text name="post.name"/></td>
							<td class="greybox2wk"><s:textfield id="postName" cssClass="selectwk" name="postName" value="%{postName}" style="width:265px;" onblur="validatePostName();"/>
								<egovtags:uniquecheck id="postnameunique" fieldtoreset="postName" fields="['Value']" url='positionMaster/postCreationWorkflow!isUniquePostName.action' />
							</td>
							<td class="greyboxwk"><s:text name="post.qualify.details"/></td>
							<td class="greybox2wk"><s:textarea id="qualificationDetails" name="qualificationDetails" value="%{qualificationDetails}" style="width: 265px; height: 56px;"/>
						</tr>	
						
						<tr>
							<td class="whiteboxwk"><s:text name="post.remarks" /></td>
							<td colspan=3" class="whitebox2wk"><s:textarea id="remarks" name="remarks" value="%{remarks}" style="width: 265px; height: 56px;"/>
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
	   			<c:import url="/commons/commonWorkflow-eis.jsp" context="/eis" />
	   		</s:if>
		</div>
	</s:if>	
  	</div> 
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
		<div align="right" class="mandatory">* Mandatory Fields</div>
	</s:if>
	</s:form>    

</body>
</html>
