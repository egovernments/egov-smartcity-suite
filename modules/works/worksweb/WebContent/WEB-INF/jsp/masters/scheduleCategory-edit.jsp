<%@ taglib prefix="s" uri="/struts-tags" %>  
<html>  
<head>  
    <title> Edit  Schedule Category</title>  
</head>  
	<body>  
		<s:actionerror/>  
		<s:fielderror />   
			<s:hidden  name="model.id" />
			<s:submit method="save" value="Save "/>  
	    <ul>  
	        <li><a href="scheduleCategory!newform.action"> Add a new scheduleCategory</a></li>  
	        <li><a href="scheduleCategory"> Schedule Category listing</a></li>  
	    </ul>  
	</body>  
</html>