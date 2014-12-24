<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>


function populateDesignation(){	
	<s:if test="%{getNextAction()!='END'}">
		<s:if test="%{getNextAction()=='Pending Approval By HOD' || paySlipCurrentState=='Rejected'}">
			var deptId = document.getElementById('approverDepartment').value;
			if(deptId!='' ){
				populateapproverDesignation({approverDeptId:deptId});
			}
		</s:if>
		<s:else>
			loadDesignationFromMatrix();
		</s:else>
	</s:if>		
}

function getUsersByDesignationAndDept()
{
	<s:if test="%{getNextAction()=='Pending Approval By HOD' || paySlipCurrentState=='Rejected'}">
		populateapproverPositionId({approverDeptId:document.getElementById('approverDepartment').value,approverDesigId:document.getElementById('approverDesignation').value});
	</s:if>
	<s:else>
		populateapproverPositionId({approverDepartmentId:document.getElementById('approverDepartment').value,designationId:document.getElementById('approverDesignation').value});
	</s:else>
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
	return true;
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
	if(!callAlertForDesignation())
		return false;
    if((name=="Forward" || name=="Approve" || name=="approve" || name=="forward") && document.getElementById('approverPositionId').value=="-1")
    {
        alert("Please Select the Approver ");
		return false;
    }
    </s:if>
	
    return true;
}
	
	
</script>
<s:hidden id="workFlowAction" name="workFlowAction"/>
<s:if test="%{getNextAction()!='END'}">
<div class="blueshadow"></div>
<s:hidden id="currentState" name="currentState" value="%{state.value}"/>
<s:hidden id="currentDesignation" name="currentDesignation" value="%{currentDesignation}"/>
<s:hidden id="additionalRule" name="additionalRule" value="%{additionalRule}"/>
<s:hidden id="amountRule" name="amountRule" value="%{amountRule}"/>
<s:hidden id="workFlowDepartment" name="workFlowDepartment" value="%{workFlowDepartment}"/>
<s:hidden id="pendingActions" name="pendingActions" value="%{pendingActions}"/>
<table width="100%" cellpadding ="0" cellspacing ="0" border = "0">
<tbody>
  <tr>&nbsp;
   <td colspan="20" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
   <p><div class="headplacer">Approval Authority Information</div></p></td><td></td> 
  </tr>
   </tbody>
</table>
 
	  	 <table width="100%" border="0" cellspacing="0" cellpadding="0" >
	  	 	 <tr>   
	  	 	 <td class="${approverCSS}" width="16%">&nbsp;</td>
			 <td class="${approverCSS}" id="deptLabel">Approver Deparment</td>
			 <td class="${approverCSS}">
				<s:select name="approverDepartment" id="approverDepartment" list="dropdownData.approverDepartmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----"  value="%{approverDepartment}"  onchange="populateDesignation();"/>
				<s:if test="%{getNextAction()=='Pending Approval By HOD' || paySlipCurrentState=='Rejected'}">
					<egov:ajaxdropdown id="approverDesignation" fields="['Text','Value']" dropdownId="approverDesignation" url="common/employeeSearch!getApproverDesignationListForHOD.action" contextToBeUsed="/payroll"/>
				</s:if>
				<s:else>	
					<egov:ajaxdropdown fields="['Text','Value']" url="workflow/ajaxWorkFlow!getDesignationsByObjectType.action" id="approverDesignation" dropdownId="approverDesignation" contextToBeUsed="/egi"/>
				</s:else>	
			</td>
			<td class="${approverCSS}" width="10%">&nbsp;</td>	  
			<td class="${approverCSS}" width="14%">Approver Designation:</td>
			<td class="${approverCSS}" width="33%">
				<s:select id="approverDesignation" name="approverDesignation" list="dropdownData.desgnationList" listKey="designationId" headerKey="-1" listValue="designationName" headerValue="----Choose----" onchange="populateApprover();" onfocus="callAlertForDepartment();"/>
				<s:if test="%{getNextAction()=='Pending Approval By HOD' || paySlipCurrentState=='Rejected'}">
					<egov:ajaxdropdown id="approverPositionId" fields="['Text','Value']" dropdownId="approverPositionId" url="common/employeeSearch!getApproverListForHOD.action" contextToBeUsed="/payroll"/>
				</s:if>
				<s:else>	
					<egov:ajaxdropdown id="approverPositionId" fields="['Text','Value']" dropdownId="approverPositionId" url="workflow/ajaxWorkFlow!getPositionByPassingDesigId.action" contextToBeUsed="/egi" />
					
				</s:else>	
			</td>
			<td class="${approverCSS}" width="10%">Approver:</td>
			<td class="${approverCSS}" width="16%">
			  	<s:select id="approverPositionId"  name="approverPositionId" list="dropdownData.approverList" headerKey="-1" headerValue="----Choose----" listKey="id" listValue="firstName"  onfocus="callAlertForDesignation();" value="%{approverPositionId}" /></td> 
			<td class="${approverCSS}" width="16%">&nbsp;</td>
			 </tr>
		</table>
</s:if>


 <div id="workflowCommentsDiv" align="center">
         <table width="100%">
         <tr>
           <td width="10%" class="${approverCSS}">&nbsp;</td>
            <td width="20%" class="${approverCSS}">&nbsp;</td>
           <td class="${approverCSS}" width="13%"> Approver Comments: </td>
           <td class="${approverCSS}"> 
           	<textarea id="approverComments" name="approverComments" rows="2" cols="35" ></textarea>  
           </td>
           <td class="${approverCSS}">&nbsp;</td>
           <td width="10%" class="${approverCSS}">&nbsp;</td>
           <td  class="${approverCSS}">&nbsp;</td>
           </tr>
         </table>
  </div>