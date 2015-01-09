<%@ tag body-content="empty"  isELIgnored="false" pageEncoding="UTF-8" description="To show Bread Crumb for each and every Screen" %>
<%@tag import="org.egov.infstr.commons.Module"%>
<%@tag import="org.egov.infstr.client.filter.EGOVThreadLocals" %>
<%@tag import="org.egov.lib.rrbac.model.Action"%>
<%@tag import="java.util.Date"%>
<%@tag import="org.egov.lib.rrbac.dao.ActionHibernateDAO"%>
<%@tag import="org.egov.infstr.utils.HibernateUtil"%>
<%@tag import="org.egov.infstr.services.SessionFactory"%>
<%

String applicationName = "Dashboard";

%>
<%="<div class=\"commontopyellowbg\">"%><%=applicationName == null ? "" : applicationName%><%="</div>"%>
<%="<div class=\"commontopbluebg\"><div class=\"commontopdate\">Today is: <span class=\"bold\" style=\"color:black\">"%>
<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date())%>
<%="</span></div>Welcome <span class=\"bold\" style=\"color:#cccccc\">"%>
<%=session.getAttribute("com.egov.user.LoginUserName")%><%="</span></div>"%>
<%="<div class=\"commontopbreadc\" id=\"breadcrumb\">"%>&nbsp;<!--%=breadCrumb%> --><%="</div>"%>
<!--  script>
	if (document.getElementById('breadcrumb').innerHTML == '') {
		document.getElementById('breadcrumb').innerHTML = '<%=session.getAttribute(request.getContextPath()) == null ? "" : session.getAttribute(request.getContextPath())%>'+  " > "+document.title;
	}
</script -->
	
 