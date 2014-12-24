<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>Add Schedule Category</title>
</head>
<body id = home>

<s:if test="%{hasErrors()}">
        <div class="errorstyle">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
	<s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:actionmessage theme="simple"/>
        </div>
    </s:if>
<s:form action="scheduleCategory" theme="simple">
<s:token/>
 <%@ include file='scheduleCategory-form.jsp'%>
</s:form>
</body>
</html>

