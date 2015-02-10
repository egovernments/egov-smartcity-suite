<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false"%>
<%@ tag import="org.egov.pims.utils.EisManagersUtill"%>

<%
	boolean isFilterByDept =EisManagersUtill.getEmployeeService().isFilterByDept();
	if(isFilterByDept)
	{
	%>
		<span class="mandatory">*</span>
	<%
	}
			
%>

<script type="text/javascript">
	
	function checkDeptMandatory(deptObj)
	{
		<%
		if(isFilterByDept)
		{
		%>
			if(deptObj=="" || deptObj==0 || deptObj=="0" || deptObj=="-1")
			{
				alert("Please select the Department");
				return false;
			}
		<%
		}
		%>
		return true;
	}
	
</script>
