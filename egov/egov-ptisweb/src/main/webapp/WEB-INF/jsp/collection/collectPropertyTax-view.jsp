<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
	<head>
		<title><s:text name="nmc.ptis.pay.collection.title" /></title>
		<script type="text/javascript">
		function loadCollection(){
		var collectXML=document.getElementById("collectXML").value;
		var form = document.createElement("form");
                form.setAttribute("method", "post");
		form.setAttribute("action", "/collection/receipts/receipt!newform.action");
		form.setAttribute("target", "_self");
		var hiddenField = document.createElement("input");
		hiddenField.setAttribute("type","hidden");              
		hiddenField.setAttribute("name", "collectXML");
		hiddenField.setAttribute("id", "collectXML");
		hiddenField.setAttribute("value", collectXML);
		form.appendChild(hiddenField);
		document.body.appendChild(form);
		form.submit();
		}
			  
		</script>
	</head>
	<body onload="loadCollection();">
		<s:form action="/collection/receipts/receipt!create.action" method="post" name="CollectPropertyTaxForm"
				theme="simple">
		<s:token/>
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="headingbg"><s:text name="nmc.ptis.view.collection.title" /></div>
				<s:hidden label="collectXML" id="collectXML" name="collectXML" value="%{collectXML}" />
		</div>
		</s:form>
	</body>
</html>
