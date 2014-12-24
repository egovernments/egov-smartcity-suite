<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
	<head>
		<title></title>
		<script type="text/javascript">
		function loadModifyForm(){
			var modelId = <s:property value="%{workFlowPropId}"/>;
			var form = document.createElement("form");
	        form.setAttribute("method", "post");
			form.setAttribute("action", "../modify/modifyProperty!modifyForm.action?modifyRsn=DATA_ENTRY&indexNumber=<s:property value="%{basicProperty.upicNo}"/>");
			form.setAttribute("target", "_self");
			var hiddenField = document.createElement('input');
			hiddenField.setAttribute("type","hidden");
			hiddenField.setAttribute("id","modelId");
			hiddenField.setAttribute("name","modelId");
			hiddenField.setAttribute("value",modelId);
			form.appendChild(hiddenField);
			var hiddenField1 = document.createElement('input');
			hiddenField1.setAttribute("type","hidden");
			hiddenField1.setAttribute("id","fromDataEntry");
			hiddenField1.setAttribute("name","fromDataEntry");
			hiddenField1.setAttribute("value","true");
			form.appendChild(hiddenField1);
			document.body.appendChild(form);
			form.submit();
		}
			  
		</script>
	</head>
	<body onload="loadModifyForm();">
	</body>
</html>
