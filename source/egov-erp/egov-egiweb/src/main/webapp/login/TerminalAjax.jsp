<%@ page language="java"
	import="org.egov.infstr.security.utils.SecurityUtils,
	org.egov.infstr.utils.EGovConfig,
	org.egov.infstr.utils.HibernateUtil,
	org.hibernate.Query,java.util.List,java.math.BigDecimal"%>

<%
	try {
		String username = SecurityUtils.checkSQLInjection(request.getParameter("username"));
		StringBuilder rsltQ = new StringBuilder("{'isLocation':");
		if (username != null && !username.trim().equals("")) {
			Query query = HibernateUtil.getCurrentSession().createSQLQuery("SELECT r.name FROM eg_role r, eg_user u, eg_userrole ur WHERE u.id = ur.user and r.id = ur.role and u.isactive=true and u.username = :userName");
			query.setString("userName", username);
			List<Object> userRoles = query.list();
			boolean locationbased = false;
			if (userRoles.isEmpty()) {
				rsltQ.append(locationbased);
			} else {
				String configuredRoles = EGovConfig.getProperty("INCLUDE_ROLES", "", "IP-BASED-LOGIN");
				String[] include_roles = configuredRoles.split(",");
				for (int u = 0; u < include_roles.length; u++) {
					for (Object role : userRoles) {
						if (role.toString().equals(include_roles[u]) && !"".equals(include_roles[u]) && !"".equals(role.toString())) {
							locationbased = true;
							break;
						}
					}
				}

				rsltQ.append(locationbased);
				query = HibernateUtil.getCurrentSession().createSQLQuery("SELECT ucm.counterid, l.LOCATIONID,l.name  FROM eg_user u, eg_usercounter_map ucm, eg_location l WHERE ucm.userid = u.id and ucm.counterid = l.id  and u.username = :userName");
				query.setString("userName", username);
				List<Object[]> result = query.list();
				if (result != null && !result.isEmpty()) {
					Object [] values = result.get(0);
					if (values[1] != null) { 
						rsltQ.append(",'locationId' :").append(values[1]);
						query = HibernateUtil.getCurrentSession().createSQLQuery("SELECT l.name  FROM eg_location l WHERE l.id=:locId and l.LOCATIONID is null");
						query.setBigDecimal("locId",(BigDecimal)values[1] );
						Object locationName = query.uniqueResult();
						rsltQ.append(",'locationName' :'").append(locationName.toString());
						rsltQ.append("','counterId' :").append(values[0]);
						query = HibernateUtil.getCurrentSession().createSQLQuery("SELECT l.name,l.id  FROM eg_location l WHERE l.id =:locId");
						query.setBigDecimal("locId", (BigDecimal)values[0]);
					} else {
						rsltQ.append(",'locationId' :").append(values[0]);
						query = HibernateUtil.getCurrentSession().createSQLQuery("SELECT l.name  FROM eg_location l WHERE l.id=:locId and l.LOCATIONID is null");
						query.setBigDecimal("locId",(BigDecimal)values[0]);
						Object locationName = query.uniqueResult();
						rsltQ.append(",'locationName' :'").append(locationName.toString());
						rsltQ.append("','counterId' :").append(0);
						query = HibernateUtil.getCurrentSession().createSQLQuery("SELECT l.name,l.id  FROM eg_location l WHERE l.LOCATIONID =:locId");
						query.setBigDecimal("locId", (BigDecimal)values[0]);
					}				
					
					List<Object[]> counters = query.list();
					rsltQ.append(",'counters' : [");
					for(Object[] counter : counters) {
						rsltQ.append("{");
						rsltQ.append("'name' :'").append(counter[0]);
						rsltQ.append("','id' :").append(counter[1]);
						rsltQ.append("},");
					}
					rsltQ.deleteCharAt(rsltQ.length()-1);
					rsltQ.append("]");
				}				
			}
		}
		
		rsltQ.append("}");
		System.out.print(rsltQ);
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(rsltQ.toString());
	} catch (Exception e) {
		throw new RuntimeException("Error occurred while getting User Terminal information", e);
	}
%>