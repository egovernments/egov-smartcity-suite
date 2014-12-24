<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>



<html>
<head>
	<title>Create Template</title>
</head>

<body class="yui-skin-sam">


	<s:if test="%{hasActionMessages()}">
			<font  style='color: green ; font-weight:bold '> 
     					<s:actionmessage/>
   				</font>
		</s:if>




</body>
</html>