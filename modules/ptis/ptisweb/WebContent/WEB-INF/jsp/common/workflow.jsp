<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<div align="center"  ><br>
	<div  class="subheadnew" ><s:text name="approval.details.title"></s:text></div><br>
	   <table width="100%" border="0" cellspacing="0" cellpadding="0" >
	<c:choose>
		<c:when test="${trclass=='greybox'}"><c:set var="trclass" value="bluebox"/></c:when>
		<c:when test="${trclass=='bluebox'}"><c:set var="trclass" value="greybox"/></c:when>
	</c:choose>
	  	 <tr>
			<td class="<c:out value="${trclass}"/>"><s:text name="approver.department"></s:text><span class="mandatory">*</span></td>
			<td class="<c:out value="${trclass}"/>"><s:select name="workflowBean.departmentId" id="departmentId" list="workflowBean.departmentList" listKey="id" listValue="deptName" headerKey="-1" 
			headerValue="----Choose----"  value="%{workflowBean.departmentId}"  onchange= "populateDesignations()"/></td>
			 <egov:ajaxdropdown id="designationId" fields="['Text','Value']" dropdownId="designationId" url="web/commonAjax!ajaxPopulateDesignationsByDept.action" />
			<td class="<c:out value="${trclass}"/>"><s:text name="approver.designation"></s:text><span class="mandatory">*</span></td>
			<td class="<c:out value="${trclass}"/>"><s:select name="workflowBean.designationId" id="designationId" list="workflowBean.designationList"
			 listKey="designationId" listValue="designationName" headerKey="-1" headerValue="----Choose----"  
			 onchange= "populateUser()" /></td>
		</tr>
	<c:choose>
		<c:when test="${trclass=='greybox'}"><c:set var="trclass" value="bluebox"/></c:when>
		<c:when test="${trclass=='bluebox'}"><c:set var="trclass" value="greybox"/></c:when>
	</c:choose>
		 <tr>
		 <egov:ajaxdropdown id="approverUserId"fields="['Text','Value']" dropdownId="approverUserId" url="web/commonAjax!ajaxPopulateUsersByDesignation.action" />
			 <td class="<c:out value="${trclass}"/>" width="13%"><s:text name="approver.position"></s:text><span class="mandatory">*</span></td>
			  <td class="<c:out value="${trclass}"/>" width="33%"><s:select id="approverUserId"  name="workflowBean.approverUserId"  list="workflowBean.appoverUserList" headerKey="-1"
			  headerValue="----Choose----" listKey="id" listValue="userName"   /> 
			 </td>
			 <td class="<c:out value="${trclass}"/>" colspan="2" >
			 <td class="<c:out value="${trclass}"/>" >
			 </tr>
			 <c:choose>
		<c:when test="${trclass=='greybox'}"><c:set var="trclass" value="bluebox"/></c:when>
		<c:when test="${trclass=='bluebox'}"><c:set var="trclass" value="greybox"/></c:when>
	</c:choose>
	<tr>
	<td class="<c:out value="${trclass}"/>" colspan="5" align="centre" >
	<center>
	<s:textarea name="workflowBean.comments" rows="3" cols="80" />
	</center>
	</td>
	</tr>	
    <s:hidden name="workflowBean.actionName" id="workflowBean.actionName"/>
    <s:hidden name="model.id" id="model.id"/>
	</table>
	</div>
	
	
<script>
designationIdFailureHandler=function(){
}

function populateDesignations(){
			populatedesignationId({departmentId:document.getElementById("departmentId").value})
}

function populateUser(){
   	populateapproverUserId({designationId:document.getElementById("designationId").value})
		
}

 
function validateApprover(obj)
{

document.getElementById("workflowBean.actionName").value=obj.value;


if(obj.value.toUpperCase()=="REJECT")
{

return true;
}else if (obj.value.toUpperCase()=="APPROVE")
{
return true;
}
else if(document.getElementById("approverUserId") && document.getElementById("approverUserId").value=="-1")
{
alert("Select Approver");
return false;
}else
{
return true;
}

}

</script>