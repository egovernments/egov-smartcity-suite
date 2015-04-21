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
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		  org.egov.lib.security.terminal.dao.UserCounterDAO,
		  org.egov.lib.security.terminal.dao.UserCounterHibernateDAO,
 			org.egov.lib.security.terminal.model.UserCounterMap,
		 		 java.text.SimpleDateFormat,
		 		 org.egov.infstr.utils.HibernateUtil,org.egov.exceptions.EGOVRuntimeException"


%>


	<%

		String userId = request.getParameter("userId");
		String dateFrom = request.getParameter("dateFrom");
		String dateTo = request.getParameter("dateTo");
		String locId = request.getParameter("locId");


		if(!userId.equals("") )
		{
			System.out.println("userId "+userId);
			System.out.println("locId "+locId);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date fromDate = null;
			java.util.Date toDate = null;
			if(dateFrom!=null && !dateFrom.equals(""))
				fromDate = sdf.parse(dateFrom.trim());
			if(dateTo!=null && !dateTo.equals(""))
				toDate = sdf.parse(dateTo.trim());

			StringBuffer result = new StringBuffer(100);
			try
			{
			UserCounterDAO objDao = (UserCounterDAO) new UserCounterHibernateDAO(UserCounterMap.class,HibernateUtil.getCurrentSession());


				if(objDao.checkUserCounter(new Integer(userId),fromDate,toDate)==true)
				{
					result.append("true");
				}
				else
				{
					result.append("false");
				}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
				HibernateUtil.rollbackTransaction();
				throw new EGOVRuntimeException("Exception occured -----> "+e.getMessage());
			}
			System.out.println("sdgfsdgf"+result.toString());
			result.append("^");
			System.out.println("sdgfsdgf"+result.toString());
			response.setContentType("text/xml;charset=utf-8");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(result.toString());
		}
	%>


