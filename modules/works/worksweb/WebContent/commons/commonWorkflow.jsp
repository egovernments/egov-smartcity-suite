<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>


function getUsersByDesignationAndDept()
{
	populateapproverPositionId({approverDepartmentId:document.getElementById('approverDepartment').value,designationId:document.getElementById('approverDesignation').value});
}

function callAlertForDepartment()
{
    var value=document.getElementById("approverDepartment").value;
	if(value=="-1")
	{
		alert("Please select the Approver Department");
		document.getElementById("approverDepartment").focus();
		return false;
	}
}

function callAlertForDesignation()
{
	var value=document.getElementById("approverDesignation").value;
	if(value=="-1")
	{
		alert("Please select the approver designation");
		document.getElementById("approverDesignation").focus();
		return false;
	}
}
	
function loadDesignationByDeptAndType(typeValue,departmentValue,currentStateValue,amountRuleValue,additionalRuleValue,pendingActionsValue)
{
	  var designationObj =document.getElementById('approverDesignation');
	  designationObj.options.length = 0;
	  designationObj.options[0] = new Option("----Choose----","-1");
	  var approverObj = document.getElementById('approverPositionId');
	  approverObj.options.length = 0;
	  approverObj.options[0] = new Option("----Choose----","-1");
	  populateapproverDesignation({departmentRule:departmentValue,type:typeValue,amountRule:amountRuleValue,additionalRule:additionalRuleValue,
	  													currentState:currentStateValue,pendingAction:pendingActionsValue});
}

function loadDesignationByDeptAndTypeWithDate(typeValue,departmentValue,currentStateValue,amountRuleValue,additionalRuleValue,pendingActionsValue,date)
{
	  var designationObj =document.getElementById('approverDesignation');
	  designationObj.options.length = 0;
	  designationObj.options[0] = new Option("----Choose----","-1");
	  var approverObj = document.getElementById('approverPositionId');
	  approverObj.options.length = 0;
	  approverObj.options[0] = new Option("----Choose----","-1");
	  populateapproverDesignation({departmentRule:departmentValue,type:typeValue,amountRule:amountRuleValue,additionalRule:additionalRuleValue,
	  													currentState:currentStateValue,pendingAction:pendingActionsValue,date:date});
}

/**
 *   This method is to validate Approver
 */
	
function validateWorkFlowApprover(name)
{
    document.getElementById('workFlowAction').value=name;
	<s:if test="%{getNextAction()!='END'}">
    if((name=="Forward" || name=="Approve" || name=="approve" || name=="forward") && document.getElementById('approverPositionId').value=="-1")
    {
        alert("Please Select the Approver ");
		return false;
    }
    </s:if>
	
    return true;
}
function validateWorkFlowApprover(name,errorDivId)
{
	document.getElementById(errorDivId).style.display='none';
    document.getElementById('workFlowAction').value=name;
	<s:if test="%{getNextAction()!='END'}">
    if((name=="Forward" || name=="Approve" || name=="approve" || name=="forward") && 
    		document.getElementById('approverPositionId').value=="-1")
    {
        document.getElementById(errorDivId).style.display='';
        document.getElementById(errorDivId).innerHTML = "Please Select the Approver";
		return false;
    }
    </s:if>
    return true;
}
	
	
</script>
<s:if test="%{getNextAction()!='END' && getNextAction()!='Pending for Closure'}">
<div class="blueshadow"></div>
<s:hidden id="currentState" name="currentState" value="%{state.value}"/>
<s:hidden id="currentDesignation" name="currentDesignation" value="%{currentDesignation}"/>
<s:hidden id="additionalRule" name="additionalRule" value="%{additionalRule}"/>
<s:hidden id="amountRule" name="amountRule" value="%{amountRule}"/>
<s:hidden id="workFlowDepartment" name="workFlowDepartment" value="%{workFlowDepartment}"/>
<s:hidden id="workFlowAction" name="workFlowAction"/>
<s:hidden id="pendingActions" name="pendingActions" value="%{pendingActions}"/>
	<div class="errorstyle" style="display:none;" id="errorDivId"></div>
	<div id="approverDetials">
	  	 <table width="100%" border="0" cellspacing="0" cellpadding="0" >
			<tr>
                <td colspan="9" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                <div class="headplacer"><s:text name="workflow.header" />:</div></td>
             </tr>
	  	 	 <tr>   
	  	 	 <td class="greyboxwk" width="16%">&nbsp;</td>
			 <td class="greyboxwk" id="deptLabel"><s:text name="workflow.approver.department"/></td>
			 <td class="greyboxwk">
				<s:select name="approverDepartment" id="approverDepartment" list="dropdownData.approverDepartmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----"  value="%{approverDepartment}"  onchange="loadDesignationFromMatrix();"/>
				<egov:ajaxdropdown fields="['Text','Value']" url="workflow/ajaxWorkFlow!getDesignationsByObjectType.action" id="approverDesignation" dropdownId="approverDesignation" contextToBeUsed="/egi"/>
			</td>
			<td class="greyboxwk" width="10%">&nbsp;</td>	  
			<td class="greyboxwk" width="14%">Approver Designation:</td>
			<td class="greyboxwk" width="33%">
				<s:select id="approverDesignation" name="approverDesignation" list="dropdownData.desgnationList" listKey="designationId" headerKey="-1" listValue="designationName" headerValue="----Choose----" onchange="populateApprover();" onfocus="callAlertForDepartment();"/>
				<egov:ajaxdropdown id="approverPositionId" fields="['Text','Value']" dropdownId="approverPositionId" url="workflow/ajaxWorkFlow!getPositionByPassingDesigId.action" contextToBeUsed="/egi" />
			</td>
			<td class="greyboxwk" width="10%">Approver:</td>
			<td class="greyboxwk" width="16%">
			  	<s:select id="approverPositionId"  name="approverPositionId" list="dropdownData.approverList" headerKey="-1" headerValue="----Choose----" listKey="id" listValue="firstName"  onfocus="callAlertForDesignation();" value="%{approverPositionId}" /></td> 
			<td class="greyboxwk" width="16%">&nbsp;</td>
			 </tr>
		</table>
</s:if>


 <div id="workflowCommentsDiv" align="center">
         <table width="100%">
         <tr>
           <td width="10%" class="greyboxwk">&nbsp;</td>
            <td width="20%" class="greyboxwk">&nbsp;</td>
           <td class="greyboxwk" width="13%"> Approver Comments: </td>
           <td class="greyboxwk"> 
           	<textarea id="approverComments" name="approverComments" rows="2" cols="35" ></textarea>  
           </td>
           <td class="greyboxwk">&nbsp;</td>
           <td width="10%" class="greyboxwk">&nbsp;</td>
           <td  class="greyboxwk">&nbsp;</td>
           </tr>
         </table>
  </div>