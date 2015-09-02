<%@ page isErrorPage="true" %>
<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<title>Error page<title>
</head>
<body>
<table class="tableStyle">
<center>
 <form>
  <table  class="tableStyle" border=1  width="754" summary>
    <tbody >
      
      				   
      				<tr>
      				<td  class="tableStyle" width="728" height="10">

      				<tr>
      					<td class="tableheader" align="middle" width="728" height="30">
      					<p align="center"><b><font="blue">
      					<%=exception.getMessage()%>
      					</font></b></p>
      					</td>


      				</tr>

	</tbody>
  </table>
</center>

</body>

</html>