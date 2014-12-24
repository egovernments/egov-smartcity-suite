
<%@ page language="java" %>
<%@ page import="java.util.*,
				 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*,
				 java.text.SimpleDateFormat,
				 org.egov.payroll.services.*,
				 org.egov.payroll.model.*,
				 org.egov.pims.model.*,
				 org.egov.pims.utils.*,
				 org.egov.pims.commons.*,
				 org.egov.pims.utils.*"
		 		
%>
	<%
		
			
		
		StringBuffer id=new StringBuffer();
		StringBuffer name=new StringBuffer();
		String values = "";
		StringBuffer result = new StringBuffer();
		
		String strDept="0";
		String strFunctionary="0";
		
		try
		{
			strDept=request.getParameter("deptId");
			strFunctionary=request.getParameter("functionaryId");
            int i=0;
            List desgination = new ArrayList();
			if( (strDept!=null && !strDept.equals("0")) || (strFunctionary!=null && !strFunctionary.equals("0")))
			{
					 desgination=(List)EisManagersUtill.getEisCommonsManager().getDesigantionBasedOnFuncDept(Integer.valueOf(strDept),Integer.valueOf(strFunctionary));
				
			}
			else
			{
			        desgination=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster");
			}
			System.out.println("check Size----"+desgination.size());
			for(Iterator itr=desgination.iterator();itr.hasNext();)
					{
						   
							DesignationMaster desgObj=(DesignationMaster)itr.next();
							if(i>0)
							{
								id.append("+");
								id.append(desgObj.getDesignationId());
								name.append("+");
								name.append(desgObj.getDesignationName());
							}
							else
							{
								id.append(desgObj.getDesignationId());
								name.append(desgObj.getDesignationName());
							}

						i++;

					}
					
				result.append(id);
				result.append("^");
				result.append(name);
				result.append("^");
				values=result.toString();
		
		
	
		}catch(Exception e){
			e.printStackTrace();
			response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
			throw new Exception("Exception occured in PopulatepayscaleAjax.jsp"+e);

		}
	   
		
		response.setContentType("text/plain");
	    response.setHeader("Cache-Control", "no-cache");
	    response.getWriter().write(values);
	%>