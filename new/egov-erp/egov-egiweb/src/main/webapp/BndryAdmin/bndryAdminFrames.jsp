<%
	String msg = request.getParameter("message");

%>
<frameset rows= "75%, 25%">
 <frame src="createBndryAndTypes.jsp?message=<%=msg%>" name="rowmainMenu">
 <frame src="" name="rowsideWindow">
</frameset>

