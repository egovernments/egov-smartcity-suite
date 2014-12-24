<%@ page language="java" import="java.util.*,org.egov.lib.rjbac.dept.DepartmentImpl,org.egov.pims.utils.*"%>

	<%
		
		String[] dept = request.getParameterValues("departmentId");
		 if(dept!=null)
		 {
			int deptLength =dept.length;
			for(int i=0;i<deptLength;i++)
			{
				if(!dept[i].equals("0"))
				{
						DepartmentImpl depObj = (DepartmentImpl)EisManagersUtill.getDeptService().getDepartment(new Integer(dept[i]));
						deptMap.put(depObj.getId(),depObj.getDeptName());
						
				}
			}

		}
		
	%>


