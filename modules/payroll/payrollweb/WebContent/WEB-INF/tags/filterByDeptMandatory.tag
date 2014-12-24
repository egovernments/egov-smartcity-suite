<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false"%>
<%@ tag import="org.egov.payroll.utils.PayrollManagersUtill"%>
	
<%
	boolean isFilterByDept = PayrollManagersUtill.getEmployeeService().isFilterByDept();
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
			if(deptObj.value=="" || deptObj.value==0 || deptObj.value=="0" || deptObj.value=="-1")
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
