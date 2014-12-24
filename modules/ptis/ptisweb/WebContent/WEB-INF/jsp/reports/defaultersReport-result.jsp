<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='defaultersListReport.title'/></title>
  </head>
  
  <body>
  <s:form name="DefaultersListForm" theme="simple">  
	<iframe src="../reportViewer?reportId=<s:property value='reportId'/>" width="98%" height="70%">
	<p>Your browser does not support iframes.</p>
	</iframe>
	<br/>
	<div class="buttonbottom">
			<input name="buttonClose" type="button" class="button"
				id="buttonClose" value="Close" onclick="window.close();" />&nbsp;
		</div>
  </s:form>
  </body>
</html>