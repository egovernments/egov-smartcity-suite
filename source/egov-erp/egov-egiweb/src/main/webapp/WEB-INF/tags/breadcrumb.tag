<%@ tag body-content="empty"  isELIgnored="false" pageEncoding="UTF-8" description="To show Bread Crumb for each and every Screen" %>
<%@tag import="java.util.Date"%>
<%
String contextPath  = request.getContextPath().replace("/","");
String applicationName = null;
try { applicationName = application.getContext("/egi").getAttribute(contextPath).toString();} catch (Exception e) {applicationName = "";}
%>
<%="<div class=\"commontopyellowbg\">"%><%=applicationName == null ? "" : applicationName%><%="</div>"%>
<%="<div class=\"commontopbluebg\"><div class=\"commontopdate\">Today is: <span class=\"bold\" style=\"color:black\">"%>
<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date())%>
<%="</span></div>Welcome <span class=\"bold\" style=\"color:#cccccc\">"%>
<%=session.getAttribute("com.egov.user.LoginUserName")%><%="</span></div>"%>
<%="<div class=\"commontopbreadc\" id=\"breadcrumb\">"%>&nbsp;<%="</div>"%>
 