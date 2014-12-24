<%
String filename = (String)request.getAttribute("filename");
%>
<html>
	<script language="javascript">
		function call()
		{
			window.open("<%=request.getContextPath()%>"+"/temp"+"<%=filename%>","_self","");
		}
	</script>
	<body onload="call()">
		
	</body>
	
</html>