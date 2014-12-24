<%
	String msg = request.getParameter("message");

%>
<frameset cols= "25%, 75%">
 <frame src="index.jsp?message=<%=msg%>" name="rowmainMenu">
 <frame src="welcome.jsp" name="rowsideWindow">
</frameset>

