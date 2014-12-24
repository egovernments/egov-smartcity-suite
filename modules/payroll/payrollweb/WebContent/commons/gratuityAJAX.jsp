<%@ page language="java" import="java.sql.*,java.util.*" %>
<%
String result = (String)request.getAttribute("result");
response.setContentType("text/xml");
response.setHeader("Cache-Control", "no-cache");
response.getWriter().write(result+"/");
%>


