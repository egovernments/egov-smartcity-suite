
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,org.egov.pims.service.*,java.text.SimpleDateFormat,org.egov.pims.model.PersonalInformation,
		 org.egov.pims.utils.EisManagersUtill,org.egov.exceptions.*"
		  	
		  				  		
%>


	<%
		String desigId = (String)request.getParameter("desigId");
		String deptId = (String)request.getParameter("deptId");
		String functionaryId = (String)request.getParameter("functionaryId");
		String empModifyId = (String)request.getParameter("empId");

		System.out.println("empModifyId id----->>>>>>>"+empModifyId);
		System.out.println("desig id----->>>>>>>"+desigId);
		System.out.println("deptId id----->>>>>>>"+deptId);
		System.out.println("functionaryId id----->>>>>>>"+functionaryId);
		
		String fromDate = (String)request.getParameter("fromDate");
		System.out.println("fromDate ----->>>>>>>"+fromDate);
		String empId=null;
		Integer employeeId=null;
		if(!"".equals(desigId) && !"".equals(deptId) && !"".equals(functionaryId) && !"".equals(deptId) && !"".equals(fromDate) ){
				if(!"0".equals(desigId) && !"0".equals(deptId) && !"0".equals(functionaryId) && !"0".equals(fromDate) ){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					java.util.Date fromDtDate = null;
					fromDtDate = sdf.parse(fromDate.trim());
					Integer desigIntId = Integer.parseInt(desigId);
					Integer deptIntId = Integer.parseInt(deptId);
					Integer functionaryIntId = Integer.parseInt(functionaryId);
					StringBuffer result = new StringBuffer(100);
					try	{
						PersonalInformation employee = EisManagersUtill.getEisCommonsService().getTempAssignedEmployeeByDeptDesigFunctionaryDate(deptIntId,desigIntId,functionaryIntId,fromDtDate);
						
						if(employee != null){

							if("null".equals(empModifyId))
							{ 
								result.append("true");
								
							}
							else
							{
								
								if(employee.getIdPersonalInformation().intValue()!=Integer.valueOf(empModifyId).intValue())
								{
									result.append("true");
								}
								else
								{
									result.append("false");
								}
							}
						}
					}catch(NoSuchObjectException e){
						System.out.println(e.getMessage());
						e.printStackTrace();
						result.append("false");
					}catch(TooManyValuesException e){
						System.out.println(e.getMessage());
						e.printStackTrace();
						result.append("true");
					}catch(Exception e){
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
					result.append("^");
					response.setContentType("text/xml");
					response.setHeader("Cache-Control", "no-cache");
					response.getWriter().write(result.toString());
				}
		}
	%>


