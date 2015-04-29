<%@ tag body-content="empty"  isELIgnored="false" pageEncoding="UTF-8" description="To show Bread Crumb for each and every Screen" %>
<%@tag import="org.egov.infstr.security.utils.SecurityUtils"%>
<%@tag import="java.util.Date"%>
<%@tag import="org.egov.infstr.utils.HibernateUtil"%>
<%@tag import="java.lang.Long"%>
<%@tag import="org.hibernate.Query"%>
<%
String contextPath  = request.getContextPath().replace("/","");
String applicationName = null;
String userName = "Guest";

try { applicationName = application.getContext("/egi").getAttribute(contextPath).toString();} catch (Exception e) {applicationName = "";}

try{
	String citizenId = SecurityUtils.checkSQLInjection(request.getParameter("citizenId"));
	StringBuilder rsltQ = new StringBuilder();
	if (citizenId != null && !citizenId.trim().equalsIgnoreCase("")) {	
		final Query query = HibernateUtil.getCurrentSession().createQuery("SELECT userName FROM PortalUser WHERE id=:id");
		query.setLong("id", Long.parseLong(citizenId));
		userName = query.uniqueResult().toString();
	}
}	
catch(Exception e)
{
	userName="Guest";
}

%>
<%="<div class=\"commontopyellowbg\">Deposit Works Management</div>"%>
<%="<div class=\"commontopbluebg\"><div class=\"commontopdate\">Today is: <span class=\"bold\" style=\"color:black\">"%>
<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date())%>
<%="</span></div>Welcome <span class=\"bold\" style=\"color:#cccccc\">"%><%=userName%><%="</span></div>"%>
<%="</span></div>"%>
<%="<div class=\"commontopbreadc\" id=\"breadcrumb\">"%>&nbsp;<!--%=breadCrumb%> --><%="</div>"%>
<!--  script>
    if (document.getElementById('breadcrumb').innerHTML == '') {
        document.getElementById('breadcrumb').innerHTML = '<%=session.getAttribute(request.getContextPath()) == null ? "" : session.getAttribute(request.getContextPath())%>'+  " > "+document.title;
    }
</script -->
    
 