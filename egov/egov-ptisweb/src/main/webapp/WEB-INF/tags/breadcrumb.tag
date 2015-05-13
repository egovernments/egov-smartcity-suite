<%@ tag body-content="empty"  isELIgnored="false" pageEncoding="UTF-8" description="To show Bread Crumb for each and every Screen" %>
<%@tag import="java.util.Date"%>
<%="<div class='commontopyellowbg'>Property Tax</div>"%>
<%="<div class='commontopbluebg'><div class='commontopdate'>Today is: <span class='bold' style='color:black'>"%>
<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date())%>
<%="</span></div>Welcome <span class='bold' style='color:#cccccc'>"%>
<%=session.getAttribute("username")%><%="</span></div>"%>
<%="<div class='commontopbreadc' id='breadcrumb'>"%>&nbsp;<!--%=breadCrumb%> --><%="</div>"%>

	
 