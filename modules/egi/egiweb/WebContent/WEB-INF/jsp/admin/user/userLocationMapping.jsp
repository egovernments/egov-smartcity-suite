<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>User Location Mapping</title>
		<script>
		var locationData = <s:property value="location" escape="true"/>;
		//var terminalData = <s:property value="terminal" escape="true"/>;		
		</script>
		<script type="text/javascript" src="/egi/javascript/admin/user/userLocationMapping.js"></script>
		<style>
		.icon-grid {background-image: url(/egi/images/grid.png) !important;}
		</style>		
	</head>
	<body>
		<div id="formContainer"></div>
		<br/>
		<div id="editor-grid"></div>
	</body>
</html>