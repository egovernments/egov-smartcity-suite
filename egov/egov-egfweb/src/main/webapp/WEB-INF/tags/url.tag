<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false"%>
<%@ attribute name="path" required="true" %>
<%@ attribute name="prefix" required="false"  %>
<%
String url="";
if(path.startsWith("/")) path=path.substring(1);
if(prefix!=null && !"".equals(prefix.trim())){
	url=prefix+"/"+path;
}else{
	url=request.getContextPath()+"/"+path;
}
%>
<%=url%>
