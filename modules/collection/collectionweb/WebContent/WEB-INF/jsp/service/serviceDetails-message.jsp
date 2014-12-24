<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%>
<html>
<head>
<title> <s:text name="service.master.search.header"></s:text> </title>

</head>

<body>
<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<font  style='color: green ; font-weight:bold '> 
     					<s:actionmessage/>
   				</font>
		</s:if>
</body>
</html>