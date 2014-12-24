<%@ include file="/includes/taglibs.jsp" %>




<html>
    <head>
        <%@ include file="/includes/meta.jsp" %>
        <title>eGov  - <decorator:title/> </title>
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/egov.css'/>" />
      	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/reset/reset.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/commonyui/build/fonts/fonts.css'/>" />

        
           
	           
		   
		 
	
        <decorator:head/>
        
      


    </head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
  <table align='center' id="table2">
	  <tr>
	  <td>
		<DIV id=main>	<DIV id=m2><DIV id=m3>
		<decorator:body/>
		
		</DIV></DIV></DIV>
	</td>
	</tr>
  </table>
</body>
	 
</html>
 




