<%@ tag body-content="empty"  isELIgnored="false" pageEncoding="UTF-8" description="To show Bread Crumb for each and every Screen" %>
<%@tag import="org.egov.infstr.commons.Module"%>
<%@tag import="org.egov.infstr.client.filter.EGOVThreadLocals" %>
<%@tag import="org.egov.lib.rrbac.model.Action"%>
<%@tag import="java.util.Date"%>
<%@tag import="org.egov.lib.rrbac.dao.ActionHibernateDAO"%>
<%@tag import="org.egov.infstr.utils.HibernateUtil"%>
<%@tag import="org.egov.infstr.services.SessionFactory"%>
<%
String contextPath  = request.getContextPath().replace("/","");
String applicationName = null;
try { applicationName = application.getContext("/egi").getAttribute(contextPath).toString();} catch (Exception e) {applicationName = "";}
/*
String requestURI   = request.getRequestURI();
String requestParam = request.getQueryString();
ActionHibernateDAO actionHibernateDAO = new ActionHibernateDAO(org.egov.lib.rrbac.model.Action.class,HibernateUtil.getCurrentSession());
Action action = actionHibernateDAO.findActionByURL(contextPath, requestURI.replaceFirst(contextPath,"")+(requestParam != null && !requestParam.trim().equals("") ? "?"+requestParam : ""));
StringBuffer breadCrumb = new StringBuffer();
String appName = "";
if (action != null) {
	Module module = null;
	module = action.getModule();
	if (module != null) {
		Module parent  = module.getParent();
		while (parent != null) {
			breadCrumb.append(parent.getModuleDescription()).append("<span class='commonbcarrow'> &gt; </span>");
			appName = parent.getModuleDescription();
			parent = parent.getParent();
		}
		breadCrumb.append(module.getModuleDescription()).append("<span class='commonbcarrow'> &gt; </span>");
	}
	breadCrumb.append(action.getDisplayName());
	session.setAttribute(contextPath,appName);
} 
*/
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
	
 