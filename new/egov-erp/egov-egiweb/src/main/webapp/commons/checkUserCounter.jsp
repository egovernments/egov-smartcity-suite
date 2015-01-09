
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


