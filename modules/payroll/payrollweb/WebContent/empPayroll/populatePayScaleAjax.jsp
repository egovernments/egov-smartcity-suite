
<%@ page language="java" %>
<%@ page import="java.util.*,
				 org.egov.infstr.utils.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*,
				 java.text.SimpleDateFormat,
				 org.egov.payroll.services.*,
				 org.egov.payroll.model.*,
				 org.egov.pims.model.*,
				 org.egov.payroll.utils.*,
				 org.egov.pims.utils.*"
		 		
%>
	<%
		StringBuffer id=new StringBuffer();
		StringBuffer name=new StringBuffer();
		String values = "";
		StringBuffer result = new StringBuffer();
		
		String strEffDate=null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String gradeIdSel="0";
		Date effDate=null;
		try
		{
			if(request.getParameter("effDate")!=null && request.getParameter("effDate")!="")
			{
				strEffDate = request.getParameter("effDate");
				effDate = sdf.parse(strEffDate);

				List payscale=(List)PayrollManagersUtill.getPayRollService().getPayScaleByEffectiveDate(effDate);
				
				int i=0;
				for(Iterator itr=payscale.iterator();itr.hasNext();)
				{
					   
						PayScaleHeader payscaleObj=(PayScaleHeader)itr.next();
						if(i>0)
						{
							id.append("+");
							id.append(payscaleObj.getId());
							name.append("+");
							name.append(payscaleObj.getName());
						}
						else
						{
							id.append(payscaleObj.getId());
							name.append(payscaleObj.getName());
						}

					i++;

				}
					
				if(i>0)
				{
					result.append(id);
					result.append("^");
					result.append(name);
					result.append("^");
				}
				values=result.toString();
			}
		}catch(Exception e){
			e.printStackTrace();
			response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
			throw new Exception("Exception occured in PopulatepayscaleAjax.jsp"+e);

		}

		response.setContentType("text/plain");
	    response.setHeader("Cache-Control", "no-cache");
	    response.getWriter().write(values);
		
	%>